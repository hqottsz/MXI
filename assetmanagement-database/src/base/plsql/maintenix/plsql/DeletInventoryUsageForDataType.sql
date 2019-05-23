--liquibase formatted sql


--changeSet DeletInventoryUsageForDataType:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE DeletInventoryUsageForDataType(p_aAssmblDbId   IN NUMBER,
                                                           p_aAssmblCd     IN VARCHAR2,
                                                           P_aDataTypeDbId IN NUMBER,
                                                           P_aDataTypeId   IN NUMBER) IS

BEGIN

   /* delete the usages for the datatype */
   DELETE FROM
      inv_curr_usage
   WHERE
      inv_curr_usage.data_type_db_id = P_aDataTypeDbId AND
      inv_curr_usage.data_type_id    = P_aDataTypeId
      AND
      (inv_curr_usage.inv_no_db_id|| ':' || inv_curr_usage.inv_no_id)
      IN
      (SELECT
         inv_inv.inv_no_db_id || ':' || inv_inv.inv_no_id
       FROM
         inv_inv
       WHERE
         (
          (-- the inventory is under the assembly
           inv_inv.assmbl_db_id = p_aAssmblDbId AND
           inv_inv.assmbl_cd    = p_aAssmblCd
          )
          OR
          (-- the inventory is the assembly
           inv_inv.orig_assmbl_db_id = p_aAssmblDbId AND
           inv_inv.orig_assmbl_cd    = p_aAssmblCd
          )
         )
      );
END;

/