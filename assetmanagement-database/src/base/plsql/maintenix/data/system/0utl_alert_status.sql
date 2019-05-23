--liquibase formatted sql


--changeSet 0utl_alert_status:1 stripComments:false
insert into utl_alert_status (ALERT_STATUS_CD, DESC_SDESC, DESC_LDESC,UTL_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER, RSTAT_CD) values
('NEW','New Alert','This status is for an alert that has not beek acknowledged', 0, '06-OCT-09', '06-OCT-09', 0, 'MxI', 0);

--changeSet 0utl_alert_status:2 stripComments:false
insert into utl_alert_status (ALERT_STATUS_CD, DESC_SDESC, DESC_LDESC,UTL_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER, RSTAT_CD) values
('ACK','Acknowledged Alert','This status is for an alert that has been acknowledged', 0, '06-OCT-09', '06-OCT-09', 0, 'MxI', 0);

--changeSet 0utl_alert_status:3 stripComments:false
insert into utl_alert_status (ALERT_STATUS_CD, DESC_SDESC, DESC_LDESC,UTL_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER, RSTAT_CD) values
('DISP','Dispositioned Alert','This status is for an alert that has been dispositioned',0, '06-OCT-09', '06-OCT-09', 0, 'MxI', 0);

--changeSet 0utl_alert_status:4 stripComments:false
insert into utl_alert_status (ALERT_STATUS_CD, DESC_SDESC, DESC_LDESC,UTL_ID, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER, RSTAT_CD) values
('ARCHIVE','Archived Alert','This status is for an alert that has been archived',0, '06-OCT-09', '06-OCT-09', 0, 'MxI', 0);