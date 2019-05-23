--liquibase formatted sql


--changeSet vw_evt_usgrec:1 stripComments:false
CREATE OR REPLACE VIEW vw_evt_usgrec(
      usgrec_db_id,
      usgrec_id,
      usgrec_sdesc,
      bitmap_db_id,
      bitmap_tag,
      bitmap_name,
      sub_event_ord,
      editor_hr_db_id,
      editor_hr_id,
      editor_hr_sdesc,
      collection_dt,
      collection_gdt,
      data_source_db_id,
      data_source_cd,
      usgrec_ldesc,
      ext_key_sdesc,
      doc_ref_sdesc,
      seq_err_bool
   ) AS
   SELECT /*+ rule */
      evt_event.event_db_id,
      evt_event.event_id,
      evt_event.event_sdesc,
      evt_event.bitmap_db_id,
      evt_event.bitmap_tag,
      ref_bitmap.bitmap_name,
      evt_event.sub_event_ord,
      evt_event.editor_hr_db_id,
      evt_event.editor_hr_id,
      DECODE( evt_event.editor_hr_db_id, NULL, NULL, org_hr.hr_cd || ' (' || utl_user.last_name || ', ' || utl_user.first_name || ')' ),
      evt_event.event_dt,
      evt_event.event_gdt,
      evt_event.data_source_db_id,
      evt_event.data_source_cd,
      evt_event.event_ldesc,
      evt_event.ext_key_sdesc,
      evt_event.doc_ref_sdesc,
      evt_event.seq_err_bool
 FROM evt_event,
      ref_bitmap,
      org_hr,
      utl_user
WHERE evt_event.event_type_db_id = 0 AND
      evt_event.event_type_cd    = 'UR'
      AND
      ref_bitmap.bitmap_db_id = evt_event.bitmap_db_id AND
      ref_bitmap.bitmap_tag   = evt_event.bitmap_tag
      AND
      org_hr.hr_db_id (+)= evt_event.editor_hr_db_id AND
      org_hr.hr_id    (+)= evt_event.editor_hr_id
      AND
      utl_user.user_id(+)= org_hr.user_id;