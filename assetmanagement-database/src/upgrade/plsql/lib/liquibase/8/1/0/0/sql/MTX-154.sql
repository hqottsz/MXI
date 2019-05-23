--liquibase formatted sql


--changeSet MTX-154:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "BLT_WF_CONTROLLER_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : BLT_WF_CONTROLLER_PKG
   -- Object Type : Package Header
   -- Date        : Nov 25,2011
   -- Coder       : Vince Chan
   -- Recent Date : Nov 25,2011
   -- Recent Coder: Vince Chan
   -- Description :
   -- This package contains methods for controlling the BLT worflow in
   -- a PLSQL framework.
   --
   ----------------------------------------------------------------------------
   -- Copyright @2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------


   -----------------------------------------------------------------------------------------
   -- Function:    check_jobs_running
   -- Arguments:    p_wf_log_id        -- Current worflow being run
   --               p_wf_id            -- Master workflow
   --               p_db_id            -- DB ID being used for run
   --               p_serial_no_oem    -- Root Serial Number
   --
   -- Description:  Runs the batch mapping and subsequent validation if requested.
   --
   -----------------------------------------------------------------------------------------
   FUNCTION check_jobs_running(p_job_no   IN NUMBER,
                               p_max_proc IN NUMBER,
                               p_wf_type  IN blt_ref_wf_log_type.ref_wf_log_sub_type_cd%TYPE) RETURN NUMBER;


   -----------------------------------------------------------------------------------------
   -- Function:    check_jobs_running
   -- Arguments:    p_wf_log_id        -- Current worflow being run
   --               p_wf_id            -- Master workflow
   --               p_db_id            -- DB ID being used for run
   --               p_serial_no_oem    -- Root Serial Number
   --
   -- Description:  Runs the batch mapping and subsequent validation if requested.
   --
   -----------------------------------------------------------------------------------------
   FUNCTION check_jobs_running(p_job_no   IN NUMBER,
                               p_max_proc IN NUMBER,
                               p_log_id   IN NUMBER,
                               p_wf_type  IN VARCHAR2) RETURN NUMBER;



   -----------------------------------------------------------------------------------------
   -- Function:     is_ref_wf_log_status_valid
   -- Arguments:    p_wf_log_status_cd
   --
   -- Description:  checks if wf_log_status_cd is valid
   --
   -----------------------------------------------------------------------------------------
   FUNCTION is_wf_log_status_valid(p_wf_log_status_cd IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE)
            RETURN NUMBER;

   -----------------------------------------------------------------------------------------
   -- Function:     is_wf_cycle_status_valid
   -- Arguments:    p_wf_cycle_status_cd
   --
   -- Description:  checks if ref_wf_cycle_status_cd is valid
   --
   -----------------------------------------------------------------------------------------
   FUNCTION is_wf_cycle_status_valid(p_wf_cycle_status_cd IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE)
            RETURN NUMBER;

   -----------------------------------------------------------------------------------------
   -- Procedure:    clean_blt_wf_cycle_log
   -- Arguments:
   --
   -- Description:  Deletes all data from the blt_wf_cycle_log table
   --
   -----------------------------------------------------------------------------------------
   PROCEDURE clean_blt_wf_cycle_log;
   -----------------------------------------------------------------------------------------
   -- Procedure:    clean_blt_wf_cycle_log
   -- Arguments:
   --
   -- Description:  Deletes all data from the blt_wf_cycle_log table
   --
   -----------------------------------------------------------------------------------------
   PROCEDURE clean_blt_wf_rec_log;

   -----------------------------------------------------------------------------------------
   -- Procedure:    insert_blt_wf_cycle_log
   -- Arguments:    p_wf_cycle_log_id         (NUMBER) -- takes and returns log id
   --               p_wf_cycle_cd             workflow cycle code
   --               p_wf_cycle_status_cd      workflow cycle status
   --               p_wf_cycle_start_dt       workflow cycle start date
   --               p_wf_cycle_end_dt         workflow cycle end date
   --               p_wf_cycle_err_status_cd  workflow cycle error status code
   --               p_wf_cycle_err_msg        workflow cycle error message
   -- Description:  Log procedure inserts and updates records into the
   --               blt_wf_cycle_log table
   -----------------------------------------------------------------------------------------
   PROCEDURE insert_blt_wf_cycle_log(p_wf_cycle_log_id  IN OUT VARCHAR2,
                         p_wf_cycle_cd              IN blt_wf_cycle_log.wf_cycle_cd%TYPE,
                         p_wf_cycle_status_cd       IN blt_wf_cycle_log.ref_wf_cycle_status_cd%TYPE,
                         p_wf_cycle_start_dt        IN blt_wf_cycle_log.wf_cycle_start_dt%TYPE,
                         p_wf_cycle_end_dt          IN blt_wf_cycle_log.wf_cycle_end_dt%TYPE,
                         p_wf_cycle_err_status_cd   IN blt_wf_cycle_log.wf_cycle_err_status_cd%TYPE,
                         p_wf_cycle_err_msg         IN blt_wf_cycle_log.wf_cycle_err_msg%TYPE);



   -----------------------------------------------------------------------------------------
   -- Procedure:    update_blt_wf_cycle_log
   -- Arguments:    p_wf_cycle_log_id         (NUMBER) -- takes and returns log id
   --               p_wf_cycle_cd             workflow cycle code
   --               p_wf_cycle_status_cd      workflow cycle status
   --               p_wf_cycle_start_dt       workflow cycle start date
   --               p_wf_cycle_end_dt         workflow cycle end date
   --               p_wf_cycle_err_status_cd  workflow cycle error status code
   --               p_wf_cycle_err_msg        workflow cycle error message
   -- Description:  Log procedure inserts and updates records into the
   --               blt_wf_cycle_log table
   -----------------------------------------------------------------------------------------
   PROCEDURE update_blt_wf_cycle_log(p_wf_cycle_log_id  IN  VARCHAR2,
                         p_wf_cycle_cd              IN blt_wf_cycle_log.wf_cycle_cd%TYPE,
                         p_wf_cycle_status_cd       IN blt_wf_cycle_log.ref_wf_cycle_status_cd%TYPE,
                         p_wf_cycle_start_dt        IN blt_wf_cycle_log.wf_cycle_start_dt%TYPE,
                         p_wf_cycle_end_dt          IN blt_wf_cycle_log.wf_cycle_end_dt%TYPE,
                         p_wf_cycle_err_status_cd   IN blt_wf_cycle_log.wf_cycle_err_status_cd%TYPE,
                         p_wf_cycle_err_msg         IN blt_wf_cycle_log.wf_cycle_err_msg%TYPE);




      -----------------------------------------------------------------------------------------
   -- Procedure:    log_staged_wf_rec_log
   -- Arguments:    p_wf_rec_log_id          varchar takes and returns log id
   --               p_wf_cycle_log_id        cycle id
   --               p_wf_log_type_cd         workflow log type
   --               p_run_mode               run mode (set based/parallel row based)
   --
   -- Description:  Updates status of staged records in blt_wf_rec_log to STGED/ERROR
   -----------------------------------------------------------------------------------------  
  PROCEDURE log_staged_wf_rec_log(p_wf_rec_log_id   IN  VARCHAR2,
                          p_wf_cycle_log_id   IN VARCHAR2,
                          p_wf_log_type_cd    IN blt_ref_wf_log_type.ref_wf_log_type_cd%TYPE,
                          p_wf_log_sub_type_cd    IN blt_ref_wf_log_type.ref_wf_log_sub_type_cd%TYPE,
                          p_run_mode          IN NUMBER
          );
          
   -----------------------------------------------------------------------------------------
   -- Procedure:    update_blt_wf_rec_log
   -- Arguments:    p_wf_rec_log_id          varchar takes and returns log id
   --               p_wf_cycle_log_id        cycle id
   --               p_wf_log_status_cd       workflow log status code
   --               p_wf_log_type_cd         workflow log type
   --               p_wf_log_rec_identifier  workflow record identifier
   --               p_wf_log_start_dt        workflow start date
   --               p_wf_log_end_dt          workflow end date
   --               p_wf_log_err_status_cd   workflow error status code
   --               p_wf_log_err_msg         workflow error message
   --               p_run_mode               run mode (set based/parallel row based)
   --
   -- Description:  Log procedure  updates records into the
   --               blt_wf_rec_logtable
   -----------------------------------------------------------------------------------------
  PROCEDURE update_blt_wf_rec_log(p_wf_rec_log_id   IN  VARCHAR2,
                            p_wf_cycle_log_id   IN VARCHAR2,
                            p_wf_log_status_cd  IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE,
                            p_wf_log_type_cd    IN blt_ref_wf_log_type.ref_wf_log_type_cd%TYPE,
                            p_wf_log_sub_type_cd    IN blt_ref_wf_log_type.ref_wf_log_sub_type_cd%TYPE,
                            p_wf_log_rec_identifier  IN blt_wf_rec_log.wf_log_rec_identifier%TYPE,
                            p_wf_log_start_dt   IN blt_wf_rec_log.wf_log_start_dt%TYPE,
                            p_wf_log_end_dt     IN blt_wf_rec_log.wf_log_end_dt%TYPE,
                            p_wf_log_err_status_cd IN blt_wf_rec_log.wf_log_err_status_cd%TYPE,
                            p_wf_log_err_msg       IN blt_wf_rec_log.wf_log_err_msg%TYPE,
                            p_run_mode          IN NUMBER);

   -----------------------------------------------------------------------------------------
   -- Procedure:    insert_blt_wf_rec_log
   -- Arguments:    p_wf_rec_log_id          varchar takes and returns log id
   --               p_wf_cycle_log_id        cycle id
   --               p_wf_log_status_cd       workflow log status code
   --               p_wf_log_type_cd         workflow log type
   --               p_wf_log_rec_identifier  workflow record identifier
   --               p_wf_log_start_dt        workflow start date
   --               p_wf_log_end_dt          workflow end date
   --               p_wf_log_err_status_cd   workflow error status code
   --               p_wf_log_err_msg         workflow error message
   --               p_run_mode               run mode (set based/parallel row based)
   --
   -- Description:  Log procedure inserts and updates records into the
   --               blt_wf_rec_logtable
   -----------------------------------------------------------------------------------------
   PROCEDURE insert_blt_wf_rec_log(p_wf_rec_log_id   IN OUT VARCHAR2,
                            p_wf_cycle_log_id   IN VARCHAR2,
                            p_wf_log_status_cd  IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE,
                            p_wf_log_type_cd    IN blt_ref_wf_log_type.ref_wf_log_type_cd%TYPE,
                            p_wf_log_sub_type_cd    IN blt_ref_wf_log_type.ref_wf_log_sub_type_cd%TYPE,
                            p_wf_log_rec_identifier  IN blt_wf_rec_log.wf_log_rec_identifier%TYPE,
                            p_wf_log_start_dt   IN blt_wf_rec_log.wf_log_start_dt%TYPE,
                            p_wf_log_end_dt     IN blt_wf_rec_log.wf_log_end_dt%TYPE,
                            p_wf_log_err_status_cd IN blt_wf_rec_log.wf_log_err_status_cd%TYPE,
                            p_wf_log_err_msg       IN blt_wf_rec_log.wf_log_err_msg%TYPE,
                            p_run_mode          IN NUMBER);

   -----------------------------------------------------------------------------------------
   -- Procedure:    reset_seqeunce
   -- Arguments:
   --
   -- Description:  Aligns staging sequence with Maintenix equivalent
   --
   -----------------------------------------------------------------------------------------
   PROCEDURE reset_sequence(p_seq_name    IN VARCHAR2,
                            p_tab_name    IN VARCHAR2,
                            p_col_name    IN VARCHAR2,
                            p_db_col_name IN VARCHAR2,
                            p_db_id       IN NUMBER);
END blt_wf_controller_pkg;
/

--changeSet MTX-154:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "BLT_WF_CONTROLLER_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : BLT_WF_CONTROLLER_PKG
   -- Object Type : Package Body
   -- Date        : Nov 25,2011
   -- Coder       : Vince Chan
   -- Recent Date : Nov 25,2011
   -- Recent Coder: Vince Chan
   -- Description :
   -- This package contains common methods for inserting into BLT log tables
   --
   --
   ----------------------------------------------------------------------------
   -- Copyright @2010-11 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------


    -----------------------------------------------------------------------------
   -- Local Package Constants
   -----------------------------------------------------------------------------
   c_pkg_name         CONSTANT VARCHAR2(30) := 'BLT_WF_CONTROLLER_PKG';

   /* Job running constants */
   c_con_task_type  VARCHAR2(8) := 'BLTCON';
   c_proc_task_type VARCHAR2(8) := 'BLTWF';

    /* Run mode */
   c_mode_set CONSTANT NUMBER := 1; -- Runs workflows in bulk
   c_mode_row CONSTANT NUMBER := 0; -- Runs workflows in parallel


   /*blt_ref_wf_log_status codes*/
   c_stged_log_status BLT_REF_WF_LOG_STATUS.REF_WF_LOG_STATUS_CD%TYPE := 'STGED';
   c_error_log_status BLT_REF_WF_LOG_STATUS.REF_WF_LOG_STATUS_CD%TYPE := 'ERROR';

   -----------------------------------------------------------------------------
   -- Local Package Variables
   -----------------------------------------------------------------------------
   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);


   -----------------------------------------------------------------------------------------
   -- Procedure:    update_blt_wf_cycle_log
   -- Arguments:    p_wf_cycle_log_id         (NUMBER) -- takes and returns log id
   --               p_wf_cycle_cd             workflow cycle code
   --               p_wf_cycle_status_cd      workflow cycle status
   --               p_wf_cycle_start_dt       workflow cycle start date
   --               p_wf_cycle_end_dt         workflow cycle end date
   --               p_wf_cycle_err_status_cd  workflow cycle error status code
   --               p_wf_cycle_err_msg        workflow cycle error message
   -- Description:  Log procedure inserts and updates records into the
   --               blt_wf_cycle_log table
   -----------------------------------------------------------------------------------------
  PROCEDURE update_blt_wf_cycle_log(p_wf_cycle_log_id  IN  VARCHAR2,
                         p_wf_cycle_cd              IN blt_wf_cycle_log.wf_cycle_cd%TYPE,
                         p_wf_cycle_status_cd       IN blt_wf_cycle_log.ref_wf_cycle_status_cd%TYPE,
                         p_wf_cycle_start_dt        IN blt_wf_cycle_log.wf_cycle_start_dt%TYPE,
                         p_wf_cycle_end_dt          IN blt_wf_cycle_log.wf_cycle_end_dt%TYPE,
                         p_wf_cycle_err_status_cd   IN blt_wf_cycle_log.wf_cycle_err_status_cd%TYPE,
                         p_wf_cycle_err_msg         IN blt_wf_cycle_log.wf_cycle_err_msg%TYPE)
  AS
    v_wf_cycle_log_id VARCHAR2(32):=p_wf_cycle_log_id;
  BEGIN
       insert_blt_wf_cycle_log(v_wf_cycle_log_id,p_wf_cycle_cd,p_wf_cycle_status_cd,p_wf_cycle_start_dt,p_wf_cycle_end_dt,p_wf_cycle_err_status_cd,p_wf_cycle_err_msg);
  END update_blt_wf_cycle_log;

   -----------------------------------------------------------------------------------------
   -- Procedure:    insert_blt_wf_cycle_log
   -- Arguments:    p_wf_cycle_log_id         (NUMBER) -- takes and returns log id
   --               p_wf_cycle_cd             workflow cycle code
   --               p_wf_cycle_status_cd      workflow cycle status
   --               p_wf_cycle_start_dt       workflow cycle start date
   --               p_wf_cycle_end_dt         workflow cycle end date
   --               p_wf_cycle_err_status_cd  workflow cycle error status code
   --               p_wf_cycle_err_msg        workflow cycle error message
   -- Description:  Log procedure inserts and updates records into the
   --               blt_wf_cycle_log table
   -----------------------------------------------------------------------------------------
   PROCEDURE insert_blt_wf_cycle_log(p_wf_cycle_log_id  IN OUT VARCHAR2,
                         p_wf_cycle_cd              IN blt_wf_cycle_log.wf_cycle_cd%TYPE,
                         p_wf_cycle_status_cd       IN blt_wf_cycle_log.ref_wf_cycle_status_cd%TYPE,
                         p_wf_cycle_start_dt        IN blt_wf_cycle_log.wf_cycle_start_dt%TYPE,
                         p_wf_cycle_end_dt          IN blt_wf_cycle_log.wf_cycle_end_dt%TYPE,
                         p_wf_cycle_err_status_cd   IN blt_wf_cycle_log.wf_cycle_err_status_cd%TYPE,
                         p_wf_cycle_err_msg         IN blt_wf_cycle_log.wf_cycle_err_msg%TYPE)
  AS

      v_method_name VARCHAR2(30) := 'insert_blt_wf_cycle_log';

      --convert varchar to raw data type
      v_wf_cycle_log_id blt_wf_cycle_log.wf_cycle_log_id%TYPE:=hextoraw(p_wf_cycle_log_id);
      PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
      --if p_blt_log_id is null, then new blt wf cycle log is to be inserted.
      v_step := 10;
      IF v_wf_cycle_log_id IS NULL
      THEN
         v_step := 20;
         SELECT mx_key_pkg.new_uuid
           INTO v_wf_cycle_log_id
           FROM dual;

         v_step := 40;
         INSERT INTO blt_wf_cycle_log
            (wf_cycle_log_id,
             wf_cycle_cd,
             ref_wf_cycle_status_cd,
             wf_cycle_start_dt,
             wf_cycle_end_dt,
             wf_cycle_err_status_cd,
             wf_cycle_err_msg)
         VALUES
            (v_wf_cycle_log_id,
             p_wf_cycle_cd,
             p_wf_cycle_status_cd,
             p_wf_cycle_start_dt,
             p_wf_cycle_end_dt,
             p_wf_cycle_err_status_cd,
             p_wf_cycle_err_msg );

      ELSE
         --update existing log
         v_step := 50;
         UPDATE blt_wf_cycle_log
            SET wf_cycle_cd              = CASE WHEN p_wf_cycle_cd IS NOT NULL THEN p_wf_cycle_cd ELSE wf_cycle_cd END,
                wf_cycle_start_dt        = CASE WHEN p_wf_cycle_start_dt IS NOT NULL THEN p_wf_cycle_start_dt ELSE wf_cycle_start_dt END,
                wf_cycle_end_dt          = CASE WHEN p_wf_cycle_end_dt IS NOT NULL THEN p_wf_cycle_end_dt ELSE wf_cycle_end_dt END,
                ref_wf_cycle_status_cd   = CASE WHEN p_wf_cycle_status_cd IS NOT NULL THEN p_wf_cycle_status_cd ELSE ref_wf_cycle_status_cd END,
                wf_cycle_err_status_cd   = CASE WHEN p_wf_cycle_err_status_cd IS NOT NULL THEN p_wf_cycle_err_status_cd ELSE wf_cycle_err_status_cd END,
                wf_cycle_err_msg         = CASE WHEN p_wf_cycle_err_msg IS NULL
                                              THEN wf_cycle_err_msg
                                              ELSE
                                                 CASE WHEN wf_cycle_err_msg IS NULL
                                                    THEN p_wf_cycle_err_msg
                                                    ELSE wf_cycle_err_msg || ', ' || p_wf_cycle_err_msg
                                                 END
                                           END
          WHERE wf_cycle_log_id = v_wf_cycle_log_id;
      END IF;

      --convert raw datatype to varchar
      p_wf_cycle_log_id:=rawtohex(v_wf_cycle_log_id);
      COMMIT;
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                              v_step || ', ERROR: ' || SQLERRM,
                              1,
                              2000);

         raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                 v_err_msg,
                                 TRUE);

   END insert_blt_wf_cycle_log;



   -----------------------------------------------------------------------------------------
   -- Procedure:    update_blt_wf_rec_log
   -- Arguments:    p_wf_rec_log_id          varchar takes and returns log id
   --               p_wf_cycle_log_id        cycle id
   --               p_wf_log_status_cd       workflow log status code
   --               p_wf_log_type_cd         workflow log type
   --               p_wf_log_rec_identifier  workflow record identifier
   --               p_wf_log_start_dt        workflow start date
   --               p_wf_log_end_dt          workflow end date
   --               p_wf_log_err_status_cd   workflow error status code
   --               p_wf_log_err_msg         workflow error message
   --               p_run_mode               run mode (set based/parallel row based)
   --
   -- Description:  Log procedure  updates records into the
   --               blt_wf_rec_logtable
   -----------------------------------------------------------------------------------------
  PROCEDURE update_blt_wf_rec_log(p_wf_rec_log_id   IN  VARCHAR2,
                            p_wf_cycle_log_id   IN VARCHAR2,
                            p_wf_log_status_cd  IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE,
                            p_wf_log_type_cd    IN blt_ref_wf_log_type.ref_wf_log_type_cd%TYPE,
                            p_wf_log_sub_type_cd    IN blt_ref_wf_log_type.ref_wf_log_sub_type_cd%TYPE,
                            p_wf_log_rec_identifier  IN blt_wf_rec_log.wf_log_rec_identifier%TYPE,
                            p_wf_log_start_dt   IN blt_wf_rec_log.wf_log_start_dt%TYPE,
                            p_wf_log_end_dt     IN blt_wf_rec_log.wf_log_end_dt%TYPE,
                            p_wf_log_err_status_cd IN blt_wf_rec_log.wf_log_err_status_cd%TYPE,
                            p_wf_log_err_msg       IN blt_wf_rec_log.wf_log_err_msg%TYPE,
                            p_run_mode          IN NUMBER)
  AS
    v_wf_rec_log_id VARCHAR2(4000):=p_wf_rec_log_id;
  BEGIN
  insert_blt_wf_rec_log(v_wf_rec_log_id,
                            p_wf_cycle_log_id,
                            p_wf_log_status_cd,
                            p_wf_log_type_cd,
                            p_wf_log_sub_type_cd,
                            p_wf_log_rec_identifier,
                            p_wf_log_start_dt,
                            p_wf_log_end_dt,
                            p_wf_log_err_status_cd,
                            p_wf_log_err_msg,
                            p_run_mode);
  END update_blt_wf_rec_log;


   -----------------------------------------------------------------------------------------
   -- Procedure:    log_staged_wf_rec_log
   -- Arguments:    p_wf_rec_log_id          varchar takes and returns log id
   --               p_wf_cycle_log_id        cycle id
   --               p_wf_log_type_cd         workflow log type
   --               p_run_mode               run mode (set based/parallel row based)
   --
   -- Description:  Updates status of staged records in blt_wf_rec_log to STGED/ERROR
   -----------------------------------------------------------------------------------------
  PROCEDURE log_staged_wf_rec_log(p_wf_rec_log_id   IN  VARCHAR2,
                          p_wf_cycle_log_id   IN VARCHAR2,
                          p_wf_log_type_cd    IN blt_ref_wf_log_type.ref_wf_log_type_cd%TYPE,
                          p_wf_log_sub_type_cd    IN blt_ref_wf_log_type.ref_wf_log_sub_type_cd%TYPE,
                          p_run_mode          IN NUMBER
          )
   AS
     --validation error cursor
     CURSOR lcur_EerrRecs IS
            --set mode errors
            SELECT bwel.wf_rec_log_id
            FROM
                 blt_wf_error_log bwel
                 JOIN blt_wf_rec_log bwrl ON
                      bwrl.wf_rec_log_id = bwel.wf_rec_log_id
            WHERE
                 rawtohex(bwrl.wf_cycle_log_id)   =    p_wf_cycle_log_id AND
                 bwrl.ref_wf_log_type_cd          =    p_wf_log_type_cd AND
                 bwrl.ref_wf_log_sub_type_cd      =    p_wf_log_sub_type_cd AND
                 p_run_mode = c_mode_set
            UNION
            --row mode errors
            SELECT bwel.wf_rec_log_id
            FROM
                 blt_wf_error_log bwel
                 JOIN blt_wf_rec_log bwrl ON
                       bwrl.wf_rec_log_id = bwel.wf_rec_log_id
            WHERE
                 rawtohex(bwrl.wf_rec_log_id) = p_wf_rec_log_id AND
                 p_run_mode = c_mode_row;

   BEGIN
        --set all processed records to STGED
        update_blt_wf_rec_log(p_wf_rec_log_id => p_wf_rec_log_id,
                          p_wf_cycle_log_id => p_wf_cycle_log_id,
                          p_wf_log_status_cd => c_stged_log_status,
                          p_wf_log_type_cd => p_wf_log_type_cd,
                          p_wf_log_sub_type_cd => p_wf_log_sub_type_cd,
                          p_wf_log_rec_identifier => NULL,
                          p_wf_log_start_dt => NULL,
                          p_wf_log_end_dt =>  SYSDATE,
                          p_wf_log_err_status_cd => NULL,
                          p_wf_log_err_msg => NULL,
                          p_run_mode => p_run_mode);

       --set blt_wf_rec_log.wf_log_status_cd to ERROR for failed records
       FOR error_rec IN lcur_EerrRecs  LOOP
          update_blt_wf_rec_log(p_wf_rec_log_id => rawtohex(error_rec.wf_rec_log_id),
                            p_wf_cycle_log_id => p_wf_cycle_log_id,
                            p_wf_log_status_cd => c_error_log_status,
                            p_wf_log_type_cd => p_wf_log_type_cd,
                            p_wf_log_sub_type_cd => p_wf_log_sub_type_cd,
                            p_wf_log_rec_identifier => NULL,
                            p_wf_log_start_dt => NULL,
                            p_wf_log_end_dt =>  SYSDATE,
                            p_wf_log_err_status_cd => 'VALIDATE',  --TODO: UPDATE
                            p_wf_log_err_msg => 'Validation errors occured.  See audit logs',
                            p_run_mode => c_mode_row);
       END LOOP;

   END log_staged_wf_rec_log;

   -----------------------------------------------------------------------------------------
   -- Procedure:    insert_blt_wf_rec_log
   -- Arguments:    p_wf_rec_log_id              (NUMBER) -- takes and returns log id
   --               p_wf_cycle_log_id        cycle id
   --               p_wf_log_status_cd       workflow log status code
   --               p_wf_log_type_cd         workflow log type
   --               p_wf_log_rec_identifier  workflow record identifier
   --               p_wf_log_start_dt        workflow start date
   --               p_wf_log_end_dt          workflow end date
   --               p_wf_log_err_status_cd   workflow error status code
   --               p_wf_log_err_msg         workflow error message
   --               p_run_mode               run mode (set based/parallel row based)
   --
   -- Description:  Log procedure inserts and updates records into the
   --               blt_wf_rec_logtable
   -----------------------------------------------------------------------------------------
   PROCEDURE insert_blt_wf_rec_log(p_wf_rec_log_id   IN OUT VARCHAR2,
                            p_wf_cycle_log_id   IN VARCHAR2,
                            p_wf_log_status_cd  IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE,
                            p_wf_log_type_cd    IN blt_ref_wf_log_type.ref_wf_log_type_cd%TYPE,
                            p_wf_log_sub_type_cd    IN blt_ref_wf_log_type.ref_wf_log_sub_type_cd%TYPE,
                            p_wf_log_rec_identifier  IN blt_wf_rec_log.wf_log_rec_identifier%TYPE,
                            p_wf_log_start_dt   IN blt_wf_rec_log.wf_log_start_dt%TYPE,
                            p_wf_log_end_dt     IN blt_wf_rec_log.wf_log_end_dt%TYPE,
                            p_wf_log_err_status_cd IN blt_wf_rec_log.wf_log_err_status_cd%TYPE,
                            p_wf_log_err_msg       IN blt_wf_rec_log.wf_log_err_msg%TYPE,
                            p_run_mode          IN NUMBER)
   AS

      v_method_name VARCHAR2(30) := 'insert_blt_wf_rec_log';
      v_wf_rec_log_id blt_wf_rec_log.wf_rec_log_id%TYPE:= hextoraw(p_wf_rec_log_id);
      v_wf_cycle_log_id  blt_wf_rec_log.wf_cycle_log_id%TYPE := hextoraw(p_wf_cycle_log_id);
      PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
      --if p_blt_wf_rec_log_id is null, then new blt log is to be inserted.
      v_step := 10;
      IF p_run_mode = c_mode_set
      THEN
         --Update status/start time of records to be processed as part of the set (same cycle/
         v_step := 15;
         UPDATE blt_wf_rec_log
            SET ref_wf_log_status_cd  = CASE WHEN p_wf_log_status_cd IS NOT NULL THEN p_wf_log_status_cd ELSE ref_wf_log_status_cd END,
                wf_log_start_dt       = CASE WHEN p_wf_log_start_dt IS NOT NULL THEN p_wf_log_start_dt ELSE wf_log_start_dt END,
                wf_log_end_dt         = CASE WHEN p_wf_log_end_dt IS NOT NULL THEN p_wf_log_end_dt ELSE wf_log_end_dt END,
                wf_log_err_status_cd  = CASE WHEN p_wf_log_err_status_cd IS NOT NULL THEN p_wf_log_err_status_cd ELSE wf_log_err_status_cd END,
                wf_log_err_msg        = CASE WHEN p_wf_log_err_msg IS NOT NULL THEN p_wf_log_err_msg ELSE wf_log_err_msg END
          WHERE wf_cycle_log_id = v_wf_cycle_log_id
            AND ref_wf_log_type_cd = p_wf_log_type_cd
            AND ref_wf_log_sub_type_cd = p_wf_log_sub_type_cd;

      ELSIF v_wf_rec_log_id IS NULL
      THEN
         v_step := 20;
         SELECT mx_key_pkg.new_uuid
           INTO v_wf_rec_log_id
           FROM dual;

         v_step := 40;
         INSERT INTO blt_wf_rec_log
            (wf_rec_log_id,
             wf_cycle_log_id,
             ref_wf_log_status_cd,
             ref_wf_log_type_cd,
             wf_log_rec_identifier,
             wf_log_start_dt,
             wf_log_end_dt,
             wf_log_err_status_cd,
             wf_log_err_msg)
         VALUES
            (v_wf_rec_log_id,
             v_wf_cycle_log_id,
             p_wf_log_status_cd,
             p_wf_log_type_cd,
             p_wf_log_rec_identifier,
             p_wf_log_start_dt,
             p_wf_log_end_dt,
             p_wf_log_err_status_cd,
             p_wf_log_err_msg);

      ELSE
         -- when there is no the specific log (with p_blt_wf_rec_log_id).
         v_step := 50;
         UPDATE blt_wf_rec_log
            SET ref_wf_log_status_cd  = CASE WHEN p_wf_log_status_cd IS NOT NULL THEN p_wf_log_status_cd ELSE ref_wf_log_status_cd END,
                wf_log_start_dt       = CASE WHEN p_wf_log_start_dt IS NOT NULL THEN p_wf_log_start_dt ELSE wf_log_start_dt END,
                wf_log_end_dt         = CASE WHEN p_wf_log_end_dt IS NOT NULL THEN p_wf_log_end_dt ELSE wf_log_end_dt END,
                wf_log_err_status_cd  = CASE WHEN p_wf_log_err_status_cd IS NOT NULL THEN p_wf_log_err_status_cd ELSE wf_log_err_status_cd END,
                wf_log_err_msg        = CASE WHEN p_wf_log_err_msg IS NOT NULL THEN p_wf_log_err_msg ELSE wf_log_err_msg END
          WHERE wf_rec_log_id = v_wf_rec_log_id;
      END IF;


      p_wf_rec_log_id := rawtohex(v_wf_rec_log_id);
      COMMIT;
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                              v_step || ', ERROR: ' || SQLERRM,
                              1,
                              2000);

         raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                 v_err_msg,
                                 TRUE);

   END insert_blt_wf_rec_log;


   -----------------------------------------------------------------------------------------
   -- Procedure:    check_jobs_running
   -- Arguments:    p_wf_rec_log_id        -- Current worflow being run
   --               p_wf_id            -- Master workflow
   --               p_db_id            -- DB ID being used for run
   --               p_serial_no_oem    -- Root Serial Number
   --               p_part_no_oem      -- Root Part Number
   --               p_manufact_cd      -- Root Manufacturing code
   --               p_validation_bool  -- Whether validation should be run 0 or 1
   --
   -- Description:  Runs the batch mapping and subsequent validation if requested.
   --
   -----------------------------------------------------------------------------------------
   FUNCTION check_jobs_running(p_job_no   IN NUMBER,
                               p_max_proc IN NUMBER,
                               p_log_id   IN NUMBER,
                               p_wf_type  IN VARCHAR2) RETURN NUMBER IS

      v_job_no            NUMBER := p_job_no;
      v_max_proc          NUMBER := p_max_proc;
      v_log_id            NUMBER := p_log_id;
      v_running_job_count NUMBER(10);
      v_method_name       VARCHAR2(30) := 'check_jobs_running';

   BEGIN

      v_step := 10;
      IF v_job_no = v_max_proc
      THEN
         -- Wait for processes to finish before starting new ones
         LOOP
            SELECT COUNT(*)
              INTO v_running_job_count
              FROM user_scheduler_jobs usj
             WHERE usj.job_name LIKE
                   '%' || c_proc_task_type || '_' || '%' || '_' || p_wf_type || '%';
            IF v_running_job_count < v_max_proc
            THEN
               v_step   := 130;
               v_job_no := v_running_job_count;
               RETURN v_job_no;
            ELSE
               v_step := 140;
               dbms_lock.sleep(5);
            END IF;
         END LOOP;
      END IF;

      RETURN v_job_no;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                              v_step || ', ERROR: ' || SQLERRM,
                              1,
                              2000);

         raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                 v_err_msg,
                                 TRUE);
   END check_jobs_running;

   -----------------------------------------------------------------------------------------
   -- Procedure:    clean_blt_wf_cycle_log
   -- Arguments:
   --
   -- Description:  Deletes all data from the blt_wf_cycle_log table
   --
   -----------------------------------------------------------------------------------------
   PROCEDURE clean_blt_wf_rec_log IS

      v_method_name VARCHAR2(30) := 'clean_blt_wf_rec_log';

   BEGIN

      -- clean out all processing tables
      EXECUTE IMMEDIATE 'DELETE FROM blt_wf_rec_log';

      COMMIT;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                              v_step || ', ERROR: ' || SQLERRM,
                              1,
                              2000);

         raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                 v_err_msg,
                                 TRUE);

   END clean_blt_wf_rec_log;



   -----------------------------------------------------------------------------------------
   -- Procedure:    clean_blt_wf_cycle_log
   -- Arguments:
   --
   -- Description:  Deletes all data from the blt_wf_cycle_log table
   --
   -----------------------------------------------------------------------------------------
   PROCEDURE clean_blt_wf_cycle_log IS

      v_method_name VARCHAR2(30) := 'clean_blt_wf_cycle_log';

   BEGIN

      -- clean out all processing tables
      EXECUTE IMMEDIATE 'DELETE FROM blt_wf_cycle_log';

      COMMIT;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                              v_step || ', ERROR: ' || SQLERRM,
                              1,
                              2000);

         raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                 v_err_msg,
                                 TRUE);

   END clean_blt_wf_cycle_log;


   -----------------------------------------------------------------------------------------
   -- Function:     is_ref_wf_log_status_valid
   -- Arguments:    p_wf_log_status_cd   ref_wf_log_status_cd to validate
   --
   -- Description:  checks if wf_log_status_cd is valid
   --
   -----------------------------------------------------------------------------------------
   FUNCTION is_wf_log_status_valid(p_wf_log_status_cd IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE
     )RETURN NUMBER IS
     v_method_name VARCHAR2(30) := 'is_wf_log_status_valid';
     ln_RecCnt NUMBER:=0;
   BEGIN

        SELECT COUNT(*)
        INTO
              ln_RecCnt
        FROM blt_ref_wf_log_status
        WHERE blt_ref_wf_log_status.ref_wf_log_status_cd = p_wf_log_status_cd;

        RETURN ln_RecCnt;

      EXCEPTION
         WHEN OTHERS THEN
           v_err_code := SQLCODE;
           v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                                v_step || ', ERROR: ' || SQLERRM,
                                1,
                                2000);

          raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                 v_err_msg,
                                 TRUE);
   END is_wf_log_status_valid;

   -----------------------------------------------------------------------------------------
   -- Function:     is_wf_cycle_status_valid
   -- Arguments:    p_wf_cycle_status_cd   ref_wf_log_status_cd to validate
   --
   -- Description:  checks if ref_wf_cycle_status_cd is valid
   --
   -----------------------------------------------------------------------------------------
   FUNCTION is_wf_cycle_status_valid(p_wf_cycle_status_cd IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE
      )RETURN NUMBER IS
      v_method_name VARCHAR2(30) := 'is_wf_cycle_status_valid';
      ln_RecCnt NUMBER:=0;
   BEGIN

        SELECT COUNT(*)
        INTO
              ln_RecCnt
        FROM blt_ref_wf_cycle_status
        WHERE blt_ref_wf_cycle_status.ref_wf_cycle_status_cd = p_wf_cycle_status_cd;

        RETURN ln_RecCnt;
        EXCEPTION
           WHEN OTHERS THEN
             v_err_code := SQLCODE;
             v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                                  v_step || ', ERROR: ' || SQLERRM,
                                  1,
                                  2000);

            raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                   v_err_msg,
                                   TRUE);
   END is_wf_cycle_status_valid;


 -----------------------------------------------------------------------------------------
   -- Procedure:    check_jobs_running
   -- Arguments:    p_wf_log_id        -- Current worflow being run
   --               p_wf_id            -- Master workflow
   --               p_db_id            -- DB ID being used for run
   --               p_serial_no_oem    -- Root Serial Number
   --               p_part_no_oem      -- Root Part Number
   --               p_manufact_cd      -- Root Manufacturing code
   --               p_validation_bool  -- Whether validation should be run 0 or 1
   --
   -- Description:  Runs the batch mapping and subsequent validation if requested.
   --
   -----------------------------------------------------------------------------------------
   FUNCTION check_jobs_running(p_job_no   IN NUMBER,
                               p_max_proc IN NUMBER,
                               p_wf_type  IN blt_ref_wf_log_type.ref_wf_log_sub_type_cd%TYPE) RETURN NUMBER IS

      v_job_no            NUMBER := p_job_no;
      v_max_proc          NUMBER := p_max_proc;
      v_running_job_count NUMBER(10);
      v_method_name       VARCHAR2(30) := 'check_jobs_running';

   BEGIN

      v_step := 10;
      IF v_job_no = v_max_proc
      THEN
         -- Wait for processes to finish before starting new ones
         LOOP
            SELECT COUNT(*)
              INTO v_running_job_count
              FROM user_scheduler_jobs usj
             WHERE usj.job_name LIKE
                   '%' || c_proc_task_type || '_' || '%' || '_' || p_wf_type || '%';
            IF v_running_job_count < v_max_proc
            THEN
               v_step   := 130;
               v_job_no := v_running_job_count;
               RETURN v_job_no;
            ELSE
               v_step := 140;
               dbms_lock.sleep(5);
            END IF;
         END LOOP;
      END IF;

      RETURN v_job_no;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                              v_step || ', ERROR: ' || SQLERRM,
                              1,
                              2000);

         raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                 v_err_msg,
                                 TRUE);
   END check_jobs_running;



   -----------------------------------------------------------------------------------------
   -- Procedure:    reset_seqeunce
   -- Arguments:
   --
   -- Description:  Aligns staging sequence with Maintenix equivalent
   --
   -----------------------------------------------------------------------------------------
   PROCEDURE reset_sequence(p_seq_name    IN VARCHAR2,
                            p_tab_name    IN VARCHAR2,
                            p_col_name    IN VARCHAR2,
                            p_db_col_name IN VARCHAR2,
                            p_db_id       IN NUMBER) IS

      v_method_name VARCHAR2(30) := 'reset_sequence';
      v_val         NUMBER;
      v_max_val     NUMBER;
      v_new_val     NUMBER;

   BEGIN

      v_step := 10;
      EXECUTE IMMEDIATE 'select ' || p_seq_name || '.nextval from dual'
         INTO v_val;

      v_step := 20;
      EXECUTE IMMEDIATE 'select NVL(max(' || p_col_name || '),0) FROM ' ||
                        p_tab_name || ' WHERE ' || p_db_col_name || ' = ' ||
                        p_db_id
         INTO v_max_val;

      v_new_val := v_max_val - v_val;
      
      --If difference is 0 or less than 0, then no sequence synchronization is needed
      IF(v_new_val > 0) THEN      

        v_step := 30;
        EXECUTE IMMEDIATE 'alter sequence ' || p_seq_name || ' increment by ' ||
                          v_new_val || ' minvalue 1';
  
        v_step := 40;
        EXECUTE IMMEDIATE 'select ' || p_seq_name || '.nextval from dual'
           INTO v_val;
  
        v_step := 50;
        EXECUTE IMMEDIATE 'alter sequence ' || p_seq_name ||
                          ' increment by 1 minvalue 1';

      END IF;
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := substr(c_pkg_name || '.' || v_method_name || ', STEP: ' ||
                              v_step || ', ERROR: ' || SQLERRM,
                              1,
                              2000);

         raise_application_error(blt_utl_err_const_pkg.gc_ex_wf_controller,
                                 v_err_msg,
                                 TRUE);

   END reset_sequence;
END blt_wf_controller_pkg;
/

--changeSet MTX-154:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************** CREATE PERMANENT BLOB TABLE *****************************************************************************/
BEGIN
  utl_migr_schema_pkg.table_create('
    Create table COR_PERM_BLOB (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE ,
      BLOB_FILE_NAME Varchar2 (255),
      BLOB_CONTENT_TYPE Varchar2 (80),
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE ,
     Constraint PK_COR_PERM_BLOB primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/

--changeSet MTX-154:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_PERM_BLOB add Constraint FK_MIMDB_CORPERMBLOB foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_PERM_BLOB add Constraint FK_MIMRSTAT_CORPERMBLOB foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************** CREATE BLOB SEGREGATION TABLES *****************************************************************************/
BEGIN
  utl_migr_schema_pkg.table_create('
    Create table COR_BLOB_INFO (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_FILE_NAME Varchar2 (255),
      BLOB_CONTENT_TYPE Varchar2 (80),
      ALT_ID Raw(16) NOT NULL  UNIQUE,
      BLOB_LOC Varchar2 (255),   
      BLOB_TYPE Varchar2 (80) NOT NULL DEFERRABLE,
      SEGREGATED_BOOL Number(1,0) Default 0 NOT NULL DEFERRABLE  Check (SEGREGATED_BOOL IN (0, 1) ) DEFERRABLE ,  
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE ,
     Constraint PK_COR_BLOB_INFO primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/  

--changeSet MTX-154:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_INFO add Constraint FK_MIMDB_CORBLOBINFO foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_INFO add Constraint FK_MIMRSTAT_CORBLOBINFO foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:9 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_JAN (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_JAN primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/   

--changeSet MTX-154:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_JAN add Constraint FK_MIMDB_CORBLOBJAN foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_JAN add Constraint FK_MIMRSTAT_CORBLOBJAN foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_FEB (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_FEB primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/    

--changeSet MTX-154:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_FEB add Constraint FK_MIMDB_CORBLOBFEB foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_FEB add Constraint FK_MIMRSTAT_CORBLOBFEB foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_MAR (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_MAR primary key (BLOB_DB_ID,BLOB_ID) 
     )'
  );
END;
/    

--changeSet MTX-154:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_MAR add Constraint FK_MIMDB_CORBLOBMAR foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_MAR add Constraint FK_MIMRSTAT_CORBLOBMAR foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_APR (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_APR primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/     

--changeSet MTX-154:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_APR add Constraint FK_MIMDB_CORBLOBAPR foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:20 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_APR add Constraint FK_MIMRSTAT_CORBLOBAPR foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/ 

--changeSet MTX-154:21 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_MAY (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_MAY primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/    

--changeSet MTX-154:22 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_MAY add Constraint FK_MIMDB_CORBLOBMAY foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:23 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_MAY add Constraint FK_MIMRSTAT_CORBLOBMAY foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/ 

--changeSet MTX-154:24 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_JUN (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_JUN primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/    

--changeSet MTX-154:25 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_JUN add Constraint FK_MIMDB_CORBLOBJUN foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:26 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_JUN add Constraint FK_MIMRSTAT_CORBLOBJUN foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/  

--changeSet MTX-154:27 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_JUL (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_JUL primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/    

--changeSet MTX-154:28 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_JUL add Constraint FK_MIMDB_CORBLOBJUL foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:29 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_JUL add Constraint FK_MIMRSTAT_CORBLOBJUL foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/  

--changeSet MTX-154:30 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_AUG (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_AUG primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/ 

--changeSet MTX-154:31 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_AUG add Constraint FK_MIMDB_CORBLOBAUG foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:32 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_AUG add Constraint FK_MIMRSTAT_CORBLOBAUG foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:33 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_SEP (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_SEP primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/ 

--changeSet MTX-154:34 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_SEP add Constraint FK_MIMDB_CORBLOBSEP foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:35 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_SEP add Constraint FK_MIMRSTAT_CORBLOBSEP foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:36 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_OCT (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_OCT primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/ 

--changeSet MTX-154:37 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_OCT add Constraint FK_MIMDB_CORBLOBOCT foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:38 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_OCT add Constraint FK_MIMRSTAT_CORBLOBOCT foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:39 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_NOV (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_NOV primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/ 

--changeSet MTX-154:40 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_NOV add Constraint FK_MIMDB_CORBLOBNOV foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:41 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_NOV add Constraint FK_MIMRSTAT_CORBLOBNOV foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:42 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    create table COR_BLOB_DEC (
      BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_ID Number(10,0) NOT NULL DEFERRABLE  Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      BLOB_DATA Blob NOT NULL DEFERRABLE , 
      RSTAT_CD Number(3,0) NOT NULL DEFERRABLE  Check (RSTAT_CD IN (0, 1, 2, 3) ) DEFERRABLE ,
      ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE,
      CREATION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DT Date NOT NULL DEFERRABLE ,
      REVISION_DB_ID Number(10,0) NOT NULL DEFERRABLE  Check (REVISION_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE ,
      REVISION_USER Varchar2 (30) NOT NULL DEFERRABLE,
     Constraint PK_COR_BLOB_DEC primary key (BLOB_DB_ID,BLOB_ID) 
    )'
  );
END;
/ 

--changeSet MTX-154:43 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_DEC add Constraint FK_MIMDB_CORBLOBDEC foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:44 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_DEC add Constraint FK_MIMRSTAT_CORBLOBDEC foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:45 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************** ADD AUDIT TRIGGERS TO PERMANENT BLOB TABLE *****************************************************************************/
CREATE OR REPLACE TRIGGER "TIBR_COR_PERM_BLOB" BEFORE INSERT
   ON "COR_PERM_BLOB" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:46 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_PERM_BLOB" BEFORE UPDATE
   ON "COR_PERM_BLOB" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:47 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************** ADD AUDIT TRIGGERS TO BLOB SEGREGATION TABLES *****************************************************************************/
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_INFO_ALT_ID" BEFORE INSERT
   ON "COR_BLOB_INFO" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
BEGIN
  IF :NEW.alt_id IS NULL THEN
     :NEW.alt_id := mx_key_pkg.new_uuid();
  END IF;
END;
/

--changeSet MTX-154:48 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_INFO" BEFORE INSERT
   ON "COR_BLOB_INFO" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:49 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_INFO" BEFORE UPDATE
   ON "COR_BLOB_INFO" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:50 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_JAN" BEFORE INSERT
   ON "COR_BLOB_JAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:51 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_JAN" BEFORE UPDATE
   ON "COR_BLOB_JAN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:52 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_FEB" BEFORE INSERT
   ON "COR_BLOB_FEB" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:53 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_FEB" BEFORE UPDATE
   ON "COR_BLOB_FEB" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:54 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_MAR" BEFORE INSERT
   ON "COR_BLOB_MAR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:55 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_MAR" BEFORE UPDATE
   ON "COR_BLOB_MAR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:56 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_APR" BEFORE INSERT
   ON "COR_BLOB_APR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:57 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_APR" BEFORE UPDATE
   ON "COR_BLOB_APR" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:58 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_MAY" BEFORE INSERT
   ON "COR_BLOB_MAY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:59 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_MAY" BEFORE UPDATE
   ON "COR_BLOB_MAY" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:60 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_JUN" BEFORE INSERT
   ON "COR_BLOB_JUN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:61 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_JUN" BEFORE UPDATE
   ON "COR_BLOB_JUN" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:62 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_JUL" BEFORE INSERT
   ON "COR_BLOB_JUL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:63 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_JUL" BEFORE UPDATE
   ON "COR_BLOB_JUL" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:64 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_AUG" BEFORE INSERT
   ON "COR_BLOB_AUG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:65 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_AUG" BEFORE UPDATE
   ON "COR_BLOB_AUG" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:66 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_SEP" BEFORE INSERT
   ON "COR_BLOB_SEP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:67 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_SEP" BEFORE UPDATE
   ON "COR_BLOB_SEP" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:68 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_OCT" BEFORE INSERT
   ON "COR_BLOB_OCT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:69 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_OCT" BEFORE UPDATE
   ON "COR_BLOB_OCT" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:70 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_NOV" BEFORE INSERT
   ON "COR_BLOB_NOV" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:71 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_NOV" BEFORE UPDATE
   ON "COR_BLOB_NOV" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:72 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_DEC" BEFORE INSERT
   ON "COR_BLOB_DEC" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:73 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_DEC" BEFORE UPDATE
   ON "COR_BLOB_DEC" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:74 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************** Copy logo blob records from BLOB_DATA to COR_PERM_BLOB *****************************************************************************/
BEGIN
   utl_migr_schema_pkg.sequence_create('PERM_BLOB_ID_SEQ', 1);
END;
/

--changeSet MTX-154:75 stripComments:false
INSERT INTO 
   utl_sequence
   ( sequence_cd,
     next_value, 
     table_name, 
     column_name, 
     oracle_seq, 
     utl_id )
VALUES ( 'PERM_BLOB_ID_SEQ', 
         100000, 
         'COR_PERM_BLOB', 
         'BLOB_ID', 
         1, 
         0);

--changeSet MTX-154:76 stripComments:false
INSERT INTO cor_perm_blob (blob_db_id, blob_id, blob_data, blob_file_name, blob_content_type, creation_dt)
SELECT 
   blob_db_id, blob_id, blob_data, blob_file_name, blob_content_type, creation_dt
FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (SELECT blob_db_id, blob_id FROM org_logo );   

--changeSet MTX-154:77 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ORG_LOGO','FK_ORGLOGO_BLOBDATA');        
END;
/ 

--changeSet MTX-154:78 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_constraint_add('
    Alter table ORG_LOGO add Constraint FK_CORPERMBLOB_ORGLOGO foreign key (BLOB_DB_ID,BLOB_ID) references COR_PERM_BLOB (BLOB_DB_ID,BLOB_ID)  DEFERRABLE
  ');
END;
/

--changeSet MTX-154:79 stripComments:false
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (SELECT blob_db_id, blob_id FROM org_logo ); 

--changeSet MTX-154:80 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Aligns staging sequence for BLOB_ID with COR_PERM_BLOB.BLOB_ID max number
DECLARE
      ln_db_id NUMBER(10);
      
BEGIN
SELECT 
      db_id
  INTO
      ln_db_id
  FROM
      mim_local_db
 WHERE
      rownum = 1;
      

BLT_WF_CONTROLLER_PKG.reset_sequence(p_seq_name => 'perm_blob_id_seq',
                                     p_tab_name => 'COR_PERM_BLOB',
                                     p_col_name => 'BLOB_ID',
                                     p_db_col_name => 'BLOB_DB_ID',
                                     p_db_id => ln_db_id );
END;
/

--changeSet MTX-154:81 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Aligns staging sequence for BLOB_ID with BLOB_DATA.BLOB_ID max number
DECLARE
      ln_db_id NUMBER(10);
      
BEGIN
SELECT 
      db_id
  INTO
      ln_db_id
  FROM
      mim_local_db
 WHERE
      rownum = 1;
 
 BLT_WF_CONTROLLER_PKG.reset_sequence(p_seq_name => 'blob_id_seq',
                                      p_tab_name => 'BLOB_DATA',
                                      p_col_name => 'BLOB_ID',
                                      p_db_col_name => 'BLOB_DB_ID',
                                      p_db_id => ln_db_id );
 END;
/ 

--changeSet MTX-154:82 stripComments:false
-- create cor_blob_info record for esig_doc blobs
/***************************************************************** Add COR_BLOB_INFO records for existing rows in BLOB_DATA *****************************************************************************/
INSERT INTO cor_blob_info(blob_db_id,blob_id,blob_file_name,blob_content_type,blob_loc,blob_type,segregated_bool)
SELECT 
   blob_data.blob_db_id,
   blob_data.blob_id,
   blob_data.blob_file_name,
   blob_data.blob_content_type,
   'COR_BLOB_DATA',
   'ESIG_DOC',
   0    
FROM 
    esig_doc
    JOIN blob_data ON
         blob_data.blob_db_id = esig_doc.blob_db_id AND
         blob_data.blob_id    = esig_doc.blob_id;

--changeSet MTX-154:83 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ESIG_DOC','FK_BLOBDATA_ESIGDOC');        
END;
/  

--changeSet MTX-154:84 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('
          Alter table "ESIG_DOC" add Constraint "FK_CORBLOBINFO_ESIGDOC" foreign key ("BLOB_DB_ID","BLOB_ID") references "COR_BLOB_INFO" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
      ');
END;
/

--changeSet MTX-154:85 stripComments:false
-- create cor_blob_info record for esig_doc_sign blobs
INSERT INTO cor_blob_info(blob_db_id,blob_id,blob_file_name,blob_content_type,blob_loc,blob_type,segregated_bool)
SELECT 
   blob_data.blob_db_id,
   blob_data.blob_id,
   blob_data.blob_file_name,
   blob_data.blob_content_type,
   'COR_BLOB_DATA',
   'ESIG_DOC_SIGN',
   0   
FROM 
    esig_doc_sign
    JOIN blob_data ON
         blob_data.blob_db_id = esig_doc_sign.blob_db_id AND
         blob_data.blob_id    = esig_doc_sign.blob_id;

--changeSet MTX-154:86 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ESIG_DOC_SIGN','FK_BLOB_DATA_ESIG_DOC_SIGN');        
END;
/   

--changeSet MTX-154:87 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('
        Alter table "ESIG_DOC_SIGN" add Constraint "FK_CORBLOBINFO_ESIGDOCSIGN" foreign key ("BLOB_DB_ID","BLOB_ID") references "COR_BLOB_INFO" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
      ');
END;
/

--changeSet MTX-154:88 stripComments:false
-- create cor_blob_info record for warranty_attach blobs
INSERT INTO cor_blob_info(blob_db_id,blob_id,blob_file_name,blob_content_type,blob_loc,blob_type,segregated_bool)
SELECT 
   blob_data.blob_db_id,
   blob_data.blob_id,
   blob_data.blob_file_name,
   blob_data.blob_content_type,
   'COR_BLOB_DATA',
   'WARRANTY_ATTACH',
   0    
FROM 
    warranty_attach
    JOIN blob_data ON
         blob_data.blob_db_id = warranty_attach.blob_db_id AND
         blob_data.blob_id    = warranty_attach.blob_id; 

--changeSet MTX-154:89 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('WARRANTY_ATTACH','FK_BLOBDATA_WARRANTYATTACH');        
END;
/  

--changeSet MTX-154:90 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('
          Alter table "WARRANTY_ATTACH" add Constraint "FK_CORBLOBINFO_WRRNTYATTACH" foreign key ("BLOB_DB_ID","BLOB_ID") references "COR_BLOB_INFO" ("BLOB_DB_ID","BLOB_ID")  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:91 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Add new columns to ASB_EXCEPTION_LOG
--
/***************************************************************** Update schema for ASB connector and Populate ASB tables with BLOB_DATA records ***********************************************/
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_EXCEPTION_LOG add BODY_BLOB blob
');
END;
/

--changeSet MTX-154:92 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_EXCEPTION_LOG add BINARY_BLOB blob
');
END;
/

--changeSet MTX-154:93 stripComments:false
-- create body blob data for asb_exception_log blobs
UPDATE asb_exception_log
SET body_blob = (
   SELECT 
      blob_data.blob_data
   FROM
      blob_data 
   WHERE 
      blob_data.blob_db_id = asb_exception_log.body_blob_db_id AND
      blob_data.blob_id    = asb_exception_log.body_blob_id
     )
;

--changeSet MTX-154:94 stripComments:false
-- create binary blob data for asb_exception_log blobs
UPDATE asb_exception_log
SET binary_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE
      blob_data.blob_db_id = asb_exception_log.binary_blob_db_id AND
      blob_data.blob_id    = asb_exception_log.binary_blob_id
            )
;   

--changeSet MTX-154:95 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Drop foreign keys for ASB_EXCEPTION_LOG
--
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_EXCEPTION_LOG','FK_ASBEXCEPTION_BLOBDATA_BODY');        
END;
/  

--changeSet MTX-154:96 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_EXCEPTION_LOG','FK_ASBEXCEPTION_BLOBDATA_BIN');        
END;
/

--changeSet MTX-154:97 stripComments:false
-- delete blob data that has been moved to asb_exception_log from table blob_data
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       body_blob_db_id,
       body_blob_id
    FROM 
       asb_exception_log
   )
;

--changeSet MTX-154:98 stripComments:false
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       binary_blob_db_id,
       binary_blob_id
    FROM 
       asb_exception_log
   )
;  

--changeSet MTX-154:99 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_EXCEPTION_LOG', 'BODY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:100 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_EXCEPTION_LOG', 'BODY_BLOB_ID');
END;
/

--changeSet MTX-154:101 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_EXCEPTION_LOG', 'BINARY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:102 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_EXCEPTION_LOG', 'BINARY_BLOB_ID');
END;
/

--changeSet MTX-154:103 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Add new columns to ASB_INBOUND_LOG
--
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_INBOUND_LOG add BODY_BLOB blob
');
END;
/

--changeSet MTX-154:104 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_INBOUND_LOG add BINARY_BLOB blob
');
END;
/

--changeSet MTX-154:105 stripComments:false
-- create body blob data for asb_inbound_log blobs
UPDATE asb_inbound_log
SET body_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE 
      blob_data.blob_db_id = asb_inbound_log.body_blob_db_id AND
      blob_data.blob_id    = asb_inbound_log.body_blob_id
            )
;

--changeSet MTX-154:106 stripComments:false
-- create binary blob data for asb_inbound_log blobs
UPDATE asb_inbound_log
SET binary_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE 
      blob_data.blob_db_id = asb_inbound_log.binary_blob_db_id AND
      blob_data.blob_id    = asb_inbound_log.binary_blob_id
            )
;

--changeSet MTX-154:107 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Drop foreign keys for ASB_INBOUND_LOG
--
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_INBOUND_LOG','FK_ASBINBOUND_BLOBDATA_BODY');        
END;
/  

--changeSet MTX-154:108 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_INBOUND_LOG','FK_ASBINBOUND_BLOBDATA_BIN');        
END;
/  

--changeSet MTX-154:109 stripComments:false
-- delete blob data that has been moved to asb_inbound_log from table blob_data
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       body_blob_db_id,
       body_blob_id
    FROM 
       asb_inbound_log
   )
;

--changeSet MTX-154:110 stripComments:false
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       binary_blob_db_id,
       binary_blob_id
    FROM 
       asb_inbound_log
   )
;

--changeSet MTX-154:111 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_INBOUND_LOG', 'BODY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:112 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_INBOUND_LOG', 'BODY_BLOB_ID');
END;
/

--changeSet MTX-154:113 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_INBOUND_LOG', 'BINARY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:114 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_INBOUND_LOG', 'BINARY_BLOB_ID');
END;
/

--changeSet MTX-154:115 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Add new columns to ASB_OUTBOUND_LOG
--
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_OUTBOUND_LOG add BODY_BLOB blob
');
END;
/

--changeSet MTX-154:116 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_OUTBOUND_LOG add BINARY_BLOB blob
');
END;
/

--changeSet MTX-154:117 stripComments:false
-- create body blob data for asb_outbound_log blobs
UPDATE asb_outbound_log
SET body_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE
      blob_data.blob_db_id = asb_outbound_log.body_blob_db_id AND
      blob_data.blob_id    = asb_outbound_log.body_blob_id
            )
;

--changeSet MTX-154:118 stripComments:false
-- create binary blob data for asb_outbound_log blobs
UPDATE asb_outbound_log
SET binary_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE
      blob_data.blob_db_id = asb_outbound_log.binary_blob_db_id AND
      blob_data.blob_id    = asb_outbound_log.binary_blob_id
            )
;

--changeSet MTX-154:119 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Drop foreign keys for ASB_OUTBOUND_LOG
--
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_OUTBOUND_LOG','FK_ASBOUTBOUND_BLOBDATA_BODY');        
END;
/  

--changeSet MTX-154:120 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_OUTBOUND_LOG','FK_ASBOUTBOUND_BLOBDATA_BIN');        
END;
/

--changeSet MTX-154:121 stripComments:false
-- delete blob data that has been moved to asb_outbound_log from table blob_data
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       body_blob_db_id,
       body_blob_id
    FROM 
       asb_outbound_log
   )
;

--changeSet MTX-154:122 stripComments:false
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       binary_blob_db_id,
       binary_blob_id
    FROM 
       asb_outbound_log
   )
;  

--changeSet MTX-154:123 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_OUTBOUND_LOG', 'BODY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:124 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_OUTBOUND_LOG', 'BODY_BLOB_ID');
END;
/

--changeSet MTX-154:125 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_OUTBOUND_LOG', 'BINARY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:126 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_OUTBOUND_LOG', 'BINARY_BLOB_ID');
END;
/

--changeSet MTX-154:127 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Add new columns to ASB_NOTIFICATION_LOG
--
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_NOTIFICATION_LOG add BODY_BLOB blob
');
END;
/

--changeSet MTX-154:128 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_NOTIFICATION_LOG add BINARY_BLOB blob
');
END;
/

--changeSet MTX-154:129 stripComments:false
-- create body blob data for asb_notification_log blobs
UPDATE asb_notification_log
SET body_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE
      blob_data.blob_db_id = asb_notification_log.body_blob_db_id AND
      blob_data.blob_id    = asb_notification_log.body_blob_id
            )
;

--changeSet MTX-154:130 stripComments:false
-- create binary blob data for asb_notification_log blobs
UPDATE asb_notification_log
SET binary_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE 
      blob_data.blob_db_id = asb_notification_log.binary_blob_db_id AND
      blob_data.blob_id    = asb_notification_log.binary_blob_id
            )
;

--changeSet MTX-154:131 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Drop foreign keys for ASB_NOTIFICATION_LOG
--
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_NOTIFICATION_LOG','FK_ASBNOTIF_BLOBDATA_BODY');        
END;
/  

--changeSet MTX-154:132 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_NOTIFICATION_LOG','FK_ASBNOTIF_BLOBDATA_BIN');        
END;
/

--changeSet MTX-154:133 stripComments:false
-- delete blob data that has been moved to asb_notification_log from table blob_data
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       body_blob_db_id,
       body_blob_id
    FROM 
       asb_notification_log
   )
;

--changeSet MTX-154:134 stripComments:false
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       binary_blob_db_id,
       binary_blob_id
    FROM 
       asb_notification_log
   )
;  

--changeSet MTX-154:135 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_NOTIFICATION_LOG', 'BODY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:136 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_NOTIFICATION_LOG', 'BODY_BLOB_ID');
END;
/

--changeSet MTX-154:137 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_NOTIFICATION_LOG', 'BINARY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:138 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_NOTIFICATION_LOG', 'BINARY_BLOB_ID');
END;
/

--changeSet MTX-154:139 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Add new columns to ASB_REQUEST_LOG
--
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_REQUEST_LOG add BODY_BLOB blob
');
END;
/

--changeSet MTX-154:140 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_REQUEST_LOG add BINARY_BLOB blob
');
END;
/

--changeSet MTX-154:141 stripComments:false
-- create body blob data for asb_request_log blobs
UPDATE asb_request_log
SET body_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE 
      blob_data.blob_db_id = asb_request_log.body_blob_db_id AND
      blob_data.blob_id    = asb_request_log.body_blob_id
            )
;

--changeSet MTX-154:142 stripComments:false
-- create binary blob data for asb_request_log blobs
UPDATE asb_request_log
SET binary_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE
      blob_data.blob_db_id = asb_request_log.binary_blob_db_id AND
      blob_data.blob_id    = asb_request_log.binary_blob_id
            )
;

--changeSet MTX-154:143 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Drop foreign keys for ASB_REQUEST_LOG
--
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_REQUEST_LOG','FK_ASBREQUEST_BLOBDATA_BODY');        
END;
/  

--changeSet MTX-154:144 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_REQUEST_LOG','FK_ASBREQUEST_BLOBDATA_BIN');        
END;
/

--changeSet MTX-154:145 stripComments:false
-- delete blob data that has been moved to asb_request_log from table blob_data
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       body_blob_db_id,
       body_blob_id
    FROM 
       asb_request_log
   )
;

--changeSet MTX-154:146 stripComments:false
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       binary_blob_db_id,
       binary_blob_id
    FROM 
       asb_request_log
   )
;  

--changeSet MTX-154:147 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_REQUEST_LOG', 'BODY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:148 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_REQUEST_LOG', 'BODY_BLOB_ID');
END;
/

--changeSet MTX-154:149 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_REQUEST_LOG', 'BINARY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:150 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_REQUEST_LOG', 'BINARY_BLOB_ID');
END;
/  

--changeSet MTX-154:151 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Add new columns to ASB_RESPONSE_LOG
--
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_RESPONSE_LOG add BODY_BLOB blob
');
END;
/

--changeSet MTX-154:152 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_add('
alter table ASB_RESPONSE_LOG add BINARY_BLOB blob
');
END;
/

--changeSet MTX-154:153 stripComments:false
-- insert blob data into asb_response_log blobs
UPDATE asb_response_log
SET body_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE
      blob_data.blob_db_id = asb_response_log.body_blob_db_id AND
      blob_data.blob_id    = asb_response_log.body_blob_id
            )
;

--changeSet MTX-154:154 stripComments:false
-- insert blob data into asb_response_log blobs
UPDATE asb_response_log
SET binary_blob = (
   SELECT 
      blob_data.blob_data
   FROM 
      blob_data 
   WHERE
      blob_data.blob_db_id = asb_response_log.binary_blob_db_id AND
      blob_data.blob_id    = asb_response_log.binary_blob_id
            )
;

--changeSet MTX-154:155 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--
-- Drop foreign keys for ASB_RESPONSE_LOG
--
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_RESPONSE_LOG','FK_ASBRESPONSE_BLOBDATA_BODY');        
END;
/  

--changeSet MTX-154:156 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_drop('ASB_RESPONSE_LOG','FK_ASBRESPONSE_BLOBDATA_BIN');        
END;
/

--changeSet MTX-154:157 stripComments:false
-- delete blob data that has been moved to asb_response_log from table blob_data
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       body_blob_db_id,
       body_blob_id
    FROM 
       asb_response_log
   )
;

--changeSet MTX-154:158 stripComments:false
DELETE FROM 
   blob_data
WHERE
   ( blob_db_id, blob_id )
   IN
   (
    SELECT
       binary_blob_db_id,
       binary_blob_id
    FROM 
       asb_response_log
   )
;  

--changeSet MTX-154:159 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_RESPONSE_LOG', 'BODY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:160 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_RESPONSE_LOG', 'BODY_BLOB_ID');
END;
/

--changeSet MTX-154:161 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_RESPONSE_LOG', 'BINARY_BLOB_DB_ID');
END;
/

--changeSet MTX-154:162 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_drop('ASB_RESPONSE_LOG', 'BINARY_BLOB_ID');
END;
/

--changeSet MTX-154:163 stripComments:false
/********************************************************************************
*
* View:           vw_asb_connector_messages
*
* Description:    This view returns a unioned list of all the messages that have been logged in the ASB logging tables
*
*********************************************************************************/
CREATE OR REPLACE VIEW VW_ASB_CONNECTOR_MESSAGES
(
   message_id,
   transaction_id,
   external_id,
   message_date,
   message_type,
   message_source,
   body_blob,
   binary_blob,
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
   message.body_blob,
   message.binary_blob,
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
         asb_inbound_log.body_blob AS body_blob,
         asb_inbound_log.binary_blob AS binary_blob
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
         asb_outbound_log.body_blob AS body_blob,
         asb_outbound_log.binary_blob AS binary_blob
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
         asb_exception_log.body_blob AS body_blob,
         asb_exception_log.binary_blob AS binary_blob
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
         asb_notification_log.body_blob AS body_blob,
         asb_notification_log.binary_blob AS binary_blob
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
         asb_request_log.body_blob AS body_blob,
         asb_request_log.binary_blob AS binary_blob
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
         asb_response_log.body_blob AS body_blob,
         asb_response_log.binary_blob AS binary_blob
      FROM
         asb_response_log
   ) message INNER JOIN asb_transaction_log ON
      message.transaction_id = asb_transaction_log.transaction_id;

--changeSet MTX-154:164 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************** ADD COLUMN ORIG_CREATION_DATE TO BLOB_DATA TO SAVE THE ORIGINAL CREATION DATE OF THE BLOB*************************************/
BEGIN

   utl_migr_schema_pkg.table_column_add('
               Alter table BLOB_DATA add ( 
                  ORIG_CREATION_DT Date
                  )
           ');
END;
/

--changeSet MTX-154:165 stripComments:false
UPDATE BLOB_DATA
SET ORIG_CREATION_DT = CREATION_DT;

--changeSet MTX-154:166 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
               Alter table BLOB_DATA modify (
                  ORIG_CREATION_DT Date DEFAULT SYSDATE NOT NULL DEFERRABLE
                  )
               ');
                
END;
/           

--changeSet MTX-154:167 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************** DROP COLUMNS FROM BLOB_DATA THAT HAVE BEEN MOVED TO COR_BLOB_INFO ***************************************************************/
BEGIN
     utl_migr_schema_pkg.table_column_drop(p_table_name => 'BLOB_DATA',p_column_name => 'BLOB_FILE_NAME');
END;
/

--changeSet MTX-154:168 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_column_drop(p_table_name => 'BLOB_DATA',p_column_name => 'BLOB_CONTENT_TYPE');
END;
/

--changeSet MTX-154:169 stripComments:false
-- Update sequence BLOB_SEQ_ID by referencing to table COR_BLOB_INFO
/***************************************************************** MIGRATE EVT_ATTACH BLOBS TO BLOB_INFO/BLOB_DATA *****************************************************************************/
UPDATE 
   utl_sequence
   SET
      table_name = 'COR_BLOB_INFO'
   WHERE
      sequence_cd = 'BLOB_ID_SEQ' 
     AND
      table_name = 'BLOB_DATA';      

--changeSet MTX-154:170 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Aligns staging sequence for BLOB_ID with COR_BLOB_INFO.BLOB_ID max number
DECLARE
      ln_db_id NUMBER(10);
      
BEGIN
SELECT 
      db_id
  INTO
      ln_db_id
  FROM
      mim_local_db
 WHERE
      rownum = 1;
      

BLT_WF_CONTROLLER_PKG.reset_sequence(p_seq_name => 'blob_id_seq',
                                     p_tab_name => 'COR_BLOB_INFO',
                                     p_col_name => 'BLOB_ID',
                                     p_db_col_name => 'BLOB_DB_ID',
                                     p_db_id => ln_db_id );
END;
/

--changeSet MTX-154:171 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create a temporary mapping table mapping PK of evt_attach to PK of cor_blob_info
BEGIN
   utl_migr_schema_pkg.table_drop(p_table_name => 'TMP_EVT_ATTACH_BLOB_ID_MAP');
END;
/

--changeSet MTX-154:172 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    CREATE  TABLE TMP_EVT_ATTACH_BLOB_ID_MAP
           ( EVENT_DB_ID   NUMBER(10),
             EVENT_ID      NUMBER(10),
             EVENT_ATTACH_ID NUMBER(10),
             BLOB_DB_ID      NUMBER(10),
             BLOB_ID         NUMBER(10)
           )'
  );
END;
/                    

--changeSet MTX-154:173 stripComments:false
INSERT INTO         
       TMP_EVT_ATTACH_BLOB_ID_MAP
SELECT 
       event_db_id,
       event_id,
       event_attach_id,
       event_db_id, 
       blob_id_seq.NEXTVAL
FROM 
     EVT_ATTACH
WHERE EVT_ATTACH.ATTACH_BLOB IS NOT NULL;       

--changeSet MTX-154:174 stripComments:false
INSERT INTO cor_blob_info(blob_db_id,blob_id,blob_file_name,blob_content_type,blob_loc,blob_type,segregated_bool)
SELECT blob_db_id,
       blob_id,
       attach_filename,
       attach_content_type,
       'COR_BLOB_DATA',
       'EVT_ATTACH',
       0
FROM
    evt_attach
    JOIN TMP_EVT_ATTACH_BLOB_ID_MAP ON
         evt_attach.event_db_id      = TMP_EVT_ATTACH_BLOB_ID_MAP.EVENT_DB_ID AND
         evt_attach.event_id         = TMP_EVT_ATTACH_BLOB_ID_MAP.EVENT_ID AND
         evt_attach.event_attach_id  = TMP_EVT_ATTACH_BLOB_ID_MAP.EVENT_ATTACH_ID;

--changeSet MTX-154:175 stripComments:false
INSERT INTO BLOB_DATA(BLOB_DB_ID,BLOB_ID,BLOB_DATA,ORIG_CREATION_DT)
SELECT blob_db_id,blob_id,evt_attach.attach_blob, evt_attach.CREATION_DT
FROM
    evt_attach
    JOIN TMP_EVT_ATTACH_BLOB_ID_MAP ON
         evt_attach.event_db_id      = TMP_EVT_ATTACH_BLOB_ID_MAP.EVENT_DB_ID AND
         evt_attach.event_id         = TMP_EVT_ATTACH_BLOB_ID_MAP.EVENT_ID AND
         evt_attach.event_attach_id  = TMP_EVT_ATTACH_BLOB_ID_MAP.EVENT_ATTACH_ID;

--changeSet MTX-154:176 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop unused columns
BEGIN
     utl_migr_schema_pkg.table_column_drop(p_table_name => 'EVT_ATTACH',p_column_name => 'ATTACH_BLOB');
END;
/

--changeSet MTX-154:177 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN     
     utl_migr_schema_pkg.table_column_drop(p_table_name => 'EVT_ATTACH',p_column_name => 'ATTACH_FILENAME');
END;
/

--changeSet MTX-154:178 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN      
     utl_migr_schema_pkg.table_column_drop(p_table_name => 'EVT_ATTACH',p_column_name => 'ATTACH_CONTENT_TYPE');
END;
/

--changeSet MTX-154:179 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add the foreign key to cor_blob_info
BEGIN    
     utl_migr_schema_pkg.table_column_add(p_sql_clob => '
       ALTER TABLE EVT_ATTACH ADD (
               BLOB_DB_ID Number(10,0) Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
       )'
     );
END;
/

--changeSet MTX-154:180 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN     
     utl_migr_schema_pkg.table_column_add(p_sql_clob => '
       ALTER TABLE EVT_ATTACH ADD (
               BLOB_ID Number(10,0) Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
       )'
     );     
END;
/      

--changeSet MTX-154:181 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('
           Alter table EVT_ATTACH add Constraint FK_CORBLOBINFO_EVTATTACH foreign key (BLOB_DB_ID,BLOB_ID) references COR_BLOB_INFO (BLOB_DB_ID,BLOB_ID)  DEFERRABLE                                               
     ');
END;
/

--changeSet MTX-154:182 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_CORBLOBINFO_EVTATTACH" ON "EVT_ATTACH" ("BLOB_DB_ID","BLOB_ID") 
');
END;
/

--changeSet MTX-154:183 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- back fill value of blob_db_id, blob_id to evt_attach table
BEGIN
MERGE INTO evt_attach org
USING 
    (SELECT blob_db_id, 
            blob_id,
            event_db_id,
            event_id,
            event_attach_id           
       FROM TMP_EVT_ATTACH_BLOB_ID_MAP 
     ) tgt
ON (
       tgt.EVENT_DB_ID     = org.event_db_id  AND
       tgt.EVENT_ID        = org.event_id  AND
       tgt.EVENT_ATTACH_ID = org.event_attach_id)
WHEN MATCHED THEN
     UPDATE 
        SET org.blob_db_id = tgt.blob_db_id,
            org.blob_id    = tgt.blob_id;
DBMS_OUTPUT.PUT_LINE('INFO: Updated ' || SQL%ROWCOUNT || 
                          ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                          ' from the evt_attach table.');
END;
/

--changeSet MTX-154:184 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_drop(p_table_name => 'TMP_EVT_ATTACH_BLOB_ID_MAP');
END;
/    

--changeSet MTX-154:185 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/***************************************************************** MIGRATE INV_ATTACH BLOBS TO COR_BLOB_INFO/BLOB_DATA  *****************************************************************************/
BEGIN
   utl_migr_schema_pkg.table_drop(p_table_name => 'TMP_INV_ATTACH_BLOB_ID_MAP');
END;
/

--changeSet MTX-154:186 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
  utl_migr_schema_pkg.table_create('
    CREATE  TABLE TMP_INV_ATTACH_BLOB_ID_MAP
           ( INV_NO_DB_ID     NUMBER(10),
             INV_NO_ID        NUMBER(10),
             INV_NO_ATTACH_ID NUMBER(10),
             BLOB_DB_ID       NUMBER(10),
             BLOB_ID          NUMBER(10)
           )'
  );
END;
/     

--changeSet MTX-154:187 stripComments:false
-- create a mapping with event attach PK to new blob PK 
INSERT INTO         
       TMP_INV_ATTACH_BLOB_ID_MAP
SELECT 
       inv_no_db_id,
       inv_no_id,
       inv_no_attach_id,
       inv_no_db_id, 
       blob_id_seq.NEXTVAL
FROM 
     INV_ATTACH
WHERE INV_ATTACH.ATTACH_BLOB IS NOT NULL;       

--changeSet MTX-154:188 stripComments:false
INSERT INTO cor_blob_info(blob_db_id,blob_id,blob_file_name,blob_content_type,blob_loc,blob_type,segregated_bool)
SELECT blob_db_id,
       blob_id,
       attach_filename,
       attach_content_type,
       'COR_BLOB_DATA',
       'INV_ATTACH',
       0
FROM
    inv_attach
    JOIN TMP_INV_ATTACH_BLOB_ID_MAP ON
         inv_attach.inv_no_db_id      = TMP_INV_ATTACH_BLOB_ID_MAP.inv_no_db_id AND
         inv_attach.inv_no_id         = TMP_INV_ATTACH_BLOB_ID_MAP.inv_no_id AND
         inv_attach.inv_no_attach_id  = TMP_INV_ATTACH_BLOB_ID_MAP.inv_no_attach_id;

--changeSet MTX-154:189 stripComments:false
INSERT INTO BLOB_DATA(BLOB_DB_ID,BLOB_ID,BLOB_DATA, ORIG_CREATION_DT)
SELECT blob_db_id,blob_id,inv_attach.attach_blob, inv_attach.CREATION_DT
FROM
    inv_attach
    JOIN TMP_INV_ATTACH_BLOB_ID_MAP ON
         inv_attach.inv_no_db_id      = TMP_INV_ATTACH_BLOB_ID_MAP.inv_no_db_id AND
         inv_attach.inv_no_id         = TMP_INV_ATTACH_BLOB_ID_MAP.inv_no_id AND
         inv_attach.inv_no_attach_id  = TMP_INV_ATTACH_BLOB_ID_MAP.inv_no_attach_id;          

--changeSet MTX-154:190 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop unused columns
BEGIN
     utl_migr_schema_pkg.table_column_drop(p_table_name => 'INV_ATTACH',p_column_name => 'ATTACH_BLOB');
END;
/

--changeSet MTX-154:191 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN     
     utl_migr_schema_pkg.table_column_drop(p_table_name => 'INV_ATTACH',p_column_name => 'ATTACH_FILENAME');
END;
/

--changeSet MTX-154:192 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN      
     utl_migr_schema_pkg.table_column_drop(p_table_name => 'INV_ATTACH',p_column_name => 'ATTACH_CONTENT_TYPE');
END;
/

--changeSet MTX-154:193 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- add the foreign key to cor_blob_info
BEGIN    
     utl_migr_schema_pkg.table_column_add(p_sql_clob => '
       ALTER TABLE INV_ATTACH ADD (
               BLOB_DB_ID Number(10,0) Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
       )'
     );
END;
/

--changeSet MTX-154:194 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN     
     utl_migr_schema_pkg.table_column_add(p_sql_clob => '
       ALTER TABLE INV_ATTACH ADD (
               BLOB_ID Number(10,0) Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
       )'
     );     
END;
/     

--changeSet MTX-154:195 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('
           Alter table INV_ATTACH add Constraint FK_CORBLOBINFO_INVATTACH foreign key (BLOB_DB_ID,BLOB_ID) references COR_BLOB_INFO (BLOB_DB_ID,BLOB_ID)  DEFERRABLE                                               
     ');
END;
/

--changeSet MTX-154:196 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
 Create Index "IX_FK_CORBLOBINFO_INVATTACH" ON "INV_ATTACH" ("BLOB_DB_ID","BLOB_ID") 
');
END;
/

--changeSet MTX-154:197 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- back fill value of blob_db_id, blob_id to inv_attach table
BEGIN
MERGE INTO inv_attach org
USING 
    (SELECT blob_db_id, 
            blob_id,
            inv_no_db_id,
            inv_no_id,
            inv_no_attach_id           
       FROM TMP_INV_ATTACH_BLOB_ID_MAP 
     ) tgt
ON (
       tgt.INV_NO_DB_ID     = org.inv_no_db_id  AND
       tgt.INV_NO_ID        = org.inv_no_id  AND
       tgt.INV_NO_ATTACH_ID = org.inv_no_attach_id)
WHEN MATCHED THEN
     UPDATE 
        SET org.blob_db_id = tgt.blob_db_id,
            org.blob_id    = tgt.blob_id;
DBMS_OUTPUT.PUT_LINE('INFO: Updated ' || SQL%ROWCOUNT || 
                     ' row' || (CASE WHEN SQL%ROWCOUNT != 1 THEN 's' END) || 
                     ' from the inv_attach table.');
END;
/

--changeSet MTX-154:198 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_drop(p_table_name => 'TMP_INV_ATTACH_BLOB_ID_MAP');
END;
/

--changeSet MTX-154:199 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
               Alter table INV_ATTACH modify (
                  BLOB_DB_ID Number(10,0) NOT NULL DEFERRABLE Check (BLOB_DB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
                  )
               ');
                
END;
/

--changeSet MTX-154:200 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_column_modify('
               Alter table INV_ATTACH modify (
                  BLOB_ID Number(10,0) NOT NULL DEFERRABLE Check (BLOB_ID BETWEEN 0 AND 4294967295 ) DEFERRABLE
                  )
               ');
                
END;
/

--changeSet MTX-154:201 stripComments:false
/***************************************************************** INSERT UTL_JOB RECORDS FOR SEGREGATION JOBS  *****************************************************************************/
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
SELECT 'MX_COMMON_BLOB_SEGREGATE', 'BLOB Segregation job', null, 120, 604800, 0, 0
FROM 
     dual
WHERE NOT EXISTS
      (SELECT 1 FROM utl_job WHERE job_cd = 'MX_COMMON_BLOB_SEGREGATE');          

--changeSet MTX-154:202 stripComments:false
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
SELECT 'MX_COMMON_BLOB_TABLE_CLEANING', 'Truncate the segregated BLOB tables', null, 120, 2419200, 0, 0
FROM
    dual
WHERE NOT EXISTS
      (SELECT 1 FROM utl_job WHERE job_cd = 'MX_COMMON_BLOB_TABLE_CLEANING');    

--changeSet MTX-154:203 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************INSERT UTL_CONFIG_PARM RECORDS FOR BLOB SEGREGATION **************************************************************************/
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'BLOB_PRIMARY_LOC',
      'LOGIC',
      'The root path to the primary BLOB storage medium,e.g.,M:/BLOB/',
      'GLOBAL',
      'The path must be accessible to Maintenix server and end with forward slash/',
      '',
      1,
      'Data Volume Management - BLOB segregation',
      '8.1-SP1',
      0
   );
END;
/

--changeSet MTX-154:204 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'BLOB_SECONDARY_LOC',
      'LOGIC',
      'The root path to backup medium of segregated BLOBs,,e.g.,N:/BLOB/',
      'GLOBAL',
      'The path must be accessible to Maintenix server and end with forward slash/',
      '',
      1,
      'Data Volume Management - BLOB segregation',
      '8.1-SP1',
      0
   );
END;
/

--changeSet MTX-154:205 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'BLOB_RETENTION_PERIOD',
      'LOGIC',
      'Indicates the minimum age of a BLOB, in months, so that the BLOB is copied to the monthly tables, and eventually copied to the BLOB storage medium. A value of 0 means that all BLOBs remain in the operational database permanently.',
      'GLOBAL',
      'Integer from 0 to 10',
      0,
      1,
      'Data Volume Management - BLOB segregation',
      '8.1-SP1',
      0
   );
END;
/

--changeSet MTX-154:206 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      'BLOB_SEGREGATE_CHUNK_SIZE',
      'LOGIC',
      'Indicates the maximum number of rows of BLOB records (satisfying the segregation criteria) that will be processed/copied in one Blob Segregation job run.',
      'GLOBAL',
      'Integer. 0 implies that the chunk size will be Integer.MAX_VALUE, which means all satisfying rows will be proccesed in the segregation job run.',
      0,
      1,
      'Data Volume Management - BLOB segregation',
      '8.1-SP1',
      0
   );
END;
/

--changeSet MTX-154:207 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_drop(p_index_name => 'IX_BLOBDATA_ESIGDOC');
END;
/

--changeSet MTX-154:208 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_drop(p_index_name => 'IX_BLOBDATA_WARRANTYATTACH');
END;
/

--changeSet MTX-154:209 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_drop(p_index_name => 'IX_BLOB_DATA_ESIG_DOC_SIGN');
END;
/

--changeSet MTX-154:210 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.index_drop(p_index_name => 'IX_ORGLOGO_BLOBDATA');
END;
/

--changeSet MTX-154:211 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.trigger_drop(p_trigger_name => 'TIBR_BLOB_DATA');
END;
/

--changeSet MTX-154:212 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.trigger_drop(p_trigger_name => 'TUBR_BLOB_DATA');
END;
/

--changeSet MTX-154:213 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_drop('BLOB_DATA', 'FK_MIMRSTAT_BLOBDATA');
END;
/

--changeSet MTX-154:214 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_constraint_drop('BLOB_DATA', 'FK_MIMDB_BLOBDATA');
END;
/

--changeSet MTX-154:215 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   utl_migr_schema_pkg.table_rename(
      'BLOB_DATA',
      'COR_BLOB_DATA'
      );
END;
/

--changeSet MTX-154:216 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('    
         Alter table COR_BLOB_DATA add Constraint FK_MIMDB_CORBLOBDATA foreign key (BLOB_DB_ID) references MIM_DB (DB_ID)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:217 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
     utl_migr_schema_pkg.table_constraint_add('  
        Alter table COR_BLOB_DATA add Constraint FK_MIMRSTAT_CORBLOBDATA foreign key (RSTAT_CD) references MIM_RSTAT (RSTAT_CD)  DEFERRABLE
     ');
END;
/

--changeSet MTX-154:218 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_CORBLOBINFO_ESIGDOC" ON "ESIG_DOC" ("BLOB_DB_ID","BLOB_ID") 
');
END;
/

--changeSet MTX-154:219 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_CORBLOBINFO_ESIGDOCSIGN" ON "ESIG_DOC_SIGN" ("BLOB_DB_ID","BLOB_ID") 
');
END;
/

--changeSet MTX-154:220 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_CORBLOBINFO_WRRNTYATTACH" ON "WARRANTY_ATTACH" ("BLOB_DB_ID","BLOB_ID") 
');
END;
/

--changeSet MTX-154:221 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.index_create('
   Create Index "IX_FK_CORPERMBLOB_ORGLOGO" ON "ORG_LOGO" ("BLOB_DB_ID","BLOB_ID") 
');
END;
/ 

--changeSet MTX-154:222 stripComments:false
ALTER TABLE COR_BLOB_DATA RENAME CONSTRAINT PK_BLOB_DATA TO PK_COR_BLOB_DATA; 

--changeSet MTX-154:223 stripComments:false
ALTER INDEX PK_BLOB_DATA RENAME TO PK_COR_BLOB_DATA;

--changeSet MTX-154:224 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TIBR_COR_BLOB_DATA" BEFORE INSERT
   ON "COR_BLOB_DATA" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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

--changeSet MTX-154:225 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TRIGGER "TUBR_COR_BLOB_DATA" BEFORE UPDATE
   ON "COR_BLOB_DATA" REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
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