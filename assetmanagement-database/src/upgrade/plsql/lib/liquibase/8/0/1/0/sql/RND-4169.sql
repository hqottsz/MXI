--liquibase formatted sql


--changeSet RND-4169:1 stripComments:false
DELETE FROM utl_job WHERE job_cd = 'MX_CORE_GERONIMO_GENERATE_WORK_ITEMS';