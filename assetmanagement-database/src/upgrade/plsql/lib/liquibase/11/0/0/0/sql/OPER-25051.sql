--liquibase formatted sql

--changeSet OPER-25051:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER tiudbr_inv_cnd_chg_event
/********************************************************************************
*
* Trigger:    TIUDBR_INV_CND_CHG_EVENT
*
* Description:  This is a ROW based trigger. Before any IUD DML on a row of the table inv_chg_event,
*				        the corresponding same action must be executed on the row of the table evt_event first for purpose.
*
********************************************************************************/
BEFORE INSERT OR UPDATE OR DELETE ON inv_cnd_chg_event
FOR EACH ROW

BEGIN

   IF (inserting OR updating) THEN
      MERGE INTO evt_event t
      USING dual
      ON (t.event_db_id = :new.event_db_id AND t.event_id = :new.event_id)
      WHEN MATCHED THEN
         UPDATE SET
            stage_reason_db_id = :new.stage_reason_db_id,
            stage_reason_cd    = :new.stage_reason_cd,
            editor_hr_db_id    = :new.editor_hr_db_id,
            editor_hr_id       = :new.editor_hr_id,
            event_status_db_id = :new.event_status_db_id,
            event_status_cd    = :new.event_status_cd,
            event_reason_db_id = :new.event_reason_db_id,
            event_reason_cd    = :new.event_reason_cd,
            data_source_db_id  = :new.data_source_db_id,
            data_source_cd     = :new.data_source_cd,
            h_event_db_id      = :new.h_event_db_id,
            h_event_id         = :new.h_event_id,
            event_sdesc        = :new.event_sdesc,
            ext_key_sdesc      = :new.ext_key_sdesc,
            seq_err_bool       = :new.seq_err_bool,
            event_ldesc        = :new.event_ldesc,
            event_dt           = :new.event_dt,
            event_gdt          = :new.event_dt,
            sched_start_dt     = :new.sched_start_dt,
            sub_event_ord      = :new.sub_event_ord
      WHEN NOT MATCHED THEN
         INSERT
         (event_db_id,
          event_id,
          event_type_db_id,
          event_type_cd,
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
          hist_bool,
          seq_err_bool,
          event_ldesc,
          event_dt,
          event_gdt,
          sched_start_dt,
          sub_event_ord,
          alt_id,
          rstat_cd,
          ctrl_db_id,
          creation_dt,
          revision_dt,
          revision_db_id,
          revision_user
          )
         VALUES
         (:new.event_db_id,
          :new.event_id,
          0,
          'AC',
          :new.stage_reason_db_id,
          :new.stage_reason_cd,
          :new.editor_hr_db_id,
          :new.editor_hr_id,
          :new.event_status_db_id,
          :new.event_status_cd,
          :new.event_reason_db_id,
          :new.event_reason_cd,
          :new.data_source_db_id,
          :new.data_source_cd,
          :new.h_event_db_id,
          :new.h_event_id,
          :new.event_sdesc,
          :new.ext_key_sdesc,
          1,
          :new.seq_err_bool,
          :new.event_ldesc,
          :new.event_dt,
          :new.event_dt,
          :new.sched_start_dt,
          :new.sub_event_ord,
          :new.alt_id,
          :new.rstat_cd,
          :new.ctrl_db_id,
          :new.creation_dt,
          :new.revision_dt,
          :new.revision_db_id,
          :new.revision_user);
      RETURN;
   END IF;

   IF (deleting) THEN
      DELETE FROM evt_event
      WHERE event_db_id = :old.event_db_id AND
            event_id    = :old.event_id;
      RETURN;
   END IF;
END;
/

--changeSet OPER-25051:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER tiudbr_inv_cnd_chg_stage
/********************************************************************************
*
* Trigger:    TIUDBR_INV_CHG_STAGE
*
* Description:  This is a ROW based trigger. Before any IUD DML on a row of the table inv_cnd_chg_stage,
*				        the corresponding same action must be executed on the row of the table evt_stage first for purpose.
*
********************************************************************************/
BEFORE INSERT OR UPDATE OR DELETE ON inv_cnd_chg_stage
FOR EACH ROW

BEGIN

   IF (inserting OR updating) THEN
      MERGE INTO evt_stage t
      USING dual
      ON (t.event_db_id = :new.event_db_id AND
          t.event_id    = :new.event_id    AND
          t.stage_id    = :new.stage_id)
      WHEN MATCHED THEN
         UPDATE SET
            event_status_db_id = :new.event_status_db_id,
            event_status_cd    = :new.event_status_cd,
            stage_reason_db_id = :new.stage_reason_db_id,
            stage_reason_cd    = :new.stage_reason_cd,
            stage_event_db_id  = :new.stage_event_db_id,
            stage_event_id     = :new.stage_event_id,
            hr_db_id           = :new.hr_db_id,
            hr_id              = :new.hr_id,
            stage_dt           = :new.stage_dt,
            stage_note         = :new.stage_note,
            system_bool        = :new.system_bool
      WHEN NOT MATCHED THEN
         INSERT
         (event_db_id,
          event_id,
          stage_id,
          event_status_db_id,
          event_status_cd,
          stage_reason_db_id,
          stage_reason_cd,
          stage_event_db_id,
          stage_event_id,
          hr_db_id,
          hr_id,
          stage_dt,
          stage_gdt,
          stage_note,
          system_bool,
          alt_id,
          rstat_cd,
          creation_dt,
          revision_dt,
          revision_db_id,
          revision_user)
      VALUES
        (:new.event_db_id,
         :new.event_id,
         :new.stage_id,
         :new.event_status_db_id,
         :new.event_status_cd,
         :new.stage_reason_db_id,
         :new.stage_reason_cd,
         :new.stage_event_db_id,
         :new.stage_event_id,
         :new.hr_db_id,
         :new.hr_id,
         :new.stage_dt,
         :new.stage_dt,
         :new.stage_note,
         :new.system_bool,
         :new.alt_id,
         :new.rstat_cd,
         :new.creation_dt,
         :new.revision_dt,
         :new.revision_db_id,
         :new.revision_user);
      RETURN;
   END IF;

   IF (deleting) THEN
      DELETE FROM evt_stage
      WHERE event_db_id = :old.event_db_id AND
            event_id    = :old.event_id    AND
            stage_id    = :old.stage_id;
      RETURN;
   END IF;
END;
/
--changeSet OPER-25051:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER tiudbr_inv_cnd_chg_inv
/********************************************************************************
*
* Trigger:    TIUDBR_INV_CND_CHG_INV
*
* Description:  This is a ROW based trigger. Before any IUD DML on a row of the table inv_cnd_chg_inv,
*				        the corresponding same action must be executed on the row of the table evt_inv first for purpose.
*
********************************************************************************/
BEFORE INSERT OR UPDATE OR DELETE ON inv_cnd_chg_inv
FOR EACH ROW

BEGIN

   IF (inserting OR updating) THEN
      MERGE INTO evt_inv t
      USING dual
      ON (t.event_db_id  = :new.event_db_id  AND
          t.event_id     = :new.event_id     AND
          t.event_inv_id = :new.event_inv_id)
      WHEN MATCHED THEN
         UPDATE SET
            inv_no_db_id        = :new.inv_no_db_id,
            inv_no_id           = :new.inv_no_id,
            nh_inv_no_db_id     = :new.nh_inv_no_db_id,
            nh_inv_no_id        = :new.nh_inv_no_id,
            assmbl_inv_no_db_id = :new.assmbl_inv_no_db_id,
            assmbl_inv_no_id    = :new.assmbl_inv_no_id,
            h_inv_no_db_id      = :new.h_inv_no_db_id,
            h_inv_no_id         = :new.h_inv_no_id,
            assmbl_db_id        = :new.assmbl_db_id,
            assmbl_cd           = :new.assmbl_cd,
            assmbl_bom_id       = :new.assmbl_bom_id,
            assmbl_pos_id       = :new.assmbl_pos_id,
            part_no_db_id       = :new.part_no_db_id,
            part_no_id          = :new.part_no_id,
            bom_part_db_id      = :new.bom_part_db_id,
            bom_part_id         = :new.bom_part_id,
            main_inv_bool       = :new.main_inv_bool
      WHEN NOT MATCHED THEN
         INSERT
         (event_db_id,
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
          rstat_cd,
          creation_dt,
          revision_dt,
          revision_db_id,
          revision_user)
      VALUES
        (:new.event_db_id,
         :new.event_id,
         :new.event_inv_id,
         :new.inv_no_db_id,
         :new.inv_no_id,
         :new.nh_inv_no_db_id,
         :new.nh_inv_no_id,
         :new.assmbl_inv_no_db_id,
         :new.assmbl_inv_no_id,
         :new.h_inv_no_db_id,
         :new.h_inv_no_id,
         :new.assmbl_db_id,
         :new.assmbl_cd,
         :new.assmbl_bom_id,
         :new.assmbl_pos_id,
         :new.part_no_db_id,
         :new.part_no_id,
         :new.bom_part_db_id,
         :new.bom_part_id,
         :new.main_inv_bool,
         :new.rstat_cd,
         :new.creation_dt,
         :new.revision_dt,
         :new.revision_db_id,
         :new.revision_user);
      RETURN;
   END IF;

   IF (deleting) THEN
      DELETE FROM evt_inv
   WHERE event_db_id  = :old.event_db_id   AND
         event_id     = :old.event_id      AND
         event_inv_id = :old.event_inv_id;
      RETURN;
   END IF;
END;
/
--changeSet OPER-25051:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER tiudbr_inv_cnd_chg_inv_usage
/********************************************************************************
*
* Trigger:    TIUDBR_INV_CND_CHG_INV_USAGE
*
* Description:  This is a ROW based trigger. Before any IUD DML on a row of the table inv_cnd_chg_inv_usage,
*				        the corresponding same action must be executed on the row of the table evt_inv first for purpose.
*
********************************************************************************/
BEFORE INSERT OR UPDATE OR DELETE ON inv_cnd_chg_inv_usage
FOR EACH ROW

BEGIN

   IF (inserting OR updating) THEN
      MERGE INTO evt_inv_usage t
      USING dual
      ON (t.event_db_id     = :new.event_db_id     AND
          t.event_id        = :new.event_id        AND
          t.event_inv_id    = :new.event_inv_id    AND
          t.data_type_db_id = :new.data_type_db_id AND
          t.data_type_id    = :new.data_type_id)
      WHEN MATCHED THEN
         UPDATE SET
            tsn_qt = :new.tsn_qt,
            tso_qt = :new.tso_qt,
            tsi_qt = :new.tsi_qt,
            assmbl_tsn_qt = :new.assmbl_tsn_qt,
            assmbl_tso_qt = :new.assmbl_tso_qt,
            h_tsn_qt = :new.h_tsn_qt,
            h_tso_qt = :new.h_tso_qt,
            nh_tsn_qt = :new.nh_tsn_qt,
            nh_tso_qt = :new.nh_tso_qt,
            negated_bool = :new.negated_bool
      WHEN NOT MATCHED THEN
         INSERT
         (event_db_id,
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
          rstat_cd,
          creation_dt,
          revision_dt,
          revision_db_id,
          revision_user)
         VALUES
         (:new.event_db_id,
          :new.event_id,
          :new.event_inv_id,
          :new.data_type_db_id,
          :new.data_type_id,
          :new.tsn_qt,
          :new.tso_qt,
          :new.tsi_qt,
          :new.assmbl_tsn_qt,
          :new.assmbl_tso_qt,
          :new.h_tsn_qt,
          :new.h_tso_qt,
          :new.nh_tsn_qt,
          :new.nh_tso_qt,
          :new.negated_bool,
          :new.rstat_cd,
          :new.creation_dt,
          :new.revision_dt,
          :new.revision_db_id,
          :new.revision_user);
		    RETURN;
   END IF;

   IF (deleting) THEN
      DELETE FROM evt_inv_usage
      WHERE event_db_id     = :old.event_db_id     AND
            event_id        = :old.event_id        AND
            event_inv_id    = :old.event_inv_id    AND
            data_type_db_id = :old.data_type_db_id AND
            data_type_id    = :old.data_type_id;
		  RETURN;
   END IF;
END;
/