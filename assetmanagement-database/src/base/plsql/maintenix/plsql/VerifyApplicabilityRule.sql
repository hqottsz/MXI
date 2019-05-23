--liquibase formatted sql


--changeSet VerifyApplicabilityRule:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure VERIFYAPPLICABILITYRULE(
               as_TaskApplSql IN task_task.task_appl_sql_ldesc%TYPE,
          an_Return OUT NUMBER)
   IS
BEGIN

  an_Return := SCHED_STASK_PKG.VERIFYAPPLICABILITYRULE(as_TaskApplSql);
END VERIFYAPPLICABILITYRULE;
/