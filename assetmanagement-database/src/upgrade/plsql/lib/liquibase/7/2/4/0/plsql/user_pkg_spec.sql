--liquibase formatted sql


--changeSet user_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE USER_PKG
IS


/******************************************************************************
* TYPE DECLARATIONS
******************************************************************************/

/* subtype declarations */
 SUBTYPE typn_RetCode IS NUMBER;
 SUBTYPE typn_Id IS po_header.po_id%TYPE;
  
  
 /* constant declarations (error codes) */
 icn_Success CONSTANT typn_RetCode := 1;   -- Success
 icn_NoProc  CONSTANT typn_RetCode := 0;   -- No processing done
 icn_Error   CONSTANT typn_RetCode := -1;  -- Error


/********************************************************************************
*
* Function:       is_user_auth_to_view_order
* Arguments:      an_PoDbId (IN NUMBER): order primary key
*                 an_PoId (IN NUMBER):   order primary key
*	          an_CurrUserId (IN NUMBER): user primary key
*                 
* Description:    Tests if an user is authorized to view an order.
*                 
*
* Orig.Coder:     Akash Motwani
* Recent Coder:   Natasa Subotic
* Recent Date:    July 28,2011
*
*********************************************************************************
*
* Copyright 2011 MxI Technologies Ltd.  All Rights Reserved.
* Any distribution of the MxI source code by any other party than
* MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION is_user_auth_to_view_order(
                             an_PoDbId IN typn_Id,
                             an_PoId IN typn_Id,
                             an_CurrUserId IN typn_Id) RETURN NUMBER;
  
END USER_PKG;
/