--liquibase formatted sql


--changeSet 0ref_sched_from:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_SCHED_FROM"
** 0-Level
** DATE: 30-AUG-04
*********************************************/
INSERT INTO REF_SCHED_FROM( SCHED_FROM_DB_ID, SCHED_FROM_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'LASTEND', 'Previous Task Completed', 'Schedule from the completion date/usage of the previous task', 0, 01,  0, '17-AUG-04', '17-AUG-04', 100, 'MXI' );

--changeSet 0ref_sched_from:2 stripComments:false
INSERT INTO REF_SCHED_FROM( SCHED_FROM_DB_ID, SCHED_FROM_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'LASTDUE', 'Previous Task Due', 'Schedule from the scheduled due date/usage of the previous task', 0, 01,  0, '17-AUG-04', '17-AUG-04', 100, 'MXI' );

--changeSet 0ref_sched_from:3 stripComments:false
INSERT INTO REF_SCHED_FROM( SCHED_FROM_DB_ID, SCHED_FROM_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'CUSTOM', 'Customized Start', 'Schedule from user-specified date/usage', 0, 01,  0, '17-AUG-04', '17-AUG-04', 100, 'MXI' );

--changeSet 0ref_sched_from:4 stripComments:false
INSERT INTO REF_SCHED_FROM( SCHED_FROM_DB_ID, SCHED_FROM_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'BIRTH', 'Manufactured/Received', 'Schedule from the manufactured or received date/usage of the equipment',  0, 01, 0, '17-AUG-04', '17-AUG-04', 100, 'MXI' );

--changeSet 0ref_sched_from:5 stripComments:false
INSERT INTO REF_SCHED_FROM( SCHED_FROM_DB_ID, SCHED_FROM_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'EFFECTIV', 'Task Definition Effectivity', 'Schedule from the effectivity date of the task definition',  0, 01, 0, '17-AUG-04', '17-AUG-04', 100, 'MXI' );

--changeSet 0ref_sched_from:6 stripComments:false
INSERT INTO REF_SCHED_FROM( SCHED_FROM_DB_ID, SCHED_FROM_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'WPSTART', 'Work Package Start Date', 'Schedule from the work package start date',  0, 01, 0, '24-MAR-09', '24-MAR-09', 100, 'MXI' );

--changeSet 0ref_sched_from:7 stripComments:false
INSERT INTO REF_SCHED_FROM( SCHED_FROM_DB_ID, SCHED_FROM_CD, DESC_SDESC, DESC_LDESC, BITMAP_DB_ID, BITMAP_TAG, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
VALUES (0, 'WPEND', 'Work Package End Date', 'Schedule from the work package end date',  0, 01, 0, '24-MAR-09', '24-MAR-09', 100, 'MXI' );