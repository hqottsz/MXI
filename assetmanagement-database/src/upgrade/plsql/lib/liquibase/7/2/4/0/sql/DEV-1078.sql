--liquibase formatted sql
 

--changeSet DEV-1078:1 stripComments:false
-- Migration script for version 8.x Qantas CR 1109
-- Author: Dungtien 
-- New table REF_CHANGE_REASON  with seven 0-level values
INSERT INTO ref_change_reason 
        ( 
            CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER
        )
        SELECT 'MXRTINE', 'Routine', 'Routine Action', 'Routine Action', 1, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
        FROM 
            DUAL 
        WHERE 
            NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD = 'MXRTINE');

--changeSet DEV-1078:2 stripComments:false
INSERT INTO 
        ref_change_reason 
        ( 
           CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER
        )
        SELECT 'MXACCEPT', 'Accept', 'Acceptance of Quotation', 'Acceptance of Quotation', 2, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
        FROM 
            DUAL 
        WHERE 
            NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD = 'MXACCEPT');

--changeSet DEV-1078:3 stripComments:false
INSERT INTO 
        ref_change_reason 
        ( 
           CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER
        )
        SELECT 'MXALTER', 'Alter', 'Alteration', 'Alteration', 3, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
        FROM 
            DUAL 
        WHERE 
            NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD = 'MXALTER');	    

--changeSet DEV-1078:4 stripComments:false
INSERT INTO 
        ref_change_reason 
        ( 
           CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER
        )
        SELECT 'MXCNCL', 'Cancel', 'Cancel', 'Cancel/Decrease Quantity', 6, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
        FROM 
            DUAL 
        WHERE 
            NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD = 'MXCNCL');	    

--changeSet DEV-1078:5 stripComments:false
INSERT INTO 
        ref_change_reason 
        ( 
           CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER
        )
        SELECT 'MXDCRSE', 'Decrease', 'Decrease Quantity', 'Cancel/Decrease Quantity', 7, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
        FROM 
            DUAL 
        WHERE 
            NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD = 'MXDCRSE');	    

--changeSet DEV-1078:6 stripComments:false
INSERT INTO 
        ref_change_reason 
        ( 
           CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER
        )
        SELECT 'MXINCRSE', 'Increase', 'Increase Quantity', 'Increase Quantity', 8, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
        FROM 
            DUAL 
        WHERE 
            NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD = 'MXINCRSE');	    

--changeSet DEV-1078:7 stripComments:false
INSERT INTO 
        ref_change_reason 
        ( 
           CHANGE_REASON_CD, DISPLAY_CODE, DISPLAY_NAME, DISPLAY_DESC, DISPLAY_ORD, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER
        )
        SELECT 'MXPRTL', 'Partial', 'Request partial ship quantity', 'Request partial Ship Quantity of an order', 9, 0, 1, 0, SYSDATE, 0, SYSDATE, 0, 'MXI'
        FROM 
            DUAL 
        WHERE 
            NOT EXISTS (SELECT 1 FROM ref_change_reason WHERE CHANGE_REASON_CD = 'MXPRTL');	     	   

--changeSet DEV-1078:8 stripComments:false
-- - New 0-level data for REF_STATGE_REASON table
INSERT INTO 
	ref_stage_reason 
	(
	   STAGE_REASON_DB_ID, 
	   STAGE_REASON_CD,  
	   EVENT_STATUS_DB_ID, 
	   EVENT_STATUS_CD, 
	   BITMAP_DB_ID, BITMAP_TAG,   
	   DESC_SDESC, DESC_LDESC ,
	   USER_REASON_CD, 
	   RSTAT_CD,CREATION_DT, 
	   REVISION_DT, 
	   REVISION_DB_ID, 
	   REVISION_USER 
	 )
	 SELECT 0, 
	 	'POALTER',
	 	NULL, 
	 	NULL,
	 	0, 
	 	80,
	 	'Order modified', 
	 	'The order has been modified from its original state' , 
	 	'ALTER', 
	 	0,
	 	TO_DATE('2011-06-29', 'YYYY-MM-DD'), 
	 	TO_DATE('2011-06-29', 'YYYY-MM-DD'), 
	 	100, 
	 	'MXI'
	 FROM
	     DUAL
	 WHERE
	     NOT EXISTS ( SELECT 1 FROM ref_stage_reason WHERE STAGE_REASON_CD = 'POALTER') ;

--changeSet DEV-1078:9 stripComments:false
-- - New 0-level data for REF_SPEC2K_CMND table
INSERT INTO
	ref_spec2k_cmnd
	(
	   SPEC2K_CMND_DB_ID, 
	   SPEC2K_CMND_CD, 
	   DESC_SDESC, 
	   DESC_LDESC, 
	   RSTAT_CD, 
	   CREATION_DT, 
	   REVISION_DT, 
	   REVISION_DB_ID, 
	   REVISION_USER
	)
	SELECT 
		0, 
		'S1INVCE', 
		'Invoice Information', 
		'Supplier has the capability to send Invoices to the Purchasing Agent for previously entered orders', 
		0, 
		TO_DATE('2011-06-29', 'YYYY-MM-DD'), 
		TO_DATE('2011-06-29', 'YYYY-MM-DD'), 
		0, 
		'MXI'
	FROM
	    DUAL
	WHERE
	    NOT EXISTS ( SELECT 1 FROM ref_spec2k_cmnd WHERE SPEC2K_CMND_CD = 'S1INVCE') ;	     