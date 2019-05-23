--liquibase formatted sql


--changeSet utl_migr_schema_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   -- Procedure:   table_column_cons_unq_drop
   -- Arguments:   p_table_name(VARCHAR2)       - table name
   --              p_column_name(VARCHAR2)      - column name
   -- Description: Drop constraint from the column in the specified table.
   -- SQL Usage:   exec utl_migr_schema_pkg.table_column_cons_unq_drop('MY_TABLE_NAME', 'MY_COLUMN_NAME');
   ----------------------------------------------------------------------------
   PROCEDURE table_column_cons_unq_drop(p_table_name  IN VARCHAR2,
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