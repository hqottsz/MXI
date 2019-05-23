--liquibase formatted sql


--changeSet PREP_DEADLINE_PKG_CALC_US:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure PREP_DEADLINE_PKG_CALC_US (
            an_StartQt       IN OUT evt_sched_dead.start_qt%TYPE,
            an_Interval      IN OUT evt_sched_dead.interval_qt%TYPE,
            an_NewDeadlineDt IN OUT evt_sched_dead.sched_dead_qt%TYPE,
            on_Return        OUT NUMBER
        ) IS

     BEGIN

       PREP_DEADLINE_PKG.FindUsageDeadlineVariables(
            an_StartQt,
            an_Interval,
            an_NewDeadlineDt,
            on_Return
   );



END PREP_DEADLINE_PKG_CALC_US;
/