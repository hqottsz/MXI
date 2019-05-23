--liquibase formatted sql


--changeSet acor_lookup_fault_severity:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_lookup_fault_severity
AS 
SELECT
      FAIL_SEV_CD as Severity_Code,
      SEV_TYPE_CD as Severity_Type_Code,
      DESC_SDESC   as Severity_Description
FROM
     REF_FAIL_SEV
WHERE
     RSTAT_CD = 0
ORDER BY FAIL_SEV_ORD;