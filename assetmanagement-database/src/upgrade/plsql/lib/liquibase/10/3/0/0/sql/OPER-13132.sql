--liquibase formatted sql

--changeSet OPER-13132:1 stripComments:false
DELETE FROM utl_job WHERE job_cd = 'MX_COMP_MVIEW_REFRESH';

--changeSet OPER-13132:2 stripComments:false
UPDATE utl_job SET start_time = '3:20', repeat_interval = '86400', start_delay='' WHERE job_cd = 'MX_REFRESH_MAT_COMP_AD_VIEWS';
