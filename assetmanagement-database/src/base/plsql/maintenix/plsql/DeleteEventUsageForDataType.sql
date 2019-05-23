--liquibase formatted sql


--changeSet DeleteEventUsageForDataType:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE DeleteEventUsageForDataType(p_aAssmblDbId   IN NUMBER,
                                                           p_aAssmblCd     IN VARCHAR2,
                                                           p_aDataTypeDbId IN NUMBER,
                                                           p_aDataTypeId   IN NUMBER) IS

BEGIN

   /* delete the usages for the datatype */
   DELETE FROM
      evt_inv_usage
   WHERE
      evt_inv_usage.data_type_db_id = p_aDataTypeDbId AND
      evt_inv_usage.data_type_id    = p_aDataTypeId
      AND
      (evt_inv_usage.event_db_id|| ':' || evt_inv_usage.event_id || ':' || evt_inv_usage.event_inv_id)
      IN
      (
       SELECT
         evt_inv.event_db_id || ':' || evt_inv.event_id || ':' || evt_inv.event_inv_id
       FROM
         evt_inv,
         inv_inv
       WHERE
         inv_inv.inv_no_db_id = evt_inv.inv_no_db_id AND
         inv_inv.inv_no_id    = evt_inv.inv_no_id
         AND
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