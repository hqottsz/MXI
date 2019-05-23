package com.mxi.am.api.resource.sys.alert;

import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.alert.impl.AlertResourceBean;
import com.mxi.am.api.resource.sys.alert.type.AlertType;
import com.mxi.am.api.resource.sys.alert.type.impl.AlertTypeController;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.sys.alert.action.raise.service.RaiseAlertService;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Integration test for AlertResourceBean
 *
 * @author "Dasuni Kumarapperuma" <dasuni.kumarapperuma@ifsworld.com>
 */
@RunWith( MockitoJUnitRunner.class )
public class AlertResourceBeanTest extends ResourceBeanTest {

   @Inject
   AlertResourceBean iAlertResourceBean;

   @Mock
   private EJBContext iEJBContext;

   @Mock
   private Principal iPrincipal;

   @Mock
   RaiseAlertService iRaiseAlertService;

   @Mock
   AlertTypeController iAlertTypeController;

   private static final Integer ALERT_ID1 = 104829;
   private static final Integer ALERT_TYPE_ID1 = 274;
   private static final String STATUS_CODE1 = "NEW";
   private static final String ALERT_MESSAGE1 = "Usage parameter synchronization was successful.";
   private static final String PARAM_TYPE1 = "STRING";
   private static final String PARAM_VALUE1 = "1";
   private static final String PARAM_DISPLAY_VALUE1 = "1";

   private static final Integer ALERT_ID2 = 104830;
   private static final Integer ALERT_TYPE_ID2 = 274;
   private static final String STATUS_CODE2 = "NEW";
   private static final String ALERT_MESSAGE2 = "Usage parameter synchronization was successful.";
   private static final String PARAM_TYPE2 = "STRING";
   private static final String PARAM_VALUE2 = "1";
   private static final String PARAM_DISPLAY_VALUE2 = "1";

   private static final Integer ALERT_ID3 = 104831;
   private static final Integer ALERT_TYPE_ID3 = 274;
   private static final String STATUS_CODE3 = "NEW";
   private static final String ALERT_MESSAGE3 = "Usage parameter synchronization was successful.";
   private static final String PARAM_TYPE3 = "STRING";
   private static final String PARAM_VALUE3 = "1";
   private static final String PARAM_DISPLAY_VALUE3 = "1";

   private static final Integer FAKE_ALERT_ID = 9999;
   private static final Integer FAKE_ALERT_TYPE_ID = 666;

   // to construct the AlertType object
   private static final String ALERT_TYPE_NAME = "core.alert.SYNC_USAGE_PARAMETERS_COMPLETED_name";
   private static final String ALERT_TYPE_DESC =
         "core.alert.SYNC_USAGE_PARAMETERS_COMPLETED_description";
   private static final String ALERT_TYPE_NOTIFY_CD = "PRIVATE";
   private static final String ALERT_TYPE_NOTIFY_CLASS = null;
   private static final String ALERT_TYPE_CATEGORY = "INVENTORY";
   private static final String ALERT_TYPE_MESSAGE =
         "core.alert.SYNC_USAGE_PARAMETERS_COMPLETED_message";

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setup() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );

      setAuthorizedUser( "authorized" );
      setUnauthorizedUser( "unauthorized" );
      initializeTest();

      iAlertResourceBean.setEJBContext( iEJBContext );
      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );

   }


   /**
    * Test GET alert success scenario
    *
    * @throws Exception
    */
   @Test
   public void testGetAlertByIdSuccessfully() throws AmApiResourceNotFoundException {
      Mockito.when( iAlertTypeController.getAlertType( ALERT_TYPE_ID1 ) )
            .thenReturn( constructAlertType( ALERT_TYPE_ID1 ) );
      Alert lActualAlert = iAlertResourceBean.get( ALERT_ID1 );
      Alert lExpectedAlert = getAlertList().get( 0 );

      assertTrue( lExpectedAlert.equals( lActualAlert ) );
   }


   /**
    * Test GET alert Resource Not found scenario
    *
    * @throws Exception
    */

   @Test
   public void testGetAlertByIdResourceNotFound() throws Exception {
      try {
         iAlertResourceBean.get( FAKE_ALERT_ID );
      } catch ( AmApiResourceNotFoundException aE ) {
         assertTrue( FAKE_ALERT_ID.toString().equals( aE.getId() ) );
      }
   }


   /**
    * Test Search alert success scenario
    *
    * @throws AmApiBusinessException
    *
    * @throws Exception
    */
   @Test
   public void testSearchAlertByIdSuccessfully() throws Exception {
      Mockito.when( iAlertTypeController.getAlertType( ALERT_TYPE_ID1 ) )
            .thenReturn( constructAlertType( ALERT_TYPE_ID1 ) );

      AlertSearchParameters lAlertSearchParameters = new AlertSearchParameters();
      lAlertSearchParameters.setAlertTypeId( ALERT_TYPE_ID1 );
      lAlertSearchParameters.setAlertStatusCode( STATUS_CODE1 );
      lAlertSearchParameters.setAssignParam( "false" );

      List<Alert> lExpectedAlertList = getAlertList();

      List<Alert> lActualAlertList = iAlertResourceBean.search( lAlertSearchParameters );

      Assert.assertEquals( 3, lActualAlertList.size() );

      Assert.assertTrue( lExpectedAlertList.get( 0 ).equals( lActualAlertList.get( 0 ) ) );
      Assert.assertTrue( lExpectedAlertList.get( 1 ).equals( lActualAlertList.get( 1 ) ) );
      Assert.assertTrue( lExpectedAlertList.get( 2 ).equals( lActualAlertList.get( 2 ) ) );

   }


   /**
    *
    * Test search method for Not found
    *
    * @throws AmApiBusinessException
    */
   @Test
   public void testSearchAlertByIdResourceNotFound() throws Exception {
      try {
         AlertSearchParameters lAlertSearchParameters = new AlertSearchParameters();
         lAlertSearchParameters.setAlertTypeId( FAKE_ALERT_TYPE_ID );
         lAlertSearchParameters.setAlertStatusCode( STATUS_CODE1 );
         lAlertSearchParameters.setAssignParam( "true" );

         iAlertResourceBean.search( lAlertSearchParameters );
         Assert.fail( "Expected exception" );
      } catch ( AmApiResourceNotFoundException aE ) {
         assertTrue( FAKE_ALERT_TYPE_ID.toString().equals( aE.getId() ) );
      }
   }


   /**
    * Test acknowledge alerts success scenario
    *
    * @throws Exception
    */
   @Test
   public void deleteAlertsSuccessfully() throws Exception {
      Mockito.when( iAlertTypeController.getAlertType( ALERT_TYPE_ID1 ) )
            .thenReturn( constructAlertType( ALERT_TYPE_ID1 ) );
      try {
         AlertSearchParameters lAlertSearchParameters = new AlertSearchParameters();
         lAlertSearchParameters.setAlertTypeId( ALERT_TYPE_ID1 );
         lAlertSearchParameters.setAlertStatusCode( STATUS_CODE1 );
         lAlertSearchParameters.setAssignParam( "true" );
         iAlertResourceBean.delete( lAlertSearchParameters );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( ALERT_TYPE_ID1.toString(), aE.getId() );
      }
   }


   /**
    * Test acknowledge alerts success scenario - Delete all Alerts
    *
    * @throws Exception
    */
   @Test
   public void deleteAllAlertsSuccessfully() throws Exception {
      Mockito.when( iAlertTypeController.getAlertType( ALERT_TYPE_ID1 ) )
            .thenReturn( constructAlertType( ALERT_TYPE_ID1 ) );
      try {
         AlertSearchParameters lAlertSearchParameters = new AlertSearchParameters();
         lAlertSearchParameters.setAlertTypeId( null );
         lAlertSearchParameters.setAlertStatusCode( null );
         lAlertSearchParameters.setAssignParam( null );
         iAlertResourceBean.delete( lAlertSearchParameters );
      } catch ( AmApiBadRequestException aE ) {
         Assert.fail( "An exception was thrown during succes test scenario" );
      }
   }


   private List<Alert> getAlertList() {
      List<Alert> lExpectedAlertList = new ArrayList<Alert>();

      // Alert1
      Alert lAlert1 = new Alert();
      lAlert1.setMessage( ALERT_MESSAGE1 );
      lAlert1.setAlertId( ALERT_ID1 );
      lAlert1.setAlertStatusCode( STATUS_CODE1 );
      lAlert1.setAlertTypeId( ALERT_TYPE_ID1 );

      List<AlertParameter> lAlertParamList1 = new ArrayList<AlertParameter>();
      AlertParameter lAlertParam1 = new AlertParameter();
      lAlertParam1.setType( PARAM_TYPE1 );
      lAlertParam1.setValue( PARAM_VALUE1 );
      lAlertParam1.setDisplayValue( PARAM_DISPLAY_VALUE1 );
      lAlertParamList1.add( lAlertParam1 );

      lAlert1.setParameters( lAlertParamList1 );

      lExpectedAlertList.add( lAlert1 );

      // Alert2
      Alert lAlert2 = new Alert();
      lAlert2.setMessage( ALERT_MESSAGE2 );
      lAlert2.setAlertId( ALERT_ID2 );
      lAlert2.setAlertStatusCode( STATUS_CODE2 );
      lAlert2.setAlertTypeId( ALERT_TYPE_ID2 );

      List<AlertParameter> lAlertParamList2 = new ArrayList<AlertParameter>();
      AlertParameter lAlertParam2 = new AlertParameter();
      lAlertParam2.setType( PARAM_TYPE2 );
      lAlertParam2.setValue( PARAM_VALUE2 );
      lAlertParam2.setDisplayValue( PARAM_DISPLAY_VALUE2 );
      lAlertParamList2.add( lAlertParam2 );

      lAlert2.setParameters( lAlertParamList2 );

      lExpectedAlertList.add( lAlert2 );

      // Alert3
      Alert lAlert3 = new Alert();
      lAlert3.setMessage( ALERT_MESSAGE3 );
      lAlert3.setAlertId( ALERT_ID3 );
      lAlert3.setAlertStatusCode( STATUS_CODE3 );
      lAlert3.setAlertTypeId( ALERT_TYPE_ID3 );

      List<AlertParameter> lAlertParamList3 = new ArrayList<AlertParameter>();
      AlertParameter lAlertParam3 = new AlertParameter();
      lAlertParam3.setType( PARAM_TYPE3 );
      lAlertParam3.setValue( PARAM_VALUE3 );
      lAlertParam3.setDisplayValue( PARAM_DISPLAY_VALUE3 );
      lAlertParamList3.add( lAlertParam3 );

      lAlert3.setParameters( lAlertParamList3 );

      lExpectedAlertList.add( lAlert3 );
      return lExpectedAlertList;

   }


   private AlertType constructAlertType( int aAlertTypeId ) {
      AlertType lAlertType = new AlertType();
      lAlertType.setId( aAlertTypeId );
      lAlertType.setName( ALERT_TYPE_NAME );
      lAlertType.setDescription( ALERT_TYPE_DESC );
      lAlertType.setNotifyCode( ALERT_TYPE_NOTIFY_CD );
      lAlertType.setNotifyClass( ALERT_TYPE_NOTIFY_CLASS );
      lAlertType.setCategory( ALERT_TYPE_CATEGORY );
      lAlertType.setMessage( ALERT_TYPE_MESSAGE );

      return lAlertType;
   }

}
