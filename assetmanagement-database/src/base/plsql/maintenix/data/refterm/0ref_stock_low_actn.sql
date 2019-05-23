--liquibase formatted sql


--changeSet 0ref_stock_low_actn:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_STOCK_LOW_ACTN"
** 0-Level
** DATE: 13-JUNE-2007
*********************************************/
INSERT INTO REF_STOCK_LOW_ACTN( STOCK_LOW_ACTN_DB_ID, STOCK_LOW_ACTN_CD, DESC_SDESC, DESC_LDESC, PROC_NAME_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'POREQ', 'Auto-Generate Purchase Request', 'This stock level will automatically generate a replenishment purchase request when it runs low.', NULL, 0, 01,  0, '12-MAY-01', '12-MAY-01', 100, 'MXI');

--changeSet 0ref_stock_low_actn:2 stripComments:false
INSERT INTO REF_STOCK_LOW_ACTN( STOCK_LOW_ACTN_DB_ID, STOCK_LOW_ACTN_CD, DESC_SDESC, DESC_LDESC, PROC_NAME_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'MANUAL', 'Manual Intervention', 'The stock level will be manually handled when it runs low.', NULL, 0, 01,  0, '12-MAY-01', '12-MAY-01', 100, 'MXI');

--changeSet 0ref_stock_low_actn:3 stripComments:false
INSERT INTO REF_STOCK_LOW_ACTN( STOCK_LOW_ACTN_DB_ID, STOCK_LOW_ACTN_CD, DESC_SDESC, DESC_LDESC, PROC_NAME_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'SHIPREQ', 'Shipping Request', 'The stock level will automatically send a shipping request to the supplier location when it runs low.', NULL, 0, 01,  0, '13-JUNE-07', '13-JUNE-07', 100, 'MXI');

--changeSet 0ref_stock_low_actn:4 stripComments:false
INSERT INTO REF_STOCK_LOW_ACTN( STOCK_LOW_ACTN_DB_ID, STOCK_LOW_ACTN_CD, DESC_SDESC, DESC_LDESC, PROC_NAME_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'DISTREQ', 'Stock Distribution Request', 'This stock level will automatically generate a stock distribution request when it runs low.', NULL, 0, 01,  0, '30-AUG-18', '30-AUG-18', 100, 'MXI');
