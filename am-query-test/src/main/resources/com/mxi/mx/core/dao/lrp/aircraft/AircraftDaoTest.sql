insert into fc_model (MODEL_DB_ID, MODEL_ID)
       values (777, 1);
insert into eqp_assmbl (assmbl_db_id, assmbl_cd, assmbl_name, assmbl_class_db_id, assmbl_class_cd)
       values (777, 'BOEING', 'LRP TEST BOEING ASSEMBLY', 0, 'ACFT');
insert into eqp_assmbl (assmbl_db_id, assmbl_cd, assmbl_name, assmbl_class_db_id, assmbl_class_cd)
       values (777, 'AIRBUS', 'LRP TEST AIRBUS ASSEMBLY', 0, 'ACFT');
insert into org_org ( org_db_id, org_id, org_sdesc )
       values (777, 1, 'Test carrier 1');
insert into org_org ( org_db_id, org_id, org_sdesc )
       values (777, 2, 'Test carrier 2');
insert into org_carrier (carrier_db_id, carrier_id, org_db_id, org_id )
       values (777, 1, 777, 1);
insert into org_carrier (carrier_db_id, carrier_id, org_db_id, org_id )
       values (777, 2, 777, 2);
insert into inv_inv (inv_no_db_id, inv_no_id, assmbl_db_id, assmbl_cd)
       values (777, 1000, 777, 'BOEING');
insert into inv_ac_reg (inv_no_db_id, inv_no_id, ac_reg_cd)
       values (777, 1000, 'ACTUAL');