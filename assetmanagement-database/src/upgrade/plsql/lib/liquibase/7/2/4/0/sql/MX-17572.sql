--liquibase formatted sql


--changeSet MX-17572:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY FlightInterface IS

/*-------------------- Private procedures --------------------------------------*/
PROCEDURE generateFlightPlanForAircraft(aInvNoDbId IN evt_inv.inv_no_db_id%TYPE,
                                          aInvNoId IN evt_inv.inv_no_id%TYPE) IS

CURSOR lcur_GetActiveFlights(aInvNoDbId inv_inv.inv_no_db_id%TYPE, aInvNoId inv_inv.inv_no_id%TYPE) IS
     SELECT
         jl_flight.flight_db_id,
         jl_flight.flight_id,
         evt_event.event_status_cd,
         arr_evt_loc.loc_db_id arr_loc_db_id,
         arr_evt_loc.loc_id arr_loc_id,
         dep_evt_loc.loc_db_id dep_loc_db_id,
         dep_evt_loc.loc_id dep_loc_id
      FROM
         evt_inv,
         evt_event,
         jl_flight,
         evt_loc arr_evt_loc,
         evt_loc dep_evt_loc
      WHERE
         evt_inv.inv_no_db_id = aInvNoDbId AND
         evt_inv.inv_no_id = aInvNoId
         AND
         evt_event.event_db_id = evt_inv.event_db_id AND
         evt_event.event_id = evt_inv.event_id AND
         hist_bool = 0
         AND
         jl_flight.flight_db_id = evt_event.event_db_id AND
         jl_flight.flight_id = evt_event.event_id
         AND
         dep_evt_loc.event_db_id = evt_event.event_db_id AND
         dep_evt_loc.event_id = evt_event.event_id AND
         dep_evt_loc.event_loc_id = 1
         AND
         arr_evt_loc.event_db_id = evt_event.event_db_id AND
         arr_evt_loc.event_id = evt_event.event_id AND
         arr_evt_loc.event_loc_id = 2
      ORDER BY
         evt_event.sched_end_gdt ASC;

CURSOR lcur_GetLastCompletedFlight(aInvNoDbId evt_inv.inv_no_db_id%TYPE, aInvNoId evt_inv.inv_no_id%TYPE) IS
     SELECT * FROM
         (SELECT
            jl_flight.flight_db_id,
            jl_flight.flight_id
         FROM
            evt_inv,
            evt_event,
            jl_flight
         WHERE
            evt_inv.inv_no_db_id = aInvNoDbId AND
            evt_inv.inv_no_id    = aInvNoId
            AND
            evt_event.event_db_id = evt_inv.event_db_id AND
            evt_event.event_id    = evt_inv.event_id AND
            hist_bool = 1
            AND
            evt_event.event_status_db_id = 0 AND
            evt_event.event_status_cd    = 'FLCMPLT'
            AND
            jl_flight.flight_db_id = evt_event.event_db_id AND
            jl_flight.flight_id    = evt_event.event_id
         ORDER BY
            evt_event.event_gdt DESC) hist_flight
      WHERE
         ROWNUM = 1;

--type definitions
TYPE flight IS RECORD(aFlightDbId      jl_flight.flight_db_id%TYPE, 
                        aFlightId         jl_flight.flight_id%TYPE, 
                     aEventStatus   evt_event.event_status_cd%TYPE,
                      aArrLocDbId           evt_loc.loc_db_id%TYPE,
                      aArrLocId                evt_loc.loc_id%TYPE,
                      aDepLocDbId           evt_loc.loc_db_id%TYPE,
                      aDepLocId                evt_loc.loc_id%TYPE
                      );
TYPE activeFlightsArray IS TABLE OF flight  INDEX BY BINARY_INTEGER;
type typ_ac_flight_plan is table of inv_ac_flight_plan%rowtype index by binary_integer;


--Local variables
lFlight flight;
lActiveFlightsArray   activeFlightsArray;
lrec_GetLastCompletedFlight lcur_GetLastCompletedFlight%ROWTYPE;
lCurrentLocDbId inv_inv.loc_db_id%TYPE;
lCurrentLocId inv_inv.loc_id%TYPE;
lFlightPlanOrd NUMBER;
l_tab_ac_flight_plan typ_ac_flight_plan;
l_ac_flight_plan inv_ac_flight_plan%rowtype;

BEGIN

       -- Initial order
     lFlightPlanOrd := 1;
     
     -- Clear collection
     l_tab_ac_flight_plan.delete;
     
     --Delete all of the rows from the flight plan for this aircraft
     DELETE FROM inv_ac_flight_plan 
           WHERE inv_ac_flight_plan.inv_no_db_id = aInvNoDbId AND 
                    inv_ac_flight_plan.inv_no_id = aInvNoId;

                 

     --Get all the active flights for the aircraft
     OPEN lcur_GetActiveFlights(aInvNoDbId,aInvNoId);
     FETCH lcur_GetActiveFlights BULK COLLECT INTO lActiveFlightsArray;


     FOR i IN 1 .. lactiveFlightsArray.count
     LOOP

         lFlight := lActiveFlightsArray(i);
         IF lcur_GetActiveFlights%ROWCOUNT = 1 THEN
            IF  lFlight.aEventStatus = 'FLPLAN' OR 
                lFlight.aEventStatus = 'FLOUT'  OR
                lFlight.aEventStatus = 'RETURN' OR 
                lFlight.aEventStatus = 'FLDELAY' THEN

               --Get most recent historic flight
               OPEN lcur_GetLastCompletedFlight(aInvNoDbId,aInvNoId);
               FETCH lcur_GetLastCompletedFlight INTO lrec_GetLastCompletedFlight;
               CLOSE lcur_GetLastCompletedFlight;

               --Create new flight plan record
               l_ac_flight_plan.inv_no_db_id := aInvNoDbId;
               l_ac_flight_plan.inv_no_id := aInvNoId;
               l_ac_flight_plan.loc_db_id := lFlight.aDepLocDbId;
               l_ac_flight_plan.loc_id := lFlight.aDepLocId;
               l_ac_flight_plan.arr_flight_db_id := lrec_GetLastCompletedFlight.Flight_Db_Id;
               l_ac_flight_plan.arr_flight_id := lrec_GetLastCompletedFlight.Flight_Id;
               l_ac_flight_plan.flight_plan_ord := lflightplanord;

	       -- Add new record to the existing collection
               l_tab_ac_flight_plan(lflightplanord) := l_ac_flight_plan;

	       -- Increment counter 
               lFlightPlanOrd := lFlightPlanOrd + 1;
            end if;
         end if;

	 -- Capture the data for the new record
         l_ac_flight_plan.inv_no_db_id := aInvNoDbId;
         l_ac_flight_plan.inv_no_id := aInvNoId;
         l_ac_flight_plan.loc_db_id := lFlight.aArrLocDbId;
         l_ac_flight_plan.loc_id := lFlight.aArrLocId;
         l_ac_flight_plan.arr_flight_db_id := lFlight.aFlightDbId;
         l_ac_flight_plan.arr_flight_id := lFlight.aFlightId;
         l_ac_flight_plan.flight_plan_ord := lflightplanord;

	 -- Add new record to the existing collection
	 l_tab_ac_flight_plan(lflightplanord) := l_ac_flight_plan;

         IF lFlightPlanOrd <> 1 THEN 

		 -- get the values from the previous collection record
		 l_ac_flight_plan := l_tab_ac_flight_plan(lflightplanord-1);

		 -- Capture previous flight key 
		 l_ac_flight_plan.dep_flight_db_id := lFlight.aFlightDbId;
		 l_ac_flight_plan.dep_flight_id := lFlight.aFlightId;

		 -- Set new record to the previous collection record
		 l_tab_ac_flight_plan(lflightplanord-1) := l_ac_flight_plan;

            END IF;
	 
	 -- Increment counter
         lFlightPlanOrd := lFlightPlanOrd + 1;


     END LOOP;

     -- If there are NO active flight
     IF lcur_GetActiveFlights%ROWCOUNT = 0 THEN

        --Get the current location of the aircraft
        SELECT inv_inv.loc_db_id,
                  inv_inv.loc_id 
          INTO   lCurrentLocDbId, 
                   lCurrentLocId 
         FROM inv_inv 
         WHERE inv_inv.inv_no_db_id = aInvNoDbId AND
                  inv_inv.inv_no_id = aInvNoId;

        -- Get most recent historic flight
        OPEN lcur_GetLastCompletedFlight(aInvNoDbId,aInvNoId);
        FETCH lcur_GetLastCompletedFlight INTO lrec_GetLastCompletedFlight;
        CLOSE lcur_GetLastCompletedFlight;

        -- Create new flight plan record
        l_ac_flight_plan.inv_no_db_id := aInvNoDbId;
        l_ac_flight_plan.inv_no_id := aInvNoId;
        l_ac_flight_plan.loc_db_id := lCurrentLocDbId;
        l_ac_flight_plan.loc_id := lCurrentLocId;
        l_ac_flight_plan.arr_flight_db_id := lrec_GetLastCompletedFlight.Flight_Db_Id;
        l_ac_flight_plan.arr_flight_id := lrec_GetLastCompletedFlight.Flight_Id;
        l_ac_flight_plan.flight_plan_ord := lflightplanord;

        l_tab_ac_flight_plan(lflightplanord) := l_ac_flight_plan;


     END IF;
     CLOSE lcur_GetActiveFlights;

     forall x in indices of l_tab_ac_flight_plan
        insert into inv_ac_flight_plan values l_tab_ac_flight_plan(x);


END generateFlightPlanForAircraft;

/******************************************************************************
*
* Procedure:    aircraftSwap
* Arguments:    aEventDbId (number):
*               aEventId (number):
*               aInvNoDbId (number):
*               aInvNoId (number):
*               aAssmblDbId (number):
*               aAssmblCd (varchar2):
*               aReturn (number): Return 1 means success, <0 means failure
* Description:  This procedure will perform aircraft swapping. Inside this procedure, 
*               procedure 'generateFlightPlanForAircraft' will be invoked, and also 
*               'USAGE_PKG.UsageCalculations' and 'EVENT_PKG.UpdateDeadlineForInvTree'.
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
PROCEDURE aircraftSwap(aEventDbId IN evt_event.event_db_id%TYPE, 
                            aEventId IN evt_event.event_id%TYPE, 
                        aInvNoDbId IN inv_inv.inv_no_db_id%TYPE, 
                             aInvNoId IN inv_inv.inv_no_id%TYPE,
                    aAssmblDbId IN eqp_assmbl.assmbl_db_id%TYPE,
                         aAssmblCd IN eqp_assmbl.assmbl_cd%TYPE,
                         aReturn OUT NUMBER
                      ) IS

CURSOR lcur_GetInvNumData(aEventDbId evt_event.event_db_id%TYPE,
                               aEventId evt_event.event_id%TYPE
                          ) IS
      SELECT i.data_type_db_id,
             i.data_type_id,
             i.tsn_delta_qt,
             i.tso_delta_qt,
             i.tsi_delta_qt,
             e.inv_no_db_id,
             e.inv_no_id
        FROM evt_inv e,
             inv_num_data i
       WHERE i.event_db_id = aEventDbId     AND
             i.event_id    = aEventId       AND
             e.event_db_id  = i.event_db_id AND
             e.event_id     = i.event_id    AND
             e.event_inv_id = i.event_inv_id;

CURSOR lcur_GetEvtInv(aEventDbId evt_event.event_db_id%TYPE,
                           aEventId evt_event.event_id%TYPE
                     ) IS
      SELECT *
        FROM evt_Inv
       WHERE Evt_Inv.event_db_id = aEventDbId AND
             evt_Inv.event_id = aEventId;

CURSOR lcur_GetSubAssemblies(aInvNoDbId inv_inv.inv_no_db_id%TYPE,
                                  aInvNoId inv_inv.inv_no_id%TYPE
                            ) IS
     SELECT   INV_INV.INV_NO_DB_ID,
               INV_INV.INV_NO_ID
        FROM   INV_INV
       WHERE   ORIG_ASSMBL_DB_ID IS NOT NULL
               AND
         NOT   ( INV_INV.INV_NO_DB_Id = aInvNoDbId     AND
                 INV_INV.INV_NO_ID    = aInvNoId )
      START WITH
               ( INV_INV.INV_NO_DB_ID = aInvNoDbId   )  AND
               ( INV_INV.INV_NO_ID    = aInvNoId     )
      CONNECT BY
               ( INV_INV.NH_INV_NO_DB_ID = PRIOR INV_INV.INV_NO_DB_ID   )  AND
               ( INV_INV.NH_INV_NO_ID    = PRIOR INV_INV.INV_NO_ID      );

--type definitions
TYPE inventory IS RECORD(aInvNoDbId inv_inv.inv_no_db_id%TYPE, 
                              aInvNoId inv_inv.inv_no_id%TYPE
                        );
TYPE inventoryArray IS TABLE OF inventory INDEX BY BINARY_INTEGER;

--local variables
lInventoryArray inventoryArray;
lInventory inventory;
lOriginalInvNoDbId evt_inv.inv_no_db_id%TYPE ;
lOriginalInvNoId  evt_inv.inv_no_id%TYPE;
lHist_bool evt_event.hist_bool%TYPE;
lMaxId NUMBER;
lNum NUMBER;
lReturn NUMBER;

--exceptions
xc_CreateEvtInvEntry        EXCEPTION;
xc_UsageCalculations        EXCEPTION;
xc_UpdateDeadlineForInvTree EXCEPTION;

BEGIN



     -- Initialize the return
     aReturn := iNoProc;

     -- Store the original aircraft inventory keys
     SELECT evt_inv.inv_no_db_id,
               evt_inv.inv_no_id 
       INTO   lOriginalInvNoDbId, 
                lOriginalInvNoId
       FROM  evt_inv 
       WHERE evt_inv.event_db_id = aEventDbId AND
                evt_inv.event_id = aEventId AND
           evt_inv.main_inv_bool = 1;

     -- Store the hist_bool for the event
     SELECT evt_event.hist_bool 
       INTO lHist_bool 
       FROM evt_event 
       WHERE evt_event.event_db_id = aEventDbId AND
                evt_event.event_id = aEventId;

     -- Check if the event is complete
     IF lHist_bool = 1 THEN
     
         FOR lrec_InvNumData IN lcur_GetInvNumData(aEventDbId, aEventId) LOOP

             UPDATE inv_curr_usage
             SET tsn_qt = tsn_qt - lrec_InvNumData.Tsn_Delta_Qt,
                 tso_qt = tso_qt - lrec_InvNumData.Tso_Delta_Qt,
                 tsi_qt = tsi_qt - lrec_InvNumData.Tsi_Delta_Qt
             WHERE data_type_db_id = lrec_InvNumData.Data_Type_Db_Id AND
                   data_type_id    = lrec_InvNumData.Data_Type_Id
             AND ( inv_no_db_id, inv_no_id )
                 IN
                 ( SELECT inv_no_db_id, inv_no_id
                     FROM inv_inv
                    WHERE ( assmbl_inv_no_db_id = lrec_InvNumData.Inv_No_Db_Id and
                            assmbl_inv_no_id    = lrec_InvNumData.Inv_No_Id   and
                            orig_assmbl_db_id is null
                          )
                      OR ( inv_no_db_id = lrec_InvNumData.Inv_No_Db_Id AND
                           inv_no_id    = lrec_InvNumData.Inv_No_Id )
                 );

       END LOOP;

       FOR lrec_EvtInv IN lcur_GetEvtInv(aEventDbId, aEventId) LOOP

           --Execute usage calculations on the inventory
           USAGE_PKG.UsageCalculations(lrec_EvtInv.Inv_No_Db_Id, lrec_EvtInv.Inv_No_Id, lReturn);
           IF lReturn < 0 THEN
              RAISE xc_UsageCalculations;
           END IF;

           IF lrec_EvtInv.Main_Inv_Bool = 1 THEN

              EVENT_PKG.UpdateDeadlineForInvTree(lrec_EvtInv.Inv_No_Db_Id, lrec_EvtInv.Inv_No_Id, lReturn);
              IF lReturn < 0 THEN
                 RAISE xc_UpdateDeadlineForInvTree;
              END IF;
           END IF;

       END LOOP;

     END IF;
     
      --Remove rows from relevant five tables for current event
      DELETE FROM inv_num_data
      WHERE event_db_id = aEventDbId AND 
               event_id = aEventId;

      DELETE FROM inv_chr_data
      WHERE event_db_id = aEventDbId AND 
               event_id = aEventId;

      DELETE FROM inv_parm_data
      WHERE event_db_id = aEventDbId AND 
               event_id = aEventId;

      DELETE FROM evt_inv_usage
      WHERE event_db_id = aEventDbId
      AND event_id = aEventId;

      DELETE FROM evt_inv
      WHERE event_db_id = aEventDbId AND 
               event_id = aEventId;


      -- Retrieve the aircraft and subassemblies
      OPEN lcur_GetSubAssemblies(aInvNoDbId,aInvNoId);
      FETCH lcur_GetSubAssemblies BULK COLLECT INTO lInventoryArray;
      CLOSE lcur_GetSubAssemblies;

      -- Default first row to one
         lMaxId := 1;

      -- Insert into EVT_INV
      INSERT INTO evt_inv( event_db_id,
                              event_id,
                          event_inv_id,
                          inv_no_db_id,
                             inv_no_id,
                         main_inv_bool
                         )
                   values(   aEventDbId,
                               aEventId,
                                 lMaxId,
                             aInvNoDbId,
                               aInvNoId,
                                      1
                          );

      -- Retrieve the row just created.
      SELECT COUNT(*) 
        INTO lNum 
        FROM evt_inv 
       WHERE     evt_inv.event_db_id = aEventDbId AND
                    evt_inv.event_id = aEventId   AND
                evt_inv.inv_no_db_id = aInvNoDbId AND
                   evt_inv.inv_no_id = aInvNoId   AND
                evt_inv.event_inv_id = lMaxId;

      -- Check if the row was 
      IF lNum <> 1 THEN
         --Exception
         RAISE xc_CreateEvtInvEntry;         
      END IF;

    -- 
    IF lInventoryArray.COUNT <> 0 THEN
      FOR i IN 1 .. lInventoryArray.LAST LOOP

         lInventory := lInventoryArray(i);

         -- Get in max(event_inv_id) from the EVT_INV table
         SELECT MAX(evt_inv.event_inv_id) 
           INTO lMaxId 
           FROM evt_inv 
           WHERE evt_inv.event_db_id = aEventDbId AND
                    evt_inv.event_id = aEventId;

         -- check if a record was found
         IF SQL%NOTFOUND THEN
            --Default
            lMaxId := 1;
         ELSE
            lMaxId := lMaxId + 1;
         END IF;

         --  a new row in the EVT_INV table
         INSERT INTO evt_inv(  event_db_id,
                                  event_id,
                              event_inv_id,
                              inv_no_db_id,
                                 inv_no_id,
                             main_inv_bool
                            )
                      VALUES(   aEventDbId,
                                  aEventId,
                                    lMaxId,
                     lInventory.aInvNoDbId,
                       lInventory.aInvNoId,
                                         0
                             );

         SELECT COUNT(*) 
           INTO lNum 
           FROM evt_inv 
          WHERE    evt_inv.event_db_id = aEventDbId AND
                      evt_inv.event_id = aEventId   AND 
                  evt_inv.inv_no_db_id = lInventory.aInvNoDbId AND
                     evt_inv.inv_no_id = lInventory.aInvNoId   AND
                  evt_inv.event_inv_id = lMaxId;


         IF lNum <> 1 THEN
            --exception
            RAISE xc_CreateEvtInvEntry;           
         END IF;

      END LOOP;
   END IF;



      --Take configuration snapshot of the event inventories
      UPDATE evt_inv
         SET (    nh_inv_no_db_id, 
                     nh_inv_no_id,
              assmbl_inv_no_db_id, 
                 assmbl_inv_no_id,
                   h_inv_no_db_id, 
                      h_inv_no_id,
                     assmbl_db_id, 
                        assmbl_cd, 
                    assmbl_bom_id, 
                    assmbl_pos_id,
                    part_no_db_id, 
                       part_no_id,
                   bom_part_db_id, 
                      bom_part_id
                ) = (SELECT  nh_inv_no_db_id, 
                                nh_inv_no_id,
                         assmbl_inv_no_db_id, 
                            assmbl_inv_no_id,
                              h_inv_no_db_id, 
                                 h_inv_no_id,
                                assmbl_db_id, 
                                   assmbl_cd, 
                               assmbl_bom_id,
                               assmbl_pos_id,
                               part_no_db_id,
                                  part_no_id,
                              bom_part_db_id, 
                                 bom_part_id
                        FROM inv_inv
                       WHERE inv_inv.inv_no_db_id  = evt_inv.inv_no_db_id AND
                             inv_inv.inv_no_id     = evt_inv.inv_no_id
                   )
      WHERE  evt_inv.event_db_id = aEventDbId AND
                evt_inv.event_id = aEventId;

      -- Assign the new aircraft type if it is provided
      IF aAssmblDbId > 0 AND aAssmblCd IS NOT NULL THEN
         UPDATE jl_flight
            SET plan_assmbl_db_id = aAssmblDbId, 
                   plan_assmbl_cd = aAssmblCd
         WHERE jl_flight.flight_db_id = aEventDbId AND
               jl_flight.flight_id = aEventId;
      END IF;

      --Regenerate the original aircraft's flight plan
      generateFlightPlanForAircraft(lOriginalInvNoDbId, lOriginalInvNoId);

      --Regenerate the new aircraft's flight plan
      generateFlightPlanForAircraft(aInvNoDbId, aInvNoId);

      --return success
      aReturn := iSuccess;


      EXCEPTION
          WHEN xc_CreateEvtInvEntry THEN
               aReturn := iCreateEvtInvEntryError;
          WHEN xc_UsageCalculations THEN
               aReturn := iUsageCalculationsError;
          WHEN xc_UpdateDeadlineForInvTree THEN
               aReturn := iUpdateDeadlineForInvTreeError;
          WHEN OTHERS THEN
               aReturn := iOracleError;

END aircraftSwap;

END FlightInterface;
/