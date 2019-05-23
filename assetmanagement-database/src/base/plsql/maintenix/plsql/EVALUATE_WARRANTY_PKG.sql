--liquibase formatted sql


--changeSet EVALUATE_WARRANTY_PKG:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE EVALUATE_WARRANTY_PKG
(
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_db_id%TYPE,
   on_Return OUT NUMBER
)
IS
BEGIN
 warranty_eval_pkg.evaluatewarranty(an_SchedDbId,an_SchedId,on_Return);
END EVALUATE_WARRANTY_PKG;
/