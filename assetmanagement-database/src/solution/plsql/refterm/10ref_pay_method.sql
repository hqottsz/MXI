/********************************************
** INSERT SCRIPT FOR TABLE "REF_PAY_METHOD"
** 10-Level
** DATE: 07-21-04
*********************************************/
insert into ref_pay_method(pay_method_db_id, pay_method_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'PART', 0, 1, 'Part Time', 'This employee is a part time employee', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_pay_method(pay_method_db_id, pay_method_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'CONT', 0, 1, 'Contract', 'This employee is contracted', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_pay_method(pay_method_db_id, pay_method_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'TEMP', 0, 1, 'Temporary', 'This employee is temporary', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
insert into ref_pay_method(pay_method_db_id, pay_method_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (10, 'HOUR', 0, 1, 'Hourly', 'This employee is paid by the hour', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');
