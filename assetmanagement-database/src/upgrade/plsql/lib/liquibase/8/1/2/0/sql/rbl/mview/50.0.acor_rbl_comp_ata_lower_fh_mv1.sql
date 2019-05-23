--liquibase formatted sql


--changeSet 50.0.acor_rbl_comp_ata_lower_fh_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('ACOR_RBL_COMP_ATA_LOWER_FH_MV1');
END;
/

--changeSet 50.0.acor_rbl_comp_ata_lower_fh_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW acor_rbl_comp_ata_lower_fh_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
   task_ata.assembly_code fleet_type,
   task_ata.config_slot,
   part.part_number,
   MIN(acor_req_sched_rules_v1.interval_quantity) lower_interval_fh
FROM
   (
      SELECT
         task_id,
         assembly_code,
         SUBSTR(config_slot_code,1,2) config_slot
      FROM
         acor_req_ata_v1
      UNION ALL
      SELECT
         task_id,
         assembly_code,
         SUBSTR(config_slot_code,1,2) config_slot
      FROM
         acor_req_exec_ata_v1
   ) task_ata
   INNER JOIN acor_req_part_v1 part  ON 
      task_ata.task_id = part.task_id
   LEFT JOIN acor_req_sched_rules_v1 ON
      task_ata.task_id = acor_req_sched_rules_v1.task_id
      AND
      acor_req_sched_rules_v1.data_type_code = 'HOURS'
GROUP BY 
   task_ata.assembly_code,
   task_ata.config_slot,
   part.part_number;            