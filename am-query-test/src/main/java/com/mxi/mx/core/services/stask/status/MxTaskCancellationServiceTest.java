package com.mxi.mx.core.services.stask.status;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.EventMessage;
import org.joda.time.Instant;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.production.task.domain.TaskCancelledEvent;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.sched.SchedStaskDao;


/**
 * Test class for {@linkplain MxTaskCancellationService}
 *
 */
public class MxTaskCancellationServiceTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private RecordingEventBus eventBus;
   private EvtEventDao evtEventDao;
   private SchedStaskDao schedStaskDao;


   @Before
   public void setup() {
      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();
      evtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      schedStaskDao = InjectorContainer.get().getInstance( SchedStaskDao.class );
   }


   @Test
   public void cancelATaskWillPublishTaskCanceledEvent() throws MxException, TriggerException {
      // ARRANGE
      TaskKey taskKey = Domain.createAdhocTask();
      HumanResourceKey hr = HumanResourceKey.ADMIN;
      RefStageReasonKey reason = RefStageReasonKey.OBSOLETE;
      RefEventStatusKey eventStatus = RefEventStatusKey.CANCEL;
      Date cancelDate = Instant.now().toDate();
      boolean baselineSynch = false;

      // ACT
      new MxTaskCancellationService().cancel( taskKey, hr, reason, eventStatus, null, null,
            cancelDate, baselineSynch );

      // ASSERT
      assertThat( "Task is not canceled correctly.",
            evtEventDao.findByPrimaryKey( taskKey.getEventKey() ).getEventStatus(),
            equalTo( RefEventStatusKey.CANCEL ) );

      List<EventMessage<?>> axonEvts = eventBus.getEventMessages().stream()
            .filter( evt -> evt.getPayload() instanceof TaskCancelledEvent )
            .collect( Collectors.toList() );

      assertThat( "Incorrect number of events is published.", axonEvts.size(), equalTo( 1 ) );

      TaskCancelledEvent event = ( TaskCancelledEvent ) axonEvts.get( 0 ).getPayload();

      assertThat( "Incorrect task key.", event.getTaskKey(), equalTo( taskKey ) );
      assertThat( "Incorrect cancel date.", event.getCancelDate(), equalTo( cancelDate ) );
      assertThat( "Incorrect reason key.", event.getRefStageReasonKey(), equalTo( reason ) );
      assertThat( "Incorrect baseline sync boolean.", event.getBaselineSynch(),
            equalTo( baselineSynch ) );
      assertThat( "Incorrect human resource key.", event.getAuthorizingHumanResourceKey(),
            equalTo( hr ) );
      assertThat( "Incorrect task class key.", event.getTaskClassKey(),
            equalTo( schedStaskDao.findByPrimaryKey( taskKey ).getTaskClass() ) );
   }
}
