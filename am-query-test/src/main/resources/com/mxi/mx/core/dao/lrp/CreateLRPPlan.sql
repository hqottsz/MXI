delete from lrp_plan;
insert into lrp_plan (LRP_DB_ID, LRP_ID, DESC_SDESC, DESC_LDESC, PUBLISHED_DT, PUB_HR_DB_ID, PUB_HR_ID, CREATED_DT, CREATED_HR_DB_ID, CREATED_HR_ID, UPDATED_DT, UPDATED_HR_DB_ID, UPDATED_HR_ID ,RSTAT_CD, CREATION_DT, REVISION_DT, REVISION_DB_ID, REVISION_USER)
       values (777, 10000, 'Test Plan short description', 'Test Plan long description', null, null, null, to_date('16-05-2008', 'dd-mm-yyyy'), 0, 2, to_date('16-05-2008', 'dd-mm-yyyy'), 0, 2 , 0, to_date('16-05-2008', 'dd-mm-yyyy'), to_date('16-05-2008', 'dd-mm-yyyy'), 100, 'MXI');
