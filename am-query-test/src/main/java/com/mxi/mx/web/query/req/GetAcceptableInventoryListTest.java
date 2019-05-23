package com.mxi.mx.web.query.req;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.EngineAssembly;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.eqp.EqpBomPart;


@RunWith( BlockJUnit4ClassRunner.class )
public final class GetAcceptableInventoryListTest {

   @ClassRule
   public static DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @ClassRule
   public static FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule =
         new FakeJavaEeDependenciesRule();

   @ClassRule
   public static InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   protected static final String POSITION_CODE = "POSITION_CODE";
   protected static final String PART_GROUP1 = "PART_GROUP1";
   private static final String TRK_CONFIG_SLOT1 = "TRK_CONFIG_SLOT1";
   private static final String TRK_CONFIG_SLOT_CD1 = "TRK_SLOT_CD1";
   private static final String ENG_ASSMBL_CD = "CFM380";
   private static final String LOCATION_CODE = "ATL";

   private static PartRequestKey iPartRequest = null;
   private static InventoryKey iInventory1 = null;
   private static InventoryKey iInventory2 = null;


   @BeforeClass
   public static void setUp() {

      createPartRequest();
   }


   // Test that only RFB inventory is returned when the filter value is true
   @Test
   public void testReserveInventoryWhenShowRfbOnlyTrue() {

      DataSet lDataSet = getAcceptableInventoryList( iPartRequest, true );
      verifyInventory( iInventory1, lDataSet );
   }


   // Test that only RFI inventory is returned when the filter value is false
   @Test
   public void testReserveInventoryWhenShowRfbOnlyFalse() {

      DataSet lDataSet = getAcceptableInventoryList( iPartRequest, false );
      verifyInventory( iInventory2, lDataSet );
   }


   private void verifyInventory( InventoryKey aExpected, DataSet aDataSet ) {
      // verify result count
      assertEquals( 1, aDataSet.getRowCount() );
      aDataSet.next();
      // verify that the new result inventory is same as expected
      assertEquals( aExpected, aDataSet.getKey( InventoryKey.class, "inventory_key" ) );

   }


   /**
    * Execute the query.
    *
    * @param aPartReq
    *           the part request key.
    * @param aShowRfbInventory
    *           Show RFB Inventory filter value
    */
   private DataSet getAcceptableInventoryList( PartRequestKey aPartReq,
         Boolean aShowRfbInventory ) {
      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aPartReq, new String[] { "aReqPartDbId", "aReqPartId" } );
      lArgs.add( "aShowRfbInventory", aShowRfbInventory );

      // run the query with given arguments and return the result
      DataSet lDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.req.getAcceptableInventoryList", lArgs );
      return lDs;
   }


   private static void createPartRequest() {

      // create supply location
      final LocationKey lSupplyLocation = new LocationDomainBuilder().withCode( LOCATION_CODE )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();

      // Create part
      final PartNoKey lPart1 = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // Create part
      final PartNoKey lPart2 = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // Set up an engine assembly
      final AssemblyKey lEngineAssy =
            Domain.createEngineAssembly( new DomainConfiguration<EngineAssembly>() {

               @Override
               public void configure( EngineAssembly aEngineAssembly ) {
                  aEngineAssembly.setCode( ENG_ASSMBL_CD );
                  aEngineAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aRootCs ) {
                              aRootCs.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void configure( ConfigurationSlot aBuilder ) {
                                          aBuilder.addPosition( TRK_CONFIG_SLOT1 );
                                          aBuilder.setCode( TRK_CONFIG_SLOT_CD1 );
                                          aBuilder.setConfigurationSlotClass( RefBOMClassKey.TRK );
                                          aBuilder.setMandatoryFlag( true );
                                          aBuilder.addPartGroup(
                                                new DomainConfiguration<PartGroup>() {

                                                   @Override
                                                   public void configure( PartGroup aPartGroup ) {
                                                      aPartGroup.setCode( PART_GROUP1 );
                                                      aPartGroup.setInventoryClass(
                                                            RefInvClassKey.TRK );
                                                      aPartGroup.addPart( lPart1 );
                                                      aPartGroup.addPart( lPart2 );
                                                   }

                                                } );
                                       }

                                    } );
                           }
                        } );
               }
            } );

      // Retrieve above created part group
      final PartGroupKey lBomPart = EqpBomPart.getBomPartKey( lEngineAssy, PART_GROUP1 );

      // Set up an engine part
      final PartNoKey lEnginePart = new PartNoBuilder().withInventoryClass( RefInvClassKey.TRK )
            .withStatus( RefPartStatusKey.ACTV ).build();

      // create engine inventory
      final InventoryKey lEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aBuilder ) {
            aBuilder.setAssembly( lEngineAssy );
            aBuilder.setPartNumber( lEnginePart );
         }
      } );

      // create TRK child inventory and attach to engine (RFB inventory)
      iInventory1 = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setLastKnownConfigSlot( ENG_ASSMBL_CD, TRK_CONFIG_SLOT_CD1, TRK_CONFIG_SLOT1 );
            aTrk.setPartNumber( lPart1 );
            aTrk.setCondition( RefInvCondKey.RFI );
            aTrk.setLocation( lSupplyLocation );
            aTrk.setComplete( false );
         }

      } );

      // create TRK child inventory and attach to engine (RFI inventory)
      iInventory2 = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setLastKnownConfigSlot( ENG_ASSMBL_CD, TRK_CONFIG_SLOT_CD1, TRK_CONFIG_SLOT1 );
            aTrk.setPartNumber( lPart2 );
            aTrk.setCondition( RefInvCondKey.RFI );
            aTrk.setLocation( lSupplyLocation );
            aTrk.setComplete( true );
         }
      } );

      // Create task for the engine
      TaskKey lTask =
            new TaskBuilder().onInventory( lEngine ).withTaskClass( RefTaskClassKey.CORR ).build();

      // Create part request for the part group
      iPartRequest = new PartRequestBuilder().forTask( lTask ).withRequestedQuantity( 1.0 )
            .forPartGroup( lBomPart ).isNeededAt( lSupplyLocation )
            .withStatus( RefEventStatusKey.PROPEN ).build();

   }
}
