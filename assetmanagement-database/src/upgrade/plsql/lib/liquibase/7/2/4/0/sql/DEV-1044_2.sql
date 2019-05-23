--liquibase formatted sql


--changeSet DEV-1044_2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY INV_CREATE_PKG IS
/*------------------- Private Procedure Declarations -----------------------*/

-- Procedure to create a new inventory record.
PROCEDURE AddInventoryRecord(
                       an_NewInvDbId IN typn_Id,
                       an_NewInvId IN typn_Id,
                       an_AssmblDbId IN typn_DbId,
                       as_AssmblCd IN typs_AssmblCd,
                       an_AssmblBomId IN typn_AssmblBomId,
                       an_AssmblPosId IN typn_AssmblPosId,
                       as_InvClass IN inv_inv.inv_class_cd%TYPE,
                       an_PartNoDbId IN typn_DbId,
                       an_PartNoId IN typn_PartId,
                       an_ParentInvNoDbId IN typn_DbId,
                       an_ParentInvNoId IN typn_Id,
                       an_NewInvBomPartDbId IN inv_inv.bom_part_db_id%TYPE,
                       an_NewInvBomPartId IN inv_inv.bom_part_id%TYPE,
                       on_Return OUT typn_RetCode);

-- Procedure to get the standard part for a bom slot.
PROCEDURE GetStandardPart(an_AssmblDbId IN typn_DbId,
                        as_AssmblCd IN typs_AssmblCd,
                        an_AssmblBomId IN typn_AssmblBomId,
                        as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
                        on_PartNoDbId OUT typn_DbId,
                        on_PartNoId OUT typn_PartId,
                        on_Return OUT typn_RetCode);

-- Procedure to drill down through the full or partial bill of materials
-- and create inventory records.
PROCEDURE RecurseBOM( as_Mode IN VARCHAR2,
                      an_AssmblDbId IN typn_DbId,
                      as_AssmblCd IN typs_AssmblCd,
                      an_AssmblBomId IN typn_AssmblBomId,
                      an_AssmblPosId IN typn_AssmblPosId,
                      an_ParentDbId IN typn_DbId,
                      an_ParentId IN typn_Id,
                      as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
                      on_Return OUT typn_RetCode );

-- Procedure to initialize the instance variables.
PROCEDURE SetGlobalInfo( an_ParentItemDbId IN typn_DbId,
                         an_ParentItemId IN typn_Id,
                         on_Return OUT typn_RetCode );

/*---------------------- Package Variable declarations ------------------------*/

-- Declare instance variables
il_LocationDbId      inv_loc.loc_db_id%TYPE;
il_LocationId        inv_loc.loc_id%TYPE;
il_RootDbId          typn_DbId;
il_RootId            typn_Id;
il_OwnerDbId         inv_owner.owner_db_id%TYPE;
il_OwnerId           inv_owner.owner_id%TYPE;
il_InvCondDbId       ref_inv_cond.inv_cond_db_id%TYPE := 0;
is_InvCondCd         ref_inv_cond.inv_cond_cd%TYPE := 'INSRV';
idt_InstallDt        inv_inv.install_dt%TYPE;
idt_InstallGDt       inv_inv.install_gdt%TYPE;
idt_ReceivedDt       inv_inv.received_dt%TYPE;
idt_ManufactDt       inv_inv.manufact_dt%TYPE;
ib_LockedBool        inv_inv.locked_bool%TYPE;
il_AssmblInvNoDbId   typn_DbId;
il_AssmblInvNoId     typn_Id;
il_PreventSynchBool  inv_inv.prevent_synch_bool%TYPE;
ib_ApplyReceivedDt   BOOLEAN DEFAULT TRUE;
ib_ApplyManufacturedDt BOOLEAN DEFAULT TRUE;

/*----------------------------- Public Modules --------------------------------*/

/********************************************************************************
*
*  Procedure:    CreateInventory
*  Arguments:    an_ParentItemDbId  - parent item database identifier
*                an_ParentItemId    - parent item identifier
*                as_CreateType      - 'FULL' or 'PARTIAL' mode of creation
*  Return:       on_Return (integer) - > 0 to indicate success
*  Description:  This is the main procedure for the bulk instantiation of
*                inventory items.
*
*  Orig. Coder:  Laura Cline
*  Recent Coder: Yungjae Cho
*  Recent Date:  December 30, 2010
*
*********************************************************************************
*
*  Copyright ? 1998 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE CreateInventory( an_ParentItemDbId IN typn_DbId,
                           an_ParentItemId IN typn_Id,
                           as_CreateType IN VARCHAR2,
                           ab_ApplyReceivedDt IN BOOLEAN,
                           ab_ApplyManufacturedDt IN BOOLEAN,
                           on_Return OUT typn_RetCode )
IS
   -- Declare local variables
   ln_AssmblDbId     typn_DbId;
   ln_AssmblBomId    typn_AssmblBomId;
   ln_AssmblPosId    typn_AssmblPosId;
   ls_AssmblCd       typs_AssmblCd;
   ls_ApplicabilityCd inv_inv.appl_eff_cd%TYPE;

   -- Declare a local variable to hold the invendory description return
   -- This variable is never really used; it is only a placeholder
   ls_Sdesc    inv_inv.inv_no_sdesc%TYPE;

   CURSOR lcur_InvHier (cn_ParentItemDbId IN typn_DbId,
                        cn_ParentItemId   IN typn_Id) IS
   SELECT inv_no_db_id, inv_no_id
     FROM inv_inv
     WHERE rstat_cd = 0
    START WITH
          inv_no_db_id = cn_ParentItemDbId  AND
          inv_no_id    = cn_ParentItemId
    CONNECT BY
          nh_inv_no_db_id = PRIOR inv_no_db_id AND
          nh_inv_no_id    = PRIOR inv_no_id;


BEGIN

   -- Initialize the return codes
   on_Return    := icn_NoProc;

   -- Initialize the instance variables.
   ib_ApplyReceivedDt := ab_ApplyReceivedDt;
   ib_ApplyManufacturedDt := ab_ApplyManufacturedDt;
   
   SetGlobalInfo( an_ParentItemDbId,
                  an_ParentItemId,
                  on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   -- Update the root inventory's description
   INV_DESC_PKG.InvUpdInvDesc( an_ParentItemDbId,
                               an_ParentItemId,
                               ls_Sdesc,
                               on_Return );
   IF on_Return < 0 THEN
      RETURN;
   END IF;

   -- Determine the assembly id of the root item.
   SELECT assmbl_db_id,
          assmbl_cd,
          assmbl_bom_id,
          assmbl_pos_id
     INTO ln_AssmblDbId,
          ls_AssmblCd,
          ln_AssmblBomId,
          ln_AssmblPosId
     FROM inv_inv
    WHERE inv_no_db_id = an_ParentItemDbId
      AND inv_no_id    = an_ParentItemId
      AND rstat_cd = 0;

   -- If the root item has an assembly, recurse and evaluate completeness
   IF ( ln_AssmblDbId IS NOT NULL ) THEN

      -- get the applicability code
      select
            inv_inv.appl_eff_cd
      into
            ls_ApplicabilityCd
      from
            inv_inv
      where
         inv_inv.inv_no_db_id =  an_ParentItemDbId AND
         inv_inv.inv_no_id    =  an_ParentItemId
         AND
         inv_inv.rstat_cd	= 0;

      -- Recurse through the bill of materials to create inventory records.
      RecurseBOM( as_CreateType,
                  ln_AssmblDbId,
                  ls_AssmblCd,
                  ln_AssmblBomId,
                  ln_AssmblPosId,
                  an_ParentItemDbId,
                  an_ParentItemId,
                  ls_ApplicabilityCd,
                  on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      -- Evaluate the completeness of the new assembly
      INV_COMPLETE_PKG.EvaluateAssemblyCompleteness(
                              an_ParentItemId,
                              an_ParentItemDbId,
                              on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      -- Evaluate the completeness of the parent inventory tree
      INV_COMPLETE_PKG.UpdateCompleteFlagOnInstall(
                              an_ParentItemId,
                              an_ParentItemDbId,
                              on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END IF;

   -- Go through the new inventory hierarchy and initialize usage

   FOR lrec_Inv IN lcur_InvHier(an_ParentItemDbId, an_ParentItemId) LOOP

      InitializeCurrentUsage(lrec_Inv.inv_no_db_id, lrec_Inv.inv_no_id, on_Return);
      IF on_Return < 0 THEN
         RETURN;
      END IF;
   END LOOP;

   -- If everything executed successfully, set the return to indicate success.
   on_Return := icn_Success;

/*-------------------------- Exception Handling ----------------------------*/
EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@CreateInventory@@@' || SQLERRM);
      RETURN;
END CreateInventory;


/********************************************************************************
*
*  Procedure:    InvCreateSys
*  Arguments:    an_AssmblDbId (long)  - new config slot
*                an_AssmblCd (string)  - new config slot
*                an_AssmblBomId (long) - new config slot
*  Return:       on_Return long  - > 0 to indicate success
*  Description:  This procedure creates new SYS inventory items when the user creates
*                new SYS config slots in the Baseliner.
*
*  Orig. Coder:  jprimeau
*  Recent Coder:
*  Recent Date:  2007-03-28
*
*********************************************************************************
*
*  Copyright ? 2006 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InvCreateSys (
      an_AssmblDbId   IN eqp_assmbl_bom.assmbl_db_id%TYPE,
      an_AssmblCd     IN eqp_assmbl_bom.assmbl_cd%TYPE,
      an_AssmblBomId  IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
      on_Return       OUT NUMBER
   ) IS

      -- find all inventory that are missing the new SYS config slot.
      CURSOR lcur_InventoryRecords (
         cn_AssmblDbId   IN eqp_assmbl_pos.assmbl_db_id%TYPE,
         cn_AssmblCd     IN eqp_assmbl_pos.assmbl_cd%TYPE,
         cn_AssmblBomId  IN eqp_assmbl_pos.assmbl_bom_id%TYPE
      ) IS
         SELECT inv_inv.inv_no_db_id,
                inv_inv.inv_no_id,
                inv_inv.inv_class_cd,
                eqp_assmbl_pos.assmbl_pos_id
           FROM inv_inv,
                eqp_assmbl_pos,
                eqp_assmbl_bom parent_bom
          WHERE eqp_assmbl_pos.assmbl_db_id  = cn_AssmblDbId  AND
                eqp_assmbl_pos.assmbl_cd     = cn_AssmblCd    AND
                eqp_assmbl_pos.assmbl_bom_id = cn_AssmblBomId
                AND
                parent_bom.assmbl_db_id = eqp_assmbl_pos.nh_assmbl_db_id AND
                parent_bom.assmbl_cd    = eqp_assmbl_pos.nh_assmbl_cd    AND
                parent_bom.assmbl_bom_id = eqp_assmbl_pos.nh_assmbl_bom_id
                AND
                (
                 (
                  parent_bom.bom_class_cd = 'ROOT'
                  AND
                  inv_inv.orig_assmbl_db_id   = eqp_assmbl_pos.nh_assmbl_db_id   AND
                  inv_inv.orig_assmbl_cd      = eqp_assmbl_pos.nh_assmbl_cd
                  AND
         	  inv_inv.rstat_cd	= 0
                  )
                OR
                  (
                   parent_bom.bom_class_cd <> 'ROOT'
                   AND
                   inv_inv.assmbl_db_id   = eqp_assmbl_pos.nh_assmbl_db_id   AND
                   inv_inv.assmbl_cd      = eqp_assmbl_pos.nh_assmbl_cd      AND
                   inv_inv.assmbl_bom_id  = eqp_assmbl_pos.nh_assmbl_bom_id
                   AND
         	   inv_inv.rstat_cd	= 0
                  )
                );
      lrec_InventoryRecords   lcur_InventoryRecords%ROWTYPE;


   -- Declare local variables
   ln_NewInvDbId         typn_DbId;
   ln_NewInvId           typn_Id;
   ln_True               inv_inv.complete_bool%TYPE := 1;
    -- Declare exceptions
    xc_SetCompleteFlagFail    EXCEPTION;

   BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   -- Call the creation procedure for each inventory found
   FOR lrec_InventoryRecords IN lcur_InventoryRecords(an_AssmblDbId, an_AssmblCd, an_AssmblBomId)
   LOOP
      -- Initialize the instance variables.
      SetGlobalInfo( lrec_InventoryRecords.inv_no_db_id,
                     lrec_InventoryRecords.inv_no_id,
                     on_Return );
      IF on_Return < 0 THEN
         RETURN;
      END IF;

      -- reset the assembly inventory key to the parent if the parent is an assembly
      IF lrec_InventoryRecords.inv_class_cd = 'ASSY' THEN
         il_AssmblInvNoDbId := lrec_InventoryRecords.inv_no_db_id;
           il_AssmblInvNoId   := lrec_InventoryRecords.inv_no_id;
        END IF;

   -- Get the next inventory DB_ID
   SELECT db_id
        INTO ln_NewInvDbId
   FROM mim_local_db;

   -- Get the next inventory id from the sequence.
   SELECT inv_no_id_seq.NEXTVAL
   INTO ln_NewInvId
   FROM dual;

   -- Add a row to the inventory table for the new item.
   AddInventoryRecord( ln_NewInvDbId,
                       ln_NewInvId,
                       an_AssmblDbId,
                       an_AssmblCd,
                       an_AssmblBomId,
                       lrec_InventoryRecords.assmbl_pos_id,
                       'SYS',
                       NULL,
                       NULL,
                   lrec_InventoryRecords.inv_no_db_id,
                  lrec_InventoryRecords.inv_no_id,
                       NULL,
                       NULL,
                            on_Return);

         IF on_Return < 0 THEN
           RETURN;
         END IF;

      -- Set the complete flag for the inventory item to TRUE.
      INV_COMPLETE_PKG.SetCompleteFlag( ln_NewInvId,
                       ln_NewInvDbId,
                       ln_True,
                       on_Return );
      IF on_Return <= 0 THEN
          RAISE xc_SetCompleteFlagFail;
      END IF;
    END LOOP;

   -- If everything executed successfully, set the return to indicate success.
   on_Return := icn_Success;

/*-------------------------- Exception Handling ----------------------------*/
EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@InvCreateSys@@@' || SQLERRM);
      RETURN;
END InvCreateSys;


/********************************************************************************
*
*  Procedure:   InitializeCurrentUsage
*  Arguments:   an_ItemDbId   - inventory item identifier
*               an_ItemId     - inventory item identifier
*  Return:       (number) - 1 to indicate success, -1 to indicate failure
*  Description:  This procedure initializes the current usage informaton
*                for the specified inventory item.
*                Basically, it takes the inventory's part no and then
*                determines which bom items this part no can be assigned to.
*                Once all of the bom items have been determined, a superset of
*                the usage parms from these bom items is generated.
*
*  Orig. Coder:  Laura Cline
*  Recent Coder: cjb
*  Recent Date:  February 27, 2005
*
*********************************************************************************
*
*  Copyright ? 1998-2004 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InitializeCurrentUsage( an_ItemDbId IN  typn_DbId,
                                  an_ItemId   IN  typn_Id,
                                  on_Return   OUT typn_RetCode )
IS

   -- Usage definition cursor
   /* get usage parms from the all inventory assembly position */
   CURSOR lcur_UsageDefn (
         cn_InvNoDbId IN typn_DbId,
         cn_InvNoId   IN typn_Id
      ) IS
   /* Since we have no idea what the input inventory is, we'll just search for all data types */
   SELECT DISTINCT
             mim_part_numdata.data_type_db_id,
             mim_part_numdata.data_type_id
        FROM inv_inv,
             mim_part_numdata,
             ref_inv_class
       WHERE inv_inv.inv_no_db_id =  cn_InvNoDbId AND
             inv_inv.inv_no_id    =  cn_InvNoId
             AND
             inv_inv.rstat_cd	= 0
             AND
             ref_inv_class.inv_class_db_id = inv_inv.inv_class_db_id AND
             ref_inv_class.inv_class_cd = inv_inv.inv_class_cd
             AND
             ( ref_inv_class.serial_bool = 1 OR inv_inv.inv_class_cd = 'SYS' )
             AND
             mim_part_numdata.assmbl_db_id  = inv_inv.assmbl_db_id  AND
             mim_part_numdata.assmbl_cd     = inv_inv.assmbl_cd     AND
             mim_part_numdata.assmbl_bom_id = inv_inv.assmbl_bom_id
             AND
             NOT EXISTS
                 ( SELECT 1
                     FROM inv_curr_usage
                    WHERE inv_no_db_id = inv_inv.inv_no_db_id AND
                          inv_no_id    = inv_inv.inv_no_id
                          AND
                          data_type_db_id = mim_part_numdata.data_type_db_id AND
                          data_type_id    = mim_part_numdata.data_type_id )
   UNION ALL
   SELECT DISTINCT
             mim_part_numdata.data_type_db_id,
             mim_part_numdata.data_type_id
        FROM inv_inv,
             eqp_part_baseline,
             eqp_bom_part,
             mim_part_numdata
       WHERE inv_inv.inv_no_db_id =  cn_InvNoDbId AND
             inv_inv.inv_no_id    =  cn_InvNoId   AND
             inv_inv.inv_class_cd =  'SER'
             AND
             inv_inv.rstat_cd	= 0
             AND
             eqp_part_baseline.part_no_db_id = inv_inv.part_no_db_id AND
             eqp_part_baseline.part_no_id = inv_inv.part_no_id
             AND
             eqp_bom_part.bom_part_db_id = eqp_part_baseline.bom_part_db_id AND
             eqp_bom_part.bom_part_id = eqp_part_baseline.bom_part_id
             AND
             mim_part_numdata.assmbl_db_id  = eqp_bom_part.assmbl_db_id  AND
             mim_part_numdata.assmbl_cd     = eqp_bom_part.assmbl_cd     AND
             mim_part_numdata.assmbl_bom_id = eqp_bom_part.assmbl_bom_id
             AND
             NOT EXISTS
                 ( SELECT 1
                     FROM inv_curr_usage
                    WHERE inv_no_db_id = inv_inv.inv_no_db_id AND
                          inv_no_id    = inv_inv.inv_no_id
                          AND
                          data_type_db_id = mim_part_numdata.data_type_db_id AND
                          data_type_id    = mim_part_numdata.data_type_id );
   lrec_UsageDefn lcur_UsageDefn%ROWTYPE;

   -- Some variables
   ls_InvClassCd  inv_inv.inv_no_sdesc%TYPE;
   ln_tsn_qt      inv_curr_usage.tsn_qt%TYPE;
   ln_tso_qt      inv_curr_usage.tso_qt%TYPE;
   ln_tsi_qt      inv_curr_usage.tsi_qt%TYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   -- Get inventory class
   SELECT   inv_class_cd
   INTO     ls_InvClassCd
   FROM     inv_inv
   WHERE    inv_inv.inv_no_db_id = an_ItemDbId  AND
            inv_inv.inv_no_id    = an_ItemId AND
            inv_inv.rstat_cd = 0;

   -- For each of the data types defined for the item's part no, set the current usage values
   IF ls_InvClassCd = 'SYS' THEN
      -- For SYSTEM, initialize the current usage to the ASSMBL_INV_NO_PK's current usage

      FOR lrec_UsageDefn IN lcur_UsageDefn( an_ItemDbId, an_ItemId )
      LOOP
         -- Get tsn/tso/tsi values from the assembl inventory
         SELECT
            decode( inv_curr_usage.tsn_qt, null, 0, inv_curr_usage.tsn_qt ),
            decode( inv_curr_usage.tso_qt, null, 0, inv_curr_usage.tso_qt ),
            decode( inv_curr_usage.tsi_qt, null, 0, inv_curr_usage.tsi_qt )
         INTO
            ln_tsn_qt,
            ln_tso_qt,
            ln_tsi_qt
         FROM
            inv_curr_usage,
            inv_inv
         WHERE
            inv_inv.inv_no_db_id = an_ItemDbId  AND
            inv_inv.inv_no_id    = an_ItemId
            AND
            inv_inv.rstat_cd	= 0
            AND
            -- Outer join in case the inventory does not have this data type yet.
            inv_curr_usage.inv_no_db_id   (+)= inv_inv.assmbl_inv_no_db_id AND
            inv_curr_usage.inv_no_id      (+)= inv_inv.assmbl_inv_no_id
            AND
            inv_curr_usage.data_type_db_id   (+)= lrec_UsageDefn.data_type_db_id AND
            inv_curr_usage.data_type_id      (+)= lrec_UsageDefn.data_type_id;

         -- Insert a new row into the current usage table.
         INSERT INTO inv_curr_usage
                ( data_type_db_id,
                  data_type_id,
                  inv_no_db_id,
                  inv_no_id,
                  tsn_qt,
                  tso_qt,
                  tsi_qt )
         VALUES ( lrec_UsageDefn.data_type_db_id,
                  lrec_UsageDefn.data_type_id,
                  an_ItemDbId,
                  an_ItemId,
                  ln_tsn_qt,
                  ln_tso_qt,
                  ln_tsi_qt );
      END LOOP;
   ELSE
      -- For non-SYSTEM, initialize the current usage to 0
      FOR lrec_UsageDefn IN lcur_UsageDefn( an_ItemDbId, an_ItemId )
      LOOP

         -- Insert a new row into the current usage table.
         INSERT INTO inv_curr_usage
                ( data_type_db_id,
                  data_type_id,
                  inv_no_db_id,
                  inv_no_id,
                  tsn_qt,
                  tso_qt,
                  tsi_qt )
         VALUES ( lrec_UsageDefn.data_type_db_id,
                  lrec_UsageDefn.data_type_id,
                  an_ItemDbId,
                  an_ItemId,
                  0,
                  0,
                  0 );
      END LOOP;
   END IF;

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError( 'DEV-99999', 'INV_CREATE_PKG@@@InitializeCurrentUsage@@@' || SQLERRM );
      RETURN;
END InitializeCurrentUsage;


/*--------------------------- Private Modules ------------------------------*/

/********************************************************************************
*
*  Procedure:   RecurseBom
*  Arguments:   as_Mode        - 'FULL' or 'PARTIAL' mode of recursion
*               an_AssmblDbId  - assembly database identifier
*               as_AssmblCd    - assembly code
*               an_AssmblBomId - assembly bill of materials identifier
*               an_AssmblPosId - assembly position identifier
*               an_ParentDbId - parent inventory item
*               an_ParentId   - ""
*  Return:      (integer) - > 0 to indicate success
*  Description: This procedure recurses through the full or partial bill of
*               materials, creating inventory records.
*
*  Orig.Coder:   Laura Cline
*  Recent Coder: Daniel Baxter
*  Date:         December 18, 2008
*
*********************************************************************************
*
*  Copyright ? 1998-2008 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE RecurseBOM( as_Mode IN VARCHAR2,
                      an_AssmblDbId IN typn_DbId,
                      as_AssmblCd IN typs_AssmblCd,
                      an_AssmblBomId IN typn_AssmblBomId,
                      an_AssmblPosId IN typn_AssmblPosId,
                      an_ParentDbId IN typn_DbId,
                      an_ParentId IN typn_Id,
                      as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
                      on_Return OUT typn_RetCode )
IS
   -- Declare local variables
   ln_PartNoDbId         typn_DbId;
   ln_PartNoId           typn_PartId;
   ln_NewInvDbId         typn_DbId;
   ln_NewInvId           typn_Id;
   ls_InvClass           inv_inv.inv_class_cd%TYPE;
   ln_NewInvBomPartDbId  inv_inv.bom_part_db_id%TYPE;
   ln_NewInvBomPartId    inv_inv.bom_part_id%TYPE;

   -- Declare a bill of materials cursor based on the mode of recursion
   -- specified; full to create all inventory, partial to create only
   -- systems.
   CURSOR lcur_Bom(
         cs_Mode IN VARCHAR2,
         cn_AssmblDbId IN typn_DbId,
         cn_AssmblBomId IN typn_AssmblBomId,
         cn_AssmblPosId IN typn_AssmblPosId,
         cs_AssmblCd IN typs_AssmblCd
      ) IS
      SELECT BOM.assmbl_db_id,
             BOM.assmbl_cd,
             BOM.assmbl_bom_id,
             BOM.bom_class_cd,
             POS.assmbl_pos_id
        FROM eqp_assmbl_bom BOM,
             eqp_assmbl_pos POS
       WHERE cs_Mode = 'FULL'
         AND POS.nh_assmbl_db_id  = cn_AssmblDbId
         AND POS.nh_assmbl_cd     = cs_AssmblCd
         AND POS.nh_assmbl_bom_id = cn_AssmblBomId
         AND POS.nh_assmbl_pos_id = cn_AssmblPosId
         AND BOM.assmbl_db_id  = POS.assmbl_db_id
         AND BOM.assmbl_cd     = POS.assmbl_cd
         AND BOM.assmbl_bom_id = POS.assmbl_bom_id
         AND BOM.bom_class_cd    != 'SUBASSY'
         AND BOM.mandatory_bool   = 1
         AND BOM.rstat_cd        <> 2
         AND POS.rstat_cd     <> 2
       UNION
      SELECT BOM.assmbl_db_id,
             BOM.assmbl_cd,
             BOM.assmbl_bom_id,
             BOM.bom_class_cd,
             POS.assmbl_pos_id
        FROM eqp_assmbl_bom BOM,
             eqp_assmbl_pos POS
       WHERE cs_Mode != 'FULL'
         AND POS.nh_assmbl_db_id  = cn_AssmblDbId
         AND POS.nh_assmbl_cd     = cs_AssmblCd
         AND POS.nh_assmbl_bom_id = cn_AssmblBomId
         AND POS.nh_assmbl_pos_id = cn_AssmblPosId
         AND BOM.assmbl_db_id  = POS.assmbl_db_id
         AND BOM.assmbl_cd     = POS.assmbl_cd
         AND BOM.assmbl_bom_id = POS.assmbl_bom_id
         AND BOM.bom_class_cd     = 'SYS'
         AND BOM.mandatory_bool   = 1
         AND BOM.rstat_cd        <> 2
         AND POS.rstat_cd     <> 2;
   lrec_Bom lcur_Bom%ROWTYPE;

BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   -- Find all of the child bom items underneath the given bom item
   FOR lrec_Bom IN lcur_Bom(as_Mode,
                            an_AssmblDbId,
                            an_AssmblBomId,
                            an_AssmblPosId,
                            as_AssmblCd
                            )
   LOOP
      IF (INV_COMPLETE_PKG.isApplicableBOMItem(lrec_Bom.assmbl_db_id,
                             lrec_Bom.assmbl_cd,
                             lrec_Bom.assmbl_bom_id,
                             lrec_Bom.assmbl_pos_id,
                             as_ApplicabilityCd,
                             on_Return
                             )) THEN

        -- Get the standard or default part information.
        GetStandardPart(lrec_Bom.assmbl_db_id,
                        lrec_Bom.assmbl_cd,
                        lrec_Bom.assmbl_bom_id,
                        as_ApplicabilityCd,
                        ln_PartNoDbId,
                        ln_PartNoId,
                        on_Return);
        IF on_Return < 0 THEN
           RETURN;
        END IF;

        -- Check the BOM class of the retrieved assembly; assign an inventory class
        -- based on this BOM class
        IF lrec_Bom.bom_class_cd = 'SYS' THEN
           ls_InvClass := 'SYS';
        ELSIF lrec_Bom.bom_class_cd = 'SUBASSY' THEN
            ls_InvClass := 'ASSY';
        ELSIF lrec_Bom.bom_class_cd = 'TRK' THEN
            ls_InvClass := 'TRK';
        END IF;

        -- Get the BOM Part for the new inventory
        IF ( ls_InvClass = 'SYS' ) THEN

           -- SYS inventory's BOM Part key must be null
           ln_NewInvBomPartDbId := NULL;
           ln_NewInvBomPartId   := NULL;
        ELSE

            -- Lookup BOM Part key for all other inventory types
            SELECT eqp_bom_part.bom_part_db_id,
                   eqp_bom_part.bom_part_id
              INTO ln_NewInvBomPartDbId,
                   ln_NewInvBomPartId
              FROM eqp_bom_part,
                   ref_inv_class
             WHERE eqp_bom_part.assmbl_db_id  = lrec_Bom.assmbl_db_id
               AND eqp_bom_part.assmbl_cd     = lrec_Bom.assmbl_cd
               AND eqp_bom_part.assmbl_bom_id = lrec_Bom.assmbl_bom_id
               AND ref_inv_class.inv_class_db_id = eqp_bom_part.inv_class_db_id
               AND ref_inv_class.inv_class_cd    = eqp_bom_part.inv_class_cd
               AND ref_inv_class.tracked_bool  = 1
               AND ref_inv_class.rstat_cd    = 0;
        END IF;

        -- Test for errors before continuing.  If no standard part was
        -- found, then the bom slot specified will be left empty in the
        -- inventory hierarchy.  This is not an error condition that we
        -- should stop processing on.
        IF ( ( lrec_Bom.bom_class_cd = 'SYS' AND on_Return >= 0 ) OR ( lrec_Bom.bom_class_cd <> 'SYS' AND on_Return > 0 ) ) THEN

           -- Get the next inventory DB_ID
           SELECT db_id
             INTO ln_NewInvDbId
             FROM mim_local_db;

           -- Get the next inventory id from the sequence.
           SELECT inv_no_id_seq.NEXTVAL
             INTO ln_NewInvId
             FROM dual;

           -- Add a row to the inventory table for the new item.
           AddInventoryRecord( ln_NewInvDbId,
                               ln_NewInvId,
                               lrec_Bom.assmbl_db_id,
                               lrec_Bom.assmbl_cd,
                               lrec_Bom.assmbl_bom_id,
                               lrec_Bom.assmbl_pos_id,
                               ls_InvClass,
                               ln_PartNoDbId,
                               ln_PartNoId,
                               an_ParentDbId,
                               an_ParentId,
                               ln_NewInvBomPartDbId,
                               ln_NewInvBomPartId,
                               on_Return);
           IF on_Return < 0 THEN
              RETURN;
           END IF;

           -- Recurse through the children of the new item.
           RecurseBOM( as_Mode,
                       lrec_Bom.assmbl_db_id,
                       lrec_Bom.assmbl_cd,
                       lrec_Bom.assmbl_bom_id,
                       lrec_Bom.assmbl_pos_id,
                       ln_NewInvDbId,
                       ln_NewInvId,
                       as_ApplicabilityCd,
                       on_Return );
           IF on_Return < 0 THEN
              RETURN;
           END IF;
        END IF;
      END IF;
   END LOOP;

   -- If all of the loops executed successfully, set the return code
   -- to indicate success.
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@RecurseBOM@@@' || SQLERRM);
      RETURN;
END RecurseBOM;


/********************************************************************************
*
*  Procedure: GetStandardPart
*  Arguments: an_AssmblDbId  - assembly database identifier
*             as_AssmblCd  - assembly code
*             an_AssmblBomId  - assembly bill of materials identifier
*             as_ApplicabilityCd - applicability code of the assembly/aircraft being constructed
*  Return:    on_PartNoDbId   - part number database identifier
*             on_PartNoId  - part number identifier
*             on_Return    - return code
*  Description: This procedure selects the standard part for the specified
*              assembly slot.  If no standard is found or it is not applicable,
*            the first part found is selected.  If no part is found, an
*              error is returned.
*
*  Orig. Coder:  Laura Cline
*  Recent Coder: Daniel Baxter
*  Recent Date:  December 18, 2008
*
*********************************************************************************
*
*  Copyright ? 1998-2008 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE GetStandardPart(an_AssmblDbId IN typn_DbId,
                    as_AssmblCd IN typs_AssmblCd,
                    an_AssmblBomId IN typn_AssmblBomId,
                    as_ApplicabilityCd IN inv_inv.appl_eff_cd%TYPE,
                    on_PartNoDbId OUT typn_DbId,
                    on_PartNoId OUT typn_PartId,
                    on_Return OUT typn_RetCode)
IS
BEGIN

   -- Initialize the return code to indicate no processing.
   on_Return := icn_NoProc;

   -- Select the part to be installed for the specified assembly item. It defaults to the standard part unless the
   -- standard part is not applicable then it grabs the first applicable part as sorted by part number
   SELECT
      applicable_parts.part_no_db_id,
      applicable_parts.part_no_id
   INTO
      on_PartNoDbId,
      on_PartNoId
   FROM
      (
      SELECT
         eqp_part_no.part_no_db_id,
         eqp_part_no.part_no_id
      FROM
         eqp_part_baseline,
         eqp_bom_part,
         ref_inv_class,
         eqp_part_no
      WHERE
         eqp_bom_part.assmbl_db_id  = an_AssmblDbId AND
         eqp_bom_part.assmbl_cd     = as_AssmblCd AND
         eqp_bom_part.assmbl_bom_id = an_AssmblBomId
         AND
         ref_inv_class.inv_class_db_id = eqp_bom_part.inv_class_db_id AND
         ref_inv_class.inv_class_cd    = eqp_bom_part.inv_class_cd AND
         ref_inv_class.tracked_bool    = 1 	  AND
         ref_inv_class.rstat_cd        = 0
         AND
         eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id AND
         eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id AND
         eqp_part_baseline.rstat_cd       <> 2
         AND
         eqp_part_no.part_no_db_id = eqp_part_baseline.part_no_db_id AND
         eqp_part_no.part_no_id    = eqp_part_baseline.part_no_id
         AND
         isApplicable(eqp_part_baseline.appl_eff_ldesc, as_ApplicabilityCd ) = 1
      ORDER BY
         eqp_part_baseline.standard_bool DESC,
         eqp_part_no.part_no_oem
      ) applicable_parts
   WHERE
      ROWNUM = 1;

   -- Set the return code to indicate success
   on_Return := icn_Success;

EXCEPTION
   WHEN NO_DATA_FOUND THEN
      -- Set the return code to indicate no processing, and return.
      on_Return := icn_NoProc;
      RETURN;
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@GetStandadrPart@@@' || SQLERRM);
      RETURN;
END GetStandardPart;

/********************************************************************************
*
*  Procedure:    AddInventoryRecord
*  Arguments:    an_NewInvDbId        - the ID of the new inventory row to add
*                an_NewInvId          - ""
*                an_AssmblDbId        - assembly database identifier
*                as_AssmblCd          - assembly code
*                an_AssmblBomId       - assembly bill of materials id
*                an_AssmblPosId       - assembly position identifier
*                as_InvClass          - inventory class code
*                an_PartNoDbId        - part number database identifier
*                an_PartNoId          - part number identifier
*                an_ParentInvNoDbId   - parent's inv no
*                an_ParentInvNoId     - ""
*                an_NewInvBomPartDbId - the Bom Part Db Id of the new inventory
*                an_NewInvBomPartId   - the Bom Part Id of the new inventory
*  Return:       (number) - 1 to indicate success, -1 to indicate failure
*  Description:  This procedure adds a new row to the inventory table.
*
*  Orig. Coder:  Laura Cline
*  Recent Coder: cjb
*  Recent Date:  February 27, 2005
*
*********************************************************************************
*
*  Copyright ? 1998-2002 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE AddInventoryRecord(
                       an_NewInvDbId IN typn_Id,
                       an_NewInvId IN typn_Id,
                       an_AssmblDbId IN typn_DbId,
                       as_AssmblCd IN typs_AssmblCd,
                       an_AssmblBomId IN typn_AssmblBomId,
                       an_AssmblPosId IN typn_AssmblPosId,
                       as_InvClass IN inv_inv.inv_class_cd%TYPE,
                       an_PartNoDbId IN typn_DbId,
                       an_PartNoId IN typn_PartId,
                       an_ParentInvNoDbId IN typn_DbId,
                       an_ParentInvNoId IN typn_Id,
                       an_NewInvBomPartDbId IN inv_inv.bom_part_db_id%TYPE,
                       an_NewInvBomPartId IN inv_inv.bom_part_id%TYPE,
                       on_Return OUT typn_RetCode)
IS

   -- Declare local variables
   ls_Sdesc             inv_inv.inv_no_sdesc%TYPE;
   ldt_ReceivedDt       inv_inv.received_dt%TYPE;
   ldt_ManufactDt       inv_inv.manufact_dt%TYPE;
   
   lnParentPoDbId    inv_inv.po_db_id%TYPE;
   lnParentPoId      inv_inv.po_id%TYPE;
   lnParentLocalBool inv_owner.local_bool%TYPE;
   

   CURSOR mostRecentShipmentCursor(cl_PoDbId NUMBER, cl_PoId NUMBER) IS
      SELECT 
         po.po_type_cd, 
         line.inv_no_db_id AS return_inv_no_db_id, 
         line.inv_no_id AS return_inv_no_id
      FROM 
         PO_HEADER po 
         LEFT OUTER JOIN PO_LINE_RETURN_MAP line ON
            line.po_db_id = po.po_db_id AND
            line.po_id    = po.po_id
      WHERE 
         po.po_db_id   = cl_PoDbId AND
         po.po_id      = cl_PoId
         AND
         po.rstat_cd	= 0;

   lTypeStr VARCHAR2(8);

 
   lMostRecentShipmentRec mostRecentShipmentCursor%ROWTYPE;
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;
   SELECT inv.po_db_id, inv.po_id, owner.local_bool
   INTO	  
          lnParentPoDbId,lnParentPoId,lnParentLocalBool
   FROM INV_INV inv, INV_OWNER owner
   WHERE inv_no_db_id=an_ParentInvNoDbId AND inv_no_id=an_ParentInvNoId
   	AND
   		inv.owner_db_id=owner.owner_db_id AND
   		inv.owner_id=owner.owner_id AND
   		inv.rstat_cd	= 0;

      -- Initially assume all further cases fails, so set ownership type to OTHER
      lTypeStr:='OTHER';
      IF (lnParentLocalBool=1) THEN
         -- If inventory owner is INV_OWNER.LOCAL_BOOL = 1 owner, set ownership type to LOCAL
         lTypeStr:='LOCAL';
      ELSE
         IF ((lnParentPoDbId IS NOT NULL) AND (lnParentPoId IS NOT NULL)) THEN
            OPEN mostRecentShipmentCursor(lnParentPoDbId, lnParentPoId);
            FETCH mostRecentShipmentCursor INTO lMostRecentShipmentRec;
            IF (mostRecentShipmentCursor%FOUND) THEN
               -- If most recent shipment was incoming shipment received on exchange PO
               -- && no return inventory has been selected. Set ownership type to EXCHRCVD
               IF (lMostRecentShipmentRec.po_type_cd = 'EXCHANGE' AND
                   lMostRecentShipmentRec.return_inv_no_db_id IS NULL AND
                   lMostRecentShipmentRec.return_inv_no_id IS NULL) THEN
                  lTypeStr:='EXCHRCVD';
               -- If most recent shipment was outgoing shipment on an exchange PO
               -- && is return inventory. Set ownership type to EXCHRTRN
               ELSIF (lMostRecentShipmentRec.po_type_cd = 'EXCHANGE' AND
                   lMostRecentShipmentRec.return_inv_no_db_id = an_ParentInvNoDbId AND
                   lMostRecentShipmentRec.return_inv_no_id = an_ParentInvNoId) THEN
                  lTypeStr:='EXCHRTRN';
               -- If most recent incoming shipment is on an borrow PO
               -- Set ownership type to BORROW
               ELSIF (lMostRecentShipmentRec.po_type_cd = 'BORROW') THEN
                  lTypeStr:='BORROW';
               -- If most recent shipment was outgoing shipment on an consignment PO
               -- && no return inventory. Set ownership type to CSGNRCVD
               ELSIF (lMostRecentShipmentRec.po_type_cd = 'CONSIGN' AND
                   lMostRecentShipmentRec.return_inv_no_db_id IS NULL AND
                   lMostRecentShipmentRec.return_inv_no_id IS NULL) THEN
                  lTypeStr:='CSGNRCVD';
               -- If most recent shipment was outgoing shipment on an consignment PO
               -- && is return inventory. Set ownership type to CSGNRTRN
               ELSIF (lMostRecentShipmentRec.po_type_cd = 'CONSIGN' AND
                   lMostRecentShipmentRec.return_inv_no_db_id = an_ParentInvNoDbId AND
                   lMostRecentShipmentRec.return_inv_no_id = an_ParentInvNoId) THEN
                  lTypeStr:='CSGNRTRN';
               END IF;
            END IF;
            CLOSE mostRecentShipmentCursor;
         END IF;
      END IF;
 
   
   -- Determine the received date and manufactured date
   IF ib_ApplyReceivedDt THEN
      ldt_ReceivedDt := idt_ReceivedDt;
   ELSIF as_InvClass = 'SYS' THEN
      ldt_ReceivedDt := idt_ReceivedDt;
   ELSE
      ldt_ReceivedDt := NULL;
   END IF;
      
   IF ib_ApplyManufacturedDt THEN
      ldt_ManufactDt := idt_ManufactDt;
   ELSIF as_InvClass = 'SYS' THEN
      ldt_ManufactDt := idt_ManufactDt;
   ELSE
      ldt_ManufactDt := NULL;      
   END IF;

   -- Insert a new row into the inventory table.
   INSERT INTO inv_inv (
          inv_no_db_id,
          inv_no_id,
          inv_class_db_id,
          inv_class_cd,
          bom_part_db_id,
          bom_part_id,
          loc_db_id,
          loc_id,
          part_no_db_id,
          part_no_id,
          h_inv_no_db_id,
          h_inv_no_id,
          nh_inv_no_db_id,
          nh_inv_no_id,
          assmbl_db_id,
          assmbl_cd,
          assmbl_bom_id,
          assmbl_pos_id,
          owner_db_id,
          owner_id,
          owner_type_db_id,
          owner_type_cd,
          complete_bool,
          install_dt,
          install_gdt,
          received_dt,
          manufact_dt,
          used_bool,
          serial_no_oem,
          inv_cond_db_id,
          inv_cond_cd,
          assmbl_inv_no_db_id,
          assmbl_inv_no_id,
          reserved_bool,
          barcode_sdesc,
          locked_bool,
          issued_bool,
          prevent_synch_bool)
   VALUES (
          an_NewInvDbId,
          an_NewInvId,
          0,
          as_InvClass,
          an_NewInvBomPartDbId,
          an_NewInvBomPartId,
          il_LocationDbId,
          il_LocationId,
          an_PartNoDbId,
          an_PartNoId,
          il_RootDbId,
          il_RootId,
          an_ParentInvNoDbId,
          an_ParentInvNoId,
          an_AssmblDbId,
          as_AssmblCd,
          an_AssmblBomId,
          an_AssmblPosId,
          il_OwnerDbId,
          il_OwnerId,
          0,
          lTypeStr,
          0,
          NULL,
          NULL,
          ldt_ReceivedDt,
          ldt_ManufactDt,
          0,
          'XXX',
          il_InvCondDbId,
          is_InvCondCd,
          il_AssmblInvNoDbId,
          il_AssmblInvNoId,
          0,
          GENERATE_INV_BARCODE(),
          ib_LockedBool,
          1,
          il_PreventSynchBool);

   -- Set the inventory short description for the new item.
   INV_DESC_PKG.InvUpdInvDesc( an_NewInvDbId,
                               an_NewInvId,
                               ls_Sdesc,
                               on_Return );

   -- Set the inventory config position description for the new created inventory
   IF ( as_InvClass = 'ASSY' OR as_InvClass = 'SYS' OR as_InvClass = 'TRK' ) THEN
   	   INV_DESC_PKG.UpdateInvConfigPosDesc( an_NewInvDbId,
                                             an_NewInvId,
                                             on_Return );
   END IF;

   IF on_Return < 0 THEN
      RETURN;
   END IF;

   -- Return success
   on_Return := icn_Success;

/*------------------------------- Exception Handling --------------------------*/
EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@AddInventoryRecord@@@' || SQLERRM);
      IF mostRecentShipmentCursor%ISOPEN THEN CLOSE mostRecentShipmentCursor; END IF;
      RETURN;
END AddInventoryRecord;


/********************************************************************************
*
*  Procedure:    SetGlobalInfo
*  Arguments:    an_ParentItemDbId  - parent inventory database identifier
*                an_ParentItemId - parent inventory identifier
*  Return:       (number) - > 0 to indicate success
*  Description:  This procedure initializes the instance variables.
*
*  Orig. Coder:  Laura Cline
*  Recent Coder: cjb
*  Recent Date:  February 27, 2005
*
*********************************************************************************
*
*  Copyright ? 1998, 1999, 2000 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the Mxi source code by any other party than
*  Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE SetGlobalInfo( an_ParentItemDbId IN typn_DbId,
                         an_ParentItemId IN typn_Id,
                         on_Return OUT typn_RetCode )
IS
BEGIN

   -- Initialize the return value
   on_Return := icn_NoProc;

   -- Determine the owner, the location, the installation and received
   -- dates, the inventory condition and the root identifier.
   SELECT loc_db_id,
          loc_id,
          owner_db_id,
          owner_id,
          install_dt,
          install_gdt,
          received_dt,
          manufact_dt,
          h_inv_no_db_id,
          h_inv_no_id,
          assmbl_inv_no_db_id,
          assmbl_inv_no_id,
          locked_bool,
          prevent_synch_bool
     INTO il_LocationDbId,
          il_LocationId,
          il_OwnerDbId,
          il_OwnerId,
          idt_InstallDt,
          idt_InstallGDt,
          idt_ReceivedDt,
          idt_ManufactDt,
          il_RootDbId,
          il_RootId,
          il_AssmblInvNoDbId,
          il_AssmblInvNoId,
          ib_LockedBool,
          il_PreventSynchBool
     FROM inv_inv
    WHERE inv_no_db_id = an_ParentItemDbId
      AND inv_no_id    = an_ParentItemId
      AND rstat_cd = 0;

   -- Return success
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_CREATE_PKG@@@SetGlobalInfo@@@' || SQLERRM);
      RETURN;
END SetGlobalInfo;

/*----------------------- End of Package -----------------------------------*/
END INV_CREATE_PKG;
/