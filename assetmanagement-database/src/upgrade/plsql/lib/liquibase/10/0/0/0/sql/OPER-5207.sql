--liquibase formatted sql

--changeSet OPER-5207:1 stripComments:false 
INSERT INTO
   int_bp_lookup(
   namespace,
   root_name,
   ref_type,
   ref_name,
   method_name,
   int_logging_type_cd,
   rstat_cd
   )
SELECT
   'http://xml.mxi.com/xsd/core/flight/aircraft-capabilities-request/1.0',
   'aircraft-capabilities-request',
   'JAVA',
   'com.mxi.mx.core.adapter.flight.messages.flight.publish.aircraftcapabilities.AircraftCapabilitiesEntryPoint10',
   'process',
   'FULL',
   0
FROM
   dual
WHERE
   NOT EXISTS
   (
   SELECT *
   FROM
      int_bp_lookup
   WHERE
      namespace = 'http://xml.mxi.com/xsd/core/flight/aircraft-capabilities-request/1.0' AND
      root_name = 'aircraft-capabilities-request'
   );