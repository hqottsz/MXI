--liquibase formatted sql


--changeSet INV_DELETE_PKG_body:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace package body INV_DELETE_PKG is

  -- find all inventories that installed to the aircraft.
  CURSOR lcur_InventoryRecords(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE, anInvNoId IN inv_inv.inv_no_id%TYPE) IS
    SELECT
       inv_inv.inv_no_db_id,
       inv_inv.inv_no_id
    FROM
       inv_inv
    WHERE
       inv_inv.rstat_cd = 0
    START WITH
       inv_no_db_id = anInvNoDbId AND
       inv_no_id    = anInvNoId
    CONNECT BY
       nh_inv_no_db_id = PRIOR inv_no_db_id AND
       nh_inv_no_id    = PRIOR inv_no_id
    ORDER BY
       level DESC;

  -- find all events of the inventory.
  CURSOR lcur_EvtInventoryRecords(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE, anInvNoId IN inv_inv.inv_no_id%TYPE) IS
    SELECT
       evt_inv.event_db_id,
       evt_inv.event_id
    FROM
       evt_inv
    WHERE
       evt_inv.inv_no_db_id = anInvNoDbId AND
       evt_inv.inv_no_id    = anInvNoId
       AND
       evt_inv.rstat_cd = 0;

  -- find all actual tasks of the inventory
  CURSOR lcur_SchedInventoryRecords(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE, anInvNoId IN inv_inv.inv_no_id%TYPE) IS
    SELECT
       sched_stask.sched_db_id,
       sched_stask.sched_id
    FROM
       sched_stask
    WHERE
       sched_stask.main_inv_no_db_id = anInvNoDbId AND
       sched_stask.main_inv_no_id    = anInvNoId
       AND
       sched_stask.rstat_cd = 0;

  -- find forign key to inv_inv in sched_rmvd_part table
  CURSOR lcur_SchedRmvdPartInvRecs(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE, anInvNoId IN inv_inv.inv_no_id%TYPE) IS
    SELECT
       sched_rmvd_part.sched_db_id,
       sched_rmvd_part.sched_id,
       sched_rmvd_part.sched_part_id,
       sched_rmvd_part.sched_rmvd_part_id
    FROM
       sched_rmvd_part
       INNER JOIN sched_part ON
          sched_rmvd_part.sched_db_id   = sched_part.sched_db_id AND
          sched_rmvd_part.sched_id      = sched_part.sched_id AND
          sched_rmvd_part.sched_part_id = sched_part.sched_part_id
       INNER JOIN sched_stask ON
          sched_part.sched_db_id = sched_stask.sched_db_id AND
          sched_part.sched_id    = sched_stask.sched_id
    WHERE
       sched_stask.main_inv_no_db_id = anInvNoDbId AND
       sched_stask.main_inv_no_id    = anInvNoId
       AND
       sched_stask.rstat_cd = 0;

  -- find repl_sched from inventory
  CURSOR lcur_SchedPartInvRecs(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE, anInvNoId IN inv_inv.inv_no_id%TYPE) IS
    SELECT
       sched_stask.repl_sched_db_id,
       sched_stask.repl_sched_id,
       sched_stask.repl_sched_part_id
    FROM
       sched_stask
       INNER JOIN sched_part ON
          sched_stask.repl_sched_db_id   = sched_part.sched_db_id AND
          sched_stask.repl_sched_id      = sched_part.sched_id AND
          sched_stask.repl_sched_part_id = sched_part.sched_part_id
    WHERE
       sched_stask.main_inv_no_db_id = anInvNoDbId AND
       sched_stask.main_inv_no_id    = anInvNoId
       AND
       sched_stask.rstat_cd = 0;

  PROCEDURE DeleteAnAircraft(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                             anInvNoId   IN inv_inv.inv_no_id%TYPE,
                             on_Return   OUT NUMBER) IS
    lrec_InventoryRecords lcur_InventoryRecords%ROWTYPE;

    -- local variables
    ll_InvNoDbId inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId   inv_inv.inv_no_id%TYPE;
    l_rowCount   NUMBER;

  BEGIN

    -- Initialize the return value
    on_Return  := icn_NoProc;

    l_rowCount := 0;
    DBMS_OUTPUT.PUT_LINE('inventory key= ' || anInvNoDbId || ':' || anInvNoId);

--    EXECUTE IMMEDIATE ('ALTER TABLE lrp_event_workscope DISABLE CONSTRAINT FK_PREVEVENTWORKSCOPE_EVENTWOR');
    FOR lrec_InventoryRecords IN lcur_InventoryRecords(anInvNoDbId, anInvNoId) LOOP
      l_rowCount := l_rowCount + 1;

      DBMS_OUTPUT.PUT_LINE('inv key ' || l_rowCount || ' = ' || lrec_InventoryRecords.Inv_No_Db_Id || ':' || lrec_InventoryRecords.Inv_No_Id);

      -- evt
      DeleteEvent1(lrec_InventoryRecords.Inv_No_Db_Id, lrec_InventoryRecords.Inv_No_Id, on_Return);

      DELETE FROM
         eqp_part_rotable_adjust
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         inv_ietm
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         sched_kit_map
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         inv_kit
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         inv_inv_oem_assmbl
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         inv_attach
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         lpa_run_inv
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         req_part
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         req_part s
      WHERE
         s.req_ac_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         s.req_ac_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         fl_leg_status_log
      WHERE
         user_note_id
      IN
         (SELECT
             flight_note_id
          FROM
             fl_leg_note
             INNER JOIN fl_leg ON
                fl_leg_note.leg_id = fl_leg.leg_id
             INNER JOIN inv_ac_reg ON
                fl_leg.aircraft_db_id = inv_ac_reg.inv_no_db_id AND
                fl_leg.aircraft_id    = inv_ac_reg.inv_no_id
          WHERE
             inv_ac_reg.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             inv_ac_reg.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         fl_leg_status_log
      WHERE
         system_note_id
      IN
         (SELECT
             flight_note_id
          FROM
             fl_leg_note
             INNER JOIN fl_leg ON
                fl_leg_note.leg_id = fl_leg.leg_id
             INNER JOIN inv_ac_reg ON
                fl_leg.aircraft_db_id = inv_ac_reg.inv_no_db_id AND
                fl_leg.aircraft_id    = inv_ac_reg.inv_no_id
          WHERE
             inv_ac_reg.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             inv_ac_reg.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         fl_leg_note ln
      WHERE
         ln.leg_id
      IN
         (SELECT
             fl.leg_id
          FROM
             fl_leg fl
             INNER JOIN inv_ac_reg s ON
                fl.aircraft_db_id = s.inv_no_db_id AND
                fl.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         fl_leg_measurement lm
      WHERE
         lm.leg_id
      IN
         (SELECT
             fl.leg_id
          FROM
             fl_leg fl
             INNER JOIN inv_ac_reg s ON
                fl.aircraft_db_id = s.inv_no_db_id AND
                fl.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         inv_ac_flight_plan fp
      WHERE
         fp.arr_leg_id
      IN
         (SELECT
             fl.leg_id
          FROM
             fl_leg fl
          WHERE
             fl.aircraft_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             fl.aircraft_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         inv_ac_flight_plan fp
      WHERE
         fp.dep_leg_id
      IN
         (SELECT
             fl.leg_id
          FROM
             fl_leg fl
          WHERE
             fl.aircraft_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             fl.aircraft_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         fl_leg_disrupt_type fdt
      WHERE
         fdt.leg_disrupt_id
      IN
         (SELECT
             fd.leg_disrupt_id
          FROM
             fl_leg_disrupt fd
             INNER JOIN fl_leg fl ON
                fd.leg_id = fl.leg_id
          WHERE
             fl.aircraft_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             fl.aircraft_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         fl_leg_disrupt fd
      WHERE
         fd.leg_id
      IN
         (SELECT
             fl.leg_id
          FROM
             fl_leg fl
          WHERE
             fl.aircraft_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             fl.aircraft_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_action sa
      WHERE
         (sa.sched_db_id,
          sa.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role_status rs
      WHERE
         (rs.labour_role_db_id,
          rs.labour_role_id)
      IN
         (select
             r.labour_role_db_id,
             r.labour_role_id
          FROM
             sched_labour_role r
             INNER JOIN sched_labour sl ON
                r.labour_db_id = sl.labour_db_id AND
                r.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_action la
      WHERE
         (la.labour_role_db_id,
          la.labour_role_id)
      IN
         (SELECT
             r.labour_role_db_id,
             r.labour_role_id
          FROM
             sched_labour_role r
             INNER JOIN sched_labour sl ON
                r.labour_db_id = sl.labour_db_id AND
                r.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role r
      WHERE
         (r.labour_db_id,
          r.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         dwt_task_labour_summary ds
      WHERE
         ds.assmbl_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ds.assmbl_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         evt_inv_usage iu
      WHERE
         (iu.event_db_id,
          iu.event_id)
      IN
         (SELECT
             ei.event_db_id,
             ei.event_id
          FROM
             evt_inv ei
             INNER JOIN inv_inv ii ON
                ei.inv_no_db_id = ii.inv_no_db_id AND
                ei.inv_no_id    = ii.inv_no_id
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv ei
      WHERE
         (ei.inv_no_db_id,
          ei.inv_no_id)
      IN
         (SELECT
             ii.inv_no_db_id,
             ii.inv_no_id
          FROM
             inv_inv ii
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv_usage eu
      WHERE
         (eu.event_db_id,
          eu.event_id,
          eu.event_inv_id)
      IN
         (SELECT
             ei.event_db_id,
             ei.event_id,
             ei.event_inv_id
          FROM
             evt_inv ei
             INNER JOIN inv_inv ii ON
                ei.assmbl_inv_no_db_id = ii.inv_no_db_id AND
                ei.assmbl_inv_no_id    = ii.inv_no_id
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv ei
      WHERE
         (ei.assmbl_inv_no_db_id,
          ei.assmbl_inv_no_id)
      IN
         (SELECT
             ii.inv_no_db_id,
             ii.inv_no_id
          FROM
             inv_inv ii
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv_usage eu
      WHERE
         (eu.event_db_id,
          eu.event_id,
          eu.event_inv_id)
      IN
         (SELECT
             ei.event_db_id,
             ei.event_id,
             ei.event_inv_id
          FROM
             evt_inv ei
             INNER JOIN inv_inv ii ON
                ei.nh_inv_no_db_id = ii.inv_no_db_id AND
                ei.nh_inv_no_id    = ii.inv_no_id
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv ei
      WHERE
         (ei.nh_inv_no_db_id,
          ei.nh_inv_no_id)
      IN
         (SELECT
             ii.inv_no_db_id,
             ii.inv_no_id
          FROM
             inv_inv ii
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv ei
      WHERE
         (ei.h_inv_no_db_id,
          ei.h_inv_no_id)
      IN
         (SELECT
             ii.inv_no_db_id,
             ii.inv_no_id
          FROM
             inv_inv ii
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         inv_curr_usage iu
      WHERE
         (iu.inv_no_db_id,
          iu.inv_no_id)
      IN
         (SELECT
             ii.inv_no_db_id,
             ii.inv_no_id
          FROM
             inv_inv ii
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv_usage eiu
      WHERE
         (eiu.event_db_id,
          eiu.event_id,
          eiu.event_inv_id)
      IN
         (SELECT
             ei.event_db_id,
             ei.event_id,
             ei.event_inv_id
          FROM
             evt_inv ei
             INNER JOIN inv_inv ii ON
                ei.inv_no_db_id = ii.inv_no_db_id AND
                ei.inv_no_id    = ii.inv_no_id
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv ei
      WHERE
         (ei.inv_no_db_id,
          ei.inv_no_id)
      IN
         (SELECT
             ii.inv_no_db_id,
             ii.inv_no_id
          FROM
             inv_inv ii
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         inv_curr_usage icu
      WHERE
         (icu.inv_no_db_id,
          icu.inv_no_id)
      IN
         (SELECT
             ii.inv_no_db_id,
             ii.inv_no_id
          FROM
             inv_inv ii
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv ei
      WHERE
         (ei.h_inv_no_db_id,
          ei.h_inv_no_id)
      IN
         (SELECT
             ii.inv_no_db_id,
             ii.inv_no_id
          FROM
             inv_inv ii
             INNER JOIN sched_rmvd_part srp ON
                ii.sched_db_id        = srp.sched_db_id AND
                ii.sched_id           = srp.sched_id AND
                ii.sched_part_id      = srp.sched_part_id AND
                ii.sched_rmvd_part_id = srp.sched_rmvd_part_id
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- need delete sched_rmvd_part which has circle reference between inv_inv
      DeleteSchedRmvdPart(lrec_InventoryRecords.Inv_No_Db_Id,
                          lrec_InventoryRecords.Inv_No_Id,
                          on_Return);

      UPDATE
         inv_inv ii
      SET
         ii.sched_db_id = NULL,
         ii.sched_id = NULL,
         ii.sched_part_id = NULL,
         ii.sched_rmvd_part_id = NULL
      WHERE
         (ii.sched_db_id,
          ii.sched_id,
          ii.sched_part_id,
          ii.sched_rmvd_part_id)
      IN
         (SELECT
             srp.sched_db_id,
             srp.sched_id,
             srp.sched_part_id,
             srp.sched_rmvd_part_id
          FROM
             sched_rmvd_part srp
             INNER JOIN sched_part sp ON
                srp.sched_db_id   = sp.sched_db_id AND
                srp.sched_id      = sp.sched_id AND
                srp.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask ss ON
                sp.sched_db_id = ss.sched_db_id AND
                sp.sched_id    = ss.sched_id
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_rmvd_part srp
      WHERE
         (srp.inv_no_db_id,
          srp.inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN sched_rmvd_part srp ON
                i.sched_db_id        = srp.sched_db_id AND
                i.sched_id           = srp.sched_id AND
                i.sched_part_id      = srp.sched_part_id AND
                i.sched_rmvd_part_id = srp.sched_rmvd_part_id
             INNER JOIN sched_part sp ON
                srp.sched_db_id   = sp.sched_db_id AND
                srp.sched_id      = sp.sched_id AND
                srp.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask ss ON
                sp.sched_db_id = ss.sched_db_id AND
                sp.sched_id = ss.sched_id
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_rmvd_part srp
      WHERE
         (srp.sched_db_id,
          srp.sched_id,
          srp.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask ss ON
                sp.sched_db_id = ss.sched_db_id AND
                sp.sched_id    = ss.sched_id
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_part sp
      WHERE
         (sp.sched_db_id,
          sp.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_action sa
      WHERE
         (sa.sched_db_id,
          sa.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role_status srs
      WHERE
         (srs.labour_role_db_id,
          srs.labour_role_id)
      IN
         (SELECT
             slr.labour_role_db_id,
             slr.labour_role_id
          FROM
             sched_labour_role slr
             INNER JOIN sched_labour sl ON
                slr.labour_db_id = sl.labour_db_id AND
                slr.labour_id    = sl.labour_id
             INNER JOIN sched_stask ss ON
                sl.sched_db_id = ss.sched_db_id AND
                sl.sched_id    = ss.sched_id
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role slr
      WHERE
         (slr.labour_db_id,
          slr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask ss ON
                sl.sched_db_id = ss.sched_db_id AND
                sl.sched_id    = ss.sched_id
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_wo_line swl
      WHERE
         (swl.sched_db_id,
          swl.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sd_fault sf ON
                ss.fault_db_id = sf.fault_db_id AND
                ss.fault_id    = sf.fault_id
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      UPDATE
         sched_stask ss
      SET
         ss.fault_db_id = NULL,
         ss.fault_id = NULL
      WHERE
         (ss.fault_db_id,
          ss.fault_id)
      IN
         (SELECT
             sf.fault_db_id,
             sf.fault_id
          FROM
             sd_fault sf
             INNER JOIN fl_leg f ON
                sf.leg_id = f.leg_id
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sd_fault sf
      WHERE
         sf.leg_id
      IN
         (SELECT
             f.leg_id
          FROM
             fl_leg f
             INNER JOIN inv_ac_reg s ON
                f.aircraft_db_id = s.inv_no_db_id AND
                f.aircraft_id    = s.inv_no_id
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         req_part rp
      WHERE
         (rp.req_ac_inv_no_db_id,
          rp.req_ac_inv_no_id)
      IN
         (SELECT
             ar.inv_no_db_id,
             ar.inv_no_id
          FROM
             inv_ac_reg ar
          WHERE
             ar.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             ar.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         task_ac_rule tar
      WHERE
         (tar.inv_no_db_id,
          tar.inv_no_id)
      IN
         (SELECT
             ar.inv_no_db_id,
             ar.inv_no_id
          FROM
             inv_ac_reg ar
          WHERE
             ar.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             ar.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         fl_leg fl
      WHERE
         (fl.aircraft_db_id,
          fl.aircraft_id)
      IN
         (SELECT
             ar.inv_no_db_id,
             ar.inv_no_id
          FROM
             inv_ac_reg ar
          WHERE
             ar.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             ar.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         lpa_run_inv ri
      WHERE
         (ri.inv_no_db_id,
          ri.inv_no_id)
      IN
         (SELECT
             ar.inv_no_db_id,
             ar.inv_no_id
          FROM
             inv_ac_reg ar
          WHERE
             ar.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             ar.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         inv_ac_reg ar
      WHERE
         ar.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ar.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         inv_advsry
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         inv_curr_usage
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      -- delete sched from evt
      DeleteSchedEvt(lrec_InventoryRecords.Inv_No_Db_Id,
                    lrec_InventoryRecords.Inv_No_Id,
                    on_Return);

      -- delete sched from inventory
      DeleteSchedInv1(lrec_InventoryRecords.Inv_No_Db_Id,
                       lrec_InventoryRecords.Inv_No_Id,
                       on_Return);

      -- sched related
      DeleteSchedRelated(lrec_InventoryRecords.Inv_No_Db_Id,
                     lrec_InventoryRecords.Inv_No_Id,
                     on_Return);

      DELETE FROM
         ship_shipment_line
      WHERE
         inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      -- evt
      DeleteEvent2(lrec_InventoryRecords.Inv_No_Db_Id,lrec_InventoryRecords.Inv_No_Id,on_Return);

      -- delete sched from inventory
      DeleteSchedInv2(lrec_InventoryRecords.Inv_No_Db_Id,lrec_InventoryRecords.Inv_No_Id,on_Return);

      DELETE FROM
         sched_labour_role_status rs
      WHERE
         (rs.labour_role_db_id,
          rs.labour_role_id)
      IN
         (SELECT
             lr.labour_role_db_id,
             lr.labour_role_id
          FROM
             sched_labour_role lr
             INNER JOIN sched_labour sl ON
                lr.labour_db_id = sl.labour_db_id AND
                lr.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role lr
      WHERE
         (lr.labour_db_id,
          lr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_panel lp
      WHERE
         (lp.labour_db_id,
          lp.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_inst_part ip
      WHERE
         (ip.labour_db_id,
          ip.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_rmvd_part rp
      WHERE
         (rp.labour_db_id,
          rp.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_work_type st
      WHERE
         (st.sched_db_id,
          st.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_wo_line sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_action sa
      WHERE
         (sa.sched_db_id,
          sa.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         req_part rp
      WHERE
         (rp.sched_db_id,
          rp.sched_id,
          rp.sched_part_id,
          rp.sched_inst_part_id)
      IN
         (SELECT
             sip.sched_db_id,
             sip.sched_id,
             sip.sched_part_id,
             sip.sched_inst_part_id
          FROM
             sched_inst_part sip
             INNER JOIN sched_part sp ON
                sip.sched_db_id   = sp.sched_db_id AND
                sip.sched_id      = sp.sched_id AND
                sip.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_inst_part sip
      WHERE
         (sip.sched_db_id,
          sip.sched_id,
          sip.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DeleteSchedRmvdPart(lrec_InventoryRecords.Inv_No_Db_Id,
                          lrec_InventoryRecords.Inv_No_Id,
                          on_Return);

      DELETE FROM
         sched_inst_part sip
      WHERE
         (sip.sched_db_id,
          sip.sched_id,
          sip.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- inv_inv and sched_rmvd_part have circle reference to each other
      -- need break it by set forign key in inv_inv table to NULL
      UPDATE
         inv_inv i
      SET
         i.sched_db_id        = NULL,
         i.sched_id           = NULL,
         i.sched_part_id      = NULL,
         i.sched_rmvd_part_id = NULL
      WHERE
         (i.sched_db_id,
          i.sched_id,
          i.sched_part_id,
          i.sched_rmvd_part_id)
      IN
         (SELECT
             srp.sched_db_id,
             srp.sched_id,
             srp.sched_part_id,
             srp.sched_rmvd_part_id
          FROM
             sched_rmvd_part srp
             INNER JOIN sched_part sp ON
                srp.sched_db_id    = sp.sched_db_id AND
                srp.sched_id      = sp.sched_id AND
                srp.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_rmvd_part srp
      WHERE
         (srp.sched_db_id,
          srp.sched_id,
          srp.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role_status slrs
      WHERE
         (slrs.labour_role_db_id,
          slrs.labour_role_id)
      IN
         (SELECT
             slr.labour_role_db_id,
             slr.labour_role_id
          FROM
             sched_labour_role slr
             INNER JOIN sched_labour sl ON
                slr.labour_db_id = sl.labour_db_id AND
                slr.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
             INNER JOIN sched_part sp ON
                s.repl_sched_db_id   = sp.sched_db_id AND
                s.repl_sched_id      = sp.sched_id AND
                s.repl_sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role slr
      WHERE
         (slr.labour_db_id,
          slr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
             INNER JOIN sched_part sp ON
                s.repl_sched_db_id   = sp.sched_db_id AND
                s.repl_sched_id      = sp.sched_id AND
                s.repl_sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
             INNER JOIN sched_part sp ON
                s.repl_sched_db_id   = sp.sched_db_id AND
                s.repl_sched_id      = sp.sched_id AND
                s.repl_sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- sched_part and sched_stask have circle reference to each other
      -- need to break it by set foreign key to NULL in one table
      DeleteSchedPart(lrec_InventoryRecords.Inv_No_Db_Id,
                      lrec_InventoryRecords.Inv_No_Id,
                      on_Return);

      DELETE FROM
         sched_labour_role_status srs
      WHERE
         (srs.labour_role_db_id,
          srs.labour_role_id)
      IN
         (SELECT
             slr.labour_role_db_id,
             slr.labour_role_id
          FROM
             sched_labour_role slr
             INNER JOIN sched_labour sl ON
                slr.labour_db_id = sl.labour_db_id AND
                slr.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.wo_sched_db_id = s.sched_db_id AND
                sl.wo_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role slr
      WHERE
         (slr.labour_db_id,
          slr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.wo_sched_db_id = s.sched_db_id AND
                sl.wo_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.wo_sched_db_id,
          sl.wo_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- need remove referece from h_sched_id -> sched_id in sched_stask table
      UPDATE
         sched_stask ss
      SET
         ss.h_sched_db_id = NULL,
         ss.h_sched_id = NULL
      WHERE
         (ss.h_sched_db_id,
          ss.h_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_work_type swt
      WHERE
         (swt.sched_db_id,
          swt.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_wo_line swl
      WHERE
         (swl.sched_db_id,
          swl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         req_part rp
      WHERE
         (rp.pr_sched_db_id,
          rp.pr_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_action sa
      WHERE
         (sa.sched_db_id,
          sa.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role_status srs
      WHERE
         (srs.labour_role_db_id,
          srs.labour_role_id)
      IN
         (SELECT
             slr.labour_role_db_id,
             slr.labour_role_id
          FROM
             sched_labour_role slr
             INNER JOIN sched_labour sl ON
                slr.labour_db_id = sl.labour_db_id AND
                slr.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.wo_sched_db_id = s.sched_db_id AND
                sl.wo_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role slr
      WHERE
         (slr.labour_db_id,
          slr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.wo_sched_db_id = s.sched_db_id AND
                sl.wo_sched_id = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.wo_sched_db_id,
          sl.wo_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role_status srs
      WHERE
         (srs.labour_role_db_id,
          srs.labour_role_id)
      IN
         (SELECT
             slr.labour_role_db_id,
             slr.labour_role_id
          FROM
             sched_labour_role slr
             INNER JOIN sched_labour sl ON
                slr.labour_db_id = sl.labour_db_id AND
                slr.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role slr
      WHERE
         (slr.labour_db_id,
          slr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         req_part rp
      WHERE
         (rp.sched_db_id,
          rp.sched_id,
          rp.sched_part_id,
          rp.sched_inst_part_id)
      IN
         (SELECT
             ip.sched_db_id,
             ip.sched_id,
             ip.sched_part_id,
             ip.sched_inst_part_id
          FROM
             sched_inst_part ip
             INNER JOIN sched_part sp ON
                ip.sched_db_id   = sp.sched_db_id AND
                ip.sched_id      = sp.sched_id AND
                ip.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_inst_part ip
      WHERE
         (ip.sched_db_id,
          ip.sched_id,
          ip.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_rmvd_part lp
      WHERE
         (lp.sched_db_id,
          lp.sched_id,
          lp.sched_part_id,
          lp.sched_rmvd_part_id)
      IN
         (SELECT
             rp.sched_db_id,
             rp.sched_id,
             rp.sched_part_id,
             rp.sched_rmvd_part_id
          FROM
             sched_rmvd_part rp
             INNER JOIN sched_part sp ON
                rp.sched_db_id   = sp.sched_db_id AND
                rp.sched_id      = sp.sched_id AND
                rp.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- break the reference between inv_inv and sched_rmvd_part
      UPDATE
         inv_inv i
      SET
         i.sched_db_id        = NULL,
         i.sched_id           = NULL,
         i.sched_part_id      = NULL,
         i.sched_rmvd_part_id = NULL
      WHERE
         (i.sched_db_id,
          i.sched_id,
          i.sched_part_id,
          i.sched_rmvd_part_id)
      IN
         (SELECT
             rp.sched_db_id,
             rp.sched_id,
             rp.sched_part_id,
             rp.sched_rmvd_part_id
          FROM
             sched_rmvd_part rp
             INNER JOIN sched_part sp ON
                rp.sched_db_id   = sp.sched_db_id AND
                rp.sched_id      = sp.sched_id AND
                rp.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_rmvd_part rp
      WHERE
         (rp.sched_db_id,
          rp.sched_id,
          rp.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         warranty_eval_part ep
      WHERE
         (ep.workscope_sched_db_id,
          ep.workscope_sched_id,
          ep.workscope_sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- break ref sched_stask.repl_sched_id -> sched_part.sched_id
      UPDATE
         sched_stask ss
      SET
         ss.repl_sched_db_id = NULL,
         ss.repl_sched_id = NULL
      WHERE
         (ss.repl_sched_db_id,
          ss.repl_sched_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_part sp
      WHERE
         (sp.sched_db_id,
          sp.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_work_type swt
      WHERE
         (swt.sched_db_id,
          swt.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_wo_line swl
      WHERE
         (swl.sched_db_id,
          swl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_zone sz
      WHERE
         (sz.sched_db_id,
          sz.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_wo_line swl
      WHERE
         (swl.wo_sched_db_id,
          swl.wo_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_wp_sign ws
      WHERE
         (ws.sign_req_db_id,
          ws.sign_req_id)
      IN
         (SELECT
             sr.sign_req_db_id,
             sr.sign_req_id
          FROM
             sched_wp_sign_req sr
             INNER JOIN sched_wp sw ON
                sr.sched_db_id = sw.sched_db_id AND
                sr.sched_id    = sw.sched_id
             INNER JOIN sched_stask s ON
                sw.sched_db_id = s.sched_db_id AND
                sw.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_wp_sign_req sr
      WHERE
         (sr.sched_db_id,
          sr.sched_id)
      IN
         (SELECT
             sw.sched_db_id,
             sw.sched_id
          FROM
             sched_wp sw
             INNER JOIN sched_stask s ON
                sw.sched_db_id = s.sched_db_id AND
                sw.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         ship_shipment_line sl
      WHERE
         (sl.po_db_id,
          sl.po_id,
          sl.po_line_id)
      IN
         (SELECT
             pl.po_db_id,
             pl.po_id,
             pl.po_line_id
          FROM
             po_line pl
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_wp_sign_req sr
      WHERE
         (sr.sched_db_id,
          sr.sched_id)
      IN
         (SELECT
             sw.sched_db_id,
             sw.sched_id
          FROM
             sched_wp sw
             INNER JOIN sched_stask s ON
                sw.sched_db_id = s.sched_db_id AND
                sw.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         ship_shipment_line sl
      WHERE
         (sl.po_db_id,
          sl.po_id,
          sl.po_line_id)
      IN
         (SELECT
             pl.po_db_id,
             pl.po_id,
             pl.po_line_id
          FROM
             po_line pl
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv_usage eu
      WHERE
         (eu.event_db_id,
          eu.event_id,
          eu.event_inv_id)
      IN
         (SELECT
             ei.event_db_id,
             ei.event_id,
             ei.event_inv_id
          FROM
             evt_inv ei
             INNER JOIN inv_inv i ON
                ei.h_inv_no_db_id = i.inv_no_db_id AND
                ei.h_inv_no_id = i.inv_no_id
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv ei
      WHERE
         (ei.h_inv_no_db_id,
          ei.h_inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv_usage eu
      WHERE
         (eu.event_db_id,
          eu.event_id,
          eu.event_inv_id)
      IN
         (SELECT
             ei.event_db_id,
             ei.event_id,
             ei.event_inv_id
          FROM
             evt_inv ei
             INNER JOIN inv_inv i ON
                ei.inv_no_db_id = i.inv_no_db_id AND
                ei.inv_no_id    = i.inv_no_id
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv ei
      WHERE
         (ei.inv_no_db_id,
          ei.inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv_usage eu
      WHERE
         (eu.event_db_id,
          eu.event_id,
          eu.event_inv_id)
      IN
         (SELECT
             ei.event_db_id,
             ei.event_id,
             ei.event_inv_id
          FROM
             evt_inv ei
             INNER JOIN inv_inv i ON
                ei.nh_inv_no_db_id = i.inv_no_db_id AND
                ei.nh_inv_no_id    = i.inv_no_id
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv ei
      WHERE
         (ei.nh_inv_no_db_id,
          ei.nh_inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         inv_curr_usage iu
      WHERE
         (iu.inv_no_db_id,
          iu.inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      UPDATE
         inv_inv ni
      SET
         ni.nh_inv_no_db_id = NULL,
         ni.nh_inv_no_id = NULL
      WHERE
         (ni.nh_inv_no_db_id,
          ni.nh_inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         inv_xfer ix
      WHERE
         (ix.inv_no_db_id,
          ix.inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         req_part rp
      WHERE
         (rp.inv_no_db_id,
          rp.inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         req_part rp
      WHERE
         (rp.sched_db_id,
          rp.sched_id,
          rp.sched_part_id,
          rp.sched_inst_part_id)
      IN
         (SELECT
             ip.sched_db_id,
             ip.sched_id,
             ip.sched_part_id,
             ip.sched_inst_part_id
          FROM
             sched_inst_part ip
             INNER JOIN inv_inv i ON
                ip.inv_no_db_id = i.inv_no_db_id AND
                ip.inv_no_id    = i.inv_no_id
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_inst_part ip
      WHERE
         (ip.inv_no_db_id,
          ip.inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      UPDATE
         sched_rmvd_part rp
      SET
         rp.inv_no_db_id = NULL,
         rp.inv_no_id = NULL
      WHERE
         (rp.inv_no_db_id,
          rp.inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_rmvd_part rp
      WHERE
         (rp.inv_no_db_id,
          rp.inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- to break ref circle: sched_stask.main_inv_no_id->inv_inv, inv_inv.po_db_id/_id/_line_id->po_line, po_line.sched_id-> sched_stask
      -- may also need break ref po_line.repl_task_id->sched_stask.sched_id
      UPDATE
         po_line pl
      SET
         pl.sched_db_id = NULL,
         pl.sched_id = NULL
      WHERE
         (pl.sched_db_id,
          pl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- need further review on this
      DELETE FROM
         sched_stask ss
      WHERE
         (ss.main_inv_no_db_id,
          ss.main_inv_no_id)
      IN
         (SELECT
             i.inv_no_db_id,
             i.inv_no_id
          FROM
             inv_inv i
             INNER JOIN po_line pl ON
                i.po_db_id   = pl.po_db_id AND
                i.po_id      = pl.po_id AND
                i.po_line_id = pl.po_line_id
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         inv_inv i
      WHERE
         (i.po_db_id,
          i.po_id,
          i.po_line_id)
      IN
         (SELECT
             pl.po_db_id,
             pl.po_id,
             pl.po_line_id
          FROM
             po_line pl
             INNER JOIN sched_stask s ON
                pl.sched_db_id = s.sched_db_id AND
                pl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         po_line pl
      WHERE
         (pl.sched_db_id,
          pl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role_status ls
      WHERE
         (ls.labour_role_db_id,
          ls.labour_role_id)
      IN
         (SELECT
             lr.labour_role_db_id,
             lr.labour_role_id
          FROM
             sched_labour_role lr
             INNER JOIN sched_labour sl ON
                lr.labour_db_id = sl.labour_db_id AND
                lr.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role lr
      WHERE
         (lr.labour_db_id,
          lr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_work_type wt
      WHERE
         (wt.sched_db_id,
          wt.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         zip_task zt
      WHERE
         (zt.sched_db_id,
          zt.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         req_part rp
      WHERE
         (rp.pr_sched_db_id,
          rp.pr_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_action sa
      WHERE
         (sa.sched_db_id,
          sa.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role_status rs
      WHERE
         (rs.labour_role_db_id,
          rs.labour_role_id)
      IN
         (SELECT
             lr.labour_role_db_id,
             lr.labour_role_id
          FROM
             sched_labour_role lr
             INNER JOIN sched_labour sl ON
                lr.labour_db_id = sl.labour_db_id AND
                lr.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role lr
      WHERE
         (lr.labour_db_id,
          lr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_inst_part ip
      WHERE
         (ip.sched_db_id,
          ip.sched_id,
          ip.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- to break reference between sched_rmvd_part and inv_inv
      UPDATE
         inv_inv i
      SET
         i.sched_db_id        = NULL,
         i.sched_id           = NULL,
         i.sched_part_id      = NULL,
         i.sched_rmvd_part_id = NULL
      WHERE
         (i.sched_db_id,
          i.sched_id,
          i.sched_part_id,
          i.sched_rmvd_part_id)
      IN
         (SELECT
             rp.sched_db_id,
             rp.sched_id,
             rp.sched_part_id,
             rp.sched_rmvd_part_id
          FROM
             sched_rmvd_part rp
             INNER JOIN sched_part sp ON
                rp.sched_db_id   = sp.sched_db_id AND
                rp.sched_id      = sp.sched_id AND
                rp.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_rmvd_part rp
      WHERE
         (rp.sched_db_id,
          rp.sched_id,
          rp.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      UPDATE
         sched_stask s
      SET
         s.repl_sched_db_id   = NULL,
         s.repl_sched_id      = NULL,
         s.repl_sched_part_id = NULL
      WHERE
         (s.repl_sched_db_id,
          s.repl_sched_id,
          s.repl_sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_part sp
      WHERE
         (sp.sched_db_id,
          sp.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_work_type wt
      WHERE
         (wt.sched_db_id,
          wt.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_wo_line wl
      WHERE
         (wl.sched_db_id,
          wl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         warranty_init_task it
      WHERE
         (it.sched_db_id,
          it.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         lpa_maint_op_conflict oc
      WHERE
         (oc.lpa_db_id,
          oc.sched_db_id,
          oc.sched_id,
          oc.stask_maint_op_id)
      IN
         (SELECT
              mo.lpa_db_id,
              mo.sched_db_id,
              mo.sched_id,
              mo.stask_maint_op_id
          FROM
             lpa_stask_maint_op mo
             INNER JOIN lpa_stask ls ON
                mo.lpa_db_id   = ls.lpa_db_id AND
                mo.sched_db_id = ls.sched_db_id AND
                mo.sched_id    = ls.sched_id
             INNER JOIN sched_stask s ON
                ls.sched_db_id = s.sched_db_id AND
                ls.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         lpa_stask_maint_op mo
      WHERE
         (mo.lpa_db_id,
          mo.sched_db_id,
          mo.sched_id)
      IN
         (SELECT
             ls.lpa_db_id,
             ls.sched_db_id,
             ls.sched_id
          FROM
             lpa_stask ls
             INNER JOIN sched_stask s ON
                ls.sched_db_id = s.sched_db_id AND
                ls.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         lpa_stask ls
      WHERE
         (ls.sched_db_id,
          ls.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_panel sp
      WHERE
         (sp.sched_db_id,
          sp.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

             
      DELETE FROM
         sched_step_appl_log ss
      WHERE
         (ss.sched_db_id,
          ss.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);
             
      DELETE FROM
         sched_step ss
      WHERE
         (ss.sched_db_id,
          ss.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_zone sz
      WHERE
         (sz.sched_db_id,
          sz.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         dwt_task_labour_summary ls
      WHERE
         (ls.sched_db_id,
          ls.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         dwt_task_labour_summary ls
      WHERE
         (ls.sched_db_id,
          ls.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         req_part rp
      WHERE
         (rp.pr_sched_db_id,
          rp.pr_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_action sa
      WHERE
         (sa.sched_db_id,
          sa.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role_status rs
      WHERE
         (rs.labour_role_db_id,
          rs.labour_role_id)
      IN
         (SELECT
             lr.labour_role_db_id,
             lr.labour_role_id
          FROM
             sched_labour_role lr
             INNER JOIN sched_labour sl ON
                lr.labour_db_id = sl.labour_db_id AND
                lr.labour_id    = sl.labour_id
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_role lr
      WHERE
         (lr.labour_db_id,
          lr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_labour_rmvd_part rp
      WHERE
         (rp.labour_db_id,
          rp.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask s ON
                sl.sched_db_id = s.sched_db_id AND
                sl.sched_id = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_inst_part ip
      WHERE
         (ip.sched_db_id,
          ip.sched_id,
          ip.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      -- update inv_inv to break reference between inv_inv and sched_rmvd_part
      UPDATE
         inv_inv i
      SET
         i.sched_db_id        = NULL,
         i.sched_id           = NULL,
         i.sched_part_id      = NULL,
         i.sched_rmvd_part_id = NULL
      WHERE
         (i.sched_db_id,
          i.sched_id,
          i.sched_part_id,
          i.sched_rmvd_part_id)
      IN
         (SELECT
             rp.sched_db_id,
             rp.sched_id,
             rp.sched_part_id,
             rp.sched_rmvd_part_id
          FROM
             sched_rmvd_part rp
             INNER JOIN sched_part sp ON
                rp.sched_db_id   = sp.sched_db_id AND
                rp.sched_id      = sp.sched_id AND
                rp.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_rmvd_part rp
      WHERE
         (rp.sched_db_id,
          rp.sched_id,
          rp.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         warranty_eval_part ep
      WHERE
         (ep.workscope_sched_db_id,
          ep.workscope_sched_id,
          ep.workscope_sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_part sp
      WHERE
         (sp.sched_db_id,
          sp.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      -- to delete children tables refer to sched_stask on h_sched_id-> sched_id
      DELETE FROM
         dwt_task_labour_summary ls
      WHERE
         (ls.sched_db_id,
          ls.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         req_part rp
      WHERE
         (rp.pr_sched_db_id,
          rp.pr_sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_action sa
      WHERE
         (sa.sched_db_id,
          sa.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_labour_role_status rs
      WHERE
         (rs.labour_role_db_id,
          rs.labour_role_id)
      IN
         (SELECT
             lr.labour_role_db_id,
             lr.labour_role_id
          FROM
             sched_labour_role lr
             INNER JOIN sched_labour sl ON
                lr.labour_db_id = sl.labour_db_id AND
                lr.labour_id    = sl.labour_id
             INNER JOIN sched_stask ss ON
                sl.sched_db_id = ss.sched_db_id AND
                sl.sched_id    = ss.sched_id
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_labour_role lr
      WHERE
         (lr.labour_db_id,
          lr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
             INNER JOIN sched_stask ss ON
                sl.sched_db_id = ss.sched_db_id AND
                sl.sched_id    = ss.sched_id
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_labour sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_inst_part ip
      WHERE
         (ip.sched_db_id,
          ip.sched_id,
          ip.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask ss ON
                sp.sched_db_id = ss.sched_db_id AND
                sp.sched_id    = ss.sched_id
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      -- break the reference between inv_inv and sched_rmvd_part to delete sched_rmvd_part
      UPDATE
         inv_inv i
      SET
         i.sched_db_id        = NULL,
         i.sched_id           = NULL,
         i.sched_part_id      = NULL,
         i.sched_rmvd_part_id = NULL
      WHERE
         (i.sched_db_id,
          i.sched_id,
          i.sched_part_id,
          i.sched_rmvd_part_id)
      IN
         (SELECT
             rp.sched_db_id,
             rp.sched_id,
             rp.sched_part_id,
             rp.sched_rmvd_part_id
          FROM
             sched_rmvd_part rp
             INNER JOIN sched_part sp ON
                rp.sched_db_id   = sp.sched_db_id AND
                rp.sched_id      = sp.sched_id AND
                rp.sched_part_id = sp.sched_part_id
             INNER JOIN sched_stask ss ON
                sp.sched_db_id = ss.sched_db_id AND
                sp.sched_id    = ss.sched_id
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_rmvd_part rp
      WHERE
         (rp.sched_db_id,
          rp.sched_id,
          rp.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask ss ON
                sp.sched_db_id = ss.sched_db_id AND
                sp.sched_id    = ss.sched_id
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_part sp
      WHERE
         (sp.sched_db_id,
          sp.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_work_type wt
      WHERE
         (wt.sched_db_id,
          wt.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
             INNER JOIN sched_stask ss ON
                s.dup_jic_sched_db_id = ss.sched_db_id AND
                s.dup_jic_sched_id = ss.sched_id
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_zone sz
      WHERE
         (sz.sched_db_id,
          sz.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
             INNER JOIN sched_stask ss ON
                s.dup_jic_sched_db_id = ss.sched_db_id AND
                s.dup_jic_sched_id    = ss.sched_id
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_stask s
      WHERE
         (s.dup_jic_sched_db_id,
          s.dup_jic_sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_work_type wt
      WHERE
         (wt.sched_db_id,
          wt.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_wo_line wl
      WHERE
         (wl.sched_db_id,
          wl.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_zone sz
      WHERE
         (sz.sched_db_id,
          sz.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         warranty_eval_task et
      WHERE
         (et.sched_db_id,
          et.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         zip_task zt
      WHERE
         (zt.sched_db_id,
          zt.sched_id)
      IN
         (SELECT
             ss.sched_db_id,
             ss.sched_id
          FROM
             sched_stask ss
             INNER JOIN sched_stask s ON
                ss.h_sched_db_id = s.sched_db_id AND
                ss.h_sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_stask ss
      WHERE
         (ss.h_sched_db_id,
          ss.h_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_work_type wt
      WHERE
         (wt.sched_db_id,
          wt.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_wo_line wl
      WHERE
         (wl.sched_db_id,
          wl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_zone sz
      WHERE
         (sz.sched_db_id,
          sz.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         sched_stask ds
      WHERE
         (ds.dup_jic_sched_db_id,
          ds.dup_jic_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

      DELETE FROM
         inv_loc il
      WHERE
         (il.predraw_sched_db_id,
          il.predraw_sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id );

       UPDATE
          inv_inv i
       SET
          i.po_db_id   = NULL,
          i.po_id      = NULL,
          i.po_line_id = NULL
       WHERE
          (i.po_db_id,
           i.po_id,
           i.po_line_id)
       IN
          (SELECT
              pl.po_db_id,
              pl.po_id,
              pl.po_line_id
           FROM
              po_line pl
              INNER JOIN sched_stask s ON
                 pl.repl_task_db_id = s.sched_db_id AND
                 pl.repl_task_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          ship_shipment_line sl
       WHERE
          (sl.po_db_id,
           sl.po_id,
           sl.po_line_id)
       IN
          (SELECT
              pl.po_db_id,
              pl.po_id,
              pl.po_line_id
           FROM
              po_line pl
              INNER JOIN sched_stask s ON
                 pl.repl_task_db_id = s.sched_db_id AND
                 pl.repl_task_id = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          req_part rp
       WHERE
          (rp.po_db_id,
           rp.po_id,
           rp.po_line_id)
       IN
          (SELECT
              pl.po_db_id,
              pl.po_id,
              pl.po_line_id
           FROM
              po_line pl
              INNER JOIN sched_stask s ON
                 pl.repl_task_db_id = s.sched_db_id AND
                 pl.repl_task_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          po_line_return_map rm
       WHERE
          (rm.po_db_id,
           rm.po_id,
           rm.po_line_id)
       IN
          (SELECT
              pl.po_db_id,
              pl.po_id,
              pl.po_line_id
           FROM
              po_line pl
              INNER JOIN sched_stask s ON
                 pl.repl_task_db_id = s.sched_db_id AND
                 pl.repl_task_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          po_line pl
       WHERE
          (pl.repl_task_db_id,
           pl.repl_task_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          req_part rp
       WHERE
          (rp.shipment_line_db_id,
           rp.shipment_line_id)
       IN
          (SELECT
              sl.shipment_line_db_id,
              sl.shipment_line_id
           FROM
              ship_shipment_line sl
              INNER JOIN ship_shipment ss ON
                 sl.shipment_db_id = ss.shipment_db_id AND
                 sl.shipment_id = ss.shipment_id
              INNER JOIN sched_stask s ON
                 ss.check_db_id = s.sched_db_id AND
                 ss.check_id = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          ship_shipment_line sl
       WHERE
          (sl.shipment_db_id,
           sl.shipment_id)
       IN
          (SELECT
              ss.shipment_db_id,
              ss.shipment_id
           FROM
              ship_shipment ss
              INNER JOIN sched_stask s ON
                 ss.check_db_id = s.sched_db_id AND
                 ss.check_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          ship_segment_map sm
       WHERE
          (sm.shipment_db_id,
           sm.shipment_id)
       IN
          (SELECT
              ss.shipment_db_id,
              ss.shipment_id
           FROM
              ship_shipment ss
              INNER JOIN sched_stask s ON
                 ss.check_db_id = s.sched_db_id AND
                 ss.check_id = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          ship_shipment ss
       WHERE
          (ss.check_db_id,
           ss.check_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          sched_cost_line_item li
       WHERE
          (li.sched_db_id,
           li.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          task_deadline_ext de
       WHERE
          (de.sched_db_id,
           de.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          sched_wo_mpc wm
       WHERE
          (wm.sched_db_id,
           wm.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          lrp_event_usages eu
       WHERE
          (eu.lrp_event_db_id,
           eu.lrp_event_id)
       IN
          (SELECT
              le.lrp_event_db_id,
              le.lrp_event_id
           FROM
              lrp_event le
              INNER JOIN sched_stask s ON
                 le.sched_db_id = s.sched_db_id AND
                 le.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       -- this deletion need disable forign key constraint which currently had problem when calling it from GUI
       DELETE FROM
          lrp_event_workscope pew
       WHERE
          (pew.prev_workscope_db_id,
           pew.prev_workscope_id)
       IN
          (SELECT
              ew.lrp_workscope_db_id,
              ew.lrp_workscope_id
           FROM
              lrp_event_workscope ew
              INNER JOIN lrp_event le ON
                 ew.lrp_event_db_id = le.lrp_event_db_id AND
                 ew.lrp_event_id    = le.lrp_event_id
              INNER JOIN sched_stask s ON
                 le.sched_db_id = s.sched_db_id AND
                 le.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          lrp_event_workscope ew
       WHERE
          (ew.lrp_event_db_id,
           ew.lrp_event_id)
       IN
          (SELECT
              le.lrp_event_db_id,
              le.lrp_event_id
           FROM
              lrp_event le
              INNER JOIN sched_stask s ON
                 le.sched_db_id = s.sched_db_id AND
                 le.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          lrp_event_bucket eb
       WHERE
          (eb.lrp_event_db_id,
           eb.lrp_event_id)
       IN
          (SELECT
              le.lrp_event_db_id,
              le.lrp_event_id
           FROM
              lrp_event le
              INNER JOIN sched_stask s ON
                 le.sched_db_id = s.sched_db_id AND
                 le.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          lrp_event le
       WHERE
          (le.sched_db_id,
           le.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          lrp_event_workscope ew
       WHERE
          (ew.sched_db_id,
           ew.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          warranty_eval_queue eq
       WHERE
          (eq.sched_db_id,
           eq.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          warranty_eval we
       WHERE
          (we.wp_sched_db_id,
           we.wp_sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          warranty_eval_labour el
       WHERE
          (el.warranty_eval_db_id,
           el.warranty_eval_id,
           el.warranty_eval_task_id)
       IN
          (SELECT
              et.warranty_eval_db_id,
              et.warranty_eval_id,
              et.warranty_eval_task_id
           FROM
              warranty_eval_task et
              INNER JOIN sched_stask s ON
                 et.sched_db_id = s.sched_db_id AND
                 et.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          warranty_eval_part ep
       WHERE
          (ep.warranty_eval_db_id,
           ep.warranty_eval_id,
           ep.warranty_eval_task_id)
       IN
          (SELECT
              et.warranty_eval_db_id,
              et.warranty_eval_id,
              et.warranty_eval_task_id
           FROM
              warranty_eval_task et
              INNER JOIN sched_stask s ON
                 et.sched_db_id = s.sched_db_id AND
                 et.sched_id    = s.sched_id
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          warranty_eval_task et
       WHERE
          (et.sched_db_id,
           et.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          claim_part_line pl
       WHERE
          (pl.sched_db_id,
           pl.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          claim_labour_line ll
       WHERE
          (ll.sched_db_id,
           ll.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          sched_stask_flags sf
       WHERE
          (sf.sched_db_id,
           sf.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          sched_wp sw
       WHERE
          (sw.sched_db_id,
           sw.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          sched_impact si
       WHERE
          (si.sched_db_id,
           si.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          inv_oil_status_log sl
       WHERE
          (sl.sched_db_id,
           sl.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          sched_kit_map km
       WHERE
          (km.sched_db_id,
           km.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          sched_service_type st
       WHERE
          (st.sched_db_id,
           st.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          sched_ext_part ep
       WHERE
         (ep.sched_db_id,
          ep.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          fl_leg_disrupt ld
       WHERE
          (ld.sched_db_id,
           ld.sched_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          lpa_run_issue ri
       WHERE
          (ri.block_db_id,
           ri.block_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
          lpa_run_issue ri
       WHERE
          (ri.wp_db_id,
           ri.wp_id)
       IN
          (SELECT
              s.sched_db_id,
              s.sched_id
           FROM
              sched_stask s
           WHERE
              s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
              s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      -- for fault
      DeleteFault(lrec_InventoryRecords.Inv_No_Db_Id,
                     lrec_InventoryRecords.Inv_No_Id,
                     on_Return);

      DELETE FROM
         evt_inv_usage u
      WHERE
         (u.event_db_id,
          u.event_id,
          u.event_inv_id)
      IN
         (SELECT
             s.event_db_id,
             s.event_id,
             s.event_inv_id
          FROM
             evt_inv s
          WHERE
             s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

       DELETE FROM
         evt_inv s
      WHERE
         s.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         s.inv_no_id = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         evt_inv_usage u
      WHERE
         (u.event_db_id,
          u.event_id,
          u.event_inv_id)
      IN
         (SELECT
             s.event_db_id,
             s.event_id,
             s.event_inv_id
          FROM
             evt_inv s
          WHERE
             s.assmbl_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             s.assmbl_inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         evt_inv s
      WHERE
         s.assmbl_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         s.assmbl_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      -- 4. delete evt_event
      DeleteEvtEvent(lrec_InventoryRecords.Inv_No_Db_Id,
                    lrec_InventoryRecords.Inv_No_Id,
                    on_Return);

      DELETE FROM
         inv_xfer x
      WHERE
         x.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         x.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         fnc_xaction_account fx
      WHERE
         (fx.xaction_db_id,
          fx.xaction_id)
      IN
         (SELECT
             x.xaction_db_id,
             x.xaction_id
          FROM
             fnc_xaction_log x
          WHERE
             x.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             x.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         fnc_xaction_log x
      WHERE
         x.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         x.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         inv_sync_queue t
      WHERE
         t.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         t.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         claim_part_line pl
      WHERE
         (pl.claim_db_id,
          pl.claim_id)
      IN
         (SELECT
             c.claim_db_id,
             c.claim_id
          FROM
             claim c
          WHERE
             c.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             c.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         claim_labour_line pl
      WHERE
         (pl.claim_db_id,
          pl.claim_id)
      IN
         (SELECT
             c.claim_db_id,
             c.claim_id
          FROM
             claim c
          WHERE
             c.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             c.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         claim c
      WHERE
         c.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         c.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         usg_usage_data ud
      WHERE
         ud.usage_record_id
      IN
        (SELECT
            ur.usage_record_id
         FROM
            usg_usage_record ur
         WHERE
            ur.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
            ur.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         usg_usage_record ur
      WHERE
         ur.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ur.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         usg_usage_data ud
      WHERE
         ud.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ud.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         usg_usage_data ud
      WHERE
         ud.assmbl_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ud.assmbl_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         sched_labour_rmvd_part rp
      WHERE
         (rp.sched_db_id,
          rp.sched_id,
          rp.sched_part_id,
          rp.sched_rmvd_part_id)
      IN
         (SELECT
             srp.sched_db_id,
             srp.sched_id,
             srp.sched_part_id,
             srp.sched_rmvd_part_id
          FROM
             sched_rmvd_part srp
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      UPDATE
         inv_inv i
      SET
         i.sched_db_id        = NULL,
         i.sched_id           = NULL,
         i.sched_part_id      = NULL,
         i.sched_rmvd_part_id = NULL
      WHERE
         (i.sched_db_id,
          i.sched_id,
          i.sched_part_id,
          i.sched_rmvd_part_id)
      IN
         (SELECT
             srp.sched_db_id,
             srp.sched_id,
             srp.sched_part_id,
             srp.sched_rmvd_part_id
          FROM
             sched_rmvd_part srp
          WHERE
             srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_rmvd_part srp
      WHERE
         srp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         srp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         req_part rp
      WHERE
         (rp.sched_db_id,
          rp.sched_id,
          rp.sched_part_id,
          rp.sched_inst_part_id)
      IN
         (SELECT
             sip.sched_db_id,
             sip.sched_id,
             sip.sched_part_id,
             sip.sched_inst_part_id
          FROM
             sched_inst_part sip
          WHERE
             sip.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             sip.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_labour_inst_part ip
      WHERE
         (ip.sched_db_id,
          ip.sched_id,
          ip.sched_part_id,
          ip.sched_inst_part_id)
      IN
         (SELECT
             sip.sched_db_id,
             sip.sched_id,
             sip.sched_part_id,
             sip.sched_inst_part_id
          FROM
             sched_inst_part sip
          WHERE
             sip.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             sip.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         sched_inst_part sip
      WHERE
         sip.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         sip.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         quar_action_status qs
      WHERE
         (qs.quar_db_id,
          qs.quar_id,
          qs.quar_action_id)
      IN
         (SELECT
             qa.quar_db_id,
             qa.quar_id,
             qa.quar_action_id
          FROM
             quar_action qa
             INNER JOIN quar_quar qq ON
                qa.quar_db_id = qq.quar_db_id AND
                qa.quar_id = qq.quar_id
          WHERE
             qq.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             qq.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         quar_action_assignment qaa
      WHERE
         (qaa.quar_db_id,
          qaa.quar_id,
          qaa.quar_action_id)
      IN
         (SELECT
             qa.quar_db_id,
             qa.quar_id,
             qa.quar_action_id
          FROM
             quar_action qa
             INNER JOIN quar_quar qq ON
                qa.quar_db_id = qq.quar_db_id AND
                qa.quar_id    = qq.quar_id
          WHERE
             qq.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             qq.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         quar_action qa
      WHERE
         (qa.quar_db_id,
          qa.quar_id)
      IN
         (SELECT
             qq.quar_db_id,
             qq.quar_id
          FROM
             quar_quar qq
          WHERE
             qq.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             qq.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         quar_quar qq
      WHERE
         qq.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         qq.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         warranty_init_inv wi
      WHERE
         wi.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         wi.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         zip_task zt
      WHERE
         zt.assmbl_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         zt.assmbl_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

    END LOOP;

    DBMS_OUTPUT.PUT_LINE('out of loop');
--  EXECUTE IMMEDIATE ('ALTER TABLE lrp_event_workscope ENABLE CONSTRAINT FK_PREVEVENTWORKSCOPE_EVENTWOR');
    DBMS_OUTPUT.PUT_LINE('delete all inventory');
    -- delete all inventory
    FOR lrec_InventoryRecords IN lcur_InventoryRecords(anInvNoDbId, anInvNoId) LOOP

      DBMS_OUTPUT.PUT_LINE('inv_key=' || lrec_InventoryRecords.Inv_No_Db_Id || ':' || lrec_InventoryRecords.Inv_No_Id);
      DELETE FROM
         sched_stask s
      WHERE
         s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      UPDATE
         evt_inv ei
      SET
         ei.nh_inv_no_db_id = NULL,
         ei.nh_inv_no_id    = NULL
      WHERE
         ei.nh_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ei.nh_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      UPDATE
         evt_inv ei
      SET
         ei.h_inv_no_db_id = NULL,
         ei.h_inv_no_id    = NULL
      WHERE
         ei.h_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ei.h_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      UPDATE
         evt_inv ei
      SET
         ei.assmbl_inv_no_db_id = NULL,
         ei.assmbl_inv_no_id    = NULL
      WHERE
         ei.assmbl_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ei.assmbl_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      -- to break the ref from inv_inv.assmbl_inv_no_id -> inv_inv.inv_no_id
      UPDATE
         inv_inv ii
      SET
         ii.assmbl_inv_no_db_id = NULL,
         ii.assmbl_inv_no_id    = NULL
      WHERE
         ii.assmbl_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ii.assmbl_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         evt_inv ei
      WHERE
         ei.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ei.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         evt_inv ei
      WHERE
         ei.h_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ei.h_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      -- there are bad data which block deleting root inventory, they have h_inv_no_id -> root inv, but they are
      -- not in inventory tree that to be deleted
      -- to remove those bad need to set theirs h_inv_no_id to theirs inv_no_id
      UPDATE
         inv_inv ii
      SET
         ii.h_inv_no_db_id = ii.inv_no_db_id,
         ii.h_inv_no_id    = ii.inv_no_id
      WHERE
         ii.h_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ii.h_inv_no_id    = lrec_InventoryRecords.Inv_No_Id AND
         ii.inv_class_cd  != 'ACFT';

      DELETE FROM
         sched_stask s
      WHERE
         s.main_inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         s.main_inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         lrp_event_usages eu
      WHERE
         (eu.lrp_event_db_id,
          eu.lrp_event_id)
      IN
         (SELECT
             le.lrp_event_db_id,
             le.lrp_event_id
          FROM
             lrp_event le
             INNER JOIN lrp_inv_inv ii ON
                le.lrp_db_id = ii.lrp_db_id AND
                le.lrp_id = ii.lrp_id AND
                le.lrp_inv_inv_id = ii.lrp_inv_inv_id
          WHERE
             ii.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             ii.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         lrp_event_workscope ew
      WHERE
         (ew.lrp_event_db_id,
          ew.lrp_event_id)
      IN
         (SELECT
             le.lrp_event_db_id,
             le.lrp_event_id
          FROM
             lrp_event le
             INNER JOIN lrp_inv_inv ii ON
                le.lrp_db_id      = ii.lrp_db_id AND
                le.lrp_id         = ii.lrp_id AND
                le.lrp_inv_inv_id = ii.lrp_inv_inv_id
          WHERE
             ii.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             ii.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         lrp_event_bucket eb
      WHERE
         (eb.lrp_event_db_id,
          eb.lrp_event_id)
      IN
         (SELECT
             le.lrp_event_db_id,
             le.lrp_event_id
          FROM
             lrp_event le
             INNER JOIN lrp_inv_inv ii ON
                le.lrp_db_id      = ii.lrp_db_id AND
                le.lrp_id         = ii.lrp_id AND
                le.lrp_inv_inv_id = ii.lrp_inv_inv_id
          WHERE
             ii.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             ii.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         lrp_event le
      WHERE
         (le.lrp_db_id,
          le.lrp_id,
          le.lrp_inv_inv_id)
      IN
         (SELECT
             ii.lrp_db_id,
             ii.lrp_id,
             ii.lrp_inv_inv_id
          FROM
             lrp_inv_inv ii
          WHERE
             ii.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
             ii.inv_no_id    = lrec_InventoryRecords.Inv_No_Id);

      DELETE FROM
         lrp_inv_inv ii
      WHERE
         ii.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         ii.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         lrp_inv_task_plan tp
      WHERE
         tp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         tp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         lrp_inv_adhoc_plan tp
      WHERE
         tp.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         tp.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

      DELETE FROM
         inv_inv i
      WHERE
         i.inv_no_db_id = lrec_InventoryRecords.Inv_No_Db_Id AND
         i.inv_no_id    = lrec_InventoryRecords.Inv_No_Id;

    END LOOP;

    DBMS_OUTPUT.PUT_LINE('l_rowCount= ' || l_rowCount);

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -111;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteAnAircraft failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteAnAircraft;

  PROCEDURE DeleteSchedRmvdPart(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                                anInvNoId   IN inv_inv.inv_no_id%TYPE,
                                on_Return    OUT NUMBER) IS
    lrec_SchedRmvdPartInvRecs lcur_SchedRmvdPartInvRecs%ROWTYPE;

    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;

  BEGIN

    -- Initialize the return value
    on_Return   := icn_NoProc;

    FOR lrec_SchedRmvdPartInvRecs IN lcur_SchedRmvdPartInvRecs(anInvNoDbId,
                                                               anInvNoId) LOOP
      UPDATE
         sched_rmvd_part srp
      SET
         srp.inv_no_db_id = NULL,
         srp.inv_no_id    = NULL
      WHERE
         (srp.sched_db_id,
          srp.sched_id,
          srp.sched_part_id)
      IN
         (SELECT
             sp.sched_db_id,
             sp.sched_id,
             sp.sched_part_id
          FROM
             sched_part sp
             INNER JOIN sched_stask s ON
                sp.sched_db_id = s.sched_db_id AND
                sp.sched_id    = s.sched_id
          WHERE
             s.main_inv_no_db_id = anInvNoDbId AND
             s.main_inv_no_id    = anInvNoId);

      DELETE FROM
         sched_rmvd_part srp
      WHERE
         srp.sched_db_id        = lrec_SchedRmvdPartInvRecs.Sched_Db_Id AND
         srp.sched_id           = lrec_SchedRmvdPartInvRecs.Sched_Id AND
         srp.sched_part_id      = lrec_SchedRmvdPartInvRecs.Sched_Part_Id AND
         srp.sched_rmvd_part_id = lrec_SchedRmvdPartInvRecs.Sched_Rmvd_Part_Id;

    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -107;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteSchedRmvdPart failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteSchedRmvdPart;

  PROCEDURE DeleteSchedPart(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                            anInvNoId   IN inv_inv.inv_no_id%TYPE,
                            on_Return    OUT NUMBER) IS
    lrec_SchedPartInvRecs lcur_SchedPartInvRecs%ROWTYPE;

    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;
  BEGIN

    -- Initialize the return value
    on_Return   := icn_NoProc;

    FOR lrec_SchedPartInvRecs IN lcur_SchedPartInvRecs(anInvNoDbId,
                                                       anInvNoId) LOOP
      UPDATE
         sched_stask ss
      SET
         ss.repl_sched_db_id   = NULL,
         ss.repl_sched_id      = NULL,
         ss.repl_sched_part_id = NULL
      WHERE
         ss.repl_sched_db_id   = lrec_SchedPartInvRecs.Repl_Sched_Db_Id AND
         ss.repl_sched_id      = lrec_SchedPartInvRecs.Repl_Sched_Id AND
         ss.repl_sched_part_id = lrec_SchedPartInvRecs.Repl_Sched_Part_Id;

      DELETE FROM
         sched_part sp
      WHERE
         sp.sched_db_id   = lrec_SchedPartInvRecs.Repl_Sched_Db_Id AND
         sp.sched_id      = lrec_SchedPartInvRecs.Repl_Sched_Id AND
         sp.sched_part_id = lrec_SchedPartInvRecs.Repl_Sched_Part_Id;

    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -108;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteSchedPart failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteSchedPart;

  PROCEDURE DeleteEvent1(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                          anInvNoId   IN inv_inv.inv_no_id%TYPE,
                          on_Return    OUT NUMBER) IS
    lrec_EvtInventoryRecords lcur_EvtInventoryRecords%ROWTYPE;
    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;

  BEGIN

    -- Initialize the return value
    on_Return := icn_NoProc;

    FOR lrec_EvtInventoryRecords IN lcur_EvtInventoryRecords(anInvNoDbId, anInvNoId) LOOP
      DELETE FROM
         evt_stage
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         evt_tool
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         evt_loc
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         evt_inv_usage
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         inv_num_data
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         inv_chr_data
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         evt_event_rel
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         evt_event_rel
      WHERE
         rel_event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         rel_event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         evt_attach
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         evt_dept
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_labour_tool
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         evt_tool
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_labour_parm_data sl
      WHERE
         (sl.event_db_id,
          sl.event_id,
          sl.event_inv_id,
          sl.data_type_db_id,
          sl.data_type_id,
          sl.inv_no_db_id,
          sl.inv_no_id)
      IN
         (SELECT
             s.event_db_id,
             s.event_id,
             s.event_inv_id,
             s.data_type_db_id,
             s.data_type_id,
             s.inv_no_db_id,
             s.inv_no_id
          FROM
             inv_parm_data s
          WHERE
             s.event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
             s.event_id    = lrec_EvtInventoryRecords.Event_Id);

      DELETE FROM
         inv_oil_status_rate os
      WHERE
         (os.event_db_id,
          os.event_id,
          os.event_inv_id,
          os.data_type_db_id,
          os.data_type_id,
          os.inv_no_db_id,
          os.inv_no_id)
      IN
         (SELECT
             s.event_db_id,
             s.event_id,
             s.event_inv_id,
             s.data_type_db_id,
             s.data_type_id,
             s.inv_no_db_id,
             s.inv_no_id
          FROM
             inv_parm_data s
          WHERE
             s.event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
             s.event_id    = lrec_EvtInventoryRecords.Event_Id);

      DELETE FROM
         inv_parm_data s
      WHERE
         s.event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         s.event_id    = lrec_EvtInventoryRecords.Event_Id;

    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -101;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteEvent1 failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteEvent1;

  PROCEDURE DeleteSchedEvt(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                          anInvNoId   IN inv_inv.inv_no_id%TYPE,
                          on_Return    OUT NUMBER) IS
    lrec_EvtInventoryRecords lcur_EvtInventoryRecords%ROWTYPE;
    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;

  BEGIN

    -- Initialize the return value
    on_Return := icn_NoProc;

    FOR lrec_EvtInventoryRecords IN lcur_EvtInventoryRecords(anInvNoDbId,
                                                             anInvNoId) LOOP
      DELETE FROM
         sd_fault_prec_proc
      WHERE
         fault_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         fault_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sd_fault_nature
      WHERE
         fault_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         fault_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sd_fault_result
      WHERE
         fault_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         fault_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_labour_rmvd_part s
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_rmvd_part
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_labour_inst_part
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_inst_part
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_ext_part
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_labour_panel
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_labour_ext_part
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_panel
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_labour_tool
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_labour_step
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_labour_role_status rs
      WHERE
         (rs.labour_role_db_id,
          rs.labour_role_id)
      IN
         (SELECT
             r.labour_role_db_id,
             r.labour_role_id
          FROM
             sched_labour_role r
             INNER JOIN sched_labour s ON
                r.labour_db_id = s.labour_db_id AND
                r.labour_id    = s.labour_id
          WHERE
             s.sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
             s.sched_id    = lrec_EvtInventoryRecords.Event_Id);

      DELETE FROM
         sched_labour_role r
      WHERE
         (r.labour_db_id,
          r.labour_id)
      IN
         (SELECT
             s.labour_db_id,
             s.labour_id
          FROM
             sched_labour s
          WHERE
             s.sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
             s.sched_id    = lrec_EvtInventoryRecords.Event_Id);

      DELETE FROM
         sched_labour
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM sched_wp_sign ws
      WHERE
         (ws.sign_req_db_id,
          ws.sign_req_id)
      IN
         (SELECT
             s.sign_req_db_id,
             s.sign_req_id
          FROM
             sched_wp_sign_req s
          WHERE
             s.sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
             s.sched_id    = lrec_EvtInventoryRecords.Event_Id);

      DELETE FROM
         sched_wp_sign_req
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -102;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteSchedEvt failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteSchedEvt;

  PROCEDURE DeleteSchedInv1(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                             anInvNoId   IN inv_inv.inv_no_id%TYPE,
                             on_Return    OUT NUMBER) IS

    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;

  BEGIN

    -- Initialize the return value
    on_Return := icn_NoProc;

    FOR lrec_SchedInventoryRecords IN lcur_SchedInventoryRecords(anInvNoDbId, anInvNoId) LOOP
      DELETE FROM
         sched_wp_sign_req s
      WHERE
         s.sched_db_id = lrec_SchedInventoryRecords.Sched_Db_Id AND
         s.sched_id    = lrec_SchedInventoryRecords.Sched_Id;

      DELETE FROM
         sched_wp w
      WHERE
         w.sched_db_id = lrec_SchedInventoryRecords.Sched_Db_Id AND
         w.sched_id    = lrec_SchedInventoryRecords.Sched_Id;

    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -103;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteSchedInv1 failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteSchedInv1;

  PROCEDURE DeleteSchedInv2(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                              anInvNoId   IN inv_inv.inv_no_id%TYPE,
                              on_Return    OUT NUMBER) IS

    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;

  BEGIN

    -- Initialize the return value
    on_Return := icn_NoProc;

    FOR lrec_SchedInventoryRecords IN lcur_SchedInventoryRecords(anInvNoDbId, anInvNoId) LOOP

      DELETE FROM
         sched_labour_role_status rs
      WHERE
         (rs.labour_role_db_id,
          rs.labour_role_id)
      IN
         (SELECT
             lr.labour_role_db_id,
             lr.labour_role_id
          FROM
             sched_labour_role lr
             INNER JOIN sched_labour s ON
                lr.labour_db_id = s.labour_db_id AND
                lr.labour_id    = s.labour_id
          WHERE
             s.sched_db_id = lrec_SchedInventoryRecords.Sched_Db_Id AND
             s.sched_id    = lrec_SchedInventoryRecords.Sched_Id);

      DELETE FROM
         sched_labour_role lr
      WHERE
         (lr.labour_db_id,
          lr.labour_id)
      IN
         (SELECT
             sl.labour_db_id,
             sl.labour_id
          FROM
             sched_labour sl
          WHERE
             sl.sched_db_id = lrec_SchedInventoryRecords.Sched_Db_Id AND
             sl.sched_id    = lrec_SchedInventoryRecords.Sched_Id);

      DELETE FROM
         sched_labour sl
      WHERE
         sl.sched_db_id = lrec_SchedInventoryRecords.Sched_Db_Id AND
         sl.sched_id    = lrec_SchedInventoryRecords.Sched_Id;

    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -106;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteSchedInv2 failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteSchedInv2;

  PROCEDURE DeleteSchedRelated(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                           anInvNoId   IN inv_inv.inv_no_id%TYPE,
                           on_Return    OUT NUMBER) IS
    lrec_EvtInventoryRecords lcur_EvtInventoryRecords%ROWTYPE;
    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;

  BEGIN

    -- Initialize the return value
    on_Return := icn_NoProc;

    FOR lrec_EvtInventoryRecords IN lcur_EvtInventoryRecords(anInvNoDbId, anInvNoId) LOOP
      DELETE FROM
         sched_work_type
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_action
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_service_type
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_step_appl_log
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;
         
      DELETE FROM
         sched_step
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_wp
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_cost_line_item
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_wo_mpc
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_part
      WHERE
         sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         evt_sched_dead
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -104;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteSchedRelated failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteSchedRelated;

  PROCEDURE DeleteEvent2(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                          anInvNoId   IN inv_inv.inv_no_id%TYPE,
                          on_Return    OUT NUMBER) IS
    lrec_EvtInventoryRecords lcur_EvtInventoryRecords%ROWTYPE;
    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;

  BEGIN

    -- Initialize the return value
    on_Return := icn_NoProc;

    FOR lrec_EvtInventoryRecords IN lcur_EvtInventoryRecords(anInvNoDbId, anInvNoId) LOOP

      DELETE FROM
         evt_stage
      WHERE
         event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         event_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         ship_segment_map m
      WHERE
         (m.shipment_db_id,
          m.shipment_id)
      IN
         (SELECT
             s.shipment_db_id,
             s.shipment_id
          FROM
             ship_shipment s
          WHERE
             s.check_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
             s.check_id    = lrec_EvtInventoryRecords.Event_Id);

      DELETE FROM
         ship_shipment_line l
      WHERE
         (l.shipment_db_id,
          l.shipment_id)
      IN
         (SELECT
             s.shipment_db_id,
             s.shipment_id
          FROM
             ship_shipment s
          WHERE
             s.check_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
             s.check_id    = lrec_EvtInventoryRecords.Event_Id);

      DELETE FROM
         ship_shipment s
      WHERE
         s.shipment_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         s.shipment_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         ship_shipment s
      WHERE
         s.check_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         s.check_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         sched_wo_line s
      WHERE
         s.wo_sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         s.wo_sched_id    = lrec_EvtInventoryRecords.Event_Id;

      DELETE FROM
         inv_oil_status_log sl
      WHERE
         (sl.sched_db_id,
          sl.sched_id)
      IN
         (SELECT
             s.sched_db_id,
             s.sched_id
          FROM
             sched_stask s
          WHERE
             s.sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
             s.sched_id    = lrec_EvtInventoryRecords.Event_Id);

      DELETE FROM
         sched_stask s
      WHERE
         s.sched_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         s.sched_id    = lrec_EvtInventoryRecords.Event_Id;

    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -105;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteEvent2 failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteEvent2;

  PROCEDURE DeleteFault(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                           anInvNoId   IN inv_inv.inv_no_id%TYPE,
                           on_Return    OUT NUMBER) IS
    lrec_EvtInventoryRecords lcur_EvtInventoryRecords%ROWTYPE;
    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;

  BEGIN

    -- Initialize the return value
    on_Return := icn_NoProc;

    FOR lrec_EvtInventoryRecords IN lcur_EvtInventoryRecords(anInvNoDbId, anInvNoId) LOOP

      DELETE FROM
         sd_fault s
      WHERE
         s.fault_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         s.fault_id = lrec_EvtInventoryRecords.Event_Id;
    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -109;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteFault failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteFault;

  PROCEDURE DeleteEvtEvent(anInvNoDbId IN inv_inv.inv_no_db_id%TYPE,
                          anInvNoId   IN inv_inv.inv_no_id%TYPE,
                          on_Return    OUT NUMBER) IS
    lrec_EvtInventoryRecords lcur_EvtInventoryRecords%ROWTYPE;
    -- local variables
    ll_InvNoDbId  inv_inv.inv_no_db_id%TYPE;
    ll_InvNoId    inv_inv.inv_no_id%TYPE;

  BEGIN

    -- Initialize the return value
    on_Return := icn_NoProc;

    FOR lrec_EvtInventoryRecords IN lcur_EvtInventoryRecords(anInvNoDbId, anInvNoId) LOOP

      DELETE FROM
         evt_event e
      WHERE
         e.event_db_id = lrec_EvtInventoryRecords.Event_Db_Id AND
         e.event_id = lrec_EvtInventoryRecords.Event_Id;

    END LOOP;

    IF on_Return < 0 THEN
      RETURN;
    END IF;

    -- Set the return value
    on_Return := icn_Success;

  EXCEPTION
    WHEN OTHERS THEN
      on_Return := -110;
      APPLICATION_OBJECT_PKG.SetMxiError('DEV-99999',
                                         'INV_DELETE_PKG@@@DeleteEvtEvent failed@@@' ||
                                         SQLERRM);
      RETURN;

  END DeleteEvtEvent;

END INV_DELETE_PKG;
/