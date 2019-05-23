--liquibase formatted sql


--changeSet inv_desc_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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