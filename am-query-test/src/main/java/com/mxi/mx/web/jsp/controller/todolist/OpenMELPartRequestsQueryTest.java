package com.mxi.mx.web.jsp.controller.todolist;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OrganizationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.domain.builder.TaskDeadlineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.unittest.MxAssert;


public class OpenMELPartRequestsQueryTest {

   private static final String REQUEST_KEY = "request_key";
   private static final Integer ANY_AIRCRAFT_GROUP = null;
   private static final Integer AIRCRAFT_GROUP_1 = 1;
   private static final Integer AIRCRAFT_GROUP_2 = 2;
   private static final String ANY_PR_STATUS = null;
   private static final Integer NO_DATE_SET = null;
   private static final Integer DATE_SET_TO_TODAY = 0;
   private static final Integer LARGE_POSITIVE_NUMBER = 100;
   private static final Integer SMALL_POSITIVE_NUMBER = 1;
   private static final Integer LARGE_NEGATIVE_NUMBER = -100;
   private static final Integer SMALL_NEGATIVE_NUMBER = -1;
   private static final boolean SHOW_ALL_PART_REQUESTS = true;

   // Current date, since calendar retains days and has to be cleared we add and substract instead
   // of setting new dates for everything
   private static final Calendar CALENDAR = Calendar.getInstance();
   private static final Date TODAY = CALENDAR.getTime();
   private static final Date PAST_DATE = DateUtils.addDays( TODAY, -40 );
   private static final Date FUTURE_DATE = DateUtils.addDays( TODAY, 80 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private OpenMELPartRequestsQuery iQuery = new OpenMELPartRequestsQuery();
   private PartRequestKey iFutureDatePR;
   private PartRequestKey iPastDatePR;
   private PartRequestKey iTodaysDatePR;
   private HumanResourceKey iHr;
   private HumanResourceKey iHrUserB;
   private PartGroupKey iPartGroup;


   /*
    * Assert that the data set contains a variable-length list of part requests, where the list is
    * provided in key order, and only those PRs
    */
   private void assertContainsInOrderOnly( DataSet aDataSet, PartRequestKey... aPartRequests ) {

      aDataSet.addSort( "dsDate( NEEDED_BY_DT )", true );
      aDataSet.filterAndSort();
      aDataSet.beforeFirst();

      for ( PartRequestKey aPartRequest : aPartRequests ) {
         aDataSet.next();
         MxAssert.assertEquals( REQUEST_KEY, aPartRequest,
               aDataSet.getKey( PartRequestKey.class, REQUEST_KEY ) );
      }

      Assert.assertFalse( "Too many rows or To Date is not filtered", aDataSet.next() );
   }


   @Test
   public void testShowAllPartRequestsWhenNoFilterIsProvided() {

      // Act
      DataSet lDataSet = iQuery.execute( iHr, ANY_AIRCRAFT_GROUP, ANY_PR_STATUS, NO_DATE_SET,
            NO_DATE_SET, SHOW_ALL_PART_REQUESTS );

      // Assert
      assertContainsInOrderOnly( lDataSet, iPastDatePR, iTodaysDatePR, iFutureDatePR );
   }


   @Test
   public void
         testAircraftGroupFilterWhenPartRequestsNotAssociatedWithTheSelectedAircraftGroupThenNothingReturned() {

      // Act
      DataSet lDataSet = iQuery.execute( iHr, AIRCRAFT_GROUP_2, ANY_PR_STATUS, NO_DATE_SET,
            NO_DATE_SET, SHOW_ALL_PART_REQUESTS );

      // Assert
      Assert.assertEquals( 0, lDataSet.getRowCount() );
   }


   @Test
   public void
         testAircraftGroupFilterWhenPartRequestAssociatedWithTheSelectedAircraftGroupThenThatPartRequestReturned() {
      // Act
      DataSet lDataSet = iQuery.execute( iHr, AIRCRAFT_GROUP_1, ANY_PR_STATUS, NO_DATE_SET,
            NO_DATE_SET, SHOW_ALL_PART_REQUESTS );

      // Assert
      assertContainsInOrderOnly( lDataSet, iFutureDatePR );
   }


   @Test
   /**
    * <pre>
    * Given To Date is equal to or after the part request
    *  and From Date is equal to or before the part request
    *
    * When the needed by date is in the FUTURE
    *
    * Then the part request should be displayed.
    * </pre>
    */
   public void testNeededByDaysFilterWhenDateIsInTheFuture() {
      // Act
      DataSet lDataSet = iQuery.execute( iHr, AIRCRAFT_GROUP_1, ANY_PR_STATUS,
            SMALL_POSITIVE_NUMBER, LARGE_POSITIVE_NUMBER, SHOW_ALL_PART_REQUESTS );

      // Assert
      assertContainsInOrderOnly( lDataSet, iFutureDatePR );
   }


   @Test
   /**
    * <pre>
    * Given To Date is equal to or after the part request
    *  and From Date is equal to or before the part request
    *
    * When the needed by date is in the PAST
    *
    * Then the part request should be displayed.
    * </pre>
    */
   public void testNeededByDaysFilterWhenDateIsInThePast() {
      // Act
      DataSet lDataSet = iQuery.execute( iHr, ANY_AIRCRAFT_GROUP, ANY_PR_STATUS,
            LARGE_NEGATIVE_NUMBER, SMALL_NEGATIVE_NUMBER, SHOW_ALL_PART_REQUESTS );

      // Assert
      assertContainsInOrderOnly( lDataSet, iPastDatePR );
   }


   @Test
   /**
    * <pre>
    * Given To Date is equal to or after the part request
    *  and From Date is equal to or before the part request
    *
    * When the needed by date is TODAY
    *
    * Then the part request should be displayed.
    * </pre>
    */
   public void testNeededByDaysFilterWhenDateIsToday() {
      // Act
      DataSet lDataSet = iQuery.execute( iHr, ANY_AIRCRAFT_GROUP, ANY_PR_STATUS, DATE_SET_TO_TODAY,
            DATE_SET_TO_TODAY, SHOW_ALL_PART_REQUESTS );

      // Assert
      assertContainsInOrderOnly( lDataSet, iTodaysDatePR );

   }


   @Test
   /**
    * <pre>
    * When To Date is provided
    *  and From Date is not provided
    * Then any (open MEL deferred) part requests after the To Date should not be displayed.
    * </pre>
    */
   public void testNeededByDaysFilterWhenFromDateIsNotProvided() {
      // Act
      DataSet lDataSet = iQuery.execute( iHr, ANY_AIRCRAFT_GROUP, ANY_PR_STATUS, NO_DATE_SET,
            DATE_SET_TO_TODAY, SHOW_ALL_PART_REQUESTS );

      // Assert
      assertContainsInOrderOnly( lDataSet, iPastDatePR, iTodaysDatePR );
   }


   @Test
   /**
    * <pre>
    * When To Date is not provided
    *  and From Date is provided
    * Then any (open MEL deferred) part requests before the From Date should not be displayed
    * </pre>
    */
   public void testNeededByDaysFilterWhenToDateIsNotProvided() {
      // Act
      DataSet lDataSet = iQuery.execute( iHr, ANY_AIRCRAFT_GROUP, ANY_PR_STATUS, DATE_SET_TO_TODAY,
            NO_DATE_SET, SHOW_ALL_PART_REQUESTS );

      // Assert
      assertContainsInOrderOnly( lDataSet, iTodaysDatePR, iFutureDatePR );
   }


   @Test
   /**
    *
    * Show Assigned To Me And Unassigned Part Requests when SHOW_ALL_PART_REQUESTS filter is false
    *
    */
   public void testShowAssignedToMeAndUnassignedPartRequests() {
      // Act: get part requests assigned to iHr or unassigned
      DataSet lDataSet = iQuery.execute( iHr, ANY_AIRCRAFT_GROUP, ANY_PR_STATUS, NO_DATE_SET,
            NO_DATE_SET, !SHOW_ALL_PART_REQUESTS );

      // Assert only assigned to me and unassigned part requests are retrieved
      // As iTodaysDatePR is assigned to iHr, and iFutureDatePR is unassigned
      assertContainsInOrderOnly( lDataSet, iTodaysDatePR, iFutureDatePR );
   }


   @Before
   public void loadData() throws Exception {

      // Create organization and HR for part request's requested-by hr
      OrgKey lOrg =
            new OrganizationDomainBuilder().withCode( "ORG" ).withDescription( "ORG_DESC" ).build();
      iHr = new HumanResourceDomainBuilder().inOrganization( lOrg ).withUserId( 101 )
            .withUsername( "User A" ).build();
      iHrUserB = new HumanResourceDomainBuilder().inOrganization( lOrg ).withUserId( 102 )
            .withUsername( "User B" ).build();

      // Create part with part group. The part group is for part request's requested part group
      PartNoBuilder lPartNoBuilder = new PartNoBuilder();
      lPartNoBuilder.withUnitType( RefQtyUnitKey.EA ).withDefaultPartGroup().build();
      iPartGroup = lPartNoBuilder.getDefaultPartGroup();

      // Create first aircraft and part request on the aircraft, and assign the first aircraft to
      // AIRCRAFT_GROUP_1
      InventoryKey lAircraft1 = new InventoryBuilder().inAicraftGroup( AIRCRAFT_GROUP_1 ).build();
      iFutureDatePR = createPartRequest( lAircraft1, iHr, FUTURE_DATE );

      // Create the second aircraft and part request on the aircraft
      InventoryKey lAircraft2 = new InventoryBuilder().build();
      iPastDatePR = createPartRequest( lAircraft2, iHrUserB, PAST_DATE );

      // Create the third aircraft and part request on the aircraft
      InventoryKey lAircraft3 = new InventoryBuilder().build();
      iTodaysDatePR = createPartRequest( lAircraft3, null, TODAY );

   }


   /**
    *
    * Create part request on the given aircraft assigned to aAssignHr with due date aDueDate
    *
    * @param aAircraft
    *           the aircraft
    * @param aAssignHr
    *           the assigned to hr
    * @param aNeededByDate
    *           the needed by date
    * @return
    */
   private PartRequestKey createPartRequest( InventoryKey aAircraft, HumanResourceKey aAssignHr,
         Date aNeededByDate ) {

      // Create a corrective task on the aircraft
      TaskKey lTask = new TaskBuilder().onInventory( aAircraft )
            .withTaskClass( RefTaskClassKey.CORR ).build();

      // Create a deadline for the part request
      new TaskDeadlineBuilder( lTask ).withDataType( DataTypeKey.CDY ).build();

      // Create a defer fault on the corrective task
      new SdFaultBuilder().withFailureSeverity( RefFailureSeverityKey.UNKNOWN )
            .withCorrectiveTask( lTask ).withStatus( RefEventStatusKey.CFDEFER ).build();

      // Create a part request on the corrective task with part group, requested by hr, and assigned
      // to hr
      PartRequestKey lPartRequest = new PartRequestBuilder().forPartGroup( iPartGroup )
            .forTask( lTask ).withStatus( RefEventStatusKey.PROPEN ).requestedBy( iHr )
            .assignedTo( aAssignHr ).requiredBy( aNeededByDate ).build();

      return lPartRequest;
   }

}
