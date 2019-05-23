--liquibase formatted sql


--changeSet MX-20705:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/** increase the column length to 16 */
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE ref_stage_reason MODIFY ( stage_reason_cd VARCHAR(16)) 
   ');
END;
/

--changeSet MX-20705:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE evt_event MODIFY ( stage_reason_cd VARCHAR(16)) 
   ');
END;
/

--changeSet MX-20705:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE evt_stage MODIFY ( stage_reason_cd VARCHAR(16)) 
   ');
END;
/

--changeSet MX-20705:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE sched_labour MODIFY ( stage_reason_cd VARCHAR(16)) 
   ');
END;
/

--changeSet MX-20705:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      ALTER TABLE org_hr_lic MODIFY ( stage_reason_cd VARCHAR(16)) 
   ');
END;
/

--changeSet MX-20705:6 stripComments:false
/** i) Add the PREVENTEXE & ALLOWEXE event status */
INSERT into ref_event_status (event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   0, 'PREVENTEXE', 0, 'TS', 0, 117, 'Prevent Execution', 'Task execution is being prevented', 'PREVENT EXE', '85', '0',  0, TO_DATE('2011-01-20', 'YYYY-MM-DD'), TO_DATE('2011-01-20', 'YYYY-MM-DD'), 100, 'MXI'
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM ref_event_status WHERE event_status_cd = 'PREVENTEXE');

--changeSet MX-20705:7 stripComments:false
INSERT into ref_event_status (event_status_db_id, event_status_cd, event_type_db_id, event_type_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_status_cd, status_ord, auth_reqd_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT 
   0, 'ALLOWEXE', 0, 'TS', 0, 80, 'Allow Execution', 'Task execution is being allowed', 'ALLOW EXE', '85', '0',  0, TO_DATE('2011-01-20', 'YYYY-MM-DD'), TO_DATE('2011-01-20', 'YYYY-MM-DD'), 100, 'MXI'
FROM
   dual
WHERE
   NOT EXISTS ( SELECT 1 FROM ref_event_status WHERE event_status_cd = 'ALLOWEXE');   

--changeSet MX-20705:8 stripComments:false
/** ii) change the SUSPEND to PREVENTEXE in ref_stage_reason */
UPDATE 
   ref_stage_reason
SET 
   event_status_cd = 'PREVENTEXE'
WHERE
   event_status_cd = 'SUSPEND';   

--changeSet MX-20705:9 stripComments:false
/** iia) Remove the SUSPEND event status */   
DELETE FROM ref_event_status where event_status_cd = 'SUSPEND';

--changeSet MX-20705:10 stripComments:false
/** iii) copy reasons for ALLOWEXE from ref_log_reason to ref_stage_reason */
INSERT INTO ref_stage_reason 
(
   stage_reason_db_id, stage_reason_cd, event_status_db_id, event_status_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, user_reason_cd, rstat_cd
)
SELECT 
   log_reason_db_id, log_reason_cd, 0, 'ALLOWEXE', 0, 1, desc_sdesc, desc_ldesc, user_cd, rstat_cd 
FROM 
   ref_log_reason
WHERE
   log_action_cd = 'TDALLOWEXE'
   AND 
   NOT EXISTS
   (SELECT 1 FROM ref_stage_reason WHERE ref_stage_reason.stage_reason_cd = ref_log_reason.log_reason_cd);