WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Validate_Operator_Cd_Value_Update_In_Applicability_Rule
DEFINE pchk_table_name=pchk_opr_cd_appl_rule
DEFINE pchk_severity=ERROR
 
SET VERIFY OFF 
SET SQLPROMPT "_date _user> "
SET TIME ON

SPOOL log.txt APPEND
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to log.txt
PROMPT ***
SET ECHO ON
SET FEEDBACK ON
SET HEADING ON
SET PAGESIZE 50000
SET LINESIZE 32767
SET TRIMSPOOL ON
SET SQLBLANKLINES ON
SET DEFINE ON
SET CONCAT OFF
SET MARKUP HTML OFF
 
SET ECHO OFF
PROMPT ***
PROMPT *** Opening file &pchk_file_name.sql
PROMPT ***
SET ECHO ON
 
BEGIN
   FOR x IN (SELECT table_name FROM user_tables WHERE table_name = UPPER('&pchk_table_name'))
   LOOP
      EXECUTE IMMEDIATE ('DROP TABLE ' || x.table_name || ' PURGE');
   END LOOP;
END;
/
 
-- Pre-check to ensure operator based task applicability rules are valid
-- prior to modify the org_carrier.carrier_do with org_org.org_cd

CREATE TABLE pchk_opr_cd_appl_rule
AS
SELECT 
   task_db_id,
   task_id,
   task_appl_sql_ldesc,
   CAST(NULL AS VARCHAR2(4000)) AS test_sql_stmt
FROM
   task_task
WHERE 
   rownum = 0
;

DECLARE
   v_raw_sql            task_task.task_appl_sql_ldesc%TYPE;
   v_bracketed_regexp   VARCHAR2(4000);
   v_unbracketed_regexp VARCHAR2(4000);
   v_like_regexp        VARCHAR2(4000);
   v_index              NUMBER;
   v_rule               task_task.task_appl_sql_ldesc%TYPE;
   v_sql_stmt           VARCHAR2(4000);
   v_table_alias        VARCHAR2(4000);
   v_carrier_cd         VARCHAR2(4000);
   v_rule_value         VARCHAR2(10000);
   
BEGIN

   FOR l_task_info IN (
      SELECT
         task_task.task_db_id, task_task.task_id, task_task.task_appl_sql_ldesc
      FROM
         task_task
      WHERE
         lower(task_appl_sql_ldesc) like '%carrier%'
         
   ) LOOP

      v_raw_sql := l_task_info.task_appl_sql_ldesc;
     
      IF INSTR(v_raw_sql,'ass_carrier') >= 1 THEN
         v_table_alias := 'ass_carrier';
      ELSE
         v_table_alias := 'org_carrier';
      END IF;
      
      v_unbracketed_regexp := '\'|| v_table_alias ||'.carrier_cd *?(=|>=|>|<=|<) *''([^'']*?)''';
      v_bracketed_regexp := '\'|| v_table_alias ||'.carrier_cd *(NOT IN|IN) *\(([^\)]*?)\)';
      
      -- Test non-bracketed rules
      v_index := 1;
      WHILE v_index < LENGTH(v_raw_sql) LOOP
         -- Find the next occurrence of a non-bracketed rule in the original sql
         v_rule := REGEXP_SUBSTR(v_raw_sql, v_unbracketed_regexp, v_index, 1, 'i');
         
         IF v_rule IS NOT NULL AND LENGTH(v_rule) > 0 THEN 

            -- Use the rule to query the org_org table to retrieve the org_cd         
            BEGIN
               v_sql_stmt := 'SELECT org_org.org_cd FROM org_org INNER JOIN org_carrier '|| v_table_alias || ' ON '|| v_table_alias ||'.org_db_id = org_org.org_db_id
               AND '|| v_table_alias ||'.org_id = org_org.org_id WHERE ' || v_rule;
               EXECUTE IMMEDIATE v_sql_stmt INTO v_carrier_cd;
            
            EXCEPTION
               WHEN NO_DATA_FOUND THEN
                  
                  INSERT INTO 
                     pchk_opr_cd_appl_rule
                  VALUES
                     (
                        l_task_info.task_db_id,
                        l_task_info.task_id,
                        l_task_info.task_appl_sql_ldesc,
                        v_sql_stmt
                     )
                  ;
                  
            END;
            -- Move the offset to the end of the rule we just processed
            v_index := INSTR(v_raw_sql, v_rule, v_index) + LENGTH(v_rule);
         ELSE
            -- If no more occurrences of non-bracketed rule then, end loop
            EXIT;
         END IF;
      END LOOP;

      -- Test bracketed rules
      v_index := 1;
      WHILE v_index < LENGTH(v_raw_sql) LOOP
         -- Find the next occurrence of an bracketed rule in the original sql
         v_rule := REGEXP_SUBSTR(v_raw_sql, v_bracketed_regexp, v_index, 1, 'i');
         
         IF v_rule IS NOT NULL AND LENGTH(v_rule) > 0 THEN 
       
            IF INSTR(v_rule,'ass_carrier') >= 1 THEN
               v_table_alias := 'ass_carrier';
            ELSE
               v_table_alias := 'org_carrier';
            END IF;
            
            -- Use the rule to query the org_org table to retrieve the org_cd          
            BEGIN
               v_sql_stmt := 'SELECT org_org.org_cd FROM org_org INNER JOIN org_carrier '|| v_table_alias || ' ON '|| v_table_alias ||'.org_db_id = org_org.org_db_id
               AND '|| v_table_alias ||'.org_id = org_org.org_id WHERE ' || v_rule;
               EXECUTE IMMEDIATE v_sql_stmt INTO v_carrier_cd;
           
            EXCEPTION
               WHEN NO_DATA_FOUND THEN
                  
                  INSERT INTO 
                     pchk_opr_cd_appl_rule
                  VALUES
                     (
                        l_task_info.task_db_id,
                        l_task_info.task_id,
                        l_task_info.task_appl_sql_ldesc,
                        v_sql_stmt
                     )
                  ;
                  
               WHEN TOO_MANY_ROWS THEN
                  NULL;
            END;
            -- Move the offset to the end of the rule we just processed
            v_index := INSTR(v_raw_sql, v_rule, v_index) + LENGTH(v_rule);
         ELSE
            -- If no more occurrences of bracketed rule then end loop
            EXIT;
         END IF;
      END LOOP;
     
   END LOOP;
 
END;
/
  
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to &pchk_file_name.html
PROMPT ***
SET ECHO ON
SPOOL OFF
 
SET ECHO OFF
SET TERMOUT OFF
SET MARKUP -
   HTML ON -
   HEAD " -
   <STYLE type='text/css'> -
      BODY {font-family:verdana;font-size:11px; font-weight:bold;} -
      TABLE {border-collapse:collapse; font-size:11px; width:100normal;} -
      TH {background-color:#4682B4; color:#fff; height:30px; font-weight:bold;} -
   </STYLE> " -
   BODY "" -
   TABLE "border=1 bordercolor=black" -
   SPOOL ON -
   ENTMAP ON -
   PREFORMAT OFF 

SPOOL &pchk_file_name.html
 
-- For each error, manually update the value of TASK_APPL_SQL_LDESC in TASK_TASK using the provided task key.
-- Ensure that the carrier code in the applicability rule is in sync with the org_org.org_cd value which will be
-- used to update the org_carrier.carrier_cd column by migration scripts MTX-973-2.sql

SELECT 
   '&pchk_severity' AS "SEV",
   task_db_id,
   task_id,
   task_appl_sql_ldesc,
   test_sql_stmt
FROM
   pchk_opr_cd_appl_rule
;

SPOOL OFF
SET MARKUP HTML OFF
SET TERMOUT ON
 
SPOOL log.txt APPEND
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to log.txt
PROMPT ***
SET ECHO ON
 
BEGIN
   FOR x IN (SELECT table_name FROM user_tables WHERE table_name = UPPER('&pchk_table_name'))
   LOOP
      EXECUTE IMMEDIATE ('DROP TABLE ' || x.table_name || ' PURGE');
   END LOOP;
END;
/
 
SET ECHO OFF
PROMPT ***
PROMPT *** Closing file &pchk_file_name.sql
PROMPT ***
SET ECHO ON
 
SPOOL OFF
 
UNDEFINE pchk_file_name
UNDEFINE pchk_table_name
UNDEFINE pchk_severity
