--liquibase formatted sql


--changeSet DEV-1076:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- create table
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- Do NOT create REF_STEP_STATUS in migration scripts because its structure (in 8.0+, we have two 
-- more columns CTRL_DB_ID and CREATION_DB_ID) is different than in 7x. Even we have creation 
-- scripts here, it won't run because REF_STEP_STATUS is already in 7x database when we migrate 
-- from 7x to the trunk. 
--
-- What we need to do for the migration on REF_STEP_STATUS is to add those two columns CTRL_DB_ID 
-- and CREATION_DB_ID as the scripts below.
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- drop all old triggers and constraints from REF_STEP_STATUS
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- drop triggers
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TIBR_REF_STEP_STATUS');
END;
/

--changeSet DEV-1076:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TRIGGER_DROP('TUBR_REF_STEP_STATUS');
END;
/

--changeSet DEV-1076:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop foreign key constrains with REF_STEP_STATUS
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_STEP', 'FK_REFSTEPSTATUS_SCHEDSTEP'); 
END;
/

--changeSet DEV-1076:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('SCHED_LABOUR_STEP', 'FK_REFSTEPSTATUS_SCHEDLBRSTEP'); 
END;
/

--changeSet DEV-1076:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop primary key (db_idcd) from REF_STEP_STATUS
BEGIN
   UTL_MIGR_SCHEMA_PKG.TABLE_CONSTRAINT_DROP('REF_STEP_STATUS', 'PK_REF_STEP_STATUS');
END;
/

--changeSet DEV-1076:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.INDEX_DROP('PK_REF_STEP_STATUS'); 
END;
/

--changeSet DEV-1076:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- apply structure changes to REF_STEP_STATUS 
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- modify column
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table REF_STEP_STATUS modify (   
  "STEP_STATUS_CD" Varchar2 (20) NOT NULL DEFERRABLE
)  
');
END;
/ 

--changeSet DEV-1076:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop column
BEGIN
utl_migr_schema_pkg.table_column_drop('REF_STEP_STATUS', 'STEP_STATUS_DB_ID');
END;
/

--changeSet DEV-1076:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add columns
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REF_STEP_STATUS add (
	"REVISION_NO" Number(10,0)
)
');
END;
/

--changeSet DEV-1076:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REF_STEP_STATUS add (
	"CTRL_DB_ID" Number(10,0) Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1076:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table REF_STEP_STATUS add (
	"CREATION_DB_ID" Number(10,0) Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1076:12 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- run migrations on REF_STEP_STATUS
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- update REF_STEP_STATUS to set REVISION_NO=1, CTRL_DB_ID=0, and CREATION_DB_ID=0
UPDATE REF_STEP_STATUS SET REVISION_NO = 1 WHERE CTRL_DB_ID IS NULL;

--changeSet DEV-1076:13 stripComments:false
UPDATE REF_STEP_STATUS SET CTRL_DB_ID = 0 WHERE CTRL_DB_ID IS NULL;

--changeSet DEV-1076:14 stripComments:false
UPDATE REF_STEP_STATUS SET CREATION_DB_ID = 0 WHERE CREATION_DB_ID IS NULL;

--changeSet DEV-1076:15 stripComments:false
-- update REF_STEP_STATUS to add 'MX' to the ref code
UPDATE 
  REF_STEP_STATUS 
SET 
  STEP_STATUS_CD = 'MX' || STEP_STATUS_CD 
WHERE 
  STEP_STATUS_CD IS NOT NULL AND 
  STEP_STATUS_CD NOT LIKE 'MX%';  

--changeSet DEV-1076:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- change the columns to not null
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table REF_STEP_STATUS modify (
     "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
END;
/ 

--changeSet DEV-1076:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table REF_STEP_STATUS modify (
     "REVISION_NO" Number(10,0) NOT NULL DEFERRABLE
  )
  ');
END;
/ 

--changeSet DEV-1076:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table REF_STEP_STATUS modify (
     "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
END;
/   

--changeSet DEV-1076:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- add contraints to REF_STEP_STATUS
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- add primary key (cd) for REF_STEP_STATUS
BEGIN
utl_migr_schema_pkg.table_constraint_add('
  Alter table "REF_STEP_STATUS" add constraint "PK_REF_STEP_STATUS" Primary key ("STEP_STATUS_CD")
');
END;
/

--changeSet DEV-1076:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add foreign key for REF_STEP_STATUS
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_STEP_STATUS" add Constraint "FK_MIMDB_REFSTEPSTATUS" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1076:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "REF_STEP_STATUS" add Constraint "FK_MIMRSTAT_REFSTEPSTATUS" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1076:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- add triggers to REF_STEP_STATUS
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
CREATE OR REPLACE TRIGGER "TIBR_REF_STEP_STATUS" BEFORE INSERT
   ON "REF_STEP_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1076:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_REF_STEP_STATUS" BEFORE UPDATE
   ON "REF_STEP_STATUS" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-1076:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- apply structure changes to SCHED_STEP
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- drop column
BEGIN
utl_migr_schema_pkg.table_column_drop('SCHED_STEP', 'STEP_STATUS_DB_ID');
END;
/

--changeSet DEV-1076:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add column
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_STEP add (
		"STEP_STATUS_CD" Varchar2 (20)
)
');
END;
/

--changeSet DEV-1076:26 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- run migrations on SCHED_STEP
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- set the value of new column for all existing rows in sched_step table. 
-- for each sched_step, 
--    if there is a matching sched_labour_step row, set status to "MXCOMPLETE"
--    if there is no matching sched_labour_step row, set status to "MXPENDING".
UPDATE 
   sched_step 
SET 
   sched_step.step_status_cd = 
      DECODE 
      (
        (
          SELECT 
             1 
          FROM
             sched_labour_step  
          WHERE   
             sched_labour_step.sched_db_id = sched_step.sched_db_id AND   
             sched_labour_step.sched_id    = sched_step.sched_id
             AND
             ROWNUM = 1
        ), 
        1, 'MXCOMPLETE', 
        null, 'MXPENDING'
     )
WHERE
   sched_step.step_status_cd IS NULL;


--changeSet DEV-1076:27 stripComments:false
-- update sched_step to add 'MX' to the ref code
UPDATE 
  SCHED_STEP 
SET 
  STEP_STATUS_CD = 'MX' || STEP_STATUS_CD 
WHERE 
  STEP_STATUS_CD IS NOT NULL AND 
  STEP_STATUS_CD NOT LIKE 'MX%';

--changeSet DEV-1076:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- change the column to not null
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table SCHED_STEP modify (
     "STEP_STATUS_CD" Varchar2 (20) NOT NULL DEFERRABLE
  )
  ');
END;
/

--changeSet DEV-1076:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- add foreign key contraint to SCHED_STEP
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_STEP" add Constraint "FK_REFSTEPSTATUS_SCHEDSTEP" foreign key ("STEP_STATUS_CD") references "REF_STEP_STATUS" ("STEP_STATUS_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1076:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- apply structure changes to SCHED_LABOUR_STEP
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- drop column
BEGIN
utl_migr_schema_pkg.table_column_drop('SCHED_LABOUR_STEP', 'STEP_STATUS_DB_ID');
END;
/

--changeSet DEV-1076:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add columns
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_LABOUR_STEP add (
	"NOTES_LDESC" Varchar2 (4000)
)
');
END;
/

--changeSet DEV-1076:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_LABOUR_STEP add (
	"STEP_STATUS_CD" Varchar2 (20)
)
');
END;
/

--changeSet DEV-1076:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table SCHED_LABOUR_STEP add (
	"ORD_ID" Number(10,0) Check (ORD_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
)
');
END;
/

--changeSet DEV-1076:34 stripComments:false
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- run updatesinserts on SCHED_LABOUR_STEP
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- NOTE: THIS SCRIPT ONLY RUNS ONCE: If there is one NOT-NULL step_status_cd, it will not run.
-- This avoids this script running after user inserts new rows in sched_labour_step 
-- otherwise, it would overwrites the existing step_status_db_id.
-- within all existing rows:
--    for each sched_labour_step, set step_status_db_id to 0 and step_status_cd to 'COMPLETE'
UPDATE
   sched_labour_step
SET 
   sched_labour_step.step_status_cd = 'MXCOMPLETE'
WHERE NOT EXISTS
  ( 
     SELECT 
        1
     FROM
        sched_labour_step 
     WHERE
        sched_labour_step.step_status_cd IS NOT NULL
  )
;

--changeSet DEV-1076:35 stripComments:false
-- update SCHED_LABOUR_STEP to add 'MX' to the ref code
UPDATE 
  SCHED_LABOUR_STEP 
SET 
  STEP_STATUS_CD = 'MX' || STEP_STATUS_CD 
WHERE 
  STEP_STATUS_CD IS NOT NULL AND 
  STEP_STATUS_CD NOT LIKE 'MX%';  

--changeSet DEV-1076:36 stripComments:false
-- NOTE: THIS SCRIPT ONLY RUNS ONCE: If there is one NOT-NULL ord_id, it will not run.
-- This avoids this script running after user inserts new rows in sched_labour_step 
-- otherwise, it would overwrites the existing ord_id.
-- within all existing rows:
--    for each set of sched_labour_step sharing the same sched_step FK, 
--       set ord_id in chronological order (starting at 1) of the creation_dt
MERGE INTO 
   sched_labour_step 
USING 
   ( 
     SELECT  
        labour_db_id, 
        labour_id, 
        sched_db_id, 
        sched_id, 
        step_id, 
        row_number() 
           over 
           (
              PARTITION BY
                 sched_db_id, 
                 sched_id,
                 step_id
              ORDER BY 
                 creation_dt
            ) AS ord_id
     FROM 
        sched_labour_step
   ) sched_labour_step_ord_id
ON 
(
   sched_labour_step.labour_db_id = sched_labour_step_ord_id.labour_db_id AND 
   sched_labour_step.labour_id    = sched_labour_step_ord_id.labour_id AND
   sched_labour_step.sched_db_id  = sched_labour_step_ord_id.sched_db_id AND
   sched_labour_step.sched_id     = sched_labour_step_ord_id.sched_id AND 
   sched_labour_step.step_id      = sched_labour_step_ord_id.step_id
)
WHEN MATCHED THEN
   UPDATE
   SET 
      sched_labour_step.ord_id = sched_labour_step_ord_id.ord_id
WHERE NOT EXISTS
  ( 
     SELECT 
        1
     FROM
        sched_labour_step 
     WHERE
        ord_id IS NOT NULL
  )
;

--changeSet DEV-1076:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- change the columns to not null
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table SCHED_LABOUR_STEP modify (
     "STEP_STATUS_CD" Varchar2 (20) NOT NULL DEFERRABLE
  )
  ');
END;
/ 

--changeSet DEV-1076:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_column_modify('
  Alter table SCHED_LABOUR_STEP modify (
     "ORD_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORD_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
  )
  ');
END;
/

--changeSet DEV-1076:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
-- add foreign key contraint to SCHED_LABOUR_STEP
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- 
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "SCHED_LABOUR_STEP" add Constraint "FK_REFSTEPSTATUS_SCHEDLBRSTEP" foreign key ("STEP_STATUS_CD") references "REF_STEP_STATUS" ("STEP_STATUS_CD")  DEFERRABLE
');
END;
/