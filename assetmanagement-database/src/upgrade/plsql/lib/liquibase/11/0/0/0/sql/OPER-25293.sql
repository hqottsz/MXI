--liquibase formatted sql

--changeSet OPER-25293:1 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, oracle_seq, utl_id )
SELECT  'STOCK_DISTREQ_BARCODE_SEQ', 100000, 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'STOCK_DISTREQ_BARCODE_SEQ');

 
--changeSet OPER-25293:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('STOCK_DISTREQ_BARCODE_SEQ', 100000);
END;
/