package com.mxi.mx.core.query.plsql.axoneventpublishpkg;

import static com.mxi.am.db.AxonDomainEventDao.PAYLOAD_COLUMN;
import static com.mxi.am.db.AxonDomainEventDao.PAYLOAD_TYPE_COLUMN;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Calendar;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.AxonDomainEventDao;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.ForecastModel;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.configuration.axon.AxonObjectMapper;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.production.task.domain.TaskDrivingDeadlineRescheduledEvent;


/**
 * This is a test class for testing the procedure
 * axon_event_publish_pkg.publish_task_deadl_resched_evt to the database
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class AxonPublishTaskDrivingDeadlineRescheduledEventTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule fakeInjectionOverrideRule = new InjectionOverrideRule();

   private final AxonObjectMapper mapper = new AxonObjectMapper();

   private static final double DEFAULT_FC_MODEL_RATE = 2.252363464d;
   private static final AxonDomainEventDao axonDomainEventDao = new AxonDomainEventDao();


   @Test
   public void publishTaskDrivingDeadlineRescheduledEvent() throws Exception {
      // ARRANGE
      final java.util.Date nowDate = DateUtils.truncate( new java.util.Date(), Calendar.SECOND );
      final DataTypeKey taskDataTypeKey = DataTypeKey.CDY;
      final Double taskDeadlineQt = 0D;

      final PartNoKey aircraftPartKey = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( configurationSlot -> {
            configurationSlot.addPartGroup( partGroup -> {
               partGroup.setInventoryClass( RefInvClassKey.ACFT );
               partGroup.addPart( aircraftPartKey );
            } );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.setForecastModel(
               Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

                  @Override
                  public void configure( ForecastModel aForecastModel ) {
                     aForecastModel.addRange( 1, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                  }
               } ) );
         aircraft.addUsage( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      final TaskKey taskKey = Domain.createRequirement( requirement -> {
         requirement.setStatus( RefEventStatusKey.ACTV );
         requirement.setInventory( aircraftKey );
         requirement.addCalendarDeadline( taskDataTypeKey, BigDecimal.valueOf( taskDeadlineQt ),
               nowDate );
      } );

      // ACT
      int result = execute( taskKey, taskDataTypeKey, new java.sql.Date( nowDate.getTime() ),
            taskDeadlineQt, new java.sql.Date( nowDate.getTime() ) );

      // VALIDATE

      final QuerySet querySet =
            axonDomainEventDao.findByPayLoadType( TaskDrivingDeadlineRescheduledEvent.class );
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );

      // validate the data is deserialized correctly.
      querySet.next();
      String payload = querySet.getString( PAYLOAD_COLUMN );
      TaskDrivingDeadlineRescheduledEvent event = ( TaskDrivingDeadlineRescheduledEvent ) mapper
            .readValue( payload, Class.forName( querySet.getString( PAYLOAD_TYPE_COLUMN ) ) );

      assertEquals( "Incorrect deserializtion of task key", taskKey, event.getTaskKey() );
      assertEquals( "Incorrect deserializtion of task deadline key", taskDataTypeKey,
            event.getDeadlineTypeKey() );
      assertEquals( "Incorrect deserializtion of deadline date", nowDate, event.getDeadlineDate() );
      assertEquals( "Incorrect deserializtion of deadline qt", taskDeadlineQt,
            event.getDeadlineQt() );
      assertEquals( "Incorrect deserializtion of estimates  date", nowDate,
            event.getEstimatedDate() );
   }


   /**
    * call axon_event_publish_pkg.publish_task_deadl_resched_evt
    *
    * @param taskKey
    *           the Task identifier
    * @param taskDataTypeKey
    *           DataType identifier
    * @param deadlineDate
    * @param taskDeadlineQt
    * @param estimatedDate
    * @return
    * @throws Exception
    */
   protected int execute( TaskKey taskKey, DataTypeKey taskDataTypeKey, java.sql.Date deadlineDate,
         Double taskDeadlineQt, java.sql.Date estimatedDate ) throws Exception {

      CallableStatement lPrepareCallPublishEvent;

      lPrepareCallPublishEvent = iDatabaseConnectionRule.getConnection()
            .prepareCall( "BEGIN axon_event_publish_pkg.publish_task_deadl_resched_evt("
                  + "an_TaskDbId => ?," + "an_TaskId => ?," + "an_DeadlineTypeDbId =>?, "
                  + "an_DeadlineTypeId => ?," + "ad_DeadlineDate =>?, " + "an_DeadlineQt => ?,"
                  + "ad_EstimatedDate => ?," + "on_Return => ?" + "); END;" );

      lPrepareCallPublishEvent.setInt( 1, taskKey.getDbId() );
      lPrepareCallPublishEvent.setInt( 2, taskKey.getId() );
      lPrepareCallPublishEvent.setInt( 3, taskDataTypeKey.getDbId() );
      lPrepareCallPublishEvent.setInt( 4, taskDataTypeKey.getId() );
      if ( deadlineDate != null ) {
         lPrepareCallPublishEvent.setDate( 5, deadlineDate );
      } else {
         lPrepareCallPublishEvent.setNull( 5, Types.NULL );

      }
      lPrepareCallPublishEvent.setDouble( 6, taskDeadlineQt );
      if ( deadlineDate != null ) {
         lPrepareCallPublishEvent.setDate( 7, estimatedDate );
      } else {
         lPrepareCallPublishEvent.setNull( 7, Types.NULL );
      }
      lPrepareCallPublishEvent.registerOutParameter( 8, Types.INTEGER );

      lPrepareCallPublishEvent.execute();

      return lPrepareCallPublishEvent.getInt( 8 );
   }

}
