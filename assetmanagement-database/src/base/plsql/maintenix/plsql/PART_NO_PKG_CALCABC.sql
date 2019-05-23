--liquibase formatted sql


--changeSet PART_NO_PKG_CALCABC:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure PART_NO_PKG_CALCABC
IS
BEGIN
   part_no_pkg.recalculateABCClass();
END PART_NO_PKG_CALCABC;
/