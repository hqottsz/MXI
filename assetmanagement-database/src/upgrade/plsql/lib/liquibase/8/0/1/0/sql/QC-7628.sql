--liquibase formatted sql


--changeSet QC-7628:1 stripComments:false
-- migration to update the start_delay to stagger job at the end of other startup jobs and repeat_interval to 1 hour
UPDATE utl_job 
SET start_delay = 300, repeat_interval = 3600 
WHERE job_cd = 'MX_CORE_GENERATE_SOLR_INDEX';