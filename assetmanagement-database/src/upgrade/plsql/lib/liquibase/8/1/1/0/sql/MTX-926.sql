--liquibase formatted sql


--changeSet MTX-926:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************
* Create new API_FINANCE_ACCOUNT_REQUEST action configuration parameter 
***********************************************************************/
BEGIN
   utl_migr_data_pkg.action_parm_insert(
          'API_FINANCE_ACCOUNT_REQUEST', 
	  'Permission to allow API retrieval call for finance account',
	  'TRUE/FALSE', 	  
	  'FALSE',  
	  1, 
	  'API - FINANCE', 
	  '8.1-SP1', 
	  0, 
	  0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet MTX-926:2 stripComments:false
/**********************************************************************
* Set the default value of the finance account configuration parameters
* to the code of the finance account from fnc_account table
**********************************************************************/
UPDATE utl_config_parm SET
    parm_value = (SELECT account_cd FROM fnc_account  WHERE account_type_cd = 'EXPENSE' AND default_bool = 1)
 WHERE
    parm_name = 'ARC_DEFAULT_ISSUE_TO_ACCOUNT';    

--changeSet MTX-926:3 stripComments:false
UPDATE utl_config_parm SET
    parm_value = (SELECT account_cd FROM fnc_account WHERE account_type_cd = 'ADJQTY' AND default_bool = 1)
 WHERE
    parm_name = 'ARC_DEFAULT_CREDIT_ACCOUNT';