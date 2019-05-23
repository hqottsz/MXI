--liquibase formatted sql


--changeSet OPER-2962:1 stripComments:false
-- Add new data rows in int_bp_lookup table
-- request-order-information-30
INSERT INTO 
   int_bp_lookup (namespace, root_name, ref_type, ref_name, method_name, int_logging_type_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   'http://xml.mxi.com/xsd/core/finance/request-order-information/3.0','request-order-information','PROCESS','http://www.mxi.com/mx/xml/finance','request-order-information-30', 'FULL', 0, to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM 
   dual
WHERE NOT EXISTS
   ( SELECT 
        1 
     FROM 
        int_bp_lookup 
     WHERE 
        namespace = 'http://xml.mxi.com/xsd/core/finance/request-order-information/3.0' 
     AND 
        root_name = 'request-order-information'
   )
;   

--changeSet OPER-2962:2 stripComments:false
-- request-order-information-by-id-30
INSERT INTO 
   int_bp_lookup (namespace, root_name, ref_type, ref_name, method_name, int_logging_type_cd, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   'http://xml.mxi.com/xsd/core/finance/request-order-information-by-id/3.0', 'request-order-information-by-id', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'request-order-information-by-id-30', 'FULL', 0, to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM 
   dual
WHERE NOT EXISTS
   ( SELECT 
        1 
     FROM 
        int_bp_lookup 
     WHERE 
        namespace = 'http://xml.mxi.com/xsd/core/finance/request-order-information-by-id/3.0' 
     AND 
        root_name = 'request-order-information-by-id'
   )
;   

--changeSet OPER-2962:3 stripComments:false
-- Add new data rows in int_process table
-- request-order-information-30
INSERT INTO 
   int_process (namespace, name, type, process, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   'http://www.mxi.com/mx/xml/finance','request-order-information-30','PROCESS',
   '<?xml version="1.0"?>
   <proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformation30" method="process"/>
   </proc:process>',
   0, to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
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
        name = 'request-order-information-30'
   )         
;   

--changeSet OPER-2962:4 stripComments:false
-- request-order-information-by-id-30
INSERT INTO 
   int_process (namespace, name, type, process, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
SELECT
   'http://www.mxi.com/mx/xml/finance', 'request-order-information-by-id-30', 'PROCESS',
   '<?xml version="1.0"?>
   <proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById30" method="process"/>
   </proc:process>',
   0, to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
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
        name = 'request-order-information-by-id-30'
    )
;

--changeSet OPER-2962:5 stripComments:false
-- update utl_trigger table to point to the new trigger class for version 3.0
UPDATE 
   utl_trigger 
SET 
   class_name = 'com.mxi.mx.core.adapter.finance.trg.TrgOrderInformationById30' 
WHERE 
   trigger_id = 99999 
   AND
   trigger_cd = 'MX_FNC_SEND_ORDER_INFORMATION'
;