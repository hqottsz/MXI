--liquibase formatted sql


--changeSet DEV-1049:1 stripComments:false
-- update all batch inventory description to the following format:
-- [Part Name] (PN: [Part No], BN: [Batch No])
UPDATE inv_inv
SET
   inv_inv.inv_no_sdesc = 
   (
        SELECT
            eqp_part_no.part_no_sdesc || ' (PN: ' || eqp_part_no.part_no_oem || ', BN: ' || inv_inv.serial_no_oem || ')'
        FROM
            eqp_part_no
        WHERE
            eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
            eqp_part_no.part_no_id    = inv_inv.part_no_id
     )
WHERE
    inv_inv.inv_class_cd = 'BATCH';

--changeSet DEV-1049:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY Inv_Desc_Pkg IS

/* instance variables */
ii_ErrNo integer;
is_ErrMsg    char(100);
is_ACFT  ref_inv_class.inv_class_cd%TYPE := 'ACFT';
is_SER ref_inv_class.inv_class_cd%TYPE := 'SER';
is_KIT ref_inv_class.inv_class_cd%TYPE := 'KIT';
is_ASSY ref_inv_class.inv_class_cd%TYPE := 'ASSY';
is_SYS   ref_inv_class.inv_class_cd%TYPE := 'SYS';
is_TRK   ref_inv_class.inv_class_cd%TYPE := 'TRK';
is_BATCH  ref_inv_class.inv_class_cd%TYPE := 'BATCH';
is_InvDesc   inv_inv.inv_no_sdesc%TYPE;
ii_MaxDesc   integer := 400;

/* procedure prototypes */
PROCEDURE BuildInvDesc (
                 as_SerialNo    IN inv_inv.serial_no_oem%TYPE,
                 as_LotNo       IN inv_inv.lot_oem_tag%TYPE,
                 as_BatchNo     IN inv_inv.batch_no_oem%TYPE,
                 as_ACRegCd     IN inv_ac_reg.ac_reg_cd%TYPE,
                 as_InvClassCd  IN inv_inv.inv_class_cd%TYPE,
                   as_PartNoOem   IN eqp_part_no.part_no_oem%TYPE,
                 as_PartDesc    IN eqp_part_no.part_no_sdesc%TYPE,
                 al_AssyDBId    IN inv_inv.assmbl_db_id%TYPE,
                 as_AssyCd      IN inv_inv.assmbl_cd%TYPE,
                 al_AssyBOMId   IN inv_inv.assmbl_bom_id%TYPE,
                 al_AssyPosId   IN inv_inv.assmbl_pos_id%TYPE,
                 al_NhInvNoDbId IN inv_inv.nh_inv_no_db_id%TYPE,
                 af_BinQt       IN inv_inv.bin_qt%TYPE,
                 al_Return      OUT NUMBER);


/********************************************************************************
*
*  Procedure:    InvUpdInvDesc
*  Arguments:    al_InvDbId (long)    - inventory item whose desc to be updated.
*                al_InvId (long)      - ""
*                as_InvSdesc (string) - new inventory description.
*  Return:       al_Return long  - > 0 to indicate success
*  Description:  This procedure updates an inventory item's description based on
*                its current properties in the database.
*
*  Orig. Coder:  ??
*  Recent Coder: cjb
*  Recent Date:  February 24, 2005
*
*  DEP_OPS (RSTAT_DONE)
*********************************************************************************
*
*  Copyright ? 2003 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InvUpdInvDesc (
      al_InvDbId   IN inv_inv.inv_no_db_id%TYPE,
      al_InvId     IN inv_inv.inv_no_id%TYPE,
      as_InvSdesc  OUT inv_inv.inv_no_sdesc%TYPE,
      al_Return    OUT NUMBER
   ) IS

       -- local variables
       ls_SerialNo    inv_inv.serial_no_oem%TYPE;
       ls_LotNo       inv_inv.lot_oem_tag%TYPE;
       ls_BatchNo     inv_inv.batch_no_oem%TYPE;
       ls_ACRegCd     inv_ac_reg.ac_reg_cd%TYPE;
       ls_InvClassCd  inv_inv.inv_class_cd%TYPE;
       ls_PartNoOem   eqp_part_no.part_no_oem%TYPE;
       ls_PartDesc    eqp_part_no.part_no_sdesc%TYPE;
       ll_AssyDBId    inv_inv.assmbl_db_id%TYPE;
       ls_AssyCd      inv_inv.assmbl_cd%TYPE;
       ll_AssyBOMId   inv_inv.assmbl_bom_id%TYPE;
       ll_AssyPosId   inv_inv.assmbl_pos_id%TYPE;
       ll_InvNoDbId   inv_inv.inv_no_db_id%TYPE;
       ll_InvNoId     inv_inv.inv_no_id%TYPE;
       ll_NhInvNoDbId inv_inv.nh_inv_no_db_id%TYPE;
       lf_BinQt       inv_inv.bin_qt%TYPE;

   BEGIN

      -- Initialize the return value
      al_Return := icn_NoProc;

      -- select arguments BuildInvDesc.

      SELECT inv.inv_class_cd,
              part.part_no_oem,
             part.part_no_sdesc,
             inv.inv_no_db_id,
             inv.inv_no_id,
             inv.nh_inv_no_db_id,
             inv.serial_no_oem,
             inv.lot_oem_tag,
             inv.batch_no_oem,
             inv.assmbl_db_id,
             inv.assmbl_cd,
             inv.assmbl_bom_id,
             inv.assmbl_pos_id,
             inv.bin_qt,
             ac.ac_reg_cd
        INTO
             ls_InvClassCd,
             ls_PartNoOem,
             ls_PartDesc,
             ll_InvNoDbId,
             ll_InvNoId,
             ll_NhInvNoDbId,
             ls_SerialNo,
             ls_LotNo,
             ls_BatchNo,
             ll_AssyDBId,
             ls_AssyCd,
             ll_AssyBOMId,
             ll_AssyPosId,
             lf_BinQt,
             ls_ACRegCd
        FROM inv_ac_reg ac,
             inv_inv inv,
             eqp_part_no part
       WHERE inv.inv_no_db_id = al_InvDbId AND
             inv.inv_no_id    = al_InvId
         AND ac.inv_no_db_id (+)= inv.inv_no_db_id AND
             ac.inv_no_id    (+)= inv.inv_no_id
         AND
             inv.rstat_cd	= 0
         AND part.part_no_db_id (+)= inv.part_no_db_id AND
             part.part_no_id    (+)= inv.part_no_id;

      -- Build the new Inventory Description
      BuildInvDesc ( ls_SerialNo,
                     ls_LotNo,
                     ls_BatchNo,
                     ls_ACRegCd,
                     ls_InvClassCd,
                     ls_PartNoOem,
                     ls_PartDesc,
                     ll_AssyDBId,
                     ls_AssyCd,
                     ll_AssyBOMId,
                     ll_AssyPosId,
                     ll_NhInvNoDbId,
                     lf_BinQt,
                     al_Return );
      IF al_Return < 0 THEN
         RETURN;
      END IF;

      -- update the inventory record
      UPDATE inv_inv
         SET inv_no_sdesc = is_InvDesc
       WHERE inv_no_db_id = ll_InvNoDbId AND
             inv_no_id    = ll_InvNoId;

      -- Set the return argument
      as_InvSdesc := is_InvDesc;

      -- Set the return value
      al_Return := icn_Success;

EXCEPTION
    WHEN OTHERS THEN
        al_Return := -100;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_DESC_PKG@@@InvUpdInvDesc failed@@@' || SQLERRM) ;
        RETURN;
END InvUpdInvDesc;



/********************************************************************************
*
*  Procedure:    InvUpdSysDesc
*  Arguments:    al_AssmblDbId (long)  - config slot info to retrieve the
*                                        inventory records
*                al_AssmblCd (string)  - ""
*                al_AssmblBomId (long) - ""
*  Return:       al_Return long  - > 0 to indicate success
*  Description:  This procedure updates SYS inventory items' description based on
*                its current properties in the database.
*
*  Orig. Coder:  ysotozaki
*  Recent Coder:
*  Recent Date:  2006-05-02
*
*********************************************************************************
*
*  Copyright ? 2006 Mxi Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE InvUpdSysDesc (
      al_AssmblDbId   IN eqp_assmbl_bom.assmbl_db_id%TYPE,
      al_AssmblCd     IN eqp_assmbl_bom.assmbl_cd%TYPE,
      al_AssmblBomId  IN eqp_assmbl_bom.assmbl_bom_id%TYPE,
      al_Return       OUT NUMBER
   ) IS

      -- find all inventory that occupy the specified config slot.
      CURSOR lcur_InventoryRecords (
         al_AssmblDbId   IN eqp_assmbl_bom.assmbl_db_id%TYPE,
         al_AssmblCd     IN eqp_assmbl_bom.assmbl_cd%TYPE,
         al_AssmblBomId  IN eqp_assmbl_bom.assmbl_bom_id%TYPE
      ) IS
         SELECT inv_inv.inv_no_db_id,
                inv_inv.inv_no_id
           FROM inv_inv
          WHERE inv_inv.assmbl_db_id   = al_AssmblDbId   AND
                inv_inv.assmbl_cd      = al_AssmblCd     AND
                inv_inv.assmbl_bom_id  = al_AssmblBomId
                AND
                inv_inv.inv_class_cd   = 'SYS'
                AND
            inv_inv.rstat_cd	= 0;
      lrec_InventoryRecords   lcur_InventoryRecords%ROWTYPE;

      -- local variables
      ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
      ll_InvNoId    inv_inv.inv_no_id%TYPE;
      ls_InvNoSdesc inv_inv.inv_no_sdesc%TYPE;
      ll_return     NUMBER;

   BEGIN

      -- Initialize the return value
      al_Return := icn_NoProc;
      ll_return := 0;

      -- Call the description update procedure for each inventory found
      FOR lrec_InventoryRecords IN lcur_InventoryRecords(al_AssmblDbId, al_AssmblCd, al_AssmblBomId)
      LOOP
         InvUpdInvDesc(lrec_InventoryRecords.inv_no_db_id, lrec_InventoryRecords.inv_no_id, ls_InvNoSdesc, al_Return);
         ll_return := ll_return + al_Return;
      END LOOP;

      IF al_Return < 0 THEN
         RETURN;
      END IF;

      -- Set the return value
      al_Return := icn_Success;

EXCEPTION
    WHEN OTHERS THEN
        al_Return := -100;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_DESC_PKG@@@InvUpdInvDesc failed@@@' || SQLERRM) ;
        RETURN;
END InvUpdSysDesc;



/********************************************************************************
*
*  Procedure:    BuildInvDesc
*  Arguments:    as_SerialNo (string)   - inventory item's serial number.
*                as_LotNo (string)      - inventory item's lot number.
*                as_BatchNo (string)    - inventory item's batch number.
*                as_ACRegCd (string)    - inventory item's aircraft registration.
*                as_InvClassCd (string) - inventory class code.
*                  as_PartNoOem (string)  - inventory's oem part number.
*                as_PartDesc (string)   - description of inventory's part number.
*                al_AssyDbId (long)     - inventory item's assembly key.
*                as_AssyCd (string)     - ""
*                al_AssyBOMId (long)    - inventory item's BOM code.
*                al_AssyPosId (long)    - inventory item's BOM item position.
*                al_NhInvNoDbId (long)  - inventory item's parent identifier.
*                af_BinQt (float)       - inventory item's BIN quantity.
*  Return:       al_Return long  - > 0 to indicate success
*  Description:  This procedure builds a description of the current inventory
*                item based on the arguments provided.
*
*  Orig. Coder:  ??
*  Recent Coder: cjb
*  Recent Date:  February 24, 2005
*
*********************************************************************************
*
*  Copyright ? 2003 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE BuildInvDesc (
      as_SerialNo    IN inv_inv.serial_no_oem%TYPE,
      as_LotNo       IN inv_inv.lot_oem_tag%TYPE,
      as_BatchNo     IN inv_inv.batch_no_oem%TYPE,
      as_ACRegCd     IN inv_ac_reg.ac_reg_cd%TYPE,
      as_InvClassCd  IN inv_inv.inv_class_cd%TYPE,
      as_PartNoOem   IN eqp_part_no.part_no_oem%TYPE,
      as_PartDesc    IN eqp_part_no.part_no_sdesc%TYPE,
      al_AssyDbId    IN inv_inv.assmbl_db_id%TYPE,
      as_AssyCd      IN inv_inv.assmbl_cd%TYPE,
      al_AssyBOMId   IN inv_inv.assmbl_bom_id%TYPE,
      al_AssyPosId   IN inv_inv.assmbl_pos_id%TYPE,
      al_NhInvNoDbId IN inv_inv.nh_inv_no_db_id%TYPE,
      af_BinQt       IN inv_inv.bin_qt%TYPE,
      al_Return      OUT NUMBER
   ) IS

   -- local variables
   li_Total       integer;
   ls_PosDesc     inv_inv.inv_no_sdesc%TYPE;
   ls_Dash        inv_inv.inv_no_sdesc%TYPE := ' - ';
   ls_PN          inv_inv.inv_no_sdesc%TYPE := ' (PN: ';
   ls_SN          inv_inv.inv_no_sdesc%TYPE := ', SN: ';
   ls_BN          inv_inv.inv_no_sdesc%TYPE := ', BN: ';
   ls_AssyBomName eqp_assmbl_bom.assmbl_bom_name%TYPE;
   ls_AssyBomCd   eqp_assmbl_bom.assmbl_bom_cd%TYPE;
   li_PosNumber   integer;

   ls_nlsNumericCharacters VARCHAR2(100);

BEGIN

   -- Initialize the return value
   al_Return := icn_NoProc;

   -- If the inventory is of type "TRK", "ASSY", "ACFT", or "SYS"
   IF as_InvClassCd = is_TRK OR
      as_InvClassCd = is_ASSY OR
      as_InvClassCd = is_ACFT OR
      as_InvClassCd = is_SYS THEN

      -- select number of possible positions for the assembly
      SELECT pos_ct,
             assmbl_bom_name,
             assmbl_bom_cd
        INTO li_PosNumber,
             ls_AssyBomName,
             ls_AssyBomCd
        FROM EQP_ASSMBL_BOM
       WHERE assmbl_db_id   = al_AssyDBId
         AND assmbl_cd      = as_AssyCd
         AND assmbl_bom_id  = al_AssyBOMId;

       -- If there is more than one possible position,
       -- and if the inventory is not a root
       -- Note that if the inventory item is a root (ie has no parent), it
       -- should not have a position
       IF li_PosNumber > 1 AND al_NhInvNoDbId IS NOT NULL THEN

          -- select description for the specified assembly position
          SELECT eqp_pos_cd
            INTO ls_PosDesc
            FROM eqp_assmbl_pos
           WHERE assmbl_db_id  = al_AssyDBId
             AND assmbl_cd     = as_AssyCd
             AND assmbl_bom_id = al_AssyBOMId
             AND assmbl_pos_id = al_AssyPosId;

          ls_PosDesc := '(' || ls_PosDesc || ') ';
       ELSE
          ls_PosDesc := '';
       END IF;

   ELSE
      ls_PosDesc := '';
   END IF;

   /* Based on the Equipment classification code create a description for the
      inventory record.  The inventory description field is only 80 characters
      so some of the information may have to get trucated. */

   -- If the inventory is an aircraft
   IF as_InvClassCd = is_ACFT THEN

      --Format: <part_no_sdesc> + ' - ' + <ac_reg_cd>
      --Check total string lengths
      li_Total  := LENGTH(as_PartDesc) + LENGTH(ls_Dash) + LENGTH(as_ACRegCd);

      --Create the description string
      IF li_Total > ii_MaxDesc THEN
         is_InvDesc := SUBSTR(as_PartDesc, 0, LENGTH(as_PartDesc) -
                      (li_Total - ii_MaxDesc));
      ELSE
         is_InvDesc := as_PartDesc;
      END IF;
      is_InvDesc := is_InvDesc || ls_Dash || as_ACRegCd;

   -- If the inventory is a Tracked Assembly or Part
   ELSIF as_InvClassCd = is_TRK OR as_InvClassCd = is_ASSY THEN
      --Format: <part_no_sdesc> (PN: <part_no_oem>, SN: <serial_no_oem>)
      --Additional:  (<eqp_pos_cd or assmbl_pos_id>) if pos_ct > 1
      --Get additional position information if there is more than one

      --Check total string lengths
      --Note that the LENGTH function will fail if the string is null;
      --since the ls_PosDesc may sometimes be null, we must look at two cases.
      IF ( ls_PosDesc IS NULL ) THEN
         li_Total := LENGTH(as_PartDesc) + LENGTH(ls_PN) + LENGTH(as_PartNoOem) + LENGTH(ls_SN) + LENGTH(as_SerialNo) + LENGTH(')');
      ELSE
         li_Total  := LENGTH(ls_PosDesc) + LENGTH(as_PartDesc) +
                  LENGTH(ls_PN) + LENGTH(as_PartNoOem) + LENGTH(ls_SN) + LENGTH(as_SerialNo) + LENGTH(')');
      END IF;

      --Create the description string
      IF li_Total > ii_MaxDesc THEN
         is_InvDesc := SUBSTR(as_PartDesc, 0, LENGTH(as_PartDesc) -
                       (li_Total - ii_MaxDesc));
      ELSE
         is_InvDesc := as_PartDesc;
      END IF;
      is_InvDesc := ls_PosDesc || is_InvDesc || ls_PN || as_PartNoOem || ls_SN || as_SerialNo || ')';

      --Now we must determine if the short description is too long; if it is, it
      --must be truncated.
      IF LENGTH(is_InvDesc) > ii_MaxDesc THEN
         is_InvDesc := SUBSTR(is_InvDesc, 0, ii_MaxDesc);
      END IF;

   -- If the inventory is a "Phantom" System
   ELSIF as_InvClassCd = is_SYS THEN
      --Format: <assmbl_bom_cd> + '(' + <assmbl_bom_name> + ')'
      --Additional:  + ' (' + <eqp_pos_cd or assmbl_pos_id> + ')' if pos_ct > 1
      --Get additional position information if there is more than one

         is_InvDesc := ls_PosDesc || ls_AssyBomCd || ' - ' || ls_AssyBomName;

   -- If the inventory is SER controlled
   ELSIF as_InvClassCd = is_SER OR
         as_InvClassCd = is_KIT THEN

      /*
       * Default format: <part_no_sdesc> (PN: <part_no_oem>, SN: <serial_no_oem>)
       */

      -- build the description
      is_InvDesc := as_PartDesc || ls_PN || as_PartNoOem || ls_SN || as_SerialNo || ')';

   -- If the inventory is BATCH controlled
   ELSIF as_InvClassCd = is_BATCH THEN

      /*
       * Default format: <part_no_sdesc> (PN: <part_no_oem>, SN: <serial_no_oem>)
       */

      -- Append the batch control number
      is_InvDesc := as_PartDesc || ls_PN || as_PartNoOem || ls_BN || as_SerialNo || ')';

   END IF;

   -- Set the return value
   al_Return := icn_Success;

EXCEPTION
    WHEN OTHERS THEN
        al_Return := -100;
        APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_DESC_PKG@@@BuildInvDesc failed@@@' || SQLERRM) ;
        RETURN;
END BuildInvDesc;

/************************************************************************
*
*
* Package:      GetAssmblInvSdesc
* Arguments:    an_AssmblDbId   (long) - assembly database id
*               as_AssmblCd   (string) - assembly code
*               an_AssmblBomId  (long) - assembly BOM id
*               an_AssmblPosId  (long) - assembly position id
* Return:       on_InvNoSdesc (string) - inventory description
*               on_Return       (long) - return code of procedure
* Description:  Generate inventory description for UNTRK inventory,
*               SYS inventory, and holes.
*
* Orig Coder:   H. Strutt
* Recent Coder: Chris Brouse
* Recent Date:  August 24, 2001
*
*************************************************************************
*
* Copyright 1998-2001 Mxi Technologies Ltd. All Rights Reserved.
* Any distribution of Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*************************************************************************
*/
PROCEDURE GetAssmblInvDesc(
          an_AssmblDbId  IN  inv_inv.assmbl_db_id%TYPE,
          as_AssmblCd    IN  inv_inv.assmbl_cd%TYPE,
          an_AssmblBomId IN  inv_inv.assmbl_bom_id%TYPE,
          an_AssmblPosId IN  inv_inv.assmbl_pos_id%TYPE,
          os_InvNoSdesc  OUT inv_inv.inv_no_sdesc%TYPE,
          on_Return      OUT NUMBER) IS

      /* declare local variables */
      ls_InvNoSdesc     inv_inv.inv_no_sdesc%TYPE;

      /* declare inventory cursor */
      CURSOR lcur_Inventory (
             an_AssmblDbId  IN  inv_inv.assmbl_db_id%TYPE,
             as_AssmblCd    IN  inv_inv.assmbl_cd%TYPE,
             an_AssmblBomId IN  inv_inv.assmbl_bom_id%TYPE,
             an_AssmblPosId IN  inv_inv.assmbl_pos_id%TYPE
             ) IS
         SELECT eqp_assmbl_pos.eqp_pos_cd,
                eqp_assmbl_bom.assmbl_bom_cd,
                eqp_assmbl_bom.assmbl_bom_name,
                eqp_assmbl_bom.pos_ct
           FROM eqp_assmbl_pos,
                eqp_assmbl_bom
          WHERE eqp_assmbl_pos.assmbl_db_id  = an_AssmblDbId
            AND eqp_assmbl_pos.assmbl_cd     = as_AssmblCd
            AND eqp_assmbl_pos.assmbl_bom_id = an_AssmblBomId
            AND eqp_assmbl_pos.assmbl_pos_id = an_AssmblPosId
            AND eqp_assmbl_bom.assmbl_db_id  = eqp_assmbl_pos.assmbl_db_id
            AND eqp_assmbl_bom.assmbl_cd     = eqp_assmbl_pos.assmbl_cd
            AND eqp_assmbl_bom.assmbl_bom_id = eqp_assmbl_pos.assmbl_bom_id;
      lrec_Inventory lcur_Inventory%ROWTYPE;

BEGIN

   /* open cursor */
   OPEN lcur_Inventory(an_AssmblDbId,
                       as_AssmblCd,
                       an_AssmblBomId,
                       an_AssmblPosId);
   FETCH lcur_Inventory INTO lrec_Inventory;
   CLOSE lcur_Inventory;


   -- build the inventory description string for holes, system and untracked
   -- inventory
   IF lrec_Inventory.pos_ct > 1 AND lrec_Inventory.eqp_pos_cd IS NOT NULL THEN
      ls_InvNoSdesc := '(' || lrec_Inventory.eqp_pos_cd || ')' ||
         lrec_Inventory.assmbl_bom_cd || ' - ' || lrec_Inventory.assmbl_bom_name;
   ELSIF lrec_Inventory.pos_ct > 1 AND lrec_Inventory.eqp_pos_cd IS NULL THEN
      ls_InvNoSdesc := '(' || TO_CHAR(an_AssmblPosId) || ')' ||
         lrec_Inventory.assmbl_bom_cd || ' - ' || lrec_Inventory.assmbl_bom_name;
   ELSE
      ls_InvNoSdesc := lrec_Inventory.assmbl_bom_cd || ' - ' ||
                       lrec_Inventory.assmbl_bom_name;
   END IF;

   /* assign  inventory description */
   os_InvNoSdesc := ls_InvNoSdesc;
   on_Return := icn_Success;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999', 'INV_DESC_PKG@@@GetAssmblInvDesc@@@' || SQLERRM);
      RETURN;
END GetAssmblInvDesc;



/********************************************************************************
*
* Procedure:    UpdateInvConfigPosDescInQueue
*
* Description:  This procedure retrieves all inventory in the queue table and
*               update their config position description
*
* Orig.Coder:     Elise Do
* Recent Coder:   Elise Do
* Recent Date:    July 17, 2009
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateInvConfigPosDescInQueue
(
     al_NumOfRows  IN NUMBER,
     on_Return  OUT NUMBER
)
IS

   -- local variable
   ln_InvNoDbId     inv_inv.inv_no_db_id%TYPE;
   ln_InvNoId       inv_inv.inv_no_id%TYPE;

    -- Dequeue inventory
    CURSOR lcurr_Inventory IS
      SELECT
          inv_no_db_id,
          inv_no_id
      FROM
          inv_pos_desc_queue
      WHERE
          ROWNUM <= al_NumOfRows;
    lRec_Inventory lcurr_Inventory%ROWTYPE;

BEGIN

     -- Update each item found in the Queue
     FOR lRec_Inventory IN lcurr_Inventory
     LOOP
          UpdateInvConfigPosDesc(
                lRec_Inventory.inv_no_db_id,
                lRec_Inventory.inv_no_id,
                on_Return
           );

           -- Delete this value.
           DELETE FROM inv_pos_desc_queue WHERE inv_no_db_id= lRec_Inventory.inv_no_db_id AND inv_no_id = lRec_Inventory.inv_no_id ;

     END LOOP;

     on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','inv_desc_pkg@@@UpdateInvConfigPosDescInQueue failed@@@'||SQLERRM);
      RETURN;

END UpdateInvConfigPosDescInQueue;



/********************************************************************************
*
* Procedure:    UpdateInvConfigPosDesc
*
* Description:  This procedure update the config position description for the
*               given inventory
*
*                When there is a Next Highest Inventory (other than the Highest)
*                 we display the Config Slot information of it
*
*                We always display the specified Inventory's Config Slot
*
*                If a Config Slot has more than one position or is a custom
*                 code, we display the Position as well
*
* Orig.Coder:     Elise Do
* Recent Coder:   Jonathan Clarkin
* Recent Date:    May 18, 2010
*
*********************************************************************************
*
* Copyright 2000-2009 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
PROCEDURE UpdateInvConfigPosDesc
(
     al_InvNoDbId   IN inv_inv.inv_no_db_id%TYPE,
     al_InvNoId     IN inv_inv.inv_no_id%TYPE,
     on_Return      OUT NUMBER
)
IS

      -- local variable
   ls_ConfigDesc    inv_inv.config_pos_sdesc%TYPE;


   CURSOR lCur_Inventory
   IS
      SELECT
         -- If the Inventory class is Serialized, Track, or Aircraft - set the description to Null
         CASE WHEN inv_inv.inv_class_cd IN ('SER', 'BATCH', 'ACFT', 'KIT')
              THEN NULL
              ELSE
                -- One of ASSY, TRK, SYS
                -- Display info about the parent assembly inventory, if the parent assembly inventory is not null and not the highest assembly inventory
                CASE WHEN ( inv_inv.assmbl_inv_no_db_id IS NULL
                            OR
                            ( nh_assmbl_inv.orig_assmbl_db_id = h_inv_inv.assmbl_db_id AND nh_assmbl_inv.orig_assmbl_cd = h_inv_inv.assmbl_cd )
                            OR
                            (inv_inv.assmbl_inv_no_db_id = al_InvNoDbId AND inv_inv.assmbl_inv_no_id = al_InvNoId ) )
                     THEN
                         NULL
                     ELSE
                         assy_eqp_assmbl_bom.assmbl_bom_cd
                         ||

                         -- Only show Position Info for NH Config Slot if: a) More than one position, b) Position Code is not the Default
                         CASE WHEN ( assy_eqp_assmbl_bom.pos_ct > 1 ) OR ( assy_eqp_assmbl_pos.eqp_pos_cd != '1' )
                              THEN ' (' || assy_eqp_assmbl_pos.eqp_pos_cd  || ') ' ||'->'
                         ELSE
                             CASE WHEN (assy_eqp_assmbl_bom.assmbl_bom_cd IS NOT NULL)
                                 THEN  '->'
                             END
                         END
              END
              ||
              -- Inventory Config Slot
              eqp_assmbl_bom.assmbl_bom_cd
              ||
              -- Only show Position Info for the Config Slot if: a) More than one position, b) Position Code is not the Default
              -- c) Inventory is not loose
              CASE WHEN ( inv_inv.nh_inv_no_db_id IS NOT NULL AND ( eqp_assmbl_bom.pos_ct > 1  OR  eqp_assmbl_pos.eqp_pos_cd != '1' ) )
                  THEN ' (' || eqp_assmbl_pos.eqp_pos_cd  || ')'
              END
         END AS config_desc
      FROM
         inv_inv
         INNER JOIN eqp_assmbl_bom ON
            eqp_assmbl_bom.assmbl_db_id   = inv_inv.assmbl_db_id AND
            eqp_assmbl_bom.assmbl_cd      = inv_inv.assmbl_cd AND
            eqp_assmbl_bom.assmbl_bom_id  = inv_inv.assmbl_bom_id
         INNER JOIN eqp_assmbl_pos ON
            eqp_assmbl_pos.assmbl_db_id   = inv_inv.assmbl_db_id AND
            eqp_assmbl_pos.assmbl_cd      = inv_inv.assmbl_cd AND
            eqp_assmbl_pos.assmbl_bom_id  = inv_inv.assmbl_bom_id AND
            eqp_assmbl_pos.assmbl_pos_id  = inv_inv.assmbl_pos_id
         LEFT OUTER JOIN inv_inv assy_inv_inv ON
            assy_inv_inv.inv_no_db_id    = inv_inv.assmbl_inv_no_db_id AND
            assy_inv_inv.inv_no_id       = inv_inv.assmbl_inv_no_id
         LEFT OUTER JOIN eqp_assmbl_bom assy_eqp_assmbl_bom ON
            assy_eqp_assmbl_bom.assmbl_db_id    = assy_inv_inv.assmbl_db_id AND
            assy_eqp_assmbl_bom.assmbl_cd       = assy_inv_inv.assmbl_cd AND
            assy_eqp_assmbl_bom.assmbl_bom_id   = assy_inv_inv.assmbl_bom_id
         LEFT OUTER JOIN eqp_assmbl_pos assy_eqp_assmbl_pos ON
            assy_eqp_assmbl_pos.assmbl_db_id    = assy_inv_inv.assmbl_db_id AND
            assy_eqp_assmbl_pos.assmbl_cd       = assy_inv_inv.assmbl_cd AND
            assy_eqp_assmbl_pos.assmbl_bom_id   = assy_inv_inv.assmbl_bom_id AND
            assy_eqp_assmbl_pos.assmbl_pos_id   = assy_inv_inv.assmbl_pos_id
         LEFT OUTER JOIN inv_inv nh_assmbl_inv ON
            nh_assmbl_inv.inv_no_db_id = inv_inv.assmbl_inv_no_db_id AND
            nh_assmbl_inv.inv_no_id    = inv_inv.assmbl_inv_no_id
         INNER JOIN inv_inv h_inv_inv ON
            h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
            h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
      WHERE
         inv_inv.inv_no_db_id = al_InvNoDbId AND
         inv_inv.inv_no_id    = al_InvNoId;
   lrec_Inventory lcur_Inventory%ROWTYPE;


BEGIN

   OPEN lCur_Inventory;
   FETCH lCur_Inventory into ls_ConfigDesc;

   IF lCur_Inventory%NOTFOUND THEN
      ls_ConfigDesc := NULL;
   END IF;

   CLOSE lCur_Inventory;

   -- update config slot description if the inventoryis not BATCH, SER or ACFT
   IF ls_ConfigDesc IS NOT NULL THEN
      UPDATE
         inv_inv
      SET
         config_pos_sdesc = ls_ConfigDesc
      WHERE
         inv_no_db_id = al_InvNoDbId AND
         inv_no_id    = al_InvNoId;
   END IF;

   on_Return := icn_Success;

EXCEPTION

   WHEN OTHERS THEN
      -- Unexpected error
      on_Return := icn_Error;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999','inv_desc_pkg@@@UpdateInvConfigPosDesc@@@'||SQLERRM);
      RETURN;

END UpdateInvConfigPosDesc;

END Inv_Desc_Pkg;
/