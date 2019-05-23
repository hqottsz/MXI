--liquibase formatted sql


--changeSet MX-17677:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('AC_REG_CD_LOWER');
END;
/

--changeSet MX-17677:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_VENDOR_WO_REF_SDESC_LOWER');
END;
/

--changeSet MX-17677:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('IX_EXT_KEY_SDESC_LOWER');
END;
/

--changeSet MX-17677:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INV_NO_SDESC_LOWER');
END;
/

--changeSet MX-17677:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INV_BARCODE_SDESC_UPPER');
END;
/

--changeSet MX-17677:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PART_NO_SDESC_LOWER');
END;
/

--changeSet MX-17677:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('STOCK_NO_DB_ID_LOWER');
END;
/

--changeSet MX-17677:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('STOCK_NO_ID_LOWER');
END;
/

--changeSet MX-17677:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PART_NO_MANUFACT_CD_LOWER');
END;
/

--changeSet MX-17677:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('DOC_REF_SDESC_LOWER');
END;
/

--changeSet MX-17677:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('EVENT_SDESC_UPPER');
END;
/

--changeSet MX-17677:12 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('RO_REF_SDESC_LOWER');
END;
/

--changeSet MX-17677:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('BARCODE_SDESC_UPPER');
END;
/

--changeSet MX-17677:14 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('EQP_POS_CD_LOWER');
END;
/

--changeSet MX-17677:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('DESC_SDESC_LOWER');
END;
/

--changeSet MX-17677:16 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('ACCOUNT_CD_LOWER');
END;
/

--changeSet MX-17677:17 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('SHPMNT_SERIAL_NO_OEM_LOWER');
END;
/

--changeSet MX-17677:18 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('QTY_UNIT_DB_ID_LOWER');
END;
/

--changeSet MX-17677:19 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INV_STOCK_NO_DB_ID_LOWER');
END;
/

--changeSet MX-17677:20 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('INV_STOCK_NO_ID_LOWER');
END;
/

--changeSet MX-17677:21 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('STOCK_NO_NAME_LOWER');
END;
/

--changeSet MX-17677:22 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('ABC_CLASS_DB_ID_LOWER');
END;
/

--changeSet MX-17677:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('ABC_CLASS_CD_LOWER');
END;
/

--changeSet MX-17677:24 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('BOM_PART_DB_ID_LOWER');
END;
/

--changeSet MX-17677:25 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('BOM_PART_ID_LOWER');
END;
/

--changeSet MX-17677:26 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('ASSMBL_DB_ID_LOWER');
END;
/

--changeSet MX-17677:27 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('ASSMBL_BOM_ID_LOWER');
END;
/

--changeSet MX-17677:28 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PART_NO_VENDOR_LOWER');
END;
/

--changeSet MX-17677:29 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('FIRST_NAME_LOWER');
END;
/

--changeSet MX-17677:30 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('LAST_NAME_LOWER');
END;
/

--changeSet MX-17677:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Commented out due to changes that have already been applied in 7.2.0.0-SP1
-- BEGIN
-- utl_migr_schema_pkg.index_create('
-- CREATE INDEX ac_reg_cd_lower ON inv_ac_reg(lower(ac_reg_cd)) 
-- ')
-- END
-- 
BEGIN
utl_migr_schema_pkg.index_create('
CREATE INDEX inv_barcode_sdesc_lower ON inv_inv(lower(barcode_sdesc))
');
END;
/