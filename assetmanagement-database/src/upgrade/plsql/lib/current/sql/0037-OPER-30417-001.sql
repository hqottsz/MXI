--liquibase formatted sql


--changeSet OPER-30417-001:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
