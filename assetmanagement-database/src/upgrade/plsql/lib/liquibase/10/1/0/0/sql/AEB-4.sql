--liquibase formatted sql


--changeSet AEB-4:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_create ('
  CREATE TABLE AEROBUY_INVOICE_TAXES
  (
    PO_INVOICE_ALT_ID RAW (16) NOT NULL ,
    LOCAL_TAX        NUMBER (4,2) ,
    STATE_TAX        NUMBER (4,2) ,
    COUNTY_TAX       NUMBER (4,2) ,
    VAT_SALE         NUMBER (4,2) ,
    VAT_PURCHASE     NUMBER (4,2) ,
    VAT_CREDIT_LOAN  NUMBER (4,2) ,
    VAT_EXCHANGE     NUMBER (4,2) ,
    VAT_LEASE        NUMBER (4,2) ,
    VAT_PROVISIONING NUMBER (4,2) ,
    VAT_COMMISSIONS  NUMBER (4,2) ,
    VAT_EXEMPT       NUMBER (4,2),
	CONSTRAINT PK_AEROBUY_INVOICE_TAXES PRIMARY KEY ( PO_INVOICE_ALT_ID ),
	CONSTRAINT FK_AEB_INV_TAXES_AEB_INV_INFO FOREIGN KEY ( PO_INVOICE_ALT_ID ) REFERENCES AEROBUY_INVOICE_INFO ( PO_INVOICE_ALT_ID ) NOT DEFERRABLE 
  )
     ');
END;
/

--changeSet AEB-4:2 stripComments:false
COMMENT ON TABLE AEROBUY_INVOICE_TAXES
IS
  'Integration specific information relating to invoice taxes' ;
  
  --changeSet AEB-4:3 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.PO_INVOICE_ALT_ID
IS
  'Primary key is the ALT_ID of the PO_INVOICE' ;
  
  --changeSet AEB-4:4 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.LOCAL_TAX
IS
  'Local Sales Tax of the invoice' ;
  
  --changeSet AEB-4:5 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.STATE_TAX
IS
  'State Sales Tax of the invoice' ;
  
  --changeSet AEB-4:6 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.COUNTY_TAX
IS
  'County Sales Tax of the invoice' ;
  
  --changeSet AEB-4:7 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.VAT_SALE
IS
  'Value Added Tax Sale of the invoice' ;
  
  --changeSet AEB-4:8 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.VAT_PURCHASE
IS
  'Value Added Tax Purchase of the invoice' ;
  
  --changeSet AEB-4:9 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.VAT_CREDIT_LOAN
IS
  'Value Added Tax Credit Loans/Conditional Sales of the invoice' ;
  
  --changeSet AEB-4:10 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.VAT_EXCHANGE
IS
  'Value Added Tax Exchange of the invoice' ;
  
  --changeSet AEB-4:11 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.VAT_LEASE
IS
  'Value Added Tax Lease/Hire/Rental of the invoice' ;
  
  --changeSet AEB-4:12 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.VAT_PROVISIONING
IS
  'Value Added Tax Provisioning of the invoice' ;
  
  --changeSet AEB-4:13 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.VAT_COMMISSIONS
IS
  'Value Added Tax Sales/Commissions of the invoice' ;
  
  --changeSet AEB-4:14 stripComments:false
  COMMENT ON COLUMN AEROBUY_INVOICE_TAXES.VAT_EXEMPT
IS
  'Value Added Tax Exempt of the invoice' ;
