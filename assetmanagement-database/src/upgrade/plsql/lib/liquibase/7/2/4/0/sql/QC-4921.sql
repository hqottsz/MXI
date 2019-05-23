--liquibase formatted sql


--changeSet QC-4921:1 stripComments:false
UPDATE UTL_TRIGGER
  SET TRIGGER_CD = 'MX_TS_REQUEST_PARTS'
      , TRIGGER_NAME = 'part request by work order after request parts'
 WHERE TRIGGER_ID = 99975; 

--changeSet QC-4921:2 stripComments:false
UPDATE UTL_TRIGGER
  SET TRIGGER_CD = 'MX_TS_CREATE'
      , TRIGGER_NAME = 'part request by task after initialize task definition which creats task on inventory'
 WHERE TRIGGER_ID = 99974; 

--changeSet QC-4921:3 stripComments:false
UPDATE UTL_TRIGGER
  SET TRIGGER_CD = 'MX_TS_TASKASSIGN'
      , TRIGGER_NAME = 'part request by task after assign task to or unassign task from work package'
 WHERE TRIGGER_ID = 99973; 

--changeSet QC-4921:4 stripComments:false
UPDATE UTL_TRIGGER
  SET TRIGGER_CD = 'MX_TS_ADD_PART_REQUIREMENT'
      , TRIGGER_NAME = 'part request by part requirement after add part requirement to task'
      , CLASS_NAME = 'com.mxi.mx.core.adapter.material.outgoing.partrequest.PartRequestPublishTrigger'
 WHERE TRIGGER_ID = 99972;  

--changeSet QC-4921:5 stripComments:false
UPDATE UTL_TRIGGER
  SET TRIGGER_CD = 'MX_TS_COMMIT'
      , TRIGGER_NAME = 'part request by work order after commit work package'
 WHERE TRIGGER_ID = 99971; 

--changeSet QC-4921:6 stripComments:false
UPDATE UTL_TRIGGER
  SET TRIGGER_CD = 'MX_TS_TASKASSIGN'
      , TRIGGER_NAME = 'part request by task after assign task to or unassign task from work package'  
 WHERE TRIGGER_ID = 99970; 

--changeSet QC-4921:7 stripComments:false
 UPDATE UTL_TRIGGER
  SET TRIGGER_CD = 'MX_TS_ADD_PART_REQUIREMENT'
      , TRIGGER_NAME = 'part request by part requirement after add part requirement to task'
 WHERE TRIGGER_ID = 99969;    

--changeSet QC-4921:8 stripComments:false
 UPDATE UTL_TRIGGER
  SET TRIGGER_CD = 'MX_TS_SCHEDULE_INTERNAL'
      , TRIGGER_NAME = 'part request by work order after internally schedule work package'
 WHERE TRIGGER_ID = 99968;   

--changeSet QC-4921:9 stripComments:false
INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
SELECT 99967, 'MX_TS_SCHEDULE_EXTERNAL', 1, 'COMPONENT', 'part request by work order after externally schedule work package', 'com.mxi.mx.core.adapter.services.material.partrequest.PartRequestOldMessagePublishTrigger', 0, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99967);   