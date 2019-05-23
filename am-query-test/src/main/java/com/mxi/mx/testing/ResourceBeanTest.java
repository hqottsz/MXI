package com.mxi.mx.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;

import javax.ejb.SessionContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.junit.Rule;
import org.mockito.Mockito;

import com.google.inject.Injector;
import com.mxi.am.api.resource.response.ResponseMessage;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.ejb.DAOLocalStub;
import com.mxi.mx.common.ejb.EjbFactory;
import com.mxi.mx.common.ejb.EjbFactoryStub;
import com.mxi.mx.common.ejb.security.SecurityIdentityStub;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.auth.UserPrincipal;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.services.user.UserService;


/**
 * Base class for Resource bean smoke test
 *
 *
 * @author "Janith Amarawickrama <janith.amarawickrama@mxi.com>"
 */
public class ResourceBeanTest {

   public static final String AUTHORIZED = "authorized";
   public static final String UNAUTHORIZED = "unauthorized";

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   /**
    * @deprecated Instead use {@link InjectionOverrideRule} and obtain a direct reference to the
    *             {@link InjectorContainer#get()}
    */
   @Deprecated
   protected Injector iInjector;

   protected SecurityContext iAuthorizedSecurityContext;
   protected SecurityContext iUnauthorizedSecurityContext;

   protected SessionContext iSessionContext;

   private String iAuthorizedUser;
   private String iUnauthorizedUser;


   protected void initializeTest() throws MxException {
      setupDataLoader();
      // set up security contexts and the user config parms for access to api
      setupSecurityContexts();

   }


   protected void initializeDataLoader() {
      setupDataLoader();
   }


   protected void initializeSecurityContext() throws MxException {
      // set up security contexts and the user config parms
      // for access to api
      setupSecurityContexts();
   }


   protected void assertStatus( Response.Status status, Response lResponse ) {
      String lMessage = null;
      Object lEntity = lResponse.getEntity();
      if ( lEntity != null ) {
         lMessage = lResponse.getEntity().toString();
      }
      assertEquals( lMessage, status.getStatusCode(), lResponse.getStatus() );
   }


   protected void assertMessageContains( String aMessage, Response lResponse ) {
      String lMessage = ( ( ResponseMessage ) lResponse.getEntity() ).getMessage();
      assertTrue( aMessage, lMessage.contains( aMessage ) );
   }


   protected SessionContext getSessionContext() {
      iSessionContext = Mockito.mock( SessionContext.class );
      Mockito.when( iSessionContext.getCallerPrincipal() )
            .thenReturn( new UserPrincipal( AUTHORIZED ) );
      return iSessionContext;
   }


   private void setupDataLoader() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
   }


   private void setupSecurityContexts() throws MxException {

      iUnauthorizedSecurityContext = new SecurityContext() {

         @Override
         public Principal getUserPrincipal() {
            return new UserPrincipal( getUnauthorizedUser() );
         }


         @Override
         public boolean isUserInRole( String role ) {
            return false;
         }


         @Override
         public boolean isSecure() {
            return false;
         }


         @Override
         public String getAuthenticationScheme() {
            return null;
         }
      };

      iAuthorizedSecurityContext = new SecurityContext() {

         @Override
         public Principal getUserPrincipal() {
            return new UserPrincipal( getAuthorizedUser() );
         }


         @Override
         public boolean isUserInRole( String role ) {
            return false;
         }


         @Override
         public boolean isSecure() {
            return false;
         }


         @Override
         public String getAuthenticationScheme() {
            return null;
         }
      };

      int lAuthorizedUserId = new UserService().find( getAuthorizedUser() ).getId();
      UserParameters.setInstance( lAuthorizedUserId, "SECURED_RESOURCE",
            new UserParametersFake( lAuthorizedUserId, "SECURED_RESOURCE" ) );

      int lUnauthorizedUserId = new UserService().find( getUnauthorizedUser() ).getId();
      UserParameters.setInstance( lUnauthorizedUserId, "SECURED_RESOURCE",
            new UserParametersFake( lUnauthorizedUserId, "SECURED_RESOURCE" ) );

      EjbFactory.setSingleton(
            new EjbFactoryStub( new SecurityIdentityStub( getAuthorizedUser(), lAuthorizedUserId ),
                  new DAOLocalStub() ) );

   }


   public String getAuthorizedUser() {
      return iAuthorizedUser;
   }


   public void setAuthorizedUser( String authorizedUser ) {
      this.iAuthorizedUser = authorizedUser;
   }


   public String getUnauthorizedUser() {
      return iUnauthorizedUser;
   }


   public void setUnauthorizedUser( String unauthorizedUser ) {
      this.iUnauthorizedUser = unauthorizedUser;
   }
}
