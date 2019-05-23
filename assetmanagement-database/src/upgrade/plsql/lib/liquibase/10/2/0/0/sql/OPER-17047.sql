--liquibase formatted sql

--changeSet OPER-17047:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Add action parameter ACTION_LOG_FAULT_AND_DEFER (If missing) 
DECLARE

  ln_parm_exists INTEGER := 0;

BEGIN
   
   SELECT COUNT(1)
   INTO ln_parm_exists
   FROM utl_action_config_parm
   WHERE utl_action_config_parm.parm_name = 'ACTION_LOG_FAULT_AND_DEFER';
   
   -- Only Perform Migration Step if the Action Parm does not already exist
   IF (ln_parm_exists = 0) THEN
   
        -- Add the Action Config Parm  
        utl_migr_data_pkg.action_parm_insert(
                   'ACTION_LOG_FAULT_AND_DEFER',
                   'Permission to log fault and defer',
                   'TRUE/FALSE',
                   'FALSE',
                   1,
                   'Maint - Faults',
                   '5.1.1',
                   0,
                   0,
                   UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
        );
       
        -- Global Scope Default Value
        UPDATE utl_action_config_parm
        SET utl_action_config_parm.parm_value = 'TRUE'
        WHERE utl_action_config_parm.parm_name = 'ACTION_LOG_FAULT_AND_DEFER' AND
        EXISTS (
                -- Where ACTION_CREATE_UNASSIGNED_FAULT and 
                -- ACTION_DEFER_MEL_TASK OR ACTION_DEFER_MINOR_TASK is enabled
                SELECT 1
                  FROM UTL_ACTION_CONFIG_PARM
                 WHERE (
                          -- Global Value of ACTION_CREATE_UNASSIGNED_FAULT enabled
                          PARM_NAME = 'ACTION_CREATE_UNASSIGNED_FAULT'
                                      AND LOWER(PARM_VALUE) = 'true'
                       )
                       AND
                       (
                          EXISTS ( -- Global Value of ACTION_DEFER_MEL_TASK or ACTION_DEFER_MINOR_TASK enabled
                                  SELECT 1
                                    FROM UTL_ACTION_CONFIG_PARM
                                   WHERE PARM_NAME IN
                                         ('ACTION_DEFER_MEL_TASK', 'ACTION_DEFER_MINOR_TASK')
                                     AND LOWER(PARM_VALUE) = 'true' )
                        )
        );  
       
       -- Role Scope Default Value
       INSERT INTO utl_action_role_parm(role_id,parm_name,parm_value,session_auth_bool,utl_id)
        -- Where ACTION_CREATE_UNASSIGNED_FAULT and 
        -- ACTION_DEFER_MEL_TASK OR ACTION_DEFER_MINOR_TASK is enabled
        SELECT DISTINCT UTL_ACTION_ROLE_PARM.ROLE_ID,
               'ACTION_LOG_FAULT_AND_DEFER',
               'TRUE',
               UTL_ACTION_ROLE_PARM.session_auth_bool,
               UTL_ACTION_ROLE_PARM.utl_id
          FROM UTL_ACTION_ROLE_PARM
         WHERE (
                    EXISTS (-- Global Value of ACTION_CREATE_UNASSIGNED_FAULT enabled
                             SELECT 1
                               FROM UTL_ACTION_CONFIG_PARM
                              WHERE PARM_NAME = 'ACTION_CREATE_UNASSIGNED_FAULT'
                                AND LOWER(PARM_VALUE) = 'true') AND
                                
                             -- Role Value of ACTION_CREATE_UNASSIGNED_FAULT enabled
                   (UTL_ACTION_ROLE_PARM.PARM_NAME = 'ACTION_CREATE_UNASSIGNED_FAULT' AND
                   LOWER(UTL_ACTION_ROLE_PARM.PARM_VALUE) = 'true')
               )
               AND
               (           
          
                   (EXISTS (-- Global Value of ACTION_DEFER_MEL_TASK or ACTION_DEFER_MINOR_TASK enabled
                             SELECT 1
                               FROM UTL_ACTION_CONFIG_PARM
                              WHERE PARM_NAME IN
                                    ('ACTION_DEFER_MEL_TASK', 'ACTION_DEFER_MINOR_TASK')
                                AND LOWER(PARM_VALUE) = 'true')) OR
                   (EXISTS (-- Role Value of ACTION_DEFER_MEL_TASK or ACTION_DEFER_MINOR_TASK enabled
                            SELECT 1
                               FROM UTL_ACTION_ROLE_PARM SUB_ROLE_PARM
                              WHERE PARM_NAME IN
                                    ('ACTION_DEFER_MEL_TASK', 'ACTION_DEFER_MINOR_TASK')
                                AND SUB_ROLE_PARM.ROLE_ID = UTL_ACTION_ROLE_PARM.ROLE_ID AND
                              LOWER(SUB_ROLE_PARM.PARM_VALUE) = 'true'))
                )
                AND 
                (
                    EXISTS ( -- do not add any role parms if ACTION_LOG_FAULT_AND_DEFER is globally enabled already
                            SELECT 1
                               FROM UTL_ACTION_CONFIG_PARM
                              WHERE PARM_NAME = 'ACTION_LOG_FAULT_AND_DEFER' AND
                              LOWER(PARM_VALUE) = 'false')
                
                );
       
       
    END IF;
END;
/