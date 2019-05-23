package com.mxi.am.api.resource.maintenance.eng.config.assembly.panel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiPreconditionFailException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.eng.config.assembly.panel.impl.PanelResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Test class for Panel Api GET methods
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class PanelResourceBeanTest extends ResourceBeanTest {

   private static final String PANEL_ID1 = "547F9AC110DC48C189A88E0D7E0B91DE";
   private static final String PANEL_ID2 = "01EFDA12E1C647C4B9EB8534C4D88B8A";
   private static final String INVALID_PANEL_ID = "5D732E2E0D41470E816E7ABF14C8AF38";
   private static final String EMPTY_PANEL_ID = "";
   private static final String PANEL_CODE = "PANEL1";
   private static final String PANEL_DESC = "Panel 1";
   private static final String PANEL_CODE2 = "PANEL2";
   private static final String PANEL_DESC2 = "Panel 2";
   private static final String ASSEMBLY_ID = "5D732E2E0D41470E816E7ABF14C8AF38";
   private static final String ZONE_CODE = "ZONE1";
   private static final String ASSEMBLY_CODE = "A320";
   private Panel expectedPanelOne = new Panel();
   private Panel expectedPanelTwo = new Panel();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Inject
   private PanelResourceBean panelResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {
      // Guice injection to avoid permission checks
      InjectorContainer.get().injectMembers( this );
      panelResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      constructExpectedResults();
   }


   /**
    *
    * Test correct panel object returns for valid panel_id - Response 200
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSingleResult200() throws AmApiResourceNotFoundException {
      Panel panel = panelResourceBean.get( PANEL_ID1 );
      assertEquals( panel, expectedPanelOne );
   }


   /**
    *
    * Test null returns for invalid panel_id - Response 200
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testNoRecordsRetrieved200() throws AmApiResourceNotFoundException {
      panelResourceBean.get( INVALID_PANEL_ID );
   }


   /**
    * Test null returns for empty panel_id - Response 200
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testPanelNotFound200() throws AmApiResourceNotFoundException {
      panelResourceBean.get( EMPTY_PANEL_ID );

   }


   /**
    *
    * Test correct panel object returns for passed assembly_id, zone_code and panel_code - Response
    * 200
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testResultSearch200() throws AmApiResourceNotFoundException {
      PanelSearchParameters panelSearchParameters =
            new PanelSearchParameters( ASSEMBLY_ID, ZONE_CODE, PANEL_CODE );
      List<Panel> panelList = panelResourceBean.search( panelSearchParameters );

      assertEquals( 1, panelList.size() );
      assertEquals( expectedPanelOne, panelList.get( 0 ) );

   }


   /**
    *
    * Test multiple panel objects return for passed assembly id - Response 200
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testMultipleResults200() throws AmApiResourceNotFoundException {
      PanelSearchParameters panelSearchParameters =
            new PanelSearchParameters( ASSEMBLY_ID, "", "" );
      List<Panel> panelList = panelResourceBean.search( panelSearchParameters );

      assertEquals( 2, panelList.size() );
      assertTrue( panelList.contains( expectedPanelOne ) );
      assertTrue( panelList.contains( expectedPanelTwo ) );
   }


   /**
    *
    * Test bad request returns for empty parameters - Response 400
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiPreconditionFailException.class )
   public void testBadRequest400() throws AmApiResourceNotFoundException {
      PanelSearchParameters panelSearchParameters = new PanelSearchParameters();
      panelResourceBean.search( panelSearchParameters );
   }


   private void constructExpectedResults() {
      expectedPanelOne.setId( PANEL_ID1 );
      expectedPanelOne.setAssemblyId( ASSEMBLY_ID );
      expectedPanelOne.setAssemblyCode( ASSEMBLY_CODE );
      expectedPanelOne.setPanelCode( PANEL_CODE );
      expectedPanelOne.setPanelDescription( PANEL_DESC );
      expectedPanelOne.setZoneCode( ZONE_CODE );

      expectedPanelTwo.setId( PANEL_ID2 );
      expectedPanelTwo.setAssemblyId( ASSEMBLY_ID );
      expectedPanelTwo.setAssemblyCode( ASSEMBLY_CODE );
      expectedPanelTwo.setPanelCode( PANEL_CODE2 );
      expectedPanelTwo.setPanelDescription( PANEL_DESC2 );
      expectedPanelTwo.setZoneCode( null );
   }
}
