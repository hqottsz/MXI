--liquibase formatted sql


--changeSet aopr_comp_ad_compl_sb_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_comp_ad_compl_sb_v1
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
   aopr_comp_ad_compl_v1 comp_ad_compl
   INNER JOIN acor_ref_docs_compl_link_v1 ref_docs_compl_link ON
      comp_ad_compl.task_id    = ref_docs_compl_link.dep_task_id
;