package com.mxi.mx.suite.planning;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.mx.web.query.acftgroup.AircraftGroupServiceTest;
import com.mxi.mx.web.query.acftgroup.GetAircraftByAssemblyTest;
import com.mxi.mx.web.query.acftgroup.GetAircraftByGroupTest;
import com.mxi.mx.web.query.acftgroup.GetAircraftGroupByNameTest;
import com.mxi.mx.web.query.acftgroup.GetAircraftGroupsTest;
import com.mxi.mx.web.query.acftgroup.GetGroupAssignmentsTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { GetAircraftByAssemblyTest.class, GetAircraftByGroupTest.class,
      GetAircraftGroupByNameTest.class, GetAircraftGroupsTest.class, GetGroupAssignmentsTest.class,
      AircraftGroupServiceTest.class } )
public class AircraftGroupingTestSuite {
   // the class remains empty,
   // used only as a holder for the above annotations
}
