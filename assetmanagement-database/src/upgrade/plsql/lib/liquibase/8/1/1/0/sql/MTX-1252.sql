--liquibase formatted sql


--changeSet MTX-1252:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_FAULT" (
   "ID" Raw(16) NOT NULL ,
   "INVENTORY_MAP_ID" Raw(16) NOT NULL ,
   "ASSET_ID" Raw(16) NOT NULL ,
   "FAILED_SYSTEM" Varchar2 (50),
   "FAULT_NAME" Varchar2 (260) NOT NULL ,
   "FAULT_LDESC" Varchar2 (4000),
   "LOGBOOK_REF" Varchar2 (80),
   "FAULT_SOURCE_CD" Varchar2 (8),
   "FOUND_ON_DT" Date NOT NULL ,
   "DEFER_CD" Varchar2 (80),
   "FAIL_SEV_CD" Varchar2 (8),
   "DUE_DT" Date,
   "DEFER_REF_SDESC" Varchar2 (80),
   "FAIL_PRIORITY_CD" Varchar2 (8),
   "EXT_KEY_SDESC" Varchar2 (80),
   "DOC_REF_SDESC" Varchar2 (80),
   "EXT_DUE_DT" Date,
   "EXT_REFERENCE_NUMBER" Varchar2 (500),
   "EXT_NOTE" Varchar2 (1000),
   "WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_FAULT" primary key ("ID") 
)  
');
END;
/

--changeSet MTX-1252:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCASSET_ARCFAULT" ON "ARC_FAULT" ("ASSET_ID")
');
END;
/

--changeSet MTX-1252:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_FAULT" add Constraint "FK_ARCASSET_ARCFAULT" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")
');
END;
/

--changeSet MTX-1252:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCINVMAP_ARCFAULT" ON "ARC_FAULT" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet MTX-1252:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_FAULT" add Constraint "FK_ARCINVMAP_ARCFAULT" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")
');
END;
/

--changeSet MTX-1252:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_FAULT_USAGE" (
   "ID" Raw(16) NOT NULL ,
   "FAULT_ID" Raw(16) NOT NULL ,
   "PARAMETER" Varchar2 (80),
   "TSN_QT" Float,
   "TSO_QT" Float,
   "TSI_QT" Float,
   "WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_FAULT_USAGE" primary key ("ID") 
)   
');
END;
/

--changeSet MTX-1252:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCFAULT_ARCFAULTUSAGE" ON "ARC_FAULT_USAGE" ("FAULT_ID")
');
END;
/

--changeSet MTX-1252:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_FAULT_USAGE" add Constraint "FK_ARCFAULT_ARCFAULTUSAGE" foreign key ("FAULT_ID") references "ARC_FAULT" ("ID")
');
END;
/

--changeSet MTX-1252:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_FAULT_DUE_VALUE" (
   "ID" Raw(16) NOT NULL ,
   "FAULT_ID" Raw(16) NOT NULL ,
   "PARAMETER" Varchar2 (80),
   "VALUE" Decimal(10,2),
   "EXTENSION_BOOL" Number(1,0) NOT NULL ,
   "WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_FAULT_DUE_VALUE" primary key ("ID") 
)    
');
END;
/

--changeSet MTX-1252:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCFAULT_ARCFAULTDUEVALUE" ON "ARC_FAULT_DUE_VALUE" ("FAULT_ID")
');
END;
/

--changeSet MTX-1252:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_FAULT_DUE_VALUE" add Constraint "FK_ARCFAULT_ARCFAULTDUEVALUE" foreign key ("FAULT_ID") references "ARC_FAULT" ("ID")
');
END;
/