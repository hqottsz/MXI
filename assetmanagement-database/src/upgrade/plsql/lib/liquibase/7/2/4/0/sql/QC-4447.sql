--liquibase formatted sql


--changeSet QC-4447:1 stripComments:false
INSERT INTO
   REF_LOG_ACTION
   (
      LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 0,'TDTOGGLEON','Toggle Workscope Order Set to TRUE','Toggle Workscope Order Set to TRUE','TDTOGGLEON', 0 , TO_DATE('2010-03-10', 'YYYY-MM-DD') , TO_DATE('2010-03-10', 'YYYY-MM-DD') , 0 , 'MXI' 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LOG_ACTION WHERE LOG_ACTION_DB_ID = 0 and LOG_ACTION_CD = 'TDTOGGLEON' );            

--changeSet QC-4447:2 stripComments:false
INSERT INTO
   REF_LOG_ACTION
   (
      LOG_ACTION_DB_ID, LOG_ACTION_CD,DESC_SDESC,DESC_LDESC,USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 0,'TDTOGGLEOFF','Toggle Workscope Order Set to FALSE','Toggle Workscope Order Set to FALSE','TDTOGGLEOFF', 0 , TO_DATE('2010-03-10', 'YYYY-MM-DD') , TO_DATE('2010-03-10', 'YYYY-MM-DD') , 0 , 'MXI' 
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_LOG_ACTION WHERE LOG_ACTION_DB_ID = 0 and LOG_ACTION_CD = 'TDTOGGLEOFF' );                