--liquibase formatted sql


--changeSet MX-21974.1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   bTableExists NUMBER;
BEGIN

   SELECT
      COUNT(1)
   INTO
      bTableExists
   FROM
      user_tables
   WHERE
      table_name = 'MAINT_PRGM_CARRIER_TEMP_TASK';

   IF bTableExists = 1 THEN

      -- Add audit columns
      BEGIN
         utl_migr_schema_pkg.table_column_add('ALTER TABLE "MAINT_PRGM_CARRIER_TEMP_TASK" ADD (
            "REVISION_NO" Number(10,0)
         )');
      END;

      BEGIN
         utl_migr_schema_pkg.table_column_add('ALTER TABLE "MAINT_PRGM_CARRIER_TEMP_TASK" ADD (
            "CTRL_DB_ID" Number(10,0) Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
         )');
      END;

      BEGIN
         utl_migr_schema_pkg.table_column_add('ALTER TABLE "MAINT_PRGM_CARRIER_TEMP_TASK" ADD (
            "CREATION_DB_ID" Number(10,0) Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
         )');
      END;

      BEGIN
         utl_migr_schema_pkg.table_column_add('ALTER TABLE "MAINT_PRGM_CARRIER_TEMP_TASK" ADD (
            "REVISION_DB_ID" Number(10,0) Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
         )');
      END;

      EXECUTE IMMEDIATE '
      UPDATE
         maint_prgm_carrier_temp_task
      SET
         ctrl_db_id = (SELECT db_id FROM mim_local_db),
         revision_no = 0,
         creation_db_id = maint_prgm_carrier_temp_task.maint_prgm_defn_db_id
      WHERE
         maint_prgm_carrier_temp_task.revision_no IS NULL
      ';

      BEGIN
         utl_migr_schema_pkg.table_column_modify('ALTER TABLE "MAINT_PRGM_CARRIER_TEMP_TASK" MODIFY (
            "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE
         )');
      END;

      BEGIN
         utl_migr_schema_pkg.table_column_modify('ALTER TABLE "MAINT_PRGM_CARRIER_TEMP_TASK" MODIFY (
            "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
         )');
      END;

      BEGIN
         utl_migr_schema_pkg.table_column_modify('ALTER TABLE "MAINT_PRGM_CARRIER_TEMP_TASK" MODIFY (
            "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
         )');
      END;

      BEGIN
         utl_migr_schema_pkg.table_column_modify('ALTER TABLE "MAINT_PRGM_CARRIER_TEMP_TASK" MODIFY (
            "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
         )');
      END;

   ELSE

      -- Add new table
      BEGIN
         utl_migr_schema_pkg.table_create('
            Create table "MAINT_PRGM_CARRIER_TEMP_TASK" (
               "MAINT_PRGM_DEFN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (MAINT_PRGM_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "MAINT_PRGM_DEFN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (MAINT_PRGM_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "CARRIER_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CARRIER_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "CARRIER_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CARRIER_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "TASK_DEFN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "TASK_DEFN_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "TASK_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "TASK_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
               "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE ,
               "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "CREATION_DT" Date NOT NULL DEFERRABLE ,
               "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "REVISION_DT" Date NOT NULL DEFERRABLE ,
               "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
               "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
             Constraint "PK_MAINT_PRGM_CARRIER_TEMP_TAS" primary key ("MAINT_PRGM_DEFN_DB_ID","MAINT_PRGM_DEFN_ID","CARRIER_DB_ID","CARRIER_ID","TASK_DEFN_DB_ID","TASK_DEFN_ID")
            )
         ');
      END;
   END IF;
END;
/

--changeSet MX-21974.1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   bTableExists NUMBER;
   ln_Count NUMBER;

   CURSOR lcur_issue_log IS
      SELECT DISTINCT
         task_temp_issue_log.task_db_id, task_temp_issue_log.task_id,
         prev_maint_prgm_carrier_map.maint_prgm_db_id, prev_maint_prgm_carrier_map.maint_prgm_id,
         task_temp_issue_log.hr_db_id, task_temp_issue_log.hr_id,
         task_temp_issue_log.temp_issue_dt
      FROM
         task_temp_issue_log
         INNER JOIN maint_prgm ON
            maint_prgm.maint_prgm_db_id = task_temp_issue_log.maint_prgm_db_id AND
            maint_prgm.maint_prgm_id    = task_temp_issue_log.maint_prgm_id
         INNER JOIN maint_prgm_carrier_map ON
            maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
            maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
         LEFT OUTER JOIN maint_prgm_carrier_map prev_maint_prgm_carrier_map ON
            (prev_maint_prgm_carrier_map.maint_prgm_db_id, prev_maint_prgm_carrier_map.maint_prgm_id) IN (
               SELECT DISTINCT
                  FIRST_VALUE(o_maint_prgm.maint_prgm_db_id) OVER (ORDER BY o_carrier_map.carrier_revision_ord DESC),
                  FIRST_VALUE(o_maint_prgm.maint_prgm_id) OVER (ORDER BY o_carrier_map.carrier_revision_ord DESC)
               FROM
                  maint_prgm o_maint_prgm
                  INNER JOIN maint_prgm_carrier_map o_carrier_map ON
                     o_carrier_map.maint_prgm_db_id = o_maint_prgm.maint_prgm_db_id AND
                     o_carrier_map.maint_prgm_id    = o_maint_prgm.maint_prgm_id
               WHERE
                  o_maint_prgm.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
                  o_maint_prgm.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
                  AND
                  o_carrier_map.carrier_db_id = maint_prgm_carrier_map.carrier_db_id AND
                  o_carrier_map.carrier_id    = maint_prgm_carrier_map.carrier_id
                  AND
                  o_carrier_map.carrier_revision_ord < maint_prgm_carrier_map.carrier_revision_ord
            )
   ;
   TYPE t_bulk_collect_issue_log IS TABLE OF lcur_issue_log%ROWTYPE;
   lt_issue_log t_bulk_collect_issue_log;
   ln_Index NUMBER;

   ls_TaskCd VARCHAR(2000);
BEGIN

   SELECT
      COUNT(1)
   INTO
      bTableExists
   FROM
      user_tables
   WHERE
      table_name = 'MAINT_PRGM_TEMP_TASK';

   IF bTableExists = 1 THEN
      EXECUTE IMMEDIATE 'SELECT
         COUNT(*)
      FROM
         maint_prgm_temp_task'
      INTO
         ln_Count;

      IF ln_Count <> 0 THEN

         -- Migrate data to new table
         EXECUTE IMMEDIATE '
         INSERT INTO maint_prgm_carrier_temp_task (
            maint_prgm_defn_db_id, maint_prgm_defn_id,
            carrier_db_id, carrier_id,
            task_defn_db_id, task_defn_id,
            task_db_id, task_id,
            rstat_cd,
            revision_no,
            ctrl_db_id,
            creation_dt, creation_db_id,
            revision_dt, revision_db_id, revision_user
         )
         SELECT
           maint_prgm.maint_prgm_defn_db_id, maint_prgm.maint_prgm_defn_id,
           maint_prgm_carrier_map.carrier_db_id, maint_prgm_carrier_map.carrier_id,
           maint_prgm_temp_task.task_defn_db_id, maint_prgm_temp_task.task_defn_id,
           maint_prgm_temp_task.task_db_id, maint_prgm_temp_task.task_id,
           maint_prgm_temp_task.rstat_cd,
           maint_prgm_temp_task.ctrl_db_id,
           maint_prgm_temp_task.revision_no,
           maint_prgm_temp_task.creation_dt, maint_prgm_temp_task.creation_db_id,
           maint_prgm_temp_task.revision_dt, maint_prgm_temp_task.revision_db_id, maint_prgm_temp_task.revision_user
         FROM
           maint_prgm_temp_task
           INNER JOIN maint_prgm ON
              maint_prgm.maint_prgm_db_id = maint_prgm_temp_task.maint_prgm_db_id AND
              maint_prgm.maint_prgm_id    = maint_prgm_temp_task.maint_prgm_id
           INNER JOIN maint_prgm_carrier_map ON
              maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
              maint_prgm_carrier_map.maint_prgm_id    = maint_prgm.maint_prgm_id
         WHERE
            maint_prgm.maint_prgm_status_db_id = 0 AND
            maint_prgm.maint_prgm_status_cd    = ''REVISION''
         ';
         EXECUTE IMMEDIATE 'DELETE FROM maint_prgm_temp_task';

         -- In this changeset, the task_temp_issue_log now points to the maintenance program that it was applicable
         -- for. In the old version, the task_temp_issue_log would point to the maintenance program in revision at the
         -- time. This is an issue as a maintenance program's operator can change. To determine the appropriate
         -- maintenance program that the temporarily issued task applies to, we now associate the task_temp_issue_log
         -- with the latest approved maintenance program for the operator.
         --
         -- NOTE: This may not copy all the information. In the case that a temporarily issued task was applied when
         -- there was no latest approved maintenance program, the temporarily issued log will be removed. This
         -- information cannot and never was displayed on the Maintenance Program's Requirements list since you cannot
         -- view a non-existent maintenance program. This data loss will not affect behavior; if a task was temporarily
         -- issued on a non-existent maintenance program, it will be in the maint_prgm_carrier_temp_task table (which
         -- drives which task is used). When the revision maintenance program is approved for that operator, the
         -- temporarily issued task information will be removed entirely. This affects the Requirement's History tab:
         -- We will no longer display that requirement as a temporarily issued.

         OPEN lcur_issue_log;
         FETCH lcur_issue_log BULK COLLECT INTO lt_issue_log;
         CLOSE lcur_issue_log;
         DELETE FROM task_temp_issue_log;

         FOR ln_Index IN 1..lt_issue_log.count LOOP
            IF lt_issue_log(ln_Index).maint_prgm_db_id IS NOT NULL THEN
               INSERT INTO task_temp_issue_log (
                  task_db_id, task_id,
                  maint_prgm_db_id, maint_prgm_id,
                  hr_db_id, hr_id,
                  temp_issue_dt
               )
               VALUES(
                  lt_issue_log(ln_Index).task_db_id, lt_issue_log(ln_Index).task_id,
                  lt_issue_log(ln_Index).maint_prgm_db_id, lt_issue_log(ln_Index).maint_prgm_id,
                  lt_issue_log(ln_Index).hr_db_id, lt_issue_log(ln_Index).hr_id,
                  lt_issue_log(ln_Index).temp_issue_dt
               );
            ELSE
               SELECT
                  task_task.task_cd
               INTO
                  ls_TaskCd
               FROM
                  task_task
               WHERE
                  task_task.task_db_id = lt_issue_log(ln_Index).task_db_id AND
                  task_task.task_id    = lt_issue_log(ln_Index).task_id
               ;
               DBMS_OUTPUT.put_line('Removing log information for ''' || ls_TaskCd || ''' ('|| lt_issue_log(ln_Index).task_db_id ||':'|| lt_issue_log(ln_Index).task_id ||') because it was temporarily issued on a non-existent maintenance program.' );
            END IF;
         END LOOP;
      END IF;
   END IF;
END;
/

--changeSet MX-21974.1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add audit columns triggers
CREATE OR REPLACE TRIGGER "TIBR_MNT_PRGM_CR_TMP_TSK" BEFORE INSERT
   ON "MAINT_PRGM_CARRIER_TEMP_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-21974.1:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_MNT_PRGM_CR_TMP_TSK" BEFORE UPDATE
   ON "MAINT_PRGM_CARRIER_TEMP_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-21974.1:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Audit Columns
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "MAINT_PRGM_CARRIER_TEMP_TASK" add Constraint "FK_CRMIMDB_MNTPRGMCRTMPTSK" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-21974.1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "MAINT_PRGM_CARRIER_TEMP_TASK" add Constraint "FK_CTMIMDB_MNTPRGMCRTMPTSK" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-21974.1:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "MAINT_PRGM_CARRIER_TEMP_TASK" add Constraint "FK_MIMDB_MNTPRGMCRTMPTSK" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-21974.1:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "MAINT_PRGM_CARRIER_TEMP_TASK" add Constraint "FK_MIMRSTAT_MNTPRGMCRTMPTSK" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
   ');
END;
/

--changeSet MX-21974.1:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Carrier
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_ORGCARRIER_MNTPRGMCRTMPTSK" ON "MAINT_PRGM_CARRIER_TEMP_TASK" ("CARRIER_DB_ID","CARRIER_ID")
   ');
END;
/

--changeSet MX-21974.1:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "MAINT_PRGM_CARRIER_TEMP_TASK" add Constraint "FK_ORGCARRIER_MNTPRGMCRTMPTSK" foreign key ("CARRIER_DB_ID","CARRIER_ID") references "ORG_CARRIER" ("CARRIER_DB_ID","CARRIER_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-21974.1:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Task
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_TASKTASK_MNTPRGMCRTMPTSK" ON "MAINT_PRGM_CARRIER_TEMP_TASK" ("TASK_DB_ID","TASK_ID")
   ');
END;
/

--changeSet MX-21974.1:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "MAINT_PRGM_CARRIER_TEMP_TASK" add Constraint "FK_TASKTASK_MNTPRGMCRTMPTSK" foreign key ("TASK_DB_ID","TASK_ID") references "TASK_TASK" ("TASK_DB_ID","TASK_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-21974.1:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Task Defn
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_TASKDEFN_MNTPRGMCRTMPTSK" ON "MAINT_PRGM_CARRIER_TEMP_TASK" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")
   ');
END;
/

--changeSet MX-21974.1:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "MAINT_PRGM_CARRIER_TEMP_TASK" add Constraint "FK_TASKDEFN_MNTPRGMCRTMPTSK" foreign key ("TASK_DEFN_DB_ID","TASK_DEFN_ID") references "TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")  DEFERRABLE
   ');
END;
/

--changeSet MX-21974.1:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- MAINT_PRGM_DEFN
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_MNTPRGMDEFN_MNTPRGMCRTMPTSK" ON "MAINT_PRGM_CARRIER_TEMP_TASK" ("MAINT_PRGM_DEFN_DB_ID","MAINT_PRGM_DEFN_ID")
   ');
END;
/

--changeSet MX-21974.1:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "MAINT_PRGM_CARRIER_TEMP_TASK" add Constraint "FK_MNTPRGMDEFN_MNTPRGMCRTMPTSK" foreign key ("MAINT_PRGM_DEFN_DB_ID","MAINT_PRGM_DEFN_ID") references "MAINT_PRGM_DEFN" ("MAINT_PRGM_DEFN_DB_ID","MAINT_PRGM_DEFN_ID")  DEFERRABLE
   ');
END;
/