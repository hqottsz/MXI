--liquibase formatted sql


--changeSet 0ref_int_step_type:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_INT_STEP_TYPE"
** 0-Level
** DATE: 07/10/2006 TIME: 10:30:00
*********************************************/
insert into REF_INT_STEP_TYPE (STEP_TYPE_DB_ID, STEP_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   values (0, 'PROCESS', 0, to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_int_step_type:2 stripComments:false
insert into REF_INT_STEP_TYPE (STEP_TYPE_DB_ID, STEP_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   values (0, 'TRANSFORM', 0, to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_int_step_type:3 stripComments:false
insert into REF_INT_STEP_TYPE (STEP_TYPE_DB_ID, STEP_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   values (0, 'VALIDATOR', 0, to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');

--changeSet 0ref_int_step_type:4 stripComments:false
insert into REF_INT_STEP_TYPE (STEP_TYPE_DB_ID, STEP_TYPE_CD, RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
   values (0, 'SERVICE', 0, to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), to_date('17-07-2006 11:31:18', 'dd-mm-yyyy hh24:mi:ss'), 0, 'MXI');