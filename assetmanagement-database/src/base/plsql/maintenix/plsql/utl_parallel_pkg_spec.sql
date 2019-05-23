--liquibase formatted sql


--changeSet utl_parallel_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE UTL_PARALLEL_PKG IS
   ----------------------------------------------------------------------------
   -- Object Name : utl_parallel_pkg
   -- Object Type : Package Spec
   -- Description :
   -- This is the parallel package containing all generic methods and
   -- constants to enable parallel execution.
   ----------------------------------------------------------------------------
   -- Copyright 2013 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Global types
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Global Constants
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Public Methods
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Procedure:    set_enable_parallel_ddl
   -- Arguments:   p_enable_parallel_ddl(BOOLEAN)
   -- Description: set instance variable based on property upgrade.enable.parallel.ddl
   --              value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_enable_parallel_ddl(p_enable_parallel_ddl IN BOOLEAN);

   ----------------------------------------------------------------------------
   -- Procedure:    set_enable_parallel_dml
   -- Arguments:   p_upgrade_enable_parallel_dml(BOOLEAN)
   -- Description: set instance variable based on property upgrade.enable.parallel.dml
   --              value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_enable_parallel_dml(P_enable_parallel_dml IN BOOLEAN);

   ----------------------------------------------------------------------------
   -- Procedure:    set_enable_parallel_query
   -- Arguments:   p_enable_parallel_query(BOOLEAN)
   -- Description: set instance variable based on property upgrade.enable.parallel.query
   --              value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_enable_parallel_query(p_enable_parallel_query IN BOOLEAN);

   ----------------------------------------------------------------------------
   -- Procedure:    set_parallel_degree_policy
   -- Arguments:
   -- Description: set instance variable upgrade_parallel_degree_policy based on
   --              instance variables upgrade_enable_parallel_query,
   --              upgrade_enable_parallel_ddl and upgrade_enable_parallel_dml
   ----------------------------------------------------------------------------
   PROCEDURE set_parallel_degree_policy;

   ----------------------------------------------------------------------------
   -- Procedure:    set_parallel_degree_limit
   -- Arguments:
   -- Description: set instance variable upgrade_parallel_degree_limit based on
   --              property upgrade.parallel.degree.limit value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_parallel_degree_limit(p_parallel_degree_limit IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:    set_min_time_threshold
   -- Arguments:
   -- Description: set instance variable upgrade_min_time_threshold based on
   --              property upgrade.min.time.threshold value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_min_time_threshold(p_min_time_threshold IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Function:    get_enable_parallel_ddl
   -- Arguments:
   -- Return:      BOOLEAN
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_enable_parallel_ddl RETURN BOOLEAN;

   ----------------------------------------------------------------------------
   -- Function:    get_enable_parallel_dml
   -- Arguments:   p_upgrade_enable_parallel_dml(BOOLEAN)
   -- Return:      BOOLEAN
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_enable_parallel_dml RETURN BOOLEAN;

   ----------------------------------------------------------------------------
   -- Function:    get_enable_parallel_query
   -- Arguments:
   -- Return:      BOOLEAN
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_enable_parallel_query RETURN BOOLEAN;

   ----------------------------------------------------------------------------
   -- Function:    get_parallel_degree_policy
   -- Arguments:
   -- Return:      VARCHAR2
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_parallel_degree_policy RETURN VARCHAR2;

   ----------------------------------------------------------------------------
   -- Function:    get_parallel_degree_limit
   -- Arguments:
   -- Return:      VARCHAR2
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_parallel_degree_limit RETURN VARCHAR2;

   ----------------------------------------------------------------------------
   -- Function:    get_min_time_threshold
   -- Arguments:
   -- Return:      VARCHAR2
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_min_time_threshold RETURN VARCHAR2;

   ----------------------------------------------------------------------------
   -- Procedure:    set_session_to_enable_parallel
   -- Arguments:
   -- Description: alter session to enable parallel based on instance variables
   ----------------------------------------------------------------------------
   PROCEDURE initialize_parallel_settings;

   ----------------------------------------------------------------------------
   -- Procedure:    parallel_delete_begin
   -- Arguments:
   -- Return:      BOOLEAN
   -- Description: Sets up the session for a parallel delete on a given table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_delete_begin(p_table_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:    parallel_delete_end
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_validate(BOOLEAN)             - validate constraints
   -- Description: Tears down the session from a parallel delete on a given
   --              table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_delete_end(p_table_name IN VARCHAR2,
                                 p_validate   IN BOOLEAN);

   ----------------------------------------------------------------------------
   -- Procedure:    parallel_update_begin
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   -- Description: Sets up the session for a parallel update on a given table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_update_begin(p_table_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:    parallel_update_end
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_validate(BOOLEAN)             - validate constraints
   -- Description: Tears down the session from a parallel update on a given
   --              table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_update_end(p_table_name IN VARCHAR2,
                                 p_validate   IN BOOLEAN);

   ----------------------------------------------------------------------------
   -- Procedure:    parallel_insert_begin
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   -- Description: Sets up the session for a parallel insert on a given table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_insert_begin(p_table_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:    parallel_insert_end
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_validate(BOOLEAN)             - validate constraints
   -- Description: Tears down the session from a parallel insert on a given
   --              table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_insert_end(p_table_name IN VARCHAR2,
                                 p_validate   IN BOOLEAN);


END UTL_PARALLEL_PKG;
/