
package com.mxi.mx.core.query.part;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.EqpPartVendorPriceKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.unittest.table.eqp.EqpPartVendorPrice;


/**
 * This class tests the SetVendorPartPricesToHistoricTest query.
 *
 * @author nsubotic
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class SetVendorPartPricesToHistoricTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(),
            SetVendorPartPricesToHistoricTest.class );
   }


   /** A vendor part price that is not historic */
   private static final EqpPartVendorPriceKey PRICE_FOR_UPDATE_1 =
         new EqpPartVendorPriceKey( "4650:1001" );

   /** A vendor part price that is not historic */
   private static final EqpPartVendorPriceKey PRICE_FOR_UPDATE_2 =
         new EqpPartVendorPriceKey( "4650:1002" );

   /** A vendor part price that is historic */
   private static final EqpPartVendorPriceKey PRICE_HISTORIC =
         new EqpPartVendorPriceKey( "4650:1003" );

   /** A part that has vendor part prices created */
   private static final PartNoKey PART_NO_KEY = new PartNoKey( "4650:2001" );

   /** A vendor that has vendor part prices created */
   private static final VendorKey VENDOR_KEY = new VendorKey( "4650:3001" );


   /**
    * Tests updating vendor part prices if their effective dates are before current date. If the
    * prices are already historic they are skipped.
    */
   @Test
   public void testUpdateWhereEffToDate() {

      // Need to set here the part price to not historic. If another test runs first it will set the
      // price historic, and this test will fail.
      EqpPartVendorPrice lEqpPartVendorPrice = new EqpPartVendorPrice( PRICE_FOR_UPDATE_2 );
      lEqpPartVendorPrice.setHistoric( false );
      lEqpPartVendorPrice.update();

      // only one part price is updated, the one that was not historic
      assertEquals( "Rows updated", 1, executeUpdateWhereEffToDate() );

      lEqpPartVendorPrice.refresh();
      lEqpPartVendorPrice.assertHistBool( true );
   }


   /**
    * Tests updating a vendor part price. If the price is already historic, it will be skipped.
    *
    * @throws Exception
    *            If an error occurs
    */
   @Test
   public void testUpdateWherePrice() throws Exception {

      // assert that vendor part price is updated/hist_bool set to true
      assertEquals( "Rows updated", 1, executeUpdateWherePrice( PRICE_FOR_UPDATE_1 ) );

      new EqpPartVendorPrice( PRICE_FOR_UPDATE_1 ).assertHistBool( true );

      // assert that vendor part price is not updated since it was already historic
      assertEquals( "Rows updated", 0, executeUpdateWherePrice( PRICE_HISTORIC ) );

      new EqpPartVendorPrice( PRICE_HISTORIC ).assertHistBool( true );
   }


   /**
    * Tests updating vendor part prices for a vendor and a part number if their effective dates are
    * before current date. If the prices are already historic they are skipped.
    */
   @Test
   public void testUpdateWhereVendorPartEffToDate() {

      // Need to set here the part price to not historic. If another test runs first it will set the
      // price historic, and this test will fail.
      EqpPartVendorPrice lEqpPartVendorPrice = new EqpPartVendorPrice( PRICE_FOR_UPDATE_2 );
      lEqpPartVendorPrice.setHistoric( false );
      lEqpPartVendorPrice.update();

      // only one vendor part price is updated, the historic price for the same vendor and part
      // number was skipped
      assertEquals( "Rows updated", 1,
            executeUpdateWhereVendorPartEffToDate( PART_NO_KEY, VENDOR_KEY ) );

      new EqpPartVendorPrice( PRICE_FOR_UPDATE_2 ).assertHistBool( true );
   }


   /**
    * Updates vendor part prices/sets hist_bool to true, if their effective to date is before
    * current date. If prices are already historic, they are skipped.
    *
    * @return number of rows updated
    */
   private int executeUpdateWhereEffToDate() {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhere( "eqp_part_vendor_price.effective_to_dt is not null and "
            + "eqp_part_vendor_price.effective_to_dt < sysdate" );

      return QueryExecutor.executeUpdate( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Updates a vendor part price/sets hist_bool to true. If the price is already historic, it is
    * skipped.
    *
    * @param aEqpPartVendorPriceKey
    *           Part vendor price key
    *
    * @return number of rows updated
    */
   private int executeUpdateWherePrice( EqpPartVendorPriceKey aEqpPartVendorPriceKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereEquals( new String[] { "eqp_part_vendor_price.part_vendor_price_db_id",
            "eqp_part_vendor_price.part_vendor_price_id" }, aEqpPartVendorPriceKey );

      return QueryExecutor.executeUpdate( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }


   /**
    * Updates vendor part prices/sets hist_bool to true, for a vendor and a part number if their
    * effective to dates are before current date. If prices are already historic they are skipped.
    *
    * @param aPartNoKey
    *           Part number key.
    * @param aVendorKey
    *           Vendor key.
    *
    * @return number of rows updated
    */
   private int executeUpdateWhereVendorPartEffToDate( PartNoKey aPartNoKey, VendorKey aVendorKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.addWhereEquals( new String[] { "eqp_part_vendor_price.part_no_db_id",
            "eqp_part_vendor_price.part_no_id" }, aPartNoKey );

      lArgs.addWhereEquals( new String[] { "eqp_part_vendor_price.vendor_db_id",
            "eqp_part_vendor_price.vendor_id" }, aVendorKey );
      lArgs.addWhere( "eqp_part_vendor_price.effective_to_dt is not null and "
            + "eqp_part_vendor_price.effective_to_dt < sysdate" );

      return QueryExecutor.executeUpdate( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
