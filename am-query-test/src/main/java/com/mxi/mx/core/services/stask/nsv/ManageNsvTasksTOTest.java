
package com.mxi.mx.core.services.stask.nsv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MandatoryArgumentException;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.nsv.ManageNsvTasksTO.Mode;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.InvInvTable;
import com.mxi.mx.core.table.sched.SchedStaskTable;
import com.mxi.mx.core.utils.MxCoreError;
import com.mxi.mx.core.utils.ValidationResults;


/**
 * Tests the ManageNsvTasksTO class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class ManageNsvTasksTOTest {

   private static final TaskKey ACFT_WP_KEY = new TaskKey( 1, 1 );
   private static final String NOTE = "NOTE";
   private static final String REASON = "REASON";
   private static final HumanResourceKey HR_KEY = new HumanResourceKey( 1, 1 );
   private static final InventoryKey ACFT_INV = new InventoryKey( 1, 1 );
   private static final TaskKey COMP_WP_KEY = new TaskKey( 2, 2 );
   private static final InventoryKey TRK_INV = new InventoryKey( 2, 2 );
   private static final String ERR_MSG_MODE = "core.lbl.MODE";
   private static final String ERR_MSG_HR_KEY = "core.lbl.HUMAN_RESOURCE_KEY";
   private static final String ERR_MSG_COMP_WP_KEY = "core.lbl.COMPONENT_WORK_PACKAGE";
   private static final String ERR_MSG_32443 = "core.err.32443";
   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that the validation of a valid TO produces no errors.
    */
   @Test
   public void testSuccessfulValidation() {
      ManageNsvTasksTO lTO = getEnforceTO();
      ValidationResults lResults = lTO.validate();
      assertFalse( lResults.hasErrors() );
   }


   /**
    * Verify that the validation results contain a
    * CannotEnforceNsvTasksOnAircraftWorkpackageException if the component work package key is
    * invalid (i.e. an aircraft work package key) and the mode is ENFORCE_NSV_TASKS.
    */
   @Test
   public void testValidateWithInvalidCompWpKey() {
      ManageNsvTasksTO lTO = getEnforceTO();
      lTO.setCompWorkpackageKey( ACFT_WP_KEY );

      ValidationResults lResults = lTO.validate();

      assertTrue( lResults.hasErrors() );

      List<MxCoreError> lErrors = lResults.getErrors();
      assertEquals( 1, lErrors.size() );
      assertTrue( lErrors.get( 0 )
            .getException() instanceof CannotEnforceNsvTasksOnAircraftWorkpackageException );
      assertEquals( "[MXERR-32443] " + i18n.get( ERR_MSG_32443, "" ),
            lErrors.get( 0 ).getMessage() );
   }


   /**
    * Verify that the validation results contain a MandatoryArgumentException if the check key is
    * null.
    */
   @Test
   public void testValidateWithNullCheckKey() {
      ManageNsvTasksTO lTO = getEnforceTO();
      lTO.setCompWorkpackageKey( null );

      ValidationResults lResults = lTO.validate();

      assertTrue( lResults.hasErrors() );

      List<MxCoreError> lErrors = lResults.getErrors();
      assertEquals( 1, lErrors.size() );
      assertContainsMandatoryArgumentExceptions( lErrors, i18n.get( ERR_MSG_COMP_WP_KEY ) );
   }


   /**
    * Verify that the validation results contain a MandatoryArgumentException if the mode is null.
    */
   @Test
   public void testValidateWithNullMode() {
      ManageNsvTasksTO lTO = getNonModeTO();

      ValidationResults lResults = lTO.validate();

      assertTrue( lResults.hasErrors() );

      List<MxCoreError> lErrors = lResults.getErrors();
      assertEquals( 1, lErrors.size() );
      assertContainsMandatoryArgumentExceptions( lErrors, i18n.get( ERR_MSG_MODE ) );
   }


   /**
    * Verify that the validation results contain all MandatoryArgumentExceptions if the mode, HR
    * key, and check key are all null.
    */
   @Test
   public void testValidateWithNullModeAndUserHrkeyAndCheckKey() {
      ManageNsvTasksTO lTO = getNonModeTO();
      lTO.setUserHrKey( null );
      lTO.setCompWorkpackageKey( null );

      ValidationResults lResults = lTO.validate();

      assertTrue( lResults.hasErrors() );

      List<MxCoreError> lErrors = lResults.getErrors();
      assertEquals( 3, lErrors.size() );
      assertContainsMandatoryArgumentExceptions( lErrors, i18n.get( ERR_MSG_MODE ) );
      assertContainsMandatoryArgumentExceptions( lErrors, i18n.get( ERR_MSG_HR_KEY ) );
      assertContainsMandatoryArgumentExceptions( lErrors, i18n.get( ERR_MSG_COMP_WP_KEY ) );
   }


   /**
    * Verify that the validation results contain a MandatoryArgumentException if the HR key is null.
    */
   @Test
   public void testValidateWithNullUserHrKey() {
      ManageNsvTasksTO lTO = getEnforceTO();
      lTO.setUserHrKey( null );

      ValidationResults lResults = lTO.validate();

      assertTrue( lResults.hasErrors() );

      List<MxCoreError> lErrors = lResults.getErrors();
      assertEquals( 1, lErrors.size() );
      assertContainsMandatoryArgumentExceptions( lErrors, i18n.get( ERR_MSG_HR_KEY ) );
   }


   @Before
   public void loadData() throws Exception {
      // setup the appropriate DB tables
      SchedStaskTable lSchedStaskTable = SchedStaskTable.create( COMP_WP_KEY );
      lSchedStaskTable.setMainInventory( TRK_INV );
      lSchedStaskTable.insert();

      InvInvTable lInvInvTable = InvInvTable.create( TRK_INV );
      lInvInvTable.setInvClass( RefInvClassKey.TRK );
      lInvInvTable.insert();

      lSchedStaskTable = SchedStaskTable.create( ACFT_WP_KEY );
      lSchedStaskTable.setMainInventory( ACFT_INV );
      lSchedStaskTable.insert();

      EvtEventTable lEvtEventTable = EvtEventTable.create( ACFT_WP_KEY.getEventKey() );
      lEvtEventTable.insert();

      lInvInvTable = InvInvTable.create( ACFT_INV );
      lInvInvTable.setInvClass( RefInvClassKey.ACFT );
      lInvInvTable.insert();
   }


   /**
    * Asserts that the list of errors contains a MandatoryArgumentException with the provided error
    * message
    *
    * @param aErrors
    * @param aErrorMsg
    */
   private void assertContainsMandatoryArgumentExceptions( List<MxCoreError> aErrors,
         String aErrorMsg ) {
      for ( MxCoreError lError : aErrors ) {
         if ( ( lError.getException() instanceof MandatoryArgumentException )
               && ( lError.getMessage().indexOf( aErrorMsg ) > 0 ) ) {
            return;
         }
      }

      fail();
   }


   private ManageNsvTasksTO getEnforceTO() {
      ManageNsvTasksTO lTO = getNonModeTO();
      lTO.setMode( Mode.ENFORCE_NSV_TASKS );

      return lTO;
   }


   private ManageNsvTasksTO getNonModeTO() {
      ManageNsvTasksTO lTO = new ManageNsvTasksTO();
      lTO.setCompWorkpackageKey( COMP_WP_KEY );
      lTO.setNote( NOTE );
      lTO.setReason( REASON );
      lTO.setUserHrKey( HR_KEY );

      return lTO;
   }
}
