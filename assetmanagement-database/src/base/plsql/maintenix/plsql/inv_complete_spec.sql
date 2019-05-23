--liquibase formatted sql


--changeSet inv_complete_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE INV_COMPLETE_PKG IS
/********************************************************************************
*
*  Package:      Inv_Complete
*  Description:  This package provides services to evaluate and assign the
*				 item completeness boolean.
*
*  Coder:        Laura Cline
*  Date:         February 25, 1998
*
*********************************************************************************
*
*	Revision History
*
*	Version	Description
*	1.0		Initial Version
*
*********************************************************************************
*
*	Copyright 1998 MxI Technologies Ltd.  All Rights Reserved.
*	Any distribution of the MxI source code by any other party than
*	MxI Technologies Ltd is prohibited.
*
*********************************************************************************/

-- Subtype declarations
SUBTYPE typn_RetCode IS NUMBER;

-- Constant declarations (return codes)
icn_Success           CONSTANT typn_RetCode := 1;  -- Success
icn_NoProc            CONSTANT typn_RetCode := 0;  -- No processing done

-- Anchored declarations for datatypes commonly used in this package.
   il_InvNoDbId       inv_inv.inv_no_db_id%TYPE;
   il_InvNoId         inv_inv.inv_no_id%TYPE;
   il_AssmblDbId      eqp_assmbl_bom.assmbl_db_id%TYPE;
   is_AssmblCd        eqp_assmbl_bom.assmbl_cd%TYPE;
   il_AssmblBomId     eqp_assmbl_bom.assmbl_bom_id%TYPE;
   il_AssmblPosId     eqp_assmbl_pos.assmbl_pos_id%TYPE;
   in_CompleteBool	  inv_inv.complete_bool%TYPE;
--
-- Procedure Specifications
--
PROCEDURE EvaluateAssemblyCompleteness( al_InventoryId IN il_InvNoId%TYPE,
					al_InventoryDbId IN il_InvNoDbId%TYPE,
					an_Success OUT NUMBER );

PROCEDURE UpdateCompleteFlagOnRemove( al_ParentId IN il_InvNoId%TYPE,
				      al_ParentDbId IN il_InvNoDbId%TYPE,
				      an_Success OUT NUMBER );

PROCEDURE UpdateCompleteFlagOnInstall( al_InstallId IN il_InvNoId%TYPE,
				       al_InstallDbId IN il_InvNoDbId%TYPE,
				       an_Success OUT NUMBER );

FUNCTION isApplicableBOMItem( al_AssmblDbId IN eqp_assmbl_pos.assmbl_db_id%TYPE,
                              as_AssmblCd IN eqp_assmbl_pos.assmbl_cd%TYPE,
                              al_AssmblBomId IN eqp_assmbl_pos.assmbl_bom_id%TYPE,
                              al_AssmblPosId IN eqp_assmbl_pos.assmbl_pos_id%TYPE,
                              as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
                              an_Success OUT NUMBER ) RETURN BOOLEAN;

PROCEDURE SetCompleteFlag( al_InventoryId IN il_InvNoId%TYPE,
                           al_InventoryDbId IN il_InvNoDbId%TYPE,
                           an_State IN in_CompleteBool%TYPE,
                           an_Success OUT NUMBER );
END INV_COMPLETE_PKG;
/