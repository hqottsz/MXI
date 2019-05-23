--liquibase formatted sql


--changeSet MX-17231:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 CREATE OR REPLACE PACKAGE BODY "BASELINESYNCPKG" IS
 /*
    Package:       BaselineSyncPkg
    Description:   This package is used to perform stored procedures related to baseline synchronizations
 
    Author:        Chris Daley
    Created Date:  Apr. 7th, 2009
 */
 
 /*
    Procedure:     updateBlockZiplist
 
    Arguments:     aTaskDbId   NUMBER   the block db id
                   aTaskId     NUMBER   the block id
                   aZipId      NUMBER   The zipqueue that the tasks are to be associated with
                   aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred
 
    Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision.
    Author:        Chris Daley
    Created Date:  Apr. 7th, 2009
 */
 PROCEDURE updateBlockZiplist(
    aZipId      IN    NUMBER,
    aTaskDbId   IN    task_task.task_db_id%TYPE,
    aTaskId     IN    task_task.task_id%TYPE,
    aResult     OUT   NUMBER
 )
 AS
       -- tables of predefined keys
       TYPE zipId IS TABLE OF zip_task.zip_id%TYPE;
       TYPE schedDbId IS TABLE OF zip_task.sched_db_id%TYPE;
       TYPE schedId IS TABLE OF zip_task.sched_id%TYPE;
       TYPE classModeCd IS TABLE OF zip_task.class_mode_cd%TYPE;
       TYPE assmblInvNoDbId IS TABLE OF zip_task.assmbl_inv_no_db_id%TYPE;
       TYPE assmblInvNoId IS TABLE OF zip_task.assmbl_inv_no_id%TYPE;
 
       -- variables for the task definitions
       lBlockList mxkeytable;
       lReqList mxkeytable;
 
       -- variables for the ziplist
       lZipId zipId;
       lSchedDbId schedDbId;
       lSchedId schedId;
       lClassModeCd classModeCd;
       lAssmblInvNoDbId assmblInvNoDbId;
       lAssmblInvNoId assmblInvNoId;
 BEGIN
    aResult := -1;
 
    -- create a table to store the BLOCK objects
    lBlockList := mxkeytable();
 
    -- create a table to store the REQ objects
    lReqList := mxkeytable();
 
    -- create tables for the ziplist()
    lZipId := zipId();
    lSchedDbId := schedDbId();
    lSchedId := schedId();
    lClassModeCd := classModeCd();
    lAssmblInvNoDbId := assmblInvNoDbId();
    lAssmblInvNoId := assmblInvNoId();
 
    -- get the list of BLOCK task definition revisions
    SELECT
       mxkey(task_db_id, task_id)
    BULK COLLECT INTO lBlockList
    FROM
    (
       -- get the single blocks (no chain)
       SELECT
          task_task.task_db_id,
          task_task.task_id
       FROM
          task_task
       WHERE
          task_task.task_db_id = aTaskDbId AND
          task_task.task_id = aTaskId
          AND
          task_task.block_chain_sdesc IS NULL
       UNION
       -- get the block chains
       SELECT
          block_chain.task_db_id,
          block_chain.task_id
       FROM
          task_task INNER JOIN task_task block_chain
             ON block_chain.block_chain_sdesc = task_task.block_chain_sdesc AND
                block_chain.assmbl_db_id = task_task.assmbl_db_id AND
                block_chain.assmbl_cd = task_task.assmbl_cd AND
                block_chain.assmbl_bom_id = task_task.assmbl_bom_id
       WHERE
          task_task.task_db_id = aTaskDbId AND
          task_task.task_id = aTaskId
    );
 
    -- define factored subquery to store block chain task definition information
    WITH blockChains AS
    (
       SELECT
          single_block.task_db_id,
          single_block.task_id,
          single_block.task_defn_db_id,
          single_block.task_defn_id,
          single_block.revision_ord
       FROM
          task_task single_block
       WHERE
          single_block.task_db_id = aTaskDbId AND
          single_block.task_id = aTaskId
          AND
          single_block.block_chain_sdesc IS NULL
       UNION ALL
       SELECT
          chain_block.task_db_id,
          chain_block.task_id,
          chain_block.task_defn_db_id,
          chain_block.task_defn_id,
          chain_block.revision_ord
       FROM
          task_task single_block INNER JOIN task_task chain_block
             ON chain_block.block_chain_sdesc = single_block.block_chain_sdesc AND
                chain_block.assmbl_db_id = single_block.assmbl_db_id AND
                chain_block.assmbl_cd = single_block.assmbl_cd AND
                chain_block.assmbl_bom_id = single_block.assmbl_bom_id AND
             chain_block.revision_ord = single_block.revision_ord
       WHERE
          single_block.task_db_id = aTaskDbId AND
          single_block.task_id = aTaskId
 
    )
    SELECT
       mxkey(req_task_defn_db_id,req_task_defn_id)
    BULK COLLECT INTO lReqList
    FROM
    (
       -- Get Mappings for this Block/Chain revision and the prior revision
       SELECT
          task_block_req_map.req_task_defn_db_id,
          task_block_req_map.req_task_defn_id
       FROM
          blockChains INNER JOIN task_block_req_map
             ON    task_block_req_map.block_task_db_id = blockChains.task_db_id AND
                   task_block_req_map.block_task_id = blockChains.task_id
       UNION
       SELECT
          task_block_req_map.req_task_defn_db_id,
          task_block_req_map.req_task_defn_id
       FROM
          blockChains INNER JOIN task_task prev_block_task
             ON prev_block_task.task_defn_db_id = blockChains.task_defn_db_id AND
                prev_block_task.task_defn_id = blockChains.task_defn_id AND
                prev_block_task.revision_ord = (blockChains.revision_ord - 1)
          INNER JOIN task_block_req_map
             ON task_block_req_map.block_task_db_id = prev_block_task.task_db_id AND
                task_block_req_map.block_task_id = prev_block_task.task_id
    );
 
    -- get all the actual BLOCKs
    SELECT DISTINCT
       aZipId,
       sched_stask.sched_db_id,
       sched_stask.sched_id,
       ref_task_class.class_mode_cd,
       CASE
          WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
          WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
          ELSE inv_inv.assmbl_inv_no_db_id
       END AS assmbl_inv_db_id,
       CASE
          WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
          WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
          ELSE inv_inv.assmbl_inv_no_id
       END AS assmbl_inv_id
    BULK COLLECT INTO lZipId, lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
    FROM
       TABLE(lBlockList) blocks INNER JOIN task_task ON
          blocks.db_id = task_task.task_db_id AND blocks.ID = task_task.task_id
       INNER JOIN task_task prev_revisions ON
          task_task.task_defn_db_id = prev_revisions.task_defn_db_id AND task_task.task_defn_id = prev_revisions.task_defn_id
       INNER JOIN sched_stask
          ON prev_revisions.task_db_id = sched_stask.task_db_id AND prev_revisions.task_id = sched_stask.task_id
       INNER JOIN ref_task_class
          ON sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND sched_stask.task_class_cd = ref_task_class.task_class_cd
       INNER JOIN  evt_event
          ON sched_stask.sched_db_id = evt_event.event_db_id AND sched_stask.sched_id = evt_event.event_id
       INNER JOIN  evt_inv
          ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
       INNER JOIN  inv_inv
          ON evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND evt_inv.inv_no_id = inv_inv.inv_no_id
       LEFT OUTER JOIN inv_inv assmbl_inv
          ON inv_inv.assmbl_inv_no_db_id = assmbl_inv.inv_no_db_id AND inv_inv.assmbl_inv_no_id = assmbl_inv.inv_no_id
    WHERE
       evt_event.hist_bool = 0
       AND
       evt_inv.main_inv_bool = 1
       AND
       inv_inv.prevent_synch_bool = 0
       AND
       inv_inv.locked_bool = 0;
 
    -- insert the actual blocks
    FORALL i in lZipId.first .. lZipId.last
       INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
       VALUES (   lZipId(i),
                lSchedDbId(i),
                lSchedId(i),
                lClassModeCd(i),
                lAssmblInvNoDbId(i),
                lAssmblInvNoId(i)
            );
 
    -- get all the actual REQs
    SELECT
       aZipId,
       sched_stask.sched_db_id,
       sched_stask.sched_id,
       ref_task_class.class_mode_cd,
       CASE
          WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
          WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
          ELSE inv_inv.assmbl_inv_no_db_id
       END AS assmbl_inv_db_id,
       CASE
          WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
          WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
          ELSE inv_inv.assmbl_inv_no_id
       END AS assmbl_inv_id
    BULK COLLECT INTO lZipId, lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
    FROM
       TABLE(lReqList) reqs INNER JOIN task_task
          ON reqs.db_id = task_task.task_defn_db_id AND reqs.id = task_task.task_defn_id
       INNER JOIN sched_stask
          ON task_task.task_db_id = sched_stask.task_db_id AND task_task.task_id = sched_stask.task_id
       INNER JOIN ref_task_class
          ON sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND sched_stask.task_class_cd = ref_task_class.task_class_cd
       INNER JOIN  evt_event
          ON sched_stask.sched_db_id = evt_event.event_db_id AND sched_stask.sched_id = evt_event.event_id
       INNER JOIN  evt_inv
          ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
       INNER JOIN  inv_inv
          ON evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND evt_inv.inv_no_id = inv_inv.inv_no_id
       LEFT OUTER JOIN inv_inv assmbl_inv
          ON inv_inv.assmbl_inv_no_db_id = assmbl_inv.inv_no_db_id AND inv_inv.assmbl_inv_no_id = assmbl_inv.inv_no_id
    WHERE
       evt_event.hist_bool = 0
       AND
       evt_inv.main_inv_bool = 1
       AND
       inv_inv.prevent_synch_bool = 0
       AND
       inv_inv.locked_bool = 0;
 
    -- insert the actual reqs
    FORALL i in lZipId.first .. lZipId.last
       INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
       VALUES (   lZipId(i),
                lSchedDbId(i),
                lSchedId(i),
                lClassModeCd(i),
                lAssmblInvNoDbId(i),
                lAssmblInvNoId(i)
            );
 
    removeDuplicateQueues(aZipId, aResult);
 
    -- set the result to 1 indicating successful operation
    aResult := 1;
 
 -- if any exception occurs, return -1
 EXCEPTION
       WHEN OTHERS THEN
          aResult := -1;
          APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', SQLERRM);
 END updateBlockZipList;
 
 /*
    Procedure:     updateZipFullBlockOnTree
 
    Arguments:     aBlockDbId  NUMBER   the block task db id
                   aBlockId       NUMBER   the block task id
         aInvNoDbId   NUMBER   the inventory db id
         aInvNoId  NUMBER    the inventory id
                   aZipId         NUMBER   The zipqueue that the tasks are to be associated with
                   aResult        NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred
 
    Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision and invenotry.
    Author:        Yungjae Cho
    Created Date:  Aug. 10th, 2009
 */
 PROCEDURE updateZipFullBlockOnTree(
    aZipId      IN    NUMBER,
    aBlockDbId  IN    sched_stask.sched_db_id%TYPE,
    aBlockId    IN    sched_stask.sched_id%TYPE,
    aInvNoDbId  IN    inv_inv.inv_no_db_id%TYPE,
    aInvNoId    IN    inv_inv.inv_no_id%TYPE,
    aResult     OUT   NUMBER
 )
 AS
       -- tables of predefined keys
       TYPE zipId IS TABLE OF zip_task.zip_id%TYPE;
       TYPE schedDbId IS TABLE OF zip_task.sched_db_id%TYPE;
       TYPE schedId IS TABLE OF zip_task.sched_id%TYPE;
       TYPE classModeCd IS TABLE OF zip_task.class_mode_cd%TYPE;
       TYPE assmblInvNoDbId IS TABLE OF zip_task.assmbl_inv_no_db_id%TYPE;
       TYPE assmblInvNoId IS TABLE OF zip_task.assmbl_inv_no_id%TYPE;
 
       -- variables for the table keys
       lBlockList mxkeytable;
       lSchedTaskList mxkeytable;
       lInventoryList mxkeytable;
 
       -- variables for the ziplist
       lZipId zipId;
       lSchedDbId schedDbId;
       lSchedId schedId;
       lClassModeCd classModeCd;
       lAssmblInvNoDbId assmblInvNoDbId;
       lAssmblInvNoId assmblInvNoId;
 BEGIN
    aResult := -1;
 
    -- create a table to store the BLOCK objects
    lBlockList := mxkeytable();
 
    -- create a table to store the actual task objects
    lSchedTaskList := mxkeytable();
 
    -- create a table to store the assembly inventory tree
    lInventoryList := mxkeytable();
 
    -- create tables for the ziplist()
    lZipId := zipId();
    lSchedDbId := schedDbId();
    lSchedId := schedId();
    lClassModeCd := classModeCd();
    lAssmblInvNoDbId := assmblInvNoDbId();
    lAssmblInvNoId := assmblInvNoId();
 
    -- get the list of BLOCK task definition revisions
    SELECT
       mxkey(task_db_id, task_id)
    BULK COLLECT INTO lBlockList
    FROM
    (
       -- get the single blocks (no chain)
       SELECT
          task_task.task_db_id,
          task_task.task_id
       FROM
     sched_stask block_stask INNER JOIN task_task
        ON    task_task.task_db_id = block_stask.task_db_id AND
            task_task.task_id    = block_stask.task_id
       WHERE
          block_stask.sched_db_id = aBlockDbId AND
          block_stask.sched_id    = aBlockId
          AND
          task_task.block_chain_sdesc IS NULL
       UNION
       -- get the block chains
       SELECT
          block_chain.task_db_id,
          block_chain.task_id
       FROM
     sched_stask block_stask INNER JOIN task_task
        ON    task_task.task_db_id = block_stask.task_db_id AND
            task_task.task_id    = block_stask.task_id
     INNER JOIN task_task block_chain
        ON    block_chain.block_chain_sdesc = task_task.block_chain_sdesc AND
            block_chain.assmbl_db_id = task_task.assmbl_db_id AND
            block_chain.assmbl_cd = task_task.assmbl_cd AND
            block_chain.assmbl_bom_id = task_task.assmbl_bom_id
       WHERE
          block_stask.sched_db_id = aBlockDbId AND
          block_stask.sched_id    = aBlockId
    );
 
     -- Get the Baseline link to all Actual REQs and BLOCKS
    SELECT
       mxkey(sched_db_id, sched_id)
    BULK COLLECT INTO lSchedTaskList
    FROM
    (
       -- Get actual REQs
       SELECT /*+ cardinality (blocks 10) */
          sched_stask.sched_db_id,
     sched_stask.sched_id
       FROM
          TABLE(lBlockList) blocks INNER JOIN task_block_req_map
        ON   task_block_req_map.block_task_db_id = blocks.db_Id AND
           task_block_req_map.block_task_id    = blocks.id
     INNER JOIN task_task req_task
        ON   req_task.task_defn_db_id = task_block_req_map.req_task_defn_db_id AND
             req_task.task_defn_id    = task_block_req_map.req_task_defn_id
     INNER JOIN sched_stask
        ON   sched_stask.task_db_id = req_task.task_db_id AND
             sched_stask.task_id  = req_task.task_id
       UNION
       -- Get actual BLOCKS
       SELECT /*+ cardinality (blocks 10) */
          sched_stask.sched_db_id,
     sched_stask.sched_id
       FROM
          TABLE(lBlockList) blocks INNER JOIN sched_stask
        ON   sched_stask.task_db_id = blocks.db_id AND
           sched_stask.task_id    = blocks.id
    );
 
       -- Lookup of the Assembly inv tree based on having the root assembly key
       -- Include the Root Assembly and any inv that belong to that root Assembly
       SELECT
          mxkey(inv_no_db_id, inv_no_id)
     BULK COLLECT INTO lInventoryList
       FROM
       (
          SELECT
             inv_inv.inv_no_db_id,
             inv_inv.inv_no_id
          FROM inv_inv
          WHERE
             -- Get the base inventory
             inv_inv.inv_no_db_id = aInvNoDbId AND
             inv_inv.inv_no_id    = aInvNoId
             AND
             -- Limit to ASSY or ACFT cases
             ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) IN ((0, 'ACFT'), (0, 'ASSY'))
          UNION ALL
          SELECT
             assmbl_inv.inv_no_db_id,
             assmbl_inv.inv_no_id
          FROM
             inv_inv assmbl_inv INNER JOIN inv_inv
                ON   assmbl_inv.assmbl_inv_no_db_id = inv_inv.inv_no_db_id AND
                    assmbl_inv.assmbl_inv_no_id    = inv_inv.inv_no_id
          WHERE
             -- Get the base inventory
             inv_inv.inv_no_db_id = aInvNoDbId AND
             inv_inv.inv_no_id    = aInvNoId
             AND
             -- Limit to ASSY or ACFT cases
             ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) IN ((0, 'ACFT'), (0, 'ASSY'))
          UNION ALL
          -- Lookup of the Assembly inv tree based on not having the root assembly key
          -- Include all those sharing the Root Assembly and the Root Assembly inv as well
          SELECT
             assmbl_inv.inv_no_db_id,
             assmbl_inv.inv_no_id
          FROM
             inv_inv assmbl_inv INNER JOIN inv_inv
                ON   assmbl_inv.assmbl_inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                     assmbl_inv.assmbl_inv_no_id    = inv_inv.assmbl_inv_no_id
          WHERE
             -- Get the base inventory
             inv_inv.inv_no_db_id = aInvNoDbId AND
             inv_inv.inv_no_id    = aInvNoId
             AND
             -- Limit to sub node cases
             ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) NOT IN ((0, 'ACFT'), (0, 'ASSY'))
          UNION ALL
          SELECT
             inv_inv.assmbl_inv_no_db_id,
             inv_inv.assmbl_inv_no_id
          FROM
             inv_inv assmbl_inv INNER JOIN inv_inv
                ON   assmbl_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                       assmbl_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
          WHERE
             -- Get the base inventory
             inv_inv.inv_no_db_id = aInvNoDbId AND
             inv_inv.inv_no_id    = aInvNoId
             AND
             -- Limit to sub node cases
             ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) NOT IN ((0, 'ACFT'), (0, 'ASSY'))
    );
 
 
    -- get all the actual tasks
    SELECT DISTINCT
       aZipId,
       sched_stask.sched_db_id,
       sched_stask.sched_id,
       ref_task_class.class_mode_cd,
       CASE
          WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
          WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
          ELSE inv_inv.assmbl_inv_no_db_id
       END AS assmbl_inv_db_id,
       CASE
          WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
          WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
          ELSE inv_inv.assmbl_inv_no_id
       END AS assmbl_inv_id
    BULK COLLECT INTO lZipId, lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
    FROM
       TABLE(lSchedTaskList) tasks INNER JOIN sched_stask
          ON tasks.db_id = sched_stask.sched_db_id AND tasks.id = sched_stask.sched_id
       INNER JOIN ref_task_class
          ON sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND sched_stask.task_class_cd = ref_task_class.task_class_cd
       INNER JOIN  evt_event
          ON sched_stask.sched_db_id = evt_event.event_db_id AND sched_stask.sched_id = evt_event.event_id
       INNER JOIN  evt_inv
          ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
       INNER JOIN inv_inv
          ON evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND evt_inv.inv_no_id = inv_inv.inv_no_id
       INNER JOIN  TABLE(lInventoryList) inventory
          ON inv_inv.inv_no_db_id = inventory.db_id AND inv_inv.inv_no_id = inventory.id
    WHERE
       evt_event.hist_bool = 0 AND
       evt_inv.main_inv_bool = 1
       AND
       inv_inv.prevent_synch_bool = 0 AND
       inv_inv.locked_bool = 0;
 
    -- insert the actual tasks
    FORALL i in lZipId.first .. lZipId.last
       INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
       VALUES (   lZipId(i),
                lSchedDbId(i),
                lSchedId(i),
                lClassModeCd(i),
                lAssmblInvNoDbId(i),
                lAssmblInvNoId(i)
            );
 
    removeDuplicateQueues(aZipId, aResult);
 
 
    -- set the result to 1 indicating successful operation
    aResult := 1;
 
 -- if any exception occurs, return -1
 EXCEPTION
       WHEN OTHERS THEN
          aResult := -1;
          APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', SQLERRM);
 END updateZipFullBlockOnTree;
 
 /********************************************************************************
 *
 * Procedure:    RequestZipForNewReq
 * Arguments:
 *        an_InvNoDbId             The inventory on which the task is created
 *        an_InvNoId               "  "
 *        an_BlockDbId             The Task requesting Zipping
 *        an_BlockId               "  "
 *
 * Return:
 *        on_Return        (long) - Success/Failure of requesting Zip
 *
 * Description:  This procedure is used to queue a new requirement for
 *                 Baseline Sync zipping
 *
 * Orig.Coder:     Jonathan Clarkin
 * Recent Coder:   Jonathan Clarkin
 *
 *********************************************************************************
 *
 * Copyright 1997-2008 Mxi Technologies Ltd.  All Rights Reserved.
 * Any distribution of the Mxi source code by any other party than
 * Mxi Technologies Ltd is prohibited.
 *
 *********************************************************************************/
 PROCEDURE RequestZipForNewReq(
       an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
       an_InvNoId            IN inv_inv.inv_no_id%TYPE,
       an_ReqDbId            IN sched_stask.sched_db_id%TYPE,
       an_ReqId              IN sched_stask.sched_id%TYPE,
       on_Return             OUT NUMBER
    ) IS
 
    /* *** DECLARE LOCAL VARIABLES *** */
    ln_ZipId          zip_queue.zip_id%TYPE;
    ln_IdGenerated    NUMBER;
 
    /* *** DECLARE CURSORS *** */
    CURSOR lcur_ZipList (
       an_InvNoDbId inv_inv.inv_no_db_id%TYPE,
       an_InvNoId   inv_inv.inv_no_id%TYPE,
       an_ReqDbId sched_stask.sched_db_id%TYPE,
       an_ReqId   sched_stask.sched_id%TYPE
    ) IS
       SELECT DISTINCT
          sched_stask.sched_db_id,
          sched_stask.sched_id,
          ref_task_class.class_mode_cd,
          inv_inv.assmbl_inv_db_id,
          inv_inv.assmbl_inv_id
       FROM
          sched_stask req_stask,
          task_block_req_map,
          task_task req_task,
          task_task gen_task,
          sched_stask,
          evt_event,
          evt_inv,
          (
             -- Get the assembly tree for the current assembly only
             SELECT
                assmbl_inv.inv_no_db_id,
                assmbl_inv.inv_no_id,
                -- This column should always be the current assembly
                -- If inv_inv is an assembly, always use the current inv pk because the
                -- assmbly pk will be that of the parent assembly
                -- Otherwise, always use the assembly pk
                nvl2( inv_inv.orig_assmbl_db_id, inv_inv.inv_no_db_id, inv_inv.assmbl_inv_no_db_id ) AS assmbl_inv_db_id,
                nvl2( inv_inv.orig_assmbl_db_id, inv_inv.inv_no_id, inv_inv.assmbl_inv_no_id ) AS assmbl_inv_id
             FROM
                inv_inv,
                inv_inv assmbl_inv
             WHERE
                inv_inv.inv_no_db_id = an_InvNoDbId AND
                inv_inv.inv_no_id    = an_InvNoId
                AND
                (
                   (  -- Get all inventory on the current assembly, if this is an assembly, connect using inv pk
                      assmbl_inv.assmbl_inv_no_db_id = nvl2( inv_inv.orig_assmbl_db_id, inv_inv.inv_no_db_id, inv_inv.assmbl_inv_no_db_id ) AND
                      assmbl_inv.assmbl_inv_no_id    = nvl2( inv_inv.orig_assmbl_db_id, inv_inv.inv_no_id, inv_inv.assmbl_inv_no_id )
                   )
                   OR
                   (  -- Get the current inv as well in cases where the argument is the pk of an assembly
                      assmbl_inv.inv_no_db_id = inv_inv.inv_no_db_id AND
                      assmbl_inv.inv_no_id    = inv_inv.inv_no_id
                   )
                   OR
                   (  -- If this is a sub inv on an ASSY, we have to also get the main assmbl inv
                      -- We only want to do this if this is not an assembly, otherwise we will end up
                      -- getting the assembly above this one
                      inv_inv.orig_assmbl_db_id IS NULL AND
                      assmbl_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                      assmbl_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
                   )
                )
                AND
                -- Exclude inventory that is flagged as Prevent Sync or Locked
                assmbl_inv.prevent_synch_bool = 0
                AND
                assmbl_inv.locked_bool = 0
          ) inv_inv,
          task_task,
          ref_task_class
       WHERE
          -- Get the Actual Requirement
          req_stask.sched_db_id = an_ReqDbId AND
          req_stask.sched_id    = an_ReqId
          AND
          -- Get the Baseline link to all Blocks
          req_task.task_db_id = req_stask.task_db_id AND
          req_task.task_id    = req_stask.task_id
          AND
          task_block_req_map.req_task_defn_db_id = req_task.task_defn_db_id AND
          task_block_req_map.req_task_defn_id    = req_task.task_defn_id
          AND
          -- Link to all revisions of the REQs and BLOCKS
          (  (  gen_task.task_db_id = req_task.task_db_id AND
                gen_task.task_id    = req_task.task_id
             )
             OR
             (  gen_task.task_db_id = task_block_req_map.block_task_db_id AND
                gen_task.task_id    = task_block_req_map.block_task_id
             )
          )
          AND
          task_task.task_defn_db_id = gen_task.task_defn_db_id AND
          task_task.task_defn_id    = gen_task.task_defn_id
          AND
          -- Link to all Actual REQs and BLOCKS
          sched_stask.task_db_id = task_task.task_db_id AND
          sched_stask.task_id    = task_task.task_id
          AND
          -- Limit to those on the same assembly inv tree
          evt_event.event_db_id = sched_stask.sched_db_id AND
          evt_event.event_id    = sched_stask.sched_id
          AND
          evt_event.hist_bool = 0
          AND
          evt_inv.event_db_id   = evt_event.event_db_id AND
          evt_inv.event_id      = evt_event.event_id    AND
          evt_inv.inv_no_db_id  = inv_inv.inv_no_db_id  AND
          evt_inv.inv_no_id     = inv_inv.inv_no_id     AND
          evt_inv.main_inv_bool = 1
          AND
          -- Get the Task Class
          ref_task_class.task_class_db_id = task_task.task_class_db_id AND
          ref_task_class.task_class_cd    = task_task.task_class_cd;
    lrec_ZipList  lcur_ZipList%ROWTYPE;
 
 BEGIN
 
    -- Initialize the return value
    on_Return := icn_NoProc;
    ln_IdGenerated := 0;
 
    -- Add all the Zip Tasks
    FOR lrec_ZipList IN lcur_ZipList( an_InvNoDbId, an_InvNoId, an_ReqDbId, an_ReqId )
    LOOP
 
       IF( ln_IdGenerated = 0 ) THEN
          -- get the next zip id
          SELECT ZIP_QUEUE_SEQ.nextval INTO ln_ZipId FROM dual;
 
          -- Insert the new Zip ID
          INSERT INTO zip_queue ( zip_id, actv_bool )
          VALUES ( ln_ZipId, 0 );
 
          ln_IdGenerated := 1;
 
       END IF;
 
       INSERT INTO zip_task (
          zip_id,
          sched_db_id,
          sched_id,
          class_mode_cd,
          assmbl_inv_no_db_id,
          assmbl_inv_no_id
       )
       VALUES (
          ln_ZipId,
          lrec_ZipList.sched_db_id,
          lrec_ZipList.sched_id,
          lrec_ZipList.class_mode_cd,
          lrec_ZipList.assmbl_inv_db_id,
          lrec_ZipList.assmbl_inv_id
       );
    END LOOP;
 
    -- Activate the Zip ID
    IF( ln_IdGenerated = 1 ) THEN
       UPDATE zip_queue
       SET    actv_bool = 1
       WHERE  zip_id = ln_ZipId;
    END IF;
 
    on_Return    := icn_Success;
 
 EXCEPTION
    WHEN OTHERS THEN
       -- Unexpected error
       on_Return := icn_Error;
       APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@RequestZipForNewReq@@@'||SQLERRM);
       RETURN;
 END RequestZipForNewReq;
 
 /********************************************************************************
 *
 * Procedure:    RequestZipForNewBlock
 * Arguments:
 *        an_InvNoDbId             The inventory on which the task is created
 *        an_InvNoId               "  "
 *        an_BlockDbId             The Task requesting Zipping
 *        an_BlockId               "  "
 *
 * Return:
 *        on_Return        (long) - Success/Failure of requesting Zip
 *
 * Description:  This procedure is used to queue a new block for Baseline Sync zipping
 *
 * Orig.Coder:     Jonathan Clarkin
 * Recent Coder:   Jonathan Clarkin
 *
 *********************************************************************************
 *
 * Copyright 1997-2008 Mxi Technologies Ltd.  All Rights Reserved.
 * Any distribution of the Mxi source code by any other party than
 * Mxi Technologies Ltd is prohibited.
 *
 *********************************************************************************/
 PROCEDURE RequestZipForNewBlock(
       an_InvNoDbId          IN inv_inv.inv_no_db_id%TYPE,
       an_InvNoId            IN inv_inv.inv_no_id%TYPE,
       an_BlockDbId          IN sched_stask.sched_db_id%TYPE,
       an_BlockId            IN sched_stask.sched_id%TYPE,
       on_Return             OUT NUMBER
    ) IS
 
    /* *** DECLARE LOCAL VARIABLES *** */
    ln_ZipId          zip_queue.zip_id%TYPE;
    ln_IdGenerated    NUMBER;
 
     -- tables of predefined keys
     TYPE schedDbId IS TABLE OF zip_task.sched_db_id%TYPE;
     TYPE schedId IS TABLE OF zip_task.sched_id%TYPE;
     TYPE classModeCd IS TABLE OF zip_task.class_mode_cd%TYPE;
     TYPE assmblInvNoDbId IS TABLE OF zip_task.assmbl_inv_no_db_id%TYPE;
     TYPE assmblInvNoId IS TABLE OF zip_task.assmbl_inv_no_id%TYPE;
 		
     -- variables for the table keys
     lBlockList mxkeytable;
     lSchedTaskList mxkeytable;
     lInventoryList mxkeytable;
 
     -- variables for the ziplist
     lSchedDbId schedDbId;
     lSchedId schedId;
     lClassModeCd classModeCd;
     lAssmblInvNoDbId assmblInvNoDbId;
     lAssmblInvNoId assmblInvNoId;
 
 BEGIN
 
    -- Initialize the return value
    on_Return := icn_NoProc;
    ln_IdGenerated := 0;
 	 
    -- create a table to store the BLOCK objects
    lBlockList := mxkeytable();
 
    -- create a table to store the actual task objects
    lSchedTaskList := mxkeytable();
 	 
    -- create a table to store the assembly inventory tree
    lInventoryList := mxkeytable();
 
    -- create tables for the ziplist()
    lSchedDbId := schedDbId();
    lSchedId := schedId();
    lClassModeCd := classModeCd();
    lAssmblInvNoDbId := assmblInvNoDbId();
    lAssmblInvNoId := assmblInvNoId();	 
 
    -- get the list of BLOCK task definition revisions
    SELECT
       mxkey(task_db_id, task_id)
    BULK COLLECT INTO lBlockList
    FROM
    (
       -- get the single blocks (no chain)
       SELECT
          task_task.task_db_id,
          task_task.task_id
       FROM
 	 sched_stask block_stask INNER JOIN task_task
 	    ON    task_task.task_db_id = block_stask.task_db_id AND
 	          task_task.task_id    = block_stask.task_id
       WHERE
          block_stask.sched_db_id = an_BlockDbId AND
          block_stask.sched_id    = an_BlockId
          AND
          task_task.block_chain_sdesc IS NULL
       UNION
       -- get the block chains
       SELECT
          block_chain.task_db_id,
          block_chain.task_id
       FROM
 	 sched_stask block_stask INNER JOIN task_task
 	    ON    task_task.task_db_id = block_stask.task_db_id AND
 	          task_task.task_id    = block_stask.task_id
 	 INNER JOIN task_task block_chain
 	    ON    block_chain.block_chain_sdesc = task_task.block_chain_sdesc AND
 	    	  block_chain.revision_ord = task_task.revision_ord AND
 	          block_chain.assmbl_db_id = task_task.assmbl_db_id AND
 	          block_chain.assmbl_cd = task_task.assmbl_cd AND
 	          block_chain.assmbl_bom_id = task_task.assmbl_bom_id	
       WHERE
          block_stask.sched_db_id = an_BlockDbId AND
          block_stask.sched_id    = an_BlockId			 
    );
 
 	 -- Get the Baseline link to all Actual REQs and BLOCKS
    SELECT
       mxkey(sched_db_id, sched_id)
    BULK COLLECT INTO lSchedTaskList
    FROM
    (
       -- Get actual REQs
       SELECT
          sched_stask.sched_db_id,
 	       sched_stask.sched_id
       FROM
          TABLE(lBlockList) blocks INNER JOIN task_block_req_map
             ON   task_block_req_map.block_task_db_id = blocks.db_Id AND
                  task_block_req_map.block_task_id    = blocks.id
          INNER JOIN task_task req_task
             ON   req_task.task_defn_db_id = task_block_req_map.req_task_defn_db_id AND
                  req_task.task_defn_id    = task_block_req_map.req_task_defn_id
          INNER JOIN sched_stask
             ON   sched_stask.task_db_id = req_task.task_db_id AND
                  sched_stask.task_id	= req_task.task_id
       UNION
       -- Get actual BLOCKS
       SELECT
          sched_stask.sched_db_id,
 	       sched_stask.sched_id
       FROM
          TABLE(lBlockList) blocks INNER JOIN sched_stask
             ON   sched_stask.task_db_id = blocks.db_id AND
                  sched_stask.task_id 	= blocks.id	
    );
 	 
     -- Lookup of the Assembly inv tree based on having the root assembly key
     -- Include the Root Assembly and any inv that belong to that root Assembly	 
     SELECT
        mxkey(inv_no_db_id, inv_no_id)
        BULK COLLECT INTO lInventoryList
     FROM
     (
        SELECT
           inv_inv.inv_no_db_id,
           inv_inv.inv_no_id
        FROM inv_inv
        WHERE
           -- Get the base inventory
           inv_inv.inv_no_db_id = an_InvNoDbId AND
           inv_inv.inv_no_id    = an_InvNoId
           AND
           -- Limit to ASSY or ACFT cases
           ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) IN ((0, 'ACFT'), (0, 'ASSY'))
        UNION ALL
        SELECT
           assmbl_inv.inv_no_db_id,
           assmbl_inv.inv_no_id
        FROM
           inv_inv assmbl_inv INNER JOIN inv_inv
              ON   assmbl_inv.assmbl_inv_no_db_id = inv_inv.inv_no_db_id AND
                   assmbl_inv.assmbl_inv_no_id    = inv_inv.inv_no_id
        WHERE
           -- Get the base inventory
           inv_inv.inv_no_db_id = an_InvNoDbId AND
           inv_inv.inv_no_id    = an_InvNoId
           AND
           -- Limit to ASSY or ACFT cases
           ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) IN ((0, 'ACFT'), (0, 'ASSY'))
        UNION ALL
        -- Lookup of the Assembly inv tree based on not having the root assembly key
        -- Include all those sharing the Root Assembly and the Root Assembly inv as well
        SELECT
           assmbl_inv.inv_no_db_id,
           assmbl_inv.inv_no_id
        FROM
           inv_inv assmbl_inv INNER JOIN inv_inv 
              ON   assmbl_inv.assmbl_inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                   assmbl_inv.assmbl_inv_no_id    = inv_inv.assmbl_inv_no_id
        WHERE
           -- Get the base inventory
           inv_inv.inv_no_db_id = an_InvNoDbId AND
           inv_inv.inv_no_id    = an_InvNoId
           AND
           -- Limit to sub node cases
           ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) NOT IN ((0, 'ACFT'), (0, 'ASSY'))
        UNION ALL
        SELECT
           inv_inv.assmbl_inv_no_db_id,
           inv_inv.assmbl_inv_no_id
        FROM
           inv_inv assmbl_inv INNER JOIN inv_inv 
              ON   assmbl_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
                   assmbl_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
        WHERE
           -- Get the base inventory
           inv_inv.inv_no_db_id = an_InvNoDbId AND
           inv_inv.inv_no_id    = an_InvNoId
           AND
           -- Limit to sub node cases
           ( inv_inv.inv_class_db_id, inv_inv.inv_class_cd) NOT IN ((0, 'ACFT'), (0, 'ASSY'))
    );
 			      
 
    -- get all the actual tasks
    SELECT DISTINCT
       sched_stask.sched_db_id,
       sched_stask.sched_id,
       ref_task_class.class_mode_cd,
       CASE
          WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
          WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
          ELSE inv_inv.assmbl_inv_no_db_id
       END AS assmbl_inv_db_id,
       CASE
          WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
          WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
          ELSE inv_inv.assmbl_inv_no_id
       END AS assmbl_inv_id
    BULK COLLECT INTO lSchedDbId, lSchedId, lClassModeCd, lAssmblInvNoDbId, lAssmblInvNoId
    FROM
       TABLE(lSchedTaskList) tasks INNER JOIN sched_stask
          ON tasks.db_id = sched_stask.sched_db_id AND tasks.id = sched_stask.sched_id
       INNER JOIN ref_task_class
          ON sched_stask.task_class_db_id = ref_task_class.task_class_db_id AND sched_stask.task_class_cd = ref_task_class.task_class_cd
       INNER JOIN  evt_event
          ON sched_stask.sched_db_id = evt_event.event_db_id AND sched_stask.sched_id = evt_event.event_id
       INNER JOIN  evt_inv
          ON evt_event.event_db_id = evt_inv.event_db_id AND evt_event.event_id = evt_inv.event_id
       INNER JOIN inv_inv
          ON evt_inv.inv_no_db_id = inv_inv.inv_no_db_id AND evt_inv.inv_no_id = inv_inv.inv_no_id
       INNER JOIN  TABLE(lInventoryList) inventory
          ON inv_inv.inv_no_db_id = inventory.db_id AND inv_inv.inv_no_id = inventory.id
    WHERE
       evt_event.hist_bool = 0 AND
       evt_inv.main_inv_bool = 1
       AND
       inv_inv.prevent_synch_bool = 0 AND
       inv_inv.locked_bool = 0;
 
     IF( ln_IdGenerated = 0 ) THEN
        -- get the next zip id
        SELECT ZIP_QUEUE_SEQ.nextval INTO ln_ZipId FROM dual;
 
        -- Insert the new Zip ID
        INSERT INTO zip_queue ( zip_id, actv_bool )
        VALUES ( ln_ZipId, 0 );
 
        ln_IdGenerated := 1;
 
     END IF;
 			
    -- insert the actual tasks
    FORALL i in lSchedDbId.first .. lSchedDbId.last				 
       INSERT INTO zip_task(zip_id, sched_db_id, sched_id, class_mode_cd, assmbl_inv_no_db_id, assmbl_inv_no_id)
       VALUES (   ln_ZipId,
                lSchedDbId(i),
                lSchedId(i),
                lClassModeCd(i),
                lAssmblInvNoDbId(i),
                lAssmblInvNoId(i)
            );
 
    -- Activate the Zip ID
    IF( ln_IdGenerated = 1 ) THEN
       UPDATE zip_queue
       SET    actv_bool = 1
       WHERE  zip_id = ln_ZipId;
    END IF;
 
    on_Return    := icn_Success;
 
 EXCEPTION
    WHEN OTHERS THEN
       -- Unexpected error
       on_Return := icn_Error;
       APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','sched_stask_pkg@@@RequestZipForNewBlock@@@'||SQLERRM);
       RETURN;
 END RequestZipForNewBlock;
 
 
 /*
    Procedure:     removeDuplicateQueues
 
    Arguments:     aZipId      NUMBER   The zipqueue that is used to search for pre-existing subsets
                   aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred
 
    Description:   This procedure looks and removes previous Queues that are proper subsets of the passed in Queue
    Author:        Jonathan Clarkin
    Created Date:  ANovember 17th, 2009
 */
 PROCEDURE removeDuplicateQueues(
    aZipId      IN    NUMBER,
    aResult     OUT   NUMBER
 )
 AS
    CURSOR lcur_AllZipId( an_ZipId NUMBER )
    IS
       SELECT zip_id
       FROM zip_queue
       WHERE
          zip_id <> an_ZipId
          AND
          NOT EXISTS (
             SELECT sched_db_id,sched_id
             FROM zip_task
             WHERE zip_task.zip_id = zip_queue.zip_id
            MINUS
             SELECT sched_db_id,sched_id
             FROM zip_task
             WHERE zip_task.zip_id = an_ZipId
          );
    lrec_AllZipId  lcur_AllZipId%ROWTYPE;
 
 
 BEGIN
    aResult := -1;
 
    -- Delete any existing Queues that are proper-subsets of this Queue
    FOR lrec_AllZipId IN lcur_AllZipId(aZipId) LOOP
       DELETE
       FROM zip_task
       WHERE zip_id = lrec_AllZipId.zip_id;
       DELETE
       FROM zip_queue
       WHERE zip_id = lrec_AllZipId.zip_id;
    END LOOP;
 
    -- set the result to 1 indicating successful operation
    aResult := 1;
 
 -- if any exception occurs, return -1
 EXCEPTION
       WHEN OTHERS THEN
          aResult := -1;
          APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', SQLERRM);
 END removeDuplicateQueues;
 
 
 /*
    Procedure:     UpdateActivateReq
 
    Arguments:     aZipId      NUMBER   The zipqueue that the tasks are to be associated with
                   aReqDbId   NUMBER   the block db id
                   aReqId     NUMBER   the block id
                   aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred
 
    Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision.
                   This is moved from UpdateActivateReq.qrx - Adds all Reqs in Maintenix to a Zip List for Baseline Sync
    Author:        Bo Wang
    Created Date:  Dec. 11, 2009
 */
 PROCEDURE UpdateActivateReq(
    aZipId      IN    NUMBER,
    aReqDbId   IN    task_task.task_db_id%TYPE,
    aReqId     IN    task_task.task_id%TYPE,
    aResult     OUT   NUMBER
 ) IS
 
 BEGIN
    aResult := -1;
    INSERT INTO ZIP_TASK (
          zip_id,
          sched_db_id,
          sched_id,
          class_mode_cd,
          assmbl_inv_no_db_id,
          assmbl_inv_no_id
       )
       (
          SELECT DISTINCT
             aZipId,
             sched_stask.sched_db_id,
             sched_stask.sched_id,
             ref_task_class.class_mode_cd,
             CASE
                WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_db_id
                WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_db_id
                ELSE inv_inv.assmbl_inv_no_db_id
             END AS assmbl_inv_db_id,
             CASE
                WHEN inv_inv.inv_class_cd = 'ACFT' THEN inv_inv.inv_no_id
                WHEN inv_inv.inv_class_cd = 'ASSY' THEN inv_inv.inv_no_id
                ELSE inv_inv.assmbl_inv_no_id
             END AS assmbl_inv_id
          FROM
             task_task req_task,
             task_block_req_map,
             task_task gen_task,
             sched_stask,
             evt_event,
             evt_inv,
             inv_inv,
             inv_inv assmbl_inv,
             task_task,
             ref_task_class
          WHERE
             -- Get the Baselined Requirement
             req_task.task_db_id = aReqDbId AND
             req_task.task_id    = aReqId
             AND
             -- Get all the parent Block Definitions
             task_block_req_map.req_task_defn_db_id = req_task.task_defn_db_id AND
             task_block_req_map.req_task_defn_id    = req_task.task_defn_id
             AND
             -- Link to all Actual REQs and BLOCKS
             (  (  gen_task.task_db_id = req_task.task_db_id AND
                   gen_task.task_id    = req_task.task_id
                )
                OR
                (  gen_task.task_db_id = task_block_req_map.block_task_db_id AND
                   gen_task.task_id    = task_block_req_map.block_task_id
                )
             )
             AND
             task_task.task_defn_db_id = gen_task.task_defn_db_id AND
             task_task.task_defn_id    = gen_task.task_defn_id
             AND
             sched_stask.task_db_id = task_task.task_db_id AND
             sched_stask.task_id    = task_task.task_id
             AND
             sched_stask.rstat_cd = 0
             AND
             -- Filter out Historic tasks
             evt_event.event_db_id = sched_stask.sched_db_id AND
             evt_event.event_id    = sched_stask.sched_id
             AND
             evt_event.hist_bool = 0
             AND
             evt_event.rstat_cd = 0
             AND
             -- Get the inventory and assembly inventory for the tasks
             evt_inv.event_db_id   = evt_event.event_db_id AND
             evt_inv.event_id      = evt_event.event_id    AND
             evt_inv.inv_no_db_id  = inv_inv.inv_no_db_id  AND
             evt_inv.inv_no_id     = inv_inv.inv_no_id     AND
             evt_inv.main_inv_bool = 1
             AND
             assmbl_inv.inv_no_db_id (+)= inv_inv.assmbl_inv_no_db_id AND
             assmbl_inv.inv_no_id    (+)= inv_inv.assmbl_inv_no_id
             AND
             assmbl_inv.rstat_cd(+) = 0
             AND
             -- Exclude Inventory that is flagged as Prevent Sync or Locked
             inv_inv.prevent_synch_bool = 0
             AND
             inv_inv.locked_bool = 0
             AND
             inv_inv.rstat_cd = 0
             AND
             -- Get the Task Class
             ref_task_class.task_class_db_id = task_task.task_class_db_id AND
             ref_task_class.task_class_cd    = task_task.task_class_cd
             AND
             ref_task_class.rstat_cd = 0
          );
 
    -- set the result to 1 indicating successful operation
    aResult := 1;
 
 -- if any exception occurs, return -1
 EXCEPTION
       WHEN OTHERS THEN
          aResult := -1;
          APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', SQLERRM);
 END;
 
 
 END BaselineSyncPkg;
 /