--liquibase formatted sql


--changeSet SWA-165:1 stripComments:false
-- create-purchase-invoice-30
insert into int_process(NAMESPACE, NAME, TYPE, PROCESS, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
select 'http://www.mxi.com/mx/xml/finance', 'create-purchase-invoice-30', 'PROCESS',
'<?xml version="1.0"?>
<proc:process xmlns:proc="http://xml.mxi.com/xsd/integration/process/1.0" xmlns:service="http://xml.mxi.com/xsd/integration/process/service/1.0">
     <!--Service Processing-->
     <service:java name="process" classname="com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice30" method="process"/>
</proc:process>',
0, to_date('30-05-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('30-05-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
from dual
where not exists
   ( SELECT 
        1
     FROM
        int_process
     WHERE
        namespace = 'http://www.mxi.com/mx/xml/finance'      
        AND
        name = 'create-purchase-invoice-30'
   )         
;

--changeSet SWA-165:2 stripComments:false
insert into INT_BP_LOOKUP (NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
select 'http://xml.mxi.com/xsd/core/finance/create-purchase-invoice/3.0', 'create-purchase-invoice', 'PROCESS', 'http://www.mxi.com/mx/xml/finance', 'create-purchase-invoice-30', 'FULL', 0, to_date('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('01-06-2016 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
from dual
where not exists 
   ( select 
        1 
     from 
        int_bp_lookup 
     where 
        namespace = 'http://xml.mxi.com/xsd/core/finance/create-purchase-invoice/3.0' 
     and 
        root_name = 'create-purchase-invoice'
   )
;