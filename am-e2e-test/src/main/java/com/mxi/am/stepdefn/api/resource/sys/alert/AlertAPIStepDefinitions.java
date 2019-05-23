package com.mxi.am.stepdefn.api.resource.sys.alert;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.sys.alert.Alert;
import com.mxi.am.api.resource.sys.alert.AlertParameter;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.driver.integrationtesting.Rest;
import com.mxi.am.driver.integrationtesting.RestDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.common.UserAlertsPageDriver;
import com.mxi.am.driver.web.common.useralerts.UnassignedAlertsPaneDriver.AlertRow;
import com.mxi.am.driver.web.po.CreateEditOrderPageDriver;
import com.mxi.am.driver.web.po.OrderDetailsPageDriver;
import com.mxi.am.driver.web.po.OrderSearchPageDriver;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;
import com.mxi.driver.standard.Wait;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * GET Alert API Step Definitions
 *
 */
public class AlertAPIStepDefinitions {

   @Inject
   @Rest
   private RestDriver restDriver;

   @Inject
   private UserAlertsPageDriver userAlertsDriver;

   @Inject
   private NavigationDriver navigationDriver;

   @Inject
   private OrderDetailsPageDriver orderDetailsPage;

   @Inject
   public OrderSearchPageDriver orderSearchDriver;

   @Inject
   public CreateEditOrderPageDriver createEditOrderDriver;

   private static String orderNumber;
   private static String purchaseOrderUUID;
   private static String purchaseOrderKey;
   private static Alert createdAlert;
   private static Alert alertFromGet;
   private static Alert newAlert;

   private static final String ALERT_PATH = "/amapi/" + Alert.PATH;

   private static final String PARENT_MENU_OPTIONS = "Options";
   private static final String CHILD_MENU_ALERTS = "Alerts";
   private static final String PARENT_MENU_PURCHASING_MANAGER = "Purchasing Manager";
   private static final String CHILD_MENU_ORDER_SEARCH = "Order Search";

   // Create STRING type Alert
   private static final Integer ALERT_TYPE_ID_STR = 22;
   private static final String ALERT_PARAMETER_TYPE_STR = "STRING";
   private static final String ALERT_PARAMETER_VALUE = "TEST ALERT VALUE";
   private static final String ALERT_PARAMETER_DISPLAY_VALUE = "TEST DISPLAY VALUE";
   private static final String ALERT_NAME_STR = "Part Created";

   // Create PO type Alert
   private static final Integer ALERT_TYPE_ID_PO = 25;
   private static final String ALERT_PARAMETER_TYPE_PO = "PURCHASE_ORDER";
   private static final String ALERT_NAME_PO = "Purchase Order Exception";
   private static final String VENDOR_CODE = "10001";
   private static final String SHIP_TO_LOC_ID = "AIRPORT1/DOCK";

   private static final int TIMEOUT_IN_SECONDS = 60 * 5;
   private static final int WAIT_IN_MILLISECONDS = 50;


   @Given( "^I have a new basic PO$" )
   public void createPurchaseOrderForAlertGeneration() throws Throwable {

      navigationDriver.navigate( PARENT_MENU_PURCHASING_MANAGER, CHILD_MENU_ORDER_SEARCH );
      orderSearchDriver.clickTabOrderFound().clickCreatePO();
      createNewPurchaseOrder();

   }


   @When( "^I create an Alert using the Alert API PURCHASE_ORDER type$" )
   public void createAlertPoTypeUsingPostRequest() throws Throwable {

      allOldNotificationsAreDeleted();

      newAlert = new Alert();
      newAlert.setAlertTypeId( ALERT_TYPE_ID_PO );

      AlertParameter alertParameter = new AlertParameter();
      alertParameter.setDisplayValue( orderNumber );
      alertParameter.setValue( purchaseOrderUUID );
      alertParameter.setType( ALERT_PARAMETER_TYPE_PO );

      List<AlertParameter> alertParameterList = new ArrayList<>();
      alertParameterList.add( alertParameter );
      newAlert.setParameters( alertParameterList );

      createdAlert = createAlert( newAlert );

   }


   @Then( "^PURCHASE_ORDER type Alert is returned correctly from the API$" )
   public void verifyAlertForPostFromUI() throws Throwable {

      assertCreatedAlert( newAlert, createdAlert );

      navigationDriver.navigateOther( PARENT_MENU_OPTIONS, CHILD_MENU_ALERTS );
      userAlertsDriver.clickTabUnassignedAlerts();

      List<AlertRow> alertMessages = fetchAlertRows();
      Optional<AlertRow> lAlertFiltered = alertMessages.stream()
            .filter( o -> ( o.getMessage() ).contains( createdAlert.getMessage() )
                  && ( o.getMessage() ).contains( orderNumber )
                  && ( o.getName().equals( ALERT_NAME_PO ) ) )
            .findAny();
      assertTrue( "Alert is mismatched in UI ", lAlertFiltered.isPresent() );
      lAlertFiltered.get().clickMessage();
      Assert.assertEquals( "Could not navigate to the PO from UI:", orderNumber,
            orderDetailsPage.getPONumber() );
   }


   @Given( "^I create an Alert using the Alert API STRING type$" )
   public void createAlertStringTypeUsingPostRequest() throws Throwable {

      allOldNotificationsAreDeleted();

      newAlert = new Alert();
      newAlert.setAlertTypeId( ALERT_TYPE_ID_STR );

      AlertParameter alertParameter = new AlertParameter();
      alertParameter.setDisplayValue( ALERT_PARAMETER_DISPLAY_VALUE );
      alertParameter.setValue( ALERT_PARAMETER_VALUE );
      alertParameter.setType( ALERT_PARAMETER_TYPE_STR );

      List<AlertParameter> alertParameterList = new ArrayList<>();
      alertParameterList.add( alertParameter );
      newAlert.setParameters( alertParameterList );

      createdAlert = createAlert( newAlert );

   }


   @When( "^I send the Alert API message to get the Alert$" )
   public void getAlertUsingGetRequest() throws Throwable {

      alertFromGet = getAlertById( createdAlert.getAlertId() );

   }


   @Then( "^STRING type Alert is returned correctly from the API$" )
   public void verifyAlertForGetFromUI() throws Throwable {

      Assert.assertEquals( "Created Alert and the Alert from the get mismatched:", createdAlert,
            alertFromGet );

      navigationDriver.navigateOther( PARENT_MENU_OPTIONS, CHILD_MENU_ALERTS );
      userAlertsDriver.clickTabUnassignedAlerts();

      List<AlertRow> alertMessages = fetchAlertRows();
      Optional<AlertRow> lAlertFiltered = alertMessages.stream()
            .filter( o -> ( o.getMessage() ).contains( createdAlert.getMessage() )
                  && ( o.getName().equals( ALERT_NAME_STR ) ) )
            .findAny();
      assertTrue( "Alert is mismatched in UI ", lAlertFiltered.isPresent() );
   }


   private void allOldNotificationsAreDeleted() throws Throwable {

      navigationDriver.navigateOther( PARENT_MENU_OPTIONS, CHILD_MENU_ALERTS );
      userAlertsDriver.clickTabUnassignedAlerts();
      userAlertsDriver.getTabUnassignedAlerts().selectAll();
      userAlertsDriver.getTabUnassignedAlerts().clickDelete();
      userAlertsDriver.clickYes();
      // Now click OK on the User Alerts page itself
      userAlertsDriver.clickYes();

   }


   private Alert createAlert( Alert alert ) throws IOException {

      ObjectMapper mapper = ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();
      String jsonAlert = mapper.writerWithDefaultPrettyPrinter().writeValueAsString( alert );

      Response response = restDriver.target( ALERT_PATH ).request()
            .accept( MediaType.APPLICATION_JSON ).post( Entity.json( jsonAlert ) );

      return constructAlertFromResponse( response );

   }


   private Alert getAlertById( Integer alerttId )

         throws JsonParseException, JsonMappingException, IOException {
      Response response = restDriver.target( ALERT_PATH + "/" + alerttId ).request()
            .accept( MediaType.APPLICATION_JSON ).get();
      return constructAlertFromResponse( response );

   }


   private static Alert constructAlertFromResponse( Response response )
         throws JsonParseException, JsonMappingException, IOException {

      return ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper()
            .readValue( response.readEntity( String.class ), Alert.class );

   }


   private void assertCreatedAlert( Alert alertExpected, Alert createdAlert ) {

      List<AlertParameter> alertParameterList = null;
      if ( alertExpected.getParameters().size() > 0 ) {

         AlertParameter alertParameter = alertExpected.getParameters().stream().findFirst().get();
         if ( createdAlert.getAlertTypeId().equals( ALERT_TYPE_ID_PO ) ) {
            alertParameter.setValue( purchaseOrderKey );
         }
         alertParameterList = new ArrayList<>();
         alertParameterList.add( 0, alertParameter );
      }
      Assert.assertEquals( "Created Alert parameters mismatched:", alertParameterList,
            createdAlert.getParameters() );
      Assert.assertEquals( "Created Alert Type ID mismatched:", alertExpected.getAlertTypeId(),
            createdAlert.getAlertTypeId() );

   }


   private void createNewPurchaseOrder()
         throws ParseException, ClassNotFoundException, SQLException {

      createEditOrderDriver.setShipTo( SHIP_TO_LOC_ID );
      createEditOrderDriver.setVendor( VENDOR_CODE );
      createEditOrderDriver.clickOK();
      orderNumber = orderDetailsPage.getPONumber();
      DatabaseDriver driver = new AssetManagementDatabaseDriverProvider().get();
      Result poResult = driver.select(
            "SELECT po_header.alt_id, po_header.po_db_id, po_header.po_id FROM po_header INNER JOIN evt_event ON "
                  + " evt_event.event_db_id = po_header.po_db_id AND "
                  + " evt_event.event_id    = po_header.po_id "
                  + " WHERE po_header.po_type_cd  = 'PURCHASE' AND " + " evt_event.event_sdesc = ?",
            orderNumber );
      purchaseOrderUUID = poResult.get( 0 ).getUuidString( "alt_id" );
      purchaseOrderKey =
            poResult.get( 0 ).get( "po_db_id" ) + ":" + poResult.get( 0 ).get( "po_id" );

   }


   private List<AlertRow> fetchAlertRows() {
      List<AlertRow> alertMessages = null;
      Date lTimeout = DateUtils.addSeconds( new Date(), TIMEOUT_IN_SECONDS );
      // Wait until the Alerts are loaded.
      do {
         alertMessages = userAlertsDriver.clickTabUnassignedAlerts().getUnassignedAlertLines();
         if ( CollectionUtils.isNotEmpty( alertMessages ) ) {
            return alertMessages;
         }
         Wait.pause( WAIT_IN_MILLISECONDS );
         navigationDriver.refreshPage();
      } while ( ( new Date() ).before( lTimeout ) );

      if ( CollectionUtils.isEmpty( alertMessages ) ) {
         Assert.fail( "No Alert found" );
      }
      return alertMessages;
   }

}
