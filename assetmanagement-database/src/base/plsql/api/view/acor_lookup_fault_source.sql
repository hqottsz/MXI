--liquibase formatted sql


--changeSet acor_lookup_fault_source:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_lookup_fault_source
AS
SELECT
      FAULT_SOURCE_CD as Fault_Source_Code,
      DESC_SDESC as Fault_Source_Description
FROM
     REF_FAULT_SOURCE
WHERE
     RSTAT_CD = 0
ORDER BY FAULT_SOURCE_CD
;