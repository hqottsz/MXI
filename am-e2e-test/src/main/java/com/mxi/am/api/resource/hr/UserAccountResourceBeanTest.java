package com.mxi.am.api.resource.hr;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.helper.Credentials;
import com.mxi.am.api.resource.erp.hr.user.UserAccount;
import com.mxi.am.driver.common.database.DatabaseDriver;
import com.mxi.am.driver.common.database.Result;
import com.mxi.am.guice.AssetManagementDatabaseDriverProvider;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class UserAccountResourceBeanTest {

   private final String ApplicationJson = "application/json";
   private final String UserAccountPath = "/amapi/" + UserAccount.PATH;

   private DatabaseDriver driver;

   // UserAccount Data Hard Coded
   private final String USERNAME = "user1";
   private final String FIRST_NAME = "User";
   private final String LAST_NAME = "1";
   private final String HR_CODE = "1000091";
   private final String ORG_CODE = "MXI";
   private final boolean LOCKED_BOOL = false;

   private final String MIDDLE_NAME = "Mid";
   private final String ALERT_EMAIL_ADDRESS = "alert@mxi.com";
   private final String EMAIL_ADDRESS = "abc@mxi.com";

   private final String RANDOM_HR_CODE = RandomStringUtils.randomAlphabetic( 10 );
   private final String RANDOM_USERNAME = RandomStringUtils.randomAlphabetic( 10 );

   // UserAccount Data Generated
   private String altId;
   private String hrId;
   private String organizationId;
   private Date lastModifiedDate;

   private UserAccount userAccountResource = new UserAccount();

   // Queries to retrieve generated alt_id
   private final String queryUtlUser = "SELECT * FROM utl_user WHERE username = ?";
   private final String queryOrgHr = "SELECT * FROM org_hr WHERE hr_cd = ?";
   private final String queryOrgOrg = "SELECT * FROM org_org WHERE org_cd = ?";

   // Query to retrieve last_modified_date
   private final String queryLastModifiedDate =
         "SELECT GREATEST (utl_user.revision_dt,org_hr.revision_dt) AS last_modified_dt FROM utl_user INNER JOIN org_hr ON utl_user.user_id = org_hr.user_id WHERE org_hr.hr_cd = ? AND utl_user.username = ?";


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

      Result user = driver.select( queryUtlUser, USERNAME );
      Result orgHr = driver.select( queryOrgHr, HR_CODE );
      Result orgOrg = driver.select( queryOrgOrg, ORG_CODE );
      Result lLastModifiedDate = driver.select( queryLastModifiedDate, HR_CODE, USERNAME );

      altId = user.get( 0 ).getUuidString( "alt_id" );
      hrId = orgHr.get( 0 ).getUuidString( "alt_id" );
      organizationId = orgOrg.get( 0 ).getUuidString( "alt_id" );
      lastModifiedDate = lLastModifiedDate.get( 0 ).get( "last_modified_dt" );

      defaultUserAccountBuilder();
   }


   @Test
   public void testCreateUserAccountSuccessReturns200() {

      userAccountResource = buildUserAccountForPost();

      Response response = create( 200, Credentials.AUTHENTICATED, userAccountResource );

      assertUserAccount( userAccountResource, response );
   }


   @Test
   public void testCreateUserAccountUnsuccessReturns500() {

      userAccountResource = buildUserAccountForPost();
      userAccountResource.setUsername( "" );

      Response response = create( 500, Credentials.AUTHENTICATED, userAccountResource );

      Assert.assertTrue(
            "Response doesn't contain the correct error message. Expected: [[MXERR-10000] The 'aUsername' is a mandatory field]. \n Actual: ["
                  + response.getBody().asString() + "]",
            response.getBody().asString()
                  .contains( "[MXERR-10000] The 'aUsername' is a mandatory field" ) );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testGetUserAccountByIdSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {

      Response response = getById( 200, Credentials.AUTHENTICATED, altId );

      UserAccount userAccount = response.jsonPath().getObject( "", UserAccount.class );

      Assert.assertEquals( "Incorrect returned user account: ", userAccountResource, userAccount );
   }


   @Test
   public void testGetUserAccountByIdUnauthenticatedReturns401() {

      getById( 401, Credentials.UNAUTHENTICATED, altId );
   }


   @Test
   public void testGetUserAccountByIdUnauthorizedReturns403() {

      getById( 403, Credentials.UNAUTHORIZED, altId );
   }


   @Test
   public void testGetUserAccountByUsernameSuccessReturns200()
         throws JsonParseException, JsonMappingException, IOException {

      Response response = getByUsername( 200, Credentials.AUTHENTICATED, USERNAME );

      List<UserAccount> userAccountList = response.jsonPath().getList( "", UserAccount.class );

      Assert.assertEquals( "Incorrect number of returned user accounts: ", 1,
            userAccountList.size() );
      Assert.assertEquals( "Incorrect returned user account: ", userAccountResource,
            userAccountList.get( 0 ) );
   }


   @Test
   @CSIContractTest( Project.SWA_FQC )
   public void testGetUserAccountByUsernameUnauthenticatedReturns401() {

      getByUsername( 401, Credentials.UNAUTHENTICATED, USERNAME );
   }


   @Test
   @CSIContractTest( Project.SWA_FQC )
   public void testGetUserAccountByUsernameUnauthorizedReturns403() {

      getByUsername( 403, Credentials.UNAUTHORIZED, USERNAME );
   }


   Response getById( int statusCode, Credentials security, String userId ) {

      return getById( statusCode, security, userId, ApplicationJson, ApplicationJson );
   }


   private Response getById( int statusCode, Credentials security, String userId,
         String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( UserAccountPath + "/" + userId );

      return lResponse;
   }


   private Response getByUsername( int statusCode, Credentials security, String userId ) {

      return getByUsername( statusCode, security, userId, ApplicationJson, ApplicationJson );
   }


   private Response getByUsername( int statusCode, Credentials security, String userName,
         String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).expect().statusCode( statusCode )
            .when().get( UserAccountPath + "?username=" + userName );

      return lResponse;
   }


   private Response create( int statusCode, Credentials security, Object bodyUserAccount ) {

      return create( statusCode, security, bodyUserAccount, ApplicationJson, ApplicationJson );
   }


   private Response create( int statusCode, Credentials security, Object userAccount,
         String contentType, String acceptType ) {

      String username = security.getUserName();
      String password = security.getPassword();

      Response lResponse = RestAssured.given().auth().preemptive().basic( username, password )
            .accept( acceptType ).contentType( contentType ).body( userAccount ).expect()
            .statusCode( statusCode ).when().post( UserAccountPath );

      return lResponse;
   }


   private void assertUserAccount( UserAccount userAccountExpected, Response response ) {

      UserAccount userAccountActual = response.jsonPath().getObject( "", UserAccount.class );

      userAccountExpected.setId( userAccountActual.getId() );
      userAccountExpected.setHrId( userAccountActual.getHrId() );
      userAccountExpected.setOrganizationId( userAccountActual.getOrganizationId() );
      userAccountExpected.setLastModifiedDate( userAccountActual.getLastModifiedDate() );

      Assert.assertEquals( "Incorrect created user account: ", userAccountExpected,
            userAccountActual );
   }


   private UserAccount defaultUserAccountBuilder() {
      userAccountResource.setUsername( USERNAME );
      userAccountResource.setFirstName( FIRST_NAME );
      userAccountResource.setLastName( LAST_NAME );
      userAccountResource.setHrCode( HR_CODE );
      userAccountResource.setLockedBool( LOCKED_BOOL );

      userAccountResource.setId( altId );
      userAccountResource.setHrId( hrId );
      userAccountResource.setOrganizationId( organizationId );
      userAccountResource.setLastModifiedDate( lastModifiedDate );

      return userAccountResource;
   }


   private UserAccount buildUserAccountForPost() {
      userAccountResource.setUsername( RANDOM_USERNAME );
      userAccountResource.setHrCode( RANDOM_HR_CODE );
      userAccountResource.setMiddleName( MIDDLE_NAME );
      userAccountResource.setAlertEmailAddress( ALERT_EMAIL_ADDRESS );
      userAccountResource.setEmailAddress( EMAIL_ADDRESS );

      return userAccountResource;
   }
}
