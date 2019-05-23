--liquibase formatted sql


--changeSet INV_CREATE_PKG_CREATESYS:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure INV_CREATE_PKG_CREATESYS(
      an_AssmblDbId IN eqp_assmbl_bom.assmbl_db_id%TYPE,
      as_AssmblCd IN eqp_assmbl_bom.assmbl_cd%TYPE,
      an_AssmblBomId IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
      on_Return OUT NUMBER)
   IS
BEGIN
   INV_CREATE_PKG.InvCreateSys( an_AssmblDbId, as_AssmblCd, an_AssmblBomId, on_Return );
END INV_CREATE_PKG_CREATESYS;
/