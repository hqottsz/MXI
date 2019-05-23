--liquibase formatted sql


--changeSet mx_trigger_pkg_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "MX_TRIGGER_PKG" IS
  /************************************************************************/
  /* Object Name : MX_TRIGGER_PKG                                 */
  /* Object Type : Package Header                                         */
  /* Date        : OCT-5-2010                                         */
  /* Coder       : sdevi                                             */
  /* Recent Date : OCT-5-2010                                             */
  /* Recent Coder:                                               */
  /* Description :                                                        */
  /* This package contains procedures to set audit and other common attributes on insert and update. */
  /************************************************************************/

  /* Procedure to set the audit attributes on insert */
  /* DEPRECATED */
  PROCEDURE before_insert(an_rstat_cd       IN OUT NUMBER,
                          an_revision_no    IN OUT NUMBER,
                          an_ctrl_db_id     IN OUT NUMBER,
                          adt_creation_dt   IN OUT DATE,
                          an_creation_db_id IN OUT NUMBER,
                          adt_revision_dt   IN OUT DATE,
                          an_revision_db_id IN OUT NUMBER,
                          av_revision_user  IN OUT VARCHAR2);

  /* Procedure to set the audit attributes on insert */
  PROCEDURE before_insert(an_rstat_cd       IN OUT NUMBER,
                          an_revision_no    IN OUT NUMBER,
                          an_ctrl_db_id     IN OUT NUMBER,
                          adt_creation_dt   IN OUT DATE,
                          adt_revision_dt   IN OUT DATE,
                          an_revision_db_id IN OUT NUMBER,
                          av_revision_user  IN OUT VARCHAR2);

  /* Procedure to set the audit attributes on update */
  PROCEDURE before_update(an_old_rstat_cd    IN NUMBER,
                          an_new_rstat_cd    IN NUMBER,
                          an_old_revision_no IN NUMBER,
                          an_new_revision_no IN OUT NUMBER,
                          adt_revision_dt    IN OUT DATE,
                          an_revision_db_id  IN OUT NUMBER,
                          av_revision_user   IN OUT VARCHAR2);

END MX_TRIGGER_PKG;
/