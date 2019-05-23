--liquibase formatted sql


--changeSet MX-28513:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add new indexed on children from mim_data_type
 **************************************************************************/ 
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_EQPDATASOURCESPEC" ON "EQP_DATA_SOURCE_SPEC" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_EQPPARTNO" ON "EQP_PART_NO" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_EVTINVUSAGE" ON "EVT_INV_USAGE" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_INVCHRDATA" ON "INV_CHR_DATA" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_INVCURRUSAGE" ON "INV_CURR_USAGE" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_INVNUMDATA" ON "INV_NUM_DATA" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_INVPARMDATA" ON "INV_PARM_DATA" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_TASKSCHEDRULE" ON "TASK_SCHED_RULE" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_EQPFLIGHTSPEC" ON "EQP_FLIGHT_SPEC" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_EVTSCHEDDEAD" ON "EVT_SCHED_DEAD" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_TASKPARMDATA" ON "TASK_PARM_DATA" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_FCRATE" ON "FC_RATE" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_TASKDEADLINEEXT" ON "TASK_DEADLINE_EXT" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_WARRANTYDEFN" ON "WARRANTY_DEFN" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_LRPEVENTUSAGES" ON "LRP_EVENT_USAGES" ("DATA_TYPE_DB_ID","DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_TASKMERULE" ON "TASK_ME_RULE" ("ME_DATA_TYPE_DB_ID","ME_DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_EQPASSMBLBOMOIL" ON "EQP_ASSMBL_BOM_OIL" ("OIL_DATA_TYPE_DB_ID","OIL_DATA_TYPE_ID") 
');
END;
/

--changeSet MX-28513:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_MDATATYPE_EQPASSMBLBOMTIME" ON "EQP_ASSMBL_BOM_OIL" ("TIME_DATA_TYPE_DB_ID","TIME_DATA_TYPE_ID") 
');
END;
/