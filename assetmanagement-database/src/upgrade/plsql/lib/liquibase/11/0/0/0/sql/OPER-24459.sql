--liquibase formatted sql
--Add the new separated user password hashing and config parameter encryption jobs

--changeSet OPER-24459:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   -- Split job into two using existing scheduling
   FOR c_cur_job IN (SELECT * FROM utl_job WHERE job_cd = 'MX_COMMON_DATA_ENCRYPT')
   LOOP
      -- UserPasswordHashingJob
      INSERT INTO UTL_JOB(job_cd,
                         job_name,
                         start_time,
                         start_delay,
                         repeat_interval,
                         active_bool,
                         status_cd,
                         schedule_date,
                         last_start_dt,
                         last_success_end_dt,
                         last_error_end_dt,
                         last_error,
                         utl_id,
                         job_type,
                         program_name,
                         program_type,
                         repeat_expression)
      VALUES('MX_CORE_USER_PASS_HASH',
             'Generate hashes for user sql authentication passwords',
             c_cur_job.start_time,
             c_cur_job.start_delay,
             c_cur_job.repeat_interval,
             c_cur_job.active_bool,
             'IDLE',
             c_cur_job.schedule_date,
             NULL,
             NULL,
             NULL,
             NULL,
             0,
             'MAINTENIX',
             NULL,
             NULL,
             NULL);
             
      -- ConfigParmEncryptionJob
      INSERT INTO UTL_JOB(job_cd,
                         job_name,
                         start_time,
                         start_delay,
                         repeat_interval,
                         active_bool,
                         status_cd,
                         schedule_date,
                         last_start_dt,
                         last_success_end_dt,
                         last_error_end_dt,
                         last_error,
                         utl_id,
                         job_type,
                         program_name,
                         program_type,
                         repeat_expression)
      VALUES('MX_CORE_CONFIG_PARM_ENCRYPT',
             'Encrypt sensitive configuration parameter values',
             c_cur_job.start_time,
             c_cur_job.start_delay,
             c_cur_job.repeat_interval,
             c_cur_job.active_bool,
             'IDLE',
             c_cur_job.schedule_date,
             NULL,
             NULL,
             NULL,
             NULL,
             0,
             'MAINTENIX',
             NULL,
             NULL,
             NULL);
      -- Remove existing job
      DELETE FROM utl_job WHERE job_cd = 'MX_COMMON_DATA_ENCRYPT';
    END LOOP;
END;
/
