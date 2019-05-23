--liquibase formatted sql


--changeSet QC-4376:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
CREATE OR REPLACE FUNCTION 
   getWorkType
   ( 
    aSchedDbid sched_stask.sched_db_id% TYPE,
    aSchedId  sched_stask.sched_id% TYPE
   ) 
RETURN VARCHAR2 

IS
   lWorkType VARCHAR2(20);
   lCount NUMBER;

BEGIN

  SELECT
      COUNT(*) INTO lcount 
  FROM 
      sched_work_type 
  WHERE 
      sched_work_type.sched_db_id = aSchedDbid AND
      sched_work_type.sched_id = aSchedId;

  IF(lcount = 0) THEN
      lWorkType := '';
      
  ELSE IF(lCount > 1) THEN
      lWorkType := 'MULTIPLE';
      
  ELSE
      SELECT  
          sched_work_type.work_type_cd  INTO  lWorkType 
      FROM 
          sched_work_type 
      WHERE 
          sched_work_type.sched_db_id = aSchedDbid AND
          sched_work_type.sched_id = aSchedId;
  END IF;
  END IF;
  
  RETURN lWorkType;
END GETWORKTYPE;
/