/******************************************************************************
* Script Filename  : MVIEW_FleetList.sql
*
* Script Description: Refreshes Fleet List materialized view
*                     
*                     
*
* ******************************************************************************
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2016 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/


-- Spool processing log
-- SPOOL log\mview_fleetlist.log

   BEGIN
           DBMS_MVIEW.REFRESH( LIST=>'MV_FLEETLIST', method => 'C', atomic_refresh => FALSE);
   END;
   /
    
   

-- SPOOL OFF;