--liquibase formatted sql


--changeSet DEV-160:1 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_ADD_FAULT_RESULTING_EVENT', 'SECURED_RESOURCE','Permission to add resulting events to a fault.','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Faults', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_FAULT_RESULTING_EVENT' );  

--changeSet DEV-160:2 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_REMOVE_FAULT_RESULTING_EVENT', 'SECURED_RESOURCE','Permission to remove resulting events from a fault.','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Faults', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_FAULT_RESULTING_EVENT' );  

--changeSet DEV-160:3 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_ADD_HISTORIC_FAULT_RESULTING_EVENT', 'SECURED_RESOURCE','Permission to add resulting events to a historic fault.','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Faults', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ADD_HISTORIC_FAULT_RESULTING_EVENT' );  

--changeSet DEV-160:4 stripComments:false
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'false', 'ACTION_REMOVE_HISTORIC_FAULT_RESULTING_EVENT', 'SECURED_RESOURCE','Permission to remove resulting events from a historic fault.','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Faults', '7.5',0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_REMOVE_HISTORIC_FAULT_RESULTING_EVENT' );  

--changeSet DEV-160:5 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_ADD_FAULT_RESULTING_EVENT','SECURED_RESOURCE','OPER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_FAULT_RESULTING_EVENT' and db_type_cd = 'OPER' ); 

--changeSet DEV-160:6 stripComments:false
 INSERT INTO
    db_type_config_parm
    (
       PARM_NAME,  PARM_TYPE, DB_TYPE_CD
    )
    SELECT 'ACTION_REMOVE_FAULT_RESULTING_EVENT','SECURED_RESOURCE','OPER'
    FROM
       dual
    WHERE
       NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_REMOVE_FAULT_RESULTING_EVENT' and db_type_cd = 'OPER' );  

--changeSet DEV-160:7 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_ADD_HISTORIC_FAULT_RESULTING_EVENT','SECURED_RESOURCE','OPER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_HISTORIC_FAULT_RESULTING_EVENT' and db_type_cd = 'OPER' ); 

--changeSet DEV-160:8 stripComments:false
INSERT INTO
   db_type_config_parm
   (
      PARM_NAME,  PARM_TYPE, DB_TYPE_CD
   )
   SELECT 'ACTION_ADD_HISTORIC_FAULT_RESULTING_EVENT','SECURED_RESOURCE','OPER'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM db_type_config_parm WHERE parm_name = 'ACTION_ADD_HISTORIC_FAULT_RESULTING_EVENT' and db_type_cd = 'OPER' );  