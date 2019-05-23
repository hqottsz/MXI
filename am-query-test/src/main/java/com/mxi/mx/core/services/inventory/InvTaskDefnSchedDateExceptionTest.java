package com.mxi.mx.core.services.inventory;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskSchedFromKey;
import com.mxi.mx.core.services.inventory.exception.InvTaskDefnSchedDateException;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;


/**
 * This tests the exception InvTaskDefnSchedDateExceptionTest
 */
@RunWith( Parameterized.class )
public class InvTaskDefnSchedDateExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   enum TaskFrom {
      FromPart, FromSlot
   };


   private RefInvClassKey iInvClassKey;
   private RefTaskSchedFromKey iTaskSchedFromKey;
   private TaskFrom iTaskFrom;
   private boolean iExpectedResult;
   private RefTaskDefinitionStatusKey iTaskDefStatus;


   public InvTaskDefnSchedDateExceptionTest(RefInvClassKey aInvClassKey,
         RefTaskSchedFromKey aTaskSchedFromKey, TaskFrom aTaskFrom,
         RefTaskDefinitionStatusKey aTaskDefStatus, boolean aExpectedResult) {
      iInvClassKey = aInvClassKey;
      iTaskSchedFromKey = aTaskSchedFromKey;
      iTaskFrom = aTaskFrom;
      iTaskDefStatus = aTaskDefStatus;
      iExpectedResult = aExpectedResult;
   }


   @Parameterized.Parameters
   public static Collection<Object[]> primeNumbers() {
      return Arrays.asList( new Object[][] {
            { RefInvClassKey.SER, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.ACTV, true },

            { RefInvClassKey.SER, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.ACTV, true },

            { RefInvClassKey.SER, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.BUILD, false },

            { RefInvClassKey.SER, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.BUILD, false },

            { RefInvClassKey.SER, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.OBSOLETE, false },

            { RefInvClassKey.SER, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.OBSOLETE, false },

            { RefInvClassKey.SER, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.REVISION, false },

            { RefInvClassKey.SER, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.REVISION, false },

            { RefInvClassKey.KIT, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.ACTV, true },

            { RefInvClassKey.KIT, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.ACTV, true },

            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.REVISION, false },

            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.REVISION, false },

            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.ACTV, true },

            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.ACTV, true },

            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.BUILD, false },

            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.BUILD, false },

            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.OBSOLETE, false },

            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.OBSOLETE, false }, } );
   }


   /**
    *
    * There are schedule from manufacture date task definitions that would require an inventory's
    * manufacture date
    *
    */
   @Test
   public void testIsMandatoryDate() {
      // create part
      final PartNoKey lPartNo = new PartNoBuilder().withInventoryClass( iInvClassKey ).build();
      final ConfigSlotKey lBomItem = setUpConfigSlotAndPartGroup( lPartNo, iInvClassKey );
      boolean lHasManufactDateResult = false;

      if ( iTaskFrom == TaskFrom.FromSlot ) {
         setUpSlotBasedTaskDefinition( lBomItem );
         lHasManufactDateResult =
               InvTaskDefnSchedDateException.hasSchedTaskDefn( lBomItem, lPartNo, false, false );
      } else {
         setUpPartBasedTaskDefinition( lPartNo );
         lHasManufactDateResult =
               InvTaskDefnSchedDateException.hasSchedTaskDefn( null, lPartNo, false, false );
      }
      assertTrue(
            "hasSchedTaskDefn returns ".concat( Boolean.toString( lHasManufactDateResult ) )
                  .concat( " for RefInvClass " ).concat( iInvClassKey.getCd() )
                  .concat( " and RefTaskSchedFromKey " ).concat( iTaskSchedFromKey.getCd() )
                  .concat( " and with a " ).concat( iTaskDefStatus.getCd() )
                  .concat( " task definition based " ).concat( iTaskFrom.name() )
                  .concat( ", expected " ).concat( Boolean.toString( iExpectedResult ) ),
            lHasManufactDateResult == iExpectedResult );
   }


   private void setUpSlotBasedTaskDefinition( final ConfigSlotKey aBomItem ) {

      Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

         @Override
         public void configure( RequirementDefinition aRequirementDefinition ) {
            if ( RefTaskSchedFromKey.MANUFACT_DT == iTaskSchedFromKey ) {
               aRequirementDefinition.setScheduledFromManufacturedDate();
            } else {
               aRequirementDefinition.setScheduledFromReceivedDate();
            }
            aRequirementDefinition.againstConfigurationSlot( aBomItem );
            aRequirementDefinition.setStatus( iTaskDefStatus );
         }
      } );
   }


   private void setUpPartBasedTaskDefinition( final PartNoKey aPartNo ) {

      Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

         @Override
         public void configure( RequirementDefinition aRequirementDefinition ) {
            if ( RefTaskSchedFromKey.MANUFACT_DT == iTaskSchedFromKey ) {
               aRequirementDefinition.setScheduledFromManufacturedDate();
            } else {
               aRequirementDefinition.setScheduledFromReceivedDate();
            }
            aRequirementDefinition.addPartNo( aPartNo );
            aRequirementDefinition.setStatus( iTaskDefStatus );
         }
      } );

   }


   /**
    * Set up an ACFT assembly with a TRK config slot, plus a part group of the specified type
    * containing the specified part.
    *
    * @return aircraft config slot
    */
   private ConfigSlotKey setUpConfigSlotAndPartGroup( final PartNoKey aPartKey,
         final RefInvClassKey aInvClass ) {

      final String lAcftCode = "ACFT";
      final String lTrkSlot = "TRK_SLOT";
      Domain.createAircraftAssembly( new DomainConfiguration<AircraftAssembly>() {

         @Override
         public void configure( AircraftAssembly aAircraftAssembly ) {
            aAircraftAssembly
                  .setRootConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                     @Override
                     public void configure( ConfigurationSlot aConfigurationSlot ) {
                        aConfigurationSlot.addPosition( "ACFT_POS" );
                        aConfigurationSlot.setCode( lAcftCode );
                        aConfigurationSlot
                              .addConfigurationSlot( new DomainConfiguration<ConfigurationSlot>() {

                                 @Override
                                 public void configure( ConfigurationSlot aSubConfigurationSlot ) {

                                    // The slot is TRK class
                                    aSubConfigurationSlot.addPosition( "TRK_POS" );
                                    aSubConfigurationSlot.setCode( lTrkSlot );
                                    aSubConfigurationSlot
                                          .setConfigurationSlotClass( RefBOMClassKey.TRK );

                                    // The part and part group can be TRK, SER, BATCH or KIT
                                    aSubConfigurationSlot
                                          .addPartGroup( new DomainConfiguration<PartGroup>() {

                                             @Override
                                             public void configure( PartGroup aPartGroup ) {
                                                aPartGroup.setInventoryClass( iInvClassKey );
                                                aPartGroup.addPart( aPartKey );
                                                aPartGroup.setCode( iInvClassKey.getCd() );
                                             }
                                          } );
                                 }
                              } );
                     }
                  } );
         }
      } );
      return EqpAssmblBom.getBomItemKey( lAcftCode, lTrkSlot );
   }

}
