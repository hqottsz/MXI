--liquibase formatted sql


--changeSet warranty_eval_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE WARRANTY_EVAL_PKG
IS

/* constant declarations (error codes) */
icn_OracleError    CONSTANT NUMBER   := -100;
icn_Success        CONSTANT NUMBER   := 1;


/********************************************************************************
*
*  Procedure:    EvaluateWarranty
*  Arguments:    an_SchedDbId (long) - Task Key(DbId)
*                an_SchedId (long)   - Task Key(Id)
*  Description:  This procedure takes in a task or workpackage key and evaluates it
*                for warranties.
*
*  Orig. Coder:  sbicharr
*  Recent Coder:
*  Recent Date:  2008-07-23
*
*********************************************************************************
*
*  Copyright 2008-07-23 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE EvaluateWarranty
(
   an_SchedDbId IN sched_stask.sched_db_id%TYPE,
   an_SchedId   IN sched_stask.sched_db_id%TYPE,
   on_Return  OUT NUMBER
);



/*********************************************************************************
*  Procedure:    isWarrantyExpired
*  Arguments:    an_WtyInitDbId  - Warranty identifier DbId.
*                an_WtyInitId    - Warranty identifier Id.
*  Return:       Bool (1 if the warranty is expired and 0 other wise)
*  Description:  Checks if the task is complete. This Function
*                takes in the task key and returns a number.
*
*  Orig. Coder:  Sidney Bicharr
*  Recent Coder:
*  Recent Date:  August 03, 2007
*
*********************************************************************************
*
*  Copyright ? 1998 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION isWarrantyExpired
(
   an_WtyInitDbId IN warranty_init.warranty_init_db_id%TYPE,
   an_WtyInitId   IN warranty_init.warranty_init_id%TYPE
)
RETURN NUMBER;


END WARRANTY_EVAL_PKG;
/