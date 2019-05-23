--liquibase formatted sql


--changeSet report_raised_orders_spec:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
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