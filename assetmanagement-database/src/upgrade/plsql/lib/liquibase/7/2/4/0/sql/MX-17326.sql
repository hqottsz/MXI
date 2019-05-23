--liquibase formatted sql


--changeSet MX-17326:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE_KIT_LINE add (
	"RETURN_SHIPMENT_DB_ID" Number(10,0) Check (RETURN_SHIPMENT_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-17326:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE_KIT_LINE add (
	"RETURN_SHIPMENT_ID" Number(10,0) Check (RETURN_SHIPMENT_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-17326:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Foreign key constraint created in 7.x with a different name, therefore, 
-- conditionally drop the existing foreign key before creating the new constraint.
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('PO_LINE_KIT_LINE', 'FK_POLINEKITLINE_SHPSHPMENT');			
END;
/

--changeSet MX-17326:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE_KIT_LINE" add Constraint "FK_POLINEKITLINE_SHIPMENT" foreign key ("RETURN_SHIPMENT_DB_ID","RETURN_SHIPMENT_ID") references "SHIP_SHIPMENT" ("SHIPMENT_DB_ID","SHIPMENT_ID")  DEFERRABLE
');
END;
/