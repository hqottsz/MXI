--liquibase formatted sql


--changeSet OPER-19576:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER tubr_inv_loc DISABLE';
END;
/

--changeSet OPER-19576:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE inv_loc ADD (
         no_hub_auto_rsrv_bool NUMBER (1) DEFAULT 0 NOT NULL DEFERRABLE
      )
   ');
END;
/

--changeSet OPER-19576:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE inv_loc ADD (
         no_auto_rsrv_bool NUMBER (1) DEFAULT 0 NOT NULL DEFERRABLE
      )
   ');
END;
/

--changeSet OPER-19576:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER tubr_inv_loc ENABLE';
END;
/

