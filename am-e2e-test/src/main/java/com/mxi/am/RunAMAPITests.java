package com.mxi.am;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.am.api.resource.batch.BatchResourceTest;
import com.mxi.am.api.resource.hr.UserAccountResourceBeanTest;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.flightmeasurement.FlightMeasurementRequirementResourceBeanTest;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.zone.ZoneResourceBeanTest;
import com.mxi.am.api.resource.maintenance.eng.config.part.PartResourceBeanTest;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.JobCardDefinitionResourceBeanTest;
import com.mxi.am.api.resource.maintenance.exec.fault.FaultResourceBeanTest;
import com.mxi.am.api.resource.maintenance.exec.maintenancereleasestatus.MaintenanceReleaseStatusResourceBeanTest;
import com.mxi.am.api.resource.maintenance.exec.task.labour.workcapture.WorkCaptureResourceBeanTest;
import com.mxi.am.api.resource.maintenance.plan.flight.FlightResourceBeanTest;
import com.mxi.am.api.resource.materials.asset.inventory.InventoryResourceBeanTest;
import com.mxi.am.api.resource.materials.asset.usage.UsageResourceBeanTest;
import com.mxi.am.api.resource.materials.plan.partrequest.PartRequestResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.owner.OwnerResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.purchaseorder.historynote.PurchaseOrderHistoryNoteResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.rfq.RFQResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.rfq.historynote.RFQHistoryNoteResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.vendor.VendorResourceBeanTest;
import com.mxi.am.api.resource.materials.tracking.shipment.ShipmentResourceBeanTest;
import com.mxi.am.api.resource.organization.OrganizationResourceBeanTest;
import com.mxi.am.api.resource.sys.alert.AlertResourceBeanTest;
import com.mxi.am.api.resource.sys.alert.type.AlertTypeResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.currency.CurrencyResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.failtype.FailTypeResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.faultsource.FaultSourceResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.flightlegstatus.FlightLegStatusResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.ietmtype.IetmTypeResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.impact.ImpactResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.jobstepstatus.JobStepStatusResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.labourskill.LabourSkillResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.labourtime.LabourTimeResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.measurementparameter.MeasurementParameterResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.quantityunit.QuantityUnitResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.removereason.RemoveReasonResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.reqpriority.ReqPriorityResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.stagereason.StageReasonResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.taskpriority.TaskPriorityResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.tasksubclass.TaskSubclassResourceBeanTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { BatchResourceTest.class, UserAccountResourceBeanTest.class,
      VendorResourceBeanTest.class, PurchaseOrderResourceBeanTest.class,
      ShipmentResourceBeanTest.class, OrganizationResourceBeanTest.class,
      CurrencyResourceBeanTest.class, QuantityUnitResourceBeanTest.class,
      ZoneResourceBeanTest.class, JobCardDefinitionResourceBeanTest.class,
      FlightMeasurementRequirementResourceBeanTest.class, FlightLegStatusResourceBeanTest.class,
      FaultResourceBeanTest.class, MaintenanceReleaseStatusResourceBeanTest.class,
      FlightResourceBeanTest.class, ReqPriorityResourceBeanTest.class,
      RFQHistoryNoteResourceBeanTest.class, StageReasonResourceBeanTest.class,
      PartResourceBeanTest.class, RFQResourceBeanTest.class, AlertResourceBeanTest.class,
      LabourTimeResourceBeanTest.class, JobStepStatusResourceBeanTest.class,
      PartRequestResourceBeanTest.class, RemoveReasonResourceBeanTest.class,
      WorkCaptureResourceBeanTest.class, AlertTypeResourceBeanTest.class,
      PurchaseOrderHistoryNoteResourceBeanTest.class, ImpactResourceBeanTest.class,
      LabourSkillResourceBeanTest.class, TaskSubclassResourceBeanTest.class,
      FailTypeResourceBeanTest.class, TaskPriorityResourceBeanTest.class,
      MeasurementParameterResourceBeanTest.class, FaultSourceResourceBeanTest.class,
      UsageResourceBeanTest.class, OwnerResourceBeanTest.class, InventoryResourceBeanTest.class,
      IetmTypeResourceBeanTest.class } )

public class RunAMAPITests {

   // the class remains empty,
   // used only as a holder for the above annotations
}
