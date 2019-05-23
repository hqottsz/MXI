--liquibase formatted sql


--changeSet MX-17445:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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
   lcur_AlertId NUMBER; /* ID of the alert to be posted */
   
   /* cursor declarations */
   CURSOR lcur_AlertUsers(lAlertType IN NUMBER) IS
      	SELECT user_id 
	FROM 
	     utl_user_role, 
	     utl_alert_type_role
	WHERE
	     utl_user_role.role_id = utl_alert_type_role.role_id
	     AND
	     utl_alert_type_role.alert_type_id = lAlertType;
	     
   
   
   CURSOR lcur_Utl IS
   	SELECT
   		mim_local_db.db_id 
   	FROM mim_local_db;
   	
   lrec_Utl lcur_Utl%ROWTYPE;

BEGIN

   /* Warranty Evaluation Alert = 123*/
   lcur_AlertType := 123;
   
   /* Read the next sequence for the Alert Table*/
   	SELECT
            alert_id.NEXTVAL
        INTO
            lcur_AlertId
        FROM
        	dual;
   
   
   OPEN  lcur_Utl;
   FETCH lcur_Utl into lrec_Utl;
   CLOSE lcur_Utl;
      
   /* Insert an Alert*/ 
   INSERT INTO utl_alert(alert_id, alert_type_id, alert_status_cd, utl_id,priority,alert_timestamp)
   VALUES( lcur_AlertId, lcur_AlertType,'NEW', lrec_Utl.Db_Id,0,sysdate);  
  
  /* Define the parameters for the alert */
  /* First parameter will always be a work-package*/
  INSERT INTO utl_alert_parm(
         alert_id, 
         parm_id, 
         parm_ord,
         parm_type,
         parm_value_sdesc,
         parm_value,
         utl_id
         ) values(
         lcur_AlertId, 
         1,
         1,
         'TASK',
         (
		 SELECT 
			evt_Event.Event_Sdesc
		 FROM
			evt_event
		 WHERE 
		       evt_event.event_db_id = task_db_id and
		       evt_Event.event_id = task_id
         ),
         task_db_id||':'||task_id,
         lrec_Utl.Db_Id
  );
  
  /* Second parameter may be either Vendor/Location key */
  INSERT INTO utl_alert_parm(
         alert_id, 
         parm_id, 
         parm_ord,
         parm_type,
         parm_value_sdesc,
         parm_value,
         utl_id
         ) VALUES(
         lcur_AlertId, 
         2,
         2,
         venloc_type,
         venloc_disp_str,
         venloc_db_id||':'||venloc_id,
         lrec_Utl.Db_Id
  );
  
  
  
  FOR users IN lcur_AlertUsers( lcur_AlertType ) LOOP
  	/* Notify the role participants of the alert */
      INSERT INTO utl_user_alert( alert_id, user_id,utl_id )
      VALUES( lcur_AlertId, users.user_id, lrec_Utl.Db_Id );
  END LOOP;
  
  on_Return := 0;

   

EXCEPTION
   WHEN OTHERS THEN
      on_Return := -1;
      application_object_pkg.SetMxiError('DEV-99999', SQLERRM);
END SendWarrantyEvaluationAlert;

/