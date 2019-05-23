--liquibase formatted sql

--changeSet OPER-23643:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Adding comments to table and columns for MT_CORE_FLEET_LIST
BEGIN
EXECUTE IMMEDIATE q'[COMMENT ON TABLE MT_CORE_FLEET_LIST
IS
  'Topical aggregation table for materialized data related to Fleet Due List tab. Updated via trigger on MT_DRV_SCHED_INFO. The trigger excutes logic in MT_CORE_FLEET_LIST_PKG and will update rows as needed.

This table is transactional with changes in evt_sched_dead. A consequence is that it will always be in sync. However, before using for other pages or reports, please examine the logic found in the MT_CORE_FLEET_LIST_PKG and verify that the data conforms to your business needs.

For performance reasons, there are no defined constraints. This table relies on source table constraint enforcement.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.EVENT_DB_ID
IS
  'FK to mt_drv_sched_info. The event in evt_sched_dead that is applicable to Fleet Due List.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.EVENT_ID
IS
  'FK to mt_drv_sched_info. The event in evt_sched_dead that is applicable to Fleet Due List.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.TASK_INV_NO_DB_ID
IS
  'FK to inv_inv. The inventory that is the main inventory on this task.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.TASK_INV_NO_ID
IS
  'FK to inv_inv. The inventory that is the main inventory on this task.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.REPL_SCHED_ID
IS
  'A reference to the replacement part requirement for replacement tasks and their sub-tasks.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.BARCODE_SDESC
IS
  'Barcode defined for this task']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.DEVIATION_QT
IS
  'Reference to EVT_SCHED_DEAD. The amount by which the deadline can "slip" past its due date before being considered overdue.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.USAGE_REM_QT
IS
  'Reference to EVT_SCHED_DEAD. The difference between the current usage count (DATA_TYPE_ID) and SCHED_DEAD_QT.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.SCHED_DEAD_DT
IS
  'Reference to EVT_SCHED_DEAD. The scheduled deadline for the event.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.INVENTORY_KEY
IS
  'Concatenated FK to aircraft inv_no_db_id and inv_no_id.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.INV_NO_SDESC
IS
  'Description of the aircraft']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.ASSMBL_CD
IS
  'Assembly code of the aircraft']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.AUTHORITY_DB_ID
IS
  'FK to authority assigned to aircraft in ORG_AUTHORITY.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.AUTHORITY_ID
IS
  'FK to authority assigned to aircraft in ORG_AUTHORITY.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.DOMAIN_TYPE_CD
IS
  'Reference to the drv_domain_type_cd in mt_drv_sched_info.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.ENG_UNIT_CD
IS
  'Reference to the drv_eng_unit_cd in mt_drv_sched_info.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.ENG_UNIT_MULT_QT
IS
  'Reference to the drv_eng_unit_cd in mt_drv_sched_info.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.PRECISION_QT
IS
  'Reference to the drv_precision_qt in mt_drv_sched_info.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.SOFT_DEADLINE
IS
  'Reference to soft_deadline_bool in sched_stask.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.PLAN_BY_DATE
IS
  'Reference to plan_by_dtl in sched_stask.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.PREVENT_EXE_BOOL
IS
  'Reference to prevent_exe_bool in sched_stask_flags.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.PREVENT_EXE_REVIEW_DT
IS
  'Reference to prevent_exe_review_dt in sched_stask_flags.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.AC_INV_NO_DB_ID
IS
  'FK to inv_inv of aircraft']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.AC_INV_NO_ID
IS
  'FK to inv_inv of aircraft.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.DEADLINE_EVENT_DB_ID
IS
  'Reference to the driving event in mt_drv_sched_info.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.DEADLINE_EVENT_ID
IS
  'Reference to the driving event in mt_drv_sched_info.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.SORT_DUE_DT
IS
  'Reference to sort_due_dt in mt_drv_sched_info.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.EXT_SCHED_DEAD_DT
IS
  'Reference to the driving ext_dead_dt in mt_drv_sched_info.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_CORE_FLEET_LIST.CONFIG_POS_SDESC
IS
  'Calculated via call to get_inv_repl_sdesc global function.']';
END;
/

--changeSet OPER-23643:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Adding comments to table and columns for MT_DRV_SCHED_INFO
BEGIN
EXECUTE IMMEDIATE q'[COMMENT ON TABLE MT_DRV_SCHED_INFO
IS
  'This is a materialization table that stores the relevant scheduling data for each driving event in the EVT_SCHED_DEAD table. It is kept consistant via trigger on EVT_SCHED_DEAD that uses logic in the mt_rep_int_evt_scded_pkg to then insert and or delete rows in this table.

Data is stored to remove the need to call functions to calculate date values at runtime. getExtendedDeadlineDt and getDueDate_PlanByDate do not need to be called and instead queries can directly use values stored in the table.

The table itself stores a reference to the base task and the driving task. It then uses the driving tasks information (if driving task is different) for all columns with DRV_ prefix. For example drv_deviation_qt is determined by this logic: nvl(drv_tasks.deviation_qt , base_task.deviation_qt).']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.EVENT_DB_ID
IS
  'FK to evt_sched_dead.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.EVENT_ID
IS
  'FK to evt_sched_dead.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_EVENT_DB_ID
IS
  'Reference to evt_event_rel rel_event_db_id.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_EVENT_ID
IS
  'Reference to evt_event_rel rel_event_id.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.EVT_EXT_DEAD_DT
IS
  'Calculated value of extended deadline date for this event if it exists.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_EXT_DEAD_DT
IS
  'Calculated value of the driving tasks extended deadline date for this event if it exists.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.EVT_SCHED_DEAD_DT
IS
  'Tasks scheduled deadline date.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_SCHED_DEAD_DT
IS
  'Driving tasks scheduled deadline date']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.EVT_PLAN_BY_DT
IS
  'Tasks plan by date.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_PLAN_BY_DT
IS
  'Driving tasks plan by date.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_DEVIATION_QT
IS
  'Driving tasks amount by which the deadline can "slip" past its due date before being considered overdue.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_USAGE_REM_QT
IS
  'Driving tasks difference between the current usage count (DATA_TYPE_ID) and SCHED_DEAD_QT.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_DOMAIN_TYPE_CD
IS
  'Driving tasks FK to REF_DOMAIN_TYPE.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_ENG_UNIT_CD
IS
  'Driving tasks FK to REF_ENG_UNIT.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_PRECISION_QT
IS
  'Driving tasks Entry Precision Quantity in mim_data_type.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_DATA_TYPE_CD
IS
  'Driving tasks user assigned code that helps identify the parameter referenced in mim_data_type.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_DATA_TYPE_DB_ID
IS
  'Identifies the creation database (MIM_LOCAL_DB) of the record and forms part of the record''s primary key.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.DRV_DATA_TYPE_ID
IS
  'Unique identifier assigned from Sequence DATA_TYPE_ID_SEQ.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_DRV_SCHED_INFO.SORT_DUE_DT
IS
  'Calculated value that looks at base task plan by date, driving tasks plan by date, and finally extended deadline dates of both. The first non-null value is stored.']';
END;
/

--changeSet OPER-23643:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Adding comments to table and columns for MT_ENH_APPLICABILITY
BEGIN
EXECUTE IMMEDIATE q'[COMMENT ON TABLE MT_ENH_APPLICABILITY
IS
  'Table that is secondary filtering (applicability) source for enhanced part search queries. Created and maintained from source tables via trigger as part of MX_CORE_GENERATE_PART_SEARCH_INDEX job. This aggregates changes made to the applicability of parts and then maps to inventory. In the query it uses inverse logic. That is, given an inventory, if I have a row returned then it is not applicable.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_APPLICABILITY.INV_NO_DB_ID
IS
  'FK to inv_inv']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_APPLICABILITY.INV_NO_ID
IS
  'FK to inv_inv']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_APPLICABILITY.BOM_PART_DB_ID
IS
  'FK to eqp_part_baseline']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_APPLICABILITY.BOM_PART_ID
IS
  'FK to eqp_part_baseline']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_APPLICABILITY.PART_NO_DB_ID
IS
  'FK to eqp_part_no']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_APPLICABILITY.PART_NO_ID
IS
  'FK to eqp_part_no']';
END;
/

--changeSet OPER-23643:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Adding comments to table and columns for MT_ENH_BOM_APL_LOG
BEGIN
EXECUTE IMMEDIATE q'[COMMENT ON TABLE MT_ENH_BOM_APL_LOG
IS
  'Logging table used for enhanced part search that will be consumed during MX_CORE_GENERATE_PART_SEARCH_INDEX job. Updated via trigger on eqp_bom_part']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_BOM_APL_LOG.BOM_PART_DB_ID
IS
  'FK to eqp_bom_part']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_BOM_APL_LOG.BOM_PART_ID
IS
  'FK to eqp_bom_part']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_BOM_APL_LOG.UD_DT
IS
  'SYSDATE when last updated.']';
END;
/

--changeSet OPER-23643:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Adding comments to table and columns for MT_ENH_PART_NO_LOG
BEGIN
EXECUTE IMMEDIATE q'[COMMENT ON TABLE MT_ENH_PART_NO_LOG
IS
  'Logging table used for enhanced part search that will be consumed during MX_CORE_GENERATE_PART_SEARCH_INDEX job. Updated via trigger on eqp_part_no.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_NO_LOG.PART_NO_DB_ID
IS
  'FK to eqp_part_no']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_NO_LOG.PART_NO_ID
IS
  'FK to eqp_part_no']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_NO_LOG.UD_DT
IS
  'SYSDATE when last updated']';
END;
/

--changeSet OPER-23643:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Adding comments to table and columns for MT_ENH_PART_SEARCH
BEGIN
EXECUTE IMMEDIATE q'[COMMENT ON TABLE MT_ENH_PART_SEARCH
IS
  'Table that is source for enhanced part search queries. Created and maintained from source tables via trigger as part of MX_CORE_GENERATE_PART_SEARCH_INDEX job. The job generates a materialization of the baseline information for a part.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_PART_NO_PART_NO_DB_ID
IS
  'FK to eqp_part_no']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_PART_NO_PART_NO_ID
IS
  'FK to eqp_part_no']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_BOM_PART_BOM_PART_DB_ID
IS
  'FK to eqp_bom_part']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_BOM_PART_BOM_PART_ID
IS
  'FK to eqp_bom_part']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_ASSMBL_POS_ASSMBL_DB_ID
IS
  'FK to eqp_assmbl_pos']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_ASSMBL_POS_ASSMBL_BOM_ID
IS
  'FK to eqp_assmbl_pos']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_ASSMBL_POS_ASSMBL_POS_ID
IS
  'FK to eqp_assmbl_pos']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.SUBASSY_POS_ASSMBL_POS_ID
IS
  'Reference to eqp_assmbl_pos']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_PART_NO_ALT_ID
IS
  'FK to eqp_part_no']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_PART_NO_PART_NO_OEM
IS
  'The Original Equipment Manufacturer''s part number.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_PART_NO_PART_NO_SDESC
IS
  'A label used to describe the part.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_BOM_PART_ALT_ID
IS
  'FK to eqp_bom_part']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_BOM_PART_BOM_PART_CD
IS
  'A code used to indentify, or classify this BOM part. It is not necessarily unique.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_BOM_PART_BOM_PART_NAME
IS
  'A label used to identify this bill of material entry. This is often the standard part number shown in the illustrated parts catalog.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_ASSMBL_POS_ASSMBL_CD
IS
  'FK to EQP_ASSMBL_BOM. The log card for which this position is defined.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_ASSMBL_POS_EQP_POS_CD
IS
  'The standard, defining code for this position (eg. "left", "right", etc.)']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.SUBASSY_POS_POS
IS
  'The standard, defining code for the sub assembly position (eg. "left", "right", etc.)']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_BOM_PART_CONDITIONS
IS
  'This is the description of the conditional applicability for this part group']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.REF_INV_CLASS_TRACKED
IS
  'Does this item represent the physical BOM item itself? Every BOM item that is not a "System", is an actual physical component. If tracked_bool=1, then this BOM part represents the physical BOM item. There can be only 1 Bom Part entry with tracked_bool=1 per BOM item.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_ASSMBL_ASSMBL_CLASS_CD
IS
  'FK to REF_ASSMBL_CLASS. Identifies the class of the assembly, which is used to activate various Maintenix functionalities.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PART_SEARCH.EQP_BOM_PART_APPL_EFF_LDESC
IS
  'This is the applicability expression for a particular BOM part slot. This is used when a particular BOM part slot exists on some aircraft, but not on others.']';
END;
/

--changeSet OPER-23643:7 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Adding comments to table and columns for MT_ENH_PRT_APL_LOG
BEGIN
EXECUTE IMMEDIATE q'[COMMENT ON TABLE MT_ENH_PRT_APL_LOG
IS
  'Logging table used for enhanced part search that will be consumed during MX_CORE_GENERATE_PART_SEARCH_INDEX job. updated via trigger on eqp_part_baseline']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PRT_APL_LOG.BOM_PART_DB_ID
IS
  'FK to eqp_part_baseline']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PRT_APL_LOG.BOM_PART_ID
IS
  'FK to eqp_part_baseline']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PRT_APL_LOG.PART_NO_DB_ID
IS
  'FK to eqp_part_baseline']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PRT_APL_LOG.PART_NO_ID
IS
  'FK to eqp_part_baseline']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_ENH_PRT_APL_LOG.UD_DT
IS
  'SYSDATE when last updated']';
END;
/

--changeSet OPER-23643:8 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment Adding comments to table and columns for MT_INV_AC_AUTHORITY
BEGIN
EXECUTE IMMEDIATE q'[COMMENT ON TABLE MT_INV_AC_AUTHORITY
IS
  'Materialization table of mappings between user''s authorities on Aircraft inventory. Updated via triggers on org_hr_authority, inv_inv, org_hr.
  This is a direct replacement for the vw_inv_ac_authority and can be directly joined instead of using the view.
  Transactionally consistent with source tables.']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_INV_AC_AUTHORITY.INV_NO_DB_ID
IS
  'FK to inv_inv']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_INV_AC_AUTHORITY.INV_NO_ID
IS
  'FK to inv_inv']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_INV_AC_AUTHORITY.HR_DB_ID
IS
  'FK to org_hr']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_INV_AC_AUTHORITY.HR_ID
IS
  'FK to org_hr']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_INV_AC_AUTHORITY.AUTHORITY_DB_ID
IS
  'FK to org_hr_authority']';
EXECUTE IMMEDIATE q'[COMMENT ON COLUMN MT_INV_AC_AUTHORITY.AUTHORITY_ID
IS
  'FK to org_hr_authority']';
END;
/
