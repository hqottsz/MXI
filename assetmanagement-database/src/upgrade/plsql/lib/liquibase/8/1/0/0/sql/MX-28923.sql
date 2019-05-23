--liquibase formatted sql


--changeSet MX-28923:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add new index for FL_LEG_DISRUPT table
 **************************************************************************/ 
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_FLLEGDISRUPT_DISRDESC_UPPER');
END;
/ 

--changeSet MX-28923:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FLLEGDISRUPT_DISRDESC_UPPER" ON "FL_LEG_DISRUPT" (UPPER(DISRUPTION_DESC))
');
END;
/ 

--changeSet MX-28923:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add new index for INV_INV table
 **************************************************************************/ 
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_INVINV_BARCODESDESC_UPPER');
END;
/ 

--changeSet MX-28923:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_INVINV_BARCODESDESC_UPPER" ON "INV_INV" (UPPER(BARCODE_SDESC))
');
END;
/

--changeSet MX-28923:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add new index for SCHED_STASK table
 **************************************************************************/ 
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_SCHEDSTASK_BARCODESD_UPPER');
END;
/ 

--changeSet MX-28923:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_SCHEDSTASK_BARCODESD_UPPER" ON "SCHED_STASK" (UPPER(BARCODE_SDESC))
');
END;
/

--changeSet MX-28923:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/************************************************************************** 
 * Add new index for QUAR_QUAR table
 **************************************************************************/ 
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_QUARQUAR_BARCODESDESC_UPPER');
END;
/ 

--changeSet MX-28923:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_QUARQUAR_BARCODESDESC_UPPER" ON "QUAR_QUAR" (UPPER(BARCODE_SDESC))
');
END;
/