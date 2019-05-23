--liquibase formatted sql


--changeSet OPER-2675:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- insert new config_parm
   utl_migr_data_pkg.config_parm_insert(
      'APPLY_APPLICABILITY_CHECK_TO_NH_ASSEMBLY', -- parameter name
      'LOGIC',                            -- parameter type
      'Controls the type of applicability check during a component installation or reservation. When set to FALSE, the full sub-inventory tree is validated against the assembly it is installed on or reserved for. When set to TRUE, the sub-inventory tree is broken up by sub-assemblies with each sub-inventory only being compared to its parent assembly (nh) and never its parent''s parent (grandparent nh or h).', -- parameter description
      'GLOBAL',                           -- configuration type (GLOBAL or USER)
      'TRUE/FALSE',                       -- allowed values for the parameter
      'TRUE',                             -- default value of the parameter
      1,                                  -- whether or not the parameter is mandatory
      'Core Logic',                       -- the parameter category
      '8.1-SP3',                          -- the version in which the parm was modified
      0                                   -- the utl_id
   ); 
   
    -- remove deprecated config_parm
   utl_migr_data_pkg.config_parm_delete('APPLY_APPLICABILITY_CHECK_TO_SUB_COMPONENTS');
END;
/