WHENEVER OSERROR EXIT 3
WHENEVER SQLERROR EXIT 3
 
DEFINE pchk_file_name=pchk_Mandatory_Fields_fl_leg
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
--Identification of existing records that are missing mandatory fields required to populate FL_LEG.

--Such records may cause the database upgrade to fail
 
-------------------------------------------------------------------------------

SELECT * FROM 
(
  SELECT 
      '&pchk_severity' AS "SEV",
      evt_event.event_db_id,
      evt_event.event_id,   
      CASE 
         WHEN 
            evt_event.event_status_db_id = 0 
         THEN 
            (
               CASE 
                  WHEN 
                     substr( evt_event.event_status_cd, 0, 2) = 'FL'
                  THEN 
                     'MX' || substr( evt_event.event_status_cd, 3)
                  ELSE 
                     'MX' || evt_event.event_status_cd 
               END
            )
         ELSE 
            evt_event.event_status_cd
      END AS flight_leg_status_cd,
      departure_event_loc.loc_db_id AS departure_loc_db_id,
      departure_event_loc.loc_id AS departure_loc_id,
     NVL(
         evt_event.actual_start_dt,
         evt_event.sched_start_dt
      ) AS actual_departure_dt,
      arrival_event_loc.loc_db_id as arrival_loc_db_id,
      arrival_event_loc.loc_id AS arrival_loc_id,
      NVL(
         evt_event.event_dt,
         evt_event.sched_end_dt
      ) AS actual_arrival_dt
  FROM 
      jl_flight
      INNER JOIN evt_event ON 
         evt_event.event_db_id = jl_flight.flight_db_id AND
         evt_event.event_id    = jl_flight.flight_id 
      LEFT OUTER JOIN evt_inv ON 
         evt_inv.event_db_id   = evt_event.event_db_id AND
         evt_inv.event_id      = evt_event.event_id AND
         evt_inv.main_inv_bool = 1    
      LEFT OUTER JOIN evt_loc departure_event_loc ON 
         departure_event_loc.event_db_id  = evt_event.event_db_id AND
         departure_event_loc.event_id     = evt_event.event_id AND
         departure_event_loc.event_loc_id = 1
      LEFT OUTER JOIN evt_loc arrival_event_loc ON 
         arrival_event_loc.event_db_id  = evt_event.event_db_id AND
         arrival_event_loc.event_id     = evt_event.event_id AND
         arrival_event_loc.event_loc_id = 2      
)
WHERE   
      flight_leg_status_cd IS NULL OR 
      departure_loc_db_id IS NULL OR
      actual_departure_dt IS NULL OR
      arrival_loc_db_id IS NULL OR
      actual_arrival_dt IS NULL
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
