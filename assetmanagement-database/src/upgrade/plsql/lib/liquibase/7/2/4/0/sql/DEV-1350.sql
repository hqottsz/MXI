--liquibase formatted sql


--changeSet DEV-1350:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************************
 * Add TAX_RATE, TAX_ID and COMPOUND_BOOL cols to REQ_LINE_VENDOR_TAX table.
 ***************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table RFQ_LINE_VENDOR_TAX add (
		"TAX_RATE" Float
)
');
END;
/

--changeSet DEV-1350:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table RFQ_LINE_VENDOR_TAX add (
		"TAX_ID" Raw(16)
)
');
END;
/

--changeSet DEV-1350:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table RFQ_LINE_VENDOR_TAX add (
		"COMPOUND_BOOL" Number(1,0) Check (COMPOUND_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1350:4 stripComments:false
UPDATE RFQ_LINE_VENDOR_TAX SET COMPOUND_BOOL = 0 WHERE COMPOUND_BOOL IS NULL ;

--changeSet DEV-1350:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table RFQ_LINE_VENDOR_TAX modify (
   COMPOUND_BOOL Number(1,0) DEFAULT 0 NOT NULL DEFERRABLE  Check (COMPOUND_BOOL IN (0, 1)) DEFERRABLE
)
');
END;
/

--changeSet DEV-1350:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add constraint
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "RFQ_LINE_VENDOR_TAX" add Constraint "FK_TAX_RFQLINEVENDORTAX" foreign key ("TAX_ID") references "TAX" ("TAX_ID")  DEFERRABLE
');
END;
/ 

--changeSet DEV-1350:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_TAX_RFQLINEVENDORTAX" ON "RFQ_LINE_VENDOR_TAX" ("TAX_ID")
');
END;
/ 

--changeSet DEV-1350:8 stripComments:false
/**************************************************************************
 * Update the rfq_line_vendor_tax.tax_id
 **************************************************************************/
 UPDATE
        rfq_line_vendor_tax
    SET 
        tax_id =
        (
          SELECT 
                 tax_id
          FROM   
                 tax
          WHERE 
               tax.tax_code = rfq_line_vendor_tax.tax_type_cd
        );

--changeSet DEV-1350:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table RFQ_LINE_VENDOR_TAX modify (
   TAX_ID Raw(16) NOT NULL DEFERRABLE
)
');
END;
/ 

--changeSet DEV-1350:10 stripComments:false
/**************************************************************************
 * Taxes are now stored as a percentage.
 **************************************************************************/
 UPDATE 
        rfq_line_vendor_tax
    SET
        rfq_line_vendor_tax.compound_bool = 0,  
   	    rfq_line_vendor_tax.tax_rate  =
          (
            SELECT 
                (( v.line_price / ( v.line_price - sum(c.charge_price) - sum(t.tax_price)) ) - 1) as tax_rate
            FROM
                rfq_line_vendor v
            LEFT JOIN rfq_line_vendor_charge c ON
               v.rfq_db_id = c.rfq_db_id AND
               v.rfq_id = c.rfq_id AND
               v.rfq_line_id = c.rfq_line_id AND
               v.vendor_db_id = c.vendor_db_id AND
               v.vendor_id = c.vendor_id
            LEFT JOIN rfq_line_vendor_tax t ON
               v.rfq_db_id = t.rfq_db_id AND
               v.rfq_id = t.rfq_id AND
               v.rfq_line_id = t.rfq_line_id AND
               v.vendor_db_id = t.vendor_db_id AND
               v.vendor_id = t.vendor_id
            WHERE 
       		     v.rfq_db_id = rfq_line_vendor_tax.rfq_db_id AND
      		     v.rfq_id    = rfq_line_vendor_tax.rfq_id 
      		     AND
      		     v.vendor_db_id = rfq_line_vendor_tax.vendor_db_id AND 
      		     v.vendor_id    = rfq_line_vendor_tax.vendor_id
      		     AND
      		     v.rfq_line_id = rfq_line_vendor_tax.rfq_line_id
            GROUP BY 
               v.rfq_db_id, v.rfq_id, v.rfq_line_id, v.vendor_db_id, v.vendor_id, v.line_price 
         );