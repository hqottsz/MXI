--liquibase formatted sql


--changeSet QC-4218:1 stripComments:false
UPDATE REF_TASK_MUST_REMOVE
  SET USER_CD = 'N/A'
  WHERE TASK_MUST_REMOVE_DB_ID = 0 AND TASK_MUST_REMOVE_CD = 'NA';