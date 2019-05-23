--liquibase formatted sql


--changeSet MX-28282:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_delete(
      'sReceiveShipmentManufactDtValidation'
   );
END;
/