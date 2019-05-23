--liquibase formatted sql


--changeSet DEV-471:1 stripComments:false
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
   SELECT  'MAINT_PRGM_LOG_SEQ', 100000, 'MAINT_PRGM_LOG', 'MAINT_LOG_ID', 1, 0
   FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'MAINT_PRGM_LOG_SEQ');  

--changeSet DEV-471:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_CREATE('MAINT_PRGM_LOG_SEQ', 1);   
END;
/   

--changeSet DEV-471:3 stripComments:false
INSERT INTO
   REF_LOG_ACTION
   (
      LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 0,'MP_LOCK','Lock Maintenance Program','Lock Maintenance Program','MP_LOCK', 0 , TO_DATE('2010-06-23', 'YYYY-MM-DD') , TO_DATE('2010-06-23', 'YYYY-MM-DD') , 0 , 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LOG_ACTION WHERE LOG_ACTION_DB_ID = 0 and LOG_ACTION_CD = 'MP_LOCK' );    

--changeSet DEV-471:4 stripComments:false
INSERT INTO
   REF_LOG_ACTION
   (
      LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 0,'MP_UNLOCK','Unlock Maintenance Program','Unlock Maintenance Program','MP_UNLOCK', 0 , TO_DATE('2010-06-23', 'YYYY-MM-DD') , TO_DATE('2010-06-23', 'YYYY-MM-DD') , 0 , 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LOG_ACTION WHERE LOG_ACTION_DB_ID = 0 and LOG_ACTION_CD = 'MP_UNLOCK' );                  