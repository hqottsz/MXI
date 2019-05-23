--liquibase formatted sql


--changeSet 0ref_log_reason:1 stripComments:false
-- -- -- --  Oil Consumption Monitoring -- -- -- -- 
/********************************************
** INSERT SCRIPT FOR TABLE "REF_LOG_REASON"
** 0-Level
** DATE: 06-NOV-09 TIME: 16:24:40
*********************************************/
insert into REF_LOG_REASON (LOG_REASON_DB_ID, LOG_REASON_CD, LOG_ACTION_DB_ID, LOG_ACTION_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'THRESHOLD',0,'OILSTATUS','A threshold was met/exceeded.','A threshold was met/exceeded which resulted in automatic update of the oil consumption status.','THRESHOLD', 0 , '06-NOV-09' , '06-NOV-09' , 0 , 'MXI' );

--changeSet 0ref_log_reason:2 stripComments:false
-- Requirements missing job cards.
insert into REF_LOG_REASON (LOG_REASON_DB_ID, LOG_REASON_CD, LOG_ACTION_DB_ID, LOG_ACTION_CD, DESC_SDESC, DESC_LDESC, USER_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'TDMISSJIC',0,'TDPREVENTEXE','Requirement missing job card(s).','Requirement missing job card(s).','TDMISSJIC',0, '22OCT2010','22OCT2010', 0,'MXI');