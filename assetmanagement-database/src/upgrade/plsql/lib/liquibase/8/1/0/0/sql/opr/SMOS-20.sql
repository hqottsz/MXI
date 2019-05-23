--liquibase formatted sql


--changeSet SMOS-20:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- OPERATOR API
BEGIN
   utl_migr_data_pkg.action_parm_insert(
      'API_INDUCTION_PLAN_REQUEST',
      'Permission to allow API induction plan retrieval call',
      'TRUE/FALSE',
      'FALSE',
      1,
      'API - OPERATOR',
      '8.1-SP1',
      0,
      0,
      UTL_MIGR_DATA_PKG.DbTypeCdList('OPER')
   );
END;
/

--changeSet SMOS-20:2 stripComments:false
-- Operator
INSERT INTO UTL_MENU_ITEM (MENU_ID,TODO_LIST_ID,MENU_NAME,MENU_LINK_URL,NEW_WINDOW_BOOL,MENU_LDESC,REPL_APPROVED,Utl_Id)
SELECT
   120939,
   NULL,
   'web.menuitem.SELECT_INDUCTION_PLANS',
   '/opr/ui/induction/SelectInductionPlans.html',
   0,
   'Select Induction Plans',
   0,
   0
FROM dual WHERE
   NOT EXISTS(
      SELECT 1 FROM
         utl_menu_item
      WHERE
         menu_id = 120939
   )
;

--changeSet SMOS-20:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table "OPR_INDUCTION_PLAN" (
   "ID" Raw(16) NOT NULL ,
   "ASSMBL_CD" Varchar2 (8) NOT NULL ,
   "MANUFACT_CD" Varchar2 (16) NOT NULL ,
   "PART_NO_OEM" Varchar2 (40) NOT NULL ,
   "AC_REG_CD" Varchar2 (16) NOT NULL ,
   "SERIAL_NO" Varchar2 (40),
   "VAR_NO_OEM" Varchar2 (40),
   "LINE_NO" Varchar2 (40),
   "APPL_EFF_CD" Varchar2 (8),
   "REG_BODY_CD" Varchar2 (8) NOT NULL ,
   "OPERATOR_CD" Varchar2 (8),
   "COUNTRY_CD" Varchar2 (8),
   "SPREADSHEET_ID" Raw(16) NOT NULL ,
   "AIRPORT_LOC_CD" Varchar2 (2000),
   "MANUFACTURED_DT" Date,
   "RECEIVED_DT" Date,
   "USAGE_HOURS" Float,
   "USAGE_CYCLES" Float,
   "LAST_EDIT_DT" Date NOT NULL ,
 Constraint "PK_OPR_INDUCTION_PLAN" primary key ("ID") 
)');
END;
/

--changeSet SMOS-20:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
Create table "OPR_BLOB" (
   "ID" Raw(16) NOT NULL ,
   "BLOB" Blob NOT NULL ,
 Constraint "PK_OPR_BLOB" primary key ("ID") 
)');
END;
/

--changeSet SMOS-20:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
Create Index "IX_ORG_INDUCTION_PLAN" ON "OPR_INDUCTION_PLAN" ("SPREADSHEET_ID")
');
END;
/

--changeSet SMOS-20:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "OPR_INDUCTION_PLAN" add Constraint "FK_OPR_BLOB_INDUCTION_PLAN" foreign key ("SPREADSHEET_ID") references "OPR_BLOB" ("ID")  DEFERRABLE
');
END;
/