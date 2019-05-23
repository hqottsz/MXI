--liquibase formatted sql

--changeSet OPER-15968:1 stripComments:false
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_USER_TEMP_ROLE', 'IX_UTLUSERTEMPROLE');
END;
/