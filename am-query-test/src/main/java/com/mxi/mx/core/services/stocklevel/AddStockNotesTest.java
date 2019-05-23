
package com.mxi.mx.core.services.stocklevel;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.stock.details.StockService;
import com.mxi.mx.core.table.eqp.EqpStockNoTable;


/**
 * This class tests the {@link StockService.editNote} method
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AddStockNotesTest {

   private StockNoKey iStockNo;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   public static final String STOCK_NOTE = "TESTING STOCK NOTES";

   public static final String STOCK_NOTE_WITH_SPECIAl_CHAR =
         "Testing special characters: &,/% $ @# in notes";


   /**
    * Tests that Stock notes should be added.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAddStockNotes() throws Exception {
      // add notes to stock
      StockService.editNote( iStockNo, STOCK_NOTE );

      // verify the notes were added
      EqpStockNoTable lStockNo = EqpStockNoTable.findByPrimaryKey( iStockNo );
      assertEquals( STOCK_NOTE, lStockNo.getNote() );

   }


   /**
    * Tests that Stock notes should be added.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAddStockNotesWithSpecialCharacters() throws Exception {
      // add notes to stock
      StockService.editNote( iStockNo, STOCK_NOTE_WITH_SPECIAl_CHAR );

      // verify the notes were added
      EqpStockNoTable lStockNo = EqpStockNoTable.findByPrimaryKey( iStockNo );
      assertEquals( STOCK_NOTE_WITH_SPECIAl_CHAR, lStockNo.getNote() );

   }


   @Before
   public void loadData() throws Exception {
      // set up the stock
      iStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).withStockCode( "STK0111" )
            .build();
   }

}
