--liquibase formatted sql


--changeSet aopr_comp_ad_compl_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_comp_ad_compl_v1
AS
SELECT
   task_id,
   task_code,
   task_name,
   config_slot_code,
   task_class_code,
   task_subclass_code,
   task_description,
   originator_code,
   reference_description,
   received_by,
   issued_by,
   receive_date,
   issue_date,
   amend_reference_description
FROM
   acor_ref_doc_defn_v1
WHERE
   task_class_code  = 'AD'
   AND
  -- bom_class_db_id  = 0 AND
   config_slot_code     = 'TRK'
   AND -- logform was used to identify assembly
   logcard_form_code IS NULL
;