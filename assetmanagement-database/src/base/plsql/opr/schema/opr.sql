--liquibase formatted sql


--changeSet opr:1 stripComments:false
-- Create Tables section
/*
Created		7/25/2013
Modified		2/12/2014
Project		SMOS
Model		Maintenix
Company		Mxi Technologies
Author		
Version		1306
Database		Oracle 10g 
*/
Create table OPR_INDUCTION_PLAN (
	ID Raw(16) NOT NULL ,
	ASSMBL_CD Varchar2 (8) NOT NULL ,
	MANUFACT_CD Varchar2 (16) NOT NULL ,
	PART_NO_OEM Varchar2 (40) NOT NULL ,
	AC_REG_CD Varchar2 (16) NOT NULL ,
	SERIAL_NO Varchar2 (40),
	VAR_NO_OEM Varchar2 (40),
	LINE_NO Varchar2 (40),
	APPL_EFF_CD Varchar2 (8),
	REG_BODY_CD Varchar2 (8) NOT NULL ,
	OPERATOR_CD Varchar2 (8),
	COUNTRY_CD Varchar2 (8),
	SPREADSHEET_ID Raw(16) NOT NULL ,
	AIRPORT_LOC_CD Varchar2 (2000),
	MANUFACTURED_DT Date,
	RECEIVED_DT Date,
	USAGE_HOURS Float,
	USAGE_CYCLES Float,
	LAST_EDIT_DT Date NOT NULL ,
 Constraint PK_OPR_INDUCTION_PLAN primary key (ID)
)
;

--changeSet opr:2 stripComments:false
Create table OPR_BLOB (
	ID Raw(16) NOT NULL ,
	BLOB Blob NOT NULL ,
 Constraint PK_OPR_BLOB primary key (ID)
)
;

--changeSet opr:3 stripComments:false
Create table OPR_ASSET_AD_STATUS (
	ID Raw(16) NOT NULL ,
	AD_ID Raw(16),
	ASSET_ID Raw(16),
	AD_STATUS_CD Varchar2 (10),
	STATUS_NOTES Varchar2 (4000),
	COMPLETION_DT Date,
 Constraint PK_OPR_ASSET_AD_STATUS primary key (ID)
)
;

--changeSet opr:4 stripComments:false
Create table OPR_AD_STATUS (
	AD_STATUS_CD Varchar2 (10) NOT NULL ,
	DISPLAY_CODE Varchar2 (10) NOT NULL ,
	DISPLAY_NAME Varchar2 (80) NOT NULL ,
	DISPLAY_DESC Varchar2 (4000),
	DISPLAY_ORD Number(4,0) NOT NULL ,
 Constraint PK_OPR_AD_STATUS primary key (AD_STATUS_CD)
)
;

--changeSet opr:5 stripComments:false
Create table OPR_ASSET_SB_STATUS (
	ID Raw(16) NOT NULL ,
	SB_ID Raw(16),
	ASSET_ID Raw(16),
	SB_STATUS_CD Varchar2 (10),
	STATUS_NOTES Varchar2 (4000),
	COMPLETION_DT Date,
 Constraint PK_OPR_ASSET_SB_STATUS primary key (ID)
)
;

--changeSet opr:6 stripComments:false
Create table OPR_SB_STATUS (
	SB_STATUS_CD Varchar2 (10) NOT NULL ,
	DISPLAY_CODE Varchar2 (10) NOT NULL ,
	DISPLAY_NAME Varchar2 (80) NOT NULL ,
	DISPLAY_DESC Varchar2 (4000),
	DISPLAY_ORD Number(4,0) NOT NULL ,
 Constraint PK_OPR_SB_STATUS primary key (SB_STATUS_CD)
)
;

--changeSet opr:7 stripComments:false
-- Create Alternate keys section
-- Create Indexes section
-- Create Foreign keys section
Create Index IX_ORG_INDUCTION_PLAN ON OPR_INDUCTION_PLAN (SPREADSHEET_ID)
;

--changeSet opr:8 stripComments:false
Alter table OPR_INDUCTION_PLAN add Constraint FK_OPR_BLOB_INDUCTION_PLAN foreign key (SPREADSHEET_ID) references OPR_BLOB (ID)  DEFERRABLE
;

--changeSet opr:9 stripComments:false
Create Index IX_OPRADSTS_OPRASSETADSTS ON OPR_ASSET_AD_STATUS (AD_STATUS_CD)
;

--changeSet opr:10 stripComments:false
Alter table OPR_ASSET_AD_STATUS add Constraint FK_OPRADSTATUS_OPRASSETADSTATU foreign key (AD_STATUS_CD) references OPR_AD_STATUS (AD_STATUS_CD)  DEFERRABLE
;

--changeSet opr:11 stripComments:false
Create Index IX_OPRSBSTS_OPRASSETSBSTS ON OPR_ASSET_SB_STATUS (SB_STATUS_CD)
;

--changeSet opr:12 stripComments:false
Alter table OPR_ASSET_SB_STATUS add Constraint FK_OPRSBSTATUS_OPRASSETSBSTATU foreign key (SB_STATUS_CD) references OPR_SB_STATUS (SB_STATUS_CD)  DEFERRABLE
;

--changeSet opr:13 stripComments:false
-- Create Table comments section
Comment on table OPR_INDUCTION_PLAN is 'This table contains information for an induction plan'
;

--changeSet opr:14 stripComments:false
Comment on table OPR_BLOB is 'This table contains blobs used by other entities to ensure that they can be easily stored in secondary storage.'
;