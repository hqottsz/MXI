--liquibase formatted sql


--changeSet aopr_ref_doc_defn_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_ref_doc_defn_v1
AS 
SELECT
   ref_doc.task_id,
   ref_doc.task_code,
   ref_doc.task_name,
   ref_doc.assembly_code,
   ref_doc.bom_id,
   ref_doc.config_slot_code,
   ref_doc.task_class_code,
   ref_doc.task_subclass_code,
   ref_doc.task_description,
   ref_doc.applicability_description,
   ref_doc.applicability_range,
   ref_doc.originator_code,
   ref_doc.reference_description,
   ref_doc.received_by,
   ref_doc.issued_by,
   ref_doc.receive_date,
   ref_doc.issue_date,
   ref_doc.amend_reference_description,
   ref_doc.engineering_description,
   ref_doc.status_code,
   ref_doc.revision_order,
   ref_doc.external_key_description,
   ref_doc.organization_code,
   ref_doc.disposition_by,
   ref_doc.disposition_date,
   ref_doc.disposition_code,
   ref_doc.disposition_description,
   ref_doc.activation_description,
   ref_doc.activation_reference,
   CASE
      WHEN ref_doc.task_class_code = 'AMP' THEN
        ietm_topic_sdesc
   END technical_ref_list
FROM
   acor_ref_doc_defn_v1 ref_doc
   LEFT JOIN (
               SELECT
                   task_id,
                   LISTAGG(topic_description,chr(13)) within GROUP ( ORDER BY task_id) ietm_topic_sdesc
               FROM
                  acor_task_defn_ietm_v1
               WHERE
                  technical_manual_code = 'AMPREF'
               GROUP BY
                  task_id
             ) ampref_task_ietm ON
      ref_doc.task_id    = ampref_task_ietm.task_id
;