--liquibase formatted sql


--changeSet MX-24311.1:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
CREATE TABLE "MAINT_PRGM_TEMP_TASK" (
	"MAINT_PRGM_DB_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (MAINT_PRGM_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"MAINT_PRGM_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (MAINT_PRGM_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_DEFN_DB_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (TASK_DEFN_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_DEFN_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (TASK_DEFN_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_DB_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (TASK_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"TASK_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (TASK_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  CHECK (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
	"CREATION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DT" Date NOT NULL DEFERRABLE ,
	"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 CONSTRAINT "PK_MAINT_PRGM_TEMP_TASK" PRIMARY KEY ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID","TASK_DEFN_DB_ID","TASK_DEFN_ID") 
)
');
END;
/

--changeSet MX-24311.1:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD (
	"REVISION_NO" Number(10,0)
)
');
END;
/

--changeSet MX-24311.1:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD (
	"CTRL_DB_ID" Number(10,0)
)
');
END;
/

--changeSet MX-24311.1:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD (
	"CREATION_DB_ID" Number(10,0)
)
');
END;
/

--changeSet MX-24311.1:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_MAINT_PRGM_TEMP_TASK" BEFORE INSERT
   ON "MAINT_PRGM_TEMP_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-24311.1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_MAINT_PRGM_TEMP_TASK" BEFORE UPDATE
   ON "MAINT_PRGM_TEMP_TASK" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MX-24311.1:7 stripComments:false
UPDATE
   maint_prgm_temp_task
SET
   ctrl_db_id = (SELECT db_id FROM mim_local_db),
   revision_no = 0,
   creation_db_id = maint_prgm_temp_task.maint_prgm_db_id
WHERE
   maint_prgm_temp_task.revision_no IS NULL
;

--changeSet MX-24311.1:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Set NOT NULL
BEGIN
utl_migr_schema_pkg.table_column_modify('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" MODIFY  (
	"REVISION_NO" Number(10,0) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet MX-24311.1:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" MODIFY (
	"CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-24311.1:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" MODIFY (
	"CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  CHECK (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet MX-24311.1:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD CONSTRAINT "FK_MIMDB_MAINTPRGMTEMPTASK" FOREIGN KEY ("REVISION_DB_ID") REFERENCES "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.1:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD CONSTRAINT "FK_CRMIMDB_MAINTPRGMTEMPTASK" FOREIGN KEY ("CREATION_DB_ID") REFERENCES "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.1:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD CONSTRAINT "FK_CTMIMDB_MAINTPRGMTEMPTASK" FOREIGN KEY ("CTRL_DB_ID") REFERENCES "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.1:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD CONSTRAINT "FK_TASKTASK_MAINTPRGMTEMPTASK" FOREIGN KEY ("TASK_DB_ID","TASK_ID") REFERENCES "TASK_TASK" ("TASK_DB_ID","TASK_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.1:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD CONSTRAINT "FK_TASKDEFN_MAINTPRGMTEMPTASK" FOREIGN KEY ("TASK_DEFN_DB_ID","TASK_DEFN_ID") REFERENCES "TASK_DEFN" ("TASK_DEFN_DB_ID","TASK_DEFN_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.1:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD CONSTRAINT "FK_MIMRSTAT_MAINTPRGMTEMPTASK" FOREIGN KEY ("RSTAT_CD") REFERENCES "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet MX-24311.1:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
ALTER TABLE "MAINT_PRGM_TEMP_TASK" ADD CONSTRAINT "FK_MAINTPRGM_MAINTPRGMTEMPTASK" FOREIGN KEY ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID") REFERENCES "MAINT_PRGM" ("MAINT_PRGM_DB_ID","MAINT_PRGM_ID")  DEFERRABLE
');
END;
/

--changeSet MX-24311.1:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
DECLARE
   vExists   NUMBER(1);
BEGIN
   SELECT
      COUNT(1)
   INTO
      vExists
   FROM
      user_views
   WHERE
      view_name = 'VW_MAINT_PRGM_TEMP_TASK';
      
      IF vExists = 1 THEN

      EXECUTE IMMEDIATE '
         INSERT INTO
            maint_prgm_temp_task (
               maint_prgm_db_id,
               maint_prgm_id,
               task_defn_db_id,
               task_defn_id,
               task_db_id,
               task_id
            )
         SELECT
            vw_maint_prgm_temp_task.maint_prgm_db_id,
            vw_maint_prgm_temp_task.maint_prgm_id,
            vw_maint_prgm_temp_task.task_defn_db_id,
            vw_maint_prgm_temp_task.task_defn_id,
            vw_maint_prgm_temp_task.task_db_id,
            vw_maint_prgm_temp_task.task_id
         FROM
            vw_maint_prgm_temp_task
         WHERE
            NOT EXISTS(
               SELECT
                  1
               FROM
                  maint_prgm_temp_task
            )'
      ;

   END IF;
END;
/

--changeSet MX-24311.1:19 stripComments:false
-- This view returns the latest approved task revisions for a carrier
CREATE OR REPLACE VIEW vw_maint_prgm_carrier_task
AS
-- Get the revision specified in the active maintenance program
WITH maint_prgm_carrier_task_defn AS (
    SELECT
       maint_prgm_carrier_map.carrier_db_id,
       maint_prgm_carrier_map.carrier_id,
       maint_prgm_task.task_defn_db_id,
       maint_prgm_task.task_defn_id
    FROM
       maint_prgm_task
       INNER JOIN maint_prgm ON
          maint_prgm_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
          maint_prgm_task.maint_prgm_id = maint_prgm.maint_prgm_id
       INNER JOIN maint_prgm_carrier_map ON
          maint_prgm.maint_prgm_db_id = maint_prgm_carrier_map.maint_prgm_db_id AND
          maint_prgm.maint_prgm_id = maint_prgm_carrier_map.maint_prgm_id
       LEFT OUTER JOIN maint_prgm_temp_task ON
          maint_prgm_temp_task.maint_prgm_db_id = maint_prgm_task.maint_prgm_db_id AND
          maint_prgm_temp_task.maint_prgm_id = maint_prgm_task.maint_prgm_id AND
          maint_prgm_temp_task.task_defn_db_id = maint_prgm_task.task_defn_db_id AND
          maint_prgm_temp_task.task_defn_id = maint_prgm_task.task_defn_id
    WHERE
       -- task is issued as temp or assigned to latest maintenance program
       (
          (
             maint_prgm.maint_prgm_status_cd = 'REVISION'
             AND
             maint_prgm_temp_task.task_db_id IS NOT NULL
          )
          OR
          (
             maint_prgm_carrier_map.latest_revision_bool = 1
             AND
             maint_prgm_task.unassign_bool = 0
          )
       )
), maint_prgm_carrier_temp_task AS (
SELECT
   maint_prgm_carrier_map.carrier_db_id,
   maint_prgm_carrier_map.carrier_id,
   maint_prgm_temp_task.task_defn_db_id,
   maint_prgm_temp_task.task_defn_id,
   maint_prgm_temp_task.task_db_id,
   maint_prgm_temp_task.task_id
FROM
   maint_prgm
   INNER JOIN maint_prgm_carrier_map ON
      maint_prgm_carrier_map.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
      maint_prgm_carrier_map.maint_prgm_id = maint_prgm.maint_prgm_id
   INNER JOIN maint_prgm_temp_task ON
      maint_prgm_temp_task.maint_prgm_db_id = maint_prgm.maint_prgm_db_id AND
      maint_prgm_temp_task.maint_prgm_id = maint_prgm.maint_prgm_id
WHERE
   -- from the revision carrier maintenance program...
   maint_prgm.maint_prgm_status_db_id = 0 AND
   maint_prgm.maint_prgm_status_cd = 'REVISION'
)
SELECT
   org_carrier.carrier_db_id,
   org_carrier.carrier_id,
   maint_prgm_task.task_defn_db_id,
   maint_prgm_task.task_defn_id,
   maint_prgm_task.task_db_id,
   maint_prgm_task.task_id
FROM
   org_carrier
   INNER JOIN maint_prgm_carrier_map ON
      maint_prgm_carrier_map.carrier_db_id = org_carrier.carrier_db_id AND
      maint_prgm_carrier_map.carrier_id = org_carrier.carrier_id
   INNER JOIN maint_prgm_task ON
      maint_prgm_task.maint_prgm_db_id = maint_prgm_carrier_map.maint_prgm_db_id AND
      maint_prgm_task.maint_prgm_id = maint_prgm_carrier_map.maint_prgm_id
WHERE
   -- from the latest carrier maintenance program...
   maint_prgm_carrier_map.latest_revision_bool = 1
   AND
   -- get assigned tasks on the active maintenance program
   maint_prgm_task.unassign_bool = 0
   AND
   -- if the revision maintenance program has a new temp task, then use temp task instead
   NOT EXISTS (
      SELECT
         1
      FROM
         maint_prgm_carrier_temp_task
      WHERE
         maint_prgm_carrier_temp_task.task_defn_db_id = maint_prgm_task.task_defn_db_id AND
         maint_prgm_carrier_temp_task.task_defn_id = maint_prgm_task.task_defn_id
         AND
         maint_prgm_carrier_temp_task.carrier_db_id = org_carrier.carrier_db_id AND
         maint_prgm_carrier_temp_task.carrier_id = org_carrier.carrier_id
   )
UNION ALL
-- If the task defn is temporarily issued, use the temporarily issued task revision instead
SELECT
   maint_prgm_carrier_temp_task.carrier_db_id,
   maint_prgm_carrier_temp_task.carrier_id,
   maint_prgm_carrier_temp_task.task_defn_db_id,
   maint_prgm_carrier_temp_task.task_defn_id,
   maint_prgm_carrier_temp_task.task_db_id,
   maint_prgm_carrier_temp_task.task_id
FROM
   maint_prgm_carrier_temp_task
UNION ALL
-- If the task defn is only approved for other carriers,
-- initialized tasks on this carrier should be cancelled
SELECT
   org_carrier.carrier_db_id,
   org_carrier.carrier_id,
   task_defn.task_defn_db_id,
   task_defn.task_defn_id,
   NULL AS task_db_id,
   NULL AS task_id
FROM
   task_defn,
   (
      SELECT
         org_carrier.carrier_db_id,
         org_carrier.carrier_id
      FROM
         org_carrier
      UNION ALL
      SELECT
         NULL AS carrier_db_id,
         NULL AS carrier_id
      FROM
         dual
   ) org_carrier
WHERE
   -- The task defn is part of any ACTV MP
   EXISTS
   (
      SELECT 1 FROM
        maint_prgm_carrier_task_defn
      WHERE
        maint_prgm_carrier_task_defn.task_defn_db_id = task_defn.task_defn_db_id AND
        maint_prgm_carrier_task_defn.task_defn_id = task_defn.task_defn_id
   )
   AND
   NOT EXISTS
   (
      SELECT 1 FROM
        maint_prgm_carrier_task_defn
      WHERE
        maint_prgm_carrier_task_defn.task_defn_db_id = task_defn.task_defn_db_id AND
        maint_prgm_carrier_task_defn.task_defn_id = task_defn.task_defn_id
        AND
        maint_prgm_carrier_task_defn.carrier_db_id = org_carrier.carrier_db_id AND
        maint_prgm_carrier_task_defn.carrier_id = org_carrier.carrier_id
   )
;

--changeSet MX-24311.1:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.view_drop('VW_MAINT_PRGM_TEMP_TASK');
END;
/