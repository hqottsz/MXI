--liquibase formatted sql


--changeSet BaselineSyncPkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*
   Package:       BaselineSyncPkg
   Description:   This package is used to perform stored procedures related to baseline synchronizations

   Author:        Chris Daley
   Created Date:  Apr. 7th, 2009
*/
CREATE OR REPLACE PACKAGE "BASELINESYNCPKG" IS

-- Basic error handling codes
 	icn_Success CONSTANT NUMBER := 1;       -- Success
 	icn_NoProc  CONSTANT NUMBER := 0;       -- No processing done
 	icn_Error   CONSTANT NUMBER := -1;      -- Error
	
	
/*
	Procedure:	createZipQueueWorkItem
	Arguments:	aZipId	NUMBER	the zip id to create the work item for

	Description:	This procedure creates a work item for the zip queue given.  This needs to be done since requesting zip can occur at the database layer.
	Author:	Chris Daley
	Created Date:	March 03, 2009
*/
PROCEDURE createZipQueueWorkItem(
	aZipId	IN	NUMBER,
  	aInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
  	aInvNoId   IN inv_inv.inv_no_id%TYPE
);
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
);

/*
   Procedure:     updateBlockZiplist

   Arguments:     aZipId      NUMBER   The zipqueue that the tasks are to be associated with
                  aTaskDbId   NUMBER   the block definition revision that is being updated
                  aTaskId     NUMBER   --
                  aHInvNoDbId NUMBER   the highest inventory in the tree for the inventory that is being synchronized
                  aHInvNoId   NUMBER   --
                  aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

   Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision.  This will find blocks/reqs across all revisions.
   Author:        Chris Daley
   Created Date:  June. 18th, 2010
*/
PROCEDURE updateBlockZiplist(
   aZipId      IN    NUMBER,
   aTaskDbId   IN    task_task.task_db_id%TYPE,
   aTaskId     IN    task_task.task_id%TYPE,
   aHInvNoDbId IN    inv_inv.inv_no_db_id%TYPE,
   aHInvNoId   IN    inv_inv.inv_no_id%TYPE,
   aResult     OUT   NUMBER
);

/*
   Procedure:     updateZipFullBlockOnTree

   Arguments:     aBlockDbId  NUMBER   the block task db id
                  aBlockId       NUMBER   the block task id
                  aInvNoDbId     NUMBER   the inventory db id
                  aInvNoId NUMBER    the inventory id
                  aZipId   NUMBER   The zipqueue that the tasks are to be associated with
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
);

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
   );

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
   );

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
);

/*
   Procedure:     UpdateActivateReq

   Arguments:     aZipId      NUMBER   The zipqueue that the tasks are to be associated with
                  aReqDbId   NUMBER   the block db id
                  aReqId     NUMBER   the block id
                  aResult     NUMBER   The result of the operation.  1 indicates success, -1 indicates an exception occurred

   Description:   This procedure inserts the actuals that needs to be Zipped for the provided task definition revision.
                  This is moved from UpdateActivateReq.qrx
   Author:        Bo Wang
   Created Date:  Dec. 11, 2009
*/
PROCEDURE UpdateActivateReq(
   aZipId      IN    NUMBER,
   aReqDbId   IN    task_task.task_db_id%TYPE,
   aReqId     IN    task_task.task_id%TYPE,
   aResult     OUT   NUMBER
);

END BaselineSyncPkg;
 
/