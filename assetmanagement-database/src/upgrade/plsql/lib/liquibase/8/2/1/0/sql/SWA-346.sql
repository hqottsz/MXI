--liquibase formatted sql


--changeSet SWA-346:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

UTL_MIGR_SCHEMA_PKG.table_create ('
CREATE TABLE INT_ORDER_EXCEPTION
  (
    ORDER_EXCEPTION_ID RAW (16) NOT NULL DEFERRABLE ,
    RPN                VARCHAR2 (40) ,
    UNP                NUMBER (15,5) ,
    QTY_ADJUST_QTO     NUMBER (5) ,
    QTY_ADJUST_QTO_UNT VARCHAR2 (2) ,
    MFR                VARCHAR2 (16) ,
	STATUS             VARCHAR2 (10) ,
    REMARKS            VARCHAR2 (4000) ,
    CPO                VARCHAR2 (32) NOT NULL DEFERRABLE ,
    SCD                DATE ,
    SCD_QTO            NUMBER (5),
    UNP_ICR            VARCHAR2 (3) ,
    RSTAT_CD           NUMBER (3) NOT NULL DEFERRABLE CHECK ( RSTAT_CD IN (0, 1, 2, 3)) DEFERRABLE ,
    REVISION_NO        NUMBER (10) NOT NULL DEFERRABLE ,
    CTRL_DB_ID         NUMBER (10) NOT NULL DEFERRABLE CHECK ( CTRL_DB_ID BETWEEN 0 AND 4294967295) DEFERRABLE ,
    CREATION_DT        DATE NOT NULL DEFERRABLE ,
    CREATION_DB_ID     NUMBER (10) NOT NULL DEFERRABLE CHECK ( CREATION_DB_ID BETWEEN 0 AND 4294967295) DEFERRABLE ,
    REVISION_DT        DATE NOT NULL DEFERRABLE ,
    REVISION_DB_ID     NUMBER (10) NOT NULL DEFERRABLE CHECK ( REVISION_DB_ID BETWEEN 0 AND 4294967295) DEFERRABLE ,
    REVISION_USER      VARCHAR2 (30) NOT NULL DEFERRABLE

  )
    ');
	
  utl_migr_schema_pkg.table_constraint_add('
    ALTER TABLE INT_ORDER_EXCEPTION ADD CONSTRAINT PK_INT_ORDER_EXCEPTION PRIMARY KEY ( ORDER_EXCEPTION_ID )
  ');
  
  utl_migr_schema_pkg.table_constraint_add('
   ALTER TABLE INT_ORDER_EXCEPTION ADD CONSTRAINT FK_MIMDB_ORDEREXCEPTION FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
  ');
  
  utl_migr_schema_pkg.table_constraint_add('
    ALTER TABLE INT_ORDER_EXCEPTION ADD CONSTRAINT FK_MIMDB_ORDEREXCEPTIONC FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
  ');
  
  utl_migr_schema_pkg.table_constraint_add('
    ALTER TABLE INT_ORDER_EXCEPTION ADD CONSTRAINT FK_MIMDB_ORDEREXCEPTIONR FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE
  ');
  
  utl_migr_schema_pkg.table_constraint_add('
    ALTER TABLE INT_ORDER_EXCEPTION ADD CONSTRAINT FK_MIMRSTAT_ORDEREXCEPTION FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE
  ');
  

	

END;
/

--changeSet SWA-346:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_INT_ORDER_EXCEPTION" BEFORE UPDATE
   ON "INT_ORDER_EXCEPTION" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet SWA-346:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_INT_ORDER_EXCEPTION" BEFORE INSERT
   ON "INT_ORDER_EXCEPTION" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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