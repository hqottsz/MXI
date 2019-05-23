-- update the external maintenance adapter config parms with values
UPDATE UTL_CONFIG_PARM SET PARM_VALUE = 'CONSUMABLE', DEFAULT_VALUE = 'CONSUMABLE' WHERE PARM_NAME='DEFAULT_CONSUM_FINANCIAL_CLASS';
UPDATE UTL_CONFIG_PARM SET PARM_VALUE = 'TEST_INVASSET', DEFAULT_VALUE = 'INVASSET' WHERE PARM_NAME='DEFAULT_CONSUM_ASSET_ACCOUNT';

-- disable the HTTP Compression config parameter which will break the unittests
UPDATE UTL_CONFIG_PARM SET PARM_VALUE = 'false' WHERE PARM_NAME='HTTP_COMPRESSION_ENABLED';

--Turn off Nightly Jobs - these should eventually be left ON
UPDATE utl_job SET active_bool = 0 WHERE start_time is not null;
UPDATE utl_job SET active_bool = 0 WHERE job_cd = 'MX_CORE_GENERATEFLIGHTPLANFORAIRCRAFT';

-- Run intensive jobs on a reduced schedule so that the build database doesn't get overloaded.
UPDATE utl_job SET active_bool = 1, repeat_interval = NULL, start_delay = 60 WHERE job_cd like '%MVIEW%';

-- Update the parm values to ensure the admin role is up to date
DELETE FROM utl_role_parm WHERE role_id = 19000;

INSERT INTO utl_role_parm (parm_name, parm_type, role_id, parm_value, utl_id) 
VALUES ('WELCOME_PAGE', 'MXCOMMONWEB', 19000, '/maintenix/common/ToDoList.jsp',0);

INSERT INTO utl_role_parm (parm_name, role_id, parm_value, parm_type, utl_id)
   (SELECT
      parm_name,
      19000 AS role_id,
      'true' as parm_value,
      parm_type,
      4650
   FROM
      utl_config_parm
   WHERE
      parm_type = 'SECURED_RESOURCE'
   );

DELETE FROM utl_action_role_parm WHERE role_id = 19000;
INSERT INTO utl_action_role_parm (parm_name, role_id, parm_value, utl_id)
   (SELECT
      parm_name,
      19000 AS role_id,
      'true' as parm_value,
      4650
   FROM
      utl_action_config_parm
   );
