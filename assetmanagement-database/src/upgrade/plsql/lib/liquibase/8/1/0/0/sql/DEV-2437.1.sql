--liquibase formatted sql


--changeSet DEV-2437.1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_INV_MAP" (
	"ID" Raw(16) NOT NULL DEFERRABLE ,
	"ASSET_ID" Raw(16) NOT NULL DEFERRABLE ,
	"MANUFACT_CD" Varchar2 (40) NOT NULL DEFERRABLE ,
	"PART_NO_OEM" Varchar2 (40) NOT NULL DEFERRABLE ,
	"SERIAL_NO_OEM" Varchar2 (40) NOT NULL DEFERRABLE ,
	"INVENTORY_ID" Varchar2 (32),
 Constraint "pk_ARC_INV_MAP" primary key ("ID") 
) 
');
END;
/

--changeSet DEV-2437.1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_ASSET', 'PART_NO_OEM');
END;
/

--changeSet DEV-2437.1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_ASSET', 'MANUFACT_CD');
END;
/

--changeSet DEV-2437.1:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_ASSET', 'SERIAL_NO_OEM');
END;
/

--changeSet DEV-2437.1:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_ASSET', 'INVENTORY_ID');
END;
/

--changeSet DEV-2437.1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "ARC_ASSET" ADD (
	"INVENTORY_MAP_ID" Raw(16)
)
');
END;
/

--changeSet DEV-2437.1:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "ARC_ASSET" ADD (
	"WORKFLOW_CD" Varchar2 (8)
)
');
END;
/

--changeSet DEV-2437.1:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_CONFIG', 'PART_NO_OEM');
END;
/

--changeSet DEV-2437.1:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_CONFIG', 'MANUFACT_CD');
END;
/

--changeSet DEV-2437.1:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_CONFIG', 'SERIAL_NO_OEM');
END;
/

--changeSet DEV-2437.1:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_CONFIG', 'NH_PART_NO_OEM');
END;
/

--changeSet DEV-2437.1:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_CONFIG', 'NH_MANUFACT_CD');
END;
/

--changeSet DEV-2437.1:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_CONFIG', 'NH_SERIAL_NO_OEM');
END;
/

--changeSet DEV-2437.1:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "ARC_CONFIG" ADD (
	"INVENTORY_MAP_ID" Raw(16) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-2437.1:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "ARC_CONFIG" ADD (
	"NH_INVENTORY_MAP_ID" Raw(16)
)
');
END;
/

--changeSet DEV-2437.1:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "ARC_CONFIG" ADD (
	"WORKFLOW_CD" Varchar2 (8)
)
');
END;
/

--changeSet DEV-2437.1:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_INV_DETAILS', 'PART_NO_OEM');
END;
/

--changeSet DEV-2437.1:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_INV_DETAILS', 'MANUFACT_CD');
END;
/

--changeSet DEV-2437.1:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ARC_INV_DETAILS', 'SERIAL_NO_OEM');
END;
/

--changeSet DEV-2437.1:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "ARC_INV_DETAILS" ADD (
	"INVENTORY_MAP_ID" Raw(16) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-2437.1:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "ARC_INV_DETAILS" ADD (
	"WORKFLOW_CD" Varchar2 (8)
)
');
END;
/

--changeSet DEV-2437.1:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCINVMAP" ON "ARC_INV_MAP" ("ASSET_ID")
');
END;
/

--changeSet DEV-2437.1:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCINVMAP_ARCASSET" ON "ARC_ASSET" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet DEV-2437.1:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCINVMAP_ARCCONFIG" ON "ARC_CONFIG" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet DEV-2437.1:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCINVMAP_ARCINVDETAILS" ON "ARC_INV_DETAILS" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet DEV-2437.1:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCINVMAP_NHARCCONFIG" ON "ARC_CONFIG" ("NH_INVENTORY_MAP_ID")
');
END;
/

--changeSet DEV-2437.1:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_MAP" add Constraint "FK_ARCASSET_ARCINVMAP" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2437.1:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_ASSET" add Constraint "FK_ARCINVMAP_ARCASSET" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2437.1:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_CONFIG" add Constraint "FK_ARCINVMAP_ARCCONFIG" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2437.1:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_DETAILS" add Constraint "FK_ARCINVMAP_ARCINVDETAILS" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2437.1:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_CONFIG" add Constraint "FK_ARCINVMAP_NHARCCONFIG" foreign key ("NH_INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")  DEFERRABLE
');
END;
/