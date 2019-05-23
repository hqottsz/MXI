--liquibase formatted sql


--changeSet aopr_hr_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_hr_v1
AS
SELECT
   acor_hr_user_v1.user_id,
   username,
   (last_name ||', ' || first_name) NAME,
   departments,
   licenses,
   skills,
   po_autho_level
FROM
  acor_hr_user_v1
  LEFT JOIN (
              SELECT
                 user_id,
                 LISTAGG(department, ',') WITHIN GROUP (ORDER BY department) departments
              FROM
                 acor_hr_department_v1
              GROUP BY
                user_id
            ) dept ON
     acor_hr_user_v1.user_id = dept.user_id
  LEFT JOIN (
              SELECT
                 user_id,
                 LISTAGG(license_tag, ',') WITHIN GROUP (ORDER BY license_tag) licenses
              FROM
                 acor_hr_license_v1
             GROUP BY
                user_id
            ) license ON
     acor_hr_user_v1.user_id = license.user_id
  LEFT JOIN (
              SELECT
                 user_id,
                 LISTAGG(labour_skill_code, ',') WITHIN GROUP (ORDER BY labour_skill_code) skills
              FROM
                 acor_hr_skill_v1
              GROUP BY
                user_id
            ) skill ON
     acor_hr_user_v1.user_id = skill.user_id
  LEFT JOIN (
              SELECT
                 user_id,
                 LISTAGG(po_autho_level_code, ',') WITHIN GROUP (ORDER BY po_autho_level_code) po_autho_level
              FROM
                 acor_hr_po_auth_level_v1
              GROUP BY
                user_id
            ) approval ON
     acor_hr_user_v1.user_id = approval.user_id
;