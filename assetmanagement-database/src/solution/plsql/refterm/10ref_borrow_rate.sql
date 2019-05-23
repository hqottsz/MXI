/********************************************
** INSERT SCRIPT FOR TABLE "REF_BORROW_RATE"
** 10-Level
** DATE: 21-MAR-2007 TIME: 00:00:00
*********************************************/
insert into REF_BORROW_RATE (BORROW_RATE_DB_ID, BORROW_RATE_CD, DESC_SDESC, DESC_LDESC, CLASS_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'IATA', 'Standard IATA Rate', '8% base price + 1% per day (days 1-10) + 2% per day (days 11-20) + 3% per day thereafter', 'com.mxi.mx.core.plugin.borrowrate.StandardIATABorrowRate', 0, to_date('15-03-2007', 'dd-mm-yyyy'), to_date('15-03-2007', 'dd-mm-yyyy'), 10, 'mxi');

insert into REF_BORROW_RATE (BORROW_RATE_DB_ID, BORROW_RATE_CD, DESC_SDESC, DESC_LDESC, CLASS_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (10, 'N/A', 'Unknown Rate', 'Always returns 0', 'com.mxi.mx.core.plugin.borrowrate.UnknownBorrowRate', 0, to_date('15-03-2007', 'dd-mm-yyyy'), to_date('15-03-2007', 'dd-mm-yyyy'), 10, 'mxi');
