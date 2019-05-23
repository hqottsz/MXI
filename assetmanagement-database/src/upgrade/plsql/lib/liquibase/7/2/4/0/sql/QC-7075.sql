--liquibase formatted sql


--changeSet QC-7075:1 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('FLEET_MTC');
END;
/

--changeSet QC-7075:2 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.TYPE_DROP('FLEET_MTC_REC');
END;
/

--changeSet QC-7075:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE "FLEET_MTC_REC" AS OBJECT
(
  -- Author  : ABHISHEK
  -- Created : 6/29/2010 4:35:10 AM
  -- Purpose :

  -- Attributes
      task_name        VARCHAR(200),
      work_type        VARCHAR(4000),
      plan_1           NUMBER(1),
      plan_2           NUMBER(1),
      is_future        NUMBER(1)

);
/

--changeSet QC-7075:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE TYPE "FLEET_MTC" as TABLE OF fleet_mtc_rec;
/