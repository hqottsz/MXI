--liquibase formatted sql


--changeSet MX-26557:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('PTASSIGNEDMANHRSRELTABLE');
END;
/

--changeSet MX-26557:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('PTASSIGNEDMANHRSREL');
END;
/

--changeSet MX-26557:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE PTASSIGNEDMANHRSREL
 AS OBJECT (
 lrp_event_db_id     NUMBER(10),
 lrp_event_id        NUMBER(10),
 planning_type_db_id NUMBER(10),
 planning_type_id    NUMBER(10),
 planning_type_cd    VARCHAR2(80),
 assignedManhours    NUMBER(6,2)
 );
/

--changeSet MX-26557:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE PTASSIGNEDMANHRSRELTABLE AS TABLE OF PTAssignedManhrsRel;
/