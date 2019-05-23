package com.mxi.mx.db.event.rfq;

import java.security.Principal;
import java.text.ParseException;
import java.util.Map;

import javax.ejb.EJBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.events.EventType;
import com.mxi.am.api.events.MaintenixEvent;
import com.mxi.am.api.events.RFQUpdatedEvent;
import com.mxi.am.api.resource.materials.proc.rfq.RFQDefinition;
import com.mxi.am.api.resource.materials.proc.rfq.RFQDefinitionSearchParameters;
import com.mxi.am.api.resource.materials.proc.rfq.impl.RFQResourceBean;
import com.mxi.am.api.util.LegacyKeyUtil;
import com.mxi.am.api.util.UserUtil;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.services.event.MxEventServiceStub;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.ejb.rfq.RFQBean;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RFQHeaderKey;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 *
 * Database level test for RFQ Send event
 *
 */

@RunWith( MockitoJUnitRunner.class )
public class RFQSendEventTest extends ResourceBeanTest {

   @Inject
   RFQResourceBean rfqResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Inject
   LegacyKeyUtil legacyKeyUtil;

   @Inject
   UserUtil userUtil;

   private static final String ID = "ACF52996EED24447BA0C73FA58921C04";

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();

   private String principalName;
   private MxEventServiceStub mxEventServiceStub;


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      rfqResourceBean.setEJBContext( ejbContext );
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      mxEventServiceStub = new MxEventServiceStub();

      principalName = ejbContext.getCallerPrincipal().getName();
   }


   // Define DataConnectionRule with events that need to be disabled/enabled for tests.
   @ClassRule
   public static final DatabaseConnectionRule databaseConnectionRule =
         new DatabaseConnectionRule( () -> {
            enableEvents();
         }, () -> {
            disableEvents();
         } );


   static void enableEvents() {
      // Enable events after loading data
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.ENABLE_EVENT_MX_RFQ_UPDATED" );

   }


   static void disableEvents() {
      MxDataAccess.getInstance()
            .execute( "com.mxi.am.data.definition.query.DISABLE_EVENT_MX_RFQ_UPDATED" );
   }


   @Test
   public void testRFQSendEventSucess() throws Exception {

      HumanResourceKey humanResourceKey =
            userUtil.getUserAsHumanResourceFromUserName( principalName );

      RFQHeaderKey rfqHeader = legacyKeyUtil.altIdToLegacyKey( ID, RFQHeaderKey.class );

      RFQHeaderKey[] rfqHeaderKeys = new RFQHeaderKey[1];
      rfqHeaderKeys[0] = rfqHeader;

      RFQBean rfqBean = new RFQBean();
      rfqBean.send( rfqHeaderKeys, humanResourceKey );

      Map<String, MaintenixEvent> map = mxEventServiceStub.getPublishedEvents();

      RFQUpdatedEvent rfqUpdatedEvent = ( RFQUpdatedEvent ) map.get( "MX_RFQ_UPDATED" );
      RFQDefinition expectedRFQDefinition = rfqUpdatedEvent.getAfter().getRFQDefinition();

      RFQDefinitionSearchParameters rfqDefinitionSearchParameters =
            new RFQDefinitionSearchParameters();
      rfqDefinitionSearchParameters.setLines( "false" );
      rfqDefinitionSearchParameters.setVendors( "false" );
      rfqDefinitionSearchParameters.setLineVendors( "false" );

      RFQDefinition actualRFQDefinition = rfqResourceBean.get( ID, rfqDefinitionSearchParameters );

      Assert.assertEquals( EventType.MX_RFQ_UPDATED, map.get( "MX_RFQ_UPDATED" ).getEventType() );
      Assert.assertEquals( expectedRFQDefinition, actualRFQDefinition );

   }

}
