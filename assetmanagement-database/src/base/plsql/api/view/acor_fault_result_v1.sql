--liquibase formatted sql


--changeSet acor_fault_result_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_fault_result_v1
AS
SELECT
   fault.alt_id                 AS fault_id,
   fault_result.result_event_cd AS result_event_code
FROM
   sd_fault fault
   INNER JOIN sd_fault_result fault_result ON
     fault_result.fault_db_id = fault.fault_db_id AND
     fault_result.fault_id    = fault.fault_id
;