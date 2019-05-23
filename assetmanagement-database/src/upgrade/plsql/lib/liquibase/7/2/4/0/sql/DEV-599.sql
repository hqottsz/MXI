--liquibase formatted sql


--changeSet DEV-599:1 stripComments:false
UPDATE INT_BP_LOOKUP
  SET REVISION_DT = to_date('07-06-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss')
WHERE NAMESPACE = 'http://xml.mxi.com/xsd/core/matadapter/part-request-request/1.0'
  AND ROOT_NAME = 'part-request-request';  

--changeSet DEV-599:2 stripComments:false
INSERT INTO
   INT_BP_LOOKUP
   (
      NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER
   )
   SELECT 'http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/1.0', 'update-part-request-request', 'JAVA', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestEntryPoint', 'coordinate', 0, to_date('02-09-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), to_date('02-09-2010 11:31:55', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
   FROM
      dual
   WHERE
      NOT EXISTS ( SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'http://xml.mxi.com/xsd/core/matadapter/update-part-request-request/1.0' and ROOT_NAME = 'update-part-request-request' );          

--changeSet DEV-599:3 stripComments:false
  INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
  SELECT 99966, 'MX_TS_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after edit request priority', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0
  FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99966);  

--changeSet DEV-599:4 stripComments:false
  INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
  SELECT 99965, 'MX_PR_CANCEL', 1, 'COMPONENT', 'update part request by part requirement after edit request priority', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0
  FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99965);  

--changeSet DEV-599:5 stripComments:false
  INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
  SELECT 99964, 'MX_PR_PRIORITY', 1, 'COMPONENT', 'update part request by part requirement after edit request priority', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0
  FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99964);  

--changeSet DEV-599:6 stripComments:false
  INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
  SELECT 99963, 'MX_PR_EXTERNAL_REFERENCE', 1, 'COMPONENT', 'update part request by part requirement after edit external reference', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0
  FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99963);     

--changeSet DEV-599:7 stripComments:false
    INSERT INTO UTL_TRIGGER (TRIGGER_ID, TRIGGER_CD, EXEC_ORDER, TYPE_CD, TRIGGER_NAME, CLASS_NAME, ACTIVE_BOOL, UTL_ID)
    SELECT 99962, 'MX_PR_NEEDED_BY_DATE', 1, 'COMPONENT', 'update part request by part requirement after edit needed by date', 'com.mxi.mx.core.adapter.material.outgoing.updatepartrequest.UpdatePartRequestPublishTrigger', 0, 0
    FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM utl_trigger WHERE utl_trigger.trigger_id = 99962);