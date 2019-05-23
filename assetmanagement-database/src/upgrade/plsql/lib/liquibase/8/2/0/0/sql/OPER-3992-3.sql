--liquibase formatted sql


--changeSet OPER-3992-3:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure PREP_DEADLINE_PKG_PREP_INV
        (an_InvNoDbId      IN inv_inv.inv_no_db_id%TYPE,
         an_InvNoId        IN inv_inv.inv_no_id%TYPE,
         anSyncWithBaseline IN INTEGER,
         on_Return         OUT NUMBER) is
begin
   prep_deadline_pkg.PrepareDeadlineForInv(an_InvNoDbId, an_InvNoId, sys.diutil.int_to_bool(anSyncWithBaseline), on_Return);
   
END PREP_DEADLINE_PKG_PREP_INV;
/