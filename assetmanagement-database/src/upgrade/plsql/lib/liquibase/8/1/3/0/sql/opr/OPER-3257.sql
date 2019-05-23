--liquibase formatted sql


--changeSet OPER-3257:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
 
utl_migr_data_pkg.config_parm_insert(
 'FLIGHT_ADAPTER_PUBLISH_MEL_ONLY',
 'LOGIC',
 'This parameter is used to control publishing the fault of MEL severity type.',
 'GLOBAL',
 'TRUE/FALSE',
 'TRUE',
 1,
 'Flight Adapter',
 '8.2',
 0
);
 
END;
/