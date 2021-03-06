--liquibase formatted sql


--changeSet INV_COMPLETE_PKG_UPDCOMPLINST:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure INV_COMPLETE_PKG_UPDCOMPLINST(
      al_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
      al_InvNoId IN inv_inv.inv_no_id%TYPE,
      al_Return OUT NUMBER)
   IS
BEGIN
   INV_COMPLETE_PKG.UpdateCompleteFlagOnInstall(al_InvNoId,
      al_InvNoDbId, al_Return);
END INV_COMPLETE_PKG_UPDCOMPLINST;
/