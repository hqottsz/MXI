--liquibase formatted sql


--changeSet acor_comp_wp_tools_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_comp_wp_tools_v1
AS
WITH rvw_event_chk_inout
AS
   (
     SELECT
       evt_event_rel.rel_event_db_id,
       evt_event_rel.rel_event_id,
       evt_inv.inv_no_db_id,
       evt_inv.inv_no_id,
       evt_event.event_status_cd,
       (utl_user.last_name || ', ' || utl_user.first_name) checked_name
     FROM
        evt_event
        INNER JOIN evt_event_rel ON
           evt_event.event_db_id = evt_event_rel.event_db_id AND
           evt_event.event_id    = evt_event_rel.event_id
        -- tool
        INNER JOIN evt_inv ON
           evt_event.event_db_id = evt_inv.event_db_id AND
           evt_event.event_id    = evt_inv.event_id    AND
           evt_inv.main_inv_bool = 1
        -- checked by
        INNER JOIN org_hr ON
           evt_event.editor_hr_db_id = org_hr.hr_db_id AND
           evt_event.editor_hr_id    = org_hr.hr_id
        INNER JOIN utl_user ON
           org_hr.user_id = utl_user.user_id
     WHERE
        evt_event.event_type_db_id = 0 AND
        evt_event.event_type_cd    = 'TCO'
   )
SELECT
   wp_workscope.wo_sched_id,
   wp_workscope.sched_id,
   eqp_bom_part.bom_part_cd      AS part_group,
   eqp_part_no.part_no_oem       AS part_number,
   eqp_part_no.part_no_sdesc     AS part_name,
   inv_inv.serial_no_oem         AS serial_number,
   chkout.checked_name           AS checked_out_by,
   chkin.checked_name            AS checked_in_by,
   evt_tool.sched_hr             AS schedule_hours,
   evt_tool.actual_hr            AS actual_hours
FROM
   acor_comp_wp_workscope_v1 wp_workscope
   INNER JOIN sched_stask ON
      wp_workscope.sched_id = sched_stask.alt_id
   INNER JOIN sched_labour ON
      sched_stask.sched_db_id = sched_labour.sched_db_id AND
      sched_stask.sched_id    = sched_labour.sched_id
   -- tool
   INNER JOIN sched_labour_tool ON
      sched_labour_tool.labour_db_id = sched_labour.labour_db_id AND
      sched_labour_tool.labour_id    = sched_labour.labour_id
   INNER JOIN evt_tool ON
      sched_labour_tool.event_db_id = evt_tool.event_db_id AND
      sched_labour_tool.event_id    = evt_tool.event_id    AND
      sched_labour_tool.tool_id     = evt_tool.tool_id
   -- part group
   INNER JOIN eqp_bom_part ON
      evt_tool.bom_part_db_id = eqp_bom_part.bom_part_db_id AND
      evt_tool.bom_part_id    = eqp_bom_part.bom_part_id
   -- part number
   INNER JOIN eqp_part_no ON
      evt_tool.part_no_db_id = eqp_part_no.part_no_db_id AND
      evt_tool.part_no_id    = eqp_part_no.part_no_id
   LEFT JOIN inv_inv ON
      evt_tool.inv_no_db_id = inv_inv.inv_no_db_id AND
      evt_tool.inv_no_id    = inv_inv.inv_no_id
   -- checked out by
   LEFT JOIN rvw_event_chk_inout chkout ON
      evt_tool.event_db_id = chkout.rel_event_db_id AND
      evt_tool.event_id    = chkout.rel_event_id
      AND
      evt_tool.Inv_No_Db_Id = chkout.inv_no_db_id AND
      evt_tool.inv_no_db_id = chkout.inv_no_db_id
      AND -- checked out status code
      chkout.event_status_cd = 'TCOOUT'
   -- checked in by
   LEFT JOIN rvw_event_chk_inout chkin ON
      evt_tool.event_db_id = chkin.rel_event_db_id AND
      evt_tool.event_id    = chkin.rel_event_id
      AND
      evt_tool.Inv_No_Db_Id = chkin.inv_no_db_id AND
      evt_tool.inv_no_db_id = chkin.inv_no_db_id
      AND -- checked-in status code
      chkin.event_status_cd = 'TCOIN'
;