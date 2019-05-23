--liquibase formatted sql


--changeSet DEV-1040:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Migration script for DEV-1040 for Batch Repairables (Section 6)
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
--
-- Purpose:
-- 1)	Remove all Scheduling details for REQ & REF task definitions tied to 
--    Batch Parts
-- 2)	Set 'On Condition = TRUE' for REQ & REF task definitions tied to Batch 
--    Parts
-- 3) Remove all Warranty Contracts tied to BATCH inventory
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
DECLARE

  -- Cursor with all task defns (REQs and REFs) tied to BATCH part numbers
  CURSOR lcur_AllBatchTaskDefns IS
    SELECT
      ttask.task_db_id,
      ttask.task_id,
      ttask.task_class_db_id,
      ttask.task_class_cd,
      ttask.task_def_status_db_id,
      ttask.task_def_status_cd,
      ttask.task_cd,
      ttask.task_name,
      ttask.task_ldesc
    FROM
      task_task ttask
        INNER JOIN ref_task_class tclass ON
          tclass.task_class_db_id = ttask.task_class_db_id AND
          tclass.task_class_cd = ttask.task_class_cd
        INNER JOIN task_part_map tpmap ON
          ttask.task_db_id = tpmap.task_db_id AND
          ttask.task_id = tpmap.task_id
        INNER JOIN eqp_part_no epno ON
          tpmap.part_no_db_id = epno.part_no_db_id AND
          tpmap.part_no_id = epno.part_no_id
    WHERE
      -- REQs and REFs only
      tclass.class_mode_cd IN ('REQ','REF')
      AND
      epno.inv_class_cd = 'BATCH';

  -- Cursor with warranties tied to BATCH inventory
  CURSOR lcur_AllBatchWarranties IS

    -- Gather all warranties linked to BATCH inventory
    SELECT
      warr.warranty_init_db_id,
      warr.warranty_init_id
    FROM
      warranty_init warr
        INNER JOIN warranty_init_inv warr_inv ON
          warr.warranty_init_db_id = warr_inv.warranty_init_db_id AND
          warr.warranty_init_id = warr_inv.warranty_init_id
        INNER JOIN inv_inv ON
          warr_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
          warr_inv.inv_no_id = inv_inv.inv_no_id
    WHERE
      inv_inv.inv_class_cd = 'BATCH';

BEGIN

  FOR lrec_BatchTaskDefn IN lcur_AllBatchTaskDefns
  LOOP

    -- 1)	Remove all Scheduling details for REQ & REF task definitions tied to
    --    Batch Parts
    DELETE
    FROM
      task_me_rule_interval
    WHERE
      (task_me_rule_interval.task_db_id, task_me_rule_interval.task_id) IN
        (
        SELECT
          task_me_rule.task_db_id,
          task_me_rule.task_id
        FROM
          task_me_rule
            INNER JOIN task_sched_rule ON
              task_me_rule.task_db_id = task_sched_rule.task_db_id AND
              task_me_rule.task_id = task_sched_rule.task_id
            INNER JOIN task_task ON
              task_sched_rule.task_db_id = task_task.task_db_id AND
              task_sched_rule.task_id = task_task.task_id
        WHERE
          task_task.task_db_id = lrec_BatchTaskDefn.task_db_id AND
          task_task.task_id = lrec_BatchTaskDefn.task_id
        );

    DELETE
    FROM
      task_me_rule
    WHERE
      (task_me_rule.task_db_id, task_me_rule.task_id) IN
        (
        SELECT
          task_me_rule.task_db_id,
          task_me_rule.task_id
        FROM
          task_sched_rule
            INNER JOIN task_task ON
              task_sched_rule.task_db_id = task_task.task_db_id AND
              task_sched_rule.task_id = task_task.task_id
        WHERE
          task_task.task_db_id = lrec_BatchTaskDefn.task_db_id AND
          task_task.task_id = lrec_BatchTaskDefn.task_id
        );

    DELETE
    FROM
      task_interval
    WHERE
      (task_interval.task_db_id, task_interval.task_id) IN
        (
        SELECT
          task_task.task_db_id,
          task_task.task_id
        FROM
          task_task
        WHERE
          task_task.task_db_id = lrec_BatchTaskDefn.task_db_id AND
          task_task.task_id = lrec_BatchTaskDefn.task_id
        );

    DELETE
    FROM
      task_sched_rule
    WHERE
      (task_sched_rule.task_db_id, task_sched_rule.task_id) IN
        (
        SELECT
          task_task.task_db_id,
          task_task.task_id
        FROM
          task_task
        WHERE
          task_task.task_db_id = lrec_BatchTaskDefn.task_db_id AND
          task_task.task_id = lrec_BatchTaskDefn.task_id
        );

    DELETE
    FROM
      task_deadline_ext
    WHERE
      (task_deadline_ext.task_db_id, task_deadline_ext.task_id) IN
        (
        SELECT
          task_task.task_db_id,
          task_task.task_id
        FROM
          task_task
        WHERE
          task_task.task_db_id = lrec_BatchTaskDefn.task_db_id AND
          task_task.task_id = lrec_BatchTaskDefn.task_id
        );

    -- 2)	Set 'On Condition = TRUE' for REQ & REF task definitions tied to Batch
    --    Parts and set them to be scheduled from the current effective date
    UPDATE
      task_task
    SET
      task_task.on_condition_bool = 1,
      task_task.relative_bool = 1,
      task_task.effective_dt = SYSDATE,
      task_task.effective_gdt = SYSDATE,
      task_task.sched_from_received_dt_bool = 0
    WHERE
      task_task.task_db_id = lrec_BatchTaskDefn.task_db_id AND
      task_task.task_id = lrec_BatchTaskDefn.task_id;

  END LOOP;

  -- 3) Remove all Warranties tied to BATCH inventory
  FOR lrec_BatchWarr IN lcur_AllBatchWarranties
  LOOP

    DELETE
    FROM
      warranty_init_task
    WHERE
      warranty_init_task.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
      warranty_init_task.warranty_init_id = lrec_BatchWarr.warranty_init_id;

    DELETE
    FROM
      warranty_init_inv
    WHERE
      warranty_init_inv.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
      warranty_init_inv.warranty_init_id = lrec_BatchWarr.warranty_init_id;

    DELETE
    FROM
      claim_part_line
    WHERE
      (claim_part_line.warranty_eval_db_id, claim_part_line.warranty_eval_id) IN
        (
        SELECT
          warranty_eval_part.warranty_eval_db_id, warranty_eval_part.warranty_eval_id
        FROM
          warranty_eval_part
            INNER JOIN warranty_eval_task ON
              warranty_eval_part.warranty_eval_db_id = warranty_eval_task.warranty_eval_db_id AND
              warranty_eval_part.warranty_eval_id = warranty_eval_task.warranty_eval_id
            INNER JOIN warranty_eval ON
              warranty_eval_task.warranty_eval_db_id = warranty_eval.warranty_eval_db_id AND
              warranty_eval_task.warranty_eval_id = warranty_eval.warranty_eval_id
        WHERE
          warranty_eval.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
          warranty_eval.warranty_init_id = lrec_BatchWarr.warranty_init_id
        );

    DELETE
    FROM
      claim_labour_line
    WHERE
      (claim_labour_line.warranty_eval_db_id, claim_labour_line.warranty_eval_id) IN
        (
        SELECT
          warranty_eval_part.warranty_eval_db_id, warranty_eval_part.warranty_eval_id
        FROM
          warranty_eval_part
            INNER JOIN warranty_eval_task ON
              warranty_eval_part.warranty_eval_db_id = warranty_eval_task.warranty_eval_db_id AND
              warranty_eval_part.warranty_eval_id = warranty_eval_task.warranty_eval_id
            INNER JOIN warranty_eval ON
              warranty_eval_task.warranty_eval_db_id = warranty_eval.warranty_eval_db_id AND
              warranty_eval_task.warranty_eval_id = warranty_eval.warranty_eval_id
        WHERE
          warranty_eval.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
          warranty_eval.warranty_init_id = lrec_BatchWarr.warranty_init_id
        );

    DELETE
    FROM
      warranty_eval_part
    WHERE
      (warranty_eval_part.warranty_eval_db_id, warranty_eval_part.warranty_eval_id) IN
        (
        SELECT
          warranty_eval_task.warranty_eval_db_id, warranty_eval_task.warranty_eval_id
        FROM
          warranty_eval_task
            INNER JOIN warranty_eval ON
              warranty_eval_task.warranty_eval_db_id = warranty_eval.warranty_eval_db_id AND
              warranty_eval_task.warranty_eval_id = warranty_eval.warranty_eval_id
        WHERE
          warranty_eval.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
          warranty_eval.warranty_init_id = lrec_BatchWarr.warranty_init_id
        );

    DELETE
    FROM
      warranty_eval_labour
    WHERE
      (warranty_eval_labour.warranty_eval_db_id, warranty_eval_labour.warranty_eval_id) IN
        (
        SELECT
          warranty_eval_task.warranty_eval_db_id, warranty_eval_task.warranty_eval_id
        FROM
          warranty_eval_task
            INNER JOIN warranty_eval ON
              warranty_eval_task.warranty_eval_db_id = warranty_eval.warranty_eval_db_id AND
              warranty_eval_task.warranty_eval_id = warranty_eval.warranty_eval_id
        WHERE
          warranty_eval.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
          warranty_eval.warranty_init_id = lrec_BatchWarr.warranty_init_id
        );

    DELETE
    FROM
      warranty_eval_task
    WHERE
      (warranty_eval_task.warranty_eval_db_id, warranty_eval_task.warranty_eval_id) IN
        (
        SELECT
          warranty_eval.warranty_eval_db_id, warranty_eval.warranty_eval_id
        FROM
          warranty_eval
        WHERE
          warranty_eval.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
          warranty_eval.warranty_init_id = lrec_BatchWarr.warranty_init_id
        );

    DELETE
    FROM
      warranty_eval_alert_queue
    WHERE
      (warranty_eval_alert_queue.warranty_eval_db_id, warranty_eval_alert_queue.warranty_eval_id) IN
        (
        SELECT
          warranty_eval.warranty_eval_db_id, warranty_eval.warranty_eval_id
        FROM
          warranty_eval
        WHERE
          warranty_eval.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
          warranty_eval.warranty_init_id = lrec_BatchWarr.warranty_init_id
        );

    DELETE
    FROM
      claim
    WHERE
      (claim.warranty_eval_db_id, claim.warranty_eval_id) IN
        (
        SELECT
          warranty_eval.warranty_eval_db_id, warranty_eval.warranty_eval_id
        FROM
          warranty_eval
        WHERE
          warranty_eval.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
          warranty_eval.warranty_init_id = lrec_BatchWarr.warranty_init_id
        );

    DELETE
    FROM
      warranty_eval
    WHERE
      warranty_eval.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
      warranty_eval.warranty_init_id = lrec_BatchWarr.warranty_init_id;

    DELETE
    FROM
      warranty_init
    WHERE
      warranty_init.warranty_init_db_id = lrec_BatchWarr.warranty_init_db_id AND
      warranty_init.warranty_init_id = lrec_BatchWarr.warranty_init_id;

  END LOOP;

END;
/