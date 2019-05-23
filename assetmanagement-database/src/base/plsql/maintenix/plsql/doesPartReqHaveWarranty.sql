--liquibase formatted sql


--changeSet doesPartReqHaveWarranty:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      doesPartReqHaveWarranty
* Arguments:     aSchedDbId, aSchedId, aSchedPartId  - pk for the sched part row 
* Description:   This function will check if there are warranty results for the part requirements
*
* Orig.Coder:    amccormack
* Recent Coder:   
* Recent Date:   Nov 10, 2008
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION doesPartReqHaveWarranty
(
   aSchedDbId   sched_part.sched_db_id%TYPE,
   aSchedId     sched_part.sched_id%TYPE,
   aSchedPartId sched_part.sched_part_id%TYPE
) RETURN VARCHAR
IS
   lWarrantyEvalDbId   warranty_eval_part.warranty_eval_db_id%TYPE;
   lWarrantyEvalId     warranty_eval_part.warranty_eval_id%TYPE;
   lWarrantyEvalTaskId warranty_eval_part.warranty_eval_task_id%TYPE;
   lWarrantyEvalPartId warranty_eval_part.warranty_eval_part_id%TYPE;
   lWarrantyEvalKey    VARCHAR(80);

 CURSOR lCur_TaskPartWar IS
   SELECT
       warranty_eval_db_id,
       warranty_eval_id,
       warranty_eval_task_id,
       warranty_eval_part_id
   FROM
      sched_part,
      warranty_eval_part
   WHERE
 	sched_part.sched_db_id 	 = aSchedDbId AND

 	sched_part.sched_id 	 = aSchedId AND

 	sched_part.sched_part_id = aSchedPartId AND
 	sched_part.rstat_cd = 0
 	AND
 	warranty_eval_part.workscope_sched_db_id   = sched_part.sched_db_id AND
	warranty_eval_part.workscope_sched_id      = sched_part.sched_id AND
	warranty_eval_part.workscope_sched_part_id = sched_part.sched_part_id
   ORDER BY
        warranty_eval_part.workscope_sched_part_id;

    lRecTaskPartWar lCur_TaskPartWar%ROWTYPE;

 BEGIN
    OPEN lCur_TaskPartWar;
    FETCH lCur_TaskPartWar INTO lRecTaskPartWar;

     -- If no record return, return 0
     IF NOT lCur_TaskPartWar%FOUND THEN
        lWarrantyEvalKey := NULL;
     ELSE
        lWarrantyEvalKey := lRecTaskPartWar.warranty_eval_db_id || ':' || lRecTaskPartWar.warranty_eval_id || ':' || lRecTaskPartWar.warranty_eval_task_id || ':' || lRecTaskPartWar.warranty_eval_part_id;
     END IF;

     CLOSE lCur_TaskPartWar;

     RETURN lWarrantyEvalKey;

END doesPartReqHaveWarranty;
/      