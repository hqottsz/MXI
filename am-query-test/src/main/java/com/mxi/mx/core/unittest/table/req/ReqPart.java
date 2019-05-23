
package com.mxi.mx.core.unittest.table.req;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.table.req.ReqPartTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>req_part</code> table.
 */
public class ReqPart extends ReqPartTable {

   /**
    * Initializes the class.
    *
    * <ol>
    * <li>Create a new {@link DataSetArgument DataSetArgument} to hold the lookup parameters for a
    * query.</li>
    * <li>Add the <code>aPartNo</code> key to the lookup parameters by calling
    * {@link DataSetArgument#add(String, int ) addInt()}.</li>
    * <li>Call {@link MxTestDataAccess#executeQuery(String[], String, DataSetArgument )
    * executeQuery()} to retrieve the record, and store it in the <code>iActual</code> instance
    * variable.</li>
    * </ol>
    *
    * <p>
    *
    * @param aPartRequest
    *           primary key of the table.
    */
   public ReqPart(PartRequestKey aPartRequest) {

      super( aPartRequest );
   }


   /**
    * Assert the value of the <inv_no_db_id:inv_no_id > columns.
    *
    * @param aInventory
    *           Type Code.
    */
   public void assertInventoryKey( InventoryKey aInventory ) {

      MxAssert.assertEquals( "inv_no_pk", aInventory, getInventory() );
   }


   /**
    * Assert the value of the <req_qt > columns.
    *
    * @param aNeededQty
    *           requested quantity.
    */
   public void assertReqQt( Double aNeededQty ) {
      Double lNeededQty = getDoubleObj( "req_qt" );

      // Check if the Actual value matches the expected value
      MxAssert.assertEquals( "req_qt", aNeededQty, lNeededQty );
   }


   /**
    * Assert the value of the <req_stock_no_db_id:req_stock_no_id > columns.
    *
    * @param aStockNo
    *           the stock no
    */
   public void assertReqStockNo( StockNoKey aStockNo ) {

      MxAssert.assertEquals( "req_stock_no_db_id:req_stock_no_id", aStockNo,
            isNull( "req_stock_no_db_id" ) ? null
                  : new StockNoKey( getInt( "req_stock_no_db_id" ), getInt( "req_stock_no_id" ) ) );
   }


   /**
    * Assert the value of the <req_type_cd > columns.
    *
    * @param aType
    *           Type Code.
    */
   public void assertReqType( RefReqTypeKey aType ) {

      // Retrieve the actual currency
      RefReqTypeKey lActual =
            new RefReqTypeKey( getInt( "req_type_db_id" ), getString( "req_type_cd" ) );

      // Assert that the actual and expected match
      MxAssert.assertEquals( "req_type_db_id:req_type_cd", aType, lActual );
   }

}
