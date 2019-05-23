--liquibase formatted sql


--changeSet QC-6444:1 stripComments:false
/********************************************************************
 * Add the config parm for MAXIMUM_TAX_CHARGE_VENDOR_SELECT_OPTIONS
 ********************************************************************/
INSERT INTO
   UTL_CONFIG_PARM
   (
      PARM_NAME, PARM_TYPE, PARM_VALUE, ENCRYPT_BOOL, PARM_DESC, CONFIG_TYPE, 
	  DEFAULT_VALUE, ALLOW_VALUE_DESC, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
   )
   SELECT 'MAXIMUM_TAX_CHARGE_VENDOR_SELECT_OPTIONS', 'MXWEB', '20', 0, 
      'Indicates the maximum number of options to be displayed in the Tax, Charge, and Vendor dropdown lists. If exceeded, an AutoSuggest widget will be used instead.', 
	  'GLOBAL', '20', 'Number', 1, 'MXWEB', '8.0', 0
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = 'MAXIMUM_TAX_CHARGE_VENDOR_SELECT_OPTIONS'  AND parm_type = 'MXWEB' );