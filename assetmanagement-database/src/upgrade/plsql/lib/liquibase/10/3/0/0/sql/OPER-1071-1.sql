--liquibase formatted sql

--changeSet OPER-1071-1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of MT_DRV_SCHED_INFO materialized table
BEGIN
   utl_migr_schema_pkg.table_create('
		CREATE TABLE "MT_DRV_SCHED_INFO"
		(
		    "EVENT_DB_ID" NUMBER(10,0),
		    "EVENT_ID" NUMBER(10,0),
		    "DEADLINE_EVENT_DB_ID" NUMBER(10,0),
		    "DEADLINE_EVENT_ID" NUMBER(10,0),
		    "DEVIATION_QT" FLOAT,
		    "USAGE_REM_QT" FLOAT,
		    "SCHED_DEAD_DT" DATE,
		    "DOMAIN_TYPE_CD" VARCHAR2(8),
		    "DATA_TYPE_CD" VARCHAR2(80),
		    "ENG_UNIT_CD" VARCHAR2(8),
		    "ENG_UNIT_MULT_QT" FLOAT,
		    "PRECISION_QT" FLOAT
		)
   ');
END;
/


--changeSet OPER-1071-1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment mt_rep_int_evt_scded_pkg spec definition
CREATE OR REPLACE PACKAGE mt_rep_int_evt_scded_pkg AS
/********************************************************************************
*
* Package:     mt_rep_int_evt_scded_pkg
*
* Description:  This package is used to build a log of events that need to be
*       processed for the datamart associated with this package. It defines
*       two tables to hold records. For simplicity and speed there are
*       only inserts or deletes.
*
*       action is a simple char that represents i: insert, u:update
*       d:delete statement
*********************************************************************************/

	TYPE gt_evt_rec
	IS RECORD(
		event_db_id NUMBER(10,0),
		event_id    NUMBER(10,0),
		action      CHAR(1));

	TYPE gt_evt_rec_tab IS TABLE OF gt_evt_rec;

	gtab_evt_ins gt_evt_rec_tab;
	gtab_evt_del gt_evt_rec_tab;

	gi_ins INTEGER := 1;
	gi_del INTEGER := 1;

	PROCEDURE post_update(irec IN gt_evt_rec);
	PROCEDURE stream_update;
	PROCEDURE populate_data;

END mt_rep_int_evt_scded_pkg;
/


--changeSet OPER-1071-1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment mt_rep_int_evt_scded_pkg body definition
create or replace PACKAGE BODY mt_rep_int_evt_scded_pkg AS
  /********************************************************************************
  *
  * Procedure:    populate_data
  *
  * Arguments:  None - This procedure populates the associated datamart.
  *
  *
  * Return: N/A
  *
  *
  * Description:  This procedure does simple truncate and re-insert of data into the
  *       datamart. Expected in cases where triggers are disabled for any
  *       reason and need to repopulate with fresh data.
  ********************************************************************************/
  PROCEDURE populate_data IS
  BEGIN
    EXECUTE IMMEDIATE 'TRUNCATE TABLE mt_drv_sched_info';

    INSERT /*+ APPEND */
    INTO mt_drv_sched_info
      SELECT evt_event.event_db_id,
             evt_event.event_id,
             nvl(wp_task_drv_deadline.event_db_id,
                 task_drv_deadline.event_db_id) AS deadline_event_db_id,
             nvl(wp_task_drv_deadline.event_id, task_drv_deadline.event_id) AS deadline_event_id,
             nvl(wp_task_drv_deadline.deviation_qt,
                 task_drv_deadline.deviation_qt) AS deviation_qt,
             nvl(wp_task_drv_deadline.usage_rem_qt,
                 task_drv_deadline.usage_rem_qt) AS usage_rem_qt,
             nvl(wp_task_drv_deadline.sched_dead_dt,
                 task_drv_deadline.sched_dead_dt) AS sched_dead_dt,
             nvl(wp_task_drv_deadline.domain_type_cd,
                 task_drv_deadline.domain_type_cd) AS domain_type_cd,
             nvl(wp_task_drv_deadline.data_type_cd,
                 task_drv_deadline.data_type_cd) AS data_type_cd,
             nvl(wp_task_drv_deadline.eng_unit_cd,
                 task_drv_deadline.eng_unit_cd) AS eng_unit_cd,
             nvl(wp_task_drv_deadline.ref_mult_qt,
                 task_drv_deadline.ref_mult_qt) AS eng_unit_mult_qt,
             nvl(wp_task_drv_deadline.entry_prec_qt,
                 task_drv_deadline.entry_prec_qt) AS precision_qt
        FROM evt_event
        LEFT OUTER JOIN (SELECT evt_sched_dead.event_db_id,
                                evt_sched_dead.event_id,
                                evt_sched_dead.data_type_db_id,
                                evt_sched_dead.data_type_id,
                                evt_sched_dead.deviation_qt,
                                evt_sched_dead.usage_rem_qt,
                                evt_sched_dead.sched_dead_dt,
                                mim_data_type.domain_type_db_id,
                                mim_data_type.domain_type_cd,
                                mim_data_type.data_type_cd,
                                mim_data_type.entry_prec_qt,
                                ref_eng_unit.eng_unit_db_id,
                                ref_eng_unit.eng_unit_cd,
                                ref_eng_unit.ref_mult_qt
                           FROM evt_sched_dead
                          INNER JOIN mim_data_type
                             ON mim_data_type.data_type_db_id =
                                evt_sched_dead.data_type_db_id
                            AND mim_data_type.data_type_id =
                                evt_sched_dead.data_type_id
                          INNER JOIN ref_eng_unit
                             ON ref_eng_unit.eng_unit_db_id =
                                mim_data_type.eng_unit_db_id
                            AND ref_eng_unit.eng_unit_cd =
                                mim_data_type.eng_unit_cd
                          WHERE evt_sched_dead.hist_bool_ro = 0
                            AND evt_sched_dead.sched_driver_bool = 1) task_drv_deadline
          ON task_drv_deadline.event_db_id = evt_event.event_db_id
         AND task_drv_deadline.event_id = evt_event.event_id
        LEFT OUTER JOIN evt_event_rel driving_event_rel
          ON driving_event_rel.event_db_id = evt_event.event_db_id
         AND driving_event_rel.event_id = evt_event.event_id
         AND driving_event_rel.rel_type_db_id = 0
         AND driving_event_rel.rel_type_cd = 'DRVTASK'
        LEFT OUTER JOIN (SELECT evt_sched_dead.event_db_id,
                                evt_sched_dead.event_id,
                                evt_sched_dead.data_type_db_id,
                                evt_sched_dead.data_type_id,
                                evt_sched_dead.deviation_qt,
                                evt_sched_dead.usage_rem_qt,
                                evt_sched_dead.sched_dead_dt,
                                mim_data_type.domain_type_db_id,
                                mim_data_type.domain_type_cd,
                                mim_data_type.data_type_cd,
                                mim_data_type.entry_prec_qt,
                                ref_eng_unit.eng_unit_db_id,
                                ref_eng_unit.eng_unit_cd,
                                ref_eng_unit.ref_mult_qt
                           FROM evt_sched_dead
                          INNER JOIN mim_data_type
                             ON mim_data_type.data_type_db_id =
                                evt_sched_dead.data_type_db_id
                            AND mim_data_type.data_type_id =
                                evt_sched_dead.data_type_id
                          INNER JOIN ref_eng_unit
                             ON ref_eng_unit.eng_unit_db_id =
                                mim_data_type.eng_unit_db_id
                            AND ref_eng_unit.eng_unit_cd =
                                mim_data_type.eng_unit_cd
                          WHERE evt_sched_dead.hist_bool_ro = 0
                            AND evt_sched_dead.sched_driver_bool = 1) wp_task_drv_deadline
          ON wp_task_drv_deadline.event_db_id = driving_event_rel.rel_event_db_id
         AND wp_task_drv_deadline.event_id = driving_event_rel.rel_event_id
       WHERE nvl(wp_task_drv_deadline.event_db_id,
                 task_drv_deadline.event_db_id) IS NOT NULL;
    COMMIT;
  END populate_data;

  /********************************************************************************
  *
  * Procedure:    reset_collections
  * Arguments:  N/A
  *
  * Return: N/A
  *
  * Description:  Essentially garbage collection/cleanup procedure internal to pkg
  ********************************************************************************/

  PROCEDURE reset_collections AS
  BEGIN
    gtab_evt_ins.DELETE;
    gtab_evt_del.DELETE;
    gi_ins := 1;
    gi_del := 1;

    RETURN;
  END reset_collections;

  /********************************************************************************
  *
  * Procedure:    post_update
  * Arguments:  gt_evt_rec      iRec - The record of event to be processed.
  *                     Defined in package header.
  *
  *
  * Return: N/A
  *
  *
  * Description:  post_update is called after a ROW trigger is fired. It then builds
  *       a log of the future operations to execute during streaming phase.
  *       This allows any number of row operations to occur with a final
  *       execution to occur in the stream_update.
  ********************************************************************************/

  PROCEDURE post_update(irec IN gt_evt_rec) AS

  BEGIN
    IF (irec.action = 'I') THEN
      IF (NOT gtab_evt_ins.EXISTS(gi_ins)) THEN
        gtab_evt_ins.extend();
      END IF;

      gtab_evt_ins(gi_ins) := irec;
      gi_ins := gi_ins + 1;

      RETURN;
    END IF;

    IF (irec.action = 'D') THEN
      IF (NOT gtab_evt_del.EXISTS(gi_del)) THEN
        gtab_evt_del.extend();
      END IF;

      gtab_evt_del(gi_del) := irec;
      gi_del := gi_del + 1;

      RETURN;
    END IF;

    IF (irec.action = 'U') THEN
      IF (NOT gtab_evt_del.EXISTS(gi_del)) THEN
        gtab_evt_del.extend();
      END IF;

      gtab_evt_del(gi_del) := irec;
      gi_del := gi_del + 1;

      IF (NOT gtab_evt_ins.EXISTS(gi_ins)) THEN
        gtab_evt_ins.extend();
      END IF;

      gtab_evt_ins(gi_ins) := irec;
      gi_ins := gi_ins + 1;

      RETURN;
    END IF;
  EXCEPTION
    WHEN OTHERS THEN
      reset_collections;
      RAISE;
  END post_update;

  /********************************************************************************
  *
  * Procedure:    stream_update
  *
  * Arguments:  None - This procedure processes N insert and delete records for
  *           the associated datamart.
  *
  *
  * Return: N/A
  *
  *
  * Description:  stream_update is called after a TABLE trigger is fired. It then
  *       processes the entries in the gtab_evt_ins, gtab_evt_del table
  *       objects.
  *
  *       Finally it flushes the log of the events.
  ********************************************************************************/

  PROCEDURE stream_update AS

    le_ex_dml_errors EXCEPTION;
    PRAGMA exception_init(le_ex_dml_errors, -24381);

    li_exi INTEGER;

  BEGIN

    /********************************************************************************
    *
    * Conduct DELETE operations
    *
    ********************************************************************************/

    BEGIN
      FORALL li_i IN 1 .. gi_del - 1 SAVE EXCEPTIONS

        DELETE FROM mt_drv_sched_info
         WHERE mt_drv_sched_info.event_db_id = gtab_evt_del(li_i).event_db_id
           AND mt_drv_sched_info.event_id = gtab_evt_del(li_i).event_id;

    EXCEPTION
      WHEN le_ex_dml_errors THEN
        FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
          IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
            raise_application_error(SQL%bulk_exceptions(li_exi).error_code,
                                    sqlerrm(- (SQL%bulk_exceptions(li_exi)
                                              .error_code)));
          END IF;
        END LOOP;
    END;

    /********************************************************************************
    *
    * Conduct INSERT operations
    *
    ********************************************************************************/

    BEGIN
      FORALL li_i IN 1 .. gi_ins - 1 SAVE EXCEPTIONS
        INSERT INTO mt_drv_sched_info
          SELECT gtab_evt_ins(li_i).event_db_id,
                 gtab_evt_ins(li_i).event_id,
                 nvl(wp_task_drv_deadline.event_db_id,
                     task_drv_deadline.event_db_id) AS deadline_event_db_id,
                 nvl(wp_task_drv_deadline.event_id,
                     task_drv_deadline.event_id) AS deadline_event_id,
                 nvl(wp_task_drv_deadline.deviation_qt,
                     task_drv_deadline.deviation_qt) AS deviation_qt,
                 round(nvl(wp_task_drv_deadline.usage_rem_qt,
                           task_drv_deadline.usage_rem_qt),
                       2) AS usage_rem_qt,
                 nvl(wp_task_drv_deadline.sched_dead_dt,
                     task_drv_deadline.sched_dead_dt) AS sched_dead_dt,
                 nvl(wp_task_drv_deadline.domain_type_cd,
                     task_drv_deadline.domain_type_cd) AS domain_type_cd,
                 nvl(wp_task_drv_deadline.data_type_cd,
                     task_drv_deadline.data_type_cd) AS data_type_cd,
                 nvl(wp_task_drv_deadline.eng_unit_cd,
                     task_drv_deadline.eng_unit_cd) AS eng_unit_cd,
                 nvl(wp_task_drv_deadline.ref_mult_qt,
                     task_drv_deadline.ref_mult_qt) AS eng_unit_mult_qt,
                 nvl(wp_task_drv_deadline.entry_prec_qt,
                     task_drv_deadline.entry_prec_qt) AS precision_qt
            FROM
            (SELECT evt_sched_dead.event_db_id,
                                    evt_sched_dead.event_id,
                                    evt_sched_dead.data_type_db_id,
                                    evt_sched_dead.data_type_id,
                                    evt_sched_dead.deviation_qt,
                                    evt_sched_dead.usage_rem_qt,
                                    evt_sched_dead.sched_dead_dt,
                                    mim_data_type.domain_type_db_id,
                                    mim_data_type.domain_type_cd,
                                    mim_data_type.data_type_cd,
                                    mim_data_type.entry_prec_qt,
                                    ref_eng_unit.eng_unit_db_id,
                                    ref_eng_unit.eng_unit_cd,
                                    ref_eng_unit.ref_mult_qt
                               FROM evt_sched_dead
                              INNER JOIN mim_data_type
                                 ON mim_data_type.data_type_db_id =
                                    evt_sched_dead.data_type_db_id
                                AND mim_data_type.data_type_id =
                                    evt_sched_dead.data_type_id
                              INNER JOIN ref_eng_unit
                                 ON ref_eng_unit.eng_unit_db_id =
                                    mim_data_type.eng_unit_db_id
                                AND ref_eng_unit.eng_unit_cd =
                                    mim_data_type.eng_unit_cd
                              WHERE evt_sched_dead.hist_bool_ro = 0
                                AND evt_sched_dead.sched_driver_bool = 1) task_drv_deadline
            LEFT OUTER JOIN evt_event_rel driving_event_rel
              ON driving_event_rel.event_db_id = gtab_evt_ins(li_i).event_db_id
             AND driving_event_rel.event_id = gtab_evt_ins(li_i).event_id
             AND driving_event_rel.rel_type_db_id = 0
             AND driving_event_rel.rel_type_cd = 'DRVTASK'
            LEFT OUTER JOIN (SELECT evt_sched_dead.event_db_id,
                                    evt_sched_dead.event_id,
                                    evt_sched_dead.data_type_db_id,
                                    evt_sched_dead.data_type_id,
                                    evt_sched_dead.deviation_qt,
                                    evt_sched_dead.usage_rem_qt,
                                    evt_sched_dead.sched_dead_dt,
                                    mim_data_type.domain_type_db_id,
                                    mim_data_type.domain_type_cd,
                                    mim_data_type.data_type_cd,
                                    mim_data_type.entry_prec_qt,
                                    ref_eng_unit.eng_unit_db_id,
                                    ref_eng_unit.eng_unit_cd,
                                    ref_eng_unit.ref_mult_qt
                               FROM evt_sched_dead
                              INNER JOIN mim_data_type
                                 ON mim_data_type.data_type_db_id =
                                    evt_sched_dead.data_type_db_id
                                AND mim_data_type.data_type_id =
                                    evt_sched_dead.data_type_id
                              INNER JOIN ref_eng_unit
                                 ON ref_eng_unit.eng_unit_db_id =
                                    mim_data_type.eng_unit_db_id
                                AND ref_eng_unit.eng_unit_cd =
                                    mim_data_type.eng_unit_cd
                              WHERE evt_sched_dead.hist_bool_ro = 0
                                AND evt_sched_dead.sched_driver_bool = 1) wp_task_drv_deadline
              ON wp_task_drv_deadline.event_db_id =
                 driving_event_rel.rel_event_db_id
             AND wp_task_drv_deadline.event_id =
                 driving_event_rel.rel_event_id

           WHERE nvl(wp_task_drv_deadline.event_db_id,
                     task_drv_deadline.event_db_id) IS NOT NULL
             AND task_drv_deadline.event_db_id = gtab_evt_ins(li_i).event_db_id
             AND task_drv_deadline.event_id = gtab_evt_ins(li_i).event_id;

    EXCEPTION
      WHEN le_ex_dml_errors THEN
        FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
          IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
            raise_application_error(SQL%bulk_exceptions(li_exi).error_code,
                                    sqlerrm(- (SQL%bulk_exceptions(li_exi)
                                              .error_code)));
          END IF;
        END LOOP;
    END;

    /****************************************************
    *
    * Flush the log
    *
    *****************************************************/
    reset_collections;

  EXCEPTION
    WHEN OTHERS THEN
      reset_collections;
      RAISE;
  END stream_update;

/****************************************************
*
* Initialization of global record tab
*
*****************************************************/
BEGIN

  gtab_evt_ins := gt_evt_rec_tab();
  gtab_evt_del := gt_evt_rec_tab();

END mt_rep_int_evt_scded_pkg;
/

--changeSet OPER-1071-1:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment fill MT_DRV_SCHED_INFO datamart with data
BEGIN
	mt_rep_int_evt_scded_pkg.populate_data;
END;
/

--changeSet OPER-1071-1:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on evt_sched_dead that reads the log of updates to MT_DRV_SCHED_INFO
CREATE OR REPLACE TRIGGER TIUDA_MT_DRV_SCHED_INFO
/********************************************************************************
*
* Trigger:    TIUDA_MT_DRV_SCHED_INFO
*
* Description:  This is a TABLE based trigger. After any IUD DML on the table
*       it will look into the mt_rep_int_evt_scded_pkg and stream_update
*       [0,N) records in the log objects for this purpose.
*
********************************************************************************/
  AFTER DELETE OR INSERT OR UPDATE ON evt_sched_dead
BEGIN
  mt_rep_int_evt_scded_pkg.stream_update;
END;
/


--changeSet OPER-1071-1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on evt_sched_dead that will create log of updates to MT_DRV_SCHED_INFO
CREATE OR REPLACE TRIGGER TIUDAR_MT_DRV_SCHED_INFO
/********************************************************************************
*
* Trigger:    TIUDAR_MT_DRV_SCHED_INFO
*
* Description:  This is a ROW based trigger. After any IUD DML on a row, a log
*       will be entered in the mt_rep_int_evt_scded_pkg table objects
*       for that purpose using the post_update procedure.
*
********************************************************************************/
  AFTER DELETE OR INSERT OR UPDATE ON evt_sched_dead
  FOR EACH ROW

DECLARE
  lt_log_rec mt_rep_int_evt_scded_pkg.gt_evt_rec;

BEGIN
  IF (inserting) THEN
    IF (:NEW.hist_bool_ro = 0 AND :NEW.sched_driver_bool = 1) THEN
      lt_log_rec.event_db_id := :NEW.event_db_id;
      lt_log_rec.event_id    := :NEW.event_id;
      lt_log_rec.action      := 'I';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
    END IF;
  END IF;

  IF (updating) THEN
    IF (:NEW.hist_bool_ro = 0 AND :NEW.sched_driver_bool = 1) THEN
      lt_log_rec.event_db_id := :NEW.event_db_id;
      lt_log_rec.event_id    := :NEW.event_id;
      lt_log_rec.action      := 'U';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
	END IF;
	IF (:NEW.hist_bool_ro <> 0 OR :NEW.sched_driver_bool <> 1 AND
        (:OLD.hist_bool_ro = 0 AND :OLD.sched_driver_bool = 1)) THEN
      lt_log_rec.event_db_id := :NEW.event_db_id;
      lt_log_rec.event_id    := :NEW.event_id;
      lt_log_rec.action      := 'D';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
	END IF;
  END IF;

  IF (deleting) THEN
    IF (:OLD.hist_bool_ro = 0 AND :OLD.sched_driver_bool = 1) THEN
      lt_log_rec.event_db_id := :OLD.event_db_id;
      lt_log_rec.event_id    := :OLD.event_id;
      lt_log_rec.action      := 'D';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
    END IF;
  END IF;
END;
/


--changeSet OPER-1071-1:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add index to mt_drv_sched_info table
BEGIN
  utl_migr_schema_pkg.index_create('
		CREATE INDEX "IX_MT_REPINT_DED"	ON mt_drv_sched_info
		(
		"DEADLINE_EVENT_DB_ID",
		"DEADLINE_EVENT_ID"
		)
	');
END;
/

--changeSet OPER-1071-1:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add second index to mt_drv_sched_info table
BEGIN
  utl_migr_schema_pkg.index_create('
		CREATE INDEX "IX_MT_REPINT_EVT" ON mt_drv_sched_info
		(
		"EVENT_DB_ID",
		"EVENT_ID"
		)
	');
END;
/

