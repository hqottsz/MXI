--liquibase formatted sql


--changeSet DEV-704:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Schema Changes --		
--
-- Add new columns to SD_FAULT
--
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table SD_FAULT add FAULT_LOG_TYPE_DB_ID number(10,0) check (FAULT_LOG_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
');
END;
/

--changeSet DEV-704:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table SD_FAULT add FAULT_LOG_TYPE_CD varchar2 (8)
');
END;
/

--changeSet DEV-704:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table SD_FAULT add FRM_SDESC varchar2 (80)
');
END;
/

--changeSet DEV-704:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table SD_FAULT add EXT_RAISED_BOOL number(1,0)  check (EXT_RAISED_BOOL IN (0, 1) ) DEFERRABLE 
');
END;
/

--changeSet DEV-704:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table SD_FAULT add EXT_CONTROLLED_BOOL number(1,0)  check (EXT_CONTROLLED_BOOL IN (0, 1) ) DEFERRABLE
');
END;
/

--changeSet DEV-704:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Create Logbook Type Reference Table 
--
BEGIN
utl_migr_schema_pkg.table_create('
 create table "REF_FAULT_LOG_TYPE" (
	FAULT_LOG_TYPE_DB_ID number(10,0) NOT NULL DEFERRABLE  check (FAULT_LOG_TYPE_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE,
	FAULT_LOG_TYPE_CD varchar2 (8) NOT NULL DEFERRABLE,
	NAME_SDESC varchar2 (80) NOT NULL DEFERRABLE,
	DESC_LDESC varchar2 (4000),
	USER_CD varchar2 (8) NOT NULL DEFERRABLE,
	RSTAT_CD number(3,0) NOT NULL DEFERRABLE  check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE,
	CREATION_DT date NOT NULL DEFERRABLE,
	REVISION_DT date NOT NULL DEFERRABLE,
	REVISION_DB_ID number(10,0) NOT NULL DEFERRABLE  check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE,
	REVISION_USER varchar2 (30) NOT NULL DEFERRABLE,
  Constraint PK_REF_FAULT_LOG_TYPE primary key (FAULT_LOG_TYPE_DB_ID,FAULT_LOG_TYPE_CD) 
 )
');
END;
/

--changeSet DEV-704:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add foreign key constraint from sd_fault to ref_fault_log_type
BEGIN
utl_migr_schema_pkg.table_constraint_add('
alter table SD_FAULT add constraint FK_SDFAULT_REFFAULTLOGTYPE foreign key (FAULT_LOG_TYPE_DB_ID,FAULT_LOG_TYPE_CD) references REF_FAULT_LOG_TYPE (FAULT_LOG_TYPE_DB_ID,FAULT_LOG_TYPE_CD)  DEFERRABLE
');
END;
/

--changeSet DEV-704:8 stripComments:false
-- Migrate exisiting data in sd_fault
update sd_fault set ext_raised_bool=0 where ext_raised_bool is null; 

--changeSet DEV-704:9 stripComments:false
update sd_fault set ext_controlled_bool=0 where ext_controlled_bool is null; 

--changeSet DEV-704:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Modify columns to add not null constraint and default 0 now that data has been migrated
BEGIN
utl_migr_schema_pkg.table_column_modify('
alter table SD_FAULT modify EXT_RAISED_BOOL default 0 NOT NULL DEFERRABLE
');
END;
/

--changeSet DEV-704:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
alter table SD_FAULT modify EXT_CONTROLLED_BOOL default 0 NOT NULL DEFERRABLE
');
END;
/

--changeSet DEV-704:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add ref_fault_log_type foriegn key constraints
BEGIN
utl_migr_schema_pkg.table_constraint_add('
alter table REF_FAULT_LOG_TYPE add constraint FK_MIMDB_REFFAULTLOGTYPE foreign key (FAULT_LOG_TYPE_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
');
END;
/

--changeSet DEV-704:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
alter table REF_FAULT_LOG_TYPE add constraint FK_MIMRSTAT_REFFAULTLOGTYPE foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
');
END;
/

--changeSet DEV-704:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add triggers for ref_fault_log_type
CREATE OR REPLACE TRIGGER TIBR_REF_FAULT_LOG_TYPE BEFORE INSERT
   ON REF_FAULT_LOG_TYPE REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-704:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TUBR_REF_FAULT_LOG_TYPE BEFORE UPDATE
   ON REF_FAULT_LOG_TYPE REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-704:16 stripComments:false
-- Update vw_evt_fault to expose new sd_fault attributes
-- Remove view first, according to wiki instructions
CREATE OR REPLACE VIEW VW_EVT_FAULT
     (fault_db_id, 
      fault_id,
      fault_sdesc,
      bitmap_db_id,
      bitmap_tag,
      bitmap_name,
      editor_hr_db_id,
      editor_hr_id,
      editor_hr_sdesc,
      fault_status_db_id,
      fault_status_cd,
      fault_status_user_cd,
      fault_ldesc,
      fault_dt,
      fault_gdt,
      raised_dt,
      raised_gdt,
      ext_key_sdesc,
      doc_ref_sdesc,
      hist_bool,
      sub_event_ord,
      fail_mode_db_id,
      fail_mode_id,
      fail_catgry_db_id,
      fail_catgry_cd,
      flight_stage_db_id,
      flight_stage_cd,
      fail_sev_db_id,
      fail_sev_cd,
      fail_priority_db_id,
      fail_priority_cd,
      fail_type_db_id,
      fail_type_cd,
      fault_source_db_id,
      fault_source_cd,
      found_by_hr_db_id,
      found_by_hr_id,
      fail_defer_db_id,
      fail_defer_cd, 
      defer_cd_sdesc, 
      defer_ref_sdesc, 
      prec_proc_ldesc, 
      sdr_bool, 
      eval_bool, 
      maint_evt_bool, 
      op_restriction_ldesc, 
      fault_log_type_db_id,
      fault_log_type_cd,
      frm_sdesc,
      ext_raised_bool,
      ext_controlled_bool, 
      log_inv_no_db_id, 
      log_inv_no_id, 
      log_inv_no_sdesc, 
      log_nh_inv_no_db_id, 
      log_nh_inv_no_id, 
      log_nh_inv_no_sdesc, 
      log_assmbl_inv_no_db_id, 
      log_assmbl_inv_no_id, 
      log_assmbl_inv_no_sdesc, 
      log_h_inv_no_db_id, 
      log_h_inv_no_id, 
      log_h_inv_no_sdesc, 
      log_assmbl_db_id, 
      log_assmbl_cd, 
      log_assmbl_bom_id, 
      log_assmbl_pos_id, 
      log_assmbl_bom_cd, 
      log_eqp_pos_cd, 
      log_bom_part_db_id, 
      log_bom_part_id, 
      log_bom_part_cd, 
      log_part_no_db_id, 
      log_part_no_id, 
      log_part_no_sdesc, 
      log_part_no_oem, 
      corrective_sched_db_id, 
      corrective_sched_id, 
      corrective_sched_sdesc, 
      previous_incident_db_id, 
      previous_incident_id, 
      previous_incident_sdesc, 
      previous_incident_raised_dt,
      previous_sched_db_id, 
      previous_sched_id, 
      previous_sched_sdesc, 
      next_incident_db_id, 
      next_incident_id, 
      next_incident_sdesc, 
      next_sched_db_id, 
      next_sched_id, 
      next_sched_sdesc, 
      parent_flight_id, 
      parent_flight_sdesc, 
      parent_flight_type_cd, 
      parent_sched_db_id, 
      parent_sched_id, 
      parent_sched_sdesc, 
      parent_barcode_sdesc, 
      contact_info_sdesc, 
      init_fault_db_id, 
      init_fault_id, 
      init_fault_sdesc)
AS
SELECT /*+ rule */
      sd_fault.fault_db_id,
      sd_fault.fault_id,
      evt_event.event_sdesc,
      evt_event.bitmap_db_id,
      evt_event.bitmap_tag,
      ref_bitmap.bitmap_name,
      evt_event.editor_hr_db_id,
      evt_event.editor_hr_id,
      DECODE( evt_event.editor_hr_db_id, NULL, NULL, org_hr.hr_cd || ' (' || utl_user.last_name || ', ' || utl_user.first_name || ')' ),
      evt_event.event_status_db_id,
      evt_event.event_status_cd,
      ref_event_status.user_status_cd,
      evt_event.event_ldesc,
      evt_event.event_dt,
      evt_event.event_gdt,
      evt_event.actual_start_dt,
      evt_event.actual_start_gdt,
      evt_event.ext_key_sdesc,
      evt_event.doc_ref_sdesc,
      evt_event.hist_bool,
      evt_event.sub_event_ord,
      sd_fault.fail_mode_db_id,
      sd_fault.fail_mode_id,
      sd_fault.fail_catgry_db_id,
      sd_fault.fail_catgry_cd,
      sd_fault.flight_stage_db_id,
      sd_fault.flight_stage_cd,
      sd_fault.fail_sev_db_id,
      sd_fault.fail_sev_cd,
      sd_fault.fail_priority_db_id,
      sd_fault.fail_priority_cd,
      sd_fault.fail_type_db_id,
      sd_fault.fail_type_cd,
      sd_fault.fault_source_db_id,
      sd_fault.fault_source_cd,
      sd_fault.found_by_hr_db_id,
      sd_fault.found_by_hr_id,
      sd_fault.fail_defer_db_id,
      sd_fault.fail_defer_cd,
      sd_fault.defer_cd_sdesc,
      sd_fault.defer_ref_sdesc,
      sd_fault.prec_proc_ldesc,
      sd_fault.sdr_bool,
      sd_fault.eval_bool,
      sd_fault.maint_evt_bool,
      sd_fault.op_restriction_ldesc,
      sd_fault.fault_log_type_db_id,
      sd_fault.fault_log_type_cd,
      sd_fault.frm_sdesc,
      sd_fault.ext_raised_bool,
      sd_fault.ext_controlled_bool,      
      evt_inv.inv_no_db_id,
      evt_inv.inv_no_id,
      inv_inv.inv_no_sdesc,
      evt_inv.nh_inv_no_db_id,
      evt_inv.nh_inv_no_id,
      nh_inv_inv.inv_no_sdesc,
      evt_inv.assmbl_inv_no_db_id,
      evt_inv.assmbl_inv_no_id,
      assmbl_inv_inv.inv_no_sdesc,
      evt_inv.h_inv_no_db_id,
      evt_inv.h_inv_no_id,
      h_inv_inv.inv_no_sdesc,
      evt_inv.assmbl_db_id,
      evt_inv.assmbl_cd,
      evt_inv.assmbl_bom_id,
      evt_inv.assmbl_pos_id,
      eqp_assmbl_bom.assmbl_bom_cd,
      eqp_assmbl_pos.eqp_pos_cd,
      evt_inv.bom_part_db_id,
      evt_inv.bom_part_id,
      eqp_bom_part.bom_part_cd,
      evt_inv.part_no_db_id,
      evt_inv.part_no_id,
      eqp_part_no.part_no_sdesc,
      eqp_part_no.part_no_oem,
      corrective_sched_event.event_db_id,
      corrective_sched_event.event_id,
      corrective_sched_event.event_sdesc,
      previous_incident_event.event_db_id,
      previous_incident_event.event_id,
      previous_incident_event.event_sdesc,
      previous_incident_event.actual_start_dt,
      previous_sched_event.event_db_id,
      previous_sched_event.event_id,
      previous_sched_event.event_sdesc,
      next_incident_event.event_db_id,
      next_incident_event.event_id,
      next_incident_event.event_sdesc,
      next_sched_event.event_db_id,
      next_sched_event.event_id,
      next_sched_event.event_sdesc,
      sd_fault.leg_id,
      fl_leg.leg_no,
      'FL',
      parent_sched_event.event_db_id,
      parent_sched_event.event_id,
      parent_sched_event.event_sdesc,
      parent_sched_stask.barcode_sdesc,
      evt_event.contact_info_sdesc,
      init_fault_event.event_db_id,
      init_fault_event.event_id,
      init_fault_event.event_sdesc
 FROM evt_event,
      evt_inv,
      evt_ietm,
      sd_fault,
      ref_event_status,
      ref_bitmap,
      evt_event_rel corrective_sched_rel,
      evt_event_rel previous_incident_rel,
      evt_event_rel previous_sched_rel,
      evt_event_rel next_incident_rel,
      evt_event_rel next_sched_rel,
      evt_event_rel parent_sched_rel,
      evt_event_rel init_fault_rel,
      evt_event corrective_sched_event,
      evt_event previous_incident_event,
      evt_event previous_sched_event,
      evt_event next_incident_event,
      evt_event next_sched_event,
      fl_leg,
      evt_event parent_sched_event,
      evt_event init_fault_event,
      sched_stask parent_sched_stask,
      eqp_assmbl_bom,
      eqp_assmbl_pos,
      eqp_bom_part,
      eqp_part_no,
      inv_inv,
      inv_inv nh_inv_inv,
      inv_inv assmbl_inv_inv,
      inv_inv h_inv_inv,
      org_hr,
      utl_user
      WHERE evt_event.event_db_id = sd_fault.fault_db_id AND
      evt_event.event_id    = sd_fault.fault_id
      AND
      evt_inv.event_db_id = evt_event.event_db_id AND
      evt_inv.event_id    = evt_event.event_id AND
      evt_inv.main_inv_bool = 1
      AND
      eqp_assmbl_bom.assmbl_db_id  (+)= evt_inv.assmbl_db_id AND
      eqp_assmbl_bom.assmbl_cd     (+)= evt_inv.assmbl_cd AND
      eqp_assmbl_bom.assmbl_bom_id (+)= evt_inv.assmbl_bom_id
      AND
      eqp_assmbl_pos.assmbl_db_id  (+)= evt_inv.assmbl_db_id AND
      eqp_assmbl_pos.assmbl_cd     (+)= evt_inv.assmbl_cd AND
      eqp_assmbl_pos.assmbl_bom_id (+)= evt_inv.assmbl_bom_id AND
      eqp_assmbl_pos.assmbl_pos_id (+)= evt_inv.assmbl_pos_id
      AND
      eqp_bom_part.bom_part_db_id (+)= evt_inv.bom_part_db_id AND
      eqp_bom_part.bom_part_id    (+)= evt_inv.bom_part_id
      AND
      inv_inv.inv_no_db_id (+)= evt_inv.inv_no_db_id AND
      inv_inv.inv_no_id    (+)= evt_inv.inv_no_id
      AND
      nh_inv_inv.inv_no_db_id (+)= evt_inv.nh_inv_no_db_id AND
      nh_inv_inv.inv_no_id    (+)= evt_inv.nh_inv_no_id
      AND
      h_inv_inv.inv_no_db_id (+)= evt_inv.h_inv_no_db_id AND
      h_inv_inv.inv_no_id    (+)= evt_inv.h_inv_no_id
      AND
      assmbl_inv_inv.inv_no_db_id (+)= evt_inv.assmbl_inv_no_db_id AND
      assmbl_inv_inv.inv_no_id    (+)= evt_inv.assmbl_inv_no_id
      AND
      eqp_part_no.part_no_db_id (+)= evt_inv.part_no_db_id AND
      eqp_part_no.part_no_id    (+)= evt_inv.part_no_id
      AND
      evt_ietm.event_db_id (+)= evt_event.event_db_id AND
      evt_ietm.event_id    (+)= evt_event.event_id
      AND
      corrective_sched_rel.event_db_id (+)= evt_event.event_db_id AND
      corrective_sched_rel.event_id    (+)= evt_event.event_id AND
      corrective_sched_rel.rel_type_cd (+)= 'CORRECT'
      AND
      corrective_sched_event.event_db_id (+)= corrective_sched_rel.rel_event_db_id AND
      corrective_sched_event.event_id    (+)= corrective_sched_rel.rel_event_id
      AND
      previous_incident_rel.event_db_id (+)= evt_event.event_db_id AND
      previous_incident_rel.event_id    (+)= evt_event.event_id AND
      previous_incident_rel.rel_type_cd (+)= 'RECUR'
      AND
      previous_incident_event.event_db_id (+)= previous_incident_rel.rel_event_db_id AND
      previous_incident_event.event_id    (+)= previous_incident_rel.rel_event_id
      AND
      previous_sched_rel.event_db_id (+)= previous_incident_event.event_db_id AND
      previous_sched_rel.event_id    (+)= previous_incident_event.event_id AND
      previous_sched_rel.rel_type_cd (+)= 'CORRECT'
      AND
      previous_sched_event.event_db_id (+)= previous_sched_rel.rel_event_db_id AND
      previous_sched_event.event_id    (+)= previous_sched_rel.rel_event_id
      AND
      next_incident_rel.rel_event_db_id (+)= evt_event.event_db_id AND
      next_incident_rel.rel_event_id    (+)= evt_event.event_id AND
      next_incident_rel.rel_type_cd     (+)= 'RECUR'
      AND
      next_incident_event.event_db_id (+)= next_incident_rel.event_db_id AND
      next_incident_event.event_id    (+)= next_incident_rel.event_id
      AND
      next_sched_rel.event_db_id (+)= next_incident_event.event_db_id AND
      next_sched_rel.event_id    (+)= next_incident_event.event_id AND
      next_sched_rel.rel_type_cd (+)= 'CORRECT'
      AND
      next_sched_event.event_db_id (+)= next_sched_rel.rel_event_db_id AND
      next_sched_event.event_id    (+)= next_sched_rel.rel_event_id
      AND
      fl_leg.leg_id (+)= sd_fault.leg_id 
      AND
      parent_sched_rel.rel_event_db_id (+)= evt_event.event_db_id AND
      parent_sched_rel.rel_event_id    (+)= evt_event.event_id AND
      parent_sched_rel.rel_type_cd     (+)= 'DISCF'
      AND
      parent_sched_event.event_db_id (+)= parent_sched_rel.event_db_id AND
      parent_sched_event.event_id    (+)= parent_sched_rel.event_id
      AND
      parent_sched_stask.sched_db_id (+)= parent_sched_event.event_db_id AND
      parent_sched_stask.sched_id (+)= parent_sched_event.event_id
      AND
      init_fault_rel.rel_event_db_id (+)= evt_event.event_db_id AND
      init_fault_rel.rel_event_id    (+)= evt_event.event_id AND
      init_fault_rel.rel_type_cd     (+)= 'RESLF'
      AND
      init_fault_event.event_db_id (+)= init_fault_rel.event_db_id AND
      init_fault_event.event_id    (+)= init_fault_rel.event_id
      AND
      ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
      ref_event_status.event_status_cd    = evt_event.event_status_cd
      AND
      ref_bitmap.bitmap_db_id = evt_event.bitmap_db_id AND
      ref_bitmap.bitmap_tag   = evt_event.bitmap_tag
      AND
      org_hr.hr_db_id (+)= evt_event.editor_hr_db_id AND
      org_hr.hr_id    (+)= evt_event.editor_hr_id
      AND
      utl_user.user_id(+)= org_hr.user_id;

--changeSet DEV-704:17 stripComments:false
-- Data Changes
-- Add config parm for security permissions around "Control External Fault" button.
INSERT INTO UTL_CONFIG_PARM 
(PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_ASSUME_CONTROL_OF_FAULT', 'SECURED_RESOURCE','Permission to allow a user to convert an externally managed fault to a Maintenix managed fault such that the external system will no longer be able to make updates to the fault.','USER', 'TRUE/FALSE', 'FALSE', 1,'Maint - Faults', '8.0',0
  FROM
    dual
  WHERE
    NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'ACTION_ASSUME_CONTROL_OF_FAULT' AND parm_type = 'SECURED_RESOURCE' );

--changeSet DEV-704:18 stripComments:false
-- Set action permissions based on database type 
insert into DB_TYPE_CONFIG_PARM (PARM_NAME, PARM_TYPE, DB_TYPE_CD)
select 'ACTION_ASSUME_CONTROL_OF_FAULT', 'SECURED_RESOURCE', 'OPER'
from dual
where not exists (select 1 from DB_TYPE_CONFIG_PARM where PARM_NAME = 'ACTION_ASSUME_CONTROL_OF_FAULT' and PARM_TYPE = 'SECURED_RESOURCE' and DB_TYPE_CD = 'OPER');