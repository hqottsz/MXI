--liquibase formatted sql

--changeSet OPER-25580:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      upg_migr_schema_v1_pkg.procedure_drop('convertAcEvents');
END;
/

--changeSet OPER-25580:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	DELETE FROM utl_work_item WHERE type = 'AC_EVENT_CONVERSION';
	DELETE FROM utl_work_item_type WHERE name = 'AC_EVENT_CONVERSION';
END;
/

--changeSet OPER-25580:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      utl_migr_data_pkg.config_parm_delete('IS_AC_EVT_CONVERSION_COMPLETE');
END;
/

--changeSet OPER-28313:4 stripComments:false
ALTER TRIGGER tiudbr_inv_cnd_chg_event DISABLE;

--changeSet OPER-28313:5 stripComments:false
ALTER TRIGGER tiudbr_inv_cnd_chg_inv DISABLE;

--changeSet OPER-28313:6 stripComments:false
ALTER TRIGGER tiudbr_inv_cnd_chg_inv_usage DISABLE;

--changeSet OPER-25580:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      utl_parallel_pkg.parallel_insert_begin('inv_cnd_chg_event');
	  
	  INSERT /*+ APPEND */ INTO inv_cnd_chg_event
	   SELECT
		  evt_event.event_db_id,
		  evt_event.event_id,
		  evt_event.stage_reason_db_id,
		  evt_event.stage_reason_cd,
		  evt_event.editor_hr_db_id,
		  evt_event.editor_hr_id,
		  evt_event.event_status_db_id,
		  evt_event.event_status_cd,
		  evt_event.event_reason_db_id,
		  evt_event.event_reason_cd,
		  evt_event.data_source_db_id,
		  evt_event.data_source_cd,
		  evt_event.h_event_db_id,
		  evt_event.h_event_id,
		  evt_event.event_sdesc,
		  evt_event.ext_key_sdesc,
		  evt_event.seq_err_bool,
		  evt_event.event_ldesc,
		  evt_event.event_dt,
		  evt_event.sched_start_dt,
		  evt_event.sub_event_ord,
		  evt_event.alt_id,
		  evt_event.rstat_cd,
		  1 AS revision_no,
		  evt_event.ctrl_db_id,
		  evt_event.creation_dt,
		  evt_event.revision_dt,
		  evt_event.revision_db_id,
		  evt_event.revision_user
	   FROM
		  evt_event
	   LEFT OUTER JOIN inv_cnd_chg_event
		  ON evt_event.event_db_id = inv_cnd_chg_event.event_db_id AND evt_event.event_id = inv_cnd_chg_event.event_id
	   WHERE
	      event_type_cd = 'AC' AND inv_cnd_chg_event.event_id is NULL;
	  
	  utl_parallel_pkg.parallel_insert_end('inv_cnd_chg_event',true);
END;
/

--changeSet OPER-25580:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      utl_parallel_pkg.parallel_insert_begin('inv_cnd_chg_inv');
	  
	   INSERT /*+ APPEND */ INTO inv_cnd_chg_inv
	   SELECT
		  evt_inv.event_db_id,
		  evt_inv.event_id,
		  evt_inv.event_inv_id,
		  evt_inv.inv_no_db_id,
		  evt_inv.inv_no_id,
		  evt_inv.nh_inv_no_db_id,
		  evt_inv.nh_inv_no_id,
		  evt_inv.assmbl_inv_no_db_id,
		  evt_inv.assmbl_inv_no_id,
		  evt_inv.h_inv_no_db_id,
		  evt_inv.h_inv_no_id,
		  evt_inv.assmbl_db_id,
		  evt_inv.assmbl_cd,
		  evt_inv.assmbl_bom_id,
		  evt_inv.assmbl_pos_id,
		  evt_inv.part_no_db_id,
		  evt_inv.part_no_id,
		  evt_inv.bom_part_db_id,
		  evt_inv.bom_part_id,
		  evt_inv.main_inv_bool,
		  evt_inv.rstat_cd,
		  1 AS revision_no,
		  evt_inv.revision_db_id AS ctrl_db_id,
		  evt_inv.creation_dt,
		  evt_inv.revision_dt,
		  evt_inv.revision_db_id,
		  evt_inv.revision_user
	   FROM evt_inv
	   JOIN evt_event
		  ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
	   LEFT OUTER JOIN inv_cnd_chg_inv
		  ON evt_inv.event_db_id = inv_cnd_chg_inv.event_db_id AND evt_inv.event_id = inv_cnd_chg_inv.event_id AND evt_inv.event_inv_id = inv_cnd_chg_inv.event_inv_id
	   WHERE
	      event_type_cd = 'AC' AND inv_cnd_chg_inv.event_id is NULL;
	  
	  utl_parallel_pkg.parallel_insert_end('inv_cnd_chg_inv',true);
END;
/

--changeSet OPER-25580:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
      utl_parallel_pkg.parallel_insert_begin('inv_cnd_chg_inv_usage');
	  
	   INSERT /*+ APPEND */ INTO inv_cnd_chg_inv_usage
	   SELECT
		  evt_inv_usage.event_db_id,
		  evt_inv_usage.event_id,
		  evt_inv_usage.event_inv_id,
		  evt_inv_usage.data_type_db_id,
		  evt_inv_usage.data_type_id,
		  evt_inv_usage.tsn_qt,
		  evt_inv_usage.tso_qt,
		  evt_inv_usage.tsi_qt,
		  evt_inv_usage.assmbl_tsn_qt,
		  evt_inv_usage.assmbl_tso_qt,
		  evt_inv_usage.h_tsn_qt,
		  evt_inv_usage.h_tso_qt,
		  evt_inv_usage.nh_tsn_qt,
		  evt_inv_usage.nh_tso_qt,
		  evt_inv_usage.negated_bool,
		  evt_inv_usage.rstat_cd,
		  1 AS revision_no,
		  evt_inv_usage.revision_db_id AS ctrl_db_id,
		  evt_inv_usage.creation_dt,
		  evt_inv_usage.revision_dt,
		  evt_inv_usage.revision_db_id,
		  evt_inv_usage.revision_user
	   FROM evt_inv_usage
	   JOIN evt_event
		  ON evt_event.event_db_id = evt_inv_usage.event_db_id AND evt_event.event_id = evt_inv_usage.event_id
	   LEFT OUTER JOIN inv_cnd_chg_inv_usage
		  ON evt_inv_usage.event_db_id = inv_cnd_chg_inv_usage.event_db_id AND evt_inv_usage.event_id = inv_cnd_chg_inv_usage.event_id AND evt_inv_usage.event_inv_id = inv_cnd_chg_inv_usage.event_inv_id
			  AND evt_inv_usage.data_type_db_id = inv_cnd_chg_inv_usage.data_type_db_id AND evt_inv_usage.data_type_id = inv_cnd_chg_inv_usage.data_type_id
	   WHERE
	      event_type_cd = 'AC' AND inv_cnd_chg_inv_usage.event_id is NULL;
	  
	  utl_parallel_pkg.parallel_insert_end('inv_cnd_chg_inv_usage',true);
END;
/

--changeSet OPER-25580:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_parallel_pkg.parallel_update_begin('quar_quar');

  UPDATE quar_quar
     SET ac_event_db_id = quar_quar.event_db_id,
         ac_event_id    = quar_quar.event_id
   WHERE EXISTS (SELECT evt_event.event_id, evt_event.event_db_id
            FROM evt_event
           WHERE evt_event.event_db_id = quar_quar.event_db_id
             AND evt_event.event_id = quar_quar.event_id
             AND event_type_cd = 'AC');

  utl_parallel_pkg.parallel_update_end('quar_quar', true);
END;
/

--changeSet OPER-25580:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_parallel_pkg.parallel_update_begin('eqp_part_rotable_adjust');

  UPDATE eqp_part_rotable_adjust
     SET ac_event_db_id = eqp_part_rotable_adjust.event_db_id,
         ac_event_id    = eqp_part_rotable_adjust.event_id
   WHERE EXISTS (SELECT evt_event.event_id, evt_event.event_db_id
            FROM evt_event
           WHERE evt_event.event_db_id = eqp_part_rotable_adjust.event_db_id
             AND evt_event.event_id = eqp_part_rotable_adjust.event_id
             AND event_type_cd = 'AC');

  utl_parallel_pkg.parallel_update_end('eqp_part_rotable_adjust', true);
END;
/

--changeSet OPER-25580:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_parallel_pkg.parallel_update_begin('fnc_xaction_log');

  UPDATE fnc_xaction_log
     SET ac_event_db_id = fnc_xaction_log.event_db_id,
         ac_event_id    = fnc_xaction_log.event_id
   WHERE EXISTS (SELECT evt_event.event_id, evt_event.event_db_id
            FROM evt_event
           WHERE evt_event.event_db_id = fnc_xaction_log.event_db_id
             AND evt_event.event_id = fnc_xaction_log.event_id
             AND event_type_cd = 'AC');

  utl_parallel_pkg.parallel_update_end('fnc_xaction_log', true);
END;
/

--changeSet OPER-28313:16 stripComments:false
ALTER TRIGGER tiudbr_inv_cnd_chg_event ENABLE;

--changeSet OPER-28313:17 stripComments:false
ALTER TRIGGER tiudbr_inv_cnd_chg_inv ENABLE;

--changeSet OPER-28313:18 stripComments:false
ALTER TRIGGER tiudbr_inv_cnd_chg_inv_usage ENABLE;

