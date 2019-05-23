--liquibase formatted sql


--changeSet appl_obj_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "APPLICATION_OBJECT_PKG" IS
   /************************************************************************/
   /* Object Name : APPLICATION_OBJECT_PKG                                 */
   /* Object Type : Package Header                                         */
   /* Date        : July 19, 1997                                          */
   /* Coder       : Jeff Cass                                              */
   /* Recent Date : May 4 2009                                             */
   /* Recent Coder: Ed Irvine                                              */
   /* Description :                                                        */
   /* This package retains the global services required by the application */
   /* This persistent repository is initialized once and provides database */
   /* level objects (Triggers, Packages) with transaction information      */
   /************************************************************************/
   /* Copyright © 1998, 1999 MxI Technologies Ltd.  All Rights Reserved.   */
   /* Any distribution of the MxI Maintenix source code by any other party */
   /* than MxI Technologies Ltd is prohibited.                             */
   /************************************************************************/

   /* A BOOLEAN value gv_dpo_avoid_trigger used in inventory transfer to avoid */
   /* insert and update trigger */
   gv_avoid_trigger BOOLEAN := FALSE;

   /* A BOOLEAN value gv_skip_rstat_upd_errors used in migrations to avoid */
   /* the logic that prevents updates of non-active (non-0 rstat_cd) records */
   gv_skip_rstat_upd_errors BOOLEAN := FALSE;
   
   /* Procedure to set the current User */
   PROCEDURE setuser(user_in IN VARCHAR2);
   /* Procedure to set the MXI Error Msgid and Message Parms */
   PROCEDURE setmxierror(error_msgid_in   IN VARCHAR2,
                         error_msgparm_in IN VARCHAR2);
   /* Procedure to append strings to the MXI Error Message Parms */
   PROCEDURE appendmxierror(error_msgparm_in IN VARCHAR2);
   /* Procedure to set the current version */
   FUNCTION getdbid RETURN NUMBER PARALLEL_ENABLE;
   /* Function to get the current user */
   FUNCTION getuser RETURN VARCHAR2 PARALLEL_ENABLE;
   /* Function to get the current system version */
   FUNCTION getversion RETURN VARCHAR2 PARALLEL_ENABLE;
   /* Function to get the current database version */
   FUNCTION getdatabaseversion RETURN VARCHAR2 PARALLEL_ENABLE;
   /* Function to get the current service pack number */
   FUNCTION getservicepacknumber RETURN VARCHAR2 PARALLEL_ENABLE;
   /* Function to get Timestamp */
   FUNCTION gettimestamp RETURN DATE PARALLEL_ENABLE;
   /* Procedure to set the MXI Error Msgid and Message Parms */
   FUNCTION getmxierror RETURN VARCHAR2 PARALLEL_ENABLE;
   /* Function to get the current execution mode */
   FUNCTION getexecmode RETURN VARCHAR2 PARALLEL_ENABLE;
   /* Function to get the local database's db_type */
   FUNCTION getdbtypecd(an_db_id IN NUMBER) RETURN VARCHAR2 PARALLEL_ENABLE;
   /* Procedure to set the audit attributes on insert */
   PROCEDURE setinsertaudit(an_rstat_cd       IN OUT NUMBER,
                            adt_creation_dt   IN OUT DATE,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2,
                            an_error          IN OUT NUMBER,
                            av_error          IN OUT VARCHAR2);
   /* Procedure to set the audit attributes on insert */
   PROCEDURE setinsertaudit(adt_creation_dt   IN OUT DATE,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2,
                            an_error          IN OUT NUMBER,
                            av_error          IN OUT VARCHAR2);
   /* Procedure to set the audit attributes on update */
   PROCEDURE setupdateaudit(an_old_rstat_cd   IN NUMBER,
                            an_new_rstat_cd   IN NUMBER,
                            adt_revision_dt   IN OUT DATE,
                            an_revision_db_id IN OUT NUMBER,
                            av_revision_user  IN OUT VARCHAR2,
                            an_error          IN OUT NUMBER,
                            av_error          IN OUT VARCHAR2);
   /* Procedure to insert the initial db into mim_local_db and mim_db*/
   PROCEDURE setlocaldb(an_dbid     IN NUMBER,
                        av_dbtypecd IN VARCHAR2,
                        av_license  IN VARCHAR2);
   /* Procedure to set the control attributes on insert */
   PROCEDURE setinsertcontrol(an_ctrl_db_id IN OUT NUMBER,
                              an_error      IN OUT NUMBER,
                              av_error      IN OUT VARCHAR2);

END application_object_pkg;
/