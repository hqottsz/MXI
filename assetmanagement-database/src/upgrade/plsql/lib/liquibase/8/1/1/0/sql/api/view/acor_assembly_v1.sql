--liquibase formatted sql


--changeSet acor_assembly_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_assembly_v1
AS 
SELECT
   eqp_assmbl.alt_id             AS assembly_id,
   assmbl_cd                     AS assembly_code,
   assmbl_class_cd               AS assembly_class_code,
   logbook_type_cd               AS logbook_type_code,
   bitmap_tag,
   assmbl_name,
   assmbl_mdesc                  AS assmbl_description,
   prog_ver_ldesc                AS prog_version_description,
   org_authority.alt_id          AS authority_id,
   org_authority.authority_cd    AS authority_code
FROM
   eqp_assmbl
   LEFT JOIN org_authority ON
      eqp_assmbl.authority_db_id = org_authority.authority_db_id AND
      eqp_assmbl.authority_id    = org_authority.authority_id
;