--liquibase formatted sql


--changeSet acor_reg_body_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_reg_body_v1
AS
SELECT
   --reg_body_db_id AS regulatory_body_db_id,
   reg_body_cd    AS regulatory_body_code,
   desc_sdesc     AS regulatory_body_name,
   desc_ldesc     AS regulatory_body_description,
   rstat_cd       AS active_bool
FROM
  ref_reg_body
;