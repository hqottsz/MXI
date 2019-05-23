--liquibase formatted sql


--changeSet 0ref_pay_method:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_PAY_METHOD"
** 0-Level
** DATE: 07-21-04
*********************************************/
insert into ref_pay_method(pay_method_db_id, pay_method_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'SALARY', 0, 1, 'Salary', 'This employee is a full time employee', 0, '23-MAR-01', '23-MAR-01', 100, 'MXI');

--changeSet 0ref_pay_method:2 stripComments:false
insert into ref_pay_method(pay_method_db_id, pay_method_cd, bitmap_db_id, bitmap_tag, desc_sdesc, desc_ldesc,  rstat_cd, creation_dt, revision_dt, revision_db_id, revision_user) 
values (0, 'NA', 0, 1, 'Claim', 'Pay Mode When Claim is Created', 0, '15-SEP-08', '15-SEP-08', 100, 'MXI');