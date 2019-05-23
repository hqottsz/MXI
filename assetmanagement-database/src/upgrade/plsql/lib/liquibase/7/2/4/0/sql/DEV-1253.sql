--liquibase formatted sql
	

--changeSet DEV-1253:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- create new tables for action config parms
--
BEGIN
	utl_migr_schema_pkg.table_create('
		Create table "UTL_ACTION_CONFIG_PARM" (
			"PARM_NAME" Varchar2 (500) NOT NULL DEFERRABLE ,
			"PARM_VALUE" Varchar2 (1000),
			"ENCRYPT_BOOL" Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (ENCRYPT_BOOL IN (0, 1) ) DEFERRABLE ,
			"PARM_DESC" Varchar2 (4000),
			"DEFAULT_VALUE" Varchar2 (1000),
			"ALLOW_VALUE_DESC" Varchar2 (1000),
			"MAND_CONFIG_BOOL" Number(1,0) Default 0 Check (MAND_CONFIG_BOOL IN (0, 1) ) DEFERRABLE ,
			"CATEGORY" Varchar2 (4000) NOT NULL DEFERRABLE ,
			"MODIFIED_IN" Varchar2 (4000) NOT NULL DEFERRABLE ,
			"REPL_APPROVED" Number(1,0) Default 0 Check (REPL_APPROVED IN (0, 1) ) DEFERRABLE ,
			"SESSION_AUTH_BOOL" Number(1,0) Default 0 Check (SESSION_AUTH_BOOL IN (0, 1) ) DEFERRABLE ,
			"UTL_ID" Number(10,0) Check (UTL_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		 Constraint "PK_UTL_ACTION_CONFIG_PARM" primary key ("PARM_NAME") 
		) 
	');
END;
/

--changeSet DEV-1253:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN	
	utl_migr_schema_pkg.table_create('
		Create table "UTL_ACTION_ROLE_PARM" (
			"ROLE_ID" Number(10,0) NOT NULL DEFERRABLE ,
			"PARM_NAME" Varchar2 (500) NOT NULL DEFERRABLE ,
			"PARM_VALUE" Varchar2 (1000),
			"SESSION_AUTH_BOOL" Number(1,0) Default 0 Check (SESSION_AUTH_BOOL IN (0, 1) ) DEFERRABLE ,
			"UTL_ID" Number(10,0) NOT NULL DEFERRABLE  Check (UTL_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
		 Constraint "PK_UTL_ACTION_ROLE_PARM" primary key ("ROLE_ID","PARM_NAME") 
		) 	
	');
END;
/

--changeSet DEV-1253:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	utl_migr_schema_pkg.table_create('
		Create table "UTL_ACTION_USER_PARM" (
			"USER_ID" Number(10,0) NOT NULL DEFERRABLE ,
			"PARM_NAME" Varchar2 (500) NOT NULL DEFERRABLE ,
			"PARM_VALUE" Varchar2 (1000),
			"SESSION_AUTH_BOOL" Number(1,0) Default 0 Check (SESSION_AUTH_BOOL IN (0, 1) ) DEFERRABLE ,
			"UTL_ID" Number(10,0) NOT NULL DEFERRABLE  Check (UTL_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
			"RSTAT_CD" Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
			"CREATION_DT" Date NOT NULL DEFERRABLE ,
			"REVISION_DT" Date NOT NULL DEFERRABLE ,
			"REVISION_DB_ID" Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
			"REVISION_USER" Varchar2 (30) NOT NULL DEFERRABLE ,
		 Constraint "PK_UTL_ACTION_USER_PARM" primary key ("USER_ID","PARM_NAME") 
		)	
	');
END;
/		

--changeSet DEV-1253:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
	--
	-- create constraints for the new tables
	--
BEGIN
	-- UTL_ACTION_CONFIG_PARM FK to MIM_DB
	utl_migr_schema_pkg.table_constraint_add('
		Alter table "UTL_ACTION_CONFIG_PARM" add Constraint "FK_UTLACTCFGPRM_MIMDB" foreign key ("UTL_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
	');
END;
/

--changeSet DEV-1253:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- UTL_ACTION_ROLE_PARM FK to UTL_ACTION_CONFIG_PARM
	utl_migr_schema_pkg.table_constraint_add('
		Alter table "UTL_ACTION_ROLE_PARM" add Constraint "FK_UTLACTCFGPRM_UTLROLEPARM" foreign key ("PARM_NAME") references "UTL_ACTION_CONFIG_PARM" ("PARM_NAME")  DEFERRABLE
	');
END;
/

--changeSet DEV-1253:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- UTL_ACTION_ROLE_PARM FK to UTL_ROLE
	utl_migr_schema_pkg.table_constraint_add('
		Alter table "UTL_ACTION_ROLE_PARM" add Constraint "FK_UTLROLE_UTLACTROLEPARM" foreign key ("ROLE_ID") references "UTL_ROLE" ("ROLE_ID")  DEFERRABLE
	');
END;
/

--changeSet DEV-1253:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- UTL_ACTION_ROLE_PARM FK to MIM_DB
	utl_migr_schema_pkg.table_constraint_add('
		Alter table "UTL_ACTION_ROLE_PARM" add Constraint "FK_UTLACTRLPRM_MIMDB" foreign key ("UTL_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
	');
END;
/

--changeSet DEV-1253:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- UTL_ACTION_USER_PARM FK to UTL_ACTION_CONFIG_PARM
	utl_migr_schema_pkg.table_constraint_add('
		Alter table "UTL_ACTION_USER_PARM" add Constraint "FK_UTLACTCFGPRM_UTLACTUSRPRM" foreign key ("PARM_NAME") references "UTL_ACTION_CONFIG_PARM" ("PARM_NAME")  DEFERRABLE
	');
END;
/

--changeSet DEV-1253:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- UTL_ACTION_USER_PARM FK to UTL_USER
	utl_migr_schema_pkg.table_constraint_add('
		Alter table "UTL_ACTION_USER_PARM" add Constraint "FK_UTLUSER_UTLACTUSERPARM" foreign key ("USER_ID") references "UTL_USER" ("USER_ID")  DEFERRABLE
	');
END;
/

--changeSet DEV-1253:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- UTL_ACTION_USER_PARM FK to MIM_DB
	utl_migr_schema_pkg.table_constraint_add('
		Alter table "UTL_ACTION_USER_PARM" add Constraint "FK_UTLACTUSRPRM_MIMDB" foreign key ("UTL_ID") references "MIM_DB" ("DB_ID")  DEFERRABLE
	');
END;
/

--changeSet DEV-1253:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- UTL_ACTION_USER_PARM FK to MIM_RSTAT
	utl_migr_schema_pkg.table_constraint_add('
		Alter table "UTL_ACTION_USER_PARM" add Constraint "FK_MIMRSTAT_UTLACTUSERPARM" foreign key ("RSTAT_CD") references "MIM_RSTAT" ("RSTAT_CD")  DEFERRABLE
	');
END;
/			

--changeSet DEV-1253:12 stripComments:false
	--
	-- copy action config parm rows from UTL_xxx_PARM tables to UTL_ACTION_xxx_PARM tables
	-- (apps are considered actions as they exist in UTL_TODO_BUTTON)
	--
	INSERT INTO UTL_ACTION_CONFIG_PARM 
	SELECT 
		PARM_NAME,
		PARM_VALUE,
		ENCRYPT_BOOL,
		PARM_DESC,
		DEFAULT_VALUE,
		ALLOW_VALUE_DESC,
		MAND_CONFIG_BOOL,
		CATEGORY,
		MODIFIED_IN,
		REPL_APPROVED,
		0,  -- SESSION_AUTH_BOOL
		UTL_ID
	FROM 
		UTL_CONFIG_PARM 
	WHERE
		(
			PARM_NAME LIKE 'ACTION|_%' ESCAPE '|' OR
			PARM_NAME LIKE 'APP|_%' ESCAPE '|'
		)
		AND
		PARM_TYPE   = 'SECURED_RESOURCE' AND 
		CONFIG_TYPE = 'USER'
		AND
		NOT EXISTS (
		SELECT 
			1 
		FROM 
			UTL_ACTION_CONFIG_PARM 
		WHERE 
			UTL_ACTION_CONFIG_PARM.PARM_NAME = UTL_CONFIG_PARM.PARM_NAME
		)
	;	

--changeSet DEV-1253:13 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	   DBMS_STATS.GATHER_TABLE_STATS(USER,'UTL_ACTION_CONFIG_PARM');
END;
/

--changeSet DEV-1253:14 stripComments:false
	INSERT INTO UTL_ACTION_ROLE_PARM 
	SELECT 
		UTL_ROLE_PARM.ROLE_ID,
		UTL_ROLE_PARM.PARM_NAME,
		UTL_ROLE_PARM.PARM_VALUE,
		0,  -- SESSION_AUTH_BOOL
		UTL_ROLE_PARM.UTL_ID
	FROM 
		UTL_ROLE_PARM 
	INNER JOIN UTL_ACTION_CONFIG_PARM ON
		UTL_ACTION_CONFIG_PARM.PARM_NAME = UTL_ROLE_PARM.PARM_NAME
	WHERE 
		NOT EXISTS (
			SELECT 
				1 
            FROM 
				UTL_ACTION_ROLE_PARM 
            WHERE 
				UTL_ACTION_ROLE_PARM.ROLE_ID = UTL_ROLE_PARM.ROLE_ID AND
				UTL_ACTION_ROLE_PARM.PARM_NAME = UTL_ROLE_PARM.PARM_NAME
			);
	

--changeSet DEV-1253:15 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	   DBMS_STATS.GATHER_TABLE_STATS(USER,'UTL_ACTION_ROLE_PARM');
END;
/

--changeSet DEV-1253:16 stripComments:false
	INSERT INTO UTL_ACTION_USER_PARM
	SELECT 
		UTL_USER_PARM.USER_ID,
		UTL_USER_PARM.PARM_NAME,
		UTL_USER_PARM.PARM_VALUE,
		0,  -- SESSION_AUTH_BOOL
		UTL_USER_PARM.UTL_ID,
		UTL_USER_PARM.RSTAT_CD,
		UTL_USER_PARM.CREATION_DT,
		UTL_USER_PARM.REVISION_DT,
		UTL_USER_PARM.REVISION_DB_ID,
		UTL_USER_PARM.REVISION_USER
	FROM 
		UTL_USER_PARM 
	INNER JOIN UTL_ACTION_CONFIG_PARM ON
		UTL_ACTION_CONFIG_PARM.PARM_NAME   = UTL_USER_PARM.PARM_NAME
	WHERE 
		NOT EXISTS (
			SELECT 
				1 
         FROM 
				UTL_ACTION_USER_PARM 
         WHERE 
				UTL_ACTION_USER_PARM.USER_ID   = UTL_USER_PARM.USER_ID AND
				UTL_ACTION_USER_PARM.PARM_NAME = UTL_USER_PARM.PARM_NAME
			);
	

--changeSet DEV-1253:17 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	   DBMS_STATS.GATHER_TABLE_STATS(USER,'UTL_ACTION_USER_PARM');
END;
/		

--changeSet DEV-1253:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
	--
	-- update DB_TYPE_CONFIG_PARM by removing its FK to UTL_CONFIG_PARM and adding a FK to UTL_ACTION_CONFIG_PARM
	-- this FK is part of the PK, thus the PK will have to be recreated
	-- as well, remove the obsoleted PARM_TYPE column (all its PARM_TYPE's are SECURED_RESOURCE by definition)
	--
BEGIN
	-- 1. remove the FK to UTL_CONFIG_PARM
	utl_migr_schema_pkg.table_constraint_drop('DB_TYPE_CONFIG_PARM', 'FK_UTLCONFIGPARM_DBTYPECFGPARM');
END;
/

--changeSet DEV-1253:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- 2. remove the PK prior to removing PARM_TYPE
	utl_migr_schema_pkg.table_constraint_drop('DB_TYPE_CONFIG_PARM', 'PK_DB_TYPE_CONFIG_PARM');
	utl_migr_schema_pkg.index_drop('PK_DB_TYPE_CONFIG_PARM');
END;
/

--changeSet DEV-1253:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- 3. remove PARM_TYPE column 
	utl_migr_schema_pkg.table_column_drop('DB_TYPE_CONFIG_PARM', 'PARM_TYPE');
END;
/

--changeSet DEV-1253:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- 4. add the FK to UTL_ACTION_CONFIG_PARM
	utl_migr_schema_pkg.table_constraint_add('
		Alter table DB_TYPE_CONFIG_PARM add constraint FK_UTLACTCFGPRM_DBTYPECFGPRM foreign key (PARM_NAME) 
			references UTL_ACTION_CONFIG_PARM (PARM_NAME) DEFERRABLE
	');
END;
/

--changeSet DEV-1253:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- 5. recreate the PK without the PARM_TYPE column
	utl_migr_schema_pkg.table_constraint_add('
		Alter table DB_TYPE_CONFIG_PARM add constraint PK_DB_TYPE_CONFIG_PARM Primary key (PARM_NAME,DB_TYPE_CD)
	');
END;
/	

--changeSet DEV-1253:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
	--
	-- update UTL_TODO_BUTTON by removing its FK to UTL_CONFIG_PARM and adding a FK to UTL_ACTION_CONFIG_PARM 
	-- as well, remove the obsoleted PARM_TYPE column (all its PARM_TYPE's are SECURED_RESOURCE by definition)
	--
BEGIN
	-- 1. remove the FK to UTL_CONFIG_PARM
	utl_migr_schema_pkg.table_constraint_drop('UTL_TODO_BUTTON', 'FK_UTLCNFGPRM_UTLTODOBUTT');
END;
/

--changeSet DEV-1253:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- 2. remove PARM_TYPE column 
	utl_migr_schema_pkg.table_column_drop('UTL_TODO_BUTTON', 'PARM_TYPE');
END;
/

--changeSet DEV-1253:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	-- 3. add the FK to UTL_ACTION_CONFIG_PARM
	utl_migr_schema_pkg.table_constraint_add('
		Alter table UTL_TODO_BUTTON add constraint FK_UTLACTCNFGPRM_UTLTODOBUTT foreign key (PARM_NAME) 
			references UTL_ACTION_CONFIG_PARM (PARM_NAME) DEFERRABLE
	');
END;
/		

--changeSet DEV-1253:26 stripComments:false
	-- 
	-- remove action config parm rows from UTL_xxx_PARM tables 
	-- (the ones that were previously copied to UTL_ACTION_xxx_PARM tables)
	-- 
	DELETE FROM UTL_USER_PARM
	WHERE 
		PARM_TYPE = 'SECURED_RESOURCE' AND
		EXISTS (
			SELECT 
				1
			FROM 
				UTL_ACTION_USER_PARM
			WHERE
				UTL_ACTION_USER_PARM.USER_ID   = UTL_USER_PARM.USER_ID AND
				UTL_ACTION_USER_PARM.PARM_NAME = UTL_USER_PARM.PARM_NAME
		);
	

--changeSet DEV-1253:27 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	   DBMS_STATS.GATHER_TABLE_STATS(USER,'UTL_USER_PARM');
END;
/

--changeSet DEV-1253:28 stripComments:false
	DELETE FROM UTL_ROLE_PARM
	WHERE 
		PARM_TYPE = 'SECURED_RESOURCE' AND
		EXISTS (
			SELECT 
				1
			FROM 
				UTL_ACTION_ROLE_PARM
			WHERE
				UTL_ACTION_ROLE_PARM.ROLE_ID   = UTL_ROLE_PARM.ROLE_ID AND
				UTL_ACTION_ROLE_PARM.PARM_NAME = UTL_ROLE_PARM.PARM_NAME
		);
	

--changeSet DEV-1253:29 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	   DBMS_STATS.GATHER_TABLE_STATS(USER,'UTL_ROLE_PARM');
END;
/	

--changeSet DEV-1253:30 stripComments:false
	DELETE FROM UTL_CONFIG_PARM
	WHERE 
		PARM_TYPE   = 'SECURED_RESOURCE' AND
		CONFIG_TYPE = 'USER' AND 
		EXISTS (
			SELECT 
				1
			FROM 
				UTL_ACTION_CONFIG_PARM
			WHERE
				UTL_ACTION_CONFIG_PARM.PARM_NAME = UTL_CONFIG_PARM.PARM_NAME
		);
	

--changeSet DEV-1253:31 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
	   DBMS_STATS.GATHER_TABLE_STATS(USER,'UTL_CONFIG_PARM');
END;
/

--changeSet DEV-1253:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- create triggers for populating audit columns of UTL_ACTION_USER_PARM
--
CREATE OR REPLACE TRIGGER "TIBR_UTL_ACTION_USER_PARM" BEFORE INSERT
   ON "UTL_ACTION_USER_PARM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setinsertaudit(
    :new.rstat_cd,
    :new.creation_dt,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-1253:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_UTL_ACTION_USER_PARM" BEFORE UPDATE
   ON "UTL_ACTION_USER_PARM" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
declare
  ln_error NUMBER := 0;
  lv_error VARCHAR2(32767) := '';
  audit_error EXCEPTION;
begin
  application_object_pkg.setupdateaudit(
    :old.rstat_cd,
    :new.rstat_cd,
    :new.revision_dt,
    :new.revision_db_id,
    :new.revision_user,
    ln_error,
    lv_error);
  IF ln_error <> 0 THEN
    RAISE_APPLICATION_ERROR (ln_error, lv_error);
  END IF;
end;
/

--changeSet DEV-1253:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- replace:
--   isAuthorizedForResource.fnc
--   utl_migr_data_pkg_spec.sql
--   utl_migr_data_pkg_body.sql
--
/********************************************************************************
*
* Function:      isAuthorizedForResource
* Arguments:     aUserId   - user id
*                aParmName - config parm name
* Description:   This function determines if the user has authority
*                over the config parm.
*
* Orig.Coder:    Wayne Leroux
* Recent Coder:  ahogan
* Recent Date:   Dec 7th, 2011
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isAuthorizedForResource
(
   aUserId      utl_user.user_id%TYPE,
   aParmName    utl_config_parm.parm_name%TYPE
)  RETURN NUMBER
IS

   lParmValue utl_config_parm.parm_value%TYPE;
   lAuthorizedForResource NUMBER;
  
   CURSOR lUserParmCursor(aUserId utl_user.user_id%TYPE, aParmName utl_config_parm.parm_name%TYPE) IS
   SELECT
      utl_user_parm.parm_value
   FROM
      utl_user
      INNER JOIN utl_user_parm ON
         utl_user_parm.user_id = utl_user.user_id
   WHERE
      utl_user.user_id = aUserId
      AND
     utl_user_parm.parm_name = aParmName;

   -- need to retrieve row with lowest role_order
   CURSOR lRoleParmCursor(aUserId utl_user.user_id%TYPE, aParmName utl_config_parm.parm_name%TYPE) IS
   SELECT
      parm_value
   FROM
      (
         SELECT
            utl_role_parm.parm_value
         FROM
            utl_user_role
            INNER JOIN utl_role_parm ON
               utl_role_parm.role_id = utl_user_role.role_id
         WHERE
            utl_user_role.user_id = aUserId
            AND
            utl_role_parm.parm_name = aParmName
      )
   WHERE
      ROWNUM = 1;
  
   CURSOR lGlobalParmCursor(aParmName utl_config_parm.parm_name%TYPE) IS
   SELECT
      utl_config_parm.parm_value
   FROM
      utl_config_parm
   WHERE
      utl_config_parm.parm_name = aParmName;
     
   CURSOR lActionUserParmCursor(aUserId utl_user.user_id%TYPE, aParmName utl_action_user_parm.parm_name%TYPE) IS
   SELECT
      utl_action_user_parm.parm_value
   FROM
      utl_user
      INNER JOIN utl_action_user_parm ON
         utl_action_user_parm.user_id = utl_user.user_id
   WHERE
      utl_user.user_id = aUserId
      AND
      utl_action_user_parm.parm_name = aParmName;

   -- need to retrieve row with lowest role_order
   CURSOR lActionRoleParmCursor(aUserId utl_user.user_id%TYPE, aParmName utl_action_role_parm.parm_name%TYPE) IS
   SELECT
      parm_value
   FROM
      (
         SELECT
            utl_action_role_parm.parm_value
         FROM
            utl_user_role
            INNER JOIN utl_action_role_parm ON
               utl_action_role_parm.role_id = utl_user_role.role_id
         WHERE
            utl_user_role.user_id = aUserId
            AND
            utl_action_role_parm.parm_name = aParmName
      )
   WHERE
      ROWNUM = 1;
  
   CURSOR lActionGlobalParmCursor(aParmName utl_action_config_parm.parm_name%TYPE) IS
   SELECT
      utl_action_config_parm.parm_value
   FROM
      utl_action_config_parm
   WHERE
      utl_action_config_parm.parm_name = aParmName;
     
BEGIN
   lParmValue := NULL;

   -- The parm should be in the action parm tables, so check them first
  
   -- Get Action User Parm Value
   FOR lRec IN lActionUserParmCursor( aUserId, aParmName ) LOOP
      lParmValue := lRec.parm_value;
   END LOOP;

   -- If Action User Parm Value is not set, use Action Role Parm Value
   IF lParmValue IS NULL THEN
      FOR lRec IN lActionRoleParmCursor(aUserId, aParmName) LOOP
         lParmValue := lRec.parm_value;
      END LOOP;
    END IF;
  
   -- If Action Role Parm Value is not set, use Default Action Parm Value
   IF lParmValue IS NULL THEN
      FOR lRec IN lActionGlobalParmCursor(aParmName) LOOP
         lParmValue := lRec.parm_value;
      END LOOP;
   END IF;
  
  
   -- If parm was not found in the action parm tables 
   -- then check the config parm tables

   -- Get User Parm Value
   FOR lRec IN lUserParmCursor( aUserId, aParmName ) LOOP
      lParmValue := lRec.parm_value;
   END LOOP;

   -- If User Parm Value is not set, use Role Parm Value
   IF lParmValue IS NULL THEN
      FOR lRec IN lRoleParmCursor(aUserId, aParmName) LOOP
         lParmValue := lRec.parm_value;
      END LOOP;
   END IF;
  
   -- If Role Parm Value is not set, use Default Parm Value
   IF lParmValue IS NULL THEN
      FOR lRec IN lGlobalParmCursor(aParmName) LOOP
         lParmValue := lRec.parm_value;
      END LOOP;
   END IF;


   IF upper(lParmValue) = 'TRUE' THEN
      lAuthorizedForResource := 1;
   ELSIF lParmValue = '1' THEN
      lAuthorizedForResource := 1;
   ELSE
      lAuthorizedForResource := 0;
   END IF;
  
   RETURN lAuthorizedForResource;
  
END isAuthorizedForResource;
/

--changeSet DEV-1253:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace PACKAGE "UTL_MIGR_DATA_PKG" IS 

   ----------------------------------------------------------------------------
   -- Object Name : utl_migr_data_pkg
   -- Object Type : Package Body
   -- Date        : Aug 13, 2010
   -- Coder       : David Sewell
   -- Recent Date : Dec 7, 2011
   -- Recent Coder: ahogan
   -- Description :
   -- This is the migration data package containing all generic methods and
   -- constants for data-specific version migrations.
   ----------------------------------------------------------------------------
   -- Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   --
   ----------------------------------------------------------------------------
   -- Global types
   ----------------------------------------------------------------------------
   TYPE DbTypeCdList IS VARRAY(7) OF VARCHAR2(8);
   
   
   ----------------------------------------------------------------------------
   -- Global Constants
   ----------------------------------------------------------------------------
   --
   --
   ----------------------------------------------------------------------------
   -- Public Methods
   ----------------------------------------------------------------------------
   
   ----------------------------------------------------------------------------
   -- Procedure:   alert_type_delete
   -- Arguments:   p_alert_type  - alert type id
   -- Description: Deletes an alert type and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.alert_type_delete(
   --                 43
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE alert_type_delete(
               p_alert_type        IN utl_alert_type.alert_type_id%TYPE
      );
   
   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_copy
   -- Arguments:   p_parm_name(utl_config_parm.parm_name%TYPE)       
   --                 - parameter name
   --              p_parm_type
   --                 - parameter type
   --              p_config_type
   --                 - configuration type (GlOBAL or USER)
   --              p_migr_parm_name
   --                 - the parameter from which we are migrating
   -- Description: Migrates (copies) the value of a config parm to a new config parm.
   --              Both the migrating parm and the new config parm must exist.
   --              If the config type is user, the role and user parms are created.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_copy(
   --                 'HIDE_SCHED_LABOUR_HOURS', 
   --                 'SECURED_RESOURCE',
   --                 'USER',
   --                 'HIDE_SCHED_LABOUR_DAYS'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_copy(
               p_parm_name        IN utl_config_parm.parm_name%TYPE,
               p_parm_type        IN utl_config_parm.parm_type%TYPE,
               p_config_type      IN utl_config_parm.config_type%TYPE,
               p_migr_parm_name   IN utl_config_parm.parm_name%TYPE
      );
      
   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_delete
   -- Arguments:   p_parm_name  - parameter name
   -- Description: Deletes a config parm and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'HIDE_SCHED_LABOUR_HOURS'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_delete(
               p_parm_name        IN utl_config_parm.parm_name%TYPE
      );
   
   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_insert
   -- Arguments:   p_parm_name(utl_config_parm.parm_name%TYPE)       
   --                 - parameter name
   --              p_parm_type
   --                 - parameter type
   --              p_parm_desc
   --                 - parameter description
   --              p_config_type
   --                 - configuration type (GlOBAL or USER)
   --              p_allow_value_desc
   --                 - allowed values for the parameter
   --              p_default_value
   --                 - default value of the parameter
   --              p_mand_config_bool
   --                 - whether or not the parameter is mandatory
   --              p_category
   --                 - the parameter category
   --              p_modified_in
   --                 - the version in which the parm was modified
   --              p_utl_id
   --                 - the utl_id
   -- Description: Creates and if needed migrates a new config parm.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'HIDE_SCHED_LABOUR_HOURS', 
   --                 'SECURED_RESOURCE',
   --                 'Determines whether a user is allowed to view scheduled labour information.',
   --                 'USER',
   --                 'TRUE/FALSE',
   --                 'FALSE',
   --                 1,
   --                 'Task - Subtypes',
   --                 '7.5',
   --                 0
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_insert(
               p_parm_name        IN utl_config_parm.parm_name%TYPE,
               p_parm_type        IN utl_config_parm.parm_type%TYPE,
               p_parm_desc        IN utl_config_parm.parm_desc%TYPE,
               p_config_type      IN utl_config_parm.config_type%TYPE,
               p_allow_value_desc IN utl_config_parm.allow_value_desc%TYPE,
               p_default_value    IN utl_config_parm.default_value%TYPE,
               p_mand_config_bool IN utl_config_parm.mand_config_bool%TYPE,
               p_category         IN utl_config_parm.category%TYPE,
               p_modified_in      IN utl_config_parm.modified_in%TYPE,
               p_utl_id           IN utl_config_parm.utl_id%TYPE
      );
   
   
   ----------------------------------------------------------------------------
   -- Procedure:   action_parm_copy
   -- Arguments:   p_parm_name(utl_action_config_parm.parm_name%TYPE)       
   --                 - parameter name
   --              p_migr_parm_name
   --                 - the parameter from which we are migrating
   -- Description: Migrates (copies) the value of a config parm to a new config parm.
   --              Both the migrating parm and the new config parm must exist.
   --              The role and user parms are also created.
   -- SQL Usage:   exec utl_migr_data_pkg.action_parm_copy(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE action_parm_copy(
               p_parm_name        IN utl_action_config_parm.parm_name%TYPE,
               p_migr_parm_name   IN utl_action_config_parm.parm_name%TYPE
      );
      
   ----------------------------------------------------------------------------
   -- Procedure:   action_parm_delete
   -- Arguments:   p_parm_name  - parameter name
   -- Description: Deletes a action parm and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.action_parm_insert(
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE action_parm_delete(
               p_parm_name        IN utl_action_config_parm.parm_name%TYPE
      );
   
   ----------------------------------------------------------------------------
   -- Procedure:   action_parm_insert
   -- Arguments:   p_parm_name(utl_action_config_parm.parm_name%TYPE)       
   --                 - parameter name
   --              p_parm_desc
   --                 - parameter description
   --              p_allow_value_desc
   --                 - allowed values for the parameter
   --              p_default_value
   --                 - default value of the parameter
   --              p_mand_config_bool
   --                 - whether or not the parameter is mandatory
   --              p_category
   --                 - the parameter category
   --              p_modified_in
   --                 - the version in which the parm was modified
   --              p_session_auth_bool
   --                 - whether or not session authentication is enabled
   --              p_utl_id
   --                 - the utl_id
   --              p_db_types
   --                 - array of db typs to be inserted into db_type_config_parm
   -- Description: Creates and if needed migrates a new action parm.
   -- SQL Usage:   exec utl_migr_data_pkg.action_parm_insert(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'Permission to edit non-historic certification or inspection labour roles.',
   --                 'TRUE/FALSE',
   --                 'FALSE',
   --                 1,
   --                 'Maint - Tasks',
   --                 '7.1-SP2',
   --                 0,
   --                 0,
   --                 UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE action_parm_insert(
               p_parm_name         IN utl_action_config_parm.parm_name%TYPE,
               p_parm_desc         IN utl_action_config_parm.parm_desc%TYPE,
               p_allow_value_desc  IN utl_action_config_parm.allow_value_desc%TYPE,
               p_default_value     IN utl_action_config_parm.default_value%TYPE,
               p_mand_config_bool  IN utl_action_config_parm.mand_config_bool%TYPE,
               p_category          IN utl_action_config_parm.category%TYPE,
               p_modified_in       IN utl_action_config_parm.modified_in%TYPE,
               p_session_auth_bool IN utl_action_config_parm.session_auth_bool%TYPE,
               p_utl_id            IN utl_action_config_parm.utl_id%TYPE,
               p_db_types          IN DbTypeCdList
      );
   
   
END UTL_MIGR_DATA_PKG;
/

--changeSet DEV-1253:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace PACKAGE BODY "UTL_MIGR_DATA_PKG" IS 

   ----------------------------------------------------------------------------
   -- Object Name : utl_migr_data_pkg
   -- Object Type : Package Body
   -- Date        : Aug 13, 2010
   -- Coder       : David Sewell
   -- Recent Date : Dec 7, 2011
   -- Recent Coder: ahogan
   -- Description :
   -- This is the migration data package containing all generic methods and
   -- constants for data-specific version migrations.
   ----------------------------------------------------------------------------
   -- Copyright 2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- Public method bodies
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------

   ----------------------------------------------------------------------------
   -- Procedure:   alert_type_delete
   -- Arguments:   p_alert_type  - alert type id
   -- Description: Deletes an alert type and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.alert_type_delete(
   --                 43
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE alert_type_delete(
               p_alert_type        IN utl_alert_type.alert_type_id%TYPE
      ) IS
   BEGIN
   
      DELETE FROM utl_alert_type_role WHERE alert_type_id = p_alert_type;
      
      DELETE FROM utl_alert_log WHERE alert_id = p_alert_type;
      DELETE FROM utl_alert_parm WHERE alert_id = p_alert_type;
      DELETE FROM utl_alert_status_log WHERE alert_id = p_alert_type;
      DELETE FROM utl_user_alert WHERE alert_id = p_alert_type;
      DELETE FROM utl_alert WHERE alert_type_id = p_alert_type;
   
   END alert_type_delete;

   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_copy
   -- Arguments:   p_parm_name
   --                 - parameter name
   --              p_parm_type
   --                 - parameter type
   --              p_config_type
   --                 - configuration type (GlOBAL or USER)
   --              p_migr_parm_name
   --                 - the parameter from which we are migrating
   -- Description: Migrates (copies) the value of a config parm to a new config parm.
   --              Both the migrating parm and the new config parm must exist.
   --              If the config type is user, the role and user parms are created.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_copy(
   --                 'HIDE_SCHED_LABOUR_HOURS', 
   --                 'SECURED_RESOURCE',
   --                 'USER',
   --                 'HIDE_SCHED_LABOUR_DAYS'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_copy(
               p_parm_name        IN utl_config_parm.parm_name%TYPE,
               p_parm_type        IN utl_config_parm.parm_type%TYPE,
               p_config_type      IN utl_config_parm.config_type%TYPE,
               p_migr_parm_name   IN utl_config_parm.parm_name%TYPE
      ) IS
      
      v_count NUMBER;
      
   BEGIN
   
      -- update the base config parm
      UPDATE
         UTL_CONFIG_PARM
      SET
         PARM_VALUE = (
            SELECT 
               NVL(old_parm.parm_value, default_value)
            FROM
               utl_config_parm old_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_parm.parm_name = p_migr_parm_name
            )
      WHERE
         parm_name = p_parm_name
         AND
         parm_type = p_parm_type
         AND
         EXISTS (
            SELECT 
               1
            FROM
               utl_config_parm old_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_parm.parm_name = p_migr_parm_name
         );
      
      -- if the config type is user, we should migrate the existing role and user data
      IF p_config_type = 'USER' THEN      
         INSERT INTO
            UTL_ROLE_PARM
            (
               ROLE_ID, PARM_NAME, PARM_TYPE, PARM_VALUE, UTL_ID
            )
            SELECT
               old_role_parm.role_id,
               p_parm_name,
               p_parm_type,
               old_role_parm.parm_value,
               old_role_parm.utl_id
            FROM
               utl_role_parm old_role_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_role_parm.parm_name = p_migr_parm_name
               AND
               NOT EXISTS ( SELECT 1 FROM utl_role_parm WHERE parm_name = p_parm_name AND role_id = old_role_parm.role_id );
      
         INSERT INTO
            UTL_USER_PARM
            (
               USER_ID, PARM_NAME, PARM_TYPE, PARM_VALUE, UTL_ID
            )
            SELECT
               old_user_parm.user_id,
               p_parm_name,
               p_parm_type,
               old_user_parm.parm_value,
               old_user_parm.utl_id
            FROM
               utl_user_parm old_user_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_user_parm.parm_name = p_migr_parm_name
               AND
               NOT EXISTS ( SELECT 1 FROM utl_user_parm WHERE parm_name = p_parm_name AND user_id = old_user_parm.user_id );
   
      END IF;
   
   END config_parm_copy;
   
   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_delete
   -- Arguments:   p_parm_name  - parameter name
   -- Description: Deletes a config parm and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'HIDE_SCHED_LABOUR_HOURS'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_delete(
               p_parm_name        IN utl_config_parm.parm_name%TYPE
      ) IS
   BEGIN
   
      DELETE FROM utl_user_parm WHERE parm_name = p_parm_name;
      DELETE FROM utl_role_parm WHERE parm_name = p_parm_name;
      DELETE FROM utl_config_parm WHERE parm_name = p_parm_name;
   
   END config_parm_delete;

   ----------------------------------------------------------------------------
   -- Procedure:   config_parm_insert
   -- Arguments:   p_parm_name   
   --                 - parameter name
   --              p_parm_type
   --                 - parameter type
   --              p_parm_desc
   --                 - parameter description
   --              p_config_type
   --                 - configuration type (GlOBAL or USER)
   --              p_allow_value_desc
   --                 - allowed values for the parameter
   --              p_default_value
   --                 - default value of the parameter
   --              p_mand_config_bool
   --                 - whether or not the parameter is mandatory
   --              p_category
   --                 - the parameter category
   --              p_modified_in
   --                 - the version in which the parm was modified
   --              p_utl_id
   --                 - the utl_id
   -- Description: Creates and if needed migrates a new config parm.
   -- SQL Usage:   exec utl_migr_data_pkg.config_parm_insert(
   --                 'HIDE_SCHED_LABOUR_HOURS', 
   --                 'SECURED_RESOURCE',
   --                 'Determines whether a user is allowed to view scheduled labour information.',
   --                 'USER',
   --                 'TRUE/FALSE',
   --                 'FALSE',
   --                 1,
   --                 'Task - Subtypes',
   --                 '7.5',
   --                 0
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE config_parm_insert(
               p_parm_name        IN utl_config_parm.parm_name%TYPE,
               p_parm_type        IN utl_config_parm.parm_type%TYPE,
               p_parm_desc        IN utl_config_parm.parm_desc%TYPE,
               p_config_type      IN utl_config_parm.config_type%TYPE,
               p_allow_value_desc IN utl_config_parm.allow_value_desc%TYPE,
               p_default_value    IN utl_config_parm.default_value%TYPE,
               p_mand_config_bool IN utl_config_parm.mand_config_bool%TYPE,
               p_category         IN utl_config_parm.category%TYPE,
               p_modified_in      IN utl_config_parm.modified_in%TYPE,
               p_utl_id           IN utl_config_parm.utl_id%TYPE
      ) IS
      
      v_count NUMBER;
      
   BEGIN
   
      -- insert the initial config parm
      INSERT INTO
         UTL_CONFIG_PARM
         (
            PARM_VALUE, PARM_NAME, PARM_TYPE, PARM_DESC, CONFIG_TYPE, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, UTL_ID
         )
         SELECT 
            p_default_value, 
            p_parm_name, 
            p_parm_type,
            p_parm_desc,
            p_config_type,
            p_allow_value_desc,
            p_default_value,
            p_mand_config_bool,
            p_category,
            p_modified_in,
            p_utl_id
         FROM
            dual
         WHERE
            NOT EXISTS ( SELECT 1 FROM utl_config_parm WHERE parm_name = p_parm_name AND parm_type = p_parm_type );
            
   END config_parm_insert;
   
   ----------------------------------------------------------------------------
   -- Procedure:   action_parm_copy
   -- Arguments:   p_parm_name(utl_action_config_parm.parm_name%TYPE)       
   --                 - parameter name
   --              p_migr_parm_name
   --                 - the parameter from which we are migrating
   -- Description: Migrates (copies) the value of a config parm to a new config parm.
   --              Both the migrating parm and the new config parm must exist.
   --              The role and user parms are also created.
   -- SQL Usage:   exec utl_migr_data_pkg.action_parm_copy(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE action_parm_copy(
               p_parm_name        IN utl_action_config_parm.parm_name%TYPE,
               p_migr_parm_name   IN utl_action_config_parm.parm_name%TYPE
      ) IS
      
      v_count NUMBER;
      
   BEGIN
   
      -- update the base action parm
      UPDATE
         UTL_ACTION_CONFIG_PARM
      SET
         PARM_VALUE = (
            SELECT 
               NVL(old_parm.parm_value, default_value)
            FROM
               utl_action_config_parm old_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_parm.parm_name = p_migr_parm_name
            )
      WHERE
         parm_name = p_parm_name
         AND
         EXISTS (
            SELECT 
               1
            FROM
               utl_action_config_parm old_parm
            WHERE
               p_migr_parm_name IS NOT NULL
               AND
               old_parm.parm_name = p_migr_parm_name
         );
      
      -- migrate the existing role and user data    
      INSERT INTO
         UTL_ACTION_ROLE_PARM
         (
            ROLE_ID, PARM_NAME, PARM_VALUE, SESSION_AUTH_BOOL, UTL_ID
         )
         SELECT
            old_role_parm.role_id,
            p_parm_name,
            old_role_parm.parm_value,
            old_role_parm.session_auth_bool,
            old_role_parm.utl_id
         FROM
            utl_action_role_parm old_role_parm
         WHERE
            p_migr_parm_name IS NOT NULL
            AND
            old_role_parm.parm_name = p_migr_parm_name
            AND
            NOT EXISTS ( SELECT 1 FROM utl_action_role_parm WHERE parm_name = p_parm_name AND role_id = old_role_parm.role_id );
      
      INSERT INTO
         UTL_ACTION_USER_PARM
         (
            USER_ID, PARM_NAME, PARM_VALUE, SESSION_AUTH_BOOL, UTL_ID
         )
         SELECT
            old_user_parm.user_id,
            p_parm_name,
            old_user_parm.parm_value,
            old_user_parm.session_auth_bool,
            old_user_parm.utl_id
         FROM
            utl_action_user_parm old_user_parm
         WHERE
            p_migr_parm_name IS NOT NULL
            AND
            old_user_parm.parm_name = p_migr_parm_name
            AND
            NOT EXISTS ( SELECT 1 FROM utl_action_user_parm WHERE parm_name = p_parm_name AND user_id = old_user_parm.user_id );
   
   END action_parm_copy;
   
      
   ----------------------------------------------------------------------------
   -- Procedure:   action_parm_delete
   -- Arguments:   p_parm_name  - parameter name
   -- Description: Deletes a action parm and all dependents.
   -- SQL Usage:   exec utl_migr_data_pkg.action_parm_insert(
   --                 'ACTION_UNCHECK_CERTIFICATION_ROLE'
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE action_parm_delete(
               p_parm_name        IN utl_action_config_parm.parm_name%TYPE
      ) IS
   BEGIN
   
      DELETE FROM db_type_config_parm WHERE parm_name = p_parm_name;
      
      DELETE FROM utl_action_user_parm WHERE parm_name = p_parm_name;
      DELETE FROM utl_action_role_parm WHERE parm_name = p_parm_name;
      DELETE FROM utl_action_config_parm WHERE parm_name = p_parm_name;
   
   END action_parm_delete;
   
   ----------------------------------------------------------------------------
   -- Procedure:   action_parm_insert
   -- Arguments:   p_parm_name(utl_action_config_parm.parm_name%TYPE)       
   --                 - parameter name
   --              p_parm_desc
   --                 - parameter description
   --              p_allow_value_desc
   --                 - allowed values for the parameter
   --              p_default_value
   --                 - default value of the parameter
   --              p_mand_config_bool
   --                 - whether or not the parameter is mandatory
   --              p_category
   --                 - the parameter category
   --              p_modified_in
   --                 - the version in which the parm was modified
   --              p_session_auth_bool
   --                 - whether or not session authentication is enabled
   --              p_utl_id
   --                 - the utl_id
   --              p_db_types
   --                 - array of db typs to be inserted into db_type_config_parm
   -- Description: Creates and if needed migrates a new action parm.
   -- SQL Usage:   exec utl_migr_data_pkg.action_parm_insert(
   --                 'ACTION_ALLOW_EDIT_CERT_INSP_REQ', 
   --                 'Permission to edit non-historic certification or inspection labour roles.',
   --                 'TRUE/FALSE',
   --                 'FALSE',
   --                 1,
   --                 'Maint - Tasks',
   --                 '7.1-SP2',
   --                 0,
   --                 0,
   --                 UTL_MIGR_DATA_PKG.DbTypeCdList('OPER','MASTER')
   --              );
   ----------------------------------------------------------------------------
   PROCEDURE action_parm_insert(
               p_parm_name         IN utl_action_config_parm.parm_name%TYPE,
               p_parm_desc         IN utl_action_config_parm.parm_desc%TYPE,
               p_allow_value_desc  IN utl_action_config_parm.allow_value_desc%TYPE,
               p_default_value     IN utl_action_config_parm.default_value%TYPE,
               p_mand_config_bool  IN utl_action_config_parm.mand_config_bool%TYPE,
               p_category          IN utl_action_config_parm.category%TYPE,
               p_modified_in       IN utl_action_config_parm.modified_in%TYPE,
               p_session_auth_bool IN utl_action_config_parm.session_auth_bool%TYPE,
               p_utl_id            IN utl_action_config_parm.utl_id%TYPE,
               p_db_types          IN DbTypeCdList
      ) IS
      
      v_count NUMBER;
      
   BEGIN
   
      -- insert the initial config parm
      INSERT INTO
         UTL_ACTION_CONFIG_PARM
         (
            PARM_VALUE, PARM_NAME, PARM_DESC, ALLOW_VALUE_DESC, DEFAULT_VALUE, MAND_CONFIG_BOOL, CATEGORY, MODIFIED_IN, SESSION_AUTH_BOOL, UTL_ID
         )
         SELECT 
            p_default_value, 
            p_parm_name, 
            p_parm_desc,
            p_allow_value_desc,
            p_default_value,
            p_mand_config_bool,
            p_category,
            p_modified_in,
            p_session_auth_bool,
            p_utl_id
         FROM
            dual
         WHERE
            NOT EXISTS ( SELECT 1 FROM utl_action_config_parm WHERE parm_name = p_parm_name );
            
      -- this section adds a db_type_config_parm for each value in p_db_types
      FOR i IN p_db_types.FIRST..p_db_types.LAST LOOP

         INSERT INTO
            DB_TYPE_CONFIG_PARM 
            (
               PARM_NAME, DB_TYPE_CD
            )
            SELECT
               p_parm_name, p_db_types(i)
            FROM
               dual
            WHERE
               NOT EXISTS (SELECT 1 FROM db_type_config_parm WHERE parm_name = p_parm_name AND db_type_cd = p_db_types(i));
      END LOOP;
   
   END action_parm_insert;

END UTL_MIGR_DATA_PKG;
/

--changeSet DEV-1253:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- ensure the sql gets recompiled
--
-- Create new configuration parameter ACTION_CREATE_TURN_OVERNIGHT_CHECK
BEGIN
UTL_MIGR_DATA_PKG.action_parm_insert(
'ACTION_CREATE_TURN_OVERNIGHT_CHECK',
'Permission to create a turn or overnight check in WebMaintenix',
'TRUE/FALSE',
'FALSE',
1,
'Maint - Work Packages',
'5.1',
0,
0,
UTL_MIGR_DATA_PKG.DbTypeCdList('OPER'));
END;
/