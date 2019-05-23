--liquibase formatted sql

--changeSet OPER-2516:1 stripComments:false
INSERT INTO 
   INT_BP_LOOKUP 
   (
      NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 'http://xml.mxi.com/xsd/core/flights/flight/2.0', 'flights', 'JAVA', 'com.mxi.mx.core.adapter.flight.messages.flight.adapter.FlightIntegrationInterface2', 'process', 'FULL', 0, to_date('08-12-2016 11:31:42', 'dd-mm-yyyy hh24:mi:ss'), to_date('08-12-2016 11:31:42', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM 
      dual 
   WHERE
      NOT EXISTS(SELECT 1 FROM int_bp_lookup WHERE namespace = 'http://xml.mxi.com/xsd/core/flights/flight/2.0');