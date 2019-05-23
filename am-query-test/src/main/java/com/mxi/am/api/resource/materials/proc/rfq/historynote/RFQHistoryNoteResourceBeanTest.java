package com.mxi.am.api.resource.materials.proc.rfq.historynote;

import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.proc.rfq.historynote.impl.RFQHistoryNoteResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Smoke test for HistoryNoteResourceBean
 *
 * @author Lavanya WidanaPathirana <lavanya.widana.pathirana@ifsworld.com>
 */

@RunWith( MockitoJUnitRunner.class )
public class RFQHistoryNoteResourceBeanTest extends ResourceBeanTest {

   @Inject
   RFQHistoryNoteResourceBean iRFQHistoryNoteResourceBean;

   @Mock
   private Principal iPrincipal;

   @Mock
   private EJBContext iEJBContext;

   private static final String ALT_ID1 = "4FEA5D917FCD11E4A0EC71C5C65E2E92";
   private static final String DATE1 = "2007-01-11T05:00:00Z";
   private static final String NOTE1 = "Added the vendor ACE (Ace Electronics) to the RFQ.";
   private static final String USER_ID1 = "A2E94D90A33A49DD8328F219D9A668D3";
   private static final String STATUS1 = null;
   private static final String RFQ_ID1 = "2B74126CF40C42A292F199272942E6BF";
   private static final String STAGE_REASON_CODE1 = "NEW";

   private static final String INVALID_ID = "00000000000110000000000000000001";

   private static final String ALT_ID2 = "4F2E11DF7FCD11E4A0EC71C5C65E2E92";
   private static final String DATE2 = "2007-01-11T05:00:00Z";
   private static final String NOTE2 = "Added the vendor BOE (Boeing) to the RFQ.";
   private static final String USER_ID2 = "A2E94D90A33A49DD8328F219D9A668D3";
   private static final String STATUS2 = null;
   private static final String RFQ_ID2 = "2B74126CF40C42A292F199272942E6BF";
   private static final String STAGE_REASON_CODE2 = "NEW";

   private static final String INVALID_RFQ_ID = "10000001111111110000011111111";


   private List<RFQHistoryNote> constructExpectedResults() throws ParseException {
      List<RFQHistoryNote> lRFQHistoryNoteList = new ArrayList<RFQHistoryNote>();
      RFQHistoryNote lRFQHistoryNote1 = new RFQHistoryNote();
      lRFQHistoryNote1.setId( ALT_ID1 );
      Date lDate1;
      try {
         lDate1 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( DATE1 );
      } catch ( ParseException e ) {
         throw ( e );
      }
      lRFQHistoryNote1.setDate( lDate1 );
      lRFQHistoryNote1.setNote( NOTE1 );
      lRFQHistoryNote1.setUserId( USER_ID1 );
      lRFQHistoryNote1.setStatus( STATUS1 );
      lRFQHistoryNote1.setRfqId( RFQ_ID1 );
      lRFQHistoryNote1.setStageReasonCode( STAGE_REASON_CODE1 );

      lRFQHistoryNoteList.add( lRFQHistoryNote1 );

      RFQHistoryNote lRFQHistoryNote2 = new RFQHistoryNote();
      lRFQHistoryNote2.setId( ALT_ID2 );
      Date lDate2;
      try {
         lDate2 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( DATE2 );
      } catch ( ParseException e ) {
         throw ( e );
      }
      lRFQHistoryNote2.setDate( lDate2 );
      lRFQHistoryNote2.setNote( NOTE2 );
      lRFQHistoryNote2.setUserId( USER_ID2 );
      lRFQHistoryNote2.setStatus( STATUS2 );
      lRFQHistoryNote2.setRfqId( RFQ_ID2 );
      lRFQHistoryNote2.setStageReasonCode( STAGE_REASON_CODE2 );

      lRFQHistoryNoteList.add( lRFQHistoryNote2 );

      return lRFQHistoryNoteList;
   }


   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      iRFQHistoryNoteResourceBean.setEJBContext( iEJBContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( iEJBContext.getCallerPrincipal() ).thenReturn( iPrincipal );
      Mockito.when( iPrincipal.getName() ).thenReturn( AUTHORIZED );
   }


   /**
    * Test get method for getHistoryNoteById
    *
    * @throws AmApiResourceNotFoundException
    * @throws ParseException
    */
   @Test
   public void testGetHistoryNoteById() throws AmApiResourceNotFoundException, ParseException {
      RFQHistoryNote lRFQHistoryNote = iRFQHistoryNoteResourceBean.get( ALT_ID1 );
      assertTrue( constructExpectedResults().get( 0 ).equals( lRFQHistoryNote ) );
   }


   /**
    * Test get method for preconditionFailed
    *
    * @throws AmApiResourceNotFoundException
    * @throws ParseException
    */
   @Test
   public void testBadRequest() throws AmApiResourceNotFoundException {
      try {
         RFQHistoryNote lRFQHistoryNote = iRFQHistoryNoteResourceBean.get( null );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide History Note alt Id", aE.getMessage() );
      }

   }


   /**
    * Test get method for historyNoteDefinitionNotFound
    */
   @Test
   public void testHistoryNoteDefinitionNotFound() {
      try {
         RFQHistoryNote lRFQHistoryNoten = iRFQHistoryNoteResourceBean.get( INVALID_ID );
      } catch ( AmApiResourceNotFoundException aE ) {
         Assert.assertEquals( INVALID_ID, aE.getId() );
      }
   }


   /**
    * Test method for RFQ History Note post method
    */
   @Test
   public void testCreateHistoryNote() throws Exception {

      RFQHistoryNote lRFQHistoryNote = constructExpectedResults().get( 0 );

      RFQHistoryNote lRespHistoryNote = iRFQHistoryNoteResourceBean.post( lRFQHistoryNote );

      Assert.assertEquals( lRFQHistoryNote.getNote(), lRespHistoryNote.getNote() );
      Assert.assertEquals( lRFQHistoryNote.getRfqId(), lRespHistoryNote.getRfqId() );
   }


   /**
    * Test precondition failed for the RFQ History Note POST Method
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void testCreateHistoryNoteBadRequest() throws AmApiResourceNotFoundException {
      try {
         RFQHistoryNote lRFQHistoryNote = iRFQHistoryNoteResourceBean.post( null );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide a note to be added", aE.getMessage() );
      }

   }


   /**
    * Test search method for getHistoryNoteById - success scenario
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void testSearchHistoryNoteByRfqId() throws Exception {
      RFQHistoryNoteSearchParameters lRFQHistoryNoteSearchParameters =
            new RFQHistoryNoteSearchParameters();
      lRFQHistoryNoteSearchParameters.setRfqId( RFQ_ID1 );

      List<RFQHistoryNote> lExpectedRFQHistoryNoteList = constructExpectedResults();

      List<RFQHistoryNote> lActualRFQHistoryNoteList =
            iRFQHistoryNoteResourceBean.search( lRFQHistoryNoteSearchParameters );

      Assert.assertEquals( lExpectedRFQHistoryNoteList.size(), lActualRFQHistoryNoteList.size() );

      Assert.assertTrue(
            lExpectedRFQHistoryNoteList.get( 0 ).equals( lActualRFQHistoryNoteList.get( 0 ) ) );
      Assert.assertTrue(
            lExpectedRFQHistoryNoteList.get( 1 ).equals( lActualRFQHistoryNoteList.get( 1 ) ) );
   }


   /**
    * Test search method for searchHistoryNoteByRfqId - bad request
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void testSearchHistoryNoteBadRequestWithNullRfqId()
         throws AmApiResourceNotFoundException {
      try {
         RFQHistoryNoteSearchParameters lRFQHistoryNoteSearchParameters =
               new RFQHistoryNoteSearchParameters();
         lRFQHistoryNoteSearchParameters.setRfqId( null );

         iRFQHistoryNoteResourceBean.search( lRFQHistoryNoteSearchParameters );
         Assert.fail( "Expected exception" );
      } catch ( AmApiBadRequestException aE ) {
         Assert.assertEquals( "Please provide a Rfq Id", aE.getMessage() );
      }
   }


   /**
    * Test search method for searchHistoryNoteByRfqId - bad request
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   public void testSearchHistoryNoteBadRequestWithInvlidRfqId()
         throws AmApiResourceNotFoundException {
      try {
         RFQHistoryNoteSearchParameters lRFQHistoryNoteSearchParameters =
               new RFQHistoryNoteSearchParameters();
         lRFQHistoryNoteSearchParameters.setRfqId( INVALID_RFQ_ID );

         iRFQHistoryNoteResourceBean.search( lRFQHistoryNoteSearchParameters );
         Assert.fail( "Expected exception" );
      } catch ( AmApiResourceNotFoundException aE ) {
         assertTrue( INVALID_RFQ_ID.toString().equals( aE.getId() ) );
      }
   }

}
