--liquibase formatted sql


--changeSet MX-26426:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/** If this code changes, ensure that the ExtendDeadline.jsp#updateNewDeadline method is updated as well */
CREATE OR REPLACE FUNCTION getExtendedDeadlineDt
(
   aDeltaQt      evt_sched_dead.deviation_qt%TYPE,
   aSchedDt      evt_sched_dead.sched_dead_dt%TYPE,
   aDomainTypeCd mim_data_type.domain_type_cd%TYPE,
   aDataTypeCd   mim_data_type.data_type_cd%TYPE,
   aMultiplier   ref_eng_unit.Ref_Mult_Qt%TYPE

) RETURN DATE
IS
   lExtDeadlineDt DATE;

BEGIN
   IF (aDomainTypeCd = 'US') THEN
      lExtDeadlineDt := aSchedDt;
      
   /* if the data type is month then use months to calculate new deadline (not days) */
   ELSIF aDataTypeCd = 'CMON' THEN
      lExtDeadlineDt := ADD_MONTHS( aSchedDt, aDeltaQt ) + TRUNC((aDeltaQt - TRUNC(aDeltaQt)) * aMultiplier);
      
   /* if the data type is month then use months to calculate new deadline (not days) */
   ELSIF aDataTypeCd = 'CLMON' THEN
      lExtDeadlineDt :=  ADD_MONTHS(  LAST_DAY(aSchedDt), aDeltaQt );
      
   /* if the data type is year then use 12 months to calculate new deadline (not days) */
   ELSIF aDataTypeCd = 'CYR' THEN
      lExtDeadlineDt := ADD_MONTHS( aSchedDt, aDeltaQt*12 );
      
   /* if the data type is hour, do not truncate */
   ELSIF aDataTypeCd = 'CHR' THEN
      lExtDeadlineDt := aSchedDt + (aDeltaQt * aMultiplier);
      
   /* otherwise add the correct # of days to the start date */
   ELSE
      lExtDeadlineDt := aSchedDt + TRUNC(aDeltaQt * aMultiplier);

   END IF;

   RETURN lExtDeadlineDt;

END getExtendedDeadlineDt;
/