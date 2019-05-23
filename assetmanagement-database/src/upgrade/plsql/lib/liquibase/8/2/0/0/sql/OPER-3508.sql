--liquibase formatted sql


--changeSet OPER-3508:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE

   JAVA_SESSION_CLEARED EXCEPTION;
   PRAGMA EXCEPTION_INIT(JAVA_SESSION_CLEARED, -29549);
   
   ln_keep_tries  NUMBER := 3;
   lv_tmp        VARCHAR2(32000); 
   lraw_uuid     RAW(16);
  
BEGIN
     
   <<BEFORE_JAVA_CALL>>
   BEGIN
     lraw_uuid := mx_key_pkg.new_uuid;
   
   EXCEPTION 
     WHEN JAVA_SESSION_CLEARED THEN
       
       lv_tmp := DBMS_JAVA.ENDSESSION_AND_RELATED_STATE;
       ln_keep_tries := ln_keep_tries - 1;
       
       IF ln_keep_tries >= 0 THEN 
         GOTO BEFORE_JAVA_CALL;
       ELSE
          RAISE;
       END IF; 
       
    WHEN OTHERS THEN
      RAISE;      
   END;
   
END;  
/

--changeSet OPER-3508:2 stripComments:false
INSERT INTO UTL_USER 
   (USER_ID, USERNAME, PASSWORD, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ALERT_EMAIL_ADDR,EMAIL_ADDR, LOCKED_BOOL, UTL_ID, CTRL_DB_ID, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT 
        16, 'mxsupplychain', 'password', 'SUPPLY', NULL, 'CHAIN', NULL, NULL, 1, 0, 0, 0, to_date('16-07-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-07-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 100, 'MXI'
   FROM
        DUAL
   WHERE
        NOT EXISTS
        (
           SELECT 1
           FROM utl_user
           WHERE user_id = 16 AND username = 'mxsupplychain'
        );        

--changeSet OPER-3508:3 stripComments:false
INSERT INTO ORG_HR 
   (HR_DB_ID, HR_ID, USER_ID, PAY_METHOD_DB_ID, PAY_METHOD_CD, RSTAT_CD, ALL_AUTHORITY_BOOL,HR_CD, ACTUAL_HOURLY_COST, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT
        0, 8, 16, 0, 'SALARY',0, 0,'SYSTEM', NULL, to_date('21-08-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('21-08-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 10, 'MXI'
   FROM
        DUAL
   WHERE NOT EXISTS
        (
           SELECT 1
           FROM ORG_HR
           WHERE hr_db_id = 0 AND hr_id = 8 AND user_id = 16
        );