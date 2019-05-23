package com.mxi.mx.core.query.inventory;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.AircraftAssembly;
import com.mxi.am.domain.ConfigurationSlot;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Part;
import com.mxi.am.domain.PartGroup;
import com.mxi.am.domain.RequirementDefinition;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.OrganizationDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.RefTaskSchedFromKey;
import com.mxi.mx.core.services.inventory.validator.PartNoHasNoFromManufactureTaskDefinitionsValidator;
import com.mxi.mx.core.services.taskdefn.TaskDefnService;
import com.mxi.mx.core.services.taskdefn.transferobject.RequirementTO;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;


/**
 * This tests the query
 * com.mxi.mx.core.query.inventory.PartNoHasFromScheduledDateTaskDefinitions.qrx
 */
@RunWith( Parameterized.class )
public class PartNoHasFromScheduledDateTaskDefinitionsTest {

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


   public PartNoHasFromScheduledDateTaskDefinitionsTest(RefInvClassKey aInvClassKey,
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
            /**
             * <pre>
             * Verify that isValid returns false for:
             * a TRK part
             * which in a part group
             * against which (via the config slot) there is an ACTV slot-based task definition
             * with a schedule-from-manufacture-date rule
             * </pre>
             */
            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.ACTV, false },

            /**
             * <pre>
             * Verify that isValid returns true for:
             * a TRK part
             * which in a part group
             * against which there is a BUILD, OBSOLETE, REVISION, or SUPERSEDE slot-based task definition
             * with schedule-from-manufacture-date
             * (but no ACTV task def as such)
             * </pre>
             */

            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.BUILD, true },
            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.OBSOLETE, true },
            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.REVISION, true },
            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.SUPRSEDE, true },

            // Creation method:
            //

            /**
             * <pre>
             * Verify that isValid returns true for:
             * a TRK part
             * which in a part group
             *against which there is an ACTV part-based task definition
             * with schedule-from-manufacture-date
             * </pre>
             */
            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.ACTV, true },
            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.BUILD, true },
            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.OBSOLETE, true },
            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.REVISION, true },
            { RefInvClassKey.TRK, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromPart,
                  RefTaskDefinitionStatusKey.SUPRSEDE, true },
            /**
             * <pre>
             * Verify that isValid returns true for:
             * a TRK part
             * which in a part group
             * against which (via the config slot) there is an ACTV slot-based task definition
             * with schedule-from-received-date or schedule-from-effective-date
             * </pre>
             */
            { RefInvClassKey.TRK, RefTaskSchedFromKey.RECEIVED_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.ACTV, true },
            { RefInvClassKey.TRK, RefTaskSchedFromKey.EFFECTIVE_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.ACTV, true },

            /**
             * <pre>
             * Verify that isValid returns true for:
             * a SER, BATCH, or KIT part
             * which is in a part group
             * against which (via the config slot) there is a slot-based task definition
             * with schedule-from-manufacture-date
             * </pre>
             */
            { RefInvClassKey.SER, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.ACTV, true },
            { RefInvClassKey.BATCH, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.ACTV, true },
            { RefInvClassKey.KIT, RefTaskSchedFromKey.MANUFACT_DT, TaskFrom.FromSlot,
                  RefTaskDefinitionStatusKey.ACTV, true }, } );
   }


   /**
    *
    * Test that the condition is valid: There are no schedule from manufacture date task definitions
    * that would require an inventory's manufacture date
    *
    */
   @Test
   public void testIsMandatory() {
      final PartNoKey lPartNo = setUpPart( iInvClassKey );
      final ConfigSlotKey aBomItem = setUpConfigSlotAndPartGroup( lPartNo, iInvClassKey );

      if ( iTaskFrom == TaskFrom.FromSlot )
         setUpSlotBasedTaskDefinition( aBomItem );
      else
         setUpPartBasedTaskDefinition();

      final boolean lActualResult =
            PartNoHasNoFromManufactureTaskDefinitionsValidator.isValid( lPartNo );
      assertTrue(
            "isValid returns ".concat( Boolean.toString( lActualResult ) )
                  .concat( " for RefInvClass " ).concat( iInvClassKey.getCd() )
                  .concat( " and RefTaskSchedFromKey " ).concat( iTaskSchedFromKey.getCd() )
                  .concat( " and with a " ).concat( iTaskDefStatus.getCd() )
                  .concat( " task definition based " ).concat( iTaskFrom.name() )
                  .concat( ", expected " ).concat( Boolean.toString( iExpectedResult ) ),
            lActualResult == iExpectedResult );
   }


   private HumanResourceKey setUpHumanResource() {
      HumanResourceKey lHrKey = new HumanResourceDomainBuilder().build();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( lHrKey ) );
      return lHrKey;
   }


   @After
   public void tearDownHumanResource() {
      SecurityIdentificationUtils.setInstance( null );
   }


   private OrgKey setUpOrgWithHumanResource( HumanResourceKey hrkey ) {
      OrgKey lOrgKey = new OrganizationDomainBuilder().withCode( "ORG" ).withDescription( "ORG" )
            .withType( RefOrgTypeKey.OPERATOR ).build();
      // Add the HR to the provided organization.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( hrkey, "hr_db_id", "hr_id" );
      lArgs.add( lOrgKey, "org_db_id", "org_id" );
      lArgs.add( "default_org_bool", 1 );
      MxDataAccess.getInstance().executeInsert( "org_org_hr", lArgs );
      return lOrgKey;
   }


   private void setUpSlotBasedTaskDefinition( final ConfigSlotKey aBomItem ) {

      Domain.createRequirementDefinition( new DomainConfiguration<RequirementDefinition>() {

         @Override
         public void configure( RequirementDefinition aRequirementDefinition ) {
            if ( RefTaskSchedFromKey.MANUFACT_DT == iTaskSchedFromKey )
               aRequirementDefinition.setScheduledFromManufacturedDate();
            else if ( RefTaskSchedFromKey.EFFECTIVE_DT == iTaskSchedFromKey )
               aRequirementDefinition.setScheduledFromEffectiveDate( new Date() );
            else if ( RefTaskSchedFromKey.RECEIVED_DT == iTaskSchedFromKey )
               aRequirementDefinition.setScheduledFromReceivedDate();
            aRequirementDefinition.againstConfigurationSlot( aBomItem );
            aRequirementDefinition.setStatus( iTaskDefStatus );
         }
      } );
   }


   private void setUpPartBasedTaskDefinition() {

      HumanResourceKey lHR = setUpHumanResource();
      OrgKey lOrg = setUpOrgWithHumanResource( lHR );
      PartNoKey lPartNo = Domain.createPart();
      RequirementTO lReqTO = new RequirementTO();
      try {
         lReqTO.setTaskClass( RefTaskClassKey.REQ, "req" );
         lReqTO.setCode( "Req", "code" );
         lReqTO.setName( "Req", "name" );
         lReqTO.setPartNo( lPartNo, "part_no" );
         lReqTO.setOnCondition( false );
         lReqTO.setScheduleFrom( iTaskSchedFromKey );
         lReqTO.setOrganization( lOrg, "Error Label" );
         lReqTO.setForecastRange( 0d, "Forecast Range" );

         TaskDefnService.create( lReqTO, lHR );
      } catch ( MxException e ) {
         Assert.fail( "Problem creating task definition: ".concat( e.getMessage() ) );
      }

   }


   private PartNoKey setUpPart( final RefInvClassKey aInvClassKey ) {
      return Domain.createPart( new DomainConfiguration<Part>() {

         @Override
         public void configure( Part aBuilder ) {
            aBuilder.setInventoryClass( aInvClassKey );
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
