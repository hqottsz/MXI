package com.mxi.mx.suite.planning;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.mx.web.query.todolist.FleetDueListTest;
import com.mxi.mx.web.query.todolist.FleetDueListTest1;
import com.mxi.mx.web.query.todolist.FleetDueListTest2;
import com.mxi.mx.web.query.todolist.FleetDueListTest3;


@RunWith( Suite.class )
@Suite.SuiteClasses( { FleetDueListTest.class, FleetDueListTest1.class, FleetDueListTest2.class,
      FleetDueListTest3.class } )
public class FleetDueListTestSuite {
   // the class remains empty,
   // used only as a holder for the above annotations
}
