--liquibase formatted sql


--changeSet 0ref_sd_nature:1 stripComments:false
-- Electronic Logbook
/********************************************
** INSERT SCRIPT FOR TABLE "REF_SD_NATURE"
** 0-Level
*********************************************/
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'ABT',0,1,'Aborted Approach Indicator','Aborted Approach Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_sd_nature:2 stripComments:false
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'ATB',0,1,'Air Turn Back Indicator','Air Turn Back Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_sd_nature:3 stripComments:false
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'CNX',0,1,'Cancellation Indicator','Cancellation Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_sd_nature:4 stripComments:false
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'DLY',0,1,'Delay Indicator','Delay Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_sd_nature:5 stripComments:false
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'DIV',0,1,'Diversion Indicator','Diversion Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_sd_nature:6 stripComments:false
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'EMD',0,1,'Emergency Descent Indicator','Emergency Descent Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_sd_nature:7 stripComments:false
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'GTB',0,1,'Ground Turn Back Indicator','Ground Turn Back Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_sd_nature:8 stripComments:false
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'IFD',0,1,'In-Flight Shut Down Indicator','In-Flight Shut Down Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_sd_nature:9 stripComments:false
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'RTO',0,1,'Aborted Take-off Indicator','Aborted Take-off Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');

--changeSet 0ref_sd_nature:10 stripComments:false
insert into ref_sd_nature (sd_nature_db_id, sd_nature_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc, rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user)
values (0,'TII',0,1,'Technical Incident Indicator','Technical Incident Indicator', 0, '15-JUN-11', '15-JUN-11', 0, 'MXI');