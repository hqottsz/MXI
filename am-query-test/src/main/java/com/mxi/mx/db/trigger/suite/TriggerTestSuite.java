package com.mxi.mx.db.trigger.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.mx.db.trigger.aircraft.OperationalStatusChangedAircraftTriggerTest;
import com.mxi.mx.db.trigger.aircraft.RegistrationCodeChangedAircraftTriggerTest;
import com.mxi.mx.db.trigger.fault.CertifyFaultTriggerTest;
import com.mxi.mx.db.trigger.fault.DeferFaultTriggerTest;
import com.mxi.mx.db.trigger.fault.EditFaultTriggerTest;
import com.mxi.mx.db.trigger.fault.FaultCreateTriggerTest;
import com.mxi.mx.db.trigger.fault.NoFaultFoundTriggerTest;
import com.mxi.mx.db.trigger.flight.AircraftReassignedFlightTriggerTest;
import com.mxi.mx.db.trigger.flight.CancelFlightTriggerTest;
import com.mxi.mx.db.trigger.flight.CompleteFlightTriggerTest;
import com.mxi.mx.db.trigger.flight.CreateFlightTriggerTest;
import com.mxi.mx.db.trigger.flight.ErrorFlightTriggerTest;
import com.mxi.mx.db.trigger.flightmeasurement.AssignFlightMeasurementRequirementTriggerTest;
import com.mxi.mx.db.trigger.flightmeasurement.CreateAssignFlightMeasurementRequirementTriggerTest;
import com.mxi.mx.db.trigger.flightmeasurement.ReorderFlightMeasurementRequirementTriggerTest;
import com.mxi.mx.db.trigger.flightmeasurement.UnassignFlightMeasurementRequirementTriggerTest;
import com.mxi.mx.db.trigger.inventory.CreateInventoryTriggerTest;
import com.mxi.mx.db.trigger.inventory.LockedInventoryTriggerTest;
import com.mxi.mx.db.trigger.inventory.UnlockedInventoryTriggerTest;
import com.mxi.mx.db.trigger.purchaseorder.CancelPurchaseOrderTriggerTest;
import com.mxi.mx.db.trigger.purchaseorder.IssuePurchaseOrderTriggerTest;
import com.mxi.mx.db.trigger.user.CreateUserTriggerTest;
import com.mxi.mx.db.trigger.user.EditUserTriggerTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { CreateUserTriggerTest.class, EditUserTriggerTest.class,
      DeferFaultTriggerTest.class, CreateInventoryTriggerTest.class,
      UnlockedInventoryTriggerTest.class, CertifyFaultTriggerTest.class,
      RegistrationCodeChangedAircraftTriggerTest.class, LockedInventoryTriggerTest.class,
      AssignFlightMeasurementRequirementTriggerTest.class,
      UnassignFlightMeasurementRequirementTriggerTest.class,
      ReorderFlightMeasurementRequirementTriggerTest.class,
      CreateAssignFlightMeasurementRequirementTriggerTest.class, NoFaultFoundTriggerTest.class,
      AircraftReassignedFlightTriggerTest.class, CancelFlightTriggerTest.class,
      CompleteFlightTriggerTest.class, CreateFlightTriggerTest.class, ErrorFlightTriggerTest.class,
      OperationalStatusChangedAircraftTriggerTest.class, EditFaultTriggerTest.class,
      FaultCreateTriggerTest.class, IssuePurchaseOrderTriggerTest.class,
      CancelPurchaseOrderTriggerTest.class } )
public class TriggerTestSuite {
   // the class remains empty,
   // used only as a holder for the above annotations
}
