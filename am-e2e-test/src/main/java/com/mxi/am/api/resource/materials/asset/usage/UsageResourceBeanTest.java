package com.mxi.am.api.resource.materials.asset.usage;

import static com.mxi.am.helper.api.GenericAmApiCalls.create;
import static com.mxi.am.helper.api.GenericAmApiCalls.search;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.ObjectMapperFactory;
import com.mxi.am.api.resource.erp.location.Location;
import com.mxi.am.api.resource.erp.location.LocationSearchParameters;
import com.mxi.am.api.resource.materials.asset.inventory.Inventory;
import com.mxi.am.api.resource.materials.proc.owner.Owner;
import com.mxi.am.api.resource.materials.proc.owner.OwnerResource;

import io.restassured.RestAssured;
import io.restassured.response.Response;


/**
 * Asset Usage API Test
 *
 */
public class UsageResourceBeanTest {

   private static final String APPLICATION_JSON = "application/json";

   private static final String LOCATION_CD = "AIRPORT1/LINE";
   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
   private static final String RECORD_DATE = "2019-10-15 15:51:00";

   private static final String INV_CLASS_CD = "TRK";
   private static final String INV_PART_NO = "A0000001";
   private static final String INV_COND_CD = "RFI";
   private static final String INV_FIN_STATUS_CD = "ROTABLE";

   private static final String USG_NAME = "Test Usage";
   private static final String USG_PARM_HR = "HOURS";
   private static final String USG_PARM_CYC = "CYCLES";

   private static Inventory givenInventory;

   private final ObjectMapper objectMapper =
         ObjectMapperFactory.DEFAULT_OBJECT_MAPPER_FACTORY.createObjectMapper();


   @BeforeClass
   public static void setUpClass() throws JsonParseException, JsonMappingException, IOException,
         ParseException, ClassNotFoundException, SQLException {
      try {
         RestAssured.reset();
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );
         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }

   }


   @Before
   public void setUpData() throws JsonParseException, JsonMappingException, IOException {

      HashMap<String, String> locParams = new HashMap<>();
      locParams.put( LocationSearchParameters.PARAM_CODE, LOCATION_CD );
      Location location = searchByParameters( Location.PATH, locParams, Location.class );
      String locationId = location.getId();

      HashMap<String, String> ownerParams = new HashMap<>();
      ownerParams.put( OwnerResource.OWNER_CODE_PARAM, "MXI" );
      Owner owner = searchByParameters( Owner.PATH, ownerParams, Owner.class );
      String ownerId = owner.getId();

      Inventory inventory = new Inventory();
      inventory.setInventoryClass( INV_CLASS_CD );
      inventory.setPartNumber( INV_PART_NO );
      inventory.setLocationId( locationId );
      inventory.setLocked( false );
      inventory.setConditionCode( INV_COND_CD );
      inventory.setOwnerId( ownerId );
      inventory.setFinanceStatusCode( INV_FIN_STATUS_CD );

      Response invResponse = create( Status.OK.getStatusCode(), Credentials.AUTHENTICATED,
            inventory, Inventory.PATH, APPLICATION_JSON );
      givenInventory = objectMapper.readValue( invResponse.getBody().asString(), Inventory.class );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testCreateUsage() throws ParseException, AmApiResourceNotFoundException,
         AmApiBusinessException, JsonParseException, JsonMappingException, IOException {

      Usage givenUsage = new Usage();
      givenUsage.setInventoryId( givenInventory.getId() );
      givenUsage.setName( USG_NAME );
      givenUsage.setRecordDate( new SimpleDateFormat( DATE_FORMAT ).parse( RECORD_DATE ) );

      UsageData usageData1 = new UsageData();
      usageData1.setTsi( 5.0 );
      usageData1.setTsn( 11.0 );
      usageData1.setTso( 0.0 );
      usageData1.setUsageParameter( USG_PARM_HR );

      UsageData usageData2 = new UsageData();
      usageData2.setTsi( 1.0 );
      usageData2.setTsn( 2.2 );
      usageData2.setTso( 0.0 );
      usageData2.setUsageParameter( USG_PARM_CYC );

      givenUsage.setUsageData( Arrays.asList( usageData1, usageData2 ) );

      Response usageResponse = create( Status.CREATED.getStatusCode(), Credentials.AUTHENTICATED,
            givenUsage, Usage.PATH, APPLICATION_JSON );
      Usage retrievedUsage =
            objectMapper.readValue( usageResponse.getBody().asString(), Usage.class );

      assertUsage( givenUsage, retrievedUsage );

   }


   private void assertUsage( Usage expectedUsage, Usage actualUsage ) {
      List<UsageData> expectedUsageDatas = expectedUsage.getUsageData();
      List<UsageData> actualUsageDatas = actualUsage.getUsageData();

      int foundUsageDataNum = 0;
      for ( UsageData actualUsageData : actualUsageDatas ) {
         for ( UsageData expectedUsageData : expectedUsageDatas ) {
            if ( actualUsageData.getUsageParameter()
                  .equals( expectedUsageData.getUsageParameter() ) ) {
               assertEquals(
                     "Incorrect TSI found for usage parameter: "
                           + actualUsageData.getUsageParameter() + ".",
                     actualUsageData.getTsi(), expectedUsageData.getTsi() );
               assertEquals(
                     "Incorrect TSN found for usage parameter: "
                           + actualUsageData.getUsageParameter() + ".",
                     actualUsageData.getTsn(), expectedUsageData.getTsn() );
               assertEquals(
                     "Incorrect TSO found for usage parameter: "
                           + actualUsageData.getUsageParameter() + ".",
                     actualUsageData.getTso(), expectedUsageData.getTso() );
               foundUsageDataNum++;
            }
         }
      }

      assertEquals( "Incorrect number of usages found in retrieved usage data.",
            expectedUsageDatas.size(), foundUsageDataNum );
      assertEquals( "Incorrect usage name found in retrieved usage data.", expectedUsage.getName(),
            actualUsage.getName() );
      assertEquals( "Incorrect record date found in retrieved usage data.",
            expectedUsage.getRecordDate(), actualUsage.getRecordDate() );
      assertEquals( "Incorrect inventory ID found in retrieved usage data.",
            expectedUsage.getInventoryId(), actualUsage.getInventoryId() );
   }


   private <T extends Object> T searchByParameters( String path,
         Map<String, String> searchParameters, Class<T> type )
         throws JsonParseException, JsonMappingException, IOException {

      Response response = search( Status.OK.getStatusCode(), Credentials.AUTHENTICATED, path,
            searchParameters, MediaType.APPLICATION_JSON );

      List<T> objectList = objectMapper.readValue( response.getBody().asString(),
            TypeFactory.defaultInstance().constructCollectionType( List.class, type ) );
      return objectList.get( 0 );
   }

}
