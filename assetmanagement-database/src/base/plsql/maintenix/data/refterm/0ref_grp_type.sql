--liquibase formatted sql


--changeSet 0ref_grp_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_GRP_TYPE"
** 0-Level
** DATE: 17-JAN-2008 TIME: 15:43:00
*********************************************/
insert into REF_GRP_TYPE (GRP_TYPE_DB_ID, GRP_TYPE_CD, DESC_SDESC, DESC_LDESC, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
values (0, 'EQUAL', 'Equals', 'All license definitions in this group are equal', 0, '17-JAN-08', '17-JAN-08', 100, 'MXI');