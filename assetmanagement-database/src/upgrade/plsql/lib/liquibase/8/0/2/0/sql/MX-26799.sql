--liquibase formatted sql


--changeSet MX-26799:1 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name='sVisibleAssemblies';

--changeSet MX-26799:2 stripComments:false
DELETE FROM utl_user_parm WHERE parm_name='sVisibleLocations';

--changeSet MX-26799:3 stripComments:false
UPDATE utl_config_parm SET parm_value='', encrypt_bool=0, modified_in='8.1' WHERE parm_name='sVisibleAssemblies';

--changeSet MX-26799:4 stripComments:false
UPDATE utl_config_parm SET parm_value='', encrypt_bool=0, modified_in='8.1' WHERE parm_name='sVisibleLocations';