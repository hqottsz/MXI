WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Task_Definitions_To_Become_Non_Unique
DEFINE pchk_table_name=pchk_non_unique_candidates
DEFINE pchk_severity=WARNING
 
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
 
--This pre-migration script will list all the active REQs that are on-condition, non-recurring,   
--are not dependency or dependent and currently the unique_bool column is set to 1.  The list of records are candidates
--to be updated, previous user revision, by applying the SQL statements generated the unique_bool_updates.txt file.

--1) Create a temporary table to capture REQs that can potentially be marked as non-unique
CREATE TABLE 
   pchk_non_unique_candidates
AS
SELECT
   task_task.task_db_id,
   task_task.task_id,
   task_task.task_class_cd,
   ref_task_class.class_mode_cd,
   task_task.task_cd,
   task_task.task_name,
   task_task.assmbl_cd,
   task_task.unique_bool,
   task_task.on_condition_bool,
   task_task.recurring_task_bool,
   task_task.task_def_status_cd,
   task_task.revision_ord,
   task_task.task_defn_db_id,
   task_task.task_defn_id
FROM
   task_task
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
WHERE
   -- on-condition = true
   task_task.on_condition_bool = 1  
   AND
   -- currently unique
   task_task.unique_bool = 1 
   AND
   -- CORR and REPL are always non-unique
   (task_task.task_class_db_id, task_task.task_class_cd) NOT IN ((0, 'CORR'), (0, 'REPL')) 
   AND
   -- only REQs
   ref_task_class.class_mode_cd = 'REQ'
   AND
   task_task.recurring_task_bool = 0
   AND
   task_task.task_def_status_db_id = 0 AND
   task_task.task_def_status_cd = 'ACTV'
   AND
   NOT EXISTS
   (  -- is not dependent 
      SELECT
         1
      FROM
         task_task_dep
      WHERE
         task_task_dep.task_db_id = task_task.task_db_id AND
         task_task_dep.task_id = task_task.task_id
   )
   AND
   NOT EXISTS
   ( --is not a dependency 
         SELECT
            1
         FROM
            task_task dependency
            INNER JOIN task_task_dep ON
               task_task_dep.dep_task_defn_db_id = dependency.task_defn_db_id AND
               task_task_dep.dep_task_defn_id    = dependency.task_defn_id
            INNER JOIN task_task dependent ON
               dependent.task_db_id = task_task_dep.task_db_id AND
               dependent.task_id    = task_task_dep.task_id
            INNER JOIN task_defn ON
               task_defn.task_defn_db_id = dependent.task_defn_db_id AND
               task_defn.task_defn_id    = dependent.task_defn_id
         WHERE
            dependency.task_db_id = task_task.task_db_id AND
            dependency.task_id    = task_task.task_id
            AND
            dependent.revision_ord = task_defn.last_revision_ord
            AND NOT
            (
             dependent.task_def_status_db_id = 0 AND
             dependent.task_def_status_cd = 'OBSOLETE'
            )  
   )
   ORDER BY task_task.creation_dt desc;

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
 
--2) List all REQs that are candidates to be updated with unique_bool = 0
SELECT
   '&pchk_severity' AS SEV,
   pchk_non_unique_candidates.task_db_id,
   pchk_non_unique_candidates.task_id,
   pchk_non_unique_candidates.class_mode_cd,
   pchk_non_unique_candidates.task_class_cd,
   pchk_non_unique_candidates.task_cd,
   pchk_non_unique_candidates.task_name,
   pchk_non_unique_candidates.assmbl_cd,
   pchk_non_unique_candidates.unique_bool,
   pchk_non_unique_candidates.on_condition_bool,
   pchk_non_unique_candidates.recurring_task_bool,
   pchk_non_unique_candidates.task_def_status_cd
FROM
   pchk_non_unique_candidates;

SPOOL OFF
SET MARKUP HTML OFF
SET TERMOUT ON
 
SPOOL log.txt APPEND
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to log.txt
PROMPT ***
SET ECHO ON
 
SET ECHO OFF
PROMPT ***
PROMPT *** Spooling to &pchk_file_name.txt
PROMPT ***
SET ECHO ON
 
SPOOL OFF
 
SET ECHO OFF
SPOOL &pchk_file_name.txt
SET FEEDBACK OFF
SET HEADING OFF
SET TERMOUT OFF
 
--3) Generate the SQL update statements to set unique_bool = 0, into a text file
SELECT
   '--this update is for Task Code[' || pchk_non_unique_candidates.task_cd ||'], ' || 
   'Task Name[' || pchk_non_unique_candidates.task_name || ']' || CHR(13) || 
   CHR(13) ||
   'UPDATE task_task SET unique_bool = 0 WHERE task_db_id = ' || pchk_non_unique_candidates.task_db_id || 
   ' AND task_id = ' || pchk_non_unique_candidates.task_id || ';' || CHR(13) 
FROM
   pchk_non_unique_candidates;

SET TERMOUT ON
SET FEEDBACK ON
SET HEADING ON
SPOOL OFF
 
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
