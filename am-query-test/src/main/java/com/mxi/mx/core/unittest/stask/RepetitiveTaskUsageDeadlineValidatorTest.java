
package com.mxi.mx.core.unittest.stask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.ejb.inventory.InventoryLocal;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefEventTypeKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.fault.RepetitiveTaskUsageDeadlineException;
import com.mxi.mx.core.services.fault.RepetitiveTaskUsageDeadlineValidator;
import com.mxi.mx.core.services.inventory.phys.TestableInventoryUsage;
import com.mxi.mx.core.services.message.MxCoreParameterFactoryStub;
import com.mxi.mx.core.services.stask.deadline.UsageDeadline;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Tests {@link RepetitiveTaskUsageDeadlineValidator}
 *
 * @author gpichetto
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class RepetitiveTaskUsageDeadlineValidatorTest {

   private static final TaskKey REP_TASK = new TaskKey( 4650, 1 );
   private static final InventoryKey ASSY_INV = new InventoryKey( 4650, 2 );
   private static final EventKey TASK_EVENT = new EventKey( 4650, 1 );
   private static final InventoryKey INVALID_ASSY_INV = new InventoryKey( 4650, 3 );
   private static final String USAGE_PARM_KEY_STRING_1 = "4650:3:0:1";
   private static final String USAGE_PARM_KEY_STRING_4 = "4650:3:0:30";
   private static final String USAGE_PARM_KEY_STRING_2 = "4650:2:0:10";
   private static final String USAGE_PARM_KEY_STRING_3 = "4650:2:0:30";

   private Mockery iContext = new Mockery();
   private InventoryLocal iInventoryLocal;
   private UsageDeadline[] iUsageDeadlines;
   private RepetitiveTaskUsageDeadlineValidator iValidator;
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws Exception {
      iInventoryLocal = iContext.mock( InventoryLocal.class );

      iValidator = new RepetitiveTaskUsageDeadlineValidator( new MxCoreParameterFactoryStub(),
            QuerySetFactory.getInstance() ) {

         @Override
         protected UsageDeadline[] getUsageDeadline( TaskKey aTaskKey ) {
            return iUsageDeadlines;
         }


         @Override
         protected InventoryLocal getInventoryLocal() {
            return iInventoryLocal;
         }
      };
   }


   /**
    * When inventory does not have usage, throw no exception
    *
    * @throws Exception
    *            an error occurs
    */
   @Test
   public void testInventoryWithoutUsage() throws Exception {

      // Set up inventory usage data
      final TestableInventoryUsage[] lTestableInventoryUsage = null;

      // Set up task usage deadlines
      final UsageDeadline[] lTaksUsage = new UsageDeadline[2];

      UsageDeadline lTaskUsg1 = new UsageDeadline( DataTypeKey.HOURS, 1.00, 1.00 );
      UsageDeadline lTaskUsg2 = new UsageDeadline( DataTypeKey.CYCLES, 1.00, 1.00 );
      lTaksUsage[0] = lTaskUsg1;
      lTaksUsage[1] = lTaskUsg2;

      iUsageDeadlines = lTaksUsage;

      iContext.checking( new Expectations() {

         {
            one( iInventoryLocal ).getUsage( ASSY_INV );
            will( returnValue( lTestableInventoryUsage ) );

         }
      } );

      iValidator.validate( REP_TASK, ASSY_INV );
      iContext.assertIsSatisfied();
   }


   /**
    * When repetitve task has usage parameters that are NOT being tracked by the failed system
    * inventory, raise an exception
    *
    * @throws Exception
    *            an error occurs
    */
   @Test
   public void testRepTaskDifferentUsageDeadlineParameters() throws Exception {

      // Set up inventory usage data
      TestableInventoryUsage lTestInvUsg1 =
            new TestableInventoryUsage( new UsageParmKey( USAGE_PARM_KEY_STRING_1 ) );
      TestableInventoryUsage lTestInvUsg2 =
            new TestableInventoryUsage( new UsageParmKey( USAGE_PARM_KEY_STRING_4 ) );

      final TestableInventoryUsage[] lTestableInventoryUsage = new TestableInventoryUsage[2];
      lTestableInventoryUsage[0] = lTestInvUsg1;
      lTestableInventoryUsage[1] = lTestInvUsg2;

      // Set up task usage deadlines
      final UsageDeadline[] lTaksUsage = new UsageDeadline[2];

      UsageDeadline lTaskUsg1 = new UsageDeadline( DataTypeKey.HOURS, 1.00, 1.00 );
      UsageDeadline lTaskUsg2 = new UsageDeadline( DataTypeKey.CYCLES, 1.00, 1.00 );
      lTaksUsage[0] = lTaskUsg1;
      lTaksUsage[1] = lTaskUsg2;

      iUsageDeadlines = lTaksUsage;

      iContext.checking( new Expectations() {

         {
            one( iInventoryLocal ).getUsage( INVALID_ASSY_INV );
            will( returnValue( lTestableInventoryUsage ) );

         }
      } );

      try {
         iValidator.validate( REP_TASK, INVALID_ASSY_INV );
         fail( "RepetitiveTaskUsageDeadlineException expected exception raised" );
      } catch ( RepetitiveTaskUsageDeadlineException e ) {
         assertEquals( "[MXERR-50227] " + i18n.get( "web.err.50227", "4650:1", "4650:3" ),
               e.getMessage() );
         assertEquals( REP_TASK.toString(), e.getArgumentList().get( 0 ) );
         assertEquals( INVALID_ASSY_INV.toString(), e.getArgumentList().get( 1 ) );
      }
   }


   /**
    * When repetitve task has usage parameters that are being tracked by the failed system
    * inventory, throw no exception
    *
    * @throws Exception
    *            an error occurs
    */
   @Test
   public void testRepTaskSameUsageDeadlineParameters() throws Exception {

      // Set up inventory usage data
      TestableInventoryUsage lTestInvUsg1 =
            new TestableInventoryUsage( new UsageParmKey( USAGE_PARM_KEY_STRING_1 ) );
      TestableInventoryUsage lTestInvUsg2 =
            new TestableInventoryUsage( new UsageParmKey( USAGE_PARM_KEY_STRING_2 ) );
      TestableInventoryUsage lTestInvUsg3 =
            new TestableInventoryUsage( new UsageParmKey( USAGE_PARM_KEY_STRING_3 ) );

      final TestableInventoryUsage[] lTestableInventoryUsage = new TestableInventoryUsage[3];
      lTestableInventoryUsage[0] = lTestInvUsg1;
      lTestableInventoryUsage[1] = lTestInvUsg2;
      lTestableInventoryUsage[2] = lTestInvUsg3;

      // Set up task usage deadlines
      final UsageDeadline[] lTaksUsage = new UsageDeadline[2];

      UsageDeadline lTaskUsg1 = new UsageDeadline( DataTypeKey.HOURS, 1.00, 1.00 );
      UsageDeadline lTaskUsg2 = new UsageDeadline( DataTypeKey.CYCLES, 1.00, 1.00 );
      lTaksUsage[0] = lTaskUsg1;
      lTaksUsage[1] = lTaskUsg2;

      iUsageDeadlines = lTaksUsage;

      iContext.checking( new Expectations() {

         {
            one( iInventoryLocal ).getUsage( ASSY_INV );
            will( returnValue( lTestableInventoryUsage ) );
         }
      } );

      iValidator.validate( REP_TASK, ASSY_INV );
      iContext.assertIsSatisfied();
   }


   /**
    * When repetitve task does not have usage deadlines, throw no exception
    *
    * @throws Exception
    *            an error occurs
    */
   @Test
   public void testRepTaskWithoutUsageDeadlines() throws Exception {

      // Set up inventory usage data
      TestableInventoryUsage lTestInvUsg1 =
            new TestableInventoryUsage( new UsageParmKey( USAGE_PARM_KEY_STRING_1 ) );
      TestableInventoryUsage lTestInvUsg2 =
            new TestableInventoryUsage( new UsageParmKey( USAGE_PARM_KEY_STRING_2 ) );

      final TestableInventoryUsage[] lTestableInventoryUsage = new TestableInventoryUsage[2];
      lTestableInventoryUsage[0] = lTestInvUsg1;
      lTestableInventoryUsage[1] = lTestInvUsg2;

      iUsageDeadlines = null;

      iContext.checking( new Expectations() {

         {
            one( iInventoryLocal ).getUsage( ASSY_INV );
            will( returnValue( lTestableInventoryUsage ) );

         }
      } );

      iValidator.validate( REP_TASK, ASSY_INV );
      iContext.assertIsSatisfied();
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void loadData() throws Exception {
      InvInvTable lInventory = InvInvTable.create( ASSY_INV );
      lInventory.setInvClass( RefInvClassKey.ASSY );
      lInventory.insert();

      InvInvTable lInvalidInv = InvInvTable.create( INVALID_ASSY_INV );
      lInvalidInv.setInvClass( RefInvClassKey.ASSY );
      lInvalidInv.insert();

      SchedStaskTable lStask = SchedStaskTable.create( REP_TASK );
      lStask.setMainInventory( ASSY_INV );
      lStask.setTaskClass( RefTaskClassKey.ADHOC );
      lStask.insert();

      EvtEventTable lEvtEventTable = EvtEventTable.create( TASK_EVENT );
      lEvtEventTable.setEventType( RefEventTypeKey.TS );
      lEvtEventTable.setStatus( RefEventStatusKey.ACTV );
      lEvtEventTable.insert();
   }
}
