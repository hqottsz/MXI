--liquibase formatted sql

--changeSet OPER-18659:1 stripComments:false
-- Replicate all SYS config slot ETOPS/RVSM Significance as system sensitivities
INSERT INTO
   eqp_assmbl_bom_sens
   (
      assmbl_db_id,
      assmbl_cd,
      assmbl_bom_id,
      sensitivity_cd
   )
   -- Get all ETOPS sensitivities to be added
   SELECT
      eqp_assmbl_bom.assmbl_db_id,
      eqp_assmbl_bom.assmbl_cd,
      eqp_assmbl_bom.assmbl_bom_id,
      'ETOPS' AS sensitivity_cd
   FROM
      eqp_assmbl_bom
      LEFT JOIN eqp_assmbl_bom_sens ON
         eqp_assmbl_bom_sens.assmbl_db_id   = eqp_assmbl_bom.assmbl_db_id AND
         eqp_assmbl_bom_sens.assmbl_cd      = eqp_assmbl_bom.assmbl_cd AND
         eqp_assmbl_bom_sens.assmbl_bom_id  = eqp_assmbl_bom.assmbl_bom_id AND
         eqp_assmbl_bom_sens.sensitivity_cd = 'ETOPS'
   WHERE
      -- Sensitivity isn't already configured
      eqp_assmbl_bom_sens.sensitivity_cd IS NULL
      AND
      -- Config slot is a system
      eqp_assmbl_bom.bom_class_db_id = 0 AND
      eqp_assmbl_bom.bom_class_cd    = 'SYS'
      AND
      -- System is ETOPS significant
      eqp_assmbl_bom.etops_bool = 1
   UNION ALL
   -- Get all RVSM sensitivities to be added
   SELECT
      eqp_assmbl_bom.assmbl_db_id,
      eqp_assmbl_bom.assmbl_cd,
      eqp_assmbl_bom.assmbl_bom_id,
      'RVSM' AS sensitivity_cd
   FROM
      eqp_assmbl_bom
      LEFT JOIN eqp_assmbl_bom_sens ON
         eqp_assmbl_bom_sens.assmbl_db_id   = eqp_assmbl_bom.assmbl_db_id AND
         eqp_assmbl_bom_sens.assmbl_cd      = eqp_assmbl_bom.assmbl_cd AND
         eqp_assmbl_bom_sens.assmbl_bom_id  = eqp_assmbl_bom.assmbl_bom_id AND
         eqp_assmbl_bom_sens.sensitivity_cd = 'RVSM'
   WHERE
      -- Sensitivity isn't already configured
      eqp_assmbl_bom_sens.sensitivity_cd IS NULL
      AND
      -- Config slot is a system
      eqp_assmbl_bom.bom_class_db_id = 0 AND
      eqp_assmbl_bom.bom_class_cd    = 'SYS'
      AND
      -- System is RVSM significant
      eqp_assmbl_bom.rvsm_bool = 1;
