--liquibase formatted sql


--changeSet getTaskDefnTotalManHours:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getTaskDefnTotalManHours
* Arguments:     aTaskDefnDbId, aTaskDefnId - pk for the task definition
*
* Description:   This function will return the total labour assigned to the task definition
*
* Orig.Coder:    jclarkin
* Recent Coder:  jclarkin
* Recent Date:   2010-20-27
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskDefnTotalManHours
(
    aTaskDefnDbId NUMBER,
    aTaskDefnId NUMBER
)   RETURN NUMBER
IS
    lTempManHours NUMBER;
    lManHours NUMBER;
    lTaskDbId task_task.task_db_id%TYPE;
    lTaskId task_task.task_db_id%TYPE;
    lWorkscope task_task.workscope_bool%TYPE;
    lClassModeCd ref_task_class.class_mode_cd%TYPE;
BEGIN
   lManHours := 0;

   SELECT
      task_task.task_db_id,
      task_task.task_id,
      task_task.workscope_bool,
      ref_task_class.class_mode_cd
   INTO
      lTaskDbId,
      lTaskId,
      lWorkscope,
      lClassModeCd
   FROM
      task_task
      INNER JOIN ref_task_class ON
         ref_task_class.task_class_db_id = task_task.task_class_db_id AND
         ref_task_class.task_class_cd = task_task.task_class_cd
   WHERE
      task_task.task_defn_db_id = aTaskDefnDbId AND
      task_task.task_defn_id = aTaskDefnId
      AND
      task_task.task_def_status_cd IN ( 'ACTV', 'BUILD');

   IF (lClassModeCd = 'BLOCK') THEN
      -- Get all labour on Executable REQs
      SELECT
         NVL(SUM((task_labour_list.work_perf_hr + task_labour_list.cert_hr + task_labour_list.insp_hr ) * task_labour_list.man_pwr_ct),0) AS manhrs
      INTO lTempManHours
      FROM
         task_task block_task
         INNER JOIN task_block_req_map ON
            task_block_req_map.block_task_db_id = block_task.task_db_id AND
            task_block_req_map.block_task_id    = block_task.task_id
         INNER JOIN task_task req_task ON
            req_task.task_defn_db_id = task_block_req_map.req_task_defn_db_id AND
            req_task.task_defn_id    = task_block_req_map.req_task_defn_id
         INNER JOIN task_labour_list ON
           task_labour_list.task_db_id = req_task.task_db_id AND
           task_labour_list.task_id    = req_task.task_id
      WHERE
         block_task.task_db_id = lTaskDbId AND
         block_task.task_id = lTaskId
         AND
         req_task.task_def_status_cd = 'ACTV'
         AND
         req_task.workscope_bool = 1;
      lManHours := lManHours + lTempManHours;

      -- Get all labour on JICs of non-Executable REQs
      SELECT
         NVL(SUM((task_labour_list.work_perf_hr + task_labour_list.cert_hr + task_labour_list.insp_hr ) * task_labour_list.man_pwr_ct),0) AS manhrs
      INTO lTempManHours
      FROM
         task_task block_task
         INNER JOIN task_block_req_map ON
            task_block_req_map.block_task_db_id = block_task.task_db_id AND
            task_block_req_map.block_task_id    = block_task.task_id
         INNER JOIN task_task req_task ON
            req_task.task_defn_db_id = task_block_req_map.req_task_defn_db_id AND
            req_task.task_defn_id    = task_block_req_map.req_task_defn_id
         INNER JOIN task_jic_req_map ON
           task_jic_req_map.req_task_defn_db_id = req_task.task_defn_db_id AND
           task_jic_req_map.req_task_defn_id    = req_task.task_defn_id
         INNER JOIN task_task jic_task ON
            jic_task.task_db_id = task_jic_req_map.jic_task_db_id AND
            jic_task.task_id    = task_jic_req_map.jic_task_id
         INNER JOIN task_labour_list ON
           task_labour_list.task_db_id = jic_task.task_db_id AND
           task_labour_list.task_id    = jic_task.task_id
      WHERE
         block_task.task_db_id = lTaskDbId AND
         block_task.task_id = lTaskId
         AND
         req_task.task_def_status_cd = 'ACTV'
         AND
         req_task.workscope_bool = 0
         AND
         jic_task.task_def_status_cd = 'ACTV';
      lManHours := lManHours + lTempManHours;

   ELSIF (lClassModeCd = 'REQ' AND lWorkscope = 0) THEN
      -- Get all labour on JICs of non-Executable REQ
      SELECT
         NVL(SUM((task_labour_list.work_perf_hr + task_labour_list.cert_hr + task_labour_list.insp_hr ) * task_labour_list.man_pwr_ct),0) AS manhrs
      INTO
         lManHours
      FROM
         task_task req_task
         INNER JOIN task_jic_req_map ON
           task_jic_req_map.req_task_defn_db_id = req_task.task_defn_db_id AND
           task_jic_req_map.req_task_defn_id    = req_task.task_defn_id
         INNER JOIN task_task jic_task ON
            jic_task.task_db_id = task_jic_req_map.jic_task_db_id AND
            jic_task.task_id    = task_jic_req_map.jic_task_id
         INNER JOIN task_labour_list ON
           task_labour_list.task_db_id = jic_task.task_db_id AND
           task_labour_list.task_id    = jic_task.task_id
      WHERE
         req_task.task_db_id = lTaskDbId AND
         req_task.task_id = lTaskId
         AND
         jic_task.task_def_status_cd = 'ACTV';
   ELSE
      -- Get all labour directly on this Task Definition
      SELECT
         NVL(SUM((task_labour_list.work_perf_hr + task_labour_list.cert_hr + task_labour_list.insp_hr ) * task_labour_list.man_pwr_ct),0) AS manhrs
      INTO
         lManHours
      FROM
         task_task
         INNER JOIN task_labour_list ON
           task_labour_list.task_db_id = task_task.task_db_id AND
           task_labour_list.task_id    = task_task.task_id
      WHERE
         task_task.task_db_id = lTaskDbId AND
         task_task.task_id = lTaskId;
   END IF;

   RETURN lManHours;
EXCEPTION
   WHEN NO_DATA_FOUND THEN RETURN 0;

END getTaskDefnTotalManHours;
/