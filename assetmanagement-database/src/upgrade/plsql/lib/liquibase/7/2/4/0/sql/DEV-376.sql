--liquibase formatted sql


--changeSet DEV-376:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Migration script for 1009 LRP concept (section 6.2)
-- Author: Karan Mehta
-- Date:   9152010
-- Objectives:
-- 1) Each block will have one date range
-- 2) Add STDR planning type for every assembly in Maintenix, to each existing plan's in-use list
-- 3)	Each block will have the STD (Standard) planning type assigned. 
-- 4) The total routine and non routine values for the block will be 
--    equal to that of its one planning type. 
-- 5) Each work package will have the STD (Standard) planning type assigned.
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
DECLARE

  -- Get all the Plans in LRP
  CURSOR lcur_allPlans IS
    SELECT
      *
    FROM 
      lrp_plan
    WHERE 
      lrp_plan.rstat_cd = 0;

  -- Get all the Block Definitions in LRP
  CURSOR lcur_allBlockDefs IS
    SELECT 
      *
    FROM
      lrp_task_defn
    WHERE 
      lrp_task_defn.rstat_cd = 0;
      
  -- Variable to store the current local database's db_id
  lCurDbId NUMBER;    
  
  -- Just a temporary variable for holding calculated NR hours
  lNrHours NUMBER;
  
  -- Some temporary variables
  lTempVar1 NUMBER;
  lTempVar2 NUMBER;
  lTempVar3 NUMBER;
  lTempVar4 NUMBER;
  
  -- Get all the STDR planning types from the database, for all assemblies
  CURSOR lcur_allSTDRPlanTypes IS
    SELECT
      eqp_planning_type.*
    FROM
      eqp_planning_type eqp_planning_type
    WHERE
      eqp_planning_type.planning_type_cd = 'STDR'
      AND
      eqp_planning_type.rstat_cd = 0;
        
  -- Get all the WORK_EVENT events in LRP_EVENT
  CURSOR lcur_allLrpEvents IS
    SELECT
      lrp_event.*
    FROM
      lrp_event
    WHERE
      lrp_event.type = 'WORK_EVENT'
      AND
      lrp_event.rstat_cd = 0;      

BEGIN

  -- Get the current db_id
  SELECT 
    db_id
  INTO
    lCurDbId
  FROM
    mim_local_db;

  -- 1) Each block will have one date range in LRP_TASK_PLAN_RANGE. 
  --    The effective from date is the current date (EFFECTIVE_FROM_DT). 
  --    The effective to date is blank (EFFECTIVE_TO_DT)
  FOR lrec_allBlockDefs IN lcur_allBlockDefs
  LOOP
  
    INSERT INTO 
      lrp_task_plan_range (
        task_plan_range_db_id,
        task_plan_range_id,
        lrp_db_id,
        lrp_id,
        task_defn_db_id,
        task_defn_id,
        effective_from_dt,
        effective_to_dt
      )
    VALUES ( 
      lCurDbId,
      GetSequenceNextValue( 'LRP_PLAN_RANGE_ID_SEQ' ),
      lrec_allblockdefs.lrp_db_id,
      lrec_allblockdefs.lrp_id,
      lrec_allblockdefs.task_defn_db_id,
      lrec_allblockdefs.task_defn_id,
      -- First day of the current month in the YYYY-MM-DD format!
      TRUNC( TO_DATE( TO_CHAR( SYSDATE, 'YYYY-MM-DD' ), 'YYYY-MM-DD' ), 'MM' ),
      NULL
      );   
  
  END LOOP;
  
  -- 2) Add STDR planning type for every assembly in Maintenix, to each existing plan's in-use list
  FOR lrec_allPlans IN lcur_allPlans
  LOOP
  
    FOR lrec_allSTDRPlanTypes IN lcur_allSTDRPlanTypes
    LOOP
    
      INSERT INTO
        lrp_plan_type (
          lrp_plan_type_db_id,
          lrp_plan_type_id,
          lrp_db_id,
          lrp_id,
          planning_type_db_id,
          planning_type_id
        )
      VALUES (
        lCurDbId,
        GetSequenceNextValue( 'LRP_PLAN_TYPE_ID_SEQ' ),
        lrec_allPlans.LRP_DB_ID,
        lrec_allPlans.LRP_ID,
        lrec_allSTDRPlanTypes.PLANNING_TYPE_DB_ID,
        lrec_allSTDRPlanTypes.PLANNING_TYPE_ID
      );
    
    END LOOP;
    
  END LOOP;
  
  -- 3)	Each block will have the STDR (Standard) planning type assigned.
  -- 4) The total routine and non routine values for the block will be 
  --    equal to that of its one planning type. 
  FOR lrec_allBlockDefs IN lcur_allBlockDefs
  LOOP
  
    -- Calculate the NR hours that we'll need for the insert statement below
    IF ( lrec_allblockdefs.man_hours > lrec_allblockdefs.planned_man_hours ) THEN
      lNrHours := 0;
    ELSE
      lNrHours := ( lrec_allblockdefs.planned_man_hours - lrec_allblockdefs.man_hours );
    END IF;
  
    -- Insert a row in the LRP_TASK_BUCKET table for the single LRP_TASK_PLAN_RANGE entry that exists for each block definition
    INSERT INTO
      lrp_task_bucket (
        task_plan_range_db_id,
        task_plan_range_id,
        lrp_plan_type_db_id,
        lrp_plan_type_id,
        routine_hrs,
        nr_hrs,
        nr_factor,
        total_hrs
      )
    SELECT  
      tskplanrng.task_plan_range_db_id,
      tskplanrng.task_plan_range_id,
      plantype.lrp_plan_type_db_id,
      plantype.lrp_plan_type_id,
      lrec_allBlockDefs.man_hours,
      lNrHours,
      DECODE( lrec_allBlockDefs.man_hours, 0, 0, ( lNrHours / lrec_allBlockDefs.man_hours ) ),
      ( lNrHours + lrec_allBlockDefs.man_hours )
    FROM 
      lrp_task_plan_range tskplanrng,
      lrp_plan_type plantype,
      eqp_planning_type eqpplantype,
      task_task taskdef
    WHERE
      lrec_allBlockDefs.lrp_db_id = tskplanrng.lrp_db_id AND
      lrec_allBlockDefs.lrp_id = tskplanrng.lrp_id
      AND
      lrec_allblockdefs.task_defn_db_id = tskplanrng.task_defn_db_id AND
      lrec_allblockdefs.task_defn_id = tskplanrng.task_defn_id
      AND    
      lrec_allBlockDefs.lrp_db_id = plantype.lrp_db_id AND
      lrec_allBlockDefs.lrp_id = plantype.lrp_id
      AND      
      lrec_allblockdefs.task_defn_db_id = taskdef.task_db_id AND
      lrec_allblockdefs.task_defn_id = taskdef.task_id
      AND
      eqpplantype.assmbl_db_id = taskdef.assmbl_db_id AND
      eqpplantype.assmbl_cd = taskdef.assmbl_cd
      AND
      plantype.planning_type_db_id = eqpplantype.planning_type_db_id AND
      plantype.planning_type_id = eqpplantype.planning_type_id
      AND
      eqpplantype.planning_type_cd = 'STDR'
      AND
      tskplanrng.rstat_cd = 0
      AND
      plantype.rstat_cd = 0
      AND
      eqpplantype.rstat_cd = 0
      AND
      taskdef.rstat_cd = 0;
      
    -- The total routine and non routine values for the block will be  
    -- equal to that of its one planning type.       
    UPDATE
      lrp_task_defn taskdefn
    SET (
      taskdefn.routine_hrs,
      taskdefn.nr_hrs,
      taskdefn.total_hrs
      ) = (
      SELECT 
        taskbkt.routine_hrs,
        taskbkt.nr_hrs,
        taskbkt.total_hrs
      FROM 
        lrp_task_plan_range lrpplanrng,
        lrp_task_bucket taskbkt
      WHERE
        lrec_allBlockDefs.lrp_db_id = lrpplanrng.lrp_db_id AND
        lrec_allBlockDefs.lrp_id = lrpplanrng.lrp_id
        AND 
        lrec_allBlockDefs.task_defn_db_id = lrpplanrng.task_defn_db_id AND
        lrec_allBlockDefs.task_defn_id = lrpplanrng.task_defn_id
        AND
        lrpplanrng.task_plan_range_db_id = taskbkt.task_plan_range_db_id AND
        lrpplanrng.task_plan_range_id = taskbkt.task_plan_range_id
        AND
        lrec_allBlockDefs.rstat_cd = 0
        AND
        lrpplanrng.rstat_cd = 0
        AND
        taskbkt.rstat_cd = 0
      )
    WHERE
      lrec_allBlockDefs.lrp_db_id = taskdefn.lrp_db_id AND
      lrec_allBlockDefs.lrp_id = taskdefn.lrp_id
      AND 
      lrec_allBlockDefs.task_defn_db_id = taskdefn.task_defn_db_id AND
      lrec_allBlockDefs.task_defn_id = taskdefn.task_defn_id;
      
  END LOOP;
  
  -- 5) Each work package will have the STDR (Standard) planning type assigned.
  -- ???????AD_HOC events too??????? 
  FOR lrec_allLrpEvents IN lcur_allLrpEvents
  LOOP
  
    -- Get the STDR planning type details for the event's inventory's assembly
    SELECT
      lrpptype.lrp_plan_type_db_id,
      lrpptype.lrp_plan_type_id,
      levt.lrp_event_db_id,
      levt.lrp_event_id
    INTO
      lTempVar1,
      lTempVar2,
      lTempVar3,
      lTempVar4      
    FROM
      lrp_inv_inv lrpinv,
      lrp_event levt,
      lrp_plan_type lrpptype,
      eqp_planning_type eptype
    WHERE
      lrec_allLrpEvents.lrp_event_db_id = levt.lrp_event_db_id AND
      lrec_allLrpEvents.lrp_event_id = levt.lrp_event_id
      AND
      lrec_allLrpEvents.lrp_db_id = levt.lrp_db_id AND
      lrec_allLrpEvents.lrp_id = levt.lrp_id AND
      lrec_allLrpEvents.lrp_inv_inv_id = levt.lrp_inv_inv_id
      AND
      lrpinv.lrp_db_id = levt.lrp_db_id AND
      lrpinv.lrp_id = levt.lrp_id AND
      lrpinv.lrp_inv_inv_id = levt.lrp_inv_inv_id
      AND
      eptype.assmbl_db_id = lrpinv.assmbl_db_id AND
      eptype.assmbl_cd = lrpinv.assmbl_cd
      AND
      eptype.planning_type_db_id = lrpptype.planning_type_db_id AND
      eptype.planning_type_id = lrpptype.planning_type_id
      AND
      eptype.planning_type_cd = 'STDR'
      AND
      levt.lrp_db_id = lrpptype.lrp_db_id AND
      levt.lrp_id = lrpptype.lrp_id
      AND
      lrec_allLrpEvents.lrp_db_id = lrpptype.lrp_db_id AND
      lrec_allLrpEvents.lrp_id = lrpptype.lrp_id
      AND
      eptype.rstat_cd = 0
      AND 
      lrpinv.rstat_cd = 0
      AND
      levt.rstat_cd = 0
      AND
      lrpptype.rstat_cd = 0;
      
    -- Insert a row in the LRP_EVENT_BUCKET table for the event
    INSERT INTO
      lrp_event_bucket( 
        lrp_plan_type_db_id, 
        lrp_plan_type_id, 
        lrp_event_db_id, 
        lrp_event_id )
    VALUES (
      lTempVar1,
      lTempVar2,
      lTempVar3,
      lTempVar4
      );
      
    -- For the newly created row in LRP_EVENT_BUCKET, calculate the 
    -- Routine Hours, Non-Routine Hours and NR Factor by getting the
    -- workscope blocks for the event
    UPDATE
      lrp_event_bucket evtbkt
    SET (
      evtbkt.routine_hrs,
      evtbkt.nr_hrs,
      evtbkt.total_hrs,
      evtbkt.nr_factor,
      evtbkt.assigned_hrs ) = (
        SELECT
          SUM( taskdefn.routine_hrs ),
          SUM( taskdefn.nr_hrs ),
          SUM( taskdefn.total_hrs ),
          DECODE( SUM( taskdefn.routine_hrs ), 0, 0, ( SUM( taskdefn.nr_hrs ) / SUM( taskdefn.routine_hrs ) ) ),
          0.00
        FROM
          lrp_task_defn taskdefn,
          lrp_event_workscope evtscope
        WHERE
           lrec_allLrpEvents.lrp_event_db_id = evtscope.lrp_event_db_id AND
           lrec_allLrpEvents.lrp_event_id = evtscope.lrp_event_id
           AND
           lrec_allLrpEvents.lrp_db_id = evtscope.lrp_db_id AND
           lrec_allLrpEvents.lrp_id = evtscope.lrp_id
           AND
           evtscope.lrp_db_id = taskdefn.lrp_db_id AND
           evtscope.lrp_id = taskdefn.lrp_id
           AND
           evtscope.task_defn_db_id = taskdefn.task_defn_db_id AND
           evtscope.task_defn_id = taskdefn.task_defn_id
           AND
           taskdefn.rstat_cd = 0 
           AND
           evtscope.rstat_cd = 0
      )
    WHERE
      evtbkt.lrp_plan_type_db_id = ltempVar1 AND
      evtbkt.lrp_plan_type_id = ltempVar2 
      AND
      evtbkt.lrp_event_db_id = lTempVar3 AND
      evtbkt.lrp_event_id = lTempVar4;
    
  END LOOP;
    
END;
/

--changeSet DEV-376:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_EVENT_BUCKET modify (
	"ROUTINE_HRS" Number(8,2) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-376:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_EVENT_BUCKET modify (
	"NR_FACTOR" Number(3,2) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-376:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_EVENT_BUCKET modify (
	"NR_HRS" Number(8,2) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-376:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_EVENT_BUCKET modify (
	"TOTAL_HRS" Number(10,2) NOT NULL DEFERRABLE
)
');
END;
/

--changeSet DEV-376:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table LRP_EVENT_BUCKET modify (
	"ASSIGNED_HRS" Number(8,2) NOT NULL DEFERRABLE
)
');
END;
/