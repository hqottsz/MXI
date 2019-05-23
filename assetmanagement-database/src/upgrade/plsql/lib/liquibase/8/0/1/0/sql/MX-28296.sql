--liquibase formatted sql


--changeSet MX-28296:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
      Create table "TASK_FLEET_APPROVAL" (
         "TASK_DEFN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
         "TASK_DEFN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
         "TASK_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
         "TASK_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
         "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
         "CREATION_DT" Date NOT NULL DEFERRABLE ,
         "REVISION_DT" Date NOT NULL DEFERRABLE ,
         "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
         "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
         Constraint "PK_TASK_FLEET_APPROVAL" primary key ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
   )');
END;
/

--changeSet MX-28296:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table "TASK_FLEET_APPROVAL" add (
         "REVISION_NO" Number(10,0)
      )
   ');
END;
/

--changeSet MX-28296:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table "TASK_FLEET_APPROVAL" add (
         "CTRL_DB_ID" Number(10,0)  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
      )
   ');
END;
/

--changeSet MX-28296:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_add('
      Alter table "TASK_FLEET_APPROVAL" add (
         "CREATION_DB_ID" Number(10,0)  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
      )
   ');
END;
/

--changeSet MX-28296:5 stripComments:false
UPDATE
   task_fleet_approval
SET
   revision_no = 1,
   ctrl_db_id = task_defn_db_id,
   creation_db_id = task_defn_db_id
WHERE
   revision_no IS NULL
;

--changeSet MX-28296:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table "TASK_FLEET_APPROVAL" modify (
         "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE
      )
   ');
END;
/

--changeSet MX-28296:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table "TASK_FLEET_APPROVAL" modify (
         "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
      )
   ');
END;
/

--changeSet MX-28296:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
      Alter table "TASK_FLEET_APPROVAL" modify (
         "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
      )
   ');
END;
/

--changeSet MX-28296:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_FK_TSKTSK_TSKFLTAPPRVL" ON "TASK_FLEET_APPROVAL" ("TASK_DB_ID","TASK_ID")
   ');
END;
/

--changeSet MX-28296:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "TASK_FLEET_APPROVAL" add Constraint "FK_TSKTSK_TSKFLTAPPRVL" foreign key ("TASK_DB_ID","TASK_ID") references "TASK_TASK" ("TASK_DB_ID","TASK_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-28296:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "TASK_FLEET_APPROVAL" add Constraint "FK_TSKDFN_TSKFLTAPPRVL" foreign key ("TASK_DEFN_DB_ID","TASK_DEFN_ID") references "TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-28296:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "TASK_FLEET_APPROVAL" add Constraint "FK_MIMRSTAT_TSKFLTAPPRVL" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
   ');
END;
/

--changeSet MX-28296:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "TASK_FLEET_APPROVAL" add Constraint "FK_MIMDB_TSKFLTAPPRVL" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-28296:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "TASK_FLEET_APPROVAL" add Constraint "FK_CRMIMDB_TSKFLTAPPRVL" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-28296:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "TASK_FLEET_APPROVAL" add Constraint "FK_CTMIMDB_TSKFLTAPPRVL" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-28296:16 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- This trigger was created with a different name in 7x, therefore, drop the 
-- old version of the trigger before creating it with the new name
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_TASK_FLEET_APPROVAL');
END;
/

--changeSet MX-28296:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TSK_FLT_APPRVL" BEFORE INSERT
   ON "TASK_FLEET_APPROVAL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

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
END;
/

--changeSet MX-28296:18 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- This trigger was created with a different name in 7x, therefore, drop the 
-- old version of the trigger before creating it with the new name
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TUBR_TASK_FLEET_APPROVAL');
END;
/

--changeSet MX-28296:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_TSK_FLT_APPRVL" BEFORE UPDATE
   ON "TASK_FLEET_APPROVAL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN

  MX_TRIGGER_PKG.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
END;
/

--changeSet MX-28296:20 stripComments:false
INSERT INTO task_fleet_approval(task_defn_db_id, task_defn_id, task_db_id, task_id)
SELECT
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   task_task.task_db_id,
   task_task.task_id
FROM
   task_task
WHERE
   task_task.task_def_status_db_id = 0 AND
   task_task.task_def_status_cd IN ( 'ACTV', 'OBSOLETE' )
   AND
   -- We want to freeze baseline synchronization when the task definition
   -- is a build or revision maintenance program
   NOT EXISTS
   (
      SELECT
         1
      FROM
         maint_prgm_task
         INNER JOIN maint_prgm ON
            maint_prgm.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
            maint_prgm.maint_prgm_id    = maint_prgm_task.maint_prgm_id
      WHERE
         maint_prgm_task.task_defn_db_id = task_task.task_defn_db_id AND
         maint_prgm_task.task_defn_id    = task_task.task_defn_id
         AND
         maint_prgm.maint_prgm_status_cd IN ( 'BUILD', 'REVISION' )
         AND
         maint_prgm_task.unassign_bool = 0
   )
   AND
   -- If the task definition is controlled by maintenance program, use alternative sub-query
   NOT EXISTS
   (
      SELECT
         1
      FROM
         vw_maint_prgm_task
      WHERE
         vw_maint_prgm_task.task_defn_db_id = task_task.task_defn_db_id AND
         vw_maint_prgm_task.task_defn_id    = task_task.task_defn_id
   )
   AND
   NOT EXISTS(
      SELECT 1 FROM task_fleet_approval
   );


--changeSet MX-28296:21 stripComments:false
-- This view determines the what task should be used on an inventory tree
CREATE OR REPLACE VIEW vw_h_baseline_task
AS
(
-- Gets the latest approved task definition
SELECT
   task_task.task_defn_db_id,
   task_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE task_task.task_id END AS task_id
FROM
   task_task
   INNER JOIN task_fleet_approval ON
      task_fleet_approval.task_db_id = task_task.task_db_id AND
      task_fleet_approval.task_id    = task_task.task_id
UNION ALL
-- For config task definitions on assemblies or aircrafts
SELECT
   vw_carrier_baseline_task.task_defn_db_id,
   vw_carrier_baseline_task.task_defn_id,
   vw_carrier_baseline_task.task_db_id,
   vw_carrier_baseline_task.task_id
FROM
   inv_inv,
   inv_inv h_inv_inv,
   vw_carrier_baseline_task
WHERE
   -- Highest inventory (attached)
   inv_inv.inv_no_db_id = context_package.get_inv_no_db_id() AND
   inv_inv.inv_no_id    = context_package.get_inv_no_id()
   AND
   h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
   h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
   AND
   h_inv_inv.inv_class_cd IN ('ACFT', 'ASSY')
UNION ALL
-- For config task definitions on loose components
SELECT
   vw_loose_baseline_task.task_defn_db_id,
   vw_loose_baseline_task.task_defn_id,
   vw_loose_baseline_task.task_db_id,
   vw_loose_baseline_task.task_id
FROM
   inv_inv,
   inv_inv h_inv_inv,
   vw_loose_baseline_task
WHERE
   -- Highest inventory (loose)
   inv_inv.inv_no_db_id = context_package.get_inv_no_db_id() AND
   inv_inv.inv_no_id    = context_package.get_inv_no_id()
   AND
   h_inv_inv.inv_no_db_id = inv_inv.h_inv_no_db_id AND
   h_inv_inv.inv_no_id    = inv_inv.h_inv_no_id
   AND
   h_inv_inv.inv_class_cd NOT IN ('ACFT', 'ASSY')
);

--changeSet MX-28296:22 stripComments:false
-- This view determines the what task should be used on an inventory
CREATE OR REPLACE VIEW vw_baseline_task
AS
-- gets the ACTV or OBSOLETE version of task defns that are not currently assigned to an ACTV MP
SELECT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_inv_task.task_db_id END AS task_db_id,
   CASE WHEN task_task.task_def_status_cd = 'OBSOLETE' THEN NULL ELSE vw_inv_task.task_id END AS task_id
FROM
   vw_inv_task
   INNER JOIN task_task ON
      task_task.task_db_id = vw_inv_task.task_db_id AND
      task_task.task_id    = vw_inv_task.task_id
   INNER JOIN task_fleet_approval ON
      task_fleet_approval.task_db_id = task_task.task_db_id AND
      task_fleet_approval.task_id    = task_task.task_id
UNION ALL
-- If a task defnition is in a maintenance program, special rules apply towards what gets returned
(
-- If the task defn is not applicable to the carrier (as determined by the MP), return NULL
SELECT DISTINCT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   NULL,
   NULL
FROM
   inv_inv assmbl_inv,
   vw_inv_task
WHERE
   assmbl_inv.inv_no_db_id =
      CASE    WHEN vw_inv_task.inv_class_cd IN ('ACFT', 'ASSY')
         THEN vw_inv_task.inv_no_db_id
         ELSE vw_inv_task.assmbl_inv_no_db_id
      END
   AND
   assmbl_inv.inv_no_id =
      CASE    WHEN vw_inv_task.inv_class_cd IN ('ACFT', 'ASSY')
        THEN vw_inv_task.inv_no_id
        ELSE vw_inv_task.assmbl_inv_no_id
     END
   AND
   -- The task defn is part of any ACTV MP
   EXISTS
   (
      SELECT
         1
      FROM
         vw_maint_prgm_task
      WHERE
         -- map to task in maintenance program
         vw_maint_prgm_task.task_defn_db_id = vw_inv_task.task_defn_db_id AND
         vw_maint_prgm_task.task_defn_id    = vw_inv_task.task_defn_id
   )
   AND
   -- The task defn is not part of the carrier MP
   NOT EXISTS
   (
      SELECT
         1
      FROM
         vw_maint_prgm_task
      WHERE
         -- from the latest carrier maintenance program...
         vw_maint_prgm_task.carrier_db_id = assmbl_inv.carrier_db_id AND
         vw_maint_prgm_task.carrier_id    = assmbl_inv.carrier_id
         AND
         vw_maint_prgm_task.task_defn_db_id = vw_inv_task.task_defn_db_id AND
         vw_maint_prgm_task.task_defn_id    = vw_inv_task.task_defn_id
   )
UNION ALL
-- If the task defn is applicable to the carrier, return the MPs task defn revision
SELECT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   vw_inv_task.task_db_id,
   vw_inv_task.task_id
FROM
   vw_inv_task
   INNER JOIN task_task ON
      task_task.task_db_id = vw_inv_task.task_db_id AND
      task_task.task_id    = vw_inv_task.task_id
   INNER JOIN inv_inv assmbl_inv ON
      -- from the latest carrier maintenance program...
      assmbl_inv.inv_no_db_id =
         CASE    WHEN vw_inv_task.inv_class_cd IN ('ACFT', 'ASSY')
            THEN vw_inv_task.inv_no_db_id
            ELSE vw_inv_task.assmbl_inv_no_db_id
         END
      AND
      assmbl_inv.inv_no_id =
         CASE    WHEN vw_inv_task.inv_class_cd IN ('ACFT', 'ASSY')
            THEN vw_inv_task.inv_no_id
            ELSE vw_inv_task.assmbl_inv_no_id
         END
   INNER JOIN vw_maint_prgm_task ON
      vw_maint_prgm_task.carrier_db_id = assmbl_inv.carrier_db_id AND
      vw_maint_prgm_task.carrier_id    = assmbl_inv.carrier_id
      AND
      vw_maint_prgm_task.task_db_id = task_task.task_db_id AND
      vw_maint_prgm_task.task_id    = task_task.task_id
UNION ALL
-- If the task defn is assigned to a MP and the task defn is also applicable to a loose inv
SELECT DISTINCT
   vw_inv_task.task_defn_db_id,
   vw_inv_task.task_defn_id,
   FIRST_VALUE(task_task.task_db_id) OVER (
      PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id
      ORDER BY task_task.revision_ord DESC
   ) AS task_db_id,
   FIRST_VALUE(task_task.task_id) OVER (
      PARTITION BY task_task.task_defn_db_id, task_task.task_defn_id
      ORDER BY task_task.revision_ord DESC
   ) AS task_id
FROM
   vw_inv_task
   INNER JOIN task_task ON
      task_task.task_db_id = vw_inv_task.task_db_id AND
      task_task.task_id    = vw_inv_task.task_id
   INNER JOIN vw_maint_prgm_task ON
      vw_maint_prgm_task.task_db_id = task_task.task_db_id AND
      vw_maint_prgm_task.task_id = task_task.task_id
WHERE
   vw_inv_task.assmbl_inv_no_db_id IS NULL AND
   vw_inv_task.assmbl_inv_no_id IS NULL
   AND
   vw_inv_task.inv_class_cd NOT IN ('ACFT', 'ASSY')
)
;