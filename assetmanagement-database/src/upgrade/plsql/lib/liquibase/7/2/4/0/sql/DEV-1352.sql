--liquibase formatted sql


--changeSet DEV-1352:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************************
 * Add TAX_RATE, TAX_ID and COMPOUND_BOOL cols to PO_INVOICE_LINE_TAX table.
 ***************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE_LINE_TAX add (
		"TAX_RATE" Float
)
');
END;
/

--changeSet DEV-1352:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE_LINE_TAX add (
		"TAX_ID" Raw(16)
)
');
END;
/

--changeSet DEV-1352:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE_LINE_TAX add (
		"COMPOUND_BOOL" Number(1,0) Check (COMPOUND_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/ 

--changeSet DEV-1352:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add constraint
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_INVOICE_LINE_TAX" add Constraint "FK_TAX_POINVOICELINETAX" foreign key ("TAX_ID") references "TAX" ("TAX_ID")  DEFERRABLE
');
END;
/   

--changeSet DEV-1352:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 /**************************************************************************
  * Add index
  **************************************************************************/
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_TAX_POINVOICELINETAX" ON "PO_INVOICE_LINE_TAX" ("TAX_ID")
 ');
 END;
/ 

--changeSet DEV-1352:6 stripComments:false
/**************************************************************************
 * Update po_line_tax table
 **************************************************************************/
    UPDATE 
       po_invoice_line_tax
    SET
       (
           tax_id,
           tax_rate,
           compound_bool
       )
       =
       (
           SELECT
              tax.tax_id,
              DECODE( po_invoice_line.line_price, 0, 0,
                      DECODE( po_invoice_line.unit_price, 0, 0,
                             (po_invoice_line_tax.tax_price / (po_invoice_line.unit_price * po_invoice_line.invoice_qt))
                      )
              ) AS tax_rate,
              0 AS compound_bool
           FROM
              tax,
              po_invoice_line
           WHERE
              tax.tax_code = po_invoice_line_tax.tax_type_cd AND
              po_invoice_line.po_invoice_db_id   = po_invoice_line_tax.po_invoice_db_id AND
              po_invoice_line.po_invoice_id      = po_invoice_line_tax.po_invoice_id AND
              po_invoice_line.po_invoice_line_id = po_invoice_line_tax.po_invoice_line_id
      );      

--changeSet DEV-1352:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
   Alter table PO_INVOICE_LINE_TAX modify (
      TAX_ID Raw(16)  NOT NULL DEFERRABLE
   )
   ');
END;
/

--changeSet DEV-1352:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
   Alter table PO_INVOICE_LINE_TAX modify (
      TAX_RATE Float NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-1352:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
   Alter table PO_INVOICE_LINE_TAX modify (
      COMPOUND_BOOL" Number(1,0) DEFAULT 0 Check (COMPOUND_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/