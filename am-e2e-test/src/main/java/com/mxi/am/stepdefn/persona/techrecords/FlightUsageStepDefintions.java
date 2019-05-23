
package com.mxi.am.stepdefn.persona.techrecords;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Assert;

import com.mxi.am.driver.integration.flight.UsageMessageDriver;
import com.mxi.am.driver.integrationtesting.IntegrationMessageUtils;
import com.mxi.am.driver.query.InventoryQueriesDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.inventorydetailspanes.DetailsPaneDriver;
import com.mxi.xml.xsd.core.flights.usage.x12.UsagesDocument;
import com.mxi.xml.xsd.core.flights.usagesResponse.x10.UsagesResponseDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class FlightUsageStepDefintions {

   @Inject
   public InventoryQueriesDriver iInventoryQueriesDriver;

   @Inject
   public NavigationDriver iNavDriver;

   @Inject
   public InventoryDetailsPageDriver iInventoryDetailsPageDriver;

   // Variables to be used for assembling of inbound Usage message
   private final static String USAGE_ATTRIBUTES_NAME = "name";
   private final static String USAGE_ATTRIBUTES_COLLECTION_DATE = "collection-date";

   private final static String USAGE_ATTRIBUTES_PROCESS_AS_HISTORIC = "process-as-historic";
   private final static String USAGE_ATTRIBUTES_DOCUMENT_REFERENCE = "document-reference";
   private final static String USAGE_ATTRIBUTES_EXTERNAL_IDENTIFIER = "external-identifier";
   private final static String ASSEMBLY_OEM_SN = "oem-serial-number";
   private final static String ASSEMBLY_OEM_PN = "oem-part-number";
   private final static String AIRCRAFT_IDENTIFIER_REGISTRATION_CODE = "registration-code";
   private final static String COLLECTED_USAGE_USAGE_PARM = "usage-parm";
   private final static String COLLECTED_USAGE_VALUE_TSN = "tsn";
   private final static String COLLECTED_USAGE_VALUE_TSO = "tso";
   private final static String COLLECTED_USAGE_VALUE_TSI = "tsi";
   private Double usageTSN = 0.00;
   private Double usageTSO = 0.00;
   private Double usageTSI = 0.00;

   // Variable to be used for response message
   private static Document iUsageResponseWithWrapper;

   // Variables to be used to validate response message
   private final static String RESPONSE_STATUS = "status";
   private final static String RESPONSE_NAMESPACE = "Namespace";
   private final static String ERROR_RESPONSE_MESSAGE = "message";
   private final static String ASSEMBLY_BARCODE = "barcode";


   @Given( "that the usage on an Engine changes" )
   public void thatTheUsageOnAnAPUChanges() throws Throwable {
      // do nothing
   }


   @When( "^the external system sends \"([^\"]*)\" inbound Usage message$" )
   public void theExternalSystemSendsInboundUsageMessage( String arg1,
         List<Map<String, String>> aDataTable ) throws Throwable {

      Map<String, String> aTableRow = aDataTable.get( 0 );

      UsagesDocument lUsageDoc = UsagesDocument.Factory.newInstance();

      lUsageDoc.addNewUsages().addNewUsage().addNewUsageAttributes();

      if ( ( aTableRow.get( USAGE_ATTRIBUTES_NAME ) ).isEmpty()
            && ( arg1.equalsIgnoreCase( "an invalid missing mandatory elements" ) ) ) {
         // If data table has null value, do not do anything
         // (missing mandatory ELEMENT exception scenarios) (this adds no tags at all)
      } else {
         // If data table has a value, add the respective Element Value
         // (Happy Path Scenario) (this adds start tag, element value, and end tag)
         // If data table has null value, add the respective Element Value of null
         // (missing mandatory element VALUE exception scenarios) (this adds only the start tag)
         lUsageDoc.getUsages().getUsageArray( 0 ).getUsageAttributes()
               .setName( aTableRow.get( USAGE_ATTRIBUTES_NAME ) );
      }

      if ( ( aTableRow.get( USAGE_ATTRIBUTES_COLLECTION_DATE ) ).isEmpty()
            && ( arg1.equalsIgnoreCase( "an invalid missing mandatory elements" ) ) ) {
         // If data table has null value, do not do anything
         // (missing mandatory ELEMENT exception scenarios) (this adds no tags at all)
      } else {
         // If data table has a value, add the respective Element Value
         // (Happy Path Scenario) (this adds start tag, element value, and end tag)
         // If data table has null value, add the respective Element Value of null
         // (missing mandatory element VALUE exception scenarios) (this adds only the start tag)
         lUsageDoc.getUsages().getUsageArray( 0 ).getUsageAttributes()
               .setCollectionDate( aTableRow.get( USAGE_ATTRIBUTES_COLLECTION_DATE ) );
      }

      if ( aTableRow.get( USAGE_ATTRIBUTES_PROCESS_AS_HISTORIC ).equalsIgnoreCase( "false" ) ) {
         lUsageDoc.getUsages().getUsageArray( 0 ).getUsageAttributes()
               .setProcessAsHistoric( false );
      } else {
         lUsageDoc.getUsages().getUsageArray( 0 ).getUsageAttributes().setProcessAsHistoric( true );
      }

      lUsageDoc.getUsages().getUsageArray( 0 ).getUsageAttributes()
            .setDocumentReference( aTableRow.get( USAGE_ATTRIBUTES_DOCUMENT_REFERENCE ) );

      if ( ( aTableRow.get( USAGE_ATTRIBUTES_EXTERNAL_IDENTIFIER ) ).isEmpty()
            && ( arg1.equalsIgnoreCase( "an invalid missing mandatory elements" ) ) ) {
         // If data table has null value, do not do anything
         // (missing mandatory ELEMENT exception scenarios) (this adds no tags at all)
      } else {
         // If data table has a value, add the respective Element Value
         // (Happy Path Scenario) (this adds start tag, element value, and end tag)
         // If data table has null value, add the respective Element Value of null
         // (missing mandatory element VALUE exception scenarios) (this adds only the start tag)
         lUsageDoc.getUsages().getUsageArray( 0 ).getUsageAttributes()
               .setExternalIdentifier( aTableRow.get( USAGE_ATTRIBUTES_EXTERNAL_IDENTIFIER ) );
      }

      lUsageDoc.getUsages().getUsageArray( 0 ).addNewAssembly().addNewInventoryIdentifier();

      if ( ( aTableRow.get( ASSEMBLY_OEM_SN ) ).isEmpty()
            && ( arg1.equalsIgnoreCase( "an invalid missing mandatory elements" ) ) ) {
         // If data table has null value, do not do anything
         // (missing mandatory ELEMENT exception scenarios) (this adds no tags at all)
      } else {
         // If data table has a value, add the respective Element Value
         // (Happy Path Scenario) (this adds start tag, element value, and end tag)
         // If data table has null value, add the respective Element Value of null
         // (missing mandatory element VALUE exception scenarios) (this adds only the start tag)
         lUsageDoc.getUsages().getUsageArray( 0 ).getAssembly().getInventoryIdentifier()
               .setOemSerialNumber( aTableRow.get( ASSEMBLY_OEM_SN ) );
      }

      lUsageDoc.getUsages().getUsageArray( 0 ).addNewAircraftIdentifier();

      if ( ( aTableRow.get( AIRCRAFT_IDENTIFIER_REGISTRATION_CODE ) ).isEmpty()
            && ( arg1.equalsIgnoreCase( "an invalid missing mandatory elements" ) ) ) {
         // If data table has null value, do not do anything
         // (missing mandatory ELEMENT exception scenarios) (this adds no tags at all)
      } else {
         // If data table has a value, add the respective Element Value
         // (Happy Path Scenario) (this adds start tag, element value, and end tag)
         // If data table has null value, add the respective Element Value of null
         // (missing mandatory element VALUE exception scenarios) (this adds only the start tag)
         lUsageDoc.getUsages().getUsageArray( 0 ).getAircraftIdentifier()
               .setRegistrationCode( aTableRow.get( AIRCRAFT_IDENTIFIER_REGISTRATION_CODE ) );
      }

      lUsageDoc.getUsages().getUsageArray( 0 ).addNewCollectedUsages().addNewCollectedUsage();

      if ( ( aTableRow.get( COLLECTED_USAGE_USAGE_PARM ) ).isEmpty()
            && ( arg1.equalsIgnoreCase( "an invalid missing mandatory elements" ) ) ) {
         // If data table has null value, do not do anything
         // (missing mandatory ELEMENT exception scenarios) (this adds no tags at all)
      } else {
         // If data table has a value, add the respective Element Value
         // (Happy Path Scenario) (this adds start tag, element value, and end tag)
         // If data table has null value, add the respective Element Value of null
         // (missing mandatory element VALUE exception scenarios) (this adds only the start tag)
         lUsageDoc.getUsages().getUsageArray( 0 ).getCollectedUsages().getCollectedUsageArray( 0 )
               .setUsageParm( aTableRow.get( COLLECTED_USAGE_USAGE_PARM ) );
      }

      // Set values of respective variables using String values in data table
      usageTSN = Double.parseDouble( aTableRow.get( COLLECTED_USAGE_VALUE_TSN ).trim() );
      usageTSO = Double.parseDouble( aTableRow.get( COLLECTED_USAGE_VALUE_TSO ).trim() );
      usageTSI = Double.parseDouble( aTableRow.get( COLLECTED_USAGE_VALUE_TSI ).trim() );

      lUsageDoc.getUsages().getUsageArray( 0 ).getCollectedUsages().getCollectedUsageArray( 0 )
            .addNewValue().addNewTotal();

      if ( ( aTableRow.get( COLLECTED_USAGE_VALUE_TSN ) ).isEmpty()
            && ( arg1.equalsIgnoreCase( "an invalid missing mandatory elements" ) ) ) {
         // If data table has null value, do not do anything
         // (missing mandatory ELEMENT exception scenarios) (this adds no tags at all)
      } else {
         // If data table has a value, add the respective Element Value
         // (Happy Path Scenario) (this adds start tag, element value, and end tag)
         // If data table has null value, add the respective Element Value of null
         // (missing mandatory element VALUE exception scenarios) (this adds only the start tag)
         lUsageDoc.getUsages().getUsageArray( 0 ).getCollectedUsages().getCollectedUsageArray( 0 )
               .getValue().getTotal().setTsn( usageTSN );
      }

      if ( ( aTableRow.get( COLLECTED_USAGE_VALUE_TSO ) ).isEmpty()
            && ( arg1.equalsIgnoreCase( "an invalid missing mandatory elements" ) ) ) {
         // If data table has null value, do not do anything
         // (missing mandatory ELEMENT exception scenarios) (this adds no tags at all)
      } else {
         // If data table has a value, add the respective Element Value
         // (Happy Path Scenario) (this adds start tag, element value, and end tag)
         // If data table has null value, add the respective Element Value of null
         // (missing mandatory element VALUE exception scenarios) (this adds only the start tag)
         lUsageDoc.getUsages().getUsageArray( 0 ).getCollectedUsages().getCollectedUsageArray( 0 )
               .getValue().getTotal().setTso( usageTSO );
      }

      if ( ( aTableRow.get( COLLECTED_USAGE_VALUE_TSI ) ).isEmpty()
            && ( arg1.equalsIgnoreCase( "an invalid missing mandatory elements" ) ) ) {
         // If data table has null value, do not do anything
         // (missing mandatory ELEMENT exception scenarios) (this adds no tags at all)
      } else {
         // If data table has a value, add the respective Element Value
         // (Happy Path Scenario) (this adds start tag, element value, and end tag)
         // If data table has null value, add the respective Element Value of null
         // (missing mandatory element VALUE exception scenarios) (this adds only the start tag)
         lUsageDoc.getUsages().getUsageArray( 0 ).getCollectedUsages().getCollectedUsageArray( 0 )
               .getValue().getTotal().setTsi( usageTSI );
      }

      // Create the inbound Create Usage Message driver
      UsageMessageDriver lMessageDriver = new UsageMessageDriver( "mxintegration", "password" );

      // successful response of type Document, with wrapper, therefore able to validate status
      // attribute which is in the Response Wrapper
      iUsageResponseWithWrapper = lMessageDriver.sendMessageReturnDocument( lUsageDoc );

   }


   @Then( "^a response message with a status of \"([^\"]*)\" is sent$" )
   public void aResponseMessageWithAStatusOfIsSent( String arg1,
         List<Map<String, String>> aDataTable ) throws Throwable {

      String lResponseXml = iUsageResponseWithWrapper.asXML();

      // Validate Status attribute in Response Wrapper
      String lStatus =
            iUsageResponseWithWrapper.getRootElement().attribute( "msg_status" ).getValue();
      Assert.assertEquals(
            "The Status contained in the Response is NOT as expected.\nResponce XML:\n"
                  + lResponseXml,
            aDataTable.get( 0 ).get( RESPONSE_STATUS ), lStatus );

      // Prerequisite steps to validate the Namespace attribute
      // Step 1: Remove the Response Wrapper
      String lMaintenixResponseXml =
            IntegrationMessageUtils.getManintenixResponseBody( lResponseXml );

      // Step 2: Convert to a Document so that it can be parsed easily using the Document type built
      // in methods
      Document lMaintenixResponseDocument = DocumentHelper.parseText( lMaintenixResponseXml );

      // Validate Namespace attribute in Root Element
      String lNamespace = lMaintenixResponseDocument.getRootElement().getNamespaceURI();
      Assert.assertEquals( "The Namespace contained in the Error Response is NOT as expected",
            aDataTable.get( 0 ).get( RESPONSE_NAMESPACE ), lNamespace );

      if ( aDataTable.get( 0 ).get( RESPONSE_STATUS ).equalsIgnoreCase( "COMPLETE" ) ) {

         // Prerequisite step to validate External Identifier
         // Step 1: Convert Mx Response Document to an actual UsagesResponseDocument
         // so that it can be parsed easily using the UsagesResponseDocument built in methods
         UsagesResponseDocument lUsageResponseDocument =
               UsagesResponseDocument.Factory.parse( lMaintenixResponseXml );

         // Validate the External Identifier element
         String lExternalIdentifier = lUsageResponseDocument.getUsagesResponse().getUsageArray( 0 )
               .getExternalIdentifier();
         Assert.assertEquals(
               "The external identifier contained in the Response is NOT as expected",
               aDataTable.get( 0 ).get( USAGE_ATTRIBUTES_EXTERNAL_IDENTIFIER ),
               lExternalIdentifier );
      } else if ( aDataTable.get( 0 ).get( RESPONSE_STATUS ).equalsIgnoreCase( "ERROR" ) ) {
         // Prerequisite steps to validate the error Message
         // Step 1: Get the the first item in the list of the Root Element of the
         // Mx Response Document which is of type Document and has no wrapper
         Element lMessageString =
               ( Element ) lMaintenixResponseDocument.getRootElement().elements().get( 0 );
         // Step 2: Get the contents of only the message Element
         Object lerrMessage = lMessageString.element( "message" ).getData();

         // Validate error Message
         Assert.assertEquals( "The message contained in the Error is NOT as expected",
               aDataTable.get( 0 ).get( ERROR_RESPONSE_MESSAGE ), lerrMessage.toString() );
      }

   }


   @Then( "^the specified Engine usage information is updated accordingly and a response message with a status of \"([^\"]*)\" is sent$" )
   public void
         theSpecifiedAPUUsageInformationIsUpdatedAccordinglyAndAResponseMessageWithAStatusOfIsSent(
               String arg1, List<Map<String, String>> aDataTable ) throws Throwable {

      String lAssyBarcode = null;
      // since we are creating inventory using data loading, we can't control the barcodes
      if ( aDataTable.get( 0 ).get( ASSEMBLY_BARCODE ).isEmpty() ) {
         lAssyBarcode = iInventoryQueriesDriver.getBarcodeBySerialPartNo(
               aDataTable.get( 0 ).get( ASSEMBLY_OEM_PN ),
               aDataTable.get( 0 ).get( ASSEMBLY_OEM_SN ) );
      } else {
         lAssyBarcode = aDataTable.get( 0 ).get( ASSEMBLY_BARCODE );
      }

      // Navigate to the Inventory Details page of the specified APU inventory item
      iNavDriver.barcodeSearch( lAssyBarcode );
      DetailsPaneDriver lDetailsPaneDriver = iInventoryDetailsPageDriver.clickTabDetails();

      // Validate the value of TSN
      Assert.assertEquals( "The TSN value is not as expected",
            aDataTable.get( 0 ).get( COLLECTED_USAGE_VALUE_TSN ),
            lDetailsPaneDriver.getCurrentTsn( "HOURS" ) );

      // Validate the value of TSI
      Assert.assertEquals( "The TSI value is not as expected",
            aDataTable.get( 0 ).get( COLLECTED_USAGE_VALUE_TSI ),
            lDetailsPaneDriver.getCurrentTsi( "HOURS" ) );

      // Validate the value of TSO
      Assert.assertEquals( "The TSO value is not as expected",
            aDataTable.get( 0 ).get( COLLECTED_USAGE_VALUE_TSO ),
            lDetailsPaneDriver.getCurrentTso( "HOURS" ) );

      // Validate that the response message with status "COMPLETE", correct Namespace and correct
      // External Identifier is sent
      aResponseMessageWithAStatusOfIsSent( "COMPLETE", aDataTable );

   }
}
