--liquibase formatted sql


--changeSet 0fnc_account:1 stripComments:false
/******************************************
** 0-Level INSERT SCRIPT FOR FNC_ACCOUNT
*******************************************/
INSERT INTO FNC_ACCOUNT ( ACCOUNT_DB_ID, ACCOUNT_ID, NH_ACCOUNT_DB_ID, NH_ACCOUNT_ID, ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, ACCOUNT_CD, ACCOUNT_SDESC, ACCOUNT_LDESC, TCODE_DB_ID, TCODE_ID, DEFAULT_BOOL, EXT_KEY_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 10, null, null, 0, 'INVOICE', 'INVOICE', 'Invoice Accrual Liability', null, null, null, 1, null, 0, '07-JUN-06', '07-JUN-06', 0, 'MXI' );

--changeSet 0fnc_account:2 stripComments:false
INSERT INTO FNC_ACCOUNT ( ACCOUNT_DB_ID, ACCOUNT_ID, NH_ACCOUNT_DB_ID, NH_ACCOUNT_ID, ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, ACCOUNT_CD, ACCOUNT_SDESC, ACCOUNT_LDESC, TCODE_DB_ID, TCODE_ID, DEFAULT_BOOL, EXT_KEY_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 20, null, null, 0, 'AP', 'AP', 'Accounts Payable', null, null, null, 1, null, 0, '07-JUN-06', '07-JUN-06', 0, 'MXI' );

--changeSet 0fnc_account:3 stripComments:false
INSERT INTO FNC_ACCOUNT ( ACCOUNT_DB_ID, ACCOUNT_ID, NH_ACCOUNT_DB_ID, NH_ACCOUNT_ID, ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, ACCOUNT_CD, ACCOUNT_SDESC, ACCOUNT_LDESC, TCODE_DB_ID, TCODE_ID, DEFAULT_BOOL, EXT_KEY_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 30, null, null, 0, 'CONSIGN', 'CONSIGN', 'Consignment Accrual Liability', null, null, null, 1, null, 0, '18-SEP-07', '18-SEP-07', 0, 'MXI' );

--changeSet 0fnc_account:4 stripComments:false
INSERT INTO FNC_ACCOUNT ( ACCOUNT_DB_ID, ACCOUNT_ID, NH_ACCOUNT_DB_ID, NH_ACCOUNT_ID, ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, ACCOUNT_CD, ACCOUNT_SDESC, ACCOUNT_LDESC, TCODE_DB_ID, TCODE_ID, DEFAULT_BOOL, EXT_KEY_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 60, null, null, 0, 'CHGFINTP', 'CHGFINTP', 'Finance Type Change Account', null, null, null, 1, null, 0, '18-JULY-16', '18-JULY-16', 0, 'MXI' );

--changeSet 0fnc_account:5 stripComments:false
-- Kits
INSERT INTO FNC_ACCOUNT ( ACCOUNT_DB_ID, ACCOUNT_ID, NH_ACCOUNT_DB_ID, NH_ACCOUNT_ID, ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, ACCOUNT_CD, ACCOUNT_SDESC, ACCOUNT_LDESC, TCODE_DB_ID, TCODE_ID, DEFAULT_BOOL, EXT_KEY_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 40, null, null, 0, 'GROUP', 'VOID', 'Transactions are not logged to this account', null, null, null, 1, null, 0, '12-DEC-07', '12-DEC-07', 0, 'MXI' );

--changeSet 0fnc_account:6 stripComments:false
-- Deployed Ops Blackout Data
insert into FNC_ACCOUNT (ACCOUNT_DB_ID, ACCOUNT_ID, NH_ACCOUNT_DB_ID, NH_ACCOUNT_ID, ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, ACCOUNT_CD, ACCOUNT_SDESC, ACCOUNT_LDESC, TCODE_DB_ID, TCODE_ID, DEFAULT_BOOL, EXT_KEY_SDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 50, null, null, 0, 'BLKOUT', 'BLKOUT', 'N/A', 'N/A', null, null, 0, null, 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');