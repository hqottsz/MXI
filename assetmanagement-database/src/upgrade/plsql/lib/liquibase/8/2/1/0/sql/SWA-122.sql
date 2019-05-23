--liquibase formatted sql


--changeSet SWA-122:1 stripComments:false
-- Add new data rows in int_bp_lookup table
-- request-order-information-40
INSERT INTO 
   int_bp_lookup (namespace, root_name, ref_type, ref_name, method_name, int_logging_type_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   'http://xml.mxi.com/xsd/core/finance/request-order-information/4.0','request-order-information','PROCESS','http://www.mxi.com/mx/xml/finance','request-order-information-40', 'FULL', 0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM 
   dual
WHERE NOT EXISTS
   ( SELECT 
        1 
     FROM 
        int_bp_lookup 
     WHERE 
        namespace = 'http://xml.mxi.com/xsd/core/finance/request-order-information/4.0' 
     AND 
        root_name = 'request-order-information'
   )
;   

--changeSet SWA-122:2 stripComments:false
-- request-order-information-by-id-40
INSERT INTO 
   int_bp_lookup (namespace, root_name, ref_type, ref_name, method_name, int_logging_type_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   'http://xml.mxi.com/xsd/core/finance/request-order-information-by-id/4.0', 'request-order-information-by-id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request-order-information-by-id-40', 'FULL', 0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM 
   dual
WHERE NOT EXISTS
   ( SELECT 
        1 
     FROM 
        int_bp_lookup 
     WHERE 
        namespace = 'http://xml.mxi.com/xsd/core/finance/request-order-information-by-id/4.0' 
     AND 
        root_name = 'request-order-information-by-id'
   )
;   

--changeSet SWA-122:3 stripComments:false
-- Add new data rows in int_process table
-- request-order-information-40
INSERT INTO 
   int_process (namespace, name, type, process, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   'http://www.mxi.com/mx/xml/finance','request-order-information-40','PROCESS',
   '<?xml version="1.0"?>
   <proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformation40" method="process"/>
   </proc:process>',
   0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM
   dual
WHERE NOT EXISTS
   ( SELECT 
        1
     FROM
        int_process
     WHERE
        namespace = 'http://www.mxi.com/mx/xml/finance'      
        AND
        name = 'request-order-information-40'
   )         
;   

--changeSet SWA-122:4 stripComments:false
-- request-order-information-by-id-40
INSERT INTO 
   int_process (namespace, name, type, process, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   'http://www.mxi.com/mx/xml/finance', 'request-order-information-by-id-40', 'PROCESS',
   '<?xml version="1.0"?>
   <proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById40" method="process"/>
   </proc:process>',
   0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM
   dual
WHERE NOT EXISTS
   ( SELECT
        1
     FROM
        int_process
     WHERE
        namespace = 'http://www.mxi.com/mx/xml/finance'      
        AND
        name = 'request-order-information-by-id-40'
    )
;

--changeSet SWA-122:5 stripComments:false
-- update utl_trigger table to point to the new trigger class for version 4.0
INSERT INTO 
    utl_trigger(
    trigger_id,
    trigger_cd,
    exec_order,
    type_cd,
    trigger_name,
    class_name,
    active_bool,
    utl_id
    )
SELECT
    99913,
    'MX_FNC_SEND_ORDER_INFORMATION',
    1,
    'COMPONENT',
    'Send Order Information',
    'com.mxi.mx.core.adapter.finance.trg.TrgOrderInformationById40',
    0,
    0
FROM	
    dual
WHERE
    NOT EXISTS
    (
    SELECT *
    FROM
        utl_trigger
    WHERE
        trigger_id = 99913
    );