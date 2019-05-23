--liquibase formatted sql


--changeSet MTX-1089:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop the foreign key constraints from the ARC tables
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_ASSET', 'FK_ARCMESSAGE_ARCASSET');
END;
/

--changeSet MTX-1089:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_CONFIG', 'FK_ARCASSET_ARCCONFIG');
END;
/

--changeSet MTX-1089:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_CONFIG', 'FK_ARCINVMAP_ARCCONFIG');
END;
/

--changeSet MTX-1089:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_CONFIG', 'FK_ARCINVMAP_NHARCCONFIG');
END;
/

--changeSet MTX-1089:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_INV_DETAILS', 'FK_ARCASSET_ARCINVDETAILS');
END;
/

--changeSet MTX-1089:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_INV_DETAILS', 'FK_ARCINVMAP_ARCINVDETAILS');
END;
/

--changeSet MTX-1089:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_INV_MAP', 'FK_ARCASSET_ARCINVMAP');
END;
/

--changeSet MTX-1089:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_INV_USAGE', 'FK_ARCASSET_ARCINVUSAGE');
END;
/

--changeSet MTX-1089:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_INV_USAGE', 'FK_ARCINVMAP_ARCINVUSAGE');
END;
/

--changeSet MTX-1089:10 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_RESULT', 'FK_ARCASSET_ARCRESULT');
END;
/

--changeSet MTX-1089:11 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_RESULT', 'FK_ARCMESSAGE_ARCRESULT');
END;
/

--changeSet MTX-1089:12 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_TASK', 'FK_ARCASSET_ARCTASK');
END;
/

--changeSet MTX-1089:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_TASK', 'FK_ARCINVMAP_ARCTASK');
END;
/

--changeSet MTX-1089:14 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('ARC_TASK_USAGE', 'FK_ARCTASK_ARCTASKUSAGE');
END;
/

--changeSet MTX-1089:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop the ARC tables
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('ARC_ASSET');
END;
/

--changeSet MTX-1089:16 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('ARC_CONFIG');
END;
/

--changeSet MTX-1089:17 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('ARC_INV_DETAILS');
END;
/

--changeSet MTX-1089:18 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('ARC_INV_MAP');
END;
/

--changeSet MTX-1089:19 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('ARC_INV_USAGE');
END;
/

--changeSet MTX-1089:20 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('ARC_MESSAGE');
END;
/

--changeSet MTX-1089:21 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('ARC_RESULT');
END;
/

--changeSet MTX-1089:22 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('ARC_TASK');
END;
/

--changeSet MTX-1089:23 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_DROP('ARC_TASK_USAGE');
END;
/

--changeSet MTX-1089:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create the ARC tables
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_MESSAGE" (
	"ID" Raw(16) NOT NULL ,
	"EXTERNAL_REF" Varchar2 (200),
	"STATUS_CD" Varchar2 (50),
	"DESC_SDESC" Varchar2 (4000),
	"NOTE" Varchar2 (4000),
	"RECEIVED_DT" Date,
	"PROCESSED_DT" Date,
 Constraint "pk_ARC_MESSAGE" primary key ("ID") 
) 
');
END;
/

--changeSet MTX-1089:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_RESULT" (
	"ID" Raw(16) NOT NULL ,
	"MSG_ID" Raw(16) NOT NULL ,
	"ASSET_ID" Raw(16),
	"ENTITY_TYPE" Varchar2 (20),
	"ENTITY_KEY" Raw(16),
	"SEVERITY_CD" Varchar2 (8),
	"RESULT_LDESC" Varchar2 (4000),
	"RESULT_DT" Timestamp(6),
 Constraint "pk_ARC_RESULT" primary key ("ID") 
) 
');
END;
/

--changeSet MTX-1089:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_ASSET" (
	"ID" Raw(16) NOT NULL ,
	"MSG_ID" Raw(16) NOT NULL ,
	"INVENTORY_MAP_ID" Raw(16),
	"AC_REG_CD" Varchar2 (10),
	"CAPABILITY_CD" Varchar2 (8),
	"COUNTRY_CD" Varchar2 (8),
	"REG_BODY_CD" Varchar2 (8),
	"VAR_NO_OEM" Varchar2 (40),
	"LINE_NO_OEM" Varchar2 (40),
	"PART_NO_ID" Varchar2 (32),
	"INV_CLASS_CD" Varchar2 (8),
	"WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_ASSET" primary key ("ID") 
) 
');
END;
/

--changeSet MTX-1089:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_CONFIG" (
	"ID" Raw(16) NOT NULL ,
	"ASSET_ID" Raw(16) NOT NULL ,
	"INVENTORY_MAP_ID" Raw(16),
	"NH_INVENTORY_MAP_ID" Raw(16),
	"PART_GROUP_CD" Varchar2 (50),
	"POS_CD" Varchar2 (200),
	"WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_CONFIG" primary key ("ID") 
) 
');
END;
/

--changeSet MTX-1089:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_INV_DETAILS" (
	"ID" Raw(16) NOT NULL ,
	"ASSET_ID" Raw(16) NOT NULL ,
	"INVENTORY_MAP_ID" Raw(16) NOT NULL ,
	"MANUFACT_DT" Date,
	"RECEIVED_DT" Date,
	"OWNER_CD" Varchar2 (16),
	"APPL_EFF_CD" Varchar2 (8),
	"RELEASE_NUMBER_SDESC" Varchar2 (80),
	"OPERATOR_CD" Varchar2 (8),
	"WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_INV_DETAILS" primary key ("ID") 
) 
');
END;
/

--changeSet MTX-1089:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_INV_USAGE" (
	"ID" Raw(16) NOT NULL ,
	"ASSET_ID" Raw(16) NOT NULL ,
	"INVENTORY_MAP_ID" Raw(16) NOT NULL ,
	"PARAMETER" Varchar2 (80),
	"TSN_QT" Float,
	"TSO_QT" Float,
	"TSI_QT" Float,
	"WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_INV_USAGE" primary key ("ID") 
) 
');
END;
/

--changeSet MTX-1089:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_INV_MAP" (
	"ID" Raw(16) NOT NULL ,
	"ASSET_ID" Raw(16) NOT NULL ,
	"MANUFACT_CD" Varchar2 (40) NOT NULL ,
	"PART_NO_OEM" Varchar2 (40) NOT NULL ,
	"SERIAL_NO_OEM" Varchar2 (40) NOT NULL ,
	"INVENTORY_ID" Varchar2 (32),
	"INV_CLASS_CD" Varchar2 (8),
	"WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_INV_MAP" primary key ("ID") 
) 
');
END;
/

--changeSet MTX-1089:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_TASK" (
	"ID" Raw(16) NOT NULL ,
	"INVENTORY_MAP_ID" Raw(16) NOT NULL ,
	"ASSET_ID" Raw(16) NOT NULL ,
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

--changeSet MTX-1089:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_TASK_USAGE" (
	"ID" Raw(16) NOT NULL ,
	"TASK_ID" Raw(16) NOT NULL ,
	"PARAMETER_SDESC" Varchar2 (80),
	"TSN_QT" Float,
	"TSO_QT" Float,
	"TSI_QT" Float,
 Constraint "pk_ARC_TASK_USAGE" primary key ("ID") 
)
');
END;
/

--changeSet MTX-1089:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create the ARC foreign keys and indexes
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCMESSAGE_ARCRESULT" ON "ARC_RESULT" ("MSG_ID")
');
END;
/

--changeSet MTX-1089:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_RESULT" add Constraint "FK_ARCMESSAGE_ARCRESULT" foreign key ("MSG_ID") references "ARC_MESSAGE" ("ID")
');
END;
/

--changeSet MTX-1089:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCMESSAGE_ARCASSET" ON "ARC_ASSET" ("MSG_ID")
');
END;
/

--changeSet MTX-1089:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_ASSET" add Constraint "FK_ARCMESSAGE_ARCASSET" foreign key ("MSG_ID") references "ARC_MESSAGE" ("ID")
');
END;
/

--changeSet MTX-1089:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCRESULT" ON "ARC_RESULT" ("ASSET_ID")
');
END;
/

--changeSet MTX-1089:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_RESULT" add Constraint "FK_ARCASSET_ARCRESULT" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")
');
END;
/

--changeSet MTX-1089:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCCONFIG" ON "ARC_CONFIG" ("ASSET_ID")
');
END;
/

--changeSet MTX-1089:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_CONFIG" add Constraint "FK_ARCASSET_ARCCONFIG" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")
');
END;
/

--changeSet MTX-1089:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCINVDETAILS" ON "ARC_INV_DETAILS" ("ASSET_ID")
');
END;
/

--changeSet MTX-1089:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_DETAILS" add Constraint "FK_ARCASSET_ARCINVDETAILS" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")
');
END;
/

--changeSet MTX-1089:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCINVUSAGE" ON "ARC_INV_USAGE" ("ASSET_ID")
');
END;
/

--changeSet MTX-1089:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_USAGE" add Constraint "FK_ARCASSET_ARCINVUSAGE" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")
');
END;
/

--changeSet MTX-1089:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCINVMAP" ON "ARC_INV_MAP" ("ASSET_ID")
');
END;
/

--changeSet MTX-1089:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_MAP" add Constraint "FK_ARCASSET_ARCINVMAP" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")
');
END;
/

--changeSet MTX-1089:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCASSET_ARCTASK" ON "ARC_TASK" ("ASSET_ID")
');
END;
/

--changeSet MTX-1089:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_TASK" add Constraint "FK_ARCASSET_ARCTASK" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")
');
END;
/

--changeSet MTX-1089:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCINVMAP_ARCCONFIG" ON "ARC_CONFIG" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet MTX-1089:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_CONFIG" add Constraint "FK_ARCINVMAP_ARCCONFIG" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")
');
END;
/

--changeSet MTX-1089:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCINVMAP_ARCINVDETAILS" ON "ARC_INV_DETAILS" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet MTX-1089:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_DETAILS" add Constraint "FK_ARCINVMAP_ARCINVDETAILS" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")
');
END;
/

--changeSet MTX-1089:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCINVMAP_NHARCCONFIG" ON "ARC_CONFIG" ("NH_INVENTORY_MAP_ID")
');
END;
/

--changeSet MTX-1089:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_CONFIG" add Constraint "FK_ARCINVMAP_NHARCCONFIG" foreign key ("NH_INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")
');
END;
/

--changeSet MTX-1089:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCINVMAP_ARCTASK" ON "ARC_TASK" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet MTX-1089:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_TASK" add Constraint "FK_ARCINVMAP_ARCTASK" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")
');
END;
/

--changeSet MTX-1089:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCINVMAP_ARCINVUSAGE" ON "ARC_INV_USAGE" ("INVENTORY_MAP_ID")
');
END;
/

--changeSet MTX-1089:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_USAGE" add Constraint "FK_ARCINVMAP_ARCINVUSAGE" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID")
');
END;
/

--changeSet MTX-1089:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCTASK_ARCTASKUSAGE" ON "ARC_TASK_USAGE" ("TASK_ID")
');
END;
/

--changeSet MTX-1089:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_TASK_USAGE" add Constraint "FK_ARCTASK_ARCTASKUSAGE" foreign key ("TASK_ID") references "ARC_TASK" ("ID")
');
END;
/