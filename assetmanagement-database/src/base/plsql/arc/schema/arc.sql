--liquibase formatted sql


--changeSet arc:1 stripComments:false
-- Create Types section
-- Create Tables section
/*
Created		4/30/2013
Modified		1/29/2015
Project		
Model		
Company		
Author		
Version		
Database		Oracle 10g 
*/
Create table "ARC_MESSAGE" (
	"ID" Raw(16) NOT NULL ,
	"EXTERNAL_REF" Varchar2 (200),
	"STATUS_CD" Varchar2 (50),
	"DESC_SDESC" Varchar2 (4000 CHAR),
	"NOTE" Varchar2 (4000),
	"RECEIVED_DT" Date,
	"PROCESSED_DT" Date,
 Constraint "pk_ARC_MESSAGE" primary key ("ID")
)
;

--changeSet arc:2 stripComments:false
Create table "ARC_RESULT" (
	"ID" Raw(16) NOT NULL ,
	"MSG_ID" Raw(16) NOT NULL ,
	"ASSET_ID" Raw(16),
	"ENTITY_TYPE" Varchar2 (20),
	"ENTITY_KEY" Raw(16),
	"SEVERITY_CD" Varchar2 (8),
	"RESULT_LDESC" Varchar2 (4000 CHAR),
	"RESULT_DT" Timestamp(6),
 Constraint "pk_ARC_RESULT" primary key ("ID")
)
;

--changeSet arc:3 stripComments:false
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
;

--changeSet arc:4 stripComments:false
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
;

--changeSet arc:5 stripComments:false
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
;

--changeSet arc:6 stripComments:false
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
;

--changeSet arc:7 stripComments:false
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
;

--changeSet arc:8 stripComments:false
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
;

--changeSet arc:9 stripComments:false
Create table "ARC_TASK_USAGE" (
	"ID" Raw(16) NOT NULL ,
	"TASK_ID" Raw(16) NOT NULL ,
	"PARAMETER_SDESC" Varchar2 (80),
	"TSN_QT" Float,
	"TSO_QT" Float,
	"TSI_QT" Float,
 Constraint "pk_ARC_TASK_USAGE" primary key ("ID")
)
;

--changeSet arc:10 stripComments:false
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
	"FAIL_DEFER_CD" Varchar2 (80),
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
	"EX_FAIL_TYPE_CD" Varchar2 (8),
	"EX_FAIL_SEV_CD" Varchar2 (8),
 Constraint "pk_ARC_FAULT" primary key ("ID")
)
;

--changeSet arc:11 stripComments:false
Create table "ARC_FAULT_USAGE" (
	"ID" Raw(16) NOT NULL ,
	"FAULT_ID" Raw(16) NOT NULL ,
	"PARAMETER" Varchar2 (80),
	"TSN_QT" Float,
	"WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_FAULT_USAGE" primary key ("ID")
)
;

--changeSet arc:12 stripComments:false
Create table "ARC_FAULT_DUE_VALUE" (
	"ID" Raw(16) NOT NULL ,
	"FAULT_ID" Raw(16) NOT NULL ,
	"PARAMETER" Varchar2 (80),
	"VALUE" Decimal(10,2),
	"EXTENSION_BOOL" Number(1,0) NOT NULL ,
	"WORKFLOW_CD" Varchar2 (8),
 Constraint "pk_ARC_FAULT_DUE_VALUE" primary key ("ID")
)
;

--changeSet arc:13 stripComments:false
-- Create Alternate keys section
-- Create Indexes section
-- Create Foreign keys section
Create Index "IX_FK_ARCMESSAGE_ARCRESULT" ON "ARC_RESULT" ("MSG_ID")
;

--changeSet arc:14 stripComments:false
Alter table "ARC_RESULT" add Constraint "FK_ARCMESSAGE_ARCRESULT" foreign key ("MSG_ID") references "ARC_MESSAGE" ("ID") 
;

--changeSet arc:15 stripComments:false
Create Index "IX_FK_ARCMESSAGE_ARCASSET" ON "ARC_ASSET" ("MSG_ID")
;

--changeSet arc:16 stripComments:false
Alter table "ARC_ASSET" add Constraint "FK_ARCMESSAGE_ARCASSET" foreign key ("MSG_ID") references "ARC_MESSAGE" ("ID") 
;

--changeSet arc:17 stripComments:false
Create Index "IX_FK_ARCASSET_ARCRESULT" ON "ARC_RESULT" ("ASSET_ID")
;

--changeSet arc:18 stripComments:false
Alter table "ARC_RESULT" add Constraint "FK_ARCASSET_ARCRESULT" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID") 
;

--changeSet arc:19 stripComments:false
Create Index "IX_FK_ARCASSET_ARCCONFIG" ON "ARC_CONFIG" ("ASSET_ID")
;

--changeSet arc:20 stripComments:false
Alter table "ARC_CONFIG" add Constraint "FK_ARCASSET_ARCCONFIG" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID") 
;

--changeSet arc:21 stripComments:false
Create Index "IX_FK_ARCASSET_ARCINVDETAILS" ON "ARC_INV_DETAILS" ("ASSET_ID")
;

--changeSet arc:22 stripComments:false
Alter table "ARC_INV_DETAILS" add Constraint "FK_ARCASSET_ARCINVDETAILS" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID") 
;

--changeSet arc:23 stripComments:false
Create Index "IX_FK_ARCASSET_ARCINVUSAGE" ON "ARC_INV_USAGE" ("ASSET_ID")
;

--changeSet arc:24 stripComments:false
Alter table "ARC_INV_USAGE" add Constraint "FK_ARCASSET_ARCINVUSAGE" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID") 
;

--changeSet arc:25 stripComments:false
Create Index "IX_FK_ARCASSET_ARCINVMAP" ON "ARC_INV_MAP" ("ASSET_ID")
;

--changeSet arc:26 stripComments:false
Alter table "ARC_INV_MAP" add Constraint "FK_ARCASSET_ARCINVMAP" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID") 
;

--changeSet arc:27 stripComments:false
Create Index "IX_ARCASSET_ARCTASK" ON "ARC_TASK" ("ASSET_ID")
;

--changeSet arc:28 stripComments:false
Alter table "ARC_TASK" add Constraint "FK_ARCASSET_ARCTASK" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID") 
;

--changeSet arc:29 stripComments:false
Create Index "IX_ARCASSET_ARCFAULT" ON "ARC_FAULT" ("ASSET_ID")
;

--changeSet arc:30 stripComments:false
Alter table "ARC_FAULT" add Constraint "FK_ARCASSET_ARCFAULT" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID") 
;

--changeSet arc:31 stripComments:false
Create Index "IX_FK_ARCINVMAP_ARCCONFIG" ON "ARC_CONFIG" ("INVENTORY_MAP_ID")
;

--changeSet arc:32 stripComments:false
Alter table "ARC_CONFIG" add Constraint "FK_ARCINVMAP_ARCCONFIG" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID") 
;

--changeSet arc:33 stripComments:false
Create Index "IX_FK_ARCINVMAP_ARCINVDETAILS" ON "ARC_INV_DETAILS" ("INVENTORY_MAP_ID")
;

--changeSet arc:34 stripComments:false
Alter table "ARC_INV_DETAILS" add Constraint "FK_ARCINVMAP_ARCINVDETAILS" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID") 
;

--changeSet arc:35 stripComments:false
Create Index "IX_FK_ARCINVMAP_NHARCCONFIG" ON "ARC_CONFIG" ("NH_INVENTORY_MAP_ID")
;

--changeSet arc:36 stripComments:false
Alter table "ARC_CONFIG" add Constraint "FK_ARCINVMAP_NHARCCONFIG" foreign key ("NH_INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID") 
;

--changeSet arc:37 stripComments:false
Create Index "IX_ARCINVMAP_ARCTASK" ON "ARC_TASK" ("INVENTORY_MAP_ID")
;

--changeSet arc:38 stripComments:false
Alter table "ARC_TASK" add Constraint "FK_ARCINVMAP_ARCTASK" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID") 
;

--changeSet arc:39 stripComments:false
Create Index "IX_FK_ARCINVMAP_ARCINVUSAGE" ON "ARC_INV_USAGE" ("INVENTORY_MAP_ID")
;

--changeSet arc:40 stripComments:false
Alter table "ARC_INV_USAGE" add Constraint "FK_ARCINVMAP_ARCINVUSAGE" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID") 
;

--changeSet arc:41 stripComments:false
Create Index "IX_ARCINVMAP_ARCFAULT" ON "ARC_FAULT" ("INVENTORY_MAP_ID")
;

--changeSet arc:42 stripComments:false
Alter table "ARC_FAULT" add Constraint "FK_ARCINVMAP_ARCFAULT" foreign key ("INVENTORY_MAP_ID") references "ARC_INV_MAP" ("ID") 
;

--changeSet arc:43 stripComments:false
Create Index "IX_ARCTASK_ARCTASKUSAGE" ON "ARC_TASK_USAGE" ("TASK_ID")
;

--changeSet arc:44 stripComments:false
Alter table "ARC_TASK_USAGE" add Constraint "FK_ARCTASK_ARCTASKUSAGE" foreign key ("TASK_ID") references "ARC_TASK" ("ID") 
;

--changeSet arc:45 stripComments:false
Create Index "IX_ARCFAULT_ARCFAULTUSAGE" ON "ARC_FAULT_USAGE" ("FAULT_ID")
;

--changeSet arc:46 stripComments:false
Alter table "ARC_FAULT_USAGE" add Constraint "FK_ARCFAULT_ARCFAULTUSAGE" foreign key ("FAULT_ID") references "ARC_FAULT" ("ID") 
;

--changeSet arc:47 stripComments:false
Create Index "IX_ARCFAULT_ARCFAULTDUEVALUE" ON "ARC_FAULT_DUE_VALUE" ("FAULT_ID")
;

--changeSet arc:48 stripComments:false
Alter table "ARC_FAULT_DUE_VALUE" add Constraint "FK_ARCFAULT_ARCFAULTDUEVALUE" foreign key ("FAULT_ID") references "ARC_FAULT" ("ID") 
;