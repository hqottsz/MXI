package com.mxi.am.api.resource.quicktext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.mxi.am.api.resource.quicktext.impl.QuickTextResourceBean;
import com.mxi.am.api.resource.quicktext.model.CreateQuickTextRequest;
import com.mxi.am.api.resource.quicktext.model.DeleteQuickTextRequest;
import com.mxi.am.api.resource.quicktext.model.QuickTextModel;
import com.mxi.am.api.resource.quicktext.model.UpdateQuickTextRequest;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefQuickTextTypeKey;
import com.mxi.mx.core.quicktext.model.QuickText;
import com.mxi.mx.core.quicktext.model.QuickTextType;
import com.mxi.mx.core.quicktext.repository.QuickTextRepository;
import com.mxi.mx.core.quicktext.repository.impl.InMemoryQuickTextRepository;


@RunWith( MockitoJUnitRunner.class )
public class QuickTextResourceTest {

   private QuickTextRepository fakeQuickTextRepository = new InMemoryQuickTextRepository();

   // Object under test
   private QuickTextResourceBean quickTextResource;

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( QuickTextRepository.class ).toInstance( fakeQuickTextRepository );
            }
         } );

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();


   @Before
   public void setUp() throws Throwable {
      quickTextResource = spy( InjectorContainer.get().getInstance( QuickTextResourceBean.class ) );
      setAuthorized( true );
   }


   @Test
   public void create_returns200ForValidRequests() {
      String type = RefQuickTextTypeKey.MX_ENG_STEPDESC.getCd();
      String value = "A valid and unique quick text value.";

      Response response = quickTextResource.create( new CreateQuickTextRequest( type, value ) );

      QuickTextModel entity = ( QuickTextModel ) response.getEntity();

      assertEquals( Status.OK.getStatusCode(), response.getStatus() );
      assertNotNull( entity.getId() );
      assertEquals( type, entity.getType() );
      assertEquals( value, entity.getValue() );
   }


   @Test
   public void create_returns403ForUnauthorizedRequests() {
      setAuthorized( false );
      String type = RefQuickTextTypeKey.MX_ENG_STEPDESC.getCd();
      String value = "A valid and unique quick text value.";

      Response response = quickTextResource.create( new CreateQuickTextRequest( type, value ) );

      assertEquals( Status.FORBIDDEN.getStatusCode(), response.getStatus() );
      assertNull( response.getEntity() );
   }


   @Test
   public void create_returns400WhenValidationFails() {
      String type = RefQuickTextTypeKey.MX_ENG_STEPDESC.getCd();
      String invalidValue = "";

      Response response =
            quickTextResource.create( new CreateQuickTextRequest( type, invalidValue ) );

      assertEquals( Status.BAD_REQUEST.getStatusCode(), response.getStatus() );
   }


   @Test
   public void search_returns400WhenValidationFails() {
      Response response = quickTextResource.search( null );

      assertEquals( Status.BAD_REQUEST.getStatusCode(), response.getStatus() );
   }


   @Test
   public void search_returns403ForUnauthorizedRequests() {
      setAuthorized( false );

      Response response = quickTextResource.search( "MX_ENG_STEPDESC" );

      assertEquals( Status.FORBIDDEN.getStatusCode(), response.getStatus() );
      assertNull( response.getEntity() );
   }


   @Test
   public void search_returns200AndListOfMatchingQuickTexts() {
      // Save 2 QuickTexts of different types
      QuickText quickText1 = QuickText.builder().type( QuickTextType.of( "SEARCHABLE_TYPE" ) )
            .value( "A quick text value." ).build();
      QuickText quickText2 = QuickText.builder().type( QuickTextType.of( "DIFFERENT_TYPE" ) )
            .value( "Another text value." ).build();
      fakeQuickTextRepository.create( quickText1 );
      fakeQuickTextRepository.create( quickText2 );

      Response response = quickTextResource.search( "SEARCHABLE_TYPE" );

      // Ensure the result came back with 1 entry
      List<QuickTextModel> results = ( List<QuickTextModel> ) response.getEntity();
      assertEquals( Status.OK.getStatusCode(), response.getStatus() );
      assertEquals( 1, results.size() );

      // Ensure the type and value are correct
      QuickTextModel result = results.get( 0 );
      assertEquals( quickText1.getType().getCode(), result.getType() );
      assertEquals( quickText1.getValue(), result.getValue() );
   }


   @Test
   public void getTypes_returns403ForUnauthorizedRequests() {
      setAuthorized( false );

      Response getTypesResponse = quickTextResource.getTypes();

      assertEquals( Status.FORBIDDEN.getStatusCode(), getTypesResponse.getStatus() );
   }


   @Test
   public void getTypes_returns200ForAuthorizedRequests() {
      Response getTypesResponse = quickTextResource.getTypes();

      assertEquals( Status.OK.getStatusCode(), getTypesResponse.getStatus() );
   }


   @Test
   public void update_returns403ForUnauthorizedRequests() {
      setAuthorized( false );

      Response updateResponse =
            quickTextResource.update( new UpdateQuickTextRequest( "1:4650", "New value." ) );

      assertEquals( Status.FORBIDDEN.getStatusCode(), updateResponse.getStatus() );
   }


   @Test
   public void update_returns400WhenValidationFails() {
      Response updateResponse =
            quickTextResource.update( new UpdateQuickTextRequest( null, null ) );

      assertEquals( Status.BAD_REQUEST.getStatusCode(), updateResponse.getStatus() );
   }


   @Test
   public void update_returns200ForValidRequests() {
      Response creationResponse = quickTextResource.create( new CreateQuickTextRequest(
            RefQuickTextTypeKey.MX_ENG_STEPDESC.getCd(), "Initial value." ) );
      QuickTextModel quickTextModel = ( QuickTextModel ) creationResponse.getEntity();

      Response updateResponse = quickTextResource
            .update( new UpdateQuickTextRequest( quickTextModel.getId(), "New value." ) );

      assertEquals( Status.OK.getStatusCode(), updateResponse.getStatus() );
   }


   @Test
   public void delete_returns200AfterInvalidDeleteRequest() {
      Response deletedResponse =
            quickTextResource.delete( new DeleteQuickTextRequest( "1234:4650" ) );

      assertEquals( Status.OK.getStatusCode(), deletedResponse.getStatus() );
   }


   @Test
   public void delete_returns200AfterValidDeleteRequest() {
      String type = RefQuickTextTypeKey.MX_ENG_STEPDESC.getCd();
      String value = "A valid and unique quick text value.";

      Response response = quickTextResource.create( new CreateQuickTextRequest( type, value ) );
      QuickTextModel entity = ( QuickTextModel ) response.getEntity();
      Response deletedResponse =
            quickTextResource.delete( new DeleteQuickTextRequest( entity.getId() ) );

      assertEquals( Status.OK.getStatusCode(), deletedResponse.getStatus() );
   }


   @Test
   public void delete_returns400WhenValidationFails() {
      Response deletedResponse = quickTextResource.delete( new DeleteQuickTextRequest( null ) );

      assertEquals( Status.BAD_REQUEST.getStatusCode(), deletedResponse.getStatus() );
   }


   @Test
   public void delete_returns403ForUnauthorizedRequests() {
      setAuthorized( false );

      Response deletedResponse =
            quickTextResource.delete( new DeleteQuickTextRequest( "1234:4650" ) );

      assertEquals( Status.FORBIDDEN.getStatusCode(), deletedResponse.getStatus() );
      assertNull( deletedResponse.getEntity() );
   }


   private void setAuthorized( boolean isAuthorized ) {
      when( quickTextResource
            .validateAuthorization( QuickTextResourceBean.MANAGE_QUICK_TEXT_PERMISSION ) )
                  .thenReturn( isAuthorized );
   }

}
