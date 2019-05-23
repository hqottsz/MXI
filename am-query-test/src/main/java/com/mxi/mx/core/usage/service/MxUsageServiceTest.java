
package com.mxi.mx.core.usage.service;

import static com.mxi.mx.testing.matchers.MxMatchers.listContainingOnly;
import static com.mxi.mx.testing.matchers.MxMatchers.usageDeltaOf;
import static com.mxi.mx.testing.matchers.MxMatchers.within;
import static org.hamcrest.Matchers.nullValue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.ibm.icu.util.Calendar;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.UsageParmKey;
import com.mxi.mx.core.services.stask.deadline.updatedeadline.UpdateDeadlineService;
import com.mxi.mx.core.unittest.table.inv.InvCurrUsage;
import com.mxi.mx.core.usage.model.UsageType;


/**
 * This class tests the MxUsageService.
 *
 * @author dsewell
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class MxUsageServiceTest {

   private static final int SECONDS = Calendar.SECOND;

   private static final EventKey WORK_PACKAGE = new EventKey( 4650, 1001 );

   private static final InventoryKey ENGINE_SYSTEM = new InventoryKey( 4650, 2001 );

   private static final InventoryKey ENGINE = new InventoryKey( 4650, 2002 );

   private static final InventoryKey ACFT_SYSTEM = new InventoryKey( 4650, 2009 );

   private static final InventoryKey TRK = new InventoryKey( 4650, 2008 );

   private static final InventoryKey APU_SYSTEM = new InventoryKey( 4650, 2005 );

   private static final InventoryKey APU = new InventoryKey( 4650, 2006 );

   private static final HumanResourceKey HR = new HumanResourceKey( 4650, 999 );

   private UsageAccrualService iAccrueUsageService;

   private Mockery iContext = new Mockery();

   private UsageService iService;

   private UpdateDeadlineService iUpdateDeadlineService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   enum Record {
      EXISTS( true ), DOESNOT_EXIST( false );

      private boolean iState;


      Record(boolean aState) {
         this.iState = aState;
      }


      boolean getState() {
         return iState;
      }
   }


   /**
    * Tests that when we adjust the usage for an installed assembly that tracks multiple usage
    * parms, the usage accrual service is called with the correct usage deltas and only once. Also
    * ensures an alert is sent out.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAdjustUsageForInstallationOfAssemblyWithMultipleUsageParms() throws Exception {

      iContext.checking( new Expectations() {

         {
            one( iAccrueUsageService ).accrueUsage( with( equal( ENGINE ) ), with( equal( HR ) ),
                  with( nullValue( String.class ) ), with( within( 10, SECONDS ) ),
                  with( nullValue( String.class ) ), with( nullValue( String.class ) ),
                  with( listContainingOnly( usageDeltaOf( equal( 50.0 ), DataTypeKey.CYCLES ),
                        usageDeltaOf( equal( 10.0 ), DataTypeKey.HOURS ) ) ),
                  with( equal( UsageType.MXACCRUAL ) ), with( nullValue( String.class ) ) );

            one( iUpdateDeadlineService ).updateDeadlinesForInvTree( ENGINE );
         }
      } );

      iService.adjustUsageForInstallation( WORK_PACKAGE, ENGINE_SYSTEM, ENGINE, HR );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests when the check is not found against any of the inventory in the "install on" inventory's
    * ancestor hierarchy, that a usage record is neither created nor updated.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAdjustUsageForInstallationWhenCheckNotFoundAgainstInvHierarchy()
         throws Exception {
      iContext.checking( new Expectations() {

         {
            never( iAccrueUsageService );
         }
      } );

      iService.adjustUsageForInstallation( WORK_PACKAGE, APU_SYSTEM, APU, HR );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that when we adjust the usage for a removed assembly that tracks multiple usage parms,
    * the usage accrual service is called with the correct usage deltas and only once. Also ensures
    * an alert is sent out.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAdjustUsageForRemovalOfAssemblyWithMultipleUsageParms() throws Exception {
      iContext.checking( new Expectations() {

         {
            one( iAccrueUsageService ).accrueUsage( with( equal( ENGINE ) ), with( equal( HR ) ),
                  with( nullValue( String.class ) ), with( within( 10, SECONDS ) ),
                  with( nullValue( String.class ) ), with( nullValue( String.class ) ),
                  with( listContainingOnly( usageDeltaOf( equal( -50.0 ), DataTypeKey.CYCLES ),
                        usageDeltaOf( equal( -10.0 ), DataTypeKey.HOURS ) ) ),
                  with( equal( UsageType.MXACCRUAL ) ), with( nullValue( String.class ) ) );
         }
      } );

      iService.adjustUsageForRemoval( WORK_PACKAGE, ENGINE_SYSTEM, ENGINE, HR );

      iContext.assertIsSatisfied();
   }


   /**
    * Tests that when we adjust the usage for a removed tracked item that tracks multiple usage
    * parms, the usage accrual service is called with the correct usage deltas and only once.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testAdjustUsageForRemovalOfTrackedAssetWithMultipleUsageParms() throws Exception {
      iService.adjustUsageForRemoval( WORK_PACKAGE, ACFT_SYSTEM, TRK, HR );

      InvCurrUsage lInvCurrUsageCycles =
            new InvCurrUsage( new UsageParmKey( TRK, DataTypeKey.CYCLES ) );
      lInvCurrUsageCycles.assertTsnQt( 50.0 ); // 100 - 50 (from ACFT)

      InvCurrUsage lInvCurrUsageHours =
            new InvCurrUsage( new UsageParmKey( TRK, DataTypeKey.HOURS ) );
      lInvCurrUsageHours.assertTsnQt( 65.0 ); // 75 - 10 (from ACFT)
   }


   /**
    * Tests when the check is not found against any of the inventory in the "install on" inventory's
    * ancestor hierarchy, that a usage record is neither created nor updated.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Test
   public void testadjustUsageForRemovalWhenCheckNotFoundAgainstInvHierarchy() throws Exception {
      iContext.checking( new Expectations() {

         {
            never( iAccrueUsageService );
         }
      } );

      iService.adjustUsageForRemoval( WORK_PACKAGE, APU_SYSTEM, APU, HR );

      iContext.assertIsSatisfied();
   }


   /**
    * Sets up the test case. Creates a mock usage accrual service, mock alert engine and a work item
    * generator stub. Creates the service to be tested.
    *
    * @throws Exception
    *            If an error occurs.
    */
   @Before
   public void setUp() throws Exception {
      iAccrueUsageService = iContext.mock( UsageAccrualService.class );
      iUpdateDeadlineService = iContext.mock( UpdateDeadlineService.class );
      iService = new UsageService( null, null, null, null, iAccrueUsageService, null,
            iUpdateDeadlineService );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }

}
