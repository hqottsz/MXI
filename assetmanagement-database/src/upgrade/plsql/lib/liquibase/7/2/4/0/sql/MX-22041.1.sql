--liquibase formatted sql


--changeSet MX-22041.1:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_SCHED_FROM', 'MIM_DB_REFSCHDFROM');
END;
/

--changeSet MX-22041.1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_SCHED_FROM add constraint FK_MIMDB_REFSCHEDFROM foreign key ( SCHED_FROM_DB_ID ) references MIM_DB ( DB_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_FLIGHT_TYPE', 'MIM_RSTAT_REFFLIGHTTYPE');
END;
/

--changeSet MX-22041.1:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_FLIGHT_TYPE add constraint FK_MIMRSTAT_REFFLIGHTTYPE foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_EVENT_TYPE', 'RFK_MIMRSTAT_REFEVENTTYPE');
END;
/

--changeSet MX-22041.1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_EVENT_TYPE add constraint FK_MIMRSTAT_REFEVENTTYPE foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('MIM_DATA_TYPE', 'REF_DOMAIN_TYPE_MIMDATATYPE');
END;
/

--changeSet MX-22041.1:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table MIM_DATA_TYPE add constraint FK_REFDOMAINTYPE_MIMDATATYPE foreign key ( DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD ) references REF_DOMAIN_TYPE ( DOMAIN_TYPE_DB_ID,DOMAIN_TYPE_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_PART_TYPE', 'NH_REFPARTTYPE_RFPRTYPE');
END;
/

--changeSet MX-22041.1:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_PART_TYPE add constraint FK_REFPARTTYPE_REFPARTTYPE foreign key ( NH_PART_TYPE_DB_ID,NH_PART_TYPE_CD ) references REF_PART_TYPE ( PART_TYPE_DB_ID,PART_TYPE_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_INVOICE_LINE', 'PO_INVOICE_POINVOICELINE');
END;
/

--changeSet MX-22041.1:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table PO_INVOICE_LINE add constraint FK_POINVOICE_POINVOICELINE foreign key ( PO_INVOICE_DB_ID,PO_INVOICE_ID ) references PO_INVOICE ( PO_INVOICE_DB_ID,PO_INVOICE_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_SHIPMENT_REASON', 'REFSHIPTYPE_SHIPREASON');
END;
/

--changeSet MX-22041.1:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_SHIPMENT_REASON add constraint FK_REFSHIPTYPE_REFSHIPREASON foreign key ( SHIPMENT_TYPE_DB_ID,SHIPMENT_TYPE_CD ) references REF_SHIPMENT_TYPE ( SHIPMENT_TYPE_DB_ID,SHIPMENT_TYPE_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_SHIPMENT_REASON', 'MIMRSTAT_REFSHIPREASON');
END;
/

--changeSet MX-22041.1:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_SHIPMENT_REASON add constraint FK_MIMRSTAT_REFSHIPMENTREASON foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:17 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SHIP_SHIPMENT', 'REFSHIPREASON_SHIPSHIP');
END;
/

--changeSet MX-22041.1:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table SHIP_SHIPMENT add constraint FK_REFSHIPREASON_SHIPSHIPMENT foreign key ( SHIPMENT_REASON_DB_ID,SHIPMENT_REASON_CD ) references REF_SHIPMENT_REASON ( SHIPMENT_REASON_DB_ID,SHIPMENT_REASON_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:19 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INV_CURR_USAGE', 'MIMDATATYPE_INVCURRUSAGE');
END;
/

--changeSet MX-22041.1:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table INV_CURR_USAGE add constraint FK_MIMDATATYPE_INVCURRUSAGE foreign key ( DATA_TYPE_DB_ID,DATA_TYPE_ID ) references MIM_DATA_TYPE ( DATA_TYPE_DB_ID,DATA_TYPE_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:21 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_ERROR_LOG', 'INT_OUTBOUND_Q_ERROR_PFK2');
END;
/

--changeSet MX-22041.1:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table INT_ERROR_LOG add constraint FK_INTINBOUNDQLOG_INTERRORLOG foreign key ( QUEUE_ID ) references INT_INBOUND_QUEUE_LOG ( QUEUE_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_INT_STEP_TYPE', 'MIM_DB_REFINTSTEPTYPE_FK');
END;
/

--changeSet MX-22041.1:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_INT_STEP_TYPE add constraint FK_MIMDB_REFINTSTEPTYPE foreign key ( STEP_TYPE_DB_ID ) references MIM_DB ( DB_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:25 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_INT_STEP_TYPE', 'MIM_RSTAT_REFINTSTEPTYPE_FK');
END;
/

--changeSet MX-22041.1:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_INT_STEP_TYPE add constraint FK_MIMRSTAT_REFINTSTEPTYPE foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:27 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('INT_PROCESS', 'MIM_RSTAT_INTPROCESS_FK');
END;
/

--changeSet MX-22041.1:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table INT_PROCESS add constraint FK_MIMRSTAT_INTPROCESS foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:29 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_MAINT_PRGM_STATUS', 'FM_RSTAT_REFMNTPGMSTATUS');
END;
/

--changeSet MX-22041.1:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_MAINT_PRGM_STATUS add constraint FK_MIMRSTAT_REFMAINTPRGMSTATUS foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:31 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_MAINT_PRGM_STATUS', 'FM_MIMDB_REFMNTPGMSTATUS');
END;
/

--changeSet MX-22041.1:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table REF_MAINT_PRGM_STATUS add constraint FK_MIMDB_REFMAINTPRGMSTATUS foreign key ( MAINT_PRGM_STATUS_DB_ID ) references MIM_DB ( DB_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:33 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('WF_STEPS', 'DK_WF_STEPS');
END;
/

--changeSet MX-22041.1:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table WF_STEPS add constraint FK_WFWF_WFSTEPS foreign key ( WF_DB_ID,WF_ID ) references WF_WF ( WF_DB_ID,WF_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:35 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_TIMEZONE', 'UTL_MIMDB_UTLTIMEZONE');
END;
/

--changeSet MX-22041.1:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table UTL_TIMEZONE add constraint FK_MIMDB_UTLTIMEZONE foreign key ( UTL_ID ) references MIM_DB ( DB_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:37 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_LABOUR_ESIG', 'FM_MIMRSTAT_SCHEDLBRESIG');
END;
/

--changeSet MX-22041.1:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table SCHED_LABOUR_ESIG add constraint FK_MIMRSTAT_SCHEDLABOURESIG foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:39 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('DPO_RLM_TYPE_ATTR', 'DPO_RLM_TYPE_ATTR_MIM_RSTAT');
END;
/

--changeSet MX-22041.1:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table DPO_RLM_TYPE_ATTR add constraint FK_MIMRSTAT_DPORLMTYPEATTR foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:41 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('DPO_RLM_VERSION', 'DPO_RLM_VERSION_MIM_RSTAT');
END;
/

--changeSet MX-22041.1:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table DPO_RLM_VERSION add constraint FK_MIMRSTAT_DPORLMVERSION foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:43 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('DPO_RLM_VERSION_LOG', 'DPO_RLM_VERSION_LOG_MIM_RSTAT');
END;
/

--changeSet MX-22041.1:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table DPO_RLM_VERSION_LOG add constraint FK_MIMRSTAT_DPORLMVERSIONLOG foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:45 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('DPO_RLM_OBJ_TYPE', 'DPO_RLM_OBJ_TYPE_MIM_RSTAT');
END;
/

--changeSet MX-22041.1:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table DPO_RLM_OBJ_TYPE add constraint FK_MIMRSTAT_DPORLMOBJTYPE foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:47 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('DPO_RLM_RULE_CLASS', 'DPO_RLM_RULE_CLASS_MIM_RSTAT');
END;
/

--changeSet MX-22041.1:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table DPO_RLM_RULE_CLASS add constraint FK_MIMRSTAT_DPORLMRULECLASS foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:49 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('DPO_RLM_FUNCTION', 'DPO_RLM_FUNCTION_MIM_RSTAT');
END;
/

--changeSet MX-22041.1:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table DPO_RLM_FUNCTION add constraint FK_MIMRSTAT_DPORLMFUNCTION foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:51 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('DPO_RLM_RULE', 'DPO_RLM_RULE_MIM_RSTAT');
END;
/

--changeSet MX-22041.1:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table DPO_RLM_RULE add constraint FK_MIMRSTAT_DPORLMRULE foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:53 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('DPO_XFER_RLM_ERROR', 'DPO_XFERRLM_ERRORRULE');
END;
/

--changeSet MX-22041.1:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table DPO_XFER_RLM_ERROR add constraint FK_DPORLMRULE_DPOXFERRLMERROR foreign key ( RULE_CLASS_CD,RULE_ID ) references DPO_RLM_RULE ( RULE_CLASS_CD,RULE_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:55 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('DPO_XFER_RLM_ERROR', 'DPO_XFER_RM_ERROR_MIM_RSTAT');
END;
/

--changeSet MX-22041.1:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table DPO_XFER_RLM_ERROR add constraint FK_MIMRSTAT_DPOXFERRLMERROR foreign key ( RSTAT_CD ) references MIM_RSTAT ( RSTAT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:57 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SD_FAULT_RESULT', 'SD_REFRSLTEVENT_SDFAULTRSLT');
END;
/

--changeSet MX-22041.1:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table SD_FAULT_RESULT add constraint FK_REFRESULTEVENT_SDFAULTRESU foreign key ( RESULT_EVENT_DB_ID,RESULT_EVENT_CD ) references REF_RESULT_EVENT ( RESULT_EVENT_DB_ID,RESULT_EVENT_CD )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:59 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('BLT_REF_WF_CYCLE_STATUS', 'REF_MIMDB_CTRLBLTREFWFCYCLESTA');
END;
/

--changeSet MX-22041.1:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table BLT_REF_WF_CYCLE_STATUS add constraint FK_MIMDB_BLTREFWFCYCLESTATUS foreign key ( CTRL_DB_ID ) references MIM_DB ( DB_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:61 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('BLT_WF_REC_LOG', 'PFK_BLTWFCYCLELOG_BLTWFRECLOG');
END;
/

--changeSet MX-22041.1:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table BLT_WF_REC_LOG add constraint FK_BLTWFCYCLELOG_BLTWFRECLOG foreign key ( WF_CYCLE_LOG_ID ) references BLT_WF_CYCLE_LOG ( WF_CYCLE_LOG_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:63 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_API_VERSION_CONFIG', 'UTLAPIDEFN_UTLAPIVERSIONCONFIG');
END;
/

--changeSet MX-22041.1:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table UTL_API_VERSION_CONFIG add constraint FK_UTLAPIDEFN_UTLAPIVERCONFG foreign key ( API_ID ) references UTL_API_DEFINITION ( API_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:65 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_NOTIFICATION_CONFIG', 'NOTIFTYPE_NOTICONFIG');
END;
/

--changeSet MX-22041.1:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table UTL_NOTIFICATION_CONFIG add constraint FK_UTLNOTIFTYPE_UTLNOTIFICATI foreign key ( NOTIF_TYPE_ID ) references UTL_NOTIF_TYPE ( NOTIF_TYPE_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:67 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_API_VERSION', 'UTLAPIDEFINITION_UTLAPIVERSION');
END;
/

--changeSet MX-22041.1:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table UTL_API_VERSION add constraint FK_UTLAPIDEFN_UTLAPIVERSION foreign key ( API_ID ) references UTL_API_DEFINITION ( API_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:69 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_API_VERSION_CONFIG', 'UTLAPIVERSION_UTLAPIVERSIONCON');
END;
/

--changeSet MX-22041.1:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table UTL_API_VERSION_CONFIG add constraint FK_UTLAPIVERSION_UTLAPIVERSIO foreign key ( API_VERSION_ID ) references UTL_API_VERSION ( API_VERSION_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:71 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_API_NOTIF_ASSIGN', 'APIVERSION_APINOTIFASSIGN');
END;
/

--changeSet MX-22041.1:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table UTL_API_NOTIF_ASSIGN add constraint FK_UTLAPIVERSION_UTLAPINOTIFA foreign key ( API_VERSION_ID ) references UTL_API_VERSION ( API_VERSION_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:73 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_API_NOTIF_ASSIGN', 'NOTIFEVENTTYPE_APINOTIFASSIGN');
END;
/

--changeSet MX-22041.1:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table UTL_API_NOTIF_ASSIGN add constraint FK_UTLNOTIFEVENTTYPE_UTLAPINO foreign key ( NOTIF_EVENT_TYPE_ID ) references UTL_NOTIF_EVENT_TYPE ( NOTIF_EVENT_TYPE_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:75 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_NOTIF_DEFINITION', 'NOTIFTYPE_NOTIFDEF');
END;
/

--changeSet MX-22041.1:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table UTL_NOTIF_DEFINITION add constraint FK_UTLNOTIFTYPE_UTLNOTIFDEFIN foreign key ( NOTIF_TYPE_ID ) references UTL_NOTIF_TYPE ( NOTIF_TYPE_ID )  DEFERRABLE
');
END;
/

--changeSet MX-22041.1:77 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('UTL_API_NOTIF_ASSIGN', 'NOTIFDEF_APINOTIFASSIGN');
END;
/

--changeSet MX-22041.1:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table UTL_API_NOTIF_ASSIGN add constraint FK_UTLNOTIFDEFINITION_UTLAPIN foreign key ( NOTIF_DEFINITION_ID ) references UTL_NOTIF_DEFINITION ( NOTIF_DEFINITION_ID )  DEFERRABLE
');
END;
/