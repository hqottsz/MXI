--liquibase formatted sql


--changeSet OPER-2492:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_cons_chk_drop('PPC_ACTIVITY', 'LOCKED_BOOL');
END;
/

--changeSet OPER-2492:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_rename('PPC_ACTIVITY','LOCKED_BOOL','PINNED_BOOL');
END;
/ 

--changeSet OPER-2492:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_cons_chk_drop('PPC_ACTIVITY', 'PINNED_BOOL');
END;
/

--changeSet OPER-2492:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
   Alter table PPC_ACTIVITY modify (
      PINNED_BOOL Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (PINNED_BOOL IN (0, 1) ) DEFERRABLE 
   )
');
END;
/ 

--changeSet OPER-2492:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_cons_chk_drop('PPC_WP', 'LOCKED_BOOL');
END;
/ 

--changeSet OPER-2492:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_rename('PPC_WP','LOCKED_BOOL','PINNED_BOOL');
END;
/ 

--changeSet OPER-2492:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_cons_chk_drop('PPC_WP', 'PINNED_BOOL');
END;
/

--changeSet OPER-2492:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
   Alter table PPC_WP modify (
      PINNED_BOOL  Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (PINNED_BOOL IN (0, 1) ) DEFERRABLE 
   )
');
END;
/   