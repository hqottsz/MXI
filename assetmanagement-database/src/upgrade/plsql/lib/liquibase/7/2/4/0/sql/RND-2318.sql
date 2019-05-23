--liquibase formatted sql


--changeSet RND-2318:1 stripComments:false
DELETE FROM utl_sequence WHERE sequence_cd = 'FRC_EVENT_LOG_ID';

--changeSet RND-2318:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('FRC_EVENT_LOG_ID');
END;
/

--changeSet RND-2318:3 stripComments:false
DELETE FROM utl_sequence WHERE sequence_cd = 'KIT_LINE_ID_SEQ';

--changeSet RND-2318:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('KIT_LINE_ID_SEQ');
END;
/

--changeSet RND-2318:5 stripComments:false
DELETE FROM utl_sequence WHERE sequence_cd = 'ADDRESS_ID';

--changeSet RND-2318:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('ADDRESS_ID');
END;
/

--changeSet RND-2318:7 stripComments:false
DELETE FROM utl_sequence WHERE sequence_cd = 'CONTACT_ID';

--changeSet RND-2318:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('CONTACT_ID');
END;
/

--changeSet RND-2318:9 stripComments:false
DELETE FROM utl_sequence WHERE sequence_cd = 'DPO_EXPORT_LOG_ID_SEQ';

--changeSet RND-2318:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('DPO_EXPORT_LOG_ID_SEQ');
END;
/

--changeSet RND-2318:11 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'DPO_EXPORT_LOG_ID_SEQ', 1, 'DPO_EXPORT_LOG', 'EXPORT_LOG_ID', 1 , 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'DPO_EXPORT_LOG_ID_SEQ');

--changeSet RND-2318:12 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('DPO_EXPORT_LOG_ID_SEQ', 1);
END;
/

--changeSet RND-2318:13 stripComments:false
DELETE FROM utl_sequence WHERE sequence_cd = 'DPO_IMPORT_LOG_ID_SEQ';

--changeSet RND-2318:14 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('DPO_IMPORT_LOG_ID_SEQ');
END;
/

--changeSet RND-2318:15 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'DPO_IMPORT_LOG_ID_SEQ', 1, 'DPO_EXPORT_LOG', 'IMPORT_LOG_ID', 1 , 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'DPO_IMPORT_LOG_ID_SEQ');

--changeSet RND-2318:16 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('DPO_IMPORT_LOG_ID_SEQ', 1);
END;
/