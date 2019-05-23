--liquibase formatted sql


--changeSet MX-17415:1 stripComments:false
INSERT INTO UTL_ASYNC_ACTION_TYPE ( ASYNC_ACTION_TYPE_CD, USER_TYPE_CD, DESC_SDESC, DESC_LDESC, REF_NAME, METHOD_NAME, UTL_ID )
   SELECT 'EVAL_INV_COMPLETENESS', 'EVAL_INV_COMPLETENESS', 'Re-evaluate inventory completeness', '', 'com.mxi.mx.core.ejb.CoreAsyncAction', 'evaluateInventoryCompleteness', 0
   FROM dual WHERE NOT EXISTS ( SELECT 1 FROM UTL_ASYNC_ACTION_TYPE WHERE ASYNC_ACTION_TYPE_CD = 'EVAL_INV_COMPLETENESS' ); 

--changeSet MX-17415:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY INV_COMPLETE_PKG IS
/*-------------------- Package Constants --------------------------------------*/
cn_True  inv_inv.complete_bool%TYPE := 1;
cn_False inv_inv.complete_bool%TYPE := 0;
/*-------------------- Private procedures --------------------------------------*/

PROCEDURE RecurseCheckParent( al_InventoryId IN il_InvNoId%TYPE,
                       al_InventoryDbId IN il_InvNoDbId%TYPE,
                       an_Success OUT NUMBER );
PROCEDURE RecurseMandatoryBOM( al_InventoryId IN il_InvNoId%TYPE,
                        al_InventoryDbId IN il_InvNoDbId%TYPE,
                        al_AssmblDbId IN il_AssmblDbId%TYPE,
                        as_AssmblCd IN is_AssmblCd%TYPE,
                        al_AssmblBomId IN il_AssmblBomId%TYPE,
                        al_AssmblPosId IN il_AssmblPosId%TYPE,
                 as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
                        an_Success OUT NUMBER );
PROCEDURE GetParent( al_InventoryId IN il_InvNoId%TYPE,
                al_InventoryDbId IN il_InvNoDbId%TYPE,
                al_ParentId OUT il_InvNoId%TYPE,
                al_ParentDbId OUT il_InvNoDbId%TYPE,
                an_Success OUT NUMBER );
PROCEDURE GetAssemblyAndPosition( al_InventoryId IN il_InvNoId%TYPE,
                          al_InventoryDbId IN il_InvNoDbId%TYPE,
                           al_AssmblDbId OUT il_AssmblDbId%TYPE,
                           as_AssmblCd OUT is_AssmblCd%TYPE,
                           al_AssmblBomId OUT il_AssmblBomId%TYPE,
                           al_AssmblPosId OUT il_AssmblPosId%TYPE,
                             an_Success OUT NUMBER );
PROCEDURE CheckParentNode(
                     al_ParentId IN il_InvNoId%TYPE,
                     al_ParentDbId IN il_InvNoDbId%TYPE,
                     as_State OUT VARCHAR2,
                     an_Success OUT NUMBER );
PROCEDURE RecurseSetParentIncomplete( al_InventoryId IN il_InvNoId%TYPE,
                             al_InventoryDbId IN il_InvNoDbId%TYPE,
                             an_Success OUT NUMBER );


PROCEDURE GetAssemblyApplicabilityCode( aInventoryDbId IN inv_inv.inv_no_db_id%TYPE,
                                        aInventoryId   IN inv_inv.inv_no_id%TYPE,
                                        aApplicabilityCd OUT inv_inv.appl_eff_cd%TYPE );

/********************************************************************************
*
*  Procedure:    EvaluateAssemblyCompleteness
*  Arguments:    al_InventoryId     - inventory item identifier
*                 al_InventoryDbId    - inventory item database identifier
*  Return:       (number) - > 0 to indicate success
*  Description:  The 'batch' procedure for the evaluation of the completeness of
*                 each item in an assembly.
*
*  Coder:        Laura Cline
*  Date:         February 25, 1998
*  Recent Coder: Y.Sotozaki
*  Recent Date:  September 11, 2006
*
*********************************************************************************
*
*    Copyright © 2006 Mxi Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE EvaluateAssemblyCompleteness( al_InventoryId IN il_InvNoId%TYPE,
                                        al_InventoryDbId IN il_InvNoDbId%TYPE,
                                        an_Success OUT NUMBER )
IS
    -- Declare local variables
    ll_AssmblDbId    il_AssmblDbId%TYPE;
    ls_AssmblCd        is_AssmblCd%TYPE;
    ll_AssmblBomId    il_AssmblBomId%TYPE;
    ll_AssmblPosId    il_AssmblPosId%TYPE;
    ls_ApplicabilityCd inv_inv.appl_eff_cd%TYPE;

    -- Declare exceptions
    xc_InventoryIdNull        EXCEPTION;
    xc_InventoryDbIdNull      EXCEPTION;
    xc_SetCompleteFlagFail    EXCEPTION;
    xc_GetAssyAndPosFail      EXCEPTION;
    xc_RecurseMandatoryFail   EXCEPTION;
    xc_RecurseCheckParent     EXCEPTION;
BEGIN

    -- Initialize the return value
    an_Success := icn_NoProc;

    -- Test for null identifiers, and raise an exception if null
    -- parameters are found.
    IF al_InventoryId IS NULL
    THEN
        RAISE xc_InventoryIdNull;
    END IF;
    IF al_InventoryDbId IS NULL
    THEN
        RAISE xc_InventoryDbIdNull;
    END IF;

    -- Set the complete flag for the inventory item to TRUE.
    SetCompleteFlag( al_InventoryId,
                     al_InventoryDbId,
                     cn_True,
                     an_Success );
    IF an_Success <= 0 THEN
        RAISE xc_SetCompleteFlagFail;
    END IF;

    -- Get the assembly information for the inventory item.
    GetAssemblyAndPosition( al_InventoryId,
                            al_InventoryDbId,
                            ll_AssmblDbId,
                            ls_AssmblCd,
                            ll_AssmblBomId,
                            ll_AssmblPosId,
                            an_Success );
    IF an_Success <= 0 THEN
        RAISE xc_GetAssyAndPosFail;
    END IF;

    -- get the applicability code
    select
        inv_inv.appl_eff_cd
    into
        ls_ApplicabilityCd
    from
        inv_inv
    where
         inv_inv.inv_no_db_id =  al_InventoryDbId AND
         inv_inv.inv_no_id    =  al_InventoryId AND
         inv_inv.rstat_cd = 0;

    -- Recurse through the mandatory bill of materials for the
    -- inventory item, updating the complete flags.
    RecurseMandatoryBOM( al_InventoryId,
                         al_InventoryDbId,
                         ll_AssmblDbId,
                         ls_AssmblCd,
                         ll_AssmblBomId,
                         ll_AssmblPosId,
                         ls_ApplicabilityCd,
                         an_Success );
    IF an_Success <= 0 THEN
        RAISE xc_RecurseMandatoryFail;
    END IF;

     -- Check each parent in the hierarchy above the installed item.
     RecurseCheckParent( al_InventoryId,
                         al_InventoryDbId,
                         an_Success );
     -- Test the success flag, and raise an exception if an error occurred.
     IF an_Success <= 0
     THEN
         RAISE xc_RecurseCheckParent;
     END IF;

EXCEPTION
    WHEN xc_InventoryIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -1;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory identifier' );
        RETURN;
    WHEN xc_InventoryDbIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -2;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory database identifier' );
        RETURN;
    WHEN xc_SetCompleteFlagFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -3;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.SetCompleteFlag');
        RETURN;
    WHEN xc_GetAssyAndPosFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -4;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.GetAssemblyAndPosition');
        RETURN;
    WHEN xc_RecurseMandatoryFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -5;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.RecurseMandatoryBOM');
        RETURN;
    WHEN xc_RecurseCheckParent
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -6;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.RecurseCheckParent');
        RETURN;
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', '');
        RETURN;
END EvaluateAssemblyCompleteness;
/********************************************************************************
*
*  Procedure:    UpdateCompleteFlagOnRemove
*  Arguments:    al_ParentId    - parent item identifier
*                 al_ParentDbId    - parent item database identifier
*  Return:       (number) - > 0 to indicate success
*  Description:  This procedure sets the complete flag of the parent of a removed
*                 item to false, and recurses up through the hierarchy setting the
*                 complete flag to false until a false node is reached.
*
*  Original Coder: Laura Cline
*  Recent Coder:   cjb
*  Recent Date:    February 25, 2005
*
*********************************************************************************
*
*    Revision History
*
*    Version    Description
*    1.0        Initial Version
*
*********************************************************************************
*
*    Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateCompleteFlagOnRemove( al_ParentId IN il_InvNoId%TYPE,
                                      al_ParentDbId IN il_InvNoDbId%TYPE,
                                      an_Success OUT NUMBER )
IS
    -- Declare exceptions.
    xc_ParentIdNull            EXCEPTION;
    xc_ParentDbIdNull        EXCEPTION;
    xc_SetCompleteFail        EXCEPTION;
    xc_RecurseParentFail    EXCEPTION;
BEGIN

    -- Initialize the return variable.
    an_Success := icn_NoProc;

    -- Test for null identifiers, and raise an exception if null
    -- parameters are found.
    IF al_ParentId IS NULL
    THEN
        RAISE xc_ParentIdNull;
    END IF;
    IF al_ParentDbId IS NULL
    THEN
        RAISE xc_ParentDbIdNull;
    END IF;

    -- Set the parent complete flag to FALSE.
    SetCompleteFlag( al_ParentId,
                      al_ParentDbId,
                      cn_False,
                      an_Success );
    -- Test the success flag, and raise an exception if an error occurred.
    IF an_Success <= 0
    THEN
        RAISE xc_SetCompleteFail;
    END IF;

    -- Recurse through the complete parents of the removed item, and
    -- set each of them to false.
    RecurseSetParentIncomplete( al_ParentId,
                                al_ParentDbId,
                                an_Success );
    -- Test the success flag, and raise an exception if an error occurred.
    IF an_Success <= 0
    THEN
        RAISE xc_RecurseParentFail;
    END IF;

    -- Return success
    an_Success := icn_Success;
EXCEPTION
    WHEN xc_ParentIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -1;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'parent identifier' );
        RETURN;
    WHEN xc_ParentDbIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -2;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'parent database identifier' );
        RETURN;
    WHEN xc_SetCompleteFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -3;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.SetCompleteFlag');
        RETURN;
    WHEN xc_RecurseParentFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -4;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.RecurseSetParentIncomplete');
        RETURN;
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', '');
        RETURN;
END UpdateCompleteFlagOnRemove;
/********************************************************************************
*
*  Procedure:    UpdateCompleteFlagOnInstall
*  Arguments:    al_InstallId   - installed item identifier
*                 al_InstallDbId    - installed item database identifier
*  Return:       (number) - > 0 to indicate success
*  Description:  This procedure tests each item in the hierarchy above the
*                 installed item to determine if they are complete, sets the flag
*                 to true if they are, and stops testing if they are not.
*
*  Original Coder: Laura Cline
*  Recent Coder:   cjb
*  Recent Date:    February 25, 2005
*
*********************************************************************************
*
*    Revision History
*
*    Version    Description
*    1.0        Initial Version
*
*********************************************************************************
*
*    Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateCompleteFlagOnInstall( al_InstallId IN il_InvNoId%TYPE,
                                       al_InstallDbId IN il_InvNoDbId%TYPE,
                                       an_Success OUT NUMBER )
IS
    -- Declare local variables.
    ln_Complete     in_CompleteBool%TYPE;
    li_SQLCode        INTEGER;
    ls_SQLErr        VARCHAR2(2000);
    ls_ErrMessage    VARCHAR2(2000);
    -- Declare exceptions
    xc_InstallIdNull        EXCEPTION;
    xc_InstallDbIdNull        EXCEPTION;
    xc_RecurseCheckParent    EXCEPTION;
    xc_SelectCompleteFail    EXCEPTION;
BEGIN

    -- Initialize the return variable.
    an_Success := icn_NoProc;

    -- Test for null identifiers, and raise an exception if null
    -- parameters are found.
    IF al_InstallId IS NULL
    THEN
        RAISE xc_InstallIdNull;
    END IF;

    IF al_InstallDbId IS NULL
    THEN
        RAISE xc_InstallDbIdNull;
    END IF;

    -- Select the installed item's complete flag.
    BEGIN
        SELECT complete_bool
          INTO ln_Complete
          FROM inv_inv
         WHERE inv_no_id     = al_InstallId
           AND inv_no_db_id = al_InstallDbId
           AND rstat_cd	= 0;
    EXCEPTION
        WHEN OTHERS
        THEN
            -- Get the SQL error code and message.
            li_SQLCode := SQLCODE;
            ls_SQLErr  := SQLERRM( li_SQLCode );
            RAISE xc_SelectCompleteFail;
    END;

    -- Test the installed item's complete flag.
    IF ln_Complete = 1 THEN
        -- Check each parent in the hierarchy above the installed item.
        RecurseCheckParent( al_InstallId,
                            al_InstallDbId,
                            an_Success );
        -- Test the success flag, and raise an exception if an error occurred.
        IF an_Success <= 0
        THEN
            RAISE xc_RecurseCheckParent;
        END IF;
    END IF;

    -- Return success
    an_Success := icn_Success;

EXCEPTION
    WHEN xc_InstallIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -1;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'installed item identifier' );
        RETURN;
    WHEN xc_InstallDbIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -2;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'installed item database identifier' );
        RETURN;
    WHEN xc_RecurseCheckParent
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -3;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.RecurseCheckParent');
        RETURN;
    WHEN xc_SelectCompleteFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -4;
        ls_ErrMessage := 'INV_COMPLETE_PKG.UpdateCompleteFlagOnInstall for inventory item: [' ||
                        to_char(al_InstallDbId) || ']' || to_char(al_InstallId) ||
                        '(SQLCode: ' || to_char( li_SQLCode ) || '  Error: ' || ls_SQLErr || ')';
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', ls_ErrMessage);
        RETURN;
    WHEN OTHERS
    THEN
        -- Get the SQL error code and message.
        li_SQLCode := SQLCODE;
        ls_SQLErr  := SQLERRM( li_SQLCode );
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', 'SQLCode: ' || to_char( li_SQLCode ) || 'Error: ' || ls_SQLErr );
        RETURN;
END UpdateCompleteFlagOnInstall;
/********************************************************************************
*
*  Procedure:    RecurseCheckParent
*  Arguments:    al_InventoryId      - inventory item identifier
*                 al_InventoryDbId    - inventory item database identifier
*  Return:       (number) - > 0 to indicate success
*  Description:  This procedure recurses through the parent hierarchy for the
*                 input inventory item, and tests each parent for completeness.
*                 If the parent is complete, the flag is set to TRUE and the
*                 recursion continues.  If the parent is not complete, the
*                 recursion halts.
*
*  Original Coder: Laura Cline
*  Recent Coder:   cjb
*  Recent Date:    February 25, 2005
*
*********************************************************************************
*
*    Revision History
*
*    Version    Description
*    1.0        Initial Version
*
*********************************************************************************
*
*    Copyright © 1998-2003 MxI Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE RecurseCheckParent( al_InventoryId IN il_InvNoId%TYPE,
                              al_InventoryDbId IN il_InvNoDbId%TYPE,
                              an_Success OUT NUMBER )
IS
    -- Declare local variables
    ll_ParentId        il_InvNoId%TYPE;
    ll_ParentDbId    il_InvNoDbId%TYPE;
    ls_State        VARCHAR2(10);
    -- Declare exceptions.
    xc_InventoryIdNull            EXCEPTION;
    xc_InventoryDbIdNull        EXCEPTION;
    xc_GetParentFail            EXCEPTION;
    xc_CheckParentNodeFail        EXCEPTION;
    xc_SetCompleteFlagFail        EXCEPTION;
    xc_RecurseCheckParentFail    EXCEPTION;
BEGIN

    -- Initialize the return variable.
    an_Success := icn_NoProc;

    -- Test for null identifiers, and raise an exception if null
    -- parameters are found.
    IF al_InventoryId IS NULL
    THEN
        RAISE xc_InventoryIdNull;
    END IF;
    IF al_InventoryDbId IS NULL
    THEN
        RAISE xc_InventoryDbIdNull;
    END IF;
    -- Determine the parent of the inventory item.
    GetParent( al_InventoryId,
               al_InventoryDbId,
               ll_ParentId,
               ll_ParentDbId,
               an_Success );
    -- Test the success flag, and raise an exception if an error occurred.
    IF an_Success <= 0
    THEN
        RAISE xc_GetParentFail;
    END IF;
    -- Test that the parent is not null.
    IF ll_ParentId IS NOT NULL THEN
        -- Test to see if the parent has all the required children,
        -- and if the children are complete.
        CheckParentNode( ll_ParentId,
                         ll_ParentDbId,
                         ls_State,
                         an_Success );
        -- Test the success flag, and raise an exception if an error occured.
        IF an_Success <= 0
        THEN
            RAISE xc_CheckParentNodeFail;
        END IF;
        -- Test the state of the parent node.  If the node is complete,
        -- then update the complete flag and repeat the process for the
        -- next parent.
        IF ls_State = 'COMPLETE' THEN
            -- Update the parent item's complete flag.
            SetCompleteFlag( ll_ParentId,
                             ll_ParentDbId,
                             cn_True,
                             an_Success );
            -- Test the success flag, and raise an exception if an error occured.
            IF an_Success <= 0
            THEN
                RAISE xc_SetCompleteFlagFail;
            END IF;
            -- Repeat the check for the parent's parent.
            RecurseCheckParent( ll_ParentId,
                                ll_ParentDbId,
                                an_Success );
            -- Test the success flag, and raise an exception if an error occurred.
            IF an_Success <= 0
            THEN
                RAISE xc_RecurseCheckParentFail;
            END IF;
        END IF;
    END IF;

    -- Return success
    an_Success := icn_Success;

EXCEPTION
    WHEN xc_InventoryIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -1;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory identifier' );
        DBMS_OUTPUT.PUT_LINE('Null inventory id');
        RETURN;
    WHEN xc_InventoryDbIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -2;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory database identifier' );
        DBMS_OUTPUT.PUT_LINE('Null inventory database id');
        RETURN;
    WHEN xc_GetParentFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -3;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.GetParent');
        DBMS_OUTPUT.PUT_LINE('GetParent Failed');
        RETURN;
    WHEN xc_SetCompleteFlagFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -4;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.SetCompleteFlag');
        DBMS_OUTPUT.PUT_LINE('SetCompleteFlag Failed');
        RETURN;
    WHEN xc_RecurseCheckParentFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -5;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.RecurseCheckParent');
        DBMS_OUTPUT.PUT_LINE('RecurseCheckParent Failed');
        RETURN;
    WHEN xc_CheckParentNodeFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -6;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.CheckParentNode');
        DBMS_OUTPUT.PUT_LINE('CheckParentNode Failed');
        RETURN;
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', '');
        RETURN;
END RecurseCheckParent;
/********************************************************************************
*
*  Procedure:    RecurseMandatoryBOM
*  Arguments:    al_InventoryId       - inventory item identifier
*                 al_InventoryDbId     - inventory item database identifier
*                 al_AssmblDbId         - assembly database identifier
*                 as_AssmblCd        - assembly code
*                 al_AssmblBomId        - assembly bill of materials identifier
*                 al_AssmblPosId        - assembly position identifier
*  Return:       (integer) - > 0 to indicate success
*  Description:  This procedure loops through the set of mandatory assembly bom
*                 items and positions, and tests for a match in the inventory
*                 hierarchy for the input inventory item.
*
*  Original Coder: Laura Cline
*  Recent Coder:   cjb
*  Recent Date:    February 24, 2005
*
*********************************************************************************
*
*    Revision History
*
*    Version    Description
*    1.0        Initial Version
*
*********************************************************************************
*
*    Copyright © 1998-2003 MxI Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE RecurseMandatoryBOM( al_InventoryId IN il_InvNoId%TYPE,
                               al_InventoryDbId IN il_InvNoDbId%TYPE,
                               al_AssmblDbId IN il_AssmblDbId%TYPE,
                               as_AssmblCd IN is_AssmblCd%TYPE,
                               al_AssmblBomId IN il_AssmblBomId%TYPE,
                               al_AssmblPosId IN il_AssmblPosId%TYPE,
                               as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
                               an_Success OUT NUMBER )
IS
    -- Declare the mandatory BOM cursor.
    -- we also only want to consider bom parts that are applicable to the
    -- inventory we are checking for completeness
    CURSOR bom_cur IS
        SELECT POS.assmbl_db_id,
               POS.assmbl_cd,
               POS.assmbl_bom_id,
               POS.assmbl_pos_id
          FROM eqp_assmbl_bom BOM,
             eqp_assmbl_pos POS
        WHERE POS.nh_assmbl_db_id  = al_AssmblDbId  AND
             POS.nh_assmbl_cd     = as_AssmblCd    AND
             POS.nh_assmbl_bom_id = al_AssmblBomId AND
             POS.nh_assmbl_pos_id = al_AssmblPosId
             AND
             BOM.assmbl_db_id     = POS.assmbl_db_id  AND
             BOM.assmbl_cd        = POS.assmbl_cd     AND
             BOM.assmbl_bom_id    = POS.assmbl_bom_id AND
             BOM.mandatory_bool   = 1;

    -- Declare a record for the BOM cursor.
    bom_rec bom_cur%ROWTYPE;

   -- Declare local variables.
    ll_InvNoId     il_InvNoId%TYPE;
    ll_InvNoDbId il_InvNoDbId%TYPE;
    ll_OrigAssmblDbId il_AssmblDbId%TYPE;

    -- Declare exceptions.
    xc_InventoryIdNull            EXCEPTION;
    xc_InventoryDbIdNull         EXCEPTION;
    xc_AssmblDbIdNull            EXCEPTION;
    xc_AssmblCdNull                EXCEPTION;
    xc_AssmblBomIdNull            EXCEPTION;
    xc_AssmblPosIdNull            EXCEPTION;
    xc_SetCompleteFlagFail        EXCEPTION;
    xc_RecurseMandatoryBOMFail    EXCEPTION;
    xc_RecurseSetIncompleteFail    EXCEPTION;
BEGIN

    -- Initialize the return variable.
    an_Success := icn_NoProc;

    -- Test for null identifiers, and raise an exception if null
    -- parameters are found.
    IF al_InventoryId IS NULL
    THEN
        RAISE xc_InventoryIdNull;
    END IF;
    IF al_InventoryDbId IS NULL
    THEN
        RAISE xc_InventoryDbIdNull;
    END IF;
    IF al_AssmblDbId IS NULL
    THEN
        RAISE xc_AssmblDbIdNull;
    END IF;
    IF as_AssmblCd IS NULL
    THEN
        RAISE xc_AssmblCdNull;
    END IF;
    IF al_AssmblBomId IS NULL
    THEN
        RAISE xc_AssmblBomIdNull;
    END IF;
    IF al_AssmblPosId IS NULL
    THEN
        RAISE xc_AssmblPosIdNull;
    END IF;
    -- Loop through the mandatory bill of materials entries
    FOR bom_rec IN bom_cur LOOP
        BEGIN

            -- Select items that match the mandatory bom from the
            -- inventory table.
            SELECT
              inv_no_id,
              inv_no_db_id,
              orig_assmbl_db_id
            INTO
              ll_InvNoId,
              ll_InvNoDbId,
              ll_OrigAssmblDbId
             FROM
              inv_inv
            WHERE
              assmbl_db_id    = bom_rec.assmbl_db_id AND
              assmbl_cd       = bom_rec.assmbl_cd AND
              assmbl_bom_id   = bom_rec.assmbl_bom_id AND
              assmbl_pos_id   = bom_rec.assmbl_pos_id AND
              nh_inv_no_id    = al_InventoryId AND
              nh_inv_no_db_id = al_InventoryDbId AND
              rstat_cd = 0;

            -- If there isn't any original assembly info, this is not
             -- an assembly itself, continue as normal
            IF ll_OrigAssmblDbId IS NULL THEN
                -- Update the complete flag for the item.
                  SetCompleteFlag( ll_InvNoId,
                                     ll_InvNoDbId,
                                     cn_True,
                                     an_Success );
                 -- Test the success flag, and raise an exception if an error occured.
                  IF an_Success <= 0 THEN
                     RAISE xc_SetCompleteFlagFail;
                  END IF;

                  -- Repeat for the children of the selected item.
                RecurseMandatoryBOM( ll_InvNoId,
                                     ll_InvNoDbId,
                                     bom_rec.assmbl_db_id,
                                     bom_rec.assmbl_cd,
                                     bom_rec.assmbl_bom_id,
                                     bom_rec.assmbl_pos_id,
                                     as_ApplicabilityCd,
                                     an_Success );
                -- Test the success flag, and raise an exception if an error occured.
                IF an_Success <= 0 THEN
                    RAISE xc_RecurseMandatoryBOMFail;
                END IF;
            ELSE
                 -- This is an assembly, start from the beginning and check this assembly's
                 -- completeness, then return to this position and continue on
                EvaluateAssemblyCompleteness( ll_InvNoId,
                                                 ll_InvNoDbId,
                                                  an_Success );
                 -- Test the success flag
                IF an_Success <= 0 THEN
                    RAISE xc_RecurseMandatoryBOMFail;
                END IF;
            END IF;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
              -- we can forgive there being no inventory if the bom part is not applicable
              -- to the current assembly.
              -- otherwise, consider it to be incomplete if it is applicable, but missing.
              IF ( isApplicableBOMItem( bom_rec.assmbl_db_id,
                                        bom_rec.assmbl_cd,
                                        bom_rec.assmbl_bom_id,
                                        bom_rec.assmbl_pos_id,
                                        as_ApplicabilityCd,
                                        an_Success ) ) THEN
                  -- Set the parent complete flag to FALSE
                  SetCompleteFlag( al_InventoryId,
                                   al_InventoryDbId,
                                   cn_False,
                                   an_Success );
                  -- Test the success flag, and raise an exception if an error occured.
                  IF an_Success <= 0 THEN
                      RAISE xc_SetCompleteFlagFail;
                  END IF;

                  -- recurse
                  RecurseSetParentIncomplete( al_InventoryId,
                                              al_InventoryDbId,
                                              an_Success );
                  -- Test the success flag, and raise an exception if an error occured.
                  IF an_Success <= 0 THEN
                      RAISE xc_RecurseSetIncompleteFail;
                  END IF;
              END IF;

            WHEN OTHERS THEN
                -- Set the return variable to indicate a failure.
                an_Success := -1;
        END;
    END LOOP;

    -- Return success
    an_Success := icn_Success;

EXCEPTION
    WHEN xc_InventoryIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -1;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory identifier' );
        RETURN;
    WHEN xc_InventoryDbIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -2;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory database identifier' );
        RETURN;
    WHEN xc_AssmblDbIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -3;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'assembly database identifier' );
        RETURN;
    WHEN xc_AssmblCdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -4;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'assembly code' );
        RETURN;
    WHEN xc_AssmblBomIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -5;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'assembly bill of materials identifier' );
        RETURN;
    WHEN xc_AssmblPosIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -6;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'assembly position identifier' );
        RETURN;
    WHEN xc_SetCompleteFlagFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -7;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.SetCompleteFlag');
        RETURN;
    WHEN xc_RecurseMandatoryBOMFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -8;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.RecurseMandatoryBOM');
        RETURN;
    WHEN xc_RecurseSetIncompleteFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -9;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.RecurseSetParentIncomplete');
        RETURN;
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', '');
        RETURN;
END RecurseMandatoryBOM;

/********************************************************************************
*
*  Function:     isApplicableBOMItem
*  Arguments:    al_AssmblDbId       - bom item assembly db id
*                as_AssmblCd         - bom item assembly cd
*                al_AssmblBomId      - bom item id
*                al_AssmblPosId      - bom item position
*                as_ApplicabilityCd  - applicability code to compare against
*                an_Success          - whether the method runs properly
*  Return:       true or false
*  Description:  This function checks to see if the tracked bom part associated
*                with the given bom item is applicable
*
*  Original Coder: nso
*  Recent Coder:
*  Recent Date:    Sep 16, 2005
*
*********************************************************************************/
FUNCTION isApplicableBOMItem( al_AssmblDbId IN eqp_assmbl_pos.assmbl_db_id%TYPE,
                              as_AssmblCd IN eqp_assmbl_pos.assmbl_cd%TYPE,
                              al_AssmblBomId IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
                              al_AssmblPosId IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
                              as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
                              an_Success OUT NUMBER ) RETURN BOOLEAN IS
  -- local vars
  lApplEffLdesc eqp_bom_part.appl_eff_ldesc%type;
  lApplicable boolean;
BEGIN
     -- if the applicability code is blank, we can automatically treat it as applicable
     IF ( as_ApplicabilityCd is null OR as_ApplicabilityCd = '' ) THEN
        an_Success := 1;
        return true;
     END IF;

     -- get the applicability range for the tracked bom part that applies to this
     -- bom part position. In the case of systems, this will return no data and
     -- we will consider it to be applicable.
     -- Note that for tracked bom items, there will only ever be one
     select
        eqp_bom_part.appl_eff_ldesc
     into
        lApplEffLdesc
     from
        eqp_bom_part
     where
        eqp_bom_part.assmbl_db_id = al_AssmblDbId and
        eqp_bom_part.assmbl_cd    = as_AssmblCd and
        eqp_bom_part.assmbl_bom_id = al_AssmblBomId
        and
        ( eqp_bom_part.inv_class_cd = 'TRK' OR eqp_bom_part.inv_class_cd = 'ASSY' );

     -- now check the applicability and return
     an_Success := 1;
     lApplicable := isApplicable( lApplEffLdesc, as_ApplicabilityCd ) = 1;
     return lApplicable;

     -- handle any exceptions
     EXCEPTION
       -- no data found means that the bom item was a system. Systems are always
       -- treated as applicable (normal)
       WHEN NO_DATA_FOUND THEN
           an_Success := 1;
           return true;
       -- unexpected exception
       WHEN OTHERS THEN
           an_Success := -1;
           return false;
END isApplicableBOMItem;

/********************************************************************************
*
*  Procedure:    SetCompleteFlag
*  Arguments:    al_InventoryId      - inventory item identifier
*                 al_InventoryDbId    - inventory item database identifier
*                 an_State            - 'TRUE' or 'FALSE'
*  Return:       (number) - > 0 to indicate success
*  Description:  This procedure updates the complete flag for the specified
*                 inventory item.
*
*  Original Coder: Laura Cline
*  Recent Coder:   cjb
*  Recent Date:    February 24, 2005
*
*********************************************************************************
*
*    Revision History
*
*    Version    Description
*    1.0        Initial Version
*
*********************************************************************************
*
*    Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE SetCompleteFlag( al_InventoryId IN il_InvNoId%TYPE,
                           al_InventoryDbId IN il_InvNoDbId%TYPE,
                           an_State IN in_CompleteBool%TYPE,
                           an_Success OUT NUMBER )
IS
BEGIN
    -- Initialize the return variable.
    an_Success := icn_NoProc;

    -- Update the row in the inventory table.
    UPDATE inv_inv
       SET complete_bool = an_State
     WHERE inv_no_id = al_InventoryId
       AND inv_no_db_id = al_InventoryDbId
		 AND complete_bool != an_State;

    -- Return success
    an_Success := icn_Success;

EXCEPTION
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', '');
        RETURN;
END SetCompleteFlag;
/********************************************************************************
*
*  Procedure:    GetParent
*  Arguments:    al_InventoryId      - inventory item identifier
*                 al_InventoryDbId    - inventory item database identifier
*  Return:       al_ParentId        - parent item identifier
*                 al_ParentDbId        - parent item database identifier
*                 (integer) - > 0 to indicate success
*  Description:  This procedure determines the parent for the specified inventory
*                 item.
*
*  Original Coder: Laura Cline
*  Recent Coder:   cjb
*  Recent Date:    February 24, 2005
*
*********************************************************************************
*
*    Revision History
*
*    Version    Description
*    1.0        Initial Version
*
*********************************************************************************
*
*    Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetParent( al_InventoryId IN il_InvNoId%TYPE,
                     al_InventoryDbId IN il_InvNoDbId%TYPE,
                     al_ParentId OUT il_InvNoId%TYPE,
                     al_ParentDbId OUT il_InvNoDbId%TYPE,
                     an_Success OUT NUMBER )
IS
    -- Declare exceptions
    xc_InventoryIdNull        EXCEPTION;
    xc_InventoryDbIdNull    EXCEPTION;
BEGIN

    -- Initialize the return variable.
    an_Success := icn_NoProc;

    -- Test for null identifiers, and raise an exception if null
    -- parameters are found.
    IF al_InventoryId IS NULL
    THEN
        RAISE xc_InventoryIdNull;
    END IF;
    IF al_InventoryDbId IS NULL
    THEN
        RAISE xc_InventoryDbIdNull;
    END IF;
    -- Select the next highest inventory from the inventory table.
    SELECT nh_inv_no_id,
           nh_inv_no_db_id
      INTO al_ParentId,
           al_ParentDbId
      FROM inv_inv
     WHERE inv_no_id    = al_InventoryId
       AND inv_no_db_id = al_InventoryDbId AND
           rstat_cd = 0;

    -- Return success
    an_Success := icn_Success;

EXCEPTION
    WHEN xc_InventoryIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -1;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory identifier' );
        DBMS_OUTPUT.PUT_LINE('GetParent: Null inventory id');
        RETURN;
    WHEN xc_InventoryDbIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -2;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory database identifier' );
        DBMS_OUTPUT.PUT_LINE('GetParent: null inventory db id');
        RETURN;
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', '');
        RETURN;
END GetParent;
/********************************************************************************
*
*  Procedure:    GetAssemblyAndPosition
*  Arguments:    al_InventoryId      - inventory item identifier
*                 al_InventoryDbId    - inventory item database identifier
*  Return:       al_AssmblDbId        - assembly database identifier
*                 as_AssmblCd        - assembly code
*                 al_AssmblBomId        - assembly bill of materials identifier
*                 al_AssmblPosId        - assembly position identifier
*                 (number) - > 0 to indicate success
*  Description:  This procedure determines the assembly database id, code, bom id
*                 and position for the specified inventory item.
*
*  Original Coder: Laura Cline
*  Recent Coder:   cjb
*  Recent Date:    February 24, 2005
*
*********************************************************************************
*
*    Revision History
*
*    Version    Description
*    1.0        Initial Version
*
*********************************************************************************
*
*    Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetAssemblyAndPosition( al_InventoryId IN il_InvNoId%TYPE,
                                  al_InventoryDbId IN il_InvNoDbId%TYPE,
                                  al_AssmblDbId OUT il_AssmblDbId%TYPE,
                                  as_AssmblCd OUT is_AssmblCd%TYPE,
                                  al_AssmblBomId OUT il_AssmblBomId%TYPE,
                                  al_AssmblPosId OUT il_AssmblPosId%TYPE,
                                  an_Success OUT NUMBER )
IS
    -- Declare local constants
    cl_OrigAssmblBomId il_AssmblDbId%TYPE := 0;
    cl_OrigAssmblPosId il_AssmblPosId%TYPE := 1;

    -- Declare local variables
    ll_OrigAssmblDbId il_AssmblDbId%TYPE;
    ls_OrigAssmblCd   is_AssmblCd%TYPE;
BEGIN

    -- Initialize the return variable.
    an_Success := icn_NoProc;

    -- Test for null identifiers, and raise an exception if null
    -- parameters are found.
    IF al_InventoryId IS NULL
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -1;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory identifier' );
        RETURN;
    END IF;
    IF al_InventoryDbId IS NULL
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -2;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory database identifier' );
        RETURN;
    END IF;

   -- Select the original assembly details.
    SELECT orig_assmbl_db_id,
           orig_assmbl_cd
      INTO ll_OrigAssmblDbId,
           ls_OrigAssmblCd
      FROM inv_inv
     WHERE inv_no_id    = al_InventoryId
       AND inv_no_db_id = al_InventoryDbId AND
           rstat_cd = 0;

   -- Test to see if the original assembly details are null.
    IF ll_OrigAssmblDbId IS NULL THEN
        -- There is no original assembly information for this item.
        -- Select the actual assembly information from the inventory table.
        SELECT assmbl_db_id,
               assmbl_cd,
               assmbl_bom_id,
               assmbl_pos_id
          INTO al_AssmblDbId,
               as_AssmblCd,
               al_AssmblBomId,
               al_AssmblPosId
          FROM inv_inv
         WHERE inv_no_id = al_InventoryId
           AND inv_no_db_id = al_InventoryDbId AND
               rstat_cd = 0;
    ELSE
        -- The item is an assembly; set the assembly information to the
        -- original details.
         al_AssmblDbId  := ll_OrigAssmblDbId;
        as_AssmblCd    := ls_OrigAssmblCd;
        al_AssmblBomId := cl_OrigAssmblBomId;
        al_AssmblPosId := cl_OrigAssmblPosId;
    END IF;

    -- Return success
    an_Success := icn_Success;

EXCEPTION
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', SQLERRM);
        RETURN;
END GetAssemblyAndPosition;


/********************************************************************************
*
*  Procedure:    GetAssemblyApplicabilityCode
*  Arguments:    aInventoryDbId       inventory item identifier
*                 aInventoryId      - inventory item database identifier
*                 aApplicabilityCd  - applicability code for the assembly
*  Description:  Fetch the applicaiblity code for the assembly associated with
*                the given inventory
*
*  Original Coder: nso
*  Recent Coder:
*  Recent Date:    sep 19, 2005
*
*********************************************************************************/
PROCEDURE GetAssemblyApplicabilityCode( aInventoryDbId IN inv_inv.inv_no_db_id%TYPE,
                                        aInventoryId   IN inv_inv.inv_no_id%TYPE,
                                        aApplicabilityCd OUT inv_inv.appl_eff_cd%TYPE )
IS
BEGIN
  select
     assembly_inv_inv.appl_eff_cd
  into
     aApplicabilityCd
  from
     inv_inv assembly_inv_inv,
     inv_inv
  where
     assembly_inv_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id and
     assembly_inv_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
     AND
     inv_inv.rstat_cd	= 0
     and
     inv_inv.inv_no_db_id = aInventoryDbId and
     inv_inv.inv_no_id    = aInventoryId;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
      -- no assembly
      aApplicabilityCd := null;

END GetAssemblyApplicabilityCode;

/********************************************************************************
*
*  Procedure:    CheckParentNode
*  Arguments:    al_ParentId      - parent inventory item identifier
*                al_ParentDbId      - parent inventory database identifier
*  Return:       as_State            - state string:  'complete' or 'incomplete'
*                 (number) - > 0 to indicate success
*  Description:  This procedure evaluates the mandatory children at the parent
*                 node to determine if the parent is complete.
*
*  Original Coder: Laura Cline
*  Recent Coder:   cjb
*  Recent Date:    February 25, 2005
*
*********************************************************************************
*
*    Revision History
*
*    Version    Description
*    1.0        Initial Version
*
*********************************************************************************
*
*    Copyright © 1998-2003 MxI Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CheckParentNode( al_ParentId IN il_InvNoId%TYPE,
                           al_ParentDbId IN il_InvNoDbId%TYPE,
                           as_State OUT VARCHAR2,
                           an_Success OUT NUMBER )
IS
    -- Declare the mandatory bom children cursor.
    CURSOR childbom_cur( al_ParentAssyDbId IN il_AssmblDbId%TYPE,
                         as_ParentAssyCd IN is_AssmblCd%TYPE,
                         al_ParentAssyBomId IN il_AssmblBomId%TYPE,
                         al_ParentAssyPosId IN il_AssmblPosId%TYPE ) IS
        SELECT POS.assmbl_db_id,
               POS.assmbl_cd,
               POS.assmbl_bom_id,
               POS.assmbl_pos_id
          FROM eqp_assmbl_bom BOM,
               eqp_assmbl_pos POS
         WHERE POS.nh_assmbl_db_id   = al_ParentAssyDbId  AND
               POS.nh_assmbl_cd      = as_ParentAssyCd    AND
               POS.nh_assmbl_bom_id  = al_ParentAssyBomId AND
               POS.nh_assmbl_pos_id  = al_ParentAssyPosId
           AND
               BOM.assmbl_db_id      = POS.assmbl_db_id  AND
               BOM.assmbl_cd         = POS.assmbl_cd     AND
               BOM.assmbl_bom_id     = POS.assmbl_bom_id AND
               BOM.mandatory_bool    = 1;
    -- Declare a record for the cursor
    childbom_rec childbom_cur%ROWTYPE;
    -- Declare local variables.
    ll_ParentAssyDbId    il_AssmblDbId%TYPE;
    ls_ParentAssyCd        is_AssmblCd%TYPE;
    ll_ParentAssyBomId    il_AssmblBomId%TYPE;
    ll_ParentAssyPosId    il_AssmblPosId%TYPE;
    ll_InvNoId            il_InvNoId%TYPE;
    ll_InvNoDbId          il_InvNoDbId%TYPE;
    ln_Complete           in_CompleteBool%TYPE;
    lApplicabilityCd      inv_inv.appl_eff_cd%TYPE;

    -- Declare exceptions
    xc_ParentIdNull            EXCEPTION;
    xc_ParentDbIdNull        EXCEPTION;
    xc_GetAssyAndPosFail    EXCEPTION;
BEGIN

    -- Initialize the success flag.
    an_Success := icn_NoProc;

    -- Initialize the state to COMPLETE.
    as_State := 'COMPLETE';

    -- Test for null identifiers, and raise an exception if null
    -- parameters are found.
    IF al_ParentId IS NULL
    THEN
        RAISE xc_ParentIdNull;
    END IF;
    IF al_ParentDbId IS NULL
    THEN
        RAISE xc_ParentDbIdNull;
    END IF;

    -- Determine the assembly details of the parent item.
    GetAssemblyAndPosition( al_ParentId,
                            al_ParentDbId,
                            ll_ParentAssyDbId,
                            ls_ParentAssyCd,
                            ll_ParentAssyBomId,
                            ll_ParentAssyPosId,
                            an_Success );
    -- Test the success flag, and raise an exception if an error occurred.
    IF an_Success <= 0
    THEN
        RAISE xc_GetAssyAndPosFail;
    END IF;
    -- Loop through each of the mandatory children, except the
    -- input item.
    FOR childbom_rec IN childbom_cur(ll_ParentAssyDbId,
                                     ls_ParentAssyCd,
                                     ll_ParentAssyBomId,
                                     ll_ParentAssyPosId)
    LOOP
        -- Set up an anonymous block to trap any errors with the
        -- execution of the select statement.
        BEGIN
            -- Select the child item id, and complete flag from the
            -- inventory table.
            SELECT inv_no_id,
                   inv_no_db_id,
                   complete_bool
              INTO ll_InvNoId,
                   ll_InvNoDbId,
                   ln_Complete
              FROM inv_inv
             WHERE assmbl_db_id    = childbom_rec.assmbl_db_id
               AND assmbl_cd       = childbom_rec.assmbl_cd
               AND assmbl_bom_id   = childbom_rec.assmbl_bom_id
               AND assmbl_pos_id   = childbom_rec.assmbl_pos_id
               AND nh_inv_no_id    = al_ParentId
               AND nh_inv_no_db_id = al_ParentDbId
               AND rstat_cd = 0;

            -- Test the complete flag.
            IF ln_Complete = 0 THEN
                -- The item is not complete. This could be ok if the inventory is not applicable

                -- get the applicability code for the assembly
                GetAssemblyApplicabilityCode( al_ParentDbId, al_ParentId, lApplicabilityCd );

                -- check the applicability

                IF ( isApplicableBOMItem( childbom_rec.assmbl_db_id,
                                          childbom_rec.assmbl_cd,
                                          childbom_rec.assmbl_bom_id,
                                          childbom_rec.assmbl_pos_id,
                                          lApplicabilityCd,
                                          an_Success ) ) THEN
                  -- if applicable, set the output state to incomplete.
                  as_State := 'INCOMPLETE';
                END IF;
            END IF;
        EXCEPTION
            WHEN NO_DATA_FOUND
            THEN
                -- No child item was found; this indicates a hole in the
                -- inventory hierarchy. This could mean the inventory is incomplete,
                -- but only if the inventory is applicable

                -- get the applicability code for the assembly
                GetAssemblyApplicabilityCode( al_ParentDbId, al_ParentId, lApplicabilityCd );

                -- check the applicability
                IF ( isApplicableBOMItem( childbom_rec.assmbl_db_id,
                                          childbom_rec.assmbl_cd,
                                          childbom_rec.assmbl_bom_id,
                                          childbom_rec.assmbl_pos_id,
                                          lApplicabilityCd,
                                          an_Success ) ) THEN
                  -- if applicable, set the output state to incomplete.
                  as_State := 'INCOMPLETE';
                END IF;
        END;
    END LOOP;

    -- Return success
    an_Success := icn_Success;

EXCEPTION
    WHEN xc_ParentIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -3;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'parent identifier' );
        DBMS_OUTPUT.PUT_LINE('null parent id');
        RETURN;
    WHEN xc_ParentDbIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -4;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'parent database identifier' );
        DBMS_OUTPUT.PUT_LINE('null parent db id');
        RETURN;
    WHEN xc_GetAssyAndPosFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -5;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.GetAssemblyAndPosition');
        DBMS_OUTPUT.PUT_LINE('GetAssyAndPos Failed');
        RETURN;
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', '');
        RETURN;
END CheckParentNode;
/********************************************************************************
*
*  Procedure:    RecurseSetParentIncomplete
*  Arguments:    al_InventoryId      - inventory item identifier
*                al_InventoryDbId    - inventory item database identifier
*
*  Return:       (number) - > 0 to indicate success
*  Description:  This procedure recurses up through the inventory hierarchy,
*                 setting each successive parent complete flag to false, until
*                 a false node is encountered.
*
*  Original Coder: Laura Cline
*  Recent Coder:   cjb
*  Recent Date:    February 24, 2005
*
*********************************************************************************
*
*    Revision History
*
*    Version    Description
*    1.0        Initial Version
*
*********************************************************************************
*
*    Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
*    Any distribution of the MxI source code by any other party than
*    MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE RecurseSetParentIncomplete( al_InventoryId IN il_InvNoId%TYPE,
                                      al_InventoryDbId IN il_InvNoDbId%TYPE,
                                      an_Success OUT NUMBER )
IS
    -- Declare local variables
    ll_ParentId        il_InvNoId%TYPE;
    ll_ParentDbId    il_InvNoDbId%TYPE;
    ln_Complete        in_CompleteBool%TYPE;
    -- Declare exceptions
    xc_InventoryIdNull        EXCEPTION;
    xc_InventoryDbIdNull    EXCEPTION;
    xc_GetParentFail        EXCEPTION;
    xc_SetCompleteFlagFail    EXCEPTION;
    xc_RecurseSetParentFail    EXCEPTION;
BEGIN

    -- Initialize the return variable.
    an_Success := icn_NoProc;

    -- Test for null identifiers, and raise an exception if null
    -- parameters are found.
    IF al_InventoryId IS NULL
    THEN
        RAISE xc_InventoryIdNull;
    END IF;
    IF al_InventoryDbId IS NULL
    THEN
        RAISE xc_InventoryDbIdNull;
    END IF;
    -- Get the parent identifiers for the input inventory item.
    GetParent( al_InventoryId,
               al_InventoryDbId,
               ll_ParentId,
               ll_ParentDbId,
               an_Success );
    -- Test the success flag, and raise an exception if an error occurred.
    IF an_Success <= 0 THEN
        RAISE xc_GetParentFail;
    END IF;
    -- Test to see if the parent is null.
    IF ll_ParentId IS NOT NULL THEN
        BEGIN
            -- Select the parent complete flag
            SELECT complete_bool
              INTO ln_Complete
              FROM inv_inv
             WHERE inv_no_id    = ll_ParentId
               AND inv_no_db_id = ll_ParentDbId
               AND rstat_cd = 0;
        EXCEPTION
            WHEN OTHERS THEN
                -- Set the return variable to indicate a failure
                an_Success := -1;
        END;
        -- Test the parent complete flag.
        IF ln_Complete = 1 THEN
            -- Set the parent complete flag to false, and repeat
            -- for the next parent.
            SetCompleteFlag( ll_ParentId,
                             ll_ParentDbId,
                             cn_False,
                             an_Success );
            -- Test the success flag, and raise an exception if an error occurred.
            IF an_Success <= 0 THEN
                RAISE xc_SetCompleteFlagFail;
            END IF;
            RecurseSetParentIncomplete( ll_ParentId,
                                        ll_ParentDbId,
                                        an_Success );
            -- Test the success flag, and raise an exception if an error occurred.
            IF an_Success <= 0 THEN
                RAISE xc_RecurseSetParentFail;
            END IF;
        END IF;
    END IF;

    -- Return success
    an_Success := icn_Success;

EXCEPTION
    WHEN xc_InventoryIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -1;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory identifier' );
        RETURN;
    WHEN xc_InventoryDbIdNull
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -2;
        APPLICATION_OBJECT_PKG.SetMxIError('BUS-00055', 'inventory database identifier' );
        RETURN;
    WHEN xc_GetParentFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -3;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.GetParent');
        RETURN;
    WHEN xc_SetCompleteFlagFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -4;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.SetCompleteFlagFail');
        RETURN;
    WHEN xc_RecurseSetParentFail
    THEN
        -- Set the return flag to indicate an error, and set the MxI error message.
        an_Success := -5;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_COMPLETE_PKG.RecurseSetParentIncomplete');
        RETURN;
    WHEN OTHERS
    THEN
        -- Set the return flag to indicate an error.
        an_Success := -100;
        APPLICATION_OBJECT_PKG.SetMxIError('DEV-99999', '');
        RETURN;
END RecurseSetParentIncomplete;
END INV_COMPLETE_PKG;
/