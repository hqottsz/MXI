--liquibase formatted sql


--changeSet MX-24523:1 stripComments:false
DELETE FROM utl_job WHERE job_cd = 'MX_COMMON_PLSQL_LOAD';