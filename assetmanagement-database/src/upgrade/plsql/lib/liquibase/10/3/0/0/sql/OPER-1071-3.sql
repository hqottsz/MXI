--liquibase formatted sql

--changeSet OPER-1071-3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment creation of MT_CORE_FLEET_LIST datamart table
BEGIN
   utl_migr_schema_pkg.table_create('
		CREATE TABLE "MT_CORE_FLEET_LIST" (
			"EVENT_DB_ID"             NUMBER(10,0),
			"EVENT_ID"                NUMBER(10,0),
			"TASK_INV_NO_DB_ID"       NUMBER(10,0),
			"TASK_INV_NO_ID"          NUMBER(10,0),
			"REPL_SCHED_ID"           NUMBER(10,0),
			"BARCODE_SDESC"           VARCHAR2(80),
			"DEVIATION_QT"            FLOAT,
			"USAGE_REM_QT"            FLOAT,
			"SCHED_DEAD_DT"           DATE,
			"INVENTORY_KEY"           VARCHAR2(400),
			"INV_NO_SDESC"            VARCHAR2(400),
			"ASSMBL_CD"               VARCHAR2(8),
			"AUTHORITY_DB_ID"         NUMBER(10,0),
			"AUTHORITY_ID"            NUMBER(10,0),
			"DOMAIN_TYPE_CD"          VARCHAR2(8),
			"ENG_UNIT_CD"             VARCHAR2(8),
			"ENG_UNIT_MULT_QT"        FLOAT,
			"PRECISION_QT"            FLOAT,
			"SOFT_DEADLINE"           NUMBER(1,0),
			"PLAN_BY_DATE"            DATE,
			"PREVENT_EXE_BOOL"        NUMBER(1,0),
			"PREVENT_EXE_REVIEW_DT"   DATE,
			"AC_INV_NO_DB_ID"         NUMBER(10,0),
			"AC_INV_NO_ID"            NUMBER(10,0),
			"DEADLINE_EVENT_DB_ID"    NUMBER(10,0),
			"DEADLINE_EVENT_ID"       NUMBER(10,0),
			"SORT_DUE_DT"             DATE,
			"EXT_SCHED_DEAD_DT"       DATE,
			"CONFIG_POS_SDESC"        VARCHAR2(4000)
		)
   ');
END;
/


--changeSet OPER-1071-3:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment  spec definition mt_core_flt_list_pkg
CREATE OR REPLACE PACKAGE mt_core_flt_list_pkg AS
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


--changeSet OPER-1071-3:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment mt_core_flt_list_pkg body definition
CREATE OR REPLACE PACKAGE BODY mt_core_flt_list_pkg AS
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
			"ENG_UNIT_MULT_QT"        ,
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
			mt_drv_sched_info.deviation_qt,
			mt_drv_sched_info.usage_rem_qt,
			mt_drv_sched_info.sched_dead_dt,
			aircraft_inv_inv.inv_no_db_id || ':' || aircraft_inv_inv.inv_no_id AS inventory_key,
			aircraft_inv_inv.inv_no_sdesc,
			aircraft_inv_inv.assmbl_cd,
			aircraft_inv_inv.authority_db_id,
			aircraft_inv_inv.authority_id,
			mt_drv_sched_info.domain_type_cd,
			mt_drv_sched_info.eng_unit_cd,
			mt_drv_sched_info.eng_unit_mult_qt AS eng_unit_mult_qt,
			mt_drv_sched_info.precision_qt AS precision_qt,
			sched_stask.soft_deadline_bool AS soft_deadline,
			sched_stask.plan_by_dt AS plan_by_date,
			sched_stask_flags.prevent_exe_bool,
			sched_stask_flags.prevent_exe_review_dt,
			-- extra columns for join on main query
			aircraft_inv_inv.inv_no_db_id AS ac_inv_no_db_id,
			aircraft_inv_inv.inv_no_id    AS ac_inv_no_id,
			mt_drv_sched_info.deadline_event_db_id,
            mt_drv_sched_info.deadline_event_id,
            CASE WHEN sched_stask.plan_by_dt IS NOT NULL
                THEN    sched_stask.plan_by_dt
            	ELSE    getextendeddeadlinedt(
						mt_drv_sched_info.deviation_qt,
						mt_drv_sched_info.sched_dead_dt,
						mt_drv_sched_info.domain_type_cd,
						mt_drv_sched_info.data_type_cd,
						mt_drv_sched_info.eng_unit_mult_qt
					)
            END AS sort_due_dt,
			getextendeddeadlinedt(
				mt_drv_sched_info.deviation_qt,
				mt_drv_sched_info.sched_dead_dt,
				mt_drv_sched_info.domain_type_cd,
				mt_drv_sched_info.data_type_cd,
				mt_drv_sched_info.eng_unit_mult_qt
			) AS ext_sched_dead_dt

		FROM mt_drv_sched_info

			INNER JOIN sched_stask ON
				sched_stask.sched_db_id = mt_drv_sched_info.event_db_id AND
				sched_stask.sched_id    = mt_drv_sched_info.event_id
			INNER JOIN sched_stask_flags ON
				sched_stask_flags.sched_db_id = sched_stask.sched_db_id AND
				sched_stask_flags.sched_id    = sched_stask.sched_id
			INNER JOIN inv_inv ON
				inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
				inv_inv.inv_no_id    = sched_stask.main_inv_no_id
			INNER JOIN inv_ac_reg ON
				inv_ac_reg.inv_no_db_id = inv_inv.h_inv_no_db_id AND
				inv_ac_reg.inv_no_id    = inv_inv.h_inv_no_id
			INNER JOIN inv_inv aircraft_inv_inv ON
				aircraft_inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
				aircraft_inv_inv.inv_no_id    = inv_ac_reg.inv_no_id

		WHERE
			sched_stask.hist_bool_ro = 0 AND
			NOT (
			  sched_stask.task_class_db_id = 0 AND
			  sched_stask.task_class_cd IN ('CHECK', 'RO')
			)
			AND
			aircraft_inv_inv.locked_bool     = 0 AND
			aircraft_inv_inv.inv_class_db_id = 0 AND
			aircraft_inv_inv.inv_class_cd    ='ACFT'
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
        FORALL  li_i IN 1..gi_ins -1  SAVE EXCEPTIONS
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
			"ENG_UNIT_MULT_QT"        ,
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
			mt_drv_sched_info.deviation_qt,
			mt_drv_sched_info.usage_rem_qt,
			mt_drv_sched_info.sched_dead_dt,
			aircraft_inv_inv.inv_no_db_id || ':' || aircraft_inv_inv.inv_no_id AS inventory_key,
			aircraft_inv_inv.inv_no_sdesc,
			aircraft_inv_inv.assmbl_cd,
			aircraft_inv_inv.authority_db_id,
			aircraft_inv_inv.authority_id,
			mt_drv_sched_info.domain_type_cd,
			mt_drv_sched_info.eng_unit_cd,
			mt_drv_sched_info.eng_unit_mult_qt AS eng_unit_mult_qt,
			mt_drv_sched_info.precision_qt AS precision_qt,
			sched_stask.soft_deadline_bool AS soft_deadline,
			sched_stask.plan_by_dt AS plan_by_date,
			sched_stask_flags.prevent_exe_bool,
			sched_stask_flags.prevent_exe_review_dt,
			-- extra columns for join on main query
			aircraft_inv_inv.inv_no_db_id AS ac_inv_no_db_id,
			aircraft_inv_inv.inv_no_id    AS ac_inv_no_id,
			mt_drv_sched_info.deadline_event_db_id,
            mt_drv_sched_info.deadline_event_id,
            CASE WHEN sched_stask.plan_by_dt IS NOT NULL
                THEN    sched_stask.plan_by_dt
            	ELSE    getextendeddeadlinedt(
						mt_drv_sched_info.deviation_qt,
						mt_drv_sched_info.sched_dead_dt,
						mt_drv_sched_info.domain_type_cd,
						mt_drv_sched_info.data_type_cd,
						mt_drv_sched_info.eng_unit_mult_qt
					)
            END AS sort_due_dt,
			getextendeddeadlinedt(
				mt_drv_sched_info.deviation_qt,
				mt_drv_sched_info.sched_dead_dt,
				mt_drv_sched_info.domain_type_cd,
				mt_drv_sched_info.data_type_cd,
				mt_drv_sched_info.eng_unit_mult_qt
			) AS ext_sched_dead_dt

		FROM mt_drv_sched_info

			INNER JOIN sched_stask ON
				sched_stask.sched_db_id = mt_drv_sched_info.event_db_id AND
				sched_stask.sched_id    = mt_drv_sched_info.event_id
			INNER JOIN sched_stask_flags ON
				sched_stask_flags.sched_db_id = sched_stask.sched_db_id AND
				sched_stask_flags.sched_id    = sched_stask.sched_id
			INNER JOIN inv_inv ON
				inv_inv.inv_no_db_id = sched_stask.main_inv_no_db_id AND
				inv_inv.inv_no_id    = sched_stask.main_inv_no_id
			INNER JOIN inv_ac_reg ON
				inv_ac_reg.inv_no_db_id = inv_inv.h_inv_no_db_id AND
				inv_ac_reg.inv_no_id    = inv_inv.h_inv_no_id
			INNER JOIN inv_inv aircraft_inv_inv ON
				aircraft_inv_inv.inv_no_db_id = inv_ac_reg.inv_no_db_id AND
				aircraft_inv_inv.inv_no_id    = inv_ac_reg.inv_no_id

		WHERE
			mt_drv_sched_info.event_db_id = gtab_evt_ins(li_i).event_db_id AND
			mt_drv_sched_info.event_id    = gtab_evt_ins(li_i).event_id AND
			sched_stask.hist_bool_ro = 0 AND
			NOT (
			  sched_stask.task_class_db_id = 0 AND
			  sched_stask.task_class_cd IN ('CHECK', 'RO')
			)
			AND
			aircraft_inv_inv.locked_bool     = 0 AND
			aircraft_inv_inv.inv_class_db_id = 0 AND
			aircraft_inv_inv.inv_class_cd    ='ACFT'
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

  gtab_evt_ins     :=  gt_evt_rec_tab ();
  gtab_evt_del     :=  gt_evt_rec_tab ();


END mt_core_flt_list_pkg;
/



--changeSet OPER-1071-3:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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


--changeSet OPER-1071-3:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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


--changeSet OPER-1071-3:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment to populate data in reasonable time, need stats on objects created earlier
BEGIN
    DBMS_STATS.GATHER_TABLE_STATS (
      ownname => USER,
      tabname => '"MT_DRV_SCHED_INFO"',
      estimate_percent => 100,
      method_opt  => 'FOR ALL COLUMNS SIZE 1',
      no_invalidate  => false
    );
END;
/


--changeSet OPER-1071-3:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment fill mt_core_fleet_list with data
BEGIN
	mt_core_flt_list_pkg.populate_data;
END;
/

--changeSet OPER-1071-3:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary filter index to mt_core_fleet_list table
BEGIN
   utl_migr_schema_pkg.index_create('
	CREATE INDEX IX_MT_CORE_FLEET_SORT ON MT_CORE_FLEET_LIST
	(
		SORT_DUE_DT ASC ,
		BARCODE_SDESC ASC
	)
	');
END;
/


--changeSet OPER-1071-3:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment function for mt_core_fleet_list virtual column or any other caller
/********************************************************************************
*
* Function:    get_inv_repl_sdesc
*
* Arguments:   inv_no_db_id,inv_no_id of the inventory task is on.
*
* Return: VARCHAR2 That contains the configuration slot and position description
* 		  of the inventory the task is on. NULL is an acceptable and expected return value.
*
* 		  Example Return Values:
* 				71-00-00-00 (LH) ->05-20
* 				71-00-00-00 (RH) ->05-20
* 				57-00
* 				34-53-02-06 (1)
* 				34-53-02-06 (2)
* 				NULL
*
* Description: Provides the return data for the virtual column CONFIG_POS_SDESC.
*			   Used to provide accurate real-time data of tasks location on aircraft
********************************************************************************/
CREATE OR REPLACE FUNCTION get_inv_repl_sdesc
(
    iInv_no_db_id  IN NUMBER,
    iInv_no_id     IN NUMBER,
    iSched_db_id   IN NUMBER,
    iSched_id      IN NUMBER,
    iRepl_sched_id IN NUMBER
) RETURN VARCHAR2 DETERMINISTIC
IS
	lv2_sdesc   VARCHAR2 (4000);

	BEGIN
		--Start with most likely case
		IF(iRepl_sched_id IS NULL)
        THEN
        SELECT
         inv_desc_pkg.BuildInvConfigPosDesc
            (
            -- Inventory information
            av_InvClassCd    => inv_inv.inv_class_cd,
            an_InvNoDbId     => inv_inv.inv_no_db_id,
            an_InvNoId       => inv_inv.inv_no_id,
            an_NhInvNoDbId   => inv_inv.nh_inv_no_db_id,
            an_AssyInvNoDbId => inv_inv.assmbl_inv_no_db_id,
            an_AssyInvNoId   => inv_inv.assmbl_inv_no_id,
            an_HInvNoDbId    => inv_inv.h_inv_no_db_id,
            an_HInvNoId      => inv_inv.h_inv_no_id,
            -- Inventory slot and position info
            lv_InvBomCd      => eqp_assmbl_bom.assmbl_bom_cd,
            lv_InvPosCd      => eqp_assmbl_pos.eqp_pos_cd,
            ln_InvPosCt      => eqp_assmbl_bom.pos_ct,
            -- Assembly slot and position info
            lv_AssyBomCd     => assy_eqp_assmbl_bom.assmbl_bom_cd,
            lv_AssyPoscd     => assy_eqp_assmbl_pos.eqp_pos_cd,
            ln_AssyPosCt     => assy_eqp_assmbl_bom.pos_ct,
            -- Next highest position info
            ln_NhPosCt       => nh_assmbl_bom.pos_ct
            )
         AS config_desc INTO lv2_sdesc
        FROM
         inv_inv
         INNER JOIN eqp_assmbl_bom ON
            eqp_assmbl_bom.assmbl_db_id   = inv_inv.assmbl_db_id AND
            eqp_assmbl_bom.assmbl_cd      = inv_inv.assmbl_cd AND
            eqp_assmbl_bom.assmbl_bom_id  = inv_inv.assmbl_bom_id
         INNER JOIN eqp_assmbl_pos ON
            eqp_assmbl_pos.assmbl_db_id   = inv_inv.assmbl_db_id AND
            eqp_assmbl_pos.assmbl_cd      = inv_inv.assmbl_cd AND
            eqp_assmbl_pos.assmbl_bom_id  = inv_inv.assmbl_bom_id AND
            eqp_assmbl_pos.assmbl_pos_id  = inv_inv.assmbl_pos_id
         LEFT OUTER JOIN eqp_assmbl_bom nh_assmbl_bom ON
            nh_assmbl_bom.assmbl_db_id   = eqp_assmbl_bom.nh_assmbl_db_id AND
            nh_assmbl_bom.assmbl_cd      = eqp_assmbl_bom.nh_assmbl_cd AND
            nh_assmbl_bom.assmbl_bom_id  = eqp_assmbl_bom.nh_assmbl_bom_id
         LEFT OUTER JOIN inv_inv assy_inv_inv ON
            assy_inv_inv.inv_no_db_id    = inv_inv.assmbl_inv_no_db_id AND
            assy_inv_inv.inv_no_id       = inv_inv.assmbl_inv_no_id
         LEFT OUTER JOIN eqp_assmbl_bom assy_eqp_assmbl_bom ON
            assy_eqp_assmbl_bom.assmbl_db_id    = assy_inv_inv.assmbl_db_id AND
            assy_eqp_assmbl_bom.assmbl_cd       = assy_inv_inv.assmbl_cd AND
            assy_eqp_assmbl_bom.assmbl_bom_id   = assy_inv_inv.assmbl_bom_id
         LEFT OUTER JOIN eqp_assmbl_pos assy_eqp_assmbl_pos ON
            assy_eqp_assmbl_pos.assmbl_db_id    = assy_inv_inv.assmbl_db_id AND
            assy_eqp_assmbl_pos.assmbl_cd       = assy_inv_inv.assmbl_cd AND
            assy_eqp_assmbl_pos.assmbl_bom_id   = assy_inv_inv.assmbl_bom_id AND
            assy_eqp_assmbl_pos.assmbl_pos_id   = assy_inv_inv.assmbl_pos_id
        WHERE
            inv_inv.inv_no_db_id = iInv_no_db_id AND
            inv_inv.inv_no_id    = iInv_no_id
         ;
         RETURN (lv2_sdesc);
        END IF;

        -- We have a REPL Task and display is different
        IF(iRepl_sched_id IS NOT NULL)
        THEN
        SELECT
            CASE
                 WHEN nh_slot.bom_class_cd != 'SUBASSY' THEN NULL
                 ELSE nh_slot.assmbl_bom_cd
                    ||
                        CASE
                            WHEN ( nh_slot.pos_ct > 1 )
                                 OR ( nh_pos.eqp_pos_cd != '1' ) THEN ' ('
                            || nh_pos.eqp_pos_cd
                            || ') '
                            ELSE ''
                        END
                    || ' ->'
                END
            || repl_slot.assmbl_bom_cd
            ||
                CASE
                    WHEN ( repl_slot.pos_ct > 1 )
                         OR ( repl_pos.eqp_pos_cd != '1' ) THEN ' ('
                    || repl_pos.eqp_pos_cd
                    || ') '
                    ELSE ''
                END
            AS config_pos_sdesc INTO lv2_sdesc
        FROM
            sched_stask
            INNER JOIN sched_part ON
               sched_part.sched_db_id   = sched_stask.repl_sched_db_id   AND
               sched_part.sched_id      = sched_stask.repl_sched_id      AND
               sched_part.sched_part_id = sched_stask.repl_sched_part_id
            INNER JOIN sched_rmvd_part ON
               sched_rmvd_part.sched_db_id        = sched_part.sched_db_id   AND
               sched_rmvd_part.sched_id           = sched_part.sched_id      AND
               sched_rmvd_part.sched_part_id      = sched_part.sched_part_id AND
               sched_rmvd_part.sched_rmvd_part_id = 1
            INNER JOIN eqp_assmbl_bom repl_slot ON
               repl_slot.assmbl_db_id  = sched_part.assmbl_db_id  AND
               repl_slot.assmbl_cd     = sched_part.assmbl_cd     AND
               repl_slot.assmbl_bom_id = sched_part.assmbl_bom_id
            INNER JOIN eqp_assmbl_pos repl_pos ON
               repl_pos.assmbl_db_id   = sched_part.assmbl_db_id  AND
               repl_pos.assmbl_cd      = sched_part.assmbl_cd     AND
               repl_pos.assmbl_bom_id  = sched_part.assmbl_bom_id AND
               repl_pos.assmbl_pos_id  = sched_part.assmbl_pos_id
            INNER JOIN eqp_assmbl_bom nh_slot ON
               nh_slot.assmbl_db_id    = sched_part.nh_assmbl_db_id AND
               nh_slot.assmbl_cd       = sched_part.nh_assmbl_cd    AND
               nh_slot.assmbl_bom_id   = sched_part.nh_assmbl_bom_id
            INNER JOIN eqp_assmbl_pos nh_pos ON
               nh_pos.assmbl_db_id     = sched_part.nh_assmbl_db_id  AND
               nh_pos.assmbl_cd        = sched_part.nh_assmbl_cd     AND
               nh_pos.assmbl_bom_id    = sched_part.nh_assmbl_bom_id AND
               nh_pos.assmbl_pos_id    = sched_part.nh_assmbl_pos_id
         WHERE
               sched_stask.sched_db_id   = iSched_db_id AND
               sched_stask.sched_id      = iSched_id
            ;
          RETURN (lv2_sdesc);
        END IF;

    RETURN (lv2_sdesc);

	EXCEPTION
	       WHEN no_data_found THEN
	            RETURN (NULL);
	       WHEN too_many_rows THEN
	            RETURN (NULL);
	       WHEN OTHERS THEN
	            RAISE;

END get_inv_repl_sdesc;
/


--changeSet OPER-1071-3:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add delete index to mt_core_fleet_list table
BEGIN
   utl_migr_schema_pkg.index_create('
	CREATE INDEX IX_MT_CORE_FLEET_EVTEVENTID ON MT_CORE_FLEET_LIST
	(
		EVENT_DB_ID ASC ,
		EVENT_ID ASC
	)
	');
END;
/
