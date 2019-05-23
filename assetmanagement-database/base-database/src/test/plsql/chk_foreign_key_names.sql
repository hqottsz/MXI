/********************************************************************************
*
* Script:        chk_foreign_key_names.sql
*
* Description:   This script validates that all foreign key names use the 
*                approved standard prefix of "FK_".  If any invalid 
*                foreign key names are found then the script will raise an
*                error to make sure that foreign key is renamed.
*
* Orig.Coder:    Mark Rutherford
* Recent Coder:
* Recent Date:   January 9, 2012
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

   CURSOR cur_invalid_fk_names IS
   -- Find foreign key names that do not start with "FK_" which is 
   -- the approved standard for foreign key constraits.
   --
   -- NOTE: The escape clause is used here to make the like clause match the
   --       literal underscore since the underscore will be compared as a 
   --       wildcard if not escaped.
   --
   SELECT table_name,
          constraint_name
     FROM user_constraints
    WHERE constraint_type = 'R'
      AND constraint_name NOT LIKE 'FK\_%' ESCAPE '\'
    ORDER BY table_name,
             constraint_name;
       
    v_error_count       NUMBER(10);
    v_previous_table    VARCHAR2(30);

BEGIN

   -- Initialize variables
   :v_error_tables := NULL;
   v_previous_table := 'EMPTY';
   v_error_count := 0;
   
   -- Output a header for the invalid foreign key names
   DBMS_OUTPUT.PUT_LINE(CHR(9));
   DBMS_OUTPUT.PUT_LINE('CHECKING FOR INVALID FOREIGN KEY NAMES');
   DBMS_OUTPUT.PUT_LINE('-------------------------------------------------------------------');
   
   -- Loop through the invalid foreign key names
   FOR rec_invalid_fk_names IN cur_invalid_fk_names
   LOOP   

      -- Increment the error count
      v_error_count := v_error_count + 1;

      -- Output the invalid foreign key name with the corresponding table name     
      DBMS_OUTPUT.PUT_LINE('- Invalid foreign key name ' || rec_invalid_fk_names.constraint_name || ' on table ' || rec_invalid_fk_names.table_name || '.');
     
      -- Add the table name to the list of error tables if it hasn't already been added to the list.
      IF rec_invalid_fk_names.table_name <> v_previous_table
      THEN
     
         -- Add a comma to the table error list if this is not the first table 
         IF :v_error_tables IS NOT NULL THEN
            :v_error_tables := SUBSTR(:v_error_tables || ', ', 1, 2000);
         END IF;
        
         -- Add the current table to the table error list
         :v_error_tables := SUBSTR(:v_error_tables || LOWER(rec_invalid_fk_names.table_name), 1, 2000);
     
      END IF;
     
     	-- Set the v_previous_table variable to the value of the current table
      v_previous_table := rec_invalid_fk_names.table_name;  
     
   END LOOP;
   
   -- Output the total count of invalid foreign key names
   DBMS_OUTPUT.PUT_LINE(CHR(9));
   DBMS_OUTPUT.PUT_LINE('INVALID FOREIGN KEY NAMES FOUND: ' || v_error_count);

   IF v_error_count > 0
   THEN
   
   	-- If any errors occurred then notify the user that they must fix the invalid foreign key names
      DBMS_OUTPUT.PUT_LINE(CHR(9));
      DBMS_OUTPUT.PUT_LINE('SOLUTION: FIX THE FOREIGN KEY NAMES IDENTIFIED ABOVE.');
      
   END IF;

END;
/

-- If any errors were detected then raise an Oracle error to fail the build.
-- Include the list of tables in the error message rather than the invalid 
-- names (which may be cryptic) to help developers identify if the error was
-- caused by their commit.  The detailed list of invalid names will appear 
-- in the log above this error.
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
      raise_application_error (-20001,'INVALID FOREIGN KEY NAMES FOUND IN TABLES: ' || :v_error_tables);
   
   END IF;
   
END;
/
