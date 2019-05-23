--liquibase formatted sql
  

--changeSet DEV-1957:1 stripComments:false
INSERT INTO 
UTL_JOB ( job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id )
SELECT 'MX_CORE_GENERATE_SOLR_INDEX', 'Generates the solr index for Advanced Search feature.', null, 30, 300, 0, 0
FROM
	dual
WHERE
  NOT EXISTS ( SELECT 1 FROM utl_job WHERE job_cd = 'MX_CORE_GENERATE_SOLR_INDEX' );