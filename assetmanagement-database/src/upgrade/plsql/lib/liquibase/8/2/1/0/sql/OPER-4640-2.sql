--liquibase formatted sql


--changeSet OPER-4640-2:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      refreshMView
* Arguments:     aViewName       The name of the Materialized View
*
* Description:   This procedure calls for a Complete refresh of a specified view
*
* Orig.Coder:    Jonathan Clarkin
* Recent Coder:
* Recent Date:   June 12, 2008
*
*********************************************************************************/
CREATE OR REPLACE PROCEDURE refreshMView
(
   aViewName IN VARCHAR2
)
IS
BEGIN
   dbms_mview.refresh( aViewName, 'C' );
   --Rebuild the index when maintenix refresh the mview "mv_materials_request_status"
   IF aViewName = 'MV_MATERIALS_REQUEST_STATUS'  THEN
      EXECUTE IMMEDIATE 'ALTER INDEX ix_mv_mc REBUILD COMPUTE STATISTICS';
   END IF;
END refreshMView;
/