--liquibase formatted sql


--changeSet SWA-1906:1 stripComments:false
/**************************************************************************************
* 
* SWA-1906 Update Utl_user and utl_user_role tables with correct revision_db_id number
*
***************************************************************************************/
UPDATE 
	utl_user
SET
	REVISION_DB_ID = 0
WHERE
	USER_ID = 17
AND
	USERNAME = 'mxspec2000pointegration';	

--changeSet SWA-1906:2 stripComments:false
UPDATE 
	utl_user_role
SET
	REVISION_DB_ID = 0
WHERE
	USER_ID = 17
AND
	ROLE_ID = 17000;