--liquibase formatted sql

--changeSet OPER-18731:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment date range parameter for TaskSearch.jsp
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'sTaskSearchCreatedBefore',
      p_parm_type        => 'SESSION',
      p_parm_desc        => 'Task search date range parameter for open tasks without scheduling.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'DATE',
      p_default_value    => '',
      p_mand_config_bool => 0,
      p_category         => 'Task Search',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/

--changeSet OPER-18731:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment date range parameter for TaskSearch.jsp
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'sTaskSearchCreatedAfter',
      p_parm_type        => 'SESSION',
      p_parm_desc        => 'Task search date range parameter for open tasks without scheduling.',
      p_config_type      => 'USER',
      p_allow_value_desc => 'DATE',
      p_default_value    => '',
      p_mand_config_bool => 0,
      p_category         => 'Task Search',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/


--changeSet OPER-18731:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add index evt_event table
BEGIN
  utl_migr_schema_pkg.index_create('
		CREATE INDEX "IX_EVTEVENT_CRTDT" ON EVT_EVENT
		(
		"CREATION_DT"
		)
	');
END;
/

--changeSet OPER-18731:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add index to evt_event table
BEGIN
  utl_migr_schema_pkg.index_create('
		CREATE INDEX "IX_EVTEVENT_GDT" ON EVT_EVENT
		(
		"EVENT_GDT"
		)
	');
END;
/

--changeSet OPER-18731:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment add index to mt_drv_sched_info table
BEGIN
  utl_migr_schema_pkg.index_create('
		CREATE INDEX "IX_MTDRVSCH_DRVDT" ON MT_DRV_SCHED_INFO
		(
		"DRV_SCHED_DEAD_DT"
		)
	');
END;
/

--changeSet OPER-18731:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
--comment date range parameter for TaskSearch.jsp
BEGIN
   utl_migr_data_pkg.config_parm_insert(
      p_parm_name        => 'TASK_SEARCH_DEFAULT_DATE_RANGE',
      p_parm_type        => 'MXWEB',
      p_parm_desc        => 'Task search date range used if user adds no date to scheduling fields. This will use the settings value to look X days in the future and X days in past for OPEN tasks and X days in past for HISTORICAL.',
      p_config_type      => 'GLOBAL',
      p_allow_value_desc => 'Integer',
      p_default_value    => '14',
      p_mand_config_bool => 0,
      p_category         => 'Task Search',
      p_modified_in      => '8.3-SP1',
      p_utl_id           => 0
   );
END;
/
