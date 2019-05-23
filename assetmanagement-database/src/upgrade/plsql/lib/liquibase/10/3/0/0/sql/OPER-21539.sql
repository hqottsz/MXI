--liquibase formatted sql

--changeSet OPER-21539:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_create('
		CREATE TABLE INV_AC_REG_TO_FP
		(
			INV_NO_DB_ID   NUMBER (10) NOT NULL ,
			INV_NO_ID      NUMBER (10) NOT NULL ,
			EXPORT_BOOL    NUMBER (1) NOT NULL ,
			RSTAT_CD       NUMBER (3) DEFAULT 0 NOT NULL ,
			REVISION_NO    NUMBER (10) NOT NULL ,
			CTRL_DB_ID     NUMBER (10) NOT NULL ,
			CREATION_DT    DATE NOT NULL ,
			CREATION_DB_ID NUMBER (10) NOT NULL ,
			REVISION_DT    DATE NOT NULL ,
			REVISION_DB_ID NUMBER (10) NOT NULL ,
			REVISION_USER  VARCHAR2 (30) NOT NULL
		)
	');
END;
/

--changeSet OPER-21539:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CHECK ( EXPORT_BOOL IN (0, 1))
	');
END;
/

--changeSet OPER-21539:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
	');
END;
/

--changeSet OPER-21539:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-21539:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-21539:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-21539:7 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.INV_NO_DB_ID IS 'The aircraft inventory''s database id.';

--changeSet OPER-21539:8 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.INV_NO_ID IS 'The aircraft inventory''s id.';

--changeSet OPER-21539:9 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.EXPORT_BOOL IS 'A boolean indicating whether or not the associated aircraft inventory shall be exported to Fleet Planner.';

--changeSet OPER-21539:10 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.RSTAT_CD IS 'Status of the record.';

--changeSet OPER-21539:11 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.REVISION_NO IS 'A number incremented each time the record is modified.';

--changeSet OPER-21539:12 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.CTRL_DB_ID IS 'The identifier of the database that owns the record.';

--changeSet OPER-21539:13 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.CREATION_DT IS 'The date and time at which the record was inserted.';

--changeSet OPER-21539:14 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.CREATION_DB_ID IS 'The identifier of the database that inserted the record.';

--changeSet OPER-21539:15 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.REVISION_DT IS 'The date and time at which the record was last updated.';

--changeSet OPER-21539:16 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.REVISION_DB_ID IS 'The identifier of the database that last updated the record.';

--changeSet OPER-21539:17 stripComments:false
COMMENT ON COLUMN INV_AC_REG_TO_FP.REVISION_USER IS 'The name of the user that last updated the record.';

--changeSet OPER-21539:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CONSTRAINT PK_INV_AC_REG_TO_FP PRIMARY KEY ( INV_NO_DB_ID, INV_NO_ID )
	');
END;
/

--changeSet OPER-21539:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_create('
		CREATE TABLE INV_LOC_TO_FP
		(
			LOC_DB_ID      NUMBER (10) NOT NULL ,
			LOC_ID         NUMBER (10) NOT NULL ,
			EXPORT_BOOL    NUMBER (1) NOT NULL ,
			RSTAT_CD       NUMBER (3) DEFAULT 0 NOT NULL ,
			REVISION_NO    NUMBER (10) NOT NULL ,
			CTRL_DB_ID     NUMBER (10) NOT NULL ,
			CREATION_DT    DATE NOT NULL ,
			CREATION_DB_ID NUMBER (10) NOT NULL ,
			REVISION_DT    DATE NOT NULL ,
			REVISION_DB_ID NUMBER (10) NOT NULL ,
			REVISION_USER  VARCHAR2 (30) NOT NULL
		)
	');
END;
/

--changeSet OPER-21539:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CHECK ( EXPORT_BOOL IN (0, 1))
	');
END;
/

--changeSet OPER-21539:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CHECK ( RSTAT_CD    IN (0, 1, 2, 3))
	');
END;
/

--changeSet OPER-21539:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-21539:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-21539:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-21539:25 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.LOC_DB_ID IS 'The location''s database id.';

--changeSet OPER-21539:26 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.LOC_ID IS 'The location''s id.';

--changeSet OPER-21539:27 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.EXPORT_BOOL IS 'A boolean indicating whether or not the associated location shall be exported to Fleet Planner.';

--changeSet OPER-21539:28 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.RSTAT_CD IS 'Status of the record.';

--changeSet OPER-21539:29 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.REVISION_NO IS 'A number incremented each time the record is modified.';

--changeSet OPER-21539:30 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.CTRL_DB_ID IS 'The identifier of the database that owns the record.';

--changeSet OPER-21539:31 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.CREATION_DT IS 'The date and time at which the record was inserted.';

--changeSet OPER-21539:32 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.CREATION_DB_ID IS 'The identifier of the database that inserted the record.';

--changeSet OPER-21539:33 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.REVISION_DT IS 'The date and time at which the record was last updated.';

--changeSet OPER-21539:34 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.REVISION_DB_ID IS 'The identifier of the database that last updated the record.';

--changeSet OPER-21539:35 stripComments:false
COMMENT ON COLUMN INV_LOC_TO_FP.REVISION_USER IS 'The name of the user that last updated the record.';

--changeSet OPER-21539:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CONSTRAINT PK_INV_LOC_TO_FP PRIMARY KEY ( LOC_DB_ID, LOC_ID )
	');
END;
/

--changeSet OPER-21539:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_create('
		CREATE TABLE TASK_DEFN_TO_FP
		(
			TASK_DEFN_DB_ID NUMBER (10) NOT NULL ,
			TASK_DEFN_ID    NUMBER (10) NOT NULL ,
			EXPORT_BOOL     NUMBER (1) NOT NULL ,
			RSTAT_CD        NUMBER (3) DEFAULT 0 NOT NULL ,
			REVISION_NO     NUMBER (10) NOT NULL ,
			CTRL_DB_ID      NUMBER (10) NOT NULL ,
			CREATION_DT     DATE NOT NULL ,
			CREATION_DB_ID  NUMBER (10) NOT NULL ,
			REVISION_DT     DATE NOT NULL ,
			REVISION_DB_ID  NUMBER (10) NOT NULL ,
			REVISION_USER   VARCHAR2 (30) NOT NULL
		)
	');
END;
/

--changeSet OPER-21539:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CHECK ( EXPORT_BOOL IN (0, 1))
	');
END;
/

--changeSet OPER-21539:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CHECK ( RSTAT_CD    IN (0, 1, 2, 3))
	');
END;
/

--changeSet OPER-21539:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-21539:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-21539:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
	');
END;
/

--changeSet OPER-21539:43 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.TASK_DEFN_DB_ID IS 'The task definition''s database id.';

--changeSet OPER-21539:44 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.TASK_DEFN_ID IS 'The task definition''s id.';

--changeSet OPER-21539:45 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.EXPORT_BOOL IS 'A boolean indicating whether or not the associated task definition shall be exported to Fleet Planner.';

--changeSet OPER-21539:46 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.RSTAT_CD IS 'Status of the record.';

--changeSet OPER-21539:47 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.REVISION_NO IS 'A number incremented each time the record is modified.';

--changeSet OPER-21539:48 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.CTRL_DB_ID IS 'The identifier of the database that owns the record.';

--changeSet OPER-21539:49 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.CREATION_DT IS 'The date and time at which the record was inserted.';

--changeSet OPER-21539:50 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.CREATION_DB_ID IS 'The identifier of the database that inserted the record.';

--changeSet OPER-21539:51 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.REVISION_DT IS 'The date and time at which the record was last updated.';

--changeSet OPER-21539:52 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.REVISION_DB_ID IS 'The identifier of the database that last updated the record.';

--changeSet OPER-21539:53 stripComments:false
COMMENT ON COLUMN TASK_DEFN_TO_FP.REVISION_USER IS 'The name of the user that last updated the record.';

--changeSet OPER-21539:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CONSTRAINT PK_TASK_DEFN_TO_FP PRIMARY KEY ( TASK_DEFN_DB_ID, TASK_DEFN_ID )
	');
END;
/

--changeSet OPER-21539:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CONSTRAINT FK_INVACREG_INVACREGTOFP FOREIGN KEY ( INV_NO_DB_ID, INV_NO_ID ) REFERENCES INV_AC_REG ( INV_NO_DB_ID, INV_NO_ID )
	');
END;
/

--changeSet OPER-21539:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CONSTRAINT FK_MIMDB_INVACREGTOFP_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/


--changeSet OPER-21539:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CONSTRAINT FK_MIMDB_INVACREGTOFP_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/


--changeSet OPER-21539:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CONSTRAINT FK_MIMDB_INVACREGTOFP_RE FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/


--changeSet OPER-21539:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_AC_REG_TO_FP ADD CONSTRAINT FK_MIMRSTAT_INVACREGTOFP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD )
	');
END;
/


--changeSet OPER-21539:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CONSTRAINT FK_INVLOC_INVLOCTOFP FOREIGN KEY ( LOC_DB_ID, LOC_ID ) REFERENCES INV_LOC ( LOC_DB_ID, LOC_ID )
	');
END;
/

--changeSet OPER-21539:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CONSTRAINT FK_MIMDB_INVLOCTOFP_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-21539:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CONSTRAINT FK_MIMDB_INVLOCTOFP_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-21539:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CONSTRAINT FK_MIMDB_INVLOCTOFP_RE FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-21539:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE INV_LOC_TO_FP ADD CONSTRAINT FK_MIMRSTAT_INVLOCTOFP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD )
	');
END;
/

--changeSet OPER-21539:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CONSTRAINT FK_TASKDEFN_TASKDEFNTOFP FOREIGN KEY ( TASK_DEFN_DB_ID, TASK_DEFN_ID ) REFERENCES TASK_DEFN ( TASK_DEFN_DB_ID, TASK_DEFN_ID )
	');
END;
/

--changeSet OPER-21539:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CONSTRAINT FK_MIMDB_TASKDEFNTOFP_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-21539:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CONSTRAINT FK_MIMDB_TASKDEFNTOFP_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-21539:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CONSTRAINT FK_MIMDB_TASKDEFNTOFP_RE FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID )
	');
END;
/

--changeSet OPER-21539:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
	upg_migr_schema_v1_pkg.table_constraint_add('
		ALTER TABLE TASK_DEFN_TO_FP ADD CONSTRAINT FK_MIMRSTAT_TASKDEFNTOFP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD )
	');
END;
/

--changeSet OPER-21539:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
    upg_migr_schema_v1_pkg.index_create('
        CREATE UNIQUE INDEX IX_EXPORTBOOL_INVACREGTOFP ON INV_AC_REG_TO_FP ( 
            INV_NO_ID ASC, 
            INV_NO_DB_ID ASC, 
            EXPORT_BOOL ASC 
        )
    ');
END;
/

--changeSet OPER-21539:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
    upg_migr_schema_v1_pkg.index_create('
        CREATE UNIQUE INDEX IX_EXPORTBOOL_TASKDEFNTOFP ON TASK_DEFN_TO_FP ( 
            TASK_DEFN_ID ASC, 
            TASK_DEFN_DB_ID ASC, 
            EXPORT_BOOL ASC 
        )
    ');
END;
/

--changeSet OPER-21539:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN 
    upg_migr_schema_v1_pkg.index_create('
        CREATE UNIQUE INDEX IX_EXPORTBOOL_INVLOCTOFP ON INV_LOC_TO_FP ( 
            LOC_ID ASC, 
            LOC_DB_ID ASC, 
            EXPORT_BOOL ASC 
        )
    ');
END;
/

--changeSet OPER-21539:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_INV_AC_REG_TO_FP" BEFORE UPDATE
   ON "INV_AC_REG_TO_FP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-21539:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_AC_REG_TO_FP" BEFORE INSERT
   ON "INV_AC_REG_TO_FP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-21539:75 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_TASK_DEFN_TO_FP" BEFORE UPDATE
   ON "TASK_DEFN_TO_FP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-21539:76 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_TASK_DEFN_TO_FP" BEFORE INSERT
   ON "TASK_DEFN_TO_FP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-21539:77 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_INV_LOC_TO_FP" BEFORE UPDATE
   ON "INV_LOC_TO_FP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-21539:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INV_LOC_TO_FP" BEFORE INSERT
   ON "INV_LOC_TO_FP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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
