package com.mxi.am.api.resource.hr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJBContext;
import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiInternalServerException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.exception.KeyConversionException;
import com.mxi.am.api.resource.erp.hr.user.UserAccount;
import com.mxi.am.api.resource.erp.hr.user.UserAccountSearchParameters;
import com.mxi.am.api.resource.erp.hr.user.impl.UserAccountResourceBean;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class UserAccountResourceBeanTest extends ResourceBeanTest {

   private final String INVALID_EMAIL_ADDRESS = "abcdef.com";
   private final int TOTAL_NO_OF_EXPECTED_RECORDS = 8;

   private UserAccount userAccount;

   private SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Inject
   UserAccountResourceBean userAccountResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Rule
   public ExpectedException exception = ExpectedException.none();

   public LegacyKeyUtil legacyKeyUtil = new LegacyKeyUtil();


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( databaseConnectionRule.getConnection(), UserAccountResourceBeanTest.class );
      initializeSecurityContext();
   }


   @ClassRule
   public static final DatabaseConnectionRule databaseConnectionRule =
         new DatabaseConnectionRule( () -> {
            disableTriggers();
         }, () -> {
            enableTriggers();
         } );


   @Before
   public void setUp() throws NamingException, MxException, KeyConversionException,
         AmApiBusinessException, SQLException, ParseException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      userAccountResourceBean.setEJBContext( ejbContext );
      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      constructUserAccount();
   }


   static void enableTriggers() {
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_ORG_HR" );
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_UTL_USER" );
   }


   static void disableTriggers() {
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_ORG_HR" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_UTL_USER" );
   }


   @Test
   @CSIContractTest( { Project.SWA_AC_STATUS, Project.SWA_FAULT_STATUS, Project.SWA_WP_STATUS,
         Project.UPS } )
   public void get_success() throws AmApiResourceNotFoundException {
      UserAccount userAccountResponse = userAccountResourceBean.get( userAccount.getId() );

      Assert.assertEquals( "Incorrect returned user account: ", userAccount, userAccountResponse );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void get_failure_userIdNotFound() throws AmApiResourceNotFoundException {

      userAccountResourceBean.get( "11111111111111111111111111111111" );
   }


   @Test
   public void search_success_allParameters() {

      UserAccountSearchParameters userSearchParm = new UserAccountSearchParameters();
      userSearchParm.username( userAccount.getUsername() );
      userSearchParm.setUserHrCode( userAccount.getHrCode() );

      List<UserAccount> userList = userAccountResourceBean.search( userSearchParm );

      Assert.assertEquals( "Incorrect returned user account: ", userAccount, userList.get( 0 ) );
   }


   @Test
   public void search_success_byUsername() {
      UserAccountSearchParameters userSearchParm = new UserAccountSearchParameters();
      userSearchParm.username( userAccount.getUsername() );

      List<UserAccount> userList = userAccountResourceBean.search( userSearchParm );

      Assert.assertEquals( "Incorrect returned user account: ", userAccount, userList.get( 0 ) );
   }


   @Test
   public void search_success_byHrCode() {

      UserAccountSearchParameters userSearchParm = new UserAccountSearchParameters();
      userSearchParm.setUserHrCode( userAccount.getHrCode() );

      List<UserAccount> userList = userAccountResourceBean.search( userSearchParm );

      assertEquals( "Incorrect number of user accounts returned: ", 1, userList.size() );
      assertTrue( "Returned user account list [" + userList
            + "] doesn't contain expected user account [" + userAccount + "]. ",
            userList.contains( userAccount ) );
   }


   @Test
   public void search_success_retrieveAllUsersByNullParameters() {
      UserAccountSearchParameters userSearchParm = new UserAccountSearchParameters();
      userSearchParm.username( null );
      userSearchParm.setUserHrCode( null );

      List<UserAccount> userList = userAccountResourceBean.search( userSearchParm );

      assertEquals( "Incorrect number of user accounts returned: ", TOTAL_NO_OF_EXPECTED_RECORDS,
            userList.size() );
      assertTrue( "Returned user account list [" + userList
            + "] doesn't contain expected user account [" + userAccount + "]. ",
            userList.contains( userAccount ) );

   }


   @Test
   public void search_success_retrieveNoUsers() {

      UserAccountSearchParameters userSearchParm = new UserAccountSearchParameters();
      userSearchParm.username( "NON_EXISTING_USERNAME" );

      List<UserAccount> userList = userAccountResourceBean.search( userSearchParm );

      Assert.assertTrue( "Returned user account list [" + userList + "] should be empty.",
            userList.isEmpty() );
   }


   @Test
   public void create_failure_nullPayload() {
      exception.expect( AmApiBadRequestException.class );
      exception.expectMessage( "Missing user account payload" );

      userAccountResourceBean.create( null );
   }


   @Test
   public void create_failure_invalidEmailAddress() {
      exception.expect( AmApiInternalServerException.class );
      exception.expectMessage( String.format( "[MXERR-30238] The email address '%s' is invalid.",
            INVALID_EMAIL_ADDRESS ) );

      userAccount.setEmailAddress( INVALID_EMAIL_ADDRESS );

      userAccountResourceBean.create( userAccount );
   }


   @Test
   public void create_failure_invalidAlertEmailAddress() {
      exception.expect( AmApiInternalServerException.class );
      exception.expectMessage( String.format( "[MXERR-30238] The email address '%s' is invalid.",
            INVALID_EMAIL_ADDRESS ) );

      userAccount.setAlertEmailAddress( INVALID_EMAIL_ADDRESS );

      userAccountResourceBean.create( userAccount );

   }


   @Test
   public void create_failure_nullUsername() {
      exception.expect( AmApiInternalServerException.class );
      exception.expectMessage( "[MXERR-10000] The 'aUsername' is a mandatory field" );

      userAccount.setUsername( null );

      userAccountResourceBean.create( userAccount );
   }


   @Test
   public void create_failure_nullHrCode() {
      exception.expect( AmApiInternalServerException.class );
      exception.expectMessage( "[MXERR-10000] The 'aHrCode' is a mandatory field" );

      userAccount.setHrCode( null );

      userAccountResourceBean.create( userAccount );
   }


   @Test
   public void create_failure_nullFirstName() {
      exception.expect( AmApiInternalServerException.class );
      exception.expectMessage( "[MXERR-10000] The 'aFirstName' is a mandatory field" );

      userAccount.setFirstName( null );

      userAccountResourceBean.create( userAccount );
   }


   @Test
   public void create_failure_nullLastName() {
      exception.expect( AmApiInternalServerException.class );
      exception.expectMessage( "[MXERR-10000] The 'aLastName' is a mandatory field" );

      userAccount.setLastName( null );

      userAccountResourceBean.create( userAccount );
   }


   private void constructUserAccount() throws ParseException {

      userAccount = new UserAccount( "00000000000000000000000000000001" );
      userAccount.setFirstName( "Marcus" );
      userAccount.setLastName( "Aurelius" );
      userAccount.setUsername( "testUser1" );
      userAccount.setHrId( "30000000000000000000000000000001" );
      userAccount.setOrganizationId( "20000000000000000000000000000001" );
      userAccount.setLastModifiedDate( simpleDateFormat.parse( "2017-10-22 00:00:00" ) );
      userAccount.setHrCode( "TESTHR" );
   }
}
