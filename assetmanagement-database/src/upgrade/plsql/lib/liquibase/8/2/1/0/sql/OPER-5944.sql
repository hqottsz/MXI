--liquibase formatted sql


--changeSet OPER-5944:1 stripComments:false
-- Create Entry for Part Request Status v1.4.
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
   'http://xml.mxi.com/xsd/core/matadapter/part-request-status/1.4',
   'part-request-status',
   'EJB',
   'com.mxi.mx.core.ejb.MaterialAdapter',
   'partRequestStatusV1_4',
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
      namespace = 'http://xml.mxi.com/xsd/core/matadapter/part-request-status/1.4' AND
      root_name = 'part-request-status'
   );