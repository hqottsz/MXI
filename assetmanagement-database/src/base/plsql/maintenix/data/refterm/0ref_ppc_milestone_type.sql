--liquibase formatted sql


--changeSet 0ref_ppc_milestone_type:1 stripComments:false
INSERT INTO ref_ppc_milestone_type (
   ppc_milestone_type_db_id, 
   ppc_milestone_type_cd,
   desc_sdesc,
   desc_ldesc,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
) VALUES (
   0, 
   'PROJECT', 
   'Project Milestone', 
   'Project Milestone type', 
   0, 
   to_date('10-08-2016 15:30:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('10-08-2016 15:30:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
);

--changeSet 0ref_ppc_milestone_type:2 stripComments:false
INSERT INTO ref_ppc_milestone_type (
   ppc_milestone_type_db_id, 
   ppc_milestone_type_cd,
   desc_sdesc,
   desc_ldesc,
   rstat_cd,
   creation_dt,
   revision_dt,
   revision_db_id,
   revision_user
) VALUES (
   0, 
   'TECHNICAL', 
   'Technical Milestone', 
   'Technical Milestone type', 
   0, 
   to_date('10-08-2016 15:30:00', 'dd-mm-yyyy hh24:mi:ss'), 
   to_date('10-08-2016 15:30:00', 'dd-mm-yyyy hh24:mi:ss'), 
   0, 
   'MXI'
);

