--liquibase formatted sql


--changeSet getLabourShedHr:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      getLabourSchedHr
* Arguments:     aTaskDefnDbId   - primary key of the task_defn 
*                aTaskDefnId     - primary key of the task_defn 
*                aSkillDbId - primary key of the labour skill
*                aSkillCd   - primary key of the labout skill
*
* Description:   the ACTV task defn revision's scheduled hours. 
*
* Returns:        null if no hours are scheduled.            
*
* Orig.Coder:    John Tang
* Recent Coder:  
* Recent Date:   October 30, 2007
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION getLabourSchedHr
(
   aTaskDefnDbId  dwt_task_labour_summary.task_defn_db_id %TYPE,
   aTaskDefnId    dwt_task_labour_summary.task_defn_id %TYPE,
   aSkillDbId     dwt_task_labour_summary.labour_skill_db_id%TYPE,
   aSkillCd       dwt_task_labour_summary.labour_skill_cd%TYPE
) RETURN NUMBER
IS
   lActvSchedHr NUMBER;
BEGIN

            /** this table acquires average actual hours per person, skill, task */
            SELECT DISTINCT
               dwt_task_labour_summary.sched_man_hr
            INTO
              lActvSchedHr
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
   RETURN lActvSchedHr;
END;
/