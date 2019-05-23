--liquibase formatted sql


--changeSet 0int_process:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "INT_PROCESS"
** DATE: 17/07/2006 TIME: 11:30:25
*********************************************/
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://process.mxi.com/test', 'echoProcess', 'Mxi',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:transformer="http://xml.mxi.com/xsd/integration/process/transformation/1.0" xmlns:validator="http://xml.mxi.com/xsd/integration/process/validation/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <validator:java name="requestValidator" classname="com.mxi.mx.integration.services.process.TestScaffolding$Request$Validator"/>
   <transformer:java name="requestTransformer" classname="com.mxi.mx.integration.services.process.TestScaffolding$Request$Transformer"/>
   <service:ejb name="echoEjb" jndi="com.mxi.mx.integration.ejb.Echo" method="echo"/>
   <transformer:java name="responseTransformer" classname="com.mxi.mx.integration.services.process.TestScaffolding$Response$Transformer"/>
   <validator:java name="requestValidator" classname="com.mxi.mx.integration.services.process.TestScaffolding$Response$Validator"/>
</proc:process>',
0, to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:2 stripComments:false
-- request_detailed_inventory_financial_log
/* --- Finance Adapter --- */
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_detailed_inventory_financial_log', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdDetailedFinancialLog" method="process"/>
</proc:process>',
0, to_date('12-06-2007 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-06-2007 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:3 stripComments:false
-- request_detailed_inventory_financial_log_11
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_detailed_inventory_financial_log_11','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdDetailedFinancialLog11" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:4 stripComments:false
-- request_detailed_inventory_financial_log_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_detailed_inventory_financial_log_20','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdDetailedFinancialLog20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:5 stripComments:false
-- request-detailed-inventory-financial-log-30
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request-detailed-inventory-financial-log-30','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdDetailedFinancialLog30" method="process"/>
</proc:process>',
0, to_date('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:6 stripComments:false
-- request_detailed_inventory_financial_log_by_id
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_detailed_inventory_financial_log_by_id', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdDetailedFinancialLogById" method="process"/>
</proc:process>',
0, to_date('12-06-2007 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-06-2007 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:7 stripComments:false
-- request_detailed_inventory_financial_log_by_id_11
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_detailed_inventory_financial_log_by_id_11','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdDetailedFinancialLogById11" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:8 stripComments:false
-- request_detailed_inventory_financial_log_by_id_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_detailed_inventory_financial_log_by_id_20','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdDetailedFinancialLogById20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:9 stripComments:false
-- request_detailed_inventory_financial_log_by_id_30
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request-detailed-inventory-financial-log-by-id-30','PROCESS',
'<?xml version="1.0"?>
  <proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
  <!--Service Processing-->
  <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdDetailedFinancialLogById30" method="process"/>
  </proc:process>',
0, TO_DATE('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), TO_DATE('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:10 stripComments:false
-- request_summary_inventory_financial_log
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_summary_inventory_financial_log', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdSummaryFinancialLog" method="process"/>
</proc:process>',
0, to_date('11-07-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-07-2007 02:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:11 stripComments:false
-- request_summary_inventory_financial_log_11
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_summary_inventory_financial_log_11','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdSummaryFinancialLog11" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:12 stripComments:false
-- request_summary_inventory_financial_log_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_summary_inventory_financial_log_20','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdSummaryFinancialLog20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:13 stripComments:false
-- create-financial-accounts-request
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'createFinancialAccountsRequest', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCreateFinancialAccountsRequest" method="process"/>
</proc:process>',
0, to_date('20-03-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:14 stripComments:false
-- update-financial-accounts-request
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'updateFinancialAccountsRequest', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdUpdateFinancialAccountsRequest" method="process"/>
</proc:process>',
0, to_date('28-03-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('28-03-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:15 stripComments:false
-- close-financial-accounts-request
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'closeFinancialAccountsRequest', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCloseFinancialAccountsRequest" method="process"/>
</proc:process>',
0, to_date('05-04-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-04-2013 19:06:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:16 stripComments:false
-- request_order_information
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_order_information', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformation" method="process"/>
</proc:process>',
0, to_date('17-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:17 stripComments:false
-- request_order_information_11
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_order_information_11','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformation11" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:18 stripComments:false
-- request_order_information_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_order_information_20','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformation20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:19 stripComments:false
-- request_order_information_21
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_order_information_21','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformation21" method="process"/>
</proc:process>',
0, to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:20 stripComments:false
-- request-order-information-30
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request-order-information-30','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformation30" method="process"/>
</proc:process>',
0, to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:21 stripComments:false
-- request-order-information-40
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request-order-information-40','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformation40" method="process"/>
</proc:process>',
0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:22 stripComments:false
-- request_order_information_by_id
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_order_information_by_id', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById" method="process"/>
</proc:process>',
0, to_date('17-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:23 stripComments:false
-- request_order_information_by_id_11
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_order_information_by_id_11', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById11" method="process"/>
</proc:process>',
0, to_date('29-01-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('29-01-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:24 stripComments:false
-- request_order_information_by_id_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_order_information_by_id_20', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:25 stripComments:false
-- request_order_information_by_id_21
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_order_information_by_id_21', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById21" method="process"/>
</proc:process>',
0, to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:26 stripComments:false
-- request-order-information-by-id-30
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request-order-information-by-id-30', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById30" method="process"/>
</proc:process>',
0, to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('20-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:27 stripComments:false
-- request-order-information-by-id-40
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request-order-information-by-id-40', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdOrderInformationById40" method="process"/>
</proc:process>',
0, to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-03-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:28 stripComments:false
-- approve_order_budget
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'approve_order_budget', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdApproveOrderBudget" method="process"/>
</proc:process>',
0, to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:29 stripComments:false
-- approve_order_budget
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'approve-order-budget-20', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdApproveOrderBudget20" method="process"/>
</proc:process>',
0, to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:30 stripComments:false
-- reject_order_budget
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'reject_order_budget', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdRejectOrderBudget" method="process"/>
</proc:process>',
0, to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('05-01-2012 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:31 stripComments:false
-- reject_order_budget
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'reject-order-budget-20', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdRejectOrderBudget20" method="process"/>
</proc:process>',
0, to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-03-2015 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:32 stripComments:false
-- request_to_be_paid_invoices
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_to_be_paid_invoices', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdToBePaidInvoices" method="process"/>
</proc:process>',
0, to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:33 stripComments:false
-- request_to_be_paid_invoices_11
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_to_be_paid_invoices_11','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdToBePaidInvoices11" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:34 stripComments:false
-- request_to_be_paid_invoices_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_to_be_paid_invoices_20','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdToBePaidInvoices20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:35 stripComments:false
-- request_to_be_paid_invoices_by_id
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'request_to_be_paid_invoices_by_id', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdToBePaidInvoicesById" method="process"/>
</proc:process>',
0, to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:36 stripComments:false
-- request_to_be_paid_invoices_by_id_11
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER) 
values ('http://www.mxi.com/mx/xml/finance','request_to_be_paid_invoices_by_id_11','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdToBePaidInvoicesById11" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:37 stripComments:false
-- request_to_be_paid_invoices_by_id_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values ('http://www.mxi.com/mx/xml/finance','request_to_be_paid_invoices_by_id_20','PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdToBePaidInvoicesById20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:38 stripComments:false
-- create_purchase_invoice
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'create_purchase_invoice', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
     <!--Service Processing-->
     <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice" method="process"/>
</proc:process>',
0, to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-07-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:39 stripComments:false
-- create_purchase_invoice_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'create_purchase_invoice_20', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
     <!--Service Processing-->
     <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:40 stripComments:false
-- create_purchase_invoice_21
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'create_purchase_invoice_21', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
     <!--Service Processing-->
     <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice21" method="process"/>
</proc:process>',
0, to_date('11-04-2013 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('11-04-2013 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:41 stripComments:false
-- create-purchase-invoice-30
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'create-purchase-invoice-30', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
    <!--Service Processing-->
    <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice30" method="process"/>
</proc:process>',
0, to_date('30-05-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-05-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:42 stripComments:false
-- update_currency_exchange_rate
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'update_currency_exchange_rate', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdUpdateCurrencyExchangeRate" method="process"/>
</proc:process>',
0, to_date('07-08-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('07-08-2007 04:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:43 stripComments:false
-- update_currency_exchange_rate_11
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'update_currency_exchange_rate_11', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdUpdateCurrencyExchangeRate11" method="process"/>
</proc:process>',
0, to_date('27-01-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('27-01-2009 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:44 stripComments:false
-- update_currency_exchange_rate_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'update_currency_exchange_rate_20', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdUpdateCurrencyExchangeRate20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:45 stripComments:false
-- mark_invoice_as_paid
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'mark_invoice_as_paid', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
     <!--Service Processing-->
     <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdMarkInvoiceAsPaid" method="process"/>
</proc:process>',
0, to_date('15-09-2008 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('15-09-2008 10:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:46 stripComments:false
-- mark_invoice_as_paid_20
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'mark_invoice_as_paid_20', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
     <!--Service Processing-->
     <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdMarkInvoiceAsPaid20" method="process"/>
</proc:process>',
0, to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('16-11-2007 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:47 stripComments:false
-- Enable User
/* --- HR Adapter --- */
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'enable_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdEnableUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:48 stripComments:false
-- Disable User
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'disable_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdDisableUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:49 stripComments:false
-- Assign User To Department
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'assign_user_to_department', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdAssignUserToDepartment" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:50 stripComments:false
-- UnAssign User From Department
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'unassign_user_from_department', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdUnassignUserFromDepartment" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:51 stripComments:false
-- Assign Role to User
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'assign_role_to_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdAssignRoleToUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:52 stripComments:false
-- Unassign Role From User
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'unassign_role_from_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdUnassignRoleFromUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:53 stripComments:false
-- Set All Authority For User
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'set_all_authority_for_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdSetAllAuthorityForUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:54 stripComments:false
-- Assign Authority
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'assign_authority_to_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdAssignAuthorityToUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:55 stripComments:false
-- Unassign Authority From User
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'unassign_authority_from_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdUnassignAuthorityFromUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:56 stripComments:false
-- Assign Skill
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'assign_skill_to_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdAssignSkillToUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:57 stripComments:false
-- Unassign Skill From User
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'unassign_skill_from_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdUnassignSkillFromUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:58 stripComments:false
-- Create User
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'create_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdCreateUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:59 stripComments:false
-- Update User
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'update_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdUpdateUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:60 stripComments:false
-- Delete User
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/hr', 'delete_user', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.hr.cmd.CmdDeleteUser" method="process"/>
</proc:process>',
0, to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('23-10-2007 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:61 stripComments:false
-- Get Pending Waybills
/* --- Shipment Adapter --- */
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/shipment', 'get_pending_waybills', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.shipment.cmd.CmdGetPendingWaybills" method="process"/>
</proc:process>',
0, to_date('10-09-2008 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-09-2008 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:62 stripComments:false
-- Update Waybill Details
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/shipment', 'update_waybill_details', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
   <!--Service Processing-->
   <service:java name="process" classname="com.mxi.mx.core.adapter.shipment.cmd.CmdUpdateWaybillDetails" method="process"/>
</proc:process>',
0, to_date('10-09-2008 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-09-2008 11:52:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
);

--changeSet 0int_process:63 stripComments:false
-- create-purchase-invoice-40
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values('http://www.mxi.com/mx/xml/finance', 'create-purchase-invoice-40', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
    <!--Service Processing-->
    <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice40" method="process"/>
</proc:process>',
0, to_date('12-09-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('12-09-2018 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
); 