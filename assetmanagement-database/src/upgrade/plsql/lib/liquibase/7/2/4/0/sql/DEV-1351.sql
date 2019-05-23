--liquibase formatted sql


--changeSet DEV-1351:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************************
 * Add TAX_RATE, TAX_ID and COMPOUND_BOOL cols to PO_LINE_TAX table.
 ***************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE_TAX add (
		"TAX_RATE" Float
)
');
END;
/

--changeSet DEV-1351:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE_TAX add (
		"TAX_ID" Raw(16)
)
');
END;
/

--changeSet DEV-1351:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE_TAX add (
		"COMPOUND_BOOL" Number(1,0) Check (COMPOUND_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1351:4 stripComments:false
UPDATE PO_LINE_TAX SET COMPOUND_BOOL = 0 WHERE COMPOUND_BOOL IS NULL ;

--changeSet DEV-1351:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_LINE_TAX modify (
   COMPOUND_BOOL Number(1,0) DEFAULT 0 NOT NULL DEFERRABLE  Check (COMPOUND_BOOL IN (0, 1)) DEFERRABLE
)
');
END;
/

--changeSet DEV-1351:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add constraint
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE_TAX" add Constraint "FK_TAX_POLINETAX" foreign key ("TAX_ID") references "TAX" ("TAX_ID")  DEFERRABLE
');
END;

/  

--changeSet DEV-1351:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TAX_POLINETAX" ON "PO_LINE_TAX" ("TAX_ID")
');
END;
/ 

--changeSet DEV-1351:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TAX_TAXLOG" ON "TAX_LOG" ("TAX_ID")
');
END;
/

--changeSet DEV-1351:9 stripComments:false
/**************************************************************************
 * Update po_line_tax table
 **************************************************************************/
    UPDATE 
       po_line_tax
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
              DECODE( po_line.line_price, 0, 0,
                      DECODE( po_line.unit_price, 0, 0,
                             (po_line_tax.tax_price / (po_line.unit_price * po_line.order_qt))
                      )
              ) AS tax_rate,
              0 AS compound_bool
           FROM
              tax,
              po_line
           WHERE
              tax.tax_code = po_line_tax.tax_type_cd AND
              po_line.po_db_id = po_line_tax.po_db_id AND
              po_line.po_id    = po_line_tax.po_id AND
              po_line.po_line_id = po_line_tax.po_line_id
      );       

--changeSet DEV-1351:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.table_column_modify('
 Alter table PO_LINE_TAX modify (
    TAX_ID Raw(16) NOT NULL DEFERRABLE
 )
 ');
 END;
/