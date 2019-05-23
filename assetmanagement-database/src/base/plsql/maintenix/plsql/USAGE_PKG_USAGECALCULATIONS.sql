--liquibase formatted sql


--changeSet USAGE_PKG_USAGECALCULATIONS:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure USAGE_PKG_USAGECALCULATIONS (an_InvNoDbId      IN inv_inv.inv_no_db_id%TYPE,
      																	an_InvNoId        IN inv_inv.inv_no_id%TYPE,
      																	on_Return         OUT NUMBER) is
begin
   USAGE_PKG.UsageCalculations(an_InvNoDbId, an_InvNoId, on_Return);
end;
/