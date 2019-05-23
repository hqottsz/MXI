--liquibase formatted sql


--changeSet DEV-1162:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- Data Migration Script
-- migrate evt_event data as per rules mentioned in section 3.3.1 EVT_EVENT of the design document
DECLARE
CURSOR lcur_orders
IS
SELECT evt_event.event_db_id,
       evt_event.event_id,
       po_header.auth_bool,
       evt_event.event_status_db_id,
       evt_event.event_status_cd
FROM
po_header
INNER JOIN evt_event ON evt_event.event_db_id = po_header.po_db_id AND
                        evt_event.event_id    = po_header.po_id
WHERE
evt_event.hist_bool = 0;

lrec_order lcur_orders%rowtype;
ln_StageId evt_stage.stage_id%TYPE;

BEGIN
  FOR lrec_order IN lcur_orders
    LOOP
      IF lrec_order.auth_bool = 0 AND lrec_order.event_status_cd <> 'POOPEN' THEN
           /* set the order status to POOPEN */
         UPDATE evt_event
         SET evt_event.event_status_db_id = 0, evt_event.event_status_cd = 'POOPEN'
         WHERE evt_event.event_db_id = lrec_order.event_db_id AND
               evt_event.event_id    = lrec_order.event_id;

         /* Get the next Stage ID */
         SELECT EVT_STAGE_ID_SEQ.nextval INTO ln_StageId FROM dual;

         /* Add an evt_stage row for the Order with the new status */
         INSERT INTO evt_stage (
           event_db_id,
           event_id,
           stage_id,
           event_status_db_id,
           event_status_cd,
           hr_db_id,
           hr_id,
           stage_reason_db_id,
           stage_reason_cd,
           stage_dt,
           stage_gdt,
           system_stage_note,
           system_bool )
        VALUES (
           lrec_order.event_db_id,
           lrec_order.event_id,
           ln_StageId,
           0,
           'POOPEN',
           0,
           3,
           NULL,
           NULL,
           SYSDATE,
           SYSDATE,
           'Status changed due to system migration',
           1
          );
      ELSE
         IF lrec_order.auth_bool = 1 AND lrec_order.event_status_cd = 'POOPEN' THEN
            /* set the order status to POAUTH */
             UPDATE evt_event
             SET evt_event.event_status_db_id = 0, evt_event.event_status_cd = 'POAUTH'
             WHERE evt_event.event_db_id = lrec_order.event_db_id AND
                   evt_event.event_id    = lrec_order.event_id;
             /* Get the next Stage ID */
             SELECT EVT_STAGE_ID_SEQ.nextval INTO ln_StageId FROM dual;

             /* Add an evt_stage row for the Order with the new status */
             INSERT INTO evt_stage (
               event_db_id,
               event_id,
               stage_id,
               event_status_db_id,
               event_status_cd,
               hr_db_id,
               hr_id,
               stage_reason_db_id,
               stage_reason_cd,
               stage_dt,
               stage_gdt,
               system_stage_note,
               system_bool )
            VALUES (
               lrec_order.event_db_id,
               lrec_order.event_id,
               ln_StageId,
               0,
               'POAUTH',
               0,
               3,
               NULL,
               NULL,
               SYSDATE,
               SYSDATE,
               'Status changed due to system migration',
               1
              );
          END IF;
     END IF;
  END LOOP;
END;
/

--changeSet DEV-1162:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- migrate po_header data as per section 3.3.2 PO_HEADER of the design document
-- set po_header.po_revision_no = 1 + # of POISSUED rows in evt_stage with matching event key
-- DEV1162: START OF SCRIPT
-- DEV1162: UPDATE PO_HEADER
BEGIN

   UTL_PARALLEL_PKG.PARALLEL_UPDATE_BEGIN('PO_HEADER');

   MERGE /*+ PARALLEL  */ INTO 
      po_header
   USING 
      (
       SELECT
          evt_stage.event_db_id, 
          evt_stage.event_id, 
          count(1) as poissued_count
       FROM
          evt_stage 
       WHERE
          evt_stage.event_status_cd = 'POISSUED'
       GROUP BY 
          evt_stage.event_db_id, 
          evt_stage.event_id
      ) evt_stage_summary
   ON
      (
       po_header.po_db_id = evt_stage_summary.event_db_id AND
       po_header.po_id = evt_stage_summary.event_id
      )
   WHEN MATCHED THEN 
      UPDATE SET 
         po_header.po_revision_no = evt_stage_summary.poissued_count + 1
      WHERE 
         po_header.po_revision_no IS NULL
   ;

   UTL_PARALLEL_PKG.PARALLEL_UPDATE_END('PO_HEADER', FALSE);

END;
/

--changeSet DEV-1162:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_HEADER DISABLE';
END;
/

--changeSet DEV-1162:4 stripComments:false
-- DEV1162: UPDATE PO_HEADER
UPDATE 
   po_header
SET
   po_revision_no = 1
WHERE
   po_revision_no IS NULL;

--changeSet DEV-1162:5 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
   EXECUTE IMMEDIATE 'ALTER TRIGGER TUBR_PO_HEADER ENABLE';
END;
/

--changeSet DEV-1162:6 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
BEGIN
utl_migr_schema_pkg.table_column_modify('
Alter table PO_HEADER modify (
	"PO_REVISION_NO" Number Default 1 NOT NULL DEFERRABLE
)
');
END;
/                    