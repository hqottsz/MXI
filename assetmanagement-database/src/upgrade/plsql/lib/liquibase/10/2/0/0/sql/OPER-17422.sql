--liquibase formatted sql
--changeSet OPER-17422:1 stripComments:false
--comment Removes any sensitivities on configuration slots that do not correspond to systems.

DELETE FROM eqp_assmbl_bom_sens
WHERE (eqp_assmbl_bom_sens.assmbl_db_id, eqp_assmbl_bom_sens.assmbl_cd, eqp_assmbl_bom_sens.assmbl_bom_id) IN (
   SELECT
      assmbl_db_id,
      assmbl_cd,
      assmbl_bom_id
   FROM eqp_assmbl_bom
   WHERE
      eqp_assmbl_bom.bom_class_cd != 'SYS'
  )
;