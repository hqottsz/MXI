--liquibase formatted sql

--changeSet OPER-11698:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
	Alter table EQP_STOCK_NO add (
	NOTE VARCHAR2(2000)
	)
');
END;
/

--changeSet OPER-11698:2 stripComments:false
COMMENT ON COLUMN EQP_STOCK_NO.NOTE
IS
  'Generic receiving notes for the stock number.' ;
  		
 --changeSet OPER-11698:4 stripComments:false
-- add new JSP Action permission for Notes in Stock Details page
INSERT INTO
  utl_action_config_parm
  (
    PARM_NAME, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
     'ACTION_STOCK_NOTES', 'TRUE',  0, 'Permission to add notes to a stock.' , 'TRUE/FALSE', 'TRUE', 1, 'Parts - Stock Numbers', '8.2-SP5', 0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE PARM_NAME = 'ACTION_STOCK_NOTES' );	