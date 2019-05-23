--liquibase formatted sql


--changeSet DEV-382:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- DEV-382: Conditional migration script for new user type definitions
--
-- Author: Karan Mehta & Abhishek Sur
-- Date  : September 20, 2010
-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -
-- Drop the PLSQL procedure first, if it exists (in case the script is being
-- run again)
BEGIN
   UTL_MIGR_SCHEMA_PKG.PACKAGE_DROP('PLAN_COMPARISION_PKG');
END;
/

--changeSet DEV-382:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Drop and recreate the new user types first, if they exist (in case the 
-- migration script is being run again)
-- Drop the types first - complex first, then base types
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('FLEET_MTC');
END;
/

--changeSet DEV-382:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('T_TAB_SCHED_DIFF');
END;
/

--changeSet DEV-382:4 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('T_TAB_NEWRMVD');
END;
/

--changeSet DEV-382:5 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('T_TAB_SCHED_DIFF');
END;
/

--changeSet DEV-382:6 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('T_SCHED_DIFF_WP');
END;
/

--changeSet DEV-382:7 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('T_NEW_RMVD_WP');
END;
/

--changeSet DEV-382:8 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('T_BUFF_DIFF_WP');
END;
/

--changeSet DEV-382:9 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('FLEET_MTC_REC');
END;
/

--changeSet DEV-382:10 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create the types
CREATE OR REPLACE TYPE FLEET_MTC_REC AS OBJECT
(
  -- Author  : ABHISHEK
  -- Created : 6/29/2010 4:35:10 AM
  -- Purpose :

  -- Attributes
      task_name        VARCHAR(80),
      work_type        VARCHAR(4000),
      plan_1           NUMBER(1),
      plan_2           NUMBER(1),
      is_future        NUMBER(1)

);
/

--changeSet DEV-382:11 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE T_BUFF_DIFF_WP AS OBJECT
(
  -- Author  : ABHISHEK
  -- Created : 7/8/2010 8:59:59 AM
  -- Purpose :

  -- Attributes
  wp_name VARCHAR2(80),
  plan_type_cd VARCHAR2(80),
  plan_1_2 VARCHAR2(7),
  r_hours NUMBER(6,1),
  nr_factor NUMBER(6,1),
  nr_hours  NUMBER(6,1),
  tot_hours NUMBER(6,1),
  ass_hrs   NUMBER(6,1),
  fill_pct NUMBER(6,2),
  fill_pct_unit VARCHAR2(3),
  is_future NUMBER(1),
  wp_idx NUMBER(10),
  tz_cd VARCHAR2(50)

);
/

--changeSet DEV-382:12 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE T_NEW_RMVD_WP AS OBJECT
(
  -- Author  : ABHISHEK
  -- Created : 7/2/2010 8:59:59 AM
  -- Purpose :

  -- Attributes
  wp_name VARCHAR2(80),
  start_dt DATE,
  end_dt  DATE,
  loc_cd  VARCHAR2(80),
  workscopes VARCHAR2(4000),
  is_future NUMBER(1),
  tz_cd VARCHAR2(50)

);
/

--changeSet DEV-382:13 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE T_SCHED_DIFF_WP
 AS object (
  -- Author  : ABHISHEK
  -- Created : 7/6/2010 8:59:59 AM
  -- Purpose : Scheduling Differences in LRP

  -- Attributes
  wp_name VARCHAR2(80),
  plan_1_2 VARCHAR2(7),
  event_status_cd VARCHAR2(16),
  start_dt DATE,
  end_dt  DATE,
  loc_cd  VARCHAR2(80),
  workscopes VARCHAR2(4000),
  is_future NUMBER(1),
  wp_key VARCHAR2(22),
  wp_idx NUMBER(6),
  tz_cd VARCHAR2(50)
);
/

--changeSet DEV-382:14 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE T_TAB_BUFF_DIFF IS TABLE OF t_buff_diff_wp;
/

--changeSet DEV-382:15 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE T_TAB_NEWRMVD IS TABLE OF t_new_rmvd_wp;
/

--changeSet DEV-382:16 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE T_TAB_SCHED_DIFF IS TABLE OF t_sched_diff_wp;
/

--changeSet DEV-382:17 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE FLEET_MTC as TABLE OF fleet_mtc_rec;
/

--changeSet DEV-382:18 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create the body of the package that'll use the type definitions
-- that were created above
CREATE OR REPLACE PACKAGE BODY "PLAN_COMPARISION_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : PLAN_COMPARISION_PKG
   -- Object Type : Package Header
   -- Date        : June 28, 2010
   -- Coder       : Abhishek Sur
   -- Recent Date :
   -- Recent Coder:
   -- Description :
   -- This package contains methods for custom Plan Comparision report in maintenix from
   -- a PLSQL framework.
   --
   ----------------------------------------------------------------------------
   -- Copyright © 2009-2010 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------


   -----------------------------------------------------------------------------
   -- Local Package Types
   -----------------------------------------------------------------------------
    /*
    t_inv_id - Represents PK into LRP_INV_INV table
    */
    TYPE t_inv_id IS RECORD(
      plan_db_id  lrp_inv_inv.lrp_db_id%TYPE,
      plan_id     lrp_inv_inv.lrp_id%TYPE,
      plan_inv_id lrp_inv_inv.lrp_inv_inv_id%TYPE);
    /*
    t_varr_inv - Array of t_inv_id. Size limited to 2 as an aircraft common to
                 specified two plans will have more 2 relevant entries in
                 LRP_INV_INV
    */
    TYPE t_varr_inv IS VARRAY(2) OF t_inv_id;

    /*
    t_common_acft - Represents a common aircraft.
                  - Data maintained in this type is as follows
                  - Inventory Key into Mx
                  - Aircraft Registration Code
                  - Aircraft Serial No OEM
                  - Flag to set when aircraft exists only in LRP (Future)
                  - Plan-specific keys
    */
    TYPE t_common_acft is RECORD(
        inv_no_db_id lrp_inv_inv.inv_no_db_id%TYPE,
        inv_no_id    lrp_inv_inv.inv_no_id%TYPE,
        ac_reg_cd    lrp_inv_inv.ac_reg_cd%TYPE,
        serial_no_oem   lrp_inv_inv.serial_no_oem%TYPE,
        is_future    NUMBER(1),
        plan_keys    t_varr_inv
    );

    /*
    t_tab_common_acft - Table of all aircraft that are common to both specified plans
    */
    TYPE t_tab_common_acft IS TABLE OF t_common_acft;

    /*
    t_plan_common - Represents a Planning Type
                  - Planning Type in Mx
                  - Corresponding Key in Plan 1 to be compared
                  - Corresponding Key in Plan 2 to be compared
    */
    TYPE t_plan_common IS RECORD(
        planning_type_db_id lrp_plan_type.planning_type_db_id%TYPE,
        planning_type_id    lrp_plan_type.planning_type_id%TYPE,
        plan_1_type_db_id   lrp_plan_type.lrp_plan_type_db_id%TYPE,
        plan_1_type_id      lrp_plan_type.lrp_plan_type_id%TYPE,
        plan_2_type_db_id   lrp_plan_type.lrp_plan_type_db_id%TYPE,
        plan_2_type_id      lrp_plan_type.lrp_plan_type_id%TYPE
    );

    /*
    t_common_plan_types - Table of all plan types that are common to both specified plans
    */
    TYPE t_common_plan_types IS TABLE OF t_plan_common;


   -----------------------------------------------------------------------------
   -- Local Package Constants
   -----------------------------------------------------------------------------
   c_pkg_name     CONSTANT VARCHAR2(30) := 'PLAN_COMPARISION_PKG';
   gv_err_msg_gen CONSTANT VARCHAR2(2000) := 'An Oracle error has occurred details are as follows: ';


   -----------------------------------------------------------------------------
   -- Local Package Variables
   -----------------------------------------------------------------------------
   v_step     NUMBER(3);
   v_err_code NUMBER;
   v_err_msg  VARCHAR(2000);

  ----------------------------------------------------------------------------
  -- Function :   calculateTotalFillPercentage
  -- Arguments:
  --              aBuffDiffWP  (t_buff_diff_wp%type) -- aBuffer Difference element
  -- Description: Given a buffer difference object, calculate and set the fill_pct
  --               as total_assigned_hours/total_scheduled_hours * 100;
  ----------------------------------------------------------------------------
  FUNCTION calculateTotalFillPercentage( aBuffDiffWP t_buff_diff_wp ) RETURN NUMBER IS
  BEGIN
  IF aBuffDiffWP.tot_hours > 0 THEN
     -- IN parameters
     RETURN ( aBuffDiffWP.ass_hrs / aBuffDiffWP.tot_hours ) * 100;
  ELSE
     RETURN NULL;
  END IF;
  END calculateTotalFillPercentage;


  ----------------------------------------------------------------------------
  -- Function :   get_common_acft
  --
  -- Arguments:
  --              p_PlanOne  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
  --              p_PlanTwo  (lrp_plan.desc_sdesc%TYPE) -- Name of the Second Plan
  --
  -- Description:  Retrieves all common aircraft (both future and actual)
  ----------------------------------------------------------------------------

  FUNCTION get_common_acft(
                           p_PlanOne lrp_plan.desc_sdesc%TYPE,
                           p_PlanTwo  lrp_plan.desc_sdesc%TYPE
                          ) RETURN t_tab_common_acft IS

          /*
          Instantiate an object of t_tab_common_acft to be returned
          */
          t_acft_table t_tab_common_acft := t_tab_common_acft();

          index_ NUMBER := 1;

          CURSOR lcur_Acft( aPlanOne lrp_plan.desc_sdesc%TYPE, aPlanTwo lrp_plan.desc_sdesc%TYPE ) IS
              -- find actual aircraft common to both plans
              SELECT
                    common_actual.*
              FROM
              (SELECT
                  lrp_inv_inv.inv_no_db_id,
                  lrp_inv_inv.inv_no_id,
                  inv_ac_reg.ac_reg_cd,
                  inv_inv.serial_no_oem,
                  0 AS is_future
              FROM
                  lrp_inv_inv JOIN lrp_plan ON
                  lrp_inv_inv.lrp_db_id = lrp_plan.lrp_db_id AND
                  lrp_inv_inv.lrp_id    = lrp_plan.lrp_id
                  JOIN inv_inv ON
                  inv_inv.inv_no_db_id = lrp_inv_inv.inv_no_db_id AND
                  inv_inv.inv_no_id    = lrp_inv_inv.inv_no_id
                  JOIN inv_ac_reg ON
                  inv_ac_reg.inv_no_db_id = inv_inv.inv_no_db_id AND
                  inv_ac_reg.inv_no_id    = inv_inv.inv_no_id
              WHERE
                   lrp_plan.desc_sdesc = aPlanOne
              INTERSECT
              SELECT
                  lrp_inv_inv.inv_no_db_id,
                  lrp_inv_inv.inv_no_id,
                  inv_ac_reg.ac_reg_cd,
                  inv_inv.serial_no_oem,
                  0 AS is_future
              FROM
                  lrp_inv_inv JOIN lrp_plan ON
                  lrp_inv_inv.lrp_db_id = lrp_plan.lrp_db_id AND
                  lrp_inv_inv.lrp_id    = lrp_plan.lrp_id
                  JOIN inv_inv ON
                  inv_inv.inv_no_db_id = lrp_inv_inv.inv_no_db_id AND
                  inv_inv.inv_no_id    = lrp_inv_inv.inv_no_id
                  JOIN inv_ac_reg ON
                  inv_ac_reg.inv_no_db_id = inv_inv.inv_no_db_id AND
                  inv_ac_reg.inv_no_id    = inv_inv.inv_no_id
              WHERE
                   lrp_plan.desc_sdesc = aPlanTwo ) common_actual


              UNION


              -- find future aircraft common to both plans
              SELECT
                    common_future.*
              FROM
              (SELECT
                  lrp_inv_inv.inv_no_db_id,
                  lrp_inv_inv.inv_no_id,
                  ac_reg_cd,
                  serial_no_oem,
                  1 AS is_future
              FROM
                  lrp_inv_inv JOIN lrp_plan ON
                  lrp_inv_inv.lrp_db_id = lrp_plan.lrp_db_id AND
                  lrp_inv_inv.lrp_id    = lrp_plan.lrp_id
              WHERE
                   lrp_plan.desc_sdesc = aPlanOne
                   AND
                   inv_no_db_id IS NULL AND
                   inv_no_id IS NULL
              INTERSECT
              SELECT
                  lrp_inv_inv.inv_no_db_id,
                  lrp_inv_inv.inv_no_id,
                  ac_reg_cd,
                  serial_no_oem,
                  1 AS is_future
              FROM
                  lrp_inv_inv JOIN lrp_plan ON
                  lrp_inv_inv.lrp_db_id = lrp_plan.lrp_db_id AND
                  lrp_inv_inv.lrp_id    = lrp_plan.lrp_id
              WHERE
                   lrp_plan.desc_sdesc = aPlanTwo
                   AND
                   inv_no_db_id IS NULL AND
                   inv_no_id IS NULL) common_future;

          CURSOR lcur_AcftLrpKeys( aPlanOne lrp_plan.desc_sdesc%TYPE, aPlanTwo lrp_plan.desc_sdesc%TYPE, aInvNoDbId lrp_inv_inv.inv_no_db_id%TYPE, aInvNoId lrp_inv_inv.inv_no_id%TYPE, aRegCd lrp_inv_inv.ac_reg_cd%TYPE, aSerialOEM lrp_inv_inv.serial_no_oem%TYPE) IS
              SELECT
                    lrp_plan.lrp_db_id,
                    lrp_plan.lrp_id,
                    lrp_inv_inv_id,
                    lrp_plan.desc_sdesc
              FROM
                   lrp_inv_inv JOIN lrp_plan ON
                   lrp_inv_inv.lrp_db_id = lrp_plan.lrp_db_id AND
                   lrp_inv_inv.lrp_id    = lrp_plan.lrp_id
              WHERE
                   lrp_plan.desc_sdesc = aPlanOne
                   AND
                   DECODE(aInvNoDbId, NULL, ac_reg_cd, inv_no_db_id) =
                   DECODE(aInvNoDbId, NULL, aRegCd, aInvNoDbId)
                   AND
                   DECODE(aInvNoId, NULL, serial_no_oem, inv_no_id) =
                   DECODE(aInvNoId, NULL, aSerialOEM, aInvNoId)
              UNION
              SELECT
                    lrp_plan.lrp_db_id,
                    lrp_plan.lrp_id,
                    lrp_inv_inv_id,
                    lrp_plan.desc_sdesc
              FROM
                   lrp_inv_inv JOIN lrp_plan ON
                   lrp_inv_inv.lrp_db_id = lrp_plan.lrp_db_id AND
                   lrp_inv_inv.lrp_id    = lrp_plan.lrp_id
              WHERE
                   lrp_plan.desc_sdesc = aPlanTwo
                   AND
                   DECODE(aInvNoDbId, NULL, ac_reg_cd, inv_no_db_id) =
                   DECODE(aInvNoDbId, NULL, aRegCd, aInvNoDbId)
                   AND
                   DECODE(aInvNoId, NULL, serial_no_oem, inv_no_id) =
                   DECODE(aInvNoId, NULL, aSerialOEM, aInvNoId) ;


             lrec_AcftLrpKeys lcur_AcftLrpKeys%ROWTYPE;

          BEGIN

          OPEN lcur_Acft( p_PlanOne, p_PlanTwo );

          LOOP

           EXIT WHEN lcur_Acft%NOTFOUND;

           t_acft_table.EXTEND;

           -- retrieve each common aircraft data; load it into the custom table
           FETCH lcur_Acft INTO t_acft_table(index_).inv_no_db_id, t_acft_table(index_).inv_no_id, t_acft_table(index_).ac_reg_cd, t_acft_table(index_).serial_no_oem, t_acft_table(index_).is_future;
           
               t_acft_table(index_).plan_keys := NEW t_varr_inv();

               t_acft_table(index_).plan_keys.EXTEND;
               t_acft_table(index_).plan_keys.EXTEND;
               
               --LOOP


               FOR lrec_AcftLrpKeys IN lcur_AcftLrpKeys( p_PlanOne, p_PlanTwo, t_acft_table(index_).inv_no_db_id, t_acft_table(index_).inv_no_id, t_acft_table(index_).ac_reg_cd, t_acft_table(index_).serial_no_oem )
                  -- hence identify the specific key for each aircraft in the context of the specific plan
               LOOP


                  IF lrec_AcftLrpKeys.desc_sdesc = p_PlanOne THEN
                     
                     t_acft_table(index_).plan_keys(t_acft_table(index_).plan_keys.FIRST).plan_db_id    := lrec_AcftLrpKeys.lrp_db_id;
                     t_acft_table(index_).plan_keys(t_acft_table(index_).plan_keys.FIRST).plan_id       := lrec_AcftLrpKeys.lrp_id;
                     t_acft_table(index_).plan_keys(t_acft_table(index_).plan_keys.FIRST).plan_inv_id   := lrec_AcftLrpKeys.lrp_inv_inv_id;

                  ELSE
                     
                     t_acft_table(index_).plan_keys(t_acft_table(index_).plan_keys.LAST).plan_db_id  := lrec_AcftLrpKeys.lrp_db_id;
                     t_acft_table(index_).plan_keys(t_acft_table(index_).plan_keys.LAST).plan_id     := lrec_AcftLrpKeys.lrp_id;
                     t_acft_table(index_).plan_keys(t_acft_table(index_).plan_keys.LAST).plan_inv_id := lrec_AcftLrpKeys.lrp_inv_inv_id;                  

                  END IF;
                              
                              
               END LOOP;



           index_ := index_ + 1;

          END LOOP;

          t_acft_table.TRIM;

          RETURN t_acft_table;
  END get_common_acft;

  ----------------------------------------------------------------------------
  -- Function :      get_common_taskdefn
  -- Arguments       p_FirstPlan (lrp_plan.desc_sdesc%TYPE)
  --                 p_SecondPlan ( -do- )
  -- Description : Find task-definition keys common to both plans
  ----------------------------------------------------------------------------
  FUNCTION get_common_taskdefn(
                               p_FirstPlan lrp_plan.desc_sdesc%TYPE,
                               p_SecondPlan lrp_plan.desc_sdesc%TYPE
                              )RETURN mxkeytable IS

           /*
           Instantiate the return object
           */
           t_task_defn_table mxkeytable := mxkeytable();


           CURSOR lcur_CommonTaskDefn( aPlanOne lrp_plan.desc_sdesc%TYPE, aPlanTwo  lrp_plan.desc_sdesc%TYPE ) IS
           /*
           Given the plan names, find all task definitions associated with each plan
           Hence return only those task definition keys that are common to both
           */
           SELECT
               lrp_task_defn.task_defn_db_id,
               lrp_task_defn.task_defn_id
           FROM
               lrp_task_defn JOIN lrp_plan ON
               lrp_task_defn.lrp_db_id = lrp_plan.lrp_db_id AND
               lrp_task_defn.lrp_id    = lrp_plan.lrp_id
           WHERE
                lrp_plan.desc_sdesc = aPlanOne
           INTERSECT
           SELECT
               lrp_task_defn.task_defn_db_id,
               lrp_task_defn.task_defn_id
           FROM
               lrp_task_defn JOIN lrp_plan ON
               lrp_task_defn.lrp_db_id = lrp_plan.lrp_db_id AND
               lrp_task_defn.lrp_id    = lrp_plan.lrp_id
           WHERE
               lrp_plan.desc_sdesc = aPlanTwo ;

           lrec_CommonTaskDefn lcur_CommonTaskDefn%ROWTYPE;

           BEGIN
               OPEN lcur_CommonTaskDefn( p_FirstPlan, p_SecondPlan );
               LOOP
               -- Loop and write the task-definitions into the return object
                   EXIT WHEN lcur_CommonTaskDefn%NOTFOUND;

                   t_task_defn_table.EXTEND;

                   FETCH lcur_CommonTaskDefn INTO lrec_CommonTaskDefn;

                   t_task_defn_table(t_task_defn_table.LAST) := mxkey(lrec_CommonTaskDefn.Task_Defn_Db_Id, lrec_CommonTaskDefn.Task_Defn_Id );

               END LOOP;
               -- one extra added at the end; get rid of it
               t_task_defn_table.TRIM;

           RETURN t_task_defn_table;

  END get_common_taskdefn;

----------------------------------------------------------------------------
-- Function:    get_planning_types
-- Arguments    p_FirstPlan (lrp_plan.desc_sdesc%TYPE)
--              p_SecondPlan(  -- do -- )
-- Description: Find Planning Types for both plans
----------------------------------------------------------------------------

FUNCTION get_planning_types(
                           p_FirstPlan lrp_plan.desc_sdesc%TYPE,
                           p_SecondPlan lrp_plan.desc_sdesc%TYPE
                          ) RETURN t_common_plan_types  IS

CURSOR lCurPlanningType ( aPlanName lrp_plan.desc_sdesc%TYPE ) IS
-- Given a plan name, return all planning types associated with it
SELECT
    lrp_plan_type.planning_type_db_id ,
    lrp_plan_type.planning_type_id,
    lrp_plan_type.lrp_plan_type_db_id,
    lrp_plan_type.lrp_plan_type_id
FROM
   lrp_plan_type JOIN lrp_plan ON
   lrp_plan_type.lrp_db_id = lrp_plan.lrp_db_id AND
   lrp_plan_type.lrp_id    = lrp_plan.lrp_id
WHERE
   lrp_plan.desc_sdesc = aPlanName
   ;

CURSOR lCurSecondPlanning( aPlanName lrp_plan.desc_sdesc%TYPE, aPlanningTypeDbId lrp_plan_type.planning_type_db_id%TYPE, aPlanningTypeId lrp_plan_type.planning_type_id%TYPE ) IS
SELECT
    lrp_plan_type.lrp_plan_type_db_id,
    lrp_plan_type.lrp_plan_type_id
FROM
    lrp_plan_type JOIN lrp_plan ON
    lrp_plan_type.lrp_db_id = lrp_plan.lrp_db_id AND
    lrp_plan_type.lrp_id    = lrp_plan.lrp_id
WHERE
    lrp_plan.desc_sdesc = aPlanName
    AND
    lrp_plan_type.planning_type_db_id = aPlanningTypeDbId AND
    lrp_plan_type.planning_type_id    = aPlanningTypeId
   ;

lrec_SecondPlanning lCurSecondPlanning%ROWTYPE;
lrec_PlanTypes lCurPlanningType%ROWTYPE;

lCommonPlanTypes t_common_plan_types  ;
lIsExist NUMBER;

BEGIN

-- instantiate the return object
lCommonPlanTypes := NEW t_common_plan_types();

FOR lrec_PlanTypes IN lCurPlanningType( p_FirstPlan )
-- read all plan types for the First Plan
LOOP
    -- extend the returnable by one row
    lCommonPlanTypes.EXTEND;
    -- insert data into it from plan 1
    lCommonPlanTypes( lCommonPlanTypes.LAST ).planning_type_db_id := lrec_PlanTypes.planning_type_db_id;
    lCommonPlanTypes( lCommonPlanTypes.LAST ).planning_type_id := lrec_PlanTypes.planning_type_id;
    lCommonPlanTypes( lCommonPlanTypes.LAST ).plan_1_type_db_id := lrec_PlanTypes.lrp_plan_type_db_id;
    lCommonPlanTypes( lCommonPlanTypes.LAST ).plan_1_type_id := lrec_PlanTypes.lrp_plan_type_id;

    -- identify the corresponding plan type key in plan 2

    OPEN lCurSecondPlanning(p_SecondPlan, lrec_PlanTypes.planning_type_db_id, lrec_PlanTypes.planning_type_id );
    FETCH lCurSecondPlanning INTO lrec_SecondPlanning;
    IF lCurSecondPlanning%FOUND THEN
        lCommonPlanTypes( lCommonPlanTypes.LAST).plan_2_type_db_id := lrec_SecondPlanning.lrp_plan_type_db_id;
        lCommonPlanTypes( lCommonPlanTypes.LAST).plan_2_type_id := lrec_SecondPlanning.lrp_plan_type_id;
    END IF;
    CLOSE lCurSecondPlanning;
END LOOP;



-- at this stage, there may be planning types in P2 which do not exist in P1; run the above loop for the second plan
FOR lrec_PlanTypes IN lCurPlanningType( p_SecondPlan )
-- read all plan types for the Second Plan
LOOP
     lIsExist := 0;
     FOR myIter IN 1 .. lCommonPlanTypes.COUNT
     LOOP
         IF
            lCommonPlanTypes(myIter).planning_type_db_id = lrec_PlanTypes.Planning_Type_Db_Id AND
            lCommonPlanTypes(myIter).planning_type_id    = lrec_PlanTypes.Planning_Type_Id
         THEN
             lIsExist := 1;
             EXIT;
         END IF;
     END LOOP;


    -- Test whether the planning type already exists in the return object
     IF lIsExist = 0 THEN
        -- planning type does not exist in the return object; set it up
        -- If the planning type existed in the first plan it would already be in the returnable
        -- The implication is this planning type only applies to the second plan.
        -- Therefore it is set-up with null entries for the first plan
        -- extend the returnable by one row
        lCommonPlanTypes.EXTEND;
         -- insert data into it from plan 1
        lCommonPlanTypes(lCommonPlanTypes.LAST).planning_type_db_id := lrec_PlanTypes.planning_type_db_id;
        lCommonPlanTypes(lCommonPlanTypes.LAST).planning_type_id := lrec_PlanTypes.planning_type_id;
        lCommonPlanTypes(lCommonPlanTypes.LAST).plan_2_type_db_id := lrec_PlanTypes.lrp_plan_type_db_id;
        lCommonPlanTypes(lCommonPlanTypes.LAST).plan_2_type_id :=  lrec_PlanTypes.lrp_plan_type_id;
     END IF;
END LOOP;

RETURN lCommonPlanTypes;

END get_planning_types;




   ----------------------------------------------------------------------------
   -- Function :   get_plan_events
   -- Arguments:
   --              p_PlanName  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --
   -- Description:  Retrieves all events bound to the specified plan
   ----------------------------------------------------------------------------

   FUNCTION get_plan_events(
                            p_PlanName lrp_plan.desc_sdesc%TYPE
                           ) RETURN mxkeytable IS


           t_event_table mxkeytable := mxkeytable();

           index_ NUMBER := 1;

           CURSOR lcur_Events(aPlan lrp_plan.desc_sdesc%TYPE) IS
               SELECT
                    lrp_event.lrp_event_db_id,
                    lrp_event.lrp_event_id
               FROM
                    lrp_event JOIN lrp_plan ON
                    lrp_event.lrp_db_id = lrp_plan.lrp_db_id AND
                    lrp_event.lrp_id    = lrp_plan.lrp_id
               WHERE
                    lrp_plan.desc_sdesc = aPlan;

           BEGIN

           OPEN lcur_Events( p_PlanName );

           LOOP

            t_event_table.EXTEND;
            FETCH lcur_Events INTO t_event_table(index_).db_id, t_event_table(index_).id;
            index_ := index_ + 1;

            EXIT WHEN lcur_Events%NOTFOUND;

           END LOOP;

           t_event_table.TRIM;

           RETURN t_event_table;
   END get_plan_events;


   ----------------------------------------------------------------------------
   -- Function :   compare_work_packages_buffer
   -- Arguments:
   --              p_PlanOne  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --              p_PlanTwo  (lrp_plan.desc_sdesc%TYPE) -- Name of the Second Plan
   -- Description: Compares work-packages for the plans; Hence compares their
   --              buffering as a function of the sequence
   ----------------------------------------------------------------------------

   FUNCTION compare_work_packages_buffer(
                p_PlanOne  lrp_plan.desc_sdesc%TYPE,
                p_PlanTwo  lrp_plan.desc_sdesc%TYPE,
                p_StartDt  lrp_event.start_dt%TYPE,
                p_EndDt    lrp_event.end_dt%TYPE
            ) RETURN t_tab_buff_diff IS

    t_common_aeroplane t_tab_common_acft ;
    t_common_task_defn mxkeytable;
    t_buff_diff t_tab_buff_diff := NEW t_tab_buff_diff();
    lCommonPlanTypes t_common_plan_types;

    CURSOR lcur_PlanOneEventsForTaskDefn( aPlanName lrp_plan.desc_sdesc%TYPE, aTaskDefnDbId lrp_task_defn.task_defn_db_id%TYPE, aTaskDefnId lrp_task_defn.task_defn_id%TYPE, aLrpDbId lrp_event.lrp_db_id%TYPE, aLrpId lrp_event.lrp_id%TYPE, aLrpInvInvId lrp_event.lrp_inv_inv_id%TYPE, aStartDt lrp_event.start_dt%TYPE, aEndDt lrp_event.end_dt%TYPE ) IS
           SELECT
                lrp_event.lrp_event_db_id,
                lrp_event.lrp_event_id,
                lrp_event.lrp_sdesc,
                lrp_event_workscope.deadline_dt,
                lrp_event.start_dt,
                lrp_event.end_dt,
                lrp_event.event_status_cd,
                count(*) OVER ( PARTITION BY 1 ) AS rows_in_result
           FROM
                lrp_event_workscope JOIN lrp_plan ON
                lrp_event_workscope.lrp_db_id = lrp_plan.lrp_db_id AND
                lrp_event_workscope.lrp_id    = lrp_plan.lrp_id
                JOIN lrp_event ON
                lrp_event.lrp_event_db_id = lrp_event_workscope.lrp_event_db_id AND
                lrp_event.lrp_event_id    = lrp_event_workscope.lrp_event_id
                AND
                lrp_event.lrp_db_id       = lrp_plan.lrp_db_id AND
                lrp_event.lrp_id          = lrp_plan.lrp_id
           WHERE
                lrp_plan.desc_sdesc = aPlanName
                AND
                lrp_event_workscope.task_defn_db_id = aTaskDefnDbId AND
                lrp_event_workscope.task_defn_id    = aTaskDefnId
                AND
                lrp_event.lrp_db_id = aLrpDbId AND
                lrp_event.lrp_id    = aLrpId AND
                lrp_event.lrp_inv_inv_id = aLrpInvInvId
                AND
                lrp_event.start_dt > (aStartDt-1)
                AND
                lrp_event.end_dt < (aEndDt+1)
           GROUP BY
                lrp_event.lrp_event_db_id,
                lrp_event.lrp_event_id,
                lrp_event.lrp_sdesc,
                lrp_event_workscope.deadline_dt,
                lrp_event.start_dt,
                lrp_event.end_dt,
                lrp_event.event_status_cd
           ORDER BY
                lrp_event_workscope.deadline_dt;


    CURSOR lcur_PlanTwoEventsForTaskDefn( aPlanName lrp_plan.desc_sdesc%TYPE, aTaskDefnDbId lrp_task_defn.task_defn_db_id%TYPE, aTaskDefnId lrp_task_defn.task_defn_id%TYPE, aLrpDbId lrp_event.lrp_db_id%TYPE, aLrpId lrp_event.lrp_id%TYPE, aLrpInvInvId lrp_event.lrp_inv_inv_id%TYPE, aStartDt lrp_event.start_dt%TYPE, aEndDt lrp_event.end_dt%TYPE ) IS
           SELECT
                lrp_event.lrp_event_db_id,
                lrp_event.lrp_event_id,
                lrp_event.lrp_sdesc,
                lrp_event_workscope.deadline_dt,
                lrp_event.start_dt,
                lrp_event.end_dt,
                lrp_event.event_status_cd,
                count(*) OVER ( PARTITION BY 1 ) AS rows_in_result
           FROM
                lrp_event_workscope JOIN lrp_plan ON
                lrp_event_workscope.lrp_db_id = lrp_plan.lrp_db_id AND
                lrp_event_workscope.lrp_id    = lrp_plan.lrp_id
                JOIN lrp_event ON
                lrp_event.lrp_event_db_id = lrp_event_workscope.lrp_event_db_id AND
                lrp_event.lrp_event_id    = lrp_event_workscope.lrp_event_id
                AND
                lrp_event.lrp_db_id       = lrp_plan.lrp_db_id AND
                lrp_event.lrp_id          = lrp_plan.lrp_id
           WHERE
                lrp_plan.desc_sdesc = aPlanName
                AND
                lrp_event_workscope.task_defn_db_id = aTaskDefnDbId AND
                lrp_event_workscope.task_defn_id    = aTaskDefnId
                AND
                lrp_event.lrp_db_id = aLrpDbId AND
                lrp_event.lrp_id    = aLrpId AND
                lrp_event.lrp_inv_inv_id = aLrpInvInvId
                AND
                lrp_event.start_dt > (aStartDt-1)
                AND
                lrp_event.end_dt < (aEndDt+1)
           GROUP BY
                lrp_event.lrp_event_db_id,
                lrp_event.lrp_event_id,
                lrp_event.lrp_sdesc,
                lrp_event_workscope.deadline_dt,
                lrp_event.start_dt,
                lrp_event.end_dt,
                lrp_event.event_status_cd
           ORDER BY
                lrp_event_workscope.deadline_dt;

    CURSOR lcur_BuffersForEvent( aEventDbId lrp_event.lrp_event_db_id%TYPE, aEventId lrp_event.lrp_event_id%TYPE ) IS
           SELECT
              lrp_event_bucket.routine_hrs,
              lrp_event_bucket.nr_factor,
              lrp_event_bucket.nr_hrs,
              lrp_event_bucket.total_hrs,
              lrp_event_bucket.assigned_hrs,
              CASE
                WHEN total_hrs = 0 AND fill_pct <> 0 THEN
                    lrp_event_bucket.total_hrs
                ELSE
                    lrp_event_bucket.fill_pct
              END AS fill_pct,
              eqp_planning_type.planning_type_cd,
              lrp_plan_type.planning_type_db_id,
              lrp_plan_type.planning_type_id,
              CASE
                              WHEN total_hrs = 0 AND fill_pct <> 0 THEN
                                  'hrs'
                              ELSE
                                  '%'
              END AS fill_pct_unit
           FROM
              lrp_event_bucket JOIN lrp_plan_type ON
              lrp_plan_type.lrp_plan_type_db_id = lrp_event_bucket.lrp_plan_type_db_id AND
              lrp_plan_type.lrp_plan_type_id    = lrp_event_bucket.lrp_plan_type_id
              JOIN eqp_planning_type ON
              eqp_planning_type.planning_type_db_id = lrp_plan_type.planning_type_db_id AND
              eqp_planning_type.planning_type_id    = lrp_plan_type.planning_type_id
           WHERE
              lrp_event_bucket.lrp_event_db_id = aEventDbId AND
              lrp_event_bucket.lrp_event_id    = aEventId;

    CURSOR lcur_BuffersForEventWithType( aEventDbId lrp_event.lrp_event_db_id%TYPE, aEventId lrp_event.lrp_event_id%TYPE, aPlanTypeDbId lrp_plan_type.planning_type_db_id%TYPE, aPlanTypeId lrp_plan_type.planning_type_db_id%TYPE ) IS
    SELECT
         lrp_event_bucket.routine_hrs,
         lrp_event_bucket.nr_factor,
         lrp_event_bucket.nr_hrs,
         lrp_event_bucket.total_hrs,
         CASE
           WHEN total_hrs = 0 AND fill_pct <> 0 THEN
               lrp_event_bucket.total_hrs
           ELSE
               lrp_event_bucket.fill_pct
         END AS fill_pct,
         lrp_event_bucket.assigned_hrs,
         CASE
             WHEN total_hrs = 0 AND fill_pct <> 0 THEN
                 'hrs'
             ELSE
                 '%'
         END AS fill_pct_unit
    FROM
         lrp_event_bucket
    WHERE
         lrp_event_bucket.lrp_event_db_id = aEventDbId AND
         lrp_event_bucket.lrp_event_id    = aEventId
         AND
         lrp_event_bucket.lrp_plan_type_db_id = aPlanTypeDbId AND
         lrp_event_bucket.lrp_plan_type_id    = aPlanTypeId;

    lrec_BuffersForEventWithType lcur_BuffersForEventWithType%ROWTYPE;

    lrec_EventsForTaskDefnPlanOne lcur_PlanOneEventsForTaskDefn%ROWTYPE;
    lrec_EventsForTaskDefnPlanTwo lcur_PlanTwoEventsForTaskDefn%ROWTYPE;

    lPlanningTypeDbId eqp_planning_type.planning_type_db_id%TYPE;
    lPlanningTypeId   eqp_planning_type.planning_type_id%TYPE;

    event_1_total t_buff_diff_wp;
    event_2_total t_buff_diff_wp;

    li_InitialRowCount NUMBER;

    li_EventIndex NUMBER := 1;


       lc_timezone VARCHAR2(50);

    BEGIN
        
        SELECT 
            user_cd
        INTO
            lc_timezone
        FROM
            utl_timezone  
        WHERE
            default_bool=1;

      -- Find all events common to both plans
      -- identify all common aircraft
          t_common_aeroplane := get_common_acft(p_PlanOne, p_PlanTwo);
      -- identify all common task-definitions
          t_common_task_defn := get_common_taskdefn(p_PlanOne, p_PlanTwo);

          lCommonPlanTypes := get_planning_types( p_PlanOne, p_PlanTwo );

          FOR iter IN NVL( t_common_aeroplane.FIRST, 0 ) .. NVL( t_common_aeroplane.LAST, -1 )
          LOOP
             -- iterate over each common aircraft
             FOR task_def_iter IN NVL( t_common_task_defn.FIRST,0 ) .. NVL( t_common_task_defn.LAST, -1 )
             LOOP
                 li_InitialRowCount := t_buff_diff.count() ;


                 -- iterate over each requirement/block common to both plans; find all work-packages where the task-definition is in-scope
                   OPEN lcur_PlanTwoEventsForTaskDefn( p_PlanTwo, t_common_task_defn(task_def_iter).db_id, t_common_task_defn(task_def_iter).id, t_common_aeroplane(iter).plan_keys(2).plan_db_id, t_common_aeroplane(iter).plan_keys(2).plan_id, t_common_aeroplane(iter).plan_keys(2).plan_inv_id, p_StartDt, p_EndDt  ) ;

                   FOR lrec_EventsForTaskDefnPlanOne IN lcur_PlanOneEventsForTaskDefn( p_PlanOne, t_common_task_defn(task_def_iter).db_id, t_common_task_defn(task_def_iter).id, t_common_aeroplane(iter).plan_keys(1).plan_db_id, t_common_aeroplane(iter).plan_keys(1).plan_id, t_common_aeroplane(iter).plan_keys(1).plan_inv_id, p_StartDt, p_EndDt )
                   LOOP

                      event_1_total := NEW t_buff_diff_wp( NULL, 'TOTAL', 'Plan 1', 0, 0, 0, 0, 0, NULL, NULL, NULL, li_EventIndex, lc_timezone );
                      event_2_total := NEW t_buff_diff_wp( NULL, 'TOTAL', 'Plan 2', 0, 0, 0, 0, 0, NULL, NULL, NULL, li_EventIndex, lc_timezone );

                      FETCH lcur_PlanTwoEventsForTaskDefn INTO lrec_EventsForTaskDefnPlanTwo;

                      IF lcur_PlanTwoEventsForTaskDefn%FOUND THEN
                      -- there are corresponding work-packages containing this task-definition in scope for Plan 2

                          FOR lrec_BuffersForEvent IN lcur_BuffersForEvent( lrec_EventsForTaskDefnPlanOne.lrp_event_db_id, lrec_EventsForTaskDefnPlanOne.lrp_event_id )
                          LOOP

                            -- Read buffer data for plan one work-package
                            -- Increment the collection size
                            t_buff_diff.EXTEND;

                            -- write relevant buffer detail for this work-package into the collection
                            t_buff_diff( t_buff_diff.LAST ) := NEW t_buff_diff_wp( NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,li_EventIndex, lc_timezone );
                            t_buff_diff( t_buff_diff.LAST ).wp_name := lrec_EventsForTaskDefnPlanOne.lrp_sdesc || '( '|| to_char(lrec_EventsForTaskDefnPlanOne.start_dt,'dd-MON-yyyy hh24:mi:ss') || ' )';
                            t_buff_diff( t_buff_diff.LAST ).plan_type_cd :=  lrec_BuffersForEvent.planning_type_cd;
                            t_buff_diff( t_buff_diff.LAST ).plan_1_2 :=  'Plan 1';
                            t_buff_diff( t_buff_diff.LAST ).r_hours :=  lrec_BuffersForEvent.routine_hrs;

                            event_1_total.wp_name := lrec_EventsForTaskDefnPlanOne.lrp_sdesc || '( '|| to_char(lrec_EventsForTaskDefnPlanOne.start_dt,'dd-MON-yyyy hh24:mi:ss') || ' )';
                            event_1_total.r_hours := event_1_total.r_hours + lrec_BuffersForEvent.routine_hrs;

                            t_buff_diff( t_buff_diff.LAST ).nr_factor :=  lrec_BuffersForEvent.nr_factor;

                            t_buff_diff( t_buff_diff.LAST ).nr_hours :=  lrec_BuffersForEvent.Nr_Hrs;

                            event_1_total.nr_hours := event_1_total.nr_hours + lrec_BuffersForEvent.nr_hrs;

                            t_buff_diff( t_buff_diff.LAST ).tot_hours :=  lrec_BuffersForEvent.total_hrs;

                            event_1_total.tot_hours := event_1_total.tot_hours + lrec_BuffersForEvent.total_hrs;

                            t_buff_diff( t_buff_diff.LAST ).fill_pct :=  lrec_BuffersForEvent.fill_pct ;

                            t_buff_diff( t_buff_diff.LAST ).ass_hrs := lrec_BuffersForEvent.assigned_hrs;

                            event_1_total.ass_hrs := event_1_total.ass_hrs + lrec_BuffersForEvent.assigned_hrs;

                            t_buff_diff( t_buff_diff.LAST ).fill_pct_unit := lrec_BuffersForEvent.fill_pct_unit;

                            t_buff_diff( t_buff_diff.LAST ).is_future := t_common_aeroplane(iter).is_future;
                            IF event_1_total.r_hours > 0 THEN
                               event_1_total.nr_factor := ROUND(event_1_total.nr_hours / event_1_total.r_hours,2);
                            END IF;
                            -- Find corresponding plan type buffer data for plan 2
                            -- Read plan type from plan 1; hence find corresponding entry in plan 2
                            lPlanningTypeDbId := NULL;
                            lPlanningTypeId := NULL;

                            FOR myIter IN 1 .. lCommonPlanTypes.COUNT
                            LOOP

                            IF
                               lCommonPlanTypes(myIter).planning_type_db_id = lrec_BuffersForEvent.planning_type_db_id AND
                               lCommonPlanTypes(myIter).planning_type_id    = lrec_BuffersForEvent.planning_type_id
                            THEN
                                lPlanningTypeDbId := lCommonPlanTypes( myIter ).plan_2_type_db_id;
                                lPlanningTypeId   := lCommonPlanTypes( myIter ).plan_2_type_id;
                                EXIT;
                            END IF;

                            END LOOP;

                        -- Read corresponding WP/Event Buffer detail from plan 2 if a corresponding planning-type exists

                            t_buff_diff.EXTEND;

                        t_buff_diff( t_buff_diff.LAST ) := NEW t_buff_diff_wp( lrec_EventsForTaskDefnPlanTwo.lrp_sdesc, lrec_BuffersForEvent.planning_type_cd, 'Plan 2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, t_common_aeroplane(iter).is_future, li_EventIndex, lc_timezone );

                        -- the first record in Plan 2 need not have the same work-package name.
                        IF lPlanningTypeDbId IS NOT NULL AND lPlanningTypeId IS NOT NULL THEN
                          -- a corresponding plan-type is mapped for plan2 . in this case, read column values as below
                          -- ELSE post null values

                          OPEN lcur_BuffersForEventWithType(lrec_EventsForTaskDefnPlanTwo.Lrp_Event_Db_Id, lrec_EventsForTaskDefnPlanTwo.Lrp_Event_Id, lPlanningTypeDbId, lPlanningTypeId);
                          -- find the corresponding buffer in the plan 2 work-package
                          event_2_total.wp_name := lrec_EventsForTaskDefnPlanTwo.Lrp_Sdesc || '( '|| to_char(lrec_EventsForTaskDefnPlanTwo.start_dt,'dd-MON-yyyy hh24:mi:ss') || ' )';

                          FETCH lCur_BuffersForEventWithType INTO lrec_BuffersForEventWithType;

                          IF lcur_BuffersForEventWithType%FOUND THEN

                              -- read values into the database
                              t_buff_diff( t_buff_diff.LAST ).r_hours := lrec_BuffersForEventWithType.Routine_Hrs;

                              t_buff_diff( t_buff_diff.LAST ).nr_factor := lrec_BuffersForEventWithType.Nr_Factor;
                              t_buff_diff( t_buff_diff.LAST ).nr_hours := lrec_BuffersForEventWithType.Nr_Hrs;
                              t_buff_diff( t_buff_diff.LAST ).tot_hours := lrec_BuffersForEventWithType.Total_Hrs;
                              t_buff_diff( t_buff_diff.LAST ).fill_pct := lrec_BuffersForEventWithType.Fill_Pct;
                              t_buff_diff( t_buff_diff.LAST ).ass_hrs := lrec_BuffersForEventWithType.Assigned_Hrs;
                              t_buff_diff( t_buff_diff.LAST ).fill_pct_unit:= lrec_BuffersForEventWithType.Fill_Pct_Unit;
                              t_buff_diff( t_buff_diff.LAST ).is_future := t_common_aeroplane(iter).is_future;

                              -- sum things up in the context of the package

                              event_2_total.r_hours := event_2_total.r_hours + lrec_BuffersForEventWithType.routine_hrs;

                              event_2_total.nr_hours := event_2_total.nr_hours + lrec_BuffersForEventWithType.nr_hrs;

                              event_2_total.tot_hours := event_2_total.tot_hours + lrec_BuffersForEventWithType.total_hrs;

                              event_2_total.ass_hrs := event_2_total.ass_hrs + lrec_BuffersForEventWithType.assigned_hrs;
                              IF event_2_total.r_hours > 0 THEN
                                 event_2_total.nr_factor := ROUND(event_2_total.nr_hours / event_2_total.r_hours,2);
                              END IF;

                          END IF;

                        CLOSE lcur_BuffersForEventWithType;
                        -- end if - when planning type does not exist in plan 2
                      END IF;
                      -- end loop for buffer/planning type
                      END LOOP;

                      FOR lrec_BuffersForEvent IN lcur_BuffersForEvent( lrec_EventsForTaskDefnPlanTwo.lrp_event_db_id, lrec_EventsForTaskDefnPlanTwo.lrp_event_id )
                      -- Find all buffers defined against event for Plan Two as there may be PT which only exist in Plan 2 WP
                      LOOP
                      EXIT WHEN lcur_BuffersForEvent%NOTFOUND;

                          lPlanningTypeDbId := NULL;
                          lPlanningTypeId := NULL;

                          FOR myIter IN 1 .. lCommonPlanTypes.COUNT
                          LOOP

                          IF
                             lCommonPlanTypes(myIter).planning_type_db_id = lrec_BuffersForEvent.planning_type_db_id AND
                             lCommonPlanTypes(myIter).planning_type_id    = lrec_BuffersForEvent.planning_type_id
                             AND
                             lCommonPlanTypes(myIter).plan_1_type_db_id IS NULL AND
                             lCommonPlanTypes(myIter).plan_1_type_id    IS NULL
                          THEN
                              -- only consider those buffer definitions whose planning types do not appear in Plan 1.
                              -- any that appear in Plan 1 will have been processed already
                              lPlanningTypeDbId := lCommonPlanTypes( myIter ).plan_2_type_db_id;
                              lPlanningTypeId   := lCommonPlanTypes( myIter ).plan_2_type_id;
                              EXIT;
                          END IF;

                          END LOOP;

                          event_2_total.wp_name := lrec_EventsForTaskDefnPlanTwo.lrp_sdesc || '( '|| to_char(lrec_EventsForTaskDefnPlanTwo.start_dt,'dd-MON-yyyy hh24:mi:ss') || ' )';

                          IF lPlanningTypeDbId IS NOT NULL AND lPlanningTypeId IS NOT NULL THEN
                             -- a filler reocrd to corespond to Plan 1 with blank data
                            t_buff_diff.EXTEND;

                            t_buff_diff( t_buff_diff.LAST ) := t_buff_diff_wp( lrec_EventsForTaskDefnPlanOne.lrp_sdesc, lrec_BuffersForEvent.planning_type_cd, 'Plan 1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, t_common_aeroplane(iter).is_future,li_EventIndex, lc_timezone );

                            -- Read corresponding WP/Event Buffer detail from plan 2

                            t_buff_diff.EXTEND;
                            t_buff_diff( t_buff_diff.LAST ) := t_buff_diff_wp( lrec_EventsForTaskDefnPlanTwo.lrp_sdesc, lrec_BuffersForEvent.planning_type_cd, 'Plan 2', lrec_BuffersForEvent.routine_hrs, lrec_BuffersForEvent.nr_factor, lrec_BuffersForEvent.Nr_Hrs, lrec_BuffersForEvent.total_hrs, lrec_BuffersForEvent.assigned_hrs, lrec_BuffersForEvent.fill_pct, lrec_BuffersForEvent.fill_pct_unit, t_common_aeroplane(iter).is_future,li_EventIndex, lc_timezone );
                            event_2_total.r_hours := event_2_total.r_hours + t_buff_diff( t_buff_diff.LAST ).r_hours;

                            event_2_total.nr_factor := event_2_total.nr_factor + t_buff_diff( t_buff_diff.LAST ).nr_factor;

                            event_2_total.nr_hours := event_2_total.nr_hours + t_buff_diff( t_buff_diff.LAST ).nr_hours;

                            event_2_total.tot_hours := event_2_total.tot_hours + t_buff_diff( t_buff_diff.LAST ).tot_hours;

                            event_2_total.wp_name := lrec_EventsForTaskDefnPlanTwo.lrp_sdesc || '( '|| to_char(lrec_EventsForTaskDefnPlanTwo.start_dt,'dd-MON-yyyy hh24:mi:ss') || ' )';

                            event_2_total.ass_hrs := event_2_total.ass_hrs + t_buff_diff( t_buff_diff.LAST ).ass_hrs;
                          -- end if - processing for Plan 2 specific Planning Type ends here
                          END IF;

                      -- loop on buffers in plan 2 ends here
                      END LOOP;

                      -- end if - when task-definition is not in scope for a work-package in plan 2
                      END IF;

                   -- Sum rows for each _event_ plan-type buffer
                   IF ( t_buff_diff.count() > li_InitialRowCount ) THEN
                     -- event total rows should only be posted to the differences if additional differences
                     -- were identified for the task-definition in the current iteration
                     t_buff_diff.EXTEND;
                     event_1_total.fill_pct := calculateTotalFillPercentage( event_1_total );
                     event_1_total.fill_pct_unit := '%';
                     t_buff_diff( t_buff_diff.LAST ) := event_1_total;
                     t_buff_diff.EXTEND;
                     event_2_total.fill_pct := calculateTotalFillPercentage( event_2_total );
                     event_2_total.fill_pct_unit := '%';
                     t_buff_diff( t_buff_diff.LAST ) := event_2_total;
                   END IF;
                   li_EventIndex := li_EventIndex + 1;

                  END LOOP;

                  CLOSE lcur_PlanTwoEventsForTaskDefn;

             END LOOP;

          END LOOP;



      RETURN t_buff_diff ;

   EXCEPTION

         WHEN OTHERS THEN
            v_err_code := SQLCODE;
            v_err_msg  := SQLERRM;
            raise_application_error(gc_ex_plancomp_err,
                                    substr(c_pkg_name || '.compare_work_packages_buffer : ' ||
                                           gv_err_msg_gen || v_err_code || ' ' ||
                                           v_err_msg || ' Errored at Step: ' ||
                                           v_step,
                                           1,
                                           2000),
                                 TRUE);
   END compare_work_packages_buffer;


   ----------------------------------------------------------------------------
   -- Function :   compare_work_packages_schedule
   -- Arguments:
   --              p_PlanOne  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --              p_PlanTwo  (lrp_plan.desc_sdesc%TYPE) -- Name of the Second Plan
   -- Description: Compares work-packages for the plans; Hence compares their scheduling
   --              as a function of the sequence
   ----------------------------------------------------------------------------
   FUNCTION compare_work_packages_schedule(
                p_PlanOne  lrp_plan.desc_sdesc%TYPE,
                p_PlanTwo  lrp_plan.desc_sdesc%TYPE,
                p_StartDt  lrp_event.start_dt%TYPE,
                p_EndDt    lrp_event.end_dt%TYPE
            ) RETURN t_tab_sched_diff IS

    /* Collection of common aircraft*/
    t_common_aeroplane t_tab_common_acft ;

    /* Collection of common task-definitions */
    t_common_task_defn mxkeytable;

    /* The return object*/
    t_wp_diff t_tab_sched_diff;

    /*
    For the specified task-definition, find all events (WP) on the specified aircraft
    where an instance of the task is in scope such that the events occur in the defined
    date-range
    */
    CURSOR lcur_PlanOneEventsForTaskDefn(
                aPlanName       lrp_plan.desc_sdesc%TYPE,
                aTaskDefnDbId   lrp_task_defn.task_defn_db_id%TYPE,
                aTaskDefnId     lrp_task_defn.task_defn_id%TYPE,
                aLrpDbId        lrp_event.lrp_db_id%TYPE,
                aLrpId          lrp_event.lrp_id%TYPE,
                aLrpInvInvId    lrp_event.lrp_inv_inv_id%TYPE,
                aStartDt        lrp_event.start_dt%TYPE,
                aEndDt          lrp_event.end_dt%TYPE ) IS
           SELECT
                lrp_event.lrp_sdesc,
                lrp_event_workscope.deadline_dt,
                lrp_event.start_dt,
                lrp_event.end_dt,
                inv_loc.loc_cd,
                getworkscopesforevent(lrp_event.lrp_event_db_id, lrp_event.lrp_event_id)AS workscopes,
                lrp_event.event_status_cd,
                lrp_event.lrp_event_db_id ||':'|| lrp_event.lrp_event_id AS wp_key
           FROM
                lrp_event_workscope JOIN lrp_plan ON
                lrp_event_workscope.lrp_db_id = lrp_plan.lrp_db_id AND
                lrp_event_workscope.lrp_id    = lrp_plan.lrp_id
                JOIN lrp_event ON
                lrp_event.lrp_event_db_id = lrp_event_workscope.lrp_event_db_id AND
                lrp_event.lrp_event_id    = lrp_event_workscope.lrp_event_id
                AND
                lrp_event.lrp_db_id       = lrp_plan.lrp_db_id AND
                lrp_event.lrp_id          = lrp_plan.lrp_id
                JOIN lrp_loc ON
                lrp_loc.lrp_db_id = lrp_plan.lrp_db_id AND
                lrp_loc.lrp_id    = lrp_plan.lrp_id
                AND
                lrp_loc.loc_db_id = lrp_event.lrp_loc_db_id AND
                lrp_loc.loc_id    = lrp_event.lrp_loc_id
                JOIN inv_loc ON
                inv_loc.loc_db_id = lrp_loc.loc_db_id AND
                inv_loc.loc_id    = lrp_loc.loc_id
           WHERE
                lrp_plan.desc_sdesc = aPlanName
                AND
                lrp_event_workscope.task_defn_db_id = aTaskDefnDbId AND
                lrp_event_workscope.task_defn_id    = aTaskDefnId
                AND
                lrp_event.lrp_db_id = aLrpDbId AND
                lrp_event.lrp_id    = aLrpId AND
                lrp_event.lrp_inv_inv_id = aLrpInvInvId
                AND
                lrp_event.start_dt > (aStartDt-1)
                AND
                lrp_event.end_dt < (aEndDt+1)
           ORDER BY
                lrp_event_workscope.deadline_dt;


    CURSOR lcur_PlanTwoEventsForTaskDefn(
                aPlanName       lrp_plan.desc_sdesc%TYPE,
                aTaskDefnDbId   lrp_task_defn.task_defn_db_id%TYPE,
                aTaskDefnId     lrp_task_defn.task_defn_id%TYPE,
                aLrpDbId        lrp_event.lrp_db_id%TYPE,
                aLrpId          lrp_event.lrp_id%TYPE,
                aLrpInvInvId    lrp_event.lrp_inv_inv_id%TYPE,
                aStartDt        lrp_event.start_dt%TYPE,
                aEndDt          lrp_event.end_dt%TYPE ) IS
           SELECT
                lrp_event.lrp_sdesc,
                lrp_event_workscope.deadline_dt,
                lrp_event.start_dt,
                lrp_event.end_dt,
                inv_loc.loc_cd,
                getworkscopesforevent(lrp_event.lrp_event_db_id, lrp_event.lrp_event_id) AS workscopes,
                lrp_event.event_status_cd,
                lrp_event.lrp_event_db_id ||':'|| lrp_event.lrp_event_id AS wp_key
           FROM
                lrp_event_workscope JOIN lrp_plan ON
                lrp_event_workscope.lrp_db_id = lrp_plan.lrp_db_id AND
                lrp_event_workscope.lrp_id    = lrp_plan.lrp_id
                JOIN lrp_event ON
                lrp_event.lrp_event_db_id = lrp_event_workscope.lrp_event_db_id AND
                lrp_event.lrp_event_id    = lrp_event_workscope.lrp_event_id
                AND
                lrp_event.lrp_db_id       = lrp_plan.lrp_db_id AND
                lrp_event.lrp_id          = lrp_plan.lrp_id
                JOIN lrp_loc ON
                lrp_loc.lrp_db_id = lrp_plan.lrp_db_id AND
                lrp_loc.lrp_id    = lrp_plan.lrp_id
                AND
                lrp_loc.loc_db_id = lrp_event.lrp_loc_db_id AND
                lrp_loc.loc_id    = lrp_event.lrp_loc_id
                JOIN inv_loc ON
                inv_loc.loc_db_id = lrp_loc.loc_db_id AND
                inv_loc.loc_id    = lrp_loc.loc_id
           WHERE
                lrp_plan.desc_sdesc = aPlanName
                AND
                lrp_event_workscope.task_defn_db_id = aTaskDefnDbId AND
                lrp_event_workscope.task_defn_id    = aTaskDefnId
                AND
                lrp_event.lrp_db_id = aLrpDbId AND
                lrp_event.lrp_id    = aLrpId AND
                lrp_event.lrp_inv_inv_id = aLrpInvInvId
                AND
                lrp_event.start_dt > (aStartDt-1)
                AND
                lrp_event.end_dt < (aEndDt+1)
           ORDER BY
                lrp_event_workscope.deadline_dt;

        -- Given an plan-name, and an acft inventory key, find the plan-specific ID for that aircraft
        CURSOR lcur_FindInventoryKey( aPlan lrp_plan.desc_sdesc%TYPE,
                                      aInvDbId lrp_inv_inv.inv_no_db_id%TYPE,
                                      aInvId lrp_inv_inv.inv_no_id%TYPE )
        IS
        SELECT
          lrp_inv_inv.lrp_db_id,
          lrp_inv_inv.lrp_id,
          lrp_inv_inv.lrp_inv_inv_id
        FROM
          lrp_inv_inv JOIN lrp_plan ON
          lrp_inv_inv.lrp_db_id = lrp_plan.lrp_db_id AND
          lrp_inv_inv.lrp_id    = lrp_plan.lrp_id
        WHERE
          lrp_plan.desc_sdesc = aPlan
          AND
          lrp_inv_inv.inv_no_db_id = aInvDbId AND
          lrp_inv_inv.inv_no_id    = aInvId;



    lrec_EventsForTaskDefnPlanOne lcur_PlanOneEventsForTaskDefn%ROWTYPE;
    lrec_EventsForTaskDefnPlanTwo lcur_PlanTwoEventsForTaskDefn%ROWTYPE;

    lrec_FindInventoryKey lcur_FindInventoryKey%ROWTYPE;

    li_lrp_inv_db_id_1 NUMBER := 0;
    li_lrp_inv_id_1 NUMBER := 0;
    li_lrp_inv_inv_id_1 NUMBER := 0;

    li_lrp_inv_db_id_2 NUMBER := 0;
    li_lrp_inv_id_2 NUMBER := 0;
    li_lrp_inv_inv_id_2 NUMBER := 0;

    li_rowIter NUMBER := 0;

    lc_timezone VARCHAR2(50);

    BEGIN
        
        SELECT 
            user_cd
        INTO
            lc_timezone
        FROM
            utl_timezone  
        WHERE
            default_bool=1;


    -- identify all common aircraft
    t_common_aeroplane := get_common_acft(p_PlanOne, p_PlanTwo);

    -- identify all common task-definitions
    t_common_task_defn := get_common_taskdefn(p_PlanOne, p_PlanTwo);

    -- the return object
    t_wp_diff := NEW t_tab_sched_diff();

    FOR iter IN NVL( t_common_aeroplane.FIRST, 0 ) .. NVL( t_common_aeroplane.LAST, -1 )
    LOOP

            OPEN lcur_FindInventoryKey( p_PlanOne, t_common_aeroplane(iter).inv_no_db_id, t_common_aeroplane(iter).inv_no_id );
            FETCH lcur_FindInventoryKey INTO lrec_FindInventoryKey;
            -- For each aircraft that is common to both plans, find it's plan-specific inventory key
            -- Here, for Plan 1
            IF lcur_FindInventoryKey%FOUND THEN

                li_lrp_inv_db_id_1 := lrec_FindInventoryKey.lrp_Db_Id;
                li_lrp_inv_id_1    := lrec_FindInventoryKey.lrp_Id;
                li_lrp_inv_inv_id_1:= lrec_FindInventoryKey.lrp_Inv_Inv_Id;
            END IF;

            CLOSE lcur_FindInventoryKey;

            OPEN lcur_FindInventoryKey( p_PlanTwo, t_common_aeroplane(iter).inv_no_db_id, t_common_aeroplane(iter).inv_no_id );
            FETCH lcur_FindInventoryKey INTO lrec_FindInventoryKey;
            -- For each aircraft that is common to both plans, find it's plan-specific inventory key
            -- Here for Plan 2
            IF lcur_FindInventoryKey%FOUND THEN

                li_lrp_inv_db_id_2 := lrec_FindInventoryKey.lrp_Db_Id;
                li_lrp_inv_id_2    := lrec_FindInventoryKey.lrp_Id;
                li_lrp_inv_inv_id_2:= lrec_FindInventoryKey.lrp_Inv_Inv_Id;

            END IF;

            CLOSE lcur_FindInventoryKey;


        -- iterate over each common aircraft
        FOR task_def_iter IN NVL( t_common_task_defn.FIRST,0 ) .. NVL( t_common_task_defn.LAST, -1 )

        LOOP

            -- iterate over each requirement/block common to both plans;

            -- find all work-packages where the task-definition is in-scope on the aircraft
            -- during the defined period
            OPEN lcur_PlanTwoEventsForTaskDefn( p_PlanTwo, t_common_task_defn(task_def_iter).db_id, t_common_task_defn(task_def_iter).id, li_lrp_inv_db_id_2, li_lrp_inv_id_2, li_lrp_inv_inv_id_2, p_StartDt, p_EndDt  ) ;

            FOR lrec_EventsForTaskDefnPlanOne IN lcur_PlanOneEventsForTaskDefn( p_PlanOne, t_common_task_defn(task_def_iter).db_id, t_common_task_defn(task_def_iter).id, li_lrp_inv_db_id_1, li_lrp_inv_id_1, li_lrp_inv_inv_id_1, p_StartDt, p_EndDt )
            -- iterate over all work-packages where the task-definition is in-scope on the aircraft
            -- during the defined period for Plan One
            LOOP

                -- Hence read the corresponding work-package for the previously opened cursor in plan two.
                -- Each FETCH here, corresponds to one advance of the enclosing loop
                FETCH lcur_PlanTwoEventsForTaskDefn INTO lrec_EventsForTaskDefnPlanTwo;

                IF
                    lcur_PlanTwoEventsForTaskDefn%FOUND AND
                    lrec_EventsForTaskDefnPlanOne.wp_key IS NOT NULL AND
                    lrec_EventsForTaskDefnPlanTwo.wp_key IS NOT NULL
                THEN

                    -- Compare the deadline-date,
                    -- Compare the start date
                    -- Compare the workscopes
                    -- Compare the Event Status
                    -- Compare the Event Location

                    -- When atleast one of the above is different, store both Events into the return object
                    IF lrec_EventsForTaskDefnPlanOne.Workscopes != lrec_EventsForTaskDefnPlanTwo.Workscopes OR
                       lrec_EventsForTaskDefnPlanOne.Start_Dt != lrec_EventsForTaskDefnPlanTwo.Start_Dt OR
                       lrec_EventsForTaskDefnPlanOne.End_Dt != lrec_EventsForTaskDefnPlanTwo.End_Dt OR
                       lrec_EventsForTaskDefnPlanOne.Event_Status_Cd != lrec_EventsForTaskDefnPlanTwo.Event_Status_Cd OR
                       lrec_EventsForTaskDefnPlanOne.Loc_Cd != lrec_EventsForTaskDefnPlanTwo.Loc_Cd

                    THEN
                       li_rowIter := li_rowIter + 1;
                       -- li_rowIter is needed because grouping in the report merely on the basis of Event Name
                       -- gets confusing when there are recurring events with the same name
                       t_wp_diff.EXTEND;
                       t_wp_diff(t_wp_diff.LAST) := NEW t_sched_diff_wp(
                                                        lrec_eventsForTaskDefnPlanOne.Lrp_Sdesc,
                                                        'Plan 1',
                                                        lrec_EventsForTaskDefnPlanOne.Event_Status_Cd,
                                                        lrec_EventsForTaskDefnPlanOne.Start_Dt,
                                                        lrec_EventsForTaskDefnPlanOne.End_Dt,
                                                        lrec_EventsForTaskDefnPlanOne.Loc_Cd,
                                                        lrec_EventsForTaskDefnPlanOne.Workscopes,
                                                        t_common_aeroplane(iter).is_future,
                                                        lrec_EventsForTaskDefnPlanOne.wp_key,
                                                        li_rowIter,
                                                        lc_timezone);

                       t_wp_diff.EXTEND;

                       t_wp_diff(t_wp_diff.LAST) := NEW t_sched_diff_wp(
                                                        lrec_eventsForTaskDefnPlanTwo.Lrp_Sdesc,
                                                        'Plan 2',
                                                        lrec_EventsForTaskDefnPlanTwo.Event_Status_Cd,
                                                        lrec_EventsForTaskDefnPlanTwo.Start_Dt,
                                                        lrec_EventsForTaskDefnPlanTwo.End_Dt,
                                                        lrec_EventsForTaskDefnPlanTwo.Loc_Cd,
                                                        lrec_EventsForTaskDefnPlanTwo.Workscopes,
                                                        t_common_aeroplane(iter).is_future,
                                                        lrec_EventsForTaskDefnPlanTwo.wp_key,
                                                        li_rowIter,
                                                        lc_timezone);


                    END IF;

                ELSE
                    EXIT;
                END IF;
            END LOOP;

            CLOSE lcur_PlanTwoEventsForTaskDefn;
        END LOOP;

    END LOOP;

   RETURN t_wp_diff;

   EXCEPTION

      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_plancomp_err,
                                 substr(c_pkg_name || '.compare_work_packages_schedule : ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' Errored at Step: ' ||
                                        v_step,
                                        1,
                                        2000),
                                 TRUE);

   END compare_work_packages_schedule;

   ----------------------------------------------------------------------------
   -- Function :   compare_work_packages_newrmvd
   -- Arguments:
   --              p_PlanOne  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --              p_PlanTwo  (lrp_plan.desc_sdesc%TYPE) -- Name of the Second Plan
   -- Description: Compares work-packages for the plans
   ----------------------------------------------------------------------------
   FUNCTION compare_work_packages_newrmvd(
                p_PlanOne  lrp_plan.desc_sdesc%TYPE,
                p_PlanTwo  lrp_plan.desc_sdesc%TYPE,
                p_StartDt  lrp_event.start_dt%TYPE,
                p_EndDt    lrp_event.end_dt%TYPE
            ) RETURN t_tab_newrmvd IS

        /*
        Colelction of aircraft common to both plans
        */
        t_common_aeroplane t_tab_common_acft ;

        /*
        Collection of task-definitions common to both plans
        */
        t_common_task_defn mxkeytable;

        /*
        Return object
        */
        t_new_rmdwp t_tab_newrmvd;



        CURSOR lcur_PlanOneEventsForTaskDefn(
                    aPlanName       lrp_plan.desc_sdesc%TYPE,
                    aTaskDefnDbId   lrp_task_defn.task_defn_db_id%TYPE,
                    aTaskDefnId     lrp_task_defn.task_defn_id%TYPE,
                    aLrpDbId        lrp_event.lrp_db_id%TYPE,
                    aLrpId          lrp_event.lrp_id%TYPE,
                    aLrpInvInvId    lrp_event.lrp_inv_inv_id%TYPE,
                    aStartDt        lrp_event.start_dt%TYPE,
                    aEndDt          lrp_event.end_dt%TYPE ) IS
        SELECT
             lrp_event.lrp_sdesc,
             lrp_event_workscope.deadline_dt,
             lrp_event.start_dt,
             lrp_event.end_dt,
             inv_loc.loc_cd,
             getworkscopesforevent(lrp_event.lrp_event_db_id, lrp_event.lrp_event_id)AS workscopes,
             COUNT(*) OVER(PARTITION BY 1) AS rows_in_result
        FROM
             lrp_event_workscope JOIN lrp_plan ON
             lrp_event_workscope.lrp_db_id = lrp_plan.lrp_db_id AND
             lrp_event_workscope.lrp_id    = lrp_plan.lrp_id
             JOIN lrp_event ON
             lrp_event.lrp_event_db_id = lrp_event_workscope.lrp_event_db_id AND
             lrp_event.lrp_event_id    = lrp_event_workscope.lrp_event_id
             AND
             lrp_event.lrp_db_id       = lrp_plan.lrp_db_id AND
             lrp_event.lrp_id          = lrp_plan.lrp_id
             JOIN lrp_loc ON
             lrp_loc.lrp_db_id = lrp_plan.lrp_db_id AND
             lrp_loc.lrp_id    = lrp_plan.lrp_id
             AND
             lrp_loc.loc_db_id = lrp_event.lrp_loc_db_id AND
             lrp_loc.loc_id    = lrp_event.lrp_loc_id
             JOIN inv_loc ON
             inv_loc.loc_db_id = lrp_loc.loc_db_id AND
             inv_loc.loc_id    = lrp_loc.loc_id
        WHERE
             lrp_plan.desc_sdesc = aPlanName
             AND
             lrp_event_workscope.task_defn_db_id = aTaskDefnDbId AND
             lrp_event_workscope.task_defn_id    = aTaskDefnId
             AND
             lrp_event.lrp_db_id = aLrpDbId AND
             lrp_event.lrp_id    = aLrpId AND
             lrp_event.lrp_inv_inv_id = aLrpInvInvId
             AND
             lrp_event.start_dt > (aStartDt-1)
             AND
             lrp_event.end_dt < (aEndDt+1)
        ORDER BY
             lrp_event_workscope.deadline_dt;


        CURSOR lcur_PlanTwoEventsForTaskDefn(
                    aPlanName       lrp_plan.desc_sdesc%TYPE,
                    aTaskDefnDbId   lrp_task_defn.task_defn_db_id%TYPE,
                    aTaskDefnId     lrp_task_defn.task_defn_id%TYPE,
                    aLrpDbId        lrp_event.lrp_db_id%TYPE,
                    aLrpId          lrp_event.lrp_id%TYPE,
                    aLrpInvInvId    lrp_event.lrp_inv_inv_id%TYPE,
                    aStartDt        lrp_event.start_dt%TYPE,
                    aEndDt          lrp_event.end_dt%TYPE ) IS
        SELECT
             lrp_event.lrp_sdesc,
             lrp_event_workscope.deadline_dt,
             lrp_event.start_dt,
             lrp_event.end_dt,
             inv_loc.loc_cd,
             getworkscopesforevent(lrp_event.lrp_event_db_id, lrp_event.lrp_event_id) AS workscopes,
             COUNT(*) OVER(PARTITION BY 1) AS rows_in_result
        FROM
             lrp_event_workscope JOIN lrp_plan ON
             lrp_event_workscope.lrp_db_id = lrp_plan.lrp_db_id AND
             lrp_event_workscope.lrp_id    = lrp_plan.lrp_id
             JOIN lrp_event ON
             lrp_event.lrp_event_db_id = lrp_event_workscope.lrp_event_db_id AND
             lrp_event.lrp_event_id    = lrp_event_workscope.lrp_event_id
             AND
             lrp_event.lrp_db_id       = lrp_plan.lrp_db_id AND
             lrp_event.lrp_id          = lrp_plan.lrp_id
             JOIN lrp_loc ON
             lrp_loc.lrp_db_id = lrp_plan.lrp_db_id AND
             lrp_loc.lrp_id    = lrp_plan.lrp_id
             AND
             lrp_loc.loc_db_id = lrp_event.lrp_loc_db_id AND
             lrp_loc.loc_id    = lrp_event.lrp_loc_id
             JOIN inv_loc ON
             inv_loc.loc_db_id = lrp_loc.loc_db_id AND
             inv_loc.loc_id    = lrp_loc.loc_id
        WHERE
             lrp_plan.desc_sdesc = aPlanName
             AND
             lrp_event_workscope.task_defn_db_id = aTaskDefnDbId AND
             lrp_event_workscope.task_defn_id    = aTaskDefnId
             AND
             lrp_event.lrp_db_id = aLrpDbId AND
             lrp_event.lrp_id    = aLrpId AND
             lrp_event.lrp_inv_inv_id = aLrpInvInvId
             AND
             lrp_event.start_dt > (aStartDt-1)
             AND
             lrp_event.end_dt < (aEndDt+1)
        ORDER BY
             lrp_event_workscope.deadline_dt;

        CURSOR lcur_FindInventoryKey( aPlan lrp_plan.desc_sdesc%TYPE,
                                      aInvDbId lrp_inv_inv.inv_no_db_id%TYPE,
                                      aInvId lrp_inv_inv.inv_no_id%TYPE )
        IS
        SELECT
          lrp_inv_inv.lrp_db_id,
          lrp_inv_inv.lrp_id,
          lrp_inv_inv.lrp_inv_inv_id
        FROM
          lrp_inv_inv JOIN lrp_plan ON
          lrp_inv_inv.lrp_db_id = lrp_plan.lrp_db_id AND
          lrp_inv_inv.lrp_id    = lrp_plan.lrp_id
        WHERE
          lrp_plan.desc_sdesc = aPlan
          AND
          lrp_inv_inv.inv_no_db_id = aInvDbId AND
          lrp_inv_inv.inv_no_id    = aInvId;

        lrec_EventsForTaskDefnPlanOne lcur_PlanOneEventsForTaskDefn%ROWTYPE;
        lrec_EventsForTaskDefnPlanTwo lcur_PlanTwoEventsForTaskDefn%ROWTYPE;
        lrec_FindInventoryKey lcur_FindInventoryKey%ROWTYPE;


        li_CurrentIdx NUMBER := 1;


        li_RowsInEvent1 NUMBER := 0;
        li_RowsInEvent2 NUMBER := 0;

        li_lrp_inv_db_id_1 NUMBER := 0;
        li_lrp_inv_id_1 NUMBER := 0;
        li_lrp_inv_inv_id_1 NUMBER := 0;

        li_lrp_inv_db_id_2 NUMBER := 0;
        li_lrp_inv_id_2 NUMBER := 0;
        li_lrp_inv_inv_id_2 NUMBER := 0;
        
        lc_timezone VARCHAR2(50);

        BEGIN
        
        SELECT 
            user_cd
        INTO
            lc_timezone
        FROM
            utl_timezone  
        WHERE
            default_bool=1;

        
        -- identify all common aircraft
        t_common_aeroplane := get_common_acft(p_PlanOne, p_PlanTwo);
        -- identify all common task-definitions
        t_common_task_defn := get_common_taskdefn(p_PlanOne, p_PlanTwo);

        t_new_rmdwp := t_tab_newrmvd();


        FOR iter IN NVL( t_common_aeroplane.FIRST, 0 ) .. NVL( t_common_aeroplane.LAST, -1 )
        LOOP


            OPEN lcur_FindInventoryKey( p_PlanOne, t_common_aeroplane(iter).inv_no_db_id, t_common_aeroplane(iter).inv_no_id );
            FETCH lcur_FindInventoryKey INTO lrec_FindInventoryKey;
            IF lcur_FindInventoryKey%FOUND THEN

                li_lrp_inv_db_id_1 := lrec_FindInventoryKey.Lrp_Db_Id;
                li_lrp_inv_id_1    := lrec_FindInventoryKey.Lrp_Id;
                li_lrp_inv_inv_id_1:= lrec_FindInventoryKey.Lrp_Inv_Inv_Id;

            END IF;

            CLOSE lcur_FindInventoryKey;

            OPEN lcur_FindInventoryKey( p_PlanTwo, t_common_aeroplane(iter).inv_no_db_id, t_common_aeroplane(iter).inv_no_id );
            FETCH lcur_FindInventoryKey INTO lrec_FindInventoryKey;
            IF lcur_FindInventoryKey%FOUND THEN

                li_lrp_inv_db_id_2 := lrec_FindInventoryKey.Lrp_Db_Id;
                li_lrp_inv_id_2    := lrec_FindInventoryKey.Lrp_Id;
                li_lrp_inv_inv_id_2:= lrec_FindInventoryKey.Lrp_Inv_Inv_Id;

            END IF;

            CLOSE lcur_FindInventoryKey;

            -- iterate over each common aircraft
            FOR task_def_iter IN NVL( t_common_task_defn.FIRST,0 ) .. NVL( t_common_task_defn.LAST, -1 )
            LOOP


            OPEN lcur_PlanTwoEventsForTaskDefn( p_PlanTwo, t_common_task_defn(task_def_iter).db_id, t_common_task_defn(task_def_iter).id, li_lrp_inv_db_id_2, li_lrp_inv_id_2, li_lrp_inv_inv_id_2, p_StartDt, p_EndDt );
            OPEN lcur_PlanOneEventsForTaskDefn( p_PlanOne, t_common_task_defn(task_def_iter).db_id, t_common_task_defn(task_def_iter).id, li_lrp_inv_db_id_1, li_lrp_inv_id_1, li_lrp_inv_inv_id_1,p_StartDt, p_EndDt );

            FETCH lcur_PlanOneEventsForTaskDefn INTO lrec_EventsForTaskDefnPlanOne;
            FETCH lcur_PlanTwoEventsForTaskDefn INTO lrec_EventsForTaskDefnPlanTwo;

            li_RowsInEvent1 := lrec_EventsForTaskDefnPlanOne.rows_in_result;
            li_RowsInEvent2 := lrec_EventsForTaskDefnPlanTwo.rows_in_result;

            CLOSE lcur_PlanOneEventsForTaskDefn;
            CLOSE lcur_PlanTwoEventsForTaskDefn;

            IF li_RowsInEvent1 > li_RowsInEvent2 THEN
                  -- more elements defined against plan one than plan two; implies events in plan one that are new

                    OPEN lcur_PlanOneEventsForTaskDefn( p_PlanOne, t_common_task_defn(task_def_iter).db_id, t_common_task_defn(task_def_iter).id, li_lrp_inv_db_id_1, li_lrp_inv_id_1, li_lrp_inv_inv_id_1, p_StartDt, p_EndDt );
                    FETCH lcur_PlanOneEventsForTaskDefn INTO lrec_EventsForTaskDefnPlanOne;

                    FOR li_CurrentIdx IN 1 .. li_RowsInEvent1
                    LOOP
                        IF li_CurrentIdx > li_RowsInEvent2 THEN
                          -- extend the returnable by one row
                          t_new_rmdwp.EXTEND;

                          -- set the content for the row
                          t_new_rmdwp(t_new_rmdwp.LAST) := NEW t_new_rmvd_wp( lrec_EventsForTaskDefnPlanOne.Lrp_Sdesc, lrec_EventsForTaskDefnPlanOne.Start_Dt, lrec_EventsForTaskDefnPlanOne.End_Dt, lrec_EventsForTaskDefnPlanOne.Loc_Cd, lrec_EventsForTaskDefnPlanOne.workscopes, t_common_aeroplane(iter).is_future, lc_timezone );

                        END IF;
                    END LOOP;

                    CLOSE lcur_PlanOneEventsForTaskDefn;
            END IF;



            END LOOP;

        END LOOP;

   RETURN t_new_rmdwp;

   EXCEPTION

      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_plancomp_err,
                                 substr(c_pkg_name || '.compare_work_packages_newrmvd : ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' Errored at Step: ' ||
                                        v_step,
                                        1,
                                        2000),
                                 TRUE);

   END compare_work_packages_newrmvd;


   ----------------------------------------------------------------------------
   -- Function :   read_fleet_mtc
   -- Arguments:   p_AssyCd   (eqp_assmbl.assmbl_cd%type)  -- Assembly Code for
   --                                                         the Fleet under consideration
   --              p_FirstPlan  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --              p_SecondPlan (lrp_plan.desc_sdesc%type) -- Name of the Second Plan
   --
   -- Description:  Take sthe Assembly Code, and Plan Names.
   --               Hence determines which task-definitions are relevant in
   --               in the context of either plan
   ----------------------------------------------------------------------------
   FUNCTION read_fleet_mtc(
                         p_AssyDbId eqp_assmbl.assmbl_db_id%TYPE,
                         p_AssyCd  eqp_assmbl.assmbl_cd%TYPE,
                         p_FirstPlan lrp_plan.desc_sdesc%TYPE,
                         p_SecondPlan lrp_plan.desc_sdesc%TYPE) RETURN fleet_mtc IS

            t_fleet_mtc fleet_mtc ;
            index_ Number := 1;

            CURSOR lCur_Commons (aAssyDbId eqp_assmbl.assmbl_db_id%TYPE, aAssyCd eqp_assmbl.assmbl_cd%TYPE, aPlan1 lrp_plan.desc_sdesc%TYPE, aPlan2 lrp_plan.desc_sdesc%TYPE) IS
                      SELECT
                          toLabel(task_task.task_cd, task_task.task_name) AS task_name,
                          getTaskWorkTypes( task_task.task_db_id, task_task.task_id ) AS work_type_cd,
                          1 AS plan_1_task,
                          DECODE(
                                   (
                                   SELECT
                                          COUNT(*)
                                   FROM
                                          lrp_task_defn dupe
                                   WHERE
                                          dupe.task_defn_db_id = task_task.task_defn_db_id AND
                                          dupe.task_defn_id    = task_task.task_defn_id AND
                                          NOT(
                                          dupe.lrp_db_id = lrp_task_defn.lrp_db_id AND
                                          dupe.lrp_id    = lrp_task_defn.lrp_id
                                      )
                                   ),
                                   0,
                                   0,
                                   1

                          ) AS plan_2_task
                  FROM
                          lrp_task_defn JOIN lrp_plan ON
                          lrp_task_defn.lrp_db_id = lrp_plan.lrp_db_id AND
                          lrp_task_defn.lrp_id    = lrp_plan.lrp_id
                          JOIN task_task ON
                          lrp_task_defn.task_defn_db_id = task_task.task_defn_db_id AND
                          lrp_task_defn.task_defn_id    = task_task.task_defn_id
                          AND
                          task_task.assmbl_db_id        = aAssyDbId AND
                          task_task.assmbl_cd           = aAssyCd

                  WHERE
                          lrp_plan.desc_sdesc = aPlan1
                          START WITH
                                          lrp_task_defn.task_defn_db_id = lrp_task_defn.task_defn_db_id AND
                                          lrp_task_defn.task_defn_id    = lrp_task_defn.task_defn_id
                          CONNECT BY
                                          lrp_task_defn.task_defn_db_id = PRIOR lrp_task_defn.task_defn_id AND
                                          lrp_task_defn.task_defn_db_id = PRIOR lrp_task_defn.task_defn_db_id
                  UNION
                  SELECT
                          toLabel(task_task.task_cd, task_task.task_name) AS task_name,
                          getTaskWorkTypes( task_task.task_db_id, task_task.task_id ) AS work_type_cd,
                          DECODE(
                                   (
                                   SELECT
                                          COUNT(*)
                                   FROM
                                          lrp_task_defn dupe
                                   WHERE
                                          dupe.task_defn_db_id = task_task.task_defn_db_id AND
                                          dupe.task_defn_id    = task_task.task_defn_id AND
                                          NOT(
                                          dupe.lrp_db_id = lrp_task_defn.lrp_db_id AND
                                          dupe.lrp_id    = lrp_task_defn.lrp_id
                                      )
                                   ),
                                   0,
                                   0,
                                   1

                          ) AS plan_1_task,
                          1 AS plan_2_task
                  FROM
                          lrp_task_defn JOIN lrp_plan ON
                          lrp_task_defn.lrp_db_id = lrp_plan.lrp_db_id AND
                          lrp_task_defn.lrp_id    = lrp_plan.lrp_id
                          JOIN task_task ON
                          lrp_task_defn.task_defn_db_id = task_task.task_defn_db_id AND
                          lrp_task_defn.task_defn_id    = task_task.task_defn_id
                          AND
                          task_task.assmbl_db_id        = aAssyDbId AND
                          task_task.assmbl_cd           = aAssyCd

                  WHERE
                          lrp_plan.desc_sdesc = aPlan2
                          START WITH
                                  lrp_task_defn.task_defn_db_id = lrp_task_defn.task_defn_db_id AND
                                  lrp_task_defn.task_defn_id    = lrp_task_defn.task_defn_id
                          CONNECT BY
                                  lrp_task_defn.task_defn_db_id = PRIOR lrp_task_defn.task_defn_id AND
                                  lrp_task_defn.task_defn_db_id = PRIOR lrp_task_defn.task_defn_db_id;



   BEGIN
            t_fleet_mtc :=fleet_mtc();

            OPEN lCur_Commons (p_AssyDbId, p_AssyCd, p_FirstPlan, p_SecondPlan) ;
            LOOP
              t_fleet_mtc.EXTEND;

              t_fleet_mtc(index_) := NEW fleet_mtc_rec('','',0,0,0);
              FETCH
                    lCur_Commons INTO t_fleet_mtc(index_).task_name, t_fleet_mtc(index_).work_type, t_fleet_mtc(index_).plan_1,    t_fleet_mtc(index_).plan_2;
              index_ := index_+1;
              EXIT WHEN lCur_Commons%NOTFOUND;
            END LOOP;
            t_fleet_mtc.TRIM;
      RETURN t_fleet_mtc;
   EXCEPTION

      WHEN OTHERS THEN
         v_err_code := SQLCODE;
         v_err_msg  := SQLERRM;
         raise_application_error(gc_ex_plancomp_err,
                                 substr(c_pkg_name || '.read_fleet_mtc : ' ||
                                        gv_err_msg_gen || v_err_code || ' ' ||
                                        v_err_msg || ' Errored at Step: ' ||
                                        v_step,
                                        1,
                                        2000),
                                 TRUE);
   END read_fleet_mtc;

END PLAN_COMPARISION_PKG;
/

--changeSet DEV-382:19 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Create the actual package with the logic that uses the types that were
-- create above
CREATE OR REPLACE PACKAGE "PLAN_COMPARISION_PKG" IS
   ----------------------------------------------------------------------------
   -- Object Name : PLAN_COMPARISION_PKG
   -- Object Type : Package Header
   -- Date        : June 28, 2009
   -- Coder       : Abhishek
   -- Recent Date :
   -- Recent Coder:
   -- Description :
   -- This package contains methods for custom Plan Comparision report in maintenix from
   -- a PLSQL framework.
   --
   ----------------------------------------------------------------------------
   -- Copyright © 2010-2011 MxI Technologies Ltd.  All Rights Reserved.
   -- Any distribution of the MxI Maintenix source code by any other party
   -- than MxI Technologies Ltd is prohibited.
   ----------------------------------------------------------------------------


   -----------------------------------------------------------------------------
   -- Public Package Types
   -----------------------------------------------------------------------------



   ----------------------------------------------------------------------------
   -- Package Exceptions
   ----------------------------------------------------------------------------
   gc_ex_plancomp_err CONSTANT NUMBER := -20100;
   gex_plancomp_err EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_plancomp_err,
                         -20100);

   gc_ex_plancomp_notfound CONSTANT NUMBER := -20101;
   gex_plancomp_notfound EXCEPTION;
   PRAGMA EXCEPTION_INIT(gex_plancomp_notfound,
                         -20101);

   ----------------------------------------------------------------------------
   -- Function :   compare_work_packages_buffer
   -- Arguments:
   --              p_PlanOne  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --              p_PlanTwo  (lrp_plan.desc_sdesc%TYPE) -- Name of the Second Plan
   -- Description: Compares work-packages for the plans; Hence compares their
   --              buffering as a function of the sequence
   ----------------------------------------------------------------------------
   FUNCTION compare_work_packages_buffer(
                p_PlanOne  lrp_plan.desc_sdesc%TYPE,
                p_PlanTwo  lrp_plan.desc_sdesc%TYPE,
                p_StartDt  lrp_event.start_dt%TYPE,
                p_EndDt    lrp_event.end_dt%TYPE
            ) RETURN t_tab_buff_diff;


   ----------------------------------------------------------------------------
   -- Function :   compare_work_packages_schedule
   -- Arguments:
   --              p_PlanOne  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --              p_PlanTwo  (lrp_plan.desc_sdesc%TYPE) -- Name of the Second Plan
   -- Description: Compares work-packages for the plans; Hence compares their scheduling
   --              as a function of the sequence
   ----------------------------------------------------------------------------
   FUNCTION compare_work_packages_schedule(
                p_PlanOne  lrp_plan.desc_sdesc%TYPE,
                p_PlanTwo  lrp_plan.desc_sdesc%TYPE,
                p_StartDt  lrp_event.start_dt%TYPE,
                p_EndDt    lrp_event.end_dt%TYPE
            ) RETURN t_tab_sched_diff;

   ----------------------------------------------------------------------------
   -- Function :   compare_work_packages_newrmvd
   -- Arguments:
   --              p_PlanOne  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --              p_PlanTwo  (lrp_plan.desc_sdesc%TYPE) -- Name of the Second Plan
   -- Description: Compares work-packages for the plans. Returns collection of WP that
   --              exist in one plan, and not in the other
   ----------------------------------------------------------------------------
   FUNCTION compare_work_packages_newrmvd(
                p_PlanOne  lrp_plan.desc_sdesc%TYPE,
                p_PlanTwo  lrp_plan.desc_sdesc%TYPE,
                p_StartDt  lrp_event.start_dt%TYPE,
                p_EndDt    lrp_event.end_dt%TYPE
            ) RETURN t_tab_newrmvd ;


   ----------------------------------------------------------------------------
   -- Function :   read_fleet_mtc
   -- Arguments:   p_AssyCd   (eqp_assmbl.assmbl_cd%type)  -- Assembly Code for 
   --                                                         the Fleet under consideration
   --              p_FirstPlan  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --              p_SecondPlan (lrp_plan.desc_sdesc%type) -- Name of the Second Plan
   --
   -- Description:  Take sthe Assembly Code, and Plan Names. 
   --               Hence determines which task-definitions are relevant in
   --               in the context of either plan
   ----------------------------------------------------------------------------
   FUNCTION read_fleet_mtc(
                         p_AssyDbId eqp_assmbl.assmbl_db_id%TYPE,
                         p_AssyCd  eqp_assmbl.assmbl_cd%TYPE,
                         p_FirstPlan lrp_plan.desc_sdesc%TYPE,
                         p_SecondPlan lrp_plan.desc_sdesc%TYPE) RETURN fleet_mtc;


END PLAN_COMPARISION_PKG;
/