--liquibase formatted sql

-- changeSet OPER-23054:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Deleting sPOSearchAOGAuthorization
BEGIN
	utl_migr_data_pkg.config_parm_delete(
		'sPOSearchAOGAuthorization' 			-- parameter name
	);
END;
/


--changeSet OPER-23054:2 stripComments:false
-- Adding sPOSearchAuthorizationStatusCd
INSERT INTO
   utl_config_parm
   (
      parm_value,
      parm_name,
      parm_type,
      parm_desc,
      config_type,
      allow_value_desc,
      default_value,
      mand_config_bool,
      category,
      modified_in,
      utl_id
   )
   SELECT
      null,
      'sPOSearchAuthorizationStatusCd',	-- parameter name
      'SESSION',
      'PO search parameters',
      'USER',
      'String',
      '',
      0,
      'PO Search',
      '8.3',
      0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'sPOSearchAuthorizationStatusCd' );