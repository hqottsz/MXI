--liquibase formatted sql


--changeSet JOBS_ADMIN_PKG_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE "JOBS_ADMIN_PKG" AS
   ----------------------------------------------------------------------------
   -- Object Name : JOBS_ADMIN_PKG
   -- Object Type : Package Header
   -- Date        : October 22, 2009
   -- Coder       : Ioulia Doumkina
   -- Recent Date : November 1, 2009
   -- Recent Coder: Ed Irvine
   -- Description :
   -- This package contains methods mainting oracle jobs scheduled in the
   -- UTL_JOB table
   --
   -----------------------------------------------------------------------------
   -- Copyright © 2009 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------
   ----------------------------------------------------------------------------
   -- global package constants
   ----------------------------------------------------------------------------
   gc_status_idle    CONSTANT utl_job.status_cd%TYPE := 'IDLE';
   gc_status_procing CONSTANT utl_job.status_cd%TYPE := 'PROCESSING';
   gc_status_queued  CONSTANT utl_job.status_cd%TYPE := 'QUEUED';

   ----------------------------------------------------------------------------
   -- Procedure:   create_job_and_schedule
   -- Arguments:   p_job_cd - name of the
   --              p_schedule_date - date to start
   --              p_repeat_expression - interval
   --              p_program_type - type of program to be executed
   --              p_program_name - name of program or plsql block
   -- Description: Creates job and schedule for given job code
   ----------------------------------------------------------------------------
   PROCEDURE create_job_and_schedule(p_job_cd            IN VARCHAR2,
                                     p_schedule_date     IN DATE,
                                     p_repeat_expression IN VARCHAR2,
                                     p_program_type      IN VARCHAR2,
                                     p_program_name      IN VARCHAR2,
                                     p_active_boolean    IN NUMBER);
   ----------------------------------------------------------------------------
   -- Procedure:   drop_job_and_schedule
   -- Arguments:   p_job_cd - name of the schedule
   --              p_force  - whether to kill current job if running
   -- Description: Procedure drops a schedule and job
   ----------------------------------------------------------------------------
   PROCEDURE drop_job_and_schedule(p_job_cd     IN VARCHAR2,
                                   p_force_bool IN BOOLEAN DEFAULT FALSE);
   ----------------------------------------------------------------------------
   -- Procedure:   enable_job.
   -- Arguments:   p_job_cd - name of the job
   -- Description: Procedure enables a job
   ----------------------------------------------------------------------------
   PROCEDURE enable_job(p_job_cd IN VARCHAR2);
   ----------------------------------------------------------------------------
   -- Procedure:   disable_job.
   -- Arguments:   p_job_cd - name of the job
   -- Description: Procedure enables or disables a job
   ----------------------------------------------------------------------------
   PROCEDURE disable_job(p_job_cd IN VARCHAR2);
   ----------------------------------------------------------------------------
   -- Procedure:   stop_job
   -- Arguments:   p_job_cd - name of the schedule
   --              p_program_name - name of the program to run
   -- Description: Procedure stops a job.
   ----------------------------------------------------------------------------
   PROCEDURE stop_job(p_job_cd     IN VARCHAR2,
                      p_force_bool IN BOOLEAN DEFAULT FALSE);
   ----------------------------------------------------------------------------
   -- Procedure:   run_program
   -- Arguments:   p_job_cd - job cd of job
   --              p_program_name - name of procedure or calling block
   --
   -- Description: Procedure runs a procedure specified in the
   ----------------------------------------------------------------------------
   PROCEDURE run_program(p_job_cd       IN VARCHAR2,
                         p_program_name IN VARCHAR2);
   ----------------------------------------------------------------------------
   -- Function:    get_job_name
   -- Arguments:   p_job_cd - name of the job code
   --
   -- Returns: VARCHAR2
   --
   -- Description: This function will return a unique job name relevant
   --              to the schema the job is for.
   ----------------------------------------------------------------------------
   FUNCTION get_job_name(p_job_cd IN VARCHAR2) RETURN VARCHAR2;
   ----------------------------------------------------------------------------
   -- Function:    get_repeat_expression
   -- Arguments:   p_start_time        - required start time
   --              p_repeat_interval   - interval between runs in seconds
   --              p_repeat_expression - repeat expression for job engine
   --
   -- Returns: VARCHAR2
   --
   -- Description: This function will return the correct repeat expression
   --              dependent on the three configurable parameters
   ----------------------------------------------------------------------------
   FUNCTION get_repeat_expression(p_start_time        IN VARCHAR2 DEFAULT NULL,
                                  p_repeat_interval   IN NUMBER DEFAULT NULL,
                                  p_repeat_expression IN VARCHAR2 DEFAULT NULL)
      RETURN VARCHAR2;
END jobs_admin_pkg;
/