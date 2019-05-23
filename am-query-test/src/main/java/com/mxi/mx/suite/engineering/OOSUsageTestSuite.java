package com.mxi.mx.suite.engineering;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.mx.core.ejb.flighthist.flighthistbean.EditHistFlightTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AddHistoryNoteForAffecedDeadlineTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AddHistoryNoteForAffecedEventTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectCurrentTsiTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectCurrentTsoTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectCurrentUsageTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectEditInventoryRecordsTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectFollowingFaultTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectFollowingUsageSnapShotForSubComponentTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectFollowingUsageSnapShotForSystemTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectLatestFlightTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectSnapshotTsnTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectTaskAndWorkPackageSnapshotTsnTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_AffectTaskDeadlineTest;
import com.mxi.mx.core.ejb.flighthist.flighthistbean.edithistflight.EditHistFlight_SystemNoteTest;
import com.mxi.mx.core.query.usage.GetAircraftAssemblyConfigurationByDateTest;
import com.mxi.mx.core.services.flightfl.hist.GetAssemblyUsageTest;
import com.mxi.mx.core.services.inventory.config.HistoricalAircraftConfigurationServiceTest;
import com.mxi.mx.core.services.inventory.config.InventoryServiceConfigurationChangeTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { EditHistFlightTest.class, InventoryServiceConfigurationChangeTest.class,
      HistoricalAircraftConfigurationServiceTest.class,
      EditHistFlight_AddHistoryNoteForAffecedDeadlineTest.class,
      EditHistFlight_AddHistoryNoteForAffecedEventTest.class,
      EditHistFlight_AffectCurrentTsiTest.class, EditHistFlight_AffectCurrentTsoTest.class,
      EditHistFlight_AffectCurrentUsageTest.class,
      EditHistFlight_AffectEditInventoryRecordsTest.class,
      EditHistFlight_AffectFollowingFaultTest.class,
      EditHistFlight_AffectFollowingUsageSnapShotForSubComponentTest.class,
      EditHistFlight_AffectFollowingUsageSnapShotForSystemTest.class,
      EditHistFlight_AffectLatestFlightTest.class, EditHistFlight_AffectSnapshotTsnTest.class,
      EditHistFlight_AffectTaskAndWorkPackageSnapshotTsnTest.class,
      EditHistFlight_AffectTaskDeadlineTest.class, EditHistFlight_AffectCurrentUsageTest.class,
      EditHistFlight_SystemNoteTest.class, GetAircraftAssemblyConfigurationByDateTest.class,
      GetAssemblyUsageTest.class } )
public class OOSUsageTestSuite {

   // the class remains empty,
   // used only as a holder for the above annotations
}
