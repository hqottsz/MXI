--liquibase formatted sql


--changeSet ARC-DEV-2467:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_TASK" (
	"ID" Raw(16) NOT NULL DEFERRABLE ,
	"INVENTORY_MAP_ID" Raw(16) NOT NULL DEFERRABLE ,
	"ASSET_ID" Raw(16) NOT NULL DEFERRABLE ,
	"TASK_CD" Varchar2 (200),
	"ACTION_LDESC" Varchar2 (4000),
	"COMPLETION_DT" Date,
	"EXT_KEY_SDESC" Varchar2 (80),
	"DOC_REF_SDESC" Varchar2 (80),
	"WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_TASK" primary key ("ID") 
) 
');
END;
/

--changeSet ARC-DEV-2467:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_TASK_USAGE" (
	"ID" Raw(16) NOT NULL DEFERRABLE ,
	"TASK_ID" Raw(16) NOT NULL DEFERRABLE ,
	"PARAMETER_SDESC" Varchar2 (80),
	"TSN_QT" Float,
	"TSO_QT" Float,
	"TSI_QT" Float,
 Constraint "pk_ARC_TASK_USAGE" primary key ("ID") 
) 
');
END;
/

--changeSet ARC-DEV-2467:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCASSET_ARCTASK" ON "ARC_TASK" ("ASSET_ID")
');
END;
/

--changeSet ARC-DEV-2467:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_TASK" add Constraint "FK_ARCASSET_ARCTASK" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")  DEFERRABLE
');
END;
/

--changeSet ARC-DEV-2467:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCINVMAP_ARCTASK" ON "ARC_TASK" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet ARC-DEV-2467:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_TASK" add Constraint "FK_ARCINVMAP_ARCTASK" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")  DEFERRABLE
');
END;
/

--changeSet ARC-DEV-2467:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCTASK_ARCTASKUSAGE" ON "ARC_TASK_USAGE" ("TASK_ID")
');
END;
/

--changeSet ARC-DEV-2467:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_TASK_USAGE" add Constraint "FK_ARCTASK_ARCTASKUSAGE" foreign key ("TASK_ID") references "ARC_TASK" ("ID")  DEFERRABLE
');
END;
/