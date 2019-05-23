--liquibase formatted sql


--changeSet mx_trigger_pkg_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "MX_TRIGGER_PKG" IS

  /************************************************************************/
  /* Object Name : MX_TRIGGER_PKG                                 */
  /* Object Type : Package Header                                 */
  /* Date        : OCT-5-2010                                     */
  /* Coder       : sdevi                                          */
  /* Recent Date : OCT-5-2010                                     */
  /* Recent Coder:                                                */
  /* Description :                                                */
  /* This package contains procedures to set audit and other common attributes on insert and update. */
  /************************************************************************/

  /*--------------------------------------------------------
  *  INSTANCE VARIABLES
  *---------------------------------------------------------*/

  /* The update error number and description */
  in_audit_upd_error          NUMBER := -20001;
  iv_audit_upd_error_inactive VARCHAR2(32767) := 'Cannot update a non-active (non-0 rstat_cd) record.';
  iv_audit_upd_error_optlock  VARCHAR2(32767) := 'Cannot update a record with stale data: the supplied revision_no is less than the current revision_no.';
  iv_no_revision_increment    NUMBER := -1;

  /*******************************************************************************
  *
  *  Procedure:  before_insert
  *  Description:   Called by the TIBR_* triggers on all tables, this procedure will
  *     will set the auditting attributes (creation and revision
  *     information) for the new record.
  *
  *  Arguments:  an_rstat_cd (number) - record status code
  *     an_revision_no (number) - revision number
  *     an_ctrl_db_id (number) - control database id
  *     adt_creation_dt (date) - creation date
  *     an_creation_db_id (number) - DEPRECATED - creation database id
  *     adt_revision_dt (date) - revision date
  *     an_revision_db_id (number) - revision database id
  *     an_revision_user (varchar2) - revision user
  *  Returns: none
  *
  *  Original Coder: sdevi
  *  Recent Coder:
  *  Last Modified: OCT-5-2010
  *********************************************************************************/
  PROCEDURE before_insert(an_rstat_cd       IN OUT NUMBER,
                          an_revision_no    IN OUT NUMBER,
                          an_ctrl_db_id     IN OUT NUMBER,
                          adt_creation_dt   IN OUT DATE,
                          an_creation_db_id IN OUT NUMBER,
                          adt_revision_dt   IN OUT DATE,
                          an_revision_db_id IN OUT NUMBER,
                          av_revision_user  IN OUT VARCHAR2) IS
  BEGIN
  
    -- gv_dpo_avoid_trigger is used in inventory transfer to avoid trigger
    IF application_object_pkg.gv_avoid_trigger = TRUE THEN
      -- Audit information only updated for live records
      IF an_rstat_cd <> 0 THEN
        RETURN;
      ELSE
        an_ctrl_db_id     := NVL(an_ctrl_db_id,
                                 application_object_pkg.getdbid);
        an_creation_db_id := NVL(an_creation_db_id,
                                 application_object_pkg.getdbid);
        -- if revision number not provided then set it to 1
        IF an_revision_no = 0 OR an_revision_no IS NULL THEN
          an_revision_no := 1;
        END IF;
        adt_revision_dt   := application_object_pkg.gettimestamp;
        an_revision_db_id := application_object_pkg.getdbid;
        av_revision_user  := application_object_pkg.getuser;
      END IF;
      RETURN;
    END IF;
  
    IF an_rstat_cd IS NULL THEN
      an_rstat_cd := 0;
    END IF;
    adt_creation_dt   := NVL(adt_creation_dt,
                             application_object_pkg.gettimestamp);
    an_ctrl_db_id     := NVL(an_ctrl_db_id, application_object_pkg.getdbid);
    an_creation_db_id := NVL(an_creation_db_id,
                             application_object_pkg.getdbid);
    -- if revision number not provided then set it to 1
    IF an_revision_no = 0 OR an_revision_no IS NULL THEN
      an_revision_no := 1;
    END IF;
    adt_revision_dt   := application_object_pkg.gettimestamp;
    an_revision_db_id := application_object_pkg.getdbid;
    av_revision_user  := application_object_pkg.getuser;
  END before_insert;

  /*******************************************************************************
  *
  *  Procedure:  before_insert
  *  Description:   Called by the TIBR_* triggers on all tables, this procedure will
  *     will set the auditting attributes (creation and revision
  *     information) for the new record.
  *
  *  Arguments:  an_rstat_cd (number) - record status code
  *     an_revision_no (number) - revision number
  *     an_ctrl_db_id (number) - control database id
  *     adt_creation_dt (date) - creation date
  *     an_creation_db_id (number) - DEPRECATED - creation database id
  *     adt_revision_dt (date) - revision date
  *     an_revision_db_id (number) - revision database id
  *     an_revision_user (varchar2) - revision user
  *  Returns: none
  *
  *  Original Coder: sdevi
  *  Recent Coder:
  *  Last Modified: OCT-5-2010
  *********************************************************************************/
  PROCEDURE before_insert(an_rstat_cd       IN OUT NUMBER,
                          an_revision_no    IN OUT NUMBER,
                          an_ctrl_db_id     IN OUT NUMBER,
                          adt_creation_dt   IN OUT DATE,
                          adt_revision_dt   IN OUT DATE,
                          an_revision_db_id IN OUT NUMBER,
                          av_revision_user  IN OUT VARCHAR2) IS
    an_creation_db_id NUMBER(10) := null;
  BEGIN
    before_insert(an_rstat_cd,
                  an_revision_no,
                  an_ctrl_db_id,
                  adt_creation_dt,
                  an_creation_db_id,
                  adt_revision_dt,
                  an_revision_db_id,
                  av_revision_user);
  END before_insert;

  /*******************************************************************************
  *
  *  Procedure:  before_update
  *  Description:   Called by the TUBR_* triggers on all tables, this procedure will
  *     will set the auditting attributes (revision
  *     information) for the record.
  *
  *  Arguments:  an_new_rstat_cd (number) - new record status code
  *     an_old_rstat_cd (number) - old record status code
  *     an_old_revision_no (number) - old record revision number
  *     an_new_revision_no (number) - new record revision number
  *     adt_revision_dt (date) - revision date
  *     an_revision_db_id (number) - revision database id
  *     an_revision_user (varchar2) - revision user
  *  Returns: none
  *
  *  Original Coder:sdevi
  *  Recent Coder:
  *  Last Modified: OCT-5-2010
  *********************************************************************************/
  PROCEDURE before_update(an_old_rstat_cd    IN NUMBER,
                          an_new_rstat_cd    IN NUMBER,
                          an_old_revision_no IN NUMBER,
                          an_new_revision_no IN OUT NUMBER,
                          adt_revision_dt    IN OUT DATE,
                          an_revision_db_id  IN OUT NUMBER,
                          av_revision_user   IN OUT VARCHAR2) IS
    ln_error NUMBER := 0;
    lv_error VARCHAR2(32767) := '';
  BEGIN
  
    -- gv_dpo_avoid_trigger is used in inventory transfer to avoid trigger
    IF application_object_pkg.gv_avoid_trigger = TRUE THEN
      -- Audit info only updated for those records that are being updated to a new
      -- rstat_cd
      IF an_old_rstat_cd = an_new_rstat_cd THEN
        RETURN;
      ELSE
        adt_revision_dt   := application_object_pkg.gettimestamp;
        an_revision_db_id := application_object_pkg.getdbid;
        av_revision_user  := application_object_pkg.getuser;
        IF an_new_revision_no = an_old_revision_no THEN
          an_new_revision_no := an_old_revision_no + 1;
        ELSE
          IF an_new_revision_no < an_old_revision_no THEN
            ln_error := in_audit_upd_error;
            lv_error := iv_audit_upd_error_optlock;
          END IF;
        END IF;
      END IF;
      RETURN;
    END IF;
  
    -- If the old rstat_cd is non-active (non-0) then raise an error if the rstat_cd
    -- is changed except when gv_skip_rstat_upd_errors has been set to true (for cases
    -- such as migrations) where this errors should be skipped.
    IF an_old_rstat_cd <> 0 AND an_old_rstat_cd = an_new_rstat_cd AND
       application_object_pkg.gv_skip_rstat_upd_errors = FALSE THEN
      --raise exception
      ln_error := in_audit_upd_error;
      lv_error := iv_audit_upd_error_inactive;
    ELSE
      adt_revision_dt   := application_object_pkg.gettimestamp;
      an_revision_db_id := application_object_pkg.getdbid;
      av_revision_user  := application_object_pkg.getuser;
      -- Only auto increment the revision number when the presented
      -- revision number is not iv_no_revision_increment (-1)
      IF an_new_revision_no <> iv_no_revision_increment THEN
        IF an_new_revision_no = an_old_revision_no THEN
          an_new_revision_no := an_old_revision_no + 1;
        ELSE
          IF an_new_revision_no < an_old_revision_no THEN
            ln_error := in_audit_upd_error;
            lv_error := iv_audit_upd_error_optlock;
          END IF;
        END IF;
      ELSE
        -- If the revision number is iv_no_revision_increment (-1), use the existing value
        an_new_revision_no := an_old_revision_no;
      END IF;
    END IF;
    -- raise error if there is any
    IF ln_error <> 0 THEN
      RAISE_APPLICATION_ERROR(ln_error, lv_error);
    END IF;
  END before_update;

END MX_TRIGGER_PKG;
/