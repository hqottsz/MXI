--liquibase formatted sql


--changeSet OPER-5078:1 stripComments:false
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
   'http://xml.mxi.com/xsd/core/matadapter/part-request-status/1.3',
   'part-request-status',
   'EJB',
   'com.mxi.mx.core.ejb.MaterialAdapter',
   'partRequestStatusV1_3',
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
      namespace = 'http://xml.mxi.com/xsd/core/matadapter/part-request-status/1.3' AND
      root_name = 'part-request-status'
   );