--liquibase formatted sql


--changeSet getSecondLevelConfigSlot:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getSecondLevelConfigSlot
(
   aAssmblDbId eqp_assmbl_bom.assmbl_db_id%TYPE,
   aAssmblCd eqp_assmbl_bom.assmbl_cd%TYPE,
   aAssmblBomId eqp_assmbl_bom.assmbl_bom_id%TYPE
) RETURN VARCHAR
IS
   lReturn VARCHAR2(200);
BEGIN

   IF aAssmblBomId = 0 THEN
      SELECT
         eqp_assmbl_bom.assmbl_bom_cd || ' (' || eqp_assmbl_bom.assmbl_bom_name || ')'
      INTO
         lReturn
      FROM
         eqp_assmbl_bom
      WHERE
         eqp_assmbl_bom.assmbl_db_id  = aAssmblDbId AND
         eqp_assmbl_bom.assmbl_cd     = aAssmblCd AND
         eqp_assmbl_bom.assmbl_bom_id = aAssmblBomId;
   ELSE
      SELECT
         config_slot
      INTO
         lReturn
      FROM
         (
            SELECT
               LEVEL AS tree_level,
               eqp_assmbl_bom.assmbl_bom_cd || ' (' || eqp_assmbl_bom.assmbl_bom_name || ')' AS config_slot
            FROM
               eqp_assmbl_bom
            START WITH
               eqp_assmbl_bom.assmbl_db_id  = aAssmblDbId AND
               eqp_assmbl_bom.assmbl_cd     = aAssmblCd AND
               eqp_assmbl_bom.assmbl_bom_id = aAssmblBomId
            CONNECT BY
               eqp_assmbl_bom.assmbl_db_id  = PRIOR eqp_assmbl_bom.nh_assmbl_db_id AND
               eqp_assmbl_bom.assmbl_cd     = PRIOR eqp_assmbl_bom.nh_assmbl_cd AND
               eqp_assmbl_bom.assmbl_bom_id = PRIOR eqp_assmbl_bom.nh_assmbl_bom_id
               AND
               eqp_assmbl_bom.assmbl_bom_id <> 0
            ORDER BY
               tree_level DESC
         ) config_slot_tree
      WHERE
         ROWNUM = 1;
   END IF;

   RETURN lReturn;

END getSecondLevelConfigSlot;
/