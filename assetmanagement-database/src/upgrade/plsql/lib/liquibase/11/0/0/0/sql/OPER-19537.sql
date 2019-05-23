--liquibase formatted sql

--changeSet OPER-19537:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment DROP the MT_DRV_SCHED_INFO Table
BEGIN
  utl_migr_schema_pkg.table_drop('MT_DRV_SCHED_INFO');
END;
/


--changeSet OPER-19537:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of MT_DRV_SCHED_INFO materialized table
BEGIN
   utl_migr_schema_pkg.table_create('
		CREATE TABLE "MT_DRV_SCHED_INFO"
		(
		    "EVENT_DB_ID" NUMBER(10,0), 
            "EVENT_ID" NUMBER(10,0), 
            "DRV_EVENT_DB_ID" NUMBER(10,0), 
            "DRV_EVENT_ID" NUMBER(10,0), 
            "EVT_EXT_DEAD_DT" DATE, 
            "DRV_EXT_DEAD_DT" DATE, 
            "EVT_SCHED_DEAD_DT" DATE, 
            "DRV_SCHED_DEAD_DT" DATE, 
            "EVT_PLAN_BY_DT" DATE, 
            "DRV_PLAN_BY_DT" DATE, 
            "DRV_DEVIATION_QT" FLOAT, 
            "DRV_USAGE_REM_QT" FLOAT, 
            "DRV_DOMAIN_TYPE_CD" VARCHAR2(8), 
            "DRV_ENG_UNIT_CD" VARCHAR2(8), 
            "DRV_PRECISION_QT" NUMBER(10,0), 
            "DRV_DATA_TYPE_CD" VARCHAR2(80), 
            "DRV_DATA_TYPE_DB_ID" NUMBER(10,0), 
            "DRV_DATA_TYPE_ID" NUMBER(10,0), 
            "SORT_DUE_DT" DATE
		) PCTFREE 0 PCTUSED 99
   ');
END;
/


--changeSet OPER-19537:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment mt_rep_int_evt_scded_pkg spec definition
create or replace PACKAGE mt_rep_int_evt_scded_pkg AS
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
		event_db_id       NUMBER(10,0),
		event_id          NUMBER(10,0),
        data_type_db_id   NUMBER(10,0),
        data_type_id      NUMBER(10,0),
		action            CHAR(1));

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


--changeSet OPER-19537:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment update of the mt_rep_int_evt_scded_pkg
CREATE or REPLACE PACKAGE BODY mt_rep_int_evt_scded_pkg AS
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
        SELECT            
            t.*,
            nvl(t.drv_plan_by_dt,
                nvl(t.evt_plan_by_dt,
                    nvl(t.drv_ext_dead_dt,t.evt_ext_dead_dt))) AS sort_due_dt
        FROM (
            WITH drv_tasks 
                 AS(
                    SELECT
                        evt_event_rel.event_db_id     AS task_event_db_id,
                        evt_event_rel.event_id        AS task_event_id,
                        evt_event_rel.rel_event_db_id AS drv_task_event_db_id,
                        evt_event_rel.rel_event_id    AS drv_task_event_id,
                        sched_stask.plan_by_dt,
                        evt_sched_dead.sched_dead_dt,
                        evt_sched_dead.deviation_qt,
                        evt_sched_dead.usage_rem_qt,
                        mim_data_type.domain_type_cd,
                        mim_data_type.entry_prec_qt,
                        mim_data_type.data_type_cd,
                        mim_data_type.data_type_db_id,
                        mim_data_type.data_type_id,
                        ref_eng_unit.ref_mult_qt,
                        ref_eng_unit.eng_unit_cd

                    FROM 
                        evt_event_rel 
                        INNER JOIN evt_sched_dead 
                        ON  evt_sched_dead.event_db_id   = evt_event_rel.rel_event_db_id  AND
                            evt_sched_dead.event_id      = evt_event_rel.rel_event_id
                        INNER JOIN mim_data_type 
                        ON mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                           mim_data_type.data_type_id    = evt_sched_dead.data_type_id
                        INNER JOIN ref_eng_unit
                        ON ref_eng_unit.eng_unit_db_id   = mim_data_type.eng_unit_db_id   AND
                           ref_eng_unit.eng_unit_cd      = mim_data_type.eng_unit_cd
                        INNER JOIN sched_stask
                        ON sched_stask.sched_db_id       = evt_sched_dead.event_db_id     AND
                           sched_stask.sched_id          = evt_sched_dead.event_id
                    WHERE
                        evt_sched_dead.hist_bool_ro      = 0 AND
                        evt_sched_dead.sched_driver_bool = 1
                        AND
                        evt_event_rel.rel_type_db_id     = 0 AND
                        evt_event_rel.rel_type_cd        = 'DRVTASK'
                ),

                base_task 
                AS (
                    SELECT
                        evt_sched_dead.event_db_id,
                        evt_sched_dead.event_id,
                        sched_stask.plan_by_dt,
                        evt_sched_dead.sched_dead_dt,
                        evt_sched_dead.deviation_qt,
                        evt_sched_dead.usage_rem_qt,
                        mim_data_type.domain_type_cd,
                        mim_data_type.entry_prec_qt,
                        mim_data_type.data_type_cd,
                        mim_data_type.data_type_db_id,
                        mim_data_type.data_type_id,
                        ref_eng_unit.ref_mult_qt,
                        ref_eng_unit.eng_unit_cd
                    FROM 
                        evt_sched_dead
                        INNER JOIN mim_data_type 
                        ON mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                           mim_data_type.data_type_id    = evt_sched_dead.data_type_id
                        INNER JOIN ref_eng_unit
                        ON ref_eng_unit.eng_unit_db_id   = mim_data_type.eng_unit_db_id AND
                           ref_eng_unit.eng_unit_cd      = mim_data_type.eng_unit_cd
                        INNER JOIN sched_stask 
                        ON sched_stask.sched_db_id       = evt_sched_dead.event_db_id AND
                           sched_stask.sched_id          = evt_sched_dead.event_id
                    WHERE
                        evt_sched_dead.hist_bool_ro      = 0 AND
                        evt_sched_dead.sched_driver_bool = 1
                )

            SELECT 
                --  main event, 1 to 1 with driving row in evt_sched_dead
                base_task.event_db_id,
                base_task.event_id,
                --  driving event, if drv_task null then task itself
                nvl(drv_tasks.drv_task_event_db_id, base_task.event_db_id) AS drv_event_db_id,
                nvl(drv_tasks.drv_task_event_id   , base_task.event_id)    AS drv_event_id,    
                CASE base_task.domain_type_cd
                    WHEN 'US' THEN base_task.sched_dead_dt
                ELSE
                    CASE base_task.data_type_cd
                        WHEN 'CMON'  THEN  ADD_MONTHS( base_task.sched_dead_dt, base_task.deviation_qt ) + TRUNC((base_task.deviation_qt - TRUNC(base_task.deviation_qt)) * base_task.ref_mult_qt)
                        WHEN 'CLMON' THEN  ADD_MONTHS( LAST_DAY(base_task.sched_dead_dt), base_task.deviation_qt ) + TRUNC((base_task.deviation_qt - TRUNC(base_task.deviation_qt)) * base_task.ref_mult_qt)
                        WHEN 'CYR'   THEN  ADD_MONTHS( base_task.sched_dead_dt, base_task.deviation_qt*12 ) + TRUNC((base_task.deviation_qt - TRUNC(base_task.deviation_qt * 12)/12) * base_task.ref_mult_qt)
                        WHEN 'CHR'   THEN  base_task.sched_dead_dt + (base_task.deviation_qt * base_task.ref_mult_qt)
                    ELSE  base_task.sched_dead_dt + TRUNC(base_task.deviation_qt * base_task.ref_mult_qt)
                    END
                END AS evt_ext_dead_dt,
                CASE drv_tasks.domain_type_cd
                    WHEN 'US' THEN drv_tasks.sched_dead_dt
                ELSE
                    CASE drv_tasks.data_type_cd
                        WHEN 'CMON'  THEN  ADD_MONTHS( drv_tasks.sched_dead_dt, drv_tasks.deviation_qt ) + TRUNC((drv_tasks.deviation_qt - TRUNC(drv_tasks.deviation_qt)) * drv_tasks.ref_mult_qt)
                        WHEN 'CLMON' THEN  ADD_MONTHS( LAST_DAY(drv_tasks.sched_dead_dt), drv_tasks.deviation_qt ) + TRUNC((drv_tasks.deviation_qt - TRUNC(drv_tasks.deviation_qt)) * drv_tasks.ref_mult_qt)
                        WHEN 'CYR'   THEN  ADD_MONTHS( drv_tasks.sched_dead_dt, drv_tasks.deviation_qt*12 ) + TRUNC((drv_tasks.deviation_qt - TRUNC(drv_tasks.deviation_qt * 12)/12) * drv_tasks.ref_mult_qt)
                        WHEN 'CHR'   THEN  drv_tasks.sched_dead_dt + (drv_tasks.deviation_qt * drv_tasks.ref_mult_qt)
                    ELSE  drv_tasks.sched_dead_dt + TRUNC(drv_tasks.deviation_qt * drv_tasks.ref_mult_qt)
                    END
                END AS drv_ext_dead_dt,
                base_task.sched_dead_dt                                   AS evt_sched_dead_dt,
				nvl(drv_tasks.sched_dead_dt  , base_task.sched_dead_dt)   AS drv_sched_dead_dt,
                base_task.plan_by_dt                                      AS evt_plan_by_dt,
                drv_tasks.plan_by_dt                                      AS drv_plan_by_dt,
                -- driving task has precedence
                nvl(drv_tasks.deviation_qt   , base_task.deviation_qt)    AS drv_deviation_qt,
				nvl(drv_tasks.usage_rem_qt   , base_task.usage_rem_qt)    AS drv_usage_rem_qt,
				nvl(drv_tasks.domain_type_cd , base_task.domain_type_cd)  AS drv_domain_type_cd,
				nvl(drv_tasks.eng_unit_cd    , base_task.eng_unit_cd)     AS drv_eng_unit_cd,
				nvl(drv_tasks.entry_prec_qt  , base_task.entry_prec_qt)   AS drv_precision_qt,
				nvl(drv_tasks.data_type_cd   , base_task.data_type_cd)    AS drv_data_type_cd,
				nvl(drv_tasks.data_type_db_id, base_task.data_type_db_id) AS drv_data_type_db_id,
				nvl(drv_tasks.data_type_id   , base_task.data_type_id)    AS drv_data_type_id

            FROM base_task

                LEFT OUTER JOIN drv_tasks 
                ON drv_tasks.task_event_db_id = base_task.event_db_id AND
                   drv_tasks.task_event_id    = base_task.event_id
        ) t
        ;
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
      gi_ins               := gi_ins + 1;

      RETURN;
    END IF;

    IF (irec.action = 'D') THEN
      IF (NOT gtab_evt_del.EXISTS(gi_del)) THEN
        gtab_evt_del.extend();
      END IF;      

      gtab_evt_del(gi_del)  := irec;
      gi_del                := gi_del + 1;

      RETURN;
    END IF;

    IF (irec.action = 'U') THEN
      IF (NOT gtab_evt_del.EXISTS(gi_del)) THEN
        gtab_evt_del.extend();
      END IF;    

      gtab_evt_del(gi_del)  := irec;
      gi_del                := gi_del + 1;

      IF (NOT gtab_evt_ins.EXISTS(gi_ins)) THEN
        gtab_evt_ins.extend();
      END IF;

      gtab_evt_ins(gi_ins) := irec;
      gi_ins               := gi_ins + 1;

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
      FORALL li_i IN 1 .. gi_del - 1  SAVE EXCEPTIONS
        DELETE FROM mt_drv_sched_info
         WHERE mt_drv_sched_info.event_db_id = gtab_evt_del(li_i).event_db_id AND
               mt_drv_sched_info.event_id    = gtab_evt_del(li_i).event_id;

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
        SELECT            
            t.*,
            nvl(t.drv_plan_by_dt,
                nvl(t.evt_plan_by_dt,
                    nvl(t.drv_ext_dead_dt,t.evt_ext_dead_dt))) AS sort_due_dt
        FROM (
            WITH drv_tasks 
                 AS(
                    SELECT
                        evt_event_rel.event_db_id     AS task_event_db_id,
                        evt_event_rel.event_id        AS task_event_id,
                        evt_event_rel.rel_event_db_id AS drv_task_event_db_id,
                        evt_event_rel.rel_event_id    AS drv_task_event_id,
                        sched_stask.plan_by_dt,
                        evt_sched_dead.sched_dead_dt,
                        evt_sched_dead.deviation_qt,
                        evt_sched_dead.usage_rem_qt,
                        mim_data_type.domain_type_cd,
                        mim_data_type.entry_prec_qt,
                        mim_data_type.data_type_cd,
                        mim_data_type.data_type_db_id,
                        mim_data_type.data_type_id,
                        ref_eng_unit.ref_mult_qt,
                        ref_eng_unit.eng_unit_cd

                    FROM 
                        evt_event_rel 
                        INNER JOIN evt_sched_dead 
                        ON  evt_sched_dead.event_db_id   = evt_event_rel.rel_event_db_id  AND
                            evt_sched_dead.event_id      = evt_event_rel.rel_event_id
                        INNER JOIN mim_data_type 
                        ON mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                           mim_data_type.data_type_id    = evt_sched_dead.data_type_id
                        INNER JOIN ref_eng_unit
                        ON ref_eng_unit.eng_unit_db_id   = mim_data_type.eng_unit_db_id   AND
                           ref_eng_unit.eng_unit_cd      = mim_data_type.eng_unit_cd
                        INNER JOIN sched_stask
                        ON sched_stask.sched_db_id       = evt_event_rel.event_db_id     AND
                           sched_stask.sched_id          = evt_event_rel.event_id
                    WHERE
                        evt_sched_dead.hist_bool_ro      = 0 AND
                        evt_sched_dead.sched_driver_bool = 1
                        AND
                        evt_event_rel.rel_type_db_id     = 0 AND
                        evt_event_rel.rel_type_cd        = 'DRVTASK'
						AND
						evt_event_rel.event_db_id        = gtab_evt_ins(li_i).event_db_id AND
                        evt_event_rel.event_id           = gtab_evt_ins(li_i).event_id   
                ),

                base_tasks 
                AS (
                    SELECT
                        evt_sched_dead.event_db_id,
                        evt_sched_dead.event_id,
                        sched_stask.plan_by_dt,
                        evt_sched_dead.sched_dead_dt,
                        evt_sched_dead.deviation_qt,
                        evt_sched_dead.usage_rem_qt,
                        mim_data_type.domain_type_cd,
                        mim_data_type.entry_prec_qt,
                        mim_data_type.data_type_cd,
                        mim_data_type.data_type_db_id,
                        mim_data_type.data_type_id,
                        ref_eng_unit.ref_mult_qt,
                        ref_eng_unit.eng_unit_cd
                    FROM 
                        evt_sched_dead
                        INNER JOIN mim_data_type 
                        ON mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
                           mim_data_type.data_type_id    = evt_sched_dead.data_type_id
                        INNER JOIN ref_eng_unit
                        ON ref_eng_unit.eng_unit_db_id   = mim_data_type.eng_unit_db_id   AND
                           ref_eng_unit.eng_unit_cd      = mim_data_type.eng_unit_cd
                        INNER JOIN sched_stask 
                        ON sched_stask.sched_db_id       = evt_sched_dead.event_db_id     AND
                           sched_stask.sched_id          = evt_sched_dead.event_id
                    WHERE
                        evt_sched_dead.hist_bool_ro      = 0 AND
                        evt_sched_dead.sched_driver_bool = 1
						AND
						evt_sched_dead.event_db_id       = gtab_evt_ins(li_i).event_db_id     AND
                        evt_sched_dead.event_id          = gtab_evt_ins(li_i).event_id        AND
                        evt_sched_dead.data_type_db_id   = gtab_evt_ins(li_i).data_type_db_id AND
                        evt_sched_dead.data_type_id      = gtab_evt_ins(li_i).data_type_id    
                )

            SELECT 
                --  main event, 1 to 1 with driving row in evt_sched_dead
                base_tasks.event_db_id,
                base_tasks.event_id,
                --  driving event, if drv_task null then task itself
                nvl(drv_tasks.drv_task_event_db_id, base_tasks.event_db_id) AS drv_event_db_id,
                nvl(drv_tasks.drv_task_event_id   , base_tasks.event_id)    AS drv_event_id,    
                CASE base_tasks.domain_type_cd
                    WHEN 'US' THEN base_tasks.sched_dead_dt
                ELSE
                    CASE base_tasks.data_type_cd
                        WHEN 'CMON'  THEN  ADD_MONTHS( base_tasks.sched_dead_dt, base_tasks.deviation_qt ) + TRUNC((base_tasks.deviation_qt - TRUNC(base_tasks.deviation_qt)) * base_tasks.ref_mult_qt)
                        WHEN 'CLMON' THEN  ADD_MONTHS( LAST_DAY(base_tasks.sched_dead_dt), base_tasks.deviation_qt ) + TRUNC((base_tasks.deviation_qt - TRUNC(base_tasks.deviation_qt)) * base_tasks.ref_mult_qt)
                        WHEN 'CYR'   THEN  ADD_MONTHS( base_tasks.sched_dead_dt, base_tasks.deviation_qt*12 ) + TRUNC((base_tasks.deviation_qt - TRUNC(base_tasks.deviation_qt * 12)/12) * base_tasks.ref_mult_qt)
                        WHEN 'CHR'   THEN  base_tasks.sched_dead_dt + (base_tasks.deviation_qt * base_tasks.ref_mult_qt)
                    ELSE  base_tasks.sched_dead_dt + TRUNC(base_tasks.deviation_qt * base_tasks.ref_mult_qt)
                    END
                END AS evt_ext_dead_dt,
                CASE drv_tasks.domain_type_cd
                    WHEN 'US' THEN drv_tasks.sched_dead_dt
                ELSE
                    CASE drv_tasks.data_type_cd
                        WHEN 'CMON'  THEN  ADD_MONTHS( drv_tasks.sched_dead_dt, drv_tasks.deviation_qt ) + TRUNC((drv_tasks.deviation_qt - TRUNC(drv_tasks.deviation_qt)) * drv_tasks.ref_mult_qt)
                        WHEN 'CLMON' THEN  ADD_MONTHS( LAST_DAY(drv_tasks.sched_dead_dt), drv_tasks.deviation_qt ) + TRUNC((drv_tasks.deviation_qt - TRUNC(drv_tasks.deviation_qt)) * drv_tasks.ref_mult_qt)
                        WHEN 'CYR'   THEN  ADD_MONTHS( drv_tasks.sched_dead_dt, drv_tasks.deviation_qt*12 ) + TRUNC((drv_tasks.deviation_qt - TRUNC(drv_tasks.deviation_qt * 12)/12) * drv_tasks.ref_mult_qt)
                        WHEN 'CHR'   THEN  drv_tasks.sched_dead_dt + (drv_tasks.deviation_qt * drv_tasks.ref_mult_qt)
                    ELSE  drv_tasks.sched_dead_dt + TRUNC(drv_tasks.deviation_qt * drv_tasks.ref_mult_qt)
                    END
                END AS drv_ext_dead_dt,
                base_tasks.sched_dead_dt                                   AS evt_sched_dead_dt,
				nvl(drv_tasks.sched_dead_dt  , base_tasks.sched_dead_dt)   AS drv_sched_dead_dt,
                base_tasks.plan_by_dt                                      AS evt_plan_by_dt,
                drv_tasks.plan_by_dt                                       AS drv_plan_by_dt,
                -- driving task has precedence
                nvl(drv_tasks.deviation_qt   , base_tasks.deviation_qt)    AS drv_deviation_qt,
				nvl(drv_tasks.usage_rem_qt   , base_tasks.usage_rem_qt)    AS drv_usage_rem_qt,
				nvl(drv_tasks.domain_type_cd , base_tasks.domain_type_cd)  AS drv_domain_type_cd,
				nvl(drv_tasks.eng_unit_cd    , base_tasks.eng_unit_cd)     AS drv_eng_unit_cd,
				nvl(drv_tasks.entry_prec_qt  , base_tasks.entry_prec_qt)   AS drv_precision_qt,
				nvl(drv_tasks.data_type_cd   , base_tasks.data_type_cd)    AS drv_data_type_cd,
				nvl(drv_tasks.data_type_db_id, base_tasks.data_type_db_id) AS drv_data_type_db_id,
				nvl(drv_tasks.data_type_id   , base_tasks.data_type_id)    AS drv_data_type_id

            FROM base_tasks

                LEFT OUTER JOIN drv_tasks 
                ON drv_tasks.task_event_db_id = base_tasks.event_db_id AND
                   drv_tasks.task_event_id    = base_tasks.event_id
        ) t
        ;


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

    gtab_evt_ins   := gt_evt_rec_tab();
    gtab_evt_del   := gt_evt_rec_tab();

END mt_rep_int_evt_scded_pkg;
/



--changeSet OPER-19537:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment fill MT_DRV_SCHED_INFO datamart with data
BEGIN
	mt_rep_int_evt_scded_pkg.populate_data;
END;
/


--changeSet OPER-19537:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on evt_sched_dead that reads the log of updates to MT_DRV_SCHED_INFO
CREATE or REPLACE TRIGGER TIUDAR_MT_DRV_SCHED_INFO
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
    IF (:NEW.hist_bool_ro = 0 AND :NEW.sched_driver_bool = 1)
	THEN
      lt_log_rec.event_db_id       := :NEW.event_db_id;
      lt_log_rec.event_id          := :NEW.event_id;
      lt_log_rec.data_type_db_id   := :NEW.data_type_db_id;
      lt_log_rec.data_type_id      := :NEW.data_type_id;
      lt_log_rec.action            := 'I';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
    END IF;
  END IF;

  IF (updating) THEN
    IF (:NEW.hist_bool_ro = 0 AND :NEW.sched_driver_bool = 1)
	THEN
      lt_log_rec.event_db_id       := :NEW.event_db_id;
      lt_log_rec.event_id          := :NEW.event_id;
      lt_log_rec.data_type_db_id   := :NEW.data_type_db_id;
      lt_log_rec.data_type_id      := :NEW.data_type_id;
      lt_log_rec.action            := 'U';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
	END IF;
	IF (:NEW.hist_bool_ro <> 0 OR :NEW.sched_driver_bool <> 1 AND
        (:OLD.hist_bool_ro = 0 AND :OLD.sched_driver_bool = 1))
	THEN
      lt_log_rec.event_db_id := :NEW.event_db_id;
      lt_log_rec.event_id    := :NEW.event_id;
      lt_log_rec.action      := 'D';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
	END IF;
  END IF;

  IF (deleting) THEN
    IF (:OLD.hist_bool_ro = 0 AND :OLD.sched_driver_bool = 1)
	THEN
      lt_log_rec.event_db_id := :OLD.event_db_id;
      lt_log_rec.event_id    := :OLD.event_id;
      lt_log_rec.action      := 'D';
      mt_rep_int_evt_scded_pkg.post_update(lt_log_rec);
    END IF;
  END IF;
END;
/

--changeSet OPER-19537:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add index to mt_drv_sched_info table
BEGIN
  utl_migr_schema_pkg.index_create('
		CREATE INDEX "IX_MT_REPINT_DED"	ON mt_drv_sched_info
		(
		"DRV_EVENT_DB_ID",
		"DRV_EVENT_ID"
		)
	');
END;
/

--changeSet OPER-19537:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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

--changeSet OPER-19537:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment mt_core_flt_list_pkg body definition
create or replace PACKAGE mt_core_flt_list_pkg AS
/********************************************************************************
*
* Package:     mt_core_flt_list_pkg
*
* Description:  This package is used to build a log of events that need to be
*				processed for the datamart associated with this package. It defines
*				two tables to hold records. For simplicity and speed there are
*				only inserts or deletes.
*
*				action is a simple char that represents i: insert, u:update
*				d:delete statement
*********************************************************************************/
	TYPE gt_evt_rec IS RECORD (
								event_db_id      evt_sched_dead.event_db_id%TYPE,
								event_id         evt_sched_dead.event_id%TYPE,
								action           CHAR (1) --iud
                            );

	TYPE gt_evt_rec_tab IS TABLE OF gt_evt_rec;

	gtab_evt_ins                          gt_evt_rec_tab;
	gtab_evt_del                          gt_evt_rec_tab;

	gi_ins                                INTEGER := 1;
	gi_del                                INTEGER := 1;

	PROCEDURE   post_update (irec IN gt_evt_rec);
	PROCEDURE   stream_update;
    PROCEDURE   populate_data;

END mt_core_flt_list_pkg;
/


--changeSet OPER-19537:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment mt_core_flt_list_pkg body definition
create or replace PACKAGE BODY mt_core_flt_list_pkg AS
/********************************************************************************
*
* Procedure:    populate_data
*
* Arguments:	None - This procedure populates the associated datamart.
*
* Return: N/A
*
* Description:  This procedure does simple truncate and re-insert of data into the
*				datamart. Expected in cases where triggers are disabled for any
*				reason and need to repopulate with fresh data.
********************************************************************************/
    PROCEDURE populate_data IS
    BEGIN
        EXECUTE IMMEDIATE 'TRUNCATE TABLE mt_core_fleet_list';

    INSERT /*+ APPEND */ INTO mt_core_fleet_list
        (
			"EVENT_DB_ID"             ,
			"EVENT_ID"                ,
			"TASK_INV_NO_DB_ID"       ,
			"TASK_INV_NO_ID"          ,
			"REPL_SCHED_ID"           ,
			"BARCODE_SDESC"           ,
			"DEVIATION_QT"            ,
			"USAGE_REM_QT"            ,
			"SCHED_DEAD_DT"           ,
			"INVENTORY_KEY"           ,
			"INV_NO_SDESC"            ,
			"ASSMBL_CD"               ,
			"AUTHORITY_DB_ID"         ,
			"AUTHORITY_ID"            ,
			"DOMAIN_TYPE_CD"          ,
			"ENG_UNIT_CD"             ,
			"PRECISION_QT"            ,
			"SOFT_DEADLINE"           ,
			"PLAN_BY_DATE"            ,
			"PREVENT_EXE_BOOL"        ,
			"PREVENT_EXE_REVIEW_DT"   ,
			"AC_INV_NO_DB_ID"         ,
			"AC_INV_NO_ID"            ,
			"DEADLINE_EVENT_DB_ID"    ,
			"DEADLINE_EVENT_ID"       ,
			"SORT_DUE_DT"             ,
			"EXT_SCHED_DEAD_DT"
       )
        SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
            mt_drv_sched_info.event_db_id,
            mt_drv_sched_info.event_id,
            inv_inv.inv_no_db_id,
            inv_inv.inv_no_id,
            sched_stask.repl_sched_id,
            sched_stask.barcode_sdesc,
            mt_drv_sched_info.drv_deviation_qt,
            mt_drv_sched_info.drv_usage_rem_qt,
            mt_drv_sched_info.drv_sched_dead_dt,
            aircraft_inv_inv.inv_no_db_id || ':' ||
            aircraft_inv_inv.inv_no_id           AS inventory_key,
            aircraft_inv_inv.inv_no_sdesc,
            aircraft_inv_inv.assmbl_cd,
            aircraft_inv_inv.authority_db_id,
            aircraft_inv_inv.authority_id,
            mt_drv_sched_info.drv_domain_type_cd,
            mt_drv_sched_info.drv_eng_unit_cd,
            mt_drv_sched_info.drv_precision_qt   AS precision_qt,
            sched_stask.soft_deadline_bool       AS soft_deadline,
            sched_stask.plan_by_dt               AS plan_by_date,
            sched_stask_flags.prevent_exe_bool,
            sched_stask_flags.prevent_exe_review_dt,
            -- extra columns for join on main query
            aircraft_inv_inv.inv_no_db_id        AS ac_inv_no_db_id,
            aircraft_inv_inv.inv_no_id           AS ac_inv_no_id,
            mt_drv_sched_info.drv_event_db_id,
            mt_drv_sched_info.drv_event_id,
            mt_drv_sched_info.sort_due_dt,
            nvl(mt_drv_sched_info.drv_ext_dead_dt,mt_drv_sched_info.evt_ext_dead_dt) as ext_sched_dead_dt
        FROM
            mt_drv_sched_info
            INNER JOIN sched_stask
            ON sched_stask.sched_db_id       = mt_drv_sched_info.event_db_id AND
               sched_stask.sched_id          = mt_drv_sched_info.event_id
            INNER JOIN sched_stask_flags
            ON sched_stask_flags.sched_db_id = sched_stask.sched_db_id       AND
               sched_stask_flags.sched_id    = sched_stask.sched_id
            INNER JOIN inv_inv
            ON inv_inv.inv_no_db_id          = sched_stask.main_inv_no_db_id AND
               inv_inv.inv_no_id             = sched_stask.main_inv_no_id
            INNER JOIN inv_ac_reg
            ON inv_ac_reg.inv_no_db_id       = inv_inv.h_inv_no_db_id        AND
               inv_ac_reg.inv_no_id          = inv_inv.h_inv_no_id
            INNER JOIN inv_inv aircraft_inv_inv
            ON aircraft_inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id       AND
               aircraft_inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
        WHERE
            sched_stask.hist_bool_ro         = 0 AND
            NOT ( 
			    sched_stask.task_class_db_id = 0	AND
                sched_stask.task_class_cd 
				IN (
                    'CHECK',
                    'RO'
                   ) 
		    ) 
			AND
            aircraft_inv_inv.locked_bool     = 0 AND
            aircraft_inv_inv.inv_class_db_id = 0 AND
            aircraft_inv_inv.inv_class_cd    = 'ACFT'
		--reduce entropy in index clustering factor
		ORDER by sort_due_dt,barcode_sdesc
		;
        COMMIT;
    END populate_data;
/********************************************************************************
*
* Procedure:    reset_collections
* Arguments:	N/A
*
* Return: N/A
*
* Description:  Essentially garbage collection/cleanup procedure internal to pkg
********************************************************************************/

  PROCEDURE reset_collections AS
  BEGIN
       gtab_evt_ins.DELETE;
       gtab_evt_del.DELETE;
       gi_ins :=  1;
	   gi_del :=  1;

       RETURN;
  END;
/********************************************************************************
*
* Procedure:    post_update
* Arguments:	gt_evt_rec			iRec - The record of event to be processed.
*											Defined in package header.
*
*
* Return: N/A
*
*
* Description:  post_update is called after a ROW trigger is fired. It then builds
*				a log of the future operations to execute during streaming phase.
*				This allows any number of row operations to occur with a final
*				execution to occur in the stream_update.
********************************************************************************/
  PROCEDURE    post_update (irec IN gt_evt_rec) AS

	BEGIN
		IF (irec.action = 'I')
		THEN
			IF (NOT gtab_evt_ins.EXISTS (gi_ins))
			THEN
				gtab_evt_ins.extend ();
			END IF;

			gtab_evt_ins (gi_ins) := irec;
			gi_ins := gi_ins + 1;

			RETURN;
		END IF;

		IF (irec.action = 'D')
		THEN
			IF (NOT gtab_evt_del.EXISTS (gi_del))
			THEN
				gtab_evt_del.extend ();
			END IF;

			gtab_evt_del (gi_del) := irec;
			gi_del := gi_del + 1;

			RETURN;
		END IF;

		IF (irec.action = 'U')
		THEN
			IF (NOT gtab_evt_del.EXISTS (gi_del))
			THEN
				gtab_evt_del.extend ();
			END IF;

			gtab_evt_del (gi_del) := irec;
			gi_del := gi_del + 1;

			IF (NOT gtab_evt_ins.EXISTS (gi_ins))
			THEN
				gtab_evt_ins.extend ();
			END IF;

			gtab_evt_ins (gi_ins) := irec;
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
* Arguments:	None - This procedure processes N insert and delete records for
*						the associated datamart.
*
*
* Return: N/A
*
*
* Description:  stream_update is called after a TABLE trigger is fired. It then
*				processes the entries in the gtab_evt_ins, gtab_evt_del table
*				objects.
*
*				Finally it flushes the log of the events.
*
********************************************************************************/
	PROCEDURE    stream_update AS

		le_ex_dml_errors EXCEPTION;
		PRAGMA exception_init(le_ex_dml_errors, -24381);

		li_exi INTEGER;

	BEGIN
/********************************************************************************
*
*	Conduct DELETE operations
*
********************************************************************************/
        BEGIN
            FORALL  li_i IN 1..gi_del -1

            DELETE FROM mt_core_fleet_list
            WHERE mt_core_fleet_list.event_db_id = gtab_evt_del(li_i).event_db_id AND
                  mt_core_fleet_list.event_id    = gtab_evt_del(li_i).event_id
            ;

        EXCEPTION
            WHEN le_ex_dml_errors THEN
            FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
            LOOP
                IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                THEN
                    raise_application_error (
                                               SQL%bulk_exceptions(li_exi).error_code,
                                               sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                            );
                END IF;
            END LOOP;
        END;
/********************************************************************************
*
*	Conduct INSERT operations
*
********************************************************************************/
       BEGIN
        FORALL  li_i IN 1..gi_ins - 1  SAVE EXCEPTIONS
		INSERT INTO mt_core_fleet_list
        (
			"EVENT_DB_ID"             ,
			"EVENT_ID"                ,
			"TASK_INV_NO_DB_ID"       ,
			"TASK_INV_NO_ID"          ,
			"REPL_SCHED_ID"           ,
			"BARCODE_SDESC"           ,
			"DEVIATION_QT"            ,
			"USAGE_REM_QT"            ,
			"SCHED_DEAD_DT"           ,
			"INVENTORY_KEY"           ,
			"INV_NO_SDESC"            ,
			"ASSMBL_CD"               ,
			"AUTHORITY_DB_ID"         ,
			"AUTHORITY_ID"            ,
			"DOMAIN_TYPE_CD"          ,
			"ENG_UNIT_CD"             ,
			"PRECISION_QT"            ,
			"SOFT_DEADLINE"           ,
			"PLAN_BY_DATE"            ,
			"PREVENT_EXE_BOOL"        ,
			"PREVENT_EXE_REVIEW_DT"   ,
			"AC_INV_NO_DB_ID"         ,
			"AC_INV_NO_ID"            ,
			"DEADLINE_EVENT_DB_ID"    ,
			"DEADLINE_EVENT_ID"       ,
			"SORT_DUE_DT"             ,
			"EXT_SCHED_DEAD_DT"
       )
        SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
            mt_drv_sched_info.event_db_id,
            mt_drv_sched_info.event_id,
            inv_inv.inv_no_db_id,
            inv_inv.inv_no_id,
            sched_stask.repl_sched_id,
            sched_stask.barcode_sdesc,
            mt_drv_sched_info.drv_deviation_qt,
            mt_drv_sched_info.drv_usage_rem_qt,
            mt_drv_sched_info.drv_sched_dead_dt,
            aircraft_inv_inv.inv_no_db_id || ':' ||
            aircraft_inv_inv.inv_no_id           AS inventory_key,
            aircraft_inv_inv.inv_no_sdesc,
            aircraft_inv_inv.assmbl_cd,
            aircraft_inv_inv.authority_db_id,
            aircraft_inv_inv.authority_id,
            mt_drv_sched_info.drv_domain_type_cd,
            mt_drv_sched_info.drv_eng_unit_cd,
            mt_drv_sched_info.drv_precision_qt   AS precision_qt,
            sched_stask.soft_deadline_bool       AS soft_deadline,
            sched_stask.plan_by_dt               AS plan_by_date,
            sched_stask_flags.prevent_exe_bool,
            sched_stask_flags.prevent_exe_review_dt,
            -- extra columns for join on main query
            aircraft_inv_inv.inv_no_db_id        AS ac_inv_no_db_id,
            aircraft_inv_inv.inv_no_id           AS ac_inv_no_id,
            mt_drv_sched_info.drv_event_db_id,
            mt_drv_sched_info.drv_event_id,
            mt_drv_sched_info.sort_due_dt,
            nvl(mt_drv_sched_info.drv_ext_dead_dt,mt_drv_sched_info.evt_ext_dead_dt) as ext_sched_dead_dt
        FROM
            mt_drv_sched_info
            INNER JOIN sched_stask
            ON sched_stask.sched_db_id       = mt_drv_sched_info.event_db_id AND
               sched_stask.sched_id          = mt_drv_sched_info.event_id
            INNER JOIN sched_stask_flags
            ON sched_stask_flags.sched_db_id = sched_stask.sched_db_id       AND
               sched_stask_flags.sched_id    = sched_stask.sched_id
            INNER JOIN inv_inv
            ON inv_inv.inv_no_db_id          = sched_stask.main_inv_no_db_id AND
               inv_inv.inv_no_id             = sched_stask.main_inv_no_id
            INNER JOIN inv_ac_reg
            ON inv_ac_reg.inv_no_db_id       = inv_inv.h_inv_no_db_id        AND
               inv_ac_reg.inv_no_id          = inv_inv.h_inv_no_id
            INNER JOIN inv_inv aircraft_inv_inv
            ON aircraft_inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id       AND
               aircraft_inv_inv.inv_no_id    = inv_ac_reg.inv_no_id
        WHERE
            mt_drv_sched_info.event_db_id = gtab_evt_ins(li_i).event_db_id AND
            mt_drv_sched_info.event_id    = gtab_evt_ins(li_i).event_id    AND
            sched_stask.hist_bool_ro      = 0                              AND
            NOT ( 
			    sched_stask.task_class_db_id = 0	AND
                sched_stask.task_class_cd 
				IN (
                    'CHECK',
                    'RO'
                   ) 
		    ) 
			AND
            aircraft_inv_inv.locked_bool     = 0 AND
            aircraft_inv_inv.inv_class_db_id = 0 AND
            aircraft_inv_inv.inv_class_cd    = 'ACFT'
		;

        EXCEPTION
            WHEN le_ex_dml_errors THEN
                FOR li_exi IN 1..SQL%bulk_exceptions.COUNT
                LOOP
                    IF (SQL%bulk_exceptions(li_exi).error_code != 1)
                    THEN
                        raise_application_error (
                                                   SQL%bulk_exceptions(li_exi).error_code,
                                                   sqlerrm(-(SQL%bulk_exceptions(li_exi).error_code))
                                                );
                    END IF;
                END LOOP;
        END;

/****************************************************
*
*	Flush the log
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
*	Initialization of global record tab
*
*****************************************************/
BEGIN

  gtab_evt_ins  :=  gt_evt_rec_tab ();
  gtab_evt_del  :=  gt_evt_rec_tab ();


END mt_core_flt_list_pkg;
/

--changeSet OPER-19537:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on mt_drv_sched_info that will consume log of updates for MT_CORE_FLEET_LIST
CREATE OR REPLACE TRIGGER TIUDA_MT_CORE_FLEET_LIST
/********************************************************************************
*
* Trigger:    TIUDA_MT_CORE_FLEET_LIST
*
* Description:  This is a TABLE based trigger. After any IUD DML on the table
*				it will look into the mt_rep_int_evt_scded_pkg and stream_update
*				[0,N) records in the log objects for this purpose.
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON mt_drv_sched_info
BEGIN
       mt_core_flt_list_pkg.stream_update;
END;
/


--changeSet OPER-19537:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of trigger on mt_drv_sched_info that will create log of updates for MT_CORE_FLEET_LIST
CREATE OR REPLACE TRIGGER TIUDAR_MT_CORE_FLEET_LIST
/********************************************************************************
*
* Trigger:    TIUDAR_MT_CORE_FLEET_LIST
*
* Description:  This is a ROW based trigger. After any IUD DML on a row, a log
*				will be entered in the mt_rep_int_evt_scded_pkg table objects
*				for that purpose using the post_update procedure.
********************************************************************************/
AFTER DELETE OR INSERT OR UPDATE ON MT_DRV_SCHED_INFO
FOR EACH ROW

DECLARE
	lt_log_rec      mt_core_flt_list_pkg.gt_evt_rec;

BEGIN
	IF (inserting)
	THEN
		lt_log_rec.event_db_id := :NEW.event_db_id;
		lt_log_rec.event_id    := :NEW.event_id;
		lt_log_rec.action      := 'I';
		mt_core_flt_list_pkg.post_update (lt_log_rec);
	END IF;

	IF (updating)
	THEN
		lt_log_rec.event_db_id := :NEW.event_db_id;
		lt_log_rec.event_id    := :NEW.event_id;
		lt_log_rec.action      := 'U';
		mt_core_flt_list_pkg.post_update (lt_log_rec);
	END IF;

	IF (deleting)
	THEN
		lt_log_rec.event_db_id := :OLD.event_db_id;
		lt_log_rec.event_id    := :OLD.event_id;
		lt_log_rec.action      := 'D';
		mt_core_flt_list_pkg.post_update (lt_log_rec);
	END IF;
END;
/


--changeSet OPER-19537:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment to populate data in reasonable time, need stats on objects created earlier
BEGIN
    DBMS_STATS.GATHER_TABLE_STATS (
      ownname => USER,
      tabname => '"MT_DRV_SCHED_INFO"',
      estimate_percent => dbms_stats.auto_sample_size,
	  degree  => DBMS_STATS.DEFAULT_DEGREE,
      method_opt  => 'FOR ALL COLUMNS SIZE 1',
      no_invalidate  => false
    );
END;
/

--changeSet OPER-19537:14 stripComments:false
--comment alter mt_core_fleet_list PCTFREE
ALTER TABLE MT_CORE_FLEET_LIST PCTFREE 0;

--changeSet OPER-19537:15 stripComments:false
--comment alter mt_core_fleet_list PCTUSED
ALTER TABLE MT_CORE_FLEET_LIST PCTUSED 99;

--changeSet OPER-19537:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment fill mt_core_fleet_list with data
BEGIN
	mt_core_flt_list_pkg.populate_data;
END;
/




