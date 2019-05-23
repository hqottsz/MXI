--liquibase formatted sql

--changeset OPER-5154-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_create('
	CREATE TABLE INV_REMOVE
	  (
	    INV_REMOVE_ID RAW (16) DEFAULT sys_guid() NOT NULL ,
	    EVENT_DB_ID         NUMBER (10) NOT NULL ,
	    EVENT_ID            NUMBER (10) NOT NULL ,
	    INV_NO_DB_ID        NUMBER (10) NOT NULL ,
	    INV_NO_ID           NUMBER (10) NOT NULL ,
	    NH_INV_NO_DB_ID     NUMBER (10) ,
	    NH_INV_NO_ID        NUMBER (10) ,
	    ASSMBL_INV_NO_DB_ID NUMBER (10) ,
	    ASSMBL_INV_NO_ID    NUMBER (10) ,
	    H_INV_NO_DB_ID      NUMBER (10) ,
	    H_INV_NO_ID         NUMBER (10) ,
	    EVENT_DT            DATE ,
	    MAIN_INV_BOOL       NUMBER (1) DEFAULT 0 NOT NULL ,
	    RSTAT_CD            NUMBER (3) NOT NULL ,
	    CTRL_DB_ID          NUMBER (10) NOT NULL ,
	    REVISION_NO         NUMBER (10) NOT NULL ,
	    CREATION_DB_ID      NUMBER (10) NOT NULL ,
	    CREATION_DT         DATE NOT NULL ,
	    REVISION_DT         DATE NOT NULL ,
	    REVISION_DB_ID      NUMBER (10) NOT NULL ,
	    REVISION_USER       VARCHAR2 (30) NOT NULL
	  )
   ');

END;
/

--changeset OPER-5154-2:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraint to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT CK_INV_RMVL_RSTAT_CD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');

END;
/  

--changeset OPER-5154-2:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraint to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT CK_INV_RMVL_CTRL_DB_ID CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');

END;
/  

--changeset OPER-5154-2:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraint to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT CK_INV_RMVL_CREATION_DB_ID CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
   ');

END;
/  

--changeset OPER-5154-2:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraint to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT CK_INV_RMVL_REVISION_DB_ID CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
   ');

END;
/  
  
--changeset OPER-5154-2:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index IX_INVRMVL_HINV_EVTDT
BEGIN

   utl_migr_schema_pkg.index_create('
	  CREATE INDEX IX_INVRMVL_HINV_EVTDT ON INV_REMOVE
	    (
	      H_INV_NO_DB_ID ASC ,
	      H_INV_NO_ID ASC ,
	      EVENT_DT ASC
	    ) 
   ');

END;
/

--changeset OPER-5154-2:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index IX_INVRMVL_EVENT_ID
BEGIN

   utl_migr_schema_pkg.index_create('
	  CREATE INDEX IX_INVRMVL_EVENT_ID ON INV_REMOVE
	    (
	      EVENT_DB_ID ASC ,
	      EVENT_ID ASC
	    )
   ');

END;
/

--changeset OPER-5154-2:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index IX_INVRMVL_INV_ID
BEGIN

   utl_migr_schema_pkg.index_create('
	  CREATE INDEX IX_INVRMVL_INV_ID ON INV_REMOVE
	    (
	      INV_NO_DB_ID ASC ,
	      INV_NO_ID ASC
	    )
   ');

END;
/

--changeset OPER-5154-2:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index IX_INVRMVL_H_INV_ID
BEGIN

   utl_migr_schema_pkg.index_create('
	  CREATE INDEX IX_INVRMVL_H_INV_ID ON INV_REMOVE
	    (
	      H_INV_NO_DB_ID ASC ,
	      H_INV_NO_ID ASC
	    )
   ');

END;
/

--changeset OPER-5154-2:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index IX_INVRMVL_ASSMBL_INV_ID
BEGIN

   utl_migr_schema_pkg.index_create('
	  CREATE INDEX IX_INVRMVL_ASSMBL_INV_ID ON INV_REMOVE
	    (
	      ASSMBL_INV_NO_DB_ID ASC ,
	      ASSMBL_INV_NO_ID ASC
	    )
   ');

END;
/

--changeset OPER-5154-2:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index IX_INVRMVL_NH_INV_ID
BEGIN

   utl_migr_schema_pkg.index_create('
	  CREATE INDEX IX_INVRMVL_NH_INV_ID ON INV_REMOVE
	    (
	      NH_INV_NO_DB_ID ASC ,
	      NH_INV_NO_ID ASC
	    )
   ');

END;
/

--changeset OPER-5154-2:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary key constraint to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT PK_INV_REMOVE PRIMARY KEY ( INV_REMOVE_ID )
   ');

END;
/

--changeset OPER-5154-2:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INVREMOVE_EVTEVENT to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT FK_INVREMOVE_EVTEVENT FOREIGN KEY ( EVENT_DB_ID, EVENT_ID ) REFERENCES EVT_EVENT ( EVENT_DB_ID, EVENT_ID ) NOT DEFERRABLE 
   ');

END;
/

--changeset OPER-5154-2:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INVREMOVE_INVINV_v1 to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT FK_INVREMOVE_INVINV_v1 FOREIGN KEY ( ASSMBL_INV_NO_DB_ID, ASSMBL_INV_NO_ID ) REFERENCES INV_INV ( INV_NO_DB_ID, INV_NO_ID ) NOT DEFERRABLE 
   ');

END;
/

--changeset OPER-5154-2:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INVREMOVE_INVINV_v2 to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT FK_INVREMOVE_INVINV_v2 FOREIGN KEY ( H_INV_NO_DB_ID, H_INV_NO_ID ) REFERENCES INV_INV ( INV_NO_DB_ID, INV_NO_ID ) NOT DEFERRABLE 
   ');

END;
/

--changeset OPER-5154-2:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INVREMOVE_INVINV_v3 to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT FK_INVREMOVE_INVINV_v3 FOREIGN KEY ( NH_INV_NO_DB_ID, NH_INV_NO_ID ) REFERENCES INV_INV ( INV_NO_DB_ID, INV_NO_ID ) NOT DEFERRABLE 
   ');

END;
/

--changeset OPER-5154-2:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INVREMOVE_INVINV_v4 to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT FK_INVREMOVE_INVINV_v4 FOREIGN KEY ( INV_NO_DB_ID, INV_NO_ID ) REFERENCES INV_INV ( INV_NO_DB_ID, INV_NO_ID ) NOT DEFERRABLE 
   ');

END;
/

--changeset OPER-5154-2:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INVREMOVE_MIMRSTAT to table INV_REMOVE
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_REMOVE ADD CONSTRAINT FK_INVREMOVE_MIMRSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE 
   ');

END;
/

--changeset OPER-5154-2:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create trigger TIBR_INV_REMOVE
CREATE OR REPLACE TRIGGER "TIBR_INV_REMOVE" BEFORE INSERT
 ON "INV_REMOVE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeset OPER-5154-2:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create trigger TUBR_INV_REMOVE
CREATE OR REPLACE TRIGGER "TUBR_INV_REMOVE" BEFORE UPDATE
   ON "INV_REMOVE" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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






