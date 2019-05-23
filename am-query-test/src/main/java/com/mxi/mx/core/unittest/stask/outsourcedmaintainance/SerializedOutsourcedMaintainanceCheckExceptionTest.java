package com.mxi.mx.core.unittest.stask.outsourcedmaintainance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.services.inventory.exception.SupplyLocationMismatchException;
import com.mxi.mx.core.services.inventory.phys.exception.InvalidConditionException;
import com.mxi.mx.core.services.stask.workcapture.autoissue.TrackedSerializedAutoIssueInventoryHandler;


@RunWith( Parameterized.class )
public class SerializedOutsourcedMaintainanceCheckExceptionTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private Boolean iSupplyLoc1;
   private Boolean iSupplyLoc2;
   private RefInvCondKey iInventoryCondition;
   private static final String INVNOSDESC = "INVENTORY001";


   public SerializedOutsourcedMaintainanceCheckExceptionTest(RefInvCondKey aInventoryCondition,
         Boolean aSupplyLoc1, Boolean aSupplyLoc2) {

      iInventoryCondition = aInventoryCondition;
      iSupplyLoc1 = aSupplyLoc1;
      iSupplyLoc2 = aSupplyLoc2;

   }


   @Parameterized.Parameters
   public static Collection<Object[]> testScenarios() {
      return Arrays.asList( new Object[][] { { RefInvCondKey.RFI, false, false },
            { RefInvCondKey.RFI, false, true }, { RefInvCondKey.RFI, true, false },
            { RefInvCondKey.RFI, true, true }, { RefInvCondKey.INSPREQ, true, true } } );
   }


   /**
    *
    * When installing inventory is not in serviceable condition(Not RFI/RFB) and Installing
    * inventory is not locally available(Work Package supply location and inventory supply locations
    * are not same ) then the expected exception will be tested.
    *
    */
   @Test
   public void testExceptionWhenPreconditionsForAutoissueIsNotSatisfied()
         throws TriggerException, MxException {

      LocationKey lSupplyLocationKey1 = null;
      LocationKey lSupplyLocationKey2 = null;

      if ( iSupplyLoc1 ) {
         lSupplyLocationKey1 = createLocation( null, false, true );
      }

      if ( iSupplyLoc2 ) {
         lSupplyLocationKey2 = createLocation( null, false, true );
      }

      LocationKey lInventoryLocationKey = createLocation( lSupplyLocationKey1, false, false );
      LocationKey lWorkPackageLocationKey = createLocation( lSupplyLocationKey2, true, false );

      PartNoKey lPartNo = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setShortDescription( "SER_PART" );
         aPart.setCode( "SER_PART" );
      } );

      InventoryKey lInventoryKey = Domain.createSerializedInventory( aInventory -> {
         aInventory.setCondition( iInventoryCondition );
         aInventory.setPartNumber( lPartNo );
         aInventory.setLocation( lInventoryLocationKey );
         aInventory.setInvNoSdesc( INVNOSDESC );
      } );

      TaskKey lTaskKey = Domain.createAdhocTask( aTaskKey -> {
         aTaskKey.addLabour( aLabour -> {
            aLabour.setSkill( RefLabourSkillKey.ENG );
            aLabour.setTechnicianRole( tech -> tech.setScheduledHours( 1 ) );
         } );
      } );

      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( lTaskKey )
            .withInstallPart( lPartNo ).withInstallQuantity( 1.0 ).withInstallSerialNumber( "SER" )
            .withInstallInventory( lInventoryKey ).build();

      TaskInstPartKey lTaskInstPartKey = new TaskInstPartKey( lTaskPartKey, 1 );

      PartRequestKey lPartRequestKey = Domain.createPartRequest( aPartRequest -> {
         aPartRequest.status( RefEventStatusKey.PRINSPREQ );
         aPartRequest.specifiedPart( lPartNo );
         aPartRequest.partRequirement( lTaskInstPartKey );

      } );

      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lTaskKey );
         aWorkPackage.setLocation( lWorkPackageLocationKey );
      } );

      try {
         HumanResourceKey lHr = Domain.createHumanResource();
         new TrackedSerializedAutoIssueInventoryHandler().process( lPartRequestKey, lHr, lTaskKey );
         fail( "Expected InvalidConditionException or SupplyLocationMismatchException" );

      }

      catch ( InvalidConditionException e ) {

         assertEquals( "[MXERR-30159] " + i18n.get( "core.err.30159", INVNOSDESC,
               RefInvCondKey.INSPREQ.getCd(), RefInvCondKey.RFI.getCd() ), e.getMessage() );
      }

      catch ( SupplyLocationMismatchException e ) {
         assertEquals( "[MXERR-33916] " + i18n.get( "core.err.33916", INVNOSDESC ),
               e.getMessage() );

      }

   }


   private LocationKey createLocation( LocationKey aSupplyLocation, Boolean aAutoIssue,
         Boolean aIsSupplyLoc ) {
      return Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.VENTRK );
         aLocation.setAutoIssueBool( aAutoIssue );
         aLocation.setSupplyLocation( aSupplyLocation );
         aLocation.setIsSupplyLocation( aIsSupplyLoc );
      } );

   }

}
