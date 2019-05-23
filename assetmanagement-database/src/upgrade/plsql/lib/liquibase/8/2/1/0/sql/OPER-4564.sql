--liquibase formatted sql
  

--changeSet OPER-4564:1 stripComments:false
-- insert the CHGFINTP transaction type
/******************************************************************************
* 
* OPER-4564 Add the ability to change the financial class of a part to the system
*
******************************************************************************/
INSERT INTO 
   ref_xaction_type
   ( XACTION_TYPE_DB_ID, XACTION_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
SELECT 
   0, 'CHGFINTP', 'CHGFINTP', 'Transaction occurred when finance type was changed. ', 0, '18-JULY-16', '18-JULY-16', 0, 'MXI'
FROM
   dual
WHERE
   NOT EXISTS 
   ( 
      SELECT 
         1
      FROM 
         ref_xaction_type
      WHERE 
         XACTION_TYPE_CD = 'CHGFINTP'      
         AND
         DESC_SDESC = 'CHGFINTP'
   ); 

--changeSet OPER-4564:2 stripComments:false
-- insert the CHGFINTP account type   
INSERT INTO 
   ref_account_type
   ( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
SELECT 
   0, 'CHGFINTP', 'CHGFINTP', 'This category is used for finance type change account.', 0, '18-JULY-16', '18-JULY-16', 0, 'MXI'
FROM
   dual
WHERE
   NOT EXISTS 
   ( 
      SELECT 
         1
      FROM 
         ref_account_type
      WHERE 
         ACCOUNT_TYPE_CD = 'CHGFINTP'      
         AND
         DESC_SDESC = 'CHGFINTP'
   );   

--changeSet OPER-4564:3 stripComments:false
-- insert the CHGFINTP account
INSERT INTO 
   fnc_account
   ( ACCOUNT_DB_ID, ACCOUNT_ID, NH_ACCOUNT_DB_ID, NH_ACCOUNT_ID, ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, ACCOUNT_CD, ACCOUNT_SDESC, ACCOUNT_LDESC, TCODE_DB_ID, TCODE_ID, DEFAULT_BOOL, EXT_KEY_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
SELECT 
   0, 60, null, null, 0, 'CHGFINTP', 'CHGFINTP', 'Finance Type Change Account', null, null, null, 1, null, 0, '18-JULY-16', '18-JULY-16', 0, 'MXI'
FROM
   dual
WHERE
   NOT EXISTS 
   ( 
      SELECT 
         1
      FROM 
         fnc_account
      WHERE 
         ACCOUNT_TYPE_CD = 'CHGFINTP'      
         AND
         ACCOUNT_CD = 'CHGFINTP'
   );   

--changeSet OPER-4564:4 stripComments:false
-- insert the PSCHANGEFINCLASS log action
INSERT INTO 
   ref_log_action
   ( LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
SELECT 
   0,'PSCHANGEFINCLASS','Part Financial Class Change', 'Part Financial Class Change', 'PSCHANGEFINCLASS', 0 , TO_DATE('2016-07-19', 'YYYY-MM-DD'), TO_DATE('2016-07-19', 'YYYY-MM-DD'), 0 , 'MXI'
FROM
   dual
WHERE
   NOT EXISTS 
   ( 
      SELECT 
         1
      FROM 
         ref_log_action
      WHERE 
         LOG_ACTION_CD = 'PSCHANGEFINCLASS'      
   );