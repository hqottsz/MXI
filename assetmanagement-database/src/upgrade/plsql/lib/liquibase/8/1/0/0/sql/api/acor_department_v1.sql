--liquibase formatted sql


--changeSet acor_department_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_department_v1
AS 
SELECT
   alt_id           AS department_id,
   desc_sdesc       AS department,
   desc_ldesc       AS department_name,
   dept_type_cd     AS department_type
FROM
   org_work_dept;      