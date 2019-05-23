--liquibase formatted sql


--changeSet DEV-1161:1 stripComments:false
-- add new POAUTH event status for Orders
INSERT INTO ref_event_status
	(
		event_status_db_id, 
		event_status_cd, 
		event_type_db_id, 
		event_type_cd, 
		bitmap_db_id, 
		bitmap_tag, 
		desc_sdesc, 
		desc_ldesc, 
		user_status_cd, 
		status_ord, 
		auth_reqd_bool, 
		rstat_cd, 
		creation_dt, 
		revision_dt, 
		revision_db_id, 
		revision_user
	)
SELECT 
	0, 
	'POAUTH', 
	0, 
	'PO', 
	0, 
	01, 
	'Order authorized', 
	'This order has been authorized and can be issued.', 
	'AUTH', 
	'10', 
	'0',  
	0, 
	to_date('27-07-2011 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
	to_date('27-07-2011 00:00:00', 'dd-mm-yyyy hh24:mi:ss'), 
	100, 
	'MXI'
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			ref_event_status 
		WHERE 
			ref_event_status.event_status_db_id = 0 AND
			ref_event_status.event_status_cd = 'POAUTH'
	)
; 	

--changeSet DEV-1161:2 stripComments:false
-- add SPEC2000_ORDER_NUM_LIMIT config parameter
INSERT INTO utl_config_parm 
	(
		parm_name, 
		parm_value, 
		parm_type, 
		parm_desc, 
		config_type, 
		allow_value_desc, 
		default_value, 
		mand_config_bool, 
		category, 
		modified_in, 
		utl_id
	)
SELECT 
	'SPEC2000_ORDER_NUM_LIMIT', 
	'FALSE', 
	'LOGIC', 
	'Order Barcode must not exceed nine characters.', 
	'GLOBAL', 
	'TRUE/FALSE', 
	'FALSE', 
	1, 
	'Core Logic', 
	'8.0', 
	0
FROM 
	dual 
WHERE NOT EXISTS 
	(
		SELECT 
			1 
		FROM 
			utl_config_parm 
		WHERE 
			utl_config_parm.parm_name = 'SPEC2000_ORDER_NUM_LIMIT'
	)
; 

--changeSet DEV-1161:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- migration script for PO_REVISION_NO column added in PO_HEADER table
BEGIN
utl_migr_schema_pkg.table_column_add('
Alter table PO_HEADER add (
	"PO_REVISION_NO" Number
)
');
END;
/

--changeSet DEV-1161:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
*
* Function:      isOrderAuthorized
* Arguments:     an_EventDbId    - event db id
*                an_EventId      - event id
* Description:   This function determines if the Order is authorized or not.
*		 Return 1 if it is authorized.  Otherwise, return 0
*
* Orig.Coder:    sdevi
* Recent Coder:  sdevi
* Recent Date:   July 28, 2011
*
*********************************************************************************/
CREATE OR REPLACE FUNCTION isOrderAuthorized
(
   an_EventDbId evt_event.event_db_id%TYPE,
   an_EventId   evt_event.event_id%TYPE
)  RETURN NUMBER
IS
   ln_EvtStageRows NUMBER;
   ls_EventTypeCd evt_event.event_type_cd%TYPE;
   ls_EventStatus evt_event.event_status_cd%TYPE;
   xc_EventTypeNotPO EXCEPTION;

BEGIN

   SELECT 
   	evt_event.event_type_cd,
   	evt_event.event_status_cd
   INTO 
   	ls_EventTypeCd,
   	ls_EventStatus
   FROM 
   	evt_event
   WHERE
	evt_event.event_db_id = an_EventDbId AND
	evt_event.event_id    = an_EventId;

   -- if event type is not PO then throw the exception
   IF ls_EventTypeCd <> 'PO' THEN
      RAISE xc_EventTypeNotPO;
   END IF;

   -- if event_status is OPEN, return 0
   IF ls_EventStatus = 'POOPEN' THEN
      RETURN 0;
   END IF;

   -- if event_status is not CANCEL, return 1
   IF ls_EventStatus <> 'POCANCEL' THEN
      RETURN 1;
   END IF;

   -- if event_status is CANCEL and if evt_stage rows with status other than CANCEL/OPEN exist then return 1
   IF ls_EventStatus = 'POCANCEL' THEN

       SELECT
           COUNT(*) INTO ln_EvtStageRows
       FROM
           evt_stage
       WHERE
          evt_stage.event_db_id = an_EventDbId AND
          evt_stage.event_id    = an_EventId   AND
          evt_stage.event_status_cd NOT IN ('POOPEN', 'POCANCEL');

       IF ln_EvtStageRows > 0 THEN
          RETURN 1;
       END IF;

   END IF;

   -- if you got this far, return 0 which means the order is not authorized
   RETURN 0;

END isOrderAuthorized;
/