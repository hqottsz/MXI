/********************************************
** INSERT SCRIPT FOR TABLE "REF_FINANCIAL_CLASS"
** 10-Level
** DATE: 09/22/2004 TIME: 00:00:00
*********************************************/
insert into REF_FINANCIAL_CLASS(FINANCIAL_CLASS_DB_ID, FINANCIAL_CLASS_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(10, 'ROTABLE', 'ROTABLE', 'These parts are usually the most expensive items in an airline, and therefore undergo the most rigorous tracking. They can be economically repaired to a fully serviceable condition, and are expected to last the life of the aircraft.', 0, 01, 0, 'ROTABLE', 0, '22-OCT-04', '22-OCT-04', 100, 'MXI');
insert into REF_FINANCIAL_CLASS(FINANCIAL_CLASS_DB_ID, FINANCIAL_CLASS_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(10, 'REPAIRABLE', 'REPAIRABLE', 'These parts can be economically repaired, but are not expected to last the life of the aircraft.', 0, 01, 0, 'ROTABLE', 0, '22-OCT-04', '22-OCT-04', 100, 'MXI');
insert into REF_FINANCIAL_CLASS(FINANCIAL_CLASS_DB_ID, FINANCIAL_CLASS_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(10, 'CONSUMABLE', 'CONSUMABLE', 'These parts cannot be repaired economically, and are expensed when they are installed.', 0, 01, 0, 'CONSUM', 0, '22-OCT-04', '22-OCT-04', 100, 'MXI');
insert into REF_FINANCIAL_CLASS(FINANCIAL_CLASS_DB_ID, FINANCIAL_CLASS_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, FINANCE_TYPE_DB_ID, FINANCE_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values(10, 'EXPENDABLE', 'EXPENDABLE', 'Fluids, glues, oils, fuels, etc that cannot be repaired (repairing glue?), and are expensed as they are used up.', 0, 01, 0, 'EXPENSE', 0, '22-OCT-04', '22-OCT-04', 100, 'MXI');