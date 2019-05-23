--liquibase formatted sql


--changeSet MX-18249:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      calcRollingAvg
* Arguments:     aSerialNo       - serial no. of the inventory
*                aDate           - end date of the calculation period
*                aNumOfDaysPrior - num of days prior to end date (start of calc period)
* Returns:       Average oil conumption rate.
* Description:   This function calculates the average oil conumption rate for 
*                an inventory with the given Serial No.
*                The time period will end on the given end date and 
*                will start on this date minus the given num of days prior.            
*
* Orig.Coder:    Al Hogan
* Recent Coder:  
* Recent Date:   Nov 29, 2009
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION calcRollingAvg
(
   aRollingAvgName    STRING,
   aRollingAvgValue   STRING,
   aAcAssemblyCd      STRING,
   aAssemblyCd        STRING,
   aDate              Date,
   aNumOfDaysPrior    INTEGER,
   aCurrentUserId     INTEGER

) RETURN FLOAT
IS
   lTotalOilQt FLOAT;
   lTotalDeltaTimeQt FLOAT;
   lAvg FLOAT;

BEGIN
   lAvg := NULL;

   SELECT
      SUM(fct_inv_oil.oil_qt),
      SUM(fct_inv_oil.delta_time_qt)
   INTO
      lTotalOilQt,
      lTotalDeltaTimeQt
   FROM 
      fct_inv_oil
   INNER JOIN dim_inv ON
      dim_inv.dim_inv_id = fct_inv_oil.dim_inv_id 
      AND
      dim_inv.ac_assmbl_cd = aAcAssemblyCd AND
      dim_inv.assmbl_cd    = aAssemblyCd
   INNER JOIN org_hr ON
      org_hr.user_id = aCurrentUserId
   LEFT OUTER JOIN org_carrier ON
      org_carrier.carrier_name = dim_inv.carrier_name
   WHERE 
      fct_inv_oil.dim_time_id IN (
         SELECT
            dim_time.dim_time_id
         FROM
            dim_time
         WHERE
            dim_time.day_timestamp >= to_char(aDate - aNumOfDaysPrior, 'YYYY-MM-DD') || ' 00:00:00' AND
            dim_time.day_timestamp <= to_char(aDate, 'YYYY-MM-DD') || ' 00:00:00'
      )
      AND
      aRollingAvgValue =
         CASE
            WHEN aRollingAvgName = 'Assembly' THEN aRollingAvgValue
            WHEN aRollingAvgName = 'SerialNo' THEN dim_inv.serial_no_oem
            WHEN aRollingAvgName = 'Operator' THEN dim_inv.carrier_name
            WHEN aRollingAvgName = 'PartNo'   THEN dim_inv.part_no_oem
         END
      AND
      isAuthorizedForAssembly(org_hr.hr_db_id, org_hr.hr_id, dim_inv.ac_assmbl_db_id, dim_inv.ac_assmbl_cd) = 1
      AND
      isAuthorizedForInv(org_hr.hr_db_id, org_hr.hr_id, dim_inv.inv_no_db_id, dim_inv.inv_no_id) = 1
      AND
      isAuthorizedForOperator(org_hr.hr_db_id, org_hr.hr_id, org_carrier.carrier_db_id, org_carrier.carrier_id) = 1
   ;

   IF ( lTotalDeltaTimeQt > 0 ) THEN
      lAvg := ( lTotalOilQt / lTotalDeltaTimeQt );
   END IF;

   RETURN lAvg;

END calcRollingAvg;
/