--liquibase formatted sql


--changeSet isTaskDefnRecurring:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:     isTaskDefnRecurring
* Arguments:    aTaskDbId, aTaskId 	- pk for the task definition revision
* Description:  This function determines if a task definition is recurring by
*               evaluating if the recurring_bool is true, or if it is a block chain
*
* Orig.Coder:    Cdaley
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isTaskDefnRecurring
(
    aTaskDbId task_task.task_db_id%TYPE,
    aTaskId task_task.task_id%TYPE
)  RETURN NUMBER
IS
   TYPE task_key IS RECORD(
        task_db_id task_task.task_db_id%TYPE,
        task_id task_task.task_id%TYPE,
        task_defn_db_id task_task.task_defn_db_id%TYPE,
        task_defn_id task_task.task_defn_id%TYPE
   );
   TYPE block_chain_table IS TABLE OF task_key INDEX BY BINARY_INTEGER;

   lTaskRecurring number;
   lTaskBlockChainSDesc varchar2(400);
   lFirst number;
   lLast number;

   blockChain block_chain_table;
BEGIN
   lTaskRecurring := 0;

   --get the recurring_bool, and the block chain
   SELECT
       task_task.block_chain_sdesc,
       task_task.recurring_task_bool
   INTO
       lTaskBlockChainSDesc,
       lTaskRecurring
   FROM
       task_task
   WHERE
       task_task.task_db_id = aTaskDbId AND
       task_task.task_id = aTaskId;

   --if there is no block chaing, return the recurring_bool
   IF (lTaskBlockChainSDesc IS NULL OR lTaskBlockChainSDesc = '') THEN
      RETURN lTaskRecurring;
   ELSE
      --get the block chain
       SELECT
           block_task_task.task_db_id,
           block_task_task.task_id,
           block_task_task.task_defn_db_id,
           block_task_task.task_defn_id
       BULK COLLECT INTO
           blockChain
       FROM
           task_task,
           task_task block_task_task
       WHERE
           task_task.task_db_id = aTaskDbId AND
           task_task.task_id = aTaskId
           AND
           block_task_task.block_chain_sdesc = task_task.block_chain_sdesc
           AND
           block_task_task.assmbl_db_id = task_task.assmbl_db_id AND
           block_task_task.assmbl_cd = task_task.assmbl_cd AND
           block_task_task.assmbl_bom_id = task_task.assmbl_bom_id
           AND
           block_task_task.revision_ord = task_task.revision_ord
       ORDER BY
           block_task_task.block_ord;
   END IF;

   -- if block chain is empty, then not recurring
   IF blockChain.COUNT = 0 THEN
       lTaskRecurring := 0;
   ELSE
       lFirst := blockChain.FIRST;
       lLast := blockChain.LAST;
       -- check if there is a cyclical dependancy in the block chain
       SELECT
           COUNT(*) as count_recurring
       INTO
           lTaskRecurring
       FROM
           task_task_dep
       WHERE
           task_task_dep.task_db_id = blockChain(lLast).task_db_id AND
           task_task_dep.task_id = blockChain(lLast).task_id
           AND
           task_task_dep.dep_task_defn_db_id = blockChain(lFirst).task_defn_db_id AND
           task_task_dep.dep_task_defn_id = blockChain(lFirst).task_defn_id;
   END IF;

   RETURN lTaskRecurring;

EXCEPTION
   WHEN OTHERS THEN
      lTaskRecurring := 0;
   RETURN lTaskRecurring;

END isTaskDefnRecurring;

/