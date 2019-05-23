--liquibase formatted sql

--changeSet OPER-24521:1 stripComments:false
INSERT INTO REF_STOCK_LOW_ACTN( STOCK_LOW_ACTN_DB_ID, STOCK_LOW_ACTN_CD, DESC_SDESC, DESC_LDESC, PROC_NAME_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   SELECT 0, 'DISTREQ', 'Stock Distribution Request', 'This stock level will automatically generate a stock distribution request when it runs low.', NULL, 0, 01,  0, TO_DATE('2018-08-31','YYYY-MM-DD'), TO_DATE('2018-08-31','YYYY-MM-DD'), 100, 'MXI'
   FROM DUAL
   WHERE NOT EXISTS (SELECT 1 FROM REF_STOCK_LOW_ACTN WHERE STOCK_LOW_ACTN_DB_ID = 0 AND STOCK_LOW_ACTN_CD = 'DISTREQ');

