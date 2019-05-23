--liquibase formatted sql


--changeSet MX-22041.2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_FAILEFFECT" ON "FAIL_EFFECT" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_INVINV" ON "INV_INV" ("ORIG_ASSMBL_DB_ID","ORIG_ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLBOM_EQPASSMBLBOM" ON "EQP_ASSMBL_BOM" ("NH_ASSMBL_DB_ID","NH_ASSMBL_CD","NH_ASSMBL_BOM_ID")
');
END;
/

--changeSet MX-22041.2:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLBOM_EQPBOMPART" ON "EQP_BOM_PART" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID")
');
END;
/

--changeSet MX-22041.2:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLBOM_FAILMODE" ON "FAIL_MODE" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID")
');
END;
/

--changeSet MX-22041.2:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLBOM_TASKTASK" ON "TASK_TASK" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID")
');
END;
/

--changeSet MX-22041.2:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLPOS_EQPASSMBLPOS" ON "EQP_ASSMBL_POS" ("NH_ASSMBL_DB_ID","NH_ASSMBL_CD","NH_ASSMBL_BOM_ID","NH_ASSMBL_POS_ID")
');
END;
/

--changeSet MX-22041.2:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLPOS_INVINV" ON "INV_INV" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")
');
END;
/

--changeSet MX-22041.2:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLPOS_EVTINV" ON "EVT_INV" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")
');
END;
/

--changeSet MX-22041.2:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLPOS_SCHEDPART" ON "SCHED_PART" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")
');
END;
/

--changeSet MX-22041.2:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_INVINV" ON "INV_INV" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_EVTINV" ON "EVT_INV" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_SCHEDPART" ON "SCHED_PART" ("SCHED_BOM_PART_DB_ID","SCHED_BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_REQPART" ON "REQ_PART" ("REQ_BOM_PART_DB_ID","REQ_BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_TASKPARTLIST" ON "TASK_PART_LIST" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPMANUFACT_EQPPARTNO" ON "EQP_PART_NO" ("MANUFACT_DB_ID","MANUFACT_CD")
');
END;
/

--changeSet MX-22041.2:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EVTINV" ON "EVT_INV" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_REQPART_SPEC" ON "REQ_PART" ("REQ_SPEC_PART_NO_DB_ID","REQ_SPEC_PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_INVINV" ON "INV_INV" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_SCHEDSTASK" ON "SCHED_STASK" ("ORIG_PART_NO_DB_ID","ORIG_PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_SHIPSHIPMENTLINE" ON "SHIP_SHIPMENT_LINE" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTN_SCHEDPART" ON "SCHED_PART" ("SPEC_PART_NO_DB_ID","SPEC_PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTNO_TASKPARTTRANSFORM" ON "TASK_PART_TRANSFORM" ("NEW_PART_NO_DB_ID","NEW_PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTEVTH" ON "EVT_EVENT" ("H_EVENT_DB_ID","H_EVENT_ID")
');
END;
/

--changeSet MX-22041.2:25 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('EVT_EVENT_NHEVENT_FK');
END;
/

--changeSet MX-22041.2:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTEVENT" ON "EVT_EVENT" ("NH_EVENT_DB_ID","NH_EVENT_ID")
');
END;
/

--changeSet MX-22041.2:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTEVENTREL2" ON "EVT_EVENT_REL" ("REL_EVENT_DB_ID","REL_EVENT_ID")
');
END;
/

--changeSet MX-22041.2:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTSTAGE" ON "EVT_STAGE" ("STAGE_EVENT_DB_ID","STAGE_EVENT_ID")
');
END;
/

--changeSet MX-22041.2:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILEFFECT_EVTFAILEFFECT" ON "EVT_FAIL_EFFECT" ("FAIL_EFFECT_DB_ID","FAIL_EFFECT_ID")
');
END;
/

--changeSet MX-22041.2:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILMODE_SDFAULT" ON "SD_FAULT" ("FAIL_MODE_DB_ID","FAIL_MODE_ID")
');
END;
/

--changeSet MX-22041.2:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_EQPASSMBL" ON "EQP_ASSMBL" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_EQPASSMBLBOM" ON "EQP_ASSMBL_BOM" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_TASKTASKIETM" ON "TASK_TASK_IETM" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_EQPPARTNO" ON "EQP_PART_NO" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_EQPBOMPART" ON "EQP_BOM_PART" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_FAILMODEIETM" ON "FAIL_MODE_IETM" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_EVTIETM" ON "EVT_IETM" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_EVTTOOL" ON "EVT_TOOL" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:39 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INV_INV_HEVTINV_FK');
END;
/

--changeSet MX-22041.2:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_EVTINV" ON "EVT_INV" ("H_INV_NO_DB_ID","H_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:41 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('MAIN_INV_EVTINV');
END;
/

--changeSet MX-22041.2:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVIN_EVTINV" ON "EVT_INV" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:43 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INV_INV_NHEVTINV_FK');
END;
/

--changeSet MX-22041.2:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IVINV_EVTINV" ON "EVT_INV" ("NH_INV_NO_DB_ID","NH_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ININV_EVTINV" ON "EVT_INV" ("ASSMBL_INV_NO_DB_ID","ASSMBL_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_SHIPSHIPMENTLINE" ON "SHIP_SHIPMENT_LINE" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_REQPART" ON "REQ_PART" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVINVH" ON "INV_INV" ("H_INV_NO_DB_ID","H_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ININV_INVINVNH" ON "INV_INV" ("NH_INV_NO_DB_ID","NH_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVINVASS" ON "INV_INV" ("ASSMBL_INV_NO_DB_ID","ASSMBL_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVOWNER_INVINV" ON "INV_INV" ("OWNER_DB_ID","OWNER_ID")
');
END;
/

--changeSet MX-22041.2:52 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INVLOC_INVINV_COND_FK');
END;
/

--changeSet MX-22041.2:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVINV" ON "INV_INV" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVLOC" ON "INV_LOC" ("NH_LOC_DB_ID","NH_LOC_ID")
');
END;
/

--changeSet MX-22041.2:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_EVTLOC" ON "EVT_LOC" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVACFLIGHTPLAN" ON "INV_AC_FLIGHT_PLAN" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_SDFAULT" ON "SD_FAULT" ("FOUND_BY_HR_DB_ID","FOUND_BY_HR_ID")
');
END;
/

--changeSet MX-22041.2:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EVTEVENT" ON "EVT_EVENT" ("EDITOR_HR_DB_ID","EDITOR_HR_ID")
');
END;
/

--changeSet MX-22041.2:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EVTSTAGE" ON "EVT_STAGE" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_SCHEDSTASK" ON "SCHED_STASK" ("RO_VENDOR_DB_ID","RO_VENDOR_ID")
');
END;
/

--changeSet MX-22041.2:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_INVINV" ON "INV_INV" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.2:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGWORKDEPT_EVTDEPT" ON "EVT_DEPT" ("DEPT_DB_ID","DEPT_ID")
');
END;
/

--changeSet MX-22041.2:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGAUTHORITY_INVINV" ON "INV_INV" ("AUTHORITY_DB_ID","AUTHORITY_ID")
');
END;
/

--changeSet MX-22041.2:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_SCHEDSTASK" ON "SCHED_STASK" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.2:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_FAILMODE" ON "FAIL_MODE" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.2:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIPSHIPMENT_SHIPSHIPMENTLI" ON "SHIP_SHIPMENT_LINE" ("SHIPMENT_DB_ID","SHIPMENT_ID")
');
END;
/

--changeSet MX-22041.2:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILEFFECT_FAILMODEEFFECT" ON "FAIL_MODE_EFFECT" ("FAIL_EFFECT_DB_ID","FAIL_EFFECT_ID")
');
END;
/

--changeSet MX-22041.2:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILMODE_FAILMODEEFFECT" ON "FAIL_MODE_EFFECT" ("FAIL_MODE_DB_ID","FAIL_MODE_ID")
');
END;
/

--changeSet MX-22041.2:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_TASK_TASK" ON "TASK_TASK" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_TASK_TASKACTV" ON "TASK_TASK" ("ACTV_HR_DB_ID","ACTV_HR_ID")
');
END;
/

--changeSet MX-22041.2:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETM_TOPIC_EQPPARTIETM" ON "EQP_PART_IETM" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SUPPLY_LOC_INVLOC" ON "INV_LOC" ("SUPPLY_LOC_DB_ID","SUPPLY_LOC_ID")
');
END;
/

--changeSet MX-22041.2:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_STOCK_NO_EQPPARTNO" ON "EQP_PART_NO" ("STOCK_NO_DB_ID","STOCK_NO_ID")
');
END;
/

--changeSet MX-22041.2:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTSK_SCDWOLINE" ON "SCHED_WO_LINE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_EQPFLIGHTSPEC" ON "EQP_FLIGHT_SPEC" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPC_ORGVNDRIETM" ON "ORG_VENDOR_IETM" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:77 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPRTNO_SCHDINSTPRT" ON "SCHED_INST_PART" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_SCDINSTPRT" ON "SCHED_INST_PART" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:79 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_SCHDRMVDPRT" ON "SCHED_RMVD_PART" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHDRMVDPRT_EQPPRTNO" ON "SCHED_RMVD_PART" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_INVXFER" ON "INV_XFER" ("RECV_BY_HR_DB_ID","RECV_BY_HR_ID")
');
END;
/

--changeSet MX-22041.2:82 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCOUNT_POINVOICELINE" ON "PO_INVOICE_LINE" ("ACCOUNT_DB_ID","ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:83 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_SHIPSHIPMENTLINE" ON "SHIP_SHIPMENT_LINE" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.2:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_INVINV" ON "INV_INV" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.2:85 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POHEADER_SHIPSHIPMENT" ON "SHIP_SHIPMENT" ("PO_DB_ID","PO_ID")
');
END;
/

--changeSet MX-22041.2:86 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCNT_FNCACCNT" ON "FNC_ACCOUNT" ("NH_ACCOUNT_DB_ID","NH_ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:87 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POINVOICELINE_FNCXACTNLOG" ON "FNC_XACTION_LOG" ("PO_INVOICE_DB_ID","PO_INVOICE_ID","PO_INVOICE_LINE_ID")
');
END;
/

--changeSet MX-22041.2:88 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCNT_EQPPRTNO" ON "EQP_PART_NO" ("ASSET_ACCOUNT_DB_ID","ASSET_ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:89 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCNT_FNCXACTNACNT" ON "FNC_XACTION_ACCOUNT" ("ACCOUNT_DB_ID","ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:90 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_FNCXACTIONLOG" ON "FNC_XACTION_LOG" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:91 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCNT_EVTEVENT" ON "EVT_EVENT" ("ACCOUNT_DB_ID","ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:92 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPSTCKNO_REQPART" ON "REQ_PART" ("REQ_STOCK_NO_DB_ID","REQ_STOCK_NO_ID")
');
END;
/

--changeSet MX-22041.2:93 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_INVLOCCONTACT" ON "INV_LOC_CONTACT" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:94 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSPOS_TASKPRTLIST" ON "TASK_PART_LIST" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID","ASSMBL_POS_ID")
');
END;
/

--changeSet MX-22041.2:95 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_POHEADER" ON "PO_HEADER" ("SHIP_TO_LOC_DB_ID","SHIP_TO_LOC_ID")
');
END;
/

--changeSet MX-22041.2:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_POHEADER" ON "PO_HEADER" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.2:97 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDORACCNT_POHEADER" ON "PO_HEADER" ("VENDOR_ACCOUNT_DB_ID","VENDOR_ACCOUNT_ID","VENDOR_ACCOUNT_CD")
');
END;
/

--changeSet MX-22041.2:98 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_POHEADER" ON "PO_HEADER" ("CONTACT_HR_DB_ID","CONTACT_HR_ID")
');
END;
/

--changeSet MX-22041.2:99 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_POLINE" ON "PO_LINE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:100 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTNO_POLINE" ON "PO_LINE" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:101 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCOUNT_POLINE" ON "PO_LINE" ("ACCOUNT_DB_ID","ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:102 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_POAUTH" ON "PO_AUTH" ("AUTH_HR_DB_ID","AUTH_HR_ID")
');
END;
/

--changeSet MX-22041.2:103 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_REQPART" ON "REQ_PART" ("PR_SCHED_DB_ID","PR_SCHED_ID")
');
END;
/

--changeSet MX-22041.2:104 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPTASKZONE_NHEQPTSKZNE" ON "EQP_TASK_ZONE" ("NH_ZONE_DB_ID","NH_ZONE_ID")
');
END;
/

--changeSet MX-22041.2:105 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPTSKPANEL_SCHED_PANEL" ON "SCHED_PANEL" ("PANEL_DB_ID","PANEL_ID")
');
END;
/

--changeSet MX-22041.2:106 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REPLCTRL_REPLHIST" ON "REPL_HIST" ("REPL_DB_ID")
');
END;
/

--changeSet MX-22041.2:107 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPTASKPANEL_TASKPANEL" ON "TASK_PANEL" ("PANEL_DB_ID","PANEL_ID")
');
END;
/

--changeSet MX-22041.2:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPTASKZONE_TASKZONE" ON "TASK_ZONE" ("ZONE_DB_ID","ZONE_ID")
');
END;
/

--changeSet MX-22041.2:109 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPTSKZN_SCHEDZONE" ON "SCHED_ZONE" ("ZONE_DB_ID","ZONE_ID")
');
END;
/

--changeSet MX-22041.2:110 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_FNCXACTLOG" ON "FNC_XACTION_LOG" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.2:111 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENTINIT_INVXFER" ON "INV_XFER" ("INIT_EVENT_DB_ID","INIT_EVENT_ID")
');
END;
/

--changeSet MX-22041.2:112 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDINSTPART_REQPART" ON "REQ_PART" ("SCHED_DB_ID","SCHED_ID","SCHED_PART_ID","SCHED_INST_PART_ID")
');
END;
/

--changeSet MX-22041.2:113 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTNO_TASKPARTLIST" ON "TASK_PART_LIST" ("SPEC_PART_NO_DB_ID","SPEC_PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:114 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_REQPART" ON "REQ_PART" ("REQ_HR_DB_ID","REQ_HR_ID")
');
END;
/

--changeSet MX-22041.2:115 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVACREG_REQPART" ON "REQ_PART" ("REQ_AC_INV_NO_DB_ID","REQ_AC_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:116 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOCREQ_REQPART" ON "REQ_PART" ("REQ_LOC_DB_ID","REQ_LOC_ID")
');
END;
/

--changeSet MX-22041.2:117 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_REQPART" ON "REQ_PART" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.2:118 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCNT_REQPART" ON "REQ_PART" ("ISSUE_ACCOUNT_DB_ID","ISSUE_ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:119 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOCPO_REQPART" ON "REQ_PART" ("PO_DEST_LOC_DB_ID","PO_DEST_LOC_ID")
');
END;
/

--changeSet MX-22041.2:120 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_AUTORSRVQUEUE" ON "AUTO_RSRV_QUEUE" ("SUP_LOC_DB_ID","SUP_LOC_ID")
');
END;
/

--changeSet MX-22041.2:121 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_AUTORSRVQUEUE" ON "AUTO_RSRV_QUEUE" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:122 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPRTNO_AUTORSRVQUEUE" ON "AUTO_RSRV_QUEUE" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:123 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKPARMDATA" ON "TASK_PARM_DATA" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.2:124 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_EVTATTACH" ON "EVT_ATTACH" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:125 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REPLEQPASSBOM_TASKTASK" ON "TASK_TASK" ("REPL_ASSMBL_DB_ID","REPL_ASSMBL_CD","REPL_ASSMBL_BOM_ID")
');
END;
/

--changeSet MX-22041.2:126 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDSTASK" ON "SCHED_STASK" ("DUP_JIC_SCHED_DB_ID","DUP_JIC_SCHED_ID")
');
END;
/

--changeSet MX-22041.2:127 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPTSKZONE_EQPTSKPANEL" ON "EQP_TASK_PANEL" ("ZONE_DB_ID","ZONE_ID")
');
END;
/

--changeSet MX-22041.2:128 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_EQPTASKZONE" ON "EQP_TASK_ZONE" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:129 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_EQPTASKPANEL" ON "EQP_TASK_PANEL" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:130 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTVD_EQPRTVENDPRICE" ON "EQP_PART_VENDOR_PRICE" ("VENDOR_DB_ID","VENDOR_ID","PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:131 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TSKFAILMODE" ON "TASK_FAIL_MODE" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.2:132 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILMOD_TASKFALMODE" ON "TASK_FAIL_MODE" ("FAIL_MODE_DB_ID","FAIL_MODE_ID")
');
END;
/

--changeSet MX-22041.2:133 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHED_STASK_INVLOC" ON "INV_LOC" ("PREDRAW_SCHED_DB_ID","PREDRAW_SCHED_ID")
');
END;
/

--changeSet MX-22041.2:134 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORG_HR_INVLOCPARTCOUNT" ON "INV_LOC_PART_COUNT" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:135 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV_INV_INVLOCINVRECOUNT" ON "INV_LOC_INV_RECOUNT" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:136 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_ORGVENDOR" ON "ORG_VENDOR" ("VENDOR_LOC_DB_ID","VENDOR_LOC_ID")
');
END;
/

--changeSet MX-22041.2:137 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORG_HR_INVLOCINVRECOUNT" ON "INV_LOC_INV_RECOUNT" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:138 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_POINVOICE" ON "PO_INVOICE" ("CONTACT_HR_DB_ID","CONTACT_HR_ID")
');
END;
/

--changeSet MX-22041.2:139 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_POINVOICE" ON "PO_INVOICE" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.2:140 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_POINVOICELINE" ON "PO_INVOICE_LINE" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:141 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDORACCOUNT_POINVOICE" ON "PO_INVOICE" ("VENDOR_ACCOUNT_DB_ID","VENDOR_ACCOUNT_ID","VENDOR_ACCOUNT_CD")
');
END;
/

--changeSet MX-22041.2:142 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCOUNT_SHIPSHIPMENT" ON "SHIP_SHIPMENT" ("RETURN_ACCOUNT_DB_ID","RETURN_ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:143 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EVTTOOL" ON "EVT_TOOL" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:144 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGAUTHORITY_EQPASSMBL" ON "EQP_ASSMBL" ("AUTHORITY_DB_ID","AUTHORITY_ID")
');
END;
/

--changeSet MX-22041.2:145 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_EVTTOOL" ON "EVT_TOOL" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:146 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTOOLLIST_EVTTOOL" ON "EVT_TOOL" ("TASK_DB_ID","TASK_ID","TASK_TOOL_ID")
');
END;
/

--changeSet MX-22041.2:147 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_TASKTOOLLIST" ON "TASK_TOOL_LIST" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:148 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_NH_EQPASSMBLPOS_SCHEDPART" ON "SCHED_PART" ("NH_ASSMBL_DB_ID","NH_ASSMBL_CD","NH_ASSMBL_BOM_ID","NH_ASSMBL_POS_ID")
');
END;
/

--changeSet MX-22041.2:149 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_REQPART_PO" ON "REQ_PART" ("PO_PART_NO_DB_ID","PO_PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:150 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCTCODE_FNCACCOUNT" ON "FNC_ACCOUNT" ("TCODE_DB_ID","TCODE_ID")
');
END;
/

--changeSet MX-22041.2:151 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_FNCXACTIONLOG" ON "FNC_XACTION_LOG" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.2:152 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_FNCXACTIONLOG" ON "FNC_XACTION_LOG" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:153 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_RFQHEADER" ON "RFQ_HEADER" ("CONTACT_HR_DB_ID","CONTACT_HR_ID")
');
END;
/

--changeSet MX-22041.2:154 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTNO_RFQLINE" ON "RFQ_LINE" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:155 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_RFQLINE" ON "RFQ_LINE" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.2:156 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_RFQLINE" ON "RFQ_LINE" ("SHIP_TO_LOC_DB_ID","SHIP_TO_LOC_ID")
');
END;
/

--changeSet MX-22041.2:157 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SDFAULT_SCHEDSTASK" ON "SCHED_STASK" ("FAULT_DB_ID","FAULT_ID")
');
END;
/

--changeSet MX-22041.2:158 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FCMODEL_INVACREG" ON "INV_AC_REG" ("FORECAST_MODEL_DB_ID","FORECAST_MODEL_ID")
');
END;
/

--changeSet MX-22041.2:159 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_HSCHEDSTASK" ON "SCHED_STASK" ("H_SCHED_DB_ID","H_SCHED_ID")
');
END;
/

--changeSet MX-22041.2:160 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOCPRINTED_REQPART" ON "REQ_PART" ("PRINTED_SUPPLY_LOC_DB_ID","PRINTED_SUPPLY_LOC_ID")
');
END;
/

--changeSet MX-22041.2:161 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_SCHEDWOLINE" ON "SCHED_WO_LINE" ("COLLECTION_HR_DB_ID","COLLECTION_HR_ID")
');
END;
/

--changeSet MX-22041.2:162 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLOBDATA_ESIGDOC" ON "ESIG_DOC" ("BLOB_DB_ID","BLOB_ID")
');
END;
/

--changeSet MX-22041.2:163 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_REQPART" ON "REQ_PART" ("REMOTE_LOC_DB_ID","REMOTE_LOC_ID")
');
END;
/

--changeSet MX-22041.2:164 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIPSHIPMENTLINE_REQPART" ON "REQ_PART" ("SHIPMENT_LINE_DB_ID","SHIPMENT_LINE_ID")
');
END;
/

--changeSet MX-22041.2:165 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_HUBINVLOC_INV_LOC" ON "INV_LOC" ("HUB_LOC_DB_ID","HUB_LOC_ID")
');
END;
/

--changeSet MX-22041.2:166 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCOUNT_SCHEDSTASK" ON "SCHED_STASK" ("ISSUE_ACCOUNT_DB_ID","ISSUE_ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:167 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SHIPSHIPMENT" ON "SHIP_SHIPMENT" ("CHECK_DB_ID","CHECK_ID")
');
END;
/

--changeSet MX-22041.2:168 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCOUNT_INVACREG" ON "INV_AC_REG" ("ISSUE_ACCOUNT_DB_ID","ISSUE_ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:169 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCOUNT_TASKTASK" ON "TASK_TASK" ("ISSUE_ACCOUNT_DB_ID","ISSUE_ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:170 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_LPALOG" ON "LPA_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:171 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_LPASTASKMAINTOP" ON "LPA_STASK_MAINT_OP" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:172 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVOWNER_ORGVENDOR" ON "ORG_VENDOR" ("OWNER_DB_ID","OWNER_ID")
');
END;
/

--changeSet MX-22041.2:173 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVOWNER_POLINE" ON "PO_LINE" ("OWNER_DB_ID","OWNER_ID")
');
END;
/

--changeSet MX-22041.2:174 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REPLSCHEDSTASK_POLINE" ON "PO_LINE" ("REPL_TASK_DB_ID","REPL_TASK_ID")
');
END;
/

--changeSet MX-22041.2:175 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIPSHIPMENT_POLINE" ON "PO_LINE" ("XCHG_SHIPMENT_DB_ID","XCHG_SHIPMENT_ID")
');
END;
/

--changeSet MX-22041.2:176 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ESIGDOC_ESIGDOCSIGN" ON "ESIG_DOC_SIGN" ("DOC_DB_ID","DOC_ID")
');
END;
/

--changeSet MX-22041.2:177 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ESIGDOC" ON "ESIG_DOC" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:178 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCACCOUNT_RFQLINE" ON "RFQ_LINE" ("ACCOUNT_DB_ID","ACCOUNT_ID")
');
END;
/

--changeSet MX-22041.2:179 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHRCERT_ORGHRCERT" ON "ORG_HR_CERT" ("CERTIFIER_HR_DB_ID","CERTIFIER_HR_ID","CERTIFIER_CERT_ID")
');
END;
/

--changeSet MX-22041.2:180 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHRCERT_ESIGDOCSIGN" ON "ESIG_DOC_SIGN" ("HR_DB_ID","HR_ID","CERT_ID")
');
END;
/

--changeSet MX-22041.2:181 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_SCHEDACTION" ON "SCHED_ACTION" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:182 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_RFQLINEVENDOR" ON "RFQ_LINE_VENDOR" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:183 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTVNDR_RFQLINEVENDOR" ON "RFQ_LINE_VENDOR" ("PURCH_VENDOR_DB_ID","PURCH_VENDOR_ID","PURCH_PART_NO_DB_ID","PURCH_PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:184 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTVNDRPRICE_RFQLINEVNDR" ON "RFQ_LINE_VENDOR" ("PART_VENDOR_PRICE_DB_ID","PART_VENDOR_PRICE_ID")
');
END;
/

--changeSet MX-22041.2:185 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_RFQLINE_REQPART" ON "REQ_PART" ("RFQ_DB_ID","RFQ_ID","RFQ_LINE_ID")
');
END;
/

--changeSet MX-22041.2:186 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SBQUERY_SBCOLUMN" ON "SB_COLUMN" ("STATUS_BOARD_DB_ID","STATUS_BOARD_ID","QUERY_ID")
');
END;
/

--changeSet MX-22041.2:187 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SBCOLUMNGROUP_SBCOLUMN" ON "SB_COLUMN" ("STATUS_BOARD_DB_ID","STATUS_BOARD_ID","GROUP_ID")
');
END;
/

--changeSet MX-22041.2:188 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_TASKLABOURSUMMARY" ON "DWT_TASK_LABOUR_SUMMARY" ("ASSMBL_INV_NO_DB_ID","ASSMBL_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:189 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_TASKLABOURSUMMARY" ON "DWT_TASK_LABOUR_SUMMARY" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:190 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVOWNER_INVLOCSTOCK" ON "INV_LOC_STOCK" ("OWNER_DB_ID","OWNER_ID")
');
END;
/

--changeSet MX-22041.2:191 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVCSGNXCHG" ON "INV_CSGN_XCHG" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:192 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POHEADER_INVCSGNXCHG" ON "INV_CSGN_XCHG" ("PO_DB_ID","PO_ID")
');
END;
/

--changeSet MX-22041.2:193 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVOWNER_INVLOCBIN" ON "INV_LOC_BIN" ("OWNER_DB_ID","OWNER_ID")
');
END;
/

--changeSet MX-22041.2:194 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_REQPART" ON "REQ_PART" ("PO_VENDOR_DB_ID","PO_VENDOR_ID")
');
END;
/

--changeSet MX-22041.2:195 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_MAINTPRGMTASK" ON "MAINT_PRGM_TASK" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.2:196 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_MAINTPRGMTASK" ON "MAINT_PRGM_TASK" ("ACTION_HR_DB_ID","ACTION_HR_ID")
');
END;
/

--changeSet MX-22041.2:197 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_MAINTPRGM" ON "MAINT_PRGM" ("ACTV_HR_DB_ID","ACTV_HR_ID")
');
END;
/

--changeSet MX-22041.2:198 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAINTPRGMDEFN_MAINTPRGM" ON "MAINT_PRGM" ("MAINT_PRGM_DEFN_DB_ID","MAINT_PRGM_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:199 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_MAINTPRGMDEFN" ON "MAINT_PRGM_DEFN" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:200 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_TASKREVHR" ON "TASK_TASK" ("REV_HR_DB_ID","REV_HR_ID")
');
END;
/

--changeSet MX-22041.2:201 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PUBHR_LRPPLAN" ON "LRP_PLAN" ("PUB_HR_DB_ID","PUB_HR_ID")
');
END;
/

--changeSet MX-22041.2:202 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_UPDATEHR_LRPPLAN" ON "LRP_PLAN" ("CREATED_HR_DB_ID","CREATED_HR_ID")
');
END;
/

--changeSet MX-22041.2:203 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CREATEHR_LRPPLAN" ON "LRP_PLAN" ("UPDATED_HR_DB_ID","UPDATED_HR_ID")
');
END;
/

--changeSet MX-22041.2:204 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARRIER_LRPINVINV" ON "LRP_INV_INV" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.2:205 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_LRPINVINV" ON "LRP_INV_INV" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:206 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_LRPINVINV" ON "LRP_INV_INV" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:207 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FCMODEL_LRPINVINV" ON "LRP_INV_INV" ("FORECAST_MODEL_DB_ID","FORECAST_MODEL_ID")
');
END;
/

--changeSet MX-22041.2:208 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGAUTHORITY_ORGCARRIER" ON "ORG_CARRIER" ("AUTHORITY_DB_ID","AUTHORITY_ID")
');
END;
/

--changeSet MX-22041.2:209 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LICDEFN_INVACREG" ON "INV_AC_REG" ("LIC_DB_ID","LIC_ID")
');
END;
/

--changeSet MX-22041.2:210 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_TAGTASKDEFN" ON "TAG_TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:211 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_TAGTAG" ON "TAG_TAG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:212 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDPANEL_OPNMPCSCHEDPANEL" ON "SCHED_PANEL" ("OPN_MPC_SCHED_DB_ID","OPN_MPC_SCHED_ID","OPN_MPC_SCHED_PANEL_ID")
');
END;
/

--changeSet MX-22041.2:213 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDPANEL_CLSMPCSCHEDPANEL" ON "SCHED_PANEL" ("CLS_MPC_SCHED_DB_ID","CLS_MPC_SCHED_ID","CLS_MPC_SCHED_PANEL_ID")
');
END;
/

--changeSet MX-22041.2:214 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVKIT_INVXFER" ON "INV_XFER" ("KIT_INV_NO_DB_ID","KIT_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:215 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARRIER_INVINV" ON "INV_INV" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.2:216 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LICDEFN_TASKLIC" ON "TASK_LIC" ("LIC_DB_ID","LIC_ID")
');
END;
/

--changeSet MX-22041.2:217 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LICDEFN_ORGHRLIC" ON "ORG_HR_LIC" ("LIC_DB_ID","LIC_ID")
');
END;
/

--changeSet MX-22041.2:218 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARRIER_LICDEFN" ON "LIC_DEFN" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.2:219 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_LICDEFN" ON "LIC_DEFN" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:220 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LICDEFN_GRPDEFNLIC" ON "GRP_DEFN_LIC" ("LIC_DB_ID","LIC_ID")
');
END;
/

--changeSet MX-22041.2:221 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_TASKTASKDEP" ON "TASK_TASK_DEP" ("DEP_TASK_DEFN_DB_ID","DEP_TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:222 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDCOSTLINE" ON "SCHED_COST_LINE_ITEM" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:223 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_EQPKITPARTGROUPS" ON "EQP_KIT_PART_GROUPS" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:224 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPFINDING_EVTFINDING" ON "EVT_FINDING" ("FINDING_DB_ID","FINDING_ID")
');
END;
/

--changeSet MX-22041.2:225 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_EQPFINDING" ON "EQP_FINDING" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:226 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_EQPFINDING" ON "EVT_FINDING" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:227 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EQPFINDING" ON "EVT_FINDING" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:228 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_INVLOC" ON "INV_LOC" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.2:229 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_WORKSCOPE" ON "LRP_EVENT_WORKSCOPE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:230 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYDEFN_WARRANTY_INIT" ON "WARRANTY_INIT" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:231 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_INVOWNER" ON "INV_OWNER" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.2:232 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_ORGCARRIER" ON "ORG_CARRIER" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.2:233 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_WARRANTYDEFN" ON "WARRANTY_DEFN" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.2:234 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOCRESHIP_POHEADER" ON "PO_HEADER" ("RE_SHIP_TO_DB_ID","RE_SHIP_TO_ID")
');
END;
/

--changeSet MX-22041.2:235 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_WARRANTYEVALQUEU" ON "WARRANTY_EVAL_QUEUE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:236 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYINIT_WARRANTYEVAL" ON "WARRANTY_EVAL" ("WARRANTY_INIT_DB_ID","WARRANTY_INIT_ID")
');
END;
/

--changeSet MX-22041.2:237 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_SHIPSEGFROM" ON "SHIP_SEGMENT" ("SHIP_FROM_DB_ID","SHIP_FROM_ID")
');
END;
/

--changeSet MX-22041.2:238 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_SHIPSEG" ON "SHIP_SEGMENT" ("COMPLETE_HR_DB_ID","COMPLETE_HR_ID")
');
END;
/

--changeSet MX-22041.2:239 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_SHIPSEGTO" ON "SHIP_SEGMENT" ("SHIP_TO_DB_ID","SHIP_TO_ID")
');
END;
/

--changeSet MX-22041.2:240 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDPART_WARRANTYEVALPART" ON "WARRANTY_EVAL_PART" ("WORKSCOPE_SCHED_DB_ID","WORKSCOPE_SCHED_ID","WORKSCOPE_SCHED_PART_ID")
');
END;
/

--changeSet MX-22041.2:241 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_WARRANTYEVALTASK" ON "WARRANTY_EVAL_TASK" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:242 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_WARRANTYEVAL" ON "WARRANTY_EVAL" ("WP_SCHED_DB_ID","WP_SCHED_ID")
');
END;
/

--changeSet MX-22041.2:243 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_WARRANTYEVAL" ON "WARRANTY_EVAL" ("REJECT_HR_DB_ID","REJECT_HR_ID")
');
END;
/

--changeSet MX-22041.2:244 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ASSIGNORGHR_REQPART" ON "REQ_PART" ("ASSIGN_HR_DB_ID","ASSIGN_HR_ID")
');
END;
/

--changeSet MX-22041.2:245 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLBOM_EQPASSMBTH" ON "EQP_ASSMBL_BOM_THRESHOLD" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID")
');
END;
/

--changeSet MX-22041.2:246 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_FAILDEFERREF" ON "FAIL_DEFER_REF" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:247 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_TASKTASKLOCKED" ON "TASK_TASK" ("LOCKED_HR_DB_ID","LOCKED_HR_ID")
');
END;
/

--changeSet MX-22041.2:248 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGAUTHORITY_FCMODEL" ON "FC_MODEL" ("AUTHORITY_DB_ID","AUTHORITY_ID")
');
END;
/

--changeSet MX-22041.2:249 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_NHORGORG" ON "ORG_ORG" ("NH_ORG_DB_ID","NH_ORG_ID")
');
END;
/

--changeSet MX-22041.2:250 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_COMPANY_ORGORG" ON "ORG_ORG" ("COMPANY_ORG_DB_ID","COMPANY_ORG_ID")
');
END;
/

--changeSet MX-22041.2:251 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('LRPEVENT_LRPINVINV_FK');
END;
/

--changeSet MX-22041.2:252 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPINV_LRPEVENT" ON "LRP_EVENT" ("LRP_DB_ID","LRP_ID","LRP_INV_INV_ID")
');
END;
/

--changeSet MX-22041.2:253 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('LRPEVENT_LRPLOC_FK');
END;
/

--changeSet MX-22041.2:254 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPLOC_LRPEVENT" ON "LRP_EVENT" ("LRP_DB_ID","LRP_ID","LRP_LOC_DB_ID","LRP_LOC_ID")
');
END;
/

--changeSet MX-22041.2:255 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPLAN_LRPEVENT" ON "LRP_EVENT" ("LRP_DB_ID","LRP_ID")
');
END;
/

--changeSet MX-22041.2:256 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_LRPEVENT" ON "LRP_EVENT" ("LOCK_HR_DB_ID","LOCK_HR_ID")
');
END;
/

--changeSet MX-22041.2:257 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGLOGO_BLOBDATA" ON "ORG_LOGO" ("BLOB_DB_ID","BLOB_ID")
');
END;
/

--changeSet MX-22041.2:258 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_ORGLOGO" ON "ORG_LOGO" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.2:259 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_LRPEVENT" ON "LRP_EVENT" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:260 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFLEVELDEFN_WFLEVEL" ON "WF_LEVEL" ("WF_LEVEL_DEFN_DB_ID","WF_LEVEL_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:261 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_WFLEVEL" ON "WF_LEVEL" ("LEVEL_HR_DB_ID","LEVEL_HR_ID")
');
END;
/

--changeSet MX-22041.2:262 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_ZIPTASK" ON "ZIP_TASK" ("ASSMBL_INV_NO_DB_ID","ASSMBL_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:263 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPEVENT_WORKSCOPE" ON "LRP_EVENT_WORKSCOPE" ("LRP_EVENT_DB_ID","LRP_EVENT_ID")
');
END;
/

--changeSet MX-22041.2:264 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_ASSSUBTYPE" ON "EQP_ASSMBL_SUBTYPE" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:265 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARRIER_ERHEADER" ON "ER_HEADER" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.2:266 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('LRPEVENT_LRPACTUALLOC_FK');
END;
/

--changeSet MX-22041.2:267 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ACTLRPLOC_LRPEVENT" ON "LRP_EVENT" ("LRP_DB_ID","LRP_ID","ACTUAL_LOC_DB_ID","ACTUAL_LOC_ID")
');
END;
/

--changeSet MX-22041.2:268 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLSUBTYPE_INVINV" ON "INV_INV" ("ASSMBL_SUBTYPE_DB_ID","ASSMBL_SUBTYPE_ID")
');
END;
/

--changeSet MX-22041.2:269 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_ERHEADER" ON "ER_HEADER" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:270 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSSUBTYPE_ERHEAD" ON "ER_HEADER" ("ASSMBL_SUBTYPE_DB_ID","ASSMBL_SUBTYPE_ID")
');
END;
/

--changeSet MX-22041.2:271 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EQPBOMPARTLOC" ON "EQP_BOM_PART_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:272 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EQPPARTNOLOG" ON "EQP_PART_NO_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:273 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EQPASSMBLBOMLOG" ON "EQP_ASSMBL_BOM_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:274 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLSUBTYPE_LRPINVINV" ON "LRP_INV_INV" ("ASSMBL_SUBTYPE_DB_ID","ASSMBL_SUBTYPE_ID")
');
END;
/

--changeSet MX-22041.2:275 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_WARRANTYIETM" ON "WARRANTY_IETM" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.2:276 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('LRPWORKSCOPE_TASKDEFN_FK');
END;
/

--changeSet MX-22041.2:277 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPTASKDEFN_LRPEVENTWORKSCO" ON "LRP_EVENT_WORKSCOPE" ("LRP_DB_ID","LRP_ID","TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:278 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('LRPWORKSCOPE_LRPPLAN_FK');
END;
/

--changeSet MX-22041.2:279 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKSCHEDRULE_LRPEVENTWORKS" ON "LRP_EVENT_WORKSCOPE" ("RULE_TASK_DB_ID","RULE_TASK_ID","RULE_DATA_TYPE_DB_ID","RULE_DATA_TYPE_ID")
');
END;
/

--changeSet MX-22041.2:280 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYEVAL_CLAIM" ON "CLAIM" ("WARRANTY_EVAL_DB_ID","WARRANTY_EVAL_ID")
');
END;
/

--changeSet MX-22041.2:281 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_CLAIM" ON "CLAIM" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:282 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_CLAIM" ON "CLAIM" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.2:283 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_CLAIM" ON "CLAIM" ("CONTACT_HR_DB_ID","CONTACT_HR_ID")
');
END;
/

--changeSet MX-22041.2:284 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_CLAIMPARTLINE" ON "CLAIM_PART_LINE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:285 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_CLAIMPARTLINE" ON "CLAIM_PART_LINE" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:286 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_CLAIMPARTLINE" ON "CLAIM_PART_LINE" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:287 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WAREVALLAB_CLMLABLINE" ON "CLAIM_LABOUR_LINE" ("WARRANTY_EVAL_DB_ID","WARRANTY_EVAL_ID","WARRANTY_EVAL_TASK_ID","WARRANTY_EVAL_LABOUR_ID")
');
END;
/

--changeSet MX-22041.2:288 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_CLAIMLABOURLINE" ON "CLAIM_LABOUR_LINE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:289 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WAREVALPRT_CLMPRTLINE" ON "CLAIM_PART_LINE" ("WARRANTY_EVAL_DB_ID","WARRANTY_EVAL_ID","WARRANTY_EVAL_TASK_ID","WARRANTY_EVAL_PART_ID")
');
END;
/

--changeSet MX-22041.2:290 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PREVEVENTWORKSCOPE_EVENTWOR" ON "LRP_EVENT_WORKSCOPE" ("PREV_WORKSCOPE_DB_ID","PREV_WORKSCOPE_ID")
');
END;
/

--changeSet MX-22041.2:291 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_GRPDEFN_LICDEFNPREREQ" ON "LIC_DEFN_PREREQ" ("GRP_DEFN_DB_ID","GRP_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:292 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPTASKDEFN_LRPTASKDEFN" ON "LRP_TASK_DEFN" ("PREV_LRP_DB_ID","PREV_LRP_ID","PREV_TASK_DEFN_DB_ID","PREV_TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:293 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_SCHEDACTIONCANCELHR" ON "SCHED_ACTION" ("CANCEL_HR_DB_ID","CANCEL_HR_ID")
');
END;
/

--changeSet MX-22041.2:294 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHEDRMVDPART_INVINV" ON "INV_INV" ("SCHED_DB_ID","SCHED_ID","SCHED_PART_ID","SCHED_RMVD_PART_ID")
');
END;
/

--changeSet MX-22041.2:295 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVIN_INVXFER" ON "INV_XFER" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:296 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAININV_SHEDSTASK" ON "SCHED_STASK" ("MAIN_INV_NO_DB_ID","MAIN_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:297 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFDEFN_WFWF" ON "WF_WF" ("WF_DEFN_DB_ID","WF_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:298 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_TASTTASK_ENGCNTC" ON "TASK_TASK" ("ENG_CONTACT_HR_DB_ID","ENG_CONTACT_HR_ID")
');
END;
/

--changeSet MX-22041.2:299 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_SCHEDLBRROLESTAT" ON "SCHED_LABOUR_ROLE_STATUS" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:300 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBR_SCHEDLBRROLE" ON "SCHED_LABOUR_ROLE" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.2:301 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_QUARQUAR" ON "QUAR_QUAR" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.2:302 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_QUARQUAR" ON "QUAR_QUAR" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:303 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_QUARQUAR" ON "QUAR_QUAR" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:304 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_QUARQUAR" ON "QUAR_QUAR" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:305 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHDSTASK_WOSCHDLBR" ON "SCHED_LABOUR" ("WO_SCHED_DB_ID","WO_SCHED_ID")
');
END;
/

--changeSet MX-22041.2:306 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHDLBR" ON "SCHED_LABOUR" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:307 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGWORKDEPT_QUARACTNASSIGN" ON "QUAR_ACTION_ASSIGNMENT" ("DEPT_DB_ID","DEPT_ID")
');
END;
/

--changeSet MX-22041.2:308 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_QUARACTNASSIGN" ON "QUAR_ACTION_ASSIGNMENT" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:309 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_QUARACTIONSTATUS" ON "QUAR_ACTION_STATUS" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:310 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_POLINEKITLINE" ON "PO_LINE_KIT_LINE" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:311 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_POLINEKITLINE" ON "PO_LINE_KIT_LINE" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:312 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_POLINEKITLINE" ON "PO_LINE_KIT_LINE" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.2:313 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVKIT_POLINEKITLINE" ON "PO_LINE_KIT_LINE" ("INV_KIT_DB_ID","INV_KIT_ID")
');
END;
/

--changeSet MX-22041.2:314 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_SUBORGCACHE_SUBORG" ON "ORG_SUBORG_CACHE" ("SUB_ORG_DB_ID","SUB_ORG_ID")
');
END;
/

--changeSet MX-22041.2:315 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_TASKTASK" ON "TASK_TASK" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.2:316 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_SUBORGCACHE_PARENT" ON "ORG_SUBORG_CACHE" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.2:317 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_EQPINSTKITMAP" ON "EQP_INSTALL_KIT_MAP" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.2:318 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EQPINSTKITMAP" ON "EQP_INSTALL_KIT_MAP" ("KIT_PART_NO_DB_ID","KIT_PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:319 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_SCHEDWPSIGN" ON "SCHED_WP_SIGN" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:320 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDWP_SCHEDWPSIGNREQ" ON "SCHED_WP_SIGN_REQ" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:321 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBR_WARRANTYEVALLBR" ON "WARRANTY_EVAL_LABOUR" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.2:322 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBRRL_SCHEDLBRRLSTAT" ON "SCHED_LABOUR_ROLE_STATUS" ("LABOUR_ROLE_DB_ID","LABOUR_ROLE_ID")
');
END;
/

--changeSet MX-22041.2:323 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLOB_DATA_ESIG_DOC_SIGN" ON "ESIG_DOC_SIGN" ("BLOB_DB_ID","BLOB_ID")
');
END;
/

--changeSet MX-22041.2:324 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EQPPARTROTABLE" ON "EQP_PART_ROTABLE_ADJUST" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.2:325 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_TASKREFDOCDISPBY" ON "TASK_REF_DOC" ("DISPOSITION_HR_DB_ID","DISPOSITION_HR_ID")
');
END;
/

--changeSet MX-22041.2:326 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_TASKREFDOC" ON "TASK_REF_DOC" ("RECEIVE_BY_HR_DB_ID","RECEIVE_BY_HR_ID")
');
END;
/

--changeSet MX-22041.2:327 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPMANUFACT_TASKREFDOC" ON "TASK_REF_DOC" ("MANUFACT_DB_ID","MANUFACT_CD")
');
END;
/

--changeSet MX-22041.2:328 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_EQPPRTROTABLE" ON "EQP_PART_ROTABLE_ADJUST" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:329 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_EQPPARTROTABLE" ON "EQP_PART_ROTABLE_ADJUST" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.2:330 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EQPPRTROTABLE" ON "EQP_PART_ROTABLE_ADJUST" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:331 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ISSUEORGHR_INVRELNOTE" ON "INV_RELIABILITY_NOTE" ("ISSUE_HR_DB_ID","ISSUE_HR_ID")
');
END;
/

--changeSet MX-22041.2:332 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_RESOLVEORGHR_INVRELNOTE" ON "INV_RELIABILITY_NOTE" ("RESOLVE_HR_DB_ID","RESOLVE_HR_ID")
');
END;
/

--changeSet MX-22041.2:333 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVKIT_INVKITMAP" ON "INV_KIT_MAP" ("KIT_INV_NO_DB_ID","KIT_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:334 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARRIER_TASKREFDOC" ON "TASK_REF_DOC" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.2:335 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPKITPARTGROUPS_INVKITMAP" ON "INV_KIT_MAP" ("EQP_KIT_PART_GROUP_DB_ID","EQP_KIT_PART_GROUP_ID")
');
END;
/

--changeSet MX-22041.2:336 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVPOSDESCQUEUE" ON "INV_POS_DESC_QUEUE" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:337 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_KITSYNCQUEUE" ON "KIT_SYNC_QUEUE" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:338 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_POHEADER" ON "PO_HEADER" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.2:339 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EQPPARTVENDORADV" ON "EQP_PART_VENDOR_ADVSRY" ("CLEAR_HR_DB_ID","CLEAR_HR_ID")
');
END;
/

--changeSet MX-22041.2:340 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_TASKTASKLOG" ON "TASK_TASK_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:341 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EQPADVSRY" ON "EQP_ADVSRY" ("ADVSRY_HR_DB_ID","ADVSRY_HR_ID")
');
END;
/

--changeSet MX-22041.2:342 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EQPPARTADVSRY" ON "EQP_PART_ADVSRY" ("CLEAR_HR_DB_ID","CLEAR_HR_ID")
');
END;
/

--changeSet MX-22041.2:343 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_INVADVSRY" ON "INV_ADVSRY" ("CLEAR_HR_DB_ID","CLEAR_HR_ID")
');
END;
/

--changeSet MX-22041.2:344 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVOILSTLOG_INVOILSTRATE" ON "INV_OIL_STATUS_RATE" ("OIL_STATUS_INV_NO_DB_ID","OIL_STATUS_INV_NO_ID","OIL_STATUS_LOG_ID")
');
END;
/

--changeSet MX-22041.2:345 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_INVOILSTATUSLOG" ON "INV_OIL_STATUS_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:346 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_INVOILSTATUSLOG" ON "INV_OIL_STATUS_LOG" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:347 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPORLMVSN_VSN" ON "DPO_RLM_VERSION" ("PARENT_RULE_VERSION_ID")
');
END;
/

--changeSet MX-22041.2:348 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPORLMEVT_STOBJTYPE" ON "DPO_RLM_EVT_STRUCT" ("RULE_OBJ_TYPE_CD")
');
END;
/

--changeSet MX-22041.2:349 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_EXFEREXPORT" ON "DPO_XFER_EXPORT_INV" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:350 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DIMTIME_FCTINVOIL" ON "FCT_INV_OIL" ("DIM_TIME_ID")
');
END;
/

--changeSet MX-22041.2:351 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DIMOILSTATUS_FCTINVOIL" ON "FCT_INV_OIL" ("DIM_OIL_STATUS_ID")
');
END;
/

--changeSet MX-22041.2:352 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DIMINV_FCTINVOIL" ON "FCT_INV_OIL" ("DIM_INV_ID")
');
END;
/

--changeSet MX-22041.2:353 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARR_EQPOILTHRESHCARR" ON "EQP_OIL_THRESHOLD_CARRIER" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.2:354 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_EQPOILTHRESHINV" ON "EQP_OIL_THRESHOLD_INV" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:355 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTNO_EQPOILTHRESHPART" ON "EQP_OIL_THRESHOLD_PART" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:356 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_XFEREXPFILE" ON "DPO_XFER_EXPORT_FILE" ("TARGET_LOC_DB_ID","TARGET_LOC_ID")
');
END;
/

--changeSet MX-22041.2:357 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_SOURCELOC" ON "DPO_XFER_IMPORT_FILE" ("SOURCE_LOC_DB_ID","SOURCE_LOC_ID")
');
END;
/

--changeSet MX-22041.2:358 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_ORIG_LOC" ON "DPO_XFER_IMPORT_FILE" ("ORIG_TARGET_LOC_DB_ID","ORIG_TARGET_LOC_ID")
');
END;
/

--changeSet MX-22041.2:359 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_TARGETLOC" ON "DPO_XFER_IMPORT_FILE" ("TARGET_LOC_DB_ID","TARGET_LOC_ID")
');
END;
/

--changeSet MX-22041.2:360 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIPSHIP_EXPORTFILE" ON "DPO_XFER_EXPORT_FILE" ("SHIPMENT_DB_ID","SHIPMENT_ID")
');
END;
/

--changeSet MX-22041.2:361 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EXPORTFILE_EXPORTINV" ON "DPO_XFER_EXPORT_INV" ("XFER_EXPORT_FILE_ID")
');
END;
/

--changeSet MX-22041.2:362 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IMPORTFILE_IMPORTINV" ON "DPO_XFER_IMPORT_INV" ("XFER_IMPORT_FILE_ID")
');
END;
/

--changeSet MX-22041.2:363 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOREPATH_DPOEXPLOG" ON "DPO_EXPORT_LOG" ("REP_PATH_CD")
');
END;
/

--changeSet MX-22041.2:364 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOREPATH_DPOIMPLOG" ON "DPO_IMPORT_LOG" ("REP_PATH_CD")
');
END;
/

--changeSet MX-22041.2:365 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOIMPLOG_DPOIMPFILE" ON "DPO_IMPORT_FILE" ("IMPORT_LOG_ID")
');
END;
/

--changeSet MX-22041.2:366 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOEXPLOG_DPOEXPFILE" ON "DPO_EXPORT_FILE" ("EXPORT_LOG_ID")
');
END;
/

--changeSet MX-22041.2:367 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOREPRUL_DPOREPTAB" ON "DPO_REP_TABLE" ("RULESET_ID")
');
END;
/

--changeSet MX-22041.2:368 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIFTSHIFT_ORGHRSHIFTPLAN" ON "ORG_HR_SHIFT_PLAN" ("SHIFT_DB_ID","SHIFT_ID")
');
END;
/

--changeSet MX-22041.2:369 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCTASK_PPCLABOUR" ON "PPC_LABOUR" ("PPC_TASK_DB_ID","PPC_TASK_ID")
');
END;
/

--changeSet MX-22041.2:370 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCLABOUR_ROLE" ON "PPC_LABOUR_ROLE" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.2:371 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCLABROLE_HRSLOT" ON "PPC_HR_SLOT" ("LABOUR_ROLE_DB_ID","LABOUR_ROLE_ID")
');
END;
/

--changeSet MX-22041.2:372 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGWORKDEPT_PPCCREW" ON "PPC_CREW" ("DEPT_DB_ID","DEPT_ID")
');
END;
/

--changeSet MX-22041.2:373 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCCREW_PPCTASK" ON "PPC_TASK" ("PPC_CREW_DB_ID","PPC_CREW_ID")
');
END;
/

--changeSet MX-22041.2:374 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIFTTSHIFT_ORGHRSHIFT" ON "ORG_HR_SHIFT" ("SHIFT_DB_ID","SHIFT_ID")
');
END;
/

--changeSet MX-22041.2:375 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCWP_PPCACTIVITY" ON "PPC_ACTIVITY" ("PPC_WP_DB_ID","PPC_WP_ID")
');
END;
/

--changeSet MX-22041.2:376 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCLOC_PPCWP" ON "PPC_WP" ("PPC_LOC_DB_ID","PPC_LOC_ID")
');
END;
/

--changeSet MX-22041.2:377 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_PPCLOC" ON "PPC_LOC" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:378 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCLOC_NHPPCLOC" ON "PPC_LOC" ("NH_PPC_LOC_DB_ID","NH_PPC_LOC_ID")
');
END;
/

--changeSet MX-22041.2:379 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCPLAN_PPCLOC" ON "PPC_LOC" ("PPC_DB_ID","PPC_ID")
');
END;
/

--changeSet MX-22041.2:380 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_NRPHASE_PPCPHASE" ON "PPC_PHASE" ("NR_PHASE_DB_ID","NR_PHASE_ID")
');
END;
/

--changeSet MX-22041.2:381 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_SCHEDKITMAP" ON "SCHED_KIT_MAP" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:382 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVOWNER_SCHEDKITMAP" ON "SCHED_KIT_MAP" ("OWNER_DB_ID","OWNER_ID")
');
END;
/

--changeSet MX-22041.2:383 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINEKITLINE_SHIPMENT" ON "PO_LINE_KIT_LINE" ("RETURN_SHIPMENT_DB_ID","RETURN_SHIPMENT_ID")
');
END;
/

--changeSet MX-22041.2:384 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCPLAN_PPCWP" ON "PPC_WP" ("PPC_DB_ID","PPC_ID")
');
END;
/

--changeSet MX-22041.2:385 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPLAN_LRPINVTASKPLAN" ON "LRP_INV_TASK_PLAN" ("LRP_DB_ID","LRP_ID")
');
END;
/

--changeSet MX-22041.2:386 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPLAN_LRPINVADHOCPLAN" ON "LRP_INV_ADHOC_PLAN" ("LRP_DB_ID","LRP_ID")
');
END;
/

--changeSet MX-22041.2:387 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPLAN_LRPLOCADHOCPLAN" ON "LRP_LOC_ADHOC_PLAN" ("LRP_DB_ID","LRP_ID")
');
END;
/

--changeSet MX-22041.2:388 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_ORGHRSCHEDULE" ON "ORG_HR_SCHEDULE" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:389 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIFTSHIFT_USPDAYSHIFT" ON "USER_SHIFT_PATTERN_DAY_SHIFT" ("SHIFT_DB_ID","SHIFT_ID")
');
END;
/

--changeSet MX-22041.2:390 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_ORGHRSHIFT" ON "ORG_HR_SHIFT" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:391 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_ORGHRSHIFTPLAN" ON "ORG_HR_SHIFT_PLAN" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:392 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGWORKDEPT_ORGHRSHIFT" ON "ORG_HR_SHIFT_PLAN" ("CREW_DB_ID","CREW_ID")
');
END;
/

--changeSet MX-22041.2:393 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPLANNINGTYPE_TASKTASK" ON "TASK_TASK" ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")
');
END;
/

--changeSet MX-22041.2:394 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_ERHEADER" ON "ER_HEADER" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:395 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIFTSHIFT_INVLOC" ON "INV_LOC" ("OVERNIGHT_SHIFT_DB_ID","OVERNIGHT_SHIFT_ID")
');
END;
/

--changeSet MX-22041.2:396 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_PPCPUBLISH" ON "PPC_PUBLISH" ("PUBLISHED_BY_HR_DB_ID","PUBLISHED_BY_HR_ID")
');
END;
/

--changeSet MX-22041.2:397 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REFACCONDSETTING_PPCMILESTO" ON "PPC_MILESTONE_COND" ("AC_COND_DB_ID","AC_COND_CD","COND_SET_DB_ID","COND_SET_CD")
');
END;
/

--changeSet MX-22041.2:398 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPLANNINGTYPE_PPCTASK" ON "PPC_TASK" ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")
');
END;
/

--changeSet MX-22041.2:399 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCTASK_PPCPUBLISHFAILURE" ON "PPC_PUBLISH_FAILURE" ("PPC_TASK_DB_ID","PPC_TASK_ID")
');
END;
/

--changeSet MX-22041.2:400 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCWORKAREA_PPCWORKAREAZONE" ON "PPC_WORK_AREA_ZONE" ("PPC_WORK_AREA_DB_ID","PPC_WORK_AREA_ID")
');
END;
/

--changeSet MX-22041.2:401 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCWORKAREA_PPCTASK" ON "PPC_TASK" ("PPC_WORK_AREA_DB_ID","PPC_WORK_AREA_ID")
');
END;
/

--changeSet MX-22041.2:402 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCPHASE_PPCTASK" ON "PPC_TASK" ("PPC_PHASE_DB_ID","PPC_PHASE_ID")
');
END;
/

--changeSet MX-22041.2:403 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCPHASE_PPCPHASECLASS" ON "PPC_PHASE_CLASS" ("PPC_PHASE_DB_ID","PPC_PHASE_ID")
');
END;
/

--changeSet MX-22041.2:404 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USP_ORGHRSCHEDULE" ON "ORG_HR_SCHEDULE" ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID")
');
END;
/

--changeSet MX-22041.2:405 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_PPCTASK" ON "PPC_TASK" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:406 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_EQPPLANNINGTYPE" ON "EQP_PLANNING_TYPE" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:407 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPTASKZONE_PPCWORKAREAZONE" ON "PPC_WORK_AREA_ZONE" ("ZONE_DB_ID","ZONE_ID")
');
END;
/

--changeSet MX-22041.2:408 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEG_FLLEGNOTE" ON "FL_LEG_NOTE" ("LEG_ID")
');
END;
/

--changeSet MX-22041.2:409 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_FLLEGNOTE" ON "FL_LEG_NOTE" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:410 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARRINVLOC_FLLEG" ON "FL_LEG" ("ARRIVAL_LOC_DB_ID","ARRIVAL_LOC_ID")
');
END;
/

--changeSet MX-22041.2:411 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_FLLEG" ON "FL_LEG" ("PLAN_ASSMBL_DB_ID","PLAN_ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:412 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USGUSREC_FLLEG" ON "FL_LEG" ("USAGE_RECORD_ID")
');
END;
/

--changeSet MX-22041.2:413 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DEPINVLOC_FLLEG" ON "FL_LEG" ("DEPARTURE_LOC_DB_ID","DEPARTURE_LOC_ID")
');
END;
/

--changeSet MX-22041.2:414 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVACREG_FLLEG" ON "FL_LEG" ("AIRCRAFT_DB_ID","AIRCRAFT_ID")
');
END;
/

--changeSet MX-22041.2:415 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGDIS_FLLEGDISTYP" ON "FL_LEG_DISRUPT_TYPE" ("LEG_DISRUPT_ID")
');
END;
/

--changeSet MX-22041.2:416 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEG_FLLEGFAILEFF" ON "FL_LEG_FAIL_EFFECT" ("LEG_ID")
');
END;
/

--changeSet MX-22041.2:417 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEG_FLLEGDISRUPT" ON "FL_LEG_DISRUPT" ("LEG_ID")
');
END;
/

--changeSet MX-22041.2:418 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_FLLEGDIS" ON "FL_LEG_DISRUPT" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:419 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SYSFLLEGNOTE_FLLEGSTALOG" ON "FL_LEG_STATUS_LOG" ("SYSTEM_NOTE_ID")
');
END;
/

--changeSet MX-22041.2:420 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USRFLLEGNOTE_FLLEGSTALOG" ON "FL_LEG_STATUS_LOG" ("USER_NOTE_ID")
');
END;
/

--changeSet MX-22041.2:421 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEG_FLLEGMEAS" ON "FL_LEG_MEASUREMENT" ("LEG_ID")
');
END;
/

--changeSet MX-22041.2:422 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEG_FLLEGSTATLOG" ON "FL_LEG_STATUS_LOG" ("LEG_ID")
');
END;
/

--changeSet MX-22041.2:423 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLEFFECT_FLLEGFLEFF" ON "FL_LEG_FAIL_EFFECT" ("FAIL_EFFECT_DB_ID","FAIL_EFFECT_ID")
');
END;
/

--changeSet MX-22041.2:424 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_FLLEGSTATLOG" ON "FL_LEG_STATUS_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:425 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEG_EVTFAILEFF" ON "EVT_FAIL_EFFECT" ("LEG_ID")
');
END;
/

--changeSet MX-22041.2:426 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARRFLLEG_INVACFLPLAN" ON "INV_AC_FLIGHT_PLAN" ("ARR_LEG_ID")
');
END;
/

--changeSet MX-22041.2:427 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DEPFLLEG_INVACFLPLAN" ON "INV_AC_FLIGHT_PLAN" ("DEP_LEG_ID")
');
END;
/

--changeSet MX-22041.2:428 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_FLLEGMEAS" ON "FL_LEG_MEASUREMENT" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:429 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEG_SDFAULT" ON "SD_FAULT" ("LEG_ID")
');
END;
/

--changeSet MX-22041.2:430 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCTEMPLATE_PPCWP" ON "PPC_WP" ("TEMPLATE_DB_ID","TEMPLATE_ID")
');
END;
/

--changeSet MX-22041.2:431 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCHRSHIFTPLAN_PPCHRSLOT" ON "PPC_HR_SLOT" ("PPC_HR_SHIFT_DB_ID","PPC_HR_SHIFT_ID")
');
END;
/

--changeSet MX-22041.2:432 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCHRSHFTPLN_PPCHRLIC" ON "PPC_HR_LIC" ("PPC_HR_SHIFT_DB_ID","PPC_HR_SHIFT_ID")
');
END;
/

--changeSet MX-22041.2:433 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_USGUSREC" ON "USG_USAGE_RECORD" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:434 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_USGUSREC" ON "USG_USAGE_RECORD" ("RECORD_HR_DB_ID","RECORD_HR_ID")
');
END;
/

--changeSet MX-22041.2:435 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USGUSREC_USGUSDATA" ON "USG_USAGE_DATA" ("USAGE_RECORD_ID")
');
END;
/

--changeSet MX-22041.2:436 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ASSMBLINVINV_USGUSDATA" ON "USG_USAGE_DATA" ("ASSMBL_INV_NO_DB_ID","ASSMBL_INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:437 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_USGUSDATA" ON "USG_USAGE_DATA" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:438 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARRIER_PPCHRLIC" ON "PPC_HR_LIC" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.2:439 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPTYP_EQPPTYPE" ON "LRP_PLAN_TYPE" ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")
');
END;
/

--changeSet MX-22041.2:440 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPLN_LRPPLNTYP" ON "LRP_PLAN_TYPE" ("LRP_DB_ID","LRP_ID")
');
END;
/

--changeSet MX-22041.2:441 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPTDFN_TSKPRNG" ON "LRP_TASK_PLAN_RANGE" ("LRP_DB_ID","LRP_ID","TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:442 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC2_ORGHRSHIFTPLAN" ON "ORG_HR_SHIFT_PLAN" ("CREW_LOC_DB_ID","CREW_LOC_ID")
');
END;
/

--changeSet MX-22041.2:443 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_MAINTPRGMLOG" ON "MAINT_PRGM_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:444 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCPUBLISH_PUBFAILURE" ON "PPC_PUBLISH_FAILURE" ("PPC_WP_DB_ID","PPC_WP_ID")
');
END;
/

--changeSet MX-22041.2:445 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_NRPHASE_PPCWP" ON "PPC_WP" ("NR_PHASE_DB_ID","NR_PHASE_ID")
');
END;
/

--changeSet MX-22041.2:446 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_NRSTRTMLSTN_PPCPHASE" ON "PPC_PHASE" ("NR_START_MILESTONE_DB_ID","NR_START_MILESTONE_ID")
');
END;
/

--changeSet MX-22041.2:447 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_NRENDMLSTN_PPCPHASE" ON "PPC_PHASE" ("NR_END_MILESTONE_DB_ID","NR_END_MILESTONE_ID")
');
END;
/

--changeSet MX-22041.2:448 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_NRSTARTMILESTONE_PPCTASK" ON "PPC_TASK" ("NR_START_MILESTONE_DB_ID","NR_START_MILESTONE_ID")
');
END;
/

--changeSet MX-22041.2:449 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_NRENDMILESTONE_PPCTASK" ON "PPC_TASK" ("NR_END_MILESTONE_DB_ID","NR_END_MILESTONE_ID")
');
END;
/

--changeSet MX-22041.2:450 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_NRPHASE_PPCTASK" ON "PPC_TASK" ("NR_PHASE_DB_ID","NR_PHASE_ID")
');
END;
/

--changeSet MX-22041.2:451 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_PPCTASKDEFN" ON "PPC_TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.2:452 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCWP_TASKDEFN" ON "PPC_TASK_DEFN" ("PPC_WP_DB_ID","PPC_WP_ID")
');
END;
/

--changeSet MX-22041.2:453 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_PPCLOCEX" ON "PPC_LOC_EXCLUDE" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.2:454 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCLOC_PPCCREW" ON "PPC_CREW" ("PPC_LOC_DB_ID","PPC_LOC_ID")
');
END;
/

--changeSet MX-22041.2:455 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCLOC_PPCLOCCAP" ON "PPC_LOC_CAPACITY" ("PPC_LOC_DB_ID","PPC_LOC_ID")
');
END;
/

--changeSet MX-22041.2:456 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CAPPATTERN_PPCLOCCAP" ON "PPC_LOC_CAPACITY" ("CAP_PATTERN_DB_ID","CAP_PATTERN_ID")
');
END;
/

--changeSet MX-22041.2:457 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCCREW_PPCHRSHFTPLN" ON "PPC_HR_SHIFT_PLAN" ("PPC_CREW_DB_ID","PPC_CREW_ID")
');
END;
/

--changeSet MX-22041.2:458 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCLOCCAP_PPCHRSHFTPLN" ON "PPC_HR_SHIFT_PLAN" ("PPC_CAPACITY_DB_ID","PPC_CAPACITY_ID")
');
END;
/

--changeSet MX-22041.2:459 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCHR_PPCHRSHFTPLN" ON "PPC_HR_SHIFT_PLAN" ("PPC_HR_DB_ID","PPC_HR_ID")
');
END;
/

--changeSet MX-22041.2:460 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCLOC_PPCHRSHFTPLN" ON "PPC_HR_SHIFT_PLAN" ("PPC_LOC_DB_ID","PPC_LOC_ID")
');
END;
/

--changeSet MX-22041.2:461 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASMBL_PPCHRLIC" ON "PPC_HR_LIC" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:462 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_PPCHR" ON "PPC_HR" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:463 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCPLAN_PPCHR" ON "PPC_HR" ("PPC_DB_ID","PPC_ID")
');
END;
/

--changeSet MX-22041.2:464 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_TASKTEMPISSUELOG" ON "TASK_TEMP_ISSUE_LOG" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.2:465 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDPART_SCHEDSTASK" ON "SCHED_STASK" ("REPL_SCHED_DB_ID","REPL_SCHED_ID","REPL_SCHED_PART_ID")
');
END;
/

--changeSet MX-22041.2:466 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('MAINT_PRGM_TASK_TASK_FK');
END;
/

--changeSet MX-22041.2:467 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_MAINTPRGMTEMPTASK" ON "MAINT_PRGM_TEMP_TASK" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.2:468 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLTREFWFCYCLESTATUS_LOG" ON "BLT_WF_CYCLE_LOG" ("REF_WF_CYCLE_STATUS_CD")
');
END;
/

--changeSet MX-22041.2:469 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('BLT_WF_ERROR_LOG_NK');
END;
/

--changeSet MX-22041.2:470 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLTREFERROR_BLTWFERRORLOG" ON "BLT_WF_ERROR_LOG" ("REF_ERROR_CD")
');
END;
/

--changeSet MX-22041.2:471 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLTWFRECLOG_BLTWFERRORLOG" ON "BLT_WF_ERROR_LOG" ("WF_REC_LOG_ID","WF_CYCLE_LOG_ID")
');
END;
/

--changeSet MX-22041.2:472 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLTREFWFLOGTYPE_BLTWFRECLOG" ON "BLT_WF_REC_LOG" ("REF_WF_LOG_TYPE_CD","REF_WF_LOG_SUB_TYPE_CD")
');
END;
/

--changeSet MX-22041.2:473 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLTREFWFLOGSTATUS_BLTWFRECL" ON "BLT_WF_REC_LOG" ("REF_WF_LOG_STATUS_CD")
');
END;
/

--changeSet MX-22041.2:474 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLTREFERRORTYPE_BLTREFERROR" ON "BLT_REF_ERROR" ("REF_ERROR_TYPE_CD")
');
END;
/

--changeSet MX-22041.2:475 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BROKORGVENDACC_POHEADER" ON "PO_HEADER" ("BROKER_ACCOUNT_DB_ID","BROKER_ACCOUNT_ID","BROKER_ACCOUNT_CD")
');
END;
/

--changeSet MX-22041.2:476 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_BROKPOHEADER" ON "PO_HEADER" ("BROKER_DB_ID","BROKER_ID")
');
END;
/

--changeSet MX-22041.2:477 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_POLRTRN" ON "PO_LINE_RETURN_MAP" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:478 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_POLRTRN" ON "PO_LINE_RETURN_MAP" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.2:479 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CBORGORG_POHEADER" ON "PO_HEADER" ("CREATED_BY_ORG_DB_ID","CREATED_BY_ORG_ID")
');
END;
/

--changeSet MX-22041.2:480 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_PART_NO_OEM" ON "EQP_PART_NO" ("PART_NO_OEM")
');
END;
/

--changeSet MX-22041.2:481 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_PRT_NO_PRTNSDSC" ON "EQP_PART_NO" ("PART_NO_SDESC")
');
END;
/

--changeSet MX-22041.2:482 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVT_EVENT_EVTSDESC" ON "EVT_EVENT" ("EVENT_SDESC")
');
END;
/

--changeSet MX-22041.2:483 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVT_STAGE_IF1066" ON "EVT_STAGE" ("STAGE_REASON_DB_ID","STAGE_REASON_CD")
');
END;
/

--changeSet MX-22041.2:484 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV_AC_REG_IE1" ON "INV_AC_REG" ("AC_REG_CD")
');
END;
/

--changeSet MX-22041.2:485 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV_INV_SERIAL_OEM" ON "INV_INV" ("SERIAL_NO_OEM")
');
END;
/

--changeSet MX-22041.2:486 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV_INV_INV_NO_SDESC" ON "INV_INV" ("INV_NO_SDESC")
');
END;
/

--changeSet MX-22041.2:487 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV_LOC_REFLOCTYPE_FK" ON "INV_LOC" ("LOC_TYPE_DB_ID","LOC_TYPE_CD")
');
END;
/

--changeSet MX-22041.2:488 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_INV_LOC_LOCCD" ON "INV_LOC" ("LOC_CD")
');
END;
/

--changeSet MX-22041.2:489 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REF_TASK_CLASS_SCHEDSTASK_F" ON "SCHED_STASK" ("TASK_CLASS_DB_ID","TASK_CLASS_CD")
');
END;
/

--changeSet MX-22041.2:490 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_ASSMBL_MIMCALC_FK" ON "MIM_CALC" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:491 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_W0_REF_SDESC" ON "SCHED_STASK" ("WO_REF_SDESC")
');
END;
/

--changeSet MX-22041.2:492 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_UQ_INVBARCODESDESC" ON "INV_INV" ("BARCODE_SDESC")
');
END;
/

--changeSet MX-22041.2:493 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_UQ_BARCODE_SDESC" ON "SCHED_STASK" ("BARCODE_SDESC")
');
END;
/

--changeSet MX-22041.2:494 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIP_SHIPLINE_SERIALNOOEM_F" ON "SHIP_SHIPMENT_LINE" ("SERIAL_NO_OEM")
');
END;
/

--changeSet MX-22041.2:495 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_UQ_USERNAME" ON "UTL_USER" ("USERNAME")
');
END;
/

--changeSet MX-22041.2:496 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVT_EVENT_TYPESTATUS" ON "EVT_EVENT" ("EVENT_TYPE_DB_ID","EVENT_TYPE_CD","EVENT_STATUS_DB_ID","EVENT_STATUS_CD","HIST_BOOL")
');
END;
/

--changeSet MX-22041.2:497 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_UQ_USERID" ON "ORG_HR" ("USER_ID")
');
END;
/

--changeSet MX-22041.2:498 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVT_EVENT_EXTKEYSDESC" ON "EVT_EVENT" ("EXT_KEY_SDESC")
');
END;
/

--changeSet MX-22041.2:499 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_UTLUSER_UTLUSERPASSWORD" ON "UTL_USER_PASSWORD" ("USER_ID")
');
END;
/

--changeSet MX-22041.2:500 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHRSHIFTPLAN_DAYDT" ON "ORG_HR_SHIFT_PLAN" ("DAY_DT")
');
END;
/

--changeSet MX-22041.2:501 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INT_IN_QUEUE_LOG_INTSTEPLOG" ON "INT_STEP_LOG" ("QUEUE_ID")
');
END;
/

--changeSet MX-22041.2:502 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_JOBFAILQ_FAILTYPE" ON "JOB_FAILURE_QUEUE" ("FAIL_TYPE")
');
END;
/

--changeSet MX-22041.2:503 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IDX_INTINBQUELOG_MSGID" ON "INT_INBOUND_QUEUE_LOG" ("MSG_ID")
');
END;
/

--changeSet MX-22041.2:504 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_EVTBANDFIELD_UNIQUEDATATYPE" ON "EVT_BAND_FIELD" ("DATA_TYPE_ID","DATA_TYPE_DB_ID")
');
END;
/

--changeSet MX-22041.2:505 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_UQ_ERHEADER_SDESC" ON "ER_HEADER" ("RULE_DB_ID","DESC_SDESC","EFFECTIVE_FROM_DT","EFFECTIVE_TO_DT")
');
END;
/

--changeSet MX-22041.2:506 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IDX_INTINBQUELOG_STREAMCD" ON "INT_INBOUND_QUEUE_LOG" ("STREAM_CD")
');
END;
/

--changeSet MX-22041.2:507 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_UQ_DPOREPPATH_INDX" ON "DPO_REP_PATH" ("SOURCE_DB_ID","TARGET_DB_ID")
');
END;
/

--changeSet MX-22041.2:508 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_UTL_USER_ORGHRSUPPLY_FK" ON "ORG_HR_SUPPLY" ("USER_ID")
');
END;
/

--changeSet MX-22041.2:509 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_STATUSTYPEHASH_UTLALERT" ON "UTL_ALERT" ("ALERT_STATUS_CD","ALERT_TYPE_ID","PARM_HASH")
');
END;
/

--changeSet MX-22041.2:510 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCHRSHIFT_HRSHIFT_IFK" ON "PPC_HR_SHIFT_PLAN" ("HR_DB_ID","HR_ID","HR_SHIFT_PLAN_ID")
');
END;
/

--changeSet MX-22041.2:511 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCWP_WP_IFK" ON "PPC_WP" ("WP_DB_ID","WP_ID")
');
END;
/

--changeSet MX-22041.2:512 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCWP_ASSMBL_IFK" ON "PPC_WP" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.2:513 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCTASK_SCHED_IFK" ON "PPC_TASK" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.2:514 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FL_LEG_REFFLTYPE_FK" ON "FL_LEG" ("FLIGHT_TYPE_DB_ID","FLIGHT_TYPE_CD")
');
END;
/

--changeSet MX-22041.2:515 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FL_LEG_FLLEGREASON_FK" ON "FL_LEG" ("FLIGHT_REASON_CD")
');
END;
/

--changeSet MX-22041.2:516 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FL_LEG_REFFLSTATUS_FK" ON "FL_LEG" ("FLIGHT_LEG_STATUS_CD")
');
END;
/

--changeSet MX-22041.2:517 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_REF_FLIGHT_LEG_STATUS_NK" ON "REF_FLIGHT_LEG_STATUS" ("DISPLAY_CODE","CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:518 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REF_FLIGHT_REASON_NK" ON "REF_FLIGHT_REASON" ("DISPLAY_CODE","CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:519 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USGUSDATA_MIMDB_FK" ON "USG_USAGE_DATA" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:520 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FL_LEG_MIMDB_FK" ON "FL_LEG" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:521 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FL_LEG_REFINVCAP_FK" ON "FL_LEG" ("INV_CAPABILITY_DB_ID","INV_CAPABILITY_CD")
');
END;
/

--changeSet MX-22041.2:522 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_REF_USAGE_TYPE_NK" ON "REF_USAGE_TYPE" ("DISPLAY_CODE","CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:523 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USGUSDATA_MIMDATATYPE_FK" ON "USG_USAGE_DATA" ("DATA_TYPE_DB_ID","DATA_TYPE_ID")
');
END;
/

--changeSet MX-22041.2:524 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USGUSREC_MIMDB" ON "USG_USAGE_RECORD" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:525 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USGUSREC_REFUSGTYPE_FK" ON "USG_USAGE_RECORD" ("USAGE_TYPE_CD")
');
END;
/

--changeSet MX-22041.2:526 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGSTAT_REFFLLEGSTAT_FK" ON "FL_LEG_STATUS_LOG" ("FLIGHT_LEG_STATUS_CD")
');
END;
/

--changeSet MX-22041.2:527 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGFAILEFF_MIMDB_FK" ON "FL_LEG_FAIL_EFFECT" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:528 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGFAILEFF_REFFLSTG_FK" ON "FL_LEG_FAIL_EFFECT" ("FLIGHT_STAGE_DB_ID","FLIGHT_STAGE_CD")
');
END;
/

--changeSet MX-22041.2:529 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGMEAS_MIMDATTYP_FK" ON "FL_LEG_MEASUREMENT" ("DATA_TYPE_DB_ID","DATA_TYPE_ID")
');
END;
/

--changeSet MX-22041.2:530 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGMEAS_MIMDB_FK" ON "FL_LEG_MEASUREMENT" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:531 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGMEAS_REFDATVAL_FK" ON "FL_LEG_MEASUREMENT" ("DATA_VALUE_DB_ID","DATA_VALUE_CD")
');
END;
/

--changeSet MX-22041.2:532 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGSTAT_MIMDB_FK" ON "FL_LEG_STATUS_LOG" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:533 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FL_LEG_DIS_REFFLSTG_FK" ON "FL_LEG_DISRUPT" ("FLIGHT_STAGE_DB_ID","FLIGHT_STAGE_CD")
');
END;
/

--changeSet MX-22041.2:534 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FL_LEG_DIS_REFDELCD_FK" ON "FL_LEG_DISRUPT" ("DELAY_CODE_DB_ID","DELAY_CODE_CD")
');
END;
/

--changeSet MX-22041.2:535 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGNOTE_MIMDB_FK" ON "FL_LEG_NOTE" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:536 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGFAILEFF_REFFAILETYP_FK" ON "FL_LEG_FAIL_EFFECT" ("FAIL_EFFECT_TYPE_DB_ID","FAIL_EFFECT_TYPE_CD")
');
END;
/

--changeSet MX-22041.2:537 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGDISTYPE_REFDISTYP_FK" ON "FL_LEG_DISRUPT_TYPE" ("DISRUPT_TYPE_DB_ID","DISRUPT_TYPE_CD")
');
END;
/

--changeSet MX-22041.2:538 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGDISTYPE_MIMDB_FK" ON "FL_LEG_DISRUPT_TYPE" ("CTRL_DB_ID")
');
END;
/

--changeSet MX-22041.2:539 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_WFLOGRECIDENTIFIER_INDX" ON "BLT_WF_REC_LOG" ("WF_LOG_REC_IDENTIFIER")
');
END;
/

--changeSet MX-22041.2:540 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_UTL_WORK_ITEM_SCHEDULED_DAT" ON "UTL_WORK_ITEM" ("SCHEDULED_DATE")
');
END;
/

--changeSet MX-22041.2:541 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_UTL_WORK_ITEM_SERVER_ID" ON "UTL_WORK_ITEM" ("SERVER_ID")
');
END;
/

--changeSet MX-22041.2:542 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_BLTWFERRORLOG_WFERRORRECIDE" ON "BLT_WF_ERROR_LOG" ("WF_ERROR_REC_IDENTIFIER")
');
END;
/

--changeSet MX-22041.2:543 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_UTL_WORK_ITEM_KEY" ON "UTL_WORK_ITEM" ("KEY")
');
END;
/

--changeSet MX-22041.2:544 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PARTPRICESP_PARTNOEM" ON "PART_PRICE_SP" ("PART_NO_OEM")
');
END;
/

--changeSet MX-22041.2:545 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_BLT_WF_CYCLE_LOG_NK" ON "BLT_WF_CYCLE_LOG" ("WF_CYCLE_CD")
');
END;
/

--changeSet MX-22041.2:546 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PARTPRICESP_VENDORCD" ON "PART_PRICE_SP" ("VENDOR_CD")
');
END;
/

--changeSet MX-22041.2:547 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PARTPRICESP_MANUFACTCD" ON "PART_PRICE_SP" ("MANUFACT_CD")
');
END;
/

--changeSet MX-22041.2:548 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNC_ACCOUNT_ACCOUNTCD" ON "FNC_ACCOUNT" ("ACCOUNT_CD")
');
END;
/

--changeSet MX-22041.2:549 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INBOUNDQUEUE_UTLALERT_FK" ON "UTL_ALERT" ("QUEUE_ID")
');
END;
/

--changeSet MX-22041.2:550 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_UTL_WORK_ITEM_TYPE_SERVER_D" ON "UTL_WORK_ITEM" ("TYPE","SERVER_ID","SCHEDULED_DATE")
');
END;
/

--changeSet MX-22041.2:551 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_EQPBOMPRT_ASSCDBOMNPRT" ON "EQP_BOM_PART" ("ASSMBL_CD","BOM_PART_CD")
');
END;
/

--changeSet MX-22041.2:552 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_EQPASS_ASSCDBOMCD" ON "EQP_ASSMBL_BOM" ("ASSMBL_CD","ASSMBL_BOM_CD")
');
END;
/

--changeSet MX-22041.2:553 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_EQPPRNO_MANCDPARTNO" ON "EQP_PART_NO" ("MANUFACT_CD","PART_NO_OEM")
');
END;
/

--changeSet MX-22041.2:554 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_SBCOLUMN_UNIQUE_FIELD" ON "SB_COLUMN" ("STATUS_BOARD_DB_ID","STATUS_BOARD_ID","COLUMN_CD")
');
END;
/

--changeSet MX-22041.2:555 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_MAINTPRGM_MPCARMAP" ON "MAINT_PRGM_CARRIER_MAP" ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID")
');
END;
/

--changeSet MX-22041.2:556 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_EQPASSMBLSUBTYPE_CD" ON "EQP_ASSMBL_SUBTYPE" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_SUBTYPE_DB_ID","ASSMBL_SUBTYPE_ID")
');
END;
/

--changeSet MX-22041.2:557 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_PARM_DATA" ON "SCHED_LABOUR_PARM_DATA" ("EVENT_DB_ID","EVENT_ID","EVENT_INV_ID","DATA_TYPE_DB_ID","DATA_TYPE_ID","INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:558 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_INST_PART" ON "SCHED_LABOUR_INST_PART" ("SCHED_DB_ID","SCHED_ID","SCHED_PART_ID","SCHED_INST_PART_ID")
');
END;
/

--changeSet MX-22041.2:559 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_RMVD_PART" ON "SCHED_LABOUR_RMVD_PART" ("SCHED_DB_ID","SCHED_ID","SCHED_PART_ID","SCHED_RMVD_PART_ID")
');
END;
/

--changeSet MX-22041.2:560 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create UNIQUE Index "IX_SCHED_ACTION" ON "SCHED_LABOUR_ACTION" ("SCHED_DB_ID","SCHED_ID","ACTION_ID")
');
END;
/

--changeSet MX-22041.2:561 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INV_INV_EVENTINV_IE');
END;
/

--changeSet MX-22041.2:562 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV_INV_EVENTINV_IE" ON "EVT_INV" ("EVENT_DB_ID","EVENT_ID","INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.2:563 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAIN_EVT_EVTINV" ON "EVT_INV" ("EVENT_DB_ID","EVENT_ID","MAIN_INV_BOOL")
');
END;
/

--changeSet MX-22041.2:564 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EVTPARTNO" ON "EVT_PART_NO" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.2:565 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTTOOL" ON "EVT_TOOL" ("EVENT_DB_ID","EVENT_ID")
');
END;
/