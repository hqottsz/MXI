package com.mxi.am.api.resource.maintenance.exec.fault.referencerequest;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJBContext;
import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.resource.maintenance.exec.fault.referencerequest.impl.ReferenceRequestResourceBean;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Test suite for {@link ReferenceRequestResourceBean} class.
 */
@RunWith( MockitoJUnitRunner.class )
public class ReferenceRequestResourceBeanTest extends ResourceBeanTest {

   private final static String APPROVED_STATUS_CD = "APPROVED";
   private final static String FAULT1_ID = "4AF698E4E6E6455C88AFE97B391DC58D";
   private final static String FAULT2_ID = "4AF698E4E6E6455C88AFE97B391DC58E";
   private final static String APPROVER_USER = "mxi";
   private final static String DEFFER_REFERENCE_TYPE_CD = "DEFERRAL";
   private final static String REPAIR_REFERENCE_TYPE_CD = "REPAIR";

   @Inject
   ReferenceRequestResourceBean referenceRequestResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext eJBContext;

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();


   /**
    * {@inheritDoc}
    */
   @Override
   protected void initializeTest() throws MxException {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(),
            ReferenceRequestResourceBeanTest.class );
      initializeSecurityContext();
   }


   @Before
   public void setUp() throws NamingException, MxException, SQLException {
      referenceRequestResourceBean =
            InjectorContainer.get().getInstance( ReferenceRequestResourceBean.class );
      referenceRequestResourceBean.setEJBContext( eJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( eJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testGetByStatusCdSuccess() {
      ReferenceRequestSearchParameters referenceRequestSearchParameters =
            new ReferenceRequestSearchParameters();
      referenceRequestSearchParameters.setStatusCode( APPROVED_STATUS_CD );
      List<ReferenceRequest> referenceRequests =
            referenceRequestResourceBean.search( referenceRequestSearchParameters );
      Assert.assertEquals( "Incorrect number of reference requests returned: ", 2,
            referenceRequests.size() );
      assertReferenceRequest( FAULT1_ID, DEFFER_REFERENCE_TYPE_CD, referenceRequests.get( 0 ) );
      assertReferenceRequest( FAULT2_ID, REPAIR_REFERENCE_TYPE_CD, referenceRequests.get( 1 ) );
   }


   @Test
   public void testGetByFaultIdSuccess() {
      ReferenceRequestSearchParameters referenceRequestSearchParameters =
            new ReferenceRequestSearchParameters();
      referenceRequestSearchParameters.setFaultId( FAULT1_ID );
      List<ReferenceRequest> referenceRequests =
            referenceRequestResourceBean.search( referenceRequestSearchParameters );
      Assert.assertEquals( "Incorrect number of reference requests returned: ", 1,
            referenceRequests.size() );
      assertReferenceRequest( FAULT1_ID, DEFFER_REFERENCE_TYPE_CD, referenceRequests.get( 0 ) );
   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testGetByFaultIdAndStatusSuccess() {
      ReferenceRequestSearchParameters referenceRequestSearchParameters =
            new ReferenceRequestSearchParameters();
      referenceRequestSearchParameters.setFaultId( FAULT1_ID );
      referenceRequestSearchParameters.setStatusCode( APPROVED_STATUS_CD );
      List<ReferenceRequest> referenceRequests =
            referenceRequestResourceBean.search( referenceRequestSearchParameters );
      Assert.assertEquals( "Incorrect number of reference requests returned: ", 1,
            referenceRequests.size() );
      assertReferenceRequest( FAULT1_ID, DEFFER_REFERENCE_TYPE_CD, referenceRequests.get( 0 ) );
   }


   private void assertReferenceRequest( String faultId, String referenceType,
         ReferenceRequest referenceRequest ) {
      Assert.assertEquals( "Incorrect Fault ID: ", faultId, referenceRequest.getFaultId() );
      Assert.assertEquals( "Incorrect Approved User: ", APPROVER_USER,
            referenceRequest.getApproverUser() );
      Assert.assertEquals( "Incorrect Status Code: ", APPROVED_STATUS_CD,
            referenceRequest.getStatusCode() );
      Assert.assertEquals( "Incorrect Reference Type: ", referenceType,
            referenceRequest.getReferenceType() );
   }

}
