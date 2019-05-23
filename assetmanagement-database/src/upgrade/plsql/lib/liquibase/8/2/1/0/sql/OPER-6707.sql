--liquibase formatted sql


--changeSet OPER-6707:1 stripComments:false
/**************************************************************************************
* 
* SWA-1845: Insert scripts for the new role system integrator
*
***************************************************************************************/
INSERT INTO 
	utl_role 
	(
		ROLE_ID, ROLE_CD, ROLE_NAME, UTL_ID
	)
  SELECT
  	17001, 'INTEGRA', 'System Integrator', 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_role WHERE ROLE_ID = 17001 AND UTL_ID = 0 );	

--changeSet OPER-6707:2 stripComments:false
INSERT INTO 
	utl_user_role 
	(
		USER_ID, ROLE_ID, ROLE_ORDER, UTL_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
	)
  SELECT
  	15,17001, 0, 0, 0, to_date('09-08-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('09-08-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_user_role WHERE ROLE_ID = 17001 AND USER_ID = 15 );				