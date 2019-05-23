--liquibase formatted sql

--changeSet OPER-14485:1 stripComments:false
-- add sPADayCount parameter for user planned attendance report
INSERT INTO
  UTL_CONFIG_PARM
  (
    PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
  )
  SELECT
    '14', 'sPADayCount', 'SESSION','Stores the last value entered in the search form searching planned attendance by number of days.', 'USER', 'Number', '14', 0, 'SESSION', '8.2-SP4', 0
  FROM
    DUAL
  WHERE
    NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME LIKE 'sPADayCount' );