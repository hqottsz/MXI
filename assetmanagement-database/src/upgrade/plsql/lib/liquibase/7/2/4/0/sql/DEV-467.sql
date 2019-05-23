--liquibase formatted sql


--changeSet DEV-467:1 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_APPROVE_VENDOR_ORDER_TYPE', 'SECURED_RESOURCE','Permission required to set the org vendor order type status to approve.','USER', 'TRUE/FALSE', 'FALSE', 1,'Purchasing - Vendors', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_APPROVE_VENDOR_ORDER_TYPE');

--changeSet DEV-467:2 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_APPROVE_VENDOR_SERVICE_TYPE', 'SECURED_RESOURCE','Permission required to set the org vendor service type status to approve.','USER', 'TRUE/FALSE', 'FALSE', 1,'Purchasing - Vendors', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_APPROVE_VENDOR_SERVICE_TYPE');

--changeSet DEV-467:3 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_UNAPPROVE_VENDOR_ORDER_TYPE', 'SECURED_RESOURCE','Permission required to set the org vendor order type status to unapprove.','USER', 'TRUE/FALSE', 'FALSE', 1,'Purchasing - Vendors', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR_ORDER_TYPE');

--changeSet DEV-467:4 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_UNAPPROVE_VENDOR_SERVICE_TYPE', 'SECURED_RESOURCE','Permission required to set the org vendor service type status to unapprove.','USER', 'TRUE/FALSE', 'FALSE', 1,'Purchasing - Vendors', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR_SERVICE_TYPE');

--changeSet DEV-467:5 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_WARNING_VENDOR_ORDER_TYPE', 'SECURED_RESOURCE','Permission required to set the org vendor order type status to warning.','USER', 'TRUE/FALSE', 'FALSE', 1,'Purchasing - Vendors', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_WARNING_VENDOR_ORDER_TYPE');

--changeSet DEV-467:6 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_WARNING_VENDOR_SERVICE_TYPE', 'SECURED_RESOURCE','Permission required to set the org vendor service type status to warning.','USER', 'TRUE/FALSE', 'FALSE', 1,'Purchasing - Vendors', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_WARNING_VENDOR_SERVICE_TYPE');

--changeSet DEV-467:7 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT 'false', 'ACTION_EDIT_APPROVAL_DETAILS', 'SECURED_RESOURCE','Permission required to edit approval details.','USER', 'TRUE/FALSE', 'FALSE', 1,'Purchasing - Vendors', '7.5',0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_config_parm WHERE utl_config_parm.parm_name = 'ACTION_EDIT_APPROVAL_DETAILS');

--changeSet DEV-467:8 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_APPROVE_VENDOR_ORDER_TYPE', 'SECURED_RESOURCE','MASTER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm 
WHERE db_type_config_parm.parm_name = 'ACTION_APPROVE_VENDOR_ORDER_TYPE' AND
db_type_config_parm.db_type_cd = 'MASTER'
);

--changeSet DEV-467:9 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_APPROVE_VENDOR_ORDER_TYPE', 'SECURED_RESOURCE','OPER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_APPROVE_VENDOR_ORDER_TYPE'
AND
db_type_config_parm.db_type_cd = 'OPER');

--changeSet DEV-467:10 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_APPROVE_VENDOR_SERVICE_TYPE', 'SECURED_RESOURCE','MASTER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_APPROVE_VENDOR_SERVICE_TYPE'
AND
db_type_config_parm.db_type_cd = 'MASTER');

--changeSet DEV-467:11 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_APPROVE_VENDOR_SERVICE_TYPE', 'SECURED_RESOURCE','OPER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_APPROVE_VENDOR_SERVICE_TYPE'
AND
db_type_config_parm.db_type_cd = 'OPER');

--changeSet DEV-467:12 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNAPPROVE_VENDOR_ORDER_TYPE', 'SECURED_RESOURCE','MASTER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR_ORDER_TYPE'
AND
db_type_config_parm.db_type_cd = 'MASTER');

--changeSet DEV-467:13 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNAPPROVE_VENDOR_ORDER_TYPE', 'SECURED_RESOURCE','OPER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR_ORDER_TYPE'
AND
db_type_config_parm.db_type_cd = 'OPER');

--changeSet DEV-467:14 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNAPPROVE_VENDOR_SERVICE_TYPE', 'SECURED_RESOURCE','MASTER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR_SERVICE_TYPE'
AND
db_type_config_parm.db_type_cd = 'MASTER');

--changeSet DEV-467:15 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_UNAPPROVE_VENDOR_SERVICE_TYPE', 'SECURED_RESOURCE','OPER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_UNAPPROVE_VENDOR_SERVICE_TYPE'
AND
db_type_config_parm.db_type_cd = 'OPER');

--changeSet DEV-467:16 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_WARNING_VENDOR_ORDER_TYPE', 'SECURED_RESOURCE','MASTER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_WARNING_VENDOR_ORDER_TYPE'
AND
db_type_config_parm.db_type_cd = 'MASTER');

--changeSet DEV-467:17 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_WARNING_VENDOR_ORDER_TYPE', 'SECURED_RESOURCE','OPER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_WARNING_VENDOR_ORDER_TYPE'
AND
db_type_config_parm.db_type_cd = 'OPER');

--changeSet DEV-467:18 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_WARNING_VENDOR_SERVICE_TYPE', 'SECURED_RESOURCE','MASTER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_WARNING_VENDOR_SERVICE_TYPE'
AND
db_type_config_parm.db_type_cd = 'MASTER');

--changeSet DEV-467:19 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_WARNING_VENDOR_SERVICE_TYPE', 'SECURED_RESOURCE','OPER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_WARNING_VENDOR_SERVICE_TYPE'
AND
db_type_config_parm.db_type_cd = 'OPER');

--changeSet DEV-467:20 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_EDIT_APPROVAL_DETAILS', 'SECURED_RESOURCE','MASTER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_EDIT_APPROVAL_DETAILS'
AND
db_type_config_parm.db_type_cd = 'MASTER');

--changeSet DEV-467:21 stripComments:false
INSERT INTO db_type_config_parm (parm_name, parm_type, db_type_cd)
SELECT 'ACTION_EDIT_APPROVAL_DETAILS', 'SECURED_RESOURCE','OPER' 
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE db_type_config_parm.parm_name = 'ACTION_EDIT_APPROVAL_DETAILS'
AND
db_type_config_parm.db_type_cd = 'OPER');

--changeSet DEV-467:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace FUNCTION isVendorApprovedExpired
( 
  aOrgDbId org_vendor_po_type.org_db_id%TYPE,
  aOrgId org_vendor_po_type.org_id%TYPE,
  aVendorDbId org_vendor_po_type.vendor_db_id%TYPE,
  aVendorId org_vendor_po_type.vendor_id%TYPE
) RETURN NUMBER
IS
   ln_IsApproveExpired NUMBER;
BEGIN

   -- Return 1 if atleast one ref_po_type is Approved Expired
   SELECT
      count(*) INTO ln_IsApproveExpired
   FROM
      org_vendor_po_type
   WHERE
       org_vendor_po_type.org_db_id = aOrgDbId
       AND
       org_vendor_po_type.org_id = aOrgId
       AND
       org_vendor_po_type.vendor_db_id = aVendorDbId
       AND
       org_vendor_po_type.vendor_id = aVendorId
       AND
       org_vendor_po_type.vendor_status_cd = 'APPROVED'
       AND
       org_vendor_po_type.approval_expiry_dt < SYSDATE;

   IF ln_IsApproveExpired <> 0 THEN
      RETURN 1;
   END IF;
   -- Return 1 if atleast one ref_service_type is Approved Expired
   SELECT
      count(*) INTO ln_IsApproveExpired
   FROM
      org_vendor_service_type
   WHERE
      org_vendor_service_type.org_db_id = aOrgDbId
      AND
      org_vendor_service_type.org_id = aOrgId
      AND
      org_vendor_service_type.vendor_db_id = aVendorDbId
      AND
      org_vendor_service_type.vendor_id = aVendorId
      AND
      org_vendor_service_type.vendor_status_cd = 'APPROVED'
      AND
      org_vendor_service_type.approval_expiry_dt < SYSDATE;

   IF ln_IsApproveExpired <> 0 THEN
      RETURN 1;
   ELSE
      RETURN 0;
   END IF;
END isVendorApprovedExpired;
/