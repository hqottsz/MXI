--liquibase formatted sql


--changeSet MX-28865:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- A task applicability rule get translated to raw sql expression and stored in the task_task.task_appl_sql_ldesc.
-- Specifically for applicability rules regarding operators (Aircraft Operator and Assembly Operator) they 
-- were previously based on the columns org_carrier.iata_cd and org_carrier.icao_cd.  
-- This has been changed and the rules are now based on the column org_carrier.carrier_cd.  
-- This migration script will parse the raw sql expression in task_task.task_appl_sql_ldesc and replace the old columns with 
-- the new carrier_cd and corresponding carrier_cd values.
-- Steps:
-- 1) Query task_task for any records with applicability rules pertaining to the AIRCRAFT_OPERATOR or ASSEMBLY_OPERATOR
--        e.g. task_task.task_appl_sql_ldesc LIKE '%org_carrier.iata_cd%' or '%ass_carrier.iata_cd%'
-- 2) Parse the sql string for any substrings containing the original column names (may be more than one)
--        e.g. "(org_carrier.iata_cd = 'OT' OR org_carrier.icao_cd = 'OT')"
-- 3) Query the org_carrier table using the substring in order to retrieve the carrier_cd value
--        e.g. carrier_cd = 'OT-OTA'
-- 4) Replace the substring with one that contains the carrier_cd value (parenthesis no longer required, thus removed)
--        e.g. "org_carrier.carrier_cd = 'OT-OTA'"
--
-- Example:
--    original = (org_carrier.iata_cd = 'OT' OR org_carrier.icao_cd = 'OT')
--    new = org_carrier.carrier_cd = 'OT-OTA'
--
-- There are three categories of operations bracketed, non-bracketed, and LIKE.  Each of these are handled slightly differently.
--
-- The Assembly Operator applicability rule uses a table alias of ass_carrier for the org_carrier, 
-- thus the raw sql has to work for both org_carrier and ass_carrier.
--
-- Note: if the carrier_cd is not able to be retrieved then the original value shall be used (and an error message generated)
-- Example:
--    original = (org_carrier.iata_cd = 'bad data' OR org_carrier.icao_cd = 'bad data')
--    new = org_carrier.carrier_cd = 'bad data'
CREATE or REPLACE FUNCTION replace_operator_appl_rule(
   a_raw_sql      task_task.task_appl_sql_ldesc%TYPE,
   a_table_alias  VARCHAR2
)
RETURN task_task.task_appl_sql_ldesc%TYPE
AS
   v_new_raw_sql        task_task.task_appl_sql_ldesc%TYPE;
   v_bracketed_regexp   VARCHAR2(4000);
   v_nonbracketed_regexp VARCHAR2(4000);
   v_like_regexp        VARCHAR2(4000);
   v_index              NUMBER;
   v_rule               task_task.task_appl_sql_ldesc%TYPE;
   v_new_rule           task_task.task_appl_sql_ldesc%TYPE;
   v_sql_stmt           VARCHAR2(4000);
   v_carrier_cd         VARCHAR2(4000);
   v_list               VARCHAR2(4000);
   
   TYPE ref_cursor_t    IS REF CURSOR;
   v_ref_cur            ref_cursor_t;
   
BEGIN
   
   -- Copy the raw sql to return after replacing the rule substrings
   v_new_raw_sql := a_raw_sql;
   
   -- Set the regular expressions for reading the sql representation of the Aircraft/Assembly Operator applicability rules
   v_nonbracketed_regexp := '(\()' || a_table_alias || '\.iata_cd *?(=|>=|>|<=|<) *''([^'']*?)'' *OR +' || a_table_alias || '.icao_cd *\2 *''\3'' *\)';
   v_bracketed_regexp := '(\()' || a_table_alias || '\.iata_cd *(NOT IN|IN) *\(([^\)]*?)\) *OR +' || a_table_alias || '\.icao_cd *\2 *\(\3\) *\)';
   v_like_regexp := '(\()' || a_table_alias || '\.iata_cd *?(LIKE) *(\)?) *''([^'']*?)'' *(\)?) *OR +' || a_table_alias || '.icao_cd *\2 *\3 *''\4'' *\5 *\)';

   -- 1. Replace non-bracketed rules
   v_index := 1;
   WHILE v_index < LENGTH(a_raw_sql) LOOP

      -- Find the next occurrence of an non-bracketed rule in the original sql expression
      v_rule := REGEXP_SUBSTR(a_raw_sql, v_nonbracketed_regexp, v_index, 1, 'i');
   
      IF v_rule IS NOT NULL AND LENGTH(v_rule) > 0 THEN 
         
         -- Use the rule to query the org_carrier table to retrieve the carrier_cd
         v_carrier_cd := '';
         BEGIN
            v_sql_stmt := 'SELECT carrier_cd FROM org_carrier ' || a_table_alias || ' WHERE ' || v_rule;
            EXECUTE IMMEDIATE v_sql_stmt INTO v_carrier_cd;

         EXCEPTION
            WHEN NO_DATA_FOUND THEN
               raise_application_error(
                  -20001, 
                  'ERROR: unable to determine carrier_cd using Operator applicability rule''s value.' || chr(13) ||
                  'Failed sql stmt: ' || v_sql_stmt || chr(13) ||
                  'Please run pre-check Pre-Check_Validate-Operator-Appl-Rules.sql and modify failing data.',
                  TRUE);

         END;
         
         v_new_rule := REGEXP_REPLACE( v_rule, v_nonbracketed_regexp, a_table_alias || '.carrier_cd \2 ''' || v_carrier_cd || '''', 1, 1, 'i' );
         
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
         
         -- Use the rule to query the org_carrier table to retrieve the carrier_cd
         -- (may be more than one matching entry so use a csv list)
         v_list := '';
         
         v_sql_stmt := 'SELECT carrier_cd FROM org_carrier ' || a_table_alias || ' WHERE ' || v_rule;
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
              'ERROR: unable to determine carrier_cd using Operator applicability rule''s value.' || chr(13) ||
              'Failed sql stmt: ' || v_sql_stmt || chr(13) ||
              'Please run pre-check Pre-Check_Validate-Operator-Appl-Rules.sql and modify failing data.',
              TRUE);
         END IF;
         
         v_new_rule := REGEXP_REPLACE( v_rule, v_bracketed_regexp, a_table_alias || '.carrier_cd \2 (' || v_list || ')', 1, 1, 'i' );         

         -- Replace with new rule
         v_new_raw_sql := REPLACE(v_new_raw_sql, v_rule, v_new_rule);
         
         -- Move the offset to the end of the rule we just processed
         v_index := INSTR(a_raw_sql, v_rule, v_index) + LENGTH(v_rule);

      ELSE
         -- If no more occurrences of bracketed rules then end loop
         EXIT;
         
      END IF;

   END LOOP;

   -- 3. Replace LIKE rules
   v_index := 1;
   WHILE v_index < LENGTH(a_raw_sql) LOOP

      -- Find the next occurrence of a LIKE rule in the original sql
      v_rule := REGEXP_SUBSTR(a_raw_sql, v_like_regexp, v_index, 1, 'i');
      
      IF v_rule IS NOT NULL AND LENGTH(v_rule) > 0 THEN 

         -- Use the reg exp to replace the appropriate parts of the rule
         v_new_rule := REGEXP_REPLACE(v_rule,  v_like_regexp, a_table_alias || '.carrier_cd \2 \3''\4''\5', 1, 1, 'i' );
         
         -- Replace the rule in the returning sql stmt with the new rule
         v_new_raw_sql := REPLACE(v_new_raw_sql, v_rule, v_new_rule);
                  
         -- Move the offset to the end of the rule we just processed
         v_index := INSTR(a_raw_sql, v_rule, v_index) + LENGTH(v_rule);

      ELSE
         -- If no more occurrences of LIKE rule then end loop
         EXIT;
         
      END IF;
      
   END LOOP;


   RETURN v_new_raw_sql;
   
END;
/

--changeSet MX-28865:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   v_raw_sql      task_task.task_appl_sql_ldesc%TYPE;
   v_new_raw_sql  task_task.task_appl_sql_ldesc%TYPE;
   v_sql_stmt     VARCHAR2(1000);
   v_total_count  NUMBER;
   v_mod_count    NUMBER;

BEGIN

   v_total_count := 0;
   v_mod_count   := 0;
   
   -- Select operator based applicability rules in task_task table
   FOR l_task_info IN (
     SELECT
        *
     FROM
        task_task
     WHERE
        task_task.task_appl_sql_ldesc LIKE ('%carrier%')
        
   ) LOOP
      
      v_raw_sql := l_task_info.task_appl_sql_ldesc;
	  
      -- For org_carrier
      v_new_raw_sql := replace_operator_appl_rule( v_raw_sql, 'org_carrier' );
	   
      -- For ass_carrier
      v_new_raw_sql := replace_operator_appl_rule( v_new_raw_sql, 'ass_carrier' );
	  
      -- When original rule is different than the replaced one
      IF v_new_raw_sql <> v_raw_sql THEN

         -- Update task_appl_sql_ldesc value in the task_task table
         UPDATE
            task_task
         SET
            task_appl_sql_ldesc = v_new_raw_sql
         WHERE
            task_appl_sql_ldesc = v_raw_sql;          

         v_mod_count := v_mod_count + 1;
		   
      END IF;
      
      v_total_count := v_total_count + 1;
	  
   END LOOP;

   COMMIT;

END;
/

--changeSet MX-28865:3 stripComments:false
DROP FUNCTION replace_operator_appl_rule;