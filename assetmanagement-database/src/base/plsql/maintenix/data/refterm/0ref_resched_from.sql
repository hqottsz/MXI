--liquibase formatted sql


--changeSet 0ref_resched_from:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_RESCHED_FROM"
** 0-Level
** DATE: 12-MARCH-2009
*********************************************/
insert into ref_resched_from(resched_from_db_id, resched_from_cd, desc_sdesc, desc_ldesc,default_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
values (0, 'EXECUTE', 'From Execution', 'Schedule from Execution', 1, 0, '12-MAR-09', '12-MAR-09', 0, 'MXI' );

--changeSet 0ref_resched_from:2 stripComments:false
insert into ref_resched_from(resched_from_db_id, resched_from_cd, desc_sdesc, desc_ldesc,default_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
values (0, 'WPSTART', 'From Work Package Start', 'Schedule the next task from Work-Package Start Date', 0, 0, '12-MAR-09', '12-MAR-09', 0, 'MXI' );

--changeSet 0ref_resched_from:3 stripComments:false
insert into ref_resched_from(resched_from_db_id, resched_from_cd, desc_sdesc, desc_ldesc,default_bool, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user )
values (0, 'WPEND', 'From Work Package End', 'Schedule the next task from Work-Package End Date', 0, 0, '12-MAR-09', '12-MAR-09', 0, 'MXI' );