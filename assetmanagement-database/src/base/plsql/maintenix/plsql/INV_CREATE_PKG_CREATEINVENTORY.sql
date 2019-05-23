--liquibase formatted sql


--changeSet INV_CREATE_PKG_CREATEINVENTORY:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure INV_CREATE_PKG_CREATEINVENTORY(
      al_InvNoDbId IN inv_inv.inv_no_db_id%TYPE,
      al_InvNoId IN inv_inv.inv_no_id%TYPE,
      as_CreateType IN VARCHAR2,
      ab_ApplyReceivedDt IN INTEGER,
      ab_ApplyManufacturedDt IN INTEGER,
      al_Return OUT NUMBER)
   IS
BEGIN
   INV_CREATE_PKG.CreateInventory(
      al_InvNoDbId,
      al_InvNoId,
      as_CreateType,
      sys.diutil.int_to_bool(ab_ApplyReceivedDt),
      sys.diutil.int_to_bool(ab_ApplyManufacturedDt),
      al_Return
   );
END INV_CREATE_PKG_CREATEINVENTORY;
/