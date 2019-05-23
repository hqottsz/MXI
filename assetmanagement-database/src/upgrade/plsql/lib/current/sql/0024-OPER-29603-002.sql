--liquibase formatted sql

--changeSet OPER-29603-002:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
          source_db_id,
          source_cd,
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
          0,
          'MAINTENIX',
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
