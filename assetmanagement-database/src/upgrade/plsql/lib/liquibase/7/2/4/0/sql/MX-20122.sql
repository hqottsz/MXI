--liquibase formatted sql


--changeSet MX-20122:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   DELETE FROM
      sched_panel
   WHERE
      panel_db_id IS NULL;
   
   DELETE FROM
      sched_zone
   WHERE
      zone_db_id IS NULL;
END;
/