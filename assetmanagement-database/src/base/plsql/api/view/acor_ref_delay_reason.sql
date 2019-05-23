--liquibase formatted sql


--changeSet acor_ref_delay_reason:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_ref_delay_reason
AS
select
   delay_code_cd as delay_type_code,
   desc_sdesc as delay_type_name,
   desc_ldesc as delay_type_description,
   tech_delay_bool as technical_delay
from
   ref_delay_code
;