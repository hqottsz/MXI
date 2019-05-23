package com.mxi.am.api.resource.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.mxi.am.api.resource.erp.location.LocationResourceBeanTest;
import com.mxi.am.api.resource.finance.FinanceAccountResourceBeanTest;
import com.mxi.am.api.resource.hr.UserAccountResourceBeanTest;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.panel.PanelResourceBeanTest;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.zone.ZoneResourceBeanTest;
import com.mxi.am.api.resource.maintenance.eng.config.part.PartResourceBeanTest;
import com.mxi.am.api.resource.maintenance.eng.prog.jobcarddefinition.JobCardDefinitionResourceBeanTest;
import com.mxi.am.api.resource.maintenance.eng.prog.requirementdefinition.RequirementDefinitionResourceBeanTest;
import com.mxi.am.api.resource.maintenance.exec.fault.FaultResourceBeanTest;
import com.mxi.am.api.resource.maintenance.exec.task.TaskResourceBeanTest;
import com.mxi.am.api.resource.maintenance.exec.task.labour.LabourResourceBeanTest;
import com.mxi.am.api.resource.maintenance.exec.task.partrequirement.PartRequirementResourceBeanTest;
import com.mxi.am.api.resource.maintenance.exec.task.taskinfo.TaskInfoResourceBeanTest;
import com.mxi.am.api.resource.maintenance.exec.work.pkg.WorkpackageResourceBeanTest;
import com.mxi.am.api.resource.maintenance.plan.flight.FlightResourceBeanTest;
import com.mxi.am.api.resource.materials.asset.inventory.InventoryResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.owner.OwnerResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.rfq.RFQResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.rfq.historynote.RFQHistoryNoteResourceBeanTest;
import com.mxi.am.api.resource.materials.proc.vendor.VendorResourceBeanTest;
import com.mxi.am.api.resource.materials.tracking.shipment.ShipmentResourceBeanTest;
import com.mxi.am.api.resource.organization.OrganizationResourceBeanTest;
import com.mxi.am.api.resource.quicktext.QuickTextResourceTest;
import com.mxi.am.api.resource.sys.alert.AlertResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.currency.CurrencyResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.labourskill.LabourSkillResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.quantityunit.QuantityUnitResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.reqpriority.ReqPriorityResourceBeanTest;
import com.mxi.am.api.resource.sys.refterm.spec2kcustomer.Spec2kCustomerResourceBeanTest;


@RunWith( Suite.class )
@Suite.SuiteClasses( { LocationResourceBeanTest.class, FinanceAccountResourceBeanTest.class,
      UserAccountResourceBeanTest.class, PanelResourceBeanTest.class, ZoneResourceBeanTest.class,
      PartResourceBeanTest.class, JobCardDefinitionResourceBeanTest.class,
      RequirementDefinitionResourceBeanTest.class, LabourResourceBeanTest.class,
      LabourSkillResourceBeanTest.class, PartRequirementResourceBeanTest.class,
      FaultResourceBeanTest.class, TaskInfoResourceBeanTest.class, TaskResourceBeanTest.class,
      WorkpackageResourceBeanTest.class, ZoneResourceBeanTest.class,
      InventoryResourceBeanTest.class, OwnerResourceBeanTest.class,
      PurchaseOrderResourceBeanTest.class, RFQHistoryNoteResourceBeanTest.class,
      RFQResourceBeanTest.class, VendorResourceBeanTest.class, ShipmentResourceBeanTest.class,
      OrganizationResourceBeanTest.class, QuickTextResourceTest.class, AlertResourceBeanTest.class,
      CurrencyResourceBeanTest.class, ReqPriorityResourceBeanTest.class,
      Spec2kCustomerResourceBeanTest.class, QuantityUnitResourceBeanTest.class,
      FlightResourceBeanTest.class } )
public class AmApiTestSuite {

   // the class remains empty,
   // used only as a holder for the above annotations
}
