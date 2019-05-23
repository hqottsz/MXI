--liquibase formatted sql


--changeSet MX-23342:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Procedue:      INSERT_UNIQUE_EVENT_REL
* Arguments:     in  - all evt_event_rel column values, except the audit columns
*                out - return value of success (1) or failure (0)
*
* Description:   This procedure attempts to insert a record into the 
*                evt_event_rel table given the provided table values.
*
*                If the PK already exists, the exception is caught and a value 
*                of 0 is returned.
*                If the insert is successful a value of 1 is returned.
*                Other exceptions are raised to the caller.
*
*                The reason for having this procudure is to handle the concurrent
*                insertions by the EvtEventRel table class.  If the java class 
*                were to capture the exception, the session would be marked as 
*                rollback only.  Thus, an attempt to change the key and insert 
*                again would still fail.  Refer to MX-23342.
*
*********************************************************************************/
CREATE OR REPLACE PROCEDURE INSERT_UNIQUE_EVENT_REL(
   aEventDbId     IN NUMBER,
   aEventId       IN NUMBER,
   aEventRelId    IN NUMBER,
   aRelEventDbId  IN NUMBER,
   aRelEventId    IN NUMBER,
   aRelTypeDbId   IN NUMBER,
   aRelTypeCd     IN STRING,
   aRelEventOrd   IN NUMBER,
   aReturn       OUT NUMBER
)
IS

BEGIN
   BEGIN
      INSERT INTO evt_event_rel (
         event_db_id, 
         event_id, 
         event_rel_id, 
         rel_event_db_id, 
         rel_event_id, 
         rel_type_db_id, 
         rel_type_cd, 
         rel_event_ord
      )
      VALUES (
         aEventDbId,
         aEventId,
         aEventRelId,
         aRelEventDbId,
         aRelEventId,
         aRelTypeDbId,
         aRelTypeCd,
         aRelEventOrd
      );
      aReturn := 1;
   EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN
         aReturn := 0;
   END;
END INSERT_UNIQUE_EVENT_REL;
/		