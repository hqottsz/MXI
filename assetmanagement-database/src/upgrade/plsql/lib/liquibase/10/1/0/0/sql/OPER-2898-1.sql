--liquibase formatted sql

--changeset OPER-2898-1:1 stripComments:false
-- change oper_ord from 25 to 26 for 0:OPEN
UPDATE
   ref_inv_oper
SET
   oper_ord = 26
WHERE
   inv_oper_db_id = 0 AND
   inv_oper_cd    = 'OPEN';