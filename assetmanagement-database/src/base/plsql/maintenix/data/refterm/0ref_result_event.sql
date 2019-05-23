--liquibase formatted sql


--changeSet 0ref_result_event:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_RESULT_EVENT"
** 0-Level
** DATE: 13/01/10 TIME: 03:43:00
*********************************************/
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'ABT','ABT','Aborted Approach','The approach was aborted.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:2 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'ATB','ATB','Air Turn Back','The aircraft made an air-turn-back.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:3 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'CNX','CNX','Cancellation','The flight was cancelled.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:4 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'DIV','DIV','Diversion','The aircraft was diverted to a different destination.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:5 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'DLY','DLY','Delay','The aircraft was delayed.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:6 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'EMD','EMD','Emergency Descent','The aircraft made an emergency descent.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:7 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'GTB','GTB','Ground Turn Back','The aircraft made a ground-turn-back.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:8 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'IFD','IFD','In-Flight Shut Down','There was an in-flight shut down of a power plant.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:9 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'RTO','RTO','Aborted Take Off','The take-off was aborted.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:10 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'TII','TII','Technical Incident','There was a technical incident.',0, '13-MAR-10', '13-JAN-10', 100, 'MXI');

--changeSet 0ref_result_event:11 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'GAI','GAI','General Air Interruption','General Air Interruption.',0, '14-FEB-19', '14-FEB-19', 100, 'MXI');

--changeSet 0ref_result_event:12 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'GGI','GGI','General Ground Interruption','General Ground Interruption.',0, '14-FEB-19', '14-FEB-19', 100, 'MXI');

--changeSet 0ref_result_event:13 stripComments:false
insert into ref_result_event(result_event_db_id,result_event_cd,user_cd,desc_sdesc,desc_ldesc,rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values(0,'AGI','AOG','Aircraft On the Ground','Aircraft On the Ground.',0, '14-FEB-19', '14-FEB-19', 100, 'MXI');