--liquibase formatted sql


--changeSet PrepDeadlinePkgResetSchedFrom:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE PrepDeadlinePkgResetSchedFrom(
            an_SchedDbId  IN sched_stask.sched_db_id%TYPE,
            an_SchedId    IN sched_stask.sched_id%TYPE,
            on_Return     OUT NUMBER
        ) IS
BEGIN

   PREP_DEADLINE_PKG.ResetActualSchedFromToBaseline(
      an_SchedDbId,
      an_SchedId,
      on_Return
   );

END PrepDeadlinePkgResetSchedFrom;
/