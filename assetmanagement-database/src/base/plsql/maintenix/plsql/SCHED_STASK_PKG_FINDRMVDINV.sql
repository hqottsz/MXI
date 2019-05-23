--liquibase formatted sql


--changeSet SCHED_STASK_PKG_FINDRMVDINV:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure SCHED_STASK_PKG_FINDRMVDINV(
           an_StartInvNoDbId    IN inv_inv.inv_no_db_id%TYPE,
      an_StartInvNoId      IN inv_inv.inv_no_id%TYPE,
      an_FindAssmblDbId    IN eqp_assmbl_pos.assmbl_db_id%TYPE,
      as_FindAssmblCd      IN eqp_assmbl_pos.assmbl_cd%TYPE,
      an_FindAssmblBomId   IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
      an_FindAssmblPosId   IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
      an_FindBomPartDbId   IN eqp_bom_part.bom_part_db_id%TYPE,
      an_FindBomPartId     IN eqp_bom_part.bom_part_id%TYPE,
      on_InvNoDbId         OUT inv_inv.inv_no_db_id%TYPE,
      on_InvNoId           OUT inv_inv.inv_no_id%TYPE,
      on_Return            OUT NUMBER)
   IS
BEGIN

  SCHED_STASK_PKG.FindRemovedInventory(
      an_StartInvNoDbId,
      an_StartInvNoId,
      an_FindAssmblDbId,
      as_FindAssmblCd,
      an_FindAssmblBomId,
      an_FindAssmblPosId,
      an_FindBomPartDbId,
      an_FindBomPartId,
      on_InvNoDbId,
      on_InvNoId,
      on_Return);
END SCHED_STASK_PKG_FINDRMVDINV;
/