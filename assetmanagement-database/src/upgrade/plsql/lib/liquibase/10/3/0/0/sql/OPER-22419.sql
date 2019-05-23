--liquibase formatted sql
--changeset OPER-22419:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Table holds association between inventories with each inventory belonging to only one association at any point in time.
BEGIN

   upg_migr_schema_v1_pkg.table_create('
      Create table INV_ASSOCIATION 
	    (
		   INV_NO_DB_ID   NUMBER (10) NOT NULL ,
	       INV_NO_ID      NUMBER (10) NOT NULL ,
           ASSOCIATION_ID    NUMBER (10) NOT NULL ,
		   RSTAT_CD       NUMBER (3) NOT NULL ,
		   REVISION_NO    NUMBER (10) NOT NULL ,
		   CTRL_DB_ID     NUMBER (10) NOT NULL ,
		   CREATION_DT    DATE NOT NULL ,
		   CREATION_DB_ID NUMBER (10) NOT NULL ,
		   REVISION_DT    DATE NOT NULL ,
		   REVISION_DB_ID NUMBER (10) NOT NULL ,
		   REVISION_USER  VARCHAR2 (30) NOT NULL
      )
	  LOGGING
   ');
END;
/

--changeset OPER-22419:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add constraint to table INV_ASSOCIATION
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_ASSOCIATION ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3) )
   ');

END;
/ 

--changeset OPER-22419:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary key constraint to table INV_ASSOCIATION
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_ASSOCIATION ADD CONSTRAINT PK_INV_ASSOCIATION PRIMARY KEY ( INV_NO_ID, INV_NO_DB_ID )
   ');

END;
/ 

--changeset OPER-22419-1:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INV_ASSOCIATION_INV_INV to table INV_ASSOCIATION
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_ASSOCIATION ADD CONSTRAINT FK_INV_ASSOCIATION_INV_INV FOREIGN KEY ( INV_NO_DB_ID, INV_NO_ID ) REFERENCES INV_INV ( INV_NO_DB_ID, INV_NO_ID ) NOT DEFERRABLE 
   ');

END;
/

--changeset OPER-22419-1:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INV_ASSOCIATION_MIM_DB to table INV_ASSOCIATION
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE INV_ASSOCIATION ADD CONSTRAINT FK_INV_ASSOCIATION_MIM_DB FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-22419-1:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INV_ASSOCIATION_MIM_DB_CR to table INV_ASSOCIATION
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE INV_ASSOCIATION ADD CONSTRAINT FK_INV_ASSOCIATION_MIM_DB_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeset OPER-22419-1:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INV_ASSOCIATION_MIM_RSTAT to table INV_ASSOCIATION
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_ASSOCIATION ADD CONSTRAINT FK_INV_ASSOCIATION_MIM_RSTAT FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE 
   ');

END;
/

--changeset OPER-22419-1:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add foreign key constraint FK_INV_ASSOCIATION_MIM_DB_CT to table INV_ASSOCIATION
BEGIN

   utl_migr_schema_pkg.table_constraint_add('
	ALTER TABLE INV_ASSOCIATION ADD CONSTRAINT FK_INV_ASSOCIATION_MIM_DB_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE 
   ');

END;
/

--changeset OPER-22419-1:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index IX_INV_ASSOCIATION_ASSO_ID
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_INV_ASSOCIATION_ASSO_ID ON INV_ASSOCIATION
        (
          ASSOCIATION_ID ASC
        )
        LOGGING
   ');
END;
/

--changeset OPER-22419-1:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create trigger TIBR_INV_ASSOCIATION
CREATE OR REPLACE TRIGGER "TIBR_INV_ASSOCIATION" BEFORE INSERT
   ON "INV_ASSOCIATION" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeset OPER-22419-1:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create trigger TUBR_INV_ASSOCIATION
CREATE OR REPLACE TRIGGER "TUBR_INV_ASSOCIATION" BEFORE UPDATE
   ON "INV_ASSOCIATION" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeset OPER-22419-1:12 stripComments:false
--comment create sequence INV_ASSOCIATION_ID_SEQ
INSERT INTO utl_sequence (sequence_cd , next_value , table_name , column_name , oracle_seq , utl_id)
SELECT 'INV_ASSOCIATION_ID_SEQ', 1, 'INV_ASSOCIATION', 'ASSOCIATION_ID', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'INV_ASSOCIATION_ID_SEQ');

--changeset OPER-22419-1:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create sequence INV_ASSOCIATION_ID_SEQ
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('INV_ASSOCIATION_ID_SEQ', 1);
END;
/