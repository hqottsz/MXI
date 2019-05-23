/********************************************************************************
*
* Script:        chk_audit_columns.sql
*
* Description:   This script validates the audit column definitions.
*                If the rstat_cd, revision_no, ctrl_db_id , creation_dt, 
*                revision_dt, revision_db_id, 
*                or revision_user columns exist for a table then the script
*                will verify that they have the correct data type, 
*                length/precision, not null constraint, check constraint,
*                and at least one insert and one update trigger.If any 
*                inconsistencies are found then the script will raise an error
*                to make sure that new audit columns are set up correctly.
*
*********************************************************************************/

SET SERVEROUTPUT ON SIZE 1000000
SET DEFINE ON
SET LINESIZE 1000
SET WRAP ON

-- Declare the SQL Plus variable for the table error count
VAR v_error_tables VARCHAR2(2000)

DECLARE

   v_standard_data_type  VARCHAR2(100);
   v_record_errs         NUMBER;
   v_trigger_errs        NUMBER;
   v_datatype_errs       NUMBER;
   v_nullable_errs       NUMBER;
   v_check_errs          NUMBER;
   v_last_table_name     user_tables.table_name%TYPE;
   v_table_reported      NUMBER(1);
   v_not_null_found      NUMBER(1);

BEGIN
   
   -- Initialize variables
   :v_error_tables := NULL;
   v_trigger_errs := 0;
   v_datatype_errs := 0;
   v_nullable_errs := 0;
   v_check_errs := 0;
   v_last_table_name := 'EMPTY';
   v_table_reported := 0;
   
   FOR x IN (SELECT   ut.table_name, 
                      utc.column_name,
                      CASE utc.data_type 
                         WHEN 'VARCHAR2' THEN 'VARCHAR2' || '(' || utc.data_length || ')'
                         WHEN 'NUMBER' THEN 'NUMBER' || '(' || utc.data_precision || ')'
                         ELSE utc.data_type 
                      END ldata_type,
                      utc.nullable,
                      NVL(chk_qry.chk_count,0) chk_count,
                      NVL(trig_qry.insert_count,0) insert_trig_count,
                      NVL(trig_qry.update_count,0) update_trig_count,
                      -- Flag tables that should not have audit triggers with a
                      -- skip_trigger_bool flag of 1.  All other tables will return 0.
                      CASE
                         WHEN ut.table_name LIKE 'ASB%LOG' THEN 1
                         WHEN ut.table_name IN ('INT_MSG_PUBLISH') THEN 1
                         ELSE 0
                      END skip_trigger_bool
             FROM     user_tables ut, 
                      user_tab_columns utc,
                      (-- For each column count the number of constraints.
                       SELECT   ucc.table_name, ucc.column_name, COUNT(*) chk_count
                       FROM     user_cons_columns ucc,
                                user_constraints uc  
                       WHERE    ucc.constraint_name = uc.constraint_name
                       AND      uc.constraint_type = 'C'
                       GROUP BY ucc.table_name, ucc.column_name
                      ) chk_qry,                      
                      (-- For each table count the number of insert and update triggers.                      
                       SELECT   table_name, 
                                SUM(DECODE(triggering_event,'INSERT',1,0)) insert_count,
                                SUM(DECODE(triggering_event,'UPDATE',1,0)) update_count
                       FROM     user_triggers
                       GROUP BY table_name
                       ) trig_qry                      
             WHERE    ut.table_name = utc.table_name
             AND      utc.table_name = chk_qry.table_name (+)
             AND      utc.column_name = chk_qry.column_name (+)
             AND      utc.table_name = trig_qry.table_name (+)
             AND      utc.column_name IN ('RSTAT_CD', 'REVISION_NO', 'CTRL_DB_ID',
                                          'CREATION_DT', 'REVISION_DT', 
                                          'REVISION_DB_ID', 'REVISION_USER')
             ORDER BY ut.table_name, utc.column_id   
            )             
   LOOP   

      -- Reset the error count for each record
      v_record_errs := 0;
      
      -- If this is a new table then check for audit triggers
      -- and set the table reported flag to zero to permit
      -- this table to be added to the list of error tables
      -- if any error are found.
      
      -- The cursor groups results by table_name and column_id, therefore, the 
      -- same table may appear multiple times in the cursor.  Therefore, check  
      -- if the table_name has changed before checking for missing triggers.
      IF x.table_name != v_last_table_name THEN
      
         -- Set the table reported flag equal to zero to indicate that this
         -- is a new table has not been added to the list of error tables yet.       
         v_table_reported := 0;
         
         -- Set the last table name equal to the current table name to indicate
         -- that processing has already started for this table.
         v_last_table_name := x.table_name;         
         
         -- Verify that the table has at least one insert trigger, excluding tables
         -- with a skip_trigger_bool of 1 since they are excluded from audit triggers.
         IF x.insert_trig_count < 1 AND x.skip_trigger_bool != 1 THEN
         
            v_record_errs := v_record_errs + 1;
            v_trigger_errs := v_trigger_errs + 1;
         
            DBMS_OUTPUT.PUT_LINE(CHR(9) || CHR(10) || 'AUDIT COLUMN TRIGGER ERROR: In table ' || 
               x.table_name || ' the insert trigger is missing.');            
            
         END IF;
         
         -- Verify that the table has at least one update trigger, excluding tables
         -- with a skip_trigger_bool of 1 since they are excluded from audit triggers.   
         IF x.update_trig_count < 1 AND x.skip_trigger_bool != 1 THEN
         
            v_record_errs := v_record_errs + 1;
            v_trigger_errs := v_trigger_errs + 1;
         
            DBMS_OUTPUT.PUT_LINE(CHR(9) || CHR(10) || 'AUDIT COLUMN TRIGGER ERROR: In table ' || 
               x.table_name || ' the update trigger is missing.');            
            
         END IF;              
         
      END IF;


      -- Identify the expected datatype for the current audit column.
      IF x.column_name = 'RSTAT_CD' THEN       
         v_standard_data_type := 'NUMBER(3)';         
      ELSIF x.column_name = 'REVISION_NO' THEN       
         v_standard_data_type := 'NUMBER(10)';         
      ELSIF x.column_name = 'CTRL_DB_ID' THEN       
         v_standard_data_type := 'NUMBER(10)';         
      ELSIF x.column_name = 'CREATION_DT' THEN       
         v_standard_data_type := 'DATE';        
      ELSIF x.column_name = 'REVISION_DT' THEN       
         v_standard_data_type := 'DATE';         
      ELSIF x.column_name = 'REVISION_DB_ID' THEN       
         v_standard_data_type := 'NUMBER(10)';         
      ELSIF x.column_name = 'REVISION_USER' THEN      
         v_standard_data_type := 'VARCHAR2(30)';        
      ELSE       
         v_standard_data_type := 'UNKNOWN';         
      END IF;
      
      
      -- Validate the datatype of the audit column
      IF x.ldata_type != v_standard_data_type THEN
      
         v_record_errs := v_record_errs + 1;
         v_datatype_errs := v_datatype_errs + 1;
         
         DBMS_OUTPUT.PUT_LINE(CHR(9) || CHR(10) || 'AUDIT COLUMN DATATYPE ERROR: In table ' || 
            x.table_name || ' in column ' || x.column_name || ' the datatype is ' || x.ldata_type || 
            ' when it should be ' || v_standard_data_type || '.');
               
      END IF;                  


      -- Verify that the audit column has a not null constraint
      IF x.nullable = 'Y' THEN
      
         -- Deferrable not null constraints also appear as 'Y' in the nullable column, therefore,
         -- we need to check the search condition of each check constraint on the column
         -- for a NOT NULL constraint.  
      
         -- Reset the flag to show that a not null constraint has not been found
         v_not_null_found := 0;
         
         -- Loop through each check constraint on the column
         FOR y IN (SELECT   uc.search_condition
                   FROM     user_constraints uc, user_cons_columns ucc
                   WHERE    uc.constraint_name = ucc.constraint_name
                   AND      uc.constraint_type = 'C'
                   AND      uc.table_name = x.table_name
                   AND      ucc.column_name = x.column_name
                   ORDER BY uc.table_name)
         LOOP
         
            IF UPPER(y.search_condition) LIKE '%NOT NULL%' THEN
                -- The search condition includes NOT NULL so set the flag to found
                v_not_null_found := 1;                
            END IF;       
            
         END LOOP;
        
         IF v_not_null_found = 0 THEN
      
            -- No NOT NULL constraints were found for this column, therefore, 
            -- report the missing NOT NULL constraint.            
            v_record_errs := v_record_errs + 1;
            v_nullable_errs := v_nullable_errs + 1;
         
            DBMS_OUTPUT.PUT_LINE(CHR(9) || CHR(10) || 'AUDIT COLUMN NULLABLE ERROR: In table ' || 
               x.table_name || ' in column ' || x.column_name || ' the not null constraint ' ||
               'is missing.');
      
         END IF;
               
      END IF;
            
      -- If the error count for this record is greater than zero and this table hasn't already 
      -- been added to the list of error tables then add it to the list.
      IF v_record_errs > 0 AND v_table_reported = 0 THEN
        
        -- Add a comma to the table error list if this is not the first table 
        IF :v_error_tables IS NOT NULL THEN
           :v_error_tables := SUBSTR(:v_error_tables || ', ', 1, 2000);
        END IF;
        
        -- Add the current table to the table error list
        :v_error_tables := SUBSTR(:v_error_tables || LOWER(x.table_name), 1, 2000);

        -- Set the table reported flag equal to one to prevent the same table from being
        -- added to the list multiple times.        
        v_table_reported := 1;
        
      END IF;
      

   END LOOP;
   
   IF :v_error_tables IS NOT NULL THEN
   
      DBMS_OUTPUT.PUT_LINE(CHR(9));
      DBMS_OUTPUT.PUT_LINE(CHR(9));      
      DBMS_OUTPUT.PUT_LINE('AUDIT COLUMN ERROR COUNTS (DETAILED ABOVE)');
      DBMS_OUTPUT.PUT_LINE('------------------------------------------');
      DBMS_OUTPUT.PUT_LINE('Missing triggers: ' || TO_CHAR(v_trigger_errs));      
      DBMS_OUTPUT.PUT_LINE('Incorrect datatypes: ' || TO_CHAR(v_datatype_errs));
      DBMS_OUTPUT.PUT_LINE('Missing not null constraints: ' || TO_CHAR(v_nullable_errs));
      DBMS_OUTPUT.PUT_LINE('Missing check constraints: ' || TO_CHAR(v_check_errs));
      DBMS_OUTPUT.PUT_LINE('Total: ' || TO_CHAR(v_trigger_errs+v_datatype_errs+v_nullable_errs+v_check_errs));
            
   END IF;
     
END;
/

BEGIN
   IF :v_error_tables IS NOT NULL THEN
      raise_application_error (-20001,'AUDIT COLUMN DEFINITION ERRORS IN TABLES: ' || :v_error_tables);
   END IF;
END;
/