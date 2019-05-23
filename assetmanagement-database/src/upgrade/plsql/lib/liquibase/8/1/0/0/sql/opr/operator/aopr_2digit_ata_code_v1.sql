--liquibase formatted sql


--changeSet aopr_2digit_ata_code_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_2digit_ata_code_v1
AS
SELECT
   ID,
   assembly_code,
   config_slot_code,
   config_slot_name
FROM
   (
     SELECT
        ID,
        assembly_code,
        SUBSTR(config_slot_code,1,2) config_slot_code,
        config_slot_name,
        ROW_NUMBER() OVER (PARTITION BY assembly_code, SUBSTR(config_slot_code,1,2) ORDER BY assembly_code, config_slot_code) rn
     FROM
        acor_assembly_bom_v1
     WHERE
        LENGTH(config_slot_code) = 2
        OR
        (
          LENGTH(config_slot_code) = 5 AND
          SUBSTR(config_slot_code,-2) = '00'
        )
    )
WHERE
   rn = 1 
;      