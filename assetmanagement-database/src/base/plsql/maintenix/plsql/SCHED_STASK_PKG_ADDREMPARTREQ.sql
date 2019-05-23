--liquibase formatted sql


--changeSet SCHED_STASK_PKG_ADDREMPARTREQ:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure SCHED_STASK_PKG_ADDREMPARTREQ(
             an_InvDbId         IN inv_inv.inv_no_db_id%TYPE,
             an_InvId         IN inv_inv.inv_no_id%TYPE,
             an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
             an_SchedId           IN sched_stask.sched_id%TYPE,
             an_SchedPartId       IN sched_part.sched_part_id%TYPE,
             an_RemovedReasonDbid  IN task_part_list.remove_reason_db_id%TYPE,
             as_RemovedReasonCd    IN task_part_list.remove_reason_cd%TYPE,
             an_SchedRmvdPartId    OUT sched_rmvd_part.sched_rmvd_part_id%TYPE,
             on_Return            OUT NUMBER)
   IS
BEGIN

  SCHED_STASK_PKG.AddRmvdPartRequirement(an_InvDbId,
             an_InvId,
             an_SchedDbId,
             an_SchedId,
             an_SchedPartId,
             an_RemovedReasonDbid,
             as_RemovedReasonCd,
             an_SchedRmvdPartId,
             on_Return);
END SCHED_STASK_PKG_ADDREMPARTREQ;
/