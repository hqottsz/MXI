--liquibase formatted sql


--changeSet doesTaskLabourHaveWarranty:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      doesTaskLabourHaveWarranty
* Arguments:     aEventDbId, aEventId, aLabourId  - pk for the evt_labour row 
* Description:   This function will check if there are warranty results for the labour requirements
*
* Orig.Coder:    amccormack
* Recent Coder:  asachan 
* Recent Date:   Oct 08, 2009
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION doesTaskLabourHaveWarranty
(
   aLabourDbId  sched_labour.labour_db_id%TYPE,
   aLabourId    sched_labour.labour_id%TYPE
) RETURN VARCHAR
IS
   lWarrantyEvalDbId   warranty_eval_labour.warranty_eval_db_id%TYPE;
   lWarrantyEvalId     warranty_eval_labour.warranty_eval_id%TYPE;
   lWarrantyEvalTaskId warranty_eval_labour.warranty_eval_task_id%TYPE;
   lWarrantyEvalLabourId warranty_eval_labour.warranty_eval_labour_id%TYPE;
   lWarrantyEvalKey    VARCHAR(80);

 CURSOR lCur_TaskLabourWar IS
   SELECT
       warranty_eval_db_id,
       warranty_eval_id,
       warranty_eval_task_id,
       warranty_eval_labour_id
   FROM
      warranty_eval_labour
   WHERE
 	      warranty_eval_labour.labour_db_id     = aLabourDbId AND
	      warranty_eval_labour.labour_id        = aLabourId
   ORDER BY
        warranty_eval_labour.labour_id;

    lRecTaskLabourWar lCur_TaskLabourWar%ROWTYPE;

 BEGIN
    OPEN lCur_TaskLabourWar;
    FETCH lCur_TaskLabourWar INTO lRecTaskLabourWar;

     -- If no record return, return 0
     IF NOT lCur_TaskLabourWar%FOUND THEN
        lWarrantyEvalKey := NULL;
     ELSE
        lWarrantyEvalKey := lRecTaskLabourWar.warranty_eval_db_id || ':' || lRecTaskLabourWar.warranty_eval_id || ':' || lRecTaskLabourWar.warranty_eval_task_id || ':' || lRecTaskLabourWar.warranty_eval_labour_id;
     END IF;

     CLOSE lCur_TaskLabourWar;

     RETURN lWarrantyEvalKey;

END doesTaskLabourHaveWarranty;
/