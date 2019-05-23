--liquibase formatted sql


--changeSet OPER-1155:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment insert new config parm
BEGIN
 
   utl_migr_data_pkg.config_parm_insert(
      'sTaskLabourSummarySearchDeviation',
      'SESSION',
      'Task Labour Summary search parameters.',
      'USER',
      'Integer',
      NULL,
      0,
      'Task Labour Summary Search',
      '8.3-SP1',
      0
   );

END;
/

--changeSet OPER-1155:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment copy the existing settings and child records from the old confirm parm to the new config parm
BEGIN
 
   utl_migr_data_pkg.config_parm_copy(
      'sTaskLabourSummarySearchDeviation', -- new parameter name
      'SESSION', -- parameter type
      'USER', -- configuration type (GlOBAL or USER)
      'sTaskLabourSummarySearchTolerance' -- old parameter
   );
 
END;
/

--changeSet OPER-1155:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment delete old config parm
BEGIN
 
   utl_migr_data_pkg.config_parm_delete(
      'sTaskLabourSummarySearchTolerance' 
   );
 
END;
/   