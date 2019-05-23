--liquibase formatted sql


--changeSet DEV-47:1 stripComments:false
-- Migration script for 1003 LRP concept: Section 2 (Enable Multiple Planners)
-- Author: Karan Mehta
-- Date  : January 12, 2010
-- Default the READ_ONLY BOOL to 0 for al rows in LRP_TASK_DEFN
UPDATE
  lrp_task_defn
SET
  lrp_task_defn.read_only_bool = 0;  

--changeSet DEV-47:2 stripComments:false
-- Default the READ_ONLY BOOL to 0 for al rows in LRP_INV_INV
UPDATE
  lrp_inv_inv
SET
  lrp_inv_inv.read_only_bool = 0;  

--changeSet DEV-47:3 stripComments:false
-- Migration for REF_LRP_CONFIG_SEV to be done by the migration team
-- changed made to the REF tables
-- Populate LRP_INV_TASK_PLAN table with the active plan
INSERT INTO
  lrp_inv_task_plan( inv_no_db_id, inv_no_id, task_defn_db_id, task_defn_id, lrp_db_id, lrp_id, published_dt ) 
SELECT DISTINCT
  lrp_inv_inv.inv_no_db_id,
  lrp_inv_inv.inv_no_id,   
  lrp_task_defn.task_defn_db_id,
  lrp_task_defn.task_defn_id,
  lrp_plan.lrp_db_id,
  lrp_plan.lrp_id,
  lrp_plan.published_dt
  FROM
  lrp_plan,
  lrp_task_defn,
  task_defn,
  task_task,
  lrp_inv_inv
  WHERE
  lrp_inv_inv.assmbl_db_id = task_task.assmbl_db_id and
  lrp_inv_inv.assmbl_cd = task_task.assmbl_cd
  AND
  task_task.task_defn_db_id = task_defn.task_defn_db_id and
  task_task.task_defn_id = task_defn.task_defn_id
  AND
  lrp_task_defn.task_defn_db_id = task_defn.task_defn_db_id and
  lrp_task_defn.task_defn_id = task_defn.task_defn_id
  AND
  lrp_task_defn.lrp_db_id = lrp_plan.lrp_db_id and
  lrp_task_defn.lrp_id = lrp_plan.lrp_id
  AND
  lrp_plan.active_bool = 1;  

--changeSet DEV-47:4 stripComments:false
-- Populate the LRP_INV_ADHOC_PLAN table with the active plan
INSERT INTO
  lrp_inv_adhoc_plan( inv_no_db_id, inv_no_id, lrp_db_id, lrp_id, published_dt )
SELECT
  lrp_inv_inv.inv_no_db_id,
  lrp_inv_inv.inv_no_id,
  lrp_plan.lrp_db_id,
  lrp_plan.lrp_id,
  lrp_plan.published_dt
FROM
  lrp_inv_inv,
  lrp_plan
WHERE
  lrp_plan.active_bool = 1
  AND
  lrp_plan.lrp_db_id = lrp_inv_inv.lrp_db_id and
  lrp_plan.lrp_id = lrp_inv_inv.lrp_id;  

--changeSet DEV-47:5 stripComments:false
-- Populate the LRP_LOC_ADHOC_PLAN table with the active plan
INSERT INTO 
  lrp_loc_adhoc_plan( loc_db_id, loc_id, lrp_db_id, lrp_id, published_dt )
SELECT
  lrp_loc.loc_db_id,
  lrp_loc.loc_id,
  lrp_plan.lrp_db_id,
  lrp_plan.lrp_id,
  lrp_plan.published_dt
FROM
  lrp_plan,
  lrp_loc
WHERE
  lrp_plan.active_bool = 1
  AND
  lrp_plan.lrp_db_id = lrp_loc.lrp_db_id and
  lrp_plan.lrp_id = lrp_loc.lrp_id;    