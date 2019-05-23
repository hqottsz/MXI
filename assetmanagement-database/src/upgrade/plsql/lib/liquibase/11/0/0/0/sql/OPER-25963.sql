--liquibase formatted sql
--changeSet OPER-25963:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$


CREATE OR REPLACE PACKAGE BODY FlightInterface IS

/******************************************************************************
*
* Procedure:    aircraftSwap
* Arguments:    aFlightLegId (raw16):*
*               aInvNoDbId (number):
*               aInvNoId (number):
*               aAssmblDbId (number):
*               aAssmblCd (varchar2):
*               aReturn (number): Return 1 means success, <0 means failure
* Description:  This procedure will perform aircraft swapping. Inside this procedure,
*               'USAGE_PKG.UsageCalculations' and 'EVENT_PKG.UpdateDeadlineForInvTree' procedures will be invoked
*
* Author:   Hong Zheng
* Created Date:  19.Aug.2008
*
*******************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE aircraftSwap( aFlightLegId IN fl_leg.leg_id%TYPE,
                         aInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                         aInvNoId IN inv_inv.inv_no_id%TYPE,
                         aAssmblDbId IN eqp_assmbl.assmbl_db_id%TYPE,
                         aAssmblCd IN eqp_assmbl.assmbl_cd%TYPE,
                         aReturn OUT NUMBER
                      ) IS

-- Cursor used to lock all rows in inv_ac_flight_plan
-- that will be deleted an regenerated
CURSOR lLockInvAcFlightPlanCur (
      aInvNoDbId1 inv_inv.inv_no_db_id%TYPE,
      aInvNoId1 inv_inv.inv_no_id%TYPE,
      aInvNoDbId2 inv_inv.inv_no_db_id%TYPE,
      aInvNoId2 inv_inv.inv_no_id%TYPE ) IS
   SELECT
     inv_ac_flight_plan.inv_no_db_id,
     inv_ac_flight_plan.inv_no_id
   FROM
     inv_ac_flight_plan
   WHERE
     ( inv_ac_flight_plan.inv_no_db_id, inv_ac_flight_plan.inv_no_id ) IN (
        ( aInvNoDbId1, aInvNoId1 ),
        ( aInvNoDbId2, aInvNoId2 )
     )
   FOR UPDATE;

CURSOR lcur_GetUsgUsageData(aFlightLegId IN fl_leg.leg_id%TYPE ) IS
      SELECT i.data_type_db_id,
             i.data_type_id,
             i.tsn_delta_qt,
             i.tso_delta_qt,
             i.tsi_delta_qt,
             e.inv_no_db_id,
             e.inv_no_id
        FROM fl_leg f,
             usg_usage_record e,
             usg_usage_data i
       WHERE f.leg_id = aFlightLegId AND
             e.usage_record_id  = f.usage_record_id AND
             i.usage_record_id =  e.usage_record_id
;
CURSOR lcur_GetUsgInv(aFlightLegId IN fl_leg.leg_id%TYPE ) IS
      SELECT usg_usage_data.inv_no_db_id, usg_usage_data.inv_no_id
       FROM usg_usage_data
       WHERE usg_usage_data.usage_record_id = ( SELECT fl_leg.usage_record_id
                                FROM fl_leg
                                WHERE leg_id = aFlightLegId );

--local variables
lOriginalInvNoDbId inv_inv.inv_no_db_id%TYPE ;
lOriginalInvNoId  inv_inv.inv_no_id%TYPE;
lHist_bool fl_leg.hist_bool%TYPE;
lUsageRecordId usg_usage_record.usage_record_id%TYPE;
lReturn NUMBER;

--exceptions
xc_UsageCalculations        EXCEPTION;
xc_UpdateDeadlineForInvTree EXCEPTION;

BEGIN
     -- Initialize the return
     aReturn := iNoProc;

      -- Store the original aircraft inventory keys
      SELECT fl_leg.aircraft_db_id,
            fl_leg.aircraft_id
       INTO   lOriginalInvNoDbId,
                lOriginalInvNoId
       FROM  fl_leg
       WHERE fl_leg.leg_id = aFlightLegId;

      -- Store the hist_bool for the flight
       SELECT fl_leg.hist_bool
       INTO lHist_bool
       FROM fl_leg
       WHERE fl_leg.leg_id = aFlightLegId;

      -- Store the usage_record_id for the flight
       SELECT fl_leg.usage_record_id
       INTO lUsageRecordId
       FROM fl_leg
       WHERE fl_leg.leg_id = aFlightLegId;

     -- Check if the event is complete
     IF lHist_bool = 1 THEN

         FOR lrec_UsgUsageData IN lcur_GetUsgUsageData(aFlightLegId) LOOP

             UPDATE inv_curr_usage
             SET tsn_qt = tsn_qt - lrec_UsgUsageData.Tsn_Delta_Qt,
                 tso_qt = tso_qt - lrec_UsgUsageData.Tso_Delta_Qt,
                 tsi_qt = tsi_qt - lrec_UsgUsageData.Tsi_Delta_Qt
             WHERE data_type_db_id = lrec_UsgUsageData.Data_Type_Db_Id AND
                   data_type_id    = lrec_UsgUsageData.Data_Type_Id
             AND ( inv_no_db_id, inv_no_id )
                 IN
                 ( SELECT inv_no_db_id, inv_no_id
                     FROM inv_inv
                    WHERE ( assmbl_inv_no_db_id = lrec_UsgUsageData.Inv_No_Db_Id and
                            assmbl_inv_no_id    = lrec_UsgUsageData.Inv_No_Id   and
                            orig_assmbl_db_id is null
                          )
                      OR ( inv_no_db_id = lrec_UsgUsageData.Inv_No_Db_Id AND
                           inv_no_id    = lrec_UsgUsageData.Inv_No_Id )
                 );

       END LOOP;
       -- update deadlines for aircraft
       FOR lrec_UsgInv IN lcur_GetUsgInv(aFlightLegId) LOOP

           --Execute usage calculations on the inventory
           USAGE_PKG.UsageCalculations(lrec_UsgInv.Inv_No_Db_Id, lrec_UsgInv.Inv_No_Id, lReturn);
           IF lReturn < 0 THEN
              RAISE xc_UsageCalculations;
           END IF;
       END LOOP;
       --update deadlines for aircraft
       EVENT_PKG.UpdateDeadlineForInvTree(lOriginalInvNoDbId, lOriginalInvNoId, lReturn);
              IF lReturn < 0 THEN
                 RAISE xc_UpdateDeadlineForInvTree;
              END IF;

     END IF;

      --Remove rows from relevant tables for current flight
      --remove usage data
      DELETE FROM usg_usage_data
      WHERE usage_record_id = lUsageRecordId;

      --remove measurements data
      DELETE FROM fl_leg_measurement
      WHERE fl_leg_measurement.leg_id = aFlightLegId;

      -- update usage record row with new aircraft
      UPDATE usg_usage_record
      SET inv_no_db_id = aInvNoDbId,
          inv_no_id    = aInvNoId
      WHERE usg_usage_record.usage_record_id = lUsageRecordId;

     -- Assign the new aircraft to flight
      UPDATE fl_leg
        SET aircraft_db_id = aInvNoDbId,
            aircraft_id    = aInvNoId
      WHERE fl_leg.leg_id = aFlightLegId;

      -- Assign the new aircraft type if it is provided
      IF aAssmblDbId > 0 AND aAssmblCd IS NOT NULL THEN
         UPDATE fl_leg
            SET plan_assmbl_db_id = aAssmblDbId,
                plan_assmbl_cd = aAssmblCd
         WHERE fl_leg.leg_id = aFlightLegId;
      END IF;

      -- Lock all of the rows for both aircrafts at the same time to avoid deadlocks
      OPEN lLockInvAcFlightPlanCur( lOriginalInvNoDbId, lOriginalInvNoId, aInvNoDbId, aInvNoId );
      CLOSE lLockInvAcFlightPlanCur;

      --return success
      aReturn := iSuccess;

      EXCEPTION
          WHEN xc_UsageCalculations THEN
               aReturn := iUsageCalculationsError;
          WHEN xc_UpdateDeadlineForInvTree THEN
               aReturn := iUpdateDeadlineForInvTreeError;
          WHEN OTHERS THEN
               aReturn := iOracleError;

END aircraftSwap;

END FlightInterface;
/