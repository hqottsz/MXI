--liquibase formatted sql

--changeSet OPER-25824:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
        'ALTER TABLE STOCK_DIST_REQ ADD QTY_UNIT_DB_ID NUMBER (10) NOT NULL'
   );
END;
/

--changeSet OPER-25824:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
        'ALTER TABLE STOCK_DIST_REQ ADD QTY_UNIT_CD VARCHAR2 (8) NOT NULL'
   );
END;
/

--changeSet OPER-25824:3 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.QTY_UNIT_DB_ID
IS
  'Units of measure are used whenever you need to describe stock quantities for movement. FK to REF_QTY_UNIT.' ;

  --changeSet OPER-25824:4 stripComments:false
COMMENT ON COLUMN STOCK_DIST_REQ.QTY_UNIT_CD
IS
  'Units of measure are used whenever you need to describe stock quantities for movement. FK to REF_QTY_UNIT.' ;
  
--changeSet OPER-25824:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE STOCK_DIST_REQ ADD CONSTRAINT FK_REFQTYUNIT_STKDISTREQ FOREIGN KEY ( QTY_UNIT_DB_ID, QTY_UNIT_CD ) REFERENCES REF_QTY_UNIT ( QTY_UNIT_DB_ID, QTY_UNIT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-25824:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--make owner columns optional
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table STOCK_DIST_REQ modify (OWNER_DB_ID  NUMBER (10))
  ');
 END;
/

--changeSet OPER-25824:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table STOCK_DIST_REQ modify (OWNER_ID  NUMBER (10))
  ');
 END;
/

