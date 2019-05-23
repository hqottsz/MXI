--liquibase formatted sql


--changeSet vw_evt_request:1 stripComments:false
CREATE OR REPLACE VIEW vw_evt_request(
      req_sdesc,
      bitmap_db_id,
      bitmap_tag,
      bitmap_name,
      editor_hr_db_id,
      editor_hr_id,
      editor_hr_sdesc,
      req_status_db_id,
      req_status_cd,
      req_status_user_cd,
      req_reason_db_id,
      req_reason_cd,
      req_reason_user_cd,
      req_closed_dt,
      req_closed_gdt,
      req_ldesc,
      ext_key_sdesc,
      doc_ref_sdesc,
      sub_event_ord,
      request_by_dt,
      request_by_gdt,
      request_dept_db_id,
      request_dept_id,
      issued_dt,
      issued_gdt,
      issued_dept_db_id,
      issued_dept_id,
      parent_sched_db_id,
      parent_sched_id,
      parent_sched_sdesc
   ) AS
   SELECT /*+ rule */
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
      evt_event.event_reason_db_id,
      evt_event.event_reason_cd,
      ref_event_reason.user_reason_cd,
      evt_event.event_dt,
      evt_event.event_gdt,
      evt_event.event_ldesc,
      evt_event.ext_key_sdesc,
      evt_event.doc_ref_sdesc,
      evt_event.sub_event_ord,
      evt_event.sched_end_dt,
      evt_event.sched_end_gdt,
      request_dept.dept_db_id,
      request_dept.dept_id,
      evt_event.actual_start_dt,
      evt_event.actual_start_gdt,
      issued_dept.dept_db_id,
      issued_dept.dept_id,
      parent_sched_rel.event_db_id,
      parent_sched_rel.event_id,
      parent_sched_event.event_sdesc
 FROM evt_event,
      ref_event_status,
      ref_event_reason,
      ref_bitmap,
      org_hr,
      evt_event_rel parent_sched_rel,
      evt_event parent_sched_event,
      evt_dept request_dept,
      evt_dept issued_dept,
      utl_user
WHERE parent_sched_rel.rel_event_db_id (+)= evt_event.event_db_id AND
      parent_sched_rel.rel_event_id    (+)= evt_event.event_id AND
      parent_sched_rel.rel_type_cd     (+)= 'RREQ'
      AND
      parent_sched_event.event_db_id (+)= parent_sched_rel.event_db_id AND
      parent_sched_event.event_id    (+)= parent_sched_rel.event_id
      AND
      ref_event_status.event_status_db_id = evt_event.event_status_db_id AND
      ref_event_status.event_status_cd    = evt_event.event_status_cd
      AND
      ref_event_reason.event_reason_db_id(+) = evt_event.event_reason_db_id AND
      ref_event_reason.event_reason_cd(+)    = evt_event.event_reason_cd
      AND
      ref_bitmap.bitmap_db_id = evt_event.bitmap_db_id AND
      ref_bitmap.bitmap_tag   = evt_event.bitmap_tag
      AND
      org_hr.hr_db_id (+)= evt_event.editor_hr_db_id AND
      org_hr.hr_id    (+)= evt_event.editor_hr_id
      AND
      request_dept.event_db_id   (+)= evt_event.event_db_id AND
      request_dept.event_id      (+)= evt_event.event_id AND
      request_dept.event_dept_id (+)= 1
      AND
      issued_dept.event_db_id   (+)= evt_event.event_db_id AND
      issued_dept.event_id      (+)= evt_event.event_id AND
      issued_dept.event_dept_id (+)= 2
      AND
      utl_user.user_id(+)= org_hr.user_id
;