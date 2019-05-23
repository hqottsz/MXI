--liquibase formatted sql


--changeSet getActualInvQt:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION getActualInvQt
(
   aLocDbId    inv_loc.loc_db_id%TYPE,
   aLocId      inv_loc.loc_id%TYPE,
   aPartNoDbId eqp_part_no.part_no_db_id%TYPE,
   aPartNoId   eqp_part_no.part_no_id%TYPE

) RETURN VARCHAR
IS
   lInventoryCount NUMBER;

BEGIN
    SELECT
       SUM( DECODE( inv_inv.bin_qt, NULL, 1, inv_inv.bin_qt ) )
    INTO
        lInventoryCount
    FROM
        inv_inv
    WHERE
        inv_inv.part_no_db_id   = aPartNoDbId AND
        inv_inv.part_no_id      = aPartNoId   AND
        inv_inv.loc_db_id       = aLocDbId    AND
        inv_inv.loc_id          = aLocId      AND
        inv_inv.nh_inv_no_db_id IS NULL       AND
        inv_inv.nh_inv_no_id    IS NULL       AND
        inv_inv.issued_bool     = 0	      AND
        inv_inv.rstat_cd	= 0;


   IF lInventoryCount IS NULL THEN
      lInventoryCount := 0;
   END IF;


   RETURN lInventoryCount;

END getActualInvQt;
/