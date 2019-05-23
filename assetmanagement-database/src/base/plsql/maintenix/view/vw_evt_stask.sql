--liquibase formatted sql


--changeSet vw_evt_stask:1 stripComments:false
CREATE OR REPLACE VIEW vw_evt_stask(
      sched_db_id,
      sched_id,
      alt_id,
      sched_sdesc,
      bitmap_db_id,
      bitmap_tag,
      bitmap_name,
      sub_event_ord,
      editor_hr_db_id,
      editor_hr_id,
      editor_hr_sdesc,
      sched_status_db_id,
      sched_status_cd,
      sched_status_user_cd,
      sched_reason_db_id,
      sched_reason_cd,
      sched_reason_user_cd,
      sched_priority_db_id,
      sched_priority_cd,
      sched_ldesc,
      sched_start_dt,
      sched_start_gdt,
      sched_end_dt,
      sched_end_gdt,
      sched_routine_bool,
      lrp_bool,
      sched_resource_sum_bool,
      warranty_note,
      actual_start_dt,
      actual_start_gdt,
      actual_end_dt,
      actual_end_gdt,
      ro_ref_sdesc,
      task_db_id,
      task_id,
      task_class_db_id,
      task_class_cd,
      task_subclass_db_id,
      task_subclass_cd,
      user_subclass_cd,
      task_originator_db_id,
      task_originator_cd,
      task_ref_sdesc,
      task_priority_db_id,
      task_priority_cd,
      task_instructions,
      work_loc_db_id,
      work_loc_id,
      work_loc_cd,
      work_dept_db_id,
      work_dept_id,
      work_dept_cd,
      action_ldesc,
      ro_vendor_db_id,
      ro_vendor_id,
      wo_ref_sdesc,
      ro_warranty_note,
      issued_dt,
      issued_gdt,
      ext_key_sdesc,
      doc_ref_sdesc,
      seq_err_bool,
      hist_bool,
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
      sched_dead_data_type_db_id,
      sched_dead_data_type_id,
      sched_dead_data_type_cd,
      sched_dead_qt,
      sched_dead_dt,
      sched_dead_usage_rem_qt,
      sched_dead_notify_qt,
      sched_dead_deviation_qt,
      soft_deadline_bool,
      contact_info_sdesc,
      parent_fault_db_id,
      parent_fault_id,
      parent_fault_sdesc,
      parent_incident_db_id,
      parent_incident_id,
      parent_incident_sdesc,
      nh_sched_db_id,
      nh_sched_id,
      nh_sched_sdesc,
      h_sched_db_id,
      h_sched_id,
      h_sched_sdesc,
      previous_sched_db_id,
      previous_sched_id,
      previous_sched_sdesc,
      previous_actual_end_gdt,
      mod_orig_part_no_db_id,
      mod_orig_part_no_id,
      mod_orig_part_no_sdesc,
      mod_orig_part_no_oem,
      inst_inv_no_db_id,
      inst_inv_no_id,
      inst_inv_no_sdesc,
      inst_hole_nh_inv_no_db_id,
      inst_hole_nh_inv_no_id,
      inst_hole_nh_inv_no_sdesc,
      inst_hole_assmbl_db_id,
      inst_hole_assmbl_cd,
      inst_hole_assmbl_bom_id,
      inst_hole_assmbl_pos_id,
      inst_hole_assmbl_bom_cd,
      inst_hole_eqp_pos_cd,
      barcode_sdesc,
      dup_jic_sched_db_id,
      dup_jic_sched_id,
      sched_min_plan_yield_pct,
      sched_est_duration_qt,
      issue_account_db_id,
      issue_account_id,
      issue_account_cd,
      issue_account_sdesc,
      class_mode_cd,
      etops_bool,
      domain_type_cd,
      data_type_cd,
      precision_qt,
      ref_mult_qt,
      deferal_start_dt,
      eng_unit_cd,
      plan_by_dt,
      prevent_deadline_sync_bool
  ) AS
  SELECT /*+ rule */
        sched_stask.sched_db_id,
        sched_stask.sched_id,
        sched_stask.alt_id,
        evt_event.event_sdesc,
        evt_event.bitmap_db_id,
        evt_event.bitmap_tag,
        ref_bitmap.bitmap_name,
        evt_event.sub_event_ord,
        evt_event.editor_hr_db_id,
        evt_event.editor_hr_id,
        DECODE( evt_event.editor_hr_db_id, NULL, NULL, org_hr.hr_cd || ' (' || utl_user.last_name || ', ' || utl_user.first_name || ')' ),
        evt_event.event_status_db_id,
        evt_event.event_status_cd,
        ref_event_status.user_status_cd,
        evt_event.event_reason_db_id,
        evt_event.event_reason_cd,
        ref_event_reason.user_reason_cd,
        evt_event.sched_priority_db_id,
        evt_event.sched_priority_cd,
        evt_event.event_ldesc,
        evt_event.sched_start_dt,
        evt_event.sched_start_gdt,
        evt_event.sched_end_dt,
        evt_event.sched_end_gdt,
        sched_stask.routine_bool,
        sched_stask.lrp_bool,
        sched_stask.resource_sum_bool,
        sched_stask.warranty_note,
        evt_event.actual_start_dt,
        evt_event.actual_start_gdt,
        evt_event.event_dt,
        evt_event.event_gdt,
        sched_stask.ro_ref_sdesc,
        sched_stask.task_db_id,
        sched_stask.task_id,
        sched_stask.task_class_db_id,
        sched_stask.task_class_cd,
        sched_stask.task_subclass_db_id,
        sched_stask.task_subclass_cd,
        ref_task_subclass.user_subclass_cd,
        sched_stask.task_originator_db_id,
        sched_stask.task_originator_cd,
        sched_stask.task_ref_sdesc,
        sched_stask.task_priority_db_id,
        sched_stask.task_priority_cd,
        sched_stask.instruction_ldesc,
        evt_loc.loc_db_id,
        evt_loc.loc_id,
        inv_loc.loc_cd,
        evt_dept.dept_db_id,
        evt_dept.dept_id,
        org_work_dept.dept_cd,
        getTaskActions( sched_stask.sched_db_id, sched_stask.sched_id ) AS action_ldesc,
        sched_stask.ro_vendor_db_id,
        sched_stask.ro_vendor_id,
        sched_stask.wo_ref_sdesc,
        sched_stask.warranty_note,
        sched_stask.issued_dt,
        sched_stask.issued_gdt,
        evt_event.ext_key_sdesc,
        evt_event.doc_ref_sdesc,
        evt_event.seq_err_bool,
        evt_event.hist_bool,
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
        evt_sched_dead.data_type_db_id,
        evt_sched_dead.data_type_id,
        mim_data_type.data_type_cd,
        evt_sched_dead.sched_dead_qt,
        evt_sched_dead.sched_dead_dt,
        evt_sched_dead.usage_rem_qt,
        evt_sched_dead.notify_qt,
        evt_sched_dead.deviation_qt,
        sched_stask.soft_deadline_bool,
        evt_event.contact_info_sdesc,
        parent_fault_rel.event_db_id,
        parent_fault_rel.event_id,
        parent_fault_event.event_sdesc,
        parent_incident_rel.event_db_id,
        parent_incident_rel.event_id,
        parent_incident_event.event_sdesc,
        evt_event.nh_event_db_id,
        evt_event.nh_event_id,
        nh_sched_event.event_sdesc,
        evt_event.h_event_db_id,
        evt_event.h_event_id,
        h_sched_event.event_sdesc,
        previous_sched_rel.event_db_id,
        previous_sched_rel.event_id,
        previous_sched_event.event_sdesc,
        previous_sched_event.event_gdt,
        DECODE( sched_stask.task_class_cd, 'MOD', sched_stask.orig_part_no_db_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'MOD', sched_stask.orig_part_no_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'MOD', mod_eqp_part_no.part_no_sdesc, NULL ),
        DECODE( sched_stask.task_class_cd, 'MOD', mod_eqp_part_no.part_no_oem, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.inv_no_db_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.inv_no_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', inv_inv.inv_no_sdesc, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.nh_inv_no_db_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.nh_inv_no_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', nh_inv_inv.inv_no_sdesc, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.assmbl_db_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.assmbl_cd, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.assmbl_bom_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', evt_inv.assmbl_pos_id, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', eqp_assmbl_bom.assmbl_bom_cd, NULL ),
        DECODE( sched_stask.task_class_cd, 'INST', eqp_assmbl_pos.eqp_pos_cd, NULL ),
        sched_stask.barcode_sdesc,
        sched_stask.dup_jic_sched_db_id,
        sched_stask.dup_jic_sched_id,
        sched_stask.min_plan_yield_pct,
        sched_stask.est_duration_qt,
        sched_stask.issue_account_db_id,
        sched_stask.issue_account_id,
        fnc_account.account_cd,
        fnc_account.account_sdesc,
        ref_task_class.class_mode_cd,
        sched_stask.etops_bool,
        mim_data_type.domain_type_cd,
        mim_data_type.data_type_cd,
        mim_data_type.entry_prec_qt,
        ref_eng_unit.ref_mult_qt,
        evt_sched_dead.start_dt,
        ref_eng_unit.eng_unit_cd,
        sched_stask.plan_by_dt,
        sched_stask.prevent_deadline_sync_bool
   FROM sched_stask,
        evt_event,
        evt_inv,
        evt_sched_dead,
        evt_event_rel parent_incident_rel,
        evt_event_rel parent_fault_rel,
        evt_event_rel previous_sched_rel,
        ref_bitmap,
        ref_event_status,
        ref_event_reason,
        ref_task_priority,
        ref_task_subclass,
        eqp_assmbl_bom,
        eqp_assmbl_pos,
        eqp_bom_part,
        inv_inv,
        inv_inv nh_inv_inv,
        inv_inv h_inv_inv,
        inv_inv assmbl_inv_inv,
        eqp_part_no,
        eqp_part_no mod_eqp_part_no,
        evt_event previous_sched_event,
        evt_event parent_incident_event,
        evt_event parent_fault_event,
        evt_event nh_sched_event,
        evt_event h_sched_event,
        mim_data_type,
        (SELECT evt_dept.* FROM evt_dept, org_work_dept where evt_dept.dept_db_id = org_work_dept.dept_db_id AND evt_dept.dept_id = org_work_dept.dept_id AND org_work_dept.dept_type_cd <> 'CREW') evt_dept,
        org_work_dept,
        evt_loc,
        inv_loc,
        ref_task_class,
        org_hr,
        utl_user,
        fnc_account,
      ref_eng_unit
      WHERE evt_event.event_db_id = sched_stask.sched_db_id AND
        evt_event.event_id    = sched_stask.sched_id
        AND
        nh_sched_event.event_db_id (+)= evt_event.nh_event_db_id AND
        nh_sched_event.event_id    (+)= evt_event.nh_event_id
        AND
        h_sched_event.event_db_id = evt_event.h_event_db_id AND
        h_sched_event.event_id    = evt_event.h_event_id
        AND
        parent_incident_rel.rel_event_db_id (+)= sched_stask.sched_db_id AND
        parent_incident_rel.rel_event_id    (+)= sched_stask.sched_id AND
        parent_incident_rel.rel_type_cd     (+)= 'ITSK'
        AND
        parent_incident_event.event_db_id (+)= parent_incident_rel.event_db_id AND
        parent_incident_event.event_id    (+)= parent_incident_rel.event_id
        AND
        parent_fault_rel.rel_event_db_id (+)= sched_stask.sched_db_id AND
        parent_fault_rel.rel_event_id    (+)= sched_stask.sched_id AND
        parent_fault_rel.rel_type_cd     (+)= 'CORRECT'
        AND
        parent_fault_event.event_db_id (+)= parent_fault_rel.event_db_id AND
        parent_fault_event.event_id    (+)= parent_fault_rel.event_id
        AND
        previous_sched_rel.rel_event_db_id (+)= sched_stask.sched_db_id AND
        previous_sched_rel.rel_event_id    (+)= sched_stask.sched_id AND
        previous_sched_rel.rel_type_cd     (+)= 'DEPT'
        AND
        previous_sched_event.event_db_id (+)= previous_sched_rel.event_db_id AND
        previous_sched_event.event_id    (+)= previous_sched_rel.event_id
        AND
        ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
        ref_event_status.event_status_cd    = evt_event.event_status_cd
        AND
        ref_event_reason.event_reason_db_id (+)= evt_event.event_reason_db_id AND
        ref_event_reason.event_reason_cd    (+)= evt_event.event_reason_cd
        AND
        ref_bitmap.bitmap_db_id = evt_event.bitmap_db_id AND
        ref_bitmap.bitmap_tag   = evt_event.bitmap_tag
        AND
        evt_inv.event_db_id = sched_stask.sched_db_id AND
        evt_inv.event_id    = sched_stask.sched_id AND
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
        mod_eqp_part_no.part_no_db_id (+)= sched_stask.orig_part_no_db_id AND
        mod_eqp_part_no.part_no_id    (+)= sched_stask.orig_part_no_id
        AND
        evt_sched_dead.event_db_id (+)= sched_stask.sched_db_id AND
        evt_sched_dead.event_id    (+)= sched_stask.sched_id AND
        evt_sched_dead.sched_driver_bool (+)= 1
        AND
        mim_data_type.data_type_db_id (+)= evt_sched_dead.data_type_db_id AND
        mim_data_type.data_type_id    (+)= evt_sched_dead.data_type_id
        AND
        ref_eng_unit.eng_unit_db_id (+)= mim_data_type.eng_unit_db_id AND
        ref_eng_unit.eng_unit_cd    (+)= mim_data_type.eng_unit_cd
        AND
        org_work_dept.dept_db_id (+)= evt_dept.dept_db_id AND
        org_work_dept.dept_id    (+)= evt_dept.dept_id
        AND
        evt_dept.event_db_id (+)= sched_stask.sched_db_id AND
        evt_dept.event_id    (+)= sched_stask.sched_id
        AND
        evt_loc.event_db_id (+)= sched_stask.sched_db_id AND
        evt_loc.event_id    (+)= sched_stask.sched_id
        AND
        inv_loc.loc_db_id (+)= evt_loc.loc_db_id AND
        inv_loc.loc_id    (+)= evt_loc.loc_id
        AND
        ref_task_class.task_class_db_id = sched_stask.task_class_db_id AND
        ref_task_class.task_class_cd    = sched_stask.task_class_cd
        AND
        ref_task_priority.task_priority_db_id(+) = sched_stask.task_priority_db_id AND
        ref_task_priority.task_priority_cd(+)    = sched_stask.task_priority_cd
        AND
        ref_task_subclass.task_subclass_db_id  (+)= sched_stask.task_subclass_db_id AND
        ref_task_subclass.task_subclass_cd     (+)= sched_stask.task_subclass_cd
        AND
        org_hr.hr_db_id (+)= evt_event.editor_hr_db_id AND
        org_hr.hr_id    (+)= evt_event.editor_hr_id
        AND
        utl_user.user_id(+)= org_hr.user_id
        AND
        fnc_account.account_db_id (+)= sched_stask.issue_account_db_id AND
        fnc_account.account_id (+)= sched_stask.issue_account_id;