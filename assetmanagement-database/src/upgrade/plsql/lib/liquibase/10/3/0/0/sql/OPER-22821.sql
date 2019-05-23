--liquibase formatted sql

--changeSet OPER-22821:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
CREATE TABLE SHIP_SHIPMENT_LINE_MP
  (
    SHIPMENT_LINE_DB_ID NUMBER (10) NOT NULL ,
    SHIPMENT_LINE_ID    NUMBER (10) NOT NULL ,
    MP_KEY_SDESC        VARCHAR2 (80) NOT NULL ,
    RSTAT_CD            NUMBER (3) NOT NULL ,
    REVISION_NO         NUMBER (10) NOT NULL ,
    CTRL_DB_ID          NUMBER (10) NOT NULL ,
    CREATION_DT         DATE NOT NULL ,
    CREATION_DB_ID      NUMBER (10) NOT NULL ,
    REVISION_DT         DATE NOT NULL ,
    REVISION_DB_ID      NUMBER (10) NOT NULL ,
    REVISION_USER       VARCHAR2 (30) NOT NULL
  )
     ');
END;
/

--changeSet OPER-22821:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('
ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CONSTRAINT PK_SHIP_SHIPMENT_LINE_MP PRIMARY KEY ( SHIPMENT_LINE_DB_ID, SHIPMENT_LINE_ID )');
END;
/

--changeSet OPER-22821:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('
ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CHECK ( RSTAT_CD IN (0, 1, 2, 3))');
END;
/

--changeSet OPER-22821:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CHECK ( REVISION_NO BETWEEN 0 AND 4294967295)');
END;
/

--changeSet OPER-22821:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295)');
END;
/

--changeSet OPER-22821:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295)');
END;
/

--changeSet OPER-22821:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295)');
END;
/
--changeSet OPER-22821:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('CREATE INDEX IX_SHIP_SHIP_LINE_MP_KEY ON SHIP_SHIPMENT_LINE_MP(MP_KEY_SDESC ASC)');
END;
/

--changeSet OPER-22821:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CONSTRAINT FK_SHIP_SHIPMENT_LINE_MP FOREIGN KEY ( SHIPMENT_LINE_DB_ID, SHIPMENT_LINE_ID ) REFERENCES SHIP_SHIPMENT_LINE ( SHIPMENT_LINE_DB_ID, SHIPMENT_LINE_ID ) NOT DEFERRABLE ');
END;
/

--changeSet OPER-22821:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CONSTRAINT FK_MIMDB_SHIP_LN_MP_CR FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE');
END;
/

--changeSet OPER-22821:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CONSTRAINT FK_MIMDB_SHIP_LN_MP_CT FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE');
END;
/

--changeSet OPER-22821:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CONSTRAINT FK_MIMDB_SHIP_LN_RE FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE');
END;
/

--changeSet OPER-22821:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add ('ALTER TABLE SHIP_SHIPMENT_LINE_MP ADD CONSTRAINT FK_MIMRSTAT_SHIP_LN_MP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE');
END;
/

--changeSet OPER-22821:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_SHIP_SHIPMENT_LINE_MP" BEFORE UPDATE
   ON "SHIP_SHIPMENT_LINE_MP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  mx_trigger_pkg.before_update(
    :old.rstat_cd,
    :new.rstat_cd,
    :old.revision_no,
    :new.revision_no,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user );
end;
/

--changeSet OPER-22821:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_SHIP_SHIPMENT_LINE_MP" BEFORE INSERT
   ON "SHIP_SHIPMENT_LINE_MP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
begin

  mx_trigger_pkg.before_insert(
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