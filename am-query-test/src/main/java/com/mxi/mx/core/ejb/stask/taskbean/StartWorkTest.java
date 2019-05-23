package com.mxi.mx.core.ejb.stask.taskbean;

import static com.mxi.am.domain.Domain.createAircraft;
import static com.mxi.am.domain.Domain.createHumanResource;
import static com.mxi.am.domain.Domain.createPart;
import static com.mxi.am.domain.Domain.createWorkPackage;
import static com.mxi.mx.common.utils.DateUtils.addDays;
import static com.mxi.mx.common.utils.DateUtils.floorSecond;
import static com.mxi.mx.core.key.RefEventStatusKey.COMMIT;
import static com.mxi.mx.core.key.RefEventStatusKey.IN_WORK;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.servlet.SessionContextFake;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.ejb.stask.TaskBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;


/**
 * Integration unit test for {@link TaskBean#startWork()}
 *
 */
public class StartWorkTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   // Database dates have a granularity of seconds.
   private static final Date SCHEDULED_START_DATE = DateUtils.floorSecond( new Date() );
   private static final LocationKey LOCATION = LocationKey.SYSTEM;

   private static final UsageSnapshot[] NA_CURRENT_USAGES = null;
   private static final Date NA_REVISION_DATE = null;

   private static EvtEventDao evtEventDao = new JdbcEvtEventDao();


   @Test
   public void startCommittedWorkpackageUsingScheduledFromDateTime() throws Exception {

      // Given a committed work package against an aircraft and the work package has a scheduled
      // start date-time.
      //
      // Note - startWork() requires:
      // - the work package location be set
      // - the aircraft location be set
      // - the aircraft have a part number
      TaskKey workpackage = createWorkPackage( wp -> {
         wp.setAircraft( createAircraft( acft -> {
            acft.setLocation( LOCATION );
            acft.setPart( createPart() );
         } ) );
         wp.setStatus( COMMIT );
         wp.setScheduledStartDate( SCHEDULED_START_DATE );
         wp.setLocation( LOCATION );
      } );

      // When the work package is started using its scheduled start date.
      HumanResourceKey hr = createHumanResource();
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      taskBean.startWork( workpackage, hr, SCHEDULED_START_DATE, NA_CURRENT_USAGES,
            NA_REVISION_DATE );

      // Then the work package is started and its actual start date is set to its scheduled start
      // date.
      EvtEventTable evtEventRow = evtEventDao.findByPrimaryKey( workpackage.getEventKey() );
      assertThat( "Unexpected wp status.", evtEventRow.getEventStatus(), is( IN_WORK ) );
      assertThat( "Unexpected wp start date.", evtEventRow.getEventDate(),
            is( SCHEDULED_START_DATE ) );
   }


   @Test
   public void startCommittedWorkpackageUsingDateInPast() throws Exception {

      // Given a committed work package against an aircraft and the work package has a scheduled
      // start date.
      //
      // Note - startWork() requires:
      // - the work package location be set
      // - the aircraft location be set
      // - the aircraft have a part number
      TaskKey workpackage = createWorkPackage( wp -> {
         wp.setAircraft( createAircraft( acft -> {
            acft.setLocation( LOCATION );
            acft.setPart( createPart() );
         } ) );
         wp.setStatus( COMMIT );
         wp.setScheduledStartDate( SCHEDULED_START_DATE );
         wp.setLocation( LOCATION );
      } );

      // When the work package is started using a date in the past.
      HumanResourceKey hr = createHumanResource();
      TaskBean taskBean = new TaskBean();
      taskBean.setSessionContext( new SessionContextFake() );

      Date dateInThePast = floorSecond( addDays( new Date(), -1 ) );
      taskBean.startWork( workpackage, hr, dateInThePast, NA_CURRENT_USAGES, NA_REVISION_DATE );

      // Then the work package is started and its actual start date is that date in the past.
      EvtEventTable evtEventRow = evtEventDao.findByPrimaryKey( workpackage.getEventKey() );
      assertThat( "Unexpected wp status.", evtEventRow.getEventStatus(), is( IN_WORK ) );
      assertThat( "Unexpected wp start date.", evtEventRow.getEventDate(), is( dateInThePast ) );
   }

}
