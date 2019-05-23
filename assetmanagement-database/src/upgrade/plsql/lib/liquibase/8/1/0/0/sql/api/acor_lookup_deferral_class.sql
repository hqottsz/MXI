--liquibase formatted sql


--changeSet acor_lookup_deferral_class:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_lookup_deferral_class
AS 
SELECT
      FAIL_SEV_CD as Severity_Code,
      FAIL_DEFER_CD as Deferral_Code
FROM
     REF_FAIL_SEV_DEFER
WHERE
     RSTAT_CD = 0
ORDER BY FAIL_DEFER_CD;