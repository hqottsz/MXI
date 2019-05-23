--liquibase formatted sql


--changeSet 50.1.aopr_rbl_fault_mon_mv1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.MATERIALIZED_VIEW_DROP('AOPR_RBL_FAULT_MON_MV1');
END;
/

--changeSet 50.1.aopr_rbl_fault_mon_mv1:2 stripComments:false
CREATE MATERIALIZED VIEW aopr_rbl_fault_mon_mv1
BUILD DEFERRED
REFRESH COMPLETE ON DEMAND
AS
SELECT
  operator_id,
  operator_code,
  fleet_type,
  tail_number,
  config_slot_code,
  fault_source_code,
  year_month,
  flight_hours,
  cycles,
  fault_cnt
FROM 
  (
     SELECT
        operator_id,
        aircraft_id,
        operator_code,
        fleet_type,
        tail_number,
        config_slot_code,
        fault_source_code,
        year_month,
        flight_hours,
        cycles,
        fault_cnt
     FROM
        opr_rbl_fault_monthly
     -- the above are maintenix data 
     UNION ALL
     -- to legacy data
     SELECT
        operator_id,
        NULL aircraft_id,
        operator_code,
        fleet_type,
        acft_serial_number,
        config_slot_code,
        fault_source_code,
        year_month,
        flight_hours,
        cycles,
        fault_cnt
     FROM
        opr_rbl_hist_fault_monthly
  );