--liquibase formatted sql


--changeSet DEV-2449:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_INV_USAGE" (
   "ID" Raw(16) NOT NULL DEFERRABLE ,
   "ASSET_ID" Raw(16) NOT NULL DEFERRABLE ,
   "INVENTORY_MAP_ID" Raw(16) NOT NULL DEFERRABLE ,
   "PARAMETER" Varchar2 (80),
   "TSN_QT" FLOAT,
   "TSO_QT" FLOAT,
   "TSI_QT" FLOAT,
   "WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_INV_USAGE" primary key ("ID") 
) 
');
END;
/

--changeSet DEV-2449:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCINVUSAGE" ON "ARC_INV_USAGE" ("ASSET_ID")
');
END;
/

--changeSet DEV-2449:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_USAGE" add Constraint "FK_ARCASSET_ARCINVUSAGE" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2449:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCINVMAP_ARCINVUSAGE" ON "ARC_INV_USAGE" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet DEV-2449:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_USAGE" add Constraint "FK_ARCINVMAP_ARCINVUSAGE" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")  DEFERRABLE
');
END;
/