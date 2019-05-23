
package com.mxi.mx.core.services.stask.taskpart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.message.MxMessage;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.unittest.security.SecurityIdentificationStub;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqPriorityKey;
import com.mxi.mx.core.key.RefReqTypeKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.part.ApplicabilityRange;
import com.mxi.mx.core.services.stask.taskpart.message.MessageToBeInstalledNotApplicable;
import com.mxi.mx.core.table.org.OrgHr;


/**
 * Tests the {@link MessageToBeInstalledNotApplicable} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class MessageToBeInstalledNotApplicableTest {

   private static final String APPLICABILITY_CODE = "1";
   private static final String APPLICABILITY_RANGE = "100-200";
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private HumanResourceKey iAuthorizingHr;
   private int iUserId;
   private UserParametersFake iUserParametersFake;

   private LocationKey iLocationKey;


   /**
    * Tests the MessageToBeInstalledNotApplicable.validateWarning for the part-request context, the
    * inventory being passed is not applicable to the part-request.
    *
    * @throws Exception
    */
   @Test
   public void testValidationFailsWhenTaskInvNotApplicableWithInventoryTobeReserved()
         throws Exception {

      // Ensure the test applicability code is not applicable to the test range.
      assertFalse( "applicability code not applicable to test range.",
            new ApplicabilityRange( APPLICABILITY_RANGE ).isApplicable( APPLICABILITY_CODE ) );

      // Create a config slot
      ConfigSlotKey lConfigSlot = new ConfigSlotBuilder( "CFG1" ).build();

      // Create a part group for the config slot that has an applicability range.
      PartGroupKey lPartGroupKey = new PartGroupDomainBuilder( "PG1" ).withConfigSlot( lConfigSlot )
            .withApplicabilityRange( APPLICABILITY_RANGE ).withPartGroupName( "PG1" ).build();

      // Create an inventory with an applicability code that is outside the part group's
      // applicability range.
      InventoryKey lInventoryKey =
            new InventoryBuilder().withApplicabilityCode( APPLICABILITY_CODE ).build();

      // Create a task against this inventory.
      TaskKey lTaskKey = new TaskBuilder().onInventory( lInventoryKey ).build();

      // create part-request against the task
      PartRequestKey lPartRequest = new PartRequestBuilder().withType( RefReqTypeKey.TASK )
            .withRequestedQuantity( 1.0 ).requiredBy( new Date() )
            .withPriority( RefReqPriorityKey.NORMAL ).withStatus( RefEventStatusKey.PROPEN )
            .isNeededAt( iLocationKey ).forPartGroup( lPartGroupKey ).forTask( lTaskKey )
            .withQuantityUnit( RefQtyUnitKey.EA ).build();

      // create inventory for the part-group that has applicability range
      InventoryKey lInventory = new InventoryBuilder().withPartGroup( lPartGroupKey ).build();

      // WHEN: call validate warning logic for the part-request
      List<MxMessage> lWarnings =
            MessageToBeInstalledNotApplicable.validateWarning( lPartRequest, lInventory );

      // THEN: the warning message is returned
      assertTrue( !lWarnings.isEmpty() );
      assertEquals( "message count", lWarnings.size(), 1 );

      // make sure the message is the one we expected
      assertEquals( "message class", lWarnings.get( 0 ).getClass(),
            MessageToBeInstalledNotApplicable.class );

      // make sure the message and titles are correct
      MessageToBeInstalledNotApplicable lMessage =
            ( MessageToBeInstalledNotApplicable ) lWarnings.get( 0 );
      assertEquals( "message frame", "core.msg.TOBE_INST_PART_GROUP_NOT_APPLICABLE_TSK_CTX_message",
            lMessage.getMsgFrameKey() );
      assertEquals( "message title", "core.msg.TOBE_INST_PART_GROUP_NOT_APPLICABLE_title",
            lMessage.getMsgTitleKey() );

   }


   @Before
   public void setup() {

      iAuthorizingHr = Domain.createHumanResource();
      iUserId = OrgHr.findByPrimaryKey( iAuthorizingHr ).getUserId();
      SecurityIdentificationUtils.setInstance( new SecurityIdentificationStub( iAuthorizingHr ) );

      iUserParametersFake = new UserParametersFake( iUserId, "LOGIC" );
      UserParameters.setInstance( iUserId, "LOGIC", iUserParametersFake );

      iLocationKey = new LocationDomainBuilder().withCode( "AIRPORT" )
            .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build();
   }


   @After
   public void tearDown() {
      UserParameters.setInstance( iUserId, "LOGIC", null );
      SecurityIdentificationUtils.setInstance( null );
   }

}
