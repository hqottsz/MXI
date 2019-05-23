--liquibase formatted sql

--changeSet OPER-11648:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_modify ('
        ALTER TABLE utl_current_version MODIFY BUILD_LABEL VARCHAR2(100 CHAR)
    ');
END;
/

--changeSet OPER-11648:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add ('
      ALTER TABLE utl_current_version ADD build_revision_tmp VARCHAR2(50 CHAR)
   ');    
END;
/

--changeSet OPER-11648:3 stripComments:false
UPDATE utl_current_version
SET 
   build_revision_tmp = build_revision, 
   build_revision = NULL;

--changeSet OPER-11648:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_cons_chk_drop('UTL_CURRENT_VERSION','BUILD_REVISION');
END;
/

--changeSet OPER-11648:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_modify ('
        ALTER TABLE utl_current_version MODIFY BUILD_REVISION VARCHAR2(50 CHAR)
    ');
END;
/

--changeSet OPER-11648:6 stripComments:false
UPDATE utl_current_version
SET build_revision = build_revision_tmp;

--changeSet OPER-11648:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    utl_migr_schema_pkg.table_column_drop('UTL_CURRENT_VERSION','BUILD_REVISION_TMP');
END;
/

