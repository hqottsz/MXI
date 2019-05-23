/********************************************************************************
*
* Script:        chk_unindexed_foreign_keys.sql
*
* Description:   This script validates that all foreign keys are indexed 
*                excluding foreign keys involving static tables (REF, UTL, or MIM).
*                If the foreign key is not indexed then the script will identify
*                the foregn key name, table, and columns. If any unindexed 
*                foreign keys are found then the script will raise an error
*                to make sure that foreign key columns are indexed.
*
* Orig.Coder:    Mark Rutherford
* Recent Coder:
* Recent Date:   January 10, 2012
*
*********************************************************************************/

SET SERVEROUTPUT ON SIZE 1000000
SET DEFINE ON
SET LINESIZE 1000
SET WRAP ON
SET TIMING ON

-- Declare the SQL Plus variable for the table error list to 
-- allow an error to be raised in a separate PLSQL block.  
VAR v_error_tables VARCHAR2(2000)

DECLARE

   CURSOR cur_unindexed_fks IS
   -- Find unindexed foreign keys excluding foreign keys involving 
   -- static tables (REF, UTL, or MIM).
   --
   -- NOTE: The following query is based on Tom Kyte's unindexed
   --       foreign key query
   --
    SELECT fk.table_name table_name,
           fk.constraint_name fk_constraint_name,
           fk.cname1 || NVL2(fk.cname2, ',' || fk.cname2, NULL) ||
           NVL2(fk.cname3, ',' || fk.cname3, NULL) ||
           NVL2(fk.cname4, ',' || fk.cname4, NULL) ||
           NVL2(fk.cname5, ',' || fk.cname5, NULL) ||
           NVL2(fk.cname6, ',' || fk.cname6, NULL) ||
           NVL2(fk.cname7, ',' || fk.cname7, NULL) ||
           NVL2(fk.cname8, ',' || fk.cname8, NULL) ||
           NVL2(fk.cname9, ',' || fk.cname9, NULL) ||
           NVL2(fk.cname10, ',' || fk.cname10, NULL) ||
           NVL2(fk.cname11, ',' || fk.cname11, NULL) ||
           NVL2(fk.cname12, ',' || fk.cname12, NULL) fk_constraint_columns
      FROM (-- Gather the details for the foreign key
            SELECT user_constraints.table_name,
                   user_constraints.constraint_name,
                   user_constraints.r_constraint_name,
                   MAX(DECODE(fk_columns.position, 1, fk_columns.column_name, NULL)) cname1,
                   MAX(DECODE(fk_columns.position, 2, fk_columns.column_name, NULL)) cname2,
                   MAX(DECODE(fk_columns.position, 3, fk_columns.column_name, NULL)) cname3,
                   MAX(DECODE(fk_columns.position, 4, fk_columns.column_name, NULL)) cname4,
                   MAX(DECODE(fk_columns.position, 5, fk_columns.column_name, NULL)) cname5,
                   MAX(DECODE(fk_columns.position, 6, fk_columns.column_name, NULL)) cname6,
                   MAX(DECODE(fk_columns.position, 7, fk_columns.column_name, NULL)) cname7,
                   MAX(DECODE(fk_columns.position, 8, fk_columns.column_name, NULL)) cname8,
                   MAX(DECODE(fk_columns.position, 9, fk_columns.column_name, NULL)) cname9,
                   MAX(DECODE(fk_columns.position, 10, fk_columns.column_name, NULL)) cname10,
                   MAX(DECODE(fk_columns.position, 11, fk_columns.column_name, NULL)) cname11,
                   MAX(DECODE(fk_columns.position, 12, fk_columns.column_name, NULL)) cname12,
                   COUNT(*) col_cnt
              FROM (SELECT SUBSTR(user_cons_columns.table_name, 1, 30) table_name,
                           SUBSTR(user_cons_columns.constraint_name, 1, 30) constraint_name,
                           SUBSTR(user_cons_columns.column_name, 1, 30) column_name,
                           user_cons_columns.position
                      FROM user_cons_columns) fk_columns,
                   user_constraints
             WHERE user_constraints.constraint_name = fk_columns.constraint_name
               AND user_constraints.constraint_type = 'R'
             GROUP BY user_constraints.table_name,
                      user_constraints.constraint_name,
                      user_constraints.r_constraint_name) fk,
           (-- Gather the details for the parent key
            SELECT user_constraints.table_name,
                   user_constraints.constraint_name
              FROM user_constraints
             WHERE user_constraints.constraint_type = 'P') pk
     WHERE fk.r_constraint_name = pk.constraint_name
       -- Only return foreign keys that have more columns than 
       -- any of the indexes on the same columns on the same table
       -- since this means that the foreign key is unindexed.
       AND col_cnt > ALL
     (-- Count the number of columns per index for the indexes that are on 
      -- the same table and columns as the foreign key index.  These results
      -- are used by the "col_cnt > ALL" condition above to determine if the 
      -- foreign key is indexed.
      SELECT COUNT(*)
              FROM user_ind_columns
             WHERE user_ind_columns.table_name = fk.table_name
               AND user_ind_columns.column_name IN
                   (fk.cname1, fk.cname2, fk.cname3, fk.cname4, fk.cname5, fk.cname6,
                    fk.cname7, fk.cname8, fk.cname9, fk.cname10, fk.cname11,
                    fk.cname12)
               AND user_ind_columns.column_position <= fk.col_cnt
             GROUP BY user_ind_columns.index_name)
       -- Exclude static parent tables based on table names starting with REF, UTL, or MIM (except MIM_DATA_TYPE)
       AND (pk.table_name NOT LIKE 'REF%' AND pk.table_name NOT LIKE 'UTL%' AND
           (pk.table_name NOT LIKE 'MIM%' OR pk.table_name = 'MIM_DATA_TYPE'))
       -- Exclude static child tables based on table names starting with REF, UTL, or MIM
       AND (fk.table_name NOT LIKE 'REF%' AND fk.table_name NOT LIKE 'UTL%' AND
           fk.table_name NOT LIKE 'MIM%')
     ORDER BY fk.table_name,
              fk.constraint_name;
    
    v_error_count       NUMBER(10);
    v_previous_table    VARCHAR2(30);

BEGIN

   -- Initialize variables
   :v_error_tables := NULL;
   v_previous_table := 'EMPTY';
   v_error_count := 0;
   
   DBMS_OUTPUT.PUT_LINE(CHR(9));
   DBMS_OUTPUT.PUT_LINE('CHECKING FOR UNINDEXED FOREIGN KEY CONSTRAINTS');
   DBMS_OUTPUT.PUT_LINE('-------------------------------------------------------------------');
   
   -- Loop through the unindexed foreign keys
   FOR rec_unindexed_fks IN cur_unindexed_fks
   LOOP   

      -- Increment the error count
      v_error_count := v_error_count + 1;
      
      -- Output the unindexed foreign key name with the corresponding table and column names
      DBMS_OUTPUT.PUT_LINE('- Unindexed foreign key ' || rec_unindexed_fks.fk_constraint_name || ' on table ' || rec_unindexed_fks.table_name || ' on columns ' || rec_unindexed_fks.fk_constraint_columns || '.');
     
      -- Add the table name to the list of error tables if it hasn't already been added to the list.
      IF rec_unindexed_fks.table_name <> v_previous_table
      THEN
      
         -- Add a comma to the table error list if this is not the first table 
         IF :v_error_tables IS NOT NULL THEN
            :v_error_tables := SUBSTR(:v_error_tables || ', ', 1, 2000);
         END IF;
         
         -- Add the current table to the table error list
         :v_error_tables := SUBSTR(:v_error_tables || LOWER(rec_unindexed_fks.table_name), 1, 2000);
     
      END IF;
      
      -- Set the v_previous_table variable to the value of the current table
      v_previous_table := rec_unindexed_fks.table_name;  
     
   END LOOP;
   
   -- Output the total count of unindexed foreign key names
   DBMS_OUTPUT.PUT_LINE(CHR(9));
   DBMS_OUTPUT.PUT_LINE('UNINDEXED FOREIGN KEYS: ' || v_error_count);
   
   IF v_error_count > 0
   THEN

      -- If any errors occurred then notify the user that they must index the unindexed foreign keys
      DBMS_OUTPUT.PUT_LINE(CHR(9));
      DBMS_OUTPUT.PUT_LINE('SOLUTION: CREATE INDEXES FOR THE UNINDEXED FOREIGN KEYS IDENTIFIED ABOVE.');
   
   END IF;
      
END;
/

-- If any errors were detected then raise an Oracle error to fail the build.
-- Include the list of tables in the error message rather than the foreign key 
-- names (which may be cryptic) to help developers identify if the error was
-- caused by their commit.  The detailed list of unindexed foreign keys will 
-- appear in the log above this error.
--
-- NOTE:  We are raising the error in a separate PLSQL block to make the log
-- easier to read.  Oracle may spool the PLSQL block when the error is raised
-- so if an error is raised in a large block then users may not see that the
-- error details are available above the spooled block.  However, by raising
-- the error in the a separate small PLSQL block then the details output by
-- the previous block will be logged immediatly above the error message.
BEGIN

	-- Check if v_error_tables is not empty
   IF :v_error_tables IS NOT NULL THEN

		-- The v_error_tables is not empty, therefore, raise an error to fail the build 
      raise_application_error (-20001,'UNINDEXED FOREIGN KEYS FOUND IN TABLES: ' || :v_error_tables);
   
   END IF;
   
END;
/




