--liquibase formatted sql


--changeSet OPER-4753:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add columns to fnc_xaction_log to track the part's AUP, total quantity and total value before and after the transactions
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE FNC_XACTION_LOG
  ADD (AVG_UNIT_PRICE_BEFORE_TX NUMBER(15,5),
       TOTAL_QT_BEFORE_TX FLOAT,
       TOTAL_VALUE_BEFORE_TX NUMBER(15,5),
       AVG_UNIT_PRICE_AFTER_TX NUMBER(15,5),
       TOTAL_QT_AFTER_TX FLOAT,
       TOTAL_VALUE_AFTER_TX NUMBER(15,5))
');
END;
/

--changeSet OPER-4753:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add columns to EQP_PART_ROTABLE_ADJUST to track the part's AUP, total quantity and total value before and after the adjust
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE EQP_PART_ROTABLE_ADJUST
  ADD (AVG_UNIT_PRICE_BEFORE_ADJUST NUMBER(15,5),
       TOTAL_QT_BEFORE_ADJUST FLOAT,
       TOTAL_VALUE_BEFORE_ADJUST NUMBER(15,5),
       AVG_UNIT_PRICE_AFTER_ADJUST NUMBER(15,5),
       TOTAL_QT_AFTER_ADJUST FLOAT,
       TOTAL_VALUE_AFTER_ADJUST NUMBER(15,5))
');
END;
/  

--changeSet OPER-4753:3 stripComments:false
  -- comments on fnc_xaction_log columns 
  COMMENT ON COLUMN FNC_XACTION_LOG.AVG_UNIT_PRICE_BEFORE_TX
IS
  'Part average unit price before the transaction' ;

--changeSet OPER-4753:4 stripComments:false
  COMMENT ON COLUMN FNC_XACTION_LOG.TOTAL_QT_BEFORE_TX
IS
  'Part total quantity before the transaction' ;

--changeSet OPER-4753:5 stripComments:false
  COMMENT ON COLUMN FNC_XACTION_LOG.TOTAL_VALUE_BEFORE_TX
IS
  'Part total value before the transaction' ;

--changeSet OPER-4753:6 stripComments:false
  COMMENT ON COLUMN FNC_XACTION_LOG.AVG_UNIT_PRICE_AFTER_TX
IS
  'Part average unit price after the transaction' ;

--changeSet OPER-4753:7 stripComments:false
  COMMENT ON COLUMN FNC_XACTION_LOG.TOTAL_QT_AFTER_TX
IS
  'Part total quantity after the transaction' ;

--changeSet OPER-4753:8 stripComments:false
  COMMENT ON COLUMN FNC_XACTION_LOG.TOTAL_VALUE_AFTER_TX
IS
  'Part total value after the transaction' ;   

--changeSet OPER-4753:9 stripComments:false
 -- comments on eqp_part_rotable_adjust columns
  COMMENT ON COLUMN EQP_PART_ROTABLE_ADJUST.AVG_UNIT_PRICE_BEFORE_ADJUST
IS
  'Part average unit price before the adjust' ;

--changeSet OPER-4753:10 stripComments:false
  COMMENT ON COLUMN EQP_PART_ROTABLE_ADJUST.TOTAL_QT_BEFORE_ADJUST
IS
  'Part total quantity before the adjust' ;

--changeSet OPER-4753:11 stripComments:false
  COMMENT ON COLUMN EQP_PART_ROTABLE_ADJUST.TOTAL_VALUE_BEFORE_ADJUST
IS
  'Part total value before the adjust' ;

--changeSet OPER-4753:12 stripComments:false
  COMMENT ON COLUMN EQP_PART_ROTABLE_ADJUST.AVG_UNIT_PRICE_AFTER_ADJUST
IS
  'Part average unit price after the adjust' ;

--changeSet OPER-4753:13 stripComments:false
  COMMENT ON COLUMN EQP_PART_ROTABLE_ADJUST.TOTAL_QT_AFTER_ADJUST
IS
  'Part total quantity after the adjust' ;

--changeSet OPER-4753:14 stripComments:false
  COMMENT ON COLUMN EQP_PART_ROTABLE_ADJUST.TOTAL_VALUE_AFTER_ADJUST
IS
  'Part total value after the adjust' ;