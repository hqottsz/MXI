--liquibase formatted sql


--changeSet utl_job:1 stripComments:false
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

--changeSet utl_job:2 stripComments:false
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

--changeSet utl_job:3 stripComments:false
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