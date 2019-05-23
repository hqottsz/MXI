package com.mxi.mx.web.query.inventory;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefRelationTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.table.evt.EvtEventRel;


public class UnassignedTasksFaultsTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void execute_unassignedTasks() {
      LocationKey lLocationKey = Domain.createLocation();

      InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setLocation( lLocationKey );
      } );

      TaskKey lTaskKey = Domain.createRequirement( aRequirement -> {
         aRequirement.setTaskClass( RefTaskClassKey.ADHOC );
         aRequirement.setStatus( RefEventStatusKey.ACTV );
         aRequirement.setInventory( lAircraft );
      } );

      PartRequestKey lPartRequestKey = new PartRequestBuilder()
            .forSpecifiedPart( Domain.createPart() ).withReqAircraft( lAircraft )
            .forTask( lTaskKey ).withRequestedQuantity( 1.0 ).build();

      QuerySet lQs = executeQuery( lAircraft );

      assertEquals( 1, lQs.getRowCount() );
      lQs.next();

      assertEquals( lTaskKey, lQs.getKey( TaskKey.class, "event_key" ) );
      assertEquals( lLocationKey, lQs.getKey( LocationKey.class, "location_key" ) );
   }


   @Test
   public void execute_unassignedHistoricTasks() {
      InventoryKey lAircraft = Domain.createAircraft();

      Domain.createRequirement( aRequirement -> {
         aRequirement.setTaskClass( RefTaskClassKey.ADHOC );
         aRequirement.setStatus( RefEventStatusKey.COMPLETE );
         aRequirement.setInventory( lAircraft );
      } );

      QuerySet lQs = executeQuery( lAircraft );

      assertEquals( 0, lQs.getRowCount() );
   }


   @Test
   public void execute_assignedTaskToWorkpackage() {
      InventoryKey lAircraft = Domain.createAircraft();

      TaskKey lWorkpackageKey = Domain.createWorkPackage();

      Domain.createRequirement( aRequirement -> {
         aRequirement.setTaskClass( RefTaskClassKey.ADHOC );
         aRequirement.setStatus( RefEventStatusKey.ACTV );
         aRequirement.setInventory( lAircraft );
         aRequirement.setParentTask( lWorkpackageKey );
      } );

      QuerySet lQs = executeQuery( lAircraft );

      assertEquals( 0, lQs.getRowCount() );
   }


   @Test
   public void execute_unassignedTaskWithDeadline() {
      InventoryKey lAircraft = Domain.createAircraft();

      TaskKey lTaskKey = Domain.createRequirement( aRequirement -> {
         aRequirement.setTaskClass( RefTaskClassKey.ADHOC );
         aRequirement.setStatus( RefEventStatusKey.ACTV );
         aRequirement.setInventory( lAircraft );
         aRequirement.addCalendarDeadline( DataTypeKey.CDY, new BigDecimal( 1 ),
               DateUtils.addDays( new Date(), 3 ) );
      } );
      // Driving task is itself
      EvtEventRel.create( lTaskKey.getEventKey(), lTaskKey.getEventKey(),
            RefRelationTypeKey.DRVTASK );

      QuerySet lQs = executeQuery( lAircraft );

      assertEquals( 0, lQs.getRowCount() );
   }


   @Test
   public void execute_workpackage() {
      InventoryKey lAircraft = Domain.createAircraft();

      Domain.createRequirement( aRequirement -> {
         aRequirement.setTaskClass( RefTaskClassKey.CHECK );
         aRequirement.setStatus( RefEventStatusKey.ACTV );
         aRequirement.setInventory( lAircraft );
      } );

      QuerySet lQs = executeQuery( lAircraft );

      assertEquals( 0, lQs.getRowCount() );
   }


   private QuerySet executeQuery( InventoryKey aInventoryKey ) {
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aInventoryKey, "aAircraftDbId", "aAircraftId" );
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
