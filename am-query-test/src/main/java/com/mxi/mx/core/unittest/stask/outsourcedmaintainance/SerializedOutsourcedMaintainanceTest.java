package com.mxi.mx.core.unittest.stask.outsourcedmaintainance;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.message.MxMessage;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.services.stask.taskpart.message.MessageToBeInstalledNotReserved;
import com.mxi.mx.core.services.stask.taskpart.message.MessageToBeInstalledPartRequestHasOrder;
import com.mxi.mx.core.services.stask.taskpart.message.MessageToBeInstalledPartRequestIssued;
import com.mxi.mx.core.services.stask.workcapture.autoissue.TrackedSerializedAutoIssueInventoryHandler;


@RunWith( Parameterized.class )
public class SerializedOutsourcedMaintainanceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private Class<MxMessage> iExpectedResult;
   private RefEventStatusKey iRefEventStatusKey;


   public SerializedOutsourcedMaintainanceTest(RefEventStatusKey aRefEventStatusKey,
         Class<MxMessage> aExpectedResult) {
      iRefEventStatusKey = aRefEventStatusKey;
      iExpectedResult = aExpectedResult;
   }


   @Parameterized.Parameters
   public static Collection<Object[]> testScenarios() {
      return Arrays.asList( new Object[][] {
            { RefEventStatusKey.PRONORDER, MessageToBeInstalledPartRequestHasOrder.class },
            { RefEventStatusKey.PRAVAIL, MessageToBeInstalledNotReserved.class },
            { RefEventStatusKey.PRINSPREQ, MessageToBeInstalledNotReserved.class },
            { RefEventStatusKey.PRQUAR, MessageToBeInstalledNotReserved.class },
            { RefEventStatusKey.PRREMOTE, MessageToBeInstalledNotReserved.class },
            { RefEventStatusKey.PRISSUED, MessageToBeInstalledPartRequestIssued.class } } );
   }


   /**
    *
    * Test if expected warning message is given when part requests are in PRONORDER, PRAVAIL,
    * PRINSPREQ, PRQUAR, PRREMOTE and PRIISUED states.
    *
    */
   @Test
   public void testMessagesWhenSerializedAutoIssueInventory() throws TriggerException, MxException {

      PartNoKey lPartNo = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
         aPart.setShortDescription( "SER1" );
         aPart.setCode( "SER1" );
      } );

      LocationKey lSupplyLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.VENTRK );
         aLocation.setIsSupplyLocation( true );
      } );

      InventoryKey lInventoryKey = Domain.createSerializedInventory( aInventory -> {
         aInventory.setCondition( RefInvCondKey.RFI );
         aInventory.setPartNumber( lPartNo );
         aInventory.setLocation( lSupplyLocationKey );
         aInventory.setSerialNumber( "SERINSTALLINV" );
      } );

      InventoryKey lReservedInventoryKey = Domain.createSerializedInventory( aInventory -> {
         aInventory.setCondition( RefInvCondKey.RFI );
         aInventory.setPartNumber( lPartNo );
         aInventory.setSerialNumber( "SERRESERVINV" );
      } );

      LocationKey lLocationKey = Domain.createLocation( aLocation -> {
         aLocation.setType( RefLocTypeKey.VENTRK );
         aLocation.setAutoIssueBool( true );
         aLocation.setSupplyLocation( lSupplyLocationKey );
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

      PurchaseOrderKey lPurchaseOrderKey = Domain.createPurchaseOrder( aOrder -> {
         aOrder.setOrderNumber( "PO01" );
      } );

      PurchaseOrderLineKey lPurchaseOrderLineKey = Domain.createPurchaseOrderLine( aOrderLine -> {
         aOrderLine.orderKey( lPurchaseOrderKey );
         aOrderLine.part( lPartNo );
      } );

      PartRequestKey lPartRequestKey = Domain.createPartRequest( aPartRequest -> {
         aPartRequest.status( iRefEventStatusKey );
         aPartRequest.specifiedPart( lPartNo );
         aPartRequest.partRequirement( lTaskInstPartKey );
         aPartRequest.reservedInventory( lReservedInventoryKey );
         if ( iRefEventStatusKey.equals( RefEventStatusKey.PRONORDER ) ) {
            aPartRequest.purchaseLine( lPurchaseOrderLineKey );
         }

      } );

      Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.addTask( lTaskKey );
         aWorkPackage.setLocation( lLocationKey );
      } );

      HumanResourceKey lHr = Domain.createHumanResource();

      List<MxMessage> lMessageList = new TrackedSerializedAutoIssueInventoryHandler()
            .process( lPartRequestKey, lHr, lTaskKey );

      assertEquals( "Purchase Order of type ".concat( iRefEventStatusKey.getCd() ), iExpectedResult,
            lMessageList.get( 0 ).getClass() );
   }

}
