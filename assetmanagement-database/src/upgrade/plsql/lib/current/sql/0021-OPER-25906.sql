--liquibase formatted sql
--changeSet OPER-25906:1 stripComments:false  endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--Ensure the Job Page is Viewable to those with ACTION_RESET_JOBS or ACTION_RUN_JOBS

BEGIN
   utl_migr_data_pkg.action_parm_insert(
                    'ACTION_VIEW_JOBS',
                    'Permission to access the Job Viewer page.',
                    'TRUE/FALSE',
                    'FALSE',
                    1,
                    'System - Jobs',
                    '8.3-SP2',
                     0,
                     0
                 );
END;
/
--changeSet OPER-25906:2 stripComments:false  endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
           'ACTION_VIEW_JOBS',
           'ACTION_RUN_JOBS'
   );
END;
/
--changeSet OPER-25906:3 stripComments:false  endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_copy(
           'ACTION_VIEW_JOBS',
           'ACTION_RESET_JOBS'
   );
END;
/

--changeSet 0utl_action_config_parm:8 stripComments:false
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'This parameter is used to determine whether a user is allowed to manually start a job from the Job Viewer page. Requires: ACTION_VIEW_JOBS'  WHERE parm_name = 'ACTION_RUN_JOBS';
UPDATE UTL_ACTION_CONFIG_PARM SET parm_desc = 'This parameter is used to determine whether a user is allowed to manually reset a job status from the Job Viewer page. Requires: ACTION_VIEW_JOBS' WHERE parm_name = 'ACTION_RESET_JOBS';



