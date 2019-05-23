--liquibase formatted sql


--changeSet DEV-1434:1 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'TAX_LOG_SEQ' , 100000 , 'TAX_LOG' , 'TAX_LOG_ORDER' , 1 ,0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'TAX_LOG_SEQ');

--changeSet DEV-1434:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('TAX_LOG_SEQ', 1);
END;
/

--changeSet DEV-1434:3 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'CHARGE_LOG_SEQ' , 100000 , 'CHARGE_LOG' , 'CHARGE_LOG_ORDER' , 1 ,0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'CHARGE_LOG_SEQ');

--changeSet DEV-1434:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('CHARGE_LOG_SEQ', 1);
END;
/