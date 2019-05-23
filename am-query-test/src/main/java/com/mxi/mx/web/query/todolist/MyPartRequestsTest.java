package com.mxi.mx.web.query.todolist;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.domain.builder.TaskBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;


/**
 * This class performs unit testing on the query file with the same package and name.
 *
 * @author Prasan Yapa
 * @created October 31, 2017
 */

@RunWith( BlockJUnit4ClassRunner.class )
public class MyPartRequestsTest {

   private static final Date DELIVERY_ETA = new GregorianCalendar( 2009, 1, 10 ).getTime();
   private static final Date SYSTEM_ETA = new GregorianCalendar( 2010, 1, 10 ).getTime();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private QuerySet iQuerySet;
   private HumanResourceKey iHr;
   private LocationKey iSupplyLocation;
   private PartRequestBuilder iPartRequestBuilder;


   @Before
   public void loadData() throws Exception {

      // Create the hr in order to create the particular part request.
      iHr = new HumanResourceDomainBuilder().build();

      // Create a supply location.
      iSupplyLocation = new LocationDomainBuilder().withType( RefLocTypeKey.AIRPORT )
            .isSupplyLocation().build();

      // Assign the task into the created work package.
      TaskKey lTask = new TaskBuilder().build();

      // Generate the task part key for the generated part request.
      TaskPartKey lTaskPartKey = new PartRequirementDomainBuilder( lTask )
            .withInstallPart( new PartNoBuilder().withUnitType( RefQtyUnitKey.EA ).build() )
            .withRequestAction( RefReqActionKey.REQ ).build();

      // Create the part request on the task with hr.
      iPartRequestBuilder =
            new PartRequestBuilder().forPartRequirement( new TaskInstPartKey( lTaskPartKey, 1 ) )
                  .isNeededAt( iSupplyLocation ).requestedBy( iHr )
                  .withStatus( RefEventStatusKey.PRONORDER );

   }


   /**
    * Test to make sure the Delivery ETA returns when both Delivery ETA and System ETA exist.
    */
   @Test
   public void testDeliveryETAReturnWhenBothDeliveryETAAndSystemETAExist() {

      iPartRequestBuilder.withDeliveryETADate( DELIVERY_ETA ).withEstimatedArrivalDate( SYSTEM_ETA )
            .build();

      // ACTION: Execute the Query
      execute();

      assertEquals( 1, iQuerySet.getRowCount() );

      iQuerySet.next();

      assertEquals( DELIVERY_ETA, iQuerySet.getDate( "eta_date" ) );

   }


   /**
    * Test to make sure the Delivery ETA returns when only Delivery ETA exists.
    */
   @Test
   public void testDeliveryETAReturnsWhenOnlyDeliveryETAExists() {

      iPartRequestBuilder.withDeliveryETADate( DELIVERY_ETA ).build();

      // ACTION: Execute the Query
      execute();

      assertEquals( 1, iQuerySet.getRowCount() );

      iQuerySet.next();

      assertEquals( DELIVERY_ETA, iQuerySet.getDate( "eta_date" ) );
   }


   /**
    * Test to make sure the System ETA returns when only System ETA exists.
    */
   @Test
   public void testSystemETAReturnsWhenOnlySystemETAExists() {

      iPartRequestBuilder.withEstimatedArrivalDate( SYSTEM_ETA ).build();

      // ACTION: Execute the Query
      execute();

      assertEquals( 1, iQuerySet.getRowCount() );

      iQuerySet.next();

      assertEquals( SYSTEM_ETA, iQuerySet.getDate( "eta_date" ) );
   }


   private void execute() {

      DataSetArgument lDataSetArgument = new DataSetArgument();
      lDataSetArgument.add( iHr, "aHrDbId", "aHrId" );
      lDataSetArgument.add( "aShowForDept", 1 );

      iQuerySet = QueryExecutor.executeQuery( getClass(), lDataSetArgument );
   }
}
