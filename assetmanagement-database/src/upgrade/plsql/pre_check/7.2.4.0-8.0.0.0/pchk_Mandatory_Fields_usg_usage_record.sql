WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Mandatory_Fields_usg_usage_record
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
--Identification of existing records that are missing mandatory fields required to populate USG_USAGE_RECORD

--Such records may cause the database upgrade to fail

-------------------------------------------------------------------------------

SELECT 
    '&pchk_severity' AS "SEV",
    subqry.* FROM 
(     
    SELECT    
        evt_event.event_db_id,
        evt_event.event_id,
        evt_event.event_type_cd,         
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS usage_dt, 
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS record_dt
    FROM    
      evt_event
      INNER JOIN evt_inv ON evt_inv.event_db_id   = evt_event.event_db_id AND
                  evt_inv.event_id      = evt_event.event_id AND
                  evt_inv.main_inv_bool = 1 
      LEFT OUTER JOIN evt_stage ON evt_event.event_db_id     = evt_stage.event_db_id AND
                    evt_event.event_id        = evt_stage.event_id  
                   AND
                    evt_stage.event_status_db_id = 0 AND
                   evt_stage.event_status_cd    = 'UCPEND'
    WHERE
      evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'UC'  
      AND
      evt_event.event_status_db_id = 0 AND
      evt_event.event_status_cd    = 'UCCOMPLETE'   
    UNION
    SELECT   
        evt_event.event_db_id,
        evt_event.event_id,
        evt_event.event_type_cd, 
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS usage_dt, 
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS record_dt
    FROM    
      evt_event  
      INNER JOIN evt_inv ON evt_inv.event_db_id = evt_event.event_db_id AND
                  evt_inv.event_id      = evt_event.event_id AND
                  evt_inv.main_inv_bool = 1 
      LEFT OUTER JOIN evt_stage ON evt_event.event_db_id     = evt_stage.event_db_id AND
                    evt_event.event_id        = evt_stage.event_id  
                   AND
                    evt_stage.event_status_db_id = 0 AND
                   evt_stage.event_status_cd = 'URPEND'  
    WHERE
      evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'UR'  
      AND
      evt_event.event_status_db_id = 0 AND
      evt_event.event_status_cd = 'URCOMPLETE'
    UNION 
    SELECT  
        evt_event.event_db_id,
        evt_event.event_id,
        evt_event.event_type_cd,             
      evt_event.event_dt AS usage_dt,  
      NVL(evt_event.event_dt, evt_stage.stage_dt) AS record_dt
    FROM    
      evt_event  
      INNER JOIN evt_inv ON evt_inv.event_db_id = evt_event.event_db_id AND
                  evt_inv.event_id      = evt_event.event_id AND
                  evt_inv.main_inv_bool = 1                 
      INNER JOIN evt_stage ON evt_event.event_db_id     = evt_stage.event_db_id AND
               evt_event.event_id        = evt_stage.event_id  
               AND                               
               evt_event.event_status_db_id = evt_stage.event_status_db_id AND
               evt_event.event_status_cd    = evt_stage.event_status_cd                          
    WHERE
      evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'FL'   
      AND  
      (evt_stage.event_db_id, evt_stage.event_id, evt_stage.stage_dt, evt_stage.stage_id) IN 
      (
      SELECT
         max_evt_stage.event_db_id, max_evt_stage.event_id, MAX(max_evt_stage.stage_dt) AS stage_dt, MAX(max_evt_stage.stage_id) AS stage_id
      FROM 
         evt_stage max_evt_stage
      WHERE 
         max_evt_stage.event_status_db_id = 0 AND
         max_evt_stage.event_status_cd    = 'FLCMPLT'      
      GROUP BY  
         max_evt_stage.event_db_id, 
         max_evt_stage.event_id
      )
    ) subqry
WHERE 
      usage_dt IS NULL OR
      record_dt IS NULL
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
 
SET ECHO OFF
PROMPT ***
PROMPT *** Closing file &pchk_file_name.sql
PROMPT ***
SET ECHO ON
 
SPOOL OFF
 
UNDEFINE pchk_file_name
UNDEFINE pchk_severity
