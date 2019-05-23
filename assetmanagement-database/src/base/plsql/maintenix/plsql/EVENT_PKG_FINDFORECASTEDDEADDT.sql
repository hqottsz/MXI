--liquibase formatted sql


--changeSet EVENT_PKG_FINDFORECASTEDDEADDT:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure EVENT_PKG_FINDFORECASTEDDEADDT(
      aAircraftDbId IN inv_ac_reg.inv_no_db_id%type,
      aAircraftId IN inv_ac_reg.inv_no_id%type,
      aDataTypeDbId IN mim_data_type.data_type_db_id%type,
      aDataTypeId IN mim_data_type.data_type_id%type,
      aRemainingUsageQt IN evt_sched_dead.usage_rem_qt%type,
      aStartDt IN OUT date,
      aForecastDt OUT evt_sched_dead.sched_dead_dt%type,
      ol_Return OUT NUMBER)
   IS
BEGIN
   EVENT_PKG.findForecastedDeadDt(
         aAircraftDbId,
         aAircraftId,
         aDataTypeDbId,
         aDataTypeId,
         aRemainingUsageQt,
         aStartDt,
         aForecastDt,
         ol_Return
      );
END EVENT_PKG_FINDFORECASTEDDEADDT;
/