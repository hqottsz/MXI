--liquibase formatted sql


--changeSet MX-27606:1 stripComments:false
-- earliest-deadlines-request-12
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, 
   ROOT_NAME, 
   REF_TYPE, 
   REF_NAME, 
   METHOD_NAME, 
   INT_LOGGING_TYPE_CD,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://xml.mxi.com/xsd/core/flight/earliest-deadlines-request/1.2', 
   'earliest-deadlines-request', 
   'JAVA', 
   'com.mxi.mx.core.adapter.flight.messages.flight.publish.earliestdeadlines.EarliestDeadlinesEntryPoint12', 
   'process', 
   'FULL',
   0, 
   to_date('15-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('15-04-2013 09:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE 
         NAMESPACE = 'http://xml.mxi.com/xsd/core/flight/earliest-deadlines-request/1.2'
         AND
         ROOT_NAME = 'earliest-deadlines-request'
   );