--liquibase formatted sql

--changeSet OPER-14727:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
  CREATE TABLE AEROBUY_INVOICE_INFO
  (
    PO_INVOICE_ALT_ID RAW (16) NOT NULL ,
    INVOICE_TYPE VARCHAR2 (1) NOT NULL ,
	SHIP_TO_CODE VARCHAR2 (5) NOT NULL ,
    PACKING_SHEET_NUMBER VARCHAR2 (15) NOT NULL ,
    BILL_OF_LADING_NUMBER VARCHAR2 (24) ,
    REFERENCE_INVOICE_NUMBER VARCHAR2 (25) ,
    HARMONIZED_TARIFF_CODE VARCHAR2 (15) ,
    CONSTRAINT PK_AEROBUY_INVOICE_INFO PRIMARY KEY ( PO_INVOICE_ALT_ID )
  )
     ');
END;
/

--changeSet OPER-14727:2 stripComments:false
COMMENT ON TABLE AEROBUY_INVOICE_INFO
IS
  'Integration specific information relating to invoices' ;

--changeSet OPER-14727:3 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_INFO.PO_INVOICE_ALT_ID
IS
  'Primary key is the ALT_ID of the PO_INVOICE' ;

--changeSet OPER-14727:4 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_INFO.INVOICE_TYPE
IS
  'Set to either ''C'' for credit or ''D'' for debit' ;

