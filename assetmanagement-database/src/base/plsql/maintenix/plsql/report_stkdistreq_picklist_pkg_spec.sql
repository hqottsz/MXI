CREATE OR REPLACE PACKAGE report_stkdistreq_picklist_pkg AS

   /************************************************************************
   ** Date:        October 23, 2018
   ** Author:      Libin Cai
   *************************************************************************
   **
   ** Confidential, proprietary and/or trade secret information of
   ** Mxi Technologies.
   **
   ** Copyright 2005-2018 Mxi Technologies. All Rights Reserved.
   **
   ** Except as expressly provided by written license signed by a duly appointed
   ** officer of Mxi Technologies. any disclosure, distribution,
   ** reproduction, compilation, modification, creation of derivative works and/or
   ** other use of the Mxi source code is strictly prohibited.  Inclusion of a
   ** copyright notice shall not be taken to indicate that the source code has
   ** been published.
   ***************************************************************************/

   -----------------------------------------------------------------------------------------------------------
   -- Description  : Get the stock dist request id ordered by bin location
   -- Return       : DistReqTable (Type Table) - collection of data
   -----------------------------------------------------------------------------------------------------------
   FUNCTION GetDistReq (
      aReqKeys IN VARCHAR2
   ) RETURN DistReqTable
   PIPELINED;  
   
   -----------------------------------------------------------------------------------------------------------
   -- Description  : Get the part list to pick
   -- Return       : PickListTable (Type Table) - collection of data
   -----------------------------------------------------------------------------------------------------------
   FUNCTION GetPickList (
      ain_stock_dist_req_id     IN stock_dist_req.stock_dist_req_id%TYPE,   
      ain_stock_dist_req_db_id  IN stock_dist_req.stock_dist_req_db_id%TYPE
   ) RETURN DistReqPickListTable
   PIPELINED;

END report_stkdistreq_picklist_pkg;
/
