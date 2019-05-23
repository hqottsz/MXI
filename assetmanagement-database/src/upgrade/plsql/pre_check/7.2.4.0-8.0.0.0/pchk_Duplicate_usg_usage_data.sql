WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Duplicate_usg_usage_data
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

----------------------------------------------------------------------------------------------
-- Pre-validation script for 8.0 - Event DBModel Development 
----------------------------------------------------------------------------------------------
----Identification of existing records that will cause Natural Key duplicates during upgrade to usg_usage_record

-- A usage data is a duplicate if all the following attributes match
--   - usage record  
--   - inventory
--   - usage data type

--   -Legacy Key(evt_inv_usage)
--   -  event_db_id
--   -  event_id
--   -  event_inv_id 
--   -  data_type_db_id 
--   -  data_type_id 
-------------------------------------------------------------------------------

SELECT * FROM 
( 
    SELECT '&pchk_severity' AS "SEV",
           evt_inv_usage.event_db_id,
           evt_inv_usage.event_id,
           evt_inv_usage.event_inv_id,
           evt_inv_usage.data_type_db_id,
           evt_inv_usage.data_type_id,
           evt_inv.inv_no_db_id,
           evt_inv.inv_no_id,
           COUNT(*) over (
                          PARTITION BY 
                            evt_inv_usage.event_db_id,
                            evt_inv_usage.event_id, 
                            evt_inv.inv_no_db_id,
                            evt_inv.inv_no_id,
                            inv_num_data.data_type_db_id, 
                            inv_num_data.data_type_id        
                          ) nk_occurences
    FROM
        evt_event
        JOIN evt_inv ON
             evt_inv.event_db_id = evt_event.event_db_id AND
             evt_inv.event_id    = evt_event.event_id 
        JOIN evt_inv_usage ON
             evt_inv_usage.event_db_id  = evt_inv.event_db_id AND
             evt_inv_usage.event_id     = evt_inv.event_id AND
             evt_inv_usage.event_inv_id = evt_inv.event_inv_id
        JOIN inv_num_data ON
             inv_num_data.event_db_id    = evt_inv_usage.event_db_id AND
          inv_num_data.event_id       = evt_inv_usage.event_id AND
          inv_num_data.event_inv_id   = evt_inv_usage.event_inv_id AND
          inv_num_data.data_type_db_id = evt_inv_usage.data_type_db_id AND
          inv_num_data.data_type_id    = evt_inv_usage.data_type_id     
    WHERE
    (
     evt_event.event_type_cd =  'FL' AND
     EXISTS(SELECT 1 FROM evt_stage WHERE event_db_id = evt_event.event_db_id AND event_id = evt_event.event_id AND event_status_cd = 'FLCMPLT')
    )
    OR
    (
     evt_event.event_type_cd = 'UR' AND
    evt_event.event_status_cd = 'URCOMPLETE'
    )
    OR              
    (
     evt_event.event_type_cd = 'UC' AND
    evt_event.event_status_cd = 'UCCOMPLETE'
    )
) WHERE nk_occurences > 1;
  
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
PROMPT *** Closing file &pchk_file_name.sql
PROMPT ***
SET ECHO ON
 
SPOOL OFF
 
UNDEFINE pchk_file_name
UNDEFINE pchk_severity
