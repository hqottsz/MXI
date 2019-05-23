/********************************************************************************
*
* Script:        chk_index_names.sql
*
* Description:   This script validates that all index names use one of the 
*                approved standard prefixes excluding the invalid names that
*                existed when this validation was created.  If any invalid 
*                index names are found then the script will raise an
*                error to make sure that index is renamed.
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

   CURSOR cur_invalid_index_names IS
   -- Use the with clause to build a list of known exceptions that will
   -- be excluded from the validation.
   WITH
   index_exceptions
   AS
   (
   SELECT 'EQP_ASSMBL' table_name, 'ASSMBL_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_ASSMBL_BOM' table_name, 'BOM_ASSMBL_BOM_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_ASSMBL_BOM' table_name, 'BOM_ASSMBL_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_ASSMBL_POS' table_name, 'EQP_POS_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_BOM_PART' table_name, 'ASSMBL_BOM_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_BOM_PART' table_name, 'ASSMBL_CD_BOM_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_BOM_PART' table_name, 'ASSMBL_DB_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_BOM_PART' table_name, 'BOM_PART_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_MANUFACT' table_name, 'MANUFACT_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_PART_BASELINE' table_name, 'BOM_PART_DB_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_PART_BASELINE' table_name, 'BOM_PART_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_PART_NO' table_name, 'PART_MANUFACT_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_PART_NO' table_name, 'PART_NO_MANUFACT_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_PART_NO' table_name, 'PART_NO_OEM_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_PART_NO' table_name, 'PART_NO_SDESC_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_PART_NO' table_name, 'STOCK_NO_DB_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_PART_NO' table_name, 'STOCK_NO_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_PART_VENDOR' table_name, 'PART_NO_VENDOR_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_STOCK_NO' table_name, 'ABC_CLASS_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_STOCK_NO' table_name, 'ABC_CLASS_DB_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_STOCK_NO' table_name, 'STOCK_NO_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EQP_STOCK_NO' table_name, 'STOCK_NO_NAME_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EVT_EVENT' table_name, 'DOC_REF_SDESC_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EVT_EVENT' table_name, 'EVENT_SDESC_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'EVT_EVENT' table_name, 'EVENT_SDESC_UPPER' index_name FROM DUAL UNION ALL
   SELECT 'INV_AC_REG' table_name, 'AC_REG_CD_LOWER' index_name FROM DUAL UNION ALL   
   SELECT 'INV_INV' table_name, 'INV_BARCODE_SDESC_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'INV_INV' table_name, 'INV_BARCODE_SDESC_UPPER' index_name FROM DUAL UNION ALL
   SELECT 'INV_INV' table_name, 'INV_NO_SDESC_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'INV_INV' table_name, 'SERIAL_NO_OEM_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'INV_LOC' table_name, 'LOC_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'INV_LOC_STOCK' table_name, 'INV_STOCK_NO_DB_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'INV_LOC_STOCK' table_name, 'INV_STOCK_NO_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'MV_OPEN_PART_REQUESTS' table_name, 'MV_OPEN_REQ_PK' index_name FROM DUAL UNION ALL
   SELECT 'ORG_HR' table_name, 'HR_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'ORG_VENDOR' table_name, 'VENDOR_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'ORG_VENDOR_ACCOUNT' table_name, 'ACCOUNT_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'ORG_WORK_DEPT' table_name, 'DEPT_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'ORG_WORK_DEPT' table_name, 'DESC_SDESC_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'REF_QTY_UNIT' table_name, 'QTY_UNIT_CD_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'REF_QTY_UNIT' table_name, 'QTY_UNIT_DB_ID_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'SCHED_STASK' table_name, 'BARCODE_SDESC_UPPER' index_name FROM DUAL UNION ALL
   SELECT 'SCHED_STASK' table_name, 'RO_REF_SDESC_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'SCHED_STASK' table_name, 'WO_REF_SDESC_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'SHIP_SHIPMENT' table_name, 'SHPMNT_WAYBILL_SDESC_UPPER' index_name FROM DUAL UNION ALL
   SELECT 'SHIP_SHIPMENT_LINE' table_name, 'SHPMNT_SERIAL_NO_OEM_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'TEST_TRIGGER' table_name, 'TEST_TRIGGER_PK' index_name FROM DUAL UNION ALL
   SELECT 'UTL_USER' table_name, 'FIRST_NAME_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'UTL_USER' table_name, 'LAST_NAME_LOWER' index_name FROM DUAL UNION ALL
   SELECT 'UTL_USER' table_name, 'USERNAME_LOWER' index_name FROM DUAL
   )
   -- FIND INDEXES THAT DO NOT START WITH ONE OF THE FOLLOWING APPROVED PREFIXES:
   -- "IX_" for standard indexes including function based indexes
   -- "UK_" for unique indexes   
   -- "PK_" for new primary key indexes
   -- "pk_" for old primary key indexes 
   -- "SYS_" for system generated indexes
   --
   -- NOTE: The escape clause is used here to make the like clause match the
   --       literal underscore since the underscore will be compared as a 
   --       wildcard if not escaped.
   --
   SELECT table_name,
          index_name
     FROM user_indexes
    WHERE index_name NOT LIKE 'IX\_%' ESCAPE '\'
      AND index_name NOT LIKE 'UK\_%' ESCAPE '\'
      AND index_name NOT LIKE 'PK\_%' ESCAPE '\'
      AND index_name NOT LIKE 'pk\_%' ESCAPE '\'
      AND index_name NOT LIKE 'SYS\_%' ESCAPE '\'
   MINUS
   -- Exclude known index name exceptions (identified above in 
   -- the index_exceptions with clause) to allow the above rules
   -- to be applied to new index names.   
   SELECT table_name,
          index_name
     FROM index_exceptions
    ORDER BY table_name,
             index_name;
       
    v_error_count       NUMBER(10);
    v_previous_table    VARCHAR2(30);

BEGIN

   -- Initialize variables
   :v_error_tables := NULL;
   v_previous_table := 'EMPTY';
   v_error_count := 0;
   
   -- Output a header for the invalid index names
   DBMS_OUTPUT.PUT_LINE(CHR(9));
   DBMS_OUTPUT.PUT_LINE('CHECKING FOR INVALID INDEX NAMES');
   DBMS_OUTPUT.PUT_LINE('-------------------------------------------------------------------');
   
   -- Loop through the invalid index names
   FOR rec_invalid_index_names IN cur_invalid_index_names
   LOOP   

      -- Increment the error count
      v_error_count := v_error_count + 1;

      -- Output the invalid index name with the corresponding table name     
      DBMS_OUTPUT.PUT_LINE('- Invalid index name ' || rec_invalid_index_names.index_name || ' on table ' || rec_invalid_index_names.table_name || '.');
     
      -- Add the table name to the list of error tables if it hasn't already been added to the list.
      IF rec_invalid_index_names.table_name <> v_previous_table
      THEN
     
         -- Add a comma to the table error list if this is not the first table 
         IF :v_error_tables IS NOT NULL THEN
            :v_error_tables := SUBSTR(:v_error_tables || ', ', 1, 2000);
         END IF;
        
         -- Add the current table to the table error list
         :v_error_tables := SUBSTR(:v_error_tables || LOWER(rec_invalid_index_names.table_name), 1, 2000);
     
      END IF;
     
     	-- Set the v_previous_table variable to the value of the current table
      v_previous_table := rec_invalid_index_names.table_name;  
     
   END LOOP;
   
   -- Output the total count of invalid indexes
   DBMS_OUTPUT.PUT_LINE(CHR(9));
   DBMS_OUTPUT.PUT_LINE('INVALID INDEX NAMES FOUND: ' || v_error_count);

   IF v_error_count > 0
   THEN
   
   	-- If any errors occurred then notify the user that they much fix the invalid index names
      DBMS_OUTPUT.PUT_LINE(CHR(9));
      DBMS_OUTPUT.PUT_LINE('SOLUTION: FIX THE INDEX NAMES IDENTIFIED ABOVE.');
      
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
      raise_application_error (-20001,'INVALID INDEX NAMES FOUND IN TABLES: ' || :v_error_tables);
   
   END IF;
   
END;
/
