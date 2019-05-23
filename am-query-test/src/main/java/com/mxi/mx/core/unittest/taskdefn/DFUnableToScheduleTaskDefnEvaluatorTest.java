
package com.mxi.mx.core.unittest.taskdefn;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.QueryAccessObject;
import com.mxi.mx.core.alert.taskdefn.UnableToScheduleTaskDefnEvaluator;
import com.mxi.mx.core.key.RefDomainTypeKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Make sure that {@link UnableToScheduleTaskDefnEvaluator} follows the logic:
 *
 * <p>
 * Returns false when:
 *
 * <ul>
 * <li>Not initialized against a config slot that is TRK</li>
 * <li>Not initialized against a part that is TRK</li>
 * <li>Has no usage-based rules</li>
 * <li>Or, Is not scheduled from effective date or received date</li>
 * </ul>
 * </p>
 *
 * <p>
 * Returns true on the first revision when:
 *
 * <ul>
 * <li>Is Scheduled From Effective Date or Received Date</li>
 * <li>Has usage-based rules</li>
 * <li>And, is initialized against a TRK config part or TRK part</li>
 * </ul>
 * </p>
 *
 * <p>
 * Returns true on subsequent revisions when any:
 *
 * <ul>
 * <li>Scheduled From changed</li>
 * <li>Scheduled Rules changed</li>
 * <li>Or, Applicability Rules changed</li>
 * </ul>
 * </p>
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class DFUnableToScheduleTaskDefnEvaluatorTest {

   private final Mockery iContext = new Mockery();

   private final QueryAccessObject iQao = iContext.mock( QueryAccessObject.class );

   private final UnableToScheduleTaskDefnEvaluator iEvaluator =
         new UnableToScheduleTaskDefnEvaluator( iQao );

   private final QuerySet iQuerySet = iContext.mock( QuerySet.class );

   private final TaskTaskKey iTaskTaskKey_ApplicabilityChanged = new TaskTaskKey( 4650, 52 );

   private final TaskTaskKey iTaskTaskKey_ConfigNotTrk = new TaskTaskKey( 4650, 3 );

   private final TaskTaskKey iTaskTaskKey_FirstRev = new TaskTaskKey( 4650, 1 );

   private final TaskTaskKey iTaskTaskKey_NoChange = new TaskTaskKey( 4650, 102 );

   private final TaskTaskKey iTaskTaskKey_PartBased = new TaskTaskKey( 4650, 2 );

   private final TaskTaskKey iTaskTaskKey_ScheduleFromChanged_EffectiveDate2ReceivedDate =
         new TaskTaskKey( 4650, 92 );

   private final TaskTaskKey iTaskTaskKey_ScheduleFromChanged_ReceivedDate2EffectiveDate =
         new TaskTaskKey( 4650, 82 );

   private final TaskTaskKey iTaskTaskKey_ScheduleFromChanged_UnScheduled2EffectiveDate =
         new TaskTaskKey( 4650, 62 );

   private final TaskTaskKey iTaskTaskKey_ScheduleFromChanged_UnScheduled2ReceivedDate =
         new TaskTaskKey( 4650, 72 );

   private final TaskTaskKey iTaskTaskKey_ScheduleFromLatest_Checked2Unchecked =
         new TaskTaskKey( 4650, 112 );

   private final TaskTaskKey iTaskTaskKey_ScheduleFromLatest_Unchecked2Checked =
         new TaskTaskKey( 4650, 122 );

   private final TaskTaskKey iTaskTaskKey_UnScheduled = new TaskTaskKey( 4650, 4 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();


   /**
    * Ensures alert is sent when the applicability changed on subsequent revisions
    */
   @Test
   public void testApplicabilityChanged() {
      isTaskBasedOnUsage( true );

      assertTrue( iEvaluator.evaluate( iTaskTaskKey_ApplicabilityChanged ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is not sent when based on non-TRK config slot
    */
   @Test
   public void testConfigBased_NotTrk() {
      assertFalse( iEvaluator.evaluate( iTaskTaskKey_ConfigNotTrk ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is sent when based on TRK config slot
    */
   @Test
   public void testConfigBased_Trk() {
      isTaskBasedOnUsage( true );

      assertTrue( iEvaluator.evaluate( iTaskTaskKey_FirstRev ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is not sent when not usage-based
    */
   @Test
   public void testNoUsageBasedSchedulingRules() {
      isTaskBasedOnUsage( false );

      assertFalse( iEvaluator.evaluate( iTaskTaskKey_NoChange ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is not sent when not based on TRK part
    */
   @Test
   public void testPartBased_NotTrk() {
      isPartTracked( false );

      assertFalse( iEvaluator.evaluate( iTaskTaskKey_PartBased ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is sent when based on TRK part
    */
   @Test
   public void testPartBased_Trk() {
      isPartTracked( true );
      isTaskBasedOnUsage( true );

      assertTrue( iEvaluator.evaluate( iTaskTaskKey_PartBased ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is sent when change in scheduling rules
    */
   @Test
   public void testScheduledRulesChanged() {
      isTaskBasedOnUsage( true );
      hasSchedulingDifference( RefDomainTypeKey.USAGE_PARM );

      assertTrue( iEvaluator.evaluate( iTaskTaskKey_NoChange ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is not sent when change in 'scheduling from' field from Effective Date to
    * Received Date
    */
   @Test
   public void testScheduleFromChanged_EffectiveDate2ReceivedDate() {

      assertFalse(
            iEvaluator.evaluate( iTaskTaskKey_ScheduleFromChanged_EffectiveDate2ReceivedDate ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is sent when change in scheduling from field
    */
   @Test
   public void testScheduleFromChanged_ReceivedDate2EffectiveDate() {
      isTaskBasedOnUsage( true );

      assertTrue(
            iEvaluator.evaluate( iTaskTaskKey_ScheduleFromChanged_ReceivedDate2EffectiveDate ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is sent when change in scheduling from field
    */
   @Test
   public void testScheduleFromChanged_Unscheduled2EffectiveDate() {
      isTaskBasedOnUsage( true );

      assertTrue(
            iEvaluator.evaluate( iTaskTaskKey_ScheduleFromChanged_UnScheduled2EffectiveDate ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is not sent when change in 'scheduling from' field from Unscheduled to
    * Received Date
    */
   @Test
   public void testScheduleFromChanged_Unscheduled2ReceivedDate() {

      assertFalse(
            iEvaluator.evaluate( iTaskTaskKey_ScheduleFromChanged_UnScheduled2ReceivedDate ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures alert is sent when the applicability changed on subsequent revisions
    */
   @Test
   public void testScheduleFromLatest_Checked2Unchecked() {
      isTaskBasedOnUsage( true );

      assertTrue( iEvaluator.evaluate( iTaskTaskKey_ScheduleFromLatest_Checked2Unchecked ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures alert is sent when the applicability changed on subsequent revisions
    */
   @Test
   public void testScheduleFromLatest_Unchecked2Checked() {
      isTaskBasedOnUsage( true );

      assertTrue( iEvaluator.evaluate( iTaskTaskKey_ScheduleFromLatest_Unchecked2Checked ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Ensures an alert is not sent when task defn is not scheduled from effective date or received
    * date
    */
   @Test
   public void testUnscheduled() {
      assertFalse( iEvaluator.evaluate( iTaskTaskKey_UnScheduled ) );

      iContext.assertIsSatisfied();
   }


   /**
    * Sets up mock expectations for scheduling rule differences
    *
    * @param aDomainTypeKeys
    *           list of scheduling rules
    */
   private void hasSchedulingDifference( final RefDomainTypeKey... aDomainTypeKeys ) {

      iContext.checking( new Expectations() {

         {
            if ( aDomainTypeKeys.length == 0 ) {
               one( iQuerySet ).next();
               will( returnValue( false ) );
            } else {
               boolean lUsageFound = false;
               for ( int i = 0; i < aDomainTypeKeys.length; i++ ) {
                  if ( RefDomainTypeKey.USAGE_PARM.equals( aDomainTypeKeys[i] ) ) {
                     lUsageFound = true;

                     break;
                  }
               }

               if ( !lUsageFound ) {
                  one( iQuerySet ).next();
                  will( returnValue( false ) );
               }
            }
         }
      } );
   }


   /**
    * Sets up mock expectations for parts
    *
    * @param aIsPartTrk
    *           TRUE if the part is tracked
    */
   private void isPartTracked( final boolean aIsPartTrk ) {
      iContext.checking( new Expectations() {

         {
            one( iQao ).executeQuery(
                  with( same( "com.mxi.mx.core.query.taskdefn.GetPartNoWithInvClassForTask" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( iQuerySet ) );
            one( iQuerySet ).hasNext();
            will( returnValue( aIsPartTrk ) );
         }
      } );
   }


   /**
    * Sets up mock expectations for task usage
    *
    * @param aIsTaskUsageBased
    *           TRUE if task is usage-based
    */
   private void isTaskBasedOnUsage( final boolean aIsTaskUsageBased ) {
      iContext.checking( new Expectations() {

         {
            one( iQao ).executeQuery(
                  with( same( "com.mxi.mx.core.query.taskdefn.IsTaskUsageBased" ) ),
                  with( any( DataSetArgument.class ) ) );
            will( returnValue( iQuerySet ) );
            one( iQuerySet ).hasNext();
            will( returnValue( aIsTaskUsageBased ) );
         }
      } );
   }


   @Before
   public void loadData() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }
}
