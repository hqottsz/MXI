--liquibase formatted sql


--changeSet DEV-1322:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "BLT_UTL_ERR_CONST_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : blt_utl_err_const_pkg
   -- Object Type : Package Header
   -- Date        : January 04, 2010
   -- Coder       : Ed Irvine
   -- Recent Date : January 04, 2010
   -- Recent Coder: Ed Irvine
   -- Description :
   -- This package contains the error constants for the BLT function.
   ----------------------------------------------------------------------------
   -- Copyright @ 2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------

   -- Exception code must be unique and between -20001 and -20999
   ----------------------------------------------------------------------------
   gv_err_msg_gen CONSTANT VARCHAR2(2000) := 'An Oracle error has occurred; details are as follows: ';

   gv_err_wf_error CONSTANT VARCHAR2(2000) := 'One or more workflows failed to finish successfully.';

   ----------------------------------------------------------------------------
   -- Common exceptions
   ----------------------------------------------------------------------------

   gc_ex_wf_controller CONSTANT NUMBER := -20100;
   gex_wf_controller EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_wf_controller,
                         -20100);
   -- LOAD EXCEPTIONS
   gc_ex_import_running CONSTANT NUMBER := -20101;
   gex_import_running EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_import_running,
                         -20101);
   gv_err_msg_import_running CONSTANT VARCHAR2(2001) := 'A load process has already been started.';

   gc_ex_amu_no_data CONSTANT NUMBER := -20102;
   gex_amu_no_data EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_amu_no_data,
                         -20102);
   gv_err_msg_amu_no_data CONSTANT VARCHAR2(2001) := 'There is currently no valid data to be processed. Make sure you have run AMU successfully.';

   -- VALIDATE EXCEPTIONS
   gc_ex_validate CONSTANT NUMBER := -20103;
   gex_validate EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_validate,
                         -20103);
   -- LOOKUP EXCEPTIONS
   gc_ex_lookup CONSTANT NUMBER := -20104;
   gex_lookup EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_lookup,
                         -20104);
END blt_utl_err_const_pkg;
/

--changeSet DEV-1322:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "BLT_UTL_ERR_CONST_PKG" IS
----------------------------------------------------------------------------
-- Object Name : blt_utl_err_const_pkg
-- Object Type : Package Body
-- Date        : January 04, 2010
-- Coder       : Ed Irvine
-- Recent Date : January 04, 2010
-- Recent Coder: Ed Irvine
-- Description :
-- This package contains the error constants for the BLT function.
----------------------------------------------------------------------------
-- Copyright @ 2010 MxI Technologies Ltd.  All Rights Reserved.
-- Any distribution of the MxI Maintenix source code by any other party
-- than MxI Technologies Ltd is prohibited.
----------------------------------------------------------------------------
END blt_utl_err_const_pkg;
/

--changeSet DEV-1322:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   PROCEDURE insert_blt_wf_cycle_log(p_wf_cycle_log_id  IN OUT blt_wf_cycle_log.wf_cycle_log_id%TYPE,
                         p_wf_cycle_cd              IN blt_wf_cycle_log.wf_cycle_cd%TYPE,
                         p_wf_cycle_status_cd       IN blt_wf_cycle_log.ref_wf_cycle_status_cd%TYPE,
                         p_wf_cycle_start_dt        IN blt_wf_cycle_log.wf_cycle_start_dt%TYPE,
                         p_wf_cycle_end_dt          IN blt_wf_cycle_log.wf_cycle_end_dt%TYPE,
                         p_wf_cycle_err_status_cd   IN blt_wf_cycle_log.wf_cycle_err_status_cd%TYPE,
                         p_wf_cycle_err_msg         IN blt_wf_cycle_log.wf_cycle_err_msg%TYPE);
                         
   

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
   PROCEDURE insert_blt_wf_rec_log(p_wf_rec_log_id   IN OUT blt_wf_rec_log.wf_rec_log_id%TYPE,
                            p_wf_cycle_log_id   IN blt_wf_rec_log.wf_cycle_log_id%TYPE,
                            p_wf_log_status_cd  IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE,
                            p_wf_log_type_cd    IN blt_ref_wf_log_type.ref_wf_log_type_cd%TYPE,
                            p_wf_log_rec_identifier  IN blt_wf_rec_log.wf_log_rec_identifier%TYPE,
                            p_wf_log_start_dt   IN blt_wf_rec_log.wf_log_start_dt%TYPE,
                            p_wf_log_end_dt     IN blt_wf_rec_log.wf_log_end_dt%TYPE,
                            p_wf_log_err_status_cd IN blt_wf_rec_log.wf_log_err_status_cd%TYPE,
                            p_wf_log_err_msg       IN blt_wf_rec_log.wf_log_err_msg%TYPE,
                            p_run_mode          IN NUMBER);
END blt_wf_controller_pkg;
/

--changeSet DEV-1322:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   
   -----------------------------------------------------------------------------
   -- Local Package Variables
   -----------------------------------------------------------------------------
   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);
   
   
   
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
   PROCEDURE insert_blt_wf_cycle_log(p_wf_cycle_log_id  IN OUT blt_wf_cycle_log.wf_cycle_log_id%TYPE,
                         p_wf_cycle_cd              IN blt_wf_cycle_log.wf_cycle_cd%TYPE,
                         p_wf_cycle_status_cd       IN blt_wf_cycle_log.ref_wf_cycle_status_cd%TYPE,
                         p_wf_cycle_start_dt        IN blt_wf_cycle_log.wf_cycle_start_dt%TYPE,
                         p_wf_cycle_end_dt          IN blt_wf_cycle_log.wf_cycle_end_dt%TYPE,
                         p_wf_cycle_err_status_cd   IN blt_wf_cycle_log.wf_cycle_err_status_cd%TYPE,
                         p_wf_cycle_err_msg         IN blt_wf_cycle_log.wf_cycle_err_msg%TYPE)
  AS                         
   
      v_method_name VARCHAR2(30) := 'insert_blt_wf_cycle_log';
   
      PRAGMA AUTONOMOUS_TRANSACTION;
   BEGIN
      --if p_blt_log_id is null, then new blt wf cycle log is to be inserted.
      v_step := 10;
      IF p_wf_cycle_log_id IS NULL
      THEN
         v_step := 20;
         SELECT mx_key_pkg.new_uuid
           INTO p_wf_cycle_log_id
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
            (p_wf_cycle_log_id,
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
          WHERE wf_cycle_log_id = p_wf_cycle_log_id;
      END IF;
   
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
   PROCEDURE insert_blt_wf_rec_log(p_wf_rec_log_id   IN OUT blt_wf_rec_log.wf_rec_log_id%TYPE,
                            p_wf_cycle_log_id   IN blt_wf_rec_log.wf_cycle_log_id%TYPE,
                            p_wf_log_status_cd  IN blt_ref_wf_log_status.ref_wf_log_status_cd%TYPE,
                            p_wf_log_type_cd    IN blt_ref_wf_log_type.ref_wf_log_type_cd%TYPE,
                            p_wf_log_rec_identifier  IN blt_wf_rec_log.wf_log_rec_identifier%TYPE,
                            p_wf_log_start_dt   IN blt_wf_rec_log.wf_log_start_dt%TYPE,
                            p_wf_log_end_dt     IN blt_wf_rec_log.wf_log_end_dt%TYPE,
                            p_wf_log_err_status_cd IN blt_wf_rec_log.wf_log_err_status_cd%TYPE,
                            p_wf_log_err_msg       IN blt_wf_rec_log.wf_log_err_msg%TYPE,
                            p_run_mode          IN NUMBER) 
   AS

      v_method_name VARCHAR2(30) := 'insert_blt_wf_rec_log';

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
          WHERE wf_cycle_log_id = p_wf_cycle_log_id
            AND ref_wf_log_type_cd = p_wf_log_type_cd;

      ELSIF p_wf_rec_log_id IS NULL
      THEN
         v_step := 20;
         SELECT mx_key_pkg.new_uuid
           INTO p_wf_rec_log_id
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
            (p_wf_rec_log_id,
             p_wf_cycle_log_id,
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
          WHERE wf_rec_log_id = p_wf_rec_log_id;
      END IF;

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
   
END blt_wf_controller_pkg;
/

--changeSet DEV-1322:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "BLT_WF_ERROR_LOG_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : BLT_WF_ERROR_LOG_PKG
   -- Object Type : Package Header
   -- Date        : Nov 25,2011
   -- Coder       : Vince Chan
   -- Recent Date : Nov 25,2011
   -- Recent Coder: Vince Chan
   -- Description :
   -- This package contains methods for inserting into blt_wf_error_log
   --
   ----------------------------------------------------------------------------
   -- Copyright @2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
 
 
   -----------------------------------------------------------------------------------------
   -- Procedure:    insert_blt_wf_error
   -- Arguments:    p_wf_error_log_id          
   --               p_wf_log_id          
   --               p_wf_cycle_log_id
   --               p_ref_error_cd
   --               p_wf_error_rec_identifier
   -- Description:  Log procedure inserts and updates records into the
   --               blt_wf_error_log table
   ----------------------------------------------------------------------------------------- 
 PROCEDURE insert_blt_wf_error(p_wf_error_log_id  IN OUT blt_wf_error_log.wf_error_log_id%TYPE,
                       p_wf_rec_log_id               IN blt_wf_error_log.wf_rec_log_id%TYPE,
                       p_wf_cycle_log_id          IN blt_wf_error_log.wf_cycle_log_id%TYPE,
                       p_ref_error_cd             IN blt_wf_error_log.ref_error_cd%TYPE,
                       p_wf_error_rec_identifier  IN blt_wf_error_log.wf_error_rec_identifier%TYPE
  );

END BLT_WF_ERROR_LOG_PKG;
/

--changeSet DEV-1322:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "BLT_WF_ERROR_LOG_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : BLT_WF_ERROR_LOG_PKG
   -- Object Type : Package Body
   -- Date        : Nov 25,2011
   -- Coder       : Vince Chan
   -- Recent Date : Nov 25,2011
   -- Recent Coder: Vince Chan
   -- Description :
   -- This package contains methods for inserting into blt_wf_error_log
   --
   ----------------------------------------------------------------------------
   -- Copyright @2010-11 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   
   -----------------------------------------------------------------------------
   -- Local Package Constants
   -----------------------------------------------------------------------------
   c_pkg_name         CONSTANT VARCHAR2(30) := 'BLT_WF_ERROR_LOG_PKG';
   
   -----------------------------------------------------------------------------
   -- Local Package Variables
   -----------------------------------------------------------------------------
   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);
   
   
   -----------------------------------------------------------------------------------------
   -- Procedure:    insert_blt_wf_error
   -- Arguments:    p_wf_error_log_id          
   --               p_wf_rec_log_id          
   --               p_wf_cycle_log_id
   --               p_ref_error_cd
   --               p_wf_error_rec_identifier
   -- Description:  Log procedure inserts and updates records into the
   --               blt_wf_error_log table
   ----------------------------------------------------------------------------------------- 
 PROCEDURE insert_blt_wf_error(p_wf_error_log_id  IN OUT blt_wf_error_log.wf_error_log_id%TYPE,
                       p_wf_rec_log_id               IN blt_wf_error_log.wf_rec_log_id%TYPE,
                       p_wf_cycle_log_id          IN blt_wf_error_log.wf_cycle_log_id%TYPE,
                       p_ref_error_cd             IN blt_wf_error_log.ref_error_cd%TYPE,
                       p_wf_error_rec_identifier  IN blt_wf_error_log.wf_error_rec_identifier%TYPE
  )
  AS                         
   
      v_method_name VARCHAR2(30) := 'insert_blt_wf_error';
      PRAGMA AUTONOMOUS_TRANSACTION;
  BEGIN
      --if p_blt_log_id is null, then new blt wf cycle log is to be inserted.
      v_step := 10;
      IF p_wf_error_log_id IS NULL
      THEN
         v_step := 20;
         SELECT mx_key_pkg.new_uuid
           INTO p_wf_error_log_id
           FROM dual;
      
         v_step := 30;
         INSERT INTO blt_wf_error_log
            (wf_error_log_id,
             wf_rec_log_id,
             wf_cycle_log_id,
             ref_error_cd,
             wf_error_rec_identifier )
         VALUES
            (p_wf_error_log_id,
             p_wf_rec_log_id,
             p_wf_cycle_log_id,
             p_ref_error_cd,
             p_wf_error_rec_identifier);
      
      ELSE
         --update existing log
         v_step := 40;
         UPDATE blt_wf_error_log
            SET wf_rec_log_id            = CASE WHEN p_wf_rec_log_id IS NOT NULL THEN p_wf_rec_log_id ELSE wf_rec_log_id END,
                wf_cycle_log_id          = CASE WHEN p_wf_cycle_log_id IS NOT NULL THEN p_wf_cycle_log_id ELSE wf_cycle_log_id END,
                ref_error_cd             = CASE WHEN p_ref_error_cd IS NOT NULL THEN p_ref_error_cd ELSE ref_error_cd END,
                wf_error_rec_identifier  = CASE WHEN p_wf_error_rec_identifier IS NOT NULL THEN p_wf_error_rec_identifier ELSE wf_error_rec_identifier END 
          WHERE wf_error_log_id = p_wf_error_log_id;
      END IF;
   
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
  END insert_blt_wf_error; 
      
END blt_wf_error_log_pkg;
/