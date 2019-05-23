--liquibase formatted sql


--changeSet MX-23042:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getLabourAverage
(
   aTaskDbId  dwt_task_labour_summary.task_defn_db_id %TYPE,
   aTaskId    dwt_task_labour_summary.task_defn_id %TYPE,
   aSkillDbId dwt_task_labour_summary.labour_skill_db_id%TYPE,
   aSkillCd   dwt_task_labour_summary.labour_skill_cd%TYPE
) RETURN NUMBER
IS
   lAvgHr NUMBER;
BEGIN
            /** this table acquires average actual hours per person, skill, task */
              SELECT
                 DECODE( total_actual_man,
                         0,
                         0,
                         (total_actual_hrs/total_actual_man) ) AS avg_man_hr
              INTO
                 lAvgHr
              FROM
                (
                 /** acquire all completed total actual hours/person, task*/
                 SELECT
                     COUNT(*) as total_tasks,
                     sum(dwt_task_labour_summary.actual_man_pwr_ct) as total_actual_man,
                     sum(dwt_task_labour_summary.actual_total_man_hr) as total_actual_hrs
                 FROM
                      dwt_task_labour_summary
                  WHERE
                      dwt_task_labour_summary.task_db_id         = aTaskDbId AND
                      dwt_task_labour_summary.task_id            = aTaskId AND
                      dwt_task_labour_summary.labour_skill_db_id = aSkillDbId AND
                      dwt_task_labour_summary.labour_skill_cd    = aSkillCd
                 )labour_summary;

   /* Return the result */
   RETURN lAvgHr;
END;
/