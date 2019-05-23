package com.mxi.mx.suite.inventory.reservation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationBatchTest;
import com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationCsnStockTest;
import com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationHubTest;
import com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationSerTest;
import com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationStealingTest;
import com.mxi.mx.core.services.inventory.reservation.auto.AutoReservationStockTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { AutoReservationStealingTest.class, AutoReservationStockTest.class,
      AutoReservationCsnStockTest.class, AutoReservationHubTest.class, AutoReservationSerTest.class,
      AutoReservationBatchTest.class } )
public class AutoReservationTestSuite {
   // the class remains empty,
   // used only as a holder for the above annotations
}
