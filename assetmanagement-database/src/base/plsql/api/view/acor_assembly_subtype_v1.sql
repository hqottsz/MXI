--liquibase formatted sql


--changeSet acor_assembly_subtype_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_assembly_subtype_v1 
AS
SELECT
      assmbl_cd       AS assembly_code,
      subtype_cd      AS subtype_code,
      desc_sdesc      AS description,
      rstat_cd        AS rstat_code
FROM
      eqp_assmbl_subtype
;         