--liquibase formatted sql

/*
 *  Add configuration parameter MATCH_RECORDS_PG_MF_SN.
 */

--changeSet OPER-19052:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'MATCH_RECORDS_PG_MF_SN',
      p_parm_type        =>  'LOGIC',
      p_parm_desc        =>  'When true and inventory is received, if IFS Maintenix searches, but does not find a matching serial number and part number in the inventory records, it searches for a matching part group, serial number, and manufacturer. If a match is found, the received part is reinducted and its record is unarchived/or if record is active then reuse. When false, IFS Maintenix does not match parts that have the same serial numbers as inventory records, but whose part numbers were changed.',
      p_config_type      =>  'GLOBAL',
      p_allow_value_desc =>  'TRUE/FALSE',
      p_default_value    =>  'TRUE',
      p_mand_config_bool =>   0,
      p_category         =>  'Core Logic',
      p_modified_in      =>  '8.2-SP5',
      p_utl_id           =>  0
   );

END;
/