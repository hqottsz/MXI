--liquibase formatted sql


--changeSet MX-24366:1 stripComments:false
/****************************************************************************************************************
 *
 * Replace and Update ref_event_status.desc_sdesc , ref_event_status.desc_ldesc column  where event_status_cd must 
 * be 'POOPEN', 'POISSUED', 'PORECEIVED', 'POCLOSED', 'POCANCEL', 'POPARTIAL' or 'POACKNOWLEDGED' 
 *
 ****************************************************************************************************************/
UPDATE ref_event_status SET 
	desc_sdesc = REPLACE (desc_sdesc, 'PO', 'order'),
        desc_ldesc = REPLACE (desc_ldesc, 'PO', 'order')
WHERE 
	event_status_cd IN ('POOPEN', 'POISSUED', 'PORECEIVED', 'POCLOSED', 'POCANCEL', 'POPARTIAL', 'POACKNOWLEDGED') 
;