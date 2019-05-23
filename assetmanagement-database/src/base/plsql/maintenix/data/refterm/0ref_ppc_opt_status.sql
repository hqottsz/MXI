--liquibase formatted sql


--changeSet 0ref_ppc_opt_status:1 stripComments:false
/*****************************************************
** INSERT SCRIPT FOR TABLE "REF_PPC_OPT_STATUS"
** 0-Level
** DATE: 29-APR-10 TIME: 00:00:00
******************************************************/
insert into ref_ppc_opt_status (ppc_opt_status_db_id, ppc_opt_status_cd, user_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'PENDING', 'PENDING', 'Pending Optimization', 'This status indicates that request to optimize was plan, but no confirmation was received from the optimizer.', 0, '29-APR-10', '29-APR-10', 0, 'MXI');

--changeSet 0ref_ppc_opt_status:2 stripComments:false
insert into ref_ppc_opt_status (ppc_opt_status_db_id, ppc_opt_status_cd, user_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'PROCESS', 'PROCESSING', 'Processing Message', 'This status indicates that an optimizer received the request to optimize the production plan, and is processing the massage.', 0, '29-APR-10', '29-APR-10', 0, 'MXI');

--changeSet 0ref_ppc_opt_status:3 stripComments:false
insert into ref_ppc_opt_status (ppc_opt_status_db_id, ppc_opt_status_cd, user_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'FORCESTP', 'FORCESTOP', 'Force stop initiated', 'This status indicates that user tried to stop the optimization of the production plan.', 0, '29-APR-10', '29-APR-10', 0, 'MXI');

--changeSet 0ref_ppc_opt_status:4 stripComments:false
insert into ref_ppc_opt_status (ppc_opt_status_db_id, ppc_opt_status_cd, user_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SUCCESS', 'SUCCESS', 'Optimization Completed', 'This status indicates that the optimization message was processed and received successfully. The production plan was optimized. ', 0, '29-APR-10', '29-APR-10', 0, 'MXI');

--changeSet 0ref_ppc_opt_status:5 stripComments:false
insert into ref_ppc_opt_status (ppc_opt_status_db_id, ppc_opt_status_cd, user_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'FAILED', 'FAILED', 'Optimization Failed', 'This status indicates that there was a failure at some point and the production plan could not be optimized.', 0, '29-APR-10', '29-APR-10', 0, 'MXI');

--changeSet 0ref_ppc_opt_status:6 stripComments:false
insert into ref_ppc_opt_status (ppc_opt_status_db_id, ppc_opt_status_cd, user_cd, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'STOPPED', 'STOPPED', 'Optimization Stopped', 'This status indicates that optimization was successfully stopped.', 0, '29-APR-10', '29-APR-10', 0, 'MXI');