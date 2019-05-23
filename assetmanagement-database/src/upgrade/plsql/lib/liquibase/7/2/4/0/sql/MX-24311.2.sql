--liquibase formatted sql


--changeSet MX-24311.2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create TASK_TEMP_ISSUE_LOG
BEGIN
utl_migr_schema_pkg.table_create('
Create table "TASK_TEMP_ISSUE_LOG" (
	"TASK_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_ID" Number(10,0) NOT NULL DEFERRABLE  Check (TASK_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"MAINT_PRGM_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (MAINT_PRGM_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"MAINT_PRGM_ID" Number(10,0) NOT NULL DEFERRABLE  Check (MAINT_PRGM_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"HR_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"HR_ID" Number(10,0) NOT NULL DEFERRABLE  Check (HR_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TEMP_ISSUE_DT" Date NOT NULL DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_TASK_TEMP_ISSUE_LOG" primary key ("TASK_DB_ID","TASK_ID","MAINT_PRGM_DB_ID","MAINT_PRGM_ID") 
) 
');
END;
/

--changeSet MX-24311.2:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD (
	"REVISION_NO" Number(10,0)
)
');
END;
/

--changeSet MX-24311.2:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD (
	"CTRL_DB_ID" Number(10,0)
)
');
END;
/

--changeSet MX-24311.2:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD (
	"CREATION_DB_ID" Number(10,0)
)
');
END;
/

--changeSet MX-24311.2:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TASK_TEMP_ISSUE_LOG" BEFORE INSERT
   ON "TASK_TEMP_ISSUE_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-24311.2:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_TASK_TEMP_ISSUE_LOG" BEFORE UPDATE
   ON "TASK_TEMP_ISSUE_LOG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-24311.2:7 stripComments:false
UPDATE
   task_temp_issue_log
SET
   ctrl_db_id = (SELECT db_id FROM mim_local_db),
   revision_no = 0,
   creation_db_id = task_temp_issue_log.maint_prgm_db_id
WHERE
   task_temp_issue_log.revision_no IS NULL
;

--changeSet MX-24311.2:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" MODIFY (
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet MX-24311.2:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" MODIFY (
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-24311.2:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" MODIFY (
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-24311.2:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD CONSTRAINT "FK_MIMDB_TASKTEMPISSUELOG" FOREIGN KEY ("REVISION_DB_ID") REFERENCES "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.2:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD CONSTRAINT "FK_ORGHR_TASKTEMPISSUELOG" FOREIGN KEY ("HR_DB_ID","HR_ID") REFERENCES "ORG_HR" ("HR_DB_ID","HR_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.2:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD CONSTRAINT "FK_TASKTASK_TASKTEMPISSUELOG" FOREIGN KEY ("TASK_DB_ID","TASK_ID") REFERENCES "TASK_TASK" ("TASK_DB_ID","TASK_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.2:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD CONSTRAINT "FK_MIMRSTAT_TASKTEMPISSUELOG" FOREIGN KEY ("RSTAT_CD") REFERENCES "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-24311.2:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD CONSTRAINT "FK_MAINTPRGM_TASKTEMPISSUELOG" FOREIGN KEY ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID") REFERENCES "MAINT_PRGM" ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.2:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD CONSTRAINT "FK_CTMIMDB_TASKTEMPISSUELOG" FOREIGN KEY ("CTRL_DB_ID") REFERENCES "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.2:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "TASK_TEMP_ISSUE_LOG" ADD CONSTRAINT "FK_CRMIMDB_TASKTEMPISSUELOG" FOREIGN KEY ("CREATION_DB_ID") REFERENCES "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.2:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   vExists   NUMBER(1);
BEGIN

   SELECT
      COUNT(1)
   INTO
      vExists
   FROM
      user_tab_columns
   WHERE
      table_name = 'TASK_TASK'
      AND
      column_name = 'TEMP_REV_BOOL'
   ;

   IF vExists = 1 THEN

      -- Migrate data into new table
      EXECUTE IMMEDIATE '
         INSERT INTO
            task_temp_issue_log (
               task_db_id,
               task_id,
               maint_prgm_db_id,
               maint_prgm_id,
               hr_db_id,
               hr_id,
               temp_issue_dt
            )
         SELECT
            maint_prgm_temp_task.task_db_id,
            maint_prgm_temp_task.task_id,
            maint_prgm_temp_task.maint_prgm_db_id,
            maint_prgm_temp_task.maint_prgm_id,
            task_task.temp_rev_hr_db_id,
            task_task.temp_rev_hr_id,
            task_task.temp_rev_dt
         FROM
            maint_prgm_temp_task
         INNER JOIN task_task ON
            task_task.task_db_id = maint_prgm_temp_task.task_db_id AND
            task_task.task_id    = maint_prgm_temp_task.task_id
         WHERE
            NOT EXISTS (
               SELECT 1 FROM task_temp_issue_log
            )';
   END IF;
END;
/

--changeSet MX-24311.2:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Remove old table columns
BEGIN
   utl_migr_schema_pkg.table_constraint_drop('TASK_TASK', 'FK_ORGHR_TASKTASKTEMPREV');
END;
/

--changeSet MX-24311.2:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_drop('TASK_TASK', 'TEMP_REV_BOOL');
END;
/

--changeSet MX-24311.2:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_drop('TASK_TASK', 'TEMP_REV_HR_DB_ID');
END;
/

--changeSet MX-24311.2:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_drop('TASK_TASK', 'TEMP_REV_HR_ID');
END;
/

--changeSet MX-24311.2:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_drop('TASK_TASK', 'TEMP_REV_DT');
END;
/