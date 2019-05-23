--liquibase formatted sql


--changeSet QC-6056:1 stripComments:false
-- migration script for QC-6056
-- remove rows from DB_TYPE_CONFIG_PARM that do not reference "action" config parms
delete from db_type_config_parm
where (parm_name,parm_type) in (
  select 
     db_type_config_parm.parm_name, 
     db_type_config_parm.parm_type 
  from 
     db_type_config_parm
  inner join utl_config_parm on 
     utl_config_parm.parm_name = db_type_config_parm.parm_name and
     utl_config_parm.parm_type = db_type_config_parm.parm_type 
  where
     not (
		-- these are the criteria for an action config parm
        utl_config_parm.parm_name like 'ACTION%' and 
        utl_config_parm.parm_type='SECURED_RESOURCE' and 
        utl_config_parm.config_type='USER'
     )
)
;