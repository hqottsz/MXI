package com.mxi.am.api.resource.materials.proc.purchaseorder.historynote;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiPreconditionFailException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.proc.purchaseorder.PurchaseOrderResource;
import com.mxi.am.api.resource.materials.proc.purchaseorder.historynote.impl.PurchaseOrderHistoryNoteResourceBean;
import com.mxi.am.api.resource.materials.proc.purchaseorder.impl.PurchaseOrderResourceBean;
import com.mxi.am.api.resource.sys.refterm.eventstatus.impl.dao.EventStatusDao;
import com.mxi.am.api.resource.sys.refterm.eventstatus.impl.dao.JpaEventStatusDao;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.apiengine.security.Security;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.apiengine.security.CoreSecurity;
import com.mxi.mx.core.table.acevent.InvCndChgInvDao;
import com.mxi.mx.core.table.acevent.JdbcInvCndChgInvDao;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.evt.JdbcEvtEventDao;
import com.mxi.mx.core.table.evt.JdbcEvtStageDao;
import com.mxi.mx.core.table.utl.JdbcUtlConfigParmDao;
import com.mxi.mx.core.table.utl.UtlConfigParmDao;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Database Tests for PurchaseOrderHistoryNote API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class PurchaseOrderHistoryNoteResourceBeanTest extends ResourceBeanTest {

   private static final String PO_HISTORY_NOTE_ALT_ID = "4FEA5D917FCD11E4A0EC71C5C65E2E92";
   private static final String PO_ALT_ID = "ABCD1FF71A5D4C58B873A75D2F23EB99";
   private static final String PO_HISTORY_NOTE_USER_ALT_ID = "A054FC32D41C4DDBBC37D76B3E6412F2";
   private static final String PO_HISTORY_NOTE = "Purchase Order History Note Database Test";
   private static final String PO_HISTORY_NOTE_CREATE =
         "Purchase Order History Note Create API Database Test";
   private static final String PO_HISTORY_NOTE_DATE = "11-jan-2007 00:00:00";
   private static final String PO_HISTORY_NOTE_STATUS = "POOPEN";

   private static final String INVALID_ALT_ID = "4FEA5D917FCD11E4A0EC71C5C65E2E93";
   private static final String EMPTY_STRING = "";

   @Inject
   private PurchaseOrderHistoryNoteResourceBean purchaseOrderHistoryNoteResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( PurchaseOrderHistoryNoteResource.class )
                     .to( PurchaseOrderHistoryNoteResourceBean.class );
               bind( PurchaseOrderResource.class ).to( PurchaseOrderResourceBean.class );
               bind( EventStatusDao.class ).to( JpaEventStatusDao.class );
               bind( EvtEventDao.class ).to( JdbcEvtEventDao.class );
               bind( EvtStageDao.class ).to( JdbcEvtStageDao.class );
               bind( Security.class ).to( CoreSecurity.class );
               bind( EJBContext.class ).toInstance( ejbContext );
               bind( InvCndChgInvDao.class ).to( JdbcInvCndChgInvDao.class );
               bind( UtlConfigParmDao.class ).to( JdbcUtlConfigParmDao.class );
            }
         } );


   @Before
   public void setUp() throws MxException {

      InjectorContainer.get().injectMembers( this );
      purchaseOrderHistoryNoteResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testGetPurchaseOrderHistoryNoteByIdSuccess()
         throws AmApiResourceNotFoundException, ParseException {

      PurchaseOrderHistoryNote actualPurchaseOrderHistoryNote =
            purchaseOrderHistoryNoteResourceBean.get( PO_HISTORY_NOTE_ALT_ID );
      Assert.assertEquals( constructExpectedResults(), actualPurchaseOrderHistoryNote );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetPurchaseOrderHistoryNoteByIdHistoryNoteNotFoundFail()
         throws AmApiResourceNotFoundException {
      purchaseOrderHistoryNoteResourceBean.get( INVALID_ALT_ID );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetPurchaseOrderHistoryNoteByIdAltIdBlankFail()
         throws AmApiResourceNotFoundException {
      purchaseOrderHistoryNoteResourceBean.get( EMPTY_STRING );
   }


   @Test
   public void testSearchPurchaseOrderHistoryNoteSuccess()
         throws AmApiResourceNotFoundException, ParseException {
      PurchaseOrderHistoryNoteSearchParameters purchaseOrderHistoryNoteSearchParams =
            new PurchaseOrderHistoryNoteSearchParameters();
      purchaseOrderHistoryNoteSearchParams.setPurchaseOrderId( PO_ALT_ID );
      List<PurchaseOrderHistoryNote> actualPurchaseOrderHistoryNote =
            purchaseOrderHistoryNoteResourceBean.search( purchaseOrderHistoryNoteSearchParams );
      Assert.assertEquals( "Expected 1 History Note but received "
            + actualPurchaseOrderHistoryNote.size() + " History Note(s).", 1,
            actualPurchaseOrderHistoryNote.size() );
      Assert.assertTrue( "Expected History Note is not there in the received History Note List.",
            actualPurchaseOrderHistoryNote.contains( constructExpectedResults() ) );
   }


   @Test
   public void testSearchPurchaseOrderHistoryNoteNotFoundFail()
         throws AmApiResourceNotFoundException {
      PurchaseOrderHistoryNoteSearchParameters purchaseOrderHistoryNoteSearchParams =
            new PurchaseOrderHistoryNoteSearchParameters();
      purchaseOrderHistoryNoteSearchParams.setPurchaseOrderId( INVALID_ALT_ID );
      List<PurchaseOrderHistoryNote> actualPurchaseOrderHistoryNote =
            purchaseOrderHistoryNoteResourceBean.search( purchaseOrderHistoryNoteSearchParams );
      Assert.assertTrue( "Received History Note List is not empty",
            actualPurchaseOrderHistoryNote.isEmpty() );
   }


   @Test( expected = AmApiPreconditionFailException.class )
   public void testSearchPurchaseOrderHistoryNoteEmptyPurchaseOrderAltIdFail()
         throws AmApiResourceNotFoundException {
      PurchaseOrderHistoryNoteSearchParameters purchaseOrderHistoryNoteSearchParams =
            new PurchaseOrderHistoryNoteSearchParameters();
      purchaseOrderHistoryNoteSearchParams.setPurchaseOrderId( EMPTY_STRING );
      purchaseOrderHistoryNoteResourceBean.search( purchaseOrderHistoryNoteSearchParams );
   }


   @Test
   public void testCreatePurchaseOrderHistoryNoteSuccess() throws ParseException {
      PurchaseOrderHistoryNote purchaseOrderHistoryNote = constructExpectedResults();
      purchaseOrderHistoryNote.setId( null );
      purchaseOrderHistoryNote.setUserId( null );
      purchaseOrderHistoryNote.setHistoryNote( PO_HISTORY_NOTE_CREATE );

      PurchaseOrderHistoryNote createdPurchaseOrderHistoryNote =
            purchaseOrderHistoryNoteResourceBean.post( purchaseOrderHistoryNote );
      purchaseOrderHistoryNote.setId( createdPurchaseOrderHistoryNote.getId() );
      purchaseOrderHistoryNote.setUserId( createdPurchaseOrderHistoryNote.getUserId() );
      purchaseOrderHistoryNote.setDate( createdPurchaseOrderHistoryNote.getDate() );

      Assert.assertEquals( purchaseOrderHistoryNote, createdPurchaseOrderHistoryNote );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreatePurchaseOrderHistoryNoteInvalidPurchaseOrderIdFail()
         throws ParseException {
      PurchaseOrderHistoryNote purchaseOrderHistoryNote = constructExpectedResults();
      purchaseOrderHistoryNote.setId( null );
      purchaseOrderHistoryNote.setUserId( null );
      purchaseOrderHistoryNote.setPurchaseOrderId( INVALID_ALT_ID );
      purchaseOrderHistoryNote.setHistoryNote( PO_HISTORY_NOTE_CREATE );
      purchaseOrderHistoryNote.setStageStatus( PO_HISTORY_NOTE_STATUS );
      purchaseOrderHistoryNoteResourceBean.post( purchaseOrderHistoryNote );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreatePurchaseOrderHistoryNoteBlankHistoryNoteFail() throws ParseException {
      PurchaseOrderHistoryNote purchaseOrderHistoryNote = constructExpectedResults();
      purchaseOrderHistoryNote.setId( null );
      purchaseOrderHistoryNote.setUserId( null );
      purchaseOrderHistoryNote.setHistoryNote( EMPTY_STRING );
      purchaseOrderHistoryNote.setStageStatus( EMPTY_STRING );
      purchaseOrderHistoryNoteResourceBean.post( purchaseOrderHistoryNote );
   }


   private PurchaseOrderHistoryNote constructExpectedResults() throws ParseException {

      PurchaseOrderHistoryNote purchaseOrderHistoryNote = new PurchaseOrderHistoryNote();
      purchaseOrderHistoryNote.setId( PO_HISTORY_NOTE_ALT_ID );
      purchaseOrderHistoryNote.setPurchaseOrderId( PO_ALT_ID );
      purchaseOrderHistoryNote.setUserId( PO_HISTORY_NOTE_USER_ALT_ID );
      purchaseOrderHistoryNote.setHistoryNote( PO_HISTORY_NOTE );

      Date date = new SimpleDateFormat( "dd-MMM-yyyy hh:mm:ss" ).parse( PO_HISTORY_NOTE_DATE );
      purchaseOrderHistoryNote.setDate( date );
      purchaseOrderHistoryNote.setStageStatus( PO_HISTORY_NOTE_STATUS );

      return purchaseOrderHistoryNote;
   }
}
