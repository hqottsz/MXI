--liquibase formatted sql


--changeSet acor_accounts_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_accounts_v1
AS
SELECT
   fnc_account.alt_id               AS ID,
   fnc_account.account_cd,
   fnc_account.account_type_cd      AS account_type_code,
   fnc_account.account_sdesc        AS account_name,
   fnc_account.account_ldesc        AS account_description,
   fnc_account.default_bool         AS default_flag,
   fnc_account.closed_bool          AS closed_flag,
   fnc_account.ext_key_sdesc        AS external_key_description,
   fnc_tcode.tcode_cd               AS tcode
FROM
   fnc_account
   INNER JOIN ref_account_type ON
      fnc_account.account_type_db_id = ref_account_type.account_type_db_id AND
      fnc_account.account_type_cd    = ref_account_type.account_type_cd
   LEFT JOIN fnc_tcode ON
      fnc_tcode.tcode_db_id = fnc_account.tcode_db_id AND
      fnc_tcode.tcode_id 	 = fnc_account.tcode_id
;