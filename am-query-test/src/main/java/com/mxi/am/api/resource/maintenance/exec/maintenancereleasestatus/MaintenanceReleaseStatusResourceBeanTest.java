package com.mxi.am.api.resource.maintenance.exec.maintenancereleasestatus;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.exec.maintenancereleasestatus.impl.MaintenanceReleaseStatusResourceBean;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query Test of Maintenance Release Status API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class MaintenanceReleaseStatusResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( Security.class ).to( CoreSecurity.class );
            }
         } );

   @Inject
   MaintenanceReleaseStatusResourceBean iMaintenanceReleaseStatusResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   private SimpleDateFormat iSimpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

   private final String AIRCRAFT_ID = "DB941D0EA40C11E894C75FEB009DBE2D";
   private final String NE_AIRCRAFT_ID = "75338E6C09934A11A23172BCA12192B6";
   private final String NULL_AIRCRAFT_ID = "";

   private final String USER_ID = "75338E6C09934A11A23172BCA12192B6";

   private final String LAST_MODIFIED_DT = "2019-01-15 21:02:44";
   private final String SIGNOFF_DT = "2018-01-20 21:02:44";
   private final boolean RELEASE_TO_SERVICE_BOOL = true;
   private final boolean ETOPS_BOOL = true;


   @Before
   public void setUp() throws MxException {

      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeData();
      initializeSecurityContext();

      iMaintenanceReleaseStatusResourceBean.setEJBContext( iEJBContext );

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );
   }


   // Define DataConnectionRule with triggers that need to be altered before/after the execution of
   // tests.
   @ClassRule
   public static final DatabaseConnectionRule iDatabaseConnectionRule =
         new DatabaseConnectionRule( () -> {
            disableTriggers();
         }, () -> {
            enableTriggers();
         } );


   private static void enableTriggers() {
      // Enable triggers after loading data
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_SCHED_WP" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_SCHED_WP_SIGN" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_INV_AC_REG" );
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.ENABLE_TIBR_INV_INV" );
   }


   private static void disableTriggers() {
      // Disable triggers before loading data
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_SCHED_WP" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_SCHED_WP_SIGN" );
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_INV_AC_REG" );
      MxDataAccess.getInstance().execute( "com.mxi.am.data.definition.query.DISABLE_TIBR_INV_INV" );
   }


   public void initializeData() {
      // Load data to database
      DataLoaders.load( iDatabaseConnectionRule.getConnection(),
            MaintenanceReleaseStatusResourceBeanTest.class );
   }


   /**
    * Test for Get Maintenance Release Status by valid id (Success)
    *
    * @throws AmApiResourceNotFoundException
    * @throws ParseException
    */
   @Test
   public void testGetMaintenanceReleaseStatusById()
         throws AmApiResourceNotFoundException, ParseException {
      MaintenanceReleaseStatus lMaintenanceReleaseStatus =
            iMaintenanceReleaseStatusResourceBean.get( AIRCRAFT_ID );
      assertEquals( constructMaintenanceReleaseStatus(), lMaintenanceReleaseStatus );
   }


   /**
    * Test for Get Maintenance Release Status by non exist id
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetMaintenanceReleaseStatusByNonExistId() throws AmApiResourceNotFoundException {
      iMaintenanceReleaseStatusResourceBean.get( NE_AIRCRAFT_ID );
   }


   /**
    * Test for Get Maintenance Release Status by null id
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetMaintenanceReleaseStatusByNullId() throws AmApiResourceNotFoundException {
      iMaintenanceReleaseStatusResourceBean.get( NULL_AIRCRAFT_ID );
   }


   private MaintenanceReleaseStatus constructMaintenanceReleaseStatus() throws ParseException {
      MaintenanceReleaseStatus lMaintenanceReleaseStatus = new MaintenanceReleaseStatus();
      CertificateReleaseService lCertificateReleaseService = new CertificateReleaseService();

      lMaintenanceReleaseStatus.setAircraftId( AIRCRAFT_ID );
      lMaintenanceReleaseStatus.setReleaseToService( RELEASE_TO_SERVICE_BOOL );
      lMaintenanceReleaseStatus.setLastModifiedDate( iSimpleDateFormat.parse( LAST_MODIFIED_DT ) );

      lCertificateReleaseService.setEtops( ETOPS_BOOL );
      lCertificateReleaseService.setSignOffDate( iSimpleDateFormat.parse( SIGNOFF_DT ) );
      lCertificateReleaseService.setSignOffUserId( USER_ID );
      lMaintenanceReleaseStatus.setCertificateReleaseService( lCertificateReleaseService );

      return lMaintenanceReleaseStatus;
   }

}
