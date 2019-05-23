--liquibase formatted sql

--changeSet DEADLINE_PKG:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY DEADLINE_PKG IS
-- Existing dealine logic still in prep_deadline_pkg
-- This new deadline pkg only contains newly added sp to recude using of deadline job

   c_pkg_name CONSTANT VARCHAR2(30) := 'DEADLINE_PKG';

   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);


PROCEDURE updateTaskDeadlineRemainUsage (
   ai_inv_db_id   IN NUMBER,
   ai_inv_id      IN NUMBER
)
AS

   v_method_name VARCHAR2(30) := 'updateTaskDeadlineRemainUsage';

   CURSOR lcur_snapshot IS
      SELECT
         (evt_sched_dead.sched_dead_qt - inv_curr_usage.tsn_qt) as new_rem,
         NVL(evt_sched_dead.deviation_qt, 0) as extended_usage,
         evt_sched_dead.event_db_id,
         evt_sched_dead.event_id,
         evt_sched_dead.data_type_db_id,
         evt_sched_dead.data_type_id
      FROM
      (
         SELECT
            inv_inv.inv_no_db_id,
            inv_inv.inv_no_id
         FROM
            inv_inv
         WHERE
            (
               inv_inv.h_inv_no_db_id = ai_inv_db_id AND
               inv_inv.h_inv_no_id    = ai_inv_id
               AND
               EXISTS 
                  ( SELECT 1
                    FROM inv_inv
                    WHERE
                       inv_inv.inv_no_db_id = ai_inv_db_id AND
                       inv_inv.inv_no_id    = ai_inv_id
                       AND
                       inv_inv.inv_class_db_id = 0 AND
                       inv_inv.inv_class_cd    = 'ACFT'
                  )
            )
            OR
            (
               inv_inv.assmbl_inv_no_db_id = ai_inv_db_id AND
               inv_inv.assmbl_inv_no_id    = ai_inv_id
               AND
               EXISTS 
                  ( SELECT 1
                    FROM inv_inv
                    WHERE
                       inv_inv.inv_no_db_id = ai_inv_db_id AND
                       inv_inv.inv_no_id    = ai_inv_id
                       AND
                       inv_inv.inv_class_db_id = 0 AND
                       inv_inv.inv_class_cd    = 'ASSY'
                  )
            )
      ) inv_list
      INNER JOIN inv_curr_usage
         ON
            inv_list.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
            inv_list.inv_no_id    = inv_curr_usage.inv_no_id
      INNER JOIN evt_inv
         ON
            evt_inv.inv_no_db_id = inv_curr_usage.inv_no_db_id AND
            evt_inv.inv_no_id    = inv_curr_usage.inv_no_id
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
            evt_event.event_status_cd in ('ACTV', 'IN WORK', 'PAUSE')
      INNER JOIN evt_sched_dead
         ON
            evt_sched_dead.event_id    = evt_event.event_id AND
            evt_sched_dead.event_db_id = evt_event.event_db_id
            AND
            evt_sched_dead.data_type_db_id = inv_curr_usage.data_type_db_id AND
            evt_sched_dead.data_type_id    = inv_curr_usage.data_type_id
      WHERE
            (evt_sched_dead.sched_dead_qt - inv_curr_usage.tsn_qt) != evt_sched_dead.usage_rem_qt
   ;

   TYPE ltyp_snapshot IS TABLE OF lcur_snapshot%ROWTYPE;
   ltab_snapshots ltyp_snapshot;
   l_return NUMBER;

BEGIN
   v_step := 10;

    OPEN lcur_snapshot;
    LOOP

       FETCH lcur_snapshot BULK COLLECT INTO ltab_snapshots LIMIT 10000;

       EXIT WHEN ltab_snapshots.COUNT = 0;

       --update deadline remaining
       FORALL i IN 1..ltab_snapshots.COUNT
         UPDATE
            evt_sched_dead
         SET
            evt_sched_dead.usage_rem_qt = ltab_snapshots(i).new_rem
         WHERE
            ltab_snapshots(i).event_db_id  = evt_sched_dead.event_db_id AND
            ltab_snapshots(i).event_id     = evt_sched_dead.event_id
            AND
            ltab_snapshots(i).data_type_db_id = evt_sched_dead.data_type_db_id AND
            ltab_snapshots(i).data_type_id    = evt_sched_dead.data_type_id;

       --update over due status if necessary
       FOR i IN 1..ltab_snapshots.COUNT LOOP
         EVENT_PKG.SetPriority( ltab_snapshots(i).event_db_id, ltab_snapshots(i).event_id, l_return );
       END LOOP;

   END LOOP;
   CLOSE lcur_snapshot;

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

END updateTaskDeadlineRemainUsage;

/*----------------------- End of Package -----------------------------------*/
END DEADLINE_PKG;
/