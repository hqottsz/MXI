--liquibase formatted sql


--changeSet acor_financial_trx_accounts_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW acor_financial_trx_accounts_v1
AS
SELECT
   xacct.credit_cost,
   xacct.debit_cost,
   acct.account_type_cd AS account_type_code,
   acct.account_cd AS account_code,
   acct.account_sdesc AS account_description,
   acct.alt_id AS account_id,
   log.alt_id AS transaction_id
FROM fnc_xaction_account xacct
   INNER JOIN fnc_account acct ON
      xacct.account_db_id = acct.account_db_id AND
      xacct.account_id    = acct.account_id
   INNER JOIN fnc_xaction_log log ON
      log.xaction_db_id = xacct.xaction_db_id AND
      log.xaction_id    = xacct.xaction_id
   LEFT JOIN ref_currency ON
      log.currency_db_id = ref_currency.currency_db_id AND
      log.currency_cd    = ref_currency.currency_cd
;