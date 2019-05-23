package com.mxi.mx.core.services.inventory.tool;

import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.DepartmentBuilder;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartGroupDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.ToolRequirementDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLabourRoleStatusKey;
import com.mxi.mx.core.key.RefLabourRoleTypeKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedLabourRoleKey;
import com.mxi.mx.core.key.SchedLabourToolKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.tool.Tool;
import com.mxi.mx.core.services.stask.tool.ToolService;
import com.mxi.mx.core.table.sched.SchedLabourRoleStatusTable;
import com.mxi.mx.core.table.sched.SchedLabourRoleTable;
import com.mxi.mx.core.table.sched.SchedLabourTable;
import com.mxi.mx.core.table.sched.SchedLabourToolTable;


/**
 * Testing the
 * {@link ToolCheckoutService#checkOut(ToolCheckoutTO, com.mxi.mx.core.key.HumanResourceKey)} method
 *
 */
public class ToolCheckoutServiceTest {

   private static final String TOOL_1 = "TOOL1";
   private static final String TOOL_2 = "TOOL2";
   private static final String USER_USERNAME = "mxi";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey iHr;
   private LocationKey iSupplyLocation;
   private InventoryKey iAircraft;


   /**
    *
    * Testing when there is a task that has no tool requirements, then a new tool requirement is
    * created for the checked out tool.
    *
    * @throws MxException
    */
   @Test
   public void testNoToolRequirement() throws MxException {

      // create a tool
      PartNoKey lToolDefinition =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).isTool().build();
      InventoryKey lTool = createTool( lToolDefinition, TOOL_1 );

      // create a task with no tool requirements
      TaskKey lTask = createAdhocTask();

      // the user checks out the tool
      new ToolCheckoutService( lTool ).checkOut( createToolCheckoutTO( lTask ), iHr );

      // assert there is a new tool requirement
      Tool[] lToolRequirements = ToolService.getTool( lTask );
      assertEquals( "there should be 1 tool requirement", 1, lToolRequirements.length );
      assertEquals( "the tool should be assigned to the requirement", lTool,
            lToolRequirements[0].getAssignedTool() );
   }


   /**
    *
    * Testing when there is a task that has a tool requirement but the requirement is for a
    * different tool, then a new tool requirement is created for the checked out tool.
    *
    * @throws MxException
    */
   @Test
   public void testNoMatchingToolRequirement() throws MxException {

      // create a tool
      PartNoKey lToolDefinition1 =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).isTool().build();
      InventoryKey lTool1 = createTool( lToolDefinition1, TOOL_1 );

      // create a second tool
      PartNoKey lToolDefinition2 =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).isTool().build();

      // create a task with a tool requirement for a tool2
      TaskKey lTask = createAdhocTask();
      new ToolRequirementDomainBuilder( lTask ).forTool( lToolDefinition2 ).build();

      // the user checks out tool1
      new ToolCheckoutService( lTool1 ).checkOut( createToolCheckoutTO( lTask ), iHr );

      // there should now be 2 tool requirements
      Tool[] lToolRequirements = ToolService.getTool( lTask );
      assertEquals( "there should be 2 tool requirements", 2, lToolRequirements.length );
      for ( Tool lToolRequirement : lToolRequirements ) {
         if ( lToolDefinition2.equals( lToolRequirement.getAssignedPartNo() ) ) {
            assertEquals( "there is no tool assigned to the requirement", null,
                  lToolRequirement.getAssignedTool() );
         } else {
            assertEquals( "there should be a tool requirement for tool1", lToolDefinition1,
                  lToolRequirement.getAssignedPartNo() );
            assertEquals( "the tool should be assigned to the requirement", lTool1,
                  lToolRequirement.getAssignedTool() );
         }
      }
   }


   /**
    *
    * Testing when there is a task that has an open tool requirement which matches the checked out
    * tool, then the checked out tool is assigned to that requirement.
    *
    * @throws MxException
    */
   @Test
   public void testOpenToolRequirement() throws MxException {

      // create tool
      PartNoKey lToolDefinition =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).isTool().build();
      InventoryKey lTool = createTool( lToolDefinition, TOOL_1 );

      // create task with one open tool requirement
      TaskKey lTask = createAdhocTask();
      new ToolRequirementDomainBuilder( lTask ).forTool( lToolDefinition ).build();

      new ToolCheckoutService( lTool ).checkOut( createToolCheckoutTO( lTask ), iHr );

      Tool[] lToolRequirements = ToolService.getTool( lTask );
      assertEquals( "there should be 1 tool requirement", 1, lToolRequirements.length );
      assertEquals( "the tool should be assigned to the requirement", lTool,
            lToolRequirements[0].getAssignedTool() );
   }


   /**
    *
    * Testing when there is a task that has a signed tool requirement AND an open tool requirement
    * which both match the checked out tool, then the checked out tool is assigned to the open
    * requirement.
    *
    * @throws MxException
    */
   @Test
   public void testOpenToolRequirementAndSignedToolRequirement() throws MxException {

      // create a tool
      PartNoKey lToolDefinition =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).isTool().build();
      InventoryKey lTool = createTool( lToolDefinition, TOOL_1 );

      // create a task
      TaskKey lTask = createAdhocTask();
      // create one signed tool requirement
      new ToolRequirementDomainBuilder( lTask ).forTool( lToolDefinition )
            .withAssignedInventory( lTool ).build();
      signOffToolRequirement( lTask, ToolService.getTool( lTask )[0] );
      // and create one open tool requirement
      new ToolRequirementDomainBuilder( lTask ).forTool( lToolDefinition ).build();

      new ToolCheckoutService( lTool ).checkOut( createToolCheckoutTO( lTask ), iHr );

      Tool[] lToolRequirements = ToolService.getTool( lTask );
      assertEquals( "there should be 2 tool requirement", 2, lToolRequirements.length );
      // the tool should be assigned to both requirements
      assertEquals( "the tool should be assigned to the requirement", lTool,
            lToolRequirements[0].getAssignedTool() );
      assertEquals( "the tool should be assigned to the requirement", lTool,
            lToolRequirements[1].getAssignedTool() );
   }


   /**
    *
    * Testing when there is a task that has a matching tool requirement with a different tool
    * assigned, a new tool requirement is created for the checked out tool.
    *
    * @throws MxException
    */
   @Test
   public void testToolRequirementWithDifferentToolAssigned() throws MxException {
      // create tool
      PartNoKey lToolDefinition =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).isTool().build();
      InventoryKey lTool1 = createTool( lToolDefinition, TOOL_1 );
      InventoryKey lTool2 = createTool( lToolDefinition, TOOL_2 );

      // create task with one tool requirement with the first tool assigned
      TaskKey lTask = createAdhocTask();
      new ToolRequirementDomainBuilder( lTask ).forTool( lToolDefinition )
            .withAssignedInventory( lTool1 ).build();

      new ToolCheckoutService( lTool2 ).checkOut( createToolCheckoutTO( lTask ), iHr );

      Tool[] lToolRequirements = ToolService.getTool( lTask );
      assertEquals( "there should be 2 tool requirements", 2, lToolRequirements.length );

      for ( Tool lToolRequirement : lToolRequirements ) {
         assertThat( "the tool should be assigned to the requirement",
               lToolRequirement.getAssignedTool(), isIn( Arrays.asList( lTool1, lTool2 ) ) );
      }

   }


   /**
    *
    * Testing when there is a task that has a matching tool requirement with the same tool already
    * assigned, a new tool requirement is NOT created.
    *
    * @throws MxException
    */
   @Test
   public void testToolRequirementWithSameToolAssigned() throws MxException {
      // create tool
      PartNoKey lToolDefinition =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).isTool().build();
      InventoryKey lTool = createTool( lToolDefinition, TOOL_1 );

      // create task with one tool requirement with the first tool assigned
      TaskKey lTask = createAdhocTask();
      new ToolRequirementDomainBuilder( lTask ).forTool( lToolDefinition )
            .withAssignedInventory( lTool ).build();

      new ToolCheckoutService( lTool ).checkOut( createToolCheckoutTO( lTask ), iHr );

      Tool[] lToolRequirements = ToolService.getTool( lTask );
      assertEquals( "there should be 1 tool requirement", 1, lToolRequirements.length );
      assertEquals( "the tool should be assigned to the requirement", lTool,
            lToolRequirements[0].getAssignedTool() );
   }


   /**
    *
    * Testing when there is a task that has a matching tool requirement with the same tool already
    * assigned, but the tool requirement has been signed off on, then a new tool requirement is
    * created.
    *
    * @throws MxException
    */
   @Test
   public void testSignedToolRequirementWithSameToolAssigned() throws MxException {
      // create a tool
      PartNoKey lToolDefinition =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).isTool().build();
      InventoryKey lTool = createTool( lToolDefinition, TOOL_1 );

      // create a task
      TaskKey lTask = createAdhocTask();
      // add a signed tool requirement
      new ToolRequirementDomainBuilder( lTask ).forTool( lToolDefinition )
            .withAssignedInventory( lTool ).build();
      signOffToolRequirement( lTask, ToolService.getTool( lTask )[0] );

      new ToolCheckoutService( lTool ).checkOut( createToolCheckoutTO( lTask ), iHr );

      Tool[] lToolRequirements = ToolService.getTool( lTask );
      assertEquals( "there should be 2 tool requirements", 2, lToolRequirements.length );

      assertEquals( "the tool should be assigned to the 1st requirement", lTool,
            lToolRequirements[0].getAssignedTool() );
      assertEquals( "the tool should also be assigned to the 2nd requirement", lTool,
            lToolRequirements[1].getAssignedTool() );
   }


   /**
    *
    * Testing when there is a task that has an open tool requirement for a tool group, and the
    * checked out tool is a tool in that tool group, then the checked out tool is assigned to the
    * requirement.
    *
    * @throws MxException
    */
   @Test
   public void testOpenToolRequirementForToolGroup() throws MxException {

      // create a tool
      PartNoKey lToolDefinition1 =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).isTool().build();

      // create a tool group for the tool
      PartGroupKey lToolGroup = new PartGroupDomainBuilder( "GROUP1" ).isToolGroup()
            .withInventoryClass( RefInvClassKey.SER ).withPartNo( lToolDefinition1 ).build();

      // create a second tool and make it an alternate in the tool group
      PartNoKey lToolDefinition2 = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .isTool().isAlternateIn( lToolGroup ).build();

      // create a tool inventory for the second (alternate) tool
      InventoryKey lTool2 = createTool( lToolDefinition2, TOOL_2 );

      // create a task with a tool requirement for the tool group
      TaskKey lTask = createAdhocTask();
      new ToolRequirementDomainBuilder( lTask ).forToolGroup( lToolGroup ).build();

      new ToolCheckoutService( lTool2 ).checkOut( createToolCheckoutTO( lTask ), iHr );

      Tool[] lToolRequirements = ToolService.getTool( lTask );
      assertEquals( "there should be 1 tool requirement", 1, lToolRequirements.length );
      assertEquals( "the tool should be assigned to the requirement", lTool2,
            lToolRequirements[0].getAssignedTool() );

   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {

      iHr = new HumanResourceDomainBuilder().withUserId( 100352 ).withUsername( USER_USERNAME ).build();

      iSupplyLocation = new LocationDomainBuilder().withCode( "YYZ" ).withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      // the user must be in a department with the supply location
      new DepartmentBuilder( "YYZ_DEP" ).withUser( iHr ).withLocation( iSupplyLocation ).build();

      iAircraft = Domain.createAircraft();

   }


   /**
    * Create an adhoc task.
    *
    * @return the task key
    */
   private TaskKey createAdhocTask() {
      return new TaskBuilder().atLocation( iSupplyLocation ).onInventory( iAircraft )
            .withLabour( RefLabourSkillKey.LBR, 1.0 ).withTaskClass( RefTaskClassKey.ADHOC )
            .build();
   }


   /**
    * Create a tool inventory.
    *
    * @param aToolDefinition
    * @param aSerialNo
    * @return the inventory key
    */
   private InventoryKey createTool( PartNoKey aToolDefinition, String aSerialNo ) {
      return new InventoryBuilder().withPartNo( aToolDefinition ).withClass( RefInvClassKey.SER )
            .atLocation( iSupplyLocation ).withSerialNo( aSerialNo )
            .withCondition( RefInvCondKey.RFI ).build();
   }


   /**
    * Create the tool checkout transfer object for the given task.
    *
    * @param aTask
    * @return the tool checkout TO
    */
   private ToolCheckoutTO createToolCheckoutTO( TaskKey aTask ) {
      ToolCheckoutTO lToolCheckoutTO = new ToolCheckoutTO();
      lToolCheckoutTO.setTask( aTask );
      lToolCheckoutTO.setUser( USER_USERNAME );

      return lToolCheckoutTO;
   }


   /**
    * Sign off on a tool requirement for a task.
    *
    * @param aTask
    * @param aToolRequirement
    */
   private void signOffToolRequirement( TaskKey aTask, Tool aToolRequirement ) {
      // sched_labour
      SchedLabourTable lSchedLabourTable = SchedLabourTable.create();
      lSchedLabourTable.setTask( aTask );
      SchedLabourKey lSchedLabourKey = lSchedLabourTable.insert();

      // sched_labour_role
      SchedLabourRoleTable lSchedLabourRole = SchedLabourRoleTable.create();
      lSchedLabourRole.setLabourRoleType( RefLabourRoleTypeKey.TECH );
      lSchedLabourRole.setSchedLabour( lSchedLabourKey );
      SchedLabourRoleKey llSchedLabourRoleKey = lSchedLabourRole.insert();

      // sched_labour_role_status
      SchedLabourRoleStatusTable lSchedLabourRoleStatus = SchedLabourRoleStatusTable.create();
      lSchedLabourRoleStatus.setSchedLabourRole( llSchedLabourRoleKey );
      lSchedLabourRoleStatus.setHr( iHr );
      lSchedLabourRoleStatus.setLabourRoleStatus( RefLabourRoleStatusKey.COMPLETE );
      lSchedLabourRoleStatus.insert();

      // sched_labour_tool
      SchedLabourToolKey lSchedLabourToolKey =
            new SchedLabourToolKey( lSchedLabourKey, aToolRequirement.getToolKey() );
      SchedLabourToolTable lSchedLabourToolTable =
            SchedLabourToolTable.create( lSchedLabourToolKey );
      lSchedLabourToolTable.insert();
   }

}
