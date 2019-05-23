--liquibase formatted sql


--changeSet MX-24742:1 stripComments:false
 UPDATE
       utl_config_parm
  SET
       parm_value = default_value
 WHERE 
       parm_name = 'SPEC2000_VENDOR_CD_ENABLE' 
       AND
       parm_value != default_value;