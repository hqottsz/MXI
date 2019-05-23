--liquibase formatted sql



--changeSet OPER-8790:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

UTL_MIGR_SCHEMA_PKG.table_create ('
CREATE TABLE ACFT_GROUP
  (
    ID             NUMBER (10) NOT NULL ,
    NAME           VARCHAR2 (40) NOT NULL ,
    DESCRIPTION    VARCHAR2 (500) ,
    RSTAT_CD       NUMBER (3) NOT NULL ,
    REVISION_NO    NUMBER (10) NOT NULL ,
    CTRL_DB_ID     NUMBER (10) NOT NULL ,
    CREATION_DT    DATE NOT NULL ,
    CREATION_DB_ID NUMBER (10) NOT NULL ,
    REVISION_DT    DATE NOT NULL ,
    REVISION_DB_ID NUMBER (10) NOT NULL ,
    REVISION_USER  VARCHAR2 (30) NOT NULL
	)
  LOGGING'  
     );

END;
/

--changeSet OPER-8790:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$ 
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_GROUP ADD CONSTRAINT PK_ACFT_GROUP PRIMARY KEY ( ID )'
   );
   
END;
/  

--changeSet OPER-8790:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_GROUP ADD CONSTRAINT FK_MIMDB_CREATIONACFTGROUP FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
   
END;
/

--changeSet OPER-8790:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_GROUP ADD CONSTRAINT FK_MIMDB_CTRLACFTGROUP FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
   
END;
/

--changeSet OPER-8790:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_GROUP ADD CONSTRAINT FK_MIMDB_REVACFTGROUP FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
   
END;
/

--changeSet OPER-8790:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_GROUP ADD CONSTRAINT FK_MIMRSTAT_ACFTGROUP FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE'
   );
   
END;
/

--changeSet OPER-8790:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

UTL_MIGR_SCHEMA_PKG.table_create ('
CREATE TABLE ACFT_GROUP_ASSIGNMENT
  (
    ACFT_NO_DB_ID  NUMBER (10) NOT NULL ,
    ACFT_NO_ID     NUMBER (10) NOT NULL ,
    GROUP_ID       NUMBER (10) NOT NULL ,
    RSTAT_CD       NUMBER (3) NOT NULL ,
    REVISION_NO    NUMBER (10) NOT NULL ,
    CTRL_DB_ID     NUMBER (10) NOT NULL ,
    CREATION_DT    DATE NOT NULL ,
    CREATION_DB_ID NUMBER (10) NOT NULL ,
    REVISION_DT    DATE NOT NULL ,
    REVISION_DB_ID NUMBER (10) NOT NULL ,
    REVISION_USER  VARCHAR2 (30) NOT NULL
  )
  LOGGING'
     );

END;
/

--changeSet OPER-8790:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add (
      'ALTER TABLE ACFT_GROUP_ASSIGNMENT ADD CONSTRAINT PK_ACFT_GROUP_ASSIGNMENT PRIMARY KEY ( ACFT_NO_DB_ID, ACFT_NO_ID, GROUP_ID )'
   );
END;
/ 

--changeSet OPER-8790:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add ( 
      'ALTER TABLE ACFT_GROUP_ASSIGNMENT ADD CONSTRAINT FK_ACFTGROUP_ACFTGROUPASS FOREIGN KEY ( GROUP_ID ) REFERENCES ACFT_GROUP ( ID ) NOT DEFERRABLE'
   );
END;
/ 

--changeSet OPER-8790:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add ( 
      'ALTER TABLE ACFT_GROUP_ASSIGNMENT ADD CONSTRAINT FK_INVACREG_ACFTGROUPASS FOREIGN KEY ( ACFT_NO_DB_ID, ACFT_NO_ID ) REFERENCES INV_AC_REG ( INV_NO_DB_ID, INV_NO_ID ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-8790:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add ( 
      'ALTER TABLE ACFT_GROUP_ASSIGNMENT ADD CONSTRAINT FK_MIMDB_CREATIONACFTGROUPASS FOREIGN KEY ( CREATION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-8790:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add ( 
      'ALTER TABLE ACFT_GROUP_ASSIGNMENT ADD CONSTRAINT FK_MIMDB_CTRLACFTGROUPASS FOREIGN KEY ( CTRL_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-8790:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add ( 
      'ALTER TABLE ACFT_GROUP_ASSIGNMENT ADD CONSTRAINT FK_MIMDB_REVACFTGROUPASS FOREIGN KEY ( REVISION_DB_ID ) REFERENCES MIM_DB ( DB_ID ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-8790:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   UTL_MIGR_SCHEMA_PKG.table_constraint_add ( 
      'ALTER TABLE ACFT_GROUP_ASSIGNMENT ADD CONSTRAINT FK_MIMRSTAT_ACFTGROUPASS FOREIGN KEY ( RSTAT_CD ) REFERENCES MIM_RSTAT ( RSTAT_CD ) NOT DEFERRABLE'
   );
END;
/

--changeSet OPER-8790:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ACFT_GROUP" BEFORE INSERT
   ON "ACFT_GROUP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-8790:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ACFT_GROUP" BEFORE UPDATE
   ON "ACFT_GROUP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-8790:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_ACFT_GROUP_ASSIGNMENT" BEFORE INSERT
   ON "ACFT_GROUP_ASSIGNMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW

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

--changeSet OPER-8790:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_ACFT_GROUP_ASSIGNMENT" BEFORE UPDATE
   ON "ACFT_GROUP_ASSIGNMENT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet OPER-8790:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.sequence_create('ACFT_GROUP_ID_SEQ', 100000);
END;
/

--changeSet OPER-8790:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   INSERT INTO 
      utl_sequence ( 
	 sequence_cd, 
         next_value, 
         table_name, 
         column_name, 
         oracle_seq, 
         utl_id 
      )
      SELECT 
         'ACFT_GROUP_ID_SEQ', 
         100000, 
         'ACFT_GROUP', 
         'ID' , 
         1, 
         0
      FROM
	 dual
      WHERE
	 NOT EXISTS ( SELECT 1 FROM utl_sequence WHERE sequence_cd = 'ACFT_GROUP_ID_SEQ' );
END;
/

--changeSet OPER-8790:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN

   INSERT INTO 
      utl_menu_item 
      (
       menu_id, todo_list_id, menu_name, menu_link_url, new_window_bool, menu_ldesc, repl_approved, utl_id
      )
   SELECT 
      120942, NULL, 'web.menuitem.MANAGE_AIRCRAFT_GROUPS', '/maintenix/web/acftgroup/ManageAircraftGroups.jsp', 0, 'Manage Aircraft Groups', 0, 0 
   FROM
      dual
   WHERE 
      NOT EXISTS ( SELECT 1 FROM utl_menu_item WHERE menu_id = 120942 );
END;
/
