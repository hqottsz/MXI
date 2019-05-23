--liquibase formatted sql


--changeSet MATERIALS_REQUEST_STATUS:1 stripComments:false
/********************************************************************************
*
* Materialized View: MATERIALS_REQUEST_STATUS
*
* Description:    This materialized view will return the details of the
*                 part requests
*
*********************************************************************************/
CREATE MATERIALIZED VIEW MT_MATERIALS_REQUEST_STATUS
PCTFREE 0
PCTUSED 99
BUILD DEFERRED
REFRESH ON DEMAND
WITH PRIMARY KEY
AS
SELECT  *
FROM  MT_MAT_REQ_STAT_LOG;