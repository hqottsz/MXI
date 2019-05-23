
package com.mxi.mx.core.unittest.po;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.PurchaseOrderAuthKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefPoAuthLvlKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.table.po.PoHeaderTable;


/**
 * The helper class for PO unit tests.
 *
 * @author Libin Cai
 * @created January 25, 2012
 */
public class PoTestHelper {

   /**
    * Inserts budget authorization record.
    *
    * @param aPurchaseOrder
    *           The purchase order key
    * @param aPoAuthLevel
    *           The PO authorization level
    * @param aPoAuthId
    *           The PO authorization id
    * @param aBudgetAuthStatus
    *           The budget authorization status key
    */
   public static void insertAuth( PurchaseOrderKey aPurchaseOrder, RefPoAuthLvlKey aPoAuthLevel,
         int aPoAuthId, RefPoAuthLvlStatusKey aBudgetAuthStatus ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPurchaseOrder, "po_db_id", "po_id" );
      lArgs.add( aPoAuthLevel, "po_auth_lvl_db_id", "po_auth_lvl_cd" );
      lArgs.add( "po_auth_id", aPoAuthId );
      lArgs.add( aBudgetAuthStatus, "auth_lvl_status_db_id", "auth_lvl_status_cd" );

      MxDataAccess.getInstance().executeInsert( PurchaseOrderAuthKey.TABLE_NAME, lArgs );
   }


   /**
    * Insert budget authorization level.
    *
    * @param aPurchaseOrder
    *           The purchase order key
    * @param aBudgetAuthStatus
    *           The budget authorization status
    */
   public static void insertBudgetAuth( PurchaseOrderKey aPurchaseOrder,
         RefPoAuthLvlStatusKey aBudgetAuthStatus ) {

      // set po budget status as aBudgetAuthStatus
      PoHeaderTable lPoHeader = PoHeaderTable.findByPrimaryKey( aPurchaseOrder );
      lPoHeader.setBudgetCheckStatus( aBudgetAuthStatus );
      lPoHeader.update();

      insertAuth( aPurchaseOrder, RefPoAuthLvlKey.BUDGET, 1, aBudgetAuthStatus );
   }


   /**
    * Inserts a record into evt_event table.
    *
    * @param aEvent
    *           The event key
    * @param aEventStatus
    *           The event status
    */
   public static void insertEvtEvent( EventKey aEvent, RefEventStatusKey aEventStatus ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aEvent, "event_db_id", "event_id" );
      lArgs.add( aEventStatus, "event_status_db_Id", "event_status_cd" );

      MxDataAccess.getInstance().executeInsert( "evt_event", lArgs );
   }


   /**
    * Inserts into po_headert table.
    *
    * @param aPurchaseOrder
    *           The purchase order key
    * @param aBudgetAuthStatus
    *           The budget authorization status
    */
   public static void insertPoHeader( PurchaseOrderKey aPurchaseOrder,
         RefPoAuthLvlStatusKey aBudgetAuthStatus ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPurchaseOrder, "po_db_id", "po_id" );
      lArgs.add( aBudgetAuthStatus, "budget_check_status_db_id", "budget_check_status_cd" );

      MxDataAccess.getInstance().executeInsert( PurchaseOrderKey.TABLE_NAME, lArgs );
   }
}
