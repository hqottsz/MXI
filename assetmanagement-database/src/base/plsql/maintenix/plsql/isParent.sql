--liquibase formatted sql


--changeSet isParent:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION isParent
(
   aParentDbId    inv_inv.inv_no_db_id%TYPE,
   aParentId      inv_inv.inv_no_id%TYPE,
   aChildDbId     inv_inv.inv_no_db_id%TYPE,
   aChildId       inv_inv.inv_no_id%TYPE

) RETURN VARCHAR
IS
   lIsParent NUMBER;


BEGIN
   lIsParent := 0;

   SELECT
       1
   INTO
       lIsParent
   FROM
      (SELECT
           inv_no_db_id,
           inv_no_id
       FROM
           inv_inv
       WHERE
           inv_inv.rstat_cd = 0
       START WITH
            inv_inv.inv_no_db_id = aChildDbId AND
            inv_inv.inv_no_id    = aChildId
       CONNECT BY
            PRIOR nh_inv_no_db_id = inv_no_db_id AND
            PRIOR nh_inv_no_id    = inv_no_id
       )inv_tree
   WHERE
       inv_tree.inv_no_db_id = aParentDbId AND
       inv_tree.inv_no_id    = aParentId;


   RETURN lIsParent;

END isParent;
/