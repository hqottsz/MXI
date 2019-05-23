package com.mxi.am.api.resource.maintenance.eng.config.assembly.zone;

import static org.junit.Assert.assertEquals;

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
import com.mxi.am.api.resource.maintenance.eng.config.assembly.zone.impl.ZoneResourceBean;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke test class for ZoneResourceBean
 *
 */

@RunWith( MockitoJUnitRunner.class )
public class ZoneResourceBeanTest extends ResourceBeanTest {

   @Inject
   ZoneResourceBean iZoneResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ZONE_CODE1 = "412";
   private static final String ZONE_CODE2 = "413";
   private static final String ASSEMBLY_ID1 = "92078DB90B2740589C72D651D415AB46";
   private static final String ASSEMBLY_ID2 = "82078DB90B2740589C72D651D415AB46";
   private static final String ASSEMBLY_CODE1 = "B767-200";
   private static final String ASSEMBLY_CODE2 = "B767-201";
   private static final String ZONE_DESCRIPTION1 = "Zone Description 1";
   private static final String ZONE_DESCRIPTION2 = "Zone Description 2";
   private static final String ID1 = "29B2673ABD6249DBBA36F2B3173B086E";
   private static final String ID2 = "28B2673ABD6249DBBA36F2B3173B086E";
   private static final String ID3 = "27B2673ABD6249DBBA36F2B3173B086E";
   private static final String INVALID_ZONE_ID = "11B2673ABD6249DBBA36F2B3173B086E";
   private static final String NULL_ZONE_ID = "";
   private static final String NULL_ASSEMBLY_ID = "";
   private static final String NULL_ZONE_CODE = "";
   private static final String INVALID_ASSEMBLY_ID = "12078DB90B2740589C72D651D415AB46";
   private static final String INVALID_ZONE_CODE = "423";
   Zone iActualZone1 = new Zone();
   Zone iActualZone2 = new Zone();
   Zone iActualZone3 = new Zone();


   @Before
   public void setUp() throws MxException {

      InjectorContainer.get().injectMembers( this );
      iZoneResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );
      constructZoneObjects();
   }


   /**
    *
    * Test correct Zone object returns for a valid zone_id success path- Response-
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testGetZoneById() throws AmApiResourceNotFoundException {
      Zone lZone = iZoneResourceBean.get( ID1 );
      assertEquals( lZone, iActualZone1 );
   }


   /**
    *
    * Test null returns for invalid zone_id
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetNoRecordRetrieved() throws AmApiResourceNotFoundException {
      iZoneResourceBean.get( INVALID_ZONE_ID );

   }


   /**
    * Test null returns for empty zone_id Response-
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetNoZoneFound() throws AmApiResourceNotFoundException {
      iZoneResourceBean.get( NULL_ZONE_ID );

   }


   /**
    *
    * Test correct Zone object returns for a valid assembly_id and zone_code single record returned
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchByAllParametersSucess() throws AmApiResourceNotFoundException {
      ZoneSearchParameters lZoneSearchParameters =
            new ZoneSearchParameters( ASSEMBLY_ID1, ZONE_CODE1 );
      List<Zone> lZoneList = iZoneResourceBean.search( lZoneSearchParameters );

      assertEquals( 1, lZoneList.size() );
      assertEquals( iActualZone1, lZoneList.get( 0 ) );
   }


   /**
    *
    * Test correct Zone object returns for a valid assembly_id without zone_code multiple records
    * returned - success path
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchByAssemblyIdSucess() throws AmApiResourceNotFoundException {
      ZoneSearchParameters lZoneSearchParameters = new ZoneSearchParameters( ASSEMBLY_ID2, "" );
      List<Zone> lZoneList = iZoneResourceBean.search( lZoneSearchParameters );

      assertEquals( 2, lZoneList.size() );
      assertEquals( iActualZone2, lZoneList.get( 0 ) );
      assertEquals( iActualZone3, lZoneList.get( 1 ) );
   }


   /**
    *
    * Test correct Zone object returns for a valid zone_code without assembly_id multiple records
    * returned - success path- Response-
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchByZoneCodeSucess() throws AmApiResourceNotFoundException {
      ZoneSearchParameters lZoneSearchParameters = new ZoneSearchParameters( "", ZONE_CODE1 );
      List<Zone> lZoneList = iZoneResourceBean.search( lZoneSearchParameters );

      assertEquals( 2, lZoneList.size() );
      assertEquals( iActualZone1, lZoneList.get( 0 ) );
      assertEquals( iActualZone2, lZoneList.get( 1 ) );
   }


   /**
    *
    * Test search method if all the null parameters passed response- 400
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test( expected = AmApiPreconditionFailException.class )
   public void testSearchAllNullParam() throws AmApiResourceNotFoundException {
      ZoneSearchParameters lZoneSearchParameters =
            new ZoneSearchParameters( NULL_ASSEMBLY_ID, NULL_ZONE_CODE );
      iZoneResourceBean.search( lZoneSearchParameters );
   }


   /**
    *
    * Test null returns for invalid assembly_id and zone_code Response-
    *
    * @throws AmApiResourceNotFoundException
    *
    */
   @Test
   public void testSearchNoResultFound() throws AmApiResourceNotFoundException {
      ZoneSearchParameters lZoneSearchParameters =
            new ZoneSearchParameters( INVALID_ASSEMBLY_ID, INVALID_ZONE_CODE );
      List<Zone> lZoneList = iZoneResourceBean.search( lZoneSearchParameters );

      assertEquals( 0, lZoneList.size() );

   }


   public void constructZoneObjects() {

      iActualZone1.setId( ID1 );
      iActualZone1.setZoneCode( ZONE_CODE1 );
      iActualZone1.setAssemblyId( ASSEMBLY_ID1 );
      iActualZone1.setAssemblyCode( ASSEMBLY_CODE1 );
      iActualZone1.setZoneDescription( ZONE_DESCRIPTION1 );

      iActualZone2.setId( ID2 );
      iActualZone2.setZoneCode( ZONE_CODE1 );
      iActualZone2.setAssemblyId( ASSEMBLY_ID2 );
      iActualZone2.setAssemblyCode( ASSEMBLY_CODE2 );
      iActualZone2.setZoneDescription( ZONE_DESCRIPTION1 );

      iActualZone3.setId( ID3 );
      iActualZone3.setZoneCode( ZONE_CODE2 );
      iActualZone3.setAssemblyId( ASSEMBLY_ID2 );
      iActualZone3.setAssemblyCode( ASSEMBLY_CODE2 );
      iActualZone3.setZoneDescription( ZONE_DESCRIPTION2 );
   }

}
