--liquibase formatted sql


--changeSet MX-24207:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace PACKAGE "UTL_MIGR_DATA_PKG" IS 

   ----------------------------------------------------------------------------
   -- Object Name : utl_migr_data_pkg
   -- Object Type : Package Body
   -- Date        : Aug 13, 2010
   -- Coder       : David Sewell
   -- Recent Date :
   -- Recent Coder:
   -- Description :
   -- This is the migration data package containing all generic methods and
   -- constants for data-specific version migrations.
   ----------------------------------------------------------------------------
   -- Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   --
   ----------------------------------------------------------------------------
   -- Global types
   ----------------------------------------------------------------------------
   TYPE DbTypeCdList IS VARRAY(7) OF VARCHAR2(8);
   
   
   ----------------------------------------------------------------------------
   -- Global Constants
   ----------------------------------------------------------------------------
   --
   --
   ----------------------------------------------------------------------------
   -- Public Methods
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
      );
   
   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_copy
   -- Arguments:   p_parm_name(utl_config_parm.parm_name%TYPE)       
   --                 - parameter name
   --              p_parm_type
   --                 - parameter type
   --              p_config_type
   --                 - configuration type (GlOBAL or USER)
   --              p_migr_parm_name
   --                 - the parameter from which we are migrating
   -- Description: Creates and if needed migrates a new config parm.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_copy(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'SECURED_RESOURCE',
   --                 'USER',
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_copy(
               p_parm_name        IN utl_config_parm.parm_name%TYPE,
               p_parm_type        IN utl_config_parm.parm_type%TYPE,
               p_config_type      IN utl_config_parm.config_type%TYPE,
               p_migr_parm_name   IN utl_config_parm.parm_name%TYPE
      );
      
   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_delete
   -- Arguments:   p_parm_name  - parameter name
   -- Description: Deletes a config parm and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_delete(
               p_parm_name        IN utl_config_parm.parm_name%TYPE
      );
   
   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_insert
   -- Arguments:   p_parm_name(utl_config_parm.parm_name%TYPE)       
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
   --              p_db_types
   --                 - array of db typs to be inserted into db_type_config_parm
   -- Description: Creates and if needed migrates a new config parm.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'SECURED_RESOURCE',
   --                 'Permission to edit non-historic certification or inspection labour roles.',
   --                 'USER',
   --                 'TRUE/FALSE',
   --                 'FALSE',
   --                 1,
   --                 'Maint - Tasks',
   --                 '7.1-SP2',
   --                 0,
   --                 UTL_MIGR_DATA_PKG.DdTypeCdList('OPER','MASTER')
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
               p_utl_id           IN utl_config_parm.utl_id%TYPE,
               p_db_types         IN DbTypeCdList
      );
   
   
END UTL_MIGR_DATA_PKG;
/

--changeSet MX-24207:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace PACKAGE BODY "UTL_MIGR_DATA_PKG" IS 

   ----------------------------------------------------------------------------
   -- Object Name : utl_migr_data_pkg
   -- Object Type : Package Body
   -- Date        : Aug 13, 2010
   -- Coder       : David Sewell
   -- Recent Date :
   -- Recent Coder:
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
      
      DELETE FROM utl_alert_log WHERE alert_id = p_alert_type;
      DELETE FROM utl_alert_parm WHERE alert_id = p_alert_type;
      DELETE FROM utl_alert_status_log WHERE alert_id = p_alert_type;
      DELETE FROM utl_user_alert WHERE alert_id = p_alert_type;
      DELETE FROM utl_alert WHERE alert_type_id = p_alert_type;
   
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
   -- Description: Creates and if needed migrates a new config parm.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_copy(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'SECURED_RESOURCE',
   --                 'USER',
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
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
   
      END IF;
   
   END config_parm_copy;
   
      ----------------------------------------------------------------------------
   -- Procedure:   config_parm_delete
   -- Arguments:   p_parm_name  - parameter name
   -- Description: Deletes a config parm and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_delete(
               p_parm_name        IN utl_config_parm.parm_name%TYPE
      ) IS
   BEGIN
   
      DELETE FROM db_type_config_parm WHERE parm_name = p_parm_name;
      
      DELETE FROM utl_user_parm WHERE parm_name = p_parm_name;
      DELETE FROM utl_role_parm WHERE parm_name = p_parm_name;
      DELETE FROM utl_config_parm WHERE parm_name = p_parm_name;
   
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
   --              p_migr_parm_name
   --                 - the parameter from which we are migrating
   --              p_db_types
   --                 - array of db typs to be inserted into db_type_config_parm
   -- Description: Creates and if needed migrates a new config parm.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'SECURED_RESOURCE',
   --                 'Permission to edit non-historic certification or inspection labour roles.',
   --                 'USER',
   --                 'TRUE/FALSE',
   --                 'FALSE',
   --                 1,
   --                 'Maint - Tasks',
   --                 '7.1-SP2',
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
               p_utl_id           IN utl_config_parm.utl_id%TYPE,
               p_db_types         IN DbTypeCdList
      ) IS
      
      v_count NUMBER;
      
   BEGIN
   
      -- insert the initial config parm
      INSERT INTO
         UTL_CONFIG_PARM
         (
            PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
         )
         SELECT 
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
         FROM
            dual
         WHERE
            NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = p_parm_name AND parm_type = p_parm_type );
            
      -- this section adds a db_type_config_parm for each value in p_db_types
      IF p_parm_type = 'SECURED_RESOURCE' THEN
         FOR i IN p_db_types.FIRST..p_db_types.LAST LOOP

            INSERT INTO
               DB_TYPE_CONFIG_PARM 
               (
                  PARM_NAME, DB_TYPE_CD, PARM_TYPE
               )
               SELECT
                  p_parm_name, p_db_types(i), p_parm_type
               FROM
                  dual
               WHERE
                  NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE parm_name = p_parm_name AND db_type_cd = p_db_types(i) AND parm_type = p_parm_type);
         END LOOP;
      END IF;
   
   END config_parm_insert;
   
END UTL_MIGR_DATA_PKG;
/

--changeSet MX-24207:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_delete('TOBE_INST_BATCH_INSUFFICIENT_QTY');
END;
/

--changeSet MX-24207:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.alert_type_delete(43);
END;
/