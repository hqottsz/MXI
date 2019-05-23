--liquibase formatted sql


--changeSet OPER-194:1 stripComments:false
-- Adding ALT_ID UUID to SCHED_LABOUR
/**********************************************************
* Add ALT_ID to SCHED_LABOUR for WorkdCapture report
************************************************************/
ALTER TRIGGER TUBR_SCHED_LABOUR DISABLE;

--changeSet OPER-194:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_LABOUR add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet OPER-194:3 stripComments:false
UPDATE 
   SCHED_LABOUR
SET 
   alt_id = mx_key_pkg.new_uuid()
WHERE 
   alt_id IS NULL
;

--changeSet OPER-194:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table SCHED_LABOUR modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet OPER-194:5 stripComments:false
ALTER TRIGGER TUBR_SCHED_LABOUR ENABLE;

--changeSet OPER-194:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_LABOUR_ALT_ID" BEFORE INSERT
   ON "SCHED_LABOUR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet OPER-194:7 stripComments:false
CREATE OR REPLACE FORCE VIEW VW_EVT_FAULT(
fault_db_id,
fault_id,
alt_id,
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
      sd_fault.alt_id,
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

--changeSet OPER-194:8 stripComments:false
-- These are used by report
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID)
SELECT
   'aInventoryKey', 
   1, 
   null, 
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aInventoryKey'
   )
;

--changeSet OPER-194:9 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aTaskKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aTaskKey'
   )
;

--changeSet OPER-194:10 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aHumanResourceKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aHumanResourceKey'
   )
;

--changeSet OPER-194:11 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aMaintProgramKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aMaintProgramKey'
   )
;

--changeSet OPER-194:12 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aPurchaseOrderKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aPurchaseOrderKey'
   )
;

--changeSet OPER-194:13 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aRFQKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aRFQKey'
   )
;

--changeSet OPER-194:14 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aShipmentKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aShipmentKey'
   )
;

--changeSet OPER-194:15 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aTagKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aTagKey'
   )
;

--changeSet OPER-194:16 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aTaskDefinitionKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aTaskDefinitionKey'
   )
;

--changeSet OPER-194:17 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aSchedTaskKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aSchedTaskKey'
   )
;

--changeSet OPER-194:18 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aWorkPackageKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aWorkPackageKey'
   )
;

--changeSet OPER-194:19 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aPartRequestKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aPartRequestKey'
   )
;

--changeSet OPER-194:20 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aTransferKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aTransferKey'
   )
;

--changeSet OPER-194:21 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aVendorKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aVendorKey'
   )
;

--changeSet OPER-194:22 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aLocationKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aLocationKey'
   )
;

--changeSet OPER-194:23 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aBomPartKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aBomPartKey'
   )
;

--changeSet OPER-194:24 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aRoleKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aRoleKey'
   )
;

--changeSet OPER-194:25 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aStockNumberKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aStockNumberKey'
   )
;

--changeSet OPER-194:26 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aPOInvoiceKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aPOInvoiceKey'
   )
;

--changeSet OPER-194:27 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aOwnerKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aOwnerKey'
   )
;

--changeSet OPER-194:28 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aManufacturerKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aManufacturerKey'
   )
;

--changeSet OPER-194:29 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aPartNumberKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aPartNumberKey'
   )
;

--changeSet OPER-194:30 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aAssemblyKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aAssemblyKey'
   )
;

--changeSet OPER-194:31 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aAuthorityKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aAuthorityKey'
   )
;

--changeSet OPER-194:32 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aDepartmentKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aDepartmentKey'
   )
;

--changeSet OPER-194:33 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aFaultKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aFaultKey'
   )
;

--changeSet OPER-194:34 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aAccountKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aAccountKey'
   )
;

--changeSet OPER-194:35 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aTCodeKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aTCodeKey'
   )
;

--changeSet OPER-194:36 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aEventKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aEventKey'
   )
;

--changeSet OPER-194:37 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aFlightKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aFlightKey'
   )
;

--changeSet OPER-194:38 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aBomItemKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aBomItemKey'
   )
;

--changeSet OPER-194:39 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aUsageRecordKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aUsageRecordKey'
   )
;

--changeSet OPER-194:40 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aUserKey', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aUserKey'
   )
;

--changeSet OPER-194:41 stripComments:false
INSERT INTO UTL_MENU_ARG (ARG_CD, ENCRYPT_BOOL, ARG_DESC,UTL_ID) 
SELECT
   'aSchedLabourKeys', 
   1, 
   null,
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_arg WHERE arg_cd = 'aSchedLabourKeys'
   )
;

--changeSet OPER-194:42 stripComments:false
INSERT INTO UTL_MENU_ITEM_ARG (MENU_ID, ARG_CD, UTL_ID)
SELECT
   120926,
   'aTagKey',
   0
FROM
   dual
WHERE
   NOT EXISTS(
      SELECT 1 FROM utl_menu_item_arg WHERE menu_id = 120926 AND arg_cd = 'aTagKey'
   )
;