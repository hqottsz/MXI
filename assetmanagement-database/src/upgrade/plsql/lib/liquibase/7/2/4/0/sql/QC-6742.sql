--liquibase formatted sql


--changeSet QC-6742:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_EQP_PART_COMPAT_DEF');	 
END;
/

--changeSet QC-6742:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN	 
 utl_migr_schema_pkg.index_create('	 
 Create Index "IX_EQP_PART_COMPAT_DEF" ON "EQP_PART_COMPAT_DEF" ("NH_PART_NO_DB_ID","NH_PART_NO_ID","NH_BOM_PART_DB_ID","NH_BOM_PART_ID") 
 ');	 
 END;	 
 /

--changeSet QC-6742:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_MAIN_INV_EVTINV');	 
END;
/

--changeSet QC-6742:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN	 
 utl_migr_schema_pkg.index_create('	 
 Create Index "IX_MAIN_INV_EVTINV" ON "EVT_INV" ("INV_NO_DB_ID","INV_NO_ID","MAIN_INV_BOOL") 
 ');	 
 END;	 
 /