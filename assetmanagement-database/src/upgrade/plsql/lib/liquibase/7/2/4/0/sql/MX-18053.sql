--liquibase formatted sql


--changeSet MX-18053:1 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_OIL_CONSUMPTION_RATE_DEFINITION';  

--changeSet MX-18053:2 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_OIL_CONSUMPTION_ASSEMBLY_THRESHOLD';  

--changeSet MX-18053:3 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_CREATE_OIL_CONSUMPTION_PARTNO_THRESHOLD';    

--changeSet MX-18053:4 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_EDIT_OIL_CONSUMPTION_PARTNO_THRESHOLD';    

--changeSet MX-18053:5 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_DELETE_OIL_CONSUMPTION_PARTNO_THRESHOLD';    

--changeSet MX-18053:6 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_CREATE_OIL_CONSUMPTION_OPERATOR_THRESHOLD';    

--changeSet MX-18053:7 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_EDIT_OIL_CONSUMPTION_OPERATOR_THRESHOLD';    

--changeSet MX-18053:8 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_DELETE_OIL_CONSUMPTION_OPERATOR_THRESHOLD';    

--changeSet MX-18053:9 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_CREATE_OIL_CONSUMPTION_SERIAL_NO_THRESHOLD';    

--changeSet MX-18053:10 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_EDIT_OIL_CONSUMPTION_SERIAL_NO_THRESHOLD';    

--changeSet MX-18053:11 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_DELETE_OIL_CONSUMPTION_SERIAL_NO_THRESHOLD';    

--changeSet MX-18053:12 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_ESCALATE_OIL_CONSUMPTION_STATUS';    

--changeSet MX-18053:13 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'ACTION_AUTO_UPDATE_ESCALATE_OIL_CONSUMPTION_STATUS';    

--changeSet MX-18053:14 stripComments:false
UPDATE utl_config_parm 
  SET CATEGORY = 'Assembly - Oil Monitoring'
  WHERE parm_name = 'HOC_BOOL_DURATION_DAYS';        