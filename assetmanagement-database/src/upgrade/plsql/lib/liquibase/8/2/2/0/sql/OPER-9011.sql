--liquibase formatted sql
--changeSet OPER-9011:1 stripComments:false

/**
* This script will migrate all of the tool check out notes
* from the evt_event table and put it in the evt_stage table
* so that the task barcode can be displayed in the check in/
* check out history tab of the tool details page.
**/
UPDATE 
    (
        SELECT
            evt_stage.user_stage_note AS tgt_user_stage_note,
            evt_event.event_ldesc AS src_event_ldesc
        FROM
            evt_stage
            INNER JOIN evt_event ON
                evt_stage.event_db_id   = evt_event.event_db_id AND
                evt_stage.event_id      = evt_event.event_id
        WHERE
            evt_stage.user_stage_note   IS NULL AND
            evt_stage.event_status_cd   = 'TCOOUT'
            AND
            evt_event.event_type_cd     = 'TCO' AND
            evt_event.event_ldesc       IS NOT NULL
    )
SET
    tgt_user_stage_note = src_event_ldesc;