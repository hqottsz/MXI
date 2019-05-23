--liquibase formatted sql

--changeSet OPER-18057:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, 
	  PARM_NAME, 
	  PARM_TYPE, 
	  PARM_DESC, 
	  CONFIG_TYPE, 
	  ALLOW_VALUE_DESC, 
	  DEFAULT_VALUE, 
	  MAND_CONFIG_BOOL, 
	  CATEGORY, 
	  MODIFIED_IN, 
	  UTL_ID
   )
   SELECT 
      '8', 
      'DEFAULT_CREW_SHIFT_DAY_FOR_NEXT_HOURS', 
	  'LOGIC','This parameter specifies the default search crew shift day for the next hours.',
	  'GLOBAL', 
	  'Number', 
	  '8', 
	  1, 
	  'Maint - Tasks', 
	  '8.2-SP5', 
	  0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'DEFAULT_CREW_SHIFT_DAY_FOR_NEXT_HOURS' );
