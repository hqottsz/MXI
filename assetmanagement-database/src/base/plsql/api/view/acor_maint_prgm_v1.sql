--liquibase formatted sql


--changeSet acor_maint_prgm_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_maint_prgm_v1
AS
SELECT
   maint_prgm.alt_id                  AS maint_prgm_id,
   maint_prgm.maint_prgm_cd           AS maintenance_program_code,
   maint_prgm.maint_prgm_sdesc        AS description,
   maint_prgm_defn.assmbl_cd          AS assembly_code,
   org_carrier.alt_id                 AS operator_id,
   org_carrier.carrier_cd             AS operator_code,
   maint_prgm.revision_ord            AS revision_order,
   maint_prgm_defn.last_revision_ord  AS last_revision_order,
   maint_prgm.maint_prgm_status_cd    AS  status_code
FROM
   maint_prgm_defn
   INNER JOIN maint_prgm ON
      maint_prgm_defn.maint_prgm_defn_db_id = maint_prgm.maint_prgm_defn_db_id AND
      maint_prgm_defn.maint_prgm_defn_id    = maint_prgm.maint_prgm_defn_id
   INNER JOIN maint_prgm_carrier_map ON
      maint_prgm.maint_prgm_db_id = maint_prgm_carrier_map.maint_prgm_db_id AND
      maint_prgm.maint_prgm_id    = maint_prgm_carrier_map.maint_prgm_id
   INNER JOIN org_carrier ON
      maint_prgm_carrier_map.carrier_db_id = org_carrier.carrier_db_id AND
      maint_prgm_carrier_map.carrier_id    = org_carrier.carrier_id
;