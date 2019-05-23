--liquibase formatted sql


--changeSet QC-4239:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table REF_TASK_CLASS modify (
    "TASK_CLASS_CD" Varchar2 (16) NOT NULL DEFERRABLE
  )
  ');
 END;
 / 

--changeSet QC-4239:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
  BEGIN
   utl_migr_schema_pkg.table_column_modify('
   Alter table REF_TASK_SUBCLASS modify (
     "TASK_CLASS_CD" Varchar2 (16) NOT NULL DEFERRABLE
   )
   ');
  END;
 / 

--changeSet QC-4239:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
   BEGIN
    utl_migr_schema_pkg.table_column_modify('
    Alter table SCHED_STASK modify (
      "TASK_CLASS_CD" Varchar2 (16) NOT NULL DEFERRABLE
    )
    ');
   END;
 /  

--changeSet QC-4239:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
    BEGIN
     utl_migr_schema_pkg.table_column_modify('
     Alter table TASK_TASK modify (
       "TASK_CLASS_CD" Varchar2 (16)
     )
     ');
    END;
 /   

--changeSet QC-4239:5 stripComments:false
  DELETE FROM ref_Task_class WHERE task_class_db_id = 0 AND task_class_cd = 'OPEN';  

--changeSet QC-4239:6 stripComments:false
    DELETE FROM ref_Task_class WHERE task_class_db_id = 0 AND task_class_cd = 'CLOSE';   

--changeSet QC-4239:7 stripComments:false
  INSERT INTO
     ref_task_class
     (
        task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
     )
     SELECT 0, 'OPENPANEL', 0, 77,  'Open Panel Job Instruction Card', 'An open panel job card is used to specify instructions for opening a panel. Open panel job cards cannot be added to requirements.', 0, 1, 0, 'JIC', 0, TO_DATE('2010-01-26', 'YYYY-MM-DD'), TO_DATE('2010-01-26', 'YYYY-MM-DD'), 100, 'MXI'
     FROM
        dual
     WHERE
        NOT EXISTS ( SELECT 1 FROM ref_task_class WHERE task_class_db_id = 0 AND task_class_cd = 'OPENPANEL' );          

--changeSet QC-4239:8 stripComments:false
  INSERT INTO
     ref_task_class
     (
        task_class_db_id, task_class_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, unique_bool, workscope_bool, auto_complete_bool, class_mode_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user
     )
     SELECT 0, 'CLOSEPANEL', 0, 77,  'Close PanelJob Instruction Card', 'An close panel job card is used to specify instructions for closing a panel. Close panel job cards cannot be added to requirements.', 0, 1, 0, 'JIC', 0, TO_DATE('2010-01-26', 'YYYY-MM-DD'), TO_DATE('2010-01-26', 'YYYY-MM-DD'), 100, 'MXI'
     FROM
        dual
     WHERE
        NOT EXISTS ( SELECT 1 FROM ref_task_class WHERE task_class_db_id = 0 AND task_class_cd = 'CLOSEPANEL' );                  

--changeSet QC-4239:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- The materialized view MV_FLEETLIST includes the TASK_CLASS_CD column of the SCHED_STASK table 
-- that was modified above, therefore, drop and recreate the materialized view.
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('MV_FLEETLIST');
END;
/ 

--changeSet QC-4239:10 stripComments:false
CREATE MATERIALIZED VIEW MV_FLEETLIST
BUILD DEFERRED
REFRESH ON DEMAND
WITH PRIMARY KEY
AS
SELECT
   -- aircraft details
   inv_inv.inv_no_db_id AS aircraft_db_id,
   inv_inv.inv_no_id AS aircraft_id,
   inv_inv.inv_no_sdesc AS aircraft_sdesc,
   inv_ac_reg.inv_capability_cd AS aircraft_capability_cd,
   inv_inv.authority_db_id AS aircraft_authority_db_id,
   inv_inv.authority_id AS aircraft_authority_id,
   inv_inv.assmbl_db_id AS aircraft_assmbl_db_id,
   inv_inv.assmbl_cd AS aircraft_assmbl_cd,
   --aircraft location details
   acft_loc.loc_db_id AS aircraft_loc_db_id,
   acft_loc.loc_id AS aircraft_loc_id,
   acft_loc.loc_cd AS aircraft_loc_cd,
   -- work package details
   check_evt_event.event_db_id AS wp_db_id,
   check_evt_event.event_id AS wp_id,
   check_evt_event.event_sdesc AS wp_desc,
   getSchedWorkTypes(check_sched_stask.sched_db_id, check_sched_stask.sched_id) AS wp_work_type_cd,
   check_evt_event.event_status_cd AS wp_status_cd,
   check_sched_stask.task_class_cd AS wp_class_cd,
   ref_task_subclass.user_subclass_cd AS wp_user_subclass_cd,
   -- work package scheduling details
   check_evt_event.actual_start_gdt AS wp_actual_start_gdt,
   check_evt_event.sched_start_gdt AS wp_sched_start_gdt,
   check_evt_event.sched_end_gdt AS wp_sched_end_gdt,
   check_evt_event.event_gdt AS wp_event_gdt,
   check_inv_loc.loc_db_id AS wp_work_loc_db_id,
   check_inv_loc.loc_id AS wp_work_loc_id,
   check_inv_loc.loc_cd AS wp_work_loc_cd,
   -- flight locations
   dep_inv_loc.loc_db_id AS dep_loc_db_id,
   dep_inv_loc.loc_id AS dep_loc_id,
   dep_inv_loc.loc_cd AS dep_loc_cd,
   arr_inv_loc.loc_db_id AS arr_loc_db_id,
   arr_inv_loc.loc_id AS arr_loc_id,
   arr_inv_loc.loc_cd AS arr_loc_cd,
   -- sorting info
   -- IF IN WORK sort before other status based on actual start date
   -- ELSE there is a scheduled check, sort before unscheduled
   DECODE( check_evt_event.event_status_cd, 'IN WORK', '0', '1' ) ||
      DECODE( check_evt_event.actual_start_gdt,
            null,
            DECODE( check_evt_event.sched_start_gdt, NULL,
               'A',
               TO_CHAR( check_evt_event.sched_start_gdt, 'YYYY-MM-DD HH24:MI:SS'
            )
         ),
         to_char( check_evt_event.actual_start_gdt, 'YYYY-MM-DD HH24:MI:SS')
   ) AS start_date_sorting
FROM
   -- aircraft details
   inv_ac_reg,
   inv_inv,
   inv_loc acft_loc,
   -- next scheduled work package details
   (  SELECT DISTINCT
         evt_inv.h_inv_no_db_id,
         evt_inv.h_inv_no_id,
         FIRST_VALUE( sched_stask.sched_db_id )
            OVER ( PARTITION BY evt_inv.h_inv_no_db_id, evt_inv.h_inv_no_id ORDER BY DECODE(evt_event.event_status_cd, 'IN WORK', 0, 1) ASC, evt_event.sched_start_gdt ) AS sched_db_id,
         FIRST_VALUE( sched_stask.sched_id )
            OVER ( PARTITION BY evt_inv.h_inv_no_db_id, evt_inv.h_inv_no_id ORDER BY DECODE(evt_event.event_status_cd, 'IN WORK', 0, 1) ASC, evt_event.sched_start_gdt ) AS sched_id
      FROM
         evt_inv,
         evt_event,
         sched_stask,
         ref_task_class
      WHERE
         ref_task_class.task_class_db_id = 0 AND
         ref_task_class.task_class_cd    = 'CHECK'
         AND
         sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND
         sched_stask.task_class_cd    = ref_task_class.task_class_cd
         AND
         evt_event.event_db_id = sched_stask.sched_db_id AND
         evt_event.event_id    = sched_stask.sched_id AND
         evt_event.hist_bool   = 0
         AND
         evt_inv.event_db_id   = evt_event.event_db_id AND
         evt_inv.event_id      = evt_event.event_id    AND
         evt_inv.main_inv_bool = 1
   ) next_sched_stask,
   evt_event check_evt_event,
   sched_stask check_sched_stask,
   ref_task_subclass,
   evt_loc check_evt_loc,
   inv_loc check_inv_loc,
   -- flight details
   (  SELECT DISTINCT
         evt_inv.h_inv_no_db_id,
         evt_inv.h_inv_no_id,
         FIRST_VALUE( evt_event.event_db_id )
            OVER ( PARTITION BY evt_inv.h_inv_no_db_id, evt_inv.h_inv_no_id ORDER BY evt_event.actual_start_gdt ) AS event_db_id,
         FIRST_VALUE( evt_event.event_id )
            OVER ( PARTITION BY evt_inv.h_inv_no_db_id, evt_inv.h_inv_no_id ORDER BY evt_event.actual_start_gdt ) AS event_id
      FROM
         evt_inv,
         evt_event,
         ref_event_type
      WHERE
         ref_event_type.event_type_db_id = 0 AND
         ref_event_type.event_type_cd    = 'FL'
         AND
         evt_event.event_type_db_id = ref_event_type.event_type_db_id AND
         evt_event.event_type_cd    = ref_event_type.event_type_cd
         AND
         evt_event.hist_bool = 0
         AND
         (  evt_event.event_status_db_id = 0
            AND
            evt_event.event_status_cd IN ('FLOUT', 'FLOFF', 'FLON', 'FLIN', 'DIVERT')
         )
         AND
         evt_inv.event_db_id = evt_event.event_db_id AND
         evt_inv.event_id    = evt_event.event_id
         AND
         evt_inv.main_inv_bool = 1
   ) current_flight,
   evt_loc dep_evt_loc,
   inv_loc dep_inv_loc,
   evt_loc arr_evt_loc,
   inv_loc arr_inv_loc
WHERE
   -- get the aircraft details
   inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
   inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
   AND
   inv_inv.inv_class_db_id = 0 AND
   inv_inv.inv_class_cd    = 'ACFT' AND 
   inv_inv.rstat_cd = 0
   AND
   inv_inv.locked_bool  = 0
   AND
   acft_loc.loc_db_id = inv_inv.loc_db_id AND
   acft_loc.loc_id    = inv_inv.loc_id
   AND
   -- get the next work package details
   next_sched_stask.h_inv_no_db_id (+)= inv_inv.inv_no_db_id AND
   next_sched_stask.h_inv_no_id    (+)= inv_inv.inv_no_id
   AND
   check_evt_event.event_db_id (+)= next_sched_stask.sched_db_id AND
   check_evt_event.event_id    (+)= next_sched_stask.sched_id
   AND
   check_sched_stask.sched_db_id (+)= check_evt_event.event_db_id AND
   check_sched_stask.sched_id    (+)= check_evt_event.event_id
   AND
   ref_task_subclass.task_subclass_db_id  (+)= check_sched_stask.task_subclass_db_id AND
   ref_task_subclass.task_subclass_cd     (+)= check_sched_stask.task_subclass_cd
   AND
   check_evt_loc.event_db_id (+)= check_evt_event.event_db_id AND
   check_evt_loc.event_id    (+)= check_evt_event.event_id
   AND
   check_inv_loc.loc_db_id (+)= check_evt_loc.loc_db_id AND
   check_inv_loc.loc_id    (+)= check_evt_loc.loc_id
   AND
   -- get the current flight details
   current_flight.h_inv_no_db_id  (+)= inv_inv.inv_no_db_id AND
   current_flight.h_inv_no_id     (+)= inv_inv.inv_no_id
   AND
   dep_evt_loc.event_db_id  (+)= current_flight.event_db_id AND
   dep_evt_loc.event_id     (+)= current_flight.event_id AND
   dep_evt_loc.event_loc_id (+)= 1
   AND
   dep_inv_loc.loc_db_id (+)= dep_evt_loc.loc_db_id AND
   dep_inv_loc.loc_id    (+)= dep_evt_loc.loc_id
   AND
   arr_evt_loc.event_db_id  (+)= current_flight.event_db_id AND
   arr_evt_loc.event_id     (+)= current_flight.event_id AND
   arr_evt_loc.event_loc_id (+)= 2
   AND
   arr_inv_loc.loc_db_id (+)= arr_evt_loc.loc_db_id AND
   arr_inv_loc.loc_id    (+)= arr_evt_loc.loc_id;