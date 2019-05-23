--liquibase formatted sql

--changeSet OPER-30726:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('UsageDeltaTable');
END;
/

--changeSet OPER-30726:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE UsageDeltaTuple
AS
  OBJECT
  (
    element1 RAW(16) ,
    element2 FLOAT ) NOT FINAL ;
/

--changeSet OPER-30726:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE UsageDeltaTable
IS
  TABLE OF UsageDeltaTuple ;
/

--changeSet OPER-30726:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY FLIGHT_OUT_OF_SEQUENCE_PKG IS

   c_pkg_name CONSTANT VARCHAR2(30) := 'FLIGHT_OUT_OF_SEQUENCE_PKG';
   SYS_NOTES_MAX_LENGTH CONSTANT INTEGER := 4000;

   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);

   le_ex_dml_errors EXCEPTION;
   PRAGMA exception_init(le_ex_dml_errors, -24381);
   li_exi INTEGER;

FUNCTION get_adjusted_usage_delta(
   airaw_usage_adjustment_id   IN RAW,
   aittb_assy_usage_deltadata  IN INTINTINTINTFLOATTABLE
) RETURN INTINTINTINTFLOATTABLE
IS
   v_method_name VARCHAR2(30) := 'get_adjusted_usage_delta';

   v_ttb_adjusted_usage_delta INTINTINTINTFLOATTABLE;

BEGIN

   v_step := 10;

   SELECT
      INTINTINTINTFLOATTUPLE(
         usg_usage_data.inv_no_db_id,
         usg_usage_data.inv_no_id,
         usg_usage_data.data_type_db_id,
         usg_usage_data.data_type_id,
         new_usage_delta.new_delta - usg_usage_data.tsn_delta_qt
      )
   BULK COLLECT INTO v_ttb_adjusted_usage_delta
   FROM
      (SELECT
         element1 as assmbl_inv_db_id,
         element2 as assmbl_inv_id,
         element3 as data_type_db_id,
         element4 as data_type_id,
         element5 as new_delta
       FROM
       TABLE (aittb_assy_usage_deltadata)
      ) new_usage_delta
      INNER JOIN usg_usage_data ON
         usg_usage_data.inv_no_db_id = new_usage_delta.assmbl_inv_db_id AND
         usg_usage_data.inv_no_id    = new_usage_delta.assmbl_inv_id
         AND
         usg_usage_data.data_type_db_id = new_usage_delta.data_type_db_id AND
         usg_usage_data.data_type_id    = new_usage_delta.data_type_id
      LEFT JOIN mim_calc ON
         mim_calc.data_type_db_id = usg_usage_data.data_type_db_id AND
         mim_calc.data_type_id    = usg_usage_data.data_type_id
   WHERE
      mim_calc.calc_id is null
      AND
      usg_usage_data.usage_record_id = airaw_usage_adjustment_id
      AND
      usg_usage_data.tsn_delta_qt != new_usage_delta.new_delta;

   RETURN v_ttb_adjusted_usage_delta;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END get_adjusted_usage_delta;


FUNCTION get_acft_hist_config(
   airaw_usage_adjustment_id IN RAW
) RETURN INTINTINTINTTABLE
IS
   v_method_name VARCHAR2(30) := 'get_acft_hist_config';

   v_ttb_inv_assmbl INTINTINTINTTABLE;
   v_ttb_assmbl INTINTTABLE;

   vi_acft_inv_db_id INTEGER;
   vi_acft_inv_id INTEGER;
   vi_usage_dt DATE;

BEGIN

   v_step := 10;

   SELECT
      inv_no_db_id, inv_no_id, usage_dt
   INTO
      vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt
   FROM
      usg_usage_record
   WHERE
      usage_record_id = airaw_usage_adjustment_id;

   v_step := 20;

   SELECT
      INTINTTUPLE(inv_no_db_id, inv_no_id)
   BULK COLLECT INTO
      v_ttb_assmbl
   FROM (
      SELECT DISTINCT
         inv_no_db_id, inv_no_id
      FROM
         usg_usage_data
      WHERE
         usage_record_id = airaw_usage_adjustment_id
   );

   v_step := 30;

   SELECT
      INTINTINTINTTUPLE(
         inv_hist.inv_no_db_id,
         inv_hist.inv_no_id,
         -- if inv_no_db_id/inv_no_id = element1/2, it is an assy itself
         -- if assmbl_inv_no_db_id/id is null, using acft instead
         CASE WHEN inv_hist.inv_no_db_id = usage_assmbl.assmbl_inv_db_id AND
                   inv_hist.inv_no_id    = usage_assmbl.assmbl_inv_id
              THEN inv_hist.inv_no_db_id
              ELSE NVL(inv_hist.assmbl_inv_no_db_id, vi_acft_inv_db_id)
         END,
         CASE WHEN inv_hist.inv_no_db_id = usage_assmbl.assmbl_inv_db_id AND
                   inv_hist.inv_no_id    = usage_assmbl.assmbl_inv_id
              THEN inv_hist.inv_no_id
              ELSE NVL(inv_hist.assmbl_inv_no_id, vi_acft_inv_id)
         END
      )
   BULK COLLECT INTO v_ttb_inv_assmbl
   FROM
      (
      SELECT
         ROW_NUMBER() OVER (PARTITION BY inv_no_db_id, inv_no_id, nh_inv_no_db_id, nh_inv_no_id, assmbl_inv_no_db_id, assmbl_inv_no_id  ORDER BY 1) AS rn,
         all_rec.*
      FROM
      (
         -- current
         SELECT
            inv_inv.inv_no_db_id,        inv_inv.inv_no_id,
            inv_inv.nh_inv_no_db_id,     inv_inv.nh_inv_no_id,
            inv_inv.assmbl_inv_no_db_id, inv_inv.assmbl_inv_no_id
         FROM inv_inv
         WHERE
            inv_inv.h_inv_no_db_id = vi_acft_inv_db_id AND
            inv_inv.h_inv_no_id    = vi_acft_inv_id
         UNION ALL
         -- removed
         SELECT
            inv_remove.inv_no_db_id,        inv_remove.inv_no_id,
            inv_remove.nh_inv_no_db_id,     inv_remove.nh_inv_no_id,
            inv_remove.assmbl_inv_no_db_id, inv_remove.assmbl_inv_no_id
         FROM inv_remove
         WHERE
            inv_remove.event_dt >= vi_usage_dt
            AND
            inv_remove.h_inv_no_db_id = vi_acft_inv_db_id AND
            inv_remove.h_inv_no_id    = vi_acft_inv_id

      ) all_rec
      MINUS
      -- installed
      SELECT
         ROW_NUMBER() OVER (PARTITION BY inv_install.inv_no_db_id, inv_install.inv_no_id, inv_install.nh_inv_no_db_id, inv_install.nh_inv_no_id, inv_install.assmbl_inv_no_db_id, inv_install.assmbl_inv_no_id  ORDER BY 1) AS rn,
         inv_install.inv_no_db_id,        inv_install.inv_no_id,
         inv_install.nh_inv_no_db_id,     inv_install.nh_inv_no_id,
         inv_install.assmbl_inv_no_db_id, inv_install.assmbl_inv_no_id
      FROM inv_install
      WHERE
         inv_install.event_dt >= vi_usage_dt
         AND
         inv_install.h_inv_no_db_id = vi_acft_inv_db_id AND
         inv_install.h_inv_no_id    = vi_acft_inv_id
      ) inv_hist
   LEFT JOIN
      (SELECT
         element1 as assmbl_inv_db_id,
         element2 as assmbl_inv_id
       FROM
          TABLE(v_ttb_assmbl)
      ) usage_assmbl
      ON
         usage_assmbl.assmbl_inv_db_id = inv_hist.inv_no_db_id AND
         usage_assmbl.assmbl_inv_id    = inv_hist.inv_no_id
   ;

   RETURN v_ttb_inv_assmbl;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END get_acft_hist_config;


FUNCTION getTrkSerUsagesByDataType(
   aittb_inv_data_type IN INTINTINTINTTABLE,
   aidt_date           IN DATE
) RETURN INTINTINTINTFLOATTABLE
IS
   v_ttb_usages  INTINTINTINTFLOATTABLE;

BEGIN
   WITH
   -- convert input to a table of inventory primary keys
   trk_ser_list AS
   (
      SELECT
         element1 as inv_no_db_id,
         element2 as inv_no_id,
         element3 AS data_type_db_id,
         element4 AS data_type_id
      FROM
      TABLE (aittb_inv_data_type)
   ),
   trk_ser_list_inv AS (
      SELECT DISTINCT
         element1 as inv_no_db_id,
         element2 as inv_no_id
      FROM
      TABLE (aittb_inv_data_type)
   ),
   -- Installed Timespans: consolidate inventory install-remove paired list for trk/ser
   inv_onoff_list AS
   (
      SELECT
         rmvl.inv_no_db_id,
         rmvl.inv_no_id,
         rmvl.assmbl_inv_no_db_id,
         rmvl.assmbl_inv_no_id,
         NVL((
            -- Find the matching install record for each removal
            SELECT
               MAX(inv_install.event_dt)
            FROM inv_install
            INNER JOIN inv_install main_inv_install ON
               main_inv_install.event_db_id   = inv_install.event_db_id AND
               main_inv_install.event_id      = inv_install.event_id
               AND
               main_inv_install.main_inv_bool = 1
            INNER JOIN inv_inv main_inv ON
               main_inv.inv_no_db_id          = main_inv_install.inv_no_db_id AND
               main_inv.inv_no_id             = main_inv_install.inv_no_id
            INNER JOIN inv_inv highest_inv ON
               highest_inv.inv_no_db_id       = inv_install.h_inv_no_db_id AND
               highest_inv.Inv_No_Id          = inv_install.h_inv_no_id
            WHERE
               inv_install.inv_no_id          = rmvl.inv_no_id AND
               inv_install.inv_no_db_id       = rmvl.inv_no_db_id
               AND
               inv_install.assmbl_inv_no_id    = rmvl.assmbl_inv_no_id AND
               inv_install.assmbl_inv_no_db_id = rmvl.assmbl_inv_no_db_id
               AND NOT (
                  main_inv.inv_class_db_Id    = 0 AND
                  main_inv.inv_class_cd       = 'ASSY'
               )
               AND NOT (
                  highest_inv.inv_class_db_id = 0 AND
                  highest_inv.inv_class_cd    = 'TRK'
               )
               AND
               rmvl.remove_date >= inv_install.event_dt
               AND
               inv_install.event_dt >= aidt_date
         ), aidt_date ) AS install_date,
         rmvl.remove_date
      FROM
         (
            SELECT
               inv_remove.inv_no_db_id,
               inv_remove.inv_no_id,
               inv_remove.nh_inv_no_db_id,
               inv_remove.nh_inv_no_id,
               inv_remove.assmbl_inv_no_db_id,
               inv_remove.assmbl_inv_no_id,
               inv_remove.h_inv_no_db_id,
               inv_remove.h_inv_no_id,
               inv_remove.event_dt as remove_date
            FROM trk_ser_list_inv
            INNER JOIN inv_remove ON
               inv_remove.inv_no_db_id = trk_ser_list_inv.inv_no_db_id AND
               inv_remove.inv_no_id    = trk_ser_list_inv.inv_no_id
            INNER JOIN inv_remove main_inv_remove ON
               main_inv_remove.event_db_id   = inv_remove.event_db_id AND
               main_inv_remove.event_id      = inv_remove.event_id
               AND
               main_inv_remove.main_inv_bool = 1
            INNER JOIN inv_inv main_inv ON
               main_inv.inv_no_db_id         = main_inv_remove.inv_no_db_id AND
               main_inv.inv_no_id            = main_inv_remove.inv_no_id
            INNER JOIN inv_inv highest_inv ON
               highest_inv.inv_no_db_id = inv_remove.h_inv_no_db_id AND
               highest_inv.Inv_No_Id    = inv_remove.h_inv_no_id
            WHERE
               -- Do not collect records where the sub-assembly is removed from an aircraft
               NOT (
                  main_inv.inv_class_db_Id = 0 AND
                  main_inv.inv_class_cd    = 'ASSY'
               )
               -- Do not collect records where this component is not being removed from an assembly
               AND NOT (
                  highest_inv.inv_class_db_id = 0 AND
                  highest_inv.inv_class_cd    = 'TRK'
               )
               AND
               inv_remove.event_dt > aidt_date
            -- Create a dummy record for today's date for all inventory fitted to an assembly
            -- This will create an Installed Timespan record for the current install location
            UNION ALL
            SELECT
               inv_inv.inv_no_db_id,
               inv_inv.inv_no_id,
               inv_inv.nh_inv_no_db_id,
               inv_inv.nh_inv_no_id,
               inv_inv.assmbl_inv_no_db_id,
               inv_inv.assmbl_inv_no_id,
               inv_inv.h_inv_no_db_id,
               inv_inv.h_inv_no_id,
               sysdate
            FROM trk_ser_list_inv
            JOIN inv_inv ON
               trk_ser_list_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
               trk_ser_list_inv.inv_no_id    = inv_inv.inv_no_id
            WHERE
               inv_inv.assmbl_inv_no_id IS NOT NULL
         ) rmvl
   ),
   -- fetch all usage changes applicable to our inventory
   inv_usage_delta AS
   (
      SELECT
         inv_no_db_id,
         inv_no_id,
         data_type_db_id,
         data_type_id,
         SUM (tsn_delta_qt) AS tsn_delta
      FROM (
         -- return all usage corrections on the inventory since our target date
         SELECT
            usg_usage_data.inv_no_db_id,
            usg_usage_data.inv_no_id,
            usg_usage_data.data_type_db_id,
            usg_usage_data.data_type_id,
            usg_usage_data.tsn_delta_qt
         FROM
         trk_ser_list
         INNER JOIN usg_usage_record ON
            usg_usage_record.inv_no_db_id = trk_ser_list.inv_no_db_id AND
            usg_usage_record.inv_no_id    = trk_ser_list.inv_no_id
         INNER JOIN usg_usage_data ON
            usg_usage_data.usage_record_id = usg_usage_record.usage_record_id
         WHERE
            usg_usage_data.data_type_db_id = trk_ser_list.data_type_db_id AND
            usg_usage_data.data_type_id    = trk_ser_list.data_type_id AND
            usg_usage_record.usage_dt      > aidt_date AND
            usg_usage_record.usage_type_cd = 'MXCORRECTION'
         -- return all usage accruals on the assembly for fitted time spans of inventory
         UNION ALL
         SELECT
            inv_onoff_list.inv_no_db_id,
            inv_onoff_list.inv_no_id,
            usg_usage_data.data_type_db_id,
            usg_usage_data.data_type_id,
            usg_usage_data.tsn_delta_qt
         FROM
         inv_onoff_list
         INNER JOIN usg_usage_data ON
            usg_usage_data.inv_no_db_id = inv_onoff_list.assmbl_inv_no_db_id AND
            usg_usage_data.inv_no_id    = inv_onoff_list.assmbl_inv_no_id
         INNER JOIN trk_ser_list ON
            trk_ser_list.data_type_db_id = usg_usage_data.data_type_db_id AND
            trk_ser_list.data_type_id    = usg_usage_data.data_type_id AND
            trk_ser_list.inv_no_db_id    = inv_onoff_list.inv_no_db_id AND
            trk_ser_list.inv_no_id       = inv_onoff_list.inv_no_id
         INNER JOIN usg_usage_record ON
            usg_usage_data.usage_record_id = usg_usage_record.usage_record_id
         WHERE
            usg_usage_record.usage_dt > inv_onoff_list.install_date AND
            usg_usage_record.usage_dt <= inv_onoff_list.remove_date
            AND
            usg_usage_record.usage_type_cd IN ('MXFLIGHT', 'MXACCRUAL')  -- flight or usage record
      )
      GROUP BY
         inv_no_db_id, inv_no_id, data_type_db_id, data_type_id
   )
   -- finally, we calculate historic tsn at that moment by subtracting our tallied deltas from current usage
   SELECT
      INTINTINTINTFLOATTUPLE(
         inv_curr_usage.inv_no_db_id,
         inv_curr_usage.inv_no_id,
         inv_curr_usage.data_type_db_id,
         inv_curr_usage.data_type_id,
         inv_curr_usage.tsn_qt - nvl(inv_usage_delta.tsn_delta, 0)
      )
   BULK COLLECT INTO v_ttb_usages
   FROM inv_curr_usage
   INNER JOIN trk_ser_list ON
      inv_curr_usage.inv_no_db_id    = trk_ser_list.inv_no_db_id       AND
      inv_curr_usage.inv_no_id       = trk_ser_list.inv_no_id
   LEFT JOIN inv_usage_delta ON
      inv_curr_usage.inv_no_db_id    = inv_usage_delta.inv_no_db_id    AND
      inv_curr_usage.inv_no_id       = inv_usage_delta.inv_no_id
      AND
      inv_curr_usage.data_type_db_id = inv_usage_delta.data_type_db_id AND
      inv_curr_usage.data_type_id    = inv_usage_delta.data_type_id;

   RETURN v_ttb_usages;

EXCEPTION
   WHEN OTHERS THEN
      application_object_pkg.SetMxiError('DEV-99999', 'USAGE_PKG@@@getTrkSerUsageOnDate@@@' || SQLERRM);

END getTrkSerUsagesByDataType;


PROCEDURE updateFollowingTaskDeadlines (
   airaw_usage_adjustment_id  IN RAW,
   aittb_adjusted_usage_delta IN INTINTINTINTFLOATTABLE,
   aittb_inv_assmbl           IN INTINTINTINTTABLE,
   ai_usage_dt                IN DATE,
   aiv_system_note            IN CLOB,
   aiv_deadline_system_note   IN VARCHAR2,
   ain_hr_db_id               IN NUMBER,
   ain_hr_id                  IN NUMBER,
   aottb_calc_deadline        OUT INTINTINTINTTABLE,
   aottb_deadlines            OUT INTINTINTINTTABLE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingTaskDeadlines';

   v_ttb_inv_data_types  INTINTINTINTTABLE;
   v_ttb_cursor_filter   INTINTINTINTTABLE;
   v_ttb_trkser_hist_tsn INTINTINTINTFLOATTABLE;
   v_ttb_inv_calc        INTINTINTINTTABLE;
   v_ttb_hist_tsn        INTINTINTINTFLOATTABLE;
   v_ttb_inv_usage       INTINTINTINTFLOATTABLE;
   v_ttb_filtered_inv    INTINTINTINTTABLE;
   v_ttb_calc_deadline   INTINTINTINTTABLE := INTINTINTINTTABLE();
   v_rec_calc_deadline   INTINTINTINTTUPLE;

   CURSOR lcur_snapshot IS
   SELECT
         evt_inv.event_db_id,
         evt_inv.event_id,
         evt_event.event_status_db_id,
         evt_event.event_status_cd,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta,
         ain_hr_db_id AS hr_db_id,
         ain_hr_id AS hr_id,
         evt_sched_dead.sched_from_cd,
         ROW_NUMBER() OVER (PARTITION BY evt_inv.event_id, evt_inv.event_db_id  ORDER BY evt_inv.event_id) AS rn
      FROM
         (SELECT
            element1 as assmbl_inv_db_id,
            element2 as assmbl_inv_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
      INNER JOIN
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as assmbl_inv_db_id,
               element4 as assmbl_inv_id
             FROM
             TABLE (v_ttb_filtered_inv)
         ) invlist
         ON
            invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
            invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      INNER JOIN usg_usage_data
         ON
            usg_usage_data.usage_record_id = airaw_usage_adjustment_id
            AND
            usg_usage_data.inv_no_db_id    = adjusted_usage_delta.assmbl_inv_db_id AND
            usg_usage_data.inv_no_id       = adjusted_usage_delta.assmbl_inv_id
            AND
            usg_usage_data.data_type_db_id = adjusted_usage_delta.data_type_db_id AND
            usg_usage_data.data_type_id    = adjusted_usage_delta.data_type_id
      LEFT JOIN
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as data_type_db_id,
               element4 as data_type_id,
               element5 as hist_tsn
            FROM TABLE (v_ttb_trkser_hist_tsn)
         ) trkser_tsn
         ON
            trkser_tsn.inv_no_db_id = invlist.inv_no_db_id AND
            trkser_tsn.inv_no_id    = invlist.inv_no_id
            AND
            adjusted_usage_delta.data_type_db_id = trkser_tsn.data_type_db_id AND
            adjusted_usage_delta.data_type_id    = trkser_tsn.data_type_id
      INNER JOIN evt_inv
         ON
            evt_inv.inv_no_db_id = invlist.inv_no_db_id AND
            evt_inv.inv_no_id    = invlist.inv_no_id
            AND
            evt_inv.main_inv_bool = 1
      INNER JOIN evt_event
         ON
            evt_inv.event_db_id = evt_event.event_db_id AND
            evt_inv.event_id    = evt_event.event_id
            AND
            evt_event.event_type_db_id = 0 AND
            evt_event.event_type_cd    = 'TS'
            AND
            evt_event.event_status_db_id = 0 AND
            evt_event.event_status_cd in ('ACTV', 'COMPLETE', 'IN WORK', 'PAUSE')
      INNER JOIN evt_sched_dead
         ON
            evt_sched_dead.event_id    = evt_event.event_id AND
            evt_sched_dead.event_db_id = evt_event.event_db_id
            AND
            evt_sched_dead.data_type_db_id = adjusted_usage_delta.data_type_db_id AND
            evt_sched_dead.data_type_id    = adjusted_usage_delta.data_type_id
            AND
            evt_sched_dead.sched_from_cd != 'BIRTH'
            AND
            evt_sched_dead.start_qt IS NOT NULL
      INNER JOIN mim_data_type
         ON
            mim_data_type.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_data_type.data_type_id    = evt_sched_dead.data_type_id
      LEFT JOIN evt_event_rel
         ON -- look for previous task
            evt_event_rel.rel_event_db_id = evt_event.event_db_id AND
            evt_event_rel.rel_event_id    = evt_event.event_id
            AND
            evt_event_rel.rel_type_db_id = 0 AND
            evt_event_rel.rel_type_cd    = 'DEPT'
      LEFT JOIN evt_event previous_task_event
         ON
            previous_task_event.event_db_id = evt_event_rel.event_db_id AND
            previous_task_event.event_id    = evt_event_rel.event_id
      LEFT JOIN sched_stask
         ON -- if the task for a fault
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
            AND
            sched_stask.fault_id IS NOT NULL
      LEFT JOIN evt_event fault_event
         ON -- look for fault found date
           sched_stask.fault_db_id = fault_event.event_db_id AND
           sched_stask.fault_id    = fault_event.event_id
      WHERE
         (
            round(evt_sched_dead.start_qt,mim_data_type.entry_prec_qt) > round(NVL(trkser_tsn.hist_tsn, usg_usage_data.tsn_qt),mim_data_type.entry_prec_qt)
            AND
            (
               evt_sched_dead.sched_from_cd != 'CUSTOM'
               OR
               (
                  -- for CUSTOM, only update when it is task of fault
                  evt_sched_dead.sched_from_cd = 'CUSTOM'
                  AND
                  sched_stask.sched_id IS NOT NULL
               )
            )
         )
         OR
         (
            round(evt_sched_dead.start_qt,mim_data_type.entry_prec_qt) = round(NVL(trkser_tsn.hist_tsn, usg_usage_data.tsn_qt),mim_data_type.entry_prec_qt)
            AND
            (
               (
                  evt_sched_dead.sched_from_cd = 'EFFECTIV'
                  AND
                  -- compare definition start date to usage date
                  evt_sched_dead.start_dt >= ai_usage_dt
               )
               OR
               (
                  evt_sched_dead.sched_from_cd IN ('WPSTART', 'WPEND', 'LASTEND', 'LASTDUE')
                  AND
                  -- compare previous task complete date to usage date
                  previous_task_event.event_dt >= ai_usage_dt
               )
               OR
               (
                  evt_sched_dead.sched_from_cd = 'CUSTOM'
                  AND
                  sched_stask.sched_id IS NOT NULL
                  AND
                  -- compare fault found date to usage date
                  fault_event.actual_start_dt >= ai_usage_dt
               )
            )
         )
      ;

      TYPE ltyp_snapshot IS TABLE OF lcur_snapshot%ROWTYPE;
      ltab_snapshots ltyp_snapshot;

      ltab_deadlines INTINTINTINTTABLE := INTINTINTINTTABLE();
      ltpl_deadline INTINTINTINTTUPLE;

      ln_calc_tsn NUMBER;

      sb CLOB; -- String buffer
      pos INTEGER;
      shift INTEGER;
BEGIN
   v_step := 10;
   -- The query in step 22 is very expensive when run against the entire aircraft tree.
   -- We are pre-filtering the list of inventory to reduce the scope
   SELECT INTINTINTINTTUPLE(
            main_inv_no_db_id,
            main_inv_no_id,
            data_type_db_id,
            data_type_id)
   BULK COLLECT INTO v_ttb_inv_data_types
   FROM (
      SELECT DISTINCT
         sched_stask.main_inv_no_db_id,
         sched_stask.main_inv_no_id,
         evt_sched_dead.data_type_db_id,
         evt_sched_dead.data_type_id
      FROM
      (SELECT
            element1 as assmbl_inv_db_id,
            element2 as assmbl_inv_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
      INNER JOIN
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as assmbl_inv_db_id,
               element4 as assmbl_inv_id
             FROM
             TABLE (aittb_inv_assmbl)
         ) invlist
         ON
            invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
            invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      INNER JOIN inv_inv
         ON invlist.inv_no_db_id = inv_inv.inv_no_db_id AND
            invlist.inv_no_id    = inv_inv.inv_no_id
            AND
            inv_inv.inv_class_db_id = 0 AND
            inv_inv.inv_class_cd IN ('TRK','SER')
      INNER JOIN sched_stask
         ON invlist.inv_no_db_id = sched_stask.main_inv_no_db_id AND
            invlist.inv_no_id    = sched_stask.main_inv_no_id
      INNER JOIN evt_sched_dead
         ON evt_sched_dead.event_db_id     = sched_stask.sched_db_id AND
            evt_sched_dead.event_id        = sched_stask.sched_id AND
            evt_sched_dead.data_type_db_id = adjusted_usage_delta.data_type_db_id AND
            evt_sched_dead.data_type_id    = adjusted_usage_delta.data_type_id
      INNER JOIN evt_event
         ON evt_event.event_db_id = sched_stask.sched_db_id AND
            evt_event.event_id    = sched_stask.sched_id
      WHERE
         (
            evt_event.hist_bool = 1 AND
            evt_event.event_gdt >= ai_usage_dt
         )
         OR
            evt_event.hist_bool = 0
   );

   v_step := 20;
   -- Find calc usages
   SELECT
      INTINTINTINTTUPLE(
         inv_no_db_id,
         inv_no_id,
         calc_db_id,
         calc_id
      )
   BULK COLLECT INTO v_ttb_inv_calc
   FROM
   (
      SELECT DISTINCT
         evt_inv.inv_no_db_id,
         evt_inv.inv_no_id,
         mim_calc.calc_db_id,
         mim_calc.calc_id
      FROM
      (SELECT
            element1 as assmbl_inv_db_id,
            element2 as assmbl_inv_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
      INNER JOIN
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as assmbl_inv_db_id,
               element4 as assmbl_inv_id
             FROM
             TABLE (aittb_inv_assmbl)
         ) invlist
         ON
            invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
            invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      INNER JOIN evt_inv
         ON
            evt_inv.inv_no_db_id = invlist.inv_no_db_id AND
            evt_inv.inv_no_id    = invlist.inv_no_id
            AND
            evt_inv.main_inv_bool = 1
      INNER JOIN evt_event
         ON
            evt_inv.event_db_id = evt_event.event_db_id AND
            evt_inv.event_id    = evt_event.event_id
            AND
            evt_event.event_type_db_id = 0 AND
            evt_event.event_type_cd    = 'TS'
            AND
            evt_event.event_status_db_id = 0 AND
            evt_event.event_status_cd in ('ACTV', 'COMPLETE', 'IN WORK', 'PAUSE')
      INNER JOIN evt_sched_dead
         ON
            evt_sched_dead.event_db_id = evt_inv.event_db_id AND
            evt_sched_dead.event_id    = evt_inv.event_id
            AND
            evt_sched_dead.sched_from_db_id = 0 AND
            evt_sched_dead.sched_from_cd IN ('LASTEND', 'LASTDUE', 'EFFECTIV')
      INNER JOIN mim_calc
         ON
           -- !Important, a data type has only ONE calc parm
            mim_calc.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_calc.data_type_id    = evt_sched_dead.data_type_id
      INNER JOIN mim_calc_input
         ON
            -- Only return calculated data types that are affected by the usage edit
            mim_calc_input.calc_db_id      = mim_calc.calc_db_id AND
            mim_calc_input.calc_id         = mim_calc.calc_id AND
            mim_calc_input.data_type_db_id = adjusted_usage_delta.data_type_db_id AND
            mim_calc_input.data_type_id    = adjusted_usage_delta.data_type_id
   );

   v_step := 21;
   -- Add any dependent parms for our calculated parameters to our pre-filter
   SELECT
      INTINTINTINTTUPLE(
         inv_no_db_id,
         inv_no_id,
         data_type_db_id,
         data_type_id
      )
   BULK COLLECT INTO v_ttb_cursor_filter
   FROM (
      SELECT
         element1 AS inv_no_db_id,
         element2 AS inv_no_id,
         element3 AS data_type_db_id,
         element4 AS data_type_id
      FROM TABLE (v_ttb_inv_data_types)
      UNION
      SELECT
         calcParms.element1 AS inv_no_db_id,
         calcParms.element2 AS inv_no_id,
         mim_calc_input.data_type_db_id,
         mim_calc_input.data_type_id
      FROM TABLE (v_ttb_inv_calc) calcParms
      INNER JOIN mim_calc_input ON
         mim_calc_input.calc_db_id = calcParms.element3 AND
         mim_calc_input.calc_id    = calcParms.element4
      WHERE
         mim_calc_input.data_type_db_id IS NOT NULL
   ) invParmList;

   v_step := 22;
   -- Get the TSN snapshots for only the TRK and SER inventory on the provided date.
   v_ttb_trkser_hist_tsn := getTrkSerUsagesByDataType(v_ttb_cursor_filter, ai_usage_dt);

   v_step := 25;
   -- Find inv historic tsn usages
   SELECT
      INTINTINTINTFLOATTUPLE(
         cal_invlist.inv_no_db_id,
         cal_invlist.inv_no_id,
         usg_usage_data.data_type_db_id,
         usg_usage_data.data_type_id,
         NVL(trkser_tsn.hist_tsn, usg_usage_data.tsn_qt)
      )
   BULK COLLECT INTO v_ttb_hist_tsn
   FROM
      (
         SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as assmbl_inv_db_id,
            element4 as assmbl_inv_id
          FROM
             TABLE (aittb_inv_assmbl)
      ) invlist
   INNER JOIN
      (
         SELECT DISTINCT
            element1 as inv_no_db_id,
            element2 as inv_no_id
          FROM
             TABLE (v_ttb_inv_calc)
      ) cal_invlist
      ON
         invlist.inv_no_db_id = cal_invlist.inv_no_db_id AND
         invlist.inv_no_id    = cal_invlist.inv_no_id
   INNER JOIN usg_usage_data
      ON
         usg_usage_data.usage_record_id = airaw_usage_adjustment_id
         AND
         usg_usage_data.inv_no_db_id    = invlist.assmbl_inv_db_id AND
         usg_usage_data.inv_no_id       = invlist.assmbl_inv_id
   LEFT JOIN
      (
         SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as hist_tsn
         FROM TABLE (v_ttb_trkser_hist_tsn)
      ) trkser_tsn
      ON
         trkser_tsn.inv_no_db_id = invlist.inv_no_db_id AND
         trkser_tsn.inv_no_id    = invlist.inv_no_id
         AND
         usg_usage_data.data_type_db_id = trkser_tsn.data_type_db_id AND
         usg_usage_data.data_type_id    = trkser_tsn.data_type_id
   ;

   v_step := 30;
   -- Find inv calc usages at usage_dt
   FOR lrec IN
   (
         SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as calc_db_id,
            element4 as calc_id
          FROM
             TABLE (v_ttb_inv_calc)
   )
   LOOP
      -- Get usages of the inv
      SELECT
         INTINTINTINTFLOATTUPLE(
            element1,
            element2,
            element3,
            element4,
            element5
          )
      BULK COLLECT INTO v_ttb_inv_usage
      FROM
         TABLE (v_ttb_hist_tsn)
      WHERE
         element1  = lrec.inv_no_db_id AND
         element2  = lrec.inv_no_id;

      -- Get inv calc tsn
      USAGE_PKG.calculate_calc_parm(
                          lrec.inv_no_db_id,
                          lrec.inv_no_id,
                          lrec.calc_db_id,
                          lrec.calc_id,
                          v_ttb_inv_usage,
                          ln_calc_tsn
                          );

       -- If deadline start after calc tsn, save in to-be-recalcute-list
       FOR lrec_deadline IN
       (
            SELECT
               evt_sched_dead.event_db_id,
               evt_sched_dead.event_id,
               evt_sched_dead.data_type_db_id,
               evt_sched_dead.data_type_id,
               evt_sched_dead.sched_from_cd
            FROM evt_sched_dead
            INNER JOIN evt_event
               ON
                  evt_sched_dead.event_db_id = evt_event.event_db_id AND
                  evt_sched_dead.event_id    = evt_event.event_id
                  AND
                  evt_event.event_type_db_id = 0 AND
                  evt_event.event_type_cd    = 'TS'
                  AND
                  evt_event.event_status_db_id = 0 AND
                  evt_event.event_status_cd in ('ACTV', 'COMPLETE', 'IN WORK', 'PAUSE')
            INNER JOIN evt_inv
               ON
                  evt_inv.event_db_id = evt_event.event_db_id AND
                  evt_inv.event_id    = evt_event.event_id
                  AND
                  evt_inv.inv_no_db_id = lrec.inv_no_db_id AND
                  evt_inv.inv_no_id    = lrec.inv_no_id
                  AND
                  evt_inv.main_inv_bool = 1
            INNER JOIN mim_calc
               ON
                  mim_calc.data_type_db_id = evt_sched_dead.data_type_db_id AND
                  mim_calc.data_type_id    = evt_sched_dead.data_type_id
                  AND
                  mim_calc.calc_db_id = lrec.calc_db_id AND
                  mim_calc.calc_id    = lrec.calc_id
            WHERE
                  evt_sched_dead.sched_from_db_id = 0 AND
                  evt_sched_dead.sched_from_cd IN ('LASTEND', 'LASTDUE', 'EFFECTIV')
                  AND
                  evt_sched_dead.start_qt >= ln_calc_tsn
       )
       LOOP

       -- pass back the calc deadlines keys for re-calculation
          v_rec_calc_deadline := INTINTINTINTTUPLE(
             lrec_deadline.event_db_id,
             lrec_deadline.event_id,
             lrec_deadline.data_type_db_id,
             lrec_deadline.data_type_id
          );
          v_ttb_calc_deadline.EXTEND;
          v_ttb_calc_deadline(v_ttb_calc_deadline.count) := v_rec_calc_deadline;

       -- pass back the updated deadlines keys for calc usage need check sched plan window
          IF (lrec_deadline.sched_from_cd = 'LASTEND') OR (lrec_deadline.sched_from_cd = 'LASTDUE') THEN
             ltpl_deadline := INTINTINTINTTUPLE(
                lrec_deadline.event_db_id,
                lrec_deadline.event_id,
                lrec_deadline.data_type_db_id,
                lrec_deadline.data_type_id
             );
             ltab_deadlines.EXTEND;
             ltab_deadlines(ltab_deadlines.count) := ltpl_deadline;
          END IF;
       END LOOP;

   END LOOP;
   aottb_calc_deadline := v_ttb_calc_deadline;

   v_step := 49;
   -- Filter out non-applicable TRK and SER inventory from aircraft tree prior to opening cursor
   SELECT
      INTINTINTINTTUPLE(
         inv_no_db_id,
         inv_no_id,
         assmbl_inv_no_db_id,
         assmbl_inv_no_id)
   BULK COLLECT INTO v_ttb_filtered_inv
   FROM (
      -- All SYS, ASSY, ACFT inventory
      SELECT
         sys_inv.element1 AS inv_no_db_id,
         sys_inv.element2 AS inv_no_id,
         sys_inv.element3 AS assmbl_inv_no_db_id,
         sys_inv.element4 AS assmbl_inv_no_id
      FROM (
         SELECT *
         FROM
            TABLE (aittb_inv_assmbl)
         ) sys_inv
         INNER JOIN inv_inv
            ON
               sys_inv.element1 = inv_inv.inv_no_db_id AND
               sys_inv.element2 = inv_inv.inv_no_id AND
               inv_inv.inv_class_cd NOT IN ('TRK','SER')
      UNION ALL
      -- Only TRK and SER from our pre-filtered list
      SELECT
         all_inv.element1 AS inv_no_db_id,
         all_inv.element2 AS inv_no_id,
         all_inv.element3 AS assmbl_inv_no_db_id,
         all_inv.element4 AS assmbl_inv_no_id
      FROM (
         SELECT *
         FROM
            TABLE(aittb_inv_assmbl)
         ) all_inv
      INNER JOIN (
         SELECT DISTINCT
            element1, element2
         FROM TABLE (v_ttb_cursor_filter)
      ) trkser_inv ON
         trkser_inv.element1 = all_inv.element1 AND
         trkser_inv.element2 = all_inv.element2
   );

   v_step := 50;
   OPEN lcur_snapshot;
   LOOP

      FETCH lcur_snapshot BULK COLLECT INTO ltab_snapshots LIMIT 10000;

      EXIT WHEN ltab_snapshots.COUNT = 0;

      --update usage
      v_step := 55;
      BEGIN
         FORALL i IN 1..ltab_snapshots.COUNT SAVE EXCEPTIONS
            UPDATE
               evt_sched_dead
            SET
               evt_sched_dead.start_qt      = evt_sched_dead.start_qt + ltab_snapshots(i).adjusted_delta,
               evt_sched_dead.sched_dead_qt = evt_sched_dead.sched_dead_qt + ltab_snapshots(i).adjusted_delta
            WHERE
               ltab_snapshots(i).event_db_id  = evt_sched_dead.event_db_id AND
               ltab_snapshots(i).event_id     = evt_sched_dead.event_id
               AND
               ltab_snapshots(i).data_type_db_id = evt_sched_dead.data_type_db_id AND
               ltab_snapshots(i).data_type_id    = evt_sched_dead.data_type_id;
      EXCEPTION
         WHEN le_ex_dml_errors THEN
            FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
               IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
                  raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                  v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
                  sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
            END IF;
         END LOOP;
      END;

      --insert system note
      v_step := 60;
      IF aiv_system_note IS NOT NULL THEN
         sb := aiv_deadline_system_note||aiv_system_note;
         WHILE length(sb) > 0 LOOP
            pos := length(sb);
            IF pos > SYS_NOTES_MAX_LENGTH THEN
               pos:=SYS_NOTES_MAX_LENGTH-1;
               shift := INSTR(substr(sb, 1,pos),'<br>',-1);
               IF shift <> 0 THEN
                  pos := shift-1;
               END IF;
            END IF;
            BEGIN
               FORALL i IN 1..ltab_snapshots.COUNT
                   INSERT
                   INTO
                      evt_stage (
                        event_db_id,
                        event_id,
                        stage_id,
                        event_status_db_id,
                        event_status_cd,
                        stage_dt,
                        stage_gdt,
                        hr_db_id,
                        hr_id,
                        stage_note,
                        system_bool
                      )
                   SELECT
                     ltab_snapshots(i).event_db_id,
                     ltab_snapshots(i).event_id,
                     evt_stage_id_seq.NEXTVAL AS stage_id,
                     ltab_snapshots(i).event_status_db_id,
                     ltab_snapshots(i).event_status_cd,
                     SYSDATE,
                     SYSDATE,
                     ltab_snapshots(i).hr_db_id,
                     ltab_snapshots(i).hr_id,
                     aiv_deadline_system_note||aiv_system_note,
                     1
                   FROM
                     dual
                   WHERE
                     --insert one record per eventid for affected deadlines
                     ltab_snapshots(i).rn = 1;
            EXCEPTION
               WHEN le_ex_dml_errors THEN
                  FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
                     IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
                        raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                        v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
                        sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
                  END IF;
               END LOOP;
            END;
            sb := SUBSTR(sb,pos+1,length(sb));
         END LOOP; 
      END IF;

      -- pass back the updated deadlines keys, based on usage delta, not include calc usage which is already added
      v_step := 65;
      FOR i IN 1..ltab_snapshots.COUNT
      LOOP
         IF (ltab_snapshots(i).sched_from_cd = 'LASTEND') OR (ltab_snapshots(i).sched_from_cd = 'LASTDUE') THEN
            ltpl_deadline := INTINTINTINTTUPLE(
               CAST(ltab_snapshots(i).event_db_id AS VARCHAR2),
               CAST(ltab_snapshots(i).event_id AS VARCHAR2),
               ltab_snapshots(i).data_type_db_id,
               ltab_snapshots(i).data_type_id
            );
            ltab_deadlines.EXTEND;
            ltab_deadlines(ltab_deadlines.count) := ltpl_deadline;
         END IF;
      END LOOP;
   END LOOP;
   CLOSE lcur_snapshot;

   aottb_deadlines := ltab_deadlines;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingTaskDeadlines;


PROCEDURE updateFollowingCalcDeadline (
   aittb_calc_deadline  IN INTINTINTINTTABLE
)
AS
   v_method_name VARCHAR2(30) := 'updateFollowingCalcDeadline';

   v_ttb_hist_tsn   INTINTINTINTFLOATTABLE;
   v_calc_tsn NUMBER;

   v_invalidDeadline NUMBER;
   v_prev_due_tsn    NUMBER;
   v_prev_end_tsn    NUMBER;

   CURSOR lcur_calc_deadline_in_order IS
      SELECT
         evt_sched_dead.event_db_id,
         evt_sched_dead.event_id,
         evt_sched_dead.data_type_db_id,
         evt_sched_dead.data_type_id,
         evt_sched_dead.sched_from_cd
      FROM
         (
            SELECT
               element1 as event_db_id,
               element2 as event_id,
               element3 as data_type_db_id,
               element4 as data_type_id
             FROM
             TABLE (aittb_calc_deadline)
         ) evt_calc
      INNER JOIN evt_sched_dead
         ON
            evt_sched_dead.event_db_id     = evt_calc.event_db_id AND
            evt_sched_dead.event_id        = evt_calc.event_id
            AND
            evt_sched_dead.data_type_db_id = evt_calc.data_type_db_id AND
            evt_sched_dead.data_type_id    = evt_calc.data_type_id
            AND
            evt_sched_dead.sched_from_db_id = 0 AND
            evt_sched_dead.sched_from_cd IN ('LASTEND', 'LASTDUE')
      INNER JOIN evt_event
         ON
            evt_event.event_db_id = evt_sched_dead.event_db_id AND
            evt_event.event_id    = evt_sched_dead.event_id
      ORDER BY
         -- order by completion date and if no completion date then by scheduled date
         NVL(evt_event.event_dt, evt_sched_dead.sched_dead_dt)
      ;

      TYPE ltyp_calc_deadline IS TABLE OF lcur_calc_deadline_in_order%ROWTYPE;
      ltab_calc_deadline ltyp_calc_deadline;

BEGIN

   v_step := 30;
   -- calculate usage at effective date for EFFECTIV
   -- EFFECTIVE shall be processed first, LASTDUE/LASTEND may depends on them as previous task
   FOR lrec IN
   (
         SELECT
            evt_sched_dead.event_db_id,
            evt_sched_dead.event_id,
            evt_sched_dead.data_type_db_id,
            evt_sched_dead.data_type_id,
            evt_inv.inv_no_db_id,
            evt_inv.inv_no_id,
            mim_calc.calc_db_id,
            mim_calc.calc_id,
            evt_sched_dead.start_dt
         FROM
            (
               SELECT
                  element1 as event_db_id,
                  element2 as event_id,
                  element3 as data_type_db_id,
                  element4 as data_type_id
                FROM
                TABLE (aittb_calc_deadline)
            ) evt_calc
         INNER JOIN evt_sched_dead
            ON
               evt_sched_dead.event_db_id = evt_calc.event_db_id AND
               evt_sched_dead.event_id    = evt_calc.event_id
               AND
               evt_sched_dead.data_type_db_id = evt_calc.data_type_db_id AND
               evt_sched_dead.data_type_id    = evt_calc.data_type_id
               AND
               evt_sched_dead.sched_from_db_id = 0 AND
               evt_sched_dead.sched_from_cd = 'EFFECTIV'
         INNER JOIN evt_inv
            ON
               evt_inv.event_db_id = evt_sched_dead.event_db_id AND
               evt_inv.event_id    = evt_sched_dead.event_id
               AND
               evt_inv.main_inv_bool = 1
         INNER JOIN mim_calc
            ON
              -- A data type has only ONE calc parm
               mim_calc.data_type_db_id = evt_sched_dead.data_type_db_id AND
               mim_calc.data_type_id    = evt_sched_dead.data_type_id
   )
   LOOP

      v_step := 35;
      -- calculate usage at the effective date
      v_ttb_hist_tsn := USAGE_PKG.getInvUsageOnDate(lrec.inv_no_db_id, lrec.inv_no_id, lrec.start_dt);

      -- Get inv calc tsn
      USAGE_PKG.calculate_calc_parm(
                          lrec.inv_no_db_id,
                          lrec.inv_no_id,
                          lrec.calc_db_id,
                          lrec.calc_id,
                          v_ttb_hist_tsn,
                          v_calc_tsn
                          );


      v_step := 40;
      -- update calc deadline start/end
      UPDATE
         evt_sched_dead
      SET
         evt_sched_dead.start_qt      = v_calc_tsn,
         evt_sched_dead.sched_dead_qt = v_calc_tsn + evt_sched_dead.interval_qt
      WHERE
         evt_sched_dead.event_db_id = lrec.event_db_id AND
         evt_sched_dead.event_id    = lrec.event_id
         AND
         evt_sched_dead.data_type_db_id = lrec.data_type_db_id AND
         evt_sched_dead.data_type_id    = lrec.data_type_id;

   END LOOP;

   v_step := 60;
   -- update start/end according to previous task for LASTEND, LASTDUE
   OPEN lcur_calc_deadline_in_order;
   LOOP

      FETCH lcur_calc_deadline_in_order BULK COLLECT INTO ltab_calc_deadline LIMIT 10000;

      EXIT WHEN ltab_calc_deadline.COUNT = 0;

      FOR i IN 1..ltab_calc_deadline.COUNT
      LOOP
         v_invalidDeadline := 0;
         BEGIN
            SELECT
               prev_sched_dead.sched_dead_qt,
               prev_inv_usage.tsn_qt
            INTO
               v_prev_due_tsn,
               v_prev_end_tsn
            FROM
               evt_event_rel
            INNER JOIN evt_sched_dead prev_sched_dead
               ON
                  prev_sched_dead.event_db_id = evt_event_rel.event_db_id AND
                  prev_sched_dead.event_id    = evt_event_rel.event_id
                  AND
                  prev_sched_dead.data_type_db_id = ltab_calc_deadline(i).data_type_db_id AND
                  prev_sched_dead.data_type_id    = ltab_calc_deadline(i).data_type_id
            INNER JOIN evt_inv prev_evt_inv
               ON
                  prev_evt_inv.event_db_id   = prev_sched_dead.event_db_id AND
                  prev_evt_inv.event_id      = prev_sched_dead.event_id
                  AND
                  prev_evt_inv.main_inv_bool = 1
            INNER JOIN evt_inv_usage prev_inv_usage
               ON
                  prev_inv_usage.event_db_id  = prev_evt_inv.event_db_id AND
                  prev_inv_usage.event_id     = prev_evt_inv.event_id AND
                  prev_inv_usage.event_inv_id = prev_evt_inv.event_inv_id
                  AND
                  prev_inv_usage.data_type_db_id = prev_sched_dead.data_type_db_id AND
                  prev_inv_usage.data_type_id    = prev_sched_dead.data_type_id
            WHERE
                  evt_event_rel.rel_event_db_id = ltab_calc_deadline(i).event_db_id AND
                  evt_event_rel.rel_event_id    = ltab_calc_deadline(i).event_id
                  AND
                  evt_event_rel.rel_type_db_id  = 0 AND
                  evt_event_rel.rel_type_cd     = 'DEPT';
         EXCEPTION
            WHEN NO_DATA_FOUND THEN
               -- If select returns no rows, it is a data error, we ignore this deadline.
               v_invalidDeadline := 1;
         END;

         IF v_invalidDeadline = 0 THEN
            UPDATE
               evt_sched_dead
            SET
               evt_sched_dead.start_qt      = DECODE(ltab_calc_deadline(i).sched_from_cd, 'LASTEND', v_prev_end_tsn, v_prev_due_tsn),
               evt_sched_dead.sched_dead_qt = evt_sched_dead.interval_qt + DECODE(ltab_calc_deadline(i).sched_from_cd, 'LASTEND', v_prev_end_tsn, v_prev_due_tsn)
            WHERE
               evt_sched_dead.event_db_id = ltab_calc_deadline(i).event_db_id AND
               evt_sched_dead.event_id    = ltab_calc_deadline(i).event_id
               AND
               evt_sched_dead.data_type_db_id = ltab_calc_deadline(i).data_type_db_id  AND
               evt_sched_dead.data_type_id    = ltab_calc_deadline(i).data_type_id;
         END IF;
      END LOOP;

   END LOOP;
   CLOSE lcur_calc_deadline_in_order;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', ERROR: ' || SQLERRM,
                         1,
                         2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingCalcDeadline;


PROCEDURE updateFollowingEventTsn(
   aittb_adjusted_usage_delta IN INTINTINTINTFLOATTABLE,
   aittb_inv_assmbl IN INTINTINTINTTABLE,
   ai_usage_dt      IN DATE,
   aiv_system_note  IN VARCHAR2,
   aiv_task_wrkpkg_system_note IN VARCHAR2,
   aiv_fault_system_note       IN VARCHAR2,
   ain_hr_db_id                IN NUMBER,
   ain_hr_id                   IN NUMBER
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingEventTsn';

   CURSOR lcur_snapshot IS
   SELECT
      evt_inv.event_db_id,
      evt_inv.event_id,
      evt_inv.event_inv_id,
      evt_event.event_status_db_id,
      evt_event.event_status_cd,
      evt_event.event_type_cd,
      adjusted_usage_delta.data_type_db_id,
      adjusted_usage_delta.data_type_id,
      adjusted_usage_delta.adjusted_delta,
      NVL(ain_hr_db_id, NULL) AS hr_db_id,
      NVL(ain_hr_id, NULL) AS hr_id,
      CASE
         --for fault found
         WHEN (evt_event.event_type_db_id = 0 AND
               evt_event.event_type_cd = 'CF') THEN
                  aiv_fault_system_note
         --for fault/task/work package
         ELSE aiv_task_wrkpkg_system_note
      END AS system_note,
      ROW_NUMBER() OVER (PARTITION BY evt_inv.event_id, evt_inv.event_db_id  ORDER BY evt_inv.event_id) AS rn
   FROM
      (SELECT
         element1 as assmbl_inv_db_id,
         element2 as assmbl_inv_id,
         element3 as data_type_db_id,
         element4 as data_type_id,
         element5 as adjusted_delta
       FROM
          TABLE (aittb_adjusted_usage_delta)
      ) adjusted_usage_delta
   INNER JOIN
      (
         SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as assmbl_inv_db_id,
            element4 as assmbl_inv_id
          FROM
             TABLE (aittb_inv_assmbl)
      ) invlist
      ON
         invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
         invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
   INNER JOIN evt_inv
      ON
         evt_inv.inv_no_db_id = invlist.inv_no_db_id AND
         evt_inv.inv_no_id    = invlist.inv_no_id
   INNER JOIN evt_event
      ON
         evt_inv.event_db_id = evt_event.event_db_id AND
         evt_inv.event_id    = evt_event.event_id
   INNER JOIN evt_inv_usage
      ON
         evt_inv.event_db_id  = evt_inv_usage.event_db_id AND
         evt_inv.event_id     = evt_inv_usage.event_id    AND
         evt_inv.event_inv_id = evt_inv_usage.event_inv_id
         AND
         adjusted_usage_delta.data_type_db_id = evt_inv_usage.data_type_db_id AND
         adjusted_usage_delta.data_type_id    = evt_inv_usage.data_type_id
   LEFT JOIN sched_stask
      ON
         sched_stask.sched_db_id = evt_event.event_db_id AND
         sched_stask.sched_id    = evt_event.event_id
   WHERE
      -- looking for fault after found date (evt_event.actual_start_dt)
   (
     (  evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd = 'CF'
         AND
         evt_event.actual_start_dt >= ai_usage_dt
      )
      OR
      -- looking for work package after start date (evt_event.actual_start_dt)
      (  evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd = 'TS'
         AND
         sched_stask.task_class_db_id = 0 AND
         sched_stask.task_class_cd IN ('CHECK', 'RO')
         AND
         evt_event.actual_start_dt >= ai_usage_dt
      )
      OR
      -- looking for task, not include work package after complete date (evt_event.event_dt)
      (  evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd = 'TS'
         AND NOT
         (
            sched_stask.task_class_db_id = 0 AND
            sched_stask.task_class_cd IN ('CHECK', 'RO')
         )
         AND
         evt_event.event_dt >= ai_usage_dt
      )
      OR
      -- looking for other events after event date (evt_event.event_dt)
      (  NOT
         (
            evt_event.event_type_db_id = 0 AND
            evt_event.event_type_cd IN ('TS', 'CF')
         )
         AND
         evt_event.event_dt >= ai_usage_dt
      )
   )
   AND
      (
         evt_inv_usage.source_db_id = 0 AND
         evt_inv_usage.source_cd = 'MAINTENIX'
      );

   -- Cloned cursor from above to pre-select events with calculated parms
   -- Added distinct to ensure only one evt_inv primary key for cases where multiple calculated data types exist
   CURSOR lcur_calc_snapshot IS
   SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */ DISTINCT
      evt_inv.event_db_id,
      evt_inv.event_id,
      evt_inv.event_inv_id
   FROM
      (SELECT
         element1 as assmbl_inv_db_id,
         element2 as assmbl_inv_id,
         element3 as data_type_db_id,
         element4 as data_type_id,
         element5 as adjusted_delta
       FROM
          TABLE (aittb_adjusted_usage_delta)
      ) adjusted_usage_delta
   INNER JOIN
      (
         SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as assmbl_inv_db_id,
            element4 as assmbl_inv_id
          FROM
             TABLE (aittb_inv_assmbl)
      ) invlist
      ON
         invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
         invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
   INNER JOIN evt_inv
      ON
         evt_inv.inv_no_db_id = invlist.inv_no_db_id AND
         evt_inv.inv_no_id    = invlist.inv_no_id
   INNER JOIN evt_event
      ON
         evt_inv.event_db_id = evt_event.event_db_id AND
         evt_inv.event_id    = evt_event.event_id
   INNER JOIN evt_inv_usage
      ON
         evt_inv.event_db_id  = evt_inv_usage.event_db_id AND
         evt_inv.event_id     = evt_inv_usage.event_id    AND
         evt_inv.event_inv_id = evt_inv_usage.event_inv_id
   -- limit query results only to events tracking calculated parms
   INNER JOIN mim_calc
      ON
         mim_calc.data_type_db_id = evt_inv_usage.data_type_db_id AND
         mim_calc.data_type_id    = evt_inv_usage.data_type_id
   -- limit query results only to calculated paramters using a data type input that was modified
   INNER JOIN mim_calc_input
      ON
         mim_calc_input.calc_db_id      = mim_calc.calc_db_id AND
         mim_calc_input.calc_id         = mim_calc.calc_id
         AND
         mim_calc_input.data_type_db_id = adjusted_usage_delta.data_type_db_id AND
         mim_calc_input.data_type_id    = adjusted_usage_delta.data_type_id
   LEFT JOIN sched_stask
      ON
         sched_stask.sched_db_id = evt_event.event_db_id AND
         sched_stask.sched_id    = evt_event.event_id
   WHERE
      -- looking for fault after found date (evt_event.actual_start_dt)
   (
     (  evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd = 'CF'
         AND
         evt_event.actual_start_dt >= ai_usage_dt
      )
      OR
      -- looking for work package after start date (evt_event.actual_start_dt)
      (  evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd = 'TS'
         AND
         sched_stask.task_class_db_id = 0 AND
         sched_stask.task_class_cd IN ('CHECK', 'RO')
         AND
         evt_event.actual_start_dt >= ai_usage_dt
      )
      OR
      -- looking for task, not include work package after complete date (evt_event.event_dt)
      (  evt_event.event_type_db_id = 0 AND
         evt_event.event_type_cd = 'TS'
         AND NOT
         (
            sched_stask.task_class_db_id = 0 AND
            sched_stask.task_class_cd IN ('CHECK', 'RO')
         )
         AND
         evt_event.event_dt >= ai_usage_dt
      )
      OR
      -- looking for other events after event date (evt_event.event_dt)
      (  NOT
         (
            evt_event.event_type_db_id = 0 AND
            evt_event.event_type_cd IN ('TS', 'CF')
         )
         AND
         evt_event.event_dt >= ai_usage_dt
      )
   )
   AND
      (
         evt_inv_usage.source_db_id = 0 AND
         evt_inv_usage.source_cd = 'MAINTENIX'
      );

   TYPE ltyp_snapshot IS TABLE OF lcur_snapshot%ROWTYPE;
   ltab_snapshots ltyp_snapshot;

BEGIN
   v_step := 10;

   OPEN lcur_snapshot;
   LOOP

      FETCH lcur_snapshot BULK COLLECT INTO ltab_snapshots LIMIT 10000;

      EXIT WHEN ltab_snapshots.COUNT = 0;

      --update usage
      BEGIN
         FORALL i IN 1..ltab_snapshots.COUNT SAVE EXCEPTIONS
            UPDATE
               evt_inv_usage
            SET
               evt_inv_usage.tsn_qt = evt_inv_usage.tsn_qt + ltab_snapshots(i).adjusted_delta
            WHERE
              evt_inv_usage.event_db_id = ltab_snapshots(i).event_db_id AND
              evt_inv_usage.event_id    = ltab_snapshots(i).event_id   AND
              evt_inv_usage.event_inv_id = ltab_snapshots(i).event_inv_id
              AND
              evt_inv_usage.data_type_db_id = ltab_snapshots(i).data_type_db_id  AND
              evt_inv_usage.data_type_id    = ltab_snapshots(i).data_type_id;
      EXCEPTION
         WHEN le_ex_dml_errors THEN
            FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
               IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
                  raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                  v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
                  sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
            END IF;
         END LOOP;
      END;
      --insert system note
      IF aiv_system_note IS NOT NULL THEN
         BEGIN
            FORALL i IN 1..ltab_snapshots.COUNT
               INSERT
               INTO
                  evt_stage (
                     event_db_id,
                     event_id,
                     stage_id,
                     event_status_db_id,
                     event_status_cd,
                     stage_dt,
                     stage_gdt,
                     hr_db_id,
                     hr_id,
                     stage_note,
                     system_bool
                  )
               SELECT
                  ltab_snapshots(i).event_db_id,
                  ltab_snapshots(i).event_id,
                  evt_stage_id_seq.NEXTVAL AS stage_id,
                  ltab_snapshots(i).event_status_db_id,
                  ltab_snapshots(i).event_status_cd,
                  SYSDATE,
                  SYSDATE,
                  ltab_snapshots(i).hr_db_id,
                  ltab_snapshots(i).hr_id,
                  ltab_snapshots(i).system_note||aiv_system_note,
                  1
               FROM
                  dual
               WHERE
                  --insert one record per eventid
                  ltab_snapshots(i).rn = 1 AND
                  ltab_snapshots(i).event_type_cd IN ('TS','CF','FG');
         EXCEPTION
            WHEN le_ex_dml_errors THEN
               FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
                  IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
                     raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                     v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
                     sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
               END IF;
            END LOOP;
         END;
      END IF;
   END LOOP;
   CLOSE lcur_snapshot;

   -- Refresh calculated parms if required
   FOR lrec_calc_snapshots IN lcur_calc_snapshot LOOP
      USAGE_PKG.update_calc_parms(
         INTINTIntTUPLE(
            lrec_calc_snapshots.event_db_id,
            lrec_calc_snapshots.event_id,
            lrec_calc_snapshots.event_inv_id
         )
      );
   END LOOP;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingEventTsn;


PROCEDURE updateFollowingEditInvOfTrkSer(
   aittb_adjusted_usage_delta IN INTINTINTINTFLOATTABLE,
   aittb_inv_assmbl IN INTINTINTINTTABLE,
   ai_usage_dt IN DATE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingEditInvOfTrkSer';
   v_correctiveDelta USAGEDELTATABLE;

BEGIN
   v_step := 10;
   SELECT
      USAGEDELTATUPLE(
         usg_usage_data.usage_data_id,
         adjusted_usage_delta.adjusted_delta
      )
   BULK COLLECT INTO v_correctiveDelta
   FROM (
      SELECT
         element1 as assmbl_inv_db_id,
         element2 as assmbl_inv_id,
         element3 as data_type_db_id,
         element4 as data_type_id,
         element5 as adjusted_delta
       FROM
          TABLE (aittb_adjusted_usage_delta)
      ) adjusted_usage_delta
      INNER JOIN
      (
         SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as assmbl_inv_db_id,
            element4 as assmbl_inv_id
          FROM
             TABLE (aittb_inv_assmbl)
      ) invlist
      ON
         invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
         invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = invlist.inv_no_db_id AND
      inv_inv.inv_no_id    = invlist.inv_no_id
   INNER JOIN usg_usage_record ON
      usg_usage_record.inv_no_db_id = inv_inv.inv_no_db_id AND
      usg_usage_record.inv_no_id    = inv_inv.inv_no_id AND
      usg_usage_record.usage_type_cd = 'MXCORRECTION'
   INNER JOIN usg_usage_data ON
      usg_usage_record.usage_record_id = usg_usage_data.usage_record_id AND
      usg_usage_data.inv_no_db_id    = inv_inv.Inv_No_Db_id AND
      usg_usage_data.inv_no_id       = inv_inv.inv_no_id AND
      usg_usage_data.data_type_db_id = adjusted_usage_delta.data_type_db_id AND
      usg_usage_data.data_type_id    = adjusted_usage_delta.data_type_id
   WHERE
      usg_usage_record.usage_dt >= ai_usage_dt AND
      inv_inv.inv_class_db_id = 0 AND
      inv_inv.inv_class_cd IN ('TRK','SER');

   v_step := 20;
   BEGIN
      FORALL i IN v_correctiveDelta.first .. v_correctiveDelta.last SAVE EXCEPTIONS
         UPDATE
            usg_usage_data
         SET
            usg_usage_data.tsn_qt = usg_usage_data.tsn_qt + v_correctiveDelta(i).element2
         WHERE
            usg_usage_data.usage_data_id = v_correctiveDelta(i).element1;
   EXCEPTION
      WHEN le_ex_dml_errors THEN
         FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
            IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
               raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
               sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
         END IF;
      END LOOP;
   END;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingEditInvOfTrkSer;


PROCEDURE updateFollowingUsgAdjustment(
   aittb_adjusted_usage_delta IN INTINTINTINTFLOATTABLE,
   ai_usage_dt IN DATE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingUsgAdjustment';
   v_correctiveDelta USAGEDELTATABLE;

BEGIN
   v_step := 10;

   SELECT
      USAGEDELTATUPLE(
         usg_usage_data.usage_data_id,
         usages.element5
      )
   BULK COLLECT INTO v_correctiveDelta
   FROM
      TABLE (aittb_adjusted_usage_delta) usages
   INNER JOIN usg_usage_data ON
      usg_usage_data.inv_no_db_id    = usages.element1 AND
      usg_usage_data.inv_no_id       = usages.element2 AND
      usg_usage_data.data_type_db_id = usages.element3 AND
      usg_usage_data.data_type_id    = usages.element4
   INNER JOIN usg_usage_record ON
      usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
   WHERE
      usg_usage_record.usage_dt >= ai_usage_dt;

   v_step := 20;
   BEGIN
      FORALL i IN v_correctiveDelta.first .. v_correctiveDelta.last SAVE EXCEPTIONS
         UPDATE
            usg_usage_data
         SET
            usg_usage_data.tsn_qt = usg_usage_data.tsn_qt + v_correctiveDelta(i).element2
         WHERE
            usg_usage_data.usage_data_id = v_correctiveDelta(i).element1;
   EXCEPTION
      WHEN le_ex_dml_errors THEN
         FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
            IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
               raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
               sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
         END IF;
      END LOOP;
   END;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingUsgAdjustment;


PROCEDURE updateFollowingSchedToPlan (
   aittb_deadlines  IN INTINTINTINTTABLE
)
AS

   CURSOR lcur_orderedDeadlines IS
      SELECT
         deadlines.event_db_id,
         deadlines.event_id,
         deadlines.data_type_db_id,
         deadlines.data_type_id
      FROM
         (
            SELECT
               element1 as event_db_id,
               element2 as event_id,
               element3 as data_type_db_id,
               element4 as data_type_id
             FROM
                TABLE (aittb_deadlines)
         ) deadlines
      INNER JOIN evt_sched_dead
         ON
            evt_sched_dead.event_db_id     = deadlines.event_db_id AND
            evt_sched_dead.event_id        = deadlines.event_id AND
            evt_sched_dead.data_type_db_id = deadlines.data_type_db_id AND
            evt_sched_dead.data_type_id    = deadlines.data_type_id
      INNER JOIN evt_event
         ON
            evt_event.event_db_id = deadlines.event_db_id AND
            evt_event.event_id    = deadlines.event_id
      ORDER BY
         -- order by usage parameter (since we are dealing with deadlines which are per usage parm)
         -- then by completion date and if no completion date then by scheduled date
         deadlines.data_type_db_id, deadlines.data_type_id,
         NVL(evt_event.event_dt, evt_sched_dead.sched_dead_dt)
      ;

   v_method_name        VARCHAR2(30) := 'updateFollowingSchedToPlan';
   v_invalidDeadline    NUMBER;
   v_eventDbId          evt_sched_dead.event_db_id%TYPE;
   v_eventId            evt_sched_dead.event_id%TYPE;
   v_dataTypeDbId       evt_sched_dead.data_type_db_id%TYPE;
   v_dataTypeId         evt_sched_dead.data_type_id%TYPE;
   v_taskDbId           sched_stask.task_db_id%TYPE;
   v_taskId             sched_stask.task_id%TYPE;
   v_return             PREP_DEADLINE_PKG.typn_RetCode;

BEGIN

   FOR lrec_orderedDeadline IN lcur_orderedDeadlines
   LOOP

      -- Pessimistically assume the deadline is invalid.
      v_invalidDeadline := 1;

      BEGIN
         -- Check if the deadline is invalid.
         -- (see WHERE clause for definition of invalid)
         SELECT
            evt_sched_dead.event_db_id,
            evt_sched_dead.event_id,
            evt_sched_dead.data_type_db_id,
            evt_sched_dead.data_type_id,
            sched_stask.task_db_id,
            sched_stask.task_id
         INTO
            v_eventDbId,
            v_eventId,
            v_dataTypeDbId,
            v_dataTypeId,
            v_taskDbId,
            v_taskId
         FROM
            sched_stask
            -- Get task's deadline.
            INNER JOIN evt_sched_dead
               ON
                  evt_sched_dead.event_db_id     = lrec_orderedDeadline.event_db_id AND
                  evt_sched_dead.event_id        = lrec_orderedDeadline.event_id AND
                  evt_sched_dead.data_type_db_id = lrec_orderedDeadline.data_type_db_id AND
                  evt_sched_dead.data_type_id    = lrec_orderedDeadline.data_type_id
            -- Get task's previous task.
            INNER JOIN evt_event_rel
               ON
                  evt_event_rel.rel_event_db_id = evt_sched_dead.event_db_id AND
                  evt_event_rel.rel_event_id    = evt_sched_dead.event_id
                  AND
                  evt_event_rel.rel_type_db_id  = 0 AND
                  evt_event_rel.rel_type_cd     = 'DEPT'
            -- Get previous task's deadline.
            INNER JOIN evt_sched_dead prev_task_sched_dead
               ON
                  prev_task_sched_dead.event_db_id = evt_event_rel.event_db_id AND
                  prev_task_sched_dead.event_id    = evt_event_rel.event_id
                  AND
                  prev_task_sched_dead.data_type_db_id = lrec_orderedDeadline.data_type_db_id AND
                  prev_task_sched_dead.data_type_id    = lrec_orderedDeadline.data_type_id
            -- Get previous task's completion usage.
            INNER JOIN evt_inv prev_task_evt_inv
               ON
                  prev_task_evt_inv.event_db_id   = evt_event_rel.event_db_id AND
                  prev_task_evt_inv.event_id      = evt_event_rel.event_id AND
                  prev_task_evt_inv.main_inv_bool = 1
            INNER JOIN evt_inv_usage prev_task_usage
               ON
                  prev_task_usage.event_db_id  = prev_task_evt_inv.event_db_id AND
                  prev_task_usage.event_id     = prev_task_evt_inv.event_id AND
                  prev_task_usage.event_inv_id = prev_task_evt_inv.event_inv_id
                  AND
                  prev_task_usage.data_type_db_id = lrec_orderedDeadline.data_type_db_id AND
                  prev_task_usage.data_type_id    = lrec_orderedDeadline.data_type_id
         WHERE
            sched_stask.sched_db_id = lrec_orderedDeadline.event_db_id AND
            sched_stask.sched_id    = lrec_orderedDeadline.event_id
            AND
            (
               (
                  -- LASTEND and previous task completed inside its sched-to-plan window
                  -- (this is an incorrect state, as it should be LASTDUE)
                  evt_sched_dead.sched_from_db_id = 0 AND
                  evt_sched_dead.sched_from_cd    = 'LASTEND'
                  AND
                  -- If on or after window begin
                  prev_task_usage.tsn_qt >= (prev_task_sched_dead.sched_dead_qt - prev_task_sched_dead.prefixed_qt)
                  AND
                  -- and on or before window end
                  prev_task_usage.tsn_qt <= (prev_task_sched_dead.sched_dead_qt + prev_task_sched_dead.postfixed_qt)
               )
               OR
               (
                  -- LASTDUE and previous task completed outside its sched-to-plan window
                  -- (this is an incorrect state, as it should be LASTEND)
                  evt_sched_dead.sched_from_db_id = 0 AND
                  evt_sched_dead.sched_from_cd    = 'LASTDUE'
                  AND
                  (
                     -- If before window begin
                     prev_task_usage.tsn_qt < (prev_task_sched_dead.sched_dead_qt - prev_task_sched_dead.prefixed_qt)
                     OR
                     -- and after window end
                     prev_task_usage.tsn_qt > (prev_task_sched_dead.sched_dead_qt + prev_task_sched_dead.postfixed_qt)
                  )
               )
               OR
               (
                  -- LASTDUE and start value does not equal previous task due value
                  -- (this is a correct state but incorrect start value,
                  --  as the start value must always equal the previous due value when LASTDUE)
                  evt_sched_dead.sched_from_db_id = 0 AND
                  evt_sched_dead.sched_from_cd    = 'LASTDUE'
                  AND
                  evt_sched_dead.start_qt != prev_task_sched_dead.sched_dead_qt
               )
            )
         ;
      EXCEPTION
         WHEN NO_DATA_FOUND THEN
            -- If select returns no rows then the deadline is valid.
            v_invalidDeadline := 0;
      END;

      IF v_invalidDeadline = 1 THEN
         PREP_DEADLINE_PKG.PrepareUsageDeadline(
            v_eventDbId,
            v_eventId,
            v_taskDbId,
            v_taskId,
            v_dataTypeDbId,
            v_dataTypeId,
            false,
            NULL,
            v_return
         );
         IF v_return < 0 THEN
            raise_application_error(-20001, 'PREP_DEADLINE_PKG.PrepareUsageDeadline() failed with return='||v_return);
         END IF;
      END IF;

   END LOOP; -- loop over lcur_orderedDeadlines

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', ERROR: ' || SQLERRM,
                         1,
                         2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateFollowingSchedToPlan;


PROCEDURE createRealtimeInvWorkItem(
   aHInvNoDbId IN inv_inv.h_inv_no_db_id%TYPE,
   aHInvNoId   IN inv_inv.h_inv_no_id%TYPE,
   aInvNoDbId  IN inv_inv.inv_no_db_id%TYPE,
   aInvNoId    IN inv_inv.inv_no_id%TYPE,
   aInvClassCd IN inv_inv.inv_class_cd%TYPE
)
AS
   workId NUMBER;
   mimDb NUMBER;
   invDesc inv_inv.inv_no_sdesc%TYPE;
BEGIN
   SELECT
      db_id,
      utl_work_item_id.nextval
   INTO
      mimDb,
      workId
   FROM
      mim_local_db;

   SELECT
      inv_inv.inv_no_sdesc
   INTO
      invDesc
   FROM
      inv_inv
   WHERE
      inv_inv.inv_no_db_id = aInvNoDbId AND
      inv_inv.inv_no_id    = aInvNoId;

   IF(aInvClassCd = 'ACFT') THEN
      INSERT INTO utl_work_item(id, type, key, data, utl_id) VALUES (workId, 'REAL_TIME_AIRCRAFT_DEADLINE_UPDATE', aHInvNoDbId || ':' || aHInvNoId, invDesc, mimDb);
   ELSE
      INSERT INTO utl_work_item(id, type, key, data, utl_id) VALUES (workId, 'REAL_TIME_INVENTORY_DEADLINE_UPDATE', aHInvNoDbId || ':' || aHInvNoId || ':' || aInvNoDbId||':'||aInvNoId, invDesc, mimDb);
   END IF;

END createRealtimeInvWorkItem;


PROCEDURE updateCurrentTsn (
   aittb_adjusted_usage_delta IN INTINTINTINTFLOATTABLE,
   aittb_inv_assmbl IN INTINTINTINTTABLE,
   ai_acft_inv_db_id IN NUMBER,
   ai_acft_inv_id IN NUMBER
)
AS

   v_method_name VARCHAR2(30) := 'updateCurrentTsn';
   v_usageDelta INTINTINTINTFLOATTABLE;

BEGIN
   v_step := 10;

   SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
      INTINTINTINTFLOATTUPLE (
         invlist.inv_no_db_id,
         invlist.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
      )
   BULK COLLECT INTO v_usageDelta
   FROM
      (
         SELECT
            element1 as assmbl_inv_db_id,
            element2 as assmbl_inv_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
         FROM
            TABLE (aittb_adjusted_usage_delta)
      ) adjusted_usage_delta
   INNER JOIN
      (
          SELECT
             element1 as inv_no_db_id,
             element2 as inv_no_id,
             element3 as assmbl_inv_db_id,
             element4 as assmbl_inv_id
           FROM
              TABLE (aittb_inv_assmbl)
      ) invlist
   ON
      invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
      invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id;

   v_step := 20;
   BEGIN
      FORALL i IN v_usageDelta.first .. v_usageDelta.last SAVE EXCEPTIONS
         UPDATE
            inv_curr_usage
         SET
            inv_curr_usage.tsn_qt = inv_curr_usage.tsn_qt + v_usageDelta(i).element5
         WHERE
            inv_curr_usage.inv_no_db_id    = v_usageDelta(i).element1 AND
            inv_curr_usage.inv_no_id       = v_usageDelta(i).element2 AND
            inv_curr_usage.data_type_db_id = v_usageDelta(i).element3 AND
            inv_curr_usage.data_type_id    = v_usageDelta(i).element4;
   EXCEPTION
      WHEN le_ex_dml_errors THEN
         FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
            IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
               raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
               sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
         END IF;
      END LOOP;
   END;

   -- Create Realtime Deadline Update Work Items for all affected highest inventory which are not our aircraft
   -- Note current Aircraft work item is handled in the java tier
   FOR lCurInv IN (
      SELECT DISTINCT
         h_inv.inv_no_db_id h_inv_no_db_id,
         h_inv.inv_no_id h_inv_no_id,
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         h_inv.inv_class_cd
      FROM
         (
          SELECT
             element1 as inv_no_db_id,
             element2 as inv_no_id,
             element3 as assmbl_inv_db_id,
             element4 as assmbl_inv_id
           FROM
              TABLE (aittb_inv_assmbl)
         ) invlist
      INNER JOIN inv_inv ON
         invlist.inv_no_db_id = inv_inv.inv_no_db_id AND
         invlist.inv_no_id = inv_inv.inv_no_id
      INNER JOIN inv_inv h_inv ON
         inv_inv.h_inv_no_db_id = h_inv.inv_no_db_id AND
         inv_inv.h_inv_no_id = h_inv.inv_no_id
      WHERE
         h_inv.inv_class_db_id = 0 AND
         h_inv.inv_class_cd <> 'ACFT'
         OR
         (
            h_inv.inv_class_db_id = 0 AND
            h_inv.inv_class_cd = 'ACFT'
            AND NOT
            (
               h_inv.inv_no_db_id = ai_acft_inv_db_id AND
               h_inv.inv_no_id    = ai_acft_inv_id
            )
         )
   )
   LOOP
       createRealtimeInvWorkItem(lCurInv.h_inv_no_db_id,lCurInv.h_inv_no_id,lCurInv.inv_no_db_id,lCurInv.inv_no_id,lCurInv.inv_class_cd);
   END LOOP;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateCurrentTsn;


PROCEDURE updateCurrentTso (
   aittb_adjusted_usage_delta IN INTINTINTINTFLOATTABLE,
   aittb_inv_assmbl IN INTINTINTINTTABLE,
   ai_usage_dt IN DATE
)
AS
   v_method_name VARCHAR2(30) := 'updateCurrentTso';
   v_usageDelta INTINTINTINTFLOATTABLE;

BEGIN
   v_step := 10;

   SELECT
      INTINTINTINTFLOATTUPLE (
         invlist.inv_no_db_id,
         invlist.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
      )
   BULK COLLECT INTO v_usageDelta
   FROM
      (
         SELECT
            element1 as assmbl_inv_db_id,
            element2 as assmbl_inv_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
         FROM
            TABLE (aittb_adjusted_usage_delta)
      ) adjusted_usage_delta
   INNER JOIN
      (
          SELECT
             element1 as inv_no_db_id,
             element2 as inv_no_id,
             element3 as assmbl_inv_db_id,
             element4 as assmbl_inv_id
           FROM
              TABLE (aittb_inv_assmbl)
      ) invlist
   ON
      invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
      invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
   WHERE NOT EXISTS (
   -- Do not perform this action on any inventory having been overhauled since the usage date
      SELECT
         1
      FROM
         sched_stask
      INNER JOIN evt_event ON
         sched_stask.sched_db_id = evt_event.event_db_id AND
         sched_stask.sched_id    = evt_event.event_id
      WHERE
         sched_stask.main_inv_no_db_id = invlist.inv_no_db_id AND
         sched_stask.main_inv_no_id    = invlist.inv_no_id
         AND
         sched_stask.task_class_db_id = 0 AND
         sched_stask.task_class_cd = 'OVHL'
         AND
         evt_event.event_status_db_id = 0 AND
         evt_event.event_status_cd    = 'COMPLETE'
         AND
         evt_event.event_dt >= ai_usage_dt
   );

   v_step := 20;
   BEGIN
      FORALL i IN v_usageDelta.first .. v_usageDelta.last SAVE EXCEPTIONS
         UPDATE
            inv_curr_usage
         SET
            inv_curr_usage.tso_qt = inv_curr_usage.tso_qt + v_usageDelta(i).element5
         WHERE
            inv_curr_usage.inv_no_db_id    = v_usageDelta(i).element1 AND
            inv_curr_usage.inv_no_id       = v_usageDelta(i).element2 AND
            inv_curr_usage.data_type_db_id = v_usageDelta(i).element3 AND
            inv_curr_usage.data_type_id    = v_usageDelta(i).element4;
   EXCEPTION
      WHEN le_ex_dml_errors THEN
         FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
            IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
               raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
               sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
         END IF;
      END LOOP;
   END;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateCurrentTso;


PROCEDURE updateCurrentTsi (
   aittb_adjusted_usage_delta IN INTINTINTINTFLOATTABLE,
   aittb_inv_assmbl IN INTINTINTINTTABLE,
   ai_usage_dt IN DATE
)
AS
   v_method_name VARCHAR2(30) := 'updateCurrentTsi';
   v_usageDelta INTINTINTINTFLOATTABLE;

BEGIN
   v_step := 10;

   SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
      INTINTINTINTFLOATTUPLE (
         invlist.inv_no_db_id,
         invlist.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
      )
   BULK COLLECT INTO v_usageDelta
   FROM
      (
         SELECT
            element1 as assmbl_inv_db_id,
            element2 as assmbl_inv_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
         FROM
            TABLE (aittb_adjusted_usage_delta)
      ) adjusted_usage_delta
   INNER JOIN
      (
          SELECT
             element1 as inv_no_db_id,
             element2 as inv_no_id,
             element3 as assmbl_inv_db_id,
             element4 as assmbl_inv_id
           FROM
              TABLE (aittb_inv_assmbl)
      ) invlist
   ON
      invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
      invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
   WHERE NOT EXISTS (
   -- Do not perform this action on any inventory having been reinstalled since the usage date
      SELECT 1
      FROM   inv_install
      WHERE  inv_install.inv_no_db_id = invlist.inv_no_db_id AND
             inv_install.inv_no_id    = invlist.inv_no_id AND
             inv_install.event_dt > ai_usage_dt AND
             inv_install.main_inv_bool = 1
   );

   v_step := 20;
   BEGIN
      FORALL i IN v_usageDelta.first .. v_usageDelta.last SAVE EXCEPTIONS
         UPDATE
            inv_curr_usage
         SET
            inv_curr_usage.tsi_qt = inv_curr_usage.tsi_qt + v_usageDelta(i).element5
         WHERE
            inv_curr_usage.inv_no_db_id    = v_usageDelta(i).element1 AND
            inv_curr_usage.inv_no_id       = v_usageDelta(i).element2 AND
            inv_curr_usage.data_type_db_id = v_usageDelta(i).element3 AND
            inv_curr_usage.data_type_id    = v_usageDelta(i).element4;
   EXCEPTION
      WHEN le_ex_dml_errors THEN
         FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
            IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
               raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
               sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
         END IF;
      END LOOP;
   END;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateCurrentTsi;


PROCEDURE updateCalculatedParameters (
   aittb_adjusted_usage_delta IN INTINTINTINTFLOATTABLE,
   aittb_inv_assmbl IN INTINTINTINTTABLE
)
AS
   v_method_name VARCHAR2(30) := 'updateCalculatedParameters';
   v_update_result NUMBER;

BEGIN

   FOR lrec IN (
      SELECT DISTINCT
      -- Distinct because we have a list with potentially numerous calculated parameters per inventory
         inv_curr_usage.inv_no_db_id,
         inv_curr_usage.inv_no_id
      FROM
         (
            SELECT
               element1 as assmbl_inv_db_id,
               element2 as assmbl_inv_id,
               element3 as data_type_db_id,
               element4 as data_type_id
            FROM
               TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
      INNER JOIN
         (
             SELECT
                element1 as inv_no_db_id,
                element2 as inv_no_id,
                element3 as assmbl_inv_db_id,
                element4 as assmbl_inv_id
              FROM
                 TABLE (aittb_inv_assmbl)
         ) invlist
      ON
         invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
         invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
      -- Modified usages are used in a calculated parameter
      INNER JOIN mim_calc_input ON
         mim_calc_input.data_type_db_id = adjusted_usage_delta.data_type_db_id AND
         mim_calc_input.data_type_id    = adjusted_usage_delta.data_type_id
      INNER JOIN mim_calc ON
         mim_calc.calc_db_id = mim_calc_input.calc_db_id AND
         mim_calc.calc_id    = mim_calc_input.calc_id
      -- inventory tracks the calculated parm
      INNER JOIN inv_curr_usage ON
         inv_curr_usage.data_type_db_id = mim_calc.data_type_db_id AND
         inv_curr_usage.data_type_id    = mim_calc.data_type_id
         AND
         inv_curr_usage.inv_no_db_id = invlist.inv_no_db_id AND
         inv_curr_usage.inv_no_id    = invlist.inv_no_id
      -- For all inventory returned, only return rows where the inventory is aircraft
      -- or is no longer on the assembly
      WHERE NOT EXISTS (
         SELECT 1
         FROM   inv_inv
         WHERE
            inv_inv.inv_no_db_id        = invlist.inv_no_db_id AND
            inv_inv.inv_no_id           = invlist.inv_no_id
            AND
            inv_inv.assmbl_inv_no_db_id = invlist.assmbl_inv_db_id AND
            inv_inv.assmbl_inv_no_id    = invlist.assmbl_inv_id
            AND
            inv_inv.inv_class_cd <> 'ACFT'
      )
   ) LOOP
      -- This function will automatically process all assembly sub-inventory when provided an inv_no_id of an assembly
      USAGE_PKG.UsageCalculations(lrec.inv_no_db_id, lrec.inv_no_id, v_update_result);
   END LOOP;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END updateCalculatedParameters;


FUNCTION isPreProcessRequired(
   ai_acft_inv_db_id IN INTEGER,
   ai_acft_inv_id    IN INTEGER,
   ai_org_usage_dt   IN DATE,
   ai_new_usage_dt   IN DATE
) RETURN INTEGER
IS
   v_method_name VARCHAR2(30) := 'isPreProcessRequired';

   vi_is_event_usage INTEGER;
   vi_range_start_dt DATE;
   vi_range_end_dt DATE;
   
BEGIN

   v_step := 10;
   IF ai_org_usage_dt < ai_new_usage_dt THEN
      vi_range_start_dt := ai_org_usage_dt;
      vi_range_end_dt   := ai_new_usage_dt;
   ELSE
      vi_range_start_dt := ai_new_usage_dt;
      vi_range_end_dt   := ai_org_usage_dt;
   END IF;   

   v_step := 20;

   SELECT
      CASE
         WHEN EXISTS
            (
               SELECT
                  1
               FROM
                  usg_usage_record
               INNER JOIN inv_inv
                  ON
                     inv_inv.inv_no_db_id = usg_usage_record.inv_no_db_id AND
                     inv_inv.inv_no_id    = usg_usage_record.inv_no_id
               WHERE
                  inv_inv.h_inv_no_db_id = ai_acft_inv_db_id AND
                  inv_inv.h_inv_no_id    = ai_acft_inv_id
                  AND
                  usg_usage_record.usage_dt BETWEEN vi_range_start_dt AND vi_range_end_dt
                  AND
                  rownum = 1
             )
         THEN 1
         ELSE 0
      END
   INTO
      vi_is_event_usage
   FROM dual;

   IF vi_is_event_usage > 0 THEN
      RETURN vi_is_event_usage;
   END IF;

   v_step := 30;
   SELECT
      CASE
         WHEN EXISTS
            (
               SELECT
                  1
               FROM
                  evt_event
               INNER JOIN evt_inv
                  ON
                     evt_inv.event_db_id = evt_event.event_db_id AND
                     evt_inv.event_id    = evt_event.event_id
               LEFT JOIN sched_stask
                  ON
                     sched_stask.sched_db_id = evt_event.event_db_id AND
                     sched_stask.sched_id    = evt_event.event_id
               WHERE
                  evt_inv.h_inv_no_db_id = ai_acft_inv_db_id AND
                  evt_inv.h_inv_no_id    = ai_acft_inv_id
                  AND
                  (
                      -- looking for fault after found date (evt_event.actual_start_dt)
                      ( evt_event.event_type_db_id = 0 AND
                        evt_event.event_type_cd = 'CF'
                        AND
                        evt_event.actual_start_dt BETWEEN vi_range_start_dt AND vi_range_end_dt
                      )
                      OR
                      -- looking for other events after event date (evt_event.event_dt)
                      (
                        (
                          evt_event.event_type_db_id != 0 OR
                          evt_event.event_type_cd NOT IN ('TS', 'CF')
                        )
                        AND
                        evt_event.event_dt BETWEEN vi_range_start_dt AND vi_range_end_dt
                      )
                      OR
                      -- looking for work package after start date (evt_event.actual_start_dt)
                      (
                        evt_event.event_type_db_id = 0 AND
                        evt_event.event_type_cd = 'TS'
                        AND
                        sched_stask.task_class_db_id = 0 AND
                        sched_stask.task_class_cd IN ('CHECK', 'RO')
                        AND
                        evt_event.event_status_db_id = 0 AND
                        evt_event.event_status_cd IN ('COMPLETE', 'IN WORK')
                        AND
                        evt_event.actual_start_dt BETWEEN vi_range_start_dt AND vi_range_end_dt
                      )
                      OR
                      -- looking for task, not include work package after complete date (evt_event.event_dt)
                      (
                        evt_event.event_type_db_id = 0 AND
                        evt_event.event_type_cd = 'TS'
                        AND
                        (
                         sched_stask.task_class_db_id != 0 OR
                         sched_stask.task_class_cd NOT IN ('CHECK', 'RO')
                        )
                        AND
                        evt_event.event_status_db_id = 0 AND
                        evt_event.event_status_cd = 'COMPLETE'
                        AND
                        evt_event.event_dt BETWEEN vi_range_start_dt AND vi_range_end_dt
                      )
                  )
                  AND
                  rownum = 1
             )
         THEN 1
         ELSE 0
      END
   INTO
      vi_is_event_usage
   FROM dual;

   RETURN vi_is_event_usage;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END isPreProcessRequired;


FUNCTION isnotLatestUsageAdjustment(
   ai_acft_inv_db_id IN INTEGER,
   ai_acft_inv_id    IN INTEGER,
   ai_usage_dt       IN DATE
) RETURN INTEGER
IS
   v_method_name VARCHAR2(30) := 'isnotLatestUsageAdjustment';

   vi_is_not_latest_usage INTEGER;

BEGIN

   v_step := 10;

   SELECT
      CASE
         WHEN EXISTS
            (
               SELECT
                  1
               FROM
                  usg_usage_record
               INNER JOIN inv_inv
                  ON
                     inv_inv.inv_no_db_id = usg_usage_record.inv_no_db_id AND
                     inv_inv.inv_no_id    = usg_usage_record.inv_no_id
               WHERE
                  inv_inv.h_inv_no_db_id = ai_acft_inv_db_id AND
                  inv_inv.h_inv_no_id    = ai_acft_inv_id
                  AND
                  usg_usage_record.usage_dt > ai_usage_dt
                  AND
                  rownum = 1
             )
         THEN 1
         ELSE 0
      END
   INTO
      vi_is_not_latest_usage
   FROM dual;

   v_step := 20;

   IF vi_is_not_latest_usage > 0 THEN
      RETURN vi_is_not_latest_usage;
   END IF;

   v_step := 30;

   SELECT
      CASE
         WHEN EXISTS
            (
               SELECT
                  1
               FROM
                  evt_event
               INNER JOIN evt_inv
                  ON
                     evt_inv.event_db_id = evt_event.event_db_id AND
                     evt_inv.event_id    = evt_event.event_id
               LEFT JOIN sched_stask
                  ON
                     sched_stask.sched_db_id = evt_event.event_db_id AND
                     sched_stask.sched_id    = evt_event.event_id
               WHERE
                  evt_inv.h_inv_no_db_id = ai_acft_inv_db_id AND
                  evt_inv.h_inv_no_id    = ai_acft_inv_id
                  AND
                  (
                      -- looking for fault after found date (evt_event.actual_start_dt)
                      ( evt_event.event_type_db_id = 0 AND
                        evt_event.event_type_cd = 'CF'
                        AND
                        evt_event.actual_start_dt >= ai_usage_dt
                      )
                      OR
                      -- looking for other events after event date (evt_event.event_dt)
                      (
                        (
                          evt_event.event_type_db_id != 0 OR
                          evt_event.event_type_cd NOT IN ('TS', 'CF')
                        )
                        AND
                        evt_event.event_dt >= ai_usage_dt
                      )
                      OR
                      -- looking for work package after start date (evt_event.actual_start_dt)
                      (
                        evt_event.event_type_db_id = 0 AND
                        evt_event.event_type_cd = 'TS'
                        AND
                        sched_stask.task_class_db_id = 0 AND
                        sched_stask.task_class_cd IN ('CHECK', 'RO')
                        AND
                        evt_event.event_status_db_id = 0 AND
                        evt_event.event_status_cd IN ('COMPLETE', 'IN WORK')
                        AND
                        evt_event.actual_start_dt >= ai_usage_dt
                      )
                      OR
                      -- looking for task, not include work package after complete date (evt_event.event_dt)
                      (
                        evt_event.event_type_db_id = 0 AND
                        evt_event.event_type_cd = 'TS'
                        AND
                        (
                         sched_stask.task_class_db_id != 0 OR
                         sched_stask.task_class_cd NOT IN ('CHECK', 'RO')
                        )
                        AND
                        evt_event.event_status_db_id = 0 AND
                        evt_event.event_status_cd = 'COMPLETE'
                        AND
                        evt_event.event_dt >= ai_usage_dt
                      )
                  )
                  AND
                  rownum = 1
             )
         THEN 1
         ELSE 0
      END
   INTO
      vi_is_not_latest_usage
   FROM dual;

   RETURN vi_is_not_latest_usage;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END isnotLatestUsageAdjustment;


PROCEDURE processLatestUsage (
   ai_acft_inv_db_id IN INTEGER,
   ai_acft_inv_id    IN INTEGER,
   airaw_usage_adjustment_id   IN RAW,
   aittb_adjusted_usage_delta IN INTINTINTINTFLOATTABLE
)
AS

   v_method_name VARCHAR2(30) := 'processLatestUsage';
   v_usage_to_update INTINTINTINTFLOATTABLE;

BEGIN
   v_step := 10;

   SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
      INTINTINTINTFLOATTUPLE(
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
      )
   BULK COLLECT INTO v_usage_to_update
   FROM
      (SELECT
         element1 as assmbl_inv_db_id,
         element2 as assmbl_inv_id,
         element3 as data_type_db_id,
         element4 as data_type_id,
         element5 as adjusted_delta
       FROM
          TABLE (aittb_adjusted_usage_delta)
      ) adjusted_usage_delta
   INNER JOIN inv_inv ON
      inv_inv.h_inv_no_db_id = ai_acft_inv_db_id AND
      inv_inv.h_inv_no_id    = ai_acft_inv_id
      AND
      DECODE(inv_inv.inv_class_cd, 'ASSY', inv_inv.inv_no_db_id, NVL(inv_inv.assmbl_inv_no_db_id, inv_inv.h_inv_no_db_id)) = adjusted_usage_delta.assmbl_inv_db_id AND
      DECODE(inv_inv.inv_class_cd, 'ASSY', inv_inv.inv_no_id, NVL(inv_inv.assmbl_inv_no_id, inv_inv.h_inv_no_id))          = adjusted_usage_delta.assmbl_inv_id;

   v_step := 20;
   BEGIN
      FORALL i IN v_usage_to_update.first .. v_usage_to_update.last SAVE EXCEPTIONS
         UPDATE
            inv_curr_usage
         SET
           inv_curr_usage.tsn_qt = inv_curr_usage.tsn_qt + v_usage_to_update(i).element5,
           inv_curr_usage.tso_qt = inv_curr_usage.tso_qt + v_usage_to_update(i).element5,
           inv_curr_usage.tsi_qt = inv_curr_usage.tsi_qt + v_usage_to_update(i).element5
         WHERE
            v_usage_to_update(i).element1 = inv_curr_usage.inv_no_db_id AND
            v_usage_to_update(i).element2 = inv_curr_usage.inv_no_id
            AND
            v_usage_to_update(i).element3 = inv_curr_usage.data_type_db_id AND
            v_usage_to_update(i).element4 = inv_curr_usage.data_type_id;
   EXCEPTION
      WHEN le_ex_dml_errors THEN
         FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
            IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
               raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
               sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
         END IF;
      END LOOP;
   END;

   v_step := 30;
   BEGIN
      FORALL i IN v_usage_to_update.first .. v_usage_to_update.last SAVE EXCEPTIONS
         UPDATE
            usg_usage_data
         SET
            usg_usage_data.tsn_qt = usg_usage_data.tsn_qt + v_usage_to_update(i).element5
         WHERE
            v_usage_to_update(i).element1 = usg_usage_data.inv_no_db_id AND
            v_usage_to_update(i).element2 = usg_usage_data.inv_no_id
            AND
            v_usage_to_update(i).element3 = usg_usage_data.data_type_db_id AND
            v_usage_to_update(i).element4 = usg_usage_data.data_type_id
            AND
            usg_usage_data.usage_record_id = airaw_usage_adjustment_id;
   EXCEPTION
      WHEN le_ex_dml_errors THEN
         FOR li_exi IN 1 .. SQL%bulk_exceptions.COUNT LOOP
            IF (SQL%bulk_exceptions(li_exi).error_code != 1) THEN
               raise_application_error(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQL%bulk_exceptions(li_exi).error_code,
               sqlerrm(- (SQL%bulk_exceptions(li_exi).error_code)));
         END IF;
      END LOOP;
   END;

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);

END processLatestUsage;

/********************************************************************************
*
* PROCEDURE:      process_usage_date_change
*
* Description:    Processes a usage change in which the usage record
*                 date will be also be changed, on top of any potential
*                 delta changes.
*
*********************************************************************************
*
* Copyright 2018 Mxi Technologies.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* Mxi Technologies is prohibited.
*
*********************************************************************************/
PROCEDURE process_usage_date_change (
   aid_new_usage_date          IN DATE,
   aib_date_outside_window     IN NUMBER,
   airaw_usage_adjustment_id   IN RAW,
   aittb_assmbl_inv_db_id      IN INTEGERTABLE,
   aittb_assmbl_inv_id         IN INTEGERTABLE,
   aittb_data_type_db_id       IN INTEGERTABLE,
   aittb_data_type_id          IN INTEGERTABLE,
   aittb_new_delta             IN FLOATTABLE,
   aiv_system_note             IN CLOB,
   aiv_negating_system_note    IN VARCHAR2,
   aiv_ratify_system_note      IN VARCHAR2,
   aiv_task_wrkpkg_system_note IN VARCHAR2,
   aiv_fault_system_note       IN VARCHAR2,
   aiv_deadline_system_note    IN VARCHAR2,
   ain_hr_db_id                IN NUMBER,
   ain_hr_id                   IN NUMBER
)
AS

   v_method_name VARCHAR2(30) := 'process_usage_date_change';
   v_ttb_zeroed_deltas FLOATTABLE := FLOATTABLE();

   v_ttb_assy_usage_deltadata INTINTINTINTFLOATTABLE;
   v_ttb_assy_usage_zerodeltadata INTINTINTINTFLOATTABLE;
   v_ttb_adjusted_usage_delta INTINTINTINTFLOATTABLE;
   v_ttb_adjusted_usage_zerodelta INTINTINTINTFLOATTABLE;
   v_ttb_inv_assmbl    INTINTINTINTTABLE;
   v_ttb_deadlines     INTINTINTINTTABLE;
   v_ttb_calc_deadline INTINTINTINTTABLE;

   vi_acft_inv_db_id INTEGER;
   vi_acft_inv_id INTEGER;
   vi_usage_dt DATE;
   vi_flight_leg RAW(16);
   vi_system_note CLOB;
   vi_zero_system_note VARCHAR2(4000);
   vi_skip_pre_process BOOLEAN := FALSE;

   vi_is_not_latest_usage INTEGER;
   vi_is_event_usage_adj_req INTEGER;

BEGIN

   --Collect some basic usage record info.
   SELECT
      inv_no_db_id, inv_no_id, usage_dt
   INTO
      vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt
   FROM
      usg_usage_record
   WHERE
      usage_record_id = airaw_usage_adjustment_id;

   -- combine set of input arrays into a table
   SELECT
      INTINTINTINTFLOATTUPLE(
         element1, element2, element3, element4, element5
      )
   BULK COLLECT INTO v_ttb_assy_usage_deltadata
   FROM
      TABLE (
         array_pkg.getINTINTINTINTFLOATTABLE (
            aittb_assmbl_inv_db_id,
            aittb_assmbl_inv_id,
            aittb_data_type_db_id,
            aittb_data_type_id,
            aittb_new_delta
         )
      );

   v_step := 1;

   v_ttb_adjusted_usage_delta := get_adjusted_usage_delta(airaw_usage_adjustment_id, v_ttb_assy_usage_deltadata);

   v_step := 2;

   --Check to see if there are any usage/events/tasks that need updating
   vi_is_event_usage_adj_req := isPreProcessRequired(vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt, aid_new_usage_date);
   -- If there is no event work to be done
   IF vi_is_event_usage_adj_req = 0 THEN
      -- If there are no events to update, but there is a delta change
      IF v_ttb_adjusted_usage_delta.COUNT > 0 THEN
         -- Skip the pre process part of the logic
         vi_skip_pre_process := TRUE;
      ELSE
         --Update the usage record date, then leave.
         UPDATE usg_usage_record
         SET usg_usage_record.usage_dt=aid_new_usage_date
         WHERE usage_record_id = airaw_usage_adjustment_id;
         RETURN;
      END IF;
   END IF;

   IF NOT(vi_skip_pre_process) THEN
      v_step := 10;

      SELECT
         leg_id
      INTO
         vi_flight_leg
      FROM
         fl_leg
      WHERE
         usage_record_id = airaw_usage_adjustment_id;

      IF(aiv_negating_system_note IS NOT NULL) THEN
        --add flight info to the system note
        vi_zero_system_note := ' <flight-leg id='||vi_flight_leg||'> <br> '||aiv_negating_system_note;
      END IF ;

      v_ttb_zeroed_deltas.EXTEND(aittb_new_delta.count);

      -- create a new zeroed usage value table, as well as a negating history note
      FOR i IN 1..aittb_new_delta.count LOOP
          SELECT 0 INTO v_ttb_zeroed_deltas(i) FROM dual;
      END LOOP;

      --process the usages data, setting the deltas to '0'
      -- combine set of input arrays into a table
      SELECT
         INTINTINTINTFLOATTUPLE(
            element1, element2, element3, element4, element5
         )
      BULK COLLECT INTO v_ttb_assy_usage_zerodeltadata
      FROM
         TABLE (
            array_pkg.getINTINTINTINTFLOATTABLE (
               aittb_assmbl_inv_db_id,
               aittb_assmbl_inv_id,
               aittb_data_type_db_id,
               aittb_data_type_id,
               v_ttb_zeroed_deltas
            )
         );

      v_step := 20;

      v_ttb_adjusted_usage_zerodelta := get_adjusted_usage_delta(airaw_usage_adjustment_id, v_ttb_assy_usage_zerodeltadata);

      v_step := 30;

      -- no delta changed, skip to the next step
      IF v_ttb_adjusted_usage_zerodelta.COUNT = 0 THEN
         vi_skip_pre_process := TRUE;
      END IF;
   END IF;

   v_step := 40;

   IF NOT(vi_skip_pre_process) THEN
      vi_is_not_latest_usage := isnotLatestUsageAdjustment(vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt);
      -- latest flight, only update current usage and then skip to the next step
      IF vi_is_not_latest_usage = 0 THEN
         processLatestUsage(vi_acft_inv_db_id, vi_acft_inv_id, airaw_usage_adjustment_id, v_ttb_adjusted_usage_zerodelta);
      ELSE
         v_step := 50;

         v_ttb_inv_assmbl := get_acft_hist_config(airaw_usage_adjustment_id);

         v_step := 60;

         -- We have to update deadline first, the calculation of historic Tsn for subcomponent relays
         -- on the unmodified current tsn value and unmodified flight Tsn value.
         updateFollowingTaskDeadlines(airaw_usage_adjustment_id, v_ttb_adjusted_usage_zerodelta, v_ttb_inv_assmbl, vi_usage_dt, vi_zero_system_note, aiv_deadline_system_note, ain_hr_db_id, ain_hr_id, v_ttb_calc_deadline, v_ttb_deadlines);

         v_step := 70;
         updateCurrentTsn(v_ttb_adjusted_usage_zerodelta, v_ttb_inv_assmbl, vi_acft_inv_db_id, vi_acft_inv_id);

         v_step := 80;
         updateCurrentTso(v_ttb_adjusted_usage_zerodelta, v_ttb_inv_assmbl, vi_usage_dt);

         v_step := 90;
         updateCurrentTsi(v_ttb_adjusted_usage_zerodelta, v_ttb_inv_assmbl, vi_usage_dt);

         v_step := 100;
         updateCalculatedParameters(v_ttb_adjusted_usage_zerodelta, v_ttb_inv_assmbl);

         v_step := 110;
         updateFollowingEventTsn(v_ttb_adjusted_usage_zerodelta, v_ttb_inv_assmbl, vi_usage_dt, vi_zero_system_note, aiv_task_wrkpkg_system_note, aiv_fault_system_note, ain_hr_db_id, ain_hr_id);

         v_step := 120;
         updateFollowingEditInvOfTrkSer(v_ttb_adjusted_usage_zerodelta, v_ttb_inv_assmbl, vi_usage_dt);

         v_step := 130;
         updateFollowingUsgAdjustment(v_ttb_adjusted_usage_zerodelta, vi_usage_dt);

         v_step := 140;
         -- Prior to re-calcute task calc deadlines, current tsn and usage adjustment need to be updated.
         -- After recalcution, SchedToPlan shall be re-adjusted.
         updateFollowingCalcDeadline(v_ttb_calc_deadline);

         v_step := 150;
         -- Prior to re-adjusting task deadlines that have a schedule-to-plan window,
         -- both the task deadlines themselves and the event TSNs all need to be updated.
         -- This is because the adjusting of the task deadlines may move that deadline
         -- either into or out of the window.
         -- Therefore, it is important that updateFollowingSchedToPlan() be called after
         -- updateFollowingTaskDeadlines() and updateFollowingEventTsn().
         updateFollowingSchedToPlan(v_ttb_deadlines);
      END IF;

   END IF;

   v_step := 160;
   --Update the usage record date.
   UPDATE usg_usage_record
   SET usg_usage_record.usage_dt=aid_new_usage_date
   WHERE usage_record_id = airaw_usage_adjustment_id;

   vi_usage_dt := aid_new_usage_date;

   v_step := 170;

   IF NOT(vi_skip_pre_process) THEN
      SELECT IntIntIntIntFloatTuple(
         element1, element2, element3, element4, element5
      ) BULK COLLECT INTO v_ttb_adjusted_usage_delta
      FROM TABLE (v_ttb_assy_usage_deltadata)
      WHERE element5 != 0;
      --v_ttb_adjusted_usage_delta := v_ttb_assy_usage_deltadata;
   END IF;

   v_step := 175;

   vi_is_not_latest_usage := isnotLatestUsageAdjustment(vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt);
   -- latest flight, only update current usage
   IF vi_is_not_latest_usage = 0 THEN
      processLatestUsage(vi_acft_inv_db_id, vi_acft_inv_id, airaw_usage_adjustment_id, v_ttb_adjusted_usage_delta);
      RETURN;
   END IF;

   v_step := 180;

   SELECT
      leg_id
   INTO
      vi_flight_leg
   FROM
      fl_leg
   WHERE
      usage_record_id = airaw_usage_adjustment_id;
   --If we didn't skip the zeroing process, we need the mroe full history note
   IF NOT(vi_skip_pre_process) THEN
      --add flight info to the system note
      vi_system_note := ' <flight-leg id='||vi_flight_leg||'> <br> '||aiv_ratify_system_note;
   --If we did skip the zeroing process, we can use the more simple note to only
   -- show actual user based delta cahnges
   ELSIF (aiv_system_note IS NOT NULL) THEN
      vi_system_note := ' <flight-leg id='||vi_flight_leg||'> <br> '||aiv_system_note;
   END IF;


   IF vi_skip_pre_process OR v_ttb_inv_assmbl IS NULL THEN
      v_step := 190;
      v_ttb_inv_assmbl := get_acft_hist_config(airaw_usage_adjustment_id);
   END IF;

   v_step := 200;

   -- We have to update deadline first, the calculation of historic Tsn for subcomponent relays
   -- on the unmodified current tsn value and unmodified flight Tsn value.
   updateFollowingTaskDeadlines(airaw_usage_adjustment_id, v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt, vi_system_note, aiv_deadline_system_note, ain_hr_db_id, ain_hr_id, v_ttb_calc_deadline, v_ttb_deadlines);

   v_step := 210;
   updateCurrentTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_acft_inv_db_id, vi_acft_inv_id);

   v_step := 220;
   updateCurrentTso(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 230;
   updateCurrentTsi(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 240;
   updateCalculatedParameters(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl);

   v_step := 250;
   updateFollowingEventTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt, vi_system_note, aiv_task_wrkpkg_system_note, aiv_fault_system_note, ain_hr_db_id, ain_hr_id);

   v_step := 260;
   updateFollowingEditInvOfTrkSer(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 270;
   updateFollowingUsgAdjustment(v_ttb_adjusted_usage_delta, vi_usage_dt);

   v_step := 280;
   -- Prior to re-calcute task calc deadlines, current tsn and usage adjustment need to be updated.
   -- After recalcution, SchedToPlan shall be re-adjusted.
   updateFollowingCalcDeadline(v_ttb_calc_deadline);

   v_step := 290;
   -- Prior to re-adjusting task deadlines that have a schedule-to-plan window,
   -- both the task deadlines themselves and the event TSNs all need to be updated.
   -- This is because the adjusting of the task deadlines may move that deadline
   -- either into or out of the window.
   -- Therefore, it is important that updateFollowingSchedToPlan() be called after
   -- updateFollowingTaskDeadlines() and updateFollowingEventTsn().
   updateFollowingSchedToPlan(v_ttb_deadlines);

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END process_usage_date_change;


PROCEDURE process_usage_data (
   airaw_usage_adjustment_id   IN RAW,
   aittb_assmbl_inv_db_id      IN INTEGERTABLE,
   aittb_assmbl_inv_id         IN INTEGERTABLE,
   aittb_data_type_db_id       IN INTEGERTABLE,
   aittb_data_type_id          IN INTEGERTABLE,
   aittb_new_delta             IN FLOATTABLE,
   aiv_system_note             IN CLOB,
   aiv_task_wrkpkg_system_note IN VARCHAR2,
   aiv_fault_system_note       IN VARCHAR2,
   aiv_deadline_system_note    IN VARCHAR2,
   ain_hr_db_id                IN NUMBER,
   ain_hr_id                   IN NUMBER
)
AS
   v_method_name VARCHAR2(30) := 'process_usage_data';

   v_ttb_assy_usage_deltadata INTINTINTINTFLOATTABLE;
   v_ttb_adjusted_usage_delta INTINTINTINTFLOATTABLE;
   v_ttb_inv_assmbl    INTINTINTINTTABLE;
   v_ttb_deadlines     INTINTINTINTTABLE;
   v_ttb_calc_deadline INTINTINTINTTABLE;

   vi_acft_inv_db_id INTEGER;
   vi_acft_inv_id INTEGER;
   vi_usage_dt DATE;
   vi_flight_leg RAW(16);
   vi_system_note CLOB;

   vi_is_not_latest_usage INTEGER;

BEGIN
   v_step := 10;

   -- combine set of input arrays into a table
   SELECT
      INTINTINTINTFLOATTUPLE(
         element1, element2, element3, element4, element5
      )
   BULK COLLECT INTO v_ttb_assy_usage_deltadata
   FROM
      TABLE (
         array_pkg.getINTINTINTINTFLOATTABLE (
            aittb_assmbl_inv_db_id,
            aittb_assmbl_inv_id,
            aittb_data_type_db_id,
            aittb_data_type_id,
            aittb_new_delta
         )
      );

   v_step := 15;

   v_ttb_adjusted_usage_delta := get_adjusted_usage_delta(airaw_usage_adjustment_id, v_ttb_assy_usage_deltadata);

   v_step := 16;

   -- no delta changed, exit
   IF v_ttb_adjusted_usage_delta.COUNT = 0 THEN
      RETURN;
   END IF;

   v_step := 20;

   SELECT
      inv_no_db_id, inv_no_id, usage_dt
   INTO
      vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt
   FROM
      usg_usage_record
   WHERE
      usage_record_id = airaw_usage_adjustment_id;

   v_step := 23;
   vi_is_not_latest_usage := isnotLatestUsageAdjustment(vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt);
   -- latest flight, only update current usage
   IF vi_is_not_latest_usage = 0 THEN
      processLatestUsage(vi_acft_inv_db_id, vi_acft_inv_id, airaw_usage_adjustment_id, v_ttb_adjusted_usage_delta);
      RETURN;
   END IF;

   v_step := 25;

   IF(aiv_system_note IS NOT NULL) THEN
      SELECT
         leg_id
      INTO
         vi_flight_leg
      FROM
         fl_leg
      WHERE
         usage_record_id = airaw_usage_adjustment_id;
      --add flight info to the system note
      vi_system_note := ' <flight-leg id='||vi_flight_leg||'> <br> '||aiv_system_note;
   END IF ;

   v_step := 30;

   v_ttb_inv_assmbl := get_acft_hist_config(airaw_usage_adjustment_id);

   v_step := 40;

   -- We have to update deadline first, the calculation of historic Tsn for subcomponent relays
   -- on the unmodified current tsn value and unmodified flight Tsn value.
   updateFollowingTaskDeadlines(airaw_usage_adjustment_id, v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt, vi_system_note, aiv_deadline_system_note, ain_hr_db_id, ain_hr_id, v_ttb_calc_deadline, v_ttb_deadlines);

   v_step := 50;
   updateCurrentTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_acft_inv_db_id, vi_acft_inv_id);

   v_step := 60;
   updateCurrentTso(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 70;
   updateCurrentTsi(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 80;
   updateCalculatedParameters(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl);

   v_step := 90;
   updateFollowingEventTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt, vi_system_note, aiv_task_wrkpkg_system_note, aiv_fault_system_note, ain_hr_db_id, ain_hr_id);

   v_step := 100;
   updateFollowingEditInvOfTrkSer(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 110;
   updateFollowingUsgAdjustment(v_ttb_adjusted_usage_delta, vi_usage_dt);

   v_step := 120;

   -- Prior to re-calcute task calc deadlines, current tsn and usage adjustment need to be updated.
   -- After recalcution, SchedToPlan shall be re-adjusted.
   updateFollowingCalcDeadline(v_ttb_calc_deadline);

   v_step := 130;

   -- Prior to re-adjusting task deadlines that have a schedule-to-plan window,
   -- both the task deadlines themselves and the event TSNs all need to be updated.
   -- This is because the adjusting of the task deadlines may move that deadline
   -- either into or out of the window.
   -- Therefore, it is important that updateFollowingSchedToPlan() be called after
   -- updateFollowingTaskDeadlines() and updateFollowingEventTsn().
   updateFollowingSchedToPlan(v_ttb_deadlines);

EXCEPTION
   WHEN OTHERS THEN
    v_err_code := SQLCODE;
    v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
               v_step || ', ERROR: ' || SQLERRM,
               1,
               2000);

    raise_application_error(-20001,
             v_err_msg,
             TRUE);
END process_usage_data;

/*----------------------- End of Package -----------------------------------*/
END FLIGHT_OUT_OF_SEQUENCE_PKG;
/

--changeSet OPER-30726:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE USAGE_PKG
IS

/******************************************************************************
* TYPE DECLARATIONS
******************************************************************************/

/* constant declarations (error codes) */
icn_Success                CONSTANT NUMBER := 1;
icn_NoProc                 CONSTANT NUMBER := 0;
icn_MissingInputOnInv      CONSTANT NUMBER := -4;
icn_BadEqnName             CONSTANT NUMBER := -5;
icn_OracleError            CONSTANT NUMBER := -100;

/* subtype declarations */
SUBTYPE typn_Id    IS inv_inv.inv_no_id%TYPE;
SUBTYPE typv_Cd    IS ref_data_source.data_source_cd%TYPE;
SUBTYPE typv_Sdesc IS inv_inv.inv_no_sdesc%TYPE;

/******************************************************************************
*
* Procedure:    UsageCalculations
* Arguments:    an_InvNoDbId (number): The inv_no whose
*                          calculations will be updated
*               an_InvNoId (number):   ""
*               on_Return (number): Return 1 means success, <0 means failure
* Description:  This procedure will calculate and apply any calculated parms
*               for a given inventory. It will trigger part-specific
*               calculations where required.
*
* Orig.Coder:   Andrew Hircock
* Recent Coder: Hayley Clark
* Recent Date:  9.JUN.2000
*
*******************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE UsageCalculations (
      an_InvNoDbId      IN typn_Id,
      an_InvNoId        IN typn_Id,
      on_Return         OUT NUMBER
   );

/******************************************************************************
*
* Procedure:    update_calc_parms
* Arguments:    ait_evt_inv_key: evt_inv key as a tuple
* Description:  This procedure finds any calculated parameters in the usage
*               usage snapshot associated with the inventory event.
*               It then recaclulates the values for those calculated parameters
*               and replaces those values in the snapshot.
*
*******************************************************************************/
PROCEDURE update_calc_parms(
   ait_evt_inv_key IN IntIntIntTuple
   );


/******************************************************************************
*
* Procedure:    ApplyCustomCalc
* Arguments:    an_InvNoDbId (number): The inv_no to update
*               an_InvNoId (number):   ""
*               an_CalcDbId (number): The calculation
*               an_CalcId (number):   ""
*               on_Return (number): Return 1 means success, <0 means failure
* Description:  This procedure is used to perform part-specific calculations
*               at a given location
*
* Orig.Coder:   Andrew Hircock
* Recent Coder: Hayley Clark
* Recent Date:  31.MAY.2000
*
*******************************************************************************
*
* Copyright 1998-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE ApplyCustomCalc (
      an_InvNoDbId            IN typn_Id,
      an_InvNoId              IN typn_Id,
      an_CalcDbId             IN typn_Id,
      an_CalcId               IN typn_Id,
      on_Return               OUT NUMBER
   );



/******************************************************************************
*
* Procedure:    Calculate
* Arguments:    av_CalcFunction     plsql block containing the calculation to execute
*               ai_Precision        precision to round the result
*               on_Return (number): Result of the calculation rounded with the specified precision                                   significant digits
* Description:  This procedure is used to perform a calculation
*
* Orig.Coder:   Vince Chan
* Recent Coder: Marius Secrieru
* Recent Date:  02.JUN.2011
*
*******************************************************************************
*
* Copyright 1998-2011 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE Calculate (
      av_CalcFunction            IN VARCHAR,
      ai_Precision               IN INTEGER,
      on_Return                  OUT NUMBER
   );


/******************************************************************************
*
* Procedure:     calculate_calc_parm
*
* Description:  This function executes the equation for the provided
*               calculated parameter and returns its results.
*
* Arguments:    ain_inv_no_db_id: inventory key
*               ain_inv_no_id:     "
*               ain_calc_db_id: calculated parameter key
*               ain_calc_id:     "
*               aitab_usages: table of usages (inv key, data type key, usage value)
*               aon_calc_usage: calculated usage value
*
*******************************************************************************/

PROCEDURE calculate_calc_parm(
   ain_inv_no_db_id   IN inv_inv.inv_no_db_id%TYPE,
   ain_inv_no_id      IN inv_inv.inv_no_id%TYPE,
   ain_calc_db_id     IN mim_calc.calc_db_id%TYPE,
   ain_calc_id        IN mim_calc.calc_id%TYPE,
   aitab_usages       IN IntIntIntIntFLOATTABLE,
   aon_calc_usage     OUT NUMBER
);


/******************************************************************************
*
* Procedure:    takeCurrentUsageSnapshotByEvent
*
* Description:  This procedure inserts records into evt_inv_usage for the
*               provided event using the usage of the inventory associated
*               to the event at the time of the event.
*
* Arguments:    an_event_db_id  event key
*               an_event_id     "
*
*******************************************************************************/
PROCEDURE takeUsageSnapshotOfEvent(
   an_event_db_id   IN NUMBER,
   an_event_id      IN NUMBER
);


/******************************************************************************
*
* Procedure:    recalculateEventCalcParms
*
* Description:  This procedure recalculates all the calculated parameters
*               within the inventory usage snapshots associated with an event.
*
* Arguments:    an_event_db_id  event key
*               an_event_id     "
*
*******************************************************************************/
PROCEDURE recalculateEventCalcParms(
   an_event_db_id   IN NUMBER,
   an_event_id      IN NUMBER
);


/******************************************************************************
*
* Function:     getAcftAssyUsageOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for the provided aircraft or assembly on the provided date.
*
* Arguments:    ain_inv_no_db_id  inventory key
*               ain_inv_no_id     "
*               aidt_date         date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages
*
* Note: query copied from
*       mxcoreejb\src\main\resources\com\mxi\mx\core\query\usage\FindUsageAtDate.qrx
*
*******************************************************************************/
FUNCTION getAcftAssyUsageOnDate(
   aittb_inv_no_db_id  IN INTEGERTABLE,
   aittb_inv_no_id     IN INTEGERTABLE,
   aidt_date           IN DATE
) RETURN IntIntIntIntFLOATTABLE;


/******************************************************************************
*
* Function:     getSysUsageOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for the provided SYS "inventory" on the provided date.
*
*               It is assumed that the aircraft/assembly under which the
*               SYS inventory is located will never move.
*               And that the usage of the SYS will always equal that of
*               the aircraft/assembly which it is under.
*
* Arguments:    ain_inv_no_db_id  inventory key
*               ain_inv_no_id     "
*               aidt_date         date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages
*
* Note: query copied from
*       mxcoreejb\src\main\resources\com\mxi\mx\core\query\usage\FindUsageAtDate.qrx
*
*******************************************************************************/
FUNCTION getSysUsageOnDate(
   aittb_inv_no_db_id  IN INTEGERTABLE,
   aittb_inv_no_id     IN INTEGERTABLE,
   aidt_date           IN DATE
) RETURN IntIntIntIntFLOATTABLE;


/******************************************************************************
*
* Function:     getTrkSerUsagesOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for any TRK or SER inventory contained within
*               the provided inventories on the provided date.
*
* Arguments:    aittb_inv_no_db_id  table of inventory keys
*               aittb_inv_no_id     "
*               aidt_date           date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages for any TRK or SER inventories
*
*******************************************************************************/
FUNCTION getTrkSerUsagesOnDate(
   aittb_inv_no_db_id  IN INTEGERTABLE,
   aittb_inv_no_id     IN INTEGERTABLE,
   aidt_date           IN DATE
) RETURN IntIntIntIntFLOATTABLE;


/******************************************************************************
*
* Function:     getInvUsageOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for the provided inventory on the provided date.
*
* Arguments:    ai_inv_no_db_id  inventory key
*               ai_inv_no_id     "
*               aidt_date         date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages
*
*******************************************************************************/
FUNCTION getInvUsageOnDate(
   ai_inv_no_db_id  IN NUMBER,
   ai_inv_no_id     IN NUMBER,
   aidt_date        IN DATE
) RETURN IntIntIntIntFLOATTABLE;

END USAGE_PKG;
/

--changeSet OPER-30726:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY USAGE_PKG
IS

   xc_BadEqnName EXCEPTION;
   gv_error_info  VARCHAR2(1000) := '';


/******************************************************************************
*
* Function:     calculate_calc_parm
*               (local function)
*
* Arguments:    ain_inv_no_db_id: inventory key
*               ain_inv_no_id:     "
*               ain_calc_db_id: calculated parameter key
*               ain_calc_id:     "
*               aitab_usages: table of usages (inv key, data type key, usage value)
*               aon_calc_usage: calculated usage value
*
* Description:  This function executes the equation for the provided
*               calculated parameter and returns its results.
*
*               Calculated parameters are configured to have:
*                - an equation (plsql function in the DB)
*                - usage parameters
*                - constants (defaults and part specific)
*               The equation and constants are pre-configured in the DB.
*               The usage parameter values (passed to the equation) are
*               extracted from the provided usages table.
*               When determining whether the default or a part specific constant
*               is to be used, the part of the provided inventory is checked.
*
*               Example: if the equations requires the value of the HOURS
*               usage parameter, then the aitab_usages is accessed to find
*               the value corresponding to the inventory-key and HOURS.
*               If the equation requires a particular constant then the
*               inventory-key is used to determine its part and if there exists
*               a part specific constant for that part then it is used
*               otherwise, the default constant is used.
*
*******************************************************************************/
PROCEDURE calculate_calc_parm(
   ain_inv_no_db_id   IN inv_inv.inv_no_db_id%TYPE,
   ain_inv_no_id      IN inv_inv.inv_no_id%TYPE,
   ain_calc_db_id     IN mim_calc.calc_db_id%TYPE,
   ain_calc_id        IN mim_calc.calc_id%TYPE,
   aitab_usages       IN INTINTINTINTFLOATTABLE,
   aon_calc_usage     OUT NUMBER
)
IS
   lv_sql_str  VARCHAR2(4000);

   ln_expected_param_num INTEGER;
   ln_actual_param_num INTEGER;

   -- Note: the cursors used in this function were moved here from the existing
   --       function ApplyCustomCalc()

   -- Cursor used to get the equation and its arguments.
   -- Part specific constants are returned instead of the default constant
   -- based on the inventory's part number.
   CURSOR lcur_equation (
         cn_InvNoDbId      typn_Id,
         cn_InvNoId        typn_Id,
         cn_CalcDbId       typn_Id,
         cn_CalcId         typn_Id
      ) IS
      SELECT mim_calc.eqn_ldesc,
             mim_calc.data_type_db_id,
             mim_calc.data_type_id,
             mim_data_type.data_type_cd,
             mim_data_type.entry_prec_qt
        FROM mim_calc,
             mim_data_type
       WHERE mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND mim_data_type.data_type_db_id = mim_calc.data_type_db_id
         AND mim_data_type.data_type_id    = mim_calc.data_type_id
         AND NOT EXISTS
             ( SELECT 1
                 FROM inv_inv,
                      mim_part_numdata
                WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
                  AND inv_inv.inv_no_id    = cn_InvNoId
                  AND inv_inv.assmbl_bom_id <> 0
                  AND inv_inv.rstat_cd     = 0
                  AND mim_part_numdata.assmbl_db_id  = inv_inv.assmbl_db_id
                  AND mim_part_numdata.assmbl_cd     = inv_inv.assmbl_cd
                  AND mim_part_numdata.assmbl_bom_id = inv_inv.assmbl_bom_id
                  AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
                  AND mim_part_numdata.data_type_id    = mim_calc.data_type_id
                  AND mim_part_numdata.eqn_ldesc IS NOT NULL
                UNION
                SELECT 1
                 FROM inv_inv,
                      mim_part_numdata
                WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
                  AND inv_inv.inv_no_id    = cn_InvNoId
                  AND inv_inv.rstat_cd     = 0
                  AND mim_part_numdata.assmbl_db_id  = inv_inv.orig_assmbl_db_id
                  AND mim_part_numdata.assmbl_cd     = inv_inv.orig_assmbl_cd
                  AND mim_part_numdata.assmbl_bom_id = 0
                  AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
                  AND mim_part_numdata.data_type_id    = mim_calc.data_type_id
                  AND mim_part_numdata.eqn_ldesc IS NOT NULL )
      UNION ALL
      SELECT mim_part_numdata.eqn_ldesc,
             mim_calc.data_type_db_id,
             mim_calc.data_type_id,
             mim_data_type.data_type_cd,
             mim_data_type.entry_prec_qt
        FROM mim_calc,
             inv_inv,
             mim_part_numdata,
             mim_data_type
       WHERE mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd     = 0
         AND inv_inv.assmbl_bom_id <> 0
         AND mim_part_numdata.assmbl_db_id  = inv_inv.assmbl_db_id
         AND mim_part_numdata.assmbl_cd     = inv_inv.assmbl_cd
         AND mim_part_numdata.assmbl_bom_id = inv_inv.assmbl_bom_id
         AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
         AND mim_part_numdata.data_type_id    = mim_calc.data_type_id
         AND mim_part_numdata.eqn_ldesc IS NOT NULL
         AND mim_data_type.data_type_db_id = mim_calc.data_type_db_id
         AND mim_data_type.data_type_id    = mim_calc.data_type_id
       UNION
       SELECT mim_part_numdata.eqn_ldesc,
             mim_calc.data_type_db_id,
             mim_calc.data_type_id,
             mim_data_type.data_type_cd,
             mim_data_type.entry_prec_qt
        FROM mim_calc,
             inv_inv,
             mim_part_numdata,
             mim_data_type
       WHERE mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd     = 0
         AND mim_part_numdata.assmbl_db_id  = inv_inv.orig_assmbl_db_id
         AND mim_part_numdata.assmbl_cd     = inv_inv.orig_assmbl_cd
         AND mim_part_numdata.assmbl_bom_id = 0
         AND mim_part_numdata.data_type_db_id = mim_calc.data_type_db_id
         AND mim_part_numdata.data_type_id    = mim_calc.data_type_id
         AND mim_part_numdata.eqn_ldesc IS NOT NULL
         AND mim_data_type.data_type_db_id = mim_calc.data_type_db_id
         AND mim_data_type.data_type_id    = mim_calc.data_type_id ;
   lrec_Equation lcur_equation%ROWTYPE;

   -- Cursor to get the input values for the equation.
   CURSOR lcur_InputValues (
         cn_InvNoDbId      typn_Id,
         cn_InvNoId        typn_Id,
         cn_CalcDbId       typn_Id,
         cn_CalcId         typn_Id,
         ct_Usages         INTINTINTINTFLOATTABLE
      ) IS
      -- Get data_type inputs (never part-specific).
      SELECT usages.usage_qt,
             mim_calc_input.input_ord
        FROM inv_inv,
             mim_calc_input,
            (SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id,
               element3 as data_type_db_id,
               element4 as data_type_id,
               element5 as usage_qt
             FROM
                TABLE (ct_Usages)
            ) usages
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd     = 0
         AND mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_calc_input.constant_bool = 0
         AND usages.inv_no_db_id = inv_inv.inv_no_db_id
         AND usages.inv_no_id    = inv_inv.inv_no_id
         AND usages.data_type_db_id = mim_calc_input.data_type_db_id
         AND usages.data_type_id    = mim_calc_input.data_type_id
      UNION ALL
      -- Get non part-specific constant values.
      SELECT mim_calc_input.input_qt AS usage_qt,
             mim_calc_input.input_ord
        FROM mim_calc_input
       WHERE mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_calc_input.constant_bool = 1
         AND NOT EXISTS
             -- Make sure there are no part-specific constant values.
             ( SELECT 1
                 FROM inv_inv,
                      mim_part_input,
                      eqp_bom_part
                WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
                  AND inv_inv.inv_no_id    = cn_InvNoId
                  AND inv_inv.assmbl_bom_id <> 0
                  AND inv_inv.rstat_cd      = 0
                  AND mim_part_input.calc_db_id = mim_calc_input.calc_db_id
                  AND mim_part_input.calc_id    = mim_calc_input.calc_id
                  AND mim_part_input.input_id   = mim_calc_input.input_id
                  AND eqp_bom_part.assmbl_db_id    = inv_inv.assmbl_db_id
                  AND eqp_bom_part.assmbl_cd       = inv_inv.assmbl_cd
                  AND eqp_bom_part.assmbl_bom_id   = inv_inv.assmbl_bom_id
                  AND mim_part_input.bom_part_db_id= eqp_bom_part.bom_part_db_id
                  AND mim_part_input.bom_part_id   = eqp_bom_part.bom_part_id
                  AND mim_part_input.part_no_db_id = inv_inv.part_no_db_id
                  AND mim_part_input.part_no_id    = inv_inv.part_no_id
                UNION
                SELECT 1
                 FROM inv_inv,
                      mim_part_input,
                      eqp_bom_part
                WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
                  AND inv_inv.inv_no_id    = cn_InvNoId
                  AND inv_inv.rstat_cd     = 0
                  AND mim_part_input.calc_db_id = mim_calc_input.calc_db_id
                  AND mim_part_input.calc_id    = mim_calc_input.calc_id
                  AND mim_part_input.input_id   = mim_calc_input.input_id
                  AND eqp_bom_part.assmbl_db_id    = inv_inv.orig_assmbl_db_id
                  AND eqp_bom_part.assmbl_cd       = inv_inv.orig_assmbl_cd
                  AND eqp_bom_part.assmbl_bom_id   = 0
                  AND mim_part_input.bom_part_db_id= eqp_bom_part.bom_part_db_id
                  AND mim_part_input.bom_part_id   = eqp_bom_part.bom_part_id
                  AND mim_part_input.part_no_db_id = inv_inv.part_no_db_id
                  AND mim_part_input.part_no_id    = inv_inv.part_no_id)
      UNION ALL
      -- Get part-specific constant values.
      (SELECT mim_part_input.input_qt AS usage_qt,
             mim_calc_input.input_ord
        FROM inv_inv,
             mim_calc_input,
             mim_part_input,
             eqp_bom_part
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.assmbl_bom_id <> 0
         AND inv_inv.rstat_cd    = 0
         AND mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_part_input.calc_db_id = mim_calc_input.calc_db_id
         AND mim_part_input.calc_id    = mim_calc_input.calc_id
         AND mim_part_input.input_id   = mim_calc_input.input_id
         AND eqp_bom_part.assmbl_db_id    = inv_inv.assmbl_db_id
         AND eqp_bom_part.assmbl_cd       = inv_inv.assmbl_cd
         AND eqp_bom_part.assmbl_bom_id   = inv_inv.assmbl_bom_id
         AND mim_part_input.bom_part_db_id= eqp_bom_part.bom_part_db_id
         AND mim_part_input.bom_part_id   = eqp_bom_part.bom_part_id
         AND mim_part_input.part_no_db_id = inv_inv.part_no_db_id
         AND mim_part_input.part_no_id    = inv_inv.part_no_id
       UNION
       SELECT mim_part_input.input_qt AS usage_qt,
             mim_calc_input.input_ord
        FROM inv_inv,
             mim_calc_input,
             mim_part_input,
             eqp_bom_part
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId
         AND inv_inv.inv_no_id    = cn_InvNoId
         AND inv_inv.rstat_cd    = 0
         AND mim_calc_input.calc_db_id = cn_CalcDbId
         AND mim_calc_input.calc_id    = cn_CalcId
         AND mim_part_input.calc_db_id = mim_calc_input.calc_db_id
         AND mim_part_input.calc_id    = mim_calc_input.calc_id
         AND mim_part_input.input_id   = mim_calc_input.input_id
         AND eqp_bom_part.assmbl_db_id    = inv_inv.orig_assmbl_db_id
         AND eqp_bom_part.assmbl_cd       = inv_inv.orig_assmbl_cd
         AND eqp_bom_part.assmbl_bom_id   = 0
         AND mim_part_input.bom_part_db_id= eqp_bom_part.bom_part_db_id
         AND mim_part_input.bom_part_id   = eqp_bom_part.bom_part_id
         AND mim_part_input.part_no_db_id = inv_inv.part_no_db_id
         AND mim_part_input.part_no_id    = inv_inv.part_no_id)
      ORDER BY 2 ASC;

BEGIN

   OPEN lcur_equation(ain_inv_no_db_id, ain_inv_no_id, ain_calc_db_id, ain_calc_id);
   FETCH lcur_equation INTO lrec_Equation;
   CLOSE lcur_equation;

   -- Retrieve required number of parameters
   SELECT
      count(1) INTO ln_expected_param_num
   FROM
      mim_calc_input
   WHERE
      mim_calc_input.calc_db_id = ain_calc_db_id AND
      mim_calc_input.calc_id = ain_calc_id;

   -- Generate the dynamic SQL for calling the equation.
   lv_sql_str := 'BEGIN :res := ' || lrec_Equation.eqn_ldesc || '(';

   ln_actual_param_num := 0;
   FOR lrec_InputValues IN lcur_InputValues(ain_inv_no_db_id,
                                            ain_inv_no_id,
                                            ain_calc_db_id,
                                            ain_calc_id,
                                            aitab_usages)
   LOOP
      lv_sql_str := lv_sql_str ||  REPLACE(lrec_InputValues.usage_qt,',','.') || ',';
      ln_actual_param_num := ln_actual_param_num + 1;
   END LOOP;

   lv_sql_str := RTRIM(lv_sql_str, ',') || ');END;';

   -- if expected number of parameters matches actual number
   IF ln_expected_param_num = ln_actual_param_num THEN
   -- ...then execute the dynamic SQL and get the result.
      Calculate(lv_sql_str, lrec_Equation.Entry_Prec_Qt, aon_calc_usage);
   END IF;

END calculate_calc_parm;


/******************************************************************************
*
* Procedure:    update_calc_parms
* Arguments:    ait_evt_inv_key: evt_inv key as a tuple
* Description:  This procedure finds any calculated parameters in the usage
*               usage snapshot associated with the inventory event.
*               It then recaclulates the values for those calculated parameters
*               and replaces those values in the snapshot.
*
*******************************************************************************/
PROCEDURE update_calc_parms(
   ait_evt_inv_key IN INTINTINTTUPLE
)
IS

   ln_event_db_id NUMBER;
   ln_event_id NUMBER;
   ln_event_inv_id NUMBER;
   ltab_usages INTINTINTINTFLOATTABLE;
   ln_tsn NUMBER;

   CURSOR lcur_calc_parm_list IS
      SELECT
         evt_inv.event_db_id AS event_db_id,
         evt_inv.event_id AS event_id,
         evt_inv.event_inv_id,
         evt_inv.inv_no_db_id,
         evt_inv.inv_no_id,
         evt_inv_usage.data_type_db_id,
         evt_inv_usage.data_type_id,
         mim_calc.calc_db_id,
         mim_calc.calc_id
      FROM
         evt_inv_usage
      INNER JOIN mim_calc ON
         mim_calc.data_type_db_id = evt_inv_usage.data_type_db_id AND
         mim_calc.data_type_id    = evt_inv_usage.data_type_id
      INNER JOIN evt_inv ON
         evt_inv_usage.event_db_id  = evt_inv.event_db_id AND
         evt_inv_usage.event_id     = evt_inv.event_id AND
         evt_inv_usage.event_inv_id = evt_inv.event_inv_id
      INNER JOIN inv_inv ON
         inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
         inv_inv.inv_no_id    = evt_inv.inv_no_id
      WHERE
         evt_inv_usage.event_db_id  = ln_event_db_id AND
         evt_inv_usage.event_id     = ln_event_id AND
         evt_inv_usage.event_inv_id = ln_event_inv_id
         AND
         (
            (
               -- For SER,KIT,BATCH the assembly cannot be determed, so return all matching mim_calc rows.
               inv_inv.inv_class_db_id = 0 AND
               inv_inv.inv_class_cd    IN ('SER','KIT','BATCH')
            )
            OR
            (
               -- For ASSY use the original assembly of the inventory.
               inv_inv.inv_class_db_id = 0 AND
               inv_inv.inv_class_cd    = 'ASSY'
               AND
               mim_calc.assmbl_db_id = inv_inv.orig_assmbl_db_id AND
               mim_calc.assmbl_cd    = inv_inv.orig_assmbl_cd
            )
            OR
            (
               -- For any other class use the assembly of the inventory.
               NOT
               (
                  inv_inv.inv_class_db_id = 0 AND
                  inv_inv.inv_class_cd    = 'ASSY'
               )
               AND
               mim_calc.assmbl_db_id = inv_inv.assmbl_db_id AND
               mim_calc.assmbl_cd    = inv_inv.assmbl_cd
            )
         );

BEGIN

   ln_event_db_id  := ait_evt_inv_key.element1;
   ln_event_id     := ait_evt_inv_key.element2;
   ln_event_inv_id := ait_evt_inv_key.element3;

   FOR lrec_calc_parm_list IN lcur_calc_parm_list  LOOP

      -- Get all usages for the event on the inventory
      -- which may be used by the calculate parms equation.
      SELECT
         INTINTINTINTFLOATTUPLE(
              lrec_calc_parm_list.inv_no_db_id,
              lrec_calc_parm_list.inv_no_id,
              evt_inv_usage.data_type_db_id,
              evt_inv_usage.data_type_id,
              evt_inv_usage.tsn_qt
          )
      BULK COLLECT INTO ltab_usages
      FROM
         evt_inv_usage
      WHERE
         evt_inv_usage.event_db_id  = lrec_calc_parm_list.event_db_id AND
         evt_inv_usage.event_id     = lrec_calc_parm_list.event_id AND
         evt_inv_usage.event_inv_id = lrec_calc_parm_list.event_inv_id;

      -- Use the equation of the calculated parameter to calculate the new TSN value.
      -- For event snapshots we are only concerned with TSN (not tso or tsi).
      calculate_calc_parm(lrec_calc_parm_list.inv_no_db_id,
                          lrec_calc_parm_list.inv_no_id,
                          lrec_calc_parm_list.calc_db_id,
                          lrec_calc_parm_list.calc_id,
                          ltab_usages,
                          ln_tsn
                          );

      -- Update the calculated parameter's TSN with the new value.
      UPDATE
         evt_inv_usage
      SET
         evt_inv_usage.tsn_qt = ln_tsn
      WHERE
         evt_inv_usage.event_db_id     = lrec_calc_parm_list.event_db_id AND
         evt_inv_usage.event_id        = lrec_calc_parm_list.event_id AND
         evt_inv_usage.event_inv_id    = lrec_calc_parm_list.event_inv_id AND
         evt_inv_usage.data_type_db_id = lrec_calc_parm_list.data_type_db_id AND
         evt_inv_usage.data_type_id    = lrec_calc_parm_list.data_type_id;
   END LOOP;

END update_calc_parms;

/******************************************************************************
*
* Procedure:    UsageCalculations
* Arguments:    an_InvNoDbId (number): The inv_no whose
*                          calculations will be updated
*               an_InvNoId (number):   ""
*               on_Return (number): Return 1 means success, <0 means failure
* Description:  This procedure will calculate and apply any calculated parms
*               for a given inventory. It will trigger part-specific
*               calculations where required (note: if the inventory is an
*           assembly, all subcomponents will also be updated.
*
* Orig.Coder:   Andrew Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*******************************************************************************
*
* Copyright 1999-2000 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE UsageCalculations (
      an_InvNoDbId      IN typn_Id,
      an_InvNoId        IN typn_Id,
      on_Return         OUT NUMBER
   ) IS

   /* CURSOR DECLARATIONS */
   /* list of calculations for a given inventory */
   CURSOR lcur_CalcList (
         cn_InvNoDbId      typn_Id,
         cn_InvNoId        typn_Id
      ) IS
      SELECT mim_calc.calc_db_id,
             mim_calc.calc_id
      FROM inv_inv,
             mim_calc
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId AND
             inv_inv.inv_no_id    = cn_InvNoId   AND
             inv_inv.rstat_cd     = 0
             AND
             mim_calc.assmbl_db_id = inv_inv.assmbl_db_id AND
             mim_calc.assmbl_cd    = inv_inv.assmbl_cd
      UNION
      SELECT mim_calc.calc_db_id,
             mim_calc.calc_id
      FROM inv_inv,
             mim_calc
       WHERE inv_inv.inv_no_db_id = cn_InvNoDbId AND
             inv_inv.inv_no_id    = cn_InvNoId   AND
             inv_inv.rstat_cd     = 0
             AND
             mim_calc.assmbl_db_id = inv_inv.orig_assmbl_db_id AND
             mim_calc.assmbl_cd    = inv_inv.orig_assmbl_cd;

   /* get a list of all inventory which require custom calculation */
   CURSOR lcur_CalcInv (
         cn_AssmblInvNoDbId   typn_Id,
         cn_AssmblInvNoId     typn_Id,
         cn_CalcDbId          typn_Id,
         cn_CalcId            typn_Id
      ) IS
      /* list the assembly itself */
      SELECT inv_curr_usage.inv_no_db_id,
             inv_curr_usage.inv_no_id
        FROM mim_calc,
             inv_curr_usage
       WHERE mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND inv_curr_usage.inv_no_db_id = cn_AssmblInvNoDbId
         AND inv_curr_usage.inv_no_id    = cn_AssmblInvNoId
         AND inv_curr_usage.data_type_db_id = mim_calc.data_type_db_id
         AND inv_curr_usage.data_type_id    = mim_calc.data_type_id
      UNION
     /* list sub-inventory which have this calc */
      SELECT inv_inv.inv_no_db_id,
             inv_inv.inv_no_id
        FROM inv_inv,
             mim_calc,
             inv_curr_usage
       WHERE inv_inv.assmbl_inv_no_db_id = cn_AssmblInvNoDbId
         AND inv_inv.assmbl_inv_no_id    = cn_AssmblInvNoId
         AND inv_inv.rstat_cd            = 0
         AND mim_calc.calc_db_id = cn_CalcDbId
         AND mim_calc.calc_id    = cn_CalcId
         AND inv_curr_usage.inv_no_db_id = inv_inv.inv_no_db_id
         AND inv_curr_usage.inv_no_id    = inv_inv.inv_no_id
         AND inv_curr_usage.data_type_db_id = mim_calc.data_type_db_id
         AND inv_curr_usage.data_type_id    = mim_calc.data_type_id;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   /* loop through all of the discovered calculations */
   FOR lrec_CalcList IN lcur_CalcList(an_InvNoDbId, an_InvNoId) Loop
     /* *** PERFORM CUSTOM CALCULATIONS *** */

      /* get a list of all inventory which require custom calculation of the
         result */
      FOR lrec_CalcInv IN lcur_CalcInv(
                        an_InvNoDbId,
                        an_InvNoId,
                        lrec_CalcList.calc_db_id,
                        lrec_CalcList.calc_id) LOOP

         /* run the custom calculation for each inventory */
          ApplyCustomCalc(lrec_CalcInv.inv_no_db_id,
                      lrec_CalcInv.inv_no_id,
                      lrec_CalcList.calc_db_id,
                      lrec_CalcList.calc_id,
                      on_Return );
         IF on_Return < 0 THEN
            RETURN;
         END IF;
      END LOOP;
   END LOOP;

   /* return success */
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_OracleError;
      application_object_pkg.SetMxiError('DEV-99999', 'USAGE_PKG@@@UsageCalculations@@@' || SQLERRM);
END UsageCalculations;


/******************************************************************************
*
* Procedure:    ApplyCustomCalc
* Arguments:    an_InvNoDbId (number): The inv_no to update
*               an_InvNoId (number):   ""
*               an_CalcDbId (number): The calculation
*               an_CalcId (number):   ""
*               on_Return (number): Return 1 means success, <0 means failure
* Description:  This procedure is used to perform part-specific calculations
*               at a given location
*
* Orig.Coder:   Andrew Hircock
* Recent Coder: cjb
* Recent Date:  February 27, 2005
*
*******************************************************************************
*
* Copyright 1999-2001 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE ApplyCustomCalc (
      an_InvNoDbId            IN typn_Id,
      an_InvNoId              IN typn_Id,
      an_CalcDbId             IN typn_Id,
      an_CalcId               IN typn_Id,
      on_Return               OUT NUMBER
   ) IS

   lv_MissingDataType    VARCHAR2(809);
   ltab_currentUsages    INTINTINTINTFLOATTABLE;
   xc_MissingInputOnInv  EXCEPTION;
   ln_tsn NUMBER;
   ln_tso NUMBER;
   ln_tsi NUMBER;

BEGIN
   -- Initialize the return value.
   on_Return := icn_NoProc;

   -- Make sure that all of the input parms are specified for the inventory.
   BEGIN
      SELECT
         LISTAGG(mim_data_type.data_type_cd,',') Within GROUP (ORDER BY 1)
      INTO
         lv_MissingDataType
      FROM
         mim_calc_input
         INNER JOIN mim_data_type ON
            mim_data_type.data_type_db_id = mim_calc_input.data_type_db_id AND
            mim_data_type.data_type_id    = mim_calc_input.data_type_id
      WHERE
         mim_calc_input.calc_db_id = an_CalcDbId AND
         mim_calc_input.calc_id    = an_CalcId
         AND
         mim_calc_input.constant_bool = 0
         AND
         NOT EXISTS
         (
            SELECT
               1
            FROM
               inv_curr_usage
            WHERE
               inv_no_db_id    = an_InvNoDbId AND
               inv_no_id       = an_InvNoId AND
               data_type_db_id = mim_calc_input.data_type_db_id AND
               data_type_id    = mim_calc_input.data_type_id
         );

      IF lv_MissingDataType IS NOT NULL THEN
         RAISE xc_MissingInputOnInv;
      END IF;

   EXCEPTION
      WHEN NO_DATA_FOUND THEN
         NULL;
      WHEN xc_MissingInputOnInv THEN
         RAISE xc_MissingInputOnInv;
   END;

   -- Collect the TSN for all the current inventory usages
   -- that the calculated parameter may need.
   SELECT
      INTINTINTINTFLOATTUPLE(
           inv_curr_usage.inv_no_db_id,
           inv_curr_usage.inv_no_id,
           inv_curr_usage.data_type_db_id,
           inv_curr_usage.data_type_id,
           inv_curr_usage.tsn_qt
       )
   BULK COLLECT INTO ltab_currentUsages
   FROM
      inv_curr_usage
   WHERE
      inv_curr_usage.inv_no_db_id = an_InvNoDbId AND
      inv_curr_usage.inv_no_id    = an_InvNoId
   ;

   -- Calculate the TSN value of the calculated parameter.
   calculate_calc_parm(an_InvNoDbId,
                       an_InvNoId,
                       an_CalcDbId,
                       an_CalcId,
                       ltab_currentUsages,
                       ln_tsn);

   -- Repeat for the TSO.
   SELECT
      INTINTINTINTFLOATTUPLE(
           inv_curr_usage.inv_no_db_id,
           inv_curr_usage.inv_no_id,
           inv_curr_usage.data_type_db_id,
           inv_curr_usage.data_type_id,
           inv_curr_usage.tso_qt
       )
   BULK COLLECT INTO ltab_currentUsages
   FROM
      inv_curr_usage
   WHERE
      inv_curr_usage.inv_no_db_id = an_InvNoDbId AND
      inv_curr_usage.inv_no_id    = an_InvNoId
   ;

   calculate_calc_parm(an_InvNoDbId,
                       an_InvNoId,
                       an_CalcDbId,
                       an_CalcId,
                       ltab_currentUsages,
                       ln_tso);

   -- Repeat for the TSI.
   SELECT
      INTINTINTINTFLOATTUPLE(
           inv_curr_usage.inv_no_db_id,
           inv_curr_usage.inv_no_id,
           inv_curr_usage.data_type_db_id,
           inv_curr_usage.data_type_id,
           inv_curr_usage.tsi_qt
       )
   BULK COLLECT INTO ltab_currentUsages
   FROM
      inv_curr_usage
   WHERE
      inv_curr_usage.inv_no_db_id = an_InvNoDbId AND
      inv_curr_usage.inv_no_id    = an_InvNoId
   ;

   calculate_calc_parm(an_InvNoDbId,
                       an_InvNoId,
                       an_CalcDbId,
                       an_CalcId,
                       ltab_currentUsages,
                       ln_tsi);

   -- Apply the new values to all inventory in the assembly.
   -- Use the same calculated value for all TSN/TSI/TSO.
   UPDATE
      inv_curr_usage
   SET
      tsn_qt = ln_tsn,
      tso_qt = ln_tso,
      tsi_qt = ln_tsi
   WHERE
      inv_no_db_id    = an_InvNoDbId AND
      inv_no_id       = an_InvNoId AND
      (data_type_db_id, data_type_id) IN (
         -- use the data type key for the calc parm
         SELECT
            mim_calc.data_type_db_id,
            mim_calc.data_type_id
         FROM
            mim_calc
         WHERE
            mim_calc.calc_db_id = an_CalcDbId AND
            mim_calc.calc_id    = an_CalcId
      )
   ;

   on_Return := icn_Success;

EXCEPTION
   WHEN xc_MissingInputOnInv THEN
      on_Return := icn_MissingInputOnInv;
      application_object_pkg.SetMxiError(
         'BUS-00275',
         an_InvNoDbId || ':' || an_InvNoId || '|' || an_CalcDbId  || ':' || an_CalcId || '|' || lv_MissingDataType);
   WHEN xc_BadEqnName THEN
      on_Return := icn_BadEqnName;
      application_object_pkg.SetMxiError(
         'BUS-00276',
         an_InvNoDbId || ':' || an_InvNoId || '|' || an_CalcDbId  || ':' || an_CalcId || '|' || gv_error_info);
   WHEN OTHERS THEN
      on_Return := icn_OracleError;
      application_object_pkg.SetMxiError('DEV-99999', 'USAGE_PKG@@@ApplyCustomCalc@@@' || SQLERRM);
END ApplyCustomCalc;


/******************************************************************************
*
* Procedure:    Calculate
* Arguments:    av_CalcFunction     plsql block containing the calculation to execute
*               ai_Precision        precision to round the result
*               on_Return (number): Result of the calculation rounded with the specified precision                                   significant digits
* Description:  This procedure is used to perform a calculation
*
* Orig.Coder:   Vince Chan
* Recent Coder: Marius Secrieru
* Recent Date:  02.JUN.2011
*
*******************************************************************************
*
* Copyright 1998-2011 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*******************************************************************************/
PROCEDURE Calculate (
      av_CalcFunction            IN VARCHAR,
      ai_Precision               IN INTEGER,
      on_Return                  OUT NUMBER
   ) IS
    li_FuncCursor         INTEGER; /* variables used to hold DBMS info */
    ln_CalcResult         NUMBER; /* Result from the calculation*/
    li_RowsProcessed      INTEGER;
BEGIN

   li_FuncCursor := DBMS_SQL.open_cursor;
   DBMS_SQL.parse(li_FuncCursor, av_CalcFunction, DBMS_SQL.v7);

   /* bind all variables used by the dynamic SQL statement */
   DBMS_SQL.bind_variable(li_FuncCursor, ':res', ln_CalcResult);

   /* execute the dynamic SQL statement */
   BEGIN
      li_RowsProcessed := DBMS_SQL.execute(li_FuncCursor);
   EXCEPTION
      WHEN OTHERS THEN
         gv_error_info := av_CalcFunction;
         RAISE xc_BadEqnName;
   END;

   /* pull out any PL/SQL variables from the dynamic SQL statement */
   DBMS_SQL.variable_value(li_FuncCursor, ':res', ln_CalcResult);

   /* close the dynamic SQL statement */
   DBMS_SQL.close_cursor(li_FuncCursor);

   /* Round the result to specified precision */
   on_Return := ROUND(ln_CalcResult, ai_Precision);

   EXCEPTION
   WHEN OTHERS THEN
      /*close any open cursor*/
      IF DBMS_SQL.is_OPEN(li_FuncCursor) THEN
         DBMS_SQL.close_cursor(li_FuncCursor);
      END IF;
      /*Reraise the exception*/
      RAISE ;

END Calculate;


/******************************************************************************
*
* Procedure:    takeCurrentUsageSnapshotByEvent
*
* Description:  This procedure inserts records into evt_inv_usage for the
*               provided event using the usage of the inventory associated
*               to the event at the time of the event.
*
* Arguments:    an_event_db_id  event key
*               an_event_id     "
*
*******************************************************************************/
PROCEDURE takeUsageSnapshotOfEvent(
   an_event_db_id   IN NUMBER,
   an_event_id      IN NUMBER
)
IS
   ld_date               evt_event.event_dt%TYPE;

   ltb_inv_no_db_ids     INTEGERTABLE;
   ltb_inv_no_ids        INTEGERTABLE;
   ltb_trk_usages        INTINTINTINTFLOATTABLE;
   ltb_sys_usages        INTINTINTINTFLOATTABLE;
   ltb_ac_usages         INTINTINTINTFLOATTABLE;
   ltb_evt_inv           INTINTINTTABLE;
   ln_calc_parm_count    NUMBER;
   v_records             INTINTINTINTFLOATTABLE;

BEGIN

   SELECT
      evt_event.event_dt
   INTO
      ld_date
   FROM
      evt_event
   WHERE
      evt_event.event_db_id = an_event_db_id AND
      evt_event.event_id    = an_event_id;

   IF (ld_date IS NULL) THEN
      RETURN;
   END IF;

   SELECT
      INTINTINTTUPLE(
         evt_inv.inv_no_db_id,
         evt_inv.inv_no_id,
         evt_inv.event_inv_id
      )
   BULK COLLECT INTO ltb_evt_inv
   FROM
      evt_inv
   WHERE
      evt_inv.event_db_id = an_event_db_id AND
      evt_inv.event_id    = an_event_id;

   SELECT
      element1
   BULK COLLECT INTO ltb_inv_no_db_ids
   FROM
      TABLE(ltb_evt_inv)
   ORDER BY
      element3;

   SELECT
      element2
   BULK COLLECT INTO ltb_inv_no_ids
   FROM
      TABLE(ltb_evt_inv)
   ORDER BY
      element3;

   -- Get the TRK or SER usages.
   ltb_trk_usages := USAGE_PKG.getTrkSerUsagesOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, ld_date);

   -- Get the ACFT or ASSY usages.
   ltb_ac_usages := USAGE_PKG.getAcftAssyUsageOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, ld_date);

   -- Get the SYS usages.
   ltb_sys_usages := USAGE_PKG.getSysUsageOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, ld_date);

   -- Create a collection of all the usage records we want to upsert to the event
   -- Note: manually performing this as a bulk update is much faster than merging
   SELECT
      INTINTINTINTFLOATTUPLE (
         tb_evt_inv.event_inv_id,
         usage.data_type_db_id,
         usage.data_type_id,
         0, -- Adding dummy value to pad into an existing type
         usage.hist_tsn
      )
   BULK COLLECT INTO v_records
   FROM
      -- Hint, wrapping in a select will make it faster (and readable).
      (SELECT
         element1 as inv_no_db_id,
         element2 as inv_no_id,
         element3 as event_inv_id
       FROM
       TABLE (ltb_evt_inv)
      ) tb_evt_inv
   INNER JOIN
      (
       SELECT
          element1 as inv_no_db_id,
          element2 as inv_no_id,
          element3 as data_type_db_id,
          element4 as data_type_id,
          element5 as hist_tsn
       FROM TABLE(ltb_trk_usages)
       UNION ALL
       SELECT
          element1 as inv_no_db_id,
          element2 as inv_no_id,
          element3 as data_type_db_id,
          element4 as data_type_id,
          element5 as hist_tsn
       FROM TABLE(ltb_ac_usages)
       UNION ALL
       SELECT
          element1 as inv_no_db_id,
          element2 as inv_no_id,
          element3 as data_type_db_id,
          element4 as data_type_id,
          element5 as hist_tsn
       FROM TABLE(ltb_sys_usages)
      ) usage
   ON
      usage.inv_no_db_id  = tb_evt_inv.inv_no_db_id AND
      usage.inv_no_id     = tb_evt_inv.inv_no_id;
   
   FORALL i IN v_records.first .. v_records.last
      UPDATE
         evt_inv_usage
      SET
         -- tso and tsi are no longer captured, so set to the tsn
         evt_inv_usage.tsn_qt = v_records(i).element5,
         evt_inv_usage.tso_qt = v_records(i).element5,
         evt_inv_usage.tsi_qt = v_records(i).element5
      WHERE
         evt_inv_usage.event_db_id     = an_event_db_id AND
         evt_inv_usage.event_id        = an_event_id AND
         evt_inv_usage.event_inv_id    = v_records(i).element1 AND
         evt_inv_usage.data_type_db_id = v_records(i).element2 AND
         evt_inv_usage.data_type_id    = v_records(i).element3;
         
   FORALL i IN v_records.first .. v_records.last
      INSERT INTO
         evt_inv_usage (
            event_db_id,
            event_id,
            event_inv_id,
            data_type_db_id,
            data_type_id,
            tsn_qt,
            tso_qt,
            tsi_qt,
            source_db_id,
            source_cd
         )
         SELECT
            an_event_db_id,
            an_event_id,
            v_records(i).element1,
            v_records(i).element2,
            v_records(i).element3,
            v_records(i).element5,
            v_records(i).element5,
            v_records(i).element5,
            0,
            'MAINTENIX'
         FROM dual
         WHERE NOT EXISTS (
            SELECT
               NULL
            FROM
               evt_inv_usage
            WHERE
               evt_inv_usage.event_db_id     = an_event_db_id AND
               evt_inv_usage.event_id        = an_event_id AND
               evt_inv_usage.event_inv_id    = v_records(i).element1 AND
               evt_inv_usage.data_type_db_id = v_records(i).element2 AND
               evt_inv_usage.data_type_id    = v_records(i).element3
         );

    -- recalculate calculated params if any inventory has calculated param usages
   SELECT
      COUNT(*)
   INTO
      ln_calc_parm_count
   FROM
      (
         SELECT
            element3 as data_type_db_id,
            element4 as data_type_id
         FROM TABLE(ltb_trk_usages)
         UNION ALL
         SELECT
            element3 as data_type_db_id,
            element4 as data_type_id
         FROM TABLE(ltb_ac_usages)
         UNION ALL
         SELECT
            element3 as data_type_db_id,
            element4 as data_type_id
         FROM TABLE(ltb_sys_usages)
       ) inv_data_type
   INNER JOIN mim_calc ON
      inv_data_type.data_type_db_id = mim_calc.data_type_db_id AND
      inv_data_type.data_type_id = mim_calc.data_type_id;

   IF (ln_calc_parm_count != 0) THEN
      recalculateEventCalcParms(an_event_db_id,an_event_id);
   END IF;

END takeUsageSnapshotOfEvent;


/******************************************************************************
*
* Procedure:    recalculateEventCalcParms
*
* Description:  This procedure recalculates all the calculated parameters
*               within the inventory usage snapshots associated with an event.
*
* Arguments:    an_event_db_id  event key
*               an_event_id     "
*
*******************************************************************************/
PROCEDURE recalculateEventCalcParms(
   an_event_db_id   IN NUMBER,
   an_event_id      IN NUMBER
)
IS
   ltbl_evt_inv INTINTINTTable;
BEGIN

   -- Get all the corresponding event_inv_id for the provided event.
   SELECT
      INTINTINTTUPLE(
         evt_inv.event_db_id,
         evt_inv.event_id,
         evt_inv.event_inv_id
      )
   BULK COLLECT INTO
      ltbl_evt_inv
   FROM
      evt_inv
   WHERE
      evt_inv.event_db_id = an_event_db_id AND
      evt_inv.event_id    = an_event_id
   ;

   -- Recalculate the calc parms within the usage snapshots
   -- for each inventory associated to the event.
   FOR i in 1..ltbl_evt_inv.COUNT LOOP
      USAGE_PKG.update_calc_parms( ltbl_evt_inv(i) );
   END LOOP;

END recalculateEventCalcParms;


/******************************************************************************
*
* Function:     getAcftAssyUsageOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for the provided aircraft or assembly on the provided date.
*
* Arguments:    ain_inv_no_db_id  inventory key
*               ain_inv_no_id     "
*               aidt_date         date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages
*
* Note: query copied from
*       mxcoreejb\src\main\resources\com\mxi\mx\core\query\usage\FindUsageAtDate.qrx
*
*******************************************************************************/
FUNCTION getAcftAssyUsageOnDate(
   aittb_inv_no_db_id  IN INTEGERTABLE,
   aittb_inv_no_id     IN INTEGERTABLE,
   aidt_date           IN DATE
) RETURN INTINTINTINTFLOATTABLE
IS
   v_ttb_inv         INTINTTABLE;
   v_ttb_usage_recs  INTINTINTINTFLOATTABLE;
   v_ttb_usages      INTINTINTINTFLOATTABLE;

BEGIN

   -- Filter the provided inventory keys to only those that are aircraft or assemblies.
   SELECT
      INTINTTUPLE(
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id
      )
   BULK COLLECT INTO v_ttb_inv
   FROM
      TABLE (
         array_pkg.getTableOfINTINTTUPLE (
            aittb_inv_no_db_id,
            aittb_inv_no_id
         )
      ) inv
      INNER JOIN inv_inv ON
         inv_inv.inv_no_db_id = inv.element1 AND
         inv_inv.inv_no_id    = inv.element2
         AND
         inv_inv.inv_class_db_id = 0 AND
         inv_inv.inv_class_cd IN ('ACFT','ASSY')
   ;

   IF v_ttb_inv.count = 0 THEN
      RETURN v_ttb_usages;
   END IF;


   SELECT
      INTINTINTINTFLOATTUPLE(
         usage.inv_no_db_id,
         usage.inv_no_id,
         usage.data_type_db_id,
         usage.data_type_id,
         NVL(usage.tsn_qt, 0)
      )
   BULK COLLECT INTO v_ttb_usage_recs
   FROM (
      SELECT
         usg_usage_data.inv_no_db_id,
         usg_usage_data.inv_no_id,
         usg_usage_data.data_type_db_id,
         usg_usage_data.data_type_id,
         usg_usage_data.tsn_qt - usg_usage_data.tsn_delta_qt AS tsn_qt,
         row_number() over (
            PARTITION BY
               usg_usage_data.inv_no_db_id,
               usg_usage_data.inv_no_id,
               usg_usage_data.data_type_db_id,
               usg_usage_data.data_type_id
            ORDER BY
               usg_usage_record.usage_dt ASC) rn
      FROM
         usg_usage_data
      INNER JOIN usg_usage_record ON
         usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
      INNER JOIN TABLE (v_ttb_inv) inv ON
         inv.element1 = usg_usage_data.inv_no_db_id AND
         inv.element2 = usg_usage_data.inv_no_id
      WHERE
         usg_usage_record.usage_dt > aidt_date
   ) usage
   WHERE
      usage.rn = 1
   ;

   -- For any aircraft that do not have usage records after the provided date
   -- use their current usage.
   SELECT
      INTINTINTINTFLOATTUPLE(
         inv_curr_usage.inv_no_db_id,
         inv_curr_usage.inv_no_id,
         NVL(usage_recs.data_type_db_id, inv_curr_usage.data_type_db_id),
         NVL(usage_recs.data_type_id, inv_curr_usage.data_type_id),
         NVL(NVL(usage_recs.tsn_qt, inv_curr_usage.tsn_qt),0)
      )
   BULK COLLECT INTO v_ttb_usages
   FROM
      TABLE (v_ttb_inv) inv
   INNER JOIN inv_curr_usage ON
      inv_curr_usage.inv_no_db_id = inv.element1 AND
      inv_curr_usage.inv_no_id    = inv.element2
   LEFT OUTER JOIN (
      (SELECT
          element1 as inv_no_db_id,
          element2 as inv_no_id,
          element3 as data_type_db_id,
          element4 as data_type_id,
          element5 as tsn_qt
       FROM
       TABLE (v_ttb_usage_recs)
      ) usage_recs
   ) ON
      usage_recs.inv_no_db_id    = inv_curr_usage.inv_no_db_id AND
      usage_recs.inv_no_id       = inv_curr_usage.inv_no_id AND
      usage_recs.data_type_db_id = inv_curr_usage.data_type_db_id AND
      usage_recs.data_type_id    = inv_curr_usage.data_type_id
   ;

   RETURN v_ttb_usages;

END getAcftAssyUsageOnDate;


/******************************************************************************
*
* Function:     getSysUsageOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for the provided SYS "inventory" on the provided date.
*
*               It is assumed that the aircraft/assembly under which the
*               SYS inventory is located will never move.
*               And that the usage of the SYS will always equal that of
*               the aircraft/assembly which it is under.
*
* Arguments:    ain_inv_no_db_id  inventory key
*               ain_inv_no_id     "
*               aidt_date         date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages
*
* Note: query copied from
*       mxcoreejb\src\main\resources\com\mxi\mx\core\query\usage\FindUsageAtDate.qrx
*
*******************************************************************************/
FUNCTION getSysUsageOnDate(
   aittb_inv_no_db_id  IN INTEGERTABLE,
   aittb_inv_no_id     IN INTEGERTABLE,
   aidt_date           IN DATE
) RETURN INTINTINTINTFLOATTABLE
IS
   v_ttb_inv           INTINTINTINTTABLE;
   v_ttb_usage_recs    INTINTINTINTFLOATTABLE;
   v_ttb_usages        INTINTINTINTFLOATTABLE;

BEGIN

   SELECT
      INTINTINTINTTUPLE(
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         inv_inv.assmbl_inv_no_db_id,
         inv_inv.assmbl_inv_no_id
      )
   BULK COLLECT INTO v_ttb_inv
   FROM
      TABLE (
         ARRAY_PKG.getTableOfINTINTTUPLE (
            aittb_inv_no_db_id,
            aittb_inv_no_id
         )
      ) inv
   INNER JOIN inv_inv ON
      inv_inv.inv_no_db_id = inv.element1 AND
      inv_inv.inv_no_id    = inv.element2
      AND
      inv_inv.inv_class_db_id = 0 AND
      inv_inv.inv_class_cd = 'SYS'
   ;

   IF v_ttb_inv.count = 0 THEN
      RETURN v_ttb_usages;
   END IF;


   SELECT
      INTINTINTINTFLOATTUPLE(
         usage.inv_no_db_id,
         usage.inv_no_id,
         usage.data_type_db_id,
         usage.data_type_id,
         NVL(usage.tsn_qt, 0)
      )
   BULK COLLECT INTO v_ttb_usage_recs
   FROM (
      SELECT
         inv.element1 AS inv_no_db_id,
         inv.element2 AS inv_no_id,
         usg_usage_data.data_type_db_id,
         usg_usage_data.data_type_id,
         usg_usage_data.tsn_qt - usg_usage_data.tsn_delta_qt AS tsn_qt,
         row_number() over (
            PARTITION BY
               inv.element1,
               inv.element2,
               usg_usage_data.data_type_db_id,
               usg_usage_data.data_type_id
            ORDER BY
               usg_usage_record.usage_dt ASC) rn
      FROM
         usg_usage_data
      INNER JOIN usg_usage_record ON
         usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
      INNER JOIN TABLE(v_ttb_inv) inv ON
         inv.element3 = usg_usage_data.inv_no_db_id AND
         inv.element4 = usg_usage_data.inv_no_id
      WHERE
         usg_usage_record.usage_dt > aidt_date
   ) usage
   WHERE
      usage.rn = 1
   ;

   -- For any systems that do not have usage records after the provided date
   -- use their current usage.
   SELECT
      INTINTINTINTFLOATTUPLE(
         inv_curr_usage.inv_no_db_id,
         inv_curr_usage.inv_no_id,
         NVL(usage_recs.data_type_db_id, inv_curr_usage.data_type_db_id),
         NVL(usage_recs.data_type_id, inv_curr_usage.data_type_id),
         NVL(NVL(usage_recs.tsn_qt, inv_curr_usage.tsn_qt),0)
      )
   BULK COLLECT INTO v_ttb_usages
   FROM
      TABLE (v_ttb_inv) inv
   INNER JOIN inv_curr_usage ON
      inv_curr_usage.inv_no_db_id = inv.element1 AND
      inv_curr_usage.inv_no_id    = inv.element2
   LEFT OUTER JOIN (
      (SELECT
          element1 as inv_no_db_id,
          element2 as inv_no_id,
          element3 as data_type_db_id,
          element4 as data_type_id,
          element5 as tsn_qt
       FROM
       TABLE (v_ttb_usage_recs)
      ) usage_recs
   ) ON
      usage_recs.inv_no_db_id    = inv_curr_usage.inv_no_db_id AND
      usage_recs.inv_no_id       = inv_curr_usage.inv_no_id AND
      usage_recs.data_type_db_id = inv_curr_usage.data_type_db_id AND
      usage_recs.data_type_id    = inv_curr_usage.data_type_id
   ;

   RETURN v_ttb_usages;

END getSysUsageOnDate;


/******************************************************************************
*
* Function:     getTrkSerUsagesOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for any TRK or SER inventory contained within
*               the provided inventories on the provided date.
*
* Arguments:    aittb_inv_no_db_id  table of inventory keys
*               aittb_inv_no_id     "
*               aidt_date           date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages for any TRK or SER inventories
*
*******************************************************************************/
FUNCTION getTrkSerUsagesOnDate(
   aittb_inv_no_db_id  IN INTEGERTABLE,
   aittb_inv_no_id     IN INTEGERTABLE,
   aidt_date           IN DATE
) RETURN INTINTINTINTFLOATTABLE
IS
   v_ttb_inventories INTINTTABLE;
   v_ttb_usages  INTINTINTINTFLOATTABLE;

BEGIN

   -- combine inventory key input arrays into a table, and prefilter to TRK/SER only
   SELECT
      INTINTTUPLE(
         element1, element2
      )
   BULK COLLECT INTO v_ttb_inventories
   FROM
      TABLE (
         ARRAY_PKG.getTableOfINTINTTUPLE (
            aittb_inv_no_db_id,
            aittb_inv_no_id
         )
      ) invlist
      INNER JOIN inv_inv ON
         inv_inv.inv_no_db_id = invlist.element1 AND
         inv_inv.inv_no_id    = invlist.element2
      WHERE
         inv_inv.inv_class_db_id = 0             AND
         inv_inv.inv_class_cd IN ('TRK', 'SER');

   WITH
   -- convert input to a table of inventory primary keys
   trk_ser_list AS
   (
      SELECT
         element1 as inv_no_db_id,
         element2 as inv_no_id
      FROM
      TABLE (v_ttb_inventories)
   ),
   -- Installed Timespans: consolidate inventory install-remove paired list for trk/ser
   inv_onoff_list AS
   (
      SELECT
         rmvl.inv_no_db_id,
         rmvl.inv_no_id,
         rmvl.assmbl_inv_no_db_id,
         rmvl.assmbl_inv_no_id,
         NVL((
            -- Find the matching install record for each removal
            SELECT
               MAX(inv_install.event_dt)
            FROM
               inv_install
            INNER JOIN inv_install main_inv_install ON
               main_inv_install.event_db_id   = inv_install.event_db_id AND
               main_inv_install.event_id      = inv_install.event_id
               AND
               main_inv_install.main_inv_bool = 1
            INNER JOIN inv_inv main_inv ON
               main_inv.inv_no_db_id          = main_inv_install.inv_no_db_id AND
               main_inv.inv_no_id             = main_inv_install.inv_no_id
            INNER JOIN inv_inv highest_inv ON
               highest_inv.inv_no_db_id       = inv_install.h_inv_no_db_id AND
               highest_inv.Inv_No_Id          = inv_install.h_inv_no_id
            WHERE
               inv_install.inv_no_id          = rmvl.inv_no_id AND
               inv_install.inv_no_db_id       = rmvl.inv_no_db_id
               AND
               inv_install.assmbl_inv_no_id    = rmvl.assmbl_inv_no_id AND
               inv_install.assmbl_inv_no_db_id = rmvl.assmbl_inv_no_db_id
               AND NOT (
                  main_inv.inv_class_db_Id    = 0 AND
                  main_inv.inv_class_cd       = 'ASSY'
               )
               AND NOT (
                  highest_inv.inv_class_db_id = 0 AND
                  highest_inv.inv_class_cd    = 'TRK'
               )
               AND
               rmvl.remove_date >= inv_install.event_dt
               AND
               inv_install.event_dt >= aidt_date
         ), aidt_date ) AS install_date,
         rmvl.remove_date
      FROM
         (
            SELECT
               inv_remove.inv_no_db_id,
               inv_remove.inv_no_id,
               inv_remove.assmbl_inv_no_db_id,
               inv_remove.assmbl_inv_no_id,
               inv_remove.event_dt as remove_date
            FROM
               trk_ser_list
            INNER JOIN inv_remove ON
                  inv_remove.inv_no_db_id = trk_ser_list.inv_no_db_id AND
                  inv_remove.inv_no_id    = trk_ser_list.inv_no_id
            INNER JOIN inv_remove main_inv_remove ON
               main_inv_remove.event_db_id   = inv_remove.event_db_id AND
               main_inv_remove.event_id      = inv_remove.event_id
               AND
               main_inv_remove.main_inv_bool = 1
            INNER JOIN inv_inv main_inv ON
               main_inv.inv_no_db_id         = main_inv_remove.inv_no_db_id AND
               main_inv.inv_no_id            = main_inv_remove.inv_no_id
            INNER JOIN inv_inv highest_inv ON
               highest_inv.inv_no_db_id = inv_remove.h_inv_no_db_id AND
               highest_inv.Inv_No_Id    = inv_remove.h_inv_no_id
            WHERE
               -- Do not collect records where the sub-assembly is removed from an aircraft
               NOT (
                  main_inv.inv_class_db_Id = 0 AND
                  main_inv.inv_class_cd    = 'ASSY'
               )
               -- Do not collect records where this component is not being removed from an assembly
               AND NOT (
                  highest_inv.inv_class_db_id = 0 AND
                  highest_inv.inv_class_cd    = 'TRK'
               )
               AND
               inv_remove.event_dt > aidt_date
            -- Create a dummy record for today's date for all inventory fitted to an assembly
            -- This will create an Installed Timespan record for the current install location
            UNION ALL
            SELECT
               inv_inv.inv_no_db_id,
               inv_inv.inv_no_id,
               inv_inv.assmbl_inv_no_db_id,
               inv_inv.assmbl_inv_no_id,
               sysdate
            FROM
               trk_ser_list
            JOIN inv_inv ON
               trk_ser_list.inv_no_db_id = inv_inv.inv_no_db_id AND
               trk_ser_list.inv_no_id    = inv_inv.Inv_No_Id
            WHERE
               inv_inv.assmbl_inv_no_id IS NOT NULL
         ) rmvl
   ),
   -- fetch all usage changes applicable to our inventory
   inv_usage_delta AS
   (
      SELECT
         inv_no_db_id,
         inv_no_id,
         data_type_db_id,
         data_type_id,
         SUM (tsn_delta_qt) AS tsn_delta
      FROM (
         -- return all usage corrections on the inventory since our target date
         SELECT
            usg_usage_data.inv_no_db_id,
            usg_usage_data.inv_no_id,
            usg_usage_data.data_type_db_id,
            usg_usage_data.data_type_id,
            usg_usage_data.tsn_delta_qt
         FROM
         trk_ser_list
         INNER JOIN usg_usage_record ON
            usg_usage_record.inv_no_db_id = trk_ser_list.inv_no_db_id AND
            usg_usage_record.inv_no_id    = trk_ser_list.inv_no_id
         INNER JOIN usg_usage_data ON
            usg_usage_data.usage_record_id = usg_usage_record.usage_record_id
         WHERE
            usg_usage_record.usage_dt > aidt_date AND
            usg_usage_record.usage_type_cd = 'MXCORRECTION'
         -- return all usage accruals on the assembly for fitted time spans of inventory
         UNION ALL
         SELECT
            inv_onoff_list.inv_no_db_id,
            inv_onoff_list.inv_no_id,
            usg_usage_data.data_type_db_id,
            usg_usage_data.data_type_id,
            usg_usage_data.tsn_delta_qt
         FROM
         usg_usage_data
         INNER JOIN inv_onoff_list ON
            usg_usage_data.inv_no_db_id = inv_onoff_list.assmbl_inv_no_db_id AND
            usg_usage_data.inv_no_id    = inv_onoff_list.assmbl_inv_no_id
         INNER JOIN usg_usage_record ON
            usg_usage_data.usage_record_id = usg_usage_record.usage_record_id
         WHERE
            usg_usage_record.usage_dt > inv_onoff_list.install_date AND
            usg_usage_record.usage_dt <= inv_onoff_list.remove_date
            AND
            usg_usage_record.usage_type_cd IN ('MXFLIGHT', 'MXACCRUAL')
      )
      GROUP BY
         inv_no_db_id, inv_no_id, data_type_db_id, data_type_id
   )
   -- finally, we calculate historic tsn at that moment by subtracting our tallied deltas from current usage
   SELECT /*+ index(inv_curr_usage IX_INVINV_INVCURRUSAGE) */
      INTINTINTINTFLOATTUPLE(
         inv_curr_usage.inv_no_db_id,
         inv_curr_usage.inv_no_id,
         inv_curr_usage.data_type_db_id,
         inv_curr_usage.data_type_id,
         inv_curr_usage.tsn_qt - nvl(inv_usage_delta.tsn_delta, 0)
      )
   BULK COLLECT INTO v_ttb_usages
   FROM trk_ser_list
   INNER JOIN inv_curr_usage ON
      inv_curr_usage.inv_no_db_id    = trk_ser_list.inv_no_db_id       AND
      inv_curr_usage.inv_no_id       = trk_ser_list.inv_no_id
   LEFT JOIN inv_usage_delta ON
      inv_curr_usage.inv_no_db_id    = inv_usage_delta.inv_no_db_id    AND
      inv_curr_usage.inv_no_id       = inv_usage_delta.inv_no_id
      AND
      inv_curr_usage.data_type_db_id = inv_usage_delta.data_type_db_id AND
      inv_curr_usage.data_type_id    = inv_usage_delta.data_type_id;

   RETURN v_ttb_usages;

EXCEPTION
   WHEN OTHERS THEN
      application_object_pkg.SetMxiError('DEV-99999', 'USAGE_PKG@@@getTrkSerUsageOnDate@@@' || SQLERRM);

END getTrkSerUsagesOnDate;


/******************************************************************************
*
* Function:     getInvUsageOnDate
*
* Description:  This function returns the TSN values for all the usages
*               for the provided inventory on the provided date.
*
* Arguments:    ai_inv_no_db_id  inventory key
*               ai_inv_no_id     "
*               aidt_date         date to find usages
*
* Returns:      table of inventory key + date type key + TSN value
*               for all retrieved usages
*
*******************************************************************************/
FUNCTION getInvUsageOnDate(
   ai_inv_no_db_id  IN NUMBER,
   ai_inv_no_id     IN NUMBER,
   aidt_date        IN DATE
) RETURN INTINTINTINTFLOATTABLE
IS
   v_ttb_usages        INTINTINTINTFLOATTABLE;
   ltb_inv_no_db_ids   INTEGERTABLE;
   ltb_inv_no_ids      INTEGERTABLE;

   ld_inv_class_cd     inv_inv.inv_class_cd%TYPE;

BEGIN

   SELECT
      ai_inv_no_db_id
   BULK COLLECT INTO ltb_inv_no_db_ids
   FROM
      dual;

   SELECT
      ai_inv_no_id
   BULK COLLECT INTO ltb_inv_no_ids
   FROM
      dual;

   SELECT
      inv_inv.inv_class_cd
   INTO
      ld_inv_class_cd
   FROM
      inv_inv
   WHERE
      inv_inv.inv_no_db_id = ai_inv_no_db_id AND
      inv_inv.inv_no_id    = ai_inv_no_id;

   IF (ld_inv_class_cd = 'ACFT') OR (ld_inv_class_cd = 'ASSY') THEN
      v_ttb_usages := getAcftAssyUsageOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, aidt_date);

   ELSIF (ld_inv_class_cd = 'SYS') THEN
      v_ttb_usages := getSysUsageOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, aidt_date);

   ELSE
      v_ttb_usages := getTrkSerUsagesOnDate(ltb_inv_no_db_ids, ltb_inv_no_ids, aidt_date);

   END IF;

   RETURN v_ttb_usages;

END getInvUsageOnDate;


END USAGE_PKG;
/