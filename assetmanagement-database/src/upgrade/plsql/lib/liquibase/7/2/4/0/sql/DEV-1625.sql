--liquibase formatted sql


--changeSet DEV-1625:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Migrations changes for adding the ASB logging tables
-- Create the tables
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ASB_INBOUND_LOG" (
	"MSG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"TRANSACTION_ID" Raw(16) NOT NULL DEFERRABLE ,
	"EXTERNAL_ID" Varchar2 (200),
	"MSG_DATE" Number(19,0) NOT NULL DEFERRABLE ,
	"MSG_SOURCE" Varchar2 (200) NOT NULL DEFERRABLE ,
	"BODY_BLOB_DB_ID" Number(10,0) Check (BODY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BODY_BLOB_ID" Number(10,0) Check (BODY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_DB_ID" Number(10,0) Check (BINARY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_ID" Number(10,0) Check (BINARY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) Default 0 NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
 Constraint "PK_ASB_INBOUND_LOG" primary key ("MSG_ID") 
)
');
END;
/

--changeSet DEV-1625:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ASB_EXCEPTION_LOG" (
	"EXCEPTION_ID" Raw(16) NOT NULL DEFERRABLE INITIALLY DEFERRED ,
	"TRANSACTION_ID" Raw(16) NOT NULL DEFERRABLE ,
	"MESSAGE_ID" Raw(16) NOT NULL DEFERRABLE ,
	"EXCEPTION_DATE" Number(19,0) NOT NULL DEFERRABLE ,
	"BODY_BLOB_DB_ID" Number(10,0) Check (BODY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BODY_BLOB_ID" Number(10,0) Check (BODY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_DB_ID" Number(10,0) Check (BINARY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_ID" Number(10,0) Check (BINARY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) Default 0 NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
 Constraint "PK_ASB_EXCEPTION_LOG" primary key ("EXCEPTION_ID") 
)
');
END;
/

--changeSet DEV-1625:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ASB_REQUEST_LOG" (
	"REQUEST_ID" Raw(16) NOT NULL DEFERRABLE ,
	"TRANSACTION_ID" Raw(16) NOT NULL DEFERRABLE ,
	"REQUEST_DATE" Number(19,0) NOT NULL DEFERRABLE ,
	"REQUEST_DEST" Varchar2 (200) NOT NULL DEFERRABLE ,
	"BODY_BLOB_DB_ID" Number(10,0) Check (BODY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BODY_BLOB_ID" Number(10,0) Check (BODY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_DB_ID" Number(10,0) Check (BINARY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_ID" Number(10,0) Check (BINARY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) Default 0 NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
 Constraint "PK_ASB_REQUEST_LOG" primary key ("REQUEST_ID") 
)
');
END;
/

--changeSet DEV-1625:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ASB_RESPONSE_LOG" (
	"RESPONSE_ID" Raw(16) NOT NULL DEFERRABLE ,
	"TRANSACTION_ID" Raw(16) NOT NULL DEFERRABLE ,
	"RESPONSE_DATE" Number(19,0) NOT NULL DEFERRABLE ,
	"RESPONSE_SOURCE" Varchar2 (200) NOT NULL DEFERRABLE ,
	"BODY_BLOB_DB_ID" Number(10,0) Check (BODY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BODY_BLOB_ID" Number(10,0) Check (BODY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_DB_ID" Number(10,0) Check (BINARY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_ID" Number(10,0) Check (BINARY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) Default 0 NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
 Constraint "PK_ASB_RESPONSE_LOG" primary key ("RESPONSE_ID") 
) 
');
END;
/

--changeSet DEV-1625:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ASB_OUTBOUND_LOG" (
	"MSG_ID" Raw(16) NOT NULL DEFERRABLE ,
	"TRANSACTION_ID" Raw(16) NOT NULL DEFERRABLE ,
	"MSG_DATE" Number(19,0) NOT NULL DEFERRABLE ,
	"MSG_DEST" Varchar2 (200) NOT NULL DEFERRABLE ,
	"BODY_BLOB_DB_ID" Number(10,0) Check (BODY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BODY_BLOB_ID" Number(10,0) Check (BODY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_DB_ID" Number(10,0) Check (BINARY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_ID" Number(10,0) Check (BINARY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) Default 0 NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
 Constraint "PK_ASB_OUTBOUND_LOG" primary key ("MSG_ID") 
)
');
END;
/

--changeSet DEV-1625:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ASB_NOTIFICATION_LOG" (
	"NOTIFICATION_ID" Raw(16) NOT NULL DEFERRABLE ,
	"TRANSACTION_ID" Raw(16) NOT NULL DEFERRABLE ,
	"NOTIFICATION_DATE" Number(19,0) NOT NULL DEFERRABLE ,
	"NOTIFICATION_SOURCE" Varchar2 (200) NOT NULL DEFERRABLE ,
	"BODY_BLOB_DB_ID" Number(10,0) Check (BODY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BODY_BLOB_ID" Number(10,0) Check (BODY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_DB_ID" Number(10,0) Check (BINARY_BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"BINARY_BLOB_ID" Number(10,0) Check (BINARY_BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) Default 0 NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
 Constraint "PK_ASB_NOTIFICATION_LOG" primary key ("NOTIFICATION_ID") 
)
');
END;
/

--changeSet DEV-1625:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_create('
Create table "ASB_TRANSACTION_LOG" (
	"TRANSACTION_ID" Raw(16) NOT NULL DEFERRABLE ,
	"CONVERSATION_ID" Raw(16) NOT NULL DEFERRABLE ,
	"MODULE" Varchar2 (100) NOT NULL DEFERRABLE ,
	"MSG_TYPE" Varchar2 (20) NOT NULL DEFERRABLE ,
	"SERVER" Varchar2 (100) NOT NULL DEFERRABLE ,
	"SYNC_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (SYNC_BOOL IN (0, 1) ) DEFERRABLE ,
	"RSTAT_CD" Number(3,0) Default 0 NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
 Constraint "PK_ASB_TRANSACTION_LOG" primary key ("TRANSACTION_ID") 
)
');
END;
/

--changeSet DEV-1625:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Add the foreign keys to the BLOB_DATA tables
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_EXCEPTION_LOG" add Constraint "FK_ASBEXCEPTION_BLOBDATA_BODY" foreign key ("BODY_BLOB_DB_ID","BODY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_EXCEPTION_LOG" add Constraint "FK_ASBEXCEPTION_BLOBDATA_BIN" foreign key ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_EXCEPTION_LOG" add Constraint "FK_MIMRSTAT_ASBEXCEPTIONLOG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_REQUEST_LOG" add Constraint "FK_ASBREQUEST_BLOBDATA_BODY" foreign key ("BODY_BLOB_DB_ID","BODY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_REQUEST_LOG" add Constraint "FK_ASBREQUEST_BLOBDATA_BIN" foreign key ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_REQUEST_LOG" add Constraint "FK_MIMRSTAT_ASBREQUESTLOG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_RESPONSE_LOG" add Constraint "FK_ASBRESPONSE_BLOBDATA_BODY" foreign key ("BODY_BLOB_DB_ID","BODY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_RESPONSE_LOG" add Constraint "FK_ASBRESPONSE_BLOBDATA_BIN" foreign key ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_RESPONSE_LOG" add Constraint "FK_MIMRSTAT_ASBRESPONSELOG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_OUTBOUND_LOG" add Constraint "FK_ASBOUTBOUND_BLOBDATA_BODY" foreign key ("BODY_BLOB_DB_ID","BODY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_OUTBOUND_LOG" add Constraint "FK_ASBOUTBOUND_BLOBDATA_BIN" foreign key ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_OUTBOUND_LOG" add Constraint "FK_MIMRSTAT_ASBOUTBOUNDLOG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_NOTIFICATION_LOG" add Constraint "FK_ASBNOTIF_BLOBDATA_BODY" foreign key ("BODY_BLOB_DB_ID","BODY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_NOTIFICATION_LOG" add Constraint "FK_ASBNOTIF_BLOBDATA_BIN" foreign key ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_NOTIFICATION_LOG" add Constraint "FK_MIMRSTAT_ASBNOTIFICATION" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_INBOUND_LOG" add Constraint "FK_ASBINBOUND_BLOBDATA_BODY" foreign key ("BODY_BLOB_DB_ID","BODY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_INBOUND_LOG" add Constraint "FK_ASBINBOUND_BLOBDATA_BIN" foreign key ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID") references "BLOB_DATA" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_INBOUND_LOG" add Constraint "FK_MIMRSTAT_ASBINBOUNDLOG" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_constraint_add('
Alter table "ASB_TRANSACTION_LOG" add Constraint "FK_MIMRSTAT_ASBTRANSACTION" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
');
END;
/

--changeSet DEV-1625:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add the indexes
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBEXCEPTION_BLOBDATA_BODY" ON "ASB_EXCEPTION_LOG" ("BODY_BLOB_DB_ID","BODY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBEXCEPTION_BLOBDATA_BIN" ON "ASB_EXCEPTION_LOG" ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_MIMRSTAT_ASBEXCEPTIONLOG" ON "ASB_EXCEPTION_LOG" ("RSTAT_CD")
 ');
 END;
/

--changeSet DEV-1625:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBREQUEST_BLOBDATA_BODY" ON "ASB_REQUEST_LOG" ("BODY_BLOB_DB_ID","BODY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBREQUEST_BLOBDATA_BIN" ON "ASB_REQUEST_LOG" ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_MIMRSTAT_ASBREQUESTLOG" ON "ASB_REQUEST_LOG" ("RSTAT_CD")
 ');
 END;
/

--changeSet DEV-1625:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBRESPONSE_BLOBDATA_BODY" ON "ASB_RESPONSE_LOG" ("BODY_BLOB_DB_ID","BODY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBRESPONSE_BLOBDATA_BIN" ON "ASB_RESPONSE_LOG" ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_MIMRSTAT_ASBRESPONSELOG" ON "ASB_RESPONSE_LOG" ("RSTAT_CD")
 ');
 END;
/

--changeSet DEV-1625:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBOUTBOUND_BLOBDATA_BODY" ON "ASB_OUTBOUND_LOG" ("BODY_BLOB_DB_ID","BODY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBOUTBOUND_BLOBDATA_BIN" ON "ASB_OUTBOUND_LOG" ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_MIMRSTAT_ASBOUTBOUNDLOG" ON "ASB_OUTBOUND_LOG" ("RSTAT_CD")
 ');
 END;
/

--changeSet DEV-1625:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBNOTIF_BLOBDATA_BODY" ON "ASB_NOTIFICATION_LOG" ("BODY_BLOB_DB_ID","BODY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBNOTIF_BLOBDATA_BIN" ON "ASB_NOTIFICATION_LOG" ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_MIMRSTAT_ASBNOTIFICATION" ON "ASB_NOTIFICATION_LOG" ("RSTAT_CD")
 ');
 END;
/

--changeSet DEV-1625:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBINBOUND_BLOBDATA_BODY" ON "ASB_INBOUND_LOG" ("BODY_BLOB_DB_ID","BODY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_ASBINBOUND_BLOBDATA_BIN" ON "ASB_INBOUND_LOG" ("BINARY_BLOB_DB_ID","BINARY_BLOB_ID")
 ');
 END;
/

--changeSet DEV-1625:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_MIMRSTAT_ASBINBOUNDLOG" ON "ASB_INBOUND_LOG" ("RSTAT_CD")
 ');
 END;
/

--changeSet DEV-1625:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 BEGIN
 utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_MIMRSTAT_ASBTRANSACTION" ON "ASB_TRANSACTION_LOG" ("RSTAT_CD")
 ');
 END;
/

--changeSet DEV-1625:46 stripComments:false
-- add the views
CREATE OR REPLACE VIEW VW_ASB_CONNECTORS
(
	module,
   msg_type
)
AS
SELECT
   asb_transaction_log.module,
   asb_transaction_log.msg_type      
FROM
   asb_transaction_log
GROUP BY
   asb_transaction_log.module,
   asb_transaction_log.msg_type;

--changeSet DEV-1625:47 stripComments:false
CREATE OR REPLACE VIEW VW_ASB_CONNECTOR_MESSAGES
(
	message_id,
   transaction_id,
   external_id,
   message_date,
   message_type,
   message_source,
   body_db_id,
   body_id,
   binary_db_id,
   binary_id,
   conversation_id,
   module,
   msg_type,
   server,
   sync_bool
)
AS
SELECT
   message.message_id,
   message.transaction_id,
   message.external_id,
   message.message_date,
   message.message_type,
   message.message_source,
   message.body_db_id,
   message.body_id,
   message.binary_db_id,
   message.binary_id,
   asb_transaction_log.conversation_id,
   asb_transaction_log.module,
   asb_transaction_log.msg_type,
   asb_transaction_log.server,
   asb_transaction_log.sync_bool
   FROM
   (
      SELECT
         asb_inbound_log.msg_id AS message_id,
         'INBOUND' AS message_type,
         asb_inbound_log.transaction_id AS transaction_id,
         asb_inbound_log.external_id AS external_id,
         asb_inbound_log.msg_date AS message_date,
         asb_inbound_log.msg_source AS message_source,
         asb_inbound_log.body_blob_db_id AS body_db_id,
         asb_inbound_log.body_blob_id AS body_id,
         asb_inbound_log.binary_blob_db_id AS binary_db_id,
         asb_inbound_log.binary_blob_id AS binary_id
      FROM
         asb_inbound_log
      UNION ALL
      SELECT
         asb_outbound_log.msg_id AS message_id,
         'OUTBOUND' AS message_type,
         asb_outbound_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_outbound_log.msg_date AS message_date,
         asb_outbound_log.msg_dest AS message_source,
         asb_outbound_log.body_blob_db_id AS body_db_id,
         asb_outbound_log.body_blob_id AS body_id,
         asb_outbound_log.binary_blob_db_id AS binary_db_id,
         asb_outbound_log.binary_blob_id AS binary_id
      FROM
         asb_outbound_log
      UNION ALL
      SELECT
         asb_exception_log.exception_id AS message_id,
         'EXCEPTION' AS message_type,
         asb_exception_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_exception_log.exception_date AS message_date,
         NULL AS message_source,
         asb_exception_log.body_blob_db_id AS body_db_id,
         asb_exception_log.body_blob_id AS body_id,
         asb_exception_log.binary_blob_db_id AS binary_db_id,
         asb_exception_log.binary_blob_id AS binary_id
      FROM
         asb_exception_log
      UNION ALL
      SELECT
         asb_notification_log.notification_id AS message_id,
         'NOTIFICATION' AS message_type,
         asb_notification_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_notification_log.notification_date AS message_date,
         asb_notification_log.notification_source AS message_source,
         asb_notification_log.body_blob_db_id AS body_db_id,
         asb_notification_log.body_blob_id AS body_id,
         asb_notification_log.binary_blob_db_id AS binary_db_id,
         asb_notification_log.binary_blob_id AS binary_id
      FROM
         asb_notification_log
      UNION ALL
      SELECT
         asb_request_log.request_id AS message_id,
         'REQUEST' AS message_type,
         asb_request_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_request_log.request_date AS message_date,
         asb_request_log.request_dest AS message_source,
         asb_request_log.body_blob_db_id AS body_db_id,
         asb_request_log.body_blob_id AS body_id,
         asb_request_log.binary_blob_db_id AS binary_db_id,
         asb_request_log.binary_blob_id AS binary_id
      FROM
         asb_request_log
      UNION ALL
      SELECT
         asb_response_log.response_id AS message_id,
         'RESPONSE' AS message_type,
         asb_response_log.transaction_id AS transaction_id,
         NULL AS external_id,
         asb_response_log.response_date AS message_date,
         asb_response_log.response_source AS message_source,
         asb_response_log.body_blob_db_id AS body_db_id,
         asb_response_log.body_blob_id AS body_id,
         asb_response_log.binary_blob_db_id AS binary_db_id,
         asb_response_log.binary_blob_id AS binary_id
      FROM
         asb_response_log
   ) message INNER JOIN asb_transaction_log ON
      message.transaction_id = asb_transaction_log.transaction_id;

--changeSet DEV-1625:48 stripComments:false
-- create purge strategy
INSERT INTO UTL_PURGE_GROUP(purge_group_cd, group_name, group_ldesc, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB', 'ASB Integration Purging', 'A set of purging policies related to removing the data from the ASB logging framework', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:49 stripComments:false
INSERT INTO UTL_PURGE_POLICY(purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user ) 
VALUES('ASB_SUCCESS', 'ASB', 'Successful ASB Transactions', 'Purges the transactions that contain no errors', 10, 0, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:50 stripComments:false
INSERT INTO UTL_PURGE_POLICY(purge_policy_cd, purge_group_cd, policy_name, policy_ldesc, retention_period, active_bool, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user ) 
VALUES('ASB_FAILED', 'ASB', 'Failed ASB Transactions', 'Purges the transactions that contain errors', 30, 0, 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:51 stripComments:false
INSERT ALL
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_INBOUND_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_EXCEPTION_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_NOTIFICATION_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_OUTBOUND_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_REQUEST_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_RESPONSE_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
   INTO UTL_PURGE_TABLE(purge_table_cd, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) VALUES ('ASB_TRANSACTION_LOG', 0, 0, sysdate, sysdate, 0, 'MXI')
SELECT * FROM DUAL;

--changeSet DEV-1625:52 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_INBOUND_LOG', 10, 
'ASB_INBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   NOT EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = asb_inbound_log.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:53 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_NOTIFICATION_LOG', 20,
'ASB_NOTIFICATION_LOG.NOTIFICATION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   NOT EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = ASB_NOTIFICATION_LOG.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:54 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_OUTBOUND_LOG', 30, 
'ASB_OUTBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   NOT EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = ASB_OUTBOUND_LOG.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:55 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_REQUEST_LOG', 40,  
'ASB_REQUEST_LOG.REQUEST_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   NOT EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = ASB_REQUEST_LOG.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:56 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_RESPONSE_LOG',50 ,  
'ASB_RESPONSE_LOG.RESPONSE_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   NOT EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = ASB_RESPONSE_LOG.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:57 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_EXCEPTION_LOG', 60,  
'ASB_EXCEPTION_LOG.EXCEPTION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   NOT EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = ASB_EXCEPTION_LOG.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:58 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_SUCCESS', 'ASB_TRANSACTION_LOG', 70, 
'NOT EXISTS
(
   SELECT
      1
   FROM
      vw_asb_connector_messages
   WHERE
      vw_asb_connector_messages.transaction_id = asb_transaction_log.transaction_id   
)
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:59 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_INBOUND_LOG', 10, 
'ASB_INBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   NOT EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = asb_inbound_log.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:60 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_NOTIFICATION_LOG', 20, 
'ASB_NOTIFICATION_LOG.NOTIFICATION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = ASB_NOTIFICATION_LOG.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:61 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_OUTBOUND_LOG', 30, 
'ASB_OUTBOUND_LOG.MSG_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = ASB_OUTBOUND_LOG.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:62 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_REQUEST_LOG', 40, 
'ASB_REQUEST_LOG.REQUEST_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = ASB_REQUEST_LOG.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:63 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_RESPONSE_LOG',50 , 
'ASB_RESPONSE_LOG.RESPONSE_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000) AND 
   EXISTS 
   (
      SELECT 
         1 
      FROM 
         ASB_EXCEPTION_LOG INNER JOIN ASB_TRANSACTION_LOG ON
            ASB_EXCEPTION_LOG.TRANSACTION_ID = ASB_TRANSACTION_LOG.TRANSACTION_ID
      WHERE
         asb_transaction_log.transaction_id = ASB_RESPONSE_LOG.transaction_id         
   )
', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:64 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_EXCEPTION_LOG', 60, 
'ASB_EXCEPTION_LOG.EXCEPTION_DATE <= ((SYSDATE - to_date(''01.01.1970 00:00:00'', ''dd.mm.yyyy HH24:mi:ss'')) * 86400000) - (30 * 86400000)', 0, 0, sysdate, sysdate, 0, 'MXI');

--changeSet DEV-1625:65 stripComments:false
INSERT INTO UTL_PURGE_STRATEGY(purge_policy_cd, purge_table_cd, purge_ord, predicate_sql, utl_id, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
VALUES('ASB_FAILED', 'ASB_TRANSACTION_LOG', 70, 
'NOT EXISTS
(
   SELECT
      1
   FROM
      vw_asb_connector_messages
   WHERE
      vw_asb_connector_messages.transaction_id = asb_transaction_log.transaction_id   
)
', 0, 0, sysdate, sysdate, 0, 'MXI'); 