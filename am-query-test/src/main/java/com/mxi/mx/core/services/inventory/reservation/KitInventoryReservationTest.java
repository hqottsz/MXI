package com.mxi.mx.core.services.inventory.reservation;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.KitBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersStub;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.services.inventory.InventoryServiceFactory;
import com.mxi.mx.core.table.org.OrgHr;
import com.mxi.mx.core.table.sched.SchedInstPartTable;


/**
 * Testing the
 * {@link InventoryReservationService#reserveInventory(InventoryKey aInventory, PartRequestKey aPartRequest, HumanResourceKey aHr, boolean aStealReservation, boolean aAutoReserve, boolean aValidateInvWarnings )}
 * method for kit inventory.
 *
 * @author Libin Cai
 * @created November 17, 2017
 *
 */
public class KitInventoryReservationTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey iHr;
   private int iUserId;


   /**
    *
    * Test reserving a batch part request which has requested quantity of less than one (0 <
    * quantity < 1) and there is exactly the correct quantity of inventory to fulfill the request.
    *
    * @throws Exception
    */
   @Test
   public void
         testGivenKitInventoryWhenReserveForAPartRequestThenKitContentInventorySetupInSchedInstPartTable()
               throws Exception {

      PartNoBuilder lPartNoBuilder = new PartNoBuilder();

      // create kit content part
      PartNoKey lKitContentPart = lPartNoBuilder.withDefaultPartGroup().build();
      PartGroupKey lKitContentPartGroup = lPartNoBuilder.getDefaultPartGroup();

      // create kit part and assign kit content part to the kit
      PartNoKey lKitPart =
            lPartNoBuilder.withInventoryClass( RefInvClassKey.KIT ).withDefaultPartGroup().build();
      new KitBuilder( lKitPart ).withContent( lKitContentPart, lKitContentPartGroup ).build();

      // create kit inventory
      InventoryKey lKitInventory = new InventoryBuilder().withPartNo( lKitPart )
            .withClass( RefInvClassKey.KIT ).withCondition( RefInvCondKey.RFI )
            .atLocation( new LocationDomainBuilder().build() ).build();

      // create kit content inventory and assign it to kit inventory
      InventoryKey lKitContentInventory = new InventoryBuilder().withPartNo( lKitContentPart )
            .withSerialNo( "KIT_CONTENT_INV" ).isInKit( lKitInventory ).build();

      // create a task
      TaskKey lTask = new TaskBuilder().build();

      // add part requirement on the kit part
      TaskPartKey lKitTaskPartKey = new PartRequirementDomainBuilder( lTask ).build();

      // create part request on the kit part requirement
      PartRequestKey lKitPartRequest = new PartRequestBuilder()
            .forPartRequirement( new TaskInstPartKey( lKitTaskPartKey, 1 ) ).build();

      // add part requirement with installed part on the kit content part
      TaskPartKey lKitContentTaskPartKey = new PartRequirementDomainBuilder( lTask )
            .withInstallPart( lKitContentPart ).forPartGroup( lKitContentPartGroup )
            .withInstallQuantity( 1.0 ).withRequestAction( RefReqActionKey.INKIT ).build();

      // reserve kit inventory
      InventoryServiceFactory.getInstance().getInventoryReservationService()
            .reserveInventory( lKitInventory, lKitPartRequest, iHr, false, false, false );

      SchedInstPartTable lSchedInstPart =
            SchedInstPartTable.findByPrimaryKey( new TaskInstPartKey( lKitContentTaskPartKey, 1 ) );

      // assure the kit content inventory is saved to sched_inst_part table on kit content part
      // requirement
      assertEquals( lKitContentInventory, lSchedInstPart.getInventory() );

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
