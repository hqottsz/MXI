--liquibase formatted sql


--changeSet acor_hr_department_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_hr_department_v1
AS
SELECT
   org_hr.alt_id        AS user_id,
   org_work_dept.alt_id AS department_id,
   org_work_dept.desc_sdesc AS department
FROM
   org_hr
   INNER JOIN org_dept_hr ON
      org_hr.hr_db_id = org_dept_hr.hr_db_id AND
      org_hr.hr_id    = org_dept_hr.hr_id
   INNER JOIN org_work_dept ON
      org_dept_hr.dept_db_id = org_work_dept.dept_db_id AND
      org_dept_hr.dept_id    = org_work_dept.dept_id;            