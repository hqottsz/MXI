--liquibase formatted sql


--changeSet SCHED_STASK_PKG_UPDATEHSCHED:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE SCHED_STASK_PKG_UPDATEHSCHED (
   an_SchedDbId   IN sched_stask.sched_db_id%TYPE,
   an_SchedId     IN sched_stask.sched_id%TYPE,
   on_Return      OUT INTEGER
) IS
BEGIN
   SCHED_STASK_PKG.UPDATEHSCHED(
      an_SchedDbId,
      an_SchedId,
      on_Return
   );
END SCHED_STASK_PKG_UPDATEHSCHED;
/