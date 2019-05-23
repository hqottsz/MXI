--liquibase formatted sql


--changeSet MTX-2015:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace PACKAGE BODY "UTL_MIGR_DATA_PKG" IS 

   ----------------------------------------------------------------------------
   -- Object Name : utl_migr_data_pkg
   -- Object Type : Package Body
   -- Date        : Aug 13, 2010
   -- Coder       : David Sewell
   -- Recent Date : Jun 16, 2014
   -- Recent Coder: Wayne Yuke
   -- Description :
   -- This is the migration data package containing all generic methods and
   -- constants for data-specific version migrations.
   ----------------------------------------------------------------------------
   -- Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Public method bodies
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Procedure:   alert_type_delete
   -- Arguments:   p_alert_type  - alert type id
   -- Description: Deletes an alert type and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.alert_type_delete(
   --                 43
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE alert_type_delete(
               p_alert_type        IN utl_alert_type.alert_type_id%TYPE
      ) IS
   BEGIN
   
      DELETE FROM utl_alert_type_role WHERE alert_type_id = p_alert_type;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_alert_type_role table.');
      
      DELETE FROM utl_alert_log WHERE alert_id IN (select alert_id from utl_alert where alert_type_id = p_alert_type);
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_alert_log table.');
                                 
      DELETE FROM utl_alert_parm WHERE alert_id IN (select alert_id from utl_alert where alert_type_id = p_alert_type);
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_alert_parm table.');
      
      DELETE FROM utl_alert_status_log WHERE alert_id IN (select alert_id from utl_alert where alert_type_id = p_alert_type);
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_alert_status_log table.');
      
      DELETE FROM utl_user_alert WHERE alert_id IN (select alert_id from utl_alert where alert_type_id = p_alert_type);
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_user_alert table.');
      
      DELETE FROM utl_alert WHERE alert_type_id = p_alert_type;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_alert table.');
         
      DELETE FROM utl_alert_type WHERE alert_type_id = p_alert_type;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_alert_type table.');
         
   END alert_type_delete;

   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_copy
   -- Arguments:   p_parm_name
   --                 - parameter name
   --              p_parm_type
   --                 - parameter type
   --              p_config_type
   --                 - configuration type (GlOBAL or USER)
   --              p_migr_parm_name
   --                 - the parameter from which we are migrating
   -- Description: Migrates (copies) the value of a config parm to a new config parm.
   --              Both the migrating parm and the new config parm must exist.
   --              If the config type is user, the role and user parms are created.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_copy(
   --                 'HIDE_SCHED_LABOUR_HOURS', 
   --                 'SECURED_RESOURCE',
   --                 'USER',
   --                 'HIDE_SCHED_LABOUR_DAYS'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_copy(
               p_parm_name        IN utl_config_parm.parm_name%TYPE,
               p_parm_type        IN utl_config_parm.parm_type%TYPE,
               p_config_type      IN utl_config_parm.config_type%TYPE,
               p_migr_parm_name   IN utl_config_parm.parm_name%TYPE
      ) IS
      
      v_count NUMBER;
      
   BEGIN
   
      -- update the base config parm
      UPDATE
         UTL_CONFIG_PARM
      SET
         PARM_VALUE = (
            SELECT 
               NVL(old_parm.parm_value, default_value)
            FROM
               utl_config_parm old_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_parm.parm_name = p_migr_parm_name
            )
      WHERE
         parm_name = p_parm_name
         AND
         parm_type = p_parm_type
         AND
         EXISTS (
            SELECT 
               1
            FROM
               utl_config_parm old_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_parm.parm_name = p_migr_parm_name
         );
      
      DBMS_OUTPUT.PUT_LINE('INFO: Updated ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' in the utl_config_parm table.');
      
      -- if the config type is user, we should migrate the existing role and user data
      IF p_config_type = 'USER' THEN      
         INSERT INTO
            UTL_ROLE_PARM
            (
               ROLE_ID, PARM_NAME, PARM_TYPE, PARM_VALUE, UTL_ID
            )
            SELECT
               old_role_parm.role_id,
               p_parm_name,
               p_parm_type,
               old_role_parm.parm_value,
               old_role_parm.utl_id
            FROM
               utl_role_parm old_role_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_role_parm.parm_name = p_migr_parm_name
               AND
               NOT EXISTS ( SELECT 1 FROM utl_role_parm WHERE parm_name = p_parm_name AND role_id = old_role_parm.role_id );
      
         DBMS_OUTPUT.PUT_LINE('INFO: Inserted ' || SQL%ROWCOUNT || 
                              ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                              ' into the utl_role_parm table.');

         INSERT INTO
            UTL_USER_PARM
            (
               USER_ID, PARM_NAME, PARM_TYPE, PARM_VALUE, UTL_ID
            )
            SELECT
               old_user_parm.user_id,
               p_parm_name,
               p_parm_type,
               old_user_parm.parm_value,
               old_user_parm.utl_id
            FROM
               utl_user_parm old_user_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_user_parm.parm_name = p_migr_parm_name
               AND
               NOT EXISTS ( SELECT 1 FROM utl_user_parm WHERE parm_name = p_parm_name AND user_id = old_user_parm.user_id );
         
         DBMS_OUTPUT.PUT_LINE('INFO: Inserted ' || SQL%ROWCOUNT || 
                              ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                              ' into the utl_user_parm table.');
   
      END IF;
   
   END config_parm_copy;
   
   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_delete
   -- Arguments:   p_parm_name  - parameter name
   -- Description: Deletes a config parm and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'HIDE_SCHED_LABOUR_HOURS'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_delete(
               p_parm_name        IN utl_config_parm.parm_name%TYPE
      ) IS
   BEGIN
   
      DELETE FROM utl_user_parm WHERE parm_name = p_parm_name;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_user_parm table.');
      
      DELETE FROM utl_role_parm WHERE parm_name = p_parm_name;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_role_parm table.');
      
      DELETE FROM utl_config_parm WHERE parm_name = p_parm_name;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_config_parm table.');
         
   END config_parm_delete;

   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_insert
   -- Arguments:   p_parm_name   
   --                 - parameter name
   --              p_parm_type
   --                 - parameter type
   --              p_parm_desc
   --                 - parameter description
   --              p_config_type
   --                 - configuration type (GlOBAL or USER)
   --              p_allow_value_desc
   --                 - allowed values for the parameter
   --              p_default_value
   --                 - default value of the parameter
   --              p_mand_config_bool
   --                 - whether or not the parameter is mandatory
   --              p_category
   --                 - the parameter category
   --              p_modified_in
   --                 - the version in which the parm was modified
   --              p_utl_id
   --                 - the utl_id
   -- Description: Inserts new config parm into the utl_config_parm table or
   --              updates the table if the config parm already exists.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'HIDE_SCHED_LABOUR_HOURS', 
   --                 'SECURED_RESOURCE',
   --                 'Determines whether a user is allowed to view scheduled labour information.',
   --                 'USER',
   --                 'TRUE/FALSE',
   --                 'FALSE',
   --                 1,
   --                 'Task - Subtypes',
   --                 '7.5',
   --                 0
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_insert(
               p_parm_name        IN utl_config_parm.parm_name%TYPE,
               p_parm_type        IN utl_config_parm.parm_type%TYPE,
               p_parm_desc        IN utl_config_parm.parm_desc%TYPE,
               p_config_type      IN utl_config_parm.config_type%TYPE,
               p_allow_value_desc IN utl_config_parm.allow_value_desc%TYPE,
               p_default_value    IN utl_config_parm.default_value%TYPE,
               p_mand_config_bool IN utl_config_parm.mand_config_bool%TYPE,
               p_category         IN utl_config_parm.category%TYPE,
               p_modified_in      IN utl_config_parm.modified_in%TYPE,
               p_utl_id           IN utl_config_parm.utl_id%TYPE
      ) IS
      
      v_count NUMBER;
      
   BEGIN
   
      BEGIN
      
         -- insert the initial config parm
         INSERT INTO
            UTL_CONFIG_PARM
            (
               PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
            )
            VALUES
            (
               p_default_value, 
               p_parm_name, 
               p_parm_type,
               p_parm_desc,
               p_config_type,
               p_allow_value_desc,
               p_default_value,
               p_mand_config_bool,
               p_category,
               p_modified_in,
               p_utl_id
            );

         DBMS_OUTPUT.PUT_LINE('INFO: Inserted ' || SQL%ROWCOUNT || 
                              ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                              ' into the utl_config_parm table.');

      EXCEPTION
         WHEN DUP_VAL_ON_INDEX THEN

            -- the dup_val_on_index exception indicates that the config 
            -- parm already exists, therefore, update the record (the parm_value
            -- column is excluded from the update to keep customer settings)
            UPDATE
               UTL_CONFIG_PARM
            SET
               PARM_DESC = p_parm_desc,
               CONFIG_TYPE = p_config_type,
               ALLOW_VALUE_DESC = p_allow_value_desc,
               DEFAULT_VALUE = p_default_value,
               MAND_CONFIG_BOOL = p_mand_config_bool,
               CATEGORY = p_category,
               MODIFIED_IN = p_modified_in,
               UTL_ID = p_utl_id
            WHERE
               PARM_NAME = p_parm_name
               AND
               PARM_TYPE = p_parm_type;               

            DBMS_OUTPUT.PUT_LINE('INFO: Updated ' || SQL%ROWCOUNT || 
                                 ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                                 ' in the utl_config_parm table.');

      END;

   END config_parm_insert;
   
   ----------------------------------------------------------------------------
   -- Procedure:   action_parm_copy
   -- Arguments:   p_parm_name(utl_action_config_parm.parm_name%TYPE)       
   --                 - parameter name
   --              p_migr_parm_name
   --                 - the parameter from which we are migrating
   -- Description: Migrates (copies) the value of a config parm to a new config parm.
   --              Both the migrating parm and the new config parm must exist.
   --              The role and user parms are also created.
   -- SQL Usage:   exec utl_migr_data_pkg.action_parm_copy(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE action_parm_copy(
               p_parm_name        IN utl_action_config_parm.parm_name%TYPE,
               p_migr_parm_name   IN utl_action_config_parm.parm_name%TYPE
      ) IS
      
      v_count NUMBER;
      
   BEGIN
   
      -- update the base action parm
      UPDATE
         UTL_ACTION_CONFIG_PARM
      SET
         PARM_VALUE = (
            SELECT 
               NVL(old_parm.parm_value, default_value)
            FROM
               utl_action_config_parm old_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_parm.parm_name = p_migr_parm_name
            )
      WHERE
         parm_name = p_parm_name
         AND
         EXISTS (
            SELECT 
               1
            FROM
               utl_action_config_parm old_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_parm.parm_name = p_migr_parm_name
         );
      
      DBMS_OUTPUT.PUT_LINE('INFO: Updated ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' in the utl_action_config_parm table.');

      -- migrate the existing role and user data    
      INSERT INTO
         UTL_ACTION_ROLE_PARM
         (
            ROLE_ID, PARM_NAME, PARM_VALUE, SESSION_AUTH_BOOL, UTL_ID
         )
         SELECT
            old_role_parm.role_id,
            p_parm_name,
            old_role_parm.parm_value,
            old_role_parm.session_auth_bool,
            old_role_parm.utl_id
         FROM
            utl_action_role_parm old_role_parm
         WHERE
            p_migr_parm_name IS NOT NULL
            AND
            old_role_parm.parm_name = p_migr_parm_name
            AND
            NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE parm_name = p_parm_name AND role_id = old_role_parm.role_id );
      
      DBMS_OUTPUT.PUT_LINE('INFO: Inserted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' into the utl_action_role_parm table.');

      INSERT INTO
         UTL_ACTION_USER_PARM
         (
            USER_ID, PARM_NAME, PARM_VALUE, SESSION_AUTH_BOOL, UTL_ID
         )
         SELECT
            old_user_parm.user_id,
            p_parm_name,
            old_user_parm.parm_value,
            old_user_parm.session_auth_bool,
            old_user_parm.utl_id
         FROM
            utl_action_user_parm old_user_parm
         WHERE
            p_migr_parm_name IS NOT NULL
            AND
            old_user_parm.parm_name = p_migr_parm_name
            AND
            NOT EXISTS ( SELECT 1 FROM utl_action_user_parm WHERE parm_name = p_parm_name AND user_id = old_user_parm.user_id );
      
      DBMS_OUTPUT.PUT_LINE('INFO: Inserted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' into the utl_action_user_parm table.');
   
   END action_parm_copy;
   
      
   ----------------------------------------------------------------------------
   -- Procedure:   action_parm_delete
   -- Arguments:   p_parm_name  - parameter name
   -- Description: Deletes a action parm and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.action_parm_insert(
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE action_parm_delete(
               p_parm_name        IN utl_action_config_parm.parm_name%TYPE
      ) IS
   BEGIN
   
      DELETE FROM db_type_config_parm WHERE parm_name = p_parm_name;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the db_type_config_parm table.');
            
      DELETE FROM utl_action_user_parm WHERE parm_name = p_parm_name;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_action_user_parm table.');
      
      DELETE FROM utl_action_role_parm WHERE parm_name = p_parm_name;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_action_role_parm table.');

      DELETE FROM utl_todo_button WHERE PARM_NAME= p_parm_name;

      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_todo_button table.');   

      
      DELETE FROM utl_action_config_parm WHERE parm_name = p_parm_name;
      
      DBMS_OUTPUT.PUT_LINE('INFO: Deleted ' || SQL%ROWCOUNT || 
                           ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                           ' from the utl_action_config_parm table.');
         
   END action_parm_delete;
   
   ----------------------------------------------------------------------------
   -- Procedure:   action_parm_insert
   -- Arguments:   p_parm_name(utl_action_config_parm.parm_name%TYPE)       
   --                 - parameter name
   --              p_parm_desc
   --                 - parameter description
   --              p_allow_value_desc
   --                 - allowed values for the parameter
   --              p_default_value
   --                 - default value of the parameter
   --              p_mand_config_bool
   --                 - whether or not the parameter is mandatory
   --              p_category
   --                 - the parameter category
   --              p_modified_in
   --                 - the version in which the parm was modified
   --              p_session_auth_bool
   --                 - whether or not session authentication is enabled
   --              p_utl_id
   --                 - the utl_id
   --              p_db_types
   --                 - array of db typs to be inserted into db_type_config_parm
   -- Description: Inserts new action parm into the utl_action_config_parm table
   --              or updates the table if the action parm already exists.  Also
   --              inserts into the db_type_config_parm table if the records do 
   --              not already exist.
   -- SQL Usage:   exec utl_migr_data_pkg.action_parm_insert(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'Permission to edit non-historic certification or inspection labour roles.',
   --                 'TRUE/FALSE',
   --                 'FALSE',
   --                 1,
   --                 'Maint - Tasks',
   --                 '7.1-SP2',
   --                 0,
   --                 0,
   --                 UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE action_parm_insert(
               p_parm_name         IN utl_action_config_parm.parm_name%TYPE,
               p_parm_desc         IN utl_action_config_parm.parm_desc%TYPE,
               p_allow_value_desc  IN utl_action_config_parm.allow_value_desc%TYPE,
               p_default_value     IN utl_action_config_parm.default_value%TYPE,
               p_mand_config_bool  IN utl_action_config_parm.mand_config_bool%TYPE,
               p_category          IN utl_action_config_parm.category%TYPE,
               p_modified_in       IN utl_action_config_parm.modified_in%TYPE,
               p_session_auth_bool IN utl_action_config_parm.session_auth_bool%TYPE,
               p_utl_id            IN utl_action_config_parm.utl_id%TYPE,
               p_db_types          IN DbTypeCdList
      ) IS
      
      v_count NUMBER;
      
   BEGIN
   
      BEGIN
      
         -- insert the initial config parm
         INSERT INTO
            UTL_ACTION_CONFIG_PARM
            (
               PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, SESSION_AUTH_BOOL, UTL_ID
            )
            VALUES
            (
               p_default_value, 
               p_parm_name, 
               p_parm_desc,
               p_allow_value_desc,
               p_default_value,
               p_mand_config_bool,
               p_category,
               p_modified_in,
               p_session_auth_bool,
               p_utl_id
            );
         
         DBMS_OUTPUT.PUT_LINE('INFO: Inserted ' || SQL%ROWCOUNT || 
                              ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                              ' into the utl_action_config_parm table.');
      
      EXCEPTION
         WHEN DUP_VAL_ON_INDEX THEN
         
            -- the dup_val_on_index exception indicates that the config 
            -- parm already exists, therefore, update the record (the parm_value
            -- column is excluded from the update to keep customer settings)
            UPDATE
               UTL_ACTION_CONFIG_PARM
            SET
               PARM_DESC = p_parm_desc,
               ALLOW_VALUE_DESC = p_allow_value_desc,
               DEFAULT_VALUE = p_default_value,
               MAND_CONFIG_BOOL = p_mand_config_bool,
               CATEGORY = p_category,
               MODIFIED_IN = p_modified_in,
               SESSION_AUTH_BOOL = p_session_auth_bool,
               UTL_ID = p_utl_id
            WHERE
               PARM_NAME = p_parm_name;
      
            DBMS_OUTPUT.PUT_LINE('INFO: Updated ' || SQL%ROWCOUNT || 
                                 ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                                 ' in the utl_action_config_parm table.');
                                 
      END;
      
      -- this section adds a db_type_config_parm for each value in p_db_types
      FOR i IN p_db_types.FIRST..p_db_types.LAST LOOP

         INSERT INTO
            DB_TYPE_CONFIG_PARM 
            (
               PARM_NAME, DB_TYPE_CD
            )
            SELECT
               p_parm_name, p_db_types(i)
            FROM
               dual
            WHERE
               NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE parm_name = p_parm_name AND db_type_cd = p_db_types(i));
         
         DBMS_OUTPUT.PUT_LINE('INFO: Inserted ' || SQL%ROWCOUNT || 
                              ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                              ' into the db_type_config_parm table.');
         
      END LOOP;
   
   END action_parm_insert;

END UTL_MIGR_DATA_PKG;
/