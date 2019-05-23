--liquibase formatted sql


--changeSet aopr_acft_ad_compl_eo_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_acft_ad_compl_eo_v1
AS
SELECT
   ref_docs_compl_link.task_id,
   ref_docs_compl_link.task_code,
   ref_docs_compl_link.task_class_code,
   --
   dep_task_id,
   dep_task_code,
   dep_task_class_code
FROM
   aopr_acft_ad_compl_v1 acft_ad_compl
   INNER JOIN acor_ref_docs_compl_link_v1 ref_docs_compl_link ON
      acft_ad_compl.task_id    = ref_docs_compl_link.dep_task_id
WHERE
   ref_docs_compl_link.task_class_mode = 'REQ'
   AND
   ref_docs_compl_link.dep_task_class_code = 'AD'
;