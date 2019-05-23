--liquibase formatted sql


--changeSet DEV-2322:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_create('
      Create table "ORG_LABOUR_SKILL_MAP" (
   "ORG_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "ORG_ID" Number(10,0) NOT NULL DEFERRABLE  Check (ORG_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "LABOUR_SKILL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (LABOUR_SKILL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "LABOUR_SKILL_CD" Varchar2 (8) NOT NULL DEFERRABLE ,
   "EST_HOURLY_COST" Number(15,5),
   "ESIG_REQ_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (ESIG_REQ_BOOL IN (0, 1) ) DEFERRABLE ,
   "RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
   "REVISION_NO" Number(10,0) Default 1 NOT NULL DEFERRABLE ,
   "CTRL_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CTRL_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "CREATION_DT" Date NOT NULL DEFERRABLE ,
   "CREATION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (CREATION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "REVISION_DT" Date NOT NULL DEFERRABLE ,
   "REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
   "REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
 Constraint "PK_ORG_LABOUR_SKILL_MAP" primary key ("ORG_DB_ID","ORG_ID","LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") 
)
');
END;
/

--changeSet DEV-2322:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_create('
      Create Index "IX_ORGORG_ORGSKILLMAP" ON "ORG_LABOUR_SKILL_MAP" ("ORG_DB_ID","ORG_ID")
   ');
END;
/

--changeSet DEV-2322:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "ORG_LABOUR_SKILL_MAP" add Constraint "FK_CTMIMDB_ORGLBRSKILLMAP" foreign key ("CTRL_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2322:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "ORG_LABOUR_SKILL_MAP" add Constraint "FK_CRMIMDB_ORGLBRSKILLMAP" foreign key ("CREATION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2322:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      Alter table "ORG_LABOUR_SKILL_MAP" add Constraint "FK_RVMIMDB_ORGLBRSKILLMAP" foreign key ("REVISION_DB_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2322:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_LABOUR_SKILL_MAP" add Constraint "FK_REFLBRSKILL_ORGLBRSKILLMAP" foreign key ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD") references "REF_LABOUR_SKILL" ("LABOUR_SKILL_DB_ID","LABOUR_SKILL_CD")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2322:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_LABOUR_SKILL_MAP" add Constraint "FK_MIMRSTAT_ORGSKILLMAP" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2322:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
Alter table "ORG_LABOUR_SKILL_MAP" add Constraint "FK_ORGORG_ORGLBRSKILLMAP" foreign key ("ORG_DB_ID","ORG_ID") references "ORG_ORG" ("ORG_DB_ID","ORG_ID")  DEFERRABLE
   ');
END;
/

--changeSet DEV-2322:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ORG_LBR_SKILL_MAP" BEFORE INSERT
   ON "ORG_LABOUR_SKILL_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-2322:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ORG_LBR_SKILL_MAP" BEFORE UPDATE
   ON "ORG_LABOUR_SKILL_MAP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet DEV-2322:11 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
   select 0, 1,  0, 'ENG', 75.0, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   from dual where not exists (select 1 from org_labour_skill_map where org_db_id = 0 and org_id = 1 and labour_skill_db_id = 0 and labour_skill_cd = 'ENG');

--changeSet DEV-2322:12 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
   select 0, 1,  0, 'LBR', 75.0, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   from dual where not exists (select 1 from org_labour_skill_map where org_db_id = 0 and org_id = 1 and labour_skill_db_id = 0 and labour_skill_cd = 'LBR');

--changeSet DEV-2322:13 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
   select 0, 1,  0, 'PILOT', 100.0, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   from dual where not exists (select 1 from org_labour_skill_map where org_db_id = 0 and org_id = 1 and labour_skill_db_id = 0 and labour_skill_cd = 'PILOT');

--changeSet DEV-2322:14 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
   select 0, 2,  0, 'ENG', 75.0, 1, 0, 1, 0, sysdate, 0, sysdate,0, 'MXI'
   from dual where not exists (select 1 from org_labour_skill_map where org_db_id = 0 and org_id = 2 and labour_skill_db_id = 0 and labour_skill_cd = 'ENG');

--changeSet DEV-2322:15 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
   select 0, 2,  0, 'LBR', 75.0, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   from dual where not exists (select 1 from org_labour_skill_map where org_db_id = 0 and org_id = 2 and labour_skill_db_id = 0 and labour_skill_cd = 'LBR');

--changeSet DEV-2322:16 stripComments:false
insert into org_labour_skill_map(org_db_id, org_id, labour_skill_db_id, labour_skill_cd, est_hourly_cost, esig_req_bool, rstat_cd, revision_no, ctrl_db_id, creation_dt, creation_db_id, revision_dt, revision_db_id, revision_user)
   select 0, 2,  0, 'PILOT', 100.0, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
   from dual where not exists (select 1 from org_labour_skill_map where org_db_id = 0 and org_id = 2 and labour_skill_db_id = 0 and labour_skill_cd = 'PILOT');   

--changeSet DEV-2322:17 stripComments:false
INSERT INTO org_labour_skill_map (org_db_id,org_id,labour_skill_db_id,labour_skill_cd,est_hourly_cost,esig_req_bool)
   SELECT 
      org.org_db_id,
      org.org_id,
      skill.labour_skill_db_id,
      skill.labour_skill_cd,
      skill.est_hourly_cost,
      skill.esig_req_bool
   FROM
      (
         SELECT 
            org_db_id,
            org_id 
         FROM 
            org_org 
      ) org,
      (  SELECT 
            labour_skill_db_id,
            labour_skill_cd,
            est_hourly_cost,
            esig_req_bool 
         FROM 
            Ref_Labour_Skill
      ) skill
   -- Do not insert records if they already exist in the org_labour_skill_map table
   WHERE 
      NOT EXISTS 
         (
            SELECT 
               1
            FROM 
               org_labour_skill_map 
            WHERE 
               org_labour_skill_map.org_db_id = org.org_db_id AND
               org_labour_skill_map.org_id = org.org_id AND 
               org_labour_skill_map.labour_skill_db_id = skill.labour_skill_db_id AND
               org_labour_skill_map.labour_skill_cd = skill.labour_skill_cd
         );