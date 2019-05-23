
package com.mxi.mx.core.unittest.po;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.RefPoAuthLvlStatusKey;
import com.mxi.mx.core.services.order.exception.InvalidBudgetCheckStatusException;
import com.mxi.mx.core.services.order.exception.InvalidBudgetCheckStatusValidator;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * This class tests the InvalidBudgetCheckStatusValidatorTest method.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class InvalidBudgetCheckStatusValidatorTest {

   private static final PurchaseOrderKey BUDGET_BLKOUT = new PurchaseOrderKey( 1, 1 );

   private static final PurchaseOrderKey BUDGET_PENDING = new PurchaseOrderKey( 1, 2 );

   private static final PurchaseOrderKey BUDGET_APPROVED = new PurchaseOrderKey( 1, 3 );

   private static final PurchaseOrderKey BUDGET_REJECTED = new PurchaseOrderKey( 1, 4 );

   private static final PurchaseOrderKey BUDGET_BYPASSED = new PurchaseOrderKey( 1, 5 );

   private static final PurchaseOrderKey BUDGET_OVERRIDDEN = new PurchaseOrderKey( 1, 6 );

   private static final String ASSERT_FAIL_MESSAGE = "Expected InvalidBudgetCheckStatusException";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   @Before
   public void loadData() throws Exception {
      PoTestHelper.insertPoHeader( BUDGET_BLKOUT, RefPoAuthLvlStatusKey.BLKOUT );
      PoTestHelper.insertPoHeader( BUDGET_PENDING, RefPoAuthLvlStatusKey.PENDING );
      PoTestHelper.insertPoHeader( BUDGET_APPROVED, RefPoAuthLvlStatusKey.APPROVED );
      PoTestHelper.insertPoHeader( BUDGET_REJECTED, RefPoAuthLvlStatusKey.REJECTED );
      PoTestHelper.insertPoHeader( BUDGET_BYPASSED, RefPoAuthLvlStatusKey.BYPASSED );
      PoTestHelper.insertPoHeader( BUDGET_OVERRIDDEN, RefPoAuthLvlStatusKey.OVERRIDDEN );
   }


   /**
    * Tests the validate method throw an exception when approving a budget that has an invalid
    * budget check status
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testValidateApproveBudget() throws Exception {
      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_BLKOUT ).validateApproveBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32407] " + i18n.get( "core.err.32407" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_PENDING ).validateApproveBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32407] " + i18n.get( "core.err.32407" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_APPROVED ).validateApproveBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32408] " + i18n.get( "core.err.32408" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_REJECTED ).validateApproveBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32409] " + i18n.get( "core.err.32409" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_BYPASSED ).validateApproveBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32410] " + i18n.get( "core.err.32410" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_OVERRIDDEN ).validateApproveBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32411] " + i18n.get( "core.err.32411" ), lException.getMessage() );
      }
   }


   /**
    * Tests the validate method throw an exception when overriding budget rejection that has an
    * invalid budget check status
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testValidateOverrideBudgetRejection() throws Exception {
      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_BLKOUT ).validateOverrideBudgetRejection();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32428] " + i18n.get( "core.err.32428" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_PENDING ).validateOverrideBudgetRejection();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32428] " + i18n.get( "core.err.32428" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_APPROVED ).validateOverrideBudgetRejection();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32429] " + i18n.get( "core.err.32429" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_BYPASSED ).validateOverrideBudgetRejection();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32430] " + i18n.get( "core.err.32430" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_OVERRIDDEN )
               .validateOverrideBudgetRejection();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32431] " + i18n.get( "core.err.32431" ), lException.getMessage() );
      }
   }


   /**
    * Tests the validate method throw an exception when approving a budget that has an invalid
    * budget check status
    *
    * @throws Exception
    *            if an error occurs
    */
   @Test
   public void testValidateRejectBudget() throws Exception {
      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_BLKOUT ).validateRejectBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32415] " + i18n.get( "core.err.32415" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_PENDING ).validateRejectBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32415] " + i18n.get( "core.err.32415" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_APPROVED ).validateRejectBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32416] " + i18n.get( "core.err.32416" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_REJECTED ).validateRejectBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32417] " + i18n.get( "core.err.32417" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_BYPASSED ).validateRejectBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32418] " + i18n.get( "core.err.32418" ), lException.getMessage() );
      }

      try {
         new InvalidBudgetCheckStatusValidator( BUDGET_OVERRIDDEN ).validateRejectBudget();

         // If this line executes, the exception wasn't thrown
         MxAssert.fail( ASSERT_FAIL_MESSAGE );
      } catch ( InvalidBudgetCheckStatusException lException ) {

         assertEquals( "[MXERR-32419] " + i18n.get( "core.err.32419" ), lException.getMessage() );
      }
   }
}
