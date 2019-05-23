--liquibase formatted sql


--changeSet DEV-1358:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************************
 * Add CHARGE_ID AND CHARGE_AMOUNT to PO_INVOICE_LINE_CHARGE table.
 ***************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE_LINE_CHARGE add (
		"CHARGE_ID" Raw(16)
)
');
END;
/

--changeSet DEV-1358:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE_LINE_CHARGE add (
		"CHARGE_AMOUNT" Number(15,5)
)
');
END;
/

--changeSet DEV-1358:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add constraint
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_INVOICE_LINE_CHARGE" add Constraint "FK_CHARGE_POINVOICELINECHARGE" foreign key ("CHARGE_ID") references "CHARGE" ("CHARGE_ID")  DEFERRABLE
');
END;
/ 

--changeSet DEV-1358:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CHARGE_POINVOICELINECHARGE" ON "PO_INVOICE_LINE_CHARGE" ("CHARGE_ID")
');
END;
/  

--changeSet DEV-1358:5 stripComments:false
/**************************************************************************
 * Update the rfq_line_vendor_charge table
 **************************************************************************/
 UPDATE
        po_invoice_line_charge
    SET
      charge_id =
      (
          SELECT 
                 charge_id
          FROM 
                 charge
          WHERE
                 charge.charge_code = po_invoice_line_charge.charge_type_cd     
     );     

--changeSet DEV-1358:6 stripComments:false
 UPDATE po_invoice_line_charge SET charge_amount = 0 WHERE charge_amount IS NULL;    

--changeSet DEV-1358:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.table_column_modify('
 Alter table po_invoice_line_charge modify (
    CHARGE_ID Raw(16) NOT NULL DEFERRABLE
 )
 ');
 END;
 / 