/*******************************************************************************
* Script Filename  : OPER-14748
*
* Original Coder   : K. Tomson
* Last Modified By :
* Last Modified On : Aug 29th, 2018
* Code Review By   : 
* Code Review Date : 
*
* Script Description: 
*    OPER-14748
*    NOT FOR USE ON CUSTOMER DATABASES!!!
*    This is to purge the fl_leg tables ext_key column when there are active 
*    duplicate external keys. Customers do not have this issue, but our Dev data does.
*
* Confidential, proprietary and/or trade secret information of  Mxi Technologies Ltd.
* Copyright 2018 Mxi Technologies Ltd. All Rights Reserved.
*
********************************************************************************/
BEGIN
     FOR lc_dup_actv_ext_key IN (SELECT ext_key FROM ( SELECT COUNT(1) total, ext_key 
                                                       FROM fl_leg where EXT_KEY IS NOT NULL AND hist_bool=0
                                                       GROUP BY fl_leg.ext_key
                                                     ) 
                                  WHERE total > 1 ) LOOP
                                 
         UPDATE fl_leg 
         SET ext_key = NULL 
         WHERE ext_key = lc_dup_actv_ext_key.ext_key;

     END LOOP;

     COMMIT;

   EXCEPTION
       WHEN OTHERS THEN ROLLBACK;
END;
/

