/******************************************************************************
* Script Filename  : locked_acft.sql
*
* Script Description: locks specific aircraft to use for test test_AL_TASK_018_InventoryLocked
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
-- SPOOL log\locked_acft.log

   BEGIN
           UPDATE inv_inv set locked_bool =1 WHERE serial_no_oem = 'SN-LOCKED';
      
  END;
  /
   
-- SPOOL OFF; 