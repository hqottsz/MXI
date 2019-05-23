--liquibase formatted sql


--changeSet MX-18462:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Procedure:    SendWarrantyEvaluationAlert
* Arguments:    task_db_id 	(number) - db_id of the work-package
		task_id 	(number) - id of the work-package
		venloc_db_id	(number) - db_id of the vendor/location key
		venloc_id	(numbeR) - id of the vendor/location key
		venloc_disp_str (varchar)- string to display in alert message
		venloc_type	(varchar)- string to define whether the venloc is a vendor, 
				or a location key [VENDOR, LOCATION]
* Return:       on_Return    (number) - success/failure of the procedure
*
* Description:  
		When a work package is scheduled to internal (location) or external (vendor) AND the scheduled location/vendor is not an authorized repair location/vendor on at least one active warranty on the work package inventory 
		NOTE: This case does not rely on the warranty evaluation logic  
		When evaluation logic determines that a work package with warranty is scheduled at non-authorized vendor or internal repair location based on warranty contract vendor and/or location lists. 
* Orig.Coder:   abhishek
* Orig.Date:  	2008-08-27
*
*********************************************************************************
*
* Copyright ? 1998-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE PROCEDURE SendWarrantyEvaluationAlert(
        task_db_id IN NUMBER,
        task_id	IN NUMBER,
        venloc_db_id IN NUMBER,
        venloc_id IN NUMBER,
        venloc_disp_str IN VARCHAR,
        venloc_type IN VARCHAR,
        on_Return OUT NUMBER
   ) IS

   /* local variables */
   lcur_AlertType NUMBER; /* Define the Alert Type; 123 */  
   lparm_value  evt_event.event_sdesc%TYPE;
   t_tab_alert_parm alert_pkg.gt_tab_alert_parm;
   
BEGIN


  SELECT
	event_sdesc
     INTO
      lparm_value 
		 FROM
			evt_event     
		 WHERE
		       evt_event.event_db_id = task_db_id and
		       evt_Event.event_id = task_id;


   /* Warranty Evaluation Alert = 123*/
   lcur_AlertType := 123;
  
    /* Define the parameters for the alert */
   t_tab_alert_parm.DELETE;
   
           /* First parameter will always be a work-package*/ 
            t_tab_alert_parm(1).parm_ord := 1;
            t_tab_alert_parm(1).parm_type := 'TASK';
            t_tab_alert_parm(1).parm_value_sdesc := lparm_value;
	    t_tab_alert_parm(1).parm_value := task_db_id||':'||task_id;   
            
            
            
            /* Second parameter is the WP barcode */ 
            t_tab_alert_parm(2).parm_ord := 2;
            t_tab_alert_parm(2).parm_type := venloc_type;
            t_tab_alert_parm(2).parm_value_sdesc := venloc_disp_str;
            t_tab_alert_parm(2).parm_value := venloc_db_id||':'||venloc_id;                                         
		 
		       
		   /* Insert an Alert*/         
   alert_pkg.raise_alert(lcur_AlertType ,0,                  
                         p_alert_parm_tb => t_tab_alert_parm);  

  on_Return := 0;



EXCEPTION
   WHEN OTHERS THEN
      on_Return := -1;
      application_object_pkg.SetMxiError('DEV-99999', SQLERRM);
END SendWarrantyEvaluationAlert;
/

--changeSet MX-18462:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Procedure:    SendWarrantyEvalFailureAlert
* Arguments:    task_db_id 	(number) - db_id of the work-package
		task_id 	(number) - id of the work-package		
		barcode_str (varchar)- string to display in alert message
* Return:       on_Return    (number) - success/failure of the procedure
*
* Description:  
		Raise an Alert when a work package is scheduled to for evalution; and the evaluation fails.
* Orig.Coder:   abhishek
* Orig.Date:  	2008-09-19
*
*********************************************************************************
*
* Copyright ? 1998-2008 Mxi Technologies Ltd.  All Rights Reserved.
* Any distribution of the Mxi source code by any other party than
* Mxi Technologies Ltd is prohibited.
*
*********************************************************************************/
CREATE OR REPLACE PROCEDURE SendWarrantyEvalFailureAlert(
        task_db_id IN NUMBER,
        task_id	IN NUMBER,
        barcode_str IN VARCHAR,
        on_Return OUT NUMBER
   ) IS

   /* local variables */
   lcur_AlertType NUMBER; /* Define the Alert Type; 124 */ 
   
   t_tab_alert_parm alert_pkg.gt_tab_alert_parm;
   lparm_value  evt_event.event_sdesc%TYPE; 
   
   
   BEGIN
   
   SELECT
			event_sdesc
     INTO
      lparm_value 
		 FROM
			evt_event     
		 WHERE
		       evt_event.event_db_id = task_db_id and
		       evt_Event.event_id = task_id;

    

   /* Warranty Evaluation Alert = 124*/
   lcur_AlertType := 124;

  
    /* Define the parameters for the alert */
   t_tab_alert_parm.DELETE;
   
           /* First parameter will always be a work-package*/ 
            t_tab_alert_parm(1).parm_ord := 1;
            t_tab_alert_parm(1).parm_type := 'TASK';
            t_tab_alert_parm(1).parm_value_sdesc := lparm_value;
			      t_tab_alert_parm(1).parm_value := task_db_id||':'||task_id;   
            
            
            
            /* Second parameter is the WP barcode */ 
            t_tab_alert_parm(2).parm_ord := 2;
            t_tab_alert_parm(2).parm_type := 'STRING';
            t_tab_alert_parm(2).parm_value_sdesc := barcode_str;
            t_tab_alert_parm(2).parm_value := barcode_str;                                         
		 
		       
		   /* Insert an Alert*/         
   alert_pkg.raise_alert(lcur_AlertType ,0,                  
                         p_alert_parm_tb => t_tab_alert_parm);     
            
    on_Return := 0;
  -- This procedure is invoked in the event of a failure; explicitly set execute_bool to false [so it'll be processed again]
  -- Also, commit explicity so the Alerts will be visible
  UPDATE warranty_eval_queue
  SET
     execute_bool = 0
  WHERE
     warranty_eval_queue.sched_db_id 	= task_db_id AND
     warranty_eval_queue.sched_id 	= task_id;
  COMMIT;

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -1;
      application_object_pkg.SetMxiError('DEV-99999', SQLERRM);
END SendWarrantyEvalFailureAlert;
/       

--changeSet MX-18462:3 stripComments:true endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   UTL_MIGR_SCHEMA_PKG.PROCEDURE_DROP('SENDWARRANTYEVALUATIONALERT');            
END;
/                                       