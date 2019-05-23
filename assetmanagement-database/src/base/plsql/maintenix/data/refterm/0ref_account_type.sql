--liquibase formatted sql


--changeSet 0ref_account_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_ACCOUNT_TYPE"
** DATE: 03-MARCH-2005 TIME: 00:00:00
*********************************************/
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'GROUP', 'GROUP', 'This category is used for accounts that are merely used for grouping and reporting. Transactions are not logged directly to this account.', 0, '15-MAR-05', '15-MAR-05', 0, 'MXI');

--changeSet 0ref_account_type:2 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'EXPENSE', 'EXPENSE', 'This category is used for any type of expense or charge.', 0, '15-MAR-05', '15-MAR-05', 0, 'MXI');

--changeSet 0ref_account_type:3 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'INVASSET', 'INVASSET', 'This category is used for a parts inventory asset account (consumables).', 0, '15-MAR-05', '15-MAR-05', 0, 'MXI');

--changeSet 0ref_account_type:4 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'FIXASSET', 'FIXASSET', 'This category is used for a part''s fixed asset account (rotables).', 0, '15-MAR-05', '15-MAR-05', 0, 'MXI');

--changeSet 0ref_account_type:5 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'INVOICE', 'INVOICE', 'This category is used for invoice accrual accounts.', 0, '15-MAR-05', '15-MAR-05', 0, 'MXI');

--changeSet 0ref_account_type:6 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'AP', 'AP', 'This category is used for a vendors accounts payable account.', 0, '15-MAR-05', '15-MAR-05', 0, 'MXI');

--changeSet 0ref_account_type:7 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'SCRAP', 'SCRAP', 'This category is used for scrapping expenses.', 0, '06-JUN-06', '06-JUN-06', 0, 'MXI');

--changeSet 0ref_account_type:8 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ADJQTY', 'ADJQTY', 'This category is used for quantity adjustment expenses.', 0, '06-JUN-06', '06-JUN-06', 0, 'MXI');

--changeSet 0ref_account_type:9 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'ADJPRICE', 'ADJPRICE', 'This category is used for price adjustment expenses.', 0, '06-JUN-06', '06-JUN-06', 0, 'MXI');

--changeSet 0ref_account_type:10 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'RTVCRED', 'RTVCRED', 'This category is used for returning to vendor credit.', 0, '06-JUN-06', '06-JUN-06', 0, 'MXI');

--changeSet 0ref_account_type:11 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CHGOWN', 'CHGOWN', 'This category is used for changing current owner to a new owner.', 0, '16-MAR-07', '16-MAR-07', 0, 'MXI');

--changeSet 0ref_account_type:12 stripComments:false
insert into ref_account_type( ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CONSIGN', 'CONSIGN', 'This category is used to accrue consignment liability.', 0, '18-SEP-2007', '18-SEP-2007', 0, 'MXI');

--changeSet 0ref_account_type:13 stripComments:false
insert into ref_account_type (ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'CHGFINTP', 'CHGFINTP', 'This category is used for finance type change account.', 0, '18-JULY-16', '18-JULY-16', 0, 'MXI');

--changeSet 0ref_account_type:14 stripComments:false
insert into REF_ACCOUNT_TYPE (ACCOUNT_TYPE_DB_ID, ACCOUNT_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'BLKOUT', 'BLKOUT', 'N/A', 3, to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-03-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');