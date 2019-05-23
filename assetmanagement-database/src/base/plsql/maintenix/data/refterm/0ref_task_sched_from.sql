--liquibase formatted sql


--changeSet 0ref_task_sched_from:1 stripComments:false
/************************************************
** 0-Level INSERT SCRIPT FOR REF_TASK_SCHED_FROM
*************************************************/
INSERT INTO REF_TASK_SCHED_FROM( TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'EFFECTIVE_DT', 'EFFECTIVE_DT', 'Effective Date', 'Schedule from the effective date of the task definition', 10, 0, TO_DATE('2013-07-19', 'YYYY-MM-DD'), TO_DATE('2013-07-19', 'YYYY-MM-DD'), 0, 'MXI' );

--changeSet 0ref_task_sched_from:2 stripComments:false
INSERT INTO REF_TASK_SCHED_FROM( TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'MP_ACTV_DT', 'MP_ACTV_DT', 'Maintenance Program Activation Date', 'Schedule from the maintenance program activation date', 20, 0, TO_DATE('2013-07-19', 'YYYY-MM-DD'), TO_DATE('2013-07-19', 'YYYY-MM-DD'), 0, 'MXI' );

--changeSet 0ref_task_sched_from:3 stripComments:false
INSERT INTO REF_TASK_SCHED_FROM( TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'MANUFACT_DT', 'MANUFACT_DT', 'Manufactured Date', 'Schedule from the manufactured date', 30, 0, TO_DATE('2013-07-19', 'YYYY-MM-DD'), TO_DATE('2013-07-19', 'YYYY-MM-DD'), 0, 'MXI' );

--changeSet 0ref_task_sched_from:4 stripComments:false
INSERT INTO REF_TASK_SCHED_FROM( TASK_SCHED_FROM_DB_ID, TASK_SCHED_FROM_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC,
DISPLAY_ORD, RSTAT_CD,CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER )
VALUES ( 0, 'RECEIVED_DT', 'RECEIVED_DT', 'Received Date', 'Schedule from the received date', 40, 0, TO_DATE('2013-07-19', 'YYYY-MM-DD'), TO_DATE('2013-07-19', 'YYYY-MM-DD'), 0, 'MXI' );