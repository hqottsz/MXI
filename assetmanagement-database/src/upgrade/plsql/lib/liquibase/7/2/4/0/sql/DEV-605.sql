--liquibase formatted sql


--changeSet DEV-605:1 stripComments:false
INSERT INTO
  int_bp_lookup
  (
     NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
  )
SELECT 'http://xml.mxi.com/xsd/core/matadapter/inventory-issued-to/1.0', 'inventory-issued-to', 'JAVA', 'com.mxi.mx.core.adapter.issueinventory.issueinventoryto.InventoryIssuedToEntryPoint', 'coordinate', 0, sysdate, sysdate, 0, 'MXI'
FROM
  dual
WHERE
  NOT EXISTS ( SELECT 1 FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/core/matadapter/inventory-issued-to/1.0' );