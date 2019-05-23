--liquibase formatted sql
  

--changeSet DEV-42:1 stripComments:false
INSERT INTO
   REF_TASK_MUST_REMOVE
   (
      TASK_MUST_REMOVE_DB_ID, TASK_MUST_REMOVE_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 0, 'NA', 'NA', 'Not Applicable', 'Not Applicable', 0, TO_DATE('2009-12-21', 'YYYY-MM-DD'), TO_DATE('2009-12-21', 'YYYY-MM-DD'), 0, 'MxI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_TASK_MUST_REMOVE WHERE TASK_MUST_REMOVE_DB_ID = 0 and TASK_MUST_REMOVE_CD = 'NA' );

--changeSet DEV-42:2 stripComments:false
INSERT INTO
   REF_TASK_MUST_REMOVE
   (
      TASK_MUST_REMOVE_DB_ID, TASK_MUST_REMOVE_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 0, 'OFFPARENT', 'Off Parent', 'The task must be completed when uninstalled from next highest inventory.', 'The task must be completed when uninstalled from next highest inventory.', 0, TO_DATE('2009-12-21', 'YYYY-MM-DD'), TO_DATE('2009-12-21', 'YYYY-MM-DD'), 0, 'MxI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_TASK_MUST_REMOVE WHERE TASK_MUST_REMOVE_DB_ID = 0 and TASK_MUST_REMOVE_CD = 'OFFPARENT' );

--changeSet DEV-42:3 stripComments:false
INSERT INTO
   REF_TASK_MUST_REMOVE
   (
      TASK_MUST_REMOVE_DB_ID, TASK_MUST_REMOVE_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 0, 'OFFWING', 'Off Wing', 'The task must be completed when parent assembly is uninstalled from an aircraft.', 'The task must be completed when parent assembly is uninstalled from an aircraft.', 0, TO_DATE('2009-12-21', 'YYYY-MM-DD'), TO_DATE('2009-12-21', 'YYYY-MM-DD'), 0, 'MxI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM REF_TASK_MUST_REMOVE WHERE TASK_MUST_REMOVE_DB_ID = 0 and TASK_MUST_REMOVE_CD = 'OFFWING' );