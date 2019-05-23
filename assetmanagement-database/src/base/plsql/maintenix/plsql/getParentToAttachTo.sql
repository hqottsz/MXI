--liquibase formatted sql


--changeSet getParentToAttachTo:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE getParentToAttachTo(
       as_InvClassCd  in inv_inv.inv_class_cd%TYPE,
       an_HInvNoDbId in inv_inv.h_inv_no_db_id%TYPE,
       an_HInvNoId in inv_inv.h_inv_no_id%TYPE,
       an_AssmblDbId in inv_inv.assmbl_db_id%TYPE,
       as_AssmblCd in inv_inv.assmbl_cd%TYPE,
       an_AssmblBomId  in inv_inv.assmbl_bom_id%TYPE,
       an_AssmblPosId  in inv_inv.assmbl_pos_id%TYPE,
       on_InvNoDbId out inv_inv.inv_no_db_id%TYPE,
       on_InvNoId out inv_inv.inv_no_id%TYPE,
       on_ReturnResult out NUMBER) is


   CURSOR lcur_GetParent (
                          cn_HInvNoDbId      in inv_inv.h_inv_no_db_id%TYPE,
                          cn_HInvNoId        in inv_inv.h_inv_no_id%TYPE,
                          cn_AssmblDbId      in inv_inv.assmbl_db_id%TYPE,
                          cs_AssmblCd        in inv_inv.assmbl_cd%TYPE,
                          cn_AssmblBomId     in inv_inv.assmbl_bom_id%TYPE,
                          cn_AssmblPosId     in inv_inv.assmbl_pos_id%TYPE) IS
       SELECT  INV.INV_NO_DB_ID,
               INV.INV_NO_ID
      FROM     INV_INV  INV,
               INV_INV INVPARENT
      WHERE    INV.ASSMBL_INV_NO_DB_ID = cn_HInvNoDbId AND
               INV.ASSMBL_INV_NO_ID    = cn_HInvNoId
               AND
               INV.ASSMBL_DB_ID  = cn_AssmblDbId    AND
               INV.ASSMBL_CD     = cs_AssmblCd      AND
               INV.ASSMBL_BOM_ID = cn_AssmblBomId   AND
               INV.ASSMBL_POS_ID = cn_AssmblPosId   AND
               INV.INV_CLASS_CD  <> 'SER'	    AND
               INV.rstat_cd	 = 0
               AND
               INVPARENT.INV_NO_DB_ID = cn_HInvNoDbId AND
               INVPARENT.INV_NO_ID    = cn_HInvNoId   AND
               INVPARENT.rstat_cd	 = 0
               AND
               ( INVPARENT.INV_CLASS_CD = 'ASSY' OR
                 INVPARENT.INV_CLASS_CD = 'ACFT' )
      UNION ALL
      SELECT   INV.INV_NO_DB_ID,
               INV.INV_NO_ID
      FROM     INV_INV  INV,
               INV_INV INVPARENT
      WHERE    INV.H_INV_NO_DB_ID = cn_HInvNoDbId AND
               INV.H_INV_NO_ID    = cn_HInvNoId
               AND
               INV.ASSMBL_DB_ID  = cn_AssmblDbId    AND
               INV.ASSMBL_CD     = cs_AssmblCd      AND
               INV.ASSMBL_BOM_ID = cn_AssmblBomId   AND
               INV.ASSMBL_POS_ID = cn_AssmblPosId   AND
               INV.INV_CLASS_CD <> 'SER'	    AND
               INV.rstat_cd	 = 0
               AND
               INVPARENT.INV_NO_DB_ID = cn_HInvNoDbId AND
               INVPARENT.INV_NO_ID    = cn_HInvNoId   AND
               INVPARENT.rstat_cd	 = 0
               AND
               INVPARENT.INV_CLASS_CD <> 'ASSY' AND
               INVPARENT.INV_CLASS_CD <> 'ACFT';

lrec_GetParent lcur_GetParent%ROWTYPE;
ln_AssmblDbId inv_inv.assmbl_db_id%TYPE;
ls_AssmblCd inv_inv.assmbl_cd%TYPE;
ln_AssmblBomId  inv_inv.assmbl_bom_id%TYPE;
ln_AssmblPosId  inv_inv.assmbl_pos_id%TYPE;

BEGIN

   -- Initialize the return value
   on_ReturnResult := 0;

   IF as_InvClassCd = 'SER' THEN
      ln_AssmblDbId  := an_AssmblDbId;
      ls_AssmblCd    := as_AssmblCd;
      ln_AssmblBomId := an_AssmblBomId;
      ln_AssmblPosId := an_AssmblPosId;
   ELSE
      SELECT   EAP.NH_ASSMBL_DB_ID,
               EAP.NH_ASSMBL_CD,
               EAP.NH_ASSMBL_BOM_ID,
               EAP.NH_ASSMBL_POS_ID
      INTO     ln_AssmblDbId,
               ls_AssmblCd,
               ln_AssmblBomId,
               ln_AssmblPosId
      FROM     EQP_ASSMBL_POS EAP
      WHERE    EAP.ASSMBL_DB_ID  = an_AssmblDbId    AND
               EAP.ASSMBL_CD     = as_AssmblCd      AND
               EAP.ASSMBL_BOM_ID = an_AssmblBomId   AND
               EAP.ASSMBL_POS_ID = an_AssmblPosId;
   END IF;

   OPEN lcur_GetParent (an_HInvNoDbId,
                        an_HInvNoId,
                        ln_AssmblDbId,
                        ls_AssmblCd,
                        ln_AssmblBomId,
                        ln_AssmblPosId);
   FETCH lcur_GetParent INTO lrec_GetParent;
   IF NOT lcur_GetParent%FOUND THEN
      on_ReturnResult := -1;
   ELSE
      on_ReturnResult := 1;
      on_InvNoDbId    := lrec_GetParent.inv_no_db_id;
      on_InvNoId      := lrec_GetParent.inv_no_id;
   END IF;

   CLOSE lcur_GetParent;
END getParentToAttachTo;
/