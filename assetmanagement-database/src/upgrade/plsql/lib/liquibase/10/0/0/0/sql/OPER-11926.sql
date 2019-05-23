--liquibase formatted sql

--changeSet OPER-11926:1 stripComments:false
UPDATE
   int_bp_lookup
SET
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.adapter.integrationinterface.v2.FlightIntegrationInterface'
WHERE
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.adapter.FlightIntegrationInterface2';

UPDATE
   int_bp_lookup
SET
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.adapter.integrationinterface.v1.FlightIntegrationInterface'
WHERE
   ref_name = 'com.mxi.mx.core.adapter.flight.messages.flight.adapter.FlightIntegrationInterface';