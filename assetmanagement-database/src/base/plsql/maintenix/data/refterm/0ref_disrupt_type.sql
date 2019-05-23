--liquibase formatted sql


--changeSet 0ref_disrupt_type:1 stripComments:false
-- -- -- --  Disruption Type Codes -- -- -- -- 
/********************************************
** INSERT SCRIPT FOR TABLE "REF_DISRUPT_TYPE"
** 0-Level
** DATE: 11-JUN-09 TIME: 15:40:00
*********************************************/
insert into REF_DISRUPT_TYPE(DISRUPT_TYPE_DB_ID, DISRUPT_TYPE_CD,DESC_SDESC,DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'DLY','Delay','This represents a flight delay.', 0 , '11-JUN-09' , '11-JUN-09' , 0 , 'MXI' );

--changeSet 0ref_disrupt_type:2 stripComments:false
insert into REF_DISRUPT_TYPE(DISRUPT_TYPE_DB_ID, DISRUPT_TYPE_CD,DESC_SDESC,DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0,'CNX','Cancellation','This represents a flight cancellation.', 0 , '11-JUN-09' , '11-JUN-09' , 0 , 'MXI' );