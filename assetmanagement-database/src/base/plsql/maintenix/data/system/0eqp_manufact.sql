--liquibase formatted sql


--changeSet 0eqp_manufact:1 stripComments:false
-- INSERT SCRIPT FOR TABLE EQP_MANUFACT
insert into eqp_manufact (manufact_db_id, manufact_cd, manufact_name, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0, 'LOCAL','LOCAL',0, '11-DEC-2007', '11-DEC-2007', 0, 'MXI');