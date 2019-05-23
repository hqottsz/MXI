--liquibase formatted sql


--changeSet OPER-23977:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure convertAcEvents(aFilterEventDbId IN NUMBER, aFilterBeginEventId IN NUMBER, aFilterEndEventId IN NUMBER, aReturn OUT NUMBER) IS
   CURSOR lcur_EvtEvent(cn_FilterEventDbId NUMBER, cn_FilterBeginEventId NUMBER, cn_FilterEndEventId NUMBER ) IS
   SELECT MXKEY(event_db_id, event_id)
   FROM
      evt_event
   WHERE
      event_type_cd = 'AC'               AND
      event_db_id   = cn_FilterEventDbId AND
      event_id BETWEEN cn_FilterBeginEventId AND cn_FilterEndEventId;
   ltab_EventKeys MXKEYTABLE;
   lSuccess                     CONSTANT NUMBER  := 1;
   lNoProc                      CONSTANT NUMBER  := 0;
   lCurrentAvoidTriggerValue    CONSTANT BOOLEAN := application_object_pkg.gv_avoid_trigger;
BEGIN
   -- Initialize the return
   aReturn := lNoProc;

    -- disable update trigger so that revision_user of evt_event table doesn't get updated during migration.
   application_object_pkg.gv_avoid_trigger := true;

      --retrieve the event keys in the specified range of the AC events in the table evt_event and store them to local variable
   OPEN lcur_EvtEvent(aFilterEventDbId, aFilterBeginEventId, aFilterEndEventId);
   FETCH lcur_EvtEvent BULK COLLECT INTO ltab_EventKeys;
   CLOSE lcur_EvtEvent;


   INSERT /*+ APPEND */ INTO inv_cnd_chg_event
   SELECT
      event_db_id,
      event_id,
      stage_reason_db_id,
      stage_reason_cd,
      editor_hr_db_id,
      editor_hr_id,
      event_status_db_id,
      event_status_cd,
      event_reason_db_id,
      event_reason_cd,
      data_source_db_id,
      data_source_cd,
      h_event_db_id,
      h_event_id,
      event_sdesc,
      ext_key_sdesc,
      seq_err_bool,
      event_ldesc,
      event_dt,
      sched_start_dt,
      sub_event_ord,
      alt_id,
      rstat_cd,
      1 AS revision_no,
      ctrl_db_id,
      creation_dt,
      revision_dt,
      revision_db_id,
      revision_user
   FROM
      evt_event
   WHERE (event_db_id, event_id)
         IN (SELECT * FROM table(ltab_EventKeys));

   INSERT /*+ APPEND */ INTO inv_cnd_chg_inv
   SELECT
      event_db_id,
      event_id,
      event_inv_id,
      inv_no_db_id,
      inv_no_id,
      nh_inv_no_db_id,
      nh_inv_no_id,
      assmbl_inv_no_db_id,
      assmbl_inv_no_id,
      h_inv_no_db_id,
      h_inv_no_id,
      assmbl_db_id,
      assmbl_cd,
      assmbl_bom_id,
      assmbl_pos_id,
      part_no_db_id,
      part_no_id,
      bom_part_db_id,
      bom_part_id,
      main_inv_bool,
      evt_inv.rstat_cd,
      1 AS revision_no,
      revision_db_id AS ctrl_db_id,
      creation_dt,
      revision_dt,
      revision_db_id,
      revision_user
   FROM evt_inv
   WHERE (event_db_id, event_id)
         IN (SELECT * FROM table(ltab_EventKeys));

   INSERT /*+ APPEND */ INTO inv_cnd_chg_inv_usage
   SELECT
      event_db_id,
      event_id,
      event_inv_id,
      data_type_db_id,
      data_type_id,
      tsn_qt,
      tso_qt,
      tsi_qt,
      assmbl_tsn_qt,
      assmbl_tso_qt,
      h_tsn_qt,
      h_tso_qt,
      nh_tsn_qt,
      nh_tso_qt,
      negated_bool,
      evt_inv_usage.rstat_cd,
      1 AS revision_no,
      revision_db_id AS ctrl_db_id,
      creation_dt,
      revision_dt,
      revision_db_id,
      revision_user
   FROM evt_inv_usage
   WHERE (event_db_id, event_id)
         IN (SELECT * FROM table(ltab_EventKeys));

   -- update quar_quar table to populate the ac_event_db and ac_event_id for events that are replicated
   -- from evt_events to inv_cnd_chg_event
   UPDATE quar_quar
   SET ac_event_db_id = event_db_id,
       ac_event_id    = event_id
   WHERE (event_db_id, event_id)
         IN (SELECT * FROM table(ltab_EventKeys));

   -- update eqp_part_rotable_adjust table to populate the ac_event_db and ac_event_id for events that are replicated
   -- from evt_events to inv_cnd_chg_event
   UPDATE eqp_part_rotable_adjust
   SET ac_event_db_id = event_db_id,
       ac_event_id    = event_id
   WHERE (event_db_id, event_id)
         IN (SELECT * FROM table(ltab_EventKeys));

   -- update eqp_part_rotable_adjust table to populate the ac_event_db and ac_event_id for events that are replicated
   -- from evt_events to inv_cnd_chg_event
   UPDATE fnc_xaction_log
   SET ac_event_db_id = event_db_id,
       ac_event_id    = event_id
   WHERE (event_db_id, event_id)
         IN (SELECT * FROM table(ltab_EventKeys));

   --return success
   aReturn := lSuccess;
   -- restore gv_avoid_trigger value prior to this stored procedure.
   application_object_pkg.gv_avoid_trigger := lCurrentAvoidTriggerValue;
   EXCEPTION
      WHEN OTHERS THEN
         application_object_pkg.gv_avoid_trigger := lCurrentAvoidTriggerValue;
         aReturn := SQLCODE;
         APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','@@@convertAcEvents@@@'||SQLERRM);
         RETURN;
END convertAcEvents;
/