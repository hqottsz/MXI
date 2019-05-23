--liquibase formatted sql


--changeSet MX-22840:1 stripComments:false
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
   'http://xml.mxi.com/xsd/core/flight/earliest-deadlines-request/1.1', 
   'earliest-deadlines-request', 
   'JAVA', 
   'com.mxi.mx.core.adapter.flight.messages.flight.publish.FlightPublishingService', 
   'generateEarliestDeadlines11', 
   'FULL',
   0, 
   to_date('29-04-2009 11:32:57', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('29-04-2009 11:32:57', 'dd-mm-yyyy hh24:mi:ss'), 
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
         NAMESPACE = 'http://xml.mxi.com/xsd/core/flight/earliest-deadlines-request/1.1'
         AND
         ROOT_NAME = 'earliest-deadlines-request'
   );