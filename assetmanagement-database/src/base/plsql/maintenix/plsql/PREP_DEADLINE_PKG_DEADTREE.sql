--liquibase formatted sql


--changeSet PREP_DEADLINE_PKG_DEADTREE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure PREP_DEADLINE_PKG_DEADTREE(
					an_StartSchedDbId IN sched_stask.sched_db_id%TYPE,
					an_StartSchedId IN sched_stask.sched_db_id%TYPE,
					on_Return OUT NUMBER)
	IS
BEGIN

  PREP_DEADLINE_PKG.UpdateDependentDeadlinesTree(an_StartSchedDbId, An_StartSchedId, on_Return);
END PREP_DEADLINE_PKG_DEADTREE;
/