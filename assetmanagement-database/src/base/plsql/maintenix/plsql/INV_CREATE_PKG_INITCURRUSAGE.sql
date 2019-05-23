--liquibase formatted sql


--changeSet INV_CREATE_PKG_INITCURRUSAGE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure INV_CREATE_PKG_INITCURRUSAGE(
      al_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
      al_InvNoId IN inv_inv.inv_no_id%TYPE,
      al_Return OUT NUMBER)
   IS
BEGIN
   INV_CREATE_PKG.InitializeCurrentUsage( Al_InvNoDbId, Al_InvNoId, Al_Return );
END INV_CREATE_PKG_INITCURRUSAGE;
/