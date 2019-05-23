--liquibase formatted sql


--changeSet SMOS-30_002:1 stripComments:false
UPDATE opr_sb_status SET display_ord = 6 WHERE sb_status_cd = 'REJECTED';