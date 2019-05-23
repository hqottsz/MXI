package com.mxi.mx.core.services.usgrec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.MimPartNumDataKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.table.inv.InvCurrUsage;
import com.mxi.mx.core.table.mim.MimPartNumData;


/**
 * This test is for {@link UsageRecordService}
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class UsageRecordServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   /**
    * Verify that usage parameter changes to an assembly will be synchronized to inventory based on
    * that assembly. Since the system is not treating different types of assembly differently, we
    * are picking aircraft as an example.
    *
    * <pre>
    * Given that I have an assembly
    * And one aircraft against this assembly
    * And I have added a new usage parameters to the assembly
    * When the usage parameters are synchronized.
    * Then verify that usage parameter added to the assembly is synchronized to aircraft.
    * </pre>
    */
   @Test
   public void itSyncsAircraftUsageParametersWhenANewUsageParameterIsAddedToTheAircraftAssembly() {

      final PartNoKey lPart = Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInventoryClass( RefInvClassKey.ACFT );
            aBuilder.setInDefaultPartGroup( true );

         }

      } );

      final AssemblyKey lAircraftAssembly =
            Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

               @Override
               public void configure( AircraftAssembly aBuilder ) {

                  aBuilder.setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aBuilder ) {
                        aBuilder.addPartGroup( new DomainConfiguration<PartGroup>() {

                           @Override
                           public void configure( PartGroup aBuilder ) {
                              aBuilder.setInventoryClass( RefInvClassKey.ACFT );
                              aBuilder.addPart( lPart );

                           }

                        } );
                        aBuilder.addUsageParameter( DataTypeKey.CYCLES );
                     }

                  } );
               }

            } );

      InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aBuilder ) {
            aBuilder.setAssembly( lAircraftAssembly );
            aBuilder.setPart( lPart );
         }

      } );

      // Add a new usage parm to the aircraft assembly
      MimPartNumData.create( new MimPartNumDataKey(
            Domain.readRootConfigurationSlot( lAircraftAssembly ), DataTypeKey.HOURS ) );

      UsageRecordService.synchronizeUsage();

      InvCurrUsage[] lInvCurrUsages = InvCurrUsage.findByInventory( lAircraft );

      List<DataTypeKey> lUsageDataTypes = new ArrayList<>();

      assertEquals( lInvCurrUsages.length, 2 );

      for ( InvCurrUsage lInvCurrUsage : lInvCurrUsages ) {
         lUsageDataTypes.add( lInvCurrUsage.getDataType() );
      }

      assertTrue( lUsageDataTypes.contains( DataTypeKey.CYCLES ) );
      assertTrue( lUsageDataTypes.contains( DataTypeKey.HOURS ) );
   }
}
