--liquibase formatted sql

--changeSet OPER-19537:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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


--changeSet OPER-19537:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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


--changeSet OPER-19537:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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


--changeSet OPER-19537:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment fill mt_core_fleet_list with data
BEGIN
	mt_core_flt_list_pkg.populate_data;
END;
/

