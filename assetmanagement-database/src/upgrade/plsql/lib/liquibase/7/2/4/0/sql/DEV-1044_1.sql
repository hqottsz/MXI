--liquibase formatted sql


--changeSet DEV-1044_1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

  -- create the new mapping table
  utl_migr_schema_pkg.table_create('
	Create table "PO_LINE_RETURN_MAP" (
		"PO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"PO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"PO_LINE_ID" Number(10,0) NOT NULL DEFERRABLE  Check (PO_LINE_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"INV_NO_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"INV_NO_ID" Number(10,0) NOT NULL DEFERRABLE  Check (INV_NO_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
		"CREATION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DT" Date NOT NULL DEFERRABLE ,
		"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
	 Constraint "PK_PO_LINE_RETURN_MAP" primary key ("PO_DB_ID","PO_ID","PO_LINE_ID","INV_NO_DB_ID","INV_NO_ID") 
	)
  ');
  
  -- INV_INV FK
  utl_migr_schema_pkg.table_constraint_add('
    Alter table "PO_LINE_RETURN_MAP" add Constraint "FK_INVINV_POLRTRN" foreign key ("INV_NO_DB_ID","INV_NO_ID") references "INV_INV" ("INV_NO_DB_ID","INV_NO_ID")  DEFERRABLE
  ');
  
  -- PO_LINE FK
  utl_migr_schema_pkg.table_constraint_add('
    Alter table "PO_LINE_RETURN_MAP" add Constraint "FK_POLINE_POLRTRN" foreign key ("PO_DB_ID","PO_ID","PO_LINE_ID") references "PO_LINE" ("PO_DB_ID","PO_ID","PO_LINE_ID")  DEFERRABLE
  ');
  
  -- mim_rstat FK
   utl_migr_schema_pkg.table_constraint_add('
     Alter table "PO_LINE_RETURN_MAP" add CONSTRAINT "FK_MIMRSTAT_POLRTRN" FOREIGN KEY ("RSTAT_CD") REFERENCES "MIM_RSTAT" ("RSTAT_CD") DEFERRABLE
   ');
   
   -- RETURNMAP_POLINE_FK index
   utl_migr_schema_pkg.index_create('
      Create Index "RETURNMAP_POLINE_FK" ON "PO_LINE_RETURN_MAP" ("PO_DB_ID","PO_ID","PO_LINE_ID") 
   ');
   
   -- RETURNMAP_INVINV_FK index
   utl_migr_schema_pkg.index_create('
      Create Index "RETURNMAP_INVINV_FK" ON "PO_LINE_RETURN_MAP" ("INV_NO_DB_ID","INV_NO_ID") 
   ');
END;
/  

--changeSet DEV-1044_1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PO_LINE_RETURN_MAP" BEFORE INSERT
 ON "PO_LINE_RETURN_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-1044_1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_PO_LINE_RETURN_MAP" BEFORE UPDATE
 ON "PO_LINE_RETURN_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-1044_1:4 stripComments:false
-- Commented out due to changes that have already been applied in 7.2.0.0-SP1
-- BEGIN
--  -- copy the po_line return inventory into the new mapping table
--  INSERT INTO PO_LINE_RETURN_MAP (PO_DB_ID, PO_ID, PO_LINE_ID, INV_NO_DB_ID, INV_NO_ID)
--  SELECT
--    PO_LINE.PO_DB_ID,
--    PO_LINE.PO_ID,
--    PO_LINE.PO_LINE_ID,
--    PO_LINE.RETURN_INV_NO_DB_ID,
--    PO_LINE.RETURN_INV_NO_ID
--  FROM
--    PO_LINE
--  WHERE
--    PO_LINE.RETURN_INV_NO_DB_ID IS NOT NULL
--    AND
--    NOT EXISTS (
--       SELECT
--          1
--       FROM
--          PO_LINE_RETURN_MAP PO_MAP
--       WHERE
--          PO_MAP.PO_DB_ID   = PO_LINE.PO_DB_ID AND
--          PO_MAP.PO_ID      = PO_LINE.PO_ID AND
--          PO_MAP.PO_LINE_ID = PO_LINE.PO_LINE_ID
--          AND
--          PO_MAP.INV_NO_DB_ID = PO_LINE.RETURN_INV_NO_DB_ID AND
--          PO_MAP.INV_NO_ID    = PO_LINE.RETURN_INV_NO_ID
--    )
    --
--  -- drop the constraint on the old columns
--  utl_migr_schema_pkg.table_constraint_drop( 'PO_LINE', 'FK_INVINV_POLINE' )
  --
--  -- drop the old columns
--  utl_migr_schema_pkg.table_column_drop('PO_LINE', 'RETURN_INV_NO_DB_ID')
--  utl_migr_schema_pkg.table_column_drop('PO_LINE', 'RETURN_INV_NO_ID')
  --
--  -- drop the inv_exchange table
--  utl_migr_schema_pkg.table_drop('INV_EXCHANGE')
  --
-- END
-- 
-- remove inv_exchange sequence
DELETE FROM utl_sequence WHERE sequence_cd = 'INV_EXCHANGE_ID_SEQ';

--changeSet DEV-1044_1:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.SEQUENCE_DROP('INV_EXCHANGE_ID_SEQ');
END;
/