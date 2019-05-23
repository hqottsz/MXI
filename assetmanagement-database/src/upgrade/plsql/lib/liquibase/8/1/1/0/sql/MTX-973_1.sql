--liquibase formatted sql


--changeSet MTX-973_1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- A task applicability rule get translated to raw sql expression and stored in the task_task.task_appl_sql_ldesc.
-- Specifically for applicability rules regarding operators (Aircraft Operator and Assembly Operator) they 
-- are based on the column org_carrier.carrier_cd  
-- Now the org_carrier.carrier_cd  has changed to be in sync with org_org.org_cd.  
-- This migration script will parse the raw sql expression in task_task.task_appl_sql_ldesc and replace the carrier_cd value with 
-- the corresponding org_cd value.
-- Steps:
-- 1) Query task_task for any records with applicability rules pertaining to the AIRCRAFT_OPERATOR or ASSEMBLY_OPERATOR
--        e.g. task_task.task_appl_sql_ldesc LIKE '%carrier_cd%'
-- 2) Parse the sql string for any substrings containing the column name 
-- 3) Query the org_org table using the substring in order to retrieve the org_cd value
--        e.g. org_cd = 'OTA'
-- 4) Replace the substring value with one that contains the org_cd value within the sql string
--        e.g. "org_carrier.carrier_cd = 'YYY'"
--
-- Example:
--    original = org_carrier.carrier_cd = 'AA-BBB'
--    new = org_carrier.carrier_cd = 'CCC', assuming that org_org.org_cd = 'CCC'
--
-- There are two categories of operations bracketed and non-bracketed.  Each of these are handled slightly differently.
-- LIKE operations are not required to modified since they may not have the entire value for the carrier code to look for in the org_carrier table
-- Example:
--  org_carrier.carrier_cd LIKE '%F%''
-- Those ones has to be changed manually if exists.
-- The Assembly Operator applicability rule uses a table alias of ass_carrier for the org_carrier, 
-- thus the raw sql has to work for both org_carrier and ass_carrier.
CREATE or REPLACE FUNCTION replace_carrier_code (
   a_raw_sql      task_task.task_appl_sql_ldesc%TYPE,
   a_table_alias  VARCHAR2
)
RETURN task_task.task_appl_sql_ldesc%TYPE
AS
   v_new_raw_sql        task_task.task_appl_sql_ldesc%TYPE;
   v_bracketed_regexp   VARCHAR2(4000);
   v_nonbracketed_regexp VARCHAR2(4000);
   v_index              NUMBER;
   v_rule               task_task.task_appl_sql_ldesc%TYPE;
   v_new_rule           task_task.task_appl_sql_ldesc%TYPE;
   v_sql_stmt           VARCHAR2(4000);
   v_carrier_cd         VARCHAR2(4000);
   v_list               VARCHAR2(4000);
   
   TYPE ref_cursor_t    IS REF CURSOR;
   v_ref_cur            ref_cursor_t;
   
BEGIN
   
   -- Copy the raw sql to return after replacing the rule sub strings
   v_new_raw_sql := a_raw_sql;
   
   -- Set the regular expressions for reading the sql representation of the Aircraft/Assembly Operator applicability rules
   v_nonbracketed_regexp := '\'|| a_table_alias ||'.carrier_cd *?(=|>=|>|<=|<) *''([^'']*?)''';
   v_bracketed_regexp := '\'|| a_table_alias ||'.carrier_cd *(NOT IN|IN) *\(([^\)]*?)\)';
   
   -- 1. Replace non-bracketed rules
   v_index := 1;
   WHILE v_index < LENGTH(a_raw_sql) LOOP

      -- Find the next occurrence of an non-bracketed rule in the original sql expression
      v_rule := REGEXP_SUBSTR(a_raw_sql, v_nonbracketed_regexp, v_index, 1, 'i');
	  
      IF v_rule IS NOT NULL AND LENGTH(v_rule) > 0 THEN 
         
         -- Use the rule to query the org_org table to retrieve the org_cd
		
         v_carrier_cd := '';
         BEGIN
            v_sql_stmt := 'SELECT org_org.org_cd FROM org_org INNER JOIN org_carrier '|| a_table_alias || ' ON '|| a_table_alias ||'.org_db_id = org_org.org_db_id
            AND '|| a_table_alias ||'.org_id = org_org.org_id WHERE ' || v_rule;
            EXECUTE IMMEDIATE v_sql_stmt INTO v_carrier_cd;
			
			-- Since a org_carrier record is associated to a org_org record by
		    -- org_carrier.org_db_id/org_carrier.org_id
		    -- that guarantees that an org_cd always exists, however below is handled un-expected exception
			EXCEPTION
            WHEN NO_DATA_FOUND THEN
               raise_application_error(
                  -20001, 
                  'ERROR: unable to determine carrier_cd using Operator applicability rule''s value.' || chr(13) ||
                  'Failed sql stmt: ' || v_sql_stmt || chr(13) ||
                  'Please run pre-check Pre-Check_Validate-Operator-Cd-Value-Update-In-Applicability-Rule.sql and modify failing data.',
                  TRUE);
         END;
         
         v_new_rule := REGEXP_REPLACE( v_rule, v_nonbracketed_regexp, a_table_alias || '.carrier_cd \1 ''' || v_carrier_cd || '''', 1, 1, 'i' );
         
         -- Replace the rule with the new rule
         v_new_raw_sql := REPLACE(v_new_raw_sql, v_rule, v_new_rule);
         
         -- Move the offset to the end of the rule we just processed
         v_index := INSTR(a_raw_sql, v_rule, v_index) + LENGTH(v_rule);

      ELSE
         -- If no more occurrences of non-bracketed rule then end loop
         EXIT;
         
      END IF;

   END LOOP;

   -- 2. Replace bracketed rules
   v_index := 1;
   WHILE v_index < LENGTH(a_raw_sql) LOOP

      -- Find the next occurrence of a bracketed rule in the original sql
      v_rule := REGEXP_SUBSTR(a_raw_sql, v_bracketed_regexp, v_index, 1, 'i');
      
      IF v_rule IS NOT NULL AND LENGTH(v_rule) > 0 THEN 
         
         -- Use the rule to query the org_org table to retrieve the org_cd
         -- (may be more than one matching entry so use a csv list)
         v_list := '';
         
         v_sql_stmt := 'SELECT org_org.org_cd FROM org_org INNER JOIN org_carrier '|| a_table_alias || ' ON '|| a_table_alias ||'.org_db_id = org_org.org_db_id
         AND '|| a_table_alias ||'.org_id = org_org.org_id WHERE ' || v_rule;
	
	     --Use a REF CURSOR here which is a PL/SQL data type whose value is the memory address of a query work area on the database. 
		 --In essence, a REF CURSOR is a pointer or a handle to a result set on the database. 
         OPEN v_ref_cur FOR v_sql_stmt;
         FETCH v_ref_cur INTO v_carrier_cd;
         
         WHILE v_ref_cur%FOUND LOOP
            v_list := v_list || '''' || v_carrier_cd || '''' || ', ';
            FETCH v_ref_cur INTO v_carrier_cd;
         END LOOP;
         CLOSE v_ref_cur;
         
         IF LENGTH(v_list) > 2 THEN 
            v_list := SUBSTR(v_list, 1, LENGTH(v_list) - 2);
         ELSE
            raise_application_error(
              -20001, 
              'ERROR: unable to determine carrier code base on organization code using applicability rule''s value.' || chr(13) ||
              'Failed sql stmt: ' || v_sql_stmt || chr(13) ||
			  'Please run pre-check Pre-Check_Validate-Operator-Cd-Value-Update-In-Applicability-Rule.sql and modify failing data.',
              TRUE);
         END IF;
         
         v_new_rule := REGEXP_REPLACE( v_rule, v_bracketed_regexp, a_table_alias || '.carrier_cd \1 (' || v_list || ')', 1, 1, 'i' );         

         -- Replace with new rule
         v_new_raw_sql := REPLACE(v_new_raw_sql, v_rule, v_new_rule);
         
         -- Move the offset to the end of the rule we just processed
         v_index := INSTR(a_raw_sql, v_rule, v_index) + LENGTH(v_rule);

      ELSE
         -- If no more occurrences of bracketed rules then end loop
         EXIT;
         
      END IF;

   END LOOP;

   RETURN v_new_raw_sql;
   
END;
/

--changeSet MTX-973_1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   v_raw_sql      task_task.task_appl_sql_ldesc%TYPE;
   v_new_raw_sql  task_task.task_appl_sql_ldesc%TYPE;
   v_task_db_id  task_task.task_db_id%TYPE;
   v_task_id     task_task.task_id%TYPE;

BEGIN
  
   -- Select operator based applicability rules in task_task table
   FOR l_task_info IN (
     SELECT
        task_task.task_appl_sql_ldesc, task_task.task_db_id, task_task.task_id
     FROM
        task_task
     WHERE
        task_task.task_appl_sql_ldesc LIKE ('%carrier%')
        
   ) LOOP
      
      v_raw_sql := l_task_info.task_appl_sql_ldesc;
      v_task_db_id := l_task_info.task_db_id;
      v_task_id :=  l_task_info.task_id;
	  
	  -- Needs to pass the v_raw_sql for both alias (org_carrier and ass_carrier) since
	  -- in a given applicability rule could be assembly and aircraft operators together
	  -- e.g.: org_carrier.carrier_cd = 'OTA' AND ass_carrier.carrier_cd = 'OTA
	  
	  -- For org_carrier
      v_new_raw_sql := replace_carrier_code( v_raw_sql, 'org_carrier' );
	   
      -- For ass_carrier
      v_new_raw_sql := replace_carrier_code( v_new_raw_sql, 'ass_carrier' );
	  
      -- When original rule is different than the replaced one
      IF v_new_raw_sql <> v_raw_sql THEN

         -- Update task_appl_sql_ldesc value in the task_task table
         UPDATE
           task_task
         SET
           task_appl_sql_ldesc = v_new_raw_sql
         WHERE
          task_task.task_db_id = v_task_db_id          
         AND
          task_task.task_id = v_task_id; 
	   
      END IF;
	  
   END LOOP;

   COMMIT;

END;
/

--changeSet MTX-973_1:3 stripComments:false
DROP FUNCTION replace_carrier_code;