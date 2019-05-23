--liquibase formatted sql

--changeSet OPER-24977:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY FLIGHT_OUT_OF_SEQUENCE_PKG IS

   c_pkg_name CONSTANT VARCHAR2(30) := 'FLIGHT_OUT_OF_SEQUENCE_PKG';

   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);


FUNCTION get_adjusted_usage_delta(
   airaw_usage_adjustment_id   IN RAW,
   aittb_assy_usage_deltadata  IN IntIntIntIntFLOATTABLE
) RETURN IntIntIntIntFLOATTABLE
IS
   v_method_name VARCHAR2(30) := 'get_adjusted_usage_delta';

   v_ttb_adjusted_usage_delta IntIntIntIntFLOATTABLE;

BEGIN

   v_step := 10;

   SELECT
      IntIntIntIntFloatTuple(
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
) RETURN IntIntIntIntTABLE
IS
   v_method_name VARCHAR2(30) := 'get_acft_hist_config';

   v_ttb_inv_assmbl IntIntIntIntTABLE;
   v_ttb_assmbl IntIntTABLE;

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
      IntIntTuple(inv_no_db_id, inv_no_id)
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
      IntIntIntIntTuple(
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


FUNCTION filterInventoryHavingOverhaul(
   aittb_inv_assmbl IN IntIntIntIntTABLE,
   ai_usage_dt IN DATE
) RETURN IntIntIntIntTABLE
IS
   v_method_name VARCHAR2(30) := 'filterInventoryHavingOverhaul';

   v_ttb_inv_assmbl IntIntIntIntTABLE;

BEGIN

   v_step := 10;

   WITH
   inv_assmbl_list AS
   (
      SELECT
         element1 as inv_no_db_id,
         element2 as inv_no_id,
         element3 as assmbl_inv_db_id,
         element4 as assmbl_inv_id
       FROM
          TABLE (aittb_inv_assmbl)
   )
   SELECT
      IntIntIntIntTuple(
         inv_assmbl_list.inv_no_db_id,
         inv_assmbl_list.inv_no_id,
         inv_assmbl_list.assmbl_inv_db_id,
         inv_assmbl_list.assmbl_inv_id
      )
   BULK COLLECT INTO v_ttb_inv_assmbl
   FROM inv_assmbl_list
   WHERE NOT EXISTS(
      SELECT
         1
      FROM
         sched_stask
      INNER JOIN evt_event ON
         sched_stask.sched_db_id = evt_event.event_db_id AND
         sched_stask.sched_id    = evt_event.event_id
      WHERE
         sched_stask.main_inv_no_db_id = inv_assmbl_list.inv_no_db_id AND
         sched_stask.main_inv_no_id    = inv_assmbl_list.inv_no_id
         AND
         sched_stask.task_class_db_id = 0 AND
         sched_stask.task_class_cd = 'OVHL'
         AND
         evt_event.event_status_db_id = 0 AND
         evt_event.event_status_cd    = 'COMPLETE'
         AND
         -- select any overhauls occurring after the date ... so that their inventories may be filtered from the list
         evt_event.event_dt >= ai_usage_dt
   )
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
END filterInventoryHavingOverhaul;


FUNCTION filterInventoryReinstalled(
   aittb_inv_assmbl IN IntIntIntIntTABLE,
   ai_usage_dt IN DATE
) RETURN IntIntIntIntTABLE
IS
   v_method_name VARCHAR2(30) := 'filterInventoryReinstalled';

   v_ttb_inv_assmbl IntIntIntIntTABLE;

BEGIN

   v_step := 10;

   WITH
   inv_assmbl_list AS
   (
      SELECT
         element1 as inv_no_db_id,
         element2 as inv_no_id,
         element3 as assmbl_inv_db_id,
         element4 as assmbl_inv_id
       FROM
          TABLE (aittb_inv_assmbl)
   )
   SELECT
      IntIntIntIntTuple(
         inv_assmbl_list.inv_no_db_id,
         inv_assmbl_list.inv_no_id,
         inv_assmbl_list.assmbl_inv_db_id,
         inv_assmbl_list.assmbl_inv_id
      )
   BULK COLLECT INTO v_ttb_inv_assmbl
   FROM inv_assmbl_list
   WHERE NOT EXISTS (
      SELECT 1
      FROM   inv_install
      WHERE  inv_install.inv_no_db_id = inv_assmbl_list.inv_no_db_id AND
             inv_install.inv_no_id    = inv_assmbl_list.inv_no_id AND
             inv_install.event_dt > ai_usage_dt AND
             inv_install.main_inv_bool = 1)
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
END filterInventoryReinstalled;


PROCEDURE updateFollowingTaskDeadlines (
   airaw_usage_adjustment_id  IN RAW,
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE,
   aittb_inv_assmbl           IN IntIntIntIntTABLE,
   ai_usage_dt                IN DATE,
   aiv_system_note            IN VARCHAR2,
   aiv_deadline_system_note   IN VARCHAR2,
   ain_hr_db_id               IN NUMBER,
   ain_hr_id                  IN NUMBER,
   aottb_calc_deadline        OUT IntIntIntIntTABLE,
   aottb_deadlines            OUT IntIntIntIntTABLE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingTaskDeadlines';

   v_ttb_dist_data_types IntIntTABLE;
   v_ttb_inv_data_types  IntIntIntIntTABLE;
   v_ttb_trkser_hist_tsn IntIntIntIntFLOATTABLE;
   v_ttb_inv_calc        IntIntIntIntTABLE;
   v_ttb_hist_tsn        IntIntIntIntFLOATTABLE;
   v_ttb_inv_usage       IntIntIntIntFLOATTABLE;
   v_ttb_inv_assmbl      IntIntIntIntTABLE;
   v_ttb_calc_deadline   IntIntIntIntTABLE := IntIntIntIntTABLE();
   v_rec_calc_deadline   IntIntIntIntTUPLE;

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
             TABLE (v_ttb_inv_assmbl)
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

      ltab_deadlines IntIntIntIntTABLE := IntIntIntIntTABLE();
      ltpl_deadline IntIntIntIntTUPLE;

      ln_calc_tsn NUMBER;

BEGIN
   v_step := 10;

   -- The query in step 15 is very expensive when run against the entire aircraft tree.
   -- We are pre-filtering the list of inventory to reduce the scope
   SELECT IntInttuple(
      dataType.data_type_db_id,
      dataType.data_type_id)
   BULK COLLECT INTO v_ttb_dist_data_types
   FROM (
      SELECT DISTINCT
         element3 AS data_type_db_id,
         element4 AS data_type_id
      FROM TABLE(aittb_adjusted_usage_delta)) dataType;

   v_step := 11;
   SELECT IntIntIntInttuple(
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
      FROM (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id
             FROM
             TABLE (aittb_inv_assmbl)
         ) invlist
      INNER JOIN inv_inv
         ON invlist.inv_no_db_id = inv_inv.inv_no_db_id AND
            invlist.inv_no_id    = inv_inv.Inv_No_Id
            AND
            inv_inv.inv_class_db_id = 0 AND
            inv_inv.inv_class_cd IN ('TRK','SER')
      INNER JOIN sched_stask
         ON invlist.inv_no_db_id = sched_stask.main_inv_no_db_id AND
            invlist.inv_no_id    = sched_stask.main_inv_no_id
      INNER JOIN evt_sched_dead
         ON evt_sched_dead.event_db_id = sched_stask.sched_db_id AND
            evt_sched_dead.event_id    = sched_stask.sched_id
      INNER JOIN (
         SELECT element1 AS data_type_db_id,
                element2 AS data_type_id
         FROM TABLE (v_ttb_dist_data_types)
      ) dataTypes ON
         dataTypes.data_type_db_id = evt_sched_dead.data_type_db_id AND
         dataTypes.data_type_id    = evt_sched_dead.data_type_id
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

   v_step := 15;

   -- Get the TSN snapshots for only the TRK and SER inventory on the provided date.
   v_ttb_trkser_hist_tsn := USAGE_PKG.getTrkSerUsagesByDataType(v_ttb_inv_data_types, ai_usage_dt);

   v_step := 16;

   -- Filter out non-applicable TRK and SER inventory from aircraft tree prior to opening cursor in step 50
   SELECT
      IntIntIntIntTuple(
         inv_no_db_id,
         inv_no_id,
         assmbl_inv_no_db_id,
         assmbl_inv_no_id)
   BULK COLLECT INTO v_ttb_inv_assmbl
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
            FROM TABLE (v_ttb_inv_data_types)
         ) trkser_inv ON
            trkser_inv.element1 = all_inv.element1 AND
            trkser_inv.element2 = all_inv.element2
   );

   v_step := 20;
   -- Find calc usages
   SELECT
      IntIntIntIntTuple(
         inv_no_db_id,
         inv_no_id,
         calc_db_id,
         calc_id
      )
   BULK COLLECT INTO v_ttb_inv_calc
   FROM
   (
      SELECT /*+ index(sched_stask IX_MAININV_SHEDSTASK) */ DISTINCT
         invlist.inv_no_db_id,
         invlist.inv_no_id,
         mim_calc.calc_db_id,
         mim_calc.calc_id
      FROM
         (
            SELECT
               element1 as inv_no_db_id,
               element2 as inv_no_id
             FROM
                TABLE (aittb_inv_assmbl)
         ) invlist
      INNER JOIN sched_stask
         ON
            sched_stask.main_inv_no_db_id = invlist.inv_no_db_id AND
            sched_stask.main_inv_no_id    = invlist.inv_no_id
      INNER JOIN evt_sched_dead
         ON
            evt_sched_dead.event_db_id = sched_stask.sched_db_id AND
            evt_sched_dead.event_id    = sched_stask.sched_id
      INNER JOIN mim_calc
         ON
           -- A data type has only ONE calc parm
            mim_calc.data_type_db_id = evt_sched_dead.data_type_db_id AND
            mim_calc.data_type_id    = evt_sched_dead.data_type_id
   )
   ;

   v_step := 25;
   -- Find inv historic tsn usages
   SELECT
      IntIntIntIntFloatTuple(
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
         IntIntIntIntFloatTuple(
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
          v_rec_calc_deadline := IntIntIntIntTUPLE(
             lrec_deadline.event_db_id,
             lrec_deadline.event_id,
             lrec_deadline.data_type_db_id,
             lrec_deadline.data_type_id
          );
          v_ttb_calc_deadline.EXTEND;
          v_ttb_calc_deadline(v_ttb_calc_deadline.count) := v_rec_calc_deadline;

       -- pass back the updated deadlines keys for calc usage need check sched plan window
          IF (lrec_deadline.sched_from_cd = 'LASTEND') OR (lrec_deadline.sched_from_cd = 'LASTDUE') THEN
             ltpl_deadline := IntIntIntIntTUPLE(
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


   v_step := 50;
   OPEN lcur_snapshot;
   LOOP

       FETCH lcur_snapshot BULK COLLECT INTO ltab_snapshots LIMIT 10000;

       EXIT WHEN ltab_snapshots.COUNT = 0;

       --update usage
       v_step := 55;
       FORALL i IN 1..ltab_snapshots.COUNT
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

       --insert system note
       v_step := 60;
       IF aiv_system_note IS NOT NULL THEN
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
       END IF;

       -- pass back the updated deadlines keys, based on usage delta, not include calc usage which is already added
       v_step := 65;
       FOR i IN 1..ltab_snapshots.COUNT
       LOOP
          IF (ltab_snapshots(i).sched_from_cd = 'LASTEND') OR (ltab_snapshots(i).sched_from_cd = 'LASTDUE') THEN
             ltpl_deadline := IntIntIntIntTUPLE(
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
   aittb_calc_deadline  IN IntIntIntIntTABLE
)
AS
   v_method_name VARCHAR2(30) := 'updateFollowingCalcDeadline';

   v_ttb_hist_tsn   IntIntIntIntFLOATTABLE;
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
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE,
   aittb_inv_assmbl IN IntIntIntIntTABLE,
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
      FORALL i IN 1..ltab_snapshots.COUNT
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

      --insert system note
       IF aiv_system_note IS NOT NULL THEN
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
       END IF;
   END LOOP;
   CLOSE lcur_snapshot;

   -- Refresh calculated parms if required
   FOR lrec_calc_snapshots IN lcur_calc_snapshot LOOP
      USAGE_PKG.update_calc_parms(
         IntIntIntTUPLE(
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
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE,
   aittb_inv_assmbl IN IntIntIntIntTABLE,
   ai_usage_dt IN DATE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingEditInvOfTrkSer';

BEGIN
   v_step := 10;

   MERGE INTO
      usg_usage_data
   USING
   (
      SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
         inv_inv.inv_no_db_id,
         inv_inv.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
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
      INNER JOIN inv_inv
         ON
            inv_inv.inv_no_db_id = invlist.inv_no_db_id AND
            inv_inv.inv_no_id    = invlist.inv_no_id
            AND
            inv_inv.inv_class_db_id = 0 AND
            inv_inv.inv_class_cd IN ('TRK', 'SER')
   ) rvw_new_delta
   ON
   (
      rvw_new_delta.inv_no_db_id  = usg_usage_data.inv_no_db_id AND
      rvw_new_delta.inv_no_id     = usg_usage_data.inv_no_id
      AND
      rvw_new_delta.data_type_db_id = usg_usage_data.data_type_db_id AND
      rvw_new_delta.data_type_id    = usg_usage_data.data_type_id
   )
   WHEN MATCHED THEN
      UPDATE
         SET
            usg_usage_data.tsn_qt = usg_usage_data.tsn_qt + rvw_new_delta.adjusted_delta
      WHERE
         EXISTS
         (
            SELECT 1
            FROM usg_usage_record
            WHERE
               usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
               AND
               usg_usage_record.usage_dt >= ai_usage_dt
         )
   ;

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
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE,
   ai_usage_dt IN DATE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingUsgAdjustment';

BEGIN
   v_step := 10;

   MERGE INTO
      usg_usage_data
   USING
   (
      SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
         adjusted_usage_delta.inv_no_db_id,
         adjusted_usage_delta.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
      FROM
         (SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM
             TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
   ) rvw_new_delta
   ON
   (
      rvw_new_delta.inv_no_db_id  = usg_usage_data.inv_no_db_id AND
      rvw_new_delta.inv_no_id     = usg_usage_data.inv_no_id
      AND
      rvw_new_delta.data_type_db_id = usg_usage_data.data_type_db_id AND
      rvw_new_delta.data_type_id    = usg_usage_data.data_type_id
   )
   WHEN MATCHED THEN
      UPDATE
         SET
            usg_usage_data.tsn_qt = usg_usage_data.tsn_qt + rvw_new_delta.adjusted_delta
      WHERE
         EXISTS
         (
            SELECT 1
            FROM usg_usage_record
            WHERE
               usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
               AND
               usg_usage_record.usage_dt >= ai_usage_dt
         )
   ;

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
   aittb_deadlines  IN IntIntIntIntTABLE
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

FUNCTION selectInvAndSubInvUsageDelta(
   -- This function is retrieving all the usage delta data type and value of the inventories passed in as well as their sub inventories
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE,
   aittb_inv_assmbl IN IntIntIntIntTABLE
) RETURN IntIntIntIntFLOATTABLE
IS
   v_ttb_inv IntIntIntIntFLOATTABLE;

   v_method_name VARCHAR2(30) := 'selectInvAndSubInvUsageDelta';

BEGIN
   SELECT  IntIntIntIntFloatTuple(
      allinv.inv_no_db_id,
      allinv.inv_no_id,
      allinv.data_type_db_id,
      allinv.data_type_id,
      allinv.adjusted_delta)
   BULK COLLECT INTO v_ttb_inv
   FROM(
   SELECT
      invlist.inv_no_db_id,
      invlist.inv_no_id,
      adjusted_usage_delta.data_type_db_id,
      adjusted_usage_delta.data_type_id,
      adjusted_usage_delta.adjusted_delta
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
      invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id) allinv ;

    RETURN  v_ttb_inv;



END selectInvAndSubInvUsageDelta;

PROCEDURE createRealtimeInvWorkItem(
   aHInvNoDbId IN inv_inv.h_inv_no_db_id%TYPE,
   aHInvNoId   IN inv_inv.h_inv_no_id%TYPE,
   aInvNoDbId  IN inv_inv.inv_no_db_id%TYPE,
   aInvNoId    IN inv_inv.inv_no_id%TYPE
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

   INSERT INTO utl_work_item(id, type, key, data, utl_id) VALUES (workId, 'Real_Time_Inventory_Deadline_Update', aHInvNoDbId || ':' || aHInvNoId || ':' || aInvNoDbId||':'||aInvNoId, invDesc, mimDb);

END createRealtimeInvWorkItem;

PROCEDURE updateCurrentTsn (
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE,
   aittb_inv_assmbl IN IntIntIntIntTABLE
)
AS

   v_method_name VARCHAR2(30) := 'updateCurrentTsn';

   v_ttb_usage_to_update IntIntIntIntFLOATTABLE := selectInvAndSubInvUsageDelta(aittb_adjusted_usage_delta, aittb_inv_assmbl);
BEGIN
   v_step := 10;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
         FROM
            TABLE(v_ttb_usage_to_update)
      ) usage_to_update
   ON
      (
         usage_to_update.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
         usage_to_update.inv_no_id    = inv_curr_usage.inv_no_id
         AND
         usage_to_update.data_type_db_id = inv_curr_usage.data_type_db_id AND
         usage_to_update.data_type_id    = inv_curr_usage.data_type_id
      )
   WHEN MATCHED THEN UPDATE
      SET
        inv_curr_usage.tsn_qt = inv_curr_usage.tsn_qt + usage_to_update.adjusted_delta
   ;

   -- Create Realtime Deadline Update Work iItems for all affected highest inventory which are not aircraft
   -- Note Aircraft work items are handled in the java tier
   FOR lCurInv IN (SELECT DISTINCT
                      h_inv.inv_no_db_id h_inv_no_db_id,
                      h_inv.inv_no_id h_inv_no_id,
                      inv_inv.inv_no_db_id,
                      inv_inv.inv_no_id
                   FROM
                      TABLE(v_ttb_usage_to_update) usage_to_update
                       JOIN inv_inv ON
                                    usage_to_update.element1 = inv_inv.inv_no_db_id AND
                                    usage_to_update.element2 = inv_inv.inv_no_id
                       JOIN inv_inv h_inv ON
                                    inv_inv.h_inv_no_db_id = h_inv.inv_no_db_id AND
                                    inv_inv.h_inv_no_id = h_inv.inv_no_id AND
                                    h_inv.inv_class_db_id = 0 AND
                                    h_inv.inv_class_cd <> 'ACFT')
   LOOP
       createRealtimeInvWorkItem(lCurInv.h_inv_no_db_id,lCurInv.h_inv_no_id,lCurInv.inv_no_db_id,lCurInv.inv_no_id);
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
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE,
   aittb_inv_assmbl IN IntIntIntIntTABLE,
   ai_usage_dt IN DATE
)
AS
   v_method_name VARCHAR2(30) := 'updateCurrentTso';

   v_ttb_inv_assmbl IntIntIntIntTABLE;

   v_ttb_usage_to_update IntIntIntIntFLOATTABLE ;

BEGIN
   v_step := 10;

   v_ttb_inv_assmbl := filterInventoryHavingOverhaul(aittb_inv_assmbl, ai_usage_dt);

   v_ttb_usage_to_update := selectInvAndSubInvUsageDelta(aittb_adjusted_usage_delta, v_ttb_inv_assmbl);

   v_step := 20;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
         FROM
            TABLE(v_ttb_usage_to_update)
      ) usage_to_update
   ON
      (
         usage_to_update.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
         usage_to_update.inv_no_id    = inv_curr_usage.inv_no_id
         AND
         usage_to_update.data_type_db_id = inv_curr_usage.data_type_db_id AND
         usage_to_update.data_type_id    = inv_curr_usage.data_type_id
      )
   WHEN MATCHED THEN UPDATE
      SET
        inv_curr_usage.tso_qt = inv_curr_usage.tso_qt + usage_to_update.adjusted_delta
   ;

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
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE,
   aittb_inv_assmbl IN IntIntIntIntTABLE,
   ai_usage_dt IN DATE
)
AS
   v_method_name VARCHAR2(30) := 'updateCurrentTsi';

   v_ttb_inv_assmbl IntIntIntIntTABLE;

   v_ttb_usage_to_update IntIntIntIntFLOATTABLE;

BEGIN
   v_step := 10;

   v_ttb_inv_assmbl := filterInventoryReinstalled(aittb_inv_assmbl, ai_usage_dt);

   v_ttb_usage_to_update := selectInvAndSubInvUsageDelta(aittb_adjusted_usage_delta, v_ttb_inv_assmbl);

   v_step := 20;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
         FROM
            TABLE(v_ttb_usage_to_update)
      ) usage_to_update
   ON
      (
         usage_to_update.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
         usage_to_update.inv_no_id    = inv_curr_usage.inv_no_id
         AND
         usage_to_update.data_type_db_id = inv_curr_usage.data_type_db_id AND
         usage_to_update.data_type_id    = inv_curr_usage.data_type_id
      )
   WHEN MATCHED THEN UPDATE
      SET
        inv_curr_usage.tsi_qt = inv_curr_usage.tsi_qt + usage_to_update.adjusted_delta
   ;

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
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE,
   aittb_inv_assmbl IN IntIntIntIntTABLE
)
AS
   v_method_name VARCHAR2(30) := 'updateCalculatedParameters';

   v_ttb_usage_to_update IntIntIntIntFLOATTABLE := selectInvAndSubInvUsageDelta(aittb_adjusted_usage_delta, aittb_inv_assmbl);

   v_update_result NUMBER;

BEGIN
   FOR lrec IN
      (
         SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id
         FROM
            TABLE(v_ttb_usage_to_update)
       )
   LOOP
      USAGE_PKG.UsageCalculations(lrec.inv_no_db_id, lrec.inv_no_id, v_update_result);
   END LOOP;

END updateCalculatedParameters;


FUNCTION isEventUsageAdjustmentRequired(
   ai_acft_inv_db_id IN INTEGER,
   ai_acft_inv_id    IN INTEGER,
   ai_org_usage_dt   IN DATE,
   ai_new_usage_dt   IN DATE
) RETURN INTEGER
IS
   v_method_name VARCHAR2(30) := 'isEventUsageAdjustmentRequired';

   vi_is_event_usage INTEGER;

BEGIN

   v_step := 10;

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
                        evt_event.actual_start_dt BETWEEN ai_org_usage_dt AND ai_new_usage_dt
                      )
                      OR
                      -- looking for other events after event date (evt_event.event_dt)
                      (
                        (
                          evt_event.event_type_db_id != 0 OR
                          evt_event.event_type_cd NOT IN ('TS', 'CF')
                        )
                        AND
                        evt_event.event_dt BETWEEN ai_org_usage_dt AND ai_new_usage_dt
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
                        evt_event.actual_start_dt BETWEEN ai_org_usage_dt AND ai_new_usage_dt
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
                        evt_event.event_dt BETWEEN ai_org_usage_dt AND ai_new_usage_dt
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
END isEventUsageAdjustmentRequired;


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
   aittb_adjusted_usage_delta IN IntIntIntIntFLOATTABLE
)
AS

   v_method_name VARCHAR2(30) := 'processLatestUsage';

BEGIN
   v_step := 10;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
            inv_inv.inv_no_db_id,
            inv_inv.inv_no_id,
            adjusted_usage_delta.data_type_db_id,
            adjusted_usage_delta.data_type_id,
            adjusted_usage_delta.adjusted_delta
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
         INNER JOIN inv_inv
            ON
               inv_inv.h_inv_no_db_id = ai_acft_inv_db_id AND
               inv_inv.h_inv_no_id    = ai_acft_inv_id
               AND
               DECODE(inv_inv.inv_class_cd, 'ASSY', inv_inv.inv_no_db_id, NVL(inv_inv.assmbl_inv_no_db_id, inv_inv.h_inv_no_db_id)) = adjusted_usage_delta.assmbl_inv_db_id AND
               DECODE(inv_inv.inv_class_cd, 'ASSY', inv_inv.inv_no_id, NVL(inv_inv.assmbl_inv_no_id, inv_inv.h_inv_no_id))          = adjusted_usage_delta.assmbl_inv_id

      ) usage_to_update
   ON
      (
         usage_to_update.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
         usage_to_update.inv_no_id    = inv_curr_usage.inv_no_id
         AND
         usage_to_update.data_type_db_id = inv_curr_usage.data_type_db_id AND
         usage_to_update.data_type_id    = inv_curr_usage.data_type_id
      )
   WHEN MATCHED THEN UPDATE
      SET
        inv_curr_usage.tsn_qt = inv_curr_usage.tsn_qt + usage_to_update.adjusted_delta,
        inv_curr_usage.tso_qt = inv_curr_usage.tso_qt + usage_to_update.adjusted_delta,
        inv_curr_usage.tsi_qt = inv_curr_usage.tsi_qt + usage_to_update.adjusted_delta
   ;

   MERGE INTO
      usg_usage_data
   USING
   (
      SELECT /*+ OPT_PARAM('_B_TREE_BITMAP_PLANS','FALSE') */
         adjusted_usage_delta.inv_no_db_id,
         adjusted_usage_delta.inv_no_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta
      FROM
         (SELECT
            element1 as inv_no_db_id,
            element2 as inv_no_id,
            element3 as data_type_db_id,
            element4 as data_type_id,
            element5 as adjusted_delta
          FROM
             TABLE (aittb_adjusted_usage_delta)
         ) adjusted_usage_delta
   ) rvw_new_delta
   ON
   (
      usg_usage_data.inv_no_db_id = rvw_new_delta.inv_no_db_id AND
      usg_usage_data.inv_no_id    = rvw_new_delta.inv_no_id
      AND
      usg_usage_data.data_type_db_id = rvw_new_delta.data_type_db_id AND
      usg_usage_data.data_type_id    = rvw_new_delta.data_type_id
      AND
      usg_usage_data.usage_record_id = airaw_usage_adjustment_id
   )
   WHEN MATCHED THEN
      UPDATE
         SET
            usg_usage_data.tsn_qt = usg_usage_data.tsn_qt + rvw_new_delta.adjusted_delta
   ;

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
   aiv_system_note             IN VARCHAR2,
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
   
   v_ttb_assy_usage_deltadata IntIntIntIntFLOATTABLE;
   v_ttb_assy_usage_zerodeltadata IntIntIntIntFLOATTABLE;
   v_ttb_adjusted_usage_delta IntIntIntIntFLOATTABLE;
   v_ttb_adjusted_usage_zerodelta IntIntIntIntFLOATTABLE;
   v_ttb_inv_assmbl    IntIntIntIntTABLE;
   v_ttb_deadlines     IntIntIntIntTABLE;
   v_ttb_calc_deadline IntIntIntIntTABLE;

   vi_acft_inv_db_id INTEGER;
   vi_acft_inv_id INTEGER;
   vi_usage_dt DATE;
   vi_flight_leg RAW(16);
   vi_system_note VARCHAR2(4000);
   vi_zero_system_note VARCHAR2(4000);
   vi_skip_pre_proccess BOOLEAN := FALSE;

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
      IntIntIntIntFloatTuple(
         element1, element2, element3, element4, element5
      )
   BULK COLLECT INTO v_ttb_assy_usage_deltadata
   FROM
      TABLE (
         array_pkg.getIntIntIntIntFloatTable (
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
   
   --If the new date is not outside of the usage record window (determined earlier on via Java)
   IF aib_date_outside_window = 0 THEN
      --Check to see if there are any events/tasks that need updating
      vi_is_event_usage_adj_req := isEventUsageAdjustmentRequired(vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt, aid_new_usage_date);
      -- If there is no event work to be done
      IF vi_is_event_usage_adj_req = 0 THEN
         -- If there are no events to update, but there is a delta change
         IF v_ttb_adjusted_usage_delta.COUNT > 0 THEN
            -- Skip the pre process part of the logic
            vi_skip_pre_proccess := TRUE;
         ELSE
            --Update the usage record date, then leave.
            UPDATE usg_usage_record 
            SET usg_usage_record.usage_dt=aid_new_usage_date
            WHERE usage_record_id = airaw_usage_adjustment_id;
            RETURN;
         END IF;
      END IF;
   END IF;

   IF NOT(vi_skip_pre_proccess) THEN
      v_step := 10;
      
      SELECT
         leg_id
      INTO
         vi_flight_leg
      FROM
         fl_leg
      WHERE
         usage_record_id = airaw_usage_adjustment_id;
         
      v_ttb_zeroed_deltas.EXTEND(aittb_new_delta.count);
      
      -- create a new zeroed usage value table, as well as a negating history note
      FOR i IN 1..aittb_new_delta.count LOOP
          SELECT 0 INTO v_ttb_zeroed_deltas(i) FROM dual;
          IF(aiv_negating_system_note IS NOT NULL) THEN
            --add flight info to the system note
            vi_zero_system_note := ' <flight-leg id='||vi_flight_leg||'> <br> '||aiv_negating_system_note;
          END IF ;
      END LOOP;
 
      --process the usages data, setting the deltas to '0'
      -- combine set of input arrays into a table
      SELECT
         IntIntIntIntFloatTuple(
            element1, element2, element3, element4, element5
         )
      BULK COLLECT INTO v_ttb_assy_usage_zerodeltadata
      FROM
         TABLE (
            array_pkg.getIntIntIntIntFloatTable (
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
         vi_skip_pre_proccess := TRUE;
      END IF;
   END IF;
   
   v_step := 40;

   IF NOT(vi_skip_pre_proccess) THEN
      vi_is_not_latest_usage := isnotLatestUsageAdjustment(vi_acft_inv_db_id, vi_acft_inv_id, vi_usage_dt);
      -- latest flight, only update current usage and then skip to the next step
      IF vi_is_not_latest_usage = 0 THEN
         processLatestUsage(vi_acft_inv_db_id, vi_acft_inv_id, airaw_usage_adjustment_id, v_ttb_adjusted_usage_zerodelta);
         vi_skip_pre_proccess := TRUE;
      END IF;
   END IF;

   IF NOT(vi_skip_pre_proccess) THEN
      v_step := 50;

      v_ttb_inv_assmbl := get_acft_hist_config(airaw_usage_adjustment_id);

      v_step := 60;

      -- We have to update deadline first, the calculation of historic Tsn for subcomponent relays
      -- on the unmodified current tsn value and unmodified flight Tsn value.
      updateFollowingTaskDeadlines(airaw_usage_adjustment_id, v_ttb_adjusted_usage_zerodelta, v_ttb_inv_assmbl, vi_usage_dt, vi_zero_system_note, aiv_deadline_system_note, ain_hr_db_id, ain_hr_id, v_ttb_calc_deadline, v_ttb_deadlines);

      v_step := 70;
      updateCurrentTsn(v_ttb_adjusted_usage_zerodelta, v_ttb_inv_assmbl);

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
            
      v_step := 160;
   END IF;
   
   --Update the usage record date.
   UPDATE usg_usage_record 
   SET usg_usage_record.usage_dt=aid_new_usage_date
   WHERE usage_record_id = airaw_usage_adjustment_id;

   vi_usage_dt := aid_new_usage_date;

   v_step := 170;

   -- combine set of input arrays into a table
   SELECT
      IntIntIntIntFloatTuple(
         element1, element2, element3, element4, element5
      )
   BULK COLLECT INTO v_ttb_assy_usage_deltadata
   FROM
      TABLE (
         array_pkg.getIntIntIntIntFloatTable (
            aittb_assmbl_inv_db_id,
            aittb_assmbl_inv_id,
            aittb_data_type_db_id,
            aittb_data_type_id,
            aittb_new_delta
         )
      );

   v_step := 175;

   IF vi_skip_pre_proccess THEN
      v_ttb_adjusted_usage_delta := get_adjusted_usage_delta(airaw_usage_adjustment_id, v_ttb_assy_usage_deltadata);
   ELSE
      SELECT IntIntIntIntFloatTuple(
         element1, element2, element3, element4, element5
      ) BULK COLLECT INTO v_ttb_adjusted_usage_delta 
      FROM TABLE (v_ttb_assy_usage_deltadata)
      WHERE element5 != 0;
      --v_ttb_adjusted_usage_delta := v_ttb_assy_usage_deltadata;
   END IF;

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
   IF NOT(vi_skip_pre_proccess) THEN
      --add flight info to the system note
      vi_system_note := ' <flight-leg id='||vi_flight_leg||'> <br> '||aiv_ratify_system_note;
   --If we did skip the zeroing process, we can use the more simple note to only 
   -- show actual user based delta cahnges
   ELSIF (aiv_system_note IS NOT NULL) THEN
      vi_system_note := ' <flight-leg id='||vi_flight_leg||'> <br> '||aiv_system_note;
   END IF;


   v_step := 190;

   v_ttb_inv_assmbl := get_acft_hist_config(airaw_usage_adjustment_id);

   v_step := 200;

   -- We have to update deadline first, the calculation of historic Tsn for subcomponent relays
   -- on the unmodified current tsn value and unmodified flight Tsn value.
   updateFollowingTaskDeadlines(airaw_usage_adjustment_id, v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt, vi_system_note, aiv_deadline_system_note, ain_hr_db_id, ain_hr_id, v_ttb_calc_deadline, v_ttb_deadlines);

   v_step := 210;
   updateCurrentTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl);

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
   aiv_system_note             IN VARCHAR2,
   aiv_task_wrkpkg_system_note IN VARCHAR2,
   aiv_fault_system_note       IN VARCHAR2,
   aiv_deadline_system_note    IN VARCHAR2,
   ain_hr_db_id                IN NUMBER,
   ain_hr_id                   IN NUMBER
)
AS
   v_method_name VARCHAR2(30) := 'process_usage_data';

   v_ttb_assy_usage_deltadata IntIntIntIntFLOATTABLE;
   v_ttb_adjusted_usage_delta IntIntIntIntFLOATTABLE;
   v_ttb_inv_assmbl    IntIntIntIntTABLE;
   v_ttb_deadlines     IntIntIntIntTABLE;
   v_ttb_calc_deadline IntIntIntIntTABLE;

   vi_acft_inv_db_id INTEGER;
   vi_acft_inv_id INTEGER;
   vi_usage_dt DATE;
   vi_flight_leg RAW(16);
   vi_system_note VARCHAR2(4000);

   vi_is_not_latest_usage INTEGER;

BEGIN
   v_step := 10;

   -- combine set of input arrays into a table
   SELECT
      IntIntIntIntFloatTuple(
         element1, element2, element3, element4, element5
      )
   BULK COLLECT INTO v_ttb_assy_usage_deltadata
   FROM
      TABLE (
         array_pkg.getIntIntIntIntFloatTable (
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
   updateCurrentTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl);

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