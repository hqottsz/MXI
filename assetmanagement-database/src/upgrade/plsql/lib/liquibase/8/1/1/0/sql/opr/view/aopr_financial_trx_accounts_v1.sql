--liquibase formatted sql


--changeSet aopr_financial_trx_accounts_v1:1 stripComments:false
CREATE OR REPLACE FORCE VIEW aopr_financial_trx_accounts_v1
AS 
SELECT
   credit_cost,
   debit_cost,
   account_type_code,
   account_code,
   account_description,
   account_id,
   transaction_id
FROM
   ACOR_FINANCIAL_TRX_ACCOUNTS_V1
;