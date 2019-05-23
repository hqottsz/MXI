-- update UTL_JOB to set MX_COMMON_CACHE_RESET start delay to 10 sec
UPDATE utl_job
SET start_delay = 10
WHERE
job_cd = 'MX_COMMON_CACHE_RESET';

-- update UTL_JOB to set MX_COMMON_CACHE_RESET interval to 10 sec
UPDATE utl_job
SET repeat_interval = 10
WHERE
job_cd = 'MX_COMMON_CACHE_RESET';