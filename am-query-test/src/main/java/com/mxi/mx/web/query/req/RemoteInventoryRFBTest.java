package com.mxi.mx.web.query.req;

import static com.mxi.mx.core.key.RefInvClassKey.ASSY;
import static com.mxi.mx.core.key.RefInvClassKey.SER;
import static com.mxi.mx.core.key.RefInvClassKey.TRK;
import static com.mxi.mx.core.key.RefInvCondKey.REPREQ;
import static com.mxi.mx.core.key.RefInvCondKey.RFI;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.SerializedInventory;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Test the RFB quantity provided in RemoteInventory.qrx for the Reserve Remote Inventory page
 */
@RunWith( Parameterized.class )
public class RemoteInventoryRFBTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private RefInvClassKey iInvClassKey;
   private boolean iInvComplete;
   private RefInvCondKey iInvCondKey;
   private boolean iReserved;
   private boolean iInstalled;
   private PartGroupKey iPartRequestPartGroupKey;
   private PartRequestKey iPartRequestKey;

   private int iExpectedAvailable;
   private int iExpectedTotal;
   private int iExpectedRFB;
   private HumanResourceKey iHr;
   private int iUserId;

   private static boolean COMPLETE = true;
   private static boolean INCOMPLETE = false;

   private static boolean RESERVED = true;
   private static boolean UNRESERVED = false;

   private static boolean INSTALLED = true;
   private static boolean LOOSE = false;


   public RemoteInventoryRFBTest(RefInvClassKey aInvClassKey, boolean aInvComplete,
         RefInvCondKey aInvCondKey, boolean aReserved, boolean aInstalled, int aExpectedAvailable,
         int aExpectedTotal, int aExpectedRFB) {
      iInvClassKey = aInvClassKey;
      iInvComplete = aInvComplete;
      iInvCondKey = aInvCondKey;
      iReserved = aReserved;
      iInstalled = aInstalled;
      iExpectedAvailable = aExpectedAvailable;
      iExpectedTotal = aExpectedTotal;
      iExpectedRFB = aExpectedRFB;
   }


   /*
    * Construct a single Maintenix inventory based on the members
    */
   private void createInventory() {

      final LocationKey lLocationKey = new LocationDomainBuilder().isSupplyLocation().build();

      InventoryKey lInvKey = null;

      final PartGroupKey iPartGroup = Domain.createPartGroup( new DomainConfiguration<PartGroup>() {

         @Override
         public void configure( PartGroup aBuilder ) {
            aBuilder.setInventoryClass( iInvClassKey );
         }
      } );

      iPartRequestPartGroupKey = iPartGroup;

      final PartNoKey lPartNoKey = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setPartGroup( iPartGroup, true );
            aBuilder.setInventoryClass( iInvClassKey );
            aBuilder.setQtyUnitKey( RefQtyUnitKey.EA );
         }
      } );

      if ( RefInvClassKey.TRK.equals( iInvClassKey ) ) {

         lInvKey = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

            @Override
            public void configure( TrackedInventory aBuilder ) {
               aBuilder.setComplete( iInvComplete );
               aBuilder.setCondition( iInvCondKey );
               aBuilder.setParent( iInstalled ? Domain.createAircraft() : null );
               aBuilder.setLocation( lLocationKey );
               aBuilder.setPartNumber( lPartNoKey );

            }
         } );
      } else if ( RefInvClassKey.ASSY.equals( iInvClassKey ) ) {
         lInvKey = Domain.createEngine( new DomainConfiguration<Engine>() {

            @Override
            public void configure( Engine aBuilder ) {
               aBuilder.setComplete( iInvComplete );
               aBuilder.setCondition( iInvCondKey );
               aBuilder.setParent( iInstalled ? Domain.createAircraft() : null );
               aBuilder.setLocation( lLocationKey );
               aBuilder.setPartNumber( lPartNoKey );

            }
         } );
      } else if ( RefInvClassKey.SER.equals( iInvClassKey ) ) {
         lInvKey =
               Domain.createSerializedInventory( new DomainConfiguration<SerializedInventory>() {

                  @Override
                  public void configure( SerializedInventory aBuilder ) {
                     aBuilder.setComplete( iInvComplete );
                     aBuilder.setCondition( iInvCondKey );
                     aBuilder.setParent( iInstalled ? Domain.createAircraft() : null );
                     aBuilder.setLocation( lLocationKey );
                     aBuilder.setPartNumber( lPartNoKey );

                  }
               } );
      } else {
         Assert.fail( "Can't test ".concat( iInvClassKey.toString() ) );
      }

      if ( iReserved ) {
         try {
            InventoryServiceFactory.getInstance().getInventoryReservationService()
                  .reserveInventory( lInvKey,
                        new PartRequestBuilder().withRequestedQuantity( 1.0 )
                              .withQuantityUnit( RefQtyUnitKey.EA ).build(),
                        iHr, false, false, false );
         } catch ( MxException | TriggerException e ) {
            Assert.fail( e.toString() );
         }
      }
   }


   private void createPartRequest() {
      PartRequestBuilder iPRBuilder = new PartRequestBuilder()
            .forPartGroup( iPartRequestPartGroupKey ).withQuantityUnit( RefQtyUnitKey.EA );
      iPartRequestKey = iPRBuilder.build();
   }


   @Parameterized.Parameters
   public static Collection<Object[]> sTestParameters() {

      return Arrays.asList( new Object[][] {
            /**
             * Inv Class, Complete, Inv Condition, reserved, available, total, rfb *
             */
            // Baseline: One loose TRK inventory on-location, RFI but not RFB, available for
            // reservation
            { TRK, COMPLETE, RFI, UNRESERVED, LOOSE, 1, 1, 0 },

            // If it is installed or non-rfi, it isn't available or total
            { TRK, COMPLETE, RFI, UNRESERVED, INSTALLED, 0, 0, 0 },
            { TRK, COMPLETE, REPREQ, UNRESERVED, LOOSE, 0, 0, 0 },

            // If it is reserved, it is included in total but not available
            { TRK, COMPLETE, RFI, RESERVED, LOOSE, 0, 1, 0 },

            // If it is RFB and available, it is included in total, available, and RFB
            { TRK, INCOMPLETE, RFI, UNRESERVED, LOOSE, 1, 1, 1 },

            // If it is RFB and reserved, it is included in total and RFB but not available
            { TRK, INCOMPLETE, RFI, RESERVED, LOOSE, 0, 1, 1 },

            // ASSY can be RFB
            { ASSY, INCOMPLETE, RFI, UNRESERVED, LOOSE, 1, 1, 1 },

            // But not SER
            { SER, INCOMPLETE, RFI, UNRESERVED, LOOSE, 1, 1, 0 } } );
   }


   @Test
   public void testQuantities() {
      createInventory();

      createPartRequest();

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iPartRequestKey, new String[] { "aReqPartDbId", "aReqPartId" } );

      // Sort the Dataset
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.req.RemoteInventory", lArgs );

      if ( iExpectedAvailable == 0 && iExpectedTotal == 0 && iExpectedRFB == 0 ) {
         Assert.assertFalse( "Didn't expect any rows", lDs.hasNext() );
         return;
      }

      lDs.next();

      int iAvailableQt = lDs.getInt( "avail_qt" );
      int iTotalQt = lDs.getInt( "total_qt" );
      int iRFBQt = lDs.getInt( "RFB_qt" );

      Assert.assertEquals( "Available quantity not as expected", iExpectedAvailable, iAvailableQt );
      Assert.assertEquals( "Total quantity not as expected", iExpectedTotal, iTotalQt );
      Assert.assertEquals( "RFB quantity not as expected", iExpectedRFB, iRFBQt );

   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void before() throws Exception {

      iHr = new HumanResourceDomainBuilder().build();
      iUserId = OrgHr.findByPrimaryKey( iHr ).getUserId();

      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iHr ) );

      UserParametersStub lUserParametersStub = new UserParametersStub( iUserId, "LOGIC" );
      lUserParametersStub.setString( "INSTALLED_INVENTORY_NOT_APPLICABLE", "" );
      UserParameters.setInstance( iUserId, "LOGIC", lUserParametersStub );
   }


   @After
   public void after() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
   }

}
