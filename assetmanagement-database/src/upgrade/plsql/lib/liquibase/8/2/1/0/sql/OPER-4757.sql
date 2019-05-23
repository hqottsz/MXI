--liquibase formatted sql


--changeSet OPER-4757:1 stripComments:false
--
-- Insert new entry to int_bp_lookup for external_maintenance v2.2
--
-- oper-4757
-- Create Found Fault for Initialized Task or Fault.
--
INSERT INTO
   int_bp_lookup(
   namespace,
   root_name,
   ref_type,
   ref_name,
   method_name,
   int_logging_type_cd,
   rstat_cd
   )
SELECT
   'http://xml.mxi.com/xsd/core/ema/external_maintenance/2.2',
   'external_maintenance',
   'EJB',
   'com.mxi.mx.core.adapter.ejb.ema.ExternalMaintenanceAdapter',
   'processExternalMaintenance',
   'FULL',
   0
FROM
   dual
WHERE
   NOT EXISTS
   (
   SELECT *
   FROM
      int_bp_lookup
   WHERE
      namespace = 'http://xml.mxi.com/xsd/core/ema/external_maintenance/2.2' AND
      root_name = 'external_maintenance'
   );