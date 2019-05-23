--liquibase formatted sql


--changeSet 0ref_task_must_remove:1 stripComments:false
/****************************************************
** INSERT SCRIPT FOR TABLE "REF_TASK_MUST_REMOVE"
** 0-Level
** DATE: December 21, 2009
*****************************************************/
INSERT into REF_TASK_MUST_REMOVE (TASK_MUST_REMOVE_DB_ID, TASK_MUST_REMOVE_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'NA', 'N/A', 'Not Applicable', 'Not Applicable', 0, '21-DEC-09', '21-DEC-09', 0, 'MxI') ;

--changeSet 0ref_task_must_remove:2 stripComments:false
INSERT into REF_TASK_MUST_REMOVE (TASK_MUST_REMOVE_DB_ID, TASK_MUST_REMOVE_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'OFFPARENT', 'Off Parent', 'The task must be completed when uninstalled from next highest inventory.', 'The task must be completed when uninstalled from next highest inventory.', 0, '21-DEC-09', '21-DEC-09', 0, 'MxI');

--changeSet 0ref_task_must_remove:3 stripComments:false
INSERT into REF_TASK_MUST_REMOVE (TASK_MUST_REMOVE_DB_ID, TASK_MUST_REMOVE_CD, USER_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'OFFWING', 'Off Wing', 'The task must be completed when parent assembly is uninstalled from an aircraft.', 'The task must be completed when parent assembly is uninstalled from an aircraft.', 0, '21-DEC-09', '21-DEC-09', 0, 'MxI');