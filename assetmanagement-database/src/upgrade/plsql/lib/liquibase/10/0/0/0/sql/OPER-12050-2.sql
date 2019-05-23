--liquibase formatted sql

--changeSet OPER-12050:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY FLIGHT_OUT_OF_SEQUENCE_PKG IS

   c_pkg_name CONSTANT VARCHAR2(30) := 'FLIGHT_OUT_OF_SEQUENCE_PKG';

   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);


FUNCTION get_adjusted_usage_delta(
   airaw_usage_adjustment_id   IN RAW,
   aittb_assy_usage_deltadata  IN STRSTRSTRSTRFLOATTABLE
) RETURN STRSTRSTRSTRFLOATTABLE
IS
   v_method_name VARCHAR2(30) := 'get_adjusted_usage_delta';

   v_ttb_adjusted_usage_delta STRSTRSTRSTRFLOATTABLE;

BEGIN

   v_step := 10;

   SELECT
      StrStrStrStrFloatTuple(
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
   WHERE
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
) RETURN STRSTRSTRSTRTABLE
IS
   v_method_name VARCHAR2(30) := 'get_acft_hist_config';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;
   v_ttb_assmbl STRSTRTABLE;

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
      StrStrTuple(inv_no_db_id, inv_no_id)
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
      StrStrStrStrTuple(
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
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
) RETURN STRSTRSTRSTRTABLE
IS
   v_method_name VARCHAR2(30) := 'filterInventoryHavingOverhaul';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;

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
      StrStrStrStrTuple(
         inv_assmbl_list.inv_no_db_id,
         inv_assmbl_list.inv_no_id,
         inv_assmbl_list.assmbl_inv_db_id,
         inv_assmbl_list.assmbl_inv_id
      )
   BULK COLLECT INTO v_ttb_inv_assmbl
   FROM inv_assmbl_list
   INNER JOIN (
     SELECT
        to_number(inv_no_db_id) as inv_no_db_id,
        to_number(inv_no_id) as inv_no_id
     FROM inv_assmbl_list
     MINUS
     SELECT DISTINCT
        sched_stask.main_inv_no_db_id,
        sched_stask.main_inv_no_id
     FROM
        sched_stask
     INNER JOIN evt_event ON
        sched_stask.sched_db_id = evt_event.event_db_id AND
        sched_stask.sched_id    = evt_event.event_id
     WHERE
        sched_stask.task_class_db_id = 0 AND
        sched_stask.task_class_cd = 'OVHL'
        AND
        evt_event.event_status_db_id = 0 AND
        evt_event.event_status_cd    = 'COMPLETE'
        AND
        -- select any overhauls occurring after the date ... so that their inventories may be filtered from the list
        evt_event.event_dt >= ai_usage_dt
   ) filtered ON
      inv_assmbl_list.inv_no_db_id = filtered.inv_no_db_id AND
      inv_assmbl_list.inv_no_id    = filtered.inv_no_id
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
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
) RETURN STRSTRSTRSTRTABLE
IS
   v_method_name VARCHAR2(30) := 'filterInventoryReinstalled';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;

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
      StrStrStrStrTuple(
         inv_assmbl_list.inv_no_db_id,
         inv_assmbl_list.inv_no_id,
         inv_assmbl_list.assmbl_inv_db_id,
         inv_assmbl_list.assmbl_inv_id
      )
   BULK COLLECT INTO v_ttb_inv_assmbl
   FROM inv_assmbl_list
   INNER JOIN (
     SELECT
        to_number(inv_no_db_id) as inv_no_db_id,
        to_number(inv_no_id) as inv_no_id
     FROM inv_assmbl_list
     MINUS
     SELECT DISTINCT
        evt_inv.inv_no_db_id,
        evt_inv.inv_no_id
     FROM
        evt_event
     INNER JOIN evt_inv ON
        evt_event.event_db_id = evt_inv.event_db_id AND
        evt_event.event_id    = evt_inv.event_id
     WHERE
        evt_event.event_status_db_id = 0 AND
        evt_event.event_status_cd = 'FGINST'
        AND
        -- select any installation occurring after the date ... so that their inventories may be filtered from the list
        evt_event.event_dt >= ai_usage_dt
        AND
        --select main inventory only
        evt_inv.main_inv_bool = 1
   ) filtered ON
      inv_assmbl_list.inv_no_db_id = filtered.inv_no_db_id AND
      inv_assmbl_list.inv_no_id    = filtered.inv_no_id
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


FUNCTION getTrkSerHistoricTsn(
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
) RETURN STRSTRSTRSTRFLOATTABLE
IS
   v_method_name VARCHAR2(30) := 'getTrkSerHistoricTsn';

   v_ttb_trkser_hist_tsn STRSTRSTRSTRFLOATTABLE;

BEGIN

   v_step := 10;

    WITH
    -- provide a list of TRK / SER only
    trk_ser_list AS
    (
       SELECT
               inv_inv.inv_no_db_id,
               inv_inv.inv_no_id,
               inv_inv.nh_inv_no_db_id,
               inv_inv.nh_inv_no_id,
               inv_inv.assmbl_inv_no_db_id,
               inv_inv.assmbl_inv_no_id,
               inv_inv.h_inv_no_db_id,
               inv_inv.h_inv_no_id
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
       INNER JOIN inv_inv
          ON
             inv_inv.inv_no_db_id = invlist.inv_no_db_id AND
             inv_inv.inv_no_id    = invlist.inv_no_id
       WHERE
          inv_inv.inv_class_db_id = 0 AND
          inv_inv.inv_class_cd IN ('TRK', 'SER')
    ),
    -- consolidate inventory install-remove paired list for trk/ser
    inv_onoff_list AS
    (
       SELECT
          rmvl.inv_no_db_id,
          rmvl.inv_no_id,
          rmvl.assmbl_inv_no_db_id,
          rmvl.assmbl_inv_no_id,
          NVL((
             SELECT
                MAX(inv_install.event_dt)
             FROM
                inv_install
             WHERE
                inv_install.inv_no_id    = rmvl.inv_no_id AND
                inv_install.inv_no_db_id = rmvl.inv_no_db_id
                AND
                inv_install.nh_inv_no_id    = rmvl.nh_inv_no_id AND
                inv_install.nh_inv_no_db_id = rmvl.nh_inv_no_db_id
                AND
                inv_install.assmbl_inv_no_id    = rmvl.assmbl_inv_no_id AND
                inv_install.assmbl_inv_no_db_id = rmvl.assmbl_inv_no_db_id
                AND
                inv_install.h_inv_no_id    = rmvl.h_inv_no_id AND
                inv_install.h_inv_no_db_id = rmvl.h_inv_no_db_id
                AND
                rmvl.remove_date >= inv_install.event_dt
                AND
                inv_install.event_dt >= ai_usage_dt
          ), ai_usage_dt ) AS install_date,
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
             FROM
                inv_remove
             INNER JOIN trk_ser_list
                ON
                   inv_remove.inv_no_db_id = trk_ser_list.inv_no_db_id AND
                   inv_remove.inv_no_id    = trk_ser_list.inv_no_id
             WHERE
                inv_remove.event_dt > ai_usage_dt
             -- If our inventory is currently installed then there won't be a remove record for it
             -- create a record with removal data set to null
             UNION
             SELECT
                trk_ser_list.inv_no_db_id,
                trk_ser_list.inv_no_id,
                trk_ser_list.nh_inv_no_db_id,
                trk_ser_list.nh_inv_no_id,
                trk_ser_list.assmbl_inv_no_db_id,
                trk_ser_list.assmbl_inv_no_id,
                trk_ser_list.h_inv_no_db_id,
                trk_ser_list.h_inv_no_id,
                sysdate
             FROM
                trk_ser_list
             WHERE
             -- assmble inv may be null but it still installed. We found such data in client database,
             -- we can NOT confirm if it is bad data (due to migration) or somewhere in maintenix will create such data.
                (
                trk_ser_list.assmbl_inv_no_id IS NOT NULL
                OR
                   (
                    trk_ser_list.inv_no_db_id != trk_ser_list.h_inv_no_db_id OR
                    trk_ser_list.inv_no_id    != trk_ser_list.h_inv_no_id
                   )
                )
          ) rmvl
    ),
    -- get total tsn delta for trk/ser
    inv_usage_delta AS
    (
       SELECT
          inv_onoff_list.inv_no_db_id,
          inv_onoff_list.inv_no_id,
          usg_usage_data.data_type_db_id,
          usg_usage_data.data_type_id,
          SUM (usg_usage_data.tsn_delta_qt) AS tsn_delta
       FROM
          usg_usage_record
       INNER JOIN usg_usage_data
          ON
             usg_usage_record.usage_record_id = usg_usage_data.usage_record_id
       INNER JOIN inv_onoff_list
          ON
             usg_usage_record.usage_dt >= inv_onoff_list.install_date AND 
             usg_usage_record.usage_dt <= inv_onoff_list.remove_date
       WHERE
             usg_usage_record.usage_dt > ai_usage_dt
             AND
                (
                   (
                      inv_onoff_list.inv_no_db_id = usg_usage_data.inv_no_db_id AND
                      inv_onoff_list.inv_no_id    = usg_usage_data.inv_no_id
                      AND
                      usg_usage_record.usage_type_cd = 'MXCORRECTION'  -- edit inv
                   )
                   OR
                   (
                      inv_onoff_list.assmbl_inv_no_db_id = usg_usage_data.inv_no_db_id AND
                      inv_onoff_list.assmbl_inv_no_id    = usg_usage_data.inv_no_id
                      AND
                      usg_usage_record.usage_type_cd IN ('MXFLIGHT', 'MXACCRUAL')  -- flight or usage record
                   )
                )
       GROUP BY inv_onoff_list.inv_no_db_id, inv_onoff_list.inv_no_id, usg_usage_data.data_type_db_id, usg_usage_data.data_type_id
    )
    -- finally, we calculate historic tsn at that moment
    SELECT
       StrStrStrStrFloatTuple(
          inv_curr_usage.inv_no_db_id,
          inv_curr_usage.inv_no_id,
          inv_curr_usage.data_type_db_id,
          inv_curr_usage.data_type_id,
          inv_curr_usage.tsn_qt - nvl(inv_usage_delta.tsn_delta, 0)
       )
    BULK COLLECT INTO v_ttb_trkser_hist_tsn
    FROM inv_curr_usage
    INNER JOIN trk_ser_list
       ON
          inv_curr_usage.inv_no_db_id = trk_ser_list.inv_no_db_id AND
          inv_curr_usage.inv_no_id    = trk_ser_list.inv_no_id
    LEFT JOIN inv_usage_delta
       ON
          inv_curr_usage.inv_no_db_id = inv_usage_delta.inv_no_db_id AND
          inv_curr_usage.inv_no_id    = inv_usage_delta.inv_no_id
          AND
          inv_curr_usage.data_type_db_id = inv_usage_delta.data_type_db_id AND
          inv_curr_usage.data_type_id    = inv_usage_delta.data_type_id
      ;

   RETURN v_ttb_trkser_hist_tsn;

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
END getTrkSerHistoricTsn;


PROCEDURE updateFollowingTaskDeadlines (
   airaw_usage_adjustment_id   IN RAW,
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingTaskDeadlines';

   v_ttb_trkser_hist_tsn STRSTRSTRSTRFLOATTABLE;

BEGIN
   v_step := 10;

   v_ttb_trkser_hist_tsn := getTrkSerHistoricTsn(aittb_inv_assmbl, ai_usage_dt);

   v_step := 20;

   MERGE INTO
      evt_sched_dead
   USING
   (
      SELECT
         evt_inv.event_db_id,
         evt_inv.event_id,
         adjusted_usage_delta.data_type_db_id,
         adjusted_usage_delta.data_type_id,
         adjusted_usage_delta.adjusted_delta,
         NVL(trkser_tsn.hist_tsn, usg_usage_data.tsn_qt) AS hist_tsn
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
   ) snapshot_to_update
   ON
   (
      snapshot_to_update.event_db_id  = evt_sched_dead.event_db_id AND
      snapshot_to_update.event_id     = evt_sched_dead.event_id
      AND
      snapshot_to_update.data_type_db_id = evt_sched_dead.data_type_db_id AND
      snapshot_to_update.data_type_id    = evt_sched_dead.data_type_id
   )
   WHEN MATCHED THEN
       UPDATE
         SET
            evt_sched_dead.start_qt      = evt_sched_dead.start_qt + snapshot_to_update.adjusted_delta,
            evt_sched_dead.sched_dead_qt = evt_sched_dead.sched_dead_qt + snapshot_to_update.adjusted_delta
       WHERE
          evt_sched_dead.start_qt >= snapshot_to_update.hist_tsn
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

END updateFollowingTaskDeadlines;


PROCEDURE updateFollowingEventTsn(
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
)
AS

   v_method_name VARCHAR2(30) := 'updateFollowingEventTsn';

BEGIN
   v_step := 10;

   MERGE INTO
      evt_inv_usage
   USING
   (
      SELECT
         evt_inv.event_db_id,
         evt_inv.event_id,
         evt_inv.event_inv_id,
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
      INNER JOIN evt_inv
         ON
            evt_inv.inv_no_db_id = invlist.inv_no_db_id AND
            evt_inv.inv_no_id    = invlist.inv_no_id
      INNER JOIN evt_event
         ON
            evt_inv.event_db_id = evt_event.event_db_id AND
            evt_inv.event_id    = evt_event.event_id
      LEFT JOIN sched_stask
         ON
            sched_stask.sched_db_id = evt_event.event_db_id AND
            sched_stask.sched_id    = evt_event.event_id
      WHERE
           ( -- looking for fault after found date (evt_event.actual_start_dt)
             ( evt_event.event_type_db_id = 0 AND
               evt_event.event_type_cd = 'CF'
               AND
               evt_event.actual_start_dt >= ai_usage_dt
             )
             OR
             -- looking for work package after start date (evt_event.actual_start_dt)
             ( evt_event.event_type_db_id = 0 AND
               evt_event.event_type_cd = 'TS'
               AND
               sched_stask.task_class_db_id = 0 AND
               sched_stask.task_class_cd IN ('CHECK', 'RO')
               AND
               evt_event.actual_start_dt >= ai_usage_dt
             )
             OR
             -- looking for task, not include work package after complete date (evt_event.event_dt)
             ( evt_event.event_type_db_id = 0 AND
               evt_event.event_type_cd = 'TS'
               AND
               (sched_stask.task_class_db_id != 0 OR
                sched_stask.task_class_cd NOT IN ('CHECK', 'RO')
               )
               AND
               evt_event.event_dt >= ai_usage_dt
             )
             OR
             -- looking for other events after event date (evt_event.event_dt)
             ( (evt_event.event_type_db_id != 0 OR
                evt_event.event_type_cd NOT IN ('TS', 'CF')
               )
               AND
               evt_event.event_dt >= ai_usage_dt
             )
           )
   ) snapshot_to_update
   ON
   (
     snapshot_to_update.event_db_id  = evt_inv_usage.event_db_id AND
     snapshot_to_update.event_id     = evt_inv_usage.event_id    AND
     snapshot_to_update.event_inv_id = evt_inv_usage.event_inv_id
     AND
     snapshot_to_update.data_type_db_id = evt_inv_usage.data_type_db_id AND
     snapshot_to_update.data_type_id    = evt_inv_usage.data_type_id
   )
   WHEN MATCHED THEN UPDATE
   SET
     evt_inv_usage.tsn_qt = evt_inv_usage.tsn_qt + snapshot_to_update.adjusted_delta
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

END updateFollowingEventTsn;


PROCEDURE updateFollowingEditInvOfTrkSer(
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
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
      SELECT
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
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
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
      SELECT
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


PROCEDURE updateCurrentTsn (
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE
)
AS

   v_method_name VARCHAR2(30) := 'updateCurrentTsn';

BEGIN
   v_step := 10;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT
            invlist.inv_no_db_id,
            invlist.inv_no_id,
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
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
)
AS
   v_method_name VARCHAR2(30) := 'updateCurrentTso';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;

BEGIN
   v_step := 10;

   v_ttb_inv_assmbl := filterInventoryHavingOverhaul(aittb_inv_assmbl, ai_usage_dt);

   v_step := 20;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT
            invlist.inv_no_db_id,
            invlist.inv_no_id,
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
                   TABLE (v_ttb_inv_assmbl)
            ) invlist
            ON
               invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
               invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
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
   aittb_adjusted_usage_delta IN STRSTRSTRSTRFLOATTABLE,
   aittb_inv_assmbl IN STRSTRSTRSTRTABLE,
   ai_usage_dt IN DATE
)
AS
   v_method_name VARCHAR2(30) := 'updateCurrentTsi';

   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;

BEGIN
   v_step := 10;

   v_ttb_inv_assmbl := filterInventoryReinstalled(aittb_inv_assmbl, ai_usage_dt);

   v_step := 20;

   MERGE INTO
      inv_curr_usage
   USING
      (
         SELECT
            invlist.inv_no_db_id,
            invlist.inv_no_id,
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
                   TABLE (v_ttb_inv_assmbl)
            ) invlist
            ON
               invlist.assmbl_inv_db_id = adjusted_usage_delta.assmbl_inv_db_id AND
               invlist.assmbl_inv_id    = adjusted_usage_delta.assmbl_inv_id
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


PROCEDURE process_usage_data (
   airaw_usage_adjustment_id   IN RAW,
   aittb_assmbl_inv_db_id  IN VARCHAR2TABLE,
   aittb_assmbl_inv_id     IN VARCHAR2TABLE,
   aittb_data_type_db_id   IN VARCHAR2TABLE,
   aittb_data_type_id      IN VARCHAR2TABLE,
   aittb_new_delta         IN FLOATTABLE
)
AS
   v_method_name VARCHAR2(30) := 'process_usage_data';

   v_ttb_assy_usage_deltadata STRSTRSTRSTRFLOATTABLE;
   v_ttb_adjusted_usage_delta STRSTRSTRSTRFLOATTABLE;
   v_ttb_inv_assmbl STRSTRSTRSTRTABLE;

   vi_acft_inv_db_id INTEGER;
   vi_acft_inv_id INTEGER;
   vi_usage_dt DATE;

BEGIN
   v_step := 10;

   -- combine sst of input arraies into a table
   SELECT
      StrStrStrStrFloatTuple(
         element1, element2, element3, element4, element5
      )
   BULK COLLECT INTO v_ttb_assy_usage_deltadata
   FROM
      TABLE (
         ARRAY_PKG.getStrStrStrStrFloatTable (
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

   v_step := 30;

   v_ttb_inv_assmbl := get_acft_hist_config(airaw_usage_adjustment_id);

   v_step := 40;

   -- Important!
   -- We have to update deadline first, the calculation of historic Tsn for subcomponent relays
   -- on the unmodified current tsn value and unmodified flight Tsn value.
   updateFollowingTaskDeadlines(airaw_usage_adjustment_id, v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 50;
   updateCurrentTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl);

   v_step := 60;
   updateCurrentTso(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 70;
   updateCurrentTsi(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 80;
   updateFollowingEventTsn(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 90;
   updateFollowingEditInvOfTrkSer(v_ttb_adjusted_usage_delta, v_ttb_inv_assmbl, vi_usage_dt);

   v_step := 100;
   updateFollowingUsgAdjustment(v_ttb_adjusted_usage_delta, vi_usage_dt);


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
