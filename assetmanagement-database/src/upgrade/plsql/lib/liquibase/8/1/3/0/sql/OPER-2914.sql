--liquibase formatted sql


--changeSet OPER-2914:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add a budget check column
BEGIN
 utl_migr_schema_pkg.table_column_add
   ('
      ALTER TABLE po_auth ADD 
      (
         BUDGET_CHECK_REF Varchar2 (40) 
      )
   ');
END;
/

--changeSet OPER-2914:2 stripComments:false
-- Add a new API definition for budget check approval
-- PART-ONE
INSERT INTO INT_BP_LOOKUP 
   (
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
SELECT 'http://xml.mxi.com/xsd/core/finance/approve-order-budget/2.0',
 'approve-order-budget',
 'PROCESS',
 'http://www.mxi.com/mx/xml/finance',
 'approve-order-budget-20',
 'FULL',
 0,
 to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
 to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
 0,
 'MXI'
FROM DUAL
WHERE NOT EXISTS 
   (
     SELECT *
     FROM INT_BP_LOOKUP
     WHERE 
      (
       NAMESPACE = 'http://xml.mxi.com/xsd/core/finance/approve-order-budget/2.0' AND 
       ROOT_NAME = 'approve-order-budget'
      )
   );

--changeSet OPER-2914:3 stripComments:false
-- PART-TWO
INSERT INTO INT_PROCESS 
   (
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
SELECT 'http://www.mxi.com/mx/xml/finance',
 'approve-order-budget-20',
 'PROCESS',
 '<?xml version="1.0"?>
			<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
			<!--Service Processing-->
			<service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdApproveOrderBudget20" method="process"/>
			</proc:process>',
 0,
 to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
 to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
 0,
 'MXI'
FROM DUAL
WHERE NOT EXISTS 
   (
     SELECT *
     FROM INT_PROCESS
     WHERE 
     (
       NAMESPACE = 'http://www.mxi.com/mx/xml/finance' AND 
       NAME = 'approve-order-budget-20'
     )
  );

--changeSet OPER-2914:4 stripComments:false
-- Add a new API definition for budget check rejection
-- PART-ONE
INSERT INTO INT_BP_LOOKUP 
   (
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
SELECT 'http://xml.mxi.com/xsd/core/finance/reject-order-budget/2.0',
 'reject-order-budget',
 'PROCESS',
 'http://www.mxi.com/mx/xml/finance',
 'reject-order-budget-20',
 'FULL',
 0,
 to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
 to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
 0,
 'MXI'
FROM DUAL
WHERE NOT EXISTS 
   (
     SELECT *
     FROM INT_BP_LOOKUP
     WHERE 
     (
       NAMESPACE = 'http://xml.mxi.com/xsd/core/finance/reject-order-budget/2.0' AND 
       ROOT_NAME = 'reject-order-budget'
     )
   );

--changeSet OPER-2914:5 stripComments:false
-- PART-TWO
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
SELECT 'http://www.mxi.com/mx/xml/finance',
 'reject-order-budget-20',
 'PROCESS',
 '<?xml version="1.0"?>
				<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
				   <!--Service Processing-->
				   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdRejectOrderBudget20" method="process"/>
				</proc:process>',
 0,
 to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
 to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'),
 0,
 'MXI'
FROM DUAL
WHERE NOT EXISTS 
   (
     SELECT *
     FROM INT_PROCESS
     WHERE 
      (
       NAMESPACE = 'http://www.mxi.com/mx/xml/finance' AND 
       NAME = 'reject-order-budget-20'
      )
   );