--liquibase formatted sql


--changeSet utl_job:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- try to drop the job if exist
DECLARE
  
   CURSOR lcur_jobs IS
   SELECT
      job_cd AS job_code
   FROM 
      utl_job
   WHERE
      job_type = 'ORACLE'
      AND
      job_cd IN ('MX_RELIABILITY_EXTRACTION',
                 'MX_RELIABILITY_TRANSFORMATION',
                 'MX_ACOR_MVIEW_24HOUR_INTERVAL'
                );
BEGIN                
 
   EXECUTE IMMEDIATE 'ALTER TRIGGER taur_utl_job DISABLE';
   FOR lrec_jobs IN lcur_jobs LOOP
   
      BEGIN
         -- drop the schedule
        DBMS_SCHEDULER.drop_schedule( schedule_name => lrec_jobs.job_code,
                                      force         => TRUE
                                    );
                                    
      EXCEPTION
        -- ignore if does not exist
        WHEN OTHERS THEN
           NULL; 
      END;
      
      --
      BEGIN
        -- drop the job                            
        DBMS_SCHEDULER.drop_job( job_name => jobs_admin_pkg.get_job_name(lrec_jobs.job_code),
                                 force    => TRUE
                               );     
                            
      EXCEPTION
        -- ignore if does not exist
        WHEN OTHERS THEN
           NULL; 
      END;


      DELETE
      FROM 
        utl_job
      WHERE
        job_cd = lrec_jobs.job_code;
                                                
   END LOOP;
   
   EXECUTE IMMEDIATE 'ALTER TRIGGER taur_utl_job ENABLE';
                             
END;
/   

--changeSet utl_job:2 stripComments:false
-- insert extract job
INSERT 
INTO 
  utl_job 
   (
     job_cd, 
     job_name, 
     start_time, 
     start_delay, 
     repeat_interval, 
     active_bool, 
     utl_id
   )
SELECT
   'MX_RELIABILITY_EXTRACTION',
   'Job that will execute the reliability extraction program',
   null,
   0,
   120,
   0,
   0 
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   utl_job
                WHERE
                   job_cd = 'MX_RELIABILITY_EXTRACTION'
              );   

--changeSet utl_job:3 stripComments:false
-- insert transform job
INSERT
INTO
   utl_job
   (
     job_cd,
     job_name,
     start_time, 
     start_delay, 
     repeat_interval, 
     active_bool, 
     utl_id
   )
SELECT
   'MX_RELIABILITY_TRANSFORMATION',
   'Job that will execute the reliability transformation program',
   null,
   0,
   120,
   0,
   0
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   utl_job
                WHERE
                   job_cd = 'MX_RELIABILITY_TRANSFORMATION'
              );                               

--changeSet utl_job:4 stripComments:false
-- insert mview refresh job
INSERT
INTO
   utl_job
   (
     job_cd,
     job_name,
     start_time, 
     start_delay, 
     repeat_interval, 
     active_bool, 
     utl_id
   )
SELECT
   'MX_ACOR_MVIEW_24HOUR_INTERVAL',
   'Job that will refresh operator materialized views',
   null,
   0,
   120,
   0,
   0
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   utl_job
                WHERE
                   job_cd = 'MX_ACOR_MVIEW_24HOUR_INTERVAL'
              );   