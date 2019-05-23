--liquibase formatted sql


--changeSet MX-17531:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EQPBOMPART_EVTINV_FK" ON "EVT_INV" ("BOM_PART_DB_ID","BOM_PART_ID") 
');
END;
/

--changeSet MX-17531:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EQPPARTNO_EVTINV_FK" ON "EVT_INV" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-17531:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EQPBOMPART_EVTINV_FK" ON "EVT_INV" ("BOM_PART_DB_ID","BOM_PART_ID")
');
END;
/

--changeSet MX-17531:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EQPPARTNO_EVTINV_FK" ON "EVT_INV" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-17531:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "PREDRAWSCHEDSTASK_INVLOC_FK" ON "INV_LOC" ("PREDRAW_SCHED_DB_ID","PREDRAW_SCHED_ID") 
');
END;
/

--changeSet MX-17531:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "INVINV_INVXFER_FK" ON "INV_XFER" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-17531:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Commented out due to changes that have already been applied in 7.2.0.0-SP1
-- BEGIN
-- utl_migr_schema_pkg.index_create('
-- Create Index "INVINV_POLINE_FK" ON "PO_LINE" ("RETURN_INV_NO_DB_ID","RETURN_INV_NO_ID")
-- ')
-- END
-- 
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EQPPARTNO_FNCXACTIONLOG_FK" ON "FNC_XACTION_LOG" ("PART_NO_DB_ID","PART_NO_ID")
');
END;
/

--changeSet MX-17531:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "INVINV_FNCXACTIONLOG_FK" ON "FNC_XACTION_LOG" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-17531:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "TASKTASK_SCHEDWOMPC_FK" ON "SCHED_WO_MPC" ("TASK_DB_ID","TASK_ID")
');
END;
/

--changeSet MX-17531:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "SCHEDSTASK_SCHEDWOMPC_FK" ON "SCHED_WO_MPC" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-17531:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "INVINV_INVINVOEMASSMBL_FK" ON "INV_INV_OEM_ASSMBL" ("INV_NO_DB_ID","INV_NO_ID")
');
END;
/

--changeSet MX-17531:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "SCHEDSTASK_WARRANTEVALQUEUE_FK" ON "WARRANTY_EVAL_QUEUE" ("SCHED_DB_ID","SCHED_ID")
');
END;
/

--changeSet MX-17531:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EVTLABOUR_WARRANTYEVALLABOU_FK" ON "WARRANTY_EVAL_LABOUR" ("LABOUR_DB_ID","LABOUR_ID")
');
END;
/

--changeSet MX-17531:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "EVTEVENT_EQPPARTROTABLE_FK" ON "EQP_PART_ROTABLE_ADJUST" ("EVENT_DB_ID","EVENT_ID")
');
END;
/