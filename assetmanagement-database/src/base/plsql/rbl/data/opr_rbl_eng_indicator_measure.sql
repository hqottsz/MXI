--liquibase formatted sql


--changeSet opr_rbl_eng_indicator_measure:1 stripComments:false
-- engine flight measurement indicator
-- 
INSERT 
INTO
   opr_rbl_eng_indicator_measure
   (
     data_type_id,
     data_type_code,
     data_type_description
   )
SELECT
   alt_id,
   data_type_cd,
   'Full Power T/O'
FROM
   mim_data_type
WHERE
   -- equivalent for rejected t/o
   data_type_cd = 'FPTAKEOFF'
   AND
   NOT EXISTS (
                SELECT
                   1
                FROM
                   opr_rbl_eng_indicator_measure measurement
                WHERE
                   measurement.data_type_id    = mim_data_type.alt_id
              );                        