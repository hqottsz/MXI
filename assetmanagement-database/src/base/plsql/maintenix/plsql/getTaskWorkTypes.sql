--liquibase formatted sql


--changeSet getTaskWorkTypes:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Function:      getTaskWorkTypes
* Arguments:     aTaskDbId : aTaskId - Primary key of the task definition
*
* Description:   This function will return the list of work types for the task
*		             definition in a comma delimited string.
*
* Orig.Coder:    jbajer
* Recent Coder:  
* Recent Date:   2008.02.26
*********************************************************************************/
CREATE OR REPLACE FUNCTION getTaskWorkTypes
(
  aTaskDbId task_task.task_db_id%TYPE,
  aTaskId   task_task.task_id%TYPE
) RETURN  VARCHAR2
IS
  ls_work_types VARCHAR2(4000);

   CURSOR lcur_WorkTypes (
         cn_TaskDbId    IN task_task.task_db_id%TYPE,
         cn_TaskId      IN task_task.task_id%TYPE
      ) IS
      SELECT
         task_work_type.work_type_db_id,
         task_work_type.work_type_cd
      FROM
         task_work_type
      WHERE
         task_work_type.task_db_id = cn_TaskDbId AND
         task_work_type.task_id    = cn_TaskId
      ORDER BY
         task_work_type.work_type_cd;
   lrec_WorkTypes lcur_WorkTypes%ROWTYPE;
BEGIN

   FOR lrec_WorkTypes IN lcur_WorkTypes (aTaskDbId,aTaskId ) LOOP
       ls_work_types := ls_work_types || ', ' || lrec_WorkTypes.work_type_cd;
   END LOOP;

   IF ls_work_types IS NULL THEN
      RETURN NULL;
   END IF;

  RETURN substr(ls_work_types, 3);
END getTaskWorkTypes;
/