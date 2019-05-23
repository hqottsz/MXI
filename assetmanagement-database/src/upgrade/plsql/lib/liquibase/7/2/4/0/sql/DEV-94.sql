--liquibase formatted sql


--changeSet DEV-94:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('TASK_TASK', 'COMPLETE_WHEN_UNINST_BOOL');
END;
/

--changeSet DEV-94:2 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, DB_TYPE_CD, PARM_TYPE
   )
   SELECT 'ACTION_ENFORCE_NSV_DEADLINES', 'OPER', 'SECURED_RESOURCE'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ENFORCE_NSV_DEADLINES' and db_type_cd = 'OPER' );      