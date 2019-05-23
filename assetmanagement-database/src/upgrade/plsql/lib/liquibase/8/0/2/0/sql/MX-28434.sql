--liquibase formatted sql


--changeSet MX-28434:1 stripComments:false
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, ROOT_NAME, REF_TYPE, 
   REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD,
   RSTAT_CD,
   CREATION_DT
)
SELECT 
   'http://xml.mxi.com/xsd/core/flight/flight-identifier-request/1.1','flight-identifier-request','JAVA', 
   'com.mxi.mx.core.adapter.flight.messages.flight.publish.flightidentifier.FlightIdentifierEntryPoint11', 'process', 'FULL',
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
         NAMESPACE = 'http://xml.mxi.com/xsd/core/flight/flight-identifier-request/1.1'
   );