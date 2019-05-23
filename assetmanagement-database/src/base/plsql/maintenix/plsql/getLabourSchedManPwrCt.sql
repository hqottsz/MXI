--liquibase formatted sql


--changeSet getLabourSchedManPwrCt:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getLabourSchedManPwrCt
* Arguments:     aTaskDefnDbId   - primary key of the task_defn 
*                aTaskDefnId     - primary key of the task_defn 
*                aSkillDbId - primary key of the labour skill
*                aSkillCd   - primary key of the labout skill
*
* Description:   the ACTV task defn revision's scheduled man pwr ct. 
*
* Returns:        null if no hours are scheduled.            
*
* Orig.Coder:    John Tang
* Recent Coder:  
* Recent Date:   October 30, 2007
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getLabourSchedManPwrCt
(
   aTaskDefnDbId  dwt_task_labour_summary.task_defn_db_id %TYPE,
   aTaskDefnId    dwt_task_labour_summary.task_defn_id %TYPE,
   aSkillDbId     dwt_task_labour_summary.labour_skill_db_id%TYPE,
   aSkillCd       dwt_task_labour_summary.labour_skill_cd%TYPE
) RETURN NUMBER
IS
   lActvSchedManPwrCt NUMBER;
BEGIN

            /** this table acquires average actual hours per person, skill, task */
            SELECT DISTINCT
               dwt_task_labour_summary.sched_man_pwr_ct
            INTO
              lActvSchedManPwrCt
            FROM
                dwt_task_labour_summary,
                task_task
            WHERE
                dwt_task_labour_summary.task_defn_db_id      = aTaskDefnDbId AND
                dwt_task_labour_summary.task_defn_id         = aTaskDefnId AND
                dwt_task_labour_summary.labour_skill_db_id   = aSkillDbId AND
                dwt_task_labour_summary.labour_skill_cd      = aSkillCd
                AND
                task_task.task_db_id         =  dwt_task_labour_summary.task_db_id AND
                task_task.task_id            =  dwt_task_labour_summary.task_id AND
                task_task.task_def_status_cd = 'ACTV'
             ;

   /* Return the result */
   RETURN lActvSchedManPwrCt;
END;
/