--liquibase formatted sql


--changeSet DEV-1356:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************************
 * Add CHARGE_ID AND CHARGE_AMOUNT to RFQ_LINE_VENDOR_CHARGE table.
 ***************************************************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table RFQ_LINE_VENDOR_CHARGE add (
		"CHARGE_ID" Raw(16)  
)
');
END;
/

--changeSet DEV-1356:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table RFQ_LINE_VENDOR_CHARGE add (
		"CHARGE_AMOUNT" Number(15,5)
)
');
END;
/

--changeSet DEV-1356:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add constraint
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "RFQ_LINE_VENDOR_CHARGE" add Constraint "FK_CHARGE_RFQLINEVENDORCHARGE" foreign key ("CHARGE_ID")  references "CHARGE" ("CHARGE_ID") DEFERRABLE
');
END;
/ 

--changeSet DEV-1356:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/**************************************************************************
 * Add index
 **************************************************************************/
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_CHARGE_RFQLINEVENDORCHARGE" ON "RFQ_LINE_VENDOR_CHARGE" ("CHARGE_ID")
');
END;
/ 

--changeSet DEV-1356:5 stripComments:false
/**************************************************************************
 * Update the rfq_line_vendor_charge table
 **************************************************************************/
 UPDATE
        rfq_line_vendor_charge
    SET
      charge_id =
      (
          SELECT 
                 charge_id
          FROM 
                 charge
          WHERE
                 charge.charge_code = rfq_line_vendor_charge.charge_type_cd     
     );     

--changeSet DEV-1356:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table RFQ_LINE_VENDOR_CHARGE modify (
     CHARGE_ID Raw(16) NOT NULL DEFERRABLE
  )
  ');
 END;
/