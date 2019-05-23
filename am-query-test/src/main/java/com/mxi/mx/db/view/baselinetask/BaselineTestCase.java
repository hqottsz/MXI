
package com.mxi.mx.db.view.baselinetask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoint;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.StringUtils;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.MaintPrgmTaskKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskDefnKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.maintprgm.MaintPrgmService;
import com.mxi.mx.core.services.maintprgm.MaintProgramTO;
import com.mxi.mx.core.services.taskdefn.TaskDefnService;
import com.mxi.mx.core.services.taskdefn.transferobject.RequirementTO;
import com.mxi.mx.core.services.taskdefn.transferobject.TaskDefnRevTO;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.task.TaskTaskTable;


/**
 * The base setup for handling task approvals
 */
public abstract class BaselineTestCase {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public OperateAsUserRule iOperateAsUser = new OperateAsUserRule( 1, "user" );

   @DataPoint
   public static final InventoryKey INV_ACFT_CARRIER = new InventoryKey( 4650, 1 );

   @DataPoint
   public static final InventoryKey INV_LOOSE_COMPONENT = new InventoryKey( 4650, 2 );

   @DataPoint
   public static final InventoryKey INV_SUB_COMPONENT = new InventoryKey( 4650, 5 );

   @DataPoint
   public static final InventoryKey INV_LOOSE_SUB_COMPONENT = new InventoryKey( 4650, 3 );

   @DataPoint
   public static final InventoryKey INV_ACFT_NO_CARRIER = new InventoryKey( 4650, 4 );

   protected static final AssemblyKey ASSEMBLY = new AssemblyKey( 4650, "TEST" );
   protected static final ConfigSlotKey COMPONENT_CONFIG_SLOT = new ConfigSlotKey( ASSEMBLY, 1 );

   protected static final ConfigSlotKey SUB_COMPONENT_CONFIG_SLOT =
         new ConfigSlotKey( ASSEMBLY, 2 );
   protected static final PartNoKey SUB_COMPONENT_PART_NO = new PartNoKey( 4650, 2 );

   private static final CarrierKey OPERATOR = new CarrierKey( 4650, 1 );

   private static final HumanResourceKey AUTH_HR = new HumanResourceKey( 4650, 1 );

   private static final OrgKey ORG = new OrgKey( 4650, 1 );


   /**
    * This matcher determines if the task definition applies to the provided inventory via
    * config-slot or part-no.
    *
    * @param aInventory
    *           the inventory
    *
    * @return the matcher
    */
   protected Matcher<TaskDefinition> appliesTo( final InventoryKey aInventory ) {
      return new TypeSafeMatcher<TaskDefinition>() {

         @Override
         public void describeTo( Description aDescription ) {
            aDescription.appendText( "applies to" );
            aDescription.appendValue( aInventory );
         }


         @Override
         public boolean matchesSafely( TaskDefinition aTaskDefinition ) {
            ConfigSlotKey lConfigSlot = InvInvTable.findByPrimaryKey( aInventory ).getBOMItem();
            PartNoKey lPartNo = InvInvTable.findByPrimaryKey( aInventory ).getPartNo();

            return lConfigSlot.equals( aTaskDefinition.iConfigSlot )
                  || lPartNo.equals( aTaskDefinition.iPartNo );
         }
      };
   }


   /**
    * Asserts that Baseline Task returns nothing
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    */
   protected void assertNoEntry( InventoryKey aInventory, TaskDefinition aTaskDefn ) {
      Assert.assertEquals( "entries", 0, getBaselineTasks( aInventory, aTaskDefn ).size() );
   }


   /**
    * This matcher determines if the inventory belongs to the context operator.
    *
    * @return the matcher
    */
   protected Matcher<InventoryKey> belongsToOperator() {
      return new TypeSafeMatcher<InventoryKey>() {

         @Override
         public void describeTo( Description aDescription ) {
            aDescription.appendText( "has operator" );
            aDescription.appendValue( OPERATOR );
         }


         @Override
         public boolean matchesSafely( InventoryKey aInventory ) {
            InventoryKey lHighestInventroy = InvInvTable.findByPrimaryKey( aInventory ).getHInvNo();

            return OPERATOR
                  .equals( InvInvTable.findByPrimaryKey( lHighestInventroy ).getCarrier() );
         }
      };
   }


   /**
    * {@inheritDoc}
    *
    * @throws Exception
    */
   @Before
   public void loadTestData() {
      XmlLoader.load( iDatabaseConnectionRule.getConnection(), BaselineTestCase.class,
            BaselineTestCase.class.getSimpleName() + ".xml" );
   }


   /**
    * This determines the task that is used for <code>vw_baseline_task</code>
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @return the task revision that is approved
    */
   protected Integer taskRevisionFor( InventoryKey aInventory, TaskDefinition aTaskDefn ) {
      List<TaskTaskKey> lTaskTask = getBaselineTasks( aInventory, aTaskDefn );

      if ( lTaskTask.size() == 0 ) {

         // This can happen when the task has never been fleet-approved or operator-approved
         Assert.fail(
               "No rows were returned from vw_baseline_task. This 'disables' baseline sync." );
      } else if ( lTaskTask.size() > 1 ) {
         Assert.fail( String.format(
               "%d rows were returned from vw_baseline_task."
                     + " This should never happen; causes ORA-01422. Rows returned: <%s>",
               lTaskTask.size(), StringUtils.toDelimitedString( ">, <", lTaskTask ) ) );
      }

      if ( lTaskTask.get( 0 ) == null ) {
         return null;
      }

      return TaskTaskTable.findByPrimaryKey( lTaskTask.get( 0 ) ).getRevisionOrd();
   }


   /**
    * Obtains a list of task revisions applicable for the inventory-task definition. There could be:
    * 0 rows (unspecified), 1 row with key (specified), 1 row with null (obsolete).
    *
    * @param aInventory
    *           the inventory
    * @param aTaskDefn
    *           the task definition
    *
    * @return the list of task revisions applicable
    */
   private List<TaskTaskKey> getBaselineTasks( InventoryKey aInventory, TaskDefinition aTaskDefn ) {

      // Set the inventory context
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "filter_inv_no_db_id", aInventory.getDbId() );
      lArgs.add( "filter_inv_no_id", aInventory.getId() );

      // Get the task revision for the task defnition
      QuerySet lQuerySet =
            QuerySetFactory.getInstance().executeQueryTable( "VW_BASELINE_TASK", lArgs );

      List<TaskTaskKey> lTaskTask = new ArrayList<TaskTaskKey>( 1 );
      while ( lQuerySet.next() ) {
         TaskDefnKey lTaskDefnKey =
               lQuerySet.getKey( TaskDefnKey.class, "task_defn_db_id", "task_defn_id" );
         if ( lTaskDefnKey.equals( aTaskDefn.iTaskDefn ) ) {
            lTaskTask.add( lQuerySet.getKey( TaskTaskKey.class, "task_db_id", "task_id" ) );
         }
      }

      return lTaskTask;
   }


   /**
    * Represents a maintenance program
    */
   public static class MaintenanceProgram {

      private MaintPrgmKey iLastMaintPrgm;


      /**
       * Activates the maintenance program
       *
       * @throws Exception
       */
      public void activate() throws Exception {
         iLastMaintPrgm =
               new MaintPrgmService().activate( iLastMaintPrgm, null, null, null, AUTH_HR );
      }


      /**
       * Assigns the task definition to the maintenance program
       *
       * @param aTaskDefn
       *           the task definition
       *
       * @throws Exception
       */
      public void assign( TaskDefinition aTaskDefn ) throws Exception {
         MaintPrgmService.assignTaskDefn( iLastMaintPrgm, Arrays.asList( aTaskDefn.iTaskDefn ), "",
               null, "", AUTH_HR );
      }


      /**
       * Creates a new maintenance program
       *
       * @throws Exception
       */
      public void create() throws Exception {
         MaintProgramTO lTO = new MaintProgramTO();
         lTO.setAssembly( new AssemblyKey( 4650, "TEST" ), "assembly" );
         lTO.setCode( "MP", "code" );
         lTO.setName( "MP", "name" );
         lTO.setOperator( OPERATOR, "operator" );

         iLastMaintPrgm = MaintPrgmService.create( lTO );
      }


      /**
       * Unassigns the task definition from the maintenace program
       *
       * @param aTaskDefn
       *           the task definition
       *
       * @throws Exception
       */
      public void unassign( TaskDefinition aTaskDefn ) throws Exception {
         new MaintPrgmService().unassignReqs(
               Arrays.asList( new MaintPrgmTaskKey( iLastMaintPrgm, aTaskDefn.iTaskDefn ) ), null,
               "", AUTH_HR );
      }
   }

   /**
    * The task definition
    */
   public static class TaskDefinition {

      static int iTaskDefnCount = 0;

      final ConfigSlotKey iConfigSlot;
      PartNoKey iPartNo;
      TaskDefnKey iTaskDefn = null;
      private TaskTaskKey iLastTaskRev;


      /**
       * Creates a new {@linkplain TaskDefinition} object.
       *
       * @param aConfigSlot
       *           the configuration slot
       */
      public TaskDefinition(ConfigSlotKey aConfigSlot) {
         iPartNo = null;
         iConfigSlot = aConfigSlot;
      }


      /**
       * Creates a new {@linkplain TaskDefinition} object.
       *
       * @param aPartNo
       *           the part number
       */
      public TaskDefinition(PartNoKey aPartNo) {
         iPartNo = aPartNo;
         iConfigSlot = null;
      }


      /**
       * Activates the task definition
       *
       * @throws Exception
       */
      public void activate() throws Exception {
         new TaskDefnService().activate( iLastTaskRev, new TaskDefnRevTO(), AUTH_HR, false );
      }


      public TaskTaskKey getLatestTaskRevision() {
         return iLastTaskRev;
      }


      public TaskDefnKey getTaskDefn() {
         return iTaskDefn;
      }


      /**
       * Temporarily issues the operator
       *
       * @throws MxException
       */
      public void issueTaskTemporarily() throws MxException {
         new TaskDefnService().issueTemporaryRevision( iLastTaskRev, new CarrierKey[] { OPERATOR },
               AUTH_HR );
      }


      /**
       * Makes a task revision obsolete
       *
       * @throws Exception
       */
      public void obsolete() throws Exception {
         new TaskDefnService().obsolete( iLastTaskRev, new TaskDefnRevTO(), AUTH_HR );
      }


      /**
       * Creates a new revision
       *
       * @throws Exception
       */
      public void revise() throws Exception {
         if ( iTaskDefn == null ) {
            RequirementTO lReq = new RequirementTO();
            lReq.setTaskClass( RefTaskClassKey.REQ, "req" );
            if ( iConfigSlot != null ) {
               lReq.setAssembly( iConfigSlot.getAssemblyKey(), "assembly" );
               lReq.setBomItem( iConfigSlot );
            }

            if ( iPartNo != null ) {
               lReq.setPartNo( iPartNo, "part_no" );
            }

            iTaskDefnCount++;
            lReq.setCode( "Req" + iTaskDefnCount, "code" );
            lReq.setName( "Req" + iTaskDefnCount, "name" );
            lReq.setOrganization( ORG, "org" );
            lReq.setForecastRange( 0d, "forecast_range" );
            lReq.setOnCondition( false );

            TaskTaskKey lCreatedTask = TaskDefnService.create( lReq, AUTH_HR );
            iTaskDefn = TaskTaskTable.findByPrimaryKey( lCreatedTask ).getTaskDefn();
            iLastTaskRev = lCreatedTask;
         } else {
            TaskDefnRevTO lRevTO = new TaskDefnRevTO();
            iLastTaskRev = TaskDefnService.createRevision( iLastTaskRev, lRevTO );
         }
      }


      /**
       * Rollsback changes
       */
      public void rollback() {
         iTaskDefn = null;
         iLastTaskRev = null;
      }
   }
}
