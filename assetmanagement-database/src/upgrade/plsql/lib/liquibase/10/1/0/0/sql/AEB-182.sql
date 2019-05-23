--liquibase formatted sql

--changeSet AEB-182:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
  CREATE TABLE AEROBUY_INVOICE_CHARGES
  (
    PO_INVOICE_ALT_ID RAW (16) NOT NULL ,
    PACKING          NUMBER (12,2) ,
    SETUP            NUMBER (12,2) ,
    DIE_CHARGE       NUMBER (12,2) ,
    DOCUMENTATION    NUMBER (12,2) ,
    RETURN_CONTAINER NUMBER (12,2) ,
    TRANSPORTATION   NUMBER (12,2) ,
    MISCELLANEOUS    NUMBER (12,2) ,
    FEDERAL_EXCISE   NUMBER (12,2) ,
    FEES_MARKUP      NUMBER (12,2) ,
    CONSTRAINT PK_AEROBUY_INVOICE_CHARGES PRIMARY KEY ( PO_INVOICE_ALT_ID ),
    CONSTRAINT FK_AEB_INV_CHARGE_AEB_INV_INFO FOREIGN KEY ( PO_INVOICE_ALT_ID ) REFERENCES AEROBUY_INVOICE_INFO ( PO_INVOICE_ALT_ID ) NOT DEFERRABLE 
  )
     ');
END;
/

--changeSet AEB-182:2 stripComments:false
COMMENT ON TABLE AEROBUY_INVOICE_CHARGES
IS
  'Integration specific information relating to invoice charges' ;
  
  --changeSet AEB-182:3 stripComments:false
 COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.PO_INVOICE_ALT_ID
IS
  'Primary key is the ALT_ID of the PO_INVOICE' ;
  
  --changeSet AEB-182:4 stripComments:false
 COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.PACKING
IS
  'The Packing Charge of the invoice' ;
  
  --changeSet AEB-182:5 stripComments:false
 COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.SETUP
IS
  'The Setup/Cutting Charge of the invoice' ;
  
  --changeSet AEB-182:6 stripComments:false
COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.DIE_CHARGE
IS
  'The Die Charge of the invoice' ;
  
  --changeSet AEB-182:7 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.DOCUMENTATION
IS
  'The Documentation Charge of the invoice' ;
  
  --changeSet AEB-182:8 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.RETURN_CONTAINER
IS
  'The Returnable Container Charge (refundable) of the invoice' ;
  
  --changeSet AEB-182:9 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.TRANSPORTATION
IS
  'The Transportation Charge of the invoice' ;

  --changeSet AEB-182:10 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.MISCELLANEOUS
IS
  'The Miscellaneous Charge of the invoice' ;
  
  --changeSet AEB-182:11 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.FEDERAL_EXCISE
IS
  'The Federal Excise Tax Charge of the invoice' ;
  
  --changeSet AEB-182:12 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_CHARGES.FEES_MARKUP
IS
  'The Fees/Markup Charge of the invoice' ;