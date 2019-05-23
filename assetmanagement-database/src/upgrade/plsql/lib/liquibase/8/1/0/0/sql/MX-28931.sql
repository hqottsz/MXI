--liquibase formatted sql


--changeSet MX-28931:1 stripComments:false
--
-- MX-28931 migration script
--
-- Add new version of the get-completed-work-hours-request to the int_bp_lookup table so that it can 
-- be used by an integration.
--
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, ROOT_NAME, REF_TYPE, 
   REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD,
   RSTAT_CD,
   CREATION_DT     
)
SELECT 
   'http://xml.mxi.com/xsd/core/labor/get-completed-work-hours-request/1.1','get-completed-work-hours-request','JAVA', 'com.mxi.mx.core.adapter.labor.getcompletedhours.GetCompletedHoursEntryPoint11', 'process', 'FULL',
   0,
   SYSDATE   
FROM
   DUAL
WHERE
   NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE
	     NAMESPACE = 'http://xml.mxi.com/xsd/core/labor/get-completed-work-hours-request/1.1'
   );