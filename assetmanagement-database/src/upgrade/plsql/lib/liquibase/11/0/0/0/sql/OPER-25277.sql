--liquibase formatted sql

--changeSet OPER-25277:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_column_add (
        'ALTER TABLE INV_LOC_STOCK ADD IGNORE_OWNER_BOOL NUMBER (1) DEFAULT 0'
   );
END;
/

--changeSet OPER-25277:2 stripComments:false
COMMENT ON COLUMN INV_LOC_STOCK.IGNORE_OWNER_BOOL
IS
  'Determines whether the stock owner is considered in stock low assessments for a stock level and in subsequent stock distribution requests.' ;

--changeSet OPER-25277:3 stripComments:false
COMMENT ON COLUMN INV_LOC_STOCK.MAX_QT
IS
  'The maximum quantity to store at this location.' ;
