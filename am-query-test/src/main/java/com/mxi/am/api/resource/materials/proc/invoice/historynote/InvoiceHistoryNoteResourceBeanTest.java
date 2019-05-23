package com.mxi.am.api.resource.materials.proc.invoice.historynote;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBContext;

import org.apache.commons.collections.CollectionUtils;
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
import com.mxi.am.api.exception.AmApiInternalServerException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.proc.invoice.historynote.impl.InvoiceHistoryNoteResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test to Invoice Historynote API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class InvoiceHistoryNoteResourceBeanTest extends ResourceBeanTest {

   private static final String INVOICE_HISTORYNOTE_ALT_ID_1 = "4F5438727FCD11E4A0EC71C5C65E2E92";
   private static final String INVOICE_ID_1 = "9A7844B819474FE6B8C88BD406695423";
   private static final String USER_ID_1 = "3EE38732492F4BDFAB8F09B51C1E4596";
   private static final String STAGE_REASON_CODE_1 = "APPROVAL";
   private static final String STAGE_NOTE_1 = "PO invoice created.";
   private static final String EVENT_STATUS_CODE_1 = "ACTV";
   private static final String STAGE_GDT_1 = "2007-01-11T05:00:00Z";

   private static final String INVOICE_HISTORYNOTE_ALT_ID_2 = "4F2930C77FCD11E4A0EC71C5C65E2E93";
   private static final String INVOICE_ID_2 = "98A0DC5475044BFB96FE1A5F53F162D1";
   private static final String USER_ID_2 = "536BE52F72764B77A8E3D4F2BE9127C7";
   private static final String STAGE_REASON_CODE_2 = "APPROVAL";
   private static final String STAGE_NOTE_2 = "PO invoice has been validated for payment.";
   private static final String EVENT_STATUS_CODE_2 = "PITOBEPAID";
   private static final String STAGE_GDT_2 = "2008-01-15T05:00:00Z";

   private static final String INVOICE_ID_TO_CREATE = "2A519CFE4BCA49DA8A5D4D703FF0BC60";
   private static final String USER_ID_TO_CREATE = "FBB6ADDF6E8342ECA5955B1CA243B9FB";
   private static final String STAGE_NOTE_TO_CREATE = "PO invoice has been paid.";
   private static final String STAGE_GDT_TO_CREATE = "2007-01-12T05:00:00Z";
   private static final String STAGE_REASON_CD_TO_CREATE = "EXP";

   private static final String INVOICE_ID_TO_CREATE_2 = "2A519CFE4BCA49DA8A5D4D703FF0BC61";

   private static final String INVALID_HISTORY_NOTE_ID = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
   private static final String SELECT_PARM = "ID";
   private static final String INVALID_SELECT_PARM = "FFFFFFFFF";
   private static final String INVALID_INVOICE_ID = "00000000000110000000000000000001";

   @Inject
   InvoiceHistoryNoteResourceBean invoiceHistoryNoteResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext EJBContext;

   @Rule
   public InjectionOverrideRule fakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      invoiceHistoryNoteResourceBean.setEJBContext( EJBContext );
      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( EJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testGetInvoiceHistoryNoteByIdSuccess()
         throws AmApiResourceNotFoundException, ParseException {
      InvoiceHistoryNote actualHistoryNoteObject =
            invoiceHistoryNoteResourceBean.get( INVOICE_HISTORYNOTE_ALT_ID_1 );
      assertInvoiceHistoryNoteObjectEquals( constructInvoiceHistoryNote(),
            actualHistoryNoteObject );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetInvoiceHistoryNoteByIdResourceNotFound()
         throws AmApiResourceNotFoundException {
      invoiceHistoryNoteResourceBean.get( INVALID_HISTORY_NOTE_ID );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetInvoiceHistoryNoteByNullId() throws AmApiResourceNotFoundException {
      invoiceHistoryNoteResourceBean.get( null );
   }


   @Test
   public void testSearchInvoiceHistoryNotesByListOfInvoiceIds() throws ParseException {
      InvoiceHistoryNoteSearchParameters invoiceHistoryNoteSearchParameters =
            new InvoiceHistoryNoteSearchParameters();
      List<String> invoiceIds = new ArrayList<String>();
      invoiceIds.add( INVOICE_ID_1 );
      invoiceIds.add( INVOICE_ID_2 );
      invoiceHistoryNoteSearchParameters.setInvoiceIds( invoiceIds );
      List<InvoiceHistoryNote> invoiceHistoryNotes =
            invoiceHistoryNoteResourceBean.getHistoryNotes( invoiceHistoryNoteSearchParameters );
      Assert.assertEquals( 2, invoiceHistoryNotes.size() );
      Assert.assertTrue( invoiceHistoryNotes.contains( constructInvoiceHistoryNote() ) );
      Assert.assertTrue( invoiceHistoryNotes.contains( constructInvoiceHistoryNote2() ) );
   }


   @Test
   public void testSearchInvoiceHistoryNotesByInvalidInvoiceIds() {
      InvoiceHistoryNoteSearchParameters invoiceHistoryNoteSearchParameters =
            new InvoiceHistoryNoteSearchParameters();
      List<String> invoiceIds = new ArrayList<String>();
      invoiceIds.add( INVALID_INVOICE_ID );
      invoiceHistoryNoteSearchParameters.setInvoiceIds( invoiceIds );
      List<InvoiceHistoryNote> invoiceHistoryNotes =
            invoiceHistoryNoteResourceBean.getHistoryNotes( invoiceHistoryNoteSearchParameters );
      assertTrue( CollectionUtils.isEmpty( invoiceHistoryNotes ) );
   }


   @Test
   public void testCreateInvoiceHistoryNoteSuccess() throws AmApiBusinessException, ParseException {
      InvoiceHistoryNote invoiceHistoryNoteToCreate = getInvoiceHistoryNoteToCreate();
      InvoiceHistoryNote createdInvoiceHistoryNote = invoiceHistoryNoteResourceBean
            .post( invoiceHistoryNoteToCreate, new InvoiceHistoryNoteSearchParameters() );
      invoiceHistoryNoteToCreate.setId( createdInvoiceHistoryNote.getId() );
      assertTrue( invoiceHistoryNoteToCreate.equals( createdInvoiceHistoryNote ) );
   }


   @Test
   public void testCreateInvoiceHistoryNoteWithSelectParmSuccess()
         throws AmApiBusinessException, ParseException {
      InvoiceHistoryNote invoiceHistoryNoteToCreate = getInvoiceHistoryNoteToCreate();
      InvoiceHistoryNoteSearchParameters invoiceHistoryNoteSearchParameters =
            new InvoiceHistoryNoteSearchParameters();
      invoiceHistoryNoteSearchParameters.setSelect( SELECT_PARM );
      InvoiceHistoryNote createdInvoiceHistoryNote = invoiceHistoryNoteResourceBean
            .post( invoiceHistoryNoteToCreate, invoiceHistoryNoteSearchParameters );
      InvoiceHistoryNote expectedInvoiceHistoryNote = new InvoiceHistoryNote();
      expectedInvoiceHistoryNote.setId( createdInvoiceHistoryNote.getId() );
      assertTrue( expectedInvoiceHistoryNote.equals( createdInvoiceHistoryNote ) );
   }


   @Test
   public void testCreateInvoiceHistoryNoteNullPayloadBadRequest() throws AmApiBusinessException {
      try {
         InvoiceHistoryNote returnedInvoiceHistoryNote = invoiceHistoryNoteResourceBean.post( null,
               new InvoiceHistoryNoteSearchParameters() );
         Assert.fail( "No bad request exception thrown for null payload" );
      } catch ( AmApiBadRequestException exception ) {
         Assert.assertEquals( "Missing invoice history note payload", exception.getMessage() );
      }
   }


   @Test
   public void testCreateInvoiceHistoryNoteNotFoundInDBInternalserverError()
         throws AmApiBusinessException, ParseException {
      try {
         InvoiceHistoryNote invoiceHistoryNoteToCreate = getInvoiceHistoryNoteToCreate();
         invoiceHistoryNoteToCreate.setInvoiceId( INVOICE_ID_TO_CREATE_2 );
         InvoiceHistoryNote returnedInvoiceHistoryNote = invoiceHistoryNoteResourceBean
               .post( invoiceHistoryNoteToCreate, new InvoiceHistoryNoteSearchParameters() );
         Assert.fail( "No internal server error thrown for history note not found in database" );
      } catch ( AmApiInternalServerException exception ) {
         Assert.assertEquals( "History note was created, but could not be found in database.",
               exception.getMessage() );
      }
   }


   @Test
   public void testCreateInvoiceHistoryNoteInvalidSelectParmInternalServerError()
         throws AmApiBusinessException, ParseException {
      try {
         InvoiceHistoryNote invoiceHistoryNoteToCreate = getInvoiceHistoryNoteToCreate();
         InvoiceHistoryNoteSearchParameters invoiceHistoryNoteSearchParameters =
               new InvoiceHistoryNoteSearchParameters();
         invoiceHistoryNoteSearchParameters.setSelect( INVALID_SELECT_PARM );
         InvoiceHistoryNote returnedInvoiceHistoryNote = invoiceHistoryNoteResourceBean
               .post( invoiceHistoryNoteToCreate, invoiceHistoryNoteSearchParameters );
         Assert.fail(
               "No internal server error " + "" + "" + "" + " thrown for invalid select parm" );
      } catch ( AmApiInternalServerException exception ) {
         Assert.assertEquals( "select option must be id", exception.getMessage() );
      }
   }


   private InvoiceHistoryNote getInvoiceHistoryNoteToCreate() throws ParseException {
      InvoiceHistoryNote invoiceHistoryNoteToCreate = new InvoiceHistoryNote();
      invoiceHistoryNoteToCreate.setInvoiceId( INVOICE_ID_TO_CREATE );
      invoiceHistoryNoteToCreate.setUserId( USER_ID_TO_CREATE );
      invoiceHistoryNoteToCreate.setNote( STAGE_NOTE_TO_CREATE );
      invoiceHistoryNoteToCreate.setStageReasonCode( STAGE_REASON_CD_TO_CREATE );
      Date invoiceDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( STAGE_GDT_TO_CREATE );
      invoiceHistoryNoteToCreate.setDate( invoiceDate );
      return invoiceHistoryNoteToCreate;
   }


   private void assertInvoiceHistoryNoteObjectEquals( InvoiceHistoryNote expectedInvoiceHistoryNote,
         InvoiceHistoryNote actualInvoiceHistoryNote ) {
      assertEquals( expectedInvoiceHistoryNote.getId(), actualInvoiceHistoryNote.getId() );
      assertEquals( expectedInvoiceHistoryNote.getDate(), actualInvoiceHistoryNote.getDate() );
      assertEquals( expectedInvoiceHistoryNote.getInvoiceId(),
            actualInvoiceHistoryNote.getInvoiceId() );
      assertEquals( expectedInvoiceHistoryNote.getUserId(), actualInvoiceHistoryNote.getUserId() );
      assertEquals( expectedInvoiceHistoryNote.getStatus(), actualInvoiceHistoryNote.getStatus() );
      assertEquals( expectedInvoiceHistoryNote.getStageReasonCode(),
            actualInvoiceHistoryNote.getStageReasonCode() );
      assertEquals( expectedInvoiceHistoryNote.getNote(), actualInvoiceHistoryNote.getNote() );

   }


   private InvoiceHistoryNote constructInvoiceHistoryNote() throws ParseException {
      InvoiceHistoryNote invoiceHistoryNote = new InvoiceHistoryNote();
      invoiceHistoryNote.setId( INVOICE_HISTORYNOTE_ALT_ID_1 );
      Date invoiceDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( STAGE_GDT_1 );
      invoiceHistoryNote.setDate( invoiceDate );
      invoiceHistoryNote.setInvoiceId( INVOICE_ID_1 );
      invoiceHistoryNote.setUserId( USER_ID_1 );
      invoiceHistoryNote.setStatus( EVENT_STATUS_CODE_1 );
      invoiceHistoryNote.setStageReasonCode( STAGE_REASON_CODE_1 );
      invoiceHistoryNote.setNote( STAGE_NOTE_1 );
      return invoiceHistoryNote;
   }


   private InvoiceHistoryNote constructInvoiceHistoryNote2() throws ParseException {
      InvoiceHistoryNote invoiceHistoryNote = new InvoiceHistoryNote();
      invoiceHistoryNote.setId( INVOICE_HISTORYNOTE_ALT_ID_2 );
      Date invoiceDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( STAGE_GDT_2 );
      invoiceHistoryNote.setDate( invoiceDate );
      invoiceHistoryNote.setInvoiceId( INVOICE_ID_2 );
      invoiceHistoryNote.setUserId( USER_ID_2 );
      invoiceHistoryNote.setStatus( EVENT_STATUS_CODE_2 );
      invoiceHistoryNote.setStageReasonCode( STAGE_REASON_CODE_2 );
      invoiceHistoryNote.setNote( STAGE_NOTE_2 );
      return invoiceHistoryNote;
   }
}
