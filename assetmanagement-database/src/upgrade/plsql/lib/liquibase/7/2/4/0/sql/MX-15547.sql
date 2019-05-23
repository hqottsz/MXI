--liquibase formatted sql


--changeSet MX-15547:1 stripComments:false
UPDATE UTL_CONFIG_PARM
SET    PARM_DESC = 'Permission to publish a Production Planning and Control plan'
WHERE  PARM_NAME = 'ACTION_PUBLISH_PROD_PLAN'
AND    PARM_TYPE = 'SECURED_RESOURCE';

--changeSet MX-15547:2 stripComments:false
UPDATE UTL_CONFIG_PARM
SET    PARM_DESC = 'Permission to open a Production Planning and Control plan'
WHERE  PARM_NAME = 'ACTION_OPEN_PROD_PLAN'
AND    PARM_TYPE = 'SECURED_RESOURCE';

--changeSet MX-15547:3 stripComments:false
UPDATE UTL_CONFIG_PARM
SET    PARM_DESC = 'Permission to save a Production Planning and Control plan'
WHERE  PARM_NAME = 'ACTION_SAVE_PROD_PLAN'
AND    PARM_TYPE = 'SECURED_RESOURCE';

--changeSet MX-15547:4 stripComments:false
UPDATE UTL_CONFIG_PARM
SET    PARM_DESC = 'Permission to load Maintenix actuals to a Production Planning and Control plan'
WHERE  PARM_NAME = 'ACTION_LOAD_ACTUALS_PROD_PLAN'
AND    PARM_TYPE = 'SECURED_RESOURCE';