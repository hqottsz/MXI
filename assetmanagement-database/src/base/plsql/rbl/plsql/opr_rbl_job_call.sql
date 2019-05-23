--liquibase formatted sql


--changeSet opr_rbl_job_call:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE PROCEDURE opr_rbl_job_call  (
   aiv_job_code  IN  utl_job.job_cd%TYPE
)
IS

   lrec_report_job     opr_report_job%ROWTYPE;
   lv_job_name         opr_report_job.job_code%TYPE;

BEGIN

   -- get job info
   lrec_report_job := opr_rbl_utility_pkg.opr_get_report_job(aiv_job_code);

   IF lrec_report_job.job_code IS NOT NULL AND
      lrec_report_job.program_name IS NOT NULL AND
      NVL(lrec_report_job.next_run_date,lrec_report_job.schedule_date) < SYSDATE THEN

      -- construct the job name
      -- appending RBL and date/time to the last 15 characters of the job code
      lv_job_name := 'RBL'||TO_CHAR(NVL(lrec_report_job.next_run_date,lrec_report_job.schedule_date),'YYYYMMDDHHMI') || SUBSTR(REPLACE(lrec_report_job.job_code,'_'),-15);

      -- check if the job is currently running
      IF NOT opr_rbl_utility_pkg.opr_check_running_job(lv_job_name) THEN

         -- start log
         opr_rbl_utility_pkg.opr_report_job_log( aiv_job_code        => lrec_report_job.job_code,
                                                 aiv_exec_stage_code => 'START'
                                               );


         -- execute the program
         DBMS_SCHEDULER.CREATE_JOB( job_name   => lv_job_name,
                                    job_type   => 'PLSQL_BLOCK',
                                    job_action => lrec_report_job.program_name,
                                    enabled    => TRUE,
                                    comments   => lrec_report_job.job_description
                                   );


         -- check and wait for the job to finished then log
         DBMS_LOCK.sleep(120);
         --
         WHILE opr_rbl_utility_pkg.opr_check_running_job(lv_job_name) LOOP
             -- sleep while waiting
             DBMS_LOCK.sleep(30);
         END LOOP;

         -- end log
         opr_rbl_utility_pkg.opr_report_job_log( aiv_job_code        => lrec_report_job.job_code,
                                                 aiv_exec_stage_code => 'END',
                                                 aiv_frequency_code  => lrec_report_job.frequency_code,
                                                 ain_interval        => lrec_report_job.INTERVAL
                                               );
      END IF;

   END IF;

END opr_rbl_job_call;
/