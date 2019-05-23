--liquibase formatted sql


--changeSet SCHED_STASK_PKG_GENREMINSPARTS:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure SCHED_STASK_PKG_GENREMINSPARTS(
             an_InvDbId         IN inv_inv.inv_no_db_id%TYPE,
             an_InvId         IN inv_inv.inv_no_id%TYPE,
             an_SchedDbId         IN sched_stask.sched_db_id%TYPE,
             an_SchedId           IN sched_stask.sched_id%TYPE,
             an_SchedPartId       IN sched_part.sched_part_id%TYPE,
             an_RemovedReasonDbid  IN task_part_list.remove_reason_db_id%TYPE,
             as_RemovedReasonCd    IN task_part_list.remove_reason_cd%TYPE,
             ab_Remove_bool        IN task_part_list.remove_bool%TYPE,
             ab_Install_bool        IN task_part_list.install_bool%TYPE,
             on_Return            OUT NUMBER)
   IS
BEGIN

  SCHED_STASK_PKG.GenerateRemoveInstallPartReq (
       an_InvDbId,
       an_InvId,
       an_SchedDbId,
       an_SchedId,
       an_SchedPartId,
       an_RemovedReasonDbid,
       as_RemovedReasonCd,
       ab_Remove_bool,
       ab_Install_bool,
       on_Return
   ) ;
END SCHED_STASK_PKG_GENREMINSPARTS;
/