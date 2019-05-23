--liquibase formatted sql


--changeSet EVENT_PKG_UPDTDEADLNFORINVTREE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure EVENT_PKG_UPDTDEADLNFORINVTREE(
                  an_InvNoDbId  IN  inv_inv.inv_no_db_id%TYPE,
                  an_InvNoId    IN  inv_inv.inv_no_id%TYPE,
                  on_Return     OUT NUMBER) is
begin
  EVENT_PKG.UpdateDeadlineForInvTree(an_InvNoDbId, an_InvNoId, on_Return);
end EVENT_PKG_UPDTDEADLNFORINVTREE;
/