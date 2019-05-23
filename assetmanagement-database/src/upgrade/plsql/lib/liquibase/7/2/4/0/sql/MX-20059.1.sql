--liquibase formatted sql


--changeSet MX-20059.1:1 stripComments:false
  -- create the default triggers
  INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
     SELECT 
        99961, 'MX_QUEUE_REQ_UPDATE', 1, 'PLSQL', 'Triggered when the req_part.req_by_dt is updated in PLSQL logic. Class name is for info only.', 'TRG_MX_QUEUE_REQ_UPDATE', 0, 0
     FROM 
        DUAL 
    WHERE 
       NOT EXISTS (SELECT 1 FROM UTL_TRIGGER WHERE TRIGGER_CD = 'MX_QUEUE_REQ_UPDATE');       

--changeSet MX-20059.1:2 stripComments:false
  INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
     SELECT 
        99960, 'MX_PR_UPDATE_REQ_BY_DT', 1, 'COMPONENT', 'Triggered when the req_update_queue entry is processed.', 'com.mxi.mx.core.services.req.DefaultUpdateReqByDateTrigger', 0, 0
     FROM 
        DUAL 
     WHERE 
        NOT EXISTS (SELECT 1 FROM UTL_TRIGGER WHERE TRIGGER_CD = 'MX_PR_UPDATE_REQ_BY_DT');  

--changeSet MX-20059.1:3 stripComments:false
  -- create work item type  
  INSERT INTO utl_work_item_type ( name, worker_class, work_manager, enabled, utl_id )
     SELECT
        'REQ_PART_UPDATE_QUEUE', 'com.mxi.mx.core.worker.req.ReqPartUpdateQueueWorker', 'wm/Maintenix-DefaultWorkManager', 0, 0
     FROM
        DUAL
     WHERE
        NOT EXISTS (SELECT 1 FROM UTL_WORK_ITEM_TYPE WHERE NAME = 'REQ_PART_UPDATE_QUEUE');