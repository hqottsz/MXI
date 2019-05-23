--liquibase formatted sql


--changeSet OPER-21343 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE Inv_Desc_Pkg IS
   --Procedure to create the Inventory description when the inventory record is first
   --created or whenrelevant attributes of the inventory record only changes,
   --e.g. new part number set.  It does not update the record.
   --Should be called from Before Insert and Before Update (on certain columns)
   --triggers on the INV_INV and INV_AC_REG tables.

/*------------------------------------ SUBTYPES ----------------------------*/
   -- Define a subtype for return codes
   SUBTYPE typn_RetCode IS NUMBER;
/*---------------------------------- Constants -----------------------------*/

   -- Basic error handling codes
   icn_Success CONSTANT typn_RetCode := 1;     -- Success
   icn_NoProc  CONSTANT typn_RetCode := 0;     -- No processing done
   icn_Error   CONSTANT typn_RetCode := -1;    -- Error

   -- Function that generates the inventory short description without additional database quering
   -- Used in bulk data migration and by core InvUpdInvDesc
   FUNCTION BuildInvDesc (
      -- Inventory
      as_InvClassCd  IN inv_inv.inv_class_cd%TYPE,
      al_NhInvNoDbId IN inv_inv.nh_inv_no_db_id%TYPE,
      as_SerialNo    IN inv_inv.serial_no_oem%TYPE,
      -- Aircraft
      as_ACRegCd     IN inv_ac_reg.ac_reg_cd%TYPE,
      -- Part
      as_PartNoOem   IN eqp_part_no.part_no_oem%TYPE,
      as_PartDesc    IN eqp_part_no.part_no_sdesc%TYPE,
      -- Assembly
      as_AssyBomName IN eqp_assmbl_bom.assmbl_bom_name%TYPE,
      as_AssyBomCd   IN eqp_assmbl_bom.assmbl_bom_cd%TYPE,
      an_PosNumber   IN eqp_assmbl_bom.pos_ct%TYPE,
      -- Position
      as_PosCd       IN eqp_assmbl_pos.eqp_pos_cd%TYPE
   )
   RETURN VARCHAR2;

   -- Function that generates the configuration position short description without additional database quering
   -- Used in bulk data migration and by core UpdateInvConfigPosDesc
   FUNCTION BuildInvConfigPosDesc
      (
      -- Inventory information
      av_InvClassCd     inv_inv.inv_class_cd%TYPE,
      an_InvNoDbId      inv_inv.inv_no_db_id%TYPE,
      an_InvNoId        inv_inv.inv_no_id%TYPE,
      an_NhInvNoDbId    inv_inv.inv_no_db_id%TYPE,
      an_AssyInvNoDbId  inv_inv.assmbl_inv_no_db_id%TYPE,
      an_AssyInvNoId    inv_inv.assmbl_inv_no_id%TYPE,
      an_HInvNoDbId     inv_inv.h_inv_no_db_id%TYPE,
      an_HInvNoId       inv_inv.h_inv_no_id%TYPE,
      -- Inventory slot and position info
      lv_InvBomCd       eqp_assmbl_bom.assmbl_bom_cd%TYPE,
      lv_InvPosCd       eqp_assmbl_pos.eqp_pos_cd%TYPE,
      ln_InvPosCt       eqp_assmbl_bom.pos_ct%TYPE,
      -- Assembly slot and position info
      lv_AssyBomCd      eqp_assmbl_bom.assmbl_bom_cd%TYPE,
      lv_AssyPoscd      eqp_assmbl_pos.eqp_pos_cd%TYPE,
      ln_AssyPosCt      eqp_assmbl_bom.pos_ct%TYPE,
      -- Next highest position info
      ln_NhPosCt        eqp_assmbl_bom.pos_ct%TYPE
      )
   RETURN VARCHAR2;

   --Procedure to update the Inventory description column of an Inventory record
   --after an item has been created, duplicated or edited by the user.
   PROCEDURE InvUpdInvDesc (
      al_InvDBId   IN  inv_inv.inv_no_db_id%TYPE,
      al_InvId     IN  inv_inv.inv_no_id%TYPE,
      as_InvSdesc  OUT inv_inv.inv_no_sdesc%TYPE,
      al_Return    OUT NUMBER);

   --Procedure to update the Inventory description column of inventory records
   --defined by config slot
   PROCEDURE InvUpdSysDesc (
      al_AssmblDbId   IN  eqp_assmbl_bom.assmbl_db_id%TYPE,
      al_AssmblCd     IN  eqp_assmbl_bom.assmbl_cd%TYPE,
      al_AssmblBomId  IN  eqp_assmbl_bom.assmbl_bom_id%TYPE,
      al_Return       OUT NUMBER);

   --Procedure to update the Inventory description column for Untracked
   --inventory, SYSTEM inventory and holes
   PROCEDURE GetAssmblInvDesc(
      an_AssmblDbId  IN  inv_inv.assmbl_db_id%TYPE,
      as_AssmblCd    IN  inv_inv.assmbl_cd%TYPE,
      an_AssmblBomId IN  inv_inv.assmbl_bom_id%TYPE,
      an_AssmblPosId IN  inv_inv.assmbl_pos_id%TYPE,
      os_InvNoSdesc  OUT inv_inv.inv_no_sdesc%TYPE,
      on_Return      OUT NUMBER);
   --May eventually want another procedure that will do the same if Assembly
   --records change.  Do not do for the time being.

   --Procedure to update the Inventory config slot position description that are
   --in the queue table
   PROCEDURE UpdateInvConfigPosDescInQueue(
      al_NumOfRows IN  NUMBER,
      on_Return    OUT NUMBER );

   --Procedure to update the Inventory config slot position description that are
   --in the queue table
   PROCEDURE UpdateInvConfigPosDesc(
      al_InvNoDbId   	IN  inv_inv.inv_no_db_id%TYPE,
      al_InvNoId     	IN  inv_inv.inv_no_id%TYPE,
      on_Return  	OUT NUMBER );

END Inv_Desc_Pkg;
/

CREATE OR REPLACE PACKAGE BODY Inv_Desc_Pkg IS

/* instance constants */
is_ACFT      CONSTANT ref_inv_class.inv_class_cd%TYPE := 'ACFT';
is_SER       CONSTANT ref_inv_class.inv_class_cd%TYPE := 'SER';
is_KIT       CONSTANT ref_inv_class.inv_class_cd%TYPE := 'KIT';
is_ASSY      CONSTANT ref_inv_class.inv_class_cd%TYPE := 'ASSY';
is_SYS       CONSTANT ref_inv_class.inv_class_cd%TYPE := 'SYS';
is_TRK       CONSTANT ref_inv_class.inv_class_cd%TYPE := 'TRK';
is_BATCH     CONSTANT ref_inv_class.inv_class_cd%TYPE := 'BATCH';
ls_Dash      CONSTANT inv_inv.inv_no_sdesc%TYPE := ' - ';
ls_PN        CONSTANT inv_inv.inv_no_sdesc%TYPE := ' (PN: ';
ls_SN        CONSTANT inv_inv.inv_no_sdesc%TYPE := ', SN: ';
ls_BN        CONSTANT inv_inv.inv_no_sdesc%TYPE := ', BN: ';
ii_MaxDesc   CONSTANT INTEGER := 400;

/********************************************************************************
*
*  Function:     BuildInvDesc
*  Arguments:    as_InvClassCd  (string) IN inventory class code
*                al_NhInvNoDbId (number) IN next highest inventory db id
*                as_SerialNo    (string) IN inventory serial number
*                as_ACRegCd     (string) IN aircraft registration code
*                as_PartNoOem   (string) IN inventory part number
*                as_PartDesc    (string) IN inventory part description
*                as_AssyBomName (string) IN inventory configuration slot name
*                as_AssyBomCd   (string) IN inventory configuration slot code
*                an_PosNumber   (number) IN inventory configuration postion count
*                as_PosCd       (string) IN inventory position code
*  Return:       VARCHAR2 inventory short description
*  Description:  This procedure builds a description of the current inventory
*                item based on the arguments provided.
*
*  Orig. Coder:  ??
*  Recent Coder: Guillaume Turcotte
*  Recent Date:  April 16, 2018
*
*********************************************************************************
*
*  Copyright 2018 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/
FUNCTION BuildInvDesc (
      -- Inventory
      as_InvClassCd  IN inv_inv.inv_class_cd%TYPE,
      al_NhInvNoDbId IN inv_inv.nh_inv_no_db_id%TYPE,
      as_SerialNo    IN inv_inv.serial_no_oem%TYPE,
      -- Aircraft
      as_ACRegCd     IN inv_ac_reg.ac_reg_cd%TYPE,
      -- Part
      as_PartNoOem   IN eqp_part_no.part_no_oem%TYPE,
      as_PartDesc    IN eqp_part_no.part_no_sdesc%TYPE,
      -- Assembly
      as_AssyBomName IN eqp_assmbl_bom.assmbl_bom_name%TYPE,
      as_AssyBomCd   IN eqp_assmbl_bom.assmbl_bom_cd%TYPE,
      an_PosNumber   IN eqp_assmbl_bom.pos_ct%TYPE,
      -- Position
      as_PosCd       IN eqp_assmbl_pos.eqp_pos_cd%TYPE
   )
   RETURN VARCHAR2
   IS

   -- local variables
   lv_InvSdesc    inv_inv.inv_no_sdesc%TYPE;
   ls_PosCd       inv_inv.inv_no_sdesc%TYPE;
   li_Total       NUMBER;

BEGIN

   -- Installed inventory that can have multiple positions should have the position code included
   IF an_PosNumber > 1 AND al_NhInvNoDbId IS NOT NULL AND
      as_InvClassCd IN (is_TRK, is_ASSY, is_ACFT, is_SYS)
      THEN
      ls_PosCd := '(' || as_PosCd || ') ';
   ELSE
      ls_PosCd := '';
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
         lv_InvSdesc := SUBSTR(as_PartDesc, 0, LENGTH(as_PartDesc) -
                      (li_Total - ii_MaxDesc));
      ELSE
         lv_InvSdesc := as_PartDesc;
      END IF;
      lv_InvSdesc := lv_InvSdesc || ls_Dash || as_ACRegCd;

   -- If the inventory is a Tracked Assembly or Part
   ELSIF as_InvClassCd = is_TRK OR as_InvClassCd = is_ASSY THEN
      --Format: <part_no_sdesc> (PN: <part_no_oem>, SN: <serial_no_oem>)
      --Additional:  (<eqp_pos_cd or assmbl_pos_id>) if pos_ct > 1
      --Get additional position information if there is more than one

      --Check total string lengths
      --Note that the LENGTH function will fail if the string is null;
      --since the ls_PosCd may sometimes be null, we must look at two cases.
      IF ( ls_PosCd IS NULL ) THEN
         li_Total := LENGTH(as_PartDesc) + LENGTH(ls_PN) + LENGTH(as_PartNoOem) + LENGTH(ls_SN) + LENGTH(as_SerialNo) + LENGTH(')');
      ELSE
         li_Total  := LENGTH(ls_PosCd) + LENGTH(as_PartDesc) +
                  LENGTH(ls_PN) + LENGTH(as_PartNoOem) + LENGTH(ls_SN) + LENGTH(as_SerialNo) + LENGTH(')');
      END IF;

      --Create the description string
      IF li_Total > ii_MaxDesc THEN
         lv_InvSdesc := SUBSTR(as_PartDesc, 0, LENGTH(as_PartDesc) -
                       (li_Total - ii_MaxDesc));
      ELSE
         lv_InvSdesc := as_PartDesc;
      END IF;
      lv_InvSdesc := ls_PosCd || lv_InvSdesc || ls_PN || as_PartNoOem || ls_SN || as_SerialNo || ')';

      --Now we must determine if the short description is too long; if it is, it
      --must be truncated.
      IF LENGTH(lv_InvSdesc) > ii_MaxDesc THEN
         lv_InvSdesc := SUBSTR(lv_InvSdesc, 0, ii_MaxDesc);
      END IF;

   -- If the inventory is a "Phantom" System
   ELSIF as_InvClassCd = is_SYS THEN
      --Format: <assmbl_bom_cd> + '(' + <assmbl_bom_name> + ')'
      --Additional:  + ' (' + <eqp_pos_cd or assmbl_pos_id> + ')' if pos_ct > 1
      --Get additional position information if there is more than one

         lv_InvSdesc := ls_PosCd || as_AssyBomCd || ' - ' || as_AssyBomName;

   -- If the inventory is SER controlled
   ELSIF as_InvClassCd = is_SER OR
         as_InvClassCd = is_KIT THEN

      /*
       * Default format: <part_no_sdesc> (PN: <part_no_oem>, SN: <serial_no_oem>)
       */

      -- build the description
      lv_InvSdesc := as_PartDesc || ls_PN || as_PartNoOem || ls_SN || as_SerialNo || ')';

   -- If the inventory is BATCH controlled
   ELSIF as_InvClassCd = is_BATCH THEN

      /*
       * Default format: <part_no_sdesc> (PN: <part_no_oem>, SN: <serial_no_oem>)
       */

      -- Append the batch control number
      lv_InvSdesc := as_PartDesc || ls_PN || as_PartNoOem || ls_BN || as_SerialNo || ')';

   END IF;

   -- Return the code
   RETURN lv_InvSdesc;


END BuildInvDesc;

/********************************************************************************
*
*  Function:     BuildInvConfigPosDesc
*  Arguments:    av_InvClassCd    (string) IN inventory class code
*                an_InvNoDbId     (number) IN inventory key
*                an_InvNoId       (number) IN inventory key
*                an_NhInvNoDbId   (number) IN inventory next highest key - used as NULL/NOT NULL Boolean
*                an_AssyInvNoDbId (number) IN inventory assembly key
*                an_AssyInvNoId   (number) IN inventory assembly key
*                an_HInvNoDbId    (number) IN inventory highest key
*                an_HInvNoId      (number) IN inventory highest key
*                lv_InvBomCd      (string) IN inventory configuration slot code
*                lv_InvPosCd      (string) IN inventory positino code
*                ln_InvPosCt      (number) IN configuration slot position count
*                lv_AssyBomCd     (string) IN assembly configuration slot code
*                lv_AssyPoscd     (string) IN assembly position code
*                ln_AssyPosCt     (number) IN assembly configuration slot position count
*                ln_NhPosCt       (number) IN next highest configuration slot position count
*  Return:       VARCHAR2 configuration position short description
*  Description:  This procedure builds a description of the current inventory
*                item based on the arguments provided.
*
*  Orig. Coder:  Guillaume Turcotte
*  Recent Coder: 
*  Recent Date:  April 16, 2018
*
*********************************************************************************
*
*  Copyright 2018 MxI Technologies Ltd.  All Rights Reserved.
*  Any distribution of the MxI source code by any other party than
*  MxI Technologies Ltd is prohibited.
*
*********************************************************************************/

FUNCTION BuildInvConfigPosDesc
   (
   -- Inventory information
   av_InvClassCd     inv_inv.inv_class_cd%TYPE,
   an_InvNoDbId      inv_inv.inv_no_db_id%TYPE,
   an_InvNoId        inv_inv.inv_no_id%TYPE,
   an_NhInvNoDbId    inv_inv.inv_no_db_id%TYPE,
   an_AssyInvNoDbId  inv_inv.assmbl_inv_no_db_id%TYPE,
   an_AssyInvNoId    inv_inv.assmbl_inv_no_id%TYPE,
   an_HInvNoDbId     inv_inv.h_inv_no_db_id%TYPE,
   an_HInvNoId       inv_inv.h_inv_no_id%TYPE,
   -- Inventory slot and position info
   lv_InvBomCd       eqp_assmbl_bom.assmbl_bom_cd%TYPE,
   lv_InvPosCd       eqp_assmbl_pos.eqp_pos_cd%TYPE,
   ln_InvPosCt       eqp_assmbl_bom.pos_ct%TYPE,
   -- Assembly slot and position info
   lv_AssyBomCd      eqp_assmbl_bom.assmbl_bom_cd%TYPE,
   lv_AssyPoscd      eqp_assmbl_pos.eqp_pos_cd%TYPE,
   ln_AssyPosCt      eqp_assmbl_bom.pos_ct%TYPE,
   -- Next highest position info
   ln_NhPosCt        eqp_assmbl_bom.pos_ct%TYPE
   )
RETURN VARCHAR2
IS

   lv_ConfigDesc     inv_inv.config_pos_sdesc%TYPE;

BEGIN

   lv_ConfigDesc := 
         -- If the Inventory class is Serialized, Track, or Aircraft - set the description to Null
         CASE WHEN av_InvClassCd IN ('SER', 'BATCH', 'ACFT', 'KIT')
              THEN NULL
              ELSE
                -- One of ASSY, TRK, SYS
                -- Display info about the parent assembly inventory, if the parent assembly inventory is not null and not the highest assembly inventory
                CASE WHEN ( an_AssyInvNoDbId IS NULL
                            OR
                            (an_AssyInvNoDbId = an_HInvNoDbId AND an_AssyInvNoId = an_HInvNoId )
                            OR
                            (an_AssyInvNoDbId = an_InvNoDbId AND an_AssyInvNoId = an_InvNoId ) )
                     THEN
                         NULL
                     ELSE
                         lv_AssyBomCd
                         ||

                         -- Only show Position Info for NH Config Slot if: 
                         -- a) More than one position 
                         -- b) Parent has more than one position
                         -- c) Position Code is not the Default
                         CASE WHEN ( ln_AssyPosCt > 1 ) OR ( ln_NhPosCt > 1 ) OR ( lv_AssyPoscd != '1' )
                              THEN ' (' || lv_AssyPoscd  || ') ' ||'->'
                         ELSE
                             CASE WHEN (lv_AssyBomCd IS NOT NULL)
                                 THEN  '->'
                             END
                         END
              END
              ||
              -- Inventory Config Slot
              lv_InvBomCd
              ||
              -- Only show Position Info for the Config Slot if: 
              -- a) More than one position 
              -- b) Parent has more than one position
              -- c) Position Code is not the Default
              -- d) Inventory is not loose
              CASE WHEN ( an_NhInvNoDbId IS NOT NULL AND ( ln_InvPosCt > 1  OR ln_NhPosCt > 1 OR lv_InvPosCd != '1' ) )
                  THEN ' (' || lv_InvPosCd  || ')'
              END
         END;

   RETURN lv_ConfigDesc;

END BuildInvConfigPosDesc;

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
*  Recent Coder: Guillaume Turcotte
*  Recent Date:  April 03, 2018
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
      -- Inventory
      ls_InvClassCd  inv_inv.inv_class_cd%TYPE;
      ll_NhInvNoDbId inv_inv.nh_inv_no_db_id%TYPE;
      ls_SerialNo    inv_inv.serial_no_oem%TYPE;
      -- Aircraft
      ls_ACRegCd     inv_ac_reg.ac_reg_cd%TYPE;
      -- Part
      ls_PartNoOem   eqp_part_no.part_no_oem%TYPE;
      ls_PartDesc    eqp_part_no.part_no_sdesc%TYPE;
      -- Assembly
      ls_AssyBomName eqp_assmbl_bom.assmbl_bom_name%TYPE;
      ls_AssyBomCd   eqp_assmbl_bom.assmbl_bom_cd%TYPE;
      ln_PosNumber   eqp_assmbl_bom.pos_ct%TYPE;
      -- Position
      ls_PosCd       eqp_assmbl_pos.eqp_pos_cd%TYPE;

   BEGIN

      -- Initialize the return value
      al_Return := icn_NoProc;

      -- Select arguments BuildInvDesc.
      SELECT
         -- Inventory
         inv_inv.inv_class_cd,
         inv_inv.nh_inv_no_db_id,
         inv_inv.serial_no_oem,
         -- Aircraft
         inv_ac_reg.ac_reg_cd,
         -- Part
         eqp_part_no.part_no_oem,
         eqp_part_no.part_no_sdesc,
         -- Assembly
         NVL(eqp_assmbl_bom.pos_ct,1) AS eqp_pos_ct,
         eqp_assmbl_bom.assmbl_bom_name,
         eqp_assmbl_bom.assmbl_bom_cd,
         -- Position
         eqp_pos_cd
      INTO
         -- Inventory
         ls_InvClassCd,
         ll_NhInvNoDbId,
         ls_SerialNo,
         -- Aircraft
         ls_ACRegCd,
         -- Part
         ls_PartNoOem,
         ls_PartDesc,
         -- Assembly
         ln_PosNumber,
         ls_AssyBomName,
         ls_AssyBomCd,
         -- Position
         ls_PosCd
      FROM
           inv_inv
           LEFT JOIN inv_ac_reg ON
              inv_ac_reg.inv_no_db_id = inv_inv.inv_no_db_id AND
              inv_ac_reg.inv_no_id    = inv_inv.inv_no_id
           LEFT JOIN eqp_part_no ON
              eqp_part_no.part_no_db_id = inv_inv.part_no_db_id AND
              eqp_part_no.part_no_id    = inv_inv.part_no_id
           LEFT JOIN eqp_assmbl_bom ON
              eqp_assmbl_bom.assmbl_db_id   = inv_inv.assmbl_db_id  AND
              eqp_assmbl_bom.assmbl_cd      = inv_inv.assmbl_cd     AND
              eqp_assmbl_bom.assmbl_bom_id  = inv_inv.assmbl_bom_id
           LEFT JOIN eqp_assmbl_pos ON
              eqp_assmbl_pos.assmbl_db_id   = inv_inv.assmbl_db_id  AND
              eqp_assmbl_pos.assmbl_cd      = inv_inv.assmbl_cd     AND
              eqp_assmbl_pos.assmbl_bom_id  = inv_inv.assmbl_bom_id AND
              eqp_assmbl_pos.assmbl_pos_id  = inv_inv.assmbl_pos_id
      WHERE
         inv_inv.inv_no_db_id = al_InvDbId AND
         inv_inv.inv_no_id    = al_InvId
         AND
         inv_inv.rstat_cd	= 0
      ;

      -- Build the new Inventory Description
      as_InvSdesc := BuildInvDesc (
         -- Inventory
         ls_InvClassCd,
         ll_NhInvNoDbId,
         ls_SerialNo,
         -- Aircraft
         ls_ACRegCd,
         -- Part
         ls_PartNoOem,
         ls_PartDesc,
         -- Assembly
         ls_AssyBomName,
         ls_AssyBomCd,
         ln_PosNumber,
         -- Position
         ls_PosCd
      );
      
      -- update the inventory record
      UPDATE inv_inv
         SET inv_no_sdesc = as_InvSdesc
       WHERE inv_no_db_id = al_InvDbId AND
             inv_no_id    = al_InvId;

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

      -- local variables
      ls_ConfigDesc    inv_inv.config_pos_sdesc%TYPE;
      ls_InvNoSdesc    inv_inv.inv_no_sdesc%TYPE;

      CURSOR lCur_Inventory
      IS
      SELECT
         BuildInvConfigPosDesc
            (
            -- Inventory information
            av_InvClassCd    => inv_inv.inv_class_cd,
            an_InvNoDbId     => inv_inv.inv_no_db_id,
            an_InvNoId       => inv_inv.inv_no_id,
            an_NhInvNoDbId   => inv_inv.nh_inv_no_db_id,
            an_AssyInvNoDbId => inv_inv.assmbl_inv_no_db_id,
            an_AssyInvNoId   => inv_inv.assmbl_inv_no_id,
            an_HInvNoDbId    => inv_inv.h_inv_no_db_id,
            an_HInvNoId      => inv_inv.h_inv_no_id,
            -- Inventory slot and position info
            lv_InvBomCd      => eqp_assmbl_bom.assmbl_bom_cd,
            lv_InvPosCd      => eqp_assmbl_pos.eqp_pos_cd,
            ln_InvPosCt      => eqp_assmbl_bom.pos_ct,
            -- Assembly slot and position info
            lv_AssyBomCd     => assy_eqp_assmbl_bom.assmbl_bom_cd,
            lv_AssyPoscd     => assy_eqp_assmbl_pos.eqp_pos_cd,
            ln_AssyPosCt     => assy_eqp_assmbl_bom.pos_ct,
            -- Next highest position info
            ln_NhPosCt       => nh_assmbl_bom.pos_ct
            )
         AS config_desc
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
         LEFT OUTER JOIN eqp_assmbl_bom nh_assmbl_bom ON
            nh_assmbl_bom.assmbl_db_id   = eqp_assmbl_bom.nh_assmbl_db_id AND
            nh_assmbl_bom.assmbl_cd      = eqp_assmbl_bom.nh_assmbl_cd AND
            nh_assmbl_bom.assmbl_bom_id  = eqp_assmbl_bom.nh_assmbl_bom_id
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
      WHERE
         inv_inv.inv_no_db_id = al_InvNoDbId AND
         inv_inv.inv_no_id    = al_InvNoId
      ;

      lrec_Inventory lcur_Inventory%ROWTYPE;

   BEGIN

      OPEN lCur_Inventory;
      FETCH lCur_Inventory into ls_ConfigDesc;

      IF lCur_Inventory%NOTFOUND THEN
         ls_ConfigDesc := NULL;
      END IF;

      CLOSE lCur_Inventory;

      -- update config slot description and inventory description if the inventory is not BATCH, SER or ACFT
      IF ls_ConfigDesc IS NOT NULL THEN
         UPDATE
            inv_inv
         SET
            config_pos_sdesc = ls_ConfigDesc
         WHERE
            inv_no_db_id = al_InvNoDbId AND
            inv_no_id    = al_InvNoId;

         InvUpdInvDesc(al_InvNoDbId, al_InvNoId, ls_InvNoSdesc, on_Return);
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