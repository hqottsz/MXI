--liquibase formatted sql


--changeSet DEV-2170:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_MESSAGE" (
   "MSG_ID" Raw(16) NOT NULL DEFERRABLE ,
   "ASB_MSG_ID" Raw(16),
   "STATUS_CD" Varchar2 (50),
   "DESC_SDESC" Varchar2 (80),
   "NOTE" Varchar2 (4000),
   "RECEIVED_DT" Date,
   "PROCESSED_DT" Date,
 Constraint "pk_ARC_MESSAGE" primary key ("MSG_ID") 
) 
');
END;
/

--changeSet DEV-2170:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_RESULT" (
   "RESULT_ID" Raw(16) NOT NULL DEFERRABLE ,
   "MSG_ID" Raw(16) NOT NULL DEFERRABLE ,
   "SEVERITY_CD" Varchar2 (8),
   "RESULT_LDESC" Varchar2 (4000),
   "ASSET_ID" Raw(16),
 Constraint "pk_ARC_RESULT" primary key ("RESULT_ID","MSG_ID") 
) 
');
END;
/

--changeSet DEV-2170:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_ASSET" (
   "ASSET_ID" Raw(16) NOT NULL DEFERRABLE ,
   "MSG_ID" Raw(16) NOT NULL DEFERRABLE ,
   "PART_NO_OEM" Varchar2 (40),
   "MANUFACT_CD" Varchar2 (16),
   "SERIAL_NO_OEM" Varchar2 (40),
   "RECEIVED_DATE" Date,
   "AC_REG_CD" Varchar2 (10),
   "MANUFACT_DATE" Date,
   "ACTION_CD" Varchar2 (50),
   "INV_CLASS_CD" Varchar2 (8),
   "OWNER_CD" Varchar2 (16),
   "LOC_CD" Varchar2 (2000),
 Constraint "pk_ARC_ASSET" primary key ("ASSET_ID","MSG_ID") 
) 
');
END;
/

--changeSet DEV-2170:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCMESSAGE_ARCRESULT" ON "ARC_RESULT" ("MSG_ID")
');
END;
/

--changeSet DEV-2170:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_RESULT" add Constraint "FK_ARCMESSAGE_ARCRESULT" foreign key ("MSG_ID") references "ARC_MESSAGE" ("MSG_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2170:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_ARCMESSAGE_ARCASSET" ON "ARC_ASSET" ("MSG_ID")
');
END;
/

--changeSet DEV-2170:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_ASSET" add Constraint "FK_ARCMESSAGE_ARCASSET" foreign key ("MSG_ID") references "ARC_MESSAGE" ("MSG_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2170:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'ACTION_GETPART',
      'Permission to allow get part call',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ASSET',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet DEV-2170:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_CREATE_ACFT_REQUEST',
      'Permission to allow create aircraft call',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ASSET',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet DEV-2170:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_CREATE_SERIALIZED_REQUEST',
      'Permission to allow create assembly, tracked and serialized inventory call',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - ASSET',
      '8.1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/