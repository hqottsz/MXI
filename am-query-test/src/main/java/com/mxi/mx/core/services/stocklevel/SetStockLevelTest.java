
package com.mxi.mx.core.services.stocklevel;

import static com.mxi.mx.core.key.RefStockLowActionKey.DISTREQ;
import static com.mxi.mx.core.key.RefStockLowActionKey.MANUAL;
import static com.mxi.mx.core.key.RefStockLowActionKey.POREQ;
import static com.mxi.mx.core.key.RefStockLowActionKey.SHIPREQ;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.AccountBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.StockBuilder;
import com.mxi.am.domain.builder.StockLevelBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.exception.NegativeValueException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.exception.InvalidReftermException;
import com.mxi.mx.core.exception.MaxLowerThanMinException;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.InvLocStockKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.key.RefFinanceTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.StockNoKey;
import com.mxi.mx.core.table.inv.InvLocStockTable;
import com.mxi.mx.core.table.inv.InvLocTable;


/**
 * This class tests the {@link StockLevelService.set} method
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class SetStockLevelTest {

   private LocationKey iLocationWithSupplier;
   private LocationKey iLocationWithNoSupplier;
   private LocationKey iSupplierLocation;

   private LocationKey iWarehouseWithSupplier;
   private LocationKey iWarehouseWithoutSupplier;
   private LocationKey iLocalSupplierLocation;

   private StockNoKey iStockNo;
   private OwnerKey iOwner;
   private FncAccountKey iAccount;

   private StockLevelService iStockLevelService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Tests the case where stock low action is set to MANUAL and all arguments are valid. Stock
    * level details should be properly updated.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testManualStockLevel() throws Exception {

      InvLocStockKey lStockLevelKey = createStockLevel();

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setLowAction( MANUAL.getCd() );
      lStockLevelTO.setReorderLevelQt( 9.0, "aReorderLevel" );
      lStockLevelTO.setMaxQt( 50.0, "aMaxQuantity" );
      lStockLevelTO.setMinReorderQt( 5.0, "aMinReorderLevel" );
      lStockLevelTO.setWeightFactorQt( 1.0, "aWeightFactor" );
      lStockLevelTO.iLocation.setInboundFlightsQt( 100.0, "aInboundFlights" );

      iStockLevelService.set( lStockLevelKey, lStockLevelTO );

      InvLocStockTable lStockLevel = InvLocStockTable.findByPrimaryKey( lStockLevelKey );
      assertEquals( MANUAL, lStockLevel.getStockLowAction() );
      assertEquals( new Double( 9.0 ), lStockLevel.getReorderQt() );
      assertEquals( new Double( 50.0 ), lStockLevel.getMaxQt() );
      assertEquals( new Double( 5.0 ), lStockLevel.getMinReorderQt() );
      assertEquals( new Double( 1.0 ), lStockLevel.getWeightFactorQt() );
      assertEquals( null, lStockLevel.getBatchSize() );

      InvLocTable lInvLoc = InvLocTable.findByPrimaryKey( lStockLevelKey.getLocationKey() );
      assertEquals( new Double( 100.0 ), lInvLoc.getInboundFlightsQt() );
   }


   /**
    * Tests the case where stock low action is set to POREQ and all arguments are valid. Stock level
    * details should be properly updated.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testPoreqStockLevel() throws Exception {

      InvLocStockKey lStockLevelKey = createStockLevel();

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setLowAction( POREQ.getCd() );
      lStockLevelTO.setLocBatchSize( 10.0, "aReorderQuantity" );
      lStockLevelTO.setReorderLevelQt( 9.0, "aReorderLevel" );
      lStockLevelTO.setMaxQt( 50.0, "aMaxQuantity" );
      lStockLevelTO.setMinReorderQt( 5.0, "aMinReorderLevel" );
      lStockLevelTO.setWeightFactorQt( 1.0, "aWeightFactor" );
      lStockLevelTO.iLocation.setInboundFlightsQt( 100.0, "aInboundFlights" );

      iStockLevelService.set( lStockLevelKey, lStockLevelTO );

      InvLocStockTable lStockLevel = InvLocStockTable.findByPrimaryKey( lStockLevelKey );
      assertEquals( POREQ, lStockLevel.getStockLowAction() );
      assertEquals( new Double( 10.0 ), lStockLevel.getBatchSize() );
      assertEquals( new Double( 9.0 ), lStockLevel.getReorderQt() );
      assertEquals( new Double( 50.0 ), lStockLevel.getMaxQt() );
      assertEquals( new Double( 5.0 ), lStockLevel.getMinReorderQt() );
      assertEquals( new Double( 1.0 ), lStockLevel.getWeightFactorQt() );

      InvLocTable lInvLoc = InvLocTable.findByPrimaryKey( lStockLevelKey.getLocationKey() );
      assertEquals( new Double( 100.0 ), lInvLoc.getInboundFlightsQt() );
   }


   /**
    * Tests the case where stock low action is set to SHIPREQ and all arguments are valid. Stock
    * level details should be properly updated.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testShipreqStockLevel() throws Exception {

      InvLocStockKey lStockLevelKey = createStockLevel();

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setLowAction( SHIPREQ.getCd() );
      lStockLevelTO.setLocBatchSize( 10.0, "aShippingQuantity" );
      lStockLevelTO.setReorderLevelQt( 9.0, "aReorderLevel" );
      lStockLevelTO.setMaxQt( 50.0, "aMaxQuantity" );
      lStockLevelTO.setMinReorderQt( 5.0, "aMinReorderLevel" );
      lStockLevelTO.setWeightFactorQt( 1.0, "aWeightFactor" );
      lStockLevelTO.iLocation.setInboundFlightsQt( 100.0, "aInboundFlights" );

      iStockLevelService.set( lStockLevelKey, lStockLevelTO );

      InvLocStockTable lStockLevel = InvLocStockTable.findByPrimaryKey( lStockLevelKey );
      assertEquals( SHIPREQ, lStockLevel.getStockLowAction() );
      assertEquals( new Double( 10.0 ), lStockLevel.getBatchSize() );
      assertEquals( new Double( 9.0 ), lStockLevel.getReorderQt() );
      assertEquals( new Double( 50.0 ), lStockLevel.getMaxQt() );
      assertEquals( new Double( 5.0 ), lStockLevel.getMinReorderQt() );
      assertEquals( new Double( 1.0 ), lStockLevel.getWeightFactorQt() );

      InvLocTable lInvLoc = InvLocTable.findByPrimaryKey( lStockLevelKey.getLocationKey() );
      assertEquals( new Double( 100.0 ), lInvLoc.getInboundFlightsQt() );
   }


   /**
    * Tests the case where stock low action is set to POREQ and one of the stock's parts is
    * non-procurable. InvalidStockLowsActionException should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testPoreqWithNonProcurablePart() throws Exception {

      InvLocStockKey lStockLevelKey = createStockLevel();

      new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH )
            .withOemPartNo( "NonProcurablePart" ).withStatus( RefPartStatusKey.ACTV )
            .withFinancialType( RefFinanceTypeKey.CONSUM ).withUnitType( RefQtyUnitKey.EA )
            .withTotalQuantity( new BigDecimal( 100.0 ) ).withTotalValue( BigDecimal.ONE )
            .withAssetAccount( iAccount ).withStock( iStockNo ).isNonProcurable().build();

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setLowAction( POREQ.getCd() );
      lStockLevelTO.setLocBatchSize( 10.0, "aReorderQuantity" );
      lStockLevelTO.setReorderLevelQt( 10.0, "aReorderLevel" );
      lStockLevelTO.setMaxQt( 50.0, "aMaxQuantity" );
      lStockLevelTO.setMinReorderQt( 5.0, "aMinReorderLevel" );
      lStockLevelTO.setWeightFactorQt( 1.0, "aWeightFactor" );
      lStockLevelTO.iLocation.setInboundFlightsQt( 100.0, "aInboundFlights" );

      try {
         // attempt to create a POREQ stock level for a stock with a non-procurable part
         iStockLevelService.set( lStockLevelKey, lStockLevelTO );
         fail( "expecting an exception since the part is non-procurable but stock low action is POREQ" );
      } catch ( InvalidStockLowsActionException e ) {
         //
      }
   }


   /**
    * Tests the case where stock low action is set to SHIPREQ, but the shipping quantity is set to
    * 0. An InvalidStockLowsActionException should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testShipreqWithZeroShippingQty() throws Exception {

      InvLocStockKey lStockLevelKey = createStockLevel();

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setLowAction( SHIPREQ.getCd() );
      lStockLevelTO.setLocBatchSize( 0.0, "aShippingQuantity" );// 0 shipping quantity
      lStockLevelTO.setReorderLevelQt( 9.0, "aReorderLevel" );
      lStockLevelTO.setMaxQt( 50.0, "aMaxQuantity" );
      lStockLevelTO.setMinReorderQt( 5.0, "aMinReorderLevel" );
      lStockLevelTO.setWeightFactorQt( 1.0, "aWeightFactor" );
      lStockLevelTO.iLocation.setInboundFlightsQt( 100.0, "aInboundFlights" );

      try {
         iStockLevelService.set( lStockLevelKey, lStockLevelTO );
         fail( "expecting an exception since shipping quantity is 0 but stock low action is SHIPREQ" );
      } catch ( InvalidStockLowsActionException e ) {
         //
      }

   }


   /**
    * Tests the case where stock low action is set to SHIPREQ, but the location of the stock level
    * is not linked to a supplier. An InvalidStockLowsActionException should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testShipreqWithNoSupplier() throws Exception {

      InvLocStockKey lStockLevelKey =
            new StockLevelBuilder( iLocationWithNoSupplier, iStockNo, iOwner ).withReorderQt( 0.0 )
                  .withMinReorderLevel( 0.0 ).build();

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setLowAction( SHIPREQ.getCd() );
      lStockLevelTO.setLocBatchSize( 10.0, "aShippingQuantity" );
      lStockLevelTO.setReorderLevelQt( 9.0, "aReorderLevel" );
      lStockLevelTO.setMaxQt( 50.0, "aMaxQuantity" );
      lStockLevelTO.setMinReorderQt( 5.0, "aMinReorderLevel" );
      lStockLevelTO.setWeightFactorQt( 1.0, "aWeightFactor" );
      lStockLevelTO.iLocation.setInboundFlightsQt( 100.0, "aInboundFlights" );

      try {
         iStockLevelService.set( lStockLevelKey, lStockLevelTO );
         fail( "expecting an exception since the stock level location has no supplier but stock low action is SHIPREQ" );
      } catch ( InvalidStockLowsActionException e ) {
         //
      }
   }


   /**
    * Tests the case where the max level is lower than the reorder level.
    * MaxLevelLowerThanReorderLevelException should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testMaxLevelLowerThanReorderLevel() throws Exception {

      InvLocStockKey lStockLevelKey = createStockLevel();

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setLowAction( POREQ.getCd() );
      lStockLevelTO.setLocBatchSize( 10.0, "aShippingQuantity" );
      lStockLevelTO.setMinReorderQt( 5.0, "aMinReorderLevel" );
      lStockLevelTO.setWeightFactorQt( 1.0, "aWeightFactor" );
      lStockLevelTO.iLocation.setInboundFlightsQt( 100.0, "aInboundFlights" );

      // set the max level < reorder level
      double lReorderLevel = 9.0;
      lStockLevelTO.setReorderLevelQt( lReorderLevel, "aReorderLevel" );
      lStockLevelTO.setMaxQt( new Double( lReorderLevel - 1 ), "aMaxQuantity" );

      try {
         iStockLevelService.set( lStockLevelKey, lStockLevelTO );
         fail( "expecting an exception since the max level is lower than the reorder level" );
      } catch ( MaxLevelLowerThanReorderLevelException e ) {
         //
      }
   }


   /**
    * Tests the case where the max level is lower than the reorder level.
    * MaxLevelLowerThanReorderLevelException should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testInvalidStockLowAction() throws Exception {

      InvLocStockKey lStockLevelKey = createStockLevel();

      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setLowAction( "TEST" ); // set invalid stock low action
      lStockLevelTO.setLocBatchSize( 10.0, "aShippingQuantity" );
      lStockLevelTO.setReorderLevelQt( 10.0, "aReorderLevel" );
      lStockLevelTO.setMaxQt( 50.0, "aMaxQuantity" );
      lStockLevelTO.setMinReorderQt( 5.0, "aMinReorderLevel" );
      lStockLevelTO.setWeightFactorQt( 1.0, "aWeightFactor" );
      lStockLevelTO.iLocation.setInboundFlightsQt( 100.0, "aInboundFlights" );

      try {
         iStockLevelService.set( lStockLevelKey, lStockLevelTO );
         fail( "expecting an exception since TEST is not a valid stock low action" );
      } catch ( InvalidReftermException e ) {
         //
      }
   }


   /**
    * Tests the case where stock low action is set to DISTREQ and all arguments are valid. Warehouse
    * stock level details should be properly updated.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testDistreqWarehouseStockLevel() throws Exception {

      InvLocStockKey lStockLevelKey =
            new StockLevelBuilder( iWarehouseWithSupplier, iStockNo, iOwner ).withReorderQt( 100.0 )
                  .build();

      StockLevelTO lStockLevelTO = setStockLevelTO();

      iStockLevelService.set( lStockLevelKey, lStockLevelTO );

      InvLocStockTable lStockLevel = InvLocStockTable.findByPrimaryKey( lStockLevelKey );
      assertEquals( DISTREQ, lStockLevel.getStockLowAction() );
      assertEquals( new Double( 9.0 ), lStockLevel.getReorderQt() );
      assertEquals( new Double( 50.0 ), lStockLevel.getMaxQt() );
      assertEquals( new Double( 5.0 ), lStockLevel.getMinReorderQt() );

   }


   /**
    * Tests the case where stock low action is set to DISTREQ, but the location of the warehouse
    * stock level is not linked to a local supplier. An InvalidWarehouseStockLowsActionValidator
    * should be thrown.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test( expected = InvalidWarehouseStockLowsActionValidator.class )
   public void testDistreqWarehouseStockLevelWithoutSupplier() throws Exception {

      InvLocStockKey lStockLevelKey =
            new StockLevelBuilder( iWarehouseWithoutSupplier, iStockNo, iOwner ).build();

      StockLevelTO lStockLevelTO = setStockLevelTO();

      try {
         iStockLevelService.set( lStockLevelKey, lStockLevelTO );
      } catch ( InvalidStockLowsActionException e ) {
         //
      }
   }


   /**
    * DOCUMENT_ME
    *
    * @return
    * @throws MandatoryArgumentException
    * @throws NegativeValueException
    * @throws MaxLowerThanMinException
    */
   private StockLevelTO setStockLevelTO()
         throws MandatoryArgumentException, NegativeValueException, MaxLowerThanMinException {
      StockLevelTO lStockLevelTO = new StockLevelTO();
      lStockLevelTO.setLowAction( DISTREQ.getCd() );
      lStockLevelTO.setReorderLevelQt( 9.0, "aReorderLevel" );
      lStockLevelTO.setMaxQt( 50.0, "aMaxQuantity" );
      lStockLevelTO.setMinReorderQt( 5.0, "aMinReorderLevel" );

      lStockLevelTO.setWeightFactorQt( 1.0, "aWeightFactor" );
      lStockLevelTO.iLocation.setInboundFlightsQt( 1.0, "aInboundFlights" );
      lStockLevelTO.setLocBatchSize( 1.0, "aShippingQuantity" );
      return lStockLevelTO;
   }


   @Before
   public void loadData() throws Exception {

      iOwner = new OwnerDomainBuilder().isDefault().build();

      iSupplierLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .withCode( "supplier" ).isSupplyLocation().build();

      // create a supply location with a supplier
      iLocationWithSupplier = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .withCode( "loc" ).isSupplyLocation().withHubLocation( iSupplierLocation ).build();

      // create a supply location with no supplier
      iLocationWithNoSupplier = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .withCode( "no-supplier" ).isSupplyLocation().build();

      // create local supplier location for warehouse stock levels
      iLocalSupplierLocation = new LocationDomainBuilder().withType( RefLocTypeKey.SRVSTORE )
            .withCode( "local-supplier" ).build();

      // create a supply location with a supplier
      iWarehouseWithSupplier = new LocationDomainBuilder().withType( RefLocTypeKey.SRVSTORE )
            .withCode( "warehouse-with-supplier" ).withHubLocation( iLocalSupplierLocation )
            .build();

      // create a supply location with no supplier
      iWarehouseWithoutSupplier = new LocationDomainBuilder().withType( RefLocTypeKey.SRVSTORE )
            .withCode( "warehouse-without-supplier" ).build();

      // set up the stock and stock levels and assign part to stock
      iStockNo = new StockBuilder().withInvClass( RefInvClassKey.BATCH ).withStockCode( "STK0111" )
            .build();

      iAccount = new AccountBuilder().withType( RefAccountTypeKey.INVASSET )
            .withCode( "TESTACCOUNT" ).isDefault().build();

      new PartNoBuilder().withInventoryClass( RefInvClassKey.BATCH ).withRepairBool( true )
            .withStatus( RefPartStatusKey.ACTV ).withFinancialType( RefFinanceTypeKey.CONSUM )
            .withUnitType( RefQtyUnitKey.EA ).withTotalQuantity( new BigDecimal( 100.0 ) )
            .withTotalValue( BigDecimal.ONE ).withAssetAccount( iAccount ).withStock( iStockNo )
            .build();

      iStockLevelService = new StockLevelService();

   }


   /**
    * Creates a basic stock level with reorder levels defaulted to 0.
    *
    * @return the stock key
    */
   private InvLocStockKey createStockLevel() {
      return new StockLevelBuilder( iLocationWithSupplier, iStockNo, iOwner ).withReorderQt( 0.0 )
            .withMinReorderLevel( 0.0 ).build();
   }

}
