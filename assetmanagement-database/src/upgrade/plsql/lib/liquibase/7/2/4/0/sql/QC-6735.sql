--liquibase formatted sql


--changeSet QC-6735:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_AC_REG_CD_LOWER');	 
END;
/

--changeSet QC-6735:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN	 
 utl_migr_schema_pkg.index_create('	 
 Create Index "IX_AC_REG_CD_LOWER" ON "INV_AC_REG" (LOWER(AC_REG_CD)) 	 
 ');	 
 END;	 
 /