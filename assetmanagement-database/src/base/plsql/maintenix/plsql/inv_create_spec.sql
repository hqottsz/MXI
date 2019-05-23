--liquibase formatted sql


--changeSet inv_create_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE INV_CREATE_PKG IS
/********************************************************************************
*
*  Package:      INV_CREATE_PKG
*  Description:  This package provides procedures to create Actual Equipment
*                records.
*
*  Orig.Coder:   Laura Cline
*  Recent Coder: Yungjae Cho
*  Recent Date:  December 30, 2010
*
*********************************************************************************
*
*  Copyright 1998 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/

/*-------------------------------- Subtype Declarations ------------------------*/

-- Define a subtype for the DB_ID domain type
SUBTYPE  typn_DbId IS mim_db.db_id%TYPE;

-- Define a subtype for the ID domain type
SUBTYPE  typn_Id  IS inv_inv.inv_no_id%TYPE;

-- Define a subtype for the rstat_cd domain type
SUBTYPE  typn_RstatCd IS mim_rstat.rstat_cd%TYPE;

-- Define a subtype for return codes
SUBTYPE typn_RetCode IS NUMBER;

-- Define a subtype for the assembly bom id, position and code types
SUBTYPE typn_AssmblBomId IS eqp_assmbl_bom.assmbl_bom_id%TYPE;
SUBTYPE typn_AssmblPosId IS eqp_assmbl_pos.assmbl_pos_id%TYPE;
SUBTYPE typs_AssmblCd IS eqp_assmbl_bom.assmbl_cd%TYPE;

-- Define a subtype for the part id.
SUBTYPE typn_PartId IS eqp_part_no.part_no_id%TYPE;

-- Define a subtype for the TSN, TSO and forecast rate types.
SUBTYPE typf_TSNQt IS inv_curr_usage.tsn_qt%TYPE;
SUBTYPE typf_TSOQt IS inv_curr_usage.tso_qt%TYPE;

-- Define a subtype for the mim data type identifier.
SUBTYPE typn_DataTypeId IS mim_data_type.data_type_id%TYPE;

-- Define a subtype for the event scheduled deadline type.
SUBTYPE typf_DeadQt IS evt_sched_dead.sched_dead_qt%TYPE;

/*---------------------------------- Constants -----------------------------*/

-- Basic error handling codes
icn_Success CONSTANT typn_RetCode := 1;   -- Success
icn_NoProc  CONSTANT typn_RetCode := 0;   -- No processing done
icn_Error   CONSTANT typn_RetCode := -1;  -- Error

-- Record Status codes
icn_Active  CONSTANT typn_RstatCd := 0;   -- Active Row
icn_Shadow  CONSTANT typn_RstatCd := 1;   -- Shadowed Row (aka. Inactive row)
icn_SoftDel CONSTANT typn_RstatCd := 2;   -- Soft Deleted Row

/*--------------------- Public Procedure Specifications --------------------*/

-- Procedure to instantiate a bill of materials
PROCEDURE CreateInventory( an_ParentItemDbId IN typn_DbId,
                     an_ParentItemId IN typn_Id,
                     as_CreateType IN VARCHAR2,
                     ab_ApplyReceivedDt IN BOOLEAN,
                     ab_ApplyManufacturedDt IN BOOLEAN,
                     on_Return OUT typn_RetCode );

-- Procedure to initialize the current usage for an item.
PROCEDURE InitializeCurrentUsage( an_ItemDbId IN typn_DbId,
                                an_ItemId IN typn_Id,
                                on_Return OUT typn_RetCode);

--Procedure to creates new SYS inventory items for the newly created SYS config slot.
PROCEDURE InvCreateSys (
              an_AssmblDbId   IN eqp_assmbl_bom.assmbl_db_id%TYPE,
              an_AssmblCd     IN eqp_assmbl_bom.assmbl_cd%TYPE,
              an_AssmblBomId  IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
              on_Return       OUT NUMBER);

END INV_CREATE_PKG;
/