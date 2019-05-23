--liquibase formatted sql


--changeSet 0utl_work_item_type:1 stripComments:false
-- The update of deadlines on aircrafts called from the job
/*************************************************
** 0-LEVEL INSERT SCRIPT FOR TABLE UTL_WORK_ITEM_TYPE
**************************************************/
/***********************
** Deadline Update
************************/
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'JOB_AIRCRAFT_DEADLINE_UPDATE', 'com.mxi.mx.core.worker.updatedeadline.AircraftDeadlineWorker',  'wm/Maintenix-AircraftDeadlinesJobWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:2 stripComments:false
-- The update of deadlines on aircrafts called from the business logic
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'REAL_TIME_AIRCRAFT_DEADLINE_UPDATE',    'com.mxi.mx.core.worker.updatedeadline.AircraftDeadlineWorker',  'wm/Maintenix-AircraftDeadlinesRealTimeWorkManager',1,10,500,0,0);

--changeSet 0utl_work_item_type:3 stripComments:false
-- The update of deadlines on loose inventory called from the job
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'JOB_INVENTORY_DEADLINE_UPDATE', 'com.mxi.mx.core.worker.updatedeadline.InventoryDeadlineWorker', 'wm/Maintenix-InventoryDeadlinesJobWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:4 stripComments:false
-- The update of deadlines on loose inventory called from the business logic
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'REAL_TIME_INVENTORY_DEADLINE_UPDATE',    'com.mxi.mx.core.worker.updatedeadline.InventoryDeadlineWorker', 'wm/Maintenix-InventoryDeadlinesRealTimeWorkManager',1,10,500,0,0);

--changeSet 0utl_work_item_type:5 stripComments:false
-- Worker to determine which tasks for an inventory need to be synchronized
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'INVENTORY_SYNC',    'com.mxi.mx.core.worker.bsync.TaskSynchronizationWorker', 'wm/Maintenix-BaselineSyncWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:6 stripComments:false
-- Worker to  update a specific task to a new revision for baseline synchronization
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'UPDATE_TASK',    'com.mxi.mx.core.worker.bsync.UpdateTaskWorker', 'wm/Maintenix-BaselineSyncWorkManager',1,0,500,0,300);

--changeSet 0utl_work_item_type:7 stripComments:false
-- Worker to  cancel a task for baseline synchronization
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'CANCEL_TASK',    'com.mxi.mx.core.worker.bsync.CancelTaskWorker', 'wm/Maintenix-BaselineSyncWorkManager',1,0,500,0,300);

--changeSet 0utl_work_item_type:8 stripComments:false
-- Worker to initialize a task for baseline synchronization
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'INITIALIZE_TASK',    'com.mxi.mx.core.worker.bsync.InitializeTaskWorker', 'wm/Maintenix-BaselineSyncWorkManager',1,0,500,0,300);

--changeSet 0utl_work_item_type:9 stripComments:false
-- Worker to process a zip queue for baseline synchronization
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'ZIP_QUEUE',    'com.mxi.mx.core.worker.bsync.ZipQueueWorker', 'wm/Maintenix-BaselineSyncWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:10 stripComments:false
-- Worker to zip a task to the proper parent for baseline synchronization
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'ZIP_TASK',    'com.mxi.mx.core.worker.bsync.ZipTaskWorker', 'wm/Maintenix-BaselineSyncWorkManager',1,0,500,0,300);

--changeSet 0utl_work_item_type:11 stripComments:false
-- Worker to calculate the aircraft operating status
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'CALCULATE_AIRCRAFT_OPERATING_STATUS',    'com.mxi.mx.core.services.inventory.oper.CalculateAircraftOperatingStatusWorker', 'wm/Maintenix-CalculateAircraftOperatingStatusWorkManager',1,0,500,0,300);

--changeSet 0utl_work_item_type:12 stripComments:false
-- Worker to update IETMs of Task
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'UPDATE_IETM','com.mxi.mx.core.worker.ietm.UpdateIETMWorker', 'wm/Maintenix-UpdateIETMWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:13 stripComments:false
-- Worker to handle TaxRateModified event
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'Tax_Rate_Modified', 'com.mxi.mx.core.worker.procurement.TaxRateModifiedWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:14 stripComments:false
-- Worker to handle TaxChargeVendorRelationshipModified event
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'Tax_Charge_Vendor_Modified', 'com.mxi.mx.core.worker.procurement.TaxChargeVendorModifiedWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:15 stripComments:false
-- Worker for basic line planning automation
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'BASIC_LINE_PLANNING_AUTOMATION', 'com.mxi.mx.core.worker.lpa.basic.BasicLinePlanningAutomationWorker', 'wm/Maintenix-BasicLinePlanningAutomationWorkManager',1,500,0,0);

--changeSet 0utl_work_item_type:16 stripComments:false
-- Worker to handle supply location StockLevelCheck event
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'STOCK_LEVEL_CHECK', 'com.mxi.mx.core.services.stocklevel.StockLevelCheckWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:17 stripComments:false
-- Worker to handle warehouse StockLevelCheck event
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit  )
VALUES ( 'WAREHOUSE_STOCK_LEVEL_CHECK', 'com.mxi.mx.core.services.stocklevel.WarehouseStockLevelCheckWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:18 stripComments:false
-- Worker to handle print jobs
INSERT INTO utl_work_item_type(name,worker_class,work_manager,enabled,max_retry_ct,retry_interval,scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES('PRINT_JOB','com.mxi.mx.core.worker.print.PrintJobWorker','wm/Maintenix-PrintingWorkManager',1,0,2,500,0,0);

--changeSet 0utl_work_item_type:19 stripComments:false
-- Worker to handle print job child work items
INSERT INTO utl_work_item_type(name,worker_class,work_manager,enabled,max_retry_ct,retry_interval,scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES('PRINT_ITEM','com.mxi.mx.core.worker.print.PrintItemWorker','wm/Maintenix-PrintingWorkManager',1,5,15,500,0,0);

--changeSet 0utl_work_item_type:21 stripComments:false
-- Worker to handle CSV import work items
INSERT INTO utl_work_item_type( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES( 'BULK_LOAD_DATA','com.mxi.mx.core.services.dataservices.BulkLoadDataWorker','wm/Maintenix-DefaultWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:22 stripComments:false
-- Worker for baseline sync validate actuals
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES('VALIDATE_TASK_ACTUALS','com.mxi.mx.core.worker.bsync.ValidateTaskActualsWorker','wm/Maintenix-BaselineSyncWorkManager',1,0,500,0,0);

--changeSet 0utl_work_item_type:23 stripComments:false
-- Worker for synchronizing usage parameters
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'USAGE_PARAMETER_SYNC', 'com.mxi.mx.core.worker.usgrecord.UsageRecordParameterSyncWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0 );

--changeSet 0utl_work_item_type:24 stripComments:false
-- Worker for validation of license definitions
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'LICENSE_DEFN_VALIDATION', 'com.mxi.mx.core.worker.licensedefn.LicenseDefnValidationWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,300 );

--changeSet 0utl_work_item_type:25 stripComments:false
-- Worker for batch completing groups of tasks
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'BATCH_COMPLETE_TASKS', 'com.mxi.mx.core.worker.task.BatchCompleteTasksWorker', 'wm/Maintenix-BatchCompletionWorkManager',1,0,500,0,0 );

--changeSet 0utl_work_item_type:26 stripComments:false
-- Worker for completing single tasks
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'COMPLETE_TASK', 'com.mxi.mx.core.worker.task.CompleteTaskWorker', 'wm/Maintenix-BatchCompletionWorkManager',1,2,500,0,0 );

--changeSet 0utl_work_item_type:27 stripComments:false
-- Worker for evaluation of inventory completeness
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'EVAL_INV_COMPLETENESS', 'com.mxi.mx.core.worker.inventory.EvaluateInventoryCompletenessWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,60 );

--changeSet 0utl_work_item_type:28 stripComments:false
-- Worker for line planning automation
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'LINE_PLANNING_AUTOMATION', 'com.mxi.mx.core.worker.lpa.LinePlanningAutomationWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0 );

--changeSet 0utl_work_item_type:29 stripComments:false
-- Worker for updating task deadlines
INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, max_retry_ct, scheduled_buffer, utl_id, batch_execution_time_limit )
VALUES ( 'TASK_TREE_DEADLINE_UPDATE', 'com.mxi.mx.core.worker.updatedeadline.TaskTreeDeadlineWorker', 'wm/Maintenix-DefaultWorkManager',1,0,500,0,0 );