--liquibase formatted sql


--changeSet PREP_DEADLINE_PKG_CALC_CA:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure PREP_DEADLINE_PKG_CALC_CA (
            an_DataTypeDbId  IN  evt_sched_dead.data_type_db_id%TYPE,
            an_DataTypeId    IN  evt_sched_dead.data_type_id%TYPE,
            as_StartDt       IN OUT evt_stage.stage_reason_cd%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            as_NewDeadlineDt IN OUT evt_stage.stage_reason_cd%TYPE,
            on_Return        OUT NUMBER
        ) IS

        ld_StartDt evt_sched_dead.start_dt%TYPE;
        ld_NewDeadlineDt evt_sched_dead.sched_dead_dt%TYPE;

BEGIN

   -- Initialize the return value
   on_Return := 0;

   IF (as_StartDt IS NOT NULL) THEN
      ld_StartDt:=to_date( as_StartDt, 'dd-mon-yyyy hh24:mi:ss');
   END IF;

   IF (as_NewDeadlineDt IS NOT NULL) THEN
      ld_NewDeadlineDt:=to_date( as_NewDeadlineDt, 'dd-mon-yyyy hh24:mi:ss');
   END IF;
   PREP_DEADLINE_PKG.FindCalendarDeadlineVariables(
            an_DataTypeDbId,
            an_DataTypeId,
            ld_StartDt,
            an_Interval,
            ld_NewDeadlineDt,
            on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   as_StartDt:=to_char(ld_StartDt,'dd-mon-yyyy hh24:mi:ss');
   as_NewDeadlineDt:=to_char(ld_NewDeadlineDt, 'dd-mon-yyyy hh24:mi:ss');

   -- Return success
   on_Return := 1;

END PREP_DEADLINE_PKG_CALC_CA;
/