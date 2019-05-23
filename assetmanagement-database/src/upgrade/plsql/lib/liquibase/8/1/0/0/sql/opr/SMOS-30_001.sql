--liquibase formatted sql


--changeSet SMOS-30_001:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- AD Solution
BEGIN
   utl_migr_schema_pkg.table_create('
Create table "OPR_AD_STATUS" (
	"AD_STATUS_CD" Varchar2 (10) NOT NULL ,
	"DISPLAY_CODE" Varchar2 (10) NOT NULL ,
	"DISPLAY_NAME" Varchar2 (80) NOT NULL ,
	"DISPLAY_DESC" Varchar2 (4000),
	"DISPLAY_ORD" Number(4,0) NOT NULL ,
 Constraint "PK_OPR_AD_STATUS" primary key ("AD_STATUS_CD") 
)');
END;
/

--changeSet SMOS-30_001:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table "OPR_ASSET_AD_STATUS" (
	"ID" Raw(16) NOT NULL ,
	"AD_ID" Raw(16),
	"ASSET_ID" Raw(16),
	"AD_STATUS_CD" Varchar2 (10) ,
	"STATUS_NOTES" Varchar2 (4000),
	"COMPLETION_DT" Date,
 Constraint "PK_OPR_ASSET_AD_STATUS" primary key ("ID") 
) 
');
END;
/

--changeSet SMOS-30_001:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
Create Index "IX_OPRADSTS_OPRASSETADSTS" ON "OPR_ASSET_AD_STATUS" ("AD_STATUS_CD")
');
END;
/

--changeSet SMOS-30_001:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "OPR_ASSET_AD_STATUS" add Constraint "FK_OPRADSTATUS_OPRASSETADSTATU" foreign key ("AD_STATUS_CD") references "OPR_AD_STATUS" ("AD_STATUS_CD")  DEFERRABLE
');
END;
/

--changeSet SMOS-30_001:5 stripComments:false
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'OPEN', 'OPEN', 'Open', 'Open', 1
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_ad_status WHERE ad_status_cd = 'OPEN');

--changeSet SMOS-30_001:6 stripComments:false
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'NA', 'N/A', 'Not Applicable', 'Not Applicable', 2
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_ad_status WHERE ad_status_cd = 'NA');

--changeSet SMOS-30_001:7 stripComments:false
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'CLOSED', 'CLOSED', 'Closed', 'Closed', 3
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_ad_status WHERE ad_status_cd = 'CLOSED');

--changeSet SMOS-30_001:8 stripComments:false
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'REPETITIVE', 'REPETITIVE', 'Repetitive', 'Repetitive', 4
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_ad_status WHERE ad_status_cd = 'REPETITIVE');

--changeSet SMOS-30_001:9 stripComments:false
INSERT INTO opr_ad_status ( ad_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'SUPERSEDED', 'SUPERSEDED', 'Superseded by another AD', 'Superseded by another AD', 5
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_ad_status WHERE ad_status_cd = 'SUPERSEDED');

--changeSet SMOS-30_001:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- SB Solution
BEGIN
   utl_migr_schema_pkg.table_create('
Create table "OPR_SB_STATUS" (
	"SB_STATUS_CD" Varchar2 (10) NOT NULL ,
	"DISPLAY_CODE" Varchar2 (10) NOT NULL ,
	"DISPLAY_NAME" Varchar2 (80) NOT NULL ,
	"DISPLAY_DESC" Varchar2 (4000),
	"DISPLAY_ORD" Number(4,0) NOT NULL ,
 Constraint "PK_OPR_SB_STATUS" primary key ("SB_STATUS_CD") 
)');
END;
/

--changeSet SMOS-30_001:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table "OPR_ASSET_SB_STATUS" (
	"ID" Raw(16) NOT NULL ,
	"SB_ID" Raw(16),
	"ASSET_ID" Raw(16),
	"SB_STATUS_CD" Varchar2 (10) ,
	"STATUS_NOTES" Varchar2 (4000),
	"COMPLETION_DT" Date,
 Constraint "PK_OPR_ASSET_SB_STATUS" primary key ("ID") 
)');
END;
/

--changeSet SMOS-30_001:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
Create Index "IX_OPRSBSTS_OPRASSETSBSTS" ON "OPR_ASSET_SB_STATUS" ("SB_STATUS_CD")
');
END;
/

--changeSet SMOS-30_001:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "OPR_ASSET_SB_STATUS" add Constraint "FK_OPRSBSTATUS_OPRASSETSBSTATU" foreign key ("SB_STATUS_CD") references "OPR_SB_STATUS" ("SB_STATUS_CD")  DEFERRABLE
');
END;
/

--changeSet SMOS-30_001:14 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'OPEN', 'OPEN', 'Open', 'Open', 1
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_sb_status WHERE sb_status_cd = 'OPEN');

--changeSet SMOS-30_001:15 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'NA', 'N/A', 'Not Applicable', 'Not Applicable', 2
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_sb_status WHERE sb_status_cd = 'NA');

--changeSet SMOS-30_001:16 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'CLOSED', 'CLOSED', 'Closed', 'Closed', 3
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_sb_status WHERE sb_status_cd = 'CLOSED');

--changeSet SMOS-30_001:17 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'REPETITIVE', 'REPETITIVE', 'Repetitive', 'Repetitive', 4
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_sb_status WHERE sb_status_cd = 'REPETITIVE');

--changeSet SMOS-30_001:18 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'SUPERSEDED', 'SUPERSEDED', 'Superseded by another SB', 'Superseded by another SB', 5
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_sb_status WHERE sb_status_cd = 'SUPERSEDED');

--changeSet SMOS-30_001:19 stripComments:false
INSERT INTO opr_sb_status ( sb_status_cd, display_code, display_name, display_desc, display_ord )
SELECT 'REJECTED', 'REJECTED', 'Rejected', 'Rejected', 5
FROM dual WHERE NOT EXISTS(SELECT 1 FROM opr_sb_status WHERE sb_status_cd = 'REJECTED');