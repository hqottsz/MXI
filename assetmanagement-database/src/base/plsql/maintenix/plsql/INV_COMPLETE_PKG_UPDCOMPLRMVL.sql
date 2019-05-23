--liquibase formatted sql


--changeSet INV_COMPLETE_PKG_UPDCOMPLRMVL:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure INV_COMPLETE_PKG_UPDCOMPLRMVL(
      al_ParentInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
      al_ParentInvNoId IN inv_inv.inv_no_id%TYPE,
      al_Return OUT NUMBER)
   IS
BEGIN
   INV_COMPLETE_PKG.UpdateCompleteFlagOnRemove(al_ParentInvNoId,
      al_ParentInvNoDbId, al_Return);
END INV_COMPLETE_PKG_UPDCOMPLRMVL;
/