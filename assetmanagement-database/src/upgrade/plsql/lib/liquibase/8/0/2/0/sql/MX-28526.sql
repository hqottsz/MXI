--liquibase formatted sql


--changeSet MX-28526:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet MX-28526:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY UTL_PARALLEL_PKG IS
   ----------------------------------------------------------------------------
   -- Object Name : utl_parallel_pkg
   -- Object Type : Package Body
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
   -- instance variables
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- indicate whether or not enable DDL operation to work in parallel
   upgrade_enable_parallel_ddl BOOLEAN := FALSE;
   -- indicate whether or not enable DML operation to work in parallel
   upgrade_enable_parallel_dml BOOLEAN := FALSE;
   -- indicate whether or not enable Query to work in parallel
   upgrade_enable_parallel_query BOOLEAN := FALSE;
   -- indicate whether or not automatic degree of Parallelism, statement queuing,
   -- and in-memory parallel execution will be enabled.
   upgrade_parallel_degree_policy VARCHAR2(40) := 'MANUAL';
   -- indicate the limit of degree of parallelism used to ensure parallel server
   -- process do not flood the system
   upgrade_parallel_degree_limit VARCHAR2(20);
   -- indicate the minimum execution time a statement should have before the
   -- statement is considered for automatic degree of parallelism
   upgrade_min_time_threshold VARCHAR2(20);


   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Private variables
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   c_pkg_name CONSTANT VARCHAR2(30) := 'utl_parallel_pkg';

   -- Boolean constants
   c_true  CONSTANT NUMBER(1) := 1;
   c_false CONSTANT NUMBER(1) := 0;

   v_err_msg     VARCHAR2(2000);
   v_err_code    VARCHAR2(200);
   v_method_name VARCHAR2(30);

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Private Method Bodies
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Procedure:    disable_parallel_dml
   -- Description: Disables parallel dml in the session
   ----------------------------------------------------------------------------
   PROCEDURE disable_parallel_dml IS

      v_step                NUMBER(4);

   BEGIN

      -- Enable parallel dml, must commit before
      v_step := 10;
      COMMIT;
      EXECUTE IMMEDIATE 'ALTER SESSION DISABLE PARALLEL DML';

      -- Notify the user the session has been changed to disable parallel DML
      dbms_output.put_line('INFO: SESSION has been changed to disable DML
                            execution in parallel mode ');

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'disable_parallel_dml';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END disable_parallel_dml;


   ----------------------------------------------------------------------------
   -- Procedure:    enable_parallel_dml
   -- Description: Enables parallel dml in the session
   ----------------------------------------------------------------------------
   PROCEDURE enable_parallel_dml IS

      v_step                NUMBER(4);

   BEGIN

      -- Enable parallel dml, must commit before
      v_step := 10;
      COMMIT;
      EXECUTE IMMEDIATE 'ALTER SESSION ENABLE PARALLEL DML';

      -- Notify the user the session has been changed to disable parallel DML
      dbms_output.put_line('INFO: SESSION has been changed to enable DML
                            execution in parallel mode ');


   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'enable_parallel_dml';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END enable_parallel_dml;


   ----------------------------------------------------------------------------
   -- Procedure:    disable_all_triggers
   -- Arguments:   p_table_name(varchar2)         - name of table
   -- Description: Disables all triggers on a given table
   ----------------------------------------------------------------------------
   PROCEDURE disable_all_triggers(p_table_name IN VARCHAR2) IS

      v_step                NUMBER(4);
      v_trigger_count       NUMBER(4);

   BEGIN

      -- Disable all triggers on given table
      v_step := 10;
      EXECUTE IMMEDIATE 'ALTER TABLE ' || p_table_name || ' DISABLE ALL TRIGGERS';

      SELECT COUNT(*)
      INTO  v_trigger_count
        FROM user_triggers
       WHERE table_name = upper(p_table_name)
         AND status = 'DISABLED';

      IF v_trigger_count = 0
      THEN
         -- Notify the user that no triggers were altered disabled.
         dbms_output.put_line('INFO: No triggers were altered because they are already set to disabled'
                              ||' for table '||p_table_name||'.');
      ELSE
         -- Notify the user that how many triggers were altered disabled.
         dbms_output.put_line('INFO: ' || v_trigger_count|| ' triggers have been altered to be disabled'
                              ||' for table '||p_table_name||'.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'disable_all_triggers';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END disable_all_triggers;


   ----------------------------------------------------------------------------
   -- Procedure:    enable_all_triggers
   -- Arguments:   p_table_name(varchar2)         - name of table
   -- Description: Enables all triggers on a given table
   ----------------------------------------------------------------------------
   PROCEDURE enable_all_triggers(p_table_name IN VARCHAR2) IS

      v_step                NUMBER(4);
      v_trigger_count       NUMBER(4);

   BEGIN

      -- Disable all triggers on given table
      v_step := 10;
      EXECUTE IMMEDIATE 'ALTER TABLE ' || p_table_name || ' ENABLE ALL TRIGGERS';
      SELECT COUNT(*)
      INTO  v_trigger_count
        FROM user_triggers
       WHERE table_name = upper(p_table_name)
         AND status = 'ENABLED';

      IF v_trigger_count = 0
      THEN
         -- Notify the user that no triggers were altered enabled.
         dbms_output.put_line('INFO: No triggers were altered because they are already set to enabled');
      ELSE
         -- Notify the user that how many triggers were altered enabled.
         dbms_output.put_line('INFO: ' || v_trigger_count|| ' triggers have been altered to be enabled. ');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'enable_all_triggers';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END enable_all_triggers;


   ----------------------------------------------------------------------------
   -- Procedure:    disable_all_constraints
   -- Arguments:   p_table_name(varchar2)         - name of table
   --              p_constraint_type(varchar2)    - constraint type R/C
   -- Description: Disables all constraints on given table and constraint type
   ----------------------------------------------------------------------------
   PROCEDURE disable_all_constraints(p_table_name      IN VARCHAR2,
                                     p_constraint_type IN VARCHAR2) IS

      v_step                NUMBER(4);
      v_constraint_count    NUMBER(4) := 0;

   BEGIN

      -- Loop through all constraints on the given table and constraint type
      v_step := 10;
      FOR lConstraint IN (
         SELECT
            user_constraints.table_name,
            user_constraints.constraint_name
         FROM
            user_constraints
         WHERE
            user_constraints.table_name = upper(p_table_name)
            AND
            user_constraints.constraint_type = upper(p_constraint_type)
            AND
            user_constraints.status = 'ENABLED'
      ) LOOP
         EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' DISABLE CONSTRAINT ' || lConstraint.constraint_name;
         -- Increment the counters for constraints that have been disabled
         v_constraint_count := v_constraint_count + 1;
      END LOOP;

      IF v_constraint_count = 0
      THEN
         -- Notify user no constraints have been disabled
         dbms_output.put_line('INFO: NO '|| upper(p_constraint_type) ||' type of constraints have been disabled '
                                  ||'for table '|| p_table_name ||'.');
      ELSE
         -- Notify user how many constraints have been disabled
         dbms_output.put_line('INFO: ' || v_constraint_count ||' ' ||upper(p_constraint_type) ||
                              ' type of constraints have been disabled '||'for table '||p_table_name||'.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'disable_all_constraints';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END disable_all_constraints;


   ----------------------------------------------------------------------------
   -- Procedure:    enable_all_constraints
   -- Arguments:   p_table_name(varchar2)         - name of table
   --              p_constraint_type(varchar2)    - constraint type R/C
   --              p_validate(boolean)             - validate constraints
   -- Description: Enables all constraints on given table and constraint type
   ----------------------------------------------------------------------------
   PROCEDURE enable_all_constraints(p_table_name      IN VARCHAR2,
                                    p_constraint_type IN VARCHAR2,
                                    p_validate        IN BOOLEAN) IS

      v_step                NUMBER(4);
      v_constraint_count    NUMBER(4) := 0;
      v_constraint_validated_count NUMBER(4) := 0;

   BEGIN

      -- Loop through all constraints on the given table and constraint type
      v_step := 10;
      FOR lConstraint IN (
         SELECT
            user_constraints.table_name,
            user_constraints.constraint_name
         FROM
            user_constraints
         WHERE
            user_constraints.table_name = upper(p_table_name)
            AND
            user_constraints.constraint_type = upper(p_constraint_type)
            AND
            user_constraints.status = 'DISABLED'
      ) LOOP

         -- Enable the constraint with novalidate
         EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' ENABLE NOVALIDATE CONSTRAINT ' || lConstraint.constraint_name;
         v_constraint_count := v_constraint_count + 1;

         -- Turn on parallel for the table and validate the constraint
         IF p_validate = TRUE THEN
            EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' PARALLEL';
            EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' MODIFY CONSTRAINT ' || lConstraint.constraint_name || ' VALIDATE';
            EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' NOPARALLEL';
            
            v_constraint_validated_count := v_constraint_validated_count + 1;

         END IF;

      END LOOP;
      
      IF v_constraint_count = 0
      THEN
         -- Notify the user that no constraints have been enabled
         dbms_output.put_line('INFO: NO '|| upper(p_constraint_type) ||' type of constraints have been enabled '
                                         ||'for table '|| p_table_name ||'.');
      ELSE
         -- Notify the user that the how many constraints have been enabled
         dbms_output.put_line('INFO: ' || v_constraint_count ||' ' ||upper(p_constraint_type) ||
                              ' type of constraints have been enabled without being validated '||'for table '||p_table_name||'.');
      END IF;


      IF v_constraint_validated_count = 0
      THEN
         -- Notify the user that no constraints have been validated
         dbms_output.put_line('INFO: NO '|| upper(p_constraint_type) ||' type of constraints have been validated '
                                         ||'for table '|| p_table_name || '.');
      ELSE
         -- Notify the user that how many constraints have been validated
         dbms_output.put_line('INFO: ' || v_constraint_validated_count || ' ' ||upper(p_constraint_type)
                                       ||' type of constraints have been validated '
                                       ||'for table '||p_table_name||'.');
      END IF;   
         
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'enable_all_constraints';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END enable_all_constraints;


   ----------------------------------------------------------------------------
   -- Procedure:    disable_self_ref_constraints
   -- Arguments:   p_table_name(varchar2)         - name of table
   -- Description: Disables all self-referential constraints on given table
   ----------------------------------------------------------------------------
   PROCEDURE disable_self_ref_constraints(p_table_name IN VARCHAR2) IS

      v_step                NUMBER(4);
      v_constraint_count    NUMBER(4) := 0;

   BEGIN

      -- Loop through all constraints on the given table and constraint type
      v_step := 10;
      FOR lConstraint IN (
         SELECT
            user_constraints.table_name,
            user_constraints.constraint_name
         FROM
            user_constraints r_constraint
            INNER JOIN user_constraints ON
               user_constraints.r_constraint_name = r_constraint.constraint_name
         WHERE
            r_constraint.table_name = upper(p_table_name)
            AND
            r_constraint.constraint_type = 'P'
            AND
            r_constraint.table_name = user_constraints.table_name
            AND
            user_constraints.status = 'ENABLED'
      ) LOOP
         EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' DISABLE CONSTRAINT ' || lConstraint.constraint_name;
         -- Increment the counters for constraints that have been disabled
         v_constraint_count := v_constraint_count + 1;
      END LOOP;


      IF v_constraint_count = 0
      THEN
         -- Notify user no constraints have been disabled
         dbms_output.put_line('INFO: NO self-referential constraints have been disabled '
                                  ||'for table '|| p_table_name ||'.');
      ELSE
         -- Notify user how many constraints have been disabled
         dbms_output.put_line('INFO: ' || v_constraint_count || ' self-referential constraints have been disabled '
                                       ||'for table '||p_table_name||'.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'disable_self_ref_constraints';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END disable_self_ref_constraints;


    ----------------------------------------------------------------------------
   -- Procedure:    enable_self_ref_constraints
   -- Arguments:   p_table_name(varchar2)         - name of table
   --              p_validate(boolean)            - validate constraints
   -- Description: Enables all self-referential constraints on given table
   ----------------------------------------------------------------------------
   PROCEDURE enable_self_ref_constraints(p_table_name IN VARCHAR2,
                                         p_validate   IN BOOLEAN) IS

      v_step                NUMBER(4);
      v_constraint_count    NUMBER(4) := 0;
      v_constraint_validated_count NUMBER(4) := 0;

   BEGIN

      -- Loop through all constraints on the given table and constraint type
      v_step := 10;
      FOR lConstraint IN (
         SELECT
            user_constraints.table_name,
            user_constraints.constraint_name
         FROM
            user_constraints r_constraint
            INNER JOIN user_constraints ON
               user_constraints.r_constraint_name = r_constraint.constraint_name
         WHERE
            r_constraint.table_name = upper(p_table_name)
            AND
            r_constraint.constraint_type = 'P'
            AND
            r_constraint.table_name = user_constraints.table_name
            AND
            user_constraints.status = 'DISABLED'
      ) LOOP

         -- Enable the constraint with novalidate
         EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' ENABLE NOVALIDATE CONSTRAINT ' || lConstraint.constraint_name;
         
         --Increment the counter for constraints being enabled without being validated
         v_constraint_count := v_constraint_count + 1;

         -- Turn on parallel for the table and validate the constraint
         IF p_validate = TRUE THEN
            EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' PARALLEL';
            EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' MODIFY CONSTRAINT ' || lConstraint.constraint_name || ' VALIDATE';
            EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' NOPARALLEL';

            --Increment the counter for constraints being validated
            v_constraint_validated_count := v_constraint_validated_count + 1;
            
         END IF;

      END LOOP;
      
      IF v_constraint_count = 0
      THEN
      -- Notify the user that no constraints have been enabled
        dbms_output.put_line('INFO: NO self-referential constraints have been enabled '
                              ||'for table '|| p_table_name ||'.');
      ELSE
      -- Notify the user that the how many self-referential constraints have been enabled
        dbms_output.put_line('INFO: ' || v_constraint_count ||' self-referential constraints have been enabled
                                      without being validated '||'for table '||p_table_name||'.');
     END IF;

     IF v_constraint_validated_count = 0
     THEN
        -- Notify the user that no constraints have been validated
        dbms_output.put_line('INFO: NO self-referential constraints have been validated '
                                        ||'for table '|| p_table_name ||'.');
     ELSE
        -- Notify the user that how many self-referential constraints have been validated
        dbms_output.put_line('INFO: ' || v_constraint_validated_count || ' self-referential constraints have been validated '
                                      ||'for table '||p_table_name||'.');
     END IF;


   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'enable_self_ref_constraints';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END enable_self_ref_constraints;


   ----------------------------------------------------------------------------
   -- Procedure:    disable_r_constraints
   -- Arguments:   p_table_name(varchar2)         - name of table
   -- Description: Disables all referential constraints on other tables that
   --              reference a given table
   ----------------------------------------------------------------------------
   PROCEDURE disable_r_constraints(p_table_name IN VARCHAR2) IS

      v_step                NUMBER(4);
      v_constraint_count    NUMBER(4) := 0;

   BEGIN

      -- Loop through all R constraints on other tables that reference this table
      v_step := 10;
      FOR lConstraint IN (
         SELECT
            user_constraints.table_name,
            user_constraints.constraint_name,
            user_constraints.status
         FROM
            user_constraints r_constraint
            INNER JOIN user_constraints ON
               user_constraints.r_constraint_name = r_constraint.constraint_name
         WHERE
            r_constraint.table_name = upper(p_table_name)
            AND
            r_constraint.constraint_type = 'P'
            AND
            user_constraints.status = 'ENABLED'
      ) LOOP
         EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' DISABLE CONSTRAINT ' || lConstraint.constraint_name;
         -- Increment counters for constraints being disabled
         v_constraint_count := v_constraint_count + 1;
      END LOOP;


      IF v_constraint_count = 0
      THEN
         -- Notify user no constraints have been disabled
         dbms_output.put_line('INFO: NO referential constraints have been disabled '
                                  ||'for table '|| p_table_name ||'.');
      ELSE
         -- Notify user how many constraints have been disabled
         dbms_output.put_line('INFO: ' || v_constraint_count || ' referential constraints have been disabled '
                                       ||'for table '||p_table_name||'.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'disable_r_constraints';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END disable_r_constraints;


   ----------------------------------------------------------------------------
   -- Procedure:    enable_r_constraints
   -- Arguments:   p_table_name(varchar2)         - name of table
   --              p_validate(boolean)            - validate constraints
   -- Description: Enables all referential constraints on other tables that
   --              reference a given table
   ----------------------------------------------------------------------------
   PROCEDURE enable_r_constraints(p_table_name IN VARCHAR2,
                                  p_validate   IN BOOLEAN) IS

      v_step                NUMBER(4);
      v_constraint_count    NUMBER(4) := 0;
      v_constraint_validated_count NUMBER(4) := 0;

   BEGIN

      -- Loop through all R constraints on other tables that reference this table
      v_step := 10;
      FOR lConstraint IN (
         SELECT
            user_constraints.table_name,
            user_constraints.constraint_name
         FROM
            user_constraints r_constraint
            INNER JOIN user_constraints ON
               user_constraints.r_constraint_name = r_constraint.constraint_name
         WHERE
            r_constraint.table_name = upper(p_table_name)
            AND
            r_constraint.constraint_type = 'P'
            AND
            user_constraints.status = 'DISABLED'
      ) LOOP

         -- Enable the constraint with novalidate
         EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' ENABLE NOVALIDATE CONSTRAINT ' || lConstraint.constraint_name;

         -- Increment the counter for constraints being enabled without validated
         v_constraint_count := v_constraint_count + 1;
         
          -- Turn on parallel for the table and validate the constraint
          IF p_validate = TRUE 
          THEN
             EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' PARALLEL';
             EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' MODIFY CONSTRAINT ' || lConstraint.constraint_name || ' VALIDATE';
             EXECUTE IMMEDIATE 'ALTER TABLE ' || lConstraint.table_name || ' NOPARALLEL';

             -- Increment the counter for constraints being validated
             v_constraint_validated_count := v_constraint_validated_count + 1;
          END IF;

      END LOOP;

      IF v_constraint_count = 0
      THEN
         -- Notify the user that no constraints have been enabled
         dbms_output.put_line('INFO: NO referential constraints have been enabled '
                               ||'for table '|| p_table_name ||'.');
       ELSE
          -- Notify the user that the how many self-referential constraints have been enabled
          dbms_output.put_line('INFO: ' || v_constraint_count ||' referential constraints have been enabled '
                                       ||'without being validated '||'for table '||p_table_name||'.');
       END IF;

       IF v_constraint_validated_count = 0
       THEN
          -- Notify the user that no constraints have been validated
          dbms_output.put_line('INFO: NO referential constraints have been validated '
                                     ||'for table '|| p_table_name||'.');
        ELSE
           -- Notify the user that how many constraints have been validated
           dbms_output.put_line('INFO: ' || v_constraint_validated_count || ' referential constraints have been validated '
                                         ||'for table '||p_table_name||'.');
        END IF;
           
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'enable_r_constraints';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END enable_r_constraints;


   ----------------------------------------------------------------------------
   -- Procedure:    log_parallel_data
   -- Arguments:   p_statistic(VARCHAR2)     - Name of the statistic
   -- Arguments:   p_log_last_query(BOOLEAN) - Flag indicating whether to log result of last query.
   --                                          - procedures run before DML should pass FALSE.
   --                                          - procedures run after DML should pass TRUE.
   -- Description: Logs parallel DML counts for the session
   ----------------------------------------------------------------------------
   PROCEDURE log_parallel_data (p_statistic IN VARCHAR2, p_log_last_query IN BOOLEAN) IS

      v_step                NUMBER(4);
      v_last_query          NUMBER;
      v_session_total       NUMBER;

   BEGIN

      -- Gather DML Parallelized count for the session
      v_step := 10;
      SELECT
         last_query, session_total
      INTO
         v_last_query, v_session_total
      FROM
         v$pq_sesstat
      WHERE
         statistic = p_statistic;

      IF p_log_last_query = FALSE THEN
         dbms_output.put_line('INFO: [PARALLEL:BEGIN] Session = ' || v_session_total || ' - ' || p_statistic);
      ELSE
         dbms_output.put_line('INFO: [PARALLEL:END] Last = ' || v_last_query || ', Session = ' || v_session_total || ' - ' || p_statistic);
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'log_parallel_data';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END log_parallel_data;


   ----------------------------------------------------------------------------
   -- Procedure:    log_parallel
   -- Arguments:   p_log_last_query(BOOLEAN) - Flag indicating whether to log result of last query.
   --                                          - procedures run before DML should pass FALSE.
   --                                          - procedures run after DML should pass TRUE.
   -- Description: Logs parallel DML counts for the session
   ----------------------------------------------------------------------------
   PROCEDURE log_parallel (p_log_last_query IN BOOLEAN) IS

      v_step                NUMBER(4);

   BEGIN

      -- Log all of the parallel statistics that we're interested in
      log_parallel_data('Queries Parallelized', p_log_last_query);
      log_parallel_data('DML Parallelized', p_log_last_query);
      log_parallel_data('DDL Parallelized', p_log_last_query);
      log_parallel_data('Server Threads', p_log_last_query);
      log_parallel_data('Allocation Height', p_log_last_query);
      log_parallel_data('Allocation Width', p_log_last_query);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'log_parallel';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END log_parallel;


   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Public method bodies
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Procedure:    set_enable_parallel_ddl
   -- Arguments:   p_enable_parallel_ddl(BOOLEAN)
   -- Description: set instance variable based on property upgrade.enable.parallel.ddl
   --              value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_enable_parallel_ddl(p_enable_parallel_ddl IN BOOLEAN) IS
   BEGIN
      upgrade_enable_parallel_ddl := p_enable_parallel_ddl;
   END set_enable_parallel_ddl;

   ----------------------------------------------------------------------------
   -- Procedure:    set_enable_parallel_dml
   -- Arguments:   p_upgrade_enable_parallel_dml(BOOLEAN)
   -- Description: set instance variable based on property upgrade.enable.parallel.dml
   --              value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_enable_parallel_dml(P_enable_parallel_dml IN BOOLEAN) IS
   BEGIN
      upgrade_enable_parallel_dml := p_enable_parallel_dml;
   END set_enable_parallel_dml;

   ----------------------------------------------------------------------------
   -- Procedure:    set_enable_parallel_query
   -- Arguments:   p_enable_parallel_query(BOOLEAN)
   -- Description: set instance variable based on property upgrade.enable.parallel.query
   --              value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_enable_parallel_query(p_enable_parallel_query IN BOOLEAN) IS
   BEGIN
      upgrade_enable_parallel_query := p_enable_parallel_query;
   END set_enable_parallel_query;

   ----------------------------------------------------------------------------
   -- Procedure:    set_parallel_degree_policy
   -- Arguments:
   -- Description: set instance variable upgrade_parallel_degree_policy based on
   --              instance variables upgrade_enable_parallel_query,
   --              upgrade_enable_parallel_ddl and upgrade_enable_parallel_dml
   ----------------------------------------------------------------------------
   PROCEDURE set_parallel_degree_policy IS
   BEGIN
      IF (upgrade_enable_parallel_ddl = TRUE or
          upgrade_enable_parallel_dml = TRUE or
          upgrade_enable_parallel_query = TRUE)
      THEN
         upgrade_parallel_degree_policy := 'AUTO';
      END IF;
   END set_parallel_degree_policy;

   ----------------------------------------------------------------------------
   -- Procedure:    set_parallel_degree_limit
   -- Arguments:
   -- Description: set instance variable upgrade_parallel_degree_limit based on
   --              property upgrade.parallel.degree.limit value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_parallel_degree_limit(p_parallel_degree_limit IN VARCHAR2) IS
   BEGIN
      IF trim(p_parallel_degree_limit) = 'CPU' OR
         trim(p_parallel_degree_limit) = 'IO' OR
         REGEXP_LIKE(trim(p_parallel_degree_limit), '^[[:digit:]]+$')
      THEN
         upgrade_parallel_degree_limit := trim(p_parallel_degree_limit);
      ELSE
         -- Use default value CPU
         upgrade_parallel_degree_limit := 'CPU';
         dbms_output.put_line('INFO: the entry for this property is not valid,
                                     thus, CPU is used');


      END IF;


   END set_parallel_degree_limit;

   ----------------------------------------------------------------------------
   -- Procedure:    set_min_time_threshold
   -- Arguments:
   -- Description: set instance variable upgrade_min_time_threshold based on
   --              property upgrade.min.time.threshold value in build.properties
   ----------------------------------------------------------------------------
   PROCEDURE set_min_time_threshold(p_min_time_threshold IN VARCHAR2) IS
   BEGIN
      IF trim(p_min_time_threshold) = 'AUTO' OR
         REGEXP_LIKE(trim(p_min_time_threshold), '^[[:digit:]]+$')
      THEN
         upgrade_min_time_threshold := trim(p_min_time_threshold);
      ELSE
         -- Use default value 1
         upgrade_min_time_threshold := 1;
         dbms_output.put_line('INFO: the entry for this property is not valid,
                                     thus, 1 is used');
      END IF;
   END set_min_time_threshold;
   ----------------------------------------------------------------------------
   -- Function:    get_enable_parallel_ddl
   -- Arguments:
   -- Return:      BOOLEAN
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_enable_parallel_ddl RETURN BOOLEAN IS
      v_enable_parallel_ddl BOOLEAN;
   BEGIN
      v_enable_parallel_ddl := upgrade_enable_parallel_ddl;
      return v_enable_parallel_ddl;
   END get_enable_parallel_ddl;


   ----------------------------------------------------------------------------
   -- Function:    get_enable_parallel_dml
   -- Arguments:   p_upgrade_enable_parallel_dml(BOOLEAN)
   -- Return:      BOOLEAN
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_enable_parallel_dml RETURN BOOLEAN IS
      v_enable_parallel_dml BOOLEAN;
   BEGIN
      v_enable_parallel_dml := upgrade_enable_parallel_dml;
      return v_enable_parallel_dml;
   END get_enable_parallel_dml;

   ----------------------------------------------------------------------------
   -- Function:    get_enable_parallel_query
   -- Arguments:
   -- Return:      BOOLEAN
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_enable_parallel_query RETURN BOOLEAN IS
      v_enable_parallel_query BOOLEAN;
   BEGIN
      v_enable_parallel_query := upgrade_enable_parallel_query;
      return v_enable_parallel_query;
   END get_enable_parallel_query;

   ----------------------------------------------------------------------------
   -- Function:    get_parallel_degree_policy
   -- Arguments:
   -- Return:      VARCHAR2
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_parallel_degree_policy RETURN VARCHAR2 IS
      v_parallel_degree_policy VARCHAR2(40);
   BEGIN
      v_parallel_degree_policy := upgrade_parallel_degree_policy;
      return v_parallel_degree_policy;
   END get_parallel_degree_policy;

   ----------------------------------------------------------------------------
   -- Function:    get_parallel_degree_limit
   -- Arguments:
   -- Return:      VARCHAR2
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_parallel_degree_limit RETURN VARCHAR2 IS
      v_parallel_degree_limit VARCHAR2(20);
   BEGIN
      v_parallel_degree_limit := upgrade_parallel_degree_limit;
      return v_parallel_degree_limit;
   END get_parallel_degree_limit;

   ----------------------------------------------------------------------------
   -- Function:    get_min_time_threshold
   -- Arguments:
   -- Return:      VARCHAR2
   -- Description: get parameter value from instance variable
   ----------------------------------------------------------------------------
   FUNCTION get_min_time_threshold RETURN VARCHAR2 IS
      v_min_time_threshold VARCHAR2(20);
   BEGIN
      v_min_time_threshold := upgrade_min_time_threshold;
      return v_min_time_threshold;
   END get_min_time_threshold;

   ----------------------------------------------------------------------------
   -- Procedure:    set_session_to_enable_parallel
   -- Arguments:
   -- Description: alter session to enable parallel based on instance variables
   ----------------------------------------------------------------------------
   PROCEDURE initialize_parallel_settings IS

      v_step NUMBER(4);

   BEGIN
   IF get_parallel_degree_policy = 'AUTO'
   THEN
      v_step := 10;
      EXECUTE IMMEDIATE 'ALTER SESSION SET PARALLEL_DEGREE_POLICY = AUTO';
      -- Notify the user the session has been changed to set parallel degree policy to AUTO
      dbms_output.put_line('INFO: SESSION has been changed to set parallel degree policy to AUTO ');
      v_step := 20;
      EXECUTE IMMEDIATE 'ALTER SESSION SET PARALLEL_DEGREE_LIMIT = ' || get_parallel_degree_limit;
      -- Notify the user the session has been changed to set parallel degree limit to the value of instance variable
      dbms_output.put_line('INFO: SESSION has been changed to set parallel degree limit to '||get_parallel_degree_limit);
      v_step := 30;
      EXECUTE IMMEDIATE 'ALTER SESSION SET PARALLEL_MIN_TIME_THRESHOLD = '|| get_min_time_threshold;
      -- Notify the user the session has been changed to set parallel minimum execution time threshold to the value of instance variable
      dbms_output.put_line('INFO: SESSION has been changed to set parallel minimum execution time threshold to '||get_min_time_threshold);

      IF utl_parallel_pkg.get_enable_parallel_ddl = TRUE
      THEN
         v_step := 40;
         EXECUTE IMMEDIATE 'ALTER SESSION ENABLE PARALLEL DDL';
         -- Notify the user the session has been changed to disable parallel DDL
         dbms_output.put_line('INFO: SESSION has been changed to enable DDL execution in parallel mode ');

      END IF;
      IF utl_parallel_pkg.get_enable_parallel_query = TRUE
      THEN
         v_step := 50;
         EXECUTE IMMEDIATE 'ALTER SESSION ENABLE PARALLEL QUERY';
         -- Notify the user the session has been changed to disable parallel QUERY
         dbms_output.put_line('INFO: SESSION has been changed to enable QUERY in parallel mode ');
      END IF;

   END IF;


   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'set_session_to_enable_parallel';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END;

   ----------------------------------------------------------------------------
   -- Procedure:    parallel_delete_begin
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   -- Description: Sets up the session for a parallel delete on a given table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_delete_begin(p_table_name IN VARCHAR2) IS

      v_step                NUMBER(4);

   BEGIN

      IF upgrade_enable_parallel_dml = TRUE
      THEN
         -- Log parallel DML counts
         v_step := 10;
         log_parallel(FALSE);

         -- Enable parallel dml, must commit before
         v_step := 20;
         enable_parallel_dml();

         -- Disable all triggers
         v_step := 30;
         disable_all_triggers(p_table_name);

         -- Disable all check constraints
         v_step := 40;
         disable_all_constraints(p_table_name, 'C');

         -- Disable all referential constraints
         v_step := 50;
         disable_all_constraints(p_table_name, 'R');

         -- Disable all r_constraints
         v_step := 60;
         disable_r_constraints(p_table_name);
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'parallel_delete_begin';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END parallel_delete_begin;


   ----------------------------------------------------------------------------
   -- Procedure:    parallel_delete_end
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_validate(BOOLEAN)             - validate constraints
   -- Description: Tears down the session from a parallel delete on a given
   --              table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_delete_end(p_table_name IN VARCHAR2,
                                 p_validate   IN BOOLEAN) IS

      v_step                NUMBER(4);

   BEGIN

      IF get_enable_parallel_dml = TRUE
      THEN
         -- Log parallel DML counts
         v_step := 10;
         log_parallel(TRUE);

         -- Enable all triggers
         v_step := 20;
         enable_all_triggers(p_table_name);

         -- Enable all check constraints
         -- Don't need to update check constraints on a delete
         v_step := 30;
         enable_all_constraints(p_table_name, 'C', FALSE);

         -- Enable all referential constraints
         -- Don't need to update ref constraints on a delete (these are constraints
         -- from the table we're deleting from, to other tables
         v_step := 40;
         enable_all_constraints(p_table_name, 'R', FALSE);

         -- Enable all r_constraints
         v_step := 50;
         enable_r_constraints(p_table_name, p_validate);

         -- Enable parallel dml, must commit before
         v_step := 60;
         disable_parallel_dml();
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'parallel_delete_end';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END parallel_delete_end;


   ----------------------------------------------------------------------------
   -- Procedure:    parallel_update_begin
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   -- Description: Sets up the session for a parallel update on a given table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_update_begin(p_table_name IN VARCHAR2) IS

      v_step                NUMBER(4);

   BEGIN

      IF get_enable_parallel_dml = TRUE
      THEN
         -- Log parallel DML counts
         v_step := 10;
         log_parallel(FALSE);

         -- Enable parallel dml, must commit before
         v_step := 20;
         enable_parallel_dml();

         -- Disable all triggers
         v_step := 30;
         disable_all_triggers(p_table_name);

         -- Disable all self-referential constraints
         v_step := 40;
         disable_all_constraints(p_table_name, 'R');

         -- Disable all check constraints
         v_step := 50;
         disable_all_constraints(p_table_name, 'C');

         -- Disable all referential constraints
         v_step := 60;
         disable_r_constraints(p_table_name);
      END IF;
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'parallel_update_begin';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END parallel_update_begin;


   ----------------------------------------------------------------------------
   -- Procedure:    parallel_update_end
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_validate(BOOLEAN)             - validate constraints
   -- Description: Tears down the session from a parallel update on a given
   --              table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_update_end(p_table_name IN VARCHAR2,
                                 p_validate   IN BOOLEAN) IS

      v_step                NUMBER(4);

   BEGIN
      IF get_enable_parallel_dml = TRUE
      THEN
         -- Log parallel DML counts
         v_step := 10;
         log_parallel(TRUE);

         -- Enable all triggers
         v_step := 20;
         enable_all_triggers(p_table_name);

         -- Enable all self-referential constraints
         v_step := 30;
         enable_all_constraints(p_table_name, 'R', p_validate);

         -- Enable all check constraints
         v_step := 40;
         enable_all_constraints(p_table_name, 'C', p_validate);

         -- Enable all referential constraints
         v_step := 50;
         enable_r_constraints(p_table_name, p_validate);

         -- Enable parallel dml, must commit before
         v_step := 60;
         disable_parallel_dml();
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'parallel_update_end';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END parallel_update_end;


   ----------------------------------------------------------------------------
   -- Procedure:    parallel_insert_begin
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   -- Description: Sets up the session for a parallel insert on a given table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_insert_begin(p_table_name IN VARCHAR2) IS

      v_step                NUMBER(4);

   BEGIN

      IF get_enable_parallel_dml = TRUE
      THEN
         -- Log parallel DML counts
         v_step := 10;
         log_parallel(FALSE);

         -- Enable parallel dml, must commit before
         v_step := 20;
         enable_parallel_dml();

         -- Disable all triggers
         v_step := 30;
         disable_all_triggers(p_table_name);

         -- Disable all referential constraints
         v_step := 40;
         disable_all_constraints(p_table_name, 'R');

         -- Disable all check constraints
         v_step := 50;
         disable_all_constraints(p_table_name, 'C');

         -- Disable all referential constraints
         v_step := 60;
         disable_r_constraints(p_table_name);
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'parallel_insert_begin';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END parallel_insert_begin;


   ----------------------------------------------------------------------------
   -- Procedure:    parallel_insert_end
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_validate(BOOLEAN)             - validate constraints
   -- Description: Tears down the session from a parallel insert on a given
   --              table
   ----------------------------------------------------------------------------
   PROCEDURE parallel_insert_end(p_table_name IN VARCHAR2,
                                 p_validate   IN BOOLEAN) IS

      v_step                NUMBER(4);

   BEGIN
      IF get_enable_parallel_dml = TRUE
      THEN
         -- Log parallel DML counts
         v_step := 10;
         log_parallel(TRUE);

         -- Enable all r_constraints
         v_step := 20;
         enable_r_constraints(p_table_name, p_validate);

         -- Enable all check constraints
         v_step := 30;
         enable_all_constraints(p_table_name, 'C', p_validate);

         -- Enable all referential constraints
         v_step := 40;
         enable_all_constraints(p_table_name, 'R', p_validate);

         -- Enable all triggers
         v_step := 50;
         enable_all_triggers(p_table_name);

         -- Enable parallel dml, must commit before
         v_step := 60;
         disable_parallel_dml();
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'parallel_insert_end';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END parallel_insert_end;

END UTL_PARALLEL_PKG;
/