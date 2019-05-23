--liquibase formatted sql


--changeSet OPER-8045:1 stripComments:false
-- add report type and menu item
INSERT INTO 
   utl_report_type
   (REPORT_NAME, REPORT_ENGINE_TYPE, REPORT_PATH, REPORT_DESC, ACTIVE_BOOL, SYSTEM_BOOL, RSTAT_CD, REVISION_NO, CTRL_DB_ID, CREATION_DT, CREATION_DB_ID, REVISION_DT, REVISION_DB_ID, REVISION_USER)
SELECT
   'po.RaisedOrdersByAccount', 'JASPER_SSO', '/organizations/Maintenix/Reports/Core/po/RaisedOrdersByAccount', 'Maps a report template name to a full path for the report to launch on the Jasper server.  This enables remapping of report calls to customized reports', 1, 1, 0, 1, 0, sysdate, 0, sysdate, 0, 'MXI'
FROM
   DUAL
WHERE NOT EXISTS
   (
      SELECT
         1
      FROM
         utl_report_type
      WHERE
         report_name = 'po.RaisedOrdersByAccount'
    )
;

--changeSet OPER-8045:2 stripComments:false
INSERT INTO 
   utl_menu_item
   (MENU_ID, TODO_LIST_ID, MENU_NAME, MENU_LINK_URL, NEW_WINDOW_BOOL, UTL_ID)
SELECT
   120945, NULL,'web.menuitem.RAISED_ORDERS_BY_ACCOUNT', '/maintenix/servlet/report/generate?aTemplate=po.RaisedOrdersByAccount&aViewPDF=true', 0, 0
FROM
   DUAL
WHERE NOT EXISTS
   (
      SELECT
         1
      FROM
         utl_menu_item
      WHERE
         menu_name = 'web.menuitem.RAISED_ORDERS_BY_ACCOUNT'
   )
;

--changeSet OPER-8045:3 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- plsql reportraisedorders specification
CREATE OR REPLACE PACKAGE reportraisedorders AS
/************************************************************************
** Date:        March 28, 2016
** Author:      Karan Tandon
** Description: raised orders by account
*************************************************************************
**
** Confidential, proprietary and/or trade secret information of
** Mxi Technologies.
**
** Copyright 2005-2016 Mxi Technologies. All Rights Reserved.
**
** Except as expressly provided by written license signed by a duly appointed
** officer of Mxi Technologies. any disclosure, distribution,
** reproduction, compilation, modification, creation of derivative works and/or
** other use of the Mxi source code is strictly prohibited.  Inclusion of a
** copyright notice shall not be taken to indicate that the source code has
** been published.
**
***************************************************************************/
  -----------------------------------------------------------------------------
   -- Function:    GetRaisedOrders
   -- Arguments:   aidt_from_date,aidt_to_date,ait_account_code
   -- Description: Bring raised orders by account results
  -----------------------------------------------------------------------------

  TYPE RaisedOrderTRec IS RECORD
   (
    po_type_cd     VARCHAR2 (8) ,
    po_number      VARCHAR2 (500) ,
    user_status_cd VARCHAR2 (16) ,
    order_qt FLOAT ,
    creation_dt     DATE ,
    vendor_cd       VARCHAR2 (16) ,
    vendor_name     VARCHAR2 (40) ,
    part_no_oem     VARCHAR2 (40) ,
    part_no_sdesc   VARCHAR2 (80) ,
    line_ldesc      VARCHAR2 (4000) ,
    serial_no_oem   VARCHAR2 (40) ,
    account_cd      VARCHAR2 (80) ,
    account_type_cd VARCHAR2 (8) ,
    line_price      NUMBER (15,5) ,
    currency_cd     VARCHAR2 (8) ,
    invoice_number  VARCHAR2 (500)
    );

  TYPE RaisedOrderTTRec IS TABLE OF RaisedOrderTRec
  ;

  FUNCTION GetRaisedOrders(
      aidt_from_date        IN DATE,
      aidt_to_date          IN DATE,
      aiv_account_code      IN VARCHAR2
   ) RETURN RaisedOrderTTRec PIPELINED;

END reportraisedorders;
/

--changeSet OPER-8045:4 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- plsql reportraisedorders body
CREATE OR REPLACE PACKAGE BODY reportraisedorders AS
/************************************************************************
** Date:        March 28, 2016
** Author:      Karan Tandon
** Description: raised orders by account
*************************************************************************
**
** Confidential, proprietary and/or trade secret information of
** Mxi Technologies.
**
** Copyright 2005-2016 Mxi Technologies. All Rights Reserved.
**
** Except as expressly provided by written license signed by a duly appointed
** officer of Mxi Technologies. any disclosure, distribution,
** reproduction, compilation, modification, creation of derivative works and/or
** other use of the Mxi source code is strictly prohibited.  Inclusion of a
** copyright notice shall not be taken to indicate that the source code has
** been published.
**
***************************************************************************/

   -----------------------------------------------------------------------------------------------------------
   -- local package constants
   -----------------------------------------------------------------------------------------------------------
   gk_row_limit    CONSTANT PLS_INTEGER := 100;

   -----------------------------------------------------------------------------
   -- Description: Bring raised orders by account results
   -----------------------------------------------------------------------------
   FUNCTION GetRaisedOrders(
      aidt_from_date        IN DATE,
      aidt_to_date          IN DATE,
      aiv_account_code      IN VARCHAR2
   ) RETURN RaisedOrderTTRec
     PIPELINED
     IS

      CURSOR lcur_rsd_ord IS
      SELECT
            po_header.po_type_cd,
            po_event.event_sdesc AS po_number,
            po_status.user_status_cd,
            po_line.order_qt,
            po_event.actual_start_dt,
            org_vendor.vendor_cd,
            org_vendor.vendor_name,
            eqp_part_no.part_no_oem,
            eqp_part_no.part_no_sdesc,
            po_line.line_ldesc,
            inv_inv.serial_no_oem,
            fnc_account.account_cd,
            fnc_account.account_type_cd,
            po_line.line_price,
            po_header.currency_cd,
            -- show the list of invoice numbers comma separated
            LISTAGG(invoice_event.event_sdesc, ', ') WITHIN GROUP (ORDER BY invoice_event.event_sdesc) AS invoice_number
      FROM
         po_header
         -- join to determine po line data
         INNER JOIN po_line ON
            po_header.po_db_id = po_line.po_db_id AND
            po_header.po_id    = po_line.po_id
         -- join determine invoice info
         LEFT JOIN po_invoice_line_map ON
            po_invoice_line_map.po_db_id = po_line.po_db_id AND
            po_invoice_line_map.po_id = po_line.po_id AND
            po_invoice_line_map.po_line_id = po_line.po_line_id
         -- join directly to evt_event as short path, since
         -- there is no usage for po_invoice_line and po_invoice in the
         -- select statement. This will also improve performance.
         LEFT JOIN evt_event invoice_event ON
            invoice_event.event_db_id = po_invoice_line_map.po_invoice_db_id AND
            invoice_event.event_id = po_invoice_line_map.po_invoice_id AND
            -- do not show invoice info when its status is CANCEL
            NOT
            (
                invoice_event.event_status_db_id = 0 AND
                invoice_event.event_status_cd = 'PICANCEL'
            )
         -- join to determine vendor code
         INNER JOIN org_vendor ON
            po_header.vendor_db_id = org_vendor.vendor_db_id AND
            po_header.vendor_id    = org_vendor.vendor_id
         -- join to determine account code
         INNER JOIN fnc_account ON
            fnc_account.account_db_id = po_line.account_db_id AND
            fnc_account.account_id    = po_line.account_id
         -- join to determine po number
         INNER JOIN evt_event po_event ON
            po_header.po_db_id = po_event.event_db_id AND
            po_header.po_id    = po_event.event_id
         -- join to determine the order status
         INNER JOIN ref_event_status po_status ON
            po_status.event_status_db_id = po_event.event_status_db_id AND
            po_status.event_status_cd    = po_event.event_status_cd
         -- join to determine first outbound shipment
         LEFT JOIN
            (
                -- get send repair inventory
                SELECT
                   po_line.po_db_id,
                   po_line.po_id,
                   po_line.po_line_id,
                   wp_stask.main_inv_no_db_id AS inv_no_db_id,
                   wp_stask.main_inv_no_id    AS inv_no_id
                FROM
                   po_line
                   INNER JOIN sched_stask wp_stask ON
                      po_line.sched_db_id = wp_stask.sched_db_id AND
                      po_line.sched_id    = wp_stask.sched_id
                WHERE
                   po_line.po_line_type_db_id = 0 AND
                   po_line.po_line_type_cd    = 'REPAIR'

                UNION ALL
                 -- get send exchange inventory
                SELECT
                   po_line.po_db_id,
                   po_line.po_id,
                   po_line.po_line_id,
                   ship_shipment_line.inv_no_db_id,
                   ship_shipment_line.inv_no_id
                FROM
                   po_line
                   INNER JOIN ship_shipment ON
                      po_line.po_db_id = ship_shipment.po_db_id AND
                      po_line.po_id    = ship_shipment.po_id
                   INNER JOIN ship_shipment_line ON
                      ship_shipment.shipment_db_id = ship_shipment_line.shipment_db_id AND
                      ship_shipment.shipment_id    = ship_shipment_line.shipment_id
                WHERE
                   po_line.po_line_type_db_id = 0 AND
                   po_line.po_line_type_cd    = 'EXCHANGE'
                   AND
                   ship_shipment.shipment_type_db_id = 0 AND
                   ship_shipment.shipment_type_cd    = 'SENDXCHG'
            ) po_inv ON
            po_inv.po_db_id   = po_line.po_db_id AND
            po_inv.po_id      = po_line.po_id AND
            po_inv.po_line_id = po_line.po_line_id
         LEFT JOIN inv_inv ON
            inv_inv.inv_no_db_id = po_inv.inv_no_db_id AND
            inv_inv.inv_no_id    = po_inv.inv_no_id
         -- join to determine part number
         LEFT JOIN eqp_part_no ON
            eqp_part_no.part_no_db_id = po_line.part_no_db_id AND
            eqp_part_no.part_no_id    = po_line.part_no_id
      WHERE
          po_event.actual_start_dt BETWEEN aidt_from_date AND aidt_to_date
          AND
          DECODE(aiv_account_code, '*ALL', '1', fnc_account.account_cd) = DECODE(aiv_account_code, '*ALL', '1', aiv_account_code)
          AND
          po_line.deleted_bool = 0
      -- group by it is necessary for the LISTAGG function.
      GROUP BY
         po_header.po_type_cd,
         po_event.event_sdesc,
         po_status.user_status_cd,
         po_line.order_qt,
         po_event.actual_start_dt,
         org_vendor.vendor_cd,
         org_vendor.vendor_name,
         eqp_part_no.part_no_oem,
         eqp_part_no.part_no_sdesc,
         po_line.line_ldesc,
         inv_inv.serial_no_oem,
         fnc_account.account_cd,
         fnc_account.account_type_cd,
         po_line.line_price,
         po_header.currency_cd
      ORDER BY
         po_event.actual_start_dt,
         po_event.event_sdesc;

      lrec_rsd_ord   RaisedOrderTTRec;

   BEGIN

      OPEN lcur_rsd_ord;

      LOOP

         FETCH lcur_rsd_ord
         BULK COLLECT INTO lrec_rsd_ord
         LIMIT gk_row_limit;

         EXIT WHEN lrec_rsd_ord.COUNT = 0;

         FOR i IN 1..lrec_rsd_ord.COUNT LOOP

            PIPE ROW (lrec_rsd_ord(i));

         END LOOP;

       END LOOP;

     CLOSE lcur_rsd_ord;

     RETURN;

     EXCEPTION
        WHEN OTHERS THEN
           RAISE;

   END GetRaisedOrders;

END reportraisedorders;
/