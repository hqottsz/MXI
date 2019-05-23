package com.mxi.mx.core.services.print;

import static com.mxi.mx.core.key.RefEventStatusKey.ACTV;

import java.time.Duration;
import java.util.Date;

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
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvLocPrinterKey;
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
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefPrinterTypeKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskInstPartKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskPartKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.services.print.model.WorkType;
import com.mxi.mx.core.services.req.PartRequestPrinterService;
import com.mxi.mx.core.services.req.PartRequestUtils;


public class PartRequestPrinterServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   private GlobalParametersFake iConfigParms;


   @Before
   public void setUp() {

      iConfigParms = new GlobalParametersFake( ParmTypeEnum.LOGIC.name() );
      iConfigParms.setString( PrintService.ENABLE_SILENT_PRINTING, "TRUE" );
      iConfigParms.setString( PrintService.PRINT_OPEN_ISSUE_TRANSFERS, "FALSE" );
      GlobalParameters.setInstance( iConfigParms );

   }


   @After
   public void tearDown() {
      GlobalParameters.setInstance( "LOGIC", null );
   }


   /***
    *
    * Create a work package with a job card that has a part installation requirement and available
    * part request and send it to silent background printing. Ensure that the correct work items and
    * document storage database data is generated. The process is asynchronous so the test coverage
    * of the service ends at successful work item generation and continues with work item processing
    * of PRINT_JOB and PRINT_ITEM work items
    *
    * This test is a basic test for a single, never printed issue ticket
    *
    */
   @Test
   public void printBackgroundWorkPackageIssueTicket() {

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
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PRAVAIL )
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

      // Simulate a user clicking the "Print Issue Tickets Button" and not touching any default
      // options
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            null, null, false, new InvLocPrinterKey( printer.toValueString() ) );

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


   /***
    *
    * Create a work package with a job card that has a part installation requirement and open part
    * request and send it to silent background printing. Ensure that the correct work items and
    * document storage database data is generated. The process is asynchronous so the test coverage
    * of the service ends at successful work item generation and continues with work item processing
    * of PRINT_JOB and PRINT_ITEM work items
    *
    * This test is a basic test for a single, never printed issue ticket that is in an OPEN state
    * while using the configuration parameter that allows printing of OPEN state part requirements
    *
    */
   @Test
   public void printBackgroundWorkPackageOpensIssueTicket() {

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

      // Simulate a user clicking the "Print Issue Tickets Button" and not touching any default
      // options
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            null, null, false, new InvLocPrinterKey( printer.toValueString() ) );

      // Expect no print job to be generated (Config parm not enabled)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Enable the configuration for printing OPEN status Part Requests
      GlobalParameters.getInstance( ParmTypeEnum.LOGIC.name() )
            .setString( PrintService.PRINT_OPEN_ISSUE_TRANSFERS, "TRUE" );

      // Simulate a user clicking the "Print Issue Tickets Button" and not touching any default
      // options
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            null, null, false, new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 1 print job to be generated (Config parm not enabled)
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

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


   /***
    *
    * Create a work package with a job card that has a part installation requirement and available
    * part request and send it to silent background printing. Ensure that the correct work items and
    * document storage database data is generated. The process is asynchronous so the test coverage
    * of the service ends at successful work item generation and continues with work item processing
    * of PRINT_JOB and PRINT_ITEM work items
    *
    * This test marks the part request as having already been printed. Tests that with reprint
    * disabled nothing happens and with re-print enabled, printing works as expected
    *
    */
   @Test
   public void rePrintBackgroundWorkPackageIssueTicket() {

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
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PRAVAIL )
            .requestedBy( hrKey ).withRequestedQuantity( 1 ).isNeededAt( location )
            .withIssueAccount( FncAccountKey.VOID ).withPrintedDate( new Date() ).build();

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

      // Simulate a user clicking the "Print Issue Tickets Button" and not touching any default
      // options
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            null, null, false, new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 0 print job to be generated (Reprinting is not enabled and this part request has
      // already been printed)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Simulate a user clicking the "Print Issue Tickets Button" and specifying the "reprint"
      // option
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            null, null, true, new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 1 print job to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

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


   /***
    *
    * Create a work package with a job card that has a part installation requirement and available
    * part request and send it to silent background printing. Ensure that the correct work items and
    * document storage database data is generated. The process is asynchronous so the test coverage
    * of the service ends at successful work item generation and continues with work item processing
    * of PRINT_JOB and PRINT_ITEM work items
    *
    * This test marks the part request as having already been printed. Tests the date filtering
    * option "Needed After" and the "reprint" option
    *
    */
   @Test
   public void rePrintBackgroundWorkPackageIssueTicketWithNeededByAfterDateFiltering() {

      Date neededByDate = new Date();

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
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PRAVAIL )
            .requestedBy( hrKey ).withRequestedQuantity( 1 ).isNeededAt( location )
            .withIssueAccount( FncAccountKey.VOID ).withPrintedDate( new Date() )
            .requiredBy( neededByDate ).build();

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

      // Simulate a user clicking the "Print Issue Tickets Button" and Specifying a needed
      // after date that is after the PR by 2 Hours
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            Date.from( neededByDate.toInstant().plus( Duration.ofHours( 2 ) ) ), null, true,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 0 print job to be generated (The PR is needed before the set filter)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Simulate a user clicking the "Print Issue Tickets Button" and specifying a valid filter
      // option where our PR should be returned (2 Hours Before our needed by date)
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            Date.from( neededByDate.toInstant().minus( Duration.ofHours( 2 ) ) ), null, true,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 1 print job to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

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


   /***
    *
    * Create a work package with a job card that has a part installation requirement and available
    * part request and send it to silent background printing. Ensure that the correct work items and
    * document storage database data is generated. The process is asynchronous so the test coverage
    * of the service ends at successful work item generation and continues with work item processing
    * of PRINT_JOB and PRINT_ITEM work items
    *
    * This test marks the part request as having already been printed. Tests the date filtering
    * option "Needed Before" and the "reprint" option
    *
    */
   @Test
   public void rePrintBackgroundWorkPackageIssueTicketWithNeededByBeforeDateFiltering() {

      Date neededByDate = new Date();

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
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PRAVAIL )
            .requestedBy( hrKey ).withRequestedQuantity( 1 ).isNeededAt( location )
            .withIssueAccount( FncAccountKey.VOID ).withPrintedDate( new Date() )
            .requiredBy( neededByDate ).build();

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

      // Simulate a user clicking the "Print Issue Tickets Button" and Specifying a needed
      // before date that is before the PR by 2 Hours
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            null, Date.from( neededByDate.toInstant().minus( Duration.ofHours( 2 ) ) ), true,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 0 print job to be generated (The PR is needed before the set filter)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Simulate a user clicking the "Print Issue Tickets Button" and specifying a valid filter
      // option where our PR should be returned (2 Hours After our needed by date filter)
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            null, Date.from( neededByDate.toInstant().plus( Duration.ofHours( 2 ) ) ), true,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 1 print job to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

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


   /***
    *
    * Create a work package with a job card that has a part installation requirement and available
    * part request and send it to silent background printing. Ensure that the correct work items and
    * document storage database data is generated. The process is asynchronous so the test coverage
    * of the service ends at successful work item generation and continues with work item processing
    * of PRINT_JOB and PRINT_ITEM work items
    *
    * This test marks the part request as having already been printed. Tests the date filtering
    * option "Needed Before" and "Needed After" at the same time combined with the "reprint" option
    *
    */
   @Test
   public void rePrintBackgroundWorkPackageIssueTicketWithNeededByBetweenDateFiltering() {

      Date neededByDate = new Date();

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
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PRAVAIL )
            .requestedBy( hrKey ).withRequestedQuantity( 1 ).isNeededAt( location )
            .withIssueAccount( FncAccountKey.VOID ).withPrintedDate( new Date() )
            .requiredBy( neededByDate ).build();

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

      // Simulate a user clicking the "Print Issue Tickets Button" and Specifying a needed
      // before date that is before the PR by 2 Hours and a needed after date that is after the PR
      // by 2 Hours
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            Date.from( neededByDate.toInstant().plus( Duration.ofHours( 2 ) ) ),
            Date.from( neededByDate.toInstant().minus( Duration.ofHours( 2 ) ) ), true,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 0 print job to be generated (The PR is outside of the set filter)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Simulate a user clicking the "Print Issue Tickets Button" and specifying a valid filter
      // option where our PR should be returned (the PR is exactly in the middle of a between range
      // of 2 Hours behind and 2 Hours ahead)
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            Date.from( neededByDate.toInstant().minus( Duration.ofHours( 2 ) ) ),
            Date.from( neededByDate.toInstant().plus( Duration.ofHours( 2 ) ) ), true,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 1 print job to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

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


   /***
    *
    * Create a work package with a job card that has a part installation requirement and available
    * part request and send it to silent background printing. Ensure that the correct work items and
    * document storage database data is generated. The process is asynchronous so the test coverage
    * of the service ends at successful work item generation and continues with work item processing
    * of PRINT_JOB and PRINT_ITEM work items
    *
    * This test marks the part request as having already been printed. Tests the date filtering
    * option "Needed After"
    *
    */
   @Test
   public void printBackgroundWorkPackageIssueTicketWithNeededByAfterDateFiltering() {

      Date neededByDate = new Date();

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
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PRAVAIL )
            .requestedBy( hrKey ).withRequestedQuantity( 1 ).isNeededAt( location )
            .withIssueAccount( FncAccountKey.VOID ).requiredBy( neededByDate ).build();

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

      // Simulate a user clicking the "Print Issue Tickets Button" and Specifying a needed
      // after date that is after the PR by 2 Hours
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            Date.from( neededByDate.toInstant().plus( Duration.ofHours( 2 ) ) ), null, false,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 0 print job to be generated (The PR is needed before the set filter)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Simulate a user clicking the "Print Issue Tickets Button" and specifying a valid filter
      // option where our PR should be returned (2 Hours Before our needed by date)
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            Date.from( neededByDate.toInstant().minus( Duration.ofHours( 2 ) ) ), null, false,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 1 print job to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

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


   /***
    *
    * Create a work package with a job card that has a part installation requirement and available
    * part request and send it to silent background printing. Ensure that the correct work items and
    * document storage database data is generated. The process is asynchronous so the test coverage
    * of the service ends at successful work item generation and continues with work item processing
    * of PRINT_JOB and PRINT_ITEM work items
    *
    * This test marks the part request as having already been printed. Tests the date filtering
    * option "Needed Before"
    *
    */
   @Test
   public void printBackgroundWorkPackageIssueTicketWithNeededByBeforeDateFiltering() {

      Date neededByDate = new Date();

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
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PRAVAIL )
            .requestedBy( hrKey ).withRequestedQuantity( 1 ).isNeededAt( location )
            .withIssueAccount( FncAccountKey.VOID ).requiredBy( neededByDate ).build();

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

      // Simulate a user clicking the "Print Issue Tickets Button" and Specifying a needed
      // before date that is before the PR by 2 Hours
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            null, Date.from( neededByDate.toInstant().minus( Duration.ofHours( 2 ) ) ), false,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 0 print job to be generated (The PR is needed before the set filter)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Simulate a user clicking the "Print Issue Tickets Button" and specifying a valid filter
      // option where our PR should be returned (2 Hours After our needed by date filter)
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            null, Date.from( neededByDate.toInstant().plus( Duration.ofHours( 2 ) ) ), false,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 1 print job to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

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


   /***
    *
    * Create a work package with a job card that has a part installation requirement and available
    * part request and send it to silent background printing. Ensure that the correct work items and
    * document storage database data is generated. The process is asynchronous so the test coverage
    * of the service ends at successful work item generation and continues with work item processing
    * of PRINT_JOB and PRINT_ITEM work items
    *
    * This test marks the part request as having already been printed. Tests the date filtering
    * option "Needed Before" and "Needed After" at the same time
    *
    */
   @Test
   public void printBackgroundWorkPackageIssueTicketWithNeededByBetweenDateFiltering() {

      Date neededByDate = new Date();

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
            .forPartRequirement( taskInstallPartKey ).withStatus( RefEventStatusKey.PRAVAIL )
            .requestedBy( hrKey ).withRequestedQuantity( 1 ).isNeededAt( location )
            .withIssueAccount( FncAccountKey.VOID ).requiredBy( neededByDate ).build();

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

      // Simulate a user clicking the "Print Issue Tickets Button" and Specifying a needed
      // before date that is before the PR by 2 Hours and a needed after date that is after the PR
      // by 2 Hours
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            Date.from( neededByDate.toInstant().plus( Duration.ofHours( 2 ) ) ),
            Date.from( neededByDate.toInstant().minus( Duration.ofHours( 2 ) ) ), false,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 0 print job to be generated (The PR is outside of the set filter)
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

      Assert.assertEquals( 0, lQs.getRowCount() );

      // Simulate a user clicking the "Print Issue Tickets Button" and specifying a valid filter
      // option where our PR should be returned (the PR is exactly in the middle of a between range
      // of 2 Hours behind and 2 Hours ahead)
      new PartRequestPrinterService().printIssueTicketsForWorkPackage( workPackage.getEventKey(),
            Date.from( neededByDate.toInstant().minus( Duration.ofHours( 2 ) ) ),
            Date.from( neededByDate.toInstant().plus( Duration.ofHours( 2 ) ) ), false,
            new InvLocPrinterKey( printer.toValueString() ) );

      // Expect 1 print job to be generated
      lArgs = new DataSetArgument();
      lArgs.add( "type", WorkType.PRINT_JOB.name() );
      lQs = QuerySetFactory.getInstance().executeQuery( "utl_work_item", lArgs, "data" );

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
}
