--liquibase formatted sql

--changeset OPER-26060:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create table AXON_ASSOC_VALUE_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_create('
      CREATE TABLE AXON_ASSOC_VALUE_ENTRY
       (
         id               NUMBER (38) NOT NULL ,
         associationKey   VARCHAR2 (255) ,
         associationValue VARCHAR2 (255) ,
         sagaId           VARCHAR2 (255) ,
         sagaType         VARCHAR2 (255)
        )
   ');
END;
/

--changeset OPER-26060:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index on AXON_ASSOC_VALUE_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_AXON_ASS_VAL_ENTRY_SAGA_ASS ON AXON_ASSOC_VALUE_ENTRY
        (
          sagaType ASC ,
          associationKey ASC ,
          associationValue ASC
        )
   ');
END;
/
  
--changeset OPER-26060:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index on AXON_ASSOC_VALUE_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.index_create('
      CREATE INDEX IX_AXON_ASS_VAL_ENTRY_SAGA ON AXON_ASSOC_VALUE_ENTRY
        (
          sagaId ASC ,
          sagaType ASC
        )
   ');
END;
/

--changeset OPER-26060:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary key constraint to table AXON_ASSOC_VALUE_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE AXON_ASSOC_VALUE_ENTRY ADD CONSTRAINT PK_AXON_ASSOC_VAL_ENTRY PRIMARY KEY ( id )
   ');
END;
/

--changeset OPER-26060:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create table AXON_DOMAIN_EVENT_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_create('
      CREATE TABLE AXON_DOMAIN_EVENT_ENTRY
        (
          globalIndex         NUMBER (19) NOT NULL ,
          aggregateIdentifier VARCHAR2 (255) NOT NULL ,
          sequenceNumber      NUMBER (19) NOT NULL ,
          type                VARCHAR2 (255) ,
          eventIdentifier     VARCHAR2 (255) NOT NULL ,
          metaData BLOB ,
          payload BLOB NOT NULL ,
          payloadRevision VARCHAR2 (255) ,
          payloadType     VARCHAR2 (255) NOT NULL ,
                          TIMESTAMP VARCHAR2 (255) NOT NULL
        )
   ');
END;
/

--changeset OPER-26060:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary key constraint to table AXON_DOMAIN_EVENT_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE AXON_DOMAIN_EVENT_ENTRY ADD CONSTRAINT PK_AXON_DOMAIN_EVENT_ENTRY PRIMARY KEY ( globalIndex )
   ');
END;
/

--changeset OPER-26060:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add unique constraint IX_AXON_DOM_EVT_AGG_IDSEQ_UNQ to table AXON_DOMAIN_EVENT_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE AXON_DOMAIN_EVENT_ENTRY ADD CONSTRAINT IX_AXON_DOM_EVT_AGG_IDSEQ_UNQ UNIQUE ( aggregateIdentifier , sequenceNumber )
   ');
END;
/

--changeset OPER-26060:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add unique constraint IX_AXON_DOM_EVT_EVENT_ID_UNQ to table AXON_DOMAIN_EVENT_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE AXON_DOMAIN_EVENT_ENTRY ADD CONSTRAINT IX_AXON_DOM_EVT_EVENT_ID_UNQ UNIQUE ( eventIdentifier )
   ');
END;
/


--changeset OPER-26060:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create table AXON_SAGA_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_create('
      CREATE TABLE AXON_SAGA_ENTRY
        (
          sagaId   VARCHAR2 (255) NOT NULL ,
          revision VARCHAR2 (255) ,
          sagaType VARCHAR2 (255) ,
          serializedSaga BLOB
        )
   ');
END;
/

--changeset OPER-26060:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary key constraint to table AXON_SAGA_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE AXON_SAGA_ENTRY ADD CONSTRAINT PK_AXON_SAGA_ENTRY PRIMARY KEY ( sagaId )
   ');
END;
/

--changeset OPER-26060:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create table AXON_SNAPSHOT_EVENT_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_create('
      CREATE TABLE AXON_SNAPSHOT_EVENT_ENTRY
        (
          aggregateIdentifier VARCHAR2 (255) NOT NULL ,
          sequenceNumber      NUMBER NOT NULL ,
          type                VARCHAR2 (255) NOT NULL ,
          eventIdentifier     VARCHAR2 (255) NOT NULL ,
          metaData BLOB ,
          payload BLOB NOT NULL ,
          payloadRevision VARCHAR2 (255) ,
          payloadType     VARCHAR2 (255) NOT NULL ,
                          TIMESTAMP VARCHAR2 (255) NOT NULL
        )
   ');
END;
/

--changeset OPER-26060:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary key constraint to table AXON_SNAPSHOT_EVENT_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE AXON_SNAPSHOT_EVENT_ENTRY ADD CONSTRAINT PK_AXON_SNAPSHOT_EVENT_ENTRY PRIMARY KEY ( aggregateIdentifier, sequenceNumber, type )
   ');
END;
/

--changeset OPER-26060:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add unique constraint IX_AXON_SNAPSHOT_EVENT_ENTRY to table AXON_SNAPSHOT_EVENT_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE AXON_SNAPSHOT_EVENT_ENTRY ADD CONSTRAINT IX_AXON_SNAPSHOT_EVENT_ENTRY UNIQUE ( eventIdentifier )
   ');
END;
/

--changeset OPER-26060:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create table AXON_TOKEN_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_create('
      CREATE TABLE AXON_TOKEN_ENTRY
        (
          processorName VARCHAR2 (255) NOT NULL ,
          segment       INTEGER NOT NULL ,
          token BLOB ,
          tokenType VARCHAR2 (255) ,
                    TIMESTAMP VARCHAR2 (255) ,
          owner     VARCHAR2 (255)
        )
   ');
END;
/

--changeset OPER-26060:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary key constraint to table AXON_TOKEN_ENTRY
BEGIN
   upg_migr_schema_v1_pkg.table_constraint_add('
      ALTER TABLE AXON_TOKEN_ENTRY ADD CONSTRAINT PK_AXON_TOKEN_ENTRY PRIMARY KEY ( processorName, segment )
   ');
END;
/

--changeSet OPER-26060:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_AXON_DOM_EVENT_ENTRY_ID
/********************************************************************************
*
* Trigger:    TIBR_AXON_DOM_EVENT_ENTRY_ID
*
* Description:  Axon Framework trigger on the AXON_DOMAIN_EVENT_ENTRY table.
*
********************************************************************************/
   BEFORE INSERT ON AXON_DOMAIN_EVENT_ENTRY
   FOR EACH ROW
   BEGIN
      :new.globalIndex := AXON_DOMAIN_EVENT_ENTRY_SEQ.nextval;
   END;
/

--changeSet OPER-26060:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER TIBR_AXON_ASSOC_VALUE_ENTRY_ID
/********************************************************************************
*
* Trigger:    TIBR_AXON_ASSOC_VALUE_ENTRY_ID
*
* Description:  Axon Framework trigger on the AXON_ASSOC_VALUE_ENTRY table.
*
********************************************************************************/
   BEFORE INSERT ON AXON_ASSOC_VALUE_ENTRY
   FOR EACH ROW
   BEGIN
      :new.id := AXON_ASSOC_VALUE_ENTRY_SEQ.nextval;
   END;
/

--changeSet OPER-26060:18 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
SELECT 'AXON_DOMAIN_EVENT_ENTRY_SEQ', 1, 'AXON_DOMAIN_EVENT_ENTRY', 'globalIndex' , 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'AXON_DOMAIN_EVENT_ENTRY_SEQ');

--changeSet OPER-26060:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('AXON_DOMAIN_EVENT_ENTRY_SEQ', 1);
END;
/

--changeSet OPER-26060:20 stripComments:false
INSERT INTO utl_sequence ( sequence_cd, next_value, table_name, column_name, oracle_seq, utl_id )
SELECT 'AXON_ASSOC_VALUE_ENTRY_SEQ', 1, 'AXON_ASSOC_VALUE_ENTRY', 'id' , 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM UTL_SEQUENCE WHERE SEQUENCE_CD = 'AXON_ASSOC_VALUE_ENTRY_SEQ');
   
--changeSet OPER-26060:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   upg_migr_schema_v1_pkg.sequence_create('AXON_ASSOC_VALUE_ENTRY_SEQ', 1);
END;
/