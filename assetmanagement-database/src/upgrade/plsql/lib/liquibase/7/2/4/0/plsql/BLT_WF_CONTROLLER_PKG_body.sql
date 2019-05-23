--liquibase formatted sql


--changeSet BLT_WF_CONTROLLER_PKG_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
                wf_cycle_err_msg         = CASE WHEN p_wf_cycle_err_msg IS NOT NULL THEN p_wf_cycle_err_msg ELSE wf_cycle_err_msg END
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

      v_step := 30;
      EXECUTE IMMEDIATE 'alter sequence ' || p_seq_name || ' increment by ' ||
                        v_new_val || ' minvalue 0';

      v_step := 40;
      EXECUTE IMMEDIATE 'select ' || p_seq_name || '.nextval from dual'
         INTO v_val;

      v_step := 50;
      EXECUTE IMMEDIATE 'alter sequence ' || p_seq_name ||
                        ' increment by 1 minvalue 0';

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