--liquibase formatted sql


--changeSet OPER-3290-2:1 stripComments:false
-- Create Entry for vendor creation V1.1.
INSERT INTO
   int_bp_lookup(
   namespace,
   root_name,
   ref_type,
   ref_name,
   method_name,
   int_logging_type_cd,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
   )
SELECT
   'http://xml.mxi.com/xsd/procurement/1.1',
   'sendRequestForVendorCreation',
   'EJB',
   'com.mxi.mx.core.ejb.ProcurementAdapter',
   'sendVendorCreationV1_1',
   'FULL',
   0,
   TO_DATE('30-09-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
   TO_DATE('30-09-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
   0,
   'MXI'
FROM
   dual
WHERE
   NOT EXISTS
   (
   SELECT *
   FROM
      int_bp_lookup
   WHERE
      namespace = 'http://xml.mxi.com/xsd/procurement/1.1' AND
      root_name = 'sendRequestForVendorCreation'
   );

--changeSet OPER-3290-2:2 stripComments:false
-- create the trigger for version 1.1
INSERT INTO 
   utl_trigger(
   trigger_id,
   trigger_cd,
   exec_order,
   type_cd,
   trigger_name,
   class_name,
   active_bool,
   utl_id
   )
SELECT
   99915,
   'MX_VENDOR_CREATE',
   1,
   'COMPONENT',
   'Vendor Creation',
   'com.mxi.mx.core.adapter.procurement.trg.SendRequestForVendorCreationV1_1',
   0,
   0
FROM
   dual
WHERE
   NOT EXISTS
   (
   SELECT
      1
   FROM
      utl_trigger
   WHERE
      utl_trigger.trigger_id = 99915 AND
      utl_trigger.trigger_cd = 'MX_VENDOR_CREATE'
   );