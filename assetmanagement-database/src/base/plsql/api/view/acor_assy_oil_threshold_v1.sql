--liquibase formatted sql


--changeSet acor_assy_oil_threshold_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_assy_oil_threshold_v1
AS
SELECT
   eqp_assmbl.alt_id     AS assmbl_id,
   eqp_assmbl.assmbl_cd  AS assmbl_code,
   threshold_qt          AS threshold_quantity,
   oil_status_cd         AS oil_status
FROM
   eqp_oil_threshold_assmbl
   INNER JOIN eqp_assmbl ON
      eqp_oil_threshold_assmbl.assmbl_db_id = eqp_assmbl.assmbl_db_id AND
      eqp_oil_threshold_assmbl.assmbl_cd    = eqp_assmbl.assmbl_cd
;      