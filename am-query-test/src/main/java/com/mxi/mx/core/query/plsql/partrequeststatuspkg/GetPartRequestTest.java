package com.mxi.mx.core.query.plsql.partrequeststatuspkg;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;


/**
 * This test class asserts that the function getPartRequestETA, getReqPartDbId and getReqPartId
 * within the PART_REQUEST_STATUS_PKG operates correctly.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class GetPartRequestTest {

   private static final Date DELIVERY_ETA = new GregorianCalendar( 2009, 1, 10 ).getTime();
   private static final Date SYSTEM_ETA_MOST_FUTURE =
         new GregorianCalendar( 2010, 1, 10 ).getTime();
   private static final String FUNCTION_GET_PART_REQUEST_ETA =
         "PART_REQUEST_STATUS_PKG.getPartRequestETA";
   private static final String FUNCTION_GET_PART_REQUEST_DBID =
         "PART_REQUEST_STATUS_PKG.getReqPartDbId";
   private static final String FUNCTION_GET_PART_REQUEST_ID =
         "PART_REQUEST_STATUS_PKG.getReqPartId";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private TaskKey iTask;
   private PartRequestBuilder iPartRequestBuilder;
   private PartRequestBuilder iOnOrderPartRequestBuilder;


   @Before
   public void loadData() throws Exception {

      iTask = new TaskBuilder().build();

      iPartRequestBuilder = new PartRequestBuilder().forTask( iTask );

      iOnOrderPartRequestBuilder = iPartRequestBuilder.withStatus( RefEventStatusKey.PRONORDER );

   }


   /**
    * Test to make sure the Delivery ETA returns when both Delivery ETA and System ETA exist.
    */
   @Test
   public void testDeliveryETAReturnedGivenAnyPartRequestWhenBothDeliveryETAAndSystemETAExist()
         throws Exception {

      new PartRequestBuilder().forTask( iTask ).withDeliveryETADate( DELIVERY_ETA )
            .withEstimatedArrivalDate( SYSTEM_ETA_MOST_FUTURE ).build();

      String lPartRequestETA = getPartRequestETA();

      Assert.assertEquals( DELIVERY_ETA, DateUtils.parseDateTimeString( lPartRequestETA ) );
   }


   /**
    * Test to make sure the System ETA returns when only System ETA exist for ONORDER part request.
    */
   @Test
   public void testSystemETAReturnedGivePartRequestOfOnorderWhenOnlySystemETAExist()
         throws Exception {

      executeAndAssertSystemETAReturned( iOnOrderPartRequestBuilder );
   }


   /**
    * Test to make sure the System ETA returns when only System ETA exist for REMOTE part request.
    */
   @Test
   public void testSystemETAReturnedGivePartRequestOfRemoteWhenOnlySystemETAExist()
         throws Exception {

      executeAndAssertSystemETAReturned(
            iPartRequestBuilder.withStatus( RefEventStatusKey.PRREMOTE ) );
   }


   /**
    * Test to make sure the System ETA returns when only System ETA exist for RESERVE part request.
    */
   @Test
   public void testSystemETAReturnedGivePartRequestOfReserveWhenOnlySystemETAExist()
         throws Exception {

      executeAndAssertSystemETAReturned(
            iPartRequestBuilder.withStatus( RefEventStatusKey.PRRESERVE ) );
   }


   /**
    * Execute the function and assert the system ETA is returned.
    *
    * @param aPartRequestBuilder
    *           the part request builder
    * @throws Exception
    *            if an error occurs
    */
   private void executeAndAssertSystemETAReturned( PartRequestBuilder aPartRequestBuilder )
         throws Exception {

      aPartRequestBuilder.withEstimatedArrivalDate( SYSTEM_ETA_MOST_FUTURE ).build();

      String lPartRequestETA = getPartRequestETA();

      Assert.assertEquals( SYSTEM_ETA_MOST_FUTURE,
            DateUtils.parseDateTimeString( lPartRequestETA ) );
   }


   /**
    * Test to make sure the part request with most future ETA is returned for multiple part
    * requests.
    */
   @Test
   public void testThePartRequestWithMostFutureETAReturnedWhenMultiplePartRequestsExist()
         throws Exception {

      PartRequestKey lPartRequestWithSystemETA =
            iOnOrderPartRequestBuilder.withEstimatedArrivalDate( SYSTEM_ETA_MOST_FUTURE ).build();
      iOnOrderPartRequestBuilder.withDeliveryETADate( DELIVERY_ETA ).build();

      String lPartRequestETA = getPartRequestETA();

      Assert.assertEquals( SYSTEM_ETA_MOST_FUTURE,
            DateUtils.parseDateTimeString( lPartRequestETA ) );

      Integer lPartRequestDbId = Integer.valueOf( execute( FUNCTION_GET_PART_REQUEST_DBID ) );
      Integer lPartRequestId = Integer.valueOf( execute( FUNCTION_GET_PART_REQUEST_ID ) );

      Assert.assertEquals( lPartRequestWithSystemETA,
            new PartRequestKey( lPartRequestDbId, lPartRequestId ) );
   }


   /**
    * Execute the function to get part request ETA.
    *
    * @return the part request eta.
    */
   private String getPartRequestETA() {

      return execute( FUNCTION_GET_PART_REQUEST_ETA );
   }


   /**
    * Execute the function.
    *
    * @param aFunctionName
    *           the function name
    * @return the result
    */
   private String execute( String aFunctionName ) {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( iTask, "aSchedDbId", "aSchedId" );

      // build parameters
      String[] lParmOrder = { "aSchedDbId", "aSchedId" };

      // return the result
      return QueryExecutor.executeFunction( iDatabaseConnectionRule.getConnection(), aFunctionName,
            lParmOrder, lArgs );
   }
}
