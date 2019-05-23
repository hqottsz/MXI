--liquibase formatted sql

--changeSet OPER-28790:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      'BULK_LOAD_BATCH_SIZE',
      'LOGIC',
      'The batch size of a single write operation when inserting data loaded into Maintenix through CSVs to the database.',
      'GLOBAL',
      'Number',
      '100',
      1,
      'Core Logic',
      '8.3-SP2',
      0
   );

END;
/