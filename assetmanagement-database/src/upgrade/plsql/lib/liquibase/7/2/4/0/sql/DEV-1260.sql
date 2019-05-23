--liquibase formatted sql


--changeSet DEV-1260:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- migration script for DEV-1260
BEGIN
utl_migr_schema_pkg.table_create('
Create table "PART_PRICE_SP" (
	"UUID" Varchar2 (36) NOT NULL DEFERRABLE ,
	"VENDOR_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"CHANGE_CD" Varchar2 (1) NOT NULL DEFERRABLE ,
	"MANUFACT_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"PART_NO_OEM" Varchar2 (40) NOT NULL DEFERRABLE ,
	"UNIT_PRICE" Number(15,5) NOT NULL DEFERRABLE ,
	"QTY_UNIT_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"CURRENCY_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
	"EFFECTIVE_FROM_DT" Date NOT NULL DEFERRABLE ,
	"LEAD_TIME" Float NOT NULL DEFERRABLE ,
	"EFFECTIVE_TO_DT" Date ,
	"DISCOUNT_PCT" Float,
	"MIN_ORDER_QT" Float NOT NULL DEFERRABLE ,
	"STD_SALE_QT" Float,
	"PRICE_TYPE_CD" Varchar2 (8),
	"CONTRACT_NO" Varchar2 (40),
	"DOC_REF_SDESC" Varchar2 (80),
	"RFQ_REF" Varchar2 (80),
	"VENDOR_NOTE" Varchar2 (4000),
	"VENDOR_EXISTS" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (VENDOR_EXISTS BETWEEN 0 AND 1 ) DEFERRABLE ,
	"MANUFACT_EXISTS" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (MANUFACT_EXISTS BETWEEN 0 AND 1 ) DEFERRABLE ,
	"PART_NO_EXISTS" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (PART_NO_EXISTS BETWEEN 0 AND 1 ) DEFERRABLE ,
	"QTY_UNIT_EXISTS" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (QTY_UNIT_EXISTS BETWEEN 0 AND 1 ) DEFERRABLE ,
	"CURRENCY_EXISTS" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (CURRENCY_EXISTS BETWEEN 0 AND 1 ) DEFERRABLE ,
	"QTY_UNIT_APPLIES" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (QTY_UNIT_APPLIES BETWEEN 0 AND 1 ) DEFERRABLE ,
	"MANUFACT_APPLIES" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (MANUFACT_APPLIES BETWEEN 0 AND 1 ) DEFERRABLE, 
	"PRT_VD_APPLIES" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (PRT_VD_APPLIES BETWEEN 0 AND 1 ) DEFERRABLE,
	"CNR_UNIQ_APPLIES" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (CNR_UNIQ_APPLIES BETWEEN 0 AND 1 ) DEFERRABLE )
');
utl_migr_schema_pkg.index_create('
Create Index "PARTPRICESP_VENDORCD" ON "PART_PRICE_SP" ("VENDOR_CD") 
');
utl_migr_schema_pkg.index_create('
Create Index "PARTPRICESP_MANUFACTCD" ON "PART_PRICE_SP" ("MANUFACT_CD") 
');
utl_migr_schema_pkg.index_create('
Create Index "PARTPRICESP_PARTNOEM" ON "PART_PRICE_SP" ("PART_NO_OEM") 
');
END;
/

--changeSet DEV-1260:2 stripComments:false
-- Part Price Management Adapter
INSERT INTO int_bp_lookup ( NAMESPACE, ROOT_NAME, REF_TYPE, REF_NAME, METHOD_NAME, INT_LOGGING_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'http://xml.mxi.com/xsd/procurement/1.0','MxPartPrices','JAVA', 'com.mxi.mx.core.adapter.procurement.partpricemanagement.PartPriceManagementEntryPoint10', 'coordinate', 'FULL', 0, to_date('10-10-2011 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), to_date('10-10-2011 15:00:00', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM INT_BP_LOOKUP WHERE NAMESPACE = 'http://xml.mxi.com/xsd/procurement/1.0' AND ROOT_NAME = 'MxPartPrices');