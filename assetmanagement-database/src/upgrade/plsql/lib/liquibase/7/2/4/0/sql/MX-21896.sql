--liquibase formatted sql


--changeSet MX-21896:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "INBOUNDQUEUE_UTLALERT_FK" ON "UTL_ALERT" ("QUEUE_ID") 
');
END;
/  

--changeSet MX-21896:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCNT_EVTEVENT_FK" ON "EVT_EVENT" ("ACCOUNT_DB_ID","ACCOUNT_ID") 
');
END;
/ 

--changeSet MX-21896:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCOUNT_SCHEDSTASK_FK" ON "SCHED_STASK" ("ISSUE_ACCOUNT_DB_ID","ISSUE_ACCOUNT_ID") 
');
END;
/ 

--changeSet MX-21896:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCOUNT_POLINE_FK" ON "PO_LINE" ("ACCOUNT_DB_ID","ACCOUNT_ID") 
');
END;
/ 

--changeSet MX-21896:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCOUNT_POINVLINE_FK" ON "PO_INVOICE_LINE" ("ACCOUNT_DB_ID","ACCOUNT_ID") 
');
END;
/ 

--changeSet MX-21896:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCNT_EQPPRTNO_FK" ON "EQP_PART_NO" ("ASSET_ACCOUNT_DB_ID","ASSET_ACCOUNT_ID") 
');
END;
/ 

--changeSet MX-21896:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCNT_REQPART_FK" ON "REQ_PART" ("ISSUE_ACCOUNT_DB_ID","ISSUE_ACCOUNT_ID") 
');
END;
/ 

--changeSet MX-21896:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCOUNT_SHIPSHIPMENT_FK" ON "SHIP_SHIPMENT" ("RETURN_ACCOUNT_DB_ID","RETURN_ACCOUNT_ID") 
');
END;
/ 

--changeSet MX-21896:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCOUNT_RFQLINE_FK" ON "RFQ_LINE" ("ACCOUNT_DB_ID","ACCOUNT_ID") 
');
END;
/ 

--changeSet MX-21896:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCOUNT_INVACREG_FK" ON "INV_AC_REG" ("ISSUE_ACCOUNT_DB_ID","ISSUE_ACCOUNT_ID") 
');
END;
/ 

--changeSet MX-21896:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCOUNT_TASKTASK_FK" ON "TASK_TASK" ("ISSUE_ACCOUNT_DB_ID","ISSUE_ACCOUNT_ID") 
');
END;
/

--changeSet MX-21896:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCACCNT_FNCACCNT_FK" ON "FNC_ACCOUNT" ("NH_ACCOUNT_DB_ID","NH_ACCOUNT_ID") 
');
END;
/

--changeSet MX-21896:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "FNCTCODE_FNCACCOUNT_FK" ON "FNC_ACCOUNT" ("TCODE_DB_ID","TCODE_ID") 
');
END;
/

--changeSet MX-21896:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EQPASSMBL_INVINV_FK" ON "INV_INV" ("ORIG_ASSMBL_DB_ID","ORIG_ASSMBL_CD") 
');
END;
/

--changeSet MX-21896:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop foreign key constraints on some of the Integration Framework tables
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_INBOUND_QUEUE_LOG', 'FK_INTMSGORDER_INTINBOUNDQUEUE'); 
END;
/

--changeSet MX-21896:16 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_INBOUND_QUEUE_LOG', 'INT_INBOUND_Q_ORIG_FK2');
END;
/

--changeSet MX-21896:17 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_INBOUND_QUEUE_LOG', 'FK_REFINTDEL_INTINBOUNDQUE');
END;
/

--changeSet MX-21896:18 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_INBOUND_QUEUE_LOG', 'INT_INBOUND_Q_REF_STATUS_FK2');
END;
/

--changeSet MX-21896:19 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_INBOUND_QUEUE_LOG', 'FK_MIMSTAT_INTINBOUND');
END;
/

--changeSet MX-21896:20 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_NOTIFICATION_LOG', 'FK_MIMSTAT_INTNOTIFLOG');
END;
/

--changeSet MX-21896:21 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_NOTIFICATION_LOG', 'FK_INTSUBWS_INTNOTIFLOG');
END;
/

--changeSet MX-21896:22 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_OUTBOUND_QUEUE_LOG', 'INT_INBOUND_Q_OUTBOUND_Q_PFK2');
END;
/

--changeSet MX-21896:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_OUTBOUND_QUEUE_LOG', 'FK_REFINTLOGSTATUS_INTOUTBOUND');
END;
/

--changeSet MX-21896:24 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_OUTBOUND_QUEUE_LOG', 'FK_MIMSTAT_INTOUTBOUND');
END;
/

--changeSet MX-21896:25 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_STEP_LOG', 'INT_IN_QUEUE_LOG_INTSTEPLOG_FK');
END;
/

--changeSet MX-21896:26 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_STEP_LOG', 'NH_INT_STEP_LOG_INTSTEPLOG_FK');
END;
/

--changeSet MX-21896:27 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_STEP_LOG', 'REF_INT_LOG_STAT_INTSTEPLOG_FK');
END;
/

--changeSet MX-21896:28 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_STEP_LOG', 'REF_INT_STEP_TYP_INTSTEPLOG_FK');
END;
/

--changeSet MX-21896:29 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_STEP_LOG', 'MIM_RSTAT_INTSTEPLOG_FK');
END;
/