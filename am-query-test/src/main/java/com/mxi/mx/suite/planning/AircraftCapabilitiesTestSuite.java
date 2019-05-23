package com.mxi.mx.suite.planning;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.am.api.query.maintenance.eng.prog.assembly.capability.GetAssemblyCapabilitiesTest;
import com.mxi.mx.core.query.capability.CapabilityHistoryUtilTest;
import com.mxi.mx.core.query.capability.EmptyCurrentCapabilityLevelsTest;
import com.mxi.mx.core.query.capability.GetAircraftKeysByAssemblyTest;
import com.mxi.mx.core.query.capability.PopulateCapabilitiesTest;
import com.mxi.mx.core.query.capability.PullCapabilitiesTest;
import com.mxi.mx.core.query.capability.UpdateCapabilityLevelTest;
import com.mxi.mx.core.query.inventory.ResetConfiguredCapabilityLevelsToBlankTest;
import com.mxi.mx.core.query.inventory.ResetCurrentCapabilityLevelsToBlankTest;
import com.mxi.mx.web.query.assembly.GetCapabilitiesTest;
import com.mxi.mx.web.query.inventory.capabilities.GetCapabilityLevelsTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { CapabilityHistoryUtilTest.class, EmptyCurrentCapabilityLevelsTest.class,
      GetAircraftKeysByAssemblyTest.class, PopulateCapabilitiesTest.class,
      PullCapabilitiesTest.class, UpdateCapabilityLevelTest.class,
      ResetConfiguredCapabilityLevelsToBlankTest.class,
      ResetCurrentCapabilityLevelsToBlankTest.class, GetCapabilitiesTest.class,
      com.mxi.mx.web.query.inventory.capabilities.GetCapabilitiesTest.class,
      GetCapabilityLevelsTest.class, GetAssemblyCapabilitiesTest.class } )
public class AircraftCapabilitiesTestSuite {
   // the class remains empty,
   // used only as a holder for the above annotations
}
