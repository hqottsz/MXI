--liquibase formatted sql


--changeSet DEV-1281:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/*************************************************************************
* 
* Add new columns BROKER_DB_ID, BROKER_ID as foreign key to ORG_VENDOR
*
**************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   BROKER_DB_ID Number(10,0) Check (BROKER_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1281:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   BROKER_ID Number(10,0) Check (BROKER_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1281:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_HEADER" add Constraint "FK_ORGVENDOR_BROKPOHEADER" foreign key ("BROKER_DB_ID","BROKER_ID") references "ORG_VENDOR" ("VENDOR_DB_ID","VENDOR_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1281:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**********************************************************************************************
* 
* Add new columns BROKER_ACCOUNT_DB_ID, BROKER_ACCOUNT_ID as foreign key to ORG_VENDOR_ACCOUNT
*
************************************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   BROKER_ACCOUNT_DB_ID Number(10,0) Check (BROKER_ACCOUNT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1281:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   BROKER_ACCOUNT_ID Number(10,0) Check (BROKER_ACCOUNT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1281:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
   BROKER_ACCOUNT_CD Varchar2 (40)
)
');
END;
/

--changeSet DEV-1281:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table PO_HEADER add constraint FK_BROKORGVENDACC_POHEADER foreign key ("BROKER_ACCOUNT_DB_ID","BROKER_ACCOUNT_ID","BROKER_ACCOUNT_CD") references "ORG_VENDOR_ACCOUNT" ("VENDOR_DB_ID","VENDOR_ID","ACCOUNT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1281:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
*
* Update all existing orders to have the same values for broker and vendor
*
***************************************************************************/
BEGIN
    EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_HEADER DISABLE';
END;
/

--changeSet DEV-1281:9 stripComments:false
UPDATE
   po_header broker_po_header
SET
   ( broker_db_id,
     broker_id,
     broker_account_db_id,
     broker_account_id,
     broker_account_cd
   )
   =
   (
     SELECT
        vendor_db_id,
        vendor_id,
        vendor_account_db_id,
        vendor_account_id,
        vendor_account_cd
     FROM
        po_header vendor_po_header         
     WHERE
        broker_po_header.po_db_id = vendor_po_header.po_db_id AND
        broker_po_header.po_id    = vendor_po_header.po_id    
    )   
WHERE  
   broker_po_header.broker_db_id IS NULL AND
   broker_po_header.broker_id IS NULL ;   

--changeSet DEV-1281:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
    EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_HEADER ENABLE';
END;
/

--changeSet DEV-1281:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
*
* Add NOT NULL constraint to BROKER_DB_ID and BROKER_ID columns 
*
***************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
   BROKER_DB_ID Number(10,0) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-1281:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
   BROKER_ID Number(10,0) NOT NULL DEFERRABLE
)
');
END;
/