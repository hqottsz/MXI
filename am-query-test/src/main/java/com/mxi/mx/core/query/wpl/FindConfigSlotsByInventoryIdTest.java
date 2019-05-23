package com.mxi.mx.core.query.wpl;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefBOMClassKey;


/**
 * Integration tests for FindConfigSlotsByInventoryId.qrx
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class FindConfigSlotsByInventoryIdTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Finds all the config slots of an aircraft
    *
    */
   @Test
   public void testQuery() throws Exception {

      // Create aircraft assembly
      final AssemblyKey lAcftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aAircraftAssembly ) {
                  aAircraftAssembly.setCode( "A320" );
                  aAircraftAssembly
                        .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                           @Override
                           public void configure( ConfigurationSlot aConfigurationSlot ) {
                              aConfigurationSlot.addPosition( "ACFT_POS" );
                              aConfigurationSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void
                                             configure( ConfigurationSlot aSubConfigurationSlot ) {
                                          aSubConfigurationSlot.addPosition( "TRK_POS" );
                                          aSubConfigurationSlot.setCode( "TRK_SLOT" );
                                          aSubConfigurationSlot
                                                .setConfigurationSlotClass( RefBOMClassKey.TRK );
                                       }
                                    } );

                              aConfigurationSlot.addConfigurationSlot(
                                    new DomainConfiguration<ConfigurationSlot>() {

                                       @Override
                                       public void
                                             configure( ConfigurationSlot aSubConfigurationSlot ) {

                                          aSubConfigurationSlot.addPosition( "ENG_POS" );
                                          aSubConfigurationSlot.setCode( "ENG_SLOT" );
                                          aSubConfigurationSlot.setConfigurationSlotClass(
                                                RefBOMClassKey.SUBASSY );
                                       }
                                    } );
                           }
                        } );
               }
            } );

      // Create aircraft
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.setAssembly( lAcftAssembly );
         }
      } );

      // Create tracked inventory
      Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aTrk ) {
            aTrk.setParent( lAircraft );
            aTrk.setLastKnownConfigSlot( lAcftAssembly.getCd(), "TRK_SLOT", "TRK_POS" );
         }
      } );

      // Create engine
      final ConfigSlotPositionKey lEnginePosition =
            new ConfigSlotPositionKey( lAcftAssembly.getDbId(), lAcftAssembly.getCd(), 2, 1 );

      Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setPosition( lEnginePosition );
            aEngine.setParent( lAircraft );
         }
      } );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lAircraft, "aAcftDbId", "aAcftId" );

      QuerySet lDs = QuerySetFactory.getInstance()
            .executeQuery( "com.mxi.mx.wpl.loader.query.FindConfigSlotsByInventoryId", lArgs );

      assertEquals( lDs.getRowCount(), 3 );
   }

}
