package com.mxi.mx.core.services.shipment;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.ShipmentDomainBuilder;
import com.mxi.am.domain.builder.ShipmentLineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.services.inventory.MxMoveInventoryWarning;
import com.mxi.mx.core.services.inventory.MxMoveInventoryWarning.WarningType;


/**
 * This class test the ValidatePickedItem method in ShipmentService, verifies that correct
 * information, error or no warning is given according to the matching lines for the picked part
 *
 * @author DuHeLK
 *
 */
@RunWith( Parameterized.class )
public class ShipmentServiceValidatePickedItemTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private String iLinePartOem;
   private StockNoKey iLinePartStockNo;
   private String iMpKeySdesc;
   private String iPickedPartOem;
   private StockNoKey iPickedPartStockNo;
   private int iWarningCount;
   private WarningType iWarningType;
   private ShipmentKey iShipment;
   private static StockNoKey iStock = new StockNoKey( "1234:12" );

   private static String STOCK_PART_OEM = "STOCK_PART";
   private static String STOCK_ALT_PART_OEM = "STOCK_ALT_PART";
   private static String NO_STOCK_PART_OEM = "NO_STOCK_PART";
   private static String MP_KEY_SDESC = "MP0001";


   @Parameterized.Parameters
   public static Collection<Object[]> sTestParameters() {

      return Arrays.asList( new Object[][] {

            // GIVEN MPI generated line with a part WHEN the same part is picked, THEN no warning
            // given
            { STOCK_PART_OEM, null, MP_KEY_SDESC, STOCK_PART_OEM, null, 0, null },

            // GIVEN MPI generated line with a part WHEN a stock alternate part is picked THEN
            // information given
            { STOCK_PART_OEM, iStock, MP_KEY_SDESC, STOCK_ALT_PART_OEM, iStock, 1,
                  WarningType.STOCK_ALTERNATIVE_PICKED },

            // GIVEN MPI generated line with a part WHEN a different part is picked THEN error is
            // given
            { STOCK_PART_OEM, iStock, MP_KEY_SDESC, NO_STOCK_PART_OEM, null, 1, WarningType.ERROR },

            // GIVEN non-MPI line with a part WHEN the same part is picked THEN no warning given
            { STOCK_PART_OEM, iStock, null, STOCK_PART_OEM, iStock, 0, null },

            // GIVEN non-MPI line with a part WHEN a stock alternate part is picked THEN error is
            // given
            { STOCK_PART_OEM, iStock, null, STOCK_ALT_PART_OEM, iStock, 1, WarningType.ERROR },

            // GIVEN non-API line with a part WHEN a different part is picked THEN error is given
            { STOCK_PART_OEM, iStock, null, NO_STOCK_PART_OEM, null, 1, WarningType.ERROR } } );

   }


   public ShipmentServiceValidatePickedItemTest(String aLinePartOem, StockNoKey aLinePartStockNo,
         String aMpKeySdesc, String aPickedPartOem, StockNoKey aPickedPartStockNo,
         Integer aWarningCount, WarningType aWarningType) {

      iLinePartOem = aLinePartOem;
      iLinePartStockNo = aLinePartStockNo;
      iMpKeySdesc = aMpKeySdesc;
      iPickedPartOem = aPickedPartOem;
      iPickedPartStockNo = aPickedPartStockNo;
      iWarningCount = aWarningCount;
      iWarningType = aWarningType;

   }


   @Test
   public void testValidatePickedItem() {

      PartNoKey lLinePart = createPart( iLinePartOem, iLinePartStockNo );
      PartNoKey lPickedPart = lLinePart;

      if ( !iLinePartOem.equals( iPickedPartOem ) ) {
         lPickedPart = createPart( iPickedPartOem, iPickedPartStockNo );
      }

      createShipment( lLinePart, iMpKeySdesc );

      List<MxMoveInventoryWarning> lWarnings =
            ShipmentService.validatePickedItem( iShipment, lPickedPart, "S001" );

      assertEquals( "Expected Warning Count : ", iWarningCount, lWarnings.size() );

      if ( lWarnings.size() > 0 ) {
         assertEquals( "Expected Warning Type : ", iWarningType, lWarnings.get( 0 ).getType() );
      }
   }


   public void createShipment( PartNoKey aLinePartNo, String aMpKeySdesc ) {

      iShipment = new ShipmentDomainBuilder().build();

      new ShipmentLineBuilder( iShipment ).forPart( aLinePartNo ).withMpKeySdesc( aMpKeySdesc )
            .build();
   }


   public static PartNoKey createPart( String aPartOem, StockNoKey aStock ) {

      return new PartNoBuilder().withOemPartNo( aPartOem )
            .withInventoryClass( RefInvClassKey.BATCH ).withShortDescription( "Description" )
            .withStock( aStock ).build();
   }

}
