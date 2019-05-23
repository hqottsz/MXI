--liquibase formatted sql


--changeSet MX-23673:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('TAB_PTROUTINEMANHOURS');
END;
/

--changeSet MX-23673:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('PTROUTINEMANHOURS');
END;
/

--changeSet MX-23673:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE PTROUTINEMANHOURS
 IS OBJECT (
    planning_type_db_id NUMBER(10),
    planning_type_id    NUMBER(10),
    planning_type_cd    VARCHAR2(80),
    manhours            FLOAT(22));
/

--changeSet MX-23673:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE TAB_PTROUTINEMANHOURS AS TABLE OF PTRoutineManHours;
/