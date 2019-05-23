--liquibase formatted sql


--changeSet mv_material_request_status:1 stripComments:false
/********************************************************************************
*
* View:           mv_material_request_status
*
* Description:    This materialized view will return the details of the
*                 part requests
*
* Orig.Coder:    bparekh
* Recent Coder:  gtate
* Recent Date:   12 DEC 2018
*
*********************************************************************************/
CREATE OR REPLACE FORCE VIEW "MV_MATERIALS_REQUEST_STATUS"
AS
SELECT
   *
FROM
   MT_MATERIALS_REQUEST_STATUS;