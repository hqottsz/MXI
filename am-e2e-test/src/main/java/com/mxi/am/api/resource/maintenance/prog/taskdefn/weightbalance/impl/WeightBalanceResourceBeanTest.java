package com.mxi.am.api.resource.maintenance.prog.taskdefn.weightbalance.impl;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.maintenance.prog.taskdefn.weightbalance.WeightBalance;
import com.mxi.am.api.resource.maintenance.prog.taskdefn.weightbalance.WeightBalanceSearchParameters;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;


/**
 * e2e test class for the Weight Balance API to test permissions and response codes
 *
 */
public class WeightBalanceResourceBeanTest {

   private DatabaseDriver driver;

   private final static String APPLICATION_JSON = "application/json";
   private final static String WEIGHTBALANCE_PATH = "/amapi/" + WeightBalance.PATH;

   private String weightBalanceId;

   private final String queryTaskWeightBalance =
         "SELECT alt_id FROM task_weight_balance WHERE task_weight_balance_id = ?";
   private final String queryGetLatestIdSeq = "SELECT task_weight_balance_id_seq.CURRVAL FROM dual";


   @BeforeClass
   public static void setUpClass() {
      try {
         RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
         RestAssured.baseURI = "http://".concat( InetAddress.getLocalHost().getHostName() );

         RestAssured.port = 80;

      } catch ( UnknownHostException e ) {
         throw new RuntimeException( "Cannot find local host name." );
      }
   }


   @Before
   public void setUpData() throws Exception {

      driver = new AssetManagementDatabaseDriverProvider().get();

      driver.insert(
            "INSERT INTO task_weight_balance (task_weight_balance_db_id, task_weight_balance_id, task_db_id, task_id, weight, moment, rstat_cd )\r\n"
                  + "   VALUES (4650, task_weight_balance_id_seq.NEXTVAL, 4650, 100567, 1.1, 2.2, 0)" );

      BigDecimal currval =
            ( BigDecimal ) driver.select( queryGetLatestIdSeq ).get( 0 ).get( "currval" );

      weightBalanceId =
            driver.select( queryTaskWeightBalance, currval ).get( 0 ).getUuidString( "alt_id" );

   }


   @Test
   public void testGetById200() {
      getById( 200, Credentials.AUTHORIZED, weightBalanceId );

   }


   @Test
   public void testEmtpySearch412() {
      searchByParameters( 412, Credentials.AUTHENTICATED, new WeightBalanceSearchParameters() );
   }


   @Test
   public void testGetByIdUnauthenticated401() {

      getById( 401, Credentials.UNAUTHENTICATED, weightBalanceId );
   }


   @Test
   public void testGetByIdUnauthorized403() {

      getById( 403, Credentials.UNAUTHORIZED, weightBalanceId );
   }


   private void getById( int statusCode, Credentials security, String weightBalanceId ) {

      String username = security.getUserName();
      String password = security.getPassword();

      RestAssured.given().auth().preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( WEIGHTBALANCE_PATH + "/" + weightBalanceId );

   }


   private void searchByParameters( int statusCode, Credentials security,
         WeightBalanceSearchParameters parameters ) {
      String username = security.getUserName();
      String password = security.getPassword();

      RestAssured.given()
            .queryParam( WeightBalanceSearchParameters.TASK_DEFINITION_ID,
                  parameters.getTaskDefinitionId() )
            .auth().preemptive().basic( username, password ).accept( APPLICATION_JSON )
            .contentType( APPLICATION_JSON ).expect().statusCode( statusCode ).when()
            .get( WEIGHTBALANCE_PATH );
   }

}
