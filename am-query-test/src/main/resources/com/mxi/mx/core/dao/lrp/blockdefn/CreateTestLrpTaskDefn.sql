delete from task_defn where task_defn_db_id = 777;
delete from task_task where task_db_id = 888;
delete from lrp_task_defn where lrp_db_id = 555;
delete from task_sched_rule;
delete from eqp_assmbl;
delete from eqp_assmbl_bom;
delete from ref_task_class;
delete from ref_task_subclass;



-- task definitions
insert into task_defn (task_defn_db_id, task_defn_id)
                values (777, 1);
insert into task_defn (task_defn_db_id, task_defn_id)
                values (777, 2);

-- task_task
insert into task_task (task_db_id, task_id, block_chain_sdesc, block_ord, task_cd, task_name, recurring_task_bool,
                       task_def_status_cd, last_sched_dead_bool, effective_dt, task_sched_from_db_id, task_sched_from_cd, revision_ord,
                       assmbl_db_id, assmbl_cd, assmbl_bom_id, task_class_db_id, task_class_cd, task_subclass_db_id, task_subclass_cd,
                       planning_type_db_id, planning_type_Id, task_defn_db_id, task_defn_id)
                values (888, 1, '', 1, '1', '1', 1,
                         'ACTV', 0, to_date('16-05-2008', 'dd-mm-yyyy'), 0, 'RECEIVED_DT', 1,
                         8000000, 'B767-200', 0, 10, 'BLOCK', NULL, NULL,
                         NULL, NULL, 777, 1 );

insert into task_task (task_db_id, task_id, block_chain_sdesc, block_ord, task_cd, task_name, recurring_task_bool,
                       task_def_status_cd, last_sched_dead_bool, effective_dt, task_sched_from_db_id, task_sched_from_cd, revision_ord,
                       assmbl_db_id, assmbl_cd, assmbl_bom_id, task_class_db_id, task_class_cd, task_subclass_db_id, task_subclass_cd,
                       planning_type_db_id, planning_type_Id, task_defn_db_id, task_defn_id)
                values (888, 2, '', 2, '2', '2', 1,
                         'ACTV', 0, to_date('16-05-2008', 'dd-mm-yyyy'), 0, 'RECEIVED_DT', 1,
                         8000000, 'B767-200', 0, 10, 'BLOCK', NULL, NULL,
                         NULL, NULL, 777, 2 );

-- lrp_task_defn
insert into lrp_task_defn (lrp_db_id, lrp_id, task_defn_db_id, task_defn_id, est_material_cost,
                           min_yield_pct, max_yield_pct, default_duration_days, min_duration_days, max_duration_days,
                           revision_ord, prev_lrp_db_id, prev_lrp_id, prev_task_defn_db_id, prev_task_defn_id,
                           dur_buffer_pct)
                    values (555, 1, 777, 1, 0,
                             0, 1, 0, 0, 9999, 1, 555, 1, 777, 2, 0.1);

insert into lrp_task_defn (lrp_db_id, lrp_id, task_defn_db_id, task_defn_id, est_material_cost,
                           min_yield_pct, max_yield_pct, default_duration_days, min_duration_days, max_duration_days,
                           revision_ord, prev_lrp_db_id, prev_lrp_id, prev_task_defn_db_id, prev_task_defn_id,
                           dur_buffer_pct)
                    values (555, 1, 777, 2, 0,
                             0, 1, 0, 0, 9999, 1, 555, 1, 777, 1, 0.1);

-- scheduling rules
insert into task_sched_rule (task_db_id, task_id, def_deviation_qt, data_type_db_id, data_type_id, def_initial_qt, def_interval_qt)
                     values (888, 1, 0, 0, 1, 2000, 2000);

insert into task_sched_rule (task_db_id, task_id, def_deviation_qt, data_type_db_id, data_type_id, def_initial_qt, def_interval_qt)
                     values (888, 1, 0, 0, 2, 200, 200);

insert into task_sched_rule (task_db_id, task_id, def_deviation_qt, data_type_db_id, data_type_id, def_initial_qt, def_interval_qt)
                     values (888, 2, 0, 0, 1, 2000, 2000);

insert into task_sched_rule (task_db_id, task_id, def_deviation_qt, data_type_db_id, data_type_id, def_initial_qt, def_interval_qt)
                     values (888, 2, 0, 0, 2, 200, 200);

insert into task_sched_rule (task_db_id, task_id, def_deviation_qt, data_type_db_id, data_type_id, def_initial_qt, def_interval_qt)
                     values (888, 2, 0, 0, 3, 3000, 3000);

-- other miscellaneous tables
insert into eqp_assmbl (assmbl_db_id, assmbl_cd, assmbl_name)
                values (8000000, 'B767-200', 'Boeing');

insert into eqp_assmbl_bom (assmbl_db_id, assmbl_cd, assmbl_bom_id, assmbl_bom_cd)
                values (8000000, 'B767-200', 0, 'Boeing');

insert into ref_task_class (task_class_db_id, task_class_cd, class_mode_cd)
                    values (10, 'BLOCK', 'BLOCK' );



