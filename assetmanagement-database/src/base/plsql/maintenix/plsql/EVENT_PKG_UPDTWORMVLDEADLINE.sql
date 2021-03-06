--liquibase formatted sql


--changeSet EVENT_PKG_UPDTWORMVLDEADLINE:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
create or replace procedure EVENT_PKG_UPDTWORMVLDEADLINE(
      al_EventDbId IN evt_event.event_db_id%TYPE,
      al_EventId IN evt_event.event_id%TYPE,
      al_Return OUT NUMBER)
   IS
BEGIN
   EVENT_PKG.UpdateWormvlDeadline(Al_Eventdbid, Al_Eventid, Al_Return);
END EVENT_PKG_UPDTWORMVLDEADLINE;
/