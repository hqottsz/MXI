--liquibase formatted sql


--changeSet AEB-561:1 stripComments:false
/**************************************************************************************
* 
* AEB-561: Insert scripts for assigning user [mxintegration] to the spec2k role
*
***************************************************************************************/
INSERT INTO 
	utl_user_role 
	(
		USER_ID, ROLE_ID, ROLE_ORDER, UTL_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
	)
  SELECT
  	15,17000, 1, 0, 0, to_date('17-01-2017 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-01-2017 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_user_role WHERE ROLE_ID = 17000 AND USER_ID = 15 );		