--liquibase formatted sql


--changeSet acor_delay_v1:1 stripComments:false
CREATE OR REPLACE VIEW acor_delay_v1
AS
SELECT
   delay_code_cd                                                       AS delay_code,
   desc_sdesc                                                          AS delay_name,
   desc_ldesc                                                          AS delay_description,
   decode(tech_delay_bool,1, 'TECHNICAL DELAY', 'NON-TECHNICAL_DELAY') AS delay_type_code,
   tech_delay_bool
FROM
   ref_delay_code;   