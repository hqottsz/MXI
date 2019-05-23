--liquibase formatted sql


--changeSet DEV-1360:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 
-- Migration script for DEV-1360 
--    1. remove the ref_tax_type table
--    2. remove the ref_charge_type table
--    3. remove references to these tables in the utl_menu_item table
--    4. remove references to these tables in the dpo_rep_table table
--    5. remove references to these tables in the utl_pb_ref_term table
--    6. remove charge_price from po_line_charge, po_invoice_line_charge, rfq_line_vendor_charge
--    7. remove tax_price from po_line_tax, po_invoice_line_tax, rfq_line_vendor_tax
--
-- The removal of the ref tables requires that the various line 
-- tables be modified to switch their PFK constraints from the 
-- ref table to the appropriate tax or charge table.
--
-- Prerequisits:
--    DEV-1350.sql (adds tax_id to rfq_line_vendor_tax and its FK to tax)
--    DEV-1351.sql (adds tax_id to po_line_tax and its FK to tax)
--    DEV-1352.sql (adds tax_id to po_invoice_line_tax and its FK to tax)
--    DEV-1356.sql (adds tax_id to rfq_line_vendor_charge and its FK to charge)
--    DEV-1357.sql (adds tax_id to po_line_charge and its FK to charge)
--    DEV-1358.sql (adds tax_id to po_invoice_line_charge and its FK to charge)
-- 
-- 1. Remove REF_TAX_TYPE
-- 1.1. drop ref_tax_type FK reference from po_invoice_line_tax
-- 1.1.1. drop the FK to ref_tax_type
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_INVOICE_LINE_TAX','FK_REFTAXTYPE_POINVOICELINETAX');
END;
/

--changeSet DEV-1360:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.1.2. drop the PK
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_INVOICE_LINE_TAX', 'PK_PO_INVOICE_LINE_TAX');
END;
/

--changeSet DEV-1360:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PO_INVOICE_LINE_TAX');
END;
/

--changeSet DEV-1360:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.1.3. drop the columns
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_INVOICE_LINE_TAX', 'TAX_TYPE_DB_ID');
END;
/

--changeSet DEV-1360:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_INVOICE_LINE_TAX', 'TAX_TYPE_CD');
END;
/

--changeSet DEV-1360:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.1.4. rebuild the PK to use FK to the tax table
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table PO_INVOICE_LINE_TAX add constraint pk_PO_INVOICE_LINE_TAX 
         Primary key (PO_INVOICE_DB_ID, PO_INVOICE_ID, PO_INVOICE_LINE_ID, TAX_ID)
   ');
END;
/ 

--changeSet DEV-1360:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.2. remove ref_tax_type FK reference from po_line_tax
-- 1.2.1. drop the FK to ref_tax_type
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_LINE_TAX','FK_REFTAXTYPE_POLINETAX');
END;
/

--changeSet DEV-1360:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.2.2. drop the PK
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_LINE_TAX', 'PK_PO_LINE_TAX');
END;
/

--changeSet DEV-1360:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PO_LINE_TAX');
END;
/

--changeSet DEV-1360:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.2.3. drop the columns
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_LINE_TAX', 'TAX_TYPE_DB_ID');
END;
/

--changeSet DEV-1360:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_LINE_TAX', 'TAX_TYPE_CD');
END;
/

--changeSet DEV-1360:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.2.4. rebuild the PK to use FK to the tax table
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table PO_LINE_TAX add constraint pk_PO_LINE_TAX 
         Primary key (PO_DB_ID, PO_ID, PO_LINE_ID, TAX_ID)
   ');
END;
/ 

--changeSet DEV-1360:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.3. remove ref_tax_type FK reference from rfq_line_vendor_tax
-- 1.3.1. drop the FK to ref_tax_type
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('RFQ_LINE_VENDOR_TAX','FK_REFTAXTYPE_RFQLINEVNDRTAX');
END;
/

--changeSet DEV-1360:14 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.3.2. drop the PK
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('RFQ_LINE_VENDOR_TAX', 'PK_RFQ_LINE_VENDOR_TAX');
END;
/

--changeSet DEV-1360:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_RFQ_LINE_VENDOR_TAX');
END;
/

--changeSet DEV-1360:16 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.3.3. drop the columns
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('RFQ_LINE_VENDOR_TAX', 'TAX_TYPE_DB_ID');
END;
/

--changeSet DEV-1360:17 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('RFQ_LINE_VENDOR_TAX', 'TAX_TYPE_CD');
END;
/

--changeSet DEV-1360:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.3.4. rebuild the PK to use FK to the tax table
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table RFQ_LINE_VENDOR_TAX add constraint PK_RFQ_LINE_VENDOR_TAX 
         Primary key (RFQ_DB_ID, RFQ_ID, RFQ_LINE_ID, VENDOR_DB_ID, VENDOR_ID, TAX_ID)
   ');
END;
/

--changeSet DEV-1360:19 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 1.4. finally, drop the ref_tax_type table
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('REF_TAX_TYPE');
END;
/  

--changeSet DEV-1360:20 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2. Remove REF_CHARGE_TYPE
-- 2.1. drop ref_charge_type FK reference from po_invoice_line_charge
-- 2.1.1. drop the FK to ref_charge_type
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_INVOICE_LINE_CHARGE','FK_REFCHGTYPE_POINVLINECHG');
END;
/

--changeSet DEV-1360:21 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.1.2. drop the PK
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_INVOICE_LINE_CHARGE', 'PK_PO_INVOICE_LINE_CHARGE');
END;
/

--changeSet DEV-1360:22 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PO_INVOICE_LINE_CHARGE');
END;
/

--changeSet DEV-1360:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.1.3. drop the columns
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_INVOICE_LINE_CHARGE', 'CHARGE_TYPE_DB_ID');
END;
/

--changeSet DEV-1360:24 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_INVOICE_LINE_CHARGE', 'CHARGE_TYPE_CD');
END;
/

--changeSet DEV-1360:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.1.4. rebuild the PK to use FK to the charge table
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table PO_INVOICE_LINE_CHARGE add constraint pk_PO_INVOICE_LINE_CHARGE 
         Primary key (PO_INVOICE_DB_ID, PO_INVOICE_ID, PO_INVOICE_LINE_ID, CHARGE_ID)
   ');
END;
/

--changeSet DEV-1360:26 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.2. remove ref_charge_type FK reference from po_line_charge
-- 2.2.1. drop the FK to ref_charge_type
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_LINE_CHARGE','FK_REFCHARGETYPE_POLINECHARGE');
END;
/

--changeSet DEV-1360:27 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.2.2. drop the PK
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_LINE_CHARGE', 'PK_PO_LINE_CHARGE');
END;
/

--changeSet DEV-1360:28 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_PO_LINE_CHARGE');
END;
/

--changeSet DEV-1360:29 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.2.3. drop the columns
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_LINE_CHARGE', 'CHARGE_TYPE_DB_ID');
END;
/

--changeSet DEV-1360:30 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_LINE_CHARGE', 'CHARGE_TYPE_CD');
END;
/

--changeSet DEV-1360:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.2.4. rebuild the PK to use FK to the charge table
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table PO_LINE_CHARGE add constraint pk_PO_LINE_CHARGE 
         Primary key (PO_DB_ID, PO_ID, PO_LINE_ID, CHARGE_ID)
   ');
END;
/  

--changeSet DEV-1360:32 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.3. remove ref_charge_type FK reference from rfq_line_vendor_charge
-- 2.3.1. drop the FK to ref_charge_type
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('RFQ_LINE_VENDOR_CHARGE','FK_REFCHRGTYPE_RFQLINEVNDRCHRG');
END;
/

--changeSet DEV-1360:33 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.3.2. drop the PK
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('RFQ_LINE_VENDOR_CHARGE', 'PK_RFQ_LINE_VENDOR_CHARGE');
END;
/

--changeSet DEV-1360:34 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_RFQ_LINE_VENDOR_CHARGE');
END;
/

--changeSet DEV-1360:35 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.3.3. drop the columns
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('RFQ_LINE_VENDOR_CHARGE', 'CHARGE_TYPE_DB_ID');
END;
/

--changeSet DEV-1360:36 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('RFQ_LINE_VENDOR_CHARGE', 'CHARGE_TYPE_CD');
END;
/

--changeSet DEV-1360:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.3.4. rebuild the PK to use FK to the charge table
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table RFQ_LINE_VENDOR_CHARGE add constraint PK_RFQ_LINE_VENDOR_CHARGE 
         Primary key (RFQ_DB_ID, RFQ_ID, RFQ_LINE_ID, VENDOR_DB_ID, VENDOR_ID, CHARGE_ID)
   ');
END;
/

--changeSet DEV-1360:38 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 2.4. finally, drop the ref_charge_type table
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('REF_CHARGE_TYPE');
END;
/

--changeSet DEV-1360:39 stripComments:false
-- 3. remove references to ref_charge_type and ref_tax_type in the utl_menu_item table
-- 3.1. remove FK references to utl_menu_item
DELETE FROM 
	utl_menu_item_arg
WHERE
	menu_id in (
		SELECT 
			menu_id
		FROM
			utl_menu_item
		WHERE
			lower(menu_name) in ('ref_charge_type','ref_tax_type')
	);


--changeSet DEV-1360:40 stripComments:false
DELETE FROM
	utl_menu_group_item
WHERE
	menu_id in (
		SELECT 
			menu_id
		FROM
			utl_menu_item
		WHERE
			lower(menu_name) in ('ref_charge_type','ref_tax_type')
	)
;

--changeSet DEV-1360:41 stripComments:false
-- 3.2. remove rows from utl_menu_item
DELETE FROM 
	utl_menu_item
WHERE
	lower(menu_name) in ('ref_charge_type','ref_tax_type')
;

--changeSet DEV-1360:42 stripComments:false
-- 4. remove references to ref_charge_type and ref_tax_type in the dpo_rep_table table
-- 4.1. remove FK references to dpo_rep_table
DELETE FROM
   dpo_handler
WHERE
   rep_table_id in (
      SELECT
         rep_table_id
      FROM
         dpo_rep_table 
      WHERE 
         lower(table_name) in ('ref_charge_type','ref_tax_type')
   )
;

--changeSet DEV-1360:43 stripComments:false
-- 4.2. remove rows from dpo_rep_table
DELETE FROM
   dpo_rep_table 
WHERE 
   lower(table_name) in ('ref_charge_type','ref_tax_type')
;

--changeSet DEV-1360:44 stripComments:false
-- 5. remove references to ref_charge_type and ref_tax_type in the utl_pb_ref_term table
DELETE FROM
   utl_pb_ref_term 
WHERE 
   lower(ref_term_name) in ('ref_charge_type','ref_tax_type')
;

--changeSet DEV-1360:45 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 6. remove charge_price column from various line tables
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_LINE_CHARGE', 'CHARGE_PRICE');
END;
/

--changeSet DEV-1360:46 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_INVOICE_LINE_CHARGE', 'CHARGE_PRICE');
END;
/

--changeSet DEV-1360:47 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('RFQ_LINE_VENDOR_CHARGE', 'CHARGE_PRICE');
END;
/

--changeSet DEV-1360:48 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- 7. remove tax_price column from various line tables
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_LINE_TAX', 'TAX_PRICE');
END;
/

--changeSet DEV-1360:49 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('PO_INVOICE_LINE_TAX', 'TAX_PRICE');
END;
/

--changeSet DEV-1360:50 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_COLUMN_DROP('RFQ_LINE_VENDOR_TAX', 'TAX_PRICE');
END;
/