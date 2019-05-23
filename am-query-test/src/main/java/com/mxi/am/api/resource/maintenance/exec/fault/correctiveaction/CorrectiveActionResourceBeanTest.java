package com.mxi.am.api.resource.maintenance.exec.fault.correctiveaction;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
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
import com.mxi.am.api.resource.maintenance.exec.fault.correctiveaction.impl.CorrectiveActionResourceBean;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Database-level test class for the Aircraft Engines API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class CorrectiveActionResourceBeanTest extends ResourceBeanTest {

   @Inject
   CorrectiveActionResourceBean corrActionResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public ExpectedException exception = ExpectedException.none();

   private SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

   CorrectiveAction correctiveAction1 = new CorrectiveAction();


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      corrActionResourceBean.setEJBContext( ejbContext );
      constructExpectedResults();
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   @CSIContractTest( Project.SWA_FAULT_STATUS )
   public void search_success_byFaultId() {
      CorrectiveActionSearchParameters searchParameters =
            new CorrectiveActionSearchParameters().faultId( correctiveAction1.getFaultId() );

      List<CorrectiveAction> returnedCorrActions =
            corrActionResourceBean.search( searchParameters );

      assertEquals( "Unexpected number of corrective actions returned: ", 1,
            returnedCorrActions.size() );
      assertCorrectiveAction( correctiveAction1, returnedCorrActions.get( 0 ) );

   }


   private void assertCorrectiveAction( CorrectiveAction expectedCorrAction,
         CorrectiveAction actualCorrAction ) {
      assertEquals( "Action date of returned corrective action is incorrect: ",
            expectedCorrAction.getActionDate(), actualCorrAction.getActionDate() );
      assertEquals( "Cancelled date of returned corrective action is incorrect: ",
            expectedCorrAction.getCanceledDate(), actualCorrAction.getCanceledDate() );
      assertEquals( "Cancelled user id of returned corrective action is incorrect: ",
            expectedCorrAction.getCanceledUserId(), actualCorrAction.getCanceledUserId() );
      assertEquals( "Creation user id of returned corrective action is incorrect: ",
            expectedCorrAction.getCreationUserId(), actualCorrAction.getCreationUserId() );
      assertEquals( "Description of returned corrective action is incorrect: ",
            expectedCorrAction.getDescription(), actualCorrAction.getDescription() );
      assertEquals( "Fault id of returned corrective action is incorrect: ",
            expectedCorrAction.getFaultId(), actualCorrAction.getFaultId() );

   }


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(),
            CorrectiveActionResourceBeanTest.class );
      initializeSecurityContext();
   }


   private void constructExpectedResults() throws ParseException {
      correctiveAction1.setDescription( "Corrective Action 1 Description" );
      correctiveAction1.setFaultId( "00000000000000000000000000000001" );
      correctiveAction1.setCreationUserId( "000000000000000000000000000000A1" );
      correctiveAction1.setCanceledUserId( "000000000000000000000000000000A9" );
      correctiveAction1.setCanceledBoolean( true );
      correctiveAction1.setActionDate( simpleDateFormat.parse( "2019-04-30 10:00:00" ) );
      correctiveAction1.setCanceledDate( simpleDateFormat.parse( "2019-04-30 13:00:00" ) );
   }

}
