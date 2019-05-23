--liquibase formatted sql

--changeset OPER-30404-001:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add the new work item types
BEGIN
   BEGIN
      INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
      VALUES ( 'USAGE_PARAMETER_SYNC', 'com.mxi.mx.core.worker.usgrecord.UsageRecordParameterSyncWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0 );
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;

   BEGIN
      INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
      VALUES ( 'LICENSE_DEFN_VALIDATION', 'com.mxi.mx.core.worker.licensedefn.LicenseDefnValidationWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,300 );
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;   

   BEGIN
      INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
      VALUES ( 'BATCH_COMPLETE_TASKS', 'com.mxi.mx.core.worker.task.BatchCompleteTasksWorker', 'wm/Maintenix-BatchCompletionWorkManager',1,0,500,0,0 );
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;

   BEGIN
      INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
      VALUES ( 'COMPLETE_TASK', 'com.mxi.mx.core.worker.task.CompleteTaskWorker', 'wm/Maintenix-BatchCompletionWorkManager',1,2,500,0,0 );
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;

   BEGIN
      INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
      VALUES ( 'EVAL_INV_COMPLETENESS', 'com.mxi.mx.core.worker.inventory.EvaluateInventoryCompletenessWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,60 );
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;

   BEGIN
      INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
      VALUES ( 'LINE_PLANNING_AUTOMATION', 'com.mxi.mx.core.worker.lpa.LinePlanningAutomationWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0 );
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;

   BEGIN
      INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
      VALUES ( 'TASK_TREE_DEADLINE_UPDATE', 'com.mxi.mx.core.worker.updatedeadline.TaskTreeDeadlineWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0 );
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;
   
   BEGIN
      INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
      (SELECT UPPER(name), worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit
       FROM utl_work_item_type
       WHERE utl_work_item_type.name
           IN ( 'Job_Aircraft_Deadline_Update',
                'Real_Time_Aircraft_Deadline_Update',
                'Job_Inventory_Deadline_Update',
                'Real_Time_Inventory_Deadline_Update') 
        );
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;

END;
/

--changeset OPER-30404-001:2 stripComments:false
--comment Rename all of the related types to match the upper case naming convension of the other work items
UPDATE UTL_WORK_ITEM
SET TYPE = UPPER(TYPE)
WHERE UTL_WORK_ITEM.TYPE IN 
('Job_Aircraft_Deadline_Update',
 'Real_Time_Aircraft_Deadline_Update',
 'Job_Inventory_Deadline_Update',
 'Real_Time_Inventory_Deadline_Update');
 
--changeset OPER-30404-001:3 stripComments:false
--comment Drop the old work item types
DELETE
FROM UTL_WORK_ITEM_TYPE
WHERE UTL_WORK_ITEM_TYPE.NAME IN 
('Job_Aircraft_Deadline_Update',
 'Real_Time_Aircraft_Deadline_Update',
 'Job_Inventory_Deadline_Update',
 'Real_Time_Inventory_Deadline_Update');

--changeset OPER-30404-001:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_drop('UTL_ASYNC_ACTION');
   utl_migr_schema_pkg.table_drop('UTL_ASYNC_ACTION_TYPE');
   utl_migr_schema_pkg.table_drop('UTL_ASYNC_ACTION_STATUS');
END;
/

--changeset OPER-30404-001:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('BATCH_COMPLETE_TASKS_ID_SEQ', 1);
   BEGIN
      INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
      VALUES ( 'BATCH_COMPLETE_TASKS_ID_SEQ', 1, 'UTL_WORK_ITEM', 'KEY',1 ,0);
   EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
      NULL;
   END;
END;
/

--changeset OPER-30404-001:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add table to store business errors for work package tasks
BEGIN
   utl_migr_schema_pkg.table_create('
      create table SCHED_WP_ERROR (
           WP_SCHED_DB_ID          NUMBER(10) NOT NULL,
           WP_SCHED_ID             NUMBER(10) NOT NULL,
           TASK_SCHED_DB_ID        NUMBER(10) NOT NULL,
           TASK_SCHED_ID           NUMBER(10) NOT NULL,
           WORKSCOPE_BOOL          NUMBER(1) DEFAULT 0 NOT NULL,
           ERROR_MSG               CLOB NOT NULL,
           ERROR_DT                DATE NOT NULL,
           CREATION_DT             DATE NOT NULL,
           REVISION_DT             DATE NOT NULL,
           REVISION_DB_ID          NUMBER(10) NOT NULL,
           REVISION_USER           VARCHAR2(30) NOT NULL
         )
   ');
   
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_WP_ERROR ADD CONSTRAINT PK_SCHED_WP_ERROR PRIMARY KEY (WP_SCHED_DB_ID, WP_SCHED_ID, TASK_SCHED_DB_ID, TASK_SCHED_ID)
   ');
   
   utl_migr_schema_pkg.index_create('
      CREATE INDEX IX_SCHEDWPERROR_SCHEDWP ON SCHED_WP_ERROR (WP_SCHED_DB_ID, WP_SCHED_ID ASC)
   ');
   
   utl_migr_schema_pkg.index_create('
      CREATE INDEX IX_SCHEDWPERROR_SCHEDSTASK ON SCHED_WP_ERROR (TASK_SCHED_DB_ID, TASK_SCHED_ID ASC)
   ');
   
   utl_migr_schema_pkg.index_create('
      CREATE INDEX IX_SCHEDWPERROR_WORKSCOPEBOOL ON SCHED_WP_ERROR (WORKSCOPE_BOOL ASC)
   ');
   
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_WP_ERROR ADD CONSTRAINT FK_SCHEDWPERROR_SCHEDWP FOREIGN KEY (WP_SCHED_DB_ID, WP_SCHED_ID) REFERENCES SCHED_WP (SCHED_DB_ID,SCHED_ID) NOT DEFERRABLE
   ');
   
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_WP_ERROR ADD CONSTRAINT FK_SCHEDWPERROR_SCHEDSTASK FOREIGN KEY (TASK_SCHED_DB_ID, TASK_SCHED_ID) REFERENCES SCHED_STASK (SCHED_DB_ID,SCHED_ID) NOT DEFERRABLE
   ');
   
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE SCHED_WP_ERROR ADD CHECK  (WORKSCOPE_BOOL IN (0,1))
   ');
   
END;
/

--changeset OPER-30404-001:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SCHED_WP_ERROR" BEFORE INSERT
   ON "SCHED_WP_ERROR" REFERENCING NEW AS NEW FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
  application_object_pkg.setinsertaudit(
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
END;
/

--changeset OPER-30404-001:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SCHED_WP_ERROR" BEFORE UPDATE
   ON "SCHED_WP_ERROR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
BEGIN
  application_object_pkg.setupdateaudit(
    0,
    0,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
END;
/

--changeSet OPER-30404-001:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  BEGIN
    INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
    VALUES(272,'core.alert.BATCH_COMPLETE_TASKS_COMPLETED_name','core.alert.BATCH_COMPLETE_TASKS_COMPLETED_description','PRIVATE',NULL,'TASK','core.alert.BATCH_COMPLETE_TASKS_COMPLETED_message',1,0,NULL,1,0);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
  BEGIN
    INSERT INTO utl_alert_type(alert_type_id,alert_name,alert_ldesc,notify_cd,notify_class,category,message,key_bool,priority,priority_calc_class,active_bool,utl_id)
    VALUES(273,'core.alert.BATCH_COMPLETE_TASKS_FAILED_name','core.alert.BATCH_COMPLETE_TASKS_FAILED_description','PRIVATE',NULL,'TASK','core.alert.BATCH_COMPLETE_TASKS_FAILED_message',1,0,NULL,1,0);
  EXCEPTION WHEN DUP_VAL_ON_INDEX THEN
    NULL;
  END;
END;
/

--changeSet OPER-30404-001:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment remove the previous async action alert data for batch complete tasks
BEGIN
   -- Delete BatchCompleteSuccessAlert, BatchCompleteFailedAlert alert parm data
   DELETE FROM
      utl_alert_parm
   WHERE EXISTS
       (SELECT
           NULL
        FROM 
           utl_alert 
        WHERE
           utl_alert.alert_id = utl_alert_parm.alert_id AND
           utl_alert.alert_type_id IN (164,165)
       );
     
   -- Delete BatchCompleteSuccessAlert, BatchCompleteFailedAlert alert log data
   DELETE FROM
      utl_alert_log  
   WHERE EXISTS
       (SELECT
           NULL
        FROM 
           utl_alert 
        WHERE
           utl_alert.alert_id = utl_alert_log.alert_id AND
           utl_alert.alert_type_id IN (164,165)
       );
    
   -- Delete BatchCompleteSuccessAlert, BatchCompleteFailedAlert alert status log data
   DELETE FROM 
      utl_alert_status_log
   WHERE EXISTS
       (SELECT
           NULL
        FROM 
           utl_alert 
        WHERE
           utl_alert.alert_id = utl_alert_status_log.alert_id AND
           utl_alert.alert_type_id IN (164,165)
       );

   -- Delete BatchCompleteSuccessAlert, BatchCompleteFailedAlert assigned to users data
   DELETE FROM
      utl_user_alert
   WHERE EXISTS
       (SELECT
           NULL
        FROM 
           utl_alert 
        WHERE
           utl_alert.alert_id = utl_user_alert.alert_id AND
           utl_alert.alert_type_id IN (164,165)
       );
    
   -- Delete BatchCompleteSuccessAlert, BatchCompleteFailedAlert alert data
   DELETE FROM
      utl_alert
   WHERE utl_alert.alert_type_id IN (164,165);

   -- Delete BatchCompleteSuccessAlert, BatchCompleteFailedAlert from any assigned roles
   -- It is a private alert, this is just for saftey
   DELETE FROM
      utl_alert_type_role
   WHERE
      utl_alert_type_role.alert_type_id IN (164,165);

   -- Delete BatchCompleteSuccessAlert, BatchCompleteFailedAlert alert type
   DELETE FROM 
      utl_alert_type
   WHERE utl_alert_type.alert_type_id IN (164,165);
END;
/

--changeSet OPER-30404-001:11 stripComments:false
--comment remove the cleanup job for async actions
DELETE FROM
   utl_job
WHERE
   job_cd = 'MX_CORE_DELETE_OLD_ASYNC_ACTIONS';

--changeSet OPER-30404-001:12 stripComments:false
--comment remove the purge policy for async actions
DELETE FROM
   utl_purge_strategy
WHERE
   purge_policy_cd = 'ASYNC_FAILED' AND
   purge_table_cd = 'UTL_ASYNC_ACTION';

--changeSet OPER-30404-001:13 stripComments:false
--comment remove the purge policy for async actions
DELETE FROM
   utl_purge_policy
WHERE
   purge_policy_cd = 'ASYNC_FAILED' AND
   purge_group_cd = 'ASYNC_ACTION';

--changeSet OPER-30404-001:14 stripComments:false
--comment remove the purge policy for async actions
DELETE FROM
   utl_purge_group
WHERE
   purge_group_cd = 'ASYNC_ACTION';
   
--changeSet OPER-30404-001:15 stripComments:false
--comment remove the purge policy for async actions
DELETE FROM
   utl_purge_table
WHERE
   purge_table_cd = 'UTL_ASYNC_ACTION';