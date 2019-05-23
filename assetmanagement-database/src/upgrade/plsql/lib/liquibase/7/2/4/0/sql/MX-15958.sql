--liquibase formatted sql


--changeSet MX-15958:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_VIEW_COMPLIANCE_REPORT', 'SECURED_RESOURCE','Permission to view a compliance report.','USER', 'TRUE/FALSE', 'FALSE', 1,'Compliance - Task Impacts', '7.0',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_VIEW_COMPLIANCE_REPORT' );               

--changeSet MX-15958:2 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME, PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_VIEW_COMPLIANCE_REPORT', 'SECURED_RESOURCE','OPER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_VIEW_COMPLIANCE_REPORT' and db_type_cd = 'OPER' );      