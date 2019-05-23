--liquibase formatted sql


--changeSet JOBS_ADMIN_PKG_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PACKAGE BODY "JOBS_ADMIN_PKG" AS
   ----------------------------------------------------------------------------
   -- Object Name : JOBS_ADMIN_PKG
   -- Object Type : Package Body
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
   -----------------------------------------------------------------------------
   -- Local Package Constants
   -----------------------------------------------------------------------------
   c_pkg_name     CONSTANT VARCHAR2(30) := 'JOBS_ADMIN_PKG';
   gv_err_msg_gen CONSTANT VARCHAR2(2000) := 'An Oracle error has occurred details are as follows: ';

   -----------------------------------------------------------------------------
   -- Local Package Variables
   -----------------------------------------------------------------------------
   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);

   gc_ex_job_admin_err CONSTANT NUMBER := -20103;
   gex_job_admin_err EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_job_admin_err,
                         -20103);
   ----------------------------------------------------------------------------
   -- Procedure:   log_job_start
   -- Arguments:   p_name - name of the job
   -- Description: Procedure updates utl_job
   --              table columns start_time, last_start_dt
   --              with sysdate and status_cd with value 'PROCESSING'
   ----------------------------------------------------------------------------
   PROCEDURE log_job_start(p_name VARCHAR2);
   ----------------------------------------------------------------------------
   -- Procedure:   log_job_end
   -- Arguments:   p_name - name of the job
   -- Description: Procedure updates utl_job
   --              table columns last_end_dt
   --              with sysdate and status_cd with value 'IDLE'
   ----------------------------------------------------------------------------
   PROCEDURE log_job_end(p_name VARCHAR2);
   ----------------------------------------------------------------------------
   -- Procedure:   log_job_error
   -- Arguments:   p_name - name of the job
   -- Description: Procedure updates utl_job
   --              table columns last_end_dt with current date, last_error with error message
   --              and status_cd with value 'IDLE'
   ----------------------------------------------------------------------------
   PROCEDURE log_job_error(p_name  VARCHAR2,
                           p_error VARCHAR2);

   ----------------------------------------------------------------------------
   -- Function:     isScheduleCreated
   -- Arguments:    p_name - name of the schedule
   --
   -- Returns:  NUMBER
   -- Description:  Function returns 1 if schedule already exist
   --               Function returns 0 if schedule does not exist
   ----------------------------------------------------------------------------
   FUNCTION isschedulecreated(p_name IN VARCHAR2) RETURN NUMBER AS
      v_scheduler_name all_scheduler_schedules.schedule_name%TYPE;
   BEGIN
      /*check if scheduler already exist*/
      SELECT schedule_name
        INTO v_scheduler_name
        FROM all_scheduler_schedules
       WHERE schedule_name = upper(p_name);
      RETURN 1;
      /*if scheduler does not exist, then create one*/
   EXCEPTION
      WHEN no_data_found THEN
         RETURN 0;
      WHEN OTHERS THEN
         application_object_pkg.setmxierror('DEV-99999',
                                            c_pkg_name ||
                                            '@@@isScheduleCreated@@@' ||
                                            SQLERRM);
         RETURN 0;
   END;

   ----------------------------------------------------------------------------
   -- Procedure:   create_job_and_schedule
   -- Arguments:   p_job_cd - name of the
   -- Description: Creates job and schedule for given job code
   ----------------------------------------------------------------------------
   PROCEDURE create_job_and_schedule(p_job_cd            IN VARCHAR2,
                                     p_schedule_date     IN DATE,
                                     p_repeat_expression IN VARCHAR2,
                                     p_program_type      IN VARCHAR2,
                                     p_program_name      IN VARCHAR2,
                                     p_active_boolean    IN NUMBER) AS

      v_job_name VARCHAR2(100);

   BEGIN

      -- Check whether schedule already exists for this job.
      v_step := 10;
      IF isschedulecreated(p_job_cd) = 0
      THEN
         -- Create schedule for job
         v_step := 25;
         dbms_scheduler.create_schedule(schedule_name   => p_job_cd,
                                        start_date      => p_schedule_date,
                                        repeat_interval => p_repeat_expression);
      END IF;

      -- Get unique job name
      v_step     := 30;
      v_job_name := get_job_name(p_job_cd);

      -- Start the job with a specified schedule
      dbms_scheduler.create_job(job_name      => v_job_name,
                                schedule_name => p_job_cd,
                                job_type      => p_program_type,
                                job_action    => p_program_name);

      COMMIT;
      -- Enable job. Job will be picked by the scheduler to run
      IF p_active_boolean = 1
      THEN
         enable_job(p_job_cd);
      ELSE
         disable_job(p_job_cd);
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name ||
                                        '.create_job_and_schedule ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' at Step: ' || v_step,
                                        1,
                                        2000),
                                 TRUE);

   END create_job_and_schedule;
   ----------------------------------------------------------------------------
   -- Procedure:   drop_job_and_schedule
   -- Arguments:   p_job_cd - name of the schedule
   --              p_force  - whether to kill current job if running
   -- Description: Procedure drops a schedule and job
   ----------------------------------------------------------------------------
   PROCEDURE drop_job_and_schedule(p_job_cd     IN VARCHAR2,
                                   p_force_bool IN BOOLEAN DEFAULT FALSE) AS
   BEGIN

      -- First drop schedule
      v_step := 10;
      dbms_scheduler.drop_schedule(schedule_name => p_job_cd,
                                   force         => p_force_bool);

      -- Drop job
      v_step := 20;
      dbms_scheduler.drop_job(job_name => get_job_name(p_job_cd),
                              force    => p_force_bool);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name || '.drop_job_and_schedule ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' at Step: ' || v_step,
                                        1,
                                        2000),
                                 TRUE);

   END drop_job_and_schedule;
   ----------------------------------------------------------------------------
   -- Procedure:   enable_job.
   -- Arguments:   p_job_cd - name of the job
   -- Description: Procedure enables a job
   ----------------------------------------------------------------------------
   PROCEDURE enable_job(p_job_cd IN VARCHAR2) AS
   BEGIN
      -- Enable job given
      v_step := 10;
      dbms_scheduler.enable(NAME => get_job_name(p_job_cd));
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name || '.enable_job ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' at Step: ' || v_step,
                                        1,
                                        2000),
                                 TRUE);
   END;

   ----------------------------------------------------------------------------
   -- Procedure:   disable_job.
   -- Arguments:   p_name - name of the schedule
   -- Description: Procedure disables a job
   ----------------------------------------------------------------------------
   PROCEDURE disable_job(p_job_cd IN VARCHAR2) AS
   BEGIN
      -- Disable job given
      v_step := 10;
      dbms_scheduler.disable(NAME => get_job_name(p_job_cd));
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name || '.disable_job ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' at Step: ' || v_step,
                                        1,
                                        2000),
                                 TRUE);
   END disable_job;
   ----------------------------------------------------------------------------
   -- Procedure:   stop_job
   -- Arguments:   p_job_cd - name of the schedule
   --              p_force_bool
   -- Description: Procedure stops a job.
   ----------------------------------------------------------------------------
   PROCEDURE stop_job(p_job_cd     IN VARCHAR2,
                      p_force_bool IN BOOLEAN DEFAULT FALSE) AS
   BEGIN
      -- Disable job given
      v_step := 10;
      dbms_scheduler.stop_job(job_name => get_job_name(p_job_cd),
                              force    => p_force_bool);
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name || '.stop_job ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' at Step: ' || v_step,
                                        1,
                                        2000),
                                 TRUE);
   END;

   ----------------------------------------------------------------------------
   -- Procedure:   run_program
   -- Arguments:   p_job_cd - job cd of job
   --              p_program_name - name of procedure or calling block
   --
   -- Description: Procedure runs as a wrapper for plsql blocks or
   --              procedures run as part of an oracle job.
   ----------------------------------------------------------------------------
   PROCEDURE run_program(p_job_cd       IN VARCHAR2,
                         p_program_name IN VARCHAR2) AS

      plsql_block VARCHAR2(4000);

   BEGIN

      --  Log job has started running
      v_step := 10;
      log_job_start(p_job_cd);

      -- Run job or procedure specified
      v_step      := 20;
      plsql_block := 'BEGIN ' || p_program_name || '; EXCEPTION ' ||
                     ' WHEN OTHERS THEN ' || '   NULL; END;';

      EXECUTE IMMEDIATE plsql_block;

      -- Log whether job was successful or errored
      log_job_end(p_job_cd);

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := 'An error occured trying to run ' || p_program_name ||
                       chr(10) || 'Oracle Error: ' || v_err_code || ' ' ||
                       SQLERRM;
         -- Set job back to idle and return error message
         log_job_error(p_job_cd,
                       v_err_msg);
   END run_program;

   ----------------------------------------------------------------------------
   -- Procedure:   log_job_start
   -- Arguments:   p_name - name of the schedule
   -- Description: Procedure updates utl_job
   --              table columns start_time, last_start_dt
   --              with sysdate and status_cd with value 'PROCESSING'
   ----------------------------------------------------------------------------
   PROCEDURE log_job_start(p_name IN VARCHAR2) AS
      v_start_date DATE := current_timestamp;
   BEGIN
      UPDATE utl_job
         SET last_start_dt = v_start_date,
             status_cd     = gc_status_procing
       WHERE job_cd = p_name;
      COMMIT;
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name || '.log_job_start ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg,
                                        1,
                                        2000),
                                 TRUE);
   END log_job_start;

   ----------------------------------------------------------------------------
   -- Procedure:   log_job_end
   -- Arguments:   p_name - name of the schedule
   -- Description: Procedure updates utl_job
   --              table columns last_end_dt
   --              with sysdate and status_cd with value 'IDLE'
   ----------------------------------------------------------------------------
   PROCEDURE log_job_end(p_name IN VARCHAR2) AS
      v_end_date DATE := current_timestamp;
   BEGIN
      UPDATE utl_job
         SET last_success_end_dt = v_end_date,
             status_cd   = gc_status_idle,
             last_error  = NULL
       WHERE job_cd = p_name;
      COMMIT;
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name || '.log_job_end ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg,
                                        1,
                                        2000),
                                 TRUE);
   END log_job_end;

   ----------------------------------------------------------------------------
   -- Procedure:   log_job_error
   -- Arguments:   p_name - name of the schedule
   -- Description: Procedure updates utl_job
   --              table columns last_end_dt with current date, last_error with error message
   --              and status_cd with value 'IDLE'
   ----------------------------------------------------------------------------
   PROCEDURE log_job_error(p_name  IN VARCHAR2,
                           p_error IN VARCHAR2) AS
      v_end_date DATE := SYSDATE;
   BEGIN
      UPDATE utl_job
         SET last_error_end_dt = v_end_date,
             status_cd   = gc_status_idle,
             last_error  = p_error
       WHERE job_cd = p_name;
      COMMIT;
   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name || '.log_job_error ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg,
                                        1,
                                        2000),
                                 TRUE);
   END log_job_error;

   ----------------------------------------------------------------------------
   -- Function:    get_job_name
   -- Arguments:   p_job_cd - name of the job code
   --
   -- Returns: VARCHAR2
   --
   -- Description: This function will return a unique job name relevant
   --              to the schema the job is for.
   ----------------------------------------------------------------------------
   FUNCTION get_job_name(p_job_cd IN VARCHAR2) RETURN VARCHAR2 IS

      v_job_cd  VARCHAR2(20);
      v_user_id NUMBER;
      v_return  VARCHAR2(30);

   BEGIN
      -- Portion of job_cd should be cut down to 20 characters
      v_step := 10;
      SELECT substr(p_job_cd,
                    -20,
                    20)
        INTO v_job_cd
        FROM dual;

      -- get the user id
      v_step    := 20;
      v_user_id := sys_context('USERENV',
                               'SESSION_USERID');

      -- only take the last 10 digits if the number is larger than 10 digits
      IF length(v_user_id) > 10
      THEN
         v_return := v_job_cd || substr(v_user_id,
                                        -10,
                                        10);
      ELSE
         v_return := v_job_cd || v_user_id;
      END IF;

      RETURN v_return;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name || '.get_job_name ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' at Step: ' || v_step,
                                        1,
                                        2000),
                                 TRUE);
   END get_job_name;
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
      RETURN VARCHAR2 IS

      v_return   utl_job.repeat_expression%TYPE;
      v_byhour   VARCHAR2(20);
      v_byminute VARCHAR2(20);
   BEGIN

      IF p_repeat_expression IS NOT NULL
      THEN
         RETURN p_repeat_expression;
      ELSE
         IF p_start_time IS NOT NULL
         THEN
            SELECT substr(p_start_time,
                          1,
                          instr(p_start_time,
                                ':',
                                1) - 1) byhour,
                   substr(p_start_time,
                          instr(p_start_time,
                                ':',
                                1) + 1) byminute
              INTO v_byhour,
                   v_byminute
              FROM dual;

            v_return := 'FREQ=DAILY; BYHOUR=' || v_byhour || '; BYMINUTE=' ||
                        v_byminute || ';';
         ELSIF p_repeat_interval IS NOT NULL
         THEN
            v_return := 'FREQ=SECONDLY; INTERVAL=' || p_repeat_interval || ';';
         END IF;
      END IF;

      RETURN v_return;

   EXCEPTION
      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_job_admin_err,
                                 substr(c_pkg_name || '.get_repeat_expression ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg,
                                        1,
                                        2000),
                                 TRUE);
   END;
END jobs_admin_pkg;
/