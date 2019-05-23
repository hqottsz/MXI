--liquibase formatted sql
  

--changeSet DEV-1234:1 stripComments:false
/********************************************
 * Add BROKER as new vendor type
 ********************************************/
 INSERT INTO
    REF_VENDOR_TYPE    
   (
   	VENDOR_TYPE_DB_ID, VENDOR_TYPE_CD, BITMAP_TAG, BITMAP_DB_ID, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, 
   	REVISION_DT, REVISION_DB_ID, REVISION_USER
   )  
    SELECT 
    	0, 'BROKER', 1, 0, 'BROKER', 'The Vendor decides which other vendor performs repairs', 0, 
    	sysdate, sysdate, 100, 'MXI'
    FROM
       dual
    WHERE
      NOT EXISTS ( SELECT 1 FROM ref_vendor_type WHERE vendor_type_db_id = 0 AND vendor_type_cd = 'BROKER' );                         

--changeSet DEV-1234:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 /********************************************
 * Make vendor_type_cd column unique
 ********************************************/
 BEGIN
 utl_migr_schema_pkg.table_column_modify('
 Alter table REF_VENDOR_TYPE modify (
   VENDOR_TYPE_CD UNIQUE DEFERRABLE
 )
 ');
 END;
 /           