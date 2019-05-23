--liquibase formatted sql


--changeSet OPER-2629:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'APPLY_APPLICABILITY_CHECK_TO_SUB_COMPONENTS', -- parameter name
      'LOGIC',                            -- parameter type
      'Controls whether the applicability check, during a component installation, verifies the component and its sub-components (TRUE) or just the component itself (FALSE).', -- parameter description
      'GLOBAL',                           -- configuration type (GLOBAL or USER)
      'TRUE/FALSE',                       -- allowed values for the parameter
      'FALSE',                            -- default value of the parameter
      1,                                  -- whether or not the parameter is mandatory
      'Core Logic',                       -- the parameter category
      '8.1-SP3',                          -- the version in which the parm was modified
      0                                   -- the utl_id
   ); 
END;
/