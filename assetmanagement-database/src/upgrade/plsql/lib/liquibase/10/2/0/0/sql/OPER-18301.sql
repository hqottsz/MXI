--liquibase formatted sql

--changeSet OPER-18301:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        =>  'RELEASE_MISSING_COMPONENT',
      p_parm_type        =>  'LOGIC',
      p_parm_desc        =>  'If you try to close a work package for an aircraft or component that is missing mandatory sub-components, the ERROR or WARN setting for this parameter determines whether you can proceed. ERROR: you cannot complete the work package (unless there is another IN WORK work package against the aircraft or component). WARN: you see a warning, but can complete the work package. OK: do not use. For incomplete components, use the complete work package as unserviceable or ready for build workflows.',
      p_config_type      =>  'USER',
      p_allow_value_desc =>  'WARN/ERROR/OK',
      p_default_value    =>  'ERROR',
      p_mand_config_bool =>  1,
      p_category         =>  'Core Logic',
      p_modified_in      =>  '8.2-SP5',
      p_utl_id           =>  0
   );

END;
/
