--liquibase formatted sql


--changeSet OPER-5825:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop the all check constraint on FINANCE_STATUS_CD first and then recreate
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_cons_chk_drop ('INV_INV','FINANCE_STATUS_CD');
END;
/ 

--changeSet OPER-5825:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE INV_INV ADD CHECK ( FINANCE_STATUS_CD IN (''NEW'', ''INSP'', ''RCVD'')) DEFERRABLE'
   );
END;
/  

--changeSet OPER-5825:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE INV_INV MODIFY (
         FINANCE_STATUS_CD  NOT NULL DEFERRABLE 
)
');
END;
/