--liquibase formatted sql


--changeSet appl_obj_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "APPLICATION_OBJECT_PKG" IS
   /************************************************************************/
   /* Object Name : APPLICATION_OBJECT_PKG                                 */
   /* Object Type : Package Body                                           */
   /* Date  : July 19, 1997                                                */
   /* Coder : Jeff Cass                                                    */
   /* Recent Date : May 4, 2009                                            */
   /* Recent Coder: Ed Irvine                                              */
   /* Description :                                                        */
   /* This package retains the global services required by the application */
   /* This persistent repository is initialized once and provides database */
   /* level objects (Triggers, Packages) with transaction information      */
   /************************************************************************/
   /* Copyright © 1998, 1999, 2004 Mxi Technologies Ltd.                   */
   /* All Rights Reserved.                                                 */
   /* Any distribution of the Mxi Maintenix source code by any other party */
   /* than Mxi Technologies Ltd is prohibited.                             */
   /************************************************************************/

   /*--------------------------------------------------------
   *  INSTANCE VARIABLES
   *---------------------------------------------------------*/

   /* Instance variables */
   pkg_db_id     NUMBER(10);
   error_msgid   VARCHAR2(9);
   error_msgparm VARCHAR2(32767);

   /* The update error number and description */
   in_audit_upd_error NUMBER := -20001;
   iv_audit_upd_error VARCHAR2(32767) := 'Cannot update a non-active (non-0 rstat_cd) record.';

   /* The system, database and service pack version numbers */
   iv_pkg_version  VARCHAR2(10) := '5.1.1';
   iv_db_version   VARCHAR2(100) := '5.1.1';
   iv_service_pack VARCHAR2(10) := '';

   /*--------------------------------------------------------
   *  SET FUNCTIONS
   *---------------------------------------------------------*/
   /* Procedure to set the current User */
   PROCEDURE setuser(user_in IN VARCHAR2) IS
   BEGIN
      dbms_session.set_identifier(user_in);
   END setuser;

   /* Procedure to set the current error */
   PROCEDURE setmxierror(error_msgid_in   IN VARCHAR2,
                         error_msgparm_in IN VARCHAR2) IS
   BEGIN
      error_msgid   := error_msgid_in;
      error_msgparm := error_msgparm_in;
   END setmxierror;

   /* Procedure to append strings to the MXI Error Message Parms */
   PROCEDURE appendmxierror(error_msgparm_in IN VARCHAR2) IS
   BEGIN
      error_msgparm := rtrim(error_msgparm,
                             '@') || '@@@' || error_msgparm_in;
   END appendmxierror;

   /*--------------------------------------------------------
   *  GET FUNCTIONS
   *---------------------------------------------------------*/
   /* Function to get the current Database Identifier*/
   FUNCTION getdbid RETURN NUMBER IS
      ll_numdbid NUMBER;
   BEGIN
      /* if the package db_id is not defined yet */
      IF (pkg_db_id IS NULL)
      THEN
      
         /* make sure that there is only one local db_id */
         SELECT COUNT(*)
           INTO ll_numdbid
           FROM mim_local_db;
         IF ll_numdbid > 1
         THEN
            RETURN - 1;
         END IF;
         IF ll_numdbid = 0
         THEN
            RETURN - 99;
         END IF;
      
         /* get the local db_id */
         SELECT db_id
           INTO pkg_db_id
           FROM mim_local_db;
         RETURN pkg_db_id;
      
         /* if the package db_id has been defined */
      ELSE
         RETURN pkg_db_id;
      END IF;
   END getdbid;

   /* Function to get the current user */
   FUNCTION getuser RETURN VARCHAR2 IS
   
      session_user VARCHAR2(30);
   
   BEGIN
      SELECT sys_context('userenv',
                         'client_identifier')
        INTO session_user
        FROM dual;
   
      IF session_user IS NULL
         OR session_user = ''
      THEN
         RETURN USER;
      ELSE
         RETURN session_user;
      END IF;
   END getuser;

   /* Function to get Timestamp */
   FUNCTION gettimestamp RETURN DATE IS
   BEGIN
      RETURN SYSDATE;
   END gettimestamp;

   /* Function to get Mxi's Error msgid and msgparm */
   FUNCTION getmxierror RETURN VARCHAR2 IS
      ls_error_msg VARCHAR2(32767);
   BEGIN
      ls_error_msg  := error_msgid || '@@@' || error_msgparm;
      error_msgid   := NULL;
      error_msgparm := NULL;
      RETURN ls_error_msg;
   END getmxierror;

   /* Function to get the current execution mode */
   FUNCTION getexecmode RETURN VARCHAR2 IS
      ls_execmodecd VARCHAR2(8);
   BEGIN
      /*Get the local execution mode*/
      SELECT exec_mode_cd
        INTO ls_execmodecd
        FROM mim_local_db;
      RETURN ls_execmodecd;
   END getexecmode;

   /* Function to get the database's DB_type */
   FUNCTION getdbtypecd(an_db_id IN NUMBER) RETURN VARCHAR2 IS
      ls_dbtypecd VARCHAR2(8);
   BEGIN
      /*Get the DB_Type for the specified DB_id*/
      SELECT db_type_cd
        INTO ls_dbtypecd
        FROM mim_db
       WHERE db_id = an_db_id;
      RETURN ls_dbtypecd;
   END getdbtypecd;

   /*--------------------------------------------------------
   *  VERSION FUNCTIONS
   *
   *---------------------------------------------------------*/
   /* Function to get the current system version */
   FUNCTION getversion RETURN VARCHAR2 IS
   BEGIN
      RETURN iv_pkg_version;
   END getversion;

   /* Function to get the current system version */
   FUNCTION getdatabaseversion RETURN VARCHAR2 IS
   BEGIN
      RETURN iv_db_version;
   END getdatabaseversion;

   /* Function to get the current system version */
   FUNCTION getservicepacknumber RETURN VARCHAR2 IS
   BEGIN
      RETURN iv_service_pack;
   END getservicepacknumber;

   /*******************************************************************************
   *
   *  Procedure:  SetInsertAudit
   *  Description:   Called by the TIBR_* triggers on all tables, this procedure will
   *     will set the auditting attributes (creation and revision
   *     information) for the new record.
   *
   *  Arguments:  an_rstat_cd (number) - record status code
   *     adt_creation_dt (date) - creation date
   *     adt_revision_dt (date) - revision date
   *     an_revision_db_id (number) - revision database id
   *     an_revision_user (varchar2) - revision user
   *     an_error (number) - error number
   *     av_error (varchar2) - error text
   *  Returns: none
   *
   *  Original Coder:  Rob Vandenberg
   *  Recent Coder:
   *  Last Modified:   July 3, 1998
   *
   *********************************************************************************
   *
   *  Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE setinsertaudit(an_rstat_cd       IN OUT NUMBER,
                            adt_creation_dt   IN OUT DATE,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2,
                            an_error          IN OUT NUMBER,
                            av_error          IN OUT VARCHAR2) IS
   BEGIN
   
      -- gv_dpo_avoid_trigger is used in inventory transfer to avoid trigger
      IF application_object_pkg.gv_avoid_trigger = TRUE
      THEN
         -- Audit information only updated for live records
         IF an_rstat_cd <> 0
         THEN
            RETURN;
         ELSE
            adt_revision_dt   := gettimestamp;
            an_revision_db_id := getdbid;
            av_revision_user  := getuser;
         END IF;
         RETURN;
      END IF;
   
      IF an_rstat_cd IS NULL
      THEN
         an_rstat_cd := 0;
      END IF;
      IF adt_creation_dt IS NULL
      THEN
         adt_creation_dt := gettimestamp;
      END IF;
      adt_revision_dt   := gettimestamp;
      an_revision_db_id := getdbid;
      av_revision_user  := getuser;
   END setinsertaudit;

   /*******************************************************************************
   *
   *  Procedure:  SetInsertAudit
   *  Description:   Called by the TIBR_* triggers on all tables, this procedure will
   *     will set the auditting attributes (creation and revision
   *     information) for the new record.
   *
   *  Arguments:  an_rstat_cd (number) - record status code
   *     adt_creation_dt (date) - creation date
   *     adt_revision_dt (date) - revision date
   *     an_revision_db_id (number) - revision database id
   *     an_revision_user (varchar2) - revision user
   *     an_error (number) - error number
   *     av_error (varchar2) - error text
   *  Returns: none
   *
   *  Original Coder:  Rob Vandenberg
   *  Recent Coder:
   *  Last Modified:   July 3, 1998
   *
   *********************************************************************************
   *
   *  Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE setinsertaudit(adt_creation_dt   IN OUT DATE,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2,
                            an_error          IN OUT NUMBER,
                            av_error          IN OUT VARCHAR2) IS
   BEGIN
   
      -- gv_dpo_avoid_trigger is used in inventory transfer to avoid trigger
      IF application_object_pkg.gv_avoid_trigger = TRUE
      THEN
         -- Revision details will always be updated for DOps consolidation
         adt_revision_dt   := gettimestamp;
         an_revision_db_id := getdbid;
         av_revision_user  := getuser;
         RETURN;
      END IF;
   
      IF adt_creation_dt IS NULL
      THEN
         adt_creation_dt := gettimestamp;
      END IF;
      adt_revision_dt   := gettimestamp;
      an_revision_db_id := getdbid;
      av_revision_user  := getuser;
   END setinsertaudit;

   /*******************************************************************************
   *
   *  Procedure:  SetUpdateAudit
   *  Description:   Called by the TUBR_* triggers on all tables, this procedure will
   *     will set the auditting attributes (revision
   *     information) for the record.
   *
   *  Arguments:  an_new_rstat_cd (number) - new record status code
   *     an_old_rstat_cd (number) - old record status code
   *     adt_revision_dt (date) - revision date
   *     an_revision_db_id (number) - revision database id
   *     an_revision_user (varchar2) - revision user
   *     an_error (number) - error number
   *     av_error (varchar2) - error text
   *  Returns: none
   *
   *  Original Coder:  Rob Vandenberg
   *  Recent Coder:
   *  Last Modified:   July 3, 1998
   *
   *********************************************************************************
   *
   *  Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE setupdateaudit(an_old_rstat_cd   IN NUMBER,
                            an_new_rstat_cd   IN NUMBER,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2,
                            an_error          IN OUT NUMBER,
                            av_error          IN OUT VARCHAR2) IS
   BEGIN
   
      -- gv_dpo_avoid_trigger is used in inventory transfer to avoid trigger
      IF application_object_pkg.gv_avoid_trigger = TRUE
      THEN
         -- Audit info only updated for those records that are being updated to a new
         -- rstat_cd
         IF an_old_rstat_cd = an_new_rstat_cd
         THEN
            RETURN;
         ELSE
            adt_revision_dt   := gettimestamp;
            an_revision_db_id := getdbid;
            av_revision_user  := getuser;
         END IF;
         RETURN;
      END IF;
   
      -- If the old rstat_cd is non-active (non-0) then raise an error if the rstat_cd
      -- is changed except when gv_skip_rstat_upd_errors has been set to true (for cases
      -- such as migrations) where this errors should be skipped.
      IF an_old_rstat_cd <> 0
         AND an_old_rstat_cd = an_new_rstat_cd
         AND application_object_pkg.gv_skip_rstat_upd_errors = FALSE
      THEN
         --raise exception
         an_error := in_audit_upd_error;
         av_error := iv_audit_upd_error;
      ELSE
         adt_revision_dt   := gettimestamp;
         an_revision_db_id := getdbid;
         av_revision_user  := getuser;
      END IF;
   END setupdateaudit;

   /*******************************************************************************
   *
   *  Procedure:  SetLocalDb
   *  Description:   Procedure to insert the initial db into mim_local_db and mim_db
   *  Arguments:  an_DbId  (NUMBER)
   *           av_DbTypeCd (VARCHAR2)
   *           av_License (VARCHAR2)
   *  Returns: none
   *
   *  Original Coder:  Joanne Levasseur
   *  Recent Coder:
   *  Last Modified:   November 17, 1998
   *
   *********************************************************************************
   *
   *  Copyright © 1998 MxI Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE setlocaldb(an_dbid     IN NUMBER,
                        av_dbtypecd IN VARCHAR2,
                        av_license  IN VARCHAR2) IS
   BEGIN
      INSERT INTO mim_local_db
         (db_id)
      VALUES
         (an_dbid);
   
      INSERT INTO mim_db
         (db_id,
          db_type_cd,
          site_cd,
          db_name)
      VALUES
         (an_dbid,
          av_dbtypecd,
          '0',
          'New Database');
   END setlocaldb;

   /*******************************************************************************
   *
   *  Procedure:  SetInsertControl
   *  Description:   Called by the TIBR_* triggers by tables whose data is can
   *                 distributed across multiple sites.
   *
   *  Arguments:
   *     an_control_db_id (number) - control database id
   *     an_error (number) - error number
   *     av_error (varchar2) - error text
   *  Returns: none
   *
   *  Original Coder:  Rob Vandenberg
   *  Recent Coder:
   *  Last Modified:   October 11, 2005
   *
   *********************************************************************************
   *
   *  Copyright © 2005 MxI Technologies Ltd.  All Rights Reserved.
   *  Any distribution of the MxI source code by any other party than
   *  MxI Technologies Ltd is prohibited.
   *
   *********************************************************************************/
   PROCEDURE setinsertcontrol(an_ctrl_db_id IN OUT NUMBER,
                              an_error      IN OUT NUMBER,
                              av_error      IN OUT VARCHAR2) IS
   BEGIN
      /* For now, return the database identifier from mim_local_db. */
      /* This logic will likely be replaced at a later date. */
      an_ctrl_db_id := getdbid;
   END setinsertcontrol;

END application_object_pkg;
/