--liquibase formatted sql


--changeSet DEV-411:1 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'LRP_PLAN_TYPE_ID_SEQ' , 1 , 'LRP_PLAN_TYPE' , 'LRP_PLAN_TYPE_ID' , 1 ,0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'LRP_PLAN_TYPE_ID_SEQ');  

--changeSet DEV-411:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('LRP_PLAN_TYPE_ID_SEQ', 1);   
END;
/