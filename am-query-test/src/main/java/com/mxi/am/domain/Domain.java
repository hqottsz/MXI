package com.mxi.am.domain;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.mxi.am.domain.builder.AdhocTaskBuilder;
import com.mxi.am.domain.builder.AircraftAssemblyBuilder;
import com.mxi.am.domain.builder.AircraftBuilder;
import com.mxi.am.domain.builder.BatchInventoryBuilder;
import com.mxi.am.domain.builder.BlockBuilder;
import com.mxi.am.domain.builder.BlockChainDefinitionBuilder;
import com.mxi.am.domain.builder.BlockDefinitionBuilder;
import com.mxi.am.domain.builder.CarrierBuilder;
import com.mxi.am.domain.builder.ComponentWorkPackageBuilder;
import com.mxi.am.domain.builder.CorrectiveTaskBuilder;
import com.mxi.am.domain.builder.CrewBuilder;
import com.mxi.am.domain.builder.CrewShiftDayBuilder;
import com.mxi.am.domain.builder.DamageRecordBuilder;
import com.mxi.am.domain.builder.DeferralReferenceBuilder;
import com.mxi.am.domain.builder.EngineAssemblyBuilder;
import com.mxi.am.domain.builder.EngineBuilder;
import com.mxi.am.domain.builder.FailureSeverityBuilder;
import com.mxi.am.domain.builder.FaultBuilder;
import com.mxi.am.domain.builder.FaultReferenceRequestBuilder;
import com.mxi.am.domain.builder.FlightBuilder;
import com.mxi.am.domain.builder.ForecastModelBuilder;
import com.mxi.am.domain.builder.HumanResourceBuilder;
import com.mxi.am.domain.builder.IetmBuilder;
import com.mxi.am.domain.builder.ImpactBuilder;
import com.mxi.am.domain.builder.InstallationRecordBuilder;
import com.mxi.am.domain.builder.InvoiceBuilder;
import com.mxi.am.domain.builder.JobCardBuilder;
import com.mxi.am.domain.builder.JobCardDefinitionBuilder;
import com.mxi.am.domain.builder.KitInventoryBuilder;
import com.mxi.am.domain.builder.LabourSkillBuilder;
import com.mxi.am.domain.builder.LocationBuilder;
import com.mxi.am.domain.builder.LocationPrinterBuilder;
import com.mxi.am.domain.builder.LocationPrinterJobBuilder;
import com.mxi.am.domain.builder.LocationZoneBuilder;
import com.mxi.am.domain.builder.MaintenanceProgramBuilder;
import com.mxi.am.domain.builder.ManufacturerBuilder;
import com.mxi.am.domain.builder.MeasurementDefinitionBuilder;
import com.mxi.am.domain.builder.OrganizationAuthorityBuilder;
import com.mxi.am.domain.builder.OrganizationBuilder;
import com.mxi.am.domain.builder.OwnerBuilder;
import com.mxi.am.domain.builder.PanelBuilder;
import com.mxi.am.domain.builder.PartBuilder;
import com.mxi.am.domain.builder.PartGroupBuilder;
import com.mxi.am.domain.builder.PartRequestDomainBuilder;
import com.mxi.am.domain.builder.PlanningTypeBuilder;
import com.mxi.am.domain.builder.PurchaseOrderBuilder;
import com.mxi.am.domain.builder.PurchaseOrderLineBuilder;
import com.mxi.am.domain.builder.ReferenceDocumentBuilder;
import com.mxi.am.domain.builder.ReferenceDocumentDefinitionBuilder;
import com.mxi.am.domain.builder.RemovalRecordBuilder;
import com.mxi.am.domain.builder.RepairOrderBuilder;
import com.mxi.am.domain.builder.RepetitiveTaskBuilder;
import com.mxi.am.domain.builder.ReplBuilder;
import com.mxi.am.domain.builder.RequirementBuilder;
import com.mxi.am.domain.builder.RequirementDefinitionBuilder;
import com.mxi.am.domain.builder.SerializedInventoryBuilder;
import com.mxi.am.domain.builder.ShiftBuilder;
import com.mxi.am.domain.builder.ShipmentBuilder;
import com.mxi.am.domain.builder.ShipmentLineDomainBuilder;
import com.mxi.am.domain.builder.StockDistReqBuilder;
import com.mxi.am.domain.builder.StockDistReqPickedItemBuilder;
import com.mxi.am.domain.builder.TaskDefinitionPartRequirementBuilder;
import com.mxi.am.domain.builder.TaskSubClassBuilder;
import com.mxi.am.domain.builder.TrackedInventoryBuilder;
import com.mxi.am.domain.builder.TransferBuilder;
import com.mxi.am.domain.builder.UsageAdjustmentBuilder;
import com.mxi.am.domain.builder.UserBuilder;
import com.mxi.am.domain.builder.VendorBuilder;
import com.mxi.am.domain.builder.WorkPackageBuilder;
import com.mxi.am.domain.builder.ZoneBuilder;
import com.mxi.am.domain.reader.ConfigurationSlotReader;
import com.mxi.am.domain.reader.CurrentUsageReader;
import com.mxi.am.domain.reader.FaultReferenceReader;
import com.mxi.am.domain.reader.FaultReferenceRequestReader;
import com.mxi.am.domain.reader.IetmAttachmentReader;
import com.mxi.am.domain.reader.IetmTechnicalReferenceReader;
import com.mxi.am.domain.reader.LabourRequirementReader;
import com.mxi.am.domain.reader.PanelReader;
import com.mxi.am.domain.reader.PartGroupReader;
import com.mxi.am.domain.reader.RequirementDefinitionStepReader;
import com.mxi.am.domain.reader.SignOffRequirementReader;
import com.mxi.am.domain.reader.SystemReader;
import com.mxi.am.domain.reader.UsageParameterReader;
import com.mxi.am.domain.reader.UsageSnapshotReader;
import com.mxi.am.domain.reader.WeightAndBalanceImpactReader;
import com.mxi.mx.common.dataset.ProcedureStatement;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.CarrierKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.EqpPlanningTypeKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.FaultReferenceKey;
import com.mxi.mx.core.key.FaultReferenceRequestKey;
import com.mxi.mx.core.key.FcModelKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.IetmDefinitionKey;
import com.mxi.mx.core.key.IetmTopicKey;
import com.mxi.mx.core.key.InvLocPrinterJobKey;
import com.mxi.mx.core.key.InvLocZoneKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.LocationPrinterKey;
import com.mxi.mx.core.key.MaintPrgmKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.OrgCrewShiftPlanKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.OwnerKey;
import com.mxi.mx.core.key.PanelKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.PurchaseInvoiceKey;
import com.mxi.mx.core.key.PurchaseOrderKey;
import com.mxi.mx.core.key.PurchaseOrderLineKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefImpactKey;
import com.mxi.mx.core.key.RefLabourSkillKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.SchedLabourKey;
import com.mxi.mx.core.key.SchedWPSignReqKey;
import com.mxi.mx.core.key.ShiftKey;
import com.mxi.mx.core.key.ShipmentKey;
import com.mxi.mx.core.key.ShipmentLineKey;
import com.mxi.mx.core.key.StockDistReqKey;
import com.mxi.mx.core.key.StockDistReqPickedItemKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartListKey;
import com.mxi.mx.core.key.TaskStepKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.TaskWeightAndBalanceKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.key.VendorKey;
import com.mxi.mx.core.key.ZoneKey;
import com.mxi.mx.core.services.event.inventory.UsageSnapshot;
import com.mxi.mx.core.usage.model.UsageAdjustmentId;


/**
 * The domain entities for Asset Management.
 *
 * The intent of the domain entities is to represent business objects used within the business
 * domain of Asset Management. These entities are implementation agnostic and should not reflect the
 * implementation of Asset Management.
 *
 * For example; there are domain entities for Faults and Tasks but NOT for events (as stored in the
 * evt_event table).
 *
 * This class will has the potential to have many methods to please keep them organized
 * alphabetically
 */
public final class Domain {

   private Domain() {
      // utility class
   }


   public static TaskKey createAdhocTask() {
      return createAdhocTask( new NoopDomainConfiguration<AdhocTask>() );
   }


   public static TaskKey createAdhocTask( DomainConfiguration<AdhocTask> aDomainConfiguration ) {
      AdhocTask lAdhocTask = new AdhocTask();
      aDomainConfiguration.configure( lAdhocTask );
      return AdhocTaskBuilder.build( lAdhocTask );
   }


   public static InventoryKey createAircraft() {
      return createAircraft( new NoopDomainConfiguration<Aircraft>() );
   }


   public static InventoryKey createAircraft( DomainConfiguration<Aircraft> aDomainConfiguration ) {
      Aircraft lAircraft = new Aircraft();
      aDomainConfiguration.configure( lAircraft );
      return AircraftBuilder.build( lAircraft );
   }


   public static AssemblyKey createAircraftAssembly() {
      return createAircraftAssembly( new NoopDomainConfiguration<AircraftAssembly>() );
   }


   public static AssemblyKey
         createAircraftAssembly( DomainConfiguration<AircraftAssembly> aDomainConfiguration ) {
      AircraftAssembly lAircraftAssembly = new AircraftAssembly();
      aDomainConfiguration.configure( lAircraftAssembly );
      return AircraftAssemblyBuilder.build( lAircraftAssembly );
   }


   public static AuthorityKey createAuthority() {
      return createAuthority( new NoopDomainConfiguration<OrganizationAuthority>() );
   }


   public static AuthorityKey
         createAuthority( DomainConfiguration<OrganizationAuthority> aDomainConfiguration ) {
      OrganizationAuthority lOrganizationAuthority = new OrganizationAuthority();
      aDomainConfiguration.configure( lOrganizationAuthority );
      return OrganizationAuthorityBuilder.build( lOrganizationAuthority );
   }


   public static TaskKey createBlock() {
      return createBlock( new NoopDomainConfiguration<Block>() );
   }


   public static TaskKey createBlock( DomainConfiguration<Block> aDomainConfiguration ) {
      Block lBlock = new Block();
      aDomainConfiguration.configure( lBlock );
      return BlockBuilder.build( lBlock );
   }


   public static Map<Integer, TaskTaskKey> createBlockChainDefinition() {
      return createBlockChainDefinition( new NoopDomainConfiguration<BlockChainDefinition>() );
   }


   public static Map<Integer, TaskTaskKey> createBlockChainDefinition(
         DomainConfiguration<BlockChainDefinition> aDomainConfiguration ) {
      BlockChainDefinition lBlockChainDefinition = new BlockChainDefinition();
      aDomainConfiguration.configure( lBlockChainDefinition );
      return BlockChainDefinitionBuilder.build( lBlockChainDefinition );
   }


   public static TaskTaskKey createBlockDefinition() {
      return createBlockDefinition( new NoopDomainConfiguration<BlockDefinition>() );
   }


   public static TaskTaskKey
         createBlockDefinition( DomainConfiguration<BlockDefinition> aDomainConfiguration ) {
      BlockDefinition lBlockDefinition = new BlockDefinition();
      aDomainConfiguration.configure( lBlockDefinition );
      return BlockDefinitionBuilder.build( lBlockDefinition );
   }


   public static DepartmentKey createCrew() {
      return createCrew( new NoopDomainConfiguration<Crew>() );
   }


   public static DepartmentKey createCrew( DomainConfiguration<Crew> aDomainConfiguration ) {
      Crew lCrew = new Crew();
      aDomainConfiguration.configure( lCrew );
      return CrewBuilder.build( lCrew );
   }


   public static InventoryKey createEngine() {
      return createEngine( new NoopDomainConfiguration<Engine>() );
   }


   public static InventoryKey createEngine( DomainConfiguration<Engine> aDomainConfiguration ) {
      Engine lEngine = new Engine();
      aDomainConfiguration.configure( lEngine );
      return EngineBuilder.build( lEngine );
   }


   public static AssemblyKey createEngineAssembly() {
      return createEngineAssembly( new NoopDomainConfiguration<EngineAssembly>() );
   }


   public static AssemblyKey
         createEngineAssembly( DomainConfiguration<EngineAssembly> aDomainConfiguration ) {
      EngineAssembly lEngineAssembly = new EngineAssembly();
      aDomainConfiguration.configure( lEngineAssembly );
      return EngineAssemblyBuilder.build( lEngineAssembly );
   }


   public static FaultKey createFault() {
      return createFault( new NoopDomainConfiguration<Fault>() );
   }


   public static FaultKey createFault( DomainConfiguration<Fault> aDomainConfiguration ) {
      Fault lFault = new Fault();
      aDomainConfiguration.configure( lFault );
      return FaultBuilder.build( lFault );
   }


   public static FaultReferenceRequestKey createFaultReferenceRequest(
         DomainConfiguration<FaultReferenceRequest> aDomainConfiguration ) {
      FaultReferenceRequest faultReferenceRequest = new FaultReferenceRequest();
      aDomainConfiguration.configure( faultReferenceRequest );
      return FaultReferenceRequestBuilder.build( faultReferenceRequest );
   }


   public static FlightLegId createFlight() {
      return createFlight( new NoopDomainConfiguration<Flight>() );
   }


   public static FlightLegId createFlight( DomainConfiguration<Flight> aDomainConfiguration ) {
      Flight lFlight = new Flight();
      aDomainConfiguration.configure( lFlight );
      return FlightBuilder.build( lFlight );
   }


   public static FcModelKey createForecastModel() {
      return createForecastModel( new NoopDomainConfiguration<ForecastModel>() );
   }


   public static FcModelKey
         createForecastModel( DomainConfiguration<ForecastModel> aDomainConfiguration ) {
      ForecastModel lForecastModel = new ForecastModel();
      aDomainConfiguration.configure( lForecastModel );
      return ForecastModelBuilder.build( lForecastModel );

   }


   public static IetmDefinitionKey createIetm() {
      return createIetm( new NoopDomainConfiguration<Ietm>() );
   }


   public static IetmDefinitionKey createIetm( DomainConfiguration<Ietm> aDomainConfiguration ) {
      Ietm lIetm = new Ietm();
      aDomainConfiguration.configure( lIetm );
      return IetmBuilder.build( lIetm );
   }


   public static RefImpactKey createImpact() {
      return createImpact( new NoopDomainConfiguration<Impact>() );
   }


   public static RefImpactKey createImpact( DomainConfiguration<Impact> aDomainConfiguration ) {
      Impact lImpact = new Impact();
      aDomainConfiguration.configure( lImpact );
      return ImpactBuilder.build( lImpact );
   }


   public static PurchaseInvoiceKey createInvoice() {
      return createInvoice( new NoopDomainConfiguration<Invoice>() );
   }


   public static PurchaseInvoiceKey
         createInvoice( DomainConfiguration<Invoice> aDomainConfiguration ) {
      Invoice lInvoice = new Invoice();
      aDomainConfiguration.configure( lInvoice );
      return InvoiceBuilder.build( lInvoice );
   }


   public static void
         createInstallationRecord( DomainConfiguration<InstallationRecord> aDomainConfiguration ) {
      InstallationRecord lInstallationRecord = new InstallationRecord();
      aDomainConfiguration.configure( lInstallationRecord );
      InstallationRecordBuilder.build( lInstallationRecord );
   }


   public static TaskKey createJobCard() {
      return createJobCard( new NoopDomainConfiguration<JobCard>() );
   }


   public static TaskKey createJobCard( DomainConfiguration<JobCard> aDomainConfiguration ) {
      JobCard lJobCard = new JobCard();
      aDomainConfiguration.configure( lJobCard );
      return JobCardBuilder.build( lJobCard );
   }


   public static TaskTaskKey createJobCardDefinition() {
      return createJobCardDefinition( new NoopDomainConfiguration<JobCardDefinition>() );
   }


   public static TaskTaskKey
         createJobCardDefinition( DomainConfiguration<JobCardDefinition> aDomainConfiguration ) {
      JobCardDefinition lJobCardDefinition = new JobCardDefinition();
      aDomainConfiguration.configure( lJobCardDefinition );
      return JobCardDefinitionBuilder.build( lJobCardDefinition );
   }


   public static LocationKey createLocation() {
      return createLocation( new NoopDomainConfiguration<Location>() );
   }


   public static LocationKey createLocation( DomainConfiguration<Location> aDomainConfiguration ) {
      Location lLocation = new Location();
      aDomainConfiguration.configure( lLocation );
      return LocationBuilder.build( lLocation );
   }


   public static InvLocZoneKey createLocationZone() {
      return createLocationZone( new NoopDomainConfiguration<LocationZone>() );
   }


   public static InvLocZoneKey
         createLocationZone( DomainConfiguration<LocationZone> aDomainConfiguration ) {
      LocationZone entity = new LocationZone();
      aDomainConfiguration.configure( entity );
      return LocationZoneBuilder.build( entity );
   }


   public static LocationPrinterKey
         createLocationPrinter( DomainConfiguration<LocationPrinter> aDomainConfiguration ) {
      LocationPrinter lPrinter = new LocationPrinter();
      aDomainConfiguration.configure( lPrinter );
      return LocationPrinterBuilder.build( lPrinter );
   }


   public static InvLocPrinterJobKey
         createLocationPrinterJob( DomainConfiguration<LocationPrinterJob> aDomainConfiguration ) {
      LocationPrinterJob lPrinter = new LocationPrinterJob();
      aDomainConfiguration.configure( lPrinter );
      return LocationPrinterJobBuilder.build( lPrinter );
   }


   public static ShiftKey createShift() {
      return createShift( new NoopDomainConfiguration<Shift>() );
   }


   public static ShiftKey createShift( DomainConfiguration<Shift> aDomainConfiguration ) {
      Shift lShift = new Shift();
      aDomainConfiguration.configure( lShift );
      return ShiftBuilder.build( lShift );
   }


   public static OrgCrewShiftPlanKey createCrewShiftDay() {
      return createCrewShiftDay( new NoopDomainConfiguration<CrewShiftDay>() );
   }


   public static OrgCrewShiftPlanKey
         createCrewShiftDay( DomainConfiguration<CrewShiftDay> aDomainConfiguration ) {
      CrewShiftDay lCrewShiftDay = new CrewShiftDay();
      aDomainConfiguration.configure( lCrewShiftDay );
      return CrewShiftDayBuilder.build( lCrewShiftDay );
   }


   public static StockDistReqKey
         createStockDistReq( DomainConfiguration<StockDistReq> aDomainConfiguration ) {
      StockDistReq lStockDistReq = new StockDistReq();
      aDomainConfiguration.configure( lStockDistReq );
      return StockDistReqBuilder.build( lStockDistReq );
   }


   public static StockDistReqPickedItemKey createStockDistReqPickedItem(
         DomainConfiguration<StockDistReqPickedItem> aDomainConfiguration ) {
      StockDistReqPickedItem lStockDistReqPickedItem = new StockDistReqPickedItem();
      aDomainConfiguration.configure( lStockDistReqPickedItem );
      return StockDistReqPickedItemBuilder.build( lStockDistReqPickedItem );
   }


   public static MaintPrgmKey createMaintenanceProgram() {
      return createMaintenanceProgram( new NoopDomainConfiguration<MaintenanceProgram>() );
   }


   public static MaintPrgmKey
         createMaintenanceProgram( DomainConfiguration<MaintenanceProgram> aDomainConfiguration ) {
      com.mxi.am.domain.MaintenanceProgram lMaintenanceProgram = new MaintenanceProgram();
      aDomainConfiguration.configure( lMaintenanceProgram );
      return MaintenanceProgramBuilder.build( lMaintenanceProgram );
   }


   public static ManufacturerKey createManufacturer() {
      return createManufacturer( new NoopDomainConfiguration<Manufacturer>() );
   }


   public static ManufacturerKey
         createManufacturer( DomainConfiguration<Manufacturer> aDomainConfiguration ) {
      Manufacturer lManufacturer = new Manufacturer();
      aDomainConfiguration.configure( lManufacturer );
      return new ManufacturerBuilder().build( lManufacturer );
   }


   public static DataTypeKey createMeasurementDefinition() {
      return createMeasurementDefinition( new NoopDomainConfiguration<MeasurementDefinition>() );
   }


   public static DataTypeKey createMeasurementDefinition(
         DomainConfiguration<MeasurementDefinition> aDomainConfiguration ) {
      MeasurementDefinition lMeasurementDefinition = new MeasurementDefinition();
      aDomainConfiguration.configure( lMeasurementDefinition );
      return MeasurementDefinitionBuilder.build( lMeasurementDefinition );
   }


   public static CarrierKey createOperator() {
      return new CarrierBuilder().build();
   }


   public static CarrierKey createOperator( DomainConfiguration<Operator> aDomainConfiguration ) {
      Operator lOperator = new Operator();
      aDomainConfiguration.configure( lOperator );
      return CarrierBuilder.build( lOperator );
   }


   public static PartNoKey createPart() {
      return createPart( new NoopDomainConfiguration<Part>() );
   }


   public static PartNoKey createPart( DomainConfiguration<Part> aDomainConfiguration ) {
      Part lPart = new Part();
      aDomainConfiguration.configure( lPart );
      return PartBuilder.build( lPart );
   }


   public static PartGroupKey createPartGroup() {
      return createPartGroup( new NoopDomainConfiguration<PartGroup>() );
   }


   public static PartGroupKey
         createPartGroup( DomainConfiguration<PartGroup> aDomainConfiguration ) {
      PartGroup lPartGroup = new PartGroup();
      aDomainConfiguration.configure( lPartGroup );
      return PartGroupBuilder.build( lPartGroup );
   }


   public static PartRequestKey
         createPartRequest( DomainConfiguration<PartRequestEntity> aDomainConfiguration ) {
      PartRequestEntity lPartRequestEntity = new PartRequestEntity();
      aDomainConfiguration.configure( lPartRequestEntity );
      return PartRequestDomainBuilder.build( lPartRequestEntity );
   }


   public static EqpPlanningTypeKey
         createPlanningType( DomainConfiguration<PlanningType> aDomainConfiguration ) {
      PlanningType lPlanningType = new PlanningType();
      aDomainConfiguration.configure( lPlanningType );
      return PlanningTypeBuilder.build( lPlanningType );
   }


   public static PurchaseOrderKey
         createPurchaseOrder( DomainConfiguration<PurchaseOrder> aDomainConfiguration ) {
      PurchaseOrder lPurchaseOrder = new PurchaseOrder();
      aDomainConfiguration.configure( lPurchaseOrder );
      return PurchaseOrderBuilder.build( lPurchaseOrder );
   }


   public static PurchaseOrderKey createPurchaseOrder() {
      return createPurchaseOrder( new NoopDomainConfiguration<PurchaseOrder>() );
   }


   public static PurchaseOrderLineKey
         createPurchaseOrderLine( DomainConfiguration<PurchaseOrderLine> aDomainConfiguration ) {
      PurchaseOrderLine lPurchaseOrderLine = new PurchaseOrderLine();
      aDomainConfiguration.configure( lPurchaseOrderLine );
      return PurchaseOrderLineBuilder.build( lPurchaseOrderLine );
   }


   public static TaskKey
         createRepairOrder( DomainConfiguration<RepairOrder> aDomainConfiguration ) {
      RepairOrder lRepairOrder = new RepairOrder();
      aDomainConfiguration.configure( lRepairOrder );
      return RepairOrderBuilder.build( lRepairOrder );
   }


   public static TaskKey createRepetitiveTask() {
      return createRepetitiveTask( new NoopDomainConfiguration<RepetitiveTask>() );
   }


   public static TaskKey
         createRepetitiveTask( DomainConfiguration<RepetitiveTask> aDomainConfiguration ) {
      RepetitiveTask lRepetitiveTask = new RepetitiveTask();
      aDomainConfiguration.configure( lRepetitiveTask );
      return RepetitiveTaskBuilder.build( lRepetitiveTask );
   }


   public static TaskKey createRepl( DomainConfiguration<Repl> aDomainConfiguration ) {
      Repl lRepl = new Repl();
      aDomainConfiguration.configure( lRepl );
      return ReplBuilder.build( lRepl );
   }


   public static TaskKey
         createCorrectiveTask( DomainConfiguration<CorrectiveTask> aDomainConfiguration ) {
      CorrectiveTask lCorrectiveTask = new CorrectiveTask();
      aDomainConfiguration.configure( lCorrectiveTask );
      return CorrectiveTaskBuilder.build( lCorrectiveTask );
   }


   public static TaskKey createCorrectiveTask() {
      return createCorrectiveTask( new NoopDomainConfiguration<CorrectiveTask>() );
   }


   public static TaskKey createRequirement() {
      return createRequirement( new NoopDomainConfiguration<Requirement>() );
   }


   public static TaskKey
         createRequirement( DomainConfiguration<Requirement> aDomainConfiguration ) {
      Requirement lReq = new Requirement();
      aDomainConfiguration.configure( lReq );
      return RequirementBuilder.build( lReq );
   }


   public static TaskTaskKey createRequirementDefinition() {
      return createRequirementDefinition( new NoopDomainConfiguration<RequirementDefinition>() );
   }


   public static TaskTaskKey createRequirementDefinition(
         DomainConfiguration<RequirementDefinition> aDomainConfiguration ) {
      RequirementDefinition lReqDefinition = new RequirementDefinition();
      aDomainConfiguration.configure( lReqDefinition );
      return RequirementDefinitionBuilder.build( lReqDefinition );
   }


   public static ShipmentKey createShipment( DomainConfiguration<Shipment> aDomainConfiguration ) {
      Shipment lShipment = new Shipment();
      aDomainConfiguration.configure( lShipment );
      return ShipmentBuilder.build( lShipment );
   }


   public static ShipmentKey createShipment() {
      return createShipment( new NoopDomainConfiguration<Shipment>() );
   }


   public static ShipmentLineKey
         createShipmentLine( DomainConfiguration<ShipmentLine> aDomainConfiguration ) {
      ShipmentLine lShipmentLine = new ShipmentLine();
      aDomainConfiguration.configure( lShipmentLine );
      return ShipmentLineDomainBuilder.build( lShipmentLine );
   }


   public static TaskPartListKey createTaskDefinitionPartRequirement() {
      return createTaskDefinitionPartRequirement(
            new NoopDomainConfiguration<TaskDefinitionPartRequirement>() );
   }


   public static TaskPartListKey createTaskDefinitionPartRequirement(
         DomainConfiguration<TaskDefinitionPartRequirement> aDomainConfiguration ) {
      TaskDefinitionPartRequirement lPartRequirement = new TaskDefinitionPartRequirement();
      aDomainConfiguration.configure( lPartRequirement );
      return TaskDefinitionPartRequirementBuilder.build( lPartRequirement );
   }


   public static TaskKey createReferenceDocument() {
      return createReferenceDocument( new NoopDomainConfiguration<ReferenceDocument>() );
   }


   public static TaskKey
         createReferenceDocument( DomainConfiguration<ReferenceDocument> aDomainConfiguration ) {
      ReferenceDocument lRefDoc = new ReferenceDocument();
      aDomainConfiguration.configure( lRefDoc );
      return ReferenceDocumentBuilder.build( lRefDoc );
   }


   public static TaskTaskKey createReferenceDocumentDefinition() {
      return createReferenceDocumentDefinition(
            new NoopDomainConfiguration<ReferenceDocumentDefinition>() );
   }


   public static TaskTaskKey createReferenceDocumentDefinition(
         DomainConfiguration<ReferenceDocumentDefinition> aDomainConfiguration ) {
      ReferenceDocumentDefinition lRefDocDefinition = new ReferenceDocumentDefinition();
      aDomainConfiguration.configure( lRefDocDefinition );
      return ReferenceDocumentDefinitionBuilder.build( lRefDocDefinition );
   }


   public static void
         createRemovalRecord( DomainConfiguration<RemovalRecord> aDomainConfiguration ) {
      RemovalRecord lRemovalRecord = new RemovalRecord();
      aDomainConfiguration.configure( lRemovalRecord );
      RemovalRecordBuilder.build( lRemovalRecord );
   }


   public static InventoryKey createBatchInventory() {
      return createBatchInventory( new NoopDomainConfiguration<BatchInventory>() );
   }


   public static InventoryKey
         createBatchInventory( DomainConfiguration<BatchInventory> aDomainConfiguration ) {
      BatchInventory lBatch = new BatchInventory();
      aDomainConfiguration.configure( lBatch );
      return BatchInventoryBuilder.build( lBatch );
   }


   public static InventoryKey
         createKitInventory( DomainConfiguration<KitInventory> aDomainConfiguration ) {
      KitInventory lKit = new KitInventory();
      aDomainConfiguration.configure( lKit );
      return KitInventoryBuilder.build( lKit );
   }


   public static InventoryKey createSerializedInventory() {
      return createSerializedInventory( new NoopDomainConfiguration<SerializedInventory>() );
   }


   public static InventoryKey createSerializedInventory(
         DomainConfiguration<SerializedInventory> aDomainConfiguration ) {
      SerializedInventory lSer = new SerializedInventory();
      aDomainConfiguration.configure( lSer );
      return SerializedInventoryBuilder.build( lSer );
   }


   public static InventoryKey createTrackedInventory() {
      return createTrackedInventory( new NoopDomainConfiguration<TrackedInventory>() );
   }


   public static InventoryKey
         createTrackedInventory( DomainConfiguration<TrackedInventory> aDomainConfiguration ) {
      TrackedInventory lTrk = new TrackedInventory();
      aDomainConfiguration.configure( lTrk );
      return TrackedInventoryBuilder.build( lTrk );
   }


   public static UsageAdjustmentId
         createUsageAdjustment( DomainConfiguration<UsageAdjustment> aDomainConfiguration ) {
      UsageAdjustment lUsageAdjustment = new UsageAdjustment();
      aDomainConfiguration.configure( lUsageAdjustment );
      return UsageAdjustmentBuilder.build( lUsageAdjustment );
   }


   public static TaskKey createWorkPackage() {
      return createWorkPackage( new NoopDomainConfiguration<WorkPackage>() );
   }


   public static TaskKey
         createWorkPackage( DomainConfiguration<WorkPackage> aDomainConfiguration ) {
      WorkPackage lWorkPackage = new WorkPackage();
      aDomainConfiguration.configure( lWorkPackage );
      return WorkPackageBuilder.build( lWorkPackage );
   }


   public static OwnerKey createOwner() {

      // Creation of owner doesn't require an entity in place as of OPER-18486
      return OwnerBuilder.build();
   }


   public static OwnerKey createOwner( DomainConfiguration<Owner> domainConfiguration ) {
      Owner owner = new Owner();
      domainConfiguration.configure( owner );
      return OwnerBuilder.build( owner );
   }


   public static RefLabourSkillKey createLabourSkill() {
      return createLabourSkill( new NoopDomainConfiguration<LabourSkill>() );
   }


   public static HumanResourceKey createHumanResource() {
      return createHumanResource( new NoopDomainConfiguration<HumanResource>() );
   }


   public static HumanResourceKey
         createHumanResource( DomainConfiguration<HumanResource> aDomainConfiguration ) {
      HumanResource lHumanResource = new HumanResource();
      aDomainConfiguration.configure( lHumanResource );
      return HumanResourceBuilder.build( lHumanResource );

   }


   public static UserKey createUser() {
      return createUser( new NoopDomainConfiguration<User>() );
   }


   public static UserKey createUser( DomainConfiguration<User> aDomainConfiguration ) {
      User lUser = new User();
      aDomainConfiguration.configure( lUser );
      return UserBuilder.build( lUser );

   }


   public static OrgKey createOrganization() {
      return createOrganization( new NoopDomainConfiguration<Organization>() );
   }


   public static OrgKey
         createOrganization( DomainConfiguration<Organization> aDomainConfiguration ) {
      Organization lOrganization = new Organization();
      aDomainConfiguration.configure( lOrganization );
      return OrganizationBuilder.build( lOrganization );

   }


   public static RefLabourSkillKey
         createLabourSkill( DomainConfiguration<LabourSkill> aDomainConfiguration ) {
      LabourSkill lLabourSkill = new LabourSkill();
      aDomainConfiguration.configure( lLabourSkill );
      return LabourSkillBuilder.build( lLabourSkill );
   }


   public static TaskKey createComponentWorkPackage() {
      return createComponentWorkPackage( new NoopDomainConfiguration<ComponentWorkPackage>() );
   }


   public static TaskKey createComponentWorkPackage(
         DomainConfiguration<ComponentWorkPackage> aDomainConfiguration ) {
      ComponentWorkPackage lComponentWorkPackge = new ComponentWorkPackage();
      aDomainConfiguration.configure( lComponentWorkPackge );
      return ComponentWorkPackageBuilder.build( lComponentWorkPackge );
   }


   public static InventoryDamageKey
         createDamageRecord( DomainConfiguration<DamageRecord> domainConfiguration ) {
      DamageRecord damageRecord = new DamageRecord();
      domainConfiguration.configure( damageRecord );
      return DamageRecordBuilder.build( damageRecord );
   }


   public static FailDeferRefKey createDeferralReference() {
      return createDeferralReference( new NoopDomainConfiguration<DeferralReference>() );
   }


   public static FailDeferRefKey
         createDeferralReference( DomainConfiguration<DeferralReference> aDomainConfiguration ) {
      DeferralReference lDeferralReference = new DeferralReference();
      aDomainConfiguration.configure( lDeferralReference );
      return DeferralReferenceBuilder.build( lDeferralReference );
   }


   public static RefFailureSeverityKey
         createFailureSeverity( DomainConfiguration<FailureSeverity> domainConfiguration ) {
      FailureSeverity failureSeverity = new FailureSeverity();
      domainConfiguration.configure( failureSeverity );
      return FailureSeverityBuilder.build( failureSeverity );
   }


   public static TransferKey createTransfer( DomainConfiguration<Transfer> aDomainConfiguration ) {
      Transfer lTransfer = new Transfer();
      aDomainConfiguration.configure( lTransfer );
      return TransferBuilder.build( lTransfer );
   }


   public static RefTaskSubclassKey
         createTaskSubClass( DomainConfiguration<TaskSubClass> domainConfiguration ) {
      TaskSubClass taskSubClass = new TaskSubClass();
      domainConfiguration.configure( taskSubClass );
      return TaskSubClassBuilder.build( taskSubClass );
   }


   public static VendorKey createVendor() {
      return createVendor( new NoopDomainConfiguration<Vendor>() );
   }


   public static VendorKey createVendor( DomainConfiguration<Vendor> aDomainConfiguration ) {
      Vendor lVendor = new Vendor();
      aDomainConfiguration.configure( lVendor );
      return VendorBuilder.build( lVendor );
   }


   public static UsageSnapshot readUsageAtCompletion( TaskKey aTask, InventoryKey aInventory,
         DataTypeKey aUsageParameter ) {
      return UsageSnapshotReader.readUsageAtCompletion( aTask, aInventory, aUsageParameter );
   }


   public static TaskStepKey readRequirementDefinitionStep( TaskTaskKey requirementDefinition,
         int stepNumber ) {
      return RequirementDefinitionStepReader.read( requirementDefinition, stepNumber );
   }


   public static InventoryKey readSystem( InventoryKey aParent, String aSystemName ) {
      return SystemReader.read( aParent, aSystemName );
   }


   public static ConfigSlotKey readSubConfigurationSlot( ConfigSlotKey aParentConfigurationSlot,
         String aConfigurationSlotCode ) {
      return ConfigurationSlotReader.readSubConfigurationSlot( aParentConfigurationSlot,
            aConfigurationSlotCode );
   }


   public static ConfigSlotPositionKey readConfigurationSlotPosition(
         ConfigSlotKey aConfigurationSlot, String aConfigurationSlotPositionCode ) {
      return ConfigurationSlotReader.readConfigurationSlotPosition( aConfigurationSlot,
            aConfigurationSlotPositionCode );
   }


   public static PartGroupKey readPartGroup( ConfigSlotKey aConfigurationSlot,
         String aPartGroupCode ) {
      return PartGroupReader.read( aConfigurationSlot, aPartGroupCode );
   }


   public static ConfigSlotKey readRootConfigurationSlot( AssemblyKey aAssembly ) {
      return ConfigurationSlotReader.readRootConfigurationSlot( aAssembly );
   }


   public static PanelKey readPanel( AssemblyKey aAssembly, String aPanelCode ) {
      return PanelReader.readPanelByCode( aAssembly, aPanelCode );
   }


   public static DataTypeKey readUsageParameter( ConfigSlotKey aConfigurationSlot, String aCode ) {

      return UsageParameterReader.read( aConfigurationSlot, aCode );
   }


   public static IetmTopicKey readTechnicalReference( IetmDefinitionKey aIetm,
         String aTechnicalReferenceName ) {

      return IetmTechnicalReferenceReader.read( aIetm, aTechnicalReferenceName );
   }


   public static IetmTopicKey readAttachment( IetmDefinitionKey aIetm, String aAttachmentName ) {

      return IetmAttachmentReader.read( aIetm, aAttachmentName );
   }


   public static Map<DataTypeKey, BigDecimal> readCurrentUsage( InventoryKey aInventory ) {

      return CurrentUsageReader.read( aInventory );
   }


   public static FaultReferenceKey readFaultReference( FaultKey faultKey ) {

      return FaultReferenceReader.read( faultKey );
   }


   /**
    * Returns the date requested value for the repair or deferral reference. Required to approve a
    * reference.
    */
   public static FaultReferenceRequest readFaultReferenceRequest( FaultKey faultKey ) {
      return FaultReferenceRequestReader.read( faultKey );

   }


   /**
    * Return the matching labour requirment provided task skill and hours. There is no natural key
    * for labour requirement, Thus if more than 1 row match this criteria then 1 row return.
    */
   public static SchedLabourKey readLabourRequirement( TaskKey task, RefLabourSkillKey skill,
         BigDecimal scheduledHours ) {

      return LabourRequirementReader.read( task, skill, scheduledHours );

   }


   /**
    * Returns a list of labour records corresponding with the provided task
    */
   public static List<SchedLabourKey> readLabourRequirement( TaskKey task ) {

      return LabourRequirementReader.read( task );

   }


   public static List<SchedWPSignReqKey> readSignOffRequirements( TaskKey workpackage ) {
      return SignOffRequirementReader.read( workpackage );
   }


   public static List<TaskWeightAndBalanceKey>
         readWeightAndBalanceImpacts( TaskTaskKey taskTaskKey ) {

      return WeightAndBalanceImpactReader.read( taskTaskKey );
   }


   /**
    *
    * It creates the equation function in database for the calculated parameter.
    *
    * IMPORTANT: all data setup in the DB prior to calling this method will be explicitly
    * rolled-back! This is because the DDL executed within this method implicitly performs a commit
    * and we cannot commit that test data.
    */
   public static void createCalculatedParameterEquationFunction( Connection aDatabaseConnection,
         String aCreateEquationFunction ) throws SQLException {

      // Function creation is DDL which implicitly commits the transaction.
      // We perform explicit roll-back before function creation ensuring no data gets committed
      // accidentally.
      aDatabaseConnection.rollback();

      ProcedureStatement lStatement = new ProcedureStatement( aCreateEquationFunction );
      lStatement.prepare( aDatabaseConnection );
      lStatement.execute();
   }


   /**
    * It drops the equation function from the database for the calculated parameter.
    *
    * IMPORTANT: All data needed for assertions must be retrieved prior to calling
    * dropCalculatedParameterEquationFunction(). As it performs an explicit roll-back.
    *
    * ALSO, all assertions must be done after dropCalculatedParameterEquationFunction() to ensure
    * the equation gets removed from the DB.
    */
   public static void dropCalculatedParameterEquationFunction( Connection aDatabaseConnection,
         String aEquationFunctionName ) throws SQLException {

      // Function dropping is DDL which implicitly commits transaction.
      // We perform explicit roll-back before function drop ensuring no data gets committed
      // accidentally.
      aDatabaseConnection.rollback();

      // Drop the function that was used as a calculated parameter equation.
      String lDropFunctionStatement = "DROP FUNCTION " + aEquationFunctionName;

      ProcedureStatement lStatement = new ProcedureStatement( lDropFunctionStatement );
      lStatement.prepare( aDatabaseConnection );
      lStatement.execute();
   }


   public static PanelKey createPanel( DomainConfiguration<Panel> panelConfiguration ) {
      Panel panel = new Panel();
      panelConfiguration.configure( panel );
      return PanelBuilder.build( panel );
   }


   public static ZoneKey createZone( DomainConfiguration<Zone> zoneConfiguration ) {
      Zone zone = new Zone();
      zoneConfiguration.configure( zone );
      return ZoneBuilder.build( zone );
   }


   /**
    * Configures the domain entity
    *
    * @param <T>
    *           the domain entity builder
    */
   public interface DomainConfiguration<T> {

      void configure( T aBuilder );

   }

   private static final class NoopDomainConfiguration<T> implements DomainConfiguration<T> {

      /**
       * {@inheritDoc}
       */
      @Override
      public void configure( T aDomainObject ) {
         // no-op
      }
   }

}
