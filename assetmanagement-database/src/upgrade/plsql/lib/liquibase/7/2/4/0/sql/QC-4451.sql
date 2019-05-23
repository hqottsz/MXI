--liquibase formatted sql


--changeSet QC-4451:1 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'CAPACITY_STD_ID_SEQ' , 1 , 'LRP_LOC_CAP_STD' , 'CAPACITY_STD_ID' , 1 ,0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'CAPACITY_STD_ID_SEQ');

--changeSet QC-4451:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('CAPACITY_STD_ID_SEQ', 1);
END;
/ 

--changeSet QC-4451:3 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'CAPACITY_EXCEPT_ID_SEQ' , 1 , 'LRP_LOC_CAP_EXCEPT' , 'CAPACITY_EXCEPT_ID' , 1 ,0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'CAPACITY_EXCEPT_ID_SEQ');

--changeSet QC-4451:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('CAPACITY_EXCEPT_ID_SEQ', 1);
END;
/