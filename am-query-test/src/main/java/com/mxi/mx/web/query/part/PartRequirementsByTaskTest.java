package com.mxi.mx.web.query.part;

import static org.junit.Assert.assertEquals;

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
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author Libin Cai
 * @created November 06, 2017
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class PartRequirementsByTaskTest {

   private static final Date DELIVERY_ETA = new GregorianCalendar( 2009, 1, 10 ).getTime();
   private static final Date SYSTEM_ETA = new GregorianCalendar( 2010, 1, 10 ).getTime();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private QuerySet iQuerySet;
   private PartRequestBuilder iPartRequestBuilder;
   private TaskKey iTask;


   @Before
   public void loadData() throws Exception {

      iTask = new TaskBuilder().build();

      PartNoBuilder lPartNoBuilder =
            new PartNoBuilder().withInventoryClass( RefInvClassKey.SER ).withDefaultPartGroup();

      PartNoKey lPartNo = lPartNoBuilder.build();

      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( iTask )
            .withInstallPart( lPartNo ).forPartGroup( lPartNoBuilder.getDefaultPartGroup() )
            .withRequestAction( RefReqActionKey.REQ ).build();

      TaskInstPartKey lTaskInstPart = new TaskInstPartKey( lTaskPartKey, 1 );

      DataSetArgument lArgs = lTaskInstPart.getPKWhereArg();
      lArgs.add( lPartNo.getPKWhereArg() );
      MxDataAccess.getInstance().executeInsert( "sched_inst_part", lArgs );

      iPartRequestBuilder = new PartRequestBuilder().forPartRequirement( lTaskInstPart )
            .withStatus( RefEventStatusKey.PRONORDER );
   }


   /**
    * Test to make sure the Delivery ETA returns when both Delivery ETA and System ETA exist.
    */
   @Test
   public void testDeliveryETAReturnedWhenBothDeliveryETAAndSystemETAExist() {

      iPartRequestBuilder.withDeliveryETADate( DELIVERY_ETA ).withEstimatedArrivalDate( SYSTEM_ETA )
            .build();

      // ACTION: Execute the Query
      execute();

      assertResult( DELIVERY_ETA );

   }


   /**
    * Test to make sure the System ETA returns when only System ETA exists.
    */
   @Test
   public void testSystemETAReturnedWhenOnlySystemETAExists() {

      iPartRequestBuilder.withEstimatedArrivalDate( SYSTEM_ETA ).build();

      // ACTION: Execute the Query
      execute();

      assertResult( SYSTEM_ETA );
   }


   private void execute() {

      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( iTask, "aTaskDbId", "aTaskId" );

      iQuerySet = QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }


   /**
    * Assert the result.
    *
    * @param aExpectedDate
    *           the expected date
    */
   private void assertResult( Date aExpectedDate ) {

      boolean lHasRowType4 = false;

      while ( iQuerySet.next() ) {
         if ( iQuerySet.getInt( "row_type" ) == 4 ) {
            lHasRowType4 = true;
            break;
         } ;
      }

      if ( !lHasRowType4 ) {
         Assert.fail( "The record with row_type=4 is not found." );
      }

      assertEquals( aExpectedDate, iQuerySet.getDate( "req_part_eta" ) );
   }

}
