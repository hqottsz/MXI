--liquibase formatted sql


--changeSet MTX-191:1 stripComments:false
-- Adding a new UUID surrogate key to UTL_USER
ALTER TRIGGER TUBR_UTL_USER DISABLE;

--changeSet MTX-191:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table UTL_USER add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-191:3 stripComments:false
UPDATE 
   UTL_USER 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-191:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table UTL_USER modify (
   ALT_ID Raw(16) NOT NULL  Constraint "UK_UTLUSER_ALTID" UNIQUE 
)
');
END;
/

--changeSet MTX-191:5 stripComments:false
ALTER TRIGGER TUBR_UTL_USER ENABLE;

--changeSet MTX-191:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_UTL_USER_ALT_ID" BEFORE INSERT
   ON "UTL_USER" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-191:7 stripComments:false
-- Adding a new UUID surrogate key to PPC_PLAN
ALTER TRIGGER TUBR_PPC_PLAN DISABLE;

--changeSet MTX-191:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PPC_PLAN add (
   ALT_ID Raw(16)
)
');
END;
/

--changeSet MTX-191:9 stripComments:false
UPDATE 
   PPC_PLAN 
SET 
   alt_id = mx_key_pkg.new_uuid() 
WHERE 
   alt_id IS NULL
;

--changeSet MTX-191:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PPC_PLAN modify (
   ALT_ID Raw(16) NOT NULL  UNIQUE 
)
');
END;
/

--changeSet MTX-191:11 stripComments:false
ALTER TRIGGER TUBR_PPC_PLAN ENABLE;

--changeSet MTX-191:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PPC_PLAN_ALT_ID" BEFORE INSERT
   ON "PPC_PLAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-191:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Adding a new FK to PPC_PLAN
BEGIN
   utl_migr_schema_pkg.table_column_add('
      ALTER TABLE PPC_PLAN ADD(
         CONTROL_USER_ALT_ID raw(16)
      )
   ');
END;
/

--changeSet MTX-191:14 stripComments:false
ALTER TRIGGER TUBR_PPC_PLAN ENABLE;

--changeSet MTX-191:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Migrating data in PPC_PLAN table: 
-- USER_ID based FK -> ALT_ID based FK
DECLARE
   CURSOR cur_controlled_plans IS
      SELECT 
         ppc_plan.ppc_db_id,
         ppc_plan.ppc_id,
         utl_user.alt_id AS control_user_alt_id
      FROM 
         ppc_plan
         INNER JOIN utl_user ON utl_user.user_id = ppc_plan.control_user_id;
   lrec_ControlledPlan cur_controlled_plans%ROWTYPE;
BEGIN
   FOR lrec_ControlledPlan IN cur_controlled_plans LOOP
      UPDATE 
         ppc_plan
      SET 
         ppc_plan.control_user_alt_id = lrec_ControlledPlan.control_user_alt_id
      WHERE
         ppc_plan.ppc_db_id = lrec_ControlledPlan.ppc_db_id AND
         ppc_plan.ppc_id    = lrec_ControlledPlan.ppc_id;
    END LOOP;
END;
/

--changeSet MTX-191:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Removing existing FK_PPCPLAN_CONTROLUSER constraint.
BEGIN
   utl_migr_schema_pkg.table_constraint_drop('PPC_PLAN', 'FK_PPCPLAN_CONTROLUSER');
END;
/

--changeSet MTX-191:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Removing PPC_PLAN.CONTROL_USER_ID column.
BEGIN
   utl_migr_schema_pkg.table_column_drop('PPC_PLAN', 'CONTROL_USER_ID');
END;
/

--changeSet MTX-191:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Renaming PPC_PLAN.CONTROL_USER_ALT_ID into PPC_PLAN.CONTROL_USER_ID column.
BEGIN
   utl_migr_schema_pkg.table_column_rename('PPC_PLAN', 'CONTROL_USER_ALT_ID', 'CONTROL_USER_ID');
END;
/

--changeSet MTX-191:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Establishing a new ALT_ID based constraint with the same name. 
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "PPC_PLAN" add Constraint "FK_PPCPLAN_CONTROLUSER" foreign key ("CONTROL_USER_ID") references "UTL_USER" ("ALT_ID")  DEFERRABLE
   ');
END;
/