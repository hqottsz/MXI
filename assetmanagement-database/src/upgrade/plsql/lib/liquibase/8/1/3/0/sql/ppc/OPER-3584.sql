--liquibase formatted sql


--changeSet OPER-3584:1 stripComments:false
-- OPER-3584: Registering PPC report ProjectStatus in Operator
DELETE FROM utl_config_parm
   WHERE
      parm_name = 'ProjectStatus';

--changeSet OPER-3584:2 stripComments:false
INSERT INTO utl_config_parm
(
   parm_name,
   parm_type,
   parm_value,
   encrypt_bool,
   parm_desc,
   config_type,
   default_value,
   allow_value_desc,
   mand_config_bool,
   category,
   modified_in,
   repl_approved,
   utl_id
)
SELECT
   'ProjectStatus',
   'REPORT_PATH_MAPPING',
   '/organizations/Maintenix/Reports/Operator/Planning/ProjectStatus',
   0,
   'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
   'GLOBAL',
   '/organizations/Maintenix/Reports/Operator/Planning/ProjectStatus',
   'Use path from the report properties in Jasper',
   0,
   'Report Parameter',
   '8.2',
   0,
   0
FROM
   DUAL
WHERE NOT EXISTS
(
   SELECT
      1
   FROM
      utl_config_parm
   WHERE
      parm_name = 'ProjectStatus' and parm_type = 'REPORT_PATH_MAPPING'
);

--changeSet OPER-3584:3 stripComments:false
INSERT INTO utl_config_parm 
(
   parm_name,
   parm_type,
   parm_value,
   encrypt_bool,
   parm_desc,
   config_type,
   default_value,
   allow_value_desc,
   mand_config_bool,
   category,
   modified_in,
   repl_approved,
   utl_id
)
SELECT
   'ProjectStatus',
   'REPORT_ENGINE',
   'JASPER_SSO',
   0,
   'Report engine name for generating Project Status report',
   'GLOBAL',
   'JASPER_SSO',
   'BOE, JASPER_SSO',
   0,
   'Report Parameter',
   '8.2',
   0,
   0
FROM
   DUAL
WHERE NOT EXISTS
(
   SELECT
      1
   FROM
      utl_config_parm
   WHERE
      parm_name = 'ProjectStatus' and parm_type = 'REPORT_ENGINE'
);

--changeSet OPER-3584:4 stripComments:false
INSERT INTO utl_report_type
(
   report_name,
   report_engine_type,
   report_path,
   report_desc,
   active_bool,
   system_bool,
   rstat_cd,
   revision_no,
   ctrl_db_id,
   creation_dt,
   creation_db_id,
   revision_dt,
   revision_db_id,
   revision_user
)
SELECT
   'ProjectStatus',
   'JASPER_SSO',
   '/organizations/Maintenix/Reports/Operator/Planning/ProjectStatus',
   'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports',
   1,
   0,
   0,
   1,
   0,
   SYSDATE,
   0,
   SYSDATE,
   0,
   'MXI'
FROM
   DUAL
WHERE NOT EXISTS
(
   SELECT
      1
   FROM
      utl_report_type
   WHERE
      report_name = 'ProjectStatus'
);