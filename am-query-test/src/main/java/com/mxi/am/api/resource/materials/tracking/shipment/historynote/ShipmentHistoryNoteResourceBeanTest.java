package com.mxi.am.api.resource.materials.tracking.shipment.historynote;

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

import com.google.inject.Inject;
import com.mxi.am.api.exception.AmApiBadRequestException;
import com.mxi.am.api.exception.AmApiMandatoryParameterMissingException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.tracking.shipment.historynote.impl.ShipmentHistoryNoteResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Query test to Shipment History Note API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class ShipmentHistoryNoteResourceBeanTest extends ResourceBeanTest {

   private static final String ID = "4F057BAB7FCD11E4A0EC71C5C65E2E92";
   private static final String NOTE = "Shipment has been created.";
   private static final String DATE = "7/7/2005 4:13:35 PM";
   private static final String SHIPMENT_ID = "226096F8106F43EB98E427B98D96EECD";
   private static final String STATUS_CODE = "IXPEND";
   private static final String USER_ID = "D082C8EEA5094C6996262D45DF279612";

   private static final String INVALID_ID = "4F057BAB7FCD11E4A0EC71C5C65E2E99";
   private static final String EMPTY_STRING = "";
   private static final String INVALID_UUID_FORMART = "4F057BAB7FCD11E4A0EC71C5C65E2E9%%";
   private static final String INVALID_SHIPMENT_ID = "226096F8106F43EB98E427B98D96EECF";
   private static final String INVALID_STATUS_CODE = "ABCDEF";

   private static final String SHIPMENT_ID_TO_CREATE = "2A519CFE4BCA49DA8A5D4D703FF0BC60";
   private static final String NOTE_TO_CREATE = "New shipment has been created.";

   @Inject
   private ShipmentHistoryNoteResourceBean shipmentHistoryNoteResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext EJBContext;

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void setUp() throws MxException, ParseException {

      InjectorContainer.get().injectMembers( this );
      shipmentHistoryNoteResourceBean.setEJBContext( EJBContext );
      initializeDataLoader();

      Mockito.when( EJBContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );
   }


   @Test
   public void testGetShipmentHistoryNoteByIdSuccess()
         throws AmApiResourceNotFoundException, ParseException {

      ShipmentHistoryNote actualShipmentHistoryNote = shipmentHistoryNoteResourceBean.get( ID );
      Assert.assertEquals( "Expected History Note is not received.", constructShipmentHistoryNote(),
            actualShipmentHistoryNote );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetShipmentHistoryNoteByInvalidId() throws AmApiResourceNotFoundException {
      shipmentHistoryNoteResourceBean.get( INVALID_ID );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetShipmentHistoryNoteByBlankId() throws AmApiResourceNotFoundException {
      shipmentHistoryNoteResourceBean.get( EMPTY_STRING );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetShipmentHistoryNoteByInvalidUUIDFormart()
         throws AmApiResourceNotFoundException {
      shipmentHistoryNoteResourceBean.get( INVALID_UUID_FORMART );
   }


   @Test
   public void testSearchShipmentHistoryNoteSuccess()
         throws AmApiResourceNotFoundException, ParseException {
      ShipmentHistoryNoteSearchParameters shipmentHistoryNoteSearchParameters =
            new ShipmentHistoryNoteSearchParameters();
      shipmentHistoryNoteSearchParameters.setShipmentId( SHIPMENT_ID );
      List<ShipmentHistoryNote> actualShipmentHistoryNote =
            shipmentHistoryNoteResourceBean.search( shipmentHistoryNoteSearchParameters );
      Assert.assertEquals( "Expected 1 History Note but received "
            + actualShipmentHistoryNote.size() + " History Note(s).", 1,
            actualShipmentHistoryNote.size() );
      Assert.assertTrue( "Expected History Note is not there in the received History Note List.",
            actualShipmentHistoryNote.contains( constructShipmentHistoryNote() ) );
   }


   @Test
   public void testSearchShipmentHistoryNoteByInvalidShipmentId()
         throws AmApiResourceNotFoundException {
      ShipmentHistoryNoteSearchParameters shipmentHistoryNoteSearchParameters =
            new ShipmentHistoryNoteSearchParameters();
      shipmentHistoryNoteSearchParameters.setShipmentId( INVALID_SHIPMENT_ID );
      List<ShipmentHistoryNote> actualShipmentHistoryNote =
            shipmentHistoryNoteResourceBean.search( shipmentHistoryNoteSearchParameters );
      Assert.assertTrue( "Received History Note List is not empty",
            actualShipmentHistoryNote.isEmpty() );
   }


   @Test( expected = AmApiMandatoryParameterMissingException.class )
   public void testSearchShipmentHistoryNoteByBlankShipmentId()
         throws AmApiMandatoryParameterMissingException {
      ShipmentHistoryNoteSearchParameters shipmentHistoryNoteSearchParameters =
            new ShipmentHistoryNoteSearchParameters();
      shipmentHistoryNoteSearchParameters.setShipmentId( EMPTY_STRING );
      List<ShipmentHistoryNote> actualShipmentHistoryNote =
            shipmentHistoryNoteResourceBean.search( shipmentHistoryNoteSearchParameters );
   }


   @Test
   public void testSearchShipmentHistoryNoteByInvalidShipmentUUIDFormart()
         throws IllegalArgumentException {
      ShipmentHistoryNoteSearchParameters shipmentHistoryNoteSearchParameters =
            new ShipmentHistoryNoteSearchParameters();
      shipmentHistoryNoteSearchParameters.setShipmentId( INVALID_UUID_FORMART );
      List<ShipmentHistoryNote> actualShipmentHistoryNote =
            shipmentHistoryNoteResourceBean.search( shipmentHistoryNoteSearchParameters );
      Assert.assertTrue( "Received History Note List is not empty",
            actualShipmentHistoryNote.isEmpty() );
   }


   @Test
   public void testCreateShipmentHistoryNoteWithStatusCodeSuccess() throws ParseException {
      ShipmentHistoryNote shipmentHistoryNote = constructShipmentHistoryNote();
      shipmentHistoryNote.setNote( NOTE_TO_CREATE );
      shipmentHistoryNote.setShipmentId( SHIPMENT_ID_TO_CREATE );
      shipmentHistoryNote.setStatusCode( STATUS_CODE );
      shipmentHistoryNote.setUserId( null );
      shipmentHistoryNote.setId( null );
      shipmentHistoryNote.setDate( null );

      ShipmentHistoryNote createdShipmentHistoryNote =
            shipmentHistoryNoteResourceBean.create( shipmentHistoryNote );
      shipmentHistoryNote.setId( createdShipmentHistoryNote.getId() );
      shipmentHistoryNote.setUserId( createdShipmentHistoryNote.getUserId() );
      shipmentHistoryNote.setDate( createdShipmentHistoryNote.getDate() );

      Assert.assertEquals( shipmentHistoryNote, createdShipmentHistoryNote );
   }


   @Test
   public void testCreateShipmentHistoryNoteWithNullStatusCodeSuccess() throws ParseException {
      ShipmentHistoryNote shipmentHistoryNote = constructShipmentHistoryNote();
      shipmentHistoryNote.setNote( NOTE_TO_CREATE );
      shipmentHistoryNote.setShipmentId( SHIPMENT_ID_TO_CREATE );
      shipmentHistoryNote.setStatusCode( null );
      shipmentHistoryNote.setUserId( null );
      shipmentHistoryNote.setId( null );
      shipmentHistoryNote.setDate( null );

      ShipmentHistoryNote createdShipmentHistoryNote =
            shipmentHistoryNoteResourceBean.create( shipmentHistoryNote );
      shipmentHistoryNote.setId( createdShipmentHistoryNote.getId() );
      shipmentHistoryNote.setUserId( createdShipmentHistoryNote.getUserId() );
      shipmentHistoryNote.setDate( createdShipmentHistoryNote.getDate() );

      Assert.assertEquals( shipmentHistoryNote, createdShipmentHistoryNote );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreateShipmentHistoryNoteByInvalidShipmentId() throws ParseException {
      ShipmentHistoryNote shipmentHistoryNote = constructShipmentHistoryNote();
      shipmentHistoryNote.setNote( NOTE_TO_CREATE );
      shipmentHistoryNote.setShipmentId( INVALID_SHIPMENT_ID );
      shipmentHistoryNote.setStatusCode( STATUS_CODE );
      shipmentHistoryNote.setUserId( null );
      shipmentHistoryNote.setId( null );
      shipmentHistoryNote.setDate( null );
      shipmentHistoryNoteResourceBean.create( shipmentHistoryNote );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreateShipmentHistoryNoteByEmptyShipmentId() throws ParseException {
      ShipmentHistoryNote shipmentHistoryNote = constructShipmentHistoryNote();
      shipmentHistoryNote.setNote( NOTE_TO_CREATE );
      shipmentHistoryNote.setShipmentId( EMPTY_STRING );
      shipmentHistoryNote.setStatusCode( STATUS_CODE );
      shipmentHistoryNote.setUserId( null );
      shipmentHistoryNote.setId( null );
      shipmentHistoryNote.setDate( null );
      shipmentHistoryNoteResourceBean.create( shipmentHistoryNote );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreateShipmentHistoryNoteByInvalidStatusCode() throws ParseException {
      ShipmentHistoryNote shipmentHistoryNote = constructShipmentHistoryNote();
      shipmentHistoryNote.setNote( NOTE_TO_CREATE );
      shipmentHistoryNote.setShipmentId( SHIPMENT_ID_TO_CREATE );
      shipmentHistoryNote.setStatusCode( INVALID_STATUS_CODE );
      shipmentHistoryNote.setUserId( null );
      shipmentHistoryNote.setId( null );
      shipmentHistoryNote.setDate( null );
      shipmentHistoryNoteResourceBean.create( shipmentHistoryNote );
   }


   @Test( expected = AmApiBadRequestException.class )
   public void testCreateShipmentHistoryNoteByBlankNote() throws ParseException {
      ShipmentHistoryNote shipmentHistoryNote = constructShipmentHistoryNote();
      shipmentHistoryNote.setNote( EMPTY_STRING );
      shipmentHistoryNote.setShipmentId( SHIPMENT_ID_TO_CREATE );
      shipmentHistoryNote.setStatusCode( STATUS_CODE );
      shipmentHistoryNote.setUserId( null );
      shipmentHistoryNote.setId( null );
      shipmentHistoryNote.setDate( null );
      shipmentHistoryNoteResourceBean.create( shipmentHistoryNote );
   }


   private ShipmentHistoryNote constructShipmentHistoryNote() throws ParseException {
      ShipmentHistoryNote shipmentHistoryNote = new ShipmentHistoryNote();
      shipmentHistoryNote.setId( ID );
      shipmentHistoryNote.setNote( NOTE );
      Date shipmentHistoryNoteDate = new SimpleDateFormat( "yyyy/MM/dd" ).parse( DATE );
      shipmentHistoryNote.setDate( shipmentHistoryNoteDate );
      shipmentHistoryNote.setShipmentId( SHIPMENT_ID );
      shipmentHistoryNote.setStatusCode( STATUS_CODE );
      shipmentHistoryNote.setUserId( USER_ID );
      return shipmentHistoryNote;
   }

}
