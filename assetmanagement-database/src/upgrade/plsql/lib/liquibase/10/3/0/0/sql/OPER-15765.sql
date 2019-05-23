--liquibase formatted sql

--changeSet OPER-15765:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--Add new user mxpurchase, used as the purchasing contact on POs auto-generated through stock low actions
BEGIN
	INSERT INTO UTL_USER
		(USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR, EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
    SELECT
    	18, 'mxpurchase', 'password', 'mxpurchase', NULL, 'mxpurchase', NULL, NULL, 0, 0, 0, 0, to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
    FROM
    	dual
    WHERE
    	NOT EXISTS (SELECT 1 FROM utl_user WHERE USER_ID = 18);
END;
/


--changeSet OPER-15765:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--Add new SYSTEM human resource mxpurchase, used as the purchasing contact on POs auto-generated through stock low actions, connected to utl_user with id 18
BEGIN
	INSERT INTO ORG_HR
		(HR_DB_ID, HR_ID, USER_ID, PAY_METHOD_DB_ID, PAY_METHOD_CD, RSTAT_CD, ALL_AUTHORITY_BOOL,HR_CD, ACTUAL_HOURLY_COST, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
	SELECT
		0, 11, 18, 0, 'SALARY',0, 0,'SYSTEM', NULL,to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),0, 'MXI'
	FROM
		dual
	WHERE
		NOT EXISTS (SELECT 1 FROM org_hr WHERE HR_DB_ID = 0 AND HR_ID = 11);
END;
/


--changeSet OPER-15765:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--Assign the new MXPURCHASE human resource to the default organization
BEGIN
	INSERT INTO ORG_ORG_HR
		(hr_db_id, hr_id, org_db_id, org_id,  default_org_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
    SELECT
    	0, 11, 0, 1, 1, 0, to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-07-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
	FROM
		dual
	WHERE
		NOT EXISTS (SELECT 1 FROM org_org_hr WHERE HR_DB_ID = 0 AND HR_ID = 11 AND ORG_DB_ID = 0 AND ORG_ID = 1);
END;
/