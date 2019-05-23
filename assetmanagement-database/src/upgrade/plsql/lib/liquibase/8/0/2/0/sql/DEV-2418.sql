--liquibase formatted sql


--changeSet DEV-2418:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   lExists NUMBER;
BEGIN
   SELECT 
      CASE
         WHEN EXISTS(SELECT 1 FROM user_tables WHERE table_name = 'ARC_CONFIG')
            THEN 1
         ELSE 0
      END CASE
         INTO lExists FROM dual;
   IF lExists != 1 THEN
      utl_migr_schema_pkg.table_drop('arc_result');
      utl_migr_schema_pkg.table_drop('arc_asset');
      utl_migr_schema_pkg.table_drop('arc_message');
   END IF;
END;
/

--changeSet DEV-2418:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_MESSAGE" (
	"ID" Raw(16) NOT NULL DEFERRABLE ,
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

--changeSet DEV-2418:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_RESULT" (
	"ID" Raw(16) NOT NULL DEFERRABLE ,
	"MSG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"ASSET_ID" Raw(16),
	"SEVERITY_CD" Varchar2 (8),
	"RESULT_LDESC" Varchar2 (4000),
 Constraint "pk_ARC_RESULT" primary key ("ID") 
) 
');
END;
/

--changeSet DEV-2418:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_ASSET" (
	"ID" Raw(16) NOT NULL DEFERRABLE ,
	"MSG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"PART_NO_OEM" Varchar2 (40),
	"MANUFACT_CD" Varchar2 (16),
	"SERIAL_NO_OEM" Varchar2 (40),
	"AC_REG_CD" Varchar2 (10),
	"CAPABILITY_CD" Varchar2 (8),
	"COUNTRY_CD" Varchar2 (8),
	"REG_BODY_CD" Varchar2 (8),
	"VAR_NO_OEM" Varchar2 (40),
	"LINE_NO_OEM" Varchar2 (40),
	"INVENTORY_ID" Varchar2 (32),
	"PART_NO_ID" Varchar2 (32),
	"INV_CLASS_CD" Varchar2 (8),
 Constraint "pk_ARC_ASSET" primary key ("ID") 
) 
');
END;
/

--changeSet DEV-2418:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_CONFIG" (
	"ID" Raw(16) NOT NULL DEFERRABLE ,
	"ASSET_ID" Raw(16) NOT NULL DEFERRABLE ,
	"PART_GROUP_CD" Varchar2 (50),
	"POS_CD" Varchar2 (200),
	"NH_PART_NO_OEM" Varchar2 (40),
	"NH_MANUFACT_CD" Varchar2 (16),
	"NH_SERIAL_NO_OEM" Varchar2 (40),
	"PART_NO_OEM" Varchar2 (40),
	"MANUFACT_CD" Varchar2 (16),
	"SERIAL_NO_OEM" Varchar2 (40),
 Constraint "pk_ARC_CONFIG" primary key ("ID") 
)
');
END;
/

--changeSet DEV-2418:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ARC_INV_DETAILS" (
	"ID" Raw(16) NOT NULL DEFERRABLE ,
	"ASSET_ID" Raw(16) NOT NULL DEFERRABLE ,
	"PART_NO_OEM" Varchar2 (40),
	"MANUFACT_CD" Varchar2 (16),
	"SERIAL_NO_OEM" Varchar2 (40),
	"MANUFACT_DT" Date,
	"RECEIVED_DT" Date,
	"OWNER_CD" Varchar2 (16),
	"APPL_EFF_CD" Varchar2 (8),
	"RELEASE_NUMBER_SDESC" Varchar2 (80),
	"OPERATOR_CD" Varchar2 (8),
 Constraint "pk_ARC_INV_DETAILS" primary key ("ID") 
) 
');
END;
/

--changeSet DEV-2418:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create Foreign keys section
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCMESSAGE_ARCRESULT" ON "ARC_RESULT" ("MSG_ID")
');
END;
/

--changeSet DEV-2418:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCMESSAGE_ARCASSET" ON "ARC_ASSET" ("MSG_ID")
');
END;
/

--changeSet DEV-2418:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCRESULT" ON "ARC_RESULT" ("ASSET_ID")
');
END;
/

--changeSet DEV-2418:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCCONFIG" ON "ARC_CONFIG" ("ASSET_ID")
');
END;
/

--changeSet DEV-2418:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
Create Index "IX_FK_ARCASSET_ARCINVDETAILS" ON "ARC_INV_DETAILS" ("ASSET_ID")
');
END;
/

--changeSet DEV-2418:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_RESULT" add Constraint "FK_ARCMESSAGE_ARCRESULT" foreign key ("MSG_ID") references "ARC_MESSAGE" ("ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2418:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_ASSET" add Constraint "FK_ARCMESSAGE_ARCASSET" foreign key ("MSG_ID") references "ARC_MESSAGE" ("ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2418:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_RESULT" add Constraint "FK_ARCASSET_ARCRESULT" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2418:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_CONFIG" add Constraint "FK_ARCASSET_ARCCONFIG" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")  DEFERRABLE
');
END;
/

--changeSet DEV-2418:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ARC_INV_DETAILS" add Constraint "FK_ARCASSET_ARCINVDETAILS" foreign key ("ASSET_ID") references "ARC_ASSET" ("ID")  DEFERRABLE
');
END;
/