--liquibase formatted sql

--changeSet OPER-9192:1 stripComments:false 
-- Delete mxsupplychain user
DELETE FROM org_hr WHERE user_id = 16;

DELETE FROM utl_user WHERE user_id = 16 AND username = 'mxsupplychain';