--liquibase formatted sql


--changeSet MX-22041.3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_EQPASSMBLBOM" ON "EQP_ASSMBL_BOM" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_EQPDATASOURCE" ON "EQP_DATA_SOURCE" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_INVLOCCAPABILITY" ON "INV_LOC_CAPABILITY" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLBOM_EQPASSMBLPOS" ON "EQP_ASSMBL_POS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID")
');
END;
/

--changeSet MX-22041.3:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_EQPPARTBASELINE" ON "EQP_PART_BASELINE" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.3:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_TASKBOMPARTLIST" ON "TASK_BOM_PART_LIST" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.3:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPDATASOURCE_EQPDATASOURCE" ON "EQP_DATA_SOURCE_SPEC" ("ASSMBL_DB_ID","ASSMBL_CD","DATA_SOURCE_DB_ID","DATA_SOURCE_CD")
');
END;
/

--changeSet MX-22041.3:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPRTBSLINE_EQPPARTCOMPATDE" ON "EQP_PART_COMPAT_DEF" ("BOM_PART_DB_ID","BOM_PART_ID","PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('EQP_PART_COMPAT_DEF_NHDEF_FK');
END;
/

--changeSet MX-22041.3:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTBASELINE_EQPPARTCOMPA" ON "EQP_PART_COMPAT_DEF" ("NH_BOM_PART_DB_ID","NH_BOM_PART_ID","NH_PART_NO_DB_ID","NH_PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EVTPARTNO" ON "EVT_PART_NO" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EQPPARTBASELINE" ON "EQP_PART_BASELINE" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_TASKPARTTRANSFORM" ON "TASK_PART_TRANSFORM" ("OLD_PART_NO_DB_ID","OLD_PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_TASKINTERVAL" ON "TASK_INTERVAL" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTDEPT" ON "EVT_DEPT" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTIETM" ON "EVT_IETM" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTFAILEFFECT" ON "EVT_FAIL_EFFECT" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTEVENTREL" ON "EVT_EVENT_REL" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTPARTNO" ON "EVT_PART_NO" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTLOC" ON "EVT_LOC" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTINV" ON "EVT_INV" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTTOOL" ON "EVT_TOOL" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTSTAGE2" ON "EVT_STAGE" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTINV_EVTINVUSAGE" ON "EVT_INV_USAGE" ("EVENT_DB_ID","EVENT_ID","EVENT_INV_ID")
');
END;
/

--changeSet MX-22041.3:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMIETM_IETMTOPIC" ON "IETM_TOPIC" ("IETM_DB_ID","IETM_ID")
');
END;
/

--changeSet MX-22041.3:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILMODE_FAILMODEFACTOR" ON "FAIL_MODE_FACTOR" ("FAIL_MODE_DB_ID","FAIL_MODE_ID")
');
END;
/

--changeSet MX-22041.3:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILMODE_FAILMODEIETM" ON "FAIL_MODE_IETM" ("FAIL_MODE_DB_ID","FAIL_MODE_ID")
');
END;
/

--changeSet MX-22041.3:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILMODE_FAILMODESUPPRESS" ON "FAIL_MODE_SUPPRESS" ("SUPPRESS_FAIL_MODE_DB_ID","SUPPRESS_FAIL_MODE_ID")
');
END;
/

--changeSet MX-22041.3:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILMODE_FAILMODESUPPRESS2" ON "FAIL_MODE_SUPPRESS" ("FAIL_MODE_DB_ID","FAIL_MODE_ID")
');
END;
/

--changeSet MX-22041.3:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVCURRUSAGE" ON "INV_CURR_USAGE" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVLOCDEPT" ON "INV_LOC_DEPT" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVLOCCAPABILITY" ON "INV_LOC_CAPABILITY" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRLICENSE" ON "ORG_HR_LICENSE" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRAUTHORITY" ON "ORG_HR_AUTHORITY" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRQUAL" ON "ORG_HR_QUAL" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGDEPTHR" ON "ORG_DEPT_HR" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGWORKDEPT_ORGDEPTHR" ON "ORG_DEPT_HR" ("DEPT_DB_ID","DEPT_ID")
');
END;
/

--changeSet MX-22041.3:38 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INV_LOC_ORGDEPT_FK');
END;
/

--changeSet MX-22041.3:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGWORKDEPT_INVLOCDEPT" ON "INV_LOC_DEPT" ("DEPT_DB_ID","DEPT_ID")
');
END;
/

--changeSet MX-22041.3:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGAUTHORITY_ORGAUTHFAILFAC" ON "ORG_AUTH_FAIL_FACTOR" ("AUTHORITY_DB_ID","AUTHORITY_ID")
');
END;
/

--changeSet MX-22041.3:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGAUTHORITY_ORGHRAUTHORITY" ON "ORG_HR_AUTHORITY" ("AUTHORITY_DB_ID","AUTHORITY_ID")
');
END;
/

--changeSet MX-22041.3:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SDFAULT_SDFAULTNATURE" ON "SD_FAULT_NATURE" ("FAULT_DB_ID","FAULT_ID")
');
END;
/

--changeSet MX-22041.3:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SDFAULT_SDFAULTPRECPROC" ON "SD_FAULT_PREC_PROC" ("FAULT_DB_ID","FAULT_ID")
');
END;
/

--changeSet MX-22041.3:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDPART" ON "SCHED_PART" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKLABOURLIST" ON "TASK_LABOUR_LIST" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKBOMPARTLIST" ON "TASK_BOM_PART_LIST" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKSCHEDRULE_TASKINTERVAL" ON "TASK_INTERVAL" ("TASK_DB_ID","TASK_ID","DATA_TYPE_DB_ID","DATA_TYPE_ID")
');
END;
/

--changeSet MX-22041.3:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKTASKIETM" ON "TASK_TASK_IETM" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKTASKDEP2" ON "TASK_TASK_DEP" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKSCHEDRULE" ON "TASK_SCHED_RULE" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKPARTTRANSFORM" ON "TASK_PART_TRANSFORM" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKPARTLIST" ON "TASK_PART_LIST" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKTOOLLIST" ON "TASK_TOOL_LIST" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTINV_INVPARMDATA" ON "INV_PARM_DATA" ("EVENT_DB_ID","EVENT_ID","EVENT_INV_ID")
');
END;
/

--changeSet MX-22041.3:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_STOCK_NO_INVLOCSTOCK" ON "INV_LOC_STOCK" ("STOCK_NO_DB_ID","STOCK_NO_ID")
');
END;
/

--changeSet MX-22041.3:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV_LOC_INVLOCSTOCK" ON "INV_LOC_STOCK" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTATTACH_EVTEVNT" ON "EVT_ATTACH" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_PART_NO_EQPPRTATTACH" ON "EQP_PART_ATTACHMENT" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_PART_NO_EQPPARTIETM" ON "EQP_PART_IETM" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_PART_NO_EQPPARTALTUNIT" ON "EQP_PART_ALT_UNIT" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV_LOC_INVLOCBIN" ON "INV_LOC_BIN" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVLOCLABRCAPACITY" ON "INV_LOC_LABOUR_CAPACITY" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVNT_EVTSHEDDEAD" ON "EVT_SCHED_DEAD" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WO_SCHDSTSK_SCHEDWOLINE" ON "SCHED_WO_LINE" ("WO_SCHED_DB_ID","WO_SCHED_ID")
');
END;
/

--changeSet MX-22041.3:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDR_ORGVNDRATTACH" ON "ORG_VENDOR_ATTACH" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVNDR_ORFVENDIETM" ON "ORG_VENDOR_IETM" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_EQPRTVENDOR" ON "EQP_PART_VENDOR_REP" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPTNO_EQPPTVENDOR" ON "EQP_PART_VENDOR_REP" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDPRT_SCDINSTPRT" ON "SCHED_INST_PART" ("SCHED_DB_ID","SCHED_ID","SCHED_PART_ID")
');
END;
/

--changeSet MX-22041.3:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDPRT_SCHDRMVDPRT" ON "SCHED_RMVD_PART" ("SCHED_DB_ID","SCHED_ID","SCHED_PART_ID")
');
END;
/

--changeSet MX-22041.3:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVLOCCONTACT" ON "INV_LOC_CONTACT" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVLCREPAIR" ON "INV_LOC_REPAIR" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTNO_INVLCREPAIR" ON "INV_LOC_REPAIR" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVNDR_EQPPRTVENDOR" ON "EQP_PART_VENDOR" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDR_ORGVENDRACCOUNT" ON "ORG_VENDOR_ACCOUNT" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTNO_EQPPRTVENDR" ON "EQP_PART_VENDOR" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:77 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POINVOICE_POINVOICELINE" ON "PO_INVOICE_LINE" ("PO_INVOICE_DB_ID","PO_INVOICE_ID")
');
END;
/

--changeSet MX-22041.3:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POINVCLIN_POINVLINCHG" ON "PO_INVOICE_LINE_CHARGE" ("PO_INVOICE_DB_ID","PO_INVOICE_ID","PO_INVOICE_LINE_ID")
');
END;
/

--changeSet MX-22041.3:79 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_POLINETAX" ON "PO_LINE_TAX" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.3:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_POLINECHARGE" ON "PO_LINE_CHARGE" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.3:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FNCXACTNLG_FNCXACTNACNT" ON "FNC_XACTION_ACCOUNT" ("XACTION_DB_ID","XACTION_ID")
');
END;
/

--changeSet MX-22041.3:82 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POHEADER_POLINE" ON "PO_LINE" ("PO_DB_ID","PO_ID")
');
END;
/

--changeSet MX-22041.3:83 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POHEADER_POAUTH" ON "PO_AUTH" ("PO_DB_ID","PO_ID")
');
END;
/

--changeSet MX-22041.3:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDPANEL" ON "SCHED_PANEL" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:85 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TSKTASK_TASKPANEL" ON "TASK_PANEL" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:86 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKZONE" ON "TASK_ZONE" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:87 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDZONE" ON "SCHED_ZONE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:88 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTINV_INVNUMDATA" ON "INV_NUM_DATA" ("EVENT_DB_ID","EVENT_ID","EVENT_INV_ID")
');
END;
/

--changeSet MX-22041.3:89 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTINV_INVCHRDATA" ON "INV_CHR_DATA" ("EVENT_DB_ID","EVENT_ID","EVENT_INV_ID")
');
END;
/

--changeSet MX-22041.3:90 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POINVOICELINE_POINVLINETAX" ON "PO_INVOICE_LINE_TAX" ("PO_INVOICE_DB_ID","PO_INVOICE_ID","PO_INVOICE_LINE_ID")
');
END;
/

--changeSet MX-22041.3:91 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPRTBASLIN_EQPRTCMPATTSK" ON "EQP_PART_COMPAT_TASK" ("BOM_PART_DB_ID","BOM_PART_ID","PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:92 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVLOCPARTCT" ON "INV_LOC_PART_COUNT" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:93 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_INVLOCPARTCT" ON "INV_LOC_PART_COUNT" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:94 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_PART_NO_INVLOCBIN" ON "INV_LOC_BIN" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:95 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRPOAUTHLVL" ON "ORG_HR_PO_AUTH_LVL" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOCPARTCT_INVLOCINVRECT" ON "INV_LOC_INV_RECOUNT" ("LOC_DB_ID","LOC_ID","PART_NO_DB_ID","PART_NO_ID","PART_COUNT_ID")
');
END;
/

--changeSet MX-22041.3:97 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POINVOICELINE_POINVLINEMAP" ON "PO_INVOICE_LINE_MAP" ("PO_INVOICE_DB_ID","PO_INVOICE_ID","PO_INVOICE_LINE_ID")
');
END;
/

--changeSet MX-22041.3:98 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_POINVOICELINEMAP" ON "PO_INVOICE_LINE_MAP" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.3:99 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVLOCPRINTER" ON "INV_LOC_PRINTER" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:100 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOCPRNTR_INVLOCPRNTRJOB" ON "INV_LOC_PRINTER_JOB" ("LOC_DB_ID","LOC_ID","LOC_PRINTER_ID")
');
END;
/

--changeSet MX-22041.3:101 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_EVTVENDOR" ON "EVT_VENDOR" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:102 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTVENDOR" ON "EVT_VENDOR" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:103 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_RFQHEADER_RFQLINE" ON "RFQ_LINE" ("RFQ_DB_ID","RFQ_ID")
');
END;
/

--changeSet MX-22041.3:104 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FCMODEL_FCRANGE" ON "FC_RANGE" ("MODEL_DB_ID","MODEL_ID")
');
END;
/

--changeSet MX-22041.3:105 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FCRANGE_FCRATE" ON "FC_RATE" ("MODEL_DB_ID","MODEL_ID","RANGE_ID")
');
END;
/

--changeSet MX-22041.3:106 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRSHIFTPLAN" ON "ORG_HR_SHIFT_PLAN" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:107 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRSHIFT" ON "ORG_HR_SHIFT" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRSCHED" ON "ORG_HR_SCHEDULE" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:109 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVIETM" ON "INV_IETM" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:110 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVATTACH" ON "INV_ATTACH" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:111 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRTIMEOFF" ON "ORG_HR_TIMEOFF" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:112 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDSTEP" ON "SCHED_STEP" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:113 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LPALOG_LPASTASK" ON "LPA_STASK" ("LPA_DB_ID")
');
END;
/

--changeSet MX-22041.3:114 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_LPASTASK" ON "LPA_STASK" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:115 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LPASTASK_LPASTASKMAINTOP" ON "LPA_STASK_MAINT_OP" ("LPA_DB_ID","SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:116 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LPASTASKMAINTOP_LPAMAINOPCO" ON "LPA_MAINT_OP_CONFLICT" ("LPA_DB_ID","SCHED_DB_ID","SCHED_ID","STASK_MAINT_OP_ID")
');
END;
/

--changeSet MX-22041.3:117 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_PARTVENDORXCHG" ON "EQP_PART_VENDOR_EXCHG" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:118 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_PARTVENDORXCHG" ON "EQP_PART_VENDOR_EXCHG" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:119 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PARTVENDORXCHG_PARTVENDORXC" ON "EQP_PART_VENDOR_EXCHG_LOC" ("VENDOR_DB_ID","VENDOR_ID","PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:120 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_PARTVENDORXCHGLOC" ON "EQP_PART_VENDOR_EXCHG_LOC" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:121 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKSTEP" ON "TASK_STEP" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:122 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRCERT" ON "ORG_HR_CERT" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:123 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDACTION" ON "SCHED_ACTION" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:124 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_RFQHEADER_RFQVENDOR" ON "RFQ_VENDOR" ("RFQ_DB_ID","RFQ_ID")
');
END;
/

--changeSet MX-22041.3:125 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_RFQLINE_RFQLINEVENDOR" ON "RFQ_LINE_VENDOR" ("RFQ_DB_ID","RFQ_ID","RFQ_LINE_ID")
');
END;
/

--changeSet MX-22041.3:126 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_RFQVENDOR" ON "RFQ_VENDOR" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:127 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_RFQLINEVENDOR" ON "RFQ_LINE_VENDOR" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:128 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_RFQLINEVNDR_RFQLINEVNDRCHRG" ON "RFQ_LINE_VENDOR_CHARGE" ("RFQ_DB_ID","RFQ_ID","RFQ_LINE_ID","VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:129 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_RFQLINEVNDR_RFQLINEVNDRTAX" ON "RFQ_LINE_VENDOR_TAX" ("RFQ_DB_ID","RFQ_ID","RFQ_LINE_ID","VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:130 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKBLKREQMAP" ON "TASK_BLOCK_REQ_MAP" ("BLOCK_TASK_DB_ID","BLOCK_TASK_ID")
');
END;
/

--changeSet MX-22041.3:131 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKJICREQMAP" ON "TASK_JIC_REQ_MAP" ("JIC_TASK_DB_ID","JIC_TASK_ID")
');
END;
/

--changeSet MX-22041.3:132 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_TASKBLKREQMAP" ON "TASK_BLOCK_REQ_MAP" ("REQ_TASK_DEFN_DB_ID","REQ_TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:133 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_TASKJICREQMAP" ON "TASK_JIC_REQ_MAP" ("REQ_TASK_DEFN_DB_ID","REQ_TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:134 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_ASSMBL" ON "SB_ASSMBL" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:135 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SBSB_SBQUERY" ON "SB_QUERY" ("STATUS_BOARD_DB_ID","STATUS_BOARD_ID")
');
END;
/

--changeSet MX-22041.3:136 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SBSB_SBCOLUMNGROUP" ON "SB_COLUMN_GROUP" ("STATUS_BOARD_DB_ID","STATUS_BOARD_ID")
');
END;
/

--changeSet MX-22041.3:137 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SBSB_SBASSMBL" ON "SB_ASSMBL" ("STATUS_BOARD_DB_ID","STATUS_BOARD_ID")
');
END;
/

--changeSet MX-22041.3:138 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_TASKLABOURSUMMARY" ON "DWT_TASK_LABOUR_SUMMARY" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:139 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKLABOURSUMMARY" ON "DWT_TASK_LABOUR_SUMMARY" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:140 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_TASKLABOURSUMMAR" ON "DWT_TASK_LABOUR_SUMMARY" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:141 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_ORGVENS2KCMD" ON "ORG_VENDOR_SPEC2K_CMND" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:142 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAINTPRGM_MAINTPRGMTASK" ON "MAINT_PRGM_TASK" ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID")
');
END;
/

--changeSet MX-22041.3:143 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_MAINTPRGMTASK" ON "MAINT_PRGM_TASK" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:144 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPLAN_LRPINVINV" ON "LRP_INV_INV" ("LRP_DB_ID","LRP_ID")
');
END;
/

--changeSet MX-22041.3:145 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_LRPLOC" ON "LRP_LOC" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:146 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPLAN_LRPLOC" ON "LRP_LOC" ("LRP_DB_ID","LRP_ID")
');
END;
/

--changeSet MX-22041.3:147 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPLAN_LRPTASKDEFN" ON "LRP_TASK_DEFN" ("LRP_DB_ID","LRP_ID")
');
END;
/

--changeSet MX-22041.3:148 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_LRPTASKDEFN" ON "LRP_TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:149 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVACREG_TASKACRULE" ON "TASK_AC_RULE" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:150 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKSCHEDRULE_TASKACRULE" ON "TASK_AC_RULE" ("TASK_DB_ID","TASK_ID","DATA_TYPE_DB_ID","DATA_TYPE_ID")
');
END;
/

--changeSet MX-22041.3:151 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVEND_ORGVENDAIR" ON "ORG_VENDOR_AIRPORT" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:152 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_ORGVENDAIR" ON "ORG_VENDOR_AIRPORT" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:153 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTLICDEFN" ON "EVT_LIC_DEFN" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:154 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TAGTAG_TAGTASKDEFN" ON "TAG_TASK_DEFN" ("TAG_DB_ID","TAG_ID")
');
END;
/

--changeSet MX-22041.3:155 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_SCHEDWOMPC" ON "SCHED_WO_MPC" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:156 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDWOMPC" ON "SCHED_WO_MPC" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:157 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_EVTORGHR" ON "EVT_ORG_HR" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:158 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRATTACHMENT" ON "ORG_HR_ATTACHMENT" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:159 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKDEADEXT" ON "TASK_DEADLINE_EXT" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:160 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTORGHR" ON "EVT_ORG_HR" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:161 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_TASKDEADEXT" ON "TASK_DEADLINE_EXT" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:162 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKLIC" ON "TASK_LIC" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:163 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRLIC" ON "ORG_HR_LIC" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:164 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LICDEFN_EVTLICDEFN" ON "EVT_LIC_DEFN" ("LIC_DB_ID","LIC_ID")
');
END;
/

--changeSet MX-22041.3:165 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARRIER_MNTPGMCAR" ON "MAINT_PRGM_CARRIER_MAP" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.3:166 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_GRPDEFN_GRPDEFNLIC" ON "GRP_DEFN_LIC" ("GRP_DEFN_DB_ID","GRP_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:167 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LICDEFN_LICDEFNPREREQ" ON "LIC_DEFN_PREREQ" ("LIC_DB_ID","LIC_ID")
');
END;
/

--changeSet MX-22041.3:168 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTBAND_EVTBANDFIELD" ON "EVT_BAND_FIELD" ("BAND_GROUP_DB_ID","BAND_GROUP_ID","BAND_ID")
');
END;
/

--changeSet MX-22041.3:169 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTBANDGROUP_EVTBAND" ON "EVT_BAND" ("BAND_GROUP_DB_ID","BAND_GROUP_ID")
');
END;
/

--changeSet MX-22041.3:170 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTFINDING" ON "EVT_FINDING" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:171 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTBAND_EVTBANDROLE" ON "EVT_BAND_ROLE" ("BAND_GROUP_DB_ID","BAND_GROUP_ID","BAND_ID")
');
END;
/

--changeSet MX-22041.3:172 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYDEFN_WARRANTYDEFNTA" ON "WARRANTY_DEFN_TASK" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:173 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_WARRANTYDEFNPARTL" ON "WARRANTY_DEFN_PART_LIST" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:174 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_WARRANTYDEFNTASK" ON "WARRANTY_DEFN_TASK" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:175 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYDEFN_WARRANTYDEFNPA" ON "WARRANTY_DEFN_PART_LIST" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:176 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_WARRANTYDEFNVENDO" ON "WARRANTY_DEFN_VENDOR_LIST" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:177 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYDEFN_WARRANTYDEFNVE" ON "WARRANTY_DEFN_VENDOR_LIST" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:178 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_WARRANTYLOCLIST" ON "WARRANTY_DEFN_LOC_LIST" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:179 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYDEFN_WARRANTYDEFNAS" ON "WARRANTY_DEFN_ASSEMBLY" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:180 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_WARRANTYDEFNASSMB" ON "WARRANTY_DEFN_ASSEMBLY" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:181 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REFADDRESS_ORGADDRESSLIST" ON "ORG_ADDRESS_LIST" ("ADDRESS_DB_ID","ADDRESS_ID")
');
END;
/

--changeSet MX-22041.3:182 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_REFCONTACT_ORGCONTACTLIST" ON "ORG_CONTACT_LIST" ("CONTACT_DB_ID","CONTACT_ID")
');
END;
/

--changeSet MX-22041.3:183 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_WARRANTYINIT_INV" ON "WARRANTY_INIT_INV" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:184 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYINIT_WRNTINITTASK" ON "WARRANTY_INIT_TASK" ("WARRANTY_INIT_DB_ID","WARRANTY_INIT_ID")
');
END;
/

--changeSet MX-22041.3:185 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRNTYINIT_WRNTYINITINV" ON "WARRANTY_INIT_INV" ("WARRANTY_INIT_DB_ID","WARRANTY_INIT_ID")
');
END;
/

--changeSet MX-22041.3:186 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTY_TERMSCONFIGSLOTS" ON "WARRANTY_TERMS_CONFIG_SLOTS" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:187 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANT_TERMSREPPARTS" ON "WARRANTY_TERMS_REP_PARTS" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:188 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTY_WRRNTTERMSNONREPPA" ON "WARRANTY_TERMS_NON_REP_PARTS" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:189 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTY_WARRANTYLOCLIST" ON "WARRANTY_DEFN_LOC_LIST" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:190 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLBOM_WARRANTYTERMSC" ON "WARRANTY_TERMS_CONFIG_SLOTS" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID")
');
END;
/

--changeSet MX-22041.3:191 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_WARRANTYTERMSREPP" ON "WARRANTY_TERMS_REP_PARTS" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:192 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_WARRANTYTERMSNONR" ON "WARRANTY_TERMS_NON_REP_PARTS" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:193 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLOBDATA_WARRANTYATTACH" ON "WARRANTY_ATTACH" ("BLOB_DB_ID","BLOB_ID")
');
END;
/

--changeSet MX-22041.3:194 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYDEFN_WARRANTYIETM" ON "WARRANTY_IETM" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:195 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYDEFN_WARRANTYATTACH" ON "WARRANTY_ATTACH" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:196 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKCOND" ON "TASK_COND" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:197 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_DEFNNREST" ON "DEFN_NR_EST" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:198 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_INVLOCORG" ON "INV_LOC_ORG" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.3:199 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYDEFN_POLINEWARRANTY" ON "PO_LINE_WARRANTY" ("WARRANTY_DEFN_DB_ID","WARRANTY_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:200 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_INVLOCORG" ON "INV_LOC_ORG" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:201 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVINVOEMASSMB_ITEM" ON "INV_INV_OEM_ASSMBL" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:202 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_WARRANTYINITTASK" ON "WARRANTY_INIT_TASK" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:203 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_POLINE_POLINEWARRANTY" ON "PO_LINE_WARRANTY" ("PO_DB_ID","PO_ID","PO_LINE_ID")
');
END;
/

--changeSet MX-22041.3:204 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INV INV_INVINVOEMASSMB_ASSM" ON "INV_INV_OEM_ASSMBL" ("OEM_ASSMBL_INV_NO_DB_ID","OEM_ASSMBL_INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:205 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIPSEG_SHIPSEGMAP" ON "SHIP_SEGMENT_MAP" ("SEGMENT_DB_ID","SEGMENT_ID")
');
END;
/

--changeSet MX-22041.3:206 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIPSHIPMENT_SHIPSEGMAP" ON "SHIP_SEGMENT_MAP" ("SHIPMENT_DB_ID","SHIPMENT_ID")
');
END;
/

--changeSet MX-22041.3:207 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYEVALTASK_WRNTEVALPA" ON "WARRANTY_EVAL_PART" ("WARRANTY_EVAL_DB_ID","WARRANTY_EVAL_ID","WARRANTY_EVAL_TASK_ID")
');
END;
/

--changeSet MX-22041.3:208 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WAREVALTSK_WAREVALAB" ON "WARRANTY_EVAL_LABOUR" ("WARRANTY_EVAL_DB_ID","WARRANTY_EVAL_ID","WARRANTY_EVAL_TASK_ID")
');
END;
/

--changeSet MX-22041.3:209 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WARRANTYEVAL_TASKS" ON "WARRANTY_EVAL_TASK" ("WARRANTY_EVAL_DB_ID","WARRANTY_EVAL_ID")
');
END;
/

--changeSet MX-22041.3:210 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILERFERREF_FAILDEFREFRL" ON "FAIL_DEFER_REF_ROLE" ("FAIL_DEFER_REF_DB_ID","FAIL_DEFER_REF_ID")
');
END;
/

--changeSet MX-22041.3:211 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGORGHR" ON "ORG_ORG_HR" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:212 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_ORGORGLOC" ON "ORG_ORG_LOC" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:213 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_ORGORGHR" ON "ORG_ORG_HR" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.3:214 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_ORGORGLOC" ON "ORG_ORG_LOC" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.3:215 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_ORGADDRESSLIST" ON "ORG_ADDRESS_LIST" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.3:216 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_ORGCONTACT" ON "ORG_CONTACT_LIST" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.3:217 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPLOC_LRPLOCCAP" ON "LRP_LOC_CAPABILITY" ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:218 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFSTEP_LEVELS" ON "WF_STEP_LEVELS" ("WF_STEP_DB_ID","WF_STEP_ID")
');
END;
/

--changeSet MX-22041.3:219 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFSTEP_WF" ON "WF_STEPS" ("WF_STEP_DB_ID","WF_STEP_ID")
');
END;
/

--changeSet MX-22041.3:220 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFWF_WFSTEPS" ON "WF_STEPS" ("WF_DB_ID","WF_ID")
');
END;
/

--changeSet MX-22041.3:221 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFLEVEL_STEPS" ON "WF_STEP_LEVELS" ("WF_LEVEL_DB_ID","WF_LEVEL_ID")
');
END;
/

--changeSet MX-22041.3:222 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFLEVELDEFN_STEPS" ON "WF_DEFN_STEP_LEVELS" ("WF_LEVEL_DEFN_DB_ID","WF_LEVEL_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:223 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFDEFNSTEP_NEXTFLOW" ON "WF_DEFN_FLOW" ("WF_DEFN_STEP_DB_ID","WF_DEFN_STEP_ID")
');
END;
/

--changeSet MX-22041.3:224 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFDEFNSTEP_FLOW" ON "WF_DEFN_FLOW" ("NEXT_WF_DEFN_STEP_DB_ID","NEXT_WF_DEFN_STEP_ID")
');
END;
/

--changeSet MX-22041.3:225 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFDEFNSTEP_CHILDREN" ON "WF_DEFN_STEP_GROUP" ("CHILD_WF_DEFN_STEP_DB_ID","CHILD_WF_DEFN_STEP_ID")
');
END;
/

--changeSet MX-22041.3:226 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFDEFNSTEP_WFDEFN" ON "WF_DEFN_STEPS" ("WF_DEFN_STEP_DB_ID","WF_DEFN_STEP_ID")
');
END;
/

--changeSet MX-22041.3:227 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFDEFN_STEPS" ON "WF_DEFN_STEPS" ("WF_DEFN_DB_ID","WF_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:228 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFDEFNSTEP_GROUPS" ON "WF_DEFN_STEP_GROUP" ("WF_DEFN_STEP_DB_ID","WF_DEFN_STEP_ID")
');
END;
/

--changeSet MX-22041.3:229 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_STEP_GROUPCHILDREN" ON "WF_STEP_GROUP" ("CHILD_WF_STEP_DB_ID","CHILD_WF_STEP_ID")
');
END;
/

--changeSet MX-22041.3:230 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_STEP_NEXTSTEP" ON "WF_STEP_FLOW" ("NEXT_WF_STEP_DB_ID","NEXT_WF_STEP_ID")
');
END;
/

--changeSet MX-22041.3:231 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFSTEP_GROUPPARENT" ON "WF_STEP_GROUP" ("WF_STEP_DB_ID","WF_STEP_ID")
');
END;
/

--changeSet MX-22041.3:232 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_STEP_FLOW" ON "WF_STEP_FLOW" ("WF_STEP_DB_ID","WF_STEP_ID")
');
END;
/

--changeSet MX-22041.3:233 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_ZIPTASK" ON "ZIP_TASK" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:234 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ZIPQUEUE_ZIPTASK" ON "ZIP_TASK" ("ZIP_ID")
');
END;
/

--changeSet MX-22041.3:235 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKPARTMAP" ON "TASK_PART_MAP" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:236 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_TASKPARTMAP" ON "TASK_PART_MAP" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:237 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMIETM_IETMASSMBL" ON "IETM_ASSMBL" ("IETM_DB_ID","IETM_ID")
');
END;
/

--changeSet MX-22041.3:238 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_IETMASSMBL" ON "IETM_ASSMBL" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:239 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ERHEADER_LRPEXRULE" ON "LRP_EXTRACTION_RULE" ("RULE_DB_ID","RULE_ID")
');
END;
/

--changeSet MX-22041.3:240 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPLAN_LRPEXRULE" ON "LRP_EXTRACTION_RULE" ("LRP_DB_ID","LRP_ID")
');
END;
/

--changeSet MX-22041.3:241 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ERHEADER_ERDATERANGE" ON "ER_DATE_RANGE" ("RULE_DB_ID","RULE_ID")
');
END;
/

--changeSet MX-22041.3:242 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPBOMPART_EQPBOMPARTLOG" ON "EQP_BOM_PART_LOG" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-22041.3:243 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EQPARTNOLOG" ON "EQP_PART_NO_LOG" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:244 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBLBOM_EQPASSMBLBMLG" ON "EQP_ASSMBL_BOM_LOG" ("ASSMBL_DB_ID","ASSMBL_CD","ASSMBL_BOM_ID")
');
END;
/

--changeSet MX-22041.3:245 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDWORKTYPE" ON "SCHED_WORK_TYPE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:246 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKWORKTYPE" ON "TASK_WORK_TYPE" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:247 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKMERULE_TASKMERULEINTRVL" ON "TASK_ME_RULE_INTERVAL" ("TASK_DB_ID","TASK_ID","RULE_DATA_TYPE_DB_ID","RULE_DATA_TYPE_ID","ME_DATA_TYPE_DB_ID","ME_DATA_TYPE_ID")
');
END;
/

--changeSet MX-22041.3:248 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKSCHEDRULE_TASKMERULE" ON "TASK_ME_RULE" ("TASK_DB_ID","TASK_ID","RULE_DATA_TYPE_DB_ID","RULE_DATA_TYPE_ID")
');
END;
/

--changeSet MX-22041.3:249 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKADVISORY" ON "TASK_ADVISORY" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:250 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ACCONDSETTING_TASKCOND" ON "TASK_COND" ("AC_COND_DB_ID","AC_COND_CD","COND_SET_DB_ID","COND_SET_CD")
');
END;
/

--changeSet MX-22041.3:251 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CLAIM_CLAIMPARTLINE" ON "CLAIM_PART_LINE" ("CLAIM_DB_ID","CLAIM_ID")
');
END;
/

--changeSet MX-22041.3:252 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CLAIM_CLMLABLINE" ON "CLAIM_LABOUR_LINE" ("CLAIM_DB_ID","CLAIM_ID")
');
END;
/

--changeSet MX-22041.3:253 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPEVENT_LRPEVENTUSAGES" ON "LRP_EVENT_USAGES" ("LRP_EVENT_DB_ID","LRP_EVENT_ID")
');
END;
/

--changeSet MX-22041.3:254 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSMBL_LRPLOCCAP" ON "LRP_LOC_CAPABILITY" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:255 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_WFLEVELHR" ON "WF_DEFN_LEVEL_HR" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:256 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFDEFNLEVEL_WFDEFNLEVELHR" ON "WF_DEFN_LEVEL_HR" ("WF_LEVEL_DEFN_DB_ID","WF_LEVEL_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:257 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFWF_TASKWF" ON "TASK_WF" ("WF_DB_ID","WF_ID")
');
END;
/

--changeSet MX-22041.3:258 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKWF" ON "TASK_WF" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:259 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_WFDEFNSTEP_LEVELS" ON "WF_DEFN_STEP_LEVELS" ("WF_DEFN_STEP_DB_ID","WF_DEFN_STEP_ID")
');
END;
/

--changeSet MX-22041.3:260 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_QUARACTION_QUARACTNASSIGN" ON "QUAR_ACTION_ASSIGNMENT" ("QUAR_DB_ID","QUAR_ID","QUAR_ACTION_ID")
');
END;
/

--changeSet MX-22041.3:261 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPINSTKITMAP_EQPINSTKITPAR" ON "EQP_INSTALL_KIT_PART_MAP" ("EQP_INSTALL_KIT_MAP_DB_ID","EQP_INSTALL_KIT_MAP_ID")
');
END;
/

--changeSet MX-22041.3:262 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EQPINSTKITPARTMAP" ON "EQP_INSTALL_KIT_PART_MAP" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:263 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ESIGDOC_SCHEDWPSIGNESIG" ON "SCHED_WP_SIGN_ESIG" ("DOC_DB_ID","DOC_ID")
');
END;
/

--changeSet MX-22041.3:264 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDWPSIGN_SCHEDWPSIGNESIG" ON "SCHED_WP_SIGN_ESIG" ("SIGN_REQ_DB_ID","SIGN_REQ_ID")
');
END;
/

--changeSet MX-22041.3:265 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBR_SCHEDLBRSTEP" ON "SCHED_LABOUR_STEP" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.3:266 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTEP_SCHEDLBRSTEP" ON "SCHED_LABOUR_STEP" ("SCHED_DB_ID","SCHED_ID","STEP_ID")
');
END;
/

--changeSet MX-22041.3:267 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBR_SCHEDLBRTOOL" ON "SCHED_LABOUR_TOOL" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.3:268 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTTOOL_SCHEDLBRTOOL" ON "SCHED_LABOUR_TOOL" ("EVENT_DB_ID","EVENT_ID","TOOL_ID")
');
END;
/

--changeSet MX-22041.3:269 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBRROLE_SCHEDLBRACTION" ON "SCHED_LABOUR_ACTION" ("LABOUR_ROLE_DB_ID","LABOUR_ROLE_ID")
');
END;
/

--changeSet MX-22041.3:270 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBR_SCHEDLBRPANEL" ON "SCHED_LABOUR_PANEL" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.3:271 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDPANEL_SCHEDLBRPANEL" ON "SCHED_LABOUR_PANEL" ("SCHED_DB_ID","SCHED_ID","SCHED_PANEL_ID")
');
END;
/

--changeSet MX-22041.3:272 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBR_SCHEDLBRINSTPART" ON "SCHED_LABOUR_INST_PART" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.3:273 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBR_SCHEDLBRRMVDPART" ON "SCHED_LABOUR_RMVD_PART" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.3:274 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ESIGDOC_SCHEDLBRESIG" ON "SCHED_LABOUR_ESIG" ("DOC_DB_ID","DOC_ID")
');
END;
/

--changeSet MX-22041.3:275 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBR_SCHEDLBR_PARMDATA" ON "SCHED_LABOUR_PARM_DATA" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.3:276 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLBRROLESTAT_SCHEDLBRES" ON "SCHED_LABOUR_ESIG" ("STATUS_DB_ID","STATUS_ID")
');
END;
/

--changeSet MX-22041.3:277 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPADVSRY_EQPADVSRYATTACH" ON "EQP_ADVSRY_ATTACH" ("ADVSRY_DB_ID","ADVSRY_ID")
');
END;
/

--changeSet MX-22041.3:278 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVRELNOTE" ON "INV_RELIABILITY_NOTE" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:279 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPRTNO_EQPPARTROTABLE" ON "EQP_PART_ROTABLE_ADJUST" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:280 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPKITPARTGROUPS_EQPKITPART" ON "EQP_KIT_PART_MAP" ("EQP_KIT_PART_GROUP_DB_ID","EQP_KIT_PART_GROUP_ID")
');
END;
/

--changeSet MX-22041.3:281 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EQPKITPARTMAP" ON "EQP_KIT_PART_MAP" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:282 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EQPKITPARTGROUPMA" ON "EQP_KIT_PART_GROUP_MAP" ("KIT_PART_NO_DB_ID","KIT_PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:283 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPKITPARTGROUPS_KITPARTGRO" ON "EQP_KIT_PART_GROUP_MAP" ("EQP_KIT_PART_GROUP_DB_ID","EQP_KIT_PART_GROUP_ID")
');
END;
/

--changeSet MX-22041.3:284 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EQPPTVENADV" ON "EQP_PART_VENDOR_ADVSRY" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:285 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_QUARQUAR_QUARACTION" ON "QUAR_ACTION" ("QUAR_DB_ID","QUAR_ID")
');
END;
/

--changeSet MX-22041.3:286 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_QUARACT_QUARACTSTATUS" ON "QUAR_ACTION_STATUS" ("QUAR_DB_ID","QUAR_ID","QUAR_ACTION_ID")
');
END;
/

--changeSet MX-22041.3:287 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENDOR_ORGORGVENDOR" ON "ORG_ORG_VENDOR" ("VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:288 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_ORGORGVENDOR" ON "ORG_ORG_VENDOR" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.3:289 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQP_ADVSRY_EQPPTVENADV" ON "EQP_PART_VENDOR_ADVSRY" ("ADVSRY_DB_ID","ADVSRY_ID")
');
END;
/

--changeSet MX-22041.3:290 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKTASKLOG" ON "TASK_TASK_LOG" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:291 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPARTNO_EQPPARTADVSRY" ON "EQP_PART_ADVSRY" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-22041.3:292 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPADVSRY_EQPPARTADVSRY" ON "EQP_PART_ADVSRY" ("ADVSRY_DB_ID","ADVSRY_ID")
');
END;
/

--changeSet MX-22041.3:293 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVADVSRY" ON "INV_ADVSRY" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:294 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPADVSRY_INVADVSRY" ON "INV_ADVSRY" ("ADVSRY_DB_ID","ADVSRY_ID")
');
END;
/

--changeSet MX-22041.3:295 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVOILSTATUSLOG" ON "INV_OIL_STATUS_LOG" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:296 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_INVPARMDATA" ON "INV_PARM_DATA" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:297 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPORLMVSN_LOGVSN" ON "DPO_RLM_VERSION_LOG" ("RULE_VERSION_ID")
');
END;
/

--changeSet MX-22041.3:298 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPORLMTYPE_ATTROBJTYPE" ON "DPO_RLM_TYPE_ATTR" ("RULE_OBJ_TYPE_CD")
');
END;
/

--changeSet MX-22041.3:299 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPORLMEVT_STRULECLASS" ON "DPO_RLM_EVT_STRUCT" ("RULE_CLASS_CD")
');
END;
/

--changeSet MX-22041.3:300 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPORLM_RULECLASSRULE" ON "DPO_RLM_RULE" ("RULE_CLASS_CD")
');
END;
/

--changeSet MX-22041.3:301 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORG_ORGDB" ON "ORG_DB" ("ORG_DB_ID","ORG_ID")
');
END;
/

--changeSet MX-22041.3:302 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPORLMRULE_DPOXFERRLMERROR" ON "DPO_XFER_RLM_ERROR" ("RULE_CLASS_CD","RULE_ID")
');
END;
/

--changeSet MX-22041.3:303 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSBOMOIL_EQPOILTHCARR" ON "EQP_OIL_THRESHOLD_CARRIER" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:304 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGHR_ORGHRSUPPLY" ON "ORG_HR_SUPPLY" ("HR_DB_ID","HR_ID")
');
END;
/

--changeSet MX-22041.3:305 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSBOMOIL_EQPOILTHINV" ON "EQP_OIL_THRESHOLD_INV" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:306 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVLOC_ORGHRSUPPLY" ON "ORG_HR_SUPPLY" ("LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:307 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDIMPACT" ON "SCHED_IMPACT" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:308 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKIMPACT" ON "TASK_IMPACT" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:309 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSBOMOIL_EQPOILTHASSM" ON "EQP_OIL_THRESHOLD_ASSMBL" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:310 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPASSBOMOIL_EQPOILTHPART" ON "EQP_OIL_THRESHOLD_PART" ("ASSMBL_DB_ID","ASSMBL_CD")
');
END;
/

--changeSet MX-22041.3:311 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOREPATH_DPOEGENOBJ" ON "DPO_GEN_OBJECTS" ("REP_PATH_CD")
');
END;
/

--changeSet MX-22041.3:312 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOREPATH_DPOREPATHRUL" ON "DPO_REP_PATH_RULSET" ("REP_PATH_CD")
');
END;
/

--changeSet MX-22041.3:313 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOREPATH_DPOEXPFILE" ON "DPO_EXPORT_FILE" ("REP_PATH_CD")
');
END;
/

--changeSet MX-22041.3:314 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOREPATH_DPOIMPFILE" ON "DPO_IMPORT_FILE" ("REP_PATH_CD")
');
END;
/

--changeSet MX-22041.3:315 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOREPRUL_DPOREPATHRUL" ON "DPO_REP_PATH_RULSET" ("RULESET_ID")
');
END;
/

--changeSet MX-22041.3:316 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_DPOCONFRES_DPOREPTAB" ON "DPO_HANDLER" ("REP_TABLE_ID")
');
END;
/

--changeSet MX-22041.3:317 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPLOC_LRPLOCCAPACITY" ON "LRP_LOC_CAPACITY" ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID")
');
END;
/

--changeSet MX-22041.3:318 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPLOCCAPACITY_LRPLOCCAPSTD" ON "LRP_LOC_CAP_STD" ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID","CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")
');
END;
/

--changeSet MX-22041.3:319 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CAPACITYPATTERN_LRPLOCCAPAC" ON "LRP_LOC_CAPACITY" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")
');
END;
/

--changeSet MX-22041.3:320 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPLOCCAPACITY_LRPLOCCAPEXC" ON "LRP_LOC_CAP_EXCEPT" ("LRP_DB_ID","LRP_ID","LOC_DB_ID","LOC_ID","CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")
');
END;
/

--changeSet MX-22041.3:321 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FROMACTIVITY_DEPENDENCY" ON "PPC_DEPENDENCY" ("FROM_ACTIVITY_DB_ID","FROM_ACTIVITY_ID")
');
END;
/

--changeSet MX-22041.3:322 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CPDAYSHIFT_CPDAYSKILL" ON "CAPACITY_PATTERN_DAY_SKILL" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_PATTERN_DAY_ORD","SHIFT_DB_ID","SHIFT_ID")
');
END;
/

--changeSet MX-22041.3:323 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_LRPINVTASKPLAN" ON "LRP_INV_TASK_PLAN" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:324 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_LRPINVTASKPLAN" ON "LRP_INV_TASK_PLAN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:325 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPLNTYP_PPCPLNTYP" ON "PPC_PLANNING_TYPE" ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")
');
END;
/

--changeSet MX-22041.3:326 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CP_CPDAY" ON "CAPACITY_PATTERN_DAY" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")
');
END;
/

--changeSet MX-22041.3:327 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CPDAY_CPDAYSHIFT" ON "CAPACITY_PATTERN_DAY_SHIFT" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID","CAPACITY_PATTERN_DAY_ORD")
');
END;
/

--changeSet MX-22041.3:328 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CP_CPSKILL" ON "CAPACITY_PATTERN_SKILL" ("CAPACITY_PATTERN_DB_ID","CAPACITY_PATTERN_ID")
');
END;
/

--changeSet MX-22041.3:329 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SHIFTSHIFT_CPDAYSHIFT" ON "CAPACITY_PATTERN_DAY_SHIFT" ("SHIFT_DB_ID","SHIFT_ID")
');
END;
/

--changeSet MX-22041.3:330 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVKIT_SCHEDKITMAP" ON "SCHED_KIT_MAP" ("KIT_INV_NO_DB_ID","KIT_INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:331 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SDFAULT_SDFAULTRESULT" ON "SD_FAULT_RESULT" ("FAULT_DB_ID","FAULT_ID")
');
END;
/

--changeSet MX-22041.3:332 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCPLNTYP_PLNTYPSKILL" ON "PPC_PLANNING_TYPE_SKILL" ("PPC_WP_DB_ID","PPC_WP_ID","PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")
');
END;
/

--changeSet MX-22041.3:333 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ERHEADER_ERWORKTYPE" ON "ER_WORK_TYPE" ("RULE_DB_ID","RULE_ID")
');
END;
/

--changeSet MX-22041.3:334 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ERHEADER_ERWEEKLYRANGE" ON "ER_WEEKLY_RANGE" ("RULE_DB_ID","RULE_ID")
');
END;
/

--changeSet MX-22041.3:335 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_SCHEDKITMAP" ON "SCHED_KIT_MAP" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-22041.3:336 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDKITMAP" ON "SCHED_KIT_MAP" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:337 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EQPPLANNINGTYPE_LABOURSKILL" ON "EQP_PLANNING_TYPE_SKILL" ("PLANNING_TYPE_DB_ID","PLANNING_TYPE_ID")
');
END;
/

--changeSet MX-22041.3:338 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TOACTIVITY_DEPENDENCY" ON "PPC_DEPENDENCY" ("TO_ACTIVITY_DB_ID","TO_ACTIVITY_ID")
');
END;
/

--changeSet MX-22041.3:339 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_USPATTERN_USPATTERNDAY" ON "USER_SHIFT_PATTERN_DAY" ("USER_SHIFT_PATTERN_DB_ID","USER_SHIFT_PATTERN_ID")
');
END;
/

--changeSet MX-22041.3:340 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKPLANNINGTYPESK" ON "TASK_PLANNING_TYPE_SKILL" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:341 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_EQPRTCMPATTSK" ON "EQP_PART_COMPAT_TASK" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:342 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_IETMTOPIC_IETMTOPICCARRIER" ON "IETM_TOPIC_CARRIER" ("IETM_DB_ID","IETM_ID","IETM_TOPIC_ID")
');
END;
/

--changeSet MX-22041.3:343 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARRIER_IETMTOPICCARRIER" ON "IETM_TOPIC_CARRIER" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.3:344 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDEXTPART" ON "SCHED_EXT_PART" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:345 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDEXTPART_SCHEDLABEXT" ON "SCHED_LABOUR_EXT_PART" ("SCHED_DB_ID","SCHED_ID","SCHED_EXT_PART_ID")
');
END;
/

--changeSet MX-22041.3:346 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDLAB_SCHEDLABEXTPART" ON "SCHED_LABOUR_EXT_PART" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-22041.3:347 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGCARRIER_FAILDEFCARRIER" ON "FAIL_DEFER_CARRIER" ("CARRIER_DB_ID","CARRIER_ID")
');
END;
/

--changeSet MX-22041.3:348 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FAILDEFREF_FAILDEFCARRIER" ON "FAIL_DEFER_CARRIER" ("FAIL_DEFER_REF_DB_ID","FAIL_DEFER_REF_ID")
');
END;
/

--changeSet MX-22041.3:349 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCTASKDEFN_PPCTASKDEFNMAP" ON "PPC_TASK_DEFN_MAP" ("PPC_TASK_DEFN_DB_ID","PPC_TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:350 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCPLAN_PPCLOCEX" ON "PPC_LOC_EXCLUDE" ("PPC_DB_ID","PPC_ID")
');
END;
/

--changeSet MX-22041.3:351 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPTYP_EVTBKT" ON "LRP_EVENT_BUCKET" ("LRP_PLAN_TYPE_DB_ID","LRP_PLAN_TYPE_ID")
');
END;
/

--changeSet MX-22041.3:352 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPEVT_EVTBKT" ON "LRP_EVENT_BUCKET" ("LRP_EVENT_DB_ID","LRP_EVENT_ID")
');
END;
/

--changeSet MX-22041.3:353 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPPTYP_LRPTBKT" ON "LRP_TASK_BUCKET" ("LRP_PLAN_TYPE_DB_ID","LRP_PLAN_TYPE_ID")
');
END;
/

--changeSet MX-22041.3:354 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_LRPTSKPLRNG_TBKT" ON "LRP_TASK_BUCKET" ("TASK_PLAN_RANGE_DB_ID","TASK_PLAN_RANGE_ID")
');
END;
/

--changeSet MX-22041.3:355 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORGVEN_ORGVENSRVTYPE" ON "ORG_VENDOR_SERVICE_TYPE" ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:356 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGORGVENDOR_ORGVENPOTYPE" ON "ORG_VENDOR_PO_TYPE" ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID")
');
END;
/

--changeSet MX-22041.3:357 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCTASK_PPCTASKDEFNMAP" ON "PPC_TASK_DEFN_MAP" ("PPC_TASK_DB_ID","PPC_TASK_ID")
');
END;
/

--changeSet MX-22041.3:358 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAINTPRGM_MAINTPRGMLOG" ON "MAINT_PRGM_LOG" ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID")
');
END;
/

--changeSet MX-22041.3:359 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_SCHEDSRVTYPE" ON "SCHED_SERVICE_TYPE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-22041.3:360 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENPOTYPE_EVTORGVENPO" ON "EVT_ORG_VENDOR_PO_TYPE" ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","PO_TYPE_DB_ID","PO_TYPE_CD")
');
END;
/

--changeSet MX-22041.3:361 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTORGVENSRVTYPE" ON "EVT_ORG_VENDOR_SERVICE_TYPE" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:362 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ORGVENSRVTYPE_EVTORGVENSR" ON "EVT_ORG_VENDOR_SERVICE_TYPE" ("ORG_DB_ID","ORG_ID","VENDOR_DB_ID","VENDOR_ID","SERVICE_TYPE_DB_ID","SERVICE_TYPE_CD")
');
END;
/

--changeSet MX-22041.3:363 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCWRKAREA_PPCWRKAREACRW" ON "PPC_WORK_AREA_CREW" ("PPC_WORK_AREA_DB_ID","PPC_WORK_AREA_ID")
');
END;
/

--changeSet MX-22041.3:364 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_EVTEVENT_EVTORGVENPOTYPE" ON "EVT_ORG_VENDOR_PO_TYPE" ("EVENT_DB_ID","EVENT_ID")
');
END;
/

--changeSet MX-22041.3:365 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCCREW_PPCWRKAREACRW" ON "PPC_WORK_AREA_CREW" ("PPC_CREW_DB_ID","PPC_CREW_ID")
');
END;
/

--changeSet MX-22041.3:366 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_PPCWP_PLANNINGTYPE" ON "PPC_PLANNING_TYPE" ("PPC_WP_DB_ID","PPC_WP_ID")
');
END;
/

--changeSet MX-22041.3:367 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAINTPRGM_TASKTEMPISSUELOG" ON "TASK_TEMP_ISSUE_LOG" ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID")
');
END;
/

--changeSet MX-22041.3:368 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKTASK_TASKTEMPISSUELOG" ON "TASK_TEMP_ISSUE_LOG" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-22041.3:369 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TASKDEFN_MAINTPRGMTEMPTASK" ON "MAINT_PRGM_TEMP_TASK" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
');
END;
/

--changeSet MX-22041.3:370 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_MAINTPRGM_MAINTPRGMTEMPTASK" ON "MAINT_PRGM_TEMP_TASK" ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID")
');
END;
/

--changeSet MX-22041.3:371 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_BLTWFCYCLELOG_BLTWFRECLOG" ON "BLT_WF_REC_LOG" ("WF_CYCLE_LOG_ID")
');
END;
/

--changeSet MX-22041.3:372 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TAX_TAXVENDOR" ON "TAX_VENDOR" ("TAX_ID")
');
END;
/