--liquibase formatted sql

--changeSet OPER-22809:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	UTL_MIGR_SCHEMA_PKG.table_create('
		CREATE TABLE PO_LINE_MP
		  (
			 PO_DB_ID       NUMBER (10) NOT NULL ,
			 PO_ID          NUMBER (10) NOT NULL ,
			 PO_LINE_ID     NUMBER (10) NOT NULL ,
			 MP_KEY_SDESC   VARCHAR2 (80) NOT NULL ,
			 RSTAT_CD       NUMBER (3) NOT NULL ,
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

--changeSet OPER-22809:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE PO_LINE_MP ADD CHECK ( PO_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-22809:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
      ALTER TABLE PO_LINE_MP ADD CHECK ( PO_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-22809:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE PO_LINE_MP ADD CHECK ( PO_LINE_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-22809:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE PO_LINE_MP ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))
   ');
END;
/

--changeSet OPER-22809:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE PO_LINE_MP ADD CHECK ( REVISION_NO BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-22809:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE PO_LINE_MP ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-22809:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE PO_LINE_MP ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-22809:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_add('
     ALTER TABLE PO_LINE_MP ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)
   ');
END;
/

--changeSet OPER-22809:11 stripComments:false
COMMENT ON TABLE PO_LINE_MP
IS
  'This table stores the material planning key description for an order line.' ;

--changeSet OPER-22809:12 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.PO_DB_ID
IS
  'The order database identifier.' ;

--changeSet OPER-22809:13 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.PO_ID
IS
  'The order identifier.' ;

--changeSet OPER-22809:14 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.PO_LINE_ID
IS
  'The order line identifier.' ;

--changeSet OPER-22809:15 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.MP_KEY_SDESC
IS
  'External key of a related object in Material Planning application. To be used only in context of Material Planning API.' ;

--changeSet OPER-22809:16 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.RSTAT_CD
IS
  'Status of the record.' ;

--changeSet OPER-22809:17 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.REVISION_NO
IS
  'A number incremented each time the record is modified.' ;

--changeSet OPER-22809:18 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.CTRL_DB_ID
IS
  'The identifier of the database that owns the record.' ;

--changeSet OPER-22809:19 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.CREATION_DT
IS
  'The date and time at which the record was inserted.' ;

--changeSet OPER-22809:20 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.CREATION_DB_ID
IS
  'The identifier of the database that inserted the record.' ;

--changeSet OPER-22809:21 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.REVISION_DT
IS
  'The date and time at which the record was last updated.' ;

--changeSet OPER-22809:22 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.REVISION_DB_ID
IS
  'The identifier of the database that last updated the record.' ;

--changeSet OPER-22809:23 stripComments:false
COMMENT ON COLUMN PO_LINE_MP.REVISION_USER
IS
  'The name of the user that last updated the record.' ;



--changeSet OPER-22809:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.index_create('
	  CREATE INDEX IX_MP_KEY_SDESC_POLINEMP ON PO_LINE_MP
		 (
			MP_KEY_SDESC ASC
		 )
  ');
END;
/

--changeSet OPER-22809:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE PO_LINE_MP ADD CONSTRAINT PK_PO_LINE_MP PRIMARY KEY ( PO_DB_ID, PO_ID, PO_LINE_ID )
   ');
END;
/

--changeSet OPER-22809:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE PO_LINE_MP ADD CONSTRAINT FK_MIMDB_POLINEMP_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-22809:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE PO_LINE_MP ADD CONSTRAINT FK_MIMDB_POLINEMP_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-22809:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE PO_LINE_MP ADD CONSTRAINT FK_MIMDB_POLINEMP_RE FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-22809:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
     ALTER TABLE PO_LINE_MP ADD CONSTRAINT FK_MIMRSTAT_POLINEMP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-22809:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.table_constraint_add('
      ALTER TABLE PO_LINE_MP ADD CONSTRAINT FK_PO_LINE_POLINEMP FOREIGN KEY ( PO_DB_ID, PO_ID, PO_LINE_ID ) REFERENCES PO_LINE ( PO_DB_ID, PO_ID, PO_LINE_ID ) NOT DEFERRABLE
   ');
END;
/

--changeSet OPER-22809:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_PO_LINE_MP" BEFORE UPDATE
   ON "PO_LINE_MP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-22809:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_PO_LINE_MP" BEFORE INSERT
   ON "PO_LINE_MP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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
