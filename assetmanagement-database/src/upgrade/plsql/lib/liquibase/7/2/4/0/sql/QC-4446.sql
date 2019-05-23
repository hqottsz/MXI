--liquibase formatted sql


--changeSet QC-4446:1 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'USER_SHIFT_PATTERN_ID_SEQ', 1, 'USER_SHIFT_PATTERN', 'USER_SHIFT_PATTERN_ID' , 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'USER_SHIFT_PATTERN_ID_SEQ');   

--changeSet QC-4446:2 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'CAPACITY_PATTERN_ID_SEQ', 1, 'CAPACITY_PATTERN', 'CAPACITY_PATTERN_ID' , 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'CAPACITY_PATTERN_ID_SEQ');   

--changeSet QC-4446:3 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'ORG_CONTACT_ID_SEQ', 100000, 'ORG_CONTACT', 'CONTACT_ID' , 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'ORG_CONTACT_ID_SEQ');   

--changeSet QC-4446:4 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'ORG_ADDRESS_ID_SEQ', 100000, 'ORG_ADDRESS', 'ADDRESS_ID' , 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'ORG_ADDRESS_ID_SEQ');   

--changeSet QC-4446:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('USER_SHIFT_PATTERN_ID_SEQ', 1);
END;
/

--changeSet QC-4446:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('CAPACITY_PATTERN_ID_SEQ', 1);
END;
/

--changeSet QC-4446:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('ORG_CONTACT_ID_SEQ', 100000);
END;
/

--changeSet QC-4446:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('ORG_ADDRESS_ID_SEQ', 100000);
END;
/