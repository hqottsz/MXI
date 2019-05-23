
package com.mxi.mx.core.query.event;

import static com.mxi.mx.core.key.DataTypeKey.HOURS;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Requirement;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;


/**
 * This class tests the query
 * com.mxi.mx.core.query.event.GetUsageSnapshotDetailsByEventAndInventory.qrx
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetUsageSnapshotDetailsByEventAndInventoryTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * GIVEN an aircraft collecting usages
    *
    * AND the aircraft has a task completed against its root
    *
    * When the query GetUsageSnapshotDetailsByEventAndInventory.qrx is executed
    *
    * Then the usage snapshot details associated with the event, usage and inventory are returned
    *
    */
   @Test
   public void itGetsUsageSnapshotForTaskCompletedAgainstAnAircraft() {

      final InventoryKey lAircraft = Domain.createAircraft();

      final Double lAircraftHoursTsn = new Double( 3 );
      final Double lAircraftHoursTso = new Double( 3 );
      final Double lAircraftHoursTsi = new Double( 3 );
      final Double lAircraftAssmblHoursTsn = new Double( 3 );
      final Double lAircraftAssmblHoursTso = new Double( 3 );
      final TaskKey lReq = Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            final UsageSnapshot lUsageSnapshot =
                  new UsageSnapshot( lAircraft, HOURS, lAircraftHoursTso, lAircraftHoursTsn,
                        lAircraftHoursTsi, lAircraftHoursTso, lAircraftAssmblHoursTsn );
            aReq.addUsage( lUsageSnapshot );
            aReq.setInventory( lAircraft );
            aReq.setStatus( RefEventStatusKey.COMPLETE );
         }

      } );

      QuerySet lQs = executeQuery( lReq.getEventKey(), HOURS, lAircraft );
      lQs.next();

      assertEquals( "Query returned incorrect HOURS TSN for the task against the aircraft ",
            lAircraftHoursTsn, lQs.getDoubleObj( "TSN_QT" ) );
      assertEquals( "Query returned incorrect HOURS TSO for the task against the aircraft ",
            lAircraftHoursTso, lQs.getDoubleObj( "TSO_QT" ) );
      assertEquals( "Query returned incorrect HOURS TSI for the task against the aircraft ",
            lAircraftHoursTsi, lQs.getDoubleObj( "TSI_QT" ) );
      assertEquals(
            "Query returned incorrect HOURS ASSMBL_TSN_QT for the task against the aircraft ",
            lAircraftAssmblHoursTsn, lQs.getDoubleObj( "ASSMBL_TSN_QT" ) );
      assertEquals(
            "Query returned incorrect HOURS ASSMBL_TSO_QT for the task against the aircraft ",
            lAircraftAssmblHoursTso, lQs.getDoubleObj( "ASSMBL_TSO_QT" ) );
   }


   private QuerySet executeQuery( EventKey aEvent, DataTypeKey aDataType,
         InventoryKey aInventory ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aEvent, "aEventDbId", "aEventId" );
      lArgs.add( aDataType, "aDataTypeDbId", "aDataTypeId" );
      lArgs.add( aInventory, "aInvNoDbId", "aInvNoId" );

      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

   }
}
