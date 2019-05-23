--liquibase formatted sql


--changeSet acor_ref_doc_defn_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_ref_doc_defn_v1
AS 
WITH rvw_org_hr
AS
   (
     SELECT
        hr_db_id,
        hr_id,
        hr_cd,
        username
     FROM
        org_hr
        INNER JOIN utl_user ON
           org_hr.user_id = utl_user.user_id
   )
SELECT
   task_task.alt_id                       AS task_id,
   task_defn.alt_id                       AS defn_id,
   task_task.task_cd                      AS task_code,
   task_task.task_name,
   eqp_assmbl_bom.alt_id                  AS bom_id,
   eqp_assmbl_bom.assmbl_cd               AS assembly_code,
   eqp_assmbl_bom.assmbl_bom_cd           AS config_slot_code,
   eqp_assmbl_bom.bom_class_cd            AS config_slot_class_code,
   eqp_assmbl.assmbl_class_cd             AS assembly_class_code,
   eqp_assmbl_bom.logcard_form_cd         AS logcard_form_code,
   ref_task_class.task_class_cd           AS task_class_code,
   task_task.task_subclass_cd             AS task_subclass_code,
   ref_task_subclass.user_subclass_cd     AS user_subclass_code,
   task_task.task_ldesc                   AS task_description,
   task_task.task_appl_ldesc              AS applicability_description,
   task_task.task_appl_eff_ldesc          AS applicability_range,
   task_task.task_originator_cd           AS originator_code,
   task_task.task_ref_sdesc               AS reference_description,
   received_by.username                   AS received_by,
   CASE task_ref_doc.task_def_issue_by_cd
      WHEN 'MANUFACT' THEN
           task_ref_doc.Manufact_Cd
      WHEN 'REGBODY' THEN
           task_ref_doc.reg_body_cd
      WHEN 'OPER' THEN
           OPERATOR.operator_code
   END                                    AS issued_by,
   task_ref_doc.receive_dt                AS receive_date,
   task_ref_doc.issue_dt                  AS issue_date,
   task_ref_doc.amend_ref_sdesc           AS amend_reference_description,
   task_task.engineering_ldesc            AS engineering_description,
   task_task.task_def_status_cd           AS status_code,
   task_task.revision_ord                 AS revision_order,
   task_task.ext_key_sdesc                AS external_key_description,
   org_org.org_cd                         AS organization_code,
   disposition_by.username                AS disposition_by,
   task_ref_doc.disposition_dt            AS disposition_date,
   task_ref_doc.task_def_disposition_cd   AS disposition_code,
   task_ref_doc.disposition_ldesc         AS disposition_description,
   task_task.actv_ldesc                   AS activation_description,
   task_task.actv_ref_sdesc               AS activation_reference
FROM
   task_task
   -- definition
   INNER JOIN task_defn ON
      task_task.task_defn_db_id = task_defn.task_defn_db_id AND
      task_task.task_defn_id    = task_defn.task_defn_id
   -- task class
   INNER JOIN ref_task_class ON
      task_task.task_class_db_id = ref_task_class.task_class_db_id AND
      task_task.task_class_cd    = ref_task_class.task_class_cd
   LEFT JOIN ref_task_subclass ON
      task_task.task_class_db_id = ref_task_subclass.task_class_db_id AND
      task_task.task_class_cd    = ref_task_subclass.task_class_cd  AND
      task_task.task_subclass_db_id = ref_task_subclass.task_subclass_db_id AND
      task_task.task_subclass_cd    = ref_task_subclass.task_subclass_cd
   -- assembly
   INNER JOIN eqp_assmbl_bom ON
      task_task.assmbl_db_Id  = eqp_assmbl_bom.assmbl_db_Id AND
      task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    AND
      task_task.assmbl_bom_Id = eqp_assmbl_bom.assmbl_bom_Id
   INNER JOIN eqp_assmbl ON
      eqp_assmbl_bom.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
      eqp_assmbl_bom.assmbl_cd    = eqp_assmbl.assmbl_cd
   -- task ref doc
   INNER JOIN task_ref_doc ON
      task_task.task_db_id = task_ref_doc.task_db_id AND
      task_task.task_id    = task_ref_doc.task_id
   -- received by
   LEFT JOIN rvw_org_hr received_by ON
      task_ref_doc.receive_by_hr_db_id = received_by.hr_db_Id AND
      task_ref_doc.receive_by_hr_id    = received_by.hr_id
   -- disposition by
   LEFT JOIN rvw_org_hr disposition_by ON
      task_ref_doc.disposition_hr_db_id = disposition_by.hr_db_Id AND
      task_ref_doc.disposition_hr_id    = disposition_by.hr_id
   -- organization
   INNER JOIN org_org ON
      task_task.Org_Db_Id = org_org.Org_Db_Id AND
      task_task.Org_Id    = org_org.org_id
   -- operator
   LEFT JOIN org_carrier ON
      task_ref_doc.carrier_db_id = org_carrier.carrier_db_id AND
      task_ref_doc.carrier_id    = org_carrier.carrier_id
   LEFT JOIN acor_operator_v1 OPERATOR ON
      org_carrier.alt_id    = OPERATOR.operator_id
WHERE
   class_mode_cd = 'REF'
;