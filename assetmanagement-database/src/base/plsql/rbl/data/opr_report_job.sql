--liquibase formatted sql


--changeSet opr_report_job:1 stripComments:false
-- insert extract job
INSERT
INTO
   opr_report_job
   (
     job_code,
     job_description,
     program_name,
     frequency_code,
     interval,
     schedule_date
   )
SELECT
   'MX_RELIABILITY_EXTRACTION',
   'Job that will execute the reliability extraction program',
   'BEGIN opr_rbl_utility_pkg.opr_rbl_extract_proc; END;',
   'DAILY',
   1,
   TRUNC(sysdate+1)-(1/(24*60*60))
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report_job
                WHERE
                   job_code = 'MX_RELIABILITY_EXTRACTION'
              );

--changeSet opr_report_job:2 stripComments:false
-- insert transform job
INSERT
INTO
   opr_report_job
   (
     job_code,
     job_description,
     program_name,
     frequency_code,
     interval,
     schedule_date
   )
SELECT
   'MX_RELIABILITY_TRANSFORMATION',
   'Job that will execute the reliability transformation program',
   'BEGIN opr_rbl_utility_pkg.opr_rbl_transform_proc; END;',
   'MONTHLY',
   1,
   (LAST_DAY(TRUNC(SYSDATE))+1) - (1/(24*60*60))
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report_job
                WHERE
                   job_code = 'MX_RELIABILITY_TRANSFORMATION'
              );              

--changeSet opr_report_job:3 stripComments:false
-- insert refresh operator materialized view  job
INSERT
INTO
   opr_report_job
   (
     job_code,
     job_description,
     program_name,
     frequency_code,
     interval,
     schedule_date
   )
SELECT
   'MX_ACOR_MVIEW_24HOUR_INTERVAL',
   'Job that will refresh ACOR materialized views daily (MIDNIGHT)',
   'DECLARE ln_return NUMBER; BEGIN ln_return := opr_rbl_utility_pkg.opr_refresh_mview(''DAILY'',''FREQUENCY''); END;',
   'DAILY',
   1,
   TRUNC(SYSDATE)+1 - (1/(24*60*60))
FROM
   dual
WHERE
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_report_job
                WHERE
                   job_code = 'MX_ACOR_MVIEW_24HOUR_INTERVAL'
              );              