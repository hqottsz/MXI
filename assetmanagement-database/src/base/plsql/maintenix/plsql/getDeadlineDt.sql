--liquibase formatted sql


--changeSet getDeadlineDt:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/** 
 * This function is used to add an interval to a starting date for CA data types only.
 *
 * Note: There is a subtle difference between this and getExtendedDeadlineDt.
 *       The CLMON does not support fractional values for interval, but does for deviation.
 */
CREATE OR REPLACE FUNCTION getDeadlineDt
(
   aIntervalQt   evt_sched_dead.interval_qt%TYPE,
   aStartDt      evt_sched_dead.sched_dead_dt%TYPE,
   aDataTypeCd   mim_data_type.data_type_cd%TYPE,
   aMultiplier   ref_eng_unit.Ref_Mult_Qt%TYPE

) RETURN DATE
IS
   lDeadlineDt DATE;

BEGIN
   /* if the data type is month then use months to calculate new deadline (not days) */
   IF aDataTypeCd = 'CMON' THEN
      lDeadlineDt := ADD_MONTHS( aStartDt, aIntervalQt ) + TRUNC((aIntervalQt - TRUNC(aIntervalQt)) * aMultiplier);

   /* if the data type is month then use months to calculate new deadline (not days) */
   ELSIF aDataTypeCd = 'CLMON' THEN
      lDeadlineDt :=  ADD_MONTHS(  LAST_DAY(aStartDt), aIntervalQt );

   /* if the data type is year then use 12 months to calculate new deadline (not days) */
   ELSIF aDataTypeCd = 'CYR' THEN
      lDeadlineDt := ADD_MONTHS( aStartDt, aIntervalQt*12 ) + TRUNC((aIntervalQt - TRUNC(aIntervalQt * 12)/12) * aMultiplier);

   /* if the data type is hour, do not truncate */
   ELSIF aDataTypeCd = 'CHR' THEN
      lDeadlineDt := aStartDt + (aIntervalQt * aMultiplier);

   /* otherwise add the correct # of days to the start date */
   ELSE
      lDeadlineDt := aStartDt + TRUNC(aIntervalQt * aMultiplier);

   END IF;

   RETURN lDeadlineDt;

END getDeadlineDt;
/