--liquibase formatted sql

--changeSet gtt_h_baseline_task:1 stripComments:false
CREATE GLOBAL TEMPORARY TABLE GTT_H_BASELINE_TASK(TASK_DB_ID NUMBER(10,0),TASK_ID NUMBER(10,0))ON COMMIT DELETE ROWS;

--changeSet gtt_h_baseline_task:2 stripComments:false
CREATE INDEX IX_GTTHBASELINETASK_TASK ON GTT_H_BASELINE_TASK (TASK_DB_ID, TASK_ID);
