--liquibase formatted sql


--changeSet MX-23925:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "UTL_MIGR_SCHEMA_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : utl_migr_schema_pkg
   -- Object Type : Package Body
   -- Date        : June 7, 2010
   -- Coder       : Mark Rutherford
   -- Recent Date : December 9, 2011
   -- Recent Coder: Mark Rutherford
   -- Description :
   -- This is the migration schema package containing all generic methods and
   -- constants for version migrations.
   ----------------------------------------------------------------------------
   -- Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   --
   ----------------------------------------------------------------------------
   -- Global types
   ----------------------------------------------------------------------------
   --
   --
   ----------------------------------------------------------------------------
   -- Global Constants
   ----------------------------------------------------------------------------
   --
   --
   ----------------------------------------------------------------------------
   -- Public Methods
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Procedure:   function_drop
   -- Arguments:   p_function_name(VARCHAR2)       - function name
   -- Description: Drops function if the function name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.function_drop('MY_FUNCTION_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE function_drop(p_function_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   index_create
   -- Arguments:   p_sql_clob(CLOB)              - SQL statement
   -- Description: Creates the index with the SQL statement provided.  If the columns
   --              are already indexed with the same index name then no changes are required.
   --              If the columns are already indexed with a different index name then the existing
   --              index is dropped and the new index is created.  If the index name already exists on
   --              different columns then the existing index is dropped and the new index is created.
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.index_create('
   --              Create Index "MY_INDEX_NAME" ON "MY_TABLE_NAME" (MY_COLUMN_ONE, MY_COLUMN_TWO)
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE index_create(p_sql_clob IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   index_drop
   -- Arguments:   p_index_name(VARCHAR2)       - index name
   -- Description: Drops index if the index name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.index_drop('MY_INDEX_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE index_drop(p_index_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   materialized_view_drop
   -- Arguments:   p_materialized_view_name(VARCHAR2)       - materialized_view name
   -- Description: Drops materialized_view if the materialized_view name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.materialized_view_drop('MY_MVIEW_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE materialized_view_drop(p_materialized_view_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   package_drop
   -- Arguments:   p_package_name(VARCHAR2)       - package name
   -- Description: Drops package if the package name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.package_drop('MY_PACKAGE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE package_drop(p_package_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   procedure_drop
   -- Arguments:   p_procedure_name(VARCHAR2)       - procedure name
   -- Description: Drops procedure if the procedure name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.procedure_drop('MY_PROCEDURE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE procedure_drop(p_procedure_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   sequence_create
   -- Arguments:   p_sequence_name(VARCHAR2)         - table name
   --              p_next_value(NUMBER)              - start with value
   -- Description: Creates the sequence with the sequence name and next value provided if
   --              the sequence name doesn't already exist.
   -- SQL Usage:   exec utl_migr_schema_pkg.sequence_create('MY_TABLE_NAME', MY_NEXT_VALUE);
   -- Note:        The MY_NEXT_VALUE parameter should usually be 100000 to allow zero
   --              level data to be created with values less than 100000.
   ----------------------------------------------------------------------------
   PROCEDURE sequence_create(p_sequence_name IN VARCHAR2,
                             p_next_value    IN NUMBER);

   ----------------------------------------------------------------------------
   -- Procedure:   sequence_drop
   -- Arguments:   p_sequence_name(VARCHAR2)       - sequence name
   -- Description: Drops sequence if the sequence name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.sequence_drop('MY_SEQUENCE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE sequence_drop(p_sequence_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   sequence_rename
   -- Arguments:   p_old_sequence_name(VARCHAR2)       - old sequence name
   --              p_new_sequence_name(VARCHAR2)       - new sequence name
   -- Description: renames sequence if the old sequence name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.sequence_rename('MY_OLD_SEQUENCE_NAME', 'MY_NEW_SEQUENCE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE sequence_rename(p_old_sequence_name IN VARCHAR2,
                             p_new_sequence_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   table_create
   -- Arguments:   p_sql(CLOB)              - SQL statement
   -- Description: Creates the table with the SQL statement provided if the table name
   --              doesn't already exist.
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.table_create('
   --              Create table MY_TABLE_NAME (
   --                 MY_COLUMN_NAME Number(10,0)
   --               Constraint PK_MY_TABLE_NAME primary key (MY_COLUMN_NAME)
   --              )
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE table_create(p_sql_clob IN CLOB);

   ----------------------------------------------------------------------------
   -- Procedure:   table_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   -- Description: Drops table if the table name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_drop('MY_TABLE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_drop(p_table_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   table_rename
   -- Arguments:   p_old_table_name(VARCHAR2)       - old table name
   --              p_new_table_name(VARCHAR2)       - new table name
   -- Description: renames table if the old table name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_rename('MY_OLD_TABLE_NAME', 'MY_NEW_TABLE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_rename(p_old_table_name IN VARCHAR2,
                          p_new_table_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_add
   -- Arguments:   p_sql_clob(CLOB)              - SQL statement
   -- Description: Creates the column with the SQL statement provided if the column name
   --              doesn't already exist.
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.table_column_add('
   --              Alter table MY_TABLE_NAME add (
   --                 MY_COLUMN_NAME Number(10,0)
   --              )
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE table_column_add(p_sql_clob IN CLOB);

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_column_name(VARCHAR2)      - column name
   -- Description: Drop column if the column name exists in the specified table.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_column_drop('MY_TABLE_NAME', 'MY_COLUMN_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_column_drop(p_table_name  IN VARCHAR2,
                               p_column_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_modify
   -- Arguments:   p_sql_clob(CLOB)              - SQL statement
   -- Description: Modifies the column with the SQL statement provided, however, constraints
   --              that already exist are removed from the SQL statement to prevent duplicate
   --              constraints.
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.table_column_modify('
   --              Alter table MY_TABLE_NAME modify (
   --                 MY_COLUMN_NAME Number(10,0)
   --              )
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE table_column_modify(p_sql_clob IN CLOB);

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_rename
   -- Arguments:   p_table_name(VARCHAR2)                   - table name
   --              p_old_column_name(VARCHAR2)              - old column name
   --              p_new_column_name(VARCHAR2)              - new column name
   -- Description: Case insensitive rename of the column if the column name exists in
   --              the specified table.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_column_rename('MY_TABLE_NAME', 'MY_OLD_COLUMN_NAME', 'MY_NEW_COLUMN_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_column_rename(p_table_name      IN VARCHAR2,
                                 p_old_column_name IN VARCHAR2,
                                 p_new_column_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_cons_chk_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_column_name(VARCHAR2)      - column name
   -- Description: Drop all check and not null constraints from the column in the specified table.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_column_cons_chk_drop('MY_TABLE_NAME', 'MY_COLUMN_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_column_cons_chk_drop(p_table_name  IN VARCHAR2,
                                        p_column_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_cons_nn_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_column_name(VARCHAR2)      - column name
   -- Description: Drop not null constraints from the column in the specified table.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_column_cons_nn_drop('MY_TABLE_NAME', 'MY_COLUMN_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_column_cons_nn_drop(p_table_name  IN VARCHAR2,
                                       p_column_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   table_constraint_add
   -- Arguments:   p_sql_clob(CLOB)              - SQL statement
   -- Description: Creates the contraint with the SQL statement provided if the
   --              contraint name doesn't already exist
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.table_constraint_add('
   --              Alter table MY_TABLE_NAME add contraint MY_CONSTRAINT_NAME foreign key (MY_COLUMN_NAME) references PARENT_TABLE_NAME (PARENT_COLUMN_NAME)  DEFERRABLE
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE table_constraint_add(p_sql_clob IN CLOB);

   ----------------------------------------------------------------------------
   -- Procedure:   table_constraint_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_constraint_name(VARCHAR2)  - constraint name
   -- Description: Drops the contraint if the contraint name exists
   -- SQL Usage:   exec utl_migr_schema_pkg.table_constraint_drop('MY_TABLE_NAME', 'MY_CONSTRAINT_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_constraint_drop(p_table_name      IN VARCHAR2,
                                   p_constraint_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   trigger_drop
   -- Arguments:   p_trigger_name(VARCHAR2)       - trigger name
   -- Description: Drops trigger if the trigger name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.trigger_drop('MY_TRIGGER_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE trigger_drop(p_trigger_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   type_drop
   -- Arguments:   p_type_name(VARCHAR2)       - type name
   -- Description: Drops type if the type name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.type_drop('MY_TYPE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE type_drop(p_type_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   view_drop
   -- Arguments:   p_view_name(VARCHAR2)       - view name
   -- Description: Drops view if the view name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.view_drop('MY_VIEW_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE view_drop(p_view_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   view_rename
   -- Arguments:   p_old_view_name(VARCHAR2)       - old view name
   --              p_new_view_name(VARCHAR2)       - new view name
   -- Description: renames view if the old view name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.view_rename('MY_OLD_VIEW_NAME', 'MY_NEW_VIEW_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE view_rename(p_old_view_name IN VARCHAR2,
                         p_new_view_name IN VARCHAR2);

   -----------------------------------------------------------------------------
   -- Procedure: set_disable_redo_logging_param
   -- Arguments: p_disable_redo_logging(BOOLEAN)
   -- Description: get parameter from build.properties file and set upgrade_disable_redo_logging
   -- SQL Usage: exec utl_migr_schema_pkg.set_disable_redo_logging_param(TRUE)
   -----------------------------------------------------------------------------
   PROCEDURE set_disable_redo_logging_param(p_disable_redo_logging IN BOOLEAN);

   -----------------------------------------------------------------------------
   -- Function:    get_disable_redo_logging_param
   -- Arguments:
   -- Return:      v_disable_redo_logging(BOOLEAN)
   -- Description: get parameter value of disable redo logging from instance variable
   -- SQL Usage:   exec utl_migr_schema_pkg.get_disable_redo_logging_param
   -----------------------------------------------------------------------------
   FUNCTION get_disable_redo_logging_param return BOOLEAN;

   -----------------------------------------------------------------------------
   -- Procedure: set_defer_column_drop_param
   -- Arguments: p_defer_column_drop(BOOLEAN)
   -- Description: get parameter value from build.properties file and set upgrade_defer_column_drop
   -- SQL Usage: exec utl_migr_schema_pkg.set_defer_column_drop_param(TRUE)
   -----------------------------------------------------------------------------
   PROCEDURE set_defer_column_drop_param(p_defer_column_drop IN BOOLEAN);

   -----------------------------------------------------------------------------
   -- Function:    get_defer_column_drop_param
   -- Arguments:
   -- Return:      v_defer_column_drop(BOOLEAN)
   -- Description: get parameter value of defer column drop from instance variable
   -- SQL Usage:   exec utl_migr_schema_pkg.get_defer_column_drop_param
   -----------------------------------------------------------------------------
   FUNCTION get_defer_column_drop_param return BOOLEAN;

END utl_migr_schema_pkg;
/

--changeSet MX-23925:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "UTL_MIGR_SCHEMA_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : utl_migr_schema_pkg
   -- Object Type : Package Body
   -- Date        : June 7, 2010
   -- Coder       : Mark Rutherford
   -- Recent Date : December 9, 2011
   -- Recent Coder: Mark Rutherford
   -- Description :
   -- This is the migration schema package containing all generic methods and
   -- constants for version migrations.
   ----------------------------------------------------------------------------
   -- Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------

   /*--------------------------------------------------------
   *  INSTANCE VARIABLES
   *---------------------------------------------------------*/
   -- This variable is used to determine whether or not create database objects
   -- with nologging mode
   upgrade_disable_redo_logging BOOLEAN:= FALSE;

   -- this parameter is used to determine whether or not to defer dropping column
   -- if true, migration script will set column unused then drop all the unused column
   -- in one go when disk space needs to be reclaimed
   upgrade_defer_column_drop BOOLEAN := FALSE;

   ----------------------------------------------------------------------------
   -- Private variables
   ----------------------------------------------------------------------------
   c_pkg_name CONSTANT VARCHAR2(30) := 'utl_migr_schema_pkg';

   -- Boolean constants
   c_true  CONSTANT NUMBER(1) := 1;
   c_false CONSTANT NUMBER(1) := 0;

   -- Object type constants
   c_object_type_index     CONSTANT VARCHAR2(30) := 'INDEX';
   c_object_type_function  CONSTANT VARCHAR2(30) := 'FUNCTION';
   c_object_type_mview     CONSTANT VARCHAR2(30) := 'MATERIALIZED VIEW';
   c_object_type_package   CONSTANT VARCHAR2(30) := 'PACKAGE';
   c_object_type_procedure CONSTANT VARCHAR2(30) := 'PROCEDURE';
   c_object_type_sequence  CONSTANT VARCHAR2(30) := 'SEQUENCE';
   c_object_type_table     CONSTANT VARCHAR2(30) := 'TABLE';
   c_object_type_trigger   CONSTANT VARCHAR2(30) := 'TRIGGER';
   c_object_type_type      CONSTANT VARCHAR2(30) := 'TYPE';
   c_object_type_view      CONSTANT VARCHAR2(30) := 'VIEW';

   -- Check constraint constants
   c_all_check_constraints     CONSTANT VARCHAR2(30) := 'ALLCHKCONS';
   c_only_not_null_constraints CONSTANT VARCHAR2(30) := 'ONLYNOTNULL';

   v_err_msg     VARCHAR2(2000);
   v_err_code    VARCHAR2(200);
   v_method_name VARCHAR2(30);

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Private method specs
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Function:    check_column_exists
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_column_name(VARCHAR2)         - column name
   -- Return:      NUMBER - BOOLEAN 0 / 1
   -- Description: 1 if the object exists and 0 if it does not
   ----------------------------------------------------------------------------
   FUNCTION check_column_exists(p_table_name  VARCHAR2,
                                p_column_name VARCHAR2) RETURN NUMBER;

   ----------------------------------------------------------------------------
   -- Function:    check_constraint_exists
   -- Arguments:   p_table_name(VARCHAR2)              - table name
   --              p_constraint_name(VARCHAR2)         - constraint name
   -- Return:      NUMBER - BOOLEAN 0 / 1
   -- Description: 1 if the object exists and 0 if it does not
   ----------------------------------------------------------------------------
   FUNCTION check_constraint_exists(p_table_name      VARCHAR2,
                                    p_constraint_name VARCHAR2) RETURN NUMBER;

   ----------------------------------------------------------------------------
   -- Function:    check_object_exists
   -- Arguments:   p_object_name(VARCHAR2)              - object name
   --              p_object_type(VARCHAR2)              - object type
   -- Return:      NUMBER - BOOLEAN 0 / 1
   -- Description: 1 if the object exists and 0 if it does not
   ----------------------------------------------------------------------------
   FUNCTION check_object_exists(p_object_name VARCHAR2,
                                p_object_type VARCHAR2) RETURN NUMBER;

   ----------------------------------------------------------------------------
   -- Procedure:   drop_column_chk_constraints
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_column_name(VARCHAR2)         - column name
   --              p_chk_constraint_type(VARCHAR2) - check constraint type
   -- Description: Drops the check constraints on the specified column based on the
   --              p_chk_constraint_type parameter.  If this parameter is set to
   --              c_all_check_constraints then all check constraints are dropped
   --              and if the parameter is set to c_only_not_null_constraints then
   --              only not null check constraints are dropped.  This drop is case
   --              insensitive.
   ----------------------------------------------------------------------------
   PROCEDURE drop_column_chk_constraints(p_table_name          IN VARCHAR2,
                                         p_column_name         IN VARCHAR2,
                                         p_chk_constraint_type IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   drop_constraint
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_constraint_name(VARCHAR2)  - constraint name
   -- Description: Drops the contraint if the contraint name exists.
   --              This drop is case insensitive.
   ----------------------------------------------------------------------------
   PROCEDURE drop_constraint(p_table_name      IN VARCHAR2,
                             p_constraint_name IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Procedure:   drop_object
   -- Arguments:   p_object_name(VARCHAR2)              - object name
   --              p_object_type(VARCHAR2)              - object type
   -- Description: Drop the object if the object name exists.
   --              This drop is case insensitive.
   ----------------------------------------------------------------------------
   PROCEDURE drop_object(p_object_name IN VARCHAR2,
                         p_object_type IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Function:    get_index_name
   -- Arguments:   p_table_name (VARCHAR2)          - Table name
   --              p_column_list (VARCHAR2)         - Comma separated column list
   -- Return:      VARCHAR2 - Index name
   -- Description: Returns the index name for the specified table name and column list.
   --              This function is used by the create_index procedure to determine
   --              if the index already exists with the same or different name.
   --              Returns NULL if the index is not found.
   ----------------------------------------------------------------------------
   FUNCTION get_index_name(p_table_name  VARCHAR2,
                           p_column_list VARCHAR2) RETURN VARCHAR2;

   ----------------------------------------------------------------------------
   -- Function:    get_string_after_pattern
   -- Arguments:   p_pattern(VARCHAR2)            - Preceding Pattern
   --              p_sql(VARCHAR2)                - SQL statement
   -- Return:      VARCHAR2 - Parameter
   -- Description: Extracts the first word after the p_pattern of the SQL statement.
   --              This function is used to extract table_name, column_name, etc.
   --              from SQL statements that are passed in as parameters.
   --              Returns NULL if the pattern is not found.
   ----------------------------------------------------------------------------
   FUNCTION get_string_after_pattern(p_pattern VARCHAR2,
                                     p_sql     VARCHAR2) RETURN VARCHAR2;

   ----------------------------------------------------------------------------
   -- Function:    get_string_between_parentheses
   -- Arguments:   p_sql(VARCHAR2)                        - SQL statement
   -- Return:      VARCHAR2 - Column list
   -- Description: Extracts the string between the first opening parenthesis and the
   --              last closing parenthesis.  This function is used to extract the
   --              column_list from a create index SQL statement.
   --              Returns NULL if the parentheses are not found.
   ----------------------------------------------------------------------------
   FUNCTION get_string_between_parentheses(p_sql VARCHAR2) RETURN VARCHAR2;

   ----------------------------------------------------------------------------
   -- Procedure:   rename_object
   -- Arguments:   p_old_object_name(VARCHAR2)              - old object name
   --              p_new_object_name(VARCHAR2)              - new object name
   --              p_object_type(VARCHAR2)                  - object type
   -- Description: Case insensitive rename of the object with the specified name and type
   --              if the old object name/type combination exists.
   ----------------------------------------------------------------------------
   PROCEDURE rename_object(p_old_object_name IN VARCHAR2,
                           p_new_object_name IN VARCHAR2,
                           p_object_type     IN VARCHAR2);

   ----------------------------------------------------------------------------
   -- Function:    replace_ignore_case_and_spaces
   -- Arguments:   p_source_string(VARCHAR2)                - source string
   --              p_search_string(VARCHAR2)                - search string
   --              p_replacement_string(VARCHAR2 OPTIONAL)  - replacement string
   -- Return:      VARCHAR2 - Source string with the search string replaced.
   -- Description: Case insensitive replace function that ignores spaces.  Returns source string with
   --              the FIRST case insensitive space excluded occurrence of search_string replaced
   --              with replacement_string.  Leading or trailing spaces in the search string will not
   --              be replaced since the function is ignoring spaces. If replacement_string is omitted
   --              or null, then the first occurrence of search_string is removed.
   --              Returns the original string if the search string is not found.
   -- Example:     replace_ignore_case_and_spaces('One Two    T h r e e Four', 'two three')
   -- Output:      Example input returns 'One  Four' even though case and spacing of the search string
   --              is different than the source string.
   ----------------------------------------------------------------------------
   FUNCTION replace_ignore_case_and_spaces(p_source_string      VARCHAR2,
                                           p_search_string      VARCHAR2,
                                           p_replacement_string VARCHAR2 DEFAULT NULL)
      RETURN VARCHAR2;

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Public method bodies
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Procedure:   function_drop
   -- Arguments:   p_function_name(VARCHAR2)       - function name
   -- Description: Drops function if the function name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.function_drop('MY_FUNCTION_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE function_drop(p_function_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_function_name,
                  c_object_type_function);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'function_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END function_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   index_create
   -- Arguments:   p_sql_clob(CLOB)              - SQL statement
   -- Description: Creates the index with the SQL statement provided.  If the columns
   --              are already indexed with the same index name then no changes are required.
   --              If the columns are already indexed with a different index name then the existing
   --              index is dropped and the new index is created.  If the index name already exists on
   --              different columns then the existing index is dropped and the new index is created.
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.index_create('
   --              Create Index "MY_INDEX_NAME" ON "MY_TABLE_NAME" (MY_COLUMN_ONE, MY_COLUMN_TWO)
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE index_create(p_sql_clob IN VARCHAR2) IS

      -- Search pattern constants to parse the index name and table name from the SQL statement
      c_index_name_pattern CONSTANT VARCHAR2(30) := ' INDEX ';
      c_table_name_pattern CONSTANT VARCHAR2(30) := ' ON ';

      v_step                NUMBER(4);
      v_index_name          user_indexes.index_name%TYPE;
      v_table_name          user_indexes.table_name%TYPE;
      v_column_list         VARCHAR2(32767);
      v_existing_index_name user_indexes.index_name%TYPE;
      v_exists              NUMBER(1);
      v_cursor_id           PLS_INTEGER;
      v_ret_val             NUMBER;
      v_sql_clob            VARCHAR2(4000);

   BEGIN

      v_step := 10;

      v_sql_clob := p_sql_clob;

      v_step := 15;

      -- Extract the index name from the SQL statement
      v_index_name := get_string_after_pattern(c_index_name_pattern,
                                               p_sql_clob);
      v_step       := 20;
      -- Extract the table name from the SQL statement
      v_table_name := get_string_after_pattern(c_table_name_pattern,
                                               p_sql_clob);
      v_step       := 30;
      -- Extract the column list from the SQL statement
      v_column_list := get_string_between_parentheses(p_sql_clob);

      v_step := 40;
      -- Check if these columns are already indexed and if so get the existing index name
      v_existing_index_name := get_index_name(v_table_name,
                                              v_column_list);

      v_step := 50;
      -- Check if the existing index name is the same as the index name in the SQL statement
      IF v_index_name = v_existing_index_name
      THEN

         v_step := 60;
         -- The index already exists with the same name, therefore, no changes are required
         dbms_output.put_line('INFO: The ' || upper(v_index_name) ||
                              ' index already existed, therefore, it was not recreated.');

      ELSE

         -- Either there is no exisiting index name (NULL) or there is an existing index name and it is
         -- not the same as the index name in the new SQL statement

         v_step := 70;
         -- If the existing name is not null then there is an existing index on the same columns
         IF v_existing_index_name IS NOT NULL
         THEN

            v_step := 80;
            -- If the existing name is not null then drop it so that we can recreate it with the correct name
            dbms_output.put_line('INFO: The same columns are already indexed by the ' ||
                                 upper(v_existing_index_name) || ' index.');
            index_drop(v_existing_index_name);

         END IF;

         v_step := 90;
         -- Check if an index exists with the index name from the SQL statement
         v_exists := utl_migr_schema_pkg.check_object_exists(v_index_name,
                                                             c_object_type_index);

         IF v_exists = c_true
         THEN

            v_step := 100;
            -- The index name exists, however, we know from the above checks that it is
            -- not on the same columns as the new index, therefore, drop the existing
            -- index so that it can be recreated on the correct columns
            dbms_output.put_line('INFO: The ' || upper(v_index_name) ||
                                 ' index already exists but it is on the wrong columns.');
            index_drop(v_index_name);

         END IF;

         -- The index name does not exist or has been dropped, therefore, execute the SQL statement
         -- to create the index.


         -- if upgrade_disable_redo_logging is set to true in build.properties file, create index
         -- with nologging mode
         IF get_disable_redo_logging_param = TRUE
         THEN
            v_sql_clob := v_sql_clob ||' NOLOGGING';
         END IF;


         v_step := 110;
         -- Open cursor
         v_cursor_id := dbms_sql.open_cursor;

         v_step := 120;
         -- Parse statement
         dbms_sql.parse(v_cursor_id,
                        v_sql_clob,
                        dbms_sql.native);

         v_step := 130;
         -- Run the SQL statement to create the index
         v_ret_val := dbms_sql.EXECUTE(v_cursor_id);

         v_step := 140;
         -- Close cursor
         dbms_sql.close_cursor(v_cursor_id);

         v_step := 150;
         -- Notify the user that the index was created.
         IF get_disable_redo_logging_param = TRUE
         THEN
            dbms_output.put_line('INFO: The ' || upper(v_index_name) ||
                              ' index has been created in NOLOGGING mode.');
         ELSE
            dbms_output.put_line('INFO: The ' || upper(v_index_name) ||
                              ' index has been created in LOGGING mode.');
         END IF;
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'index_create';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         -- Close the dbms_sql cursor if it is still open
         IF dbms_sql.is_open(v_cursor_id)
         THEN
            dbms_sql.close_cursor(v_cursor_id);
         END IF;

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END index_create;

   ----------------------------------------------------------------------------
   -- Procedure:   index_drop
   -- Arguments:   p_index_name(VARCHAR2)       - index name
   -- Description: Drops index if the index name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.index_drop('MY_INDEX_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE index_drop(p_index_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_index_name,
                  c_object_type_index);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'index_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END index_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   materialized_view_drop
   -- Arguments:   p_materialized_view_name(VARCHAR2)       - materialized_view name
   -- Description: Drops materialized_view if the materialized_view name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.materialized_view_drop('MY_MVIEW_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE materialized_view_drop(p_materialized_view_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_materialized_view_name,
                  c_object_type_mview);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'materialized_view_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END materialized_view_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   package_drop
   -- Arguments:   p_package_name(VARCHAR2)       - package name
   -- Description: Drops package if the package name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.package_drop('MY_PACKAGE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE package_drop(p_package_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_package_name,
                  c_object_type_package);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'package_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END package_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   procedure_drop
   -- Arguments:   p_procedure_name(VARCHAR2)       - procedure name
   -- Description: Drops procedure if the procedure name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.procedure_drop('MY_PROCEDURE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE procedure_drop(p_procedure_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_procedure_name,
                  c_object_type_procedure);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'procedure_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END procedure_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   sequence_create
   -- Arguments:   p_sequence_name(VARCHAR2)         - table name
   --              p_next_value(NUMBER)              - start with value
   -- Description: Creates the sequence with the sequence name and next value provided if
   --              the sequence name doesn't already exist.
   -- SQL Usage:   exec utl_migr_schema_pkg.sequence_create('MY_TABLE_NAME', MY_NEXT_VALUE);
   -- Note:        The MY_NEXT_VALUE parameter should usually be 100000 to allow zero
   --              level data to be created with values less than 100000.
   ----------------------------------------------------------------------------
   PROCEDURE sequence_create(p_sequence_name IN VARCHAR2,
                             p_next_value    IN NUMBER) IS

      v_step   NUMBER(4);
      v_exists NUMBER(1);

   BEGIN

      v_step := 10;
      -- Check if the sequence name already exists
      v_exists := utl_migr_schema_pkg.check_object_exists(p_sequence_name,
                                                          c_object_type_sequence);

      v_step := 20;
      IF v_exists = c_false
      THEN

         v_step := 30;
         -- The sequence name doesn't exist, therefore, run the SQL statement to create it.
         EXECUTE IMMEDIATE ('CREATE SEQUENCE ' || upper(p_sequence_name) ||
                           ' INCREMENT BY 1 START WITH ' || p_next_value ||
                           ' MINVALUE 1 MAXVALUE 4294967295 CACHE 5 NOCYCLE NOORDER');

         -- Notify the user that the sequence was created.
         dbms_output.put_line('INFO: Sequence ' || upper(p_sequence_name) ||
                              ' created.');

      ELSE

         v_step := 40;
         -- The sequence name already exists, therefore, notify the user that it will
         -- not be recreated.
         dbms_output.put_line('INFO: Sequence ' || upper(p_sequence_name) ||
                              ' already existed, therefore, it was not created.');

      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'sequence_create';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END sequence_create;

   ----------------------------------------------------------------------------
   -- Procedure:   sequence_drop
   -- Arguments:   p_sequence_name(VARCHAR2)       - sequence name
   -- Description: Drops sequence if the sequence name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.sequence_drop('MY_SEQUENCE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE sequence_drop(p_sequence_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_sequence_name,
                  c_object_type_sequence);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'sequence_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END sequence_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   sequence_rename
   -- Arguments:   p_old_sequence_name(VARCHAR2)       - old sequence name
   --              p_new_sequence_name(VARCHAR2)       - new sequence name
   -- Description: renames sequence if the old sequence name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.sequence_rename('MY_OLD_SEQUENCE_NAME', 'MY_NEW_SEQUENCE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE sequence_rename(p_old_sequence_name IN VARCHAR2,
                             p_new_sequence_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to rename the object
      rename_object(p_old_sequence_name,
                    p_new_sequence_name,
                    c_object_type_sequence);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'sequence_rename';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END sequence_rename;

   ----------------------------------------------------------------------------
   -- Procedure:   table_create
   -- Arguments:   p_sql(CLOB)              - SQL statement
   -- Description: Creates the table with the SQL statement provided if the table name
   --              doesn't already exist.
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.table_create('
   --              Create table MY_TABLE_NAME (
   --                 MY_COLUMN_NAME Number(10,0)
   --               Constraint PK_MY_TABLE_NAME primary key (MY_COLUMN_NAME)
   --              )
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE table_create(p_sql_clob IN CLOB) IS

      -- Search pattern constants to parse the table name from the SQL statement
      c_table_name_pattern CONSTANT VARCHAR2(15) := 'CREATE TABLE ';

      v_step       NUMBER(4);
      v_table_name user_tables.table_name%TYPE;
      v_exists     NUMBER(1);
      v_cursor_id  PLS_INTEGER;
      v_ret_val    NUMBER;
      v_sql_clob   CLOB;

   BEGIN

      v_step := 10;

      v_sql_clob := p_sql_clob;

      v_step := 15;

      -- Extract the table name from the SQL statement
      v_table_name := get_string_after_pattern(c_table_name_pattern,
                                               p_sql_clob);

      v_step := 20;
      -- Check if the table name already exists
      v_exists := utl_migr_schema_pkg.check_object_exists(v_table_name,
                                                          c_object_type_table);

      v_step := 30;
      IF v_exists = c_false
      THEN

         -- if upgrade.disable.redo.logging is set to true in build.properties file
         -- then create table in nologging mode
         IF get_disable_redo_logging_param = TRUE
         THEN
            v_sql_clob := v_sql_clob || ' NOLOGGING';
         END IF;

         -- The table name does not exist, therefore, execute the SQL statement
         -- to create the table.

         v_step := 40;
         -- Open cursor
         v_cursor_id := dbms_sql.open_cursor;

         v_step := 50;
         -- Parse statement
         dbms_sql.parse(v_cursor_id,
                        v_sql_clob,
                        dbms_sql.native);

         v_step := 60;
         -- Execute statement
         v_ret_val := dbms_sql.EXECUTE(v_cursor_id);

         v_step := 70;
         -- Close cursor
         dbms_sql.close_cursor(v_cursor_id);

         v_step := 80;
         -- Notify the user that the table was created.
         IF get_disable_redo_logging_param = TRUE
         THEN
            dbms_output.put_line('INFO: Table ' || upper(v_table_name) ||
                              ' created in NOLOGGING mode.');
         ELSE
            dbms_output.put_line('INFO: Table ' || upper(v_table_name) ||
                              ' created in LOGGING mode.');
         END IF;
      ELSE

         v_step := 90;
         -- The table name already exists, therefore, notify the user that it will
         -- not be recreated.
         dbms_output.put_line('INFO: Table ' || upper(v_table_name) ||
                              ' already existed, therefore, it was not created.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_create';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         -- Close the dbms_sql cursor if it is still open
         IF dbms_sql.is_open(v_cursor_id)
         THEN
            dbms_sql.close_cursor(v_cursor_id);
         END IF;

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_create;


   ----------------------------------------------------------------------------
   -- Procedure:   table_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   -- Description: Drops table if the table name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_drop('MY_TABLE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_drop(p_table_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_table_name,
                  c_object_type_table);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   table_rename
   -- Arguments:   p_old_table_name(VARCHAR2)       - old table name
   --              p_new_table_name(VARCHAR2)       - new table name
   -- Description: renames table if the old table name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_rename('MY_OLD_TABLE_NAME', 'MY_NEW_TABLE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_rename(p_old_table_name IN VARCHAR2,
                          p_new_table_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to rename the object
      rename_object(p_old_table_name,
                    p_new_table_name,
                    c_object_type_table);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_rename';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_rename;

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_add
   -- Arguments:   p_sql_clob(CLOB)              - SQL statement
   -- Description: Creates the column with the SQL statement provided if the column name
   --              doesn't already exist.
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.table_column_add('
   --              Alter table MY_TABLE_NAME add (
   --                 MY_COLUMN_NAME Number(10,0)
   --              )
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE table_column_add(p_sql_clob IN CLOB) IS

      -- Search pattern constants to parse the table name and column name from the SQL statement
      c_table_name_pattern  CONSTANT VARCHAR2(30) := 'ALTER TABLE ';
      c_column_name_pattern CONSTANT VARCHAR2(30) := ' ADD ';

      v_step        NUMBER(4);
      v_table_name  user_tab_columns.table_name%TYPE;
      v_column_name user_tab_columns.column_name%TYPE;
      v_exists      NUMBER(1);
      v_cursor_id   PLS_INTEGER;
      v_ret_val     NUMBER;

   BEGIN

      v_step := 10;
      -- Extract the table name from the SQL statement
      v_table_name := get_string_after_pattern(c_table_name_pattern,
                                               p_sql_clob);

      v_step := 20;
      -- Extract the column name from the SQL statement
      v_column_name := get_string_after_pattern(c_column_name_pattern,
                                                p_sql_clob);

      v_step := 30;
      -- Check if the column already exists on this table
      v_exists := utl_migr_schema_pkg.check_column_exists(v_table_name,
                                                          v_column_name);

      v_step := 40;
      IF v_exists = c_false
      THEN

         -- The column does not exist, therefore, execute the SQL statement
         -- to add the column.

         v_step := 50;
         -- Open cursor
         v_cursor_id := dbms_sql.open_cursor;

         v_step := 60;
         -- Parse statement
         dbms_sql.parse(v_cursor_id,
                        p_sql_clob,
                        dbms_sql.native);

         v_step := 70;
         -- Execute statement
         v_ret_val := dbms_sql.EXECUTE(v_cursor_id);

         v_step := 80;
         -- Close cursor
         dbms_sql.close_cursor(v_cursor_id);

         v_step := 90;
         -- Notify the user that the column was added.
         dbms_output.put_line('INFO: The ' || upper(v_column_name) ||
                              ' column has been added to the ' || v_table_name ||
                              ' table.');
      ELSE

         v_step := 100;
         -- The column does exists, therefore, notify the user that the column
         -- was not added.
         dbms_output.put_line('INFO: The ' || upper(v_column_name) ||
                              ' column already existed in the ' ||
                              v_table_name ||
                              ' table, therefore, it was not created.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_column_add';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         -- Close the dbms_sql cursor if it is still open
         IF dbms_sql.is_open(v_cursor_id)
         THEN
            dbms_sql.close_cursor(v_cursor_id);
         END IF;

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_column_add;

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_column_name(VARCHAR2)      - column name
   -- Description: Drop column if the column name exists in the specified table.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_column_drop('MY_TABLE_NAME', 'MY_COLUMN_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_column_drop(p_table_name  IN VARCHAR2,
                               p_column_name IN VARCHAR2) IS

      v_step        NUMBER(4);
      v_table_name  user_tab_columns.table_name%TYPE;
      v_column_name user_tab_columns.column_name%TYPE;
      v_exists      NUMBER(1);

   BEGIN

      v_step := 10;
      -- Get the exact table name and column name from the data dictionary
      BEGIN

         -- Query the data dictionary to get the object names.
         -- Convert the names to uppercase in the where clause of the
         -- query to also find objects where the name is the same but the case
         -- is different (e.g. "cNAME" versus "CNAME").
         -- We do not convert the results to allow us to drop the actual
         -- name of the object as it exists in the data dictionary.
         SELECT table_name,
                column_name
           INTO v_table_name,
                v_column_name
           FROM user_tab_columns
          WHERE upper(table_name) = upper(p_table_name)
            AND upper(column_name) = upper(p_column_name);

         -- The above query was successful which means that the column exists
         v_exists := c_true;

      EXCEPTION
         WHEN no_data_found THEN

            -- The above query was not successful which means that the column does not exist
            v_exists := c_false;

      END;

      v_step := 20;
      IF v_exists = c_true
      THEN

         v_step := 30;
         -- If upgrade.defer.drop.column is set to true in Build.properties file
         -- then defer column drop by set the column unused
         IF (get_defer_column_drop_param = TRUE)
         THEN
            EXECUTE IMMEDIATE ('ALTER TABLE "' || v_table_name ||
                              '" SET UNUSED COLUMN "' || v_column_name || '"');

            -- Notify the user that the column name was set UNUSED
            dbms_output.put_line('INFO: The ' || v_column_name ||
                              ' column of the ' || v_table_name ||
                              ' table has been set unused.');

         ELSE

             -- The column name exists, therefore, run the SQL statement to drop it.
             -- Wrap double quotation marks around the names to use the exact case
             -- that exists in the data dictionary.
             EXECUTE IMMEDIATE ('ALTER TABLE "' || v_table_name ||
                               '" DROP COLUMN "' || v_column_name || '"');

             -- Notify the user that the column name was dropped
             dbms_output.put_line('INFO: The ' || v_column_name ||
                                  ' column of the ' || v_table_name ||
                                  ' table has been dropped.');
          END IF;

      ELSE

         v_step := 40;
         -- Notify the user that the column did not exist, therefore, it was not dropped
         dbms_output.put_line('INFO: The ' || p_column_name ||
                              ' column does not exist in the ' || p_table_name ||
                              ' table, therefore, it was not dropped.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_column_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_column_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_modify
   -- Arguments:   p_sql_clob(CLOB)              - SQL statement
   -- Description: Modifies the column with the SQL statement provided, however, constraints
   --              that already exist are removed from the SQL statement to prevent duplicate
   --              constraints.
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.table_column_modify('
   --              Alter table MY_TABLE_NAME modify (
   --                 MY_COLUMN_NAME Number(10,0)
   --              )
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE table_column_modify(p_sql_clob IN CLOB) IS

      -- Search pattern constants to parse the table name and column name from the SQL statement
      c_table_name_pattern  CONSTANT VARCHAR2(30) := 'ALTER TABLE ';
      c_column_name_pattern CONSTANT VARCHAR2(30) := ' MODIFY ';

      v_step             NUMBER(4);
      v_table_name       user_tab_columns.table_name%TYPE;
      v_column_name      user_tab_columns.column_name%TYPE;
      v_sql              VARCHAR2(32767);
      v_search_condition VARCHAR2(32767);
      v_replace          VARCHAR2(32767);
      v_exists           NUMBER(1);
      v_cursor_id        PLS_INTEGER;
      v_ret_val          NUMBER;

      -- This cursor will return all of the check constraint search conditions.
      -- Convert the object names to uppercase in the where clause to handle
      -- object with mixed case names.
      CURSOR cur_constraint_details IS
         SELECT user_constraints.search_condition
           FROM user_constraints,
                user_cons_columns
          WHERE user_constraints.table_name = user_cons_columns.table_name
            AND user_constraints.constraint_name =
                user_cons_columns.constraint_name
            AND upper(user_constraints.table_name) = upper(v_table_name)
            AND upper(user_cons_columns.column_name) = upper(v_column_name)
            AND user_constraints.constraint_type = 'C';

   BEGIN

      v_step := 10;
      -- Extract the table name from the SQL statement
      v_table_name := get_string_after_pattern(c_table_name_pattern,
                                               p_sql_clob);

      v_step := 20;
      -- Extract the column name from the SQL statement
      v_column_name := get_string_after_pattern(c_column_name_pattern,
                                                p_sql_clob);

      v_step := 30;
      -- Remove double quotes from the SQL statement
      v_sql := REPLACE(p_sql_clob,
                       '"');

      -- Loop through the check constraints on the specified table
      FOR constraint_detail_rec IN cur_constraint_details
      LOOP

         v_step := 40;
         -- Remove double quotes from the search condition
         v_search_condition := upper(REPLACE(constraint_detail_rec.search_condition,
                                             '"'));

         IF upper(v_search_condition) LIKE '%IS NOT NULL%'
         THEN

            v_step := 50;
            -- The search condition is a not null constraint, therefore, set the
            -- replace string to NOT NULL to allow the replace function
            -- to remove the not null constraint from the sql statement
            v_replace := 'NOT NULL';

         ELSE

            v_step := 60;
            -- The search condition is NOT a not null constraint, therefore, build
            -- the replace string as a check constraint to allow the replace
            -- function to remove the check constraint from the sql statement
            v_replace := 'CHECK (' || v_search_condition || ')';

         END IF;

         v_step := 70;
         -- Use the replace custom function to remove the replace string defined
         -- above from the sql statement.  Run the replace twice with and without
         -- the deferrable clause to handle both cases
         v_sql := replace_ignore_case_and_spaces(v_sql,
                                                 v_replace || ' DEFERRABLE');
         v_sql := replace_ignore_case_and_spaces(v_sql,
                                                 v_replace);

      END LOOP;

      -- Execute the modified SQL statement to alter the column.

      v_step := 80;
      -- Open cursor
      v_cursor_id := dbms_sql.open_cursor;

      v_step := 90;
      -- Parse statement
      dbms_sql.parse(v_cursor_id,
                     v_sql,
                     dbms_sql.native);

      v_step := 100;
      -- Execute statement
      v_ret_val := dbms_sql.EXECUTE(v_cursor_id);

      v_step := 110;
      -- Close cursor
      dbms_sql.close_cursor(v_cursor_id);

      v_step := 120;
      -- Notify the user that the column has been modified
      dbms_output.put_line('INFO: The ' || upper(v_column_name) ||
                           ' column of the ' || upper(v_table_name) ||
                           ' table has been modified.');

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_column_modify';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         -- Close the dbms_sql cursor if it is still open
         IF dbms_sql.is_open(v_cursor_id)
         THEN
            dbms_sql.close_cursor(v_cursor_id);
         END IF;

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_column_modify;

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_rename
   -- Arguments:   p_table_name(VARCHAR2)                   - table name
   --              p_old_column_name(VARCHAR2)              - old column name
   --              p_new_column_name(VARCHAR2)              - new column name
   -- Description: Case insensitive rename of the column if the column name exists in
   --              the specified table.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_column_rename('MY_TABLE_NAME', 'MY_OLD_COLUMN_NAME', 'MY_NEW_COLUMN_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_column_rename(p_table_name      IN VARCHAR2,
                                 p_old_column_name IN VARCHAR2,
                                 p_new_column_name IN VARCHAR2) IS

      v_step            NUMBER(4);
      v_table_name      user_tab_columns.table_name%TYPE;
      v_old_column_name user_tab_columns.column_name%TYPE;
      v_exists          NUMBER(1);

   BEGIN

      v_step := 10;
      -- Get the exact table name and column name from the data dictionary.
      BEGIN

         -- Query the data dictionary to get the object names.
         -- Convert the names to uppercase in the where clause of the
         -- query to also find objects where the name is the same but the case
         -- is different (e.g. "cNAME" versus "CNAME").
         -- We do not convert the results to allow us to rename the actual
         -- name of the object as it exists in the data dictionary.
         SELECT table_name,
                column_name
           INTO v_table_name,
                v_old_column_name
           FROM user_tab_columns
          WHERE upper(table_name) = upper(p_table_name)
            AND upper(column_name) = upper(p_old_column_name);

         -- The above query was successful which means that the column exists
         v_exists := c_true;

      EXCEPTION
         WHEN no_data_found THEN

            -- The above query was not successful which means that the column does not exist
            v_exists := c_false;

      END;

      IF v_exists = c_true
      THEN

         v_step := 20;
         -- The column name exists, therefore, run the SQL statement to rename the column.
         -- Wrap double quotation marks around the names to use the exact case
         -- that exists in the data dictionary.
         EXECUTE IMMEDIATE ('ALTER TABLE "' || v_table_name ||
                           '" RENAME COLUMN "' || v_old_column_name || '" TO ' ||
                           upper(p_new_column_name));

         -- Notify the user that the column was rename
         dbms_output.put_line('INFO: The ' || v_old_column_name ||
                              ' column in the ' || v_table_name ||
                              ' table has been renamed to ' ||
                              upper(p_new_column_name) || '.');

      ELSE

         v_step := 30;
         -- Notify the user that the column name did not exist
         dbms_output.put_line('INFO: The ' || p_old_column_name ||
                              ' column did not exist in the ' || p_table_name ||
                              ' table, therefore, it was not renamed.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_column_rename';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_column_rename;

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_cons_chk_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_column_name(VARCHAR2)      - column name
   -- Description: Drop all check and not null constraints from the column in the specified table.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_column_cons_chk_drop('MY_TABLE_NAME', 'MY_COLUMN_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_column_cons_chk_drop(p_table_name  IN VARCHAR2,
                                        p_column_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the check constraint
      drop_column_chk_constraints(p_table_name,
                                  p_column_name,
                                  c_all_check_constraints);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_column_cons_chk_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_column_cons_chk_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   table_column_cons_nn_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_column_name(VARCHAR2)      - column name
   -- Description: Drop not null constraints from the column in the specified table.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_column_cons_nn_drop('MY_TABLE_NAME', 'MY_COLUMN_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_column_cons_nn_drop(p_table_name  IN VARCHAR2,
                                       p_column_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the not null constraint
      drop_column_chk_constraints(p_table_name,
                                  p_column_name,
                                  c_only_not_null_constraints);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_column_cons_nn_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_column_cons_nn_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   table_constraint_add
   -- Arguments:   p_sql_clob(CLOB)              - SQL statement
   -- Description: Creates the contraint with the SQL statement provided if the
   --              contraint name doesn't already exist
   -- SQL Usage:   BEGIN
   --              utl_migr_schema_pkg.table_constraint_add('
   --              Alter table MY_TABLE_NAME add contraint MY_CONSTRAINT_NAME foreign key (MY_COLUMN_NAME) references PARENT_TABLE_NAME (PARENT_COLUMN_NAME)  DEFERRABLE
   --              ');
   --              END;
   --              /
   -- Notes:       Convert ' (single quotes) into '' (two single quotes) and remove the
   --              semi-colon from the end of the SQL statement prior to passing the
   --              SQL statement into the procedure.
   ----------------------------------------------------------------------------
   PROCEDURE table_constraint_add(p_sql_clob IN CLOB) IS

      -- Search pattern constants to parse the table name and column name from the SQL statement
      c_table_name_pattern      CONSTANT VARCHAR2(30) := 'ALTER TABLE ';
      c_constraint_name_pattern CONSTANT VARCHAR2(30) := ' ADD CONSTRAINT ';

      v_step            NUMBER(4);
      v_table_name      user_tab_columns.table_name%TYPE;
      v_constraint_name user_tab_columns.column_name%TYPE;
      v_exists          NUMBER(1);
      v_cursor_id       PLS_INTEGER;
      v_ret_val         NUMBER;

   BEGIN

      v_step := 10;
      -- Extract the table name from the SQL statement
      v_table_name := get_string_after_pattern(c_table_name_pattern,
                                               p_sql_clob);

      v_step := 20;
      -- Extract the constraint name from the SQL statement
      v_constraint_name := get_string_after_pattern(c_constraint_name_pattern,
                                                    p_sql_clob);

      v_step := 30;
      -- Determine if the check constraint name exists
      v_exists := utl_migr_schema_pkg.check_constraint_exists(v_table_name,
                                                              v_constraint_name);

      IF v_exists = c_false
      THEN

         -- The constraint name does not exist, therefore, execute the SQL statement
         -- to alter the table to add the constraint.

         v_step := 40;
         -- Open cursor
         v_cursor_id := dbms_sql.open_cursor;

         v_step := 50;
         -- Parse statement
         dbms_sql.parse(v_cursor_id,
                        p_sql_clob,
                        dbms_sql.native);

         v_step := 60;
         -- Execute statement
         v_ret_val := dbms_sql.EXECUTE(v_cursor_id);

         v_step := 70;
         -- Close cursor
         dbms_sql.close_cursor(v_cursor_id);

         v_step := 80;
         -- Notify the user that the constraint has been added
         dbms_output.put_line('INFO: The ' || upper(v_constraint_name) ||
                              ' constraint has been added to the ' ||
                              v_table_name || ' table.');

      ELSE

         v_step := 90;
         -- Notify the user that the constraint name already existed
         dbms_output.put_line('INFO: The ' || upper(v_constraint_name) ||
                              ' constraint already existed on the ' ||
                              v_table_name ||
                              ' table, therefore, it was not created.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_constraint_add';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         -- Close the dbms_sql cursor if it is still open
         IF dbms_sql.is_open(v_cursor_id)
         THEN
            dbms_sql.close_cursor(v_cursor_id);
         END IF;

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_constraint_add;

   ----------------------------------------------------------------------------
   -- Procedure:   table_constraint_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_constraint_name(VARCHAR2)  - constraint name
   -- Description: Drops the contraint if the contraint name exists
   -- SQL Usage:   exec utl_migr_schema_pkg.table_constraint_drop('MY_TABLE_NAME', 'MY_CONSTRAINT_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_constraint_drop(p_table_name      IN VARCHAR2,
                                   p_constraint_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      drop_constraint(p_table_name,
                      p_constraint_name);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'table_constraint_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END table_constraint_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   trigger_drop
   -- Arguments:   p_trigger_name(VARCHAR2)       - trigger name
   -- Description: Drops trigger if the trigger name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.trigger_drop('MY_TRIGGER_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE trigger_drop(p_trigger_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_trigger_name,
                  c_object_type_trigger);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'trigger_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END trigger_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   type_drop
   -- Arguments:   p_type_name(VARCHAR2)       - type name
   -- Description: Drops type if the type name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.type_drop('MY_TYPE_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE type_drop(p_type_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_type_name,
                  c_object_type_type);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'type_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END type_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   view_drop
   -- Arguments:   p_view_name(VARCHAR2)       - view name
   -- Description: Drops view if the view name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.view_drop('MY_VIEW_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE view_drop(p_view_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to drop the object
      drop_object(p_view_name,
                  c_object_type_view);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'view_drop';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END view_drop;

   ----------------------------------------------------------------------------
   -- Procedure:   view_rename
   -- Arguments:   p_old_view_name(VARCHAR2)       - old view name
   --              p_new_view_name(VARCHAR2)       - new view name
   -- Description: renames view if the old view name exists.
   -- SQL Usage:   exec utl_migr_schema_pkg.view_rename('MY_OLD_VIEW_NAME', 'MY_NEW_VIEW_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE view_rename(p_old_view_name IN VARCHAR2,
                         p_new_view_name IN VARCHAR2) IS

      v_step NUMBER(4);

   BEGIN

      v_step := 10;
      -- Call the private procedure to rename the object
      rename_object(p_old_view_name,
                    p_new_view_name,
                    c_object_type_view);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'view_rename';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END view_rename;

   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Private method bodies
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Function:    check_column_exists
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_column_name(VARCHAR2)         - column name
   -- Return:      NUMBER - BOOLEAN 0 / 1
   -- Description: 1 if the object exists and 0 if it does not
   ----------------------------------------------------------------------------
   FUNCTION check_column_exists(p_table_name  VARCHAR2,
                                p_column_name VARCHAR2) RETURN NUMBER IS

      v_step  NUMBER(4);
      v_count NUMBER;

   BEGIN

      v_step := 10;
      -- Get the count from the data dictionary to determine if the object exists.
      -- Convert the names to uppercase in the where clause of the
      -- query to also find objects where the name is the same but the case
      -- is different (e.g. "cNAME" versus "CNAME").
      SELECT COUNT(*)
        INTO v_count
        FROM user_tab_columns
       WHERE upper(table_name) = upper(p_table_name)
         AND upper(column_name) = upper(p_column_name);

      v_step := 20;
      IF v_count > 0
      THEN
         -- Return the c_true constant to indicate that the object exists
         RETURN c_true;
      ELSE
         -- Return the c_false constant to indicate that the object exists
         RETURN c_false;
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'check_column_exists';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END check_column_exists;

   ----------------------------------------------------------------------------
   -- Function:    check_constraint_exists
   -- Arguments:   p_table_name(VARCHAR2)              - table name
   --              p_constraint_name(VARCHAR2)         - constraint name
   -- Return:      NUMBER - BOOLEAN 0 / 1
   -- Description: 1 if the object exists and 0 if it does not
   ----------------------------------------------------------------------------
   FUNCTION check_constraint_exists(p_table_name      VARCHAR2,
                                    p_constraint_name VARCHAR2) RETURN NUMBER IS

      v_step  NUMBER(4);
      v_count NUMBER;

   BEGIN

      v_step := 10;
      -- Get the count from the data dictionary to determine if the object exists.
      -- Convert the names to uppercase in the where clause of the
      -- query to also find objects where the name is the same but the case
      -- is different (e.g. "cNAME" versus "CNAME").
      SELECT COUNT(*)
        INTO v_count
        FROM user_constraints
       WHERE upper(table_name) = upper(p_table_name)
         AND upper(constraint_name) = upper(p_constraint_name);

      v_step := 20;
      IF v_count > 0
      THEN
         -- Return the c_true constant to indicate that the object exists
         RETURN c_true;
      ELSE
         -- Return the c_false constant to indicate that the object exists
         RETURN c_false;
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'check_constraint_exists';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END check_constraint_exists;

   ----------------------------------------------------------------------------
   -- Function:    check_object_exists
   -- Arguments:   p_object_name(VARCHAR2)              - object name
   --              p_object_type(VARCHAR2)              - object type
   -- Return:      NUMBER - BOOLEAN 0 / 1
   -- Description: 1 if the object exists and 0 if it does not
   ----------------------------------------------------------------------------
   FUNCTION check_object_exists(p_object_name VARCHAR2,
                                p_object_type VARCHAR2) RETURN NUMBER IS

      v_step  NUMBER(4);
      v_count NUMBER;

   BEGIN

      v_step := 10;
      -- Get the count from the data dictionary to determine if the object exists.
      -- Convert the names to uppercase in the where clause of the
      -- query to also find objects where the name is the same but the case
      -- is different (e.g. "cNAME" versus "CNAME").
      SELECT COUNT(*)
        INTO v_count
        FROM user_objects
       WHERE upper(object_name) = upper(p_object_name)
         AND upper(object_type) = upper(p_object_type);

      v_step := 20;
      IF v_count > 0
      THEN
         -- Return the c_true constant to indicate that the object exists
         RETURN c_true;
      ELSE
         -- Return the c_false constant to indicate that the object exists
         RETURN c_false;
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'check_object_exists';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END check_object_exists;

   ----------------------------------------------------------------------------
   -- Procedure:   drop_column_chk_constraints
   -- Arguments:   p_table_name(VARCHAR2)          - table name
   --              p_column_name(VARCHAR2)         - column name
   --              p_chk_constraint_type(VARCHAR2) - check constraint type
   -- Description: Drops the check constraints on the specified column based on the
   --              p_chk_constraint_type parameter.  If this parameter is set to
   --              c_all_check_constraints then all check constraints are dropped
   --              and if the parameter is set to c_only_not_null_constraints then
   --              only not null check constraints are dropped.  This drop is case
   --              insensitive.
   ----------------------------------------------------------------------------
   PROCEDURE drop_column_chk_constraints(p_table_name          IN VARCHAR2,
                                         p_column_name         IN VARCHAR2,
                                         p_chk_constraint_type IN VARCHAR2) IS

      v_step             NUMBER(4);
      v_table_name       user_cons_columns.table_name%TYPE;
      v_column_name      user_cons_columns.column_name%TYPE;
      v_search_condition VARCHAR2(32767);

      -- This cursor will return name and search condition of all of the check
      -- constraints.  Convert the object names to uppercase in the where clause
      -- to handle objects with mixed case names.
      CURSOR cur_constraint_details IS
         SELECT user_constraints.constraint_name,
                user_constraints.search_condition
           FROM user_constraints,
                user_cons_columns
          WHERE user_constraints.table_name = user_cons_columns.table_name
            AND user_constraints.constraint_name =
                user_cons_columns.constraint_name
            AND upper(user_constraints.table_name) = upper(p_table_name)
            AND upper(user_cons_columns.column_name) = upper(p_column_name)
            AND user_constraints.constraint_type = 'C';

   BEGIN

      v_step := 10;
      -- Loop through the check constraints on the specified table
      FOR constraint_detail_rec IN cur_constraint_details
      LOOP

         v_step := 20;
         IF p_chk_constraint_type = c_only_not_null_constraints
         THEN

            -- The p_chk_constraint_type parameter indicates that we should only
            -- drop not null constraints, therefore, drop the current constraint
            -- if the search condition includes the text 'IS NOT NULL'.

            v_step := 30;
            IF upper(constraint_detail_rec.search_condition) LIKE
               '%IS NOT NULL%'
            THEN
               v_step := 40;
               -- Drop the not null check constraint
               drop_constraint(p_table_name,
                               constraint_detail_rec.constraint_name);

            END IF;

         ELSIF p_chk_constraint_type = c_all_check_constraints
         THEN

            -- The p_chk_constraint_type parameter indicates that we should drop
            -- all constraints, therefore, drop the current constraint.

            v_step := 50;
            -- Drop the check constraint
            drop_constraint(p_table_name,
                            constraint_detail_rec.constraint_name);

         ELSE

            -- The p_chk_constraint_type parameter is invalid because it does not match any
            -- of the acceptable constants listed above, therefore, raise an error.

            v_step := 60;
            -- Raise invalid check constraint type error
            raise_application_error(-20002,
                                    'Invalid check constraint type for the drop_column_chk_constraints procedure.',
                                    TRUE);

         END IF;

      END LOOP;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'drop_column_chk_constraints';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END drop_column_chk_constraints;

   ----------------------------------------------------------------------------
   -- Procedure:   drop_constraint
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_constraint_name(VARCHAR2)  - constraint name
   -- Description: Drops the contraint if the contraint name exists.
   --              This drop is case insensitive.
   ----------------------------------------------------------------------------
   PROCEDURE drop_constraint(p_table_name      IN VARCHAR2,
                             p_constraint_name IN VARCHAR2) IS

      v_step             NUMBER(4);
      v_table_name       user_constraints.table_name%TYPE;
      v_constraint_name  user_constraints.constraint_name%TYPE;
      v_search_condition VARCHAR2(32767);
      v_exists           NUMBER(1);

   BEGIN

      v_step := 10;
      BEGIN

         -- Query the data dictionary to get the constraint information.
         -- Convert the names to uppercase in the where clause of the
         -- query to also find objects where the name is the same but the case
         -- is different (e.g. "cNAME" versus "CNAME").
         -- We do not convert the results to allow us to drop the actual
         -- name of the object as it exists in the data dictionary.
         SELECT table_name,
                constraint_name,
                search_condition
           INTO v_table_name,
                v_constraint_name,
                v_search_condition
           FROM user_constraints
          WHERE upper(table_name) = upper(p_table_name)
            AND upper(constraint_name) = upper(p_constraint_name);

         -- The above query was successful which means that the constraint exists
         v_exists := c_true;

      EXCEPTION
         WHEN no_data_found THEN

            -- The above query was not successful which means that the constraint does not exist
            v_exists := c_false;

      END;

      v_step := 20;
      IF v_exists = c_true
      THEN

         v_step := 30;
         -- The constraint name exists, therefore, run the SQL statement to drop the constraint.
         -- Wrap double quotation marks around the names to use the exact case
         -- that exists in the data dictionary.
         EXECUTE IMMEDIATE ('ALTER TABLE "' || v_table_name ||
                           '" DROP CONSTRAINT "' || v_constraint_name || '"');

         v_step := 40;
         -- For check constraints add parentheses around the search condition so that
         -- it can be added to the message that is provided to the user.  We do not
         -- add parentheses if the search condition is null since for those cases
         -- (e.g. foreign keys) there is no search condition to return to the user.
         IF v_search_condition IS NOT NULL
         THEN

            v_step             := 50;
            v_search_condition := '(' || v_search_condition || ') ';

         END IF;

         v_step := 60;
         -- Inform the user that the constraint has been dropped
         dbms_output.put_line('INFO: The ' || v_constraint_name ||
                              ' constraint ' || v_search_condition ||
                              'of the ' || v_table_name ||
                              ' table has been dropped.');

      ELSE

         v_step := 70;
         -- Inform the user that the constraint does not exist
         dbms_output.put_line('INFO: The ' || p_constraint_name ||
                              ' constraint of the ' || p_table_name ||
                              ' does not exist, therefore, it was not dropped.');

      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'drop_constraint';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END drop_constraint;

   ----------------------------------------------------------------------------
   -- Procedure:   drop_object
   -- Arguments:   p_object_name(VARCHAR2)              - object name
   --              p_object_type(VARCHAR2)              - object type
   -- Description: Drop the object if the object name exists.
   --              This drop is case insensitive.
   ----------------------------------------------------------------------------
   PROCEDURE drop_object(p_object_name IN VARCHAR2,
                         p_object_type IN VARCHAR2) IS

      v_step        NUMBER(4);
      v_object_name user_objects.object_name%TYPE;
      v_object_type user_objects.object_type%TYPE;
      v_exists      NUMBER(1);
      v_sql         VARCHAR2(32767);

   BEGIN

      v_step := 10;
      BEGIN

         -- Query the data dictionary to get the object information.
         -- Convert the names to uppercase in the where clause of the
         -- query to also find objects where the name is the same but the case
         -- is different (e.g. "cNAME" versus "CNAME").
         -- We do not convert the results to allow us to drop the actual
         -- name of the object as it exists in the data dictionary.
         SELECT object_name,
                object_type
           INTO v_object_name,
                v_object_type
           FROM user_objects
          WHERE upper(object_name) = upper(p_object_name)
            AND upper(object_type) = upper(p_object_type);

         -- The above query was successful which means that the object exists
         v_exists := c_true;

      EXCEPTION
         WHEN no_data_found THEN

            -- The above query was not successful which means that the object does not exist
            v_exists := c_false;

      END;

      v_step := 20;
      IF v_exists = c_true
      THEN

         v_step := 30;
         -- The object name exists, therefore, build the SQL statement to drop the object.
         -- Wrap double quotation marks around the names to use the exact case
         -- that exists in the data dictionary.
         v_sql := 'DROP ' || v_object_type || ' "' || v_object_name || '"';

         v_step := 40;
         -- Check the object type to determine if any extra clauses need to be
         -- added to the drop statement.
         IF p_object_type = c_object_type_table
         THEN

            v_step := 50;
            -- If the object type is a TABLE, then add the PURGE clause to the SQL
            -- to keep the dropped table from going into the recyclebin
            v_sql := v_sql || ' PURGE';

         ELSIF p_object_type = c_object_type_type
         THEN

            v_step := 60;
            -- If the object type is a TYPE, then add the FORCE clause to the SQL
            -- because types won't drop if they have dependant objects
            v_sql := v_sql || ' FORCE';

         END IF;

         v_step := 70;
         -- The object exists, therefore, run the SQL statement to drop the object.
         EXECUTE IMMEDIATE (v_sql);

         -- Inform the user that the object was dropped
         dbms_output.put_line('INFO: The ' || v_object_name || ' ' ||
                              lower(v_object_type) || ' has been dropped.');

      ELSE

         v_step := 80;
         -- Inform the user that the object did not exist
         dbms_output.put_line('INFO: The ' || p_object_name || ' ' ||
                              lower(p_object_type) ||
                              ' does not exist, therefore, it was not dropped.');

      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'drop_object';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END drop_object;

   ----------------------------------------------------------------------------
   -- Function:    get_index_name
   -- Arguments:   p_table_name (VARCHAR2)          - Table name
   --              p_column_list (VARCHAR2)         - Comma separated column list
   -- Return:      VARCHAR2 - Index name
   -- Description: Returns the index name for the specified table name and column list.
   --              This function is used by the create_index procedure to determine
   --              if the index already exists with the same or different name.
   --              Returns NULL if the index is not found.
   ----------------------------------------------------------------------------
   FUNCTION get_index_name(p_table_name  VARCHAR2,
                           p_column_list VARCHAR2) RETURN VARCHAR2 IS

      v_step         NUMBER(4);
      v_table_name   user_indexes.table_name%TYPE;
      v_column_list  VARCHAR2(32767);
      v_compare_list VARCHAR2(32767);
      v_index_name   user_indexes.index_name%TYPE;

      -- Pull the index details to build the column list for comparison purposes.
      -- The query includes the maximum column position for each index to identify
      -- when we are processing the last column in the index.
      CURSOR cur_index_details IS
         SELECT index_name,
                column_name,
                column_position,
                MAX(column_position) over(PARTITION BY index_name) max_column_position
           FROM user_ind_columns
          WHERE table_name = upper(p_table_name)
          GROUP BY index_name,
                   column_name,
                   column_position
          ORDER BY index_name,
                   column_position;

      index_details_rec cur_index_details%ROWTYPE;

   BEGIN

      v_step := 10;
      -- Modify the column list to convert it to upper case
      v_column_list := upper(p_column_list);

      v_step := 20;
      -- Modify the column list to remove the following characters listed below
      v_column_list := REPLACE(v_column_list,
                               '"'); -- double quotes
      v_column_list := REPLACE(v_column_list,
                               ''''); -- single quotes
      v_column_list := REPLACE(v_column_list,
                               '('); -- opening parenthesis
      v_column_list := REPLACE(v_column_list,
                               ')'); -- closing parenthesis
      v_column_list := REPLACE(v_column_list,
                               chr(9)); -- tab
      v_column_list := REPLACE(v_column_list,
                               chr(10)); -- linefeed
      v_column_list := REPLACE(v_column_list,
                               chr(13)); -- carriage return
      v_column_list := REPLACE(v_column_list,
                               chr(32)); -- space

      v_step := 30;
      -- Open the index detail cursor
      OPEN cur_index_details;

      v_step := 40;
      LOOP

         v_step := 50;
         -- Fetch the next record from the cursor
         FETCH cur_index_details
            INTO index_details_rec;
         EXIT WHEN cur_index_details%NOTFOUND;

         v_step := 60;
         -- Determine if this is the first column in the index
         IF index_details_rec.column_position = 1
         THEN

            v_step := 70;
            -- This is the first column in the index, therefore, set the
            -- compare list to the current column
            v_compare_list := upper(index_details_rec.column_name);

         ELSE

            v_step := 80;
            -- This is not the first column in the index, therefore, add the
            -- current column to the end of the compare list
            v_compare_list := v_compare_list || ',' ||
                              upper(index_details_rec.column_name);

         END IF;

         v_step := 90;
         -- If the column position matches the maximum column position for the index
         -- then that indicates that the current column is the last column for this index
         IF index_details_rec.column_position =
            index_details_rec.max_column_position
         THEN

            v_step := 100;
            -- Check if the column list for this index matches the column list parameter
            IF v_compare_list = v_column_list
            THEN

               v_step := 110;
               -- The column lists are the same, therefore, set the index name to the
               -- current index and exit the loop
               v_index_name := index_details_rec.index_name;
               EXIT;

            END IF;
         END IF;

      END LOOP;

      v_step := 120;
      -- Close the event detail cursor
      CLOSE cur_index_details;

      v_step := 130;
      RETURN v_index_name;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'get_index_name';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         -- Close the cursor if it is still open
         IF cur_index_details%ISOPEN
         THEN
            CLOSE cur_index_details;
         END IF;

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END get_index_name;

   ----------------------------------------------------------------------------
   -- Function:    get_string_after_pattern
   -- Arguments:   p_pattern(VARCHAR2)            - Preceding Pattern
   --              p_sql(VARCHAR2)                - SQL statement
   -- Return:      VARCHAR2 - Parameter
   -- Description: Extracts the first word after the p_pattern of the SQL statement.
   --              This function is used to extract table_name, column_name, etc.
   --              from SQL statements that are passed in as parameters.
   --              Returns NULL if the pattern is not found.
   ----------------------------------------------------------------------------
   FUNCTION get_string_after_pattern(p_pattern VARCHAR2,
                                     p_sql     VARCHAR2) RETURN VARCHAR2 IS

      v_step             NUMBER(4);
      v_sql              VARCHAR2(32767);
      v_sql_length       NUMBER(4);
      v_pattern_length   NUMBER(4);
      v_position         NUMBER;
      v_char             CHAR(1);
      v_parm_start_found NUMBER := c_false;
      v_parm_end_found   NUMBER := c_false;
      v_output           VARCHAR2(32767);

   BEGIN

      v_step := 10;
      -- This function will parse the SQL statement based on the space character CHR(32)
      -- therefore, convert all of the following characters into CHR(32).
      v_sql := REPLACE(p_sql,
                       '"',
                       chr(32)); -- double quotes
      v_sql := REPLACE(v_sql,
                       '''',
                       chr(32)); -- single quotes
      v_sql := REPLACE(v_sql,
                       ',',
                       chr(32)); -- comma
      v_sql := REPLACE(v_sql,
                       '(',
                       chr(32)); -- opening parenthesis
      v_sql := REPLACE(v_sql,
                       ')',
                       chr(32)); -- closing parenthesis
      v_sql := REPLACE(v_sql,
                       chr(9),
                       chr(32)); -- tab
      v_sql := REPLACE(v_sql,
                       chr(10),
                       chr(32)); -- linefeed
      v_sql := REPLACE(v_sql,
                       chr(13),
                       chr(32)); -- carriage return

      -- Determine of the length of the SQL and pattern strings
      v_sql_length     := length(v_sql);
      v_pattern_length := length(p_pattern);

      -- Locate the position of the pattern in the SQL statement.  Leading and trailing
      -- spaces are included in this search to make sure the pattern is not found within
      -- another word in the SQL statement.
      v_position := instr(upper(v_sql),
                          upper(p_pattern));

      v_step := 20;
      -- If the pattern is found then extract the next word in the SQL statement
      IF v_position > 0
      THEN

         v_step := 30;
         -- Advance the position to the end of the search pattern
         v_position := v_position + v_pattern_length;

         -- Process each character in the SQL string until the end of the
         -- parameter or end of the SQL string is found
         WHILE v_parm_end_found = c_false
               AND v_position <= v_sql_length
         LOOP

            v_step := 40;
            -- Set the variable to the current character
            v_char := substr(v_sql,
                             v_position,
                             1);

            -- If the parameter start has been found and another
            -- space is found then set the flag to indicate that
            -- the end of the parameter has been found
            IF v_parm_start_found = c_true
               AND v_char = chr(32)
            THEN
               v_step           := 50;
               v_parm_end_found := c_true;
            END IF;

            -- If the start of the parameter has not been found
            -- and the current character is not a space then
            -- set the flag to indicate that the parameter has
            -- been found
            IF v_parm_start_found = c_false
               AND v_char <> chr(32)
            THEN
               v_step             := 60;
               v_parm_start_found := c_true;
            END IF;

            -- If the parameter start has been found and the
            -- parameter end has not then add the current
            -- character to the output string
            IF v_parm_start_found = c_true
               AND v_parm_end_found = c_false
            THEN
               v_step   := 70;
               v_output := v_output || v_char;
            END IF;

            -- Advance the position by 1 to process the next character
            v_position := v_position + 1;

         END LOOP;

      END IF;

      v_step := 80;
      RETURN v_output;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'get_string_after_pattern';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END get_string_after_pattern;

   ----------------------------------------------------------------------------
   -- Function:    get_string_between_parentheses
   -- Arguments:   p_sql(VARCHAR2)                        - SQL statement
   -- Return:      VARCHAR2 - Column list
   -- Description: Extracts the string between the first opening parenthesis and the
   --              last closing parenthesis.  This function is used to extract the
   --              column_list from a create index SQL statement.
   --              Returns NULL if the parentheses are not found.
   ----------------------------------------------------------------------------
   FUNCTION get_string_between_parentheses(p_sql VARCHAR2) RETURN VARCHAR2 IS

      v_step           NUMBER(4);
      v_start_position NUMBER;
      v_end_position   NUMBER;
      v_length         NUMBER;
      v_output         VARCHAR2(32767);

   BEGIN

      v_step := 10;
      -- Locate the position of the first opening parenthesis in the SQL string
      v_start_position := instr(p_sql,
                                '(',
                                1);

      -- Locate the position of the last closing parenthesis in the SQL string
      -- (Note: The postion -1 parameter in the INSTR call causes it to search
      --        backward from the end of the string)
      v_end_position := instr(p_sql,
                              ')',
                              -1);

      v_step := 20;
      -- Check if start and end positions are found and that the end position
      -- is greater than the start position.
      IF v_start_position > 0
         AND v_end_position > 0
         AND v_end_position > v_start_position
      THEN

         v_step := 30;
         -- The start and end positions were found, therefore, set the start position
         -- and length of the output string
         v_start_position := v_start_position + 1;
         v_length         := v_end_position - v_start_position;

         v_step := 40;
         -- Build the output string using the start position and length variables
         v_output := substr(p_sql,
                            v_start_position,
                            v_length);

      END IF;

      v_step := 50;
      RETURN v_output;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'get_string_between_parentheses';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END get_string_between_parentheses;

   ----------------------------------------------------------------------------
   -- Procedure:   rename_object
   -- Arguments:   p_old_object_name(VARCHAR2)              - old object name
   --              p_new_object_name(VARCHAR2)              - new object name
   --              p_object_type(VARCHAR2)                  - object type
   -- Description: Case insensitive rename of the object with the specified name and type
   --              if the old object name/type combination exists.
   ----------------------------------------------------------------------------
   PROCEDURE rename_object(p_old_object_name IN VARCHAR2,
                           p_new_object_name IN VARCHAR2,
                           p_object_type     IN VARCHAR2) IS

      v_step            NUMBER(4);
      v_old_object_name user_objects.object_name%TYPE;
      v_object_type     user_objects.object_type%TYPE;
      v_exists          NUMBER(1);

   BEGIN

      v_step := 10;
      -- Get the exact object name from the data dictionary.
      BEGIN

         -- Query the data dictionary to get the object name.
         -- Convert the names to uppercase in the where clause of the
         -- query to also find objects where the name is the same but the case
         -- is different (e.g. "cNAME" versus "CNAME").
         -- We do not convert the results to allow us to rename the actual
         -- name of the object as it exists in the data dictionary.
         SELECT object_name
           INTO v_old_object_name
           FROM user_objects
          WHERE upper(object_name) = upper(p_old_object_name)
            AND upper(object_type) = upper(p_object_type);

         -- The above query was successful which means that the object exists
         v_exists := c_true;

      EXCEPTION
         WHEN no_data_found THEN

            -- The above query was not successful which means that the object does not exist
            v_exists := c_false;

      END;

      v_step := 20;
      IF v_exists = c_true
      THEN

         v_step := 30;
         -- The object name exists, therefore, run the SQL statement to rename the object.
         -- Wrap double quotation marks around the name to use the exact case
         -- that exists in the data dictionary.
         EXECUTE IMMEDIATE ('RENAME "' || v_old_object_name || '" TO ' ||
                           upper(p_new_object_name));

         -- Notify the user that the object was renamed
         dbms_output.put_line('INFO: The ' || lower(p_object_type) || ' ' ||
                              v_old_object_name || ' has been renamed to ' ||
                              upper(p_new_object_name) || '.');

      ELSE

         v_step := 40;
         -- Notify the user that the object did not exist
         dbms_output.put_line('INFO: The ' || lower(p_object_type) || ' ' ||
                              p_old_object_name ||
                              ' does not exist, therefore, it was not renamed.');

      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'rename_object';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END rename_object;

   ----------------------------------------------------------------------------
   -- Function:    replace_ignore_case_and_spaces
   -- Arguments:   p_source_string(VARCHAR2)                - source string
   --              p_search_string(VARCHAR2)                - search string
   --              p_replacement_string(VARCHAR2 OPTIONAL)  - replacement string
   -- Return:      VARCHAR2 - Source string with the search string replaced.
   -- Description: Case insensitive replace function that ignores spaces.  Returns source string with
   --              the FIRST case insensitive space excluded occurrence of search_string replaced
   --              with replacement_string.  Leading or trailing spaces in the search string will not
   --              be replaced since the function is ignoring spaces. If replacement_string is omitted
   --              or null, then the first occurrence of search_string is removed.
   --              Returns the original string if the search string is not found.
   -- Example:     replace_ignore_case_and_spaces('One Two    T h r e e Four', 'two three')
   -- Output:      Example input returns 'One  Four' even though case and spacing of the search string
   --              is different than the source string.
   ----------------------------------------------------------------------------
   FUNCTION replace_ignore_case_and_spaces(p_source_string      VARCHAR2,
                                           p_search_string      VARCHAR2,
                                           p_replacement_string VARCHAR2 DEFAULT NULL)
      RETURN VARCHAR2 IS

      v_step            NUMBER(4);
      v_source_upper    VARCHAR2(32767);
      v_search_upper    VARCHAR2(32767);
      v_start_position  NUMBER(4);
      v_search_position NUMBER(4);
      v_space_position  NUMBER(4);
      v_output          VARCHAR2(32767);

   BEGIN

      v_step := 10;
      -- Create uppercase versions of the source and search strings with spaces removed
      v_source_upper := upper(REPLACE(p_source_string,
                                      ' '));
      v_search_upper := upper(REPLACE(p_search_string,
                                      ' '));

      -- Identify the position of the modified search string in the modified source string
      v_search_position := instr(v_source_upper,
                                 v_search_upper);

      IF v_search_position = 0
      THEN

         v_step := 20;
         -- If the search string was not found then return the original source string
         v_output := p_source_string;

      ELSE

         v_step := 30;
         ------------------------------------------------
         -- ADD THE STARTING SECTION TO THE OUTPUT STRING
         ------------------------------------------------

         v_start_position := 1;

         -- Since spaces were removed for the comparison we need to adjust the search position
         -- for all spaces located before the search string.

         -- Identify the position of the first space in the source string
         v_space_position := instr(p_source_string,
                                   ' ');

         v_step := 40;
         -- For each space found before or equal to the search position increase the search position by 1
         WHILE v_space_position > 0
               AND v_space_position <= v_search_position
         LOOP

            v_step := 50;
            -- Increment the start position to account for the space that was found
            v_search_position := v_search_position + 1;

            -- Find the position of the next space
            v_space_position := instr(p_source_string,
                                      ' ',
                                      v_space_position + 1);

         END LOOP;

         v_step := 60;
         -- Set the output string to the substring from in front of the search string
         v_output := substr(p_source_string,
                            v_start_position,
                            v_search_position - v_start_position);

         ----------------------------------------------
         -- ADD THE MIDDLE SECTION TO THE OUTPUT STRING
         ----------------------------------------------

         v_step := 70;
         -- Add the replacement string to the output string
         v_output := v_output || p_replacement_string;

         ----------------------------------------------
         -- ADD THE ENDING SECTION TO THE OUTPUT STRING
         ----------------------------------------------

         v_step := 80;

         -- Identify the position of the first space in the part of the source that is being replaced
         v_space_position := instr(p_source_string,
                                   ' ',
                                   v_search_position);

         -- Set the new search position to the old search position plus the length of the search string
         v_search_position := v_search_position + length(v_search_upper);

         v_step := 90;
         -- For each space found before or equal to the search position increase the search position by 1
         WHILE v_space_position > 0
               AND v_space_position < v_search_position
         LOOP

            v_step := 100;
            -- Increment the start position to account for the space that was found
            v_search_position := v_search_position + 1;

            -- Find the position of the next space
            v_space_position := instr(p_source_string,
                                      ' ',
                                      v_space_position + 1);

         END LOOP;

         v_step := 110;
         -- Set the start position for the ending section to the current search position
         v_start_position := v_search_position;

         -- Add the substring from after the search string to the output string
         v_output := v_output ||
                     substr(p_source_string,
                            v_start_position,
                            (length(p_source_string) - v_start_position + 1));

      END IF;

      v_step := 120;
      RETURN v_output;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code    := SQLCODE;
         v_method_name := 'replace_ignore_case_and_spaces';
         v_err_msg     := substr(c_pkg_name || '.' || v_method_name ||
                                 ', STEP: ' || v_step || ', ERROR: ' || SQLERRM,
                                 1,
                                 2000);

         raise_application_error(-20001,
                                 v_err_msg,
                                 TRUE);

   END replace_ignore_case_and_spaces;

   -----------------------------------------------------------------------------
   -- Procedure: set_disable_redo_logging_param
   -- Arguments: p_disable_redo_logging(BOOLEAN)
   -- Description: get parameter from build.properties file and set instance variable
   --		   upgrade_disable_redo_logging
   -- SQL Usage: exec utl_migr_schema_pkg.set_disable_redo_logging_param(TRUE)
   -----------------------------------------------------------------------------
   PROCEDURE set_disable_redo_logging_param(p_disable_redo_logging IN BOOLEAN) IS
   BEGIN
       upgrade_disable_redo_logging := p_disable_redo_logging;
   END set_disable_redo_logging_param;

   -----------------------------------------------------------------------------
   -- Function:  get_disable_redo_logging_param
   -- Arguments:
   -- Return:    v_disable_redo_logging(BOOLEAN)
   -- Description: get value from instance variable for disable redo logging parameter
   -- SQL Usage: exec utl_migr_schema_pkg.get_disable_redo_logging_param
   -----------------------------------------------------------------------------
   FUNCTION get_disable_redo_logging_param return BOOLEAN IS
   	v_disable_redo_logging BOOLEAN;
   BEGIN
   	v_disable_redo_logging := upgrade_disable_redo_logging;
      return (v_disable_redo_logging);
   END get_disable_redo_logging_param;

   -----------------------------------------------------------------------------
   -- Procedure: set_defer_column_drop_param
   -- Arguments: p_defer_column_drop(BOOLEAN)
   -- Description: get parameter value from build.properties file and set upgrade_defer_column_drop
   -- SQL Usage: exec utl_migr_schema_pkg.set_defer_column_drop_param(TRUE)
   -----------------------------------------------------------------------------
   PROCEDURE set_defer_column_drop_param(p_defer_column_drop IN BOOLEAN) IS
   BEGIN
       upgrade_defer_column_drop := p_defer_column_drop;
   END set_defer_column_drop_param;

   -----------------------------------------------------------------------------
   -- Function:    get_defer_column_drop_param
   -- Arguments:
   -- Return:      v_defer_column_drop(BOOLEAN)
   -- Description: get parameter value from instance variable for defer column drop
   -- SQL Usage:   exec utl_migr_schema_pkg.get_defer_column_drop_param
   -----------------------------------------------------------------------------
   FUNCTION get_defer_column_drop_param return BOOLEAN IS
      v_defer_column_drop BOOLEAN;
   BEGIN
      v_defer_column_drop := upgrade_defer_column_drop;
      return (v_defer_column_drop);
   END get_defer_column_drop_param;

END utl_migr_schema_pkg;
/