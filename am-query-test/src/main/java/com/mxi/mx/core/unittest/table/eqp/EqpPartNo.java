
package com.mxi.mx.core.unittest.table.eqp;

import java.math.BigDecimal;

import org.junit.Assert;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.MxCoreUtils;
import com.mxi.mx.core.table.eqp.EqpPartNoTable;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class is used to check values in the <code>eqp_part_no</code> table.
 *
 * @author Chris Brouse
 * @created April 7, 2002
 */
public class EqpPartNo extends EqpPartNoTable {

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
    * @param aPartNo
    *           primary key of the table.
    */
   public EqpPartNo(PartNoKey aPartNo) {
      super( aPartNo );
   }


   /**
    * Asserts that the given expected value and the actual value match.
    *
    * @param aExpected
    *           expected value.
    */
   public void assertInventoryClass( String aExpected ) {

      // Check if the actual value matches the argument
      this.assertStringField( ColumnName.INV_CLASS_CD, aExpected );
   }


   public void assertStockNumber( StockNoKey aExpected ) {
      Assert.assertEquals( aExpected, getStockNumber() );
   }


   public void assertStockPct( Double aExpected ) {
      MxAssert.assertEquals( "stock_pct", aExpected, getStockPct() );
   }


   /**
    * Asserts that the given expected value and the actual value match.
    *
    * @param aExpected
    *           expected value.
    */
   public void assertTotalQt( BigDecimal aExpected ) {

      // Check if the actual matches the expected
      MxAssert.assertEquals( "total_qt", aExpected, getTotalQt() );
   }


   /**
    * Asserts that the given expected value and the actual value match.
    *
    * @param aExpected
    *           expected value.
    */
   public void assertTotalValue( BigDecimal aExpected ) {

      // Check if the actual matches the expected
      MxAssert.assertEquals( "total_value", aExpected, getTotalValue() );
   }


   /**
    * Asserts that the value in the column specified by <code>aColumnName</code> and the argument
    * value are equal.
    *
    * @param aColumnName
    *           name of the string field.
    * @param aColumnValue
    *           expected value of the string field.
    */
   private void assertStringField( ColumnName aColumnName, String aColumnValue ) {

      // Retrieve the actual value
      String lColumnValue = getString( aColumnName );

      // Check if the argument is blank we must change it to null (the database treats this as
      // null)
      if ( MxCoreUtils.isNullOrBlank( aColumnValue ) ) {
         aColumnValue = null;
      }

      // Check if the actual value matches the argument
      MxAssert.assertEquals( aColumnName.name(), aColumnValue, lColumnValue );
   }
}
