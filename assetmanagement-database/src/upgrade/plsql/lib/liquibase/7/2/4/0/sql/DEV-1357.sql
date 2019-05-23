--liquibase formatted sql


--changeSet DEV-1357:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************************
 * Add CHARGE_ID, CHARGE_AMOUNT   cols to PO_LINE_CHARGE table.
 ***************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE_CHARGE add (
		"CHARGE_ID" Raw(16) 
)
');
END;
/

--changeSet DEV-1357:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE_CHARGE add (
		"CHARGE_AMOUNT" Number(15,5)
)
');
END;
/ 

--changeSet DEV-1357:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add constraint
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE_CHARGE" add Constraint "FK_CHARGE_POLINECHARGE" foreign key ("CHARGE_ID") references "CHARGE" ("CHARGE_ID")  DEFERRABLE
');
END;
/ 

--changeSet DEV-1357:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CHARGE_POLINECHARGE" ON "PO_LINE_CHARGE" ("CHARGE_ID")
');
END;
/  

--changeSet DEV-1357:5 stripComments:false
/**************************************************************************
 * Update the po_line_charge.tax_id
 **************************************************************************/
 UPDATE
        po_line_charge
    SET
      charge_id =
      (
          SELECT 
                 charge_id
          FROM 
                 charge
          WHERE
                 charge.charge_code = po_line_charge.charge_type_cd     
     );     

--changeSet DEV-1357:6 stripComments:false
UPDATE po_line_charge SET charge_amount = 0 WHERE charge_amount IS NULL;   

--changeSet DEV-1357:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_LINE_CHARGE modify (
   CHARGE_ID Raw(16) NOT NULL DEFERRABLE
)
');
END;
/ 