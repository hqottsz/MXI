--liquibase formatted sql


--changeSet DEV-1075:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- migration script for DEV-1075
BEGIN
utl_migr_schema_pkg.table_create('
Create table "REF_CHANGE_REASON" (
	"CHANGE_REASON_CD" Varchar2 (16) NOT NULL DEFERRABLE ,
	"DISPLAY_CODE" Varchar2 (8) NOT NULL DEFERRABLE ,
	"DISPLAY_NAME" Varchar2 (80) NOT NULL DEFERRABLE ,
	"DISPLAY_DESC" Varchar2 (4000) NOT NULL DEFERRABLE ,
	"DISPLAY_ORD" Number(4,0) NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_REF_CHANGE_REASON" primary key ("CHANGE_REASON_CD") 
)
');
END;
/

--changeSet DEV-1075:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_REF_CHANGE_REASON" BEFORE INSERT
   ON "REF_CHANGE_REASON" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_insert(
    :new.rstat_cd,
    :new.revision_no,
    :new.ctrl_db_id,
    :new.creation_dt,
    :new.creation_db_id,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user
    );
end;
/

--changeSet DEV-1075:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_CHANGE_REASON" BEFORE UPDATE
   ON "REF_CHANGE_REASON" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet DEV-1075:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_CHANGE_REASON" add Constraint "FK_MIMRSTAT_REFCHANGEREASON" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1075:5 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'MXRTINE', 'Routine', 'Routine Action', 'Routine Action', 1, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD='MXRTINE');

--changeSet DEV-1075:6 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'MXACCEPT', 'Accept', 'Acceptance of Quotation', 'Acceptance of Quotation', 2, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD='MXACCEPT');

--changeSet DEV-1075:7 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'MXALTER', 'Alter', 'Alteration', 'Alteration', 3, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD='MXALTER');

--changeSet DEV-1075:8 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'MXCNCL', 'Cancel', 'Cancel', 'Cancel/Decrease Quantity', 6, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD='MXCNCL');

--changeSet DEV-1075:9 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'MXDCRSE', 'Decrease', 'Decrease Quantity', 'Cancel/Decrease Quantity', 7, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD='MXDCRSE');

--changeSet DEV-1075:10 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'MXINCRSE', 'Increase', 'Increase Quantity', 'Increase Quantity', 8, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD='MXINCRSE');

--changeSet DEV-1075:11 stripComments:false
insert into ref_change_reason ( CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT 'MXPRTL', 'Partial', 'Request partial ship quantity', 'Request partial Ship Quantity of an order', 9, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD='MXPRTL');

--changeSet DEV-1075:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE add (
"DELETED_BOOL" Number(1,0) Check (DELETED_BOOL IN (0, 1) ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1075:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_LINE add (
"CHANGE_REASON_CD" Varchar2 (16)
)
');
END;
/

--changeSet DEV-1075:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "PO_LINE" add Constraint "FK_REFCHANGEREASON_POLINE" foreign key ("CHANGE_REASON_CD") references "REF_CHANGE_REASON" ("CHANGE_REASON_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1075:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_LINE DISABLE';
END;
/

--changeSet DEV-1075:16 stripComments:false
UPDATE
   PO_LINE
SET
   deleted_bool=0
WHERE
   deleted_bool IS NULL;   

--changeSet DEV-1075:17 stripComments:false
UPDATE
   PO_LINE
SET
   change_reason_cd='MXRTINE'
WHERE
   change_reason_cd IS NULL;   

--changeSet DEV-1075:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_LINE ENABLE';
END;
/

--changeSet DEV-1075:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table "PO_LINE" modify (
"DELETED_BOOL" Default 0 NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-1075:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table "PO_LINE" modify (
"CHANGE_REASON_CD" NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-1075:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE add (
"INV_SEQ_NUM" Number(10,0)
)
');
END;
/

--changeSet DEV-1075:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE add (
"VENDOR_INVOICE_SDESC" Varchar2 (80)
)
');
END;
/

--changeSet DEV-1075:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_INVOICE add (
"VENDOR_INVOICE_DT" Date
)
');
END;
/