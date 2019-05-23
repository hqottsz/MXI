--liquibase formatted sql

--changeSet OPER-29648:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.ACTION_PARM_DELETE('ACTION_STATIONMONITORING_STATION_MONITORING');
END;
/

--changeSet OPER-29648:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_DATA_PKG.CONFIG_PARM_DELETE('STATION_MONITORING_VIEW');
END;
/
