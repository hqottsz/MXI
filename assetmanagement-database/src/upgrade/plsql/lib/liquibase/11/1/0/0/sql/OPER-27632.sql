--liquibase formatted sql

--changeSet OPER-27632:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on inv_inv that will create log of updates to MT_INV_AC_AUTHORITY
CREATE OR REPLACE TRIGGER tiudar_mt_inv_inv
/********************************************************************************
*
* Trigger:    TIUDAR_MT_INV_INV
*
* Description:  This is a ROW based trigger. After any IUD DML on a row, a log
*				will be entered in the mt_rep_int_evt_scded_pkg table objects
*				for that purpose using the post_update procedure.
*
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE OF inv_no_db_id, inv_no_id, authority_db_id, authority_id, locked_bool, inv_class_cd, inv_class_db_id ON inv_inv
FOR EACH ROW
DECLARE
       lt_work_record	mt_inv_ac_auth_inv_inv_pkg.gt_inv_rec;

BEGIN
	IF (deleting)
		THEN
			IF (:OLD.inv_class_cd    != 'ACFT')
			THEN
			   RETURN;
			END IF;

			IF (:OLD.inv_class_db_id  != 0)
			THEN
			   RETURN;
			END IF;

			lt_work_record.inv_no_db_id     := :OLD.inv_no_db_id;
			lt_work_record.inv_no_id        := :OLD.inv_no_id;
			lt_work_record.action           := 'D';
			mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
	END IF;

	IF (inserting)
		THEN
			IF (:NEW.inv_class_cd    != 'ACFT')
			THEN
			   RETURN;
			END IF;

			IF (:NEW.inv_class_db_id  != 0)
			THEN
			   RETURN;
			END IF;

			lt_work_record.inv_no_db_id    := :NEW.inv_no_db_id;
			lt_work_record.inv_no_id       := :NEW.inv_no_id;
			lt_work_record.authority_db_id := :NEW.authority_db_id;
			lt_work_record.authority_id    := :NEW.authority_id;
			lt_work_record.locked_bool     := :NEW.locked_bool;
			lt_work_record.action          := 'I';
			mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
	END IF;


    IF (updating)
    	THEN
			IF (:NEW.inv_class_cd    != 'ACFT')
			THEN
				RETURN;
			END IF;

			IF (:NEW.inv_class_db_id  != 0)
			THEN
				RETURN;
			END IF;

			lt_work_record.inv_no_db_id    := :NEW.inv_no_db_id;
			lt_work_record.inv_no_id       := :NEW.inv_no_id;
			lt_work_record.authority_db_id := :NEW.authority_db_id;
			lt_work_record.authority_id    := :NEW.authority_id;
			lt_work_record.locked_bool     := :NEW.locked_bool;
			lt_work_record.action          := 'U';
			mt_inv_ac_auth_inv_inv_pkg.post_update (lt_work_record);
		RETURN;
	END IF;
END;
/