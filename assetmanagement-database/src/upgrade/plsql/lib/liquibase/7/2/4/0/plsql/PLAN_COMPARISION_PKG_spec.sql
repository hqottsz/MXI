--liquibase formatted sql


--changeSet PLAN_COMPARISION_PKG_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   -- Procedures : read_fleet_mtc
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
   -- Description: Compares work-packages for the plans; returns a collection of
   --              wp that are in one plan, and not in the other
   ----------------------------------------------------------------------------
   FUNCTION compare_work_packages_newrmvd(
                p_PlanOne  lrp_plan.desc_sdesc%TYPE,
                p_PlanTwo  lrp_plan.desc_sdesc%TYPE,
                p_StartDt  lrp_event.start_dt%TYPE,
                p_EndDt    lrp_event.end_dt%TYPE
            ) RETURN t_tab_newrmvd ;

   ----------------------------------------------------------------------------
   -- Procedure:   read_fleet_mtc
   -- Arguments:
   --              p_AssyDbId (eqp_assmbl.assmbl_db_id%TYPE) -- DB Id for the
   --              p_AssyCd   (eqp_assmbl.assmbl_cd%type) -- Assembly Code for the Fleet under consideration
   --              p_FirstPlan  (lrp_plan.desc_sdesc%type) -- Name of the First Plan
   --              p_SecondPlan   (-do-) -- Name of the Second Plan
   --
   -- Description:  Takes the Assembly Code, and Plan Names. Hence determines which task-definitions are relevant in
   --               in the context of either plan
   ----------------------------------------------------------------------------
   FUNCTION read_fleet_mtc(
                         p_AssyDbId eqp_assmbl.assmbl_db_id%TYPE,
                         p_AssyCd  eqp_assmbl.assmbl_cd%TYPE,
                         p_FirstPlan lrp_plan.desc_sdesc%type,
                         p_SecondPlan lrp_plan.desc_sdesc%type) RETURN fleet_mtc;


END PLAN_COMPARISION_PKG;
/