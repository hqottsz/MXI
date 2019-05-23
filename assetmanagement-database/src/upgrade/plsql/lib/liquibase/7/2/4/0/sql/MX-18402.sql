--liquibase formatted sql


--changeSet MX-18402:1 stripComments:false
DELETE FROM db_type_config_parm t WHERE t.parm_name = 'ACTION_REMOVE_PART_APPLICABILITY' and t.db_type_cd = 'MASTER'; 