--liquibase formatted sql


--changeSet DEV-1404:1 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- insert into INT_BP_LOOKUP table
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- approve_order_budget
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, 
   ROOT_NAME, 
   REF_TYPE, 
   REF_NAME, 
   METHOD_NAME, 
   INT_LOGGING_TYPE_CD,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://xml.mxi.com/xsd/core/finance/approve_order_budget/1.0', 
   'approve_order_budget', 
   'PROCESS', 
   'http://www.mxi.com/mx/xml/finance', 
   'approve_order_budget', 
   'FULL',
   0, 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE 
         NAMESPACE = 'http://xml.mxi.com/xsd/core/finance/approve_order_budget/1.0'
         AND
         ROOT_NAME = 'approve_order_budget'
   );   

--changeSet DEV-1404:2 stripComments:false
-- reject_order_budget
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, 
   ROOT_NAME, 
   REF_TYPE, 
   REF_NAME, 
   METHOD_NAME, 
   INT_LOGGING_TYPE_CD,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://xml.mxi.com/xsd/core/finance/reject_order_budget/1.0', 
   'reject_order_budget', 
   'PROCESS', 
   'http://www.mxi.com/mx/xml/finance', 
   'reject_order_budget', 
   'FULL',
   0, 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE 
         NAMESPACE = 'http://xml.mxi.com/xsd/core/finance/reject_order_budget/1.0'
         AND
         ROOT_NAME = 'reject_order_budget'
   );      

--changeSet DEV-1404:3 stripComments:false
-- request_order_information_21
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, 
   ROOT_NAME, 
   REF_TYPE, 
   REF_NAME, 
   METHOD_NAME, 
   INT_LOGGING_TYPE_CD,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://xml.mxi.com/xsd/core/finance/request_order_information/2.1', 
   'request_order_information', 
   'PROCESS', 
   'http://www.mxi.com/mx/xml/finance', 
   'request_order_information_21', 
   'FULL',
   0, 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE 
         NAMESPACE = 'http://xml.mxi.com/xsd/core/finance/request_order_information/2.1'
         AND
         ROOT_NAME = 'request_order_information'
   );      

--changeSet DEV-1404:4 stripComments:false
-- request_order_information_by_id_21
INSERT INTO INT_BP_LOOKUP (
   NAMESPACE, 
   ROOT_NAME, 
   REF_TYPE, 
   REF_NAME, 
   METHOD_NAME, 
   INT_LOGGING_TYPE_CD,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://xml.mxi.com/xsd/core/finance/request_order_information_by_id/2.1', 
   'request_order_information_by_id', 
   'PROCESS', 
   'http://www.mxi.com/mx/xml/finance', 
   'request_order_information_by_id_21', 
   'FULL',
   0, 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_BP_LOOKUP 
      WHERE 
         NAMESPACE = 'http://xml.mxi.com/xsd/core/finance/request_order_information_by_id/2.1'
         AND
         ROOT_NAME = 'request_order_information_by_id'
   );       

--changeSet DEV-1404:5 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- insert into INT_PROCESS table
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --    
-- approve_order_budget
INSERT INTO INT_PROCESS (
   NAMESPACE, 
   NAME, 
   TYPE, 
   PROCESS,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://www.mxi.com/mx/xml/finance', 
   'approve_order_budget', 
   'PROCESS', 
   '<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById21" method="process"/>
</proc:process>', 
   0, 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_PROCESS 
      WHERE 
         NAMESPACE = 'http://www.mxi.com/mx/xml/finance'
         AND
         NAME = 'approve_order_budget'
   );

--changeSet DEV-1404:6 stripComments:false
-- reject_order_budget
INSERT INTO INT_PROCESS (
   NAMESPACE, 
   NAME, 
   TYPE, 
   PROCESS,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://www.mxi.com/mx/xml/finance', 
   'reject_order_budget', 
   'PROCESS', 
   '<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdRejectOrderBudget" method="process"/>
</proc:process>', 
   0, 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_PROCESS 
      WHERE 
         NAMESPACE = 'http://www.mxi.com/mx/xml/finance'
         AND
         NAME = 'reject_order_budget'
   );   

--changeSet DEV-1404:7 stripComments:false
-- request_order_information_21
INSERT INTO INT_PROCESS (
   NAMESPACE, 
   NAME, 
   TYPE, 
   PROCESS,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://www.mxi.com/mx/xml/finance', 
   'request_order_information_21', 
   'PROCESS', 
   '<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformation21" method="process"/>
</proc:process>', 
   0, 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_PROCESS 
      WHERE 
         NAMESPACE = 'http://www.mxi.com/mx/xml/finance'
         AND
         NAME = 'request_order_information_21'
   );   

--changeSet DEV-1404:8 stripComments:false
-- request_order_information_by_id_21
INSERT INTO INT_PROCESS (
   NAMESPACE, 
   NAME, 
   TYPE, 
   PROCESS,
   RSTAT_CD, 
   CREATION_DT, 
   REVISION_DT, 
   REVISION_DB_ID, 
   REVISION_USER
)
SELECT 
   'http://www.mxi.com/mx/xml/finance', 
   'request_order_information_by_id_21', 
   'PROCESS', 
   '<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById21" method="process"/>
</proc:process>', 
   0, 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
FROM
   DUAL
WHERE
    NOT EXISTS (
      SELECT 
         1 
      FROM 
         INT_PROCESS 
      WHERE 
         NAMESPACE = 'http://www.mxi.com/mx/xml/finance'
         AND
         NAME = 'request_order_information_by_id_21'
   );         