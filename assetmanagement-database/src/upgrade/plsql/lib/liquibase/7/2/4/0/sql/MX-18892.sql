--liquibase formatted sql


--changeSet MX-18892:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.index_create('
    Create Index "EQPPARTNO_EVTPARTNO_FK" ON "EVT_PART_NO" ("PART_NO_DB_ID","PART_NO_ID") 
    ');
     END;
     /

--changeSet MX-18892:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.index_create('
    Create Index "EQPPARTNO_SCHEDPART_FK" ON "SCHED_PART" ("SPEC_PART_NO_DB_ID","SPEC_PART_NO_ID") 
    ');
     END;
     /

--changeSet MX-18892:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.index_create('
    Create Index "EQPPARTNO_SCHEDSTASK_FK" ON "SCHED_STASK" ("ORIG_PART_NO_DB_ID","ORIG_PART_NO_ID") 
    ');
     END;
     /               

--changeSet MX-18892:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.index_create('
    Create Index "INVINV_INVLOCINVRECOUNT_FK" ON "INV_LOC_INV_RECOUNT" ("INV_NO_DB_ID","INV_NO_ID") 
    ');
     END;
     /                         