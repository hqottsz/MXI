--liquibase formatted sql


--changeSet getSchedWorkTypes:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Function:      getSchedWorkTypes
* Arguments:     aSchedDbId : aSchedId - Primary key of the task
*
* Description:   This function will return the list of work types for the task
*		 in a comma delimited string.
*
* Orig.Coder:    jbajer
* Recent Coder:  
* Recent Date:   2008.02.26
*********************************************************************************/
CREATE OR REPLACE FUNCTION getSchedWorkTypes
(
  aSchedDbId sched_stask.sched_db_id%TYPE,
  aSchedId   sched_stask.sched_id%TYPE
) RETURN  VARCHAR2
IS
  ls_work_types VARCHAR2(4000);

   CURSOR lcur_WorkTypes (
         cn_SchedDbId    IN sched_stask.sched_db_id%TYPE,
         cn_SchedId      IN sched_stask.sched_id%TYPE
      ) IS
      SELECT
         sched_work_type.work_type_db_id,
         sched_work_type.work_type_cd
      FROM
         sched_work_type
      WHERE
         sched_work_type.sched_db_id = cn_SchedDbId AND
         sched_work_type.sched_id    = cn_SchedId
      ORDER BY
         sched_work_type.work_type_cd;
   lrec_WorkTypes lcur_WorkTypes%ROWTYPE;
BEGIN
   FOR lrec_WorkTypes IN lcur_WorkTypes (aSchedDbId, aSchedId ) LOOP
       ls_work_types := ls_work_types || ', ' || lrec_WorkTypes.work_type_cd;
   END LOOP;

   IF ls_work_types IS NULL THEN
      RETURN NULL;
   END IF;

  RETURN substr(ls_work_types, 3);
END getSchedWorkTypes;
/