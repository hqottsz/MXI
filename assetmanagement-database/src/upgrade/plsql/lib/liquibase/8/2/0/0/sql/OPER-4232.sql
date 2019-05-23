--liquibase formatted sql


--changeSet OPER-4232:1 stripComments:false
-- Maintenix Job to generate Solr Index for advanced part search 
INSERT INTO utl_job (job_cd, job_name, start_time, start_delay, repeat_interval, active_bool, utl_id)
SELECT
  	'MX_CORE_GENERATE_PART_SEARCH_SOLR_INDEX', 'Generates the solr index for Advanced Part Search.', null, 300, 600, 0, 0
  FROM
  	dual
  WHERE
  	NOT EXISTS ( SELECT 1 FROM utl_job WHERE job_cd = 'MX_CORE_GENERATE_PART_SEARCH_SOLR_INDEX' );