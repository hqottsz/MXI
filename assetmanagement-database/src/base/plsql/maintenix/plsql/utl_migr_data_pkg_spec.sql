--liquibase formatted sql


--changeSet utl_migr_data_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace PACKAGE "UTL_MIGR_DATA_PKG" IS

   ----------------------------------------------------------------------------
   -- Object Name : utl_migr_data_pkg
   -- Object Type : Package Body
   -- Date        : Aug 13, 2010
   -- Coder       : David Sewell
   -- Recent Date : Oct 19, 2012
   -- Recent Coder: Mark Rutherford
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
      );

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
      );

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
      );


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
      );

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
      );

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
   --                 0
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
               p_utl_id            IN utl_action_config_parm.utl_id%TYPE
      );


END UTL_MIGR_DATA_PKG;
/