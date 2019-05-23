package com.mxi.mx.core.services.print;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.builder.InventoryBuilder;
import com.mxi.am.domain.builder.OwnerDomainBuilder;
import com.mxi.am.domain.builder.PartRequestBuilder;
import com.mxi.am.domain.builder.PartRequirementDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.key.TimeZoneKey;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.LocationPrinterKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.PartRequestKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefJobTypeKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPrinterTypeKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.key.TransferKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.services.print.model.WorkType;
import com.mxi.mx.core.services.req.PartRequestUtils;
import com.mxi.mx.core.services.transfer.CreatePutAwayTO;
import com.mxi.mx.core.services.transfer.TransferService;


public class PrintServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public OperateAsUserRule operateAsUser = new OperateAsUserRule( 1992, "currentuser" );

   private GlobalParametersFake iConfigParms;


   @Before
   public void setUp() {

      iConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      iConfigParms.setString( PrintService.ENABLE_SILENT_PRINTING, "TRUE" );
      GlobalParameters.setInstance( iConfigParms );

      Domain.createHumanResource( hr -> {
         hr.setUser( new UserKey( 1992 ) );
      } );

   }


   @After
   public void tearDown() {
      GlobalParameters.setInstance( "LOGIC", null );
   }


   /***
    *
    * Create a work package with a job card and send it to silent background printing against an
    * EXTERNAL type printer
    *
    * Asserts that no core logic is done.
    */
   @Test
   public void printBackgroundWorkPackageJobCardsExternalPrinter() {

      // Create a location for the printer and work to be done
      final LocationKey location = Domain.createLocation( config -> {
         config.setCode( "TEST" );
      } );

      // Create a file printer at the location
      final LocationPrinterKey printer = Domain.createLocationPrinter( config -> {
         config.setLocation( location );
         config.setPrinterSdesc( "Test Printer" );
         config.setPrinterType( RefPrinterTypeKey.EXTERNAL );
      } );

      // Create a Job Card printer against this printer
      Domain.createLocationPrinterJob( config -> {
         config.setDefault( true );
         config.setPrinter( printer );
         config.setJobType( RefJobTypeKey.TASK_CRD );
      } );

      // Create a Requirement linked to a JIC
      final TaskTaskKey jicDefn = Domain.createJobCardDefinition();
      final TaskTaskKey requirementDefn = Domain.createRequirementDefinition( config -> {
         config.addJobCardDefinition( jicDefn );

      } );

      final InventoryKey aircraft = Domain.createAircraft( config -> {
         config.setLocation( location );
      } );

      final TaskKey jobCard = Domain.createJobCard( config -> {
         config.setInventory( aircraft );

      } );

      final TaskKey requirement = Domain.createRequirement( config -> {
         config.setInventory( aircraft );
         config.setDefinition( requirementDefn );
         config.setStatus( ACTV );
         config.addJobCard( jobCard );

      } );

      // Add the Requirement and Job Card to a work package and commit the work scope
      TaskKey workPackage = Domain.createWorkPackage( config -> {
         config.setLocation( location );
         config.setAircraft( aircraft );
         config.addTask( requirement );
         config.addWorkScopeTask( requirement );

      } );

      // Ask the Print Service to print the task card in the work package in the background
      new PrintService().printBackground( printer, workPackage.getEventKey(),
            RefJobTypeKey.TASK_CRD, jobCard.getEventKey() );

      // Expect 0 print job to be generated
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

   }


   /***
    *
    * Create a work package with a job card and send it to silent background printing. Ensure that
    * the correct work items and document storage database data is generated. The process is
    * asynchronous so the test coverage of the service ends at successful work item generation and
    * continues with work item processing of PRINT_JOB and PRINT_ITEM work items
    *
    */
   @Test
   public void printBackgroundWorkPackageJobCards() {

      // Create a location for the printer and work to be done
      final LocationKey location = Domain.createLocation( config -> {
         config.setCode( "TEST" );
      } );

      // Create a file printer at the location
      final LocationPrinterKey printer = Domain.createLocationPrinter( config -> {
         config.setLocation( location );
         config.setPrinterParm( "/tmp/printer" );
         config.setPrinterSdesc( "Test Printer" );
         config.setPrinterType( RefPrinterTypeKey.FILE );
      } );

      // Create a Job Card printer against this printer
      Domain.createLocationPrinterJob( config -> {
         config.setDefault( true );
         config.setPrinter( printer );
         config.setJobType( RefJobTypeKey.TASK_CRD );
      } );

      // Create a Requirement linked to a JIC
      final TaskTaskKey jicDefn = Domain.createJobCardDefinition();
      final TaskTaskKey requirementDefn = Domain.createRequirementDefinition( config -> {
         config.addJobCardDefinition( jicDefn );

      } );

      final InventoryKey aircraft = Domain.createAircraft( config -> {
         config.setLocation( location );
      } );

      final TaskKey jobCard = Domain.createJobCard( config -> {
         config.setInventory( aircraft );

      } );

      final TaskKey requirement = Domain.createRequirement( config -> {
         config.setInventory( aircraft );
         config.setDefinition( requirementDefn );
         config.setStatus( ACTV );
         config.addJobCard( jobCard );

      } );

      // Add the Requirement and Job Card to a work package and commit the work scope
      TaskKey workPackage = Domain.createWorkPackage( config -> {
         config.setLocation( location );
         config.setAircraft( aircraft );
         config.addTask( requirement );
         config.addWorkScopeTask( requirement );

      } );

      // Ask the Print Service to print the task card in the work package in the background
      new PrintService().printBackground( printer, workPackage.getEventKey(),
            RefJobTypeKey.TASK_CRD, jobCard.getEventKey() );

      // Expect 1 print job to be generated
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 1, lQs.getRowCount() );

      // Ensure the work package is properly mentioned in the print job data
      lQs.next();
      Assert.assertTrue(
            lQs.getString( "data" ).contains( workPackage.getEventKey().toValueString() ) );

      // Expect 1 print item to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_ITEM.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 1, lQs.getRowCount() );

      // Ensure the job card is properly mentioned in the print item data
      lQs.next();
      Assert.assertTrue(
            lQs.getString( "data" ).contains( jobCard.getEventKey().toValueString() ) );

      // Expect 1 document stub to be generated
      lArgs = new DataSetArgument();
      lQs = QuerySetFactory.getInstance().executeQuery( "cor_blob_print", lArgs, "doc_db_id",
            "doc_id" );

      Assert.assertEquals( 1, lQs.getRowCount() );

   }


   /***
    *
    * Create a work package with a issue tickets and send it to silent background printing against
    * an EXTERNAL type printer
    *
    * Asserts that no core logic is executed.
    *
    */
   @Test
   public void printBackgroundWorkPackageIssueTicketsExternalPrinter() {

      // Create a location for the printer and work to be done
      final LocationKey location = Domain.createLocation( config -> {
         config.setCode( "TEST" );
      } );

      // Create a file printer at the location
      final LocationPrinterKey printer = Domain.createLocationPrinter( config -> {
         config.setLocation( location );
         config.setPrinterSdesc( "Test Custom Printer" );
         config.setPrinterType( RefPrinterTypeKey.EXTERNAL );
      } );

      // Create an Issue Transfer Ticket printer against this printer
      Domain.createLocationPrinterJob( config -> {
         config.setDefault( true );
         config.setPrinter( printer );
         config.setJobType( RefJobTypeKey.ISSUE_TX );
      } );

      // Create a Requirement linked to a JIC
      final TaskTaskKey jicDefn = Domain.createJobCardDefinition();

      final PartGroupKey partGroup = Domain.createPartGroup( config -> {
         config.setCode( "TESTGROUP" );
         config.setInventoryClass( RefInvClassKey.TRK );
         config.setName( "TestGroup" );
      } );

      final PartNoKey partNumber = Domain.createPart( config -> {
         config.setCode( "TESTPART" );
         config.setPartStatus( RefPartStatusKey.ACTV );
         config.setPartGroup( partGroup, true );
      } );

      Domain.createTaskDefinitionPartRequirement( config -> {
         config.setTaskDefinition( jicDefn );
         config.setIsInstall( true );
         config.setRequiredQuantity( 1.0d );
         config.setReqAction( RefReqActionKey.REQ );
         config.setPartGroup( partGroup );
         config.setSpecificPart( partNumber );
      } );

      final TaskTaskKey requirementDefn = Domain.createRequirementDefinition( config -> {
         config.addJobCardDefinition( jicDefn );

      } );

      final InventoryKey aircraft = Domain.createAircraft( config -> {
         config.setLocation( location );
      } );

      final TaskKey jobCard = Domain.createJobCard( config -> {
         config.setInventory( aircraft );

      } );

      InventoryKey installedInventory = new InventoryBuilder().withPartNo( partNumber )
            .withSerialNo( "123" ).atLocation( location ).withCondition( RefInvCondKey.RFI )
            .withOwner( new OwnerDomainBuilder().build() ).build();

      TaskPartKey taskPartKey = new PartRequirementDomainBuilder( jobCard ).forPart( partNumber )
            .forPartGroup( partGroup ).withInstallPart( partNumber )
            .withInstallInventory( installedInventory ).withInstallQuantity( 1 )
            .withInstallSerialNumber( "123" ).build();

      TaskInstPartKey taskInstallPartKey = new TaskInstPartKey( taskPartKey, 1 );

      HumanResourceKey hrKey = Domain.createHumanResource();

      // Create a part request that we can print an issue ticket from
      final PartRequestKey partRequest = new PartRequestBuilder().forTask( jobCard )
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PROPEN )
            .requestedBy( hrKey ).withRequestedQuantity( 1 ).isNeededAt( location )
            .withIssueAccount( FncAccountKey.VOID ).build();

      final TaskKey requirement = Domain.createRequirement( config -> {
         config.setInventory( aircraft );
         config.setDefinition( requirementDefn );
         config.setStatus( ACTV );
         config.addJobCard( jobCard );

      } );

      // Add the Requirement and Job Card to a work package and commit the work scope
      TaskKey workPackage = Domain.createWorkPackage( config -> {
         config.setLocation( location );
         config.setAircraft( aircraft );
         config.addTask( requirement );
         config.addWorkScopeTask( requirement );

      } );

      // Ask the Print Service to print the part request as an issue ticket in the work package in
      // the background
      try {
         new PrintService().printBackground( location, null, RefJobTypeKey.ISSUE_TX, partRequest );
      } catch ( NoDefaultPrinterForLocationException e ) {
         Assert.fail();
      }

      // Expect 0 print job to be generated
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Expect 0 print item to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_ITEM.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

   }


   /***
    *
    * Create a work package with a issue tickets and send it to silent background printing. Ensure
    * that the correct work items and document storage database data is generated. The process is
    * asynchronous so the test coverage of the service ends at successful work item generation and
    * continues with work item processing of PRINT_JOB and PRINT_ITEM work items
    *
    */
   @Test
   public void printBackgroundWorkPackageIssueTickets() {

      // Create a location for the printer and work to be done
      final LocationKey location = Domain.createLocation( config -> {
         config.setCode( "TEST" );
      } );

      // Create a file printer at the location
      final LocationPrinterKey printer = Domain.createLocationPrinter( config -> {
         config.setLocation( location );
         config.setPrinterParm( "/tmp/printer" );
         config.setPrinterSdesc( "Test Printer" );
         config.setPrinterType( RefPrinterTypeKey.FILE );
      } );

      // Create an Issue Transfer Ticket printer against this printer
      Domain.createLocationPrinterJob( config -> {
         config.setDefault( true );
         config.setPrinter( printer );
         config.setJobType( RefJobTypeKey.ISSUE_TX );
      } );

      // Create a Requirement linked to a JIC
      final TaskTaskKey jicDefn = Domain.createJobCardDefinition();

      final PartGroupKey partGroup = Domain.createPartGroup( config -> {
         config.setCode( "TESTGROUP" );
         config.setInventoryClass( RefInvClassKey.TRK );
         config.setName( "TestGroup" );
      } );

      final PartNoKey partNumber = Domain.createPart( config -> {
         config.setCode( "TESTPART" );
         config.setPartStatus( RefPartStatusKey.ACTV );
         config.setPartGroup( partGroup, true );
      } );

      Domain.createTaskDefinitionPartRequirement( config -> {
         config.setTaskDefinition( jicDefn );
         config.setIsInstall( true );
         config.setRequiredQuantity( 1.0d );
         config.setReqAction( RefReqActionKey.REQ );
         config.setPartGroup( partGroup );
         config.setSpecificPart( partNumber );
      } );

      final TaskTaskKey requirementDefn = Domain.createRequirementDefinition( config -> {
         config.addJobCardDefinition( jicDefn );

      } );

      final InventoryKey aircraft = Domain.createAircraft( config -> {
         config.setLocation( location );
      } );

      final TaskKey jobCard = Domain.createJobCard( config -> {
         config.setInventory( aircraft );

      } );

      InventoryKey installedInventory = new InventoryBuilder().withPartNo( partNumber )
            .withSerialNo( "123" ).atLocation( location ).withCondition( RefInvCondKey.RFI )
            .withOwner( new OwnerDomainBuilder().build() ).build();

      TaskPartKey taskPartKey = new PartRequirementDomainBuilder( jobCard ).forPart( partNumber )
            .forPartGroup( partGroup ).withInstallPart( partNumber )
            .withInstallInventory( installedInventory ).withInstallQuantity( 1 )
            .withInstallSerialNumber( "123" ).build();

      TaskInstPartKey taskInstallPartKey = new TaskInstPartKey( taskPartKey, 1 );

      HumanResourceKey hrKey = Domain.createHumanResource();

      // Create a part request that we can print an issue ticket from
      final PartRequestKey partRequest = new PartRequestBuilder().forTask( jobCard )
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PROPEN )
            .requestedBy( hrKey ).withRequestedQuantity( 1 ).isNeededAt( location )
            .withIssueAccount( FncAccountKey.VOID ).build();

      final TaskKey requirement = Domain.createRequirement( config -> {
         config.setInventory( aircraft );
         config.setDefinition( requirementDefn );
         config.setStatus( ACTV );
         config.addJobCard( jobCard );

      } );

      // Add the Requirement and Job Card to a work package and commit the work scope
      TaskKey workPackage = Domain.createWorkPackage( config -> {
         config.setLocation( location );
         config.setAircraft( aircraft );
         config.addTask( requirement );
         config.addWorkScopeTask( requirement );

      } );

      // Ask the Print Service to print the part request as an issue ticket in the work package in
      // the background
      try {
         new PrintService().printBackground( location, null, RefJobTypeKey.ISSUE_TX, partRequest );
      } catch ( NoDefaultPrinterForLocationException e ) {
         Assert.fail();
      }

      // Expect 1 print job to be generated
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 1, lQs.getRowCount() );

      // Expect 1 print item to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_ITEM.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 1, lQs.getRowCount() );

      // Ensure the part request key is properly mentioned in the print item data
      lQs.next();
      Assert.assertTrue(
            lQs.getString( "data" ).contains( partRequest.getEventKey().toValueString() ) );

      // Expect 1 document stub to be generated
      lArgs = new DataSetArgument();
      lQs = QuerySetFactory.getInstance().executeQuery( "cor_blob_print", lArgs, "doc_db_id",
            "doc_id", "filename_desc" );

      Assert.assertEquals( 1, lQs.getRowCount() );

      // Check that the output file name contains the issue ticket barcode
      lQs.next();
      Assert.assertTrue( lQs.getString( "filename_desc" )
            .contains( new PartRequestUtils().getBarcode( partRequest ) ) );

   }


   /**
    * create a printer, location, inventory and then generate a put away transfer and send it to
    * silent background printing against an EXTERNAL type printer
    *
    * Asserts that no core logic is executed.
    *
    */
   @Test
   public void printBackgroundPutawayTransferTicketsExternalPrinter()
         throws MxException, TriggerException {

      // Configure the logic parameters for silent background putaway printing
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC.name() ).setString( "AUTO_COMPLETE_PUTAWAY",
            "FALSE" );

      GlobalParameters.getInstance( ParmTypeEnum.LOGIC.name() )
            .setString( "AUTO_PRINT_PUTAWAY_TICKET", "TRUE" );

      // Create a location for the printer and work to be done
      final LocationKey location = Domain.createLocation( config -> {
         config.setCode( "TEST" );
      } );

      // Create a file printer at the location
      final LocationPrinterKey printer = Domain.createLocationPrinter( config -> {
         config.setLocation( location );
         config.setPrinterSdesc( "Test Printer" );
         config.setPrinterType( RefPrinterTypeKey.EXTERNAL );
      } );

      // Create an Putaway Transfer Ticket printer against this printer
      Domain.createLocationPrinterJob( config -> {
         config.setDefault( true );
         config.setPrinter( printer );
         config.setJobType( RefJobTypeKey.PAWAY_TX );
      } );

      final PartGroupKey partGroup = Domain.createPartGroup( config -> {
         config.setCode( "TESTGROUP" );
         config.setInventoryClass( RefInvClassKey.TRK );
         config.setName( "TestGroup" );
      } );

      final PartNoKey partNumber = Domain.createPart( config -> {
         config.setCode( "TESTPART" );
         config.setPartStatus( RefPartStatusKey.ACTV );
         config.setPartGroup( partGroup, true );
      } );

      InventoryKey inventory = new InventoryBuilder().withPartNo( partNumber ).withSerialNo( "123" )
            .atLocation( location ).withClass( RefInvClassKey.SER )
            .withCondition( RefInvCondKey.RFI ).withOwner( new OwnerDomainBuilder().build() )
            .build();

      // Create a Store type location
      Domain.createLocation( aLocation -> {
         aLocation.setCode( "ATL" );
         aLocation.setName( "Atlanta" );
         aLocation.setTimeZone( TimeZoneKey.LONDON );
         aLocation.setType( RefLocTypeKey.STORE );
      } );

      // Create a Bin location
      Domain.createLocation( aLocation -> {
         aLocation.setCode( "ATL/BIN" );
         aLocation.setName( "Atlanta" );
         aLocation.setType( RefLocTypeKey.BIN );
      } );

      // Prepare a putaway transfer the same way the UI would do it
      CreatePutAwayTO lCreatePutAwayTO = new CreatePutAwayTO();
      lCreatePutAwayTO.setInventory( inventory, "Inventory" );
      lCreatePutAwayTO.setLocation( "ATL/BIN", "Put Away Location" );
      lCreatePutAwayTO.setCreateBinOption( "MAKE_THIS_STANDARD_BIN" );
      lCreatePutAwayTO.setQuantity( 1.0 );

      // Call the service object to create put away (NOTE: this will result in the silent print
      // happening during normal business logic execution)
      TransferKey putaway = TransferService.createPutAway( lCreatePutAwayTO.getInventory(),
            lCreatePutAwayTO.getLocation(), lCreatePutAwayTO.getQuantity().doubleValue(),
            lCreatePutAwayTO.getCreateBinOption(), false );

      // Expect 0 print job to be generated
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Expect 0 print item to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_ITEM.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

   }


   /**
    * create a printer, location, inventory and then generate a put away transfer and send it to
    * silent background printing. Ensure that the correct work items and document storage database
    * data is generated. The process is asynchronous so the test coverage of the service ends at
    * successful work item generation and continues with work item processing of PRINT_JOB and
    * PRINT_ITEM work items
    *
    */
   @Test
   public void printBackgroundPutawayTransferTickets() throws MxException, TriggerException {

      // Configure the logic parameters for silent background putaway printing
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC.name() ).setString( "AUTO_COMPLETE_PUTAWAY",
            "FALSE" );

      GlobalParameters.getInstance( ParmTypeEnum.LOGIC.name() )
            .setString( "AUTO_PRINT_PUTAWAY_TICKET", "TRUE" );

      // Create a location for the printer and work to be done
      final LocationKey location = Domain.createLocation( config -> {
         config.setCode( "TEST" );
      } );

      // Create a file printer at the location
      final LocationPrinterKey printer = Domain.createLocationPrinter( config -> {
         config.setLocation( location );
         config.setPrinterParm( "/tmp/printer" );
         config.setPrinterSdesc( "Test Printer" );
         config.setPrinterType( RefPrinterTypeKey.FILE );
      } );

      // Create an Putaway Transfer Ticket printer against this printer
      Domain.createLocationPrinterJob( config -> {
         config.setDefault( true );
         config.setPrinter( printer );
         config.setJobType( RefJobTypeKey.PAWAY_TX );
      } );

      final PartGroupKey partGroup = Domain.createPartGroup( config -> {
         config.setCode( "TESTGROUP" );
         config.setInventoryClass( RefInvClassKey.TRK );
         config.setName( "TestGroup" );
      } );

      final PartNoKey partNumber = Domain.createPart( config -> {
         config.setCode( "TESTPART" );
         config.setPartStatus( RefPartStatusKey.ACTV );
         config.setPartGroup( partGroup, true );
      } );

      InventoryKey inventory = new InventoryBuilder().withPartNo( partNumber ).withSerialNo( "123" )
            .atLocation( location ).withClass( RefInvClassKey.SER )
            .withCondition( RefInvCondKey.RFI ).withOwner( new OwnerDomainBuilder().build() )
            .build();

      // Create a Store type location
      Domain.createLocation( aLocation -> {
         aLocation.setCode( "ATL" );
         aLocation.setName( "Atlanta" );
         aLocation.setTimeZone( TimeZoneKey.LONDON );
         aLocation.setType( RefLocTypeKey.STORE );
      } );

      // Create a Bin location
      Domain.createLocation( aLocation -> {
         aLocation.setCode( "ATL/BIN" );
         aLocation.setName( "Atlanta" );
         aLocation.setType( RefLocTypeKey.BIN );
      } );

      // Prepare a putaway transfer the same way the UI would do it
      CreatePutAwayTO lCreatePutAwayTO = new CreatePutAwayTO();
      lCreatePutAwayTO.setInventory( inventory, "Inventory" );
      lCreatePutAwayTO.setLocation( "ATL/BIN", "Put Away Location" );
      lCreatePutAwayTO.setCreateBinOption( "MAKE_THIS_STANDARD_BIN" );
      lCreatePutAwayTO.setQuantity( 1.0 );

      // Call the service object to create put away (NOTE: this will result in the silent print
      // happening during normal business logic execution)
      TransferKey putaway = TransferService.createPutAway( lCreatePutAwayTO.getInventory(),
            lCreatePutAwayTO.getLocation(), lCreatePutAwayTO.getQuantity().doubleValue(),
            lCreatePutAwayTO.getCreateBinOption(), false );

      // Expect 1 print job to be generated
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 1, lQs.getRowCount() );

      // Expect 1 print item to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_ITEM.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 1, lQs.getRowCount() );

      // Ensure the transfer key is properly mentioned in the print item data
      lQs.next();
      Assert.assertTrue(
            lQs.getString( "data" ).contains( putaway.getEventKey().toValueString() ) );

      // Expect 1 document stub to be generated
      lArgs = new DataSetArgument();
      lQs = QuerySetFactory.getInstance().executeQuery( "cor_blob_print", lArgs, "doc_db_id",
            "doc_id", "filename_desc" );

      Assert.assertEquals( 1, lQs.getRowCount() );

      // Check that the output file name contains the putaway ticket barcode
      lQs.next();
      Assert.assertTrue(
            lQs.getString( "filename_desc" ).contains( TransferService.get( putaway ).getId() ) );

   }
}
