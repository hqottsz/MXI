--liquibase formatted sql
--comment insert new axon tables

--changeset OPER-29660:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create table AXON_SUBSCRIPTION
BEGIN
   upg_migr_schema_v1_pkg.table_create('
CREATE TABLE AXON_SUBSCRIPTION
  (
    subscriptionIdentifier VARCHAR2 (255) NOT NULL ,
    metaData BLOB ,
    payload BLOB ,
    payloadType        VARCHAR2 (255) ,
    queryName          VARCHAR2 (255) ,
    responseType       VARCHAR2 (255) ,
    updateResponseType VARCHAR2 (255) ,
                       TIMESTAMP VARCHAR2 (255) ,
    owner              VARCHAR2 (255)
  )
   ');
END;
/

--changeset OPER-29660:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create index IX_AXON_SUBSCRIPTION_OWNER
BEGIN

   upg_migr_schema_v1_pkg.index_create('
CREATE INDEX IX_AXON_SUBSCRIPTION_OWNER ON AXON_SUBSCRIPTION
  ( owner ASC
  )
   ');
END;
/

--changeset OPER-29660:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary key constraint to table MY_TABLE_NAME
BEGIN

   upg_migr_schema_v1_pkg.table_constraint_add('
ALTER TABLE AXON_SUBSCRIPTION ADD CONSTRAINT PK_AXON_SUBSCRIPTION PRIMARY KEY ( subscriptionIdentifier )
   ');

END;
/


--changeset OPER-29660:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment create table AXON_SUBSCRIPTION_MESSAGE
BEGIN
   upg_migr_schema_v1_pkg.table_create('
CREATE TABLE AXON_SUBSCRIPTION_MESSAGE
  (
    messageIdentifier       VARCHAR2 (255) NOT NULL ,
    subscriptionIdentifier VARCHAR2 (255) NOT NULL ,
    metaData BLOB NOT NULL ,
    payload BLOB NOT NULL ,
    payloadType VARCHAR2 (255) NOT NULL
  )
   ');
END;
/


--changeset OPER-29660:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add primary key constraint to table AXON_SUBSCRIPTION_MESSAGE
BEGIN

   upg_migr_schema_v1_pkg.table_constraint_add('
ALTER TABLE AXON_SUBSCRIPTION_MESSAGE ADD CONSTRAINT PK_AXON_SUBSCRIPTION_MESSAGE PRIMARY KEY ( messageIdentifier )
   ');

END;
/

--changeSet OPER-29660:6 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT
   '100', 'AXON_QUERY_UPDATE_MESSAGE_INTERVAL', 'LOGIC', 'This parameter controls the interval (in milliseconds) for checking new Axon Subscription Update Messages.', 'GLOBAL', 'Number', '100', 1, 'Axon Framework', '8.3-SP2', 0
FROM
   dual
WHERE
   NOT EXISTS(SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'AXON_QUERY_UPDATE_MESSAGE_INTERVAL');

--changeSet OPER-29660:7 stripComments:false
INSERT INTO UTL_CONFIG_PARM (PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID)
SELECT
   '600000', 'AXON_QUERY_UPDATE_SUBSCRIPTION_INACTIVE_DURATION', 'LOGIC', 'This parameter controls the duration (in milliseconds) for a query update subscription to be considered inactive.', 'GLOBAL', 'Number', '600000', 1, 'Axon Framework', '8.3-SP2', 0
FROM
   dual
WHERE
   NOT EXISTS(SELECT 1 FROM UTL_CONFIG_PARM WHERE PARM_NAME = 'AXON_QUERY_UPDATE_SUBSCRIPTION_INACTIVE_DURATION');
