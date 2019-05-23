--liquibase formatted sql


--changeSet INV_DESC_PKG_INVUPDINVDESC:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure INV_DESC_PKG_INVUPDINVDESC(
      al_InvDbId IN inv_inv.inv_no_db_id%TYPE,
      al_InvId IN inv_inv.inv_no_id%TYPE,
      as_InvSdesc OUT inv_inv.inv_no_sdesc%TYPE,
      al_Return OUT NUMBER
   ) IS
BEGIN
   INV_DESC_PKG.InvUpdInvDesc( al_Invdbid, al_Invid, as_Invsdesc, al_Return );
END INV_DESC_PKG_INVUPDINVDESC;
/