package com.mxi.mx.web.rest.crew;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.DepartmentKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.org.OrgWorkDept;
import com.mxi.mx.web.rest.Authorizer;


/**
 * Integration unit tests for the behaviours of {@link CrewsEndpoint}
 *
 * The boundaries for these tests are the CrewsEndpoint methods and the DB. Thus exercising the
 * integration of all the code between those boundaries. Note that the authorization validation is
 * mocked and thus excluded from these tests, however, the resulting behaviours regarding the
 * success and failure of that validation are exercised.
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class CrewsEndpointTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   // Constants that are required but not applicable to the tests.
   private static final SecurityContext NA_SESSION_CONTEXT = null;
   private static final String NA_USER_ID = null;

   // Constants used by tests.
   private static final String CREW_CODE_1 = "CREW_CODE_1";
   private static final String CREW_CODE_2 = "CREW_CODE_2";
   private static final String CREW_CODE_3 = "CREW_CODE_3";
   private static final String CREW_CODE_4 = "CREW_CODE_4";

   private static final String CREW_NAME_1 = "CREW_NAME_1";
   private static final String CREW_NAME_2 = "CREW_NAME_2";
   private static final String CREW_NAME_3 = "CREW_NAME_3";
   private static final String CREW_NAME_4 = "CREW_NAME_4";

   @Mock
   private Authorizer mockAuthorizer;


   @Before
   public void before() {
      // In order to control the data in the DB, any rows that exist in the org_work_dept will be
      // deleted.
      // (e.g. any 0-level data)
      MxDataAccess.getInstance().executeDelete( "org_work_dept", null );

      // Most tests will require the authorization to be valid.
      // Those that do not can simply overwrite the mocked behaviour.
      when( mockAuthorizer.validate( any( SecurityContext.class ), any( String.class ) ) )
            .thenReturn( true );
   }


   /**
    * Verify that {@link CrewsEndpoint#getCrews} returns a FORBIDDEN response when the authorization
    * validation fails.
    */
   @Test
   public void getCrewsReturnsResponseWithForbiddenStatusWhenAuthorizationFails() {
      // Given authorization is invalid.
      when( mockAuthorizer.validate( any( SecurityContext.class ), any( String.class ) ) )
            .thenReturn( false );

      // When getCrews is called .
      Response response =
            new CrewsEndpoint( mockAuthorizer ).getCrews( NA_SESSION_CONTEXT, NA_USER_ID );

      // Then the response status is returned as FORBIDDEN.
      assertThat( "", response.getStatusInfo(), is( Status.FORBIDDEN ) );
   }


   /**
    * Verify that {@link CrewsEndpoint#getCrews} returns an OK response with an empty list when no
    * user id is provided and when there are no crews.
    */
   @Test
   public void getCrewsReturnsEmptyListWhenThereAreNoCrews() {
      // Given no crews.
      // (refer to the before method)

      // When getCrews is called with no user id.
      Response response = new CrewsEndpoint( mockAuthorizer ).getCrews( NA_SESSION_CONTEXT, "" );

      // Then a response is returned
      // and the status of the response of OK
      // and the entity of the response contains an empty list.
      assertThat( "Unexpected status in the response.", response.getStatus(),
            is( Status.OK.getStatusCode() ) );
      assertThat( "Unexpected size of list in the response.",
            getListFromResponse( response ).size(), is( 0 ) );
   }


   /**
    * Verify that {@link CrewsEndpoint#getCrews} returns an OK response with a list of all the crews
    * when no user id is provided and when crews exist.
    */
   @Test
   public void getCrewsReturnsAllCrewsWhenCrewsExistAndNoUserProvided() {
      // Given many crews.
      DepartmentKey crew1 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_1 );
         crew.setName( CREW_NAME_1 );
      } );
      UUID crewId1 = OrgWorkDept.findByPrimaryKey( crew1 ).getAltId();
      DepartmentKey crew2 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_2 );
         crew.setName( CREW_NAME_2 );
      } );
      UUID crewId2 = OrgWorkDept.findByPrimaryKey( crew2 ).getAltId();
      DepartmentKey crew3 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_3 );
         crew.setName( CREW_NAME_3 );
      } );
      UUID crewId3 = OrgWorkDept.findByPrimaryKey( crew3 ).getAltId();

      List<CrewsResponse> expectedList = new ArrayList<>( 3 );
      expectedList.add( buildCrewResponse( crewId1, CREW_CODE_1, CREW_NAME_1 ) );
      expectedList.add( buildCrewResponse( crewId2, CREW_CODE_2, CREW_NAME_2 ) );
      expectedList.add( buildCrewResponse( crewId3, CREW_CODE_3, CREW_NAME_3 ) );

      // When getCrews is called with no user id.
      Response response = new CrewsEndpoint( mockAuthorizer ).getCrews( NA_SESSION_CONTEXT, null );

      // Then a response is returned
      // and the status of the response of OK
      // and the entity of the response contains a list of all the crews
      assertThat( "Unexpected status in the response.", response.getStatus(),
            is( Status.OK.getStatusCode() ) );

      List<CrewsResponse> actualList = getListFromResponse( response );
      assertThat( "Unexpected size of list in the response.", actualList.size(),
            is( expectedList.size() ) );
      assertThat( "Unexpected size of list in the response.", actualList,
            Matchers.containsInAnyOrder( expectedList.toArray() ) );
   }


   /**
    * Verify that {@link CrewsEndpoint#getCrews} returns an OK response with a list of all the crews
    * when a blank user id is provided and when crews exist.
    */
   @Test
   public void getCrewsReturnsAllCrewsWhenCrewsExistAndBlankUserProvided() {
      // Given many crews.
      DepartmentKey crew1 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_1 );
         crew.setName( CREW_NAME_1 );
      } );
      UUID crewId1 = OrgWorkDept.findByPrimaryKey( crew1 ).getAltId();
      DepartmentKey crew2 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_2 );
         crew.setName( CREW_NAME_2 );
      } );
      UUID crewId2 = OrgWorkDept.findByPrimaryKey( crew2 ).getAltId();
      DepartmentKey crew3 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_3 );
         crew.setName( CREW_NAME_3 );
      } );
      UUID crewId3 = OrgWorkDept.findByPrimaryKey( crew3 ).getAltId();

      List<CrewsResponse> expectedList = new ArrayList<>( 3 );
      expectedList.add( buildCrewResponse( crewId1, CREW_CODE_1, CREW_NAME_1 ) );
      expectedList.add( buildCrewResponse( crewId2, CREW_CODE_2, CREW_NAME_2 ) );
      expectedList.add( buildCrewResponse( crewId3, CREW_CODE_3, CREW_NAME_3 ) );

      // When getCrews is called with a blank user id.
      Response response = new CrewsEndpoint( mockAuthorizer ).getCrews( NA_SESSION_CONTEXT, "" );

      // Then a response is returned
      // and the status of the response of OK
      // and the entity of the response contains a list of all the crews
      assertThat( "Unexpected status in the response.", response.getStatus(),
            is( Status.OK.getStatusCode() ) );

      List<CrewsResponse> actualList = getListFromResponse( response );
      assertThat( "Unexpected size of list in the response.", actualList.size(),
            is( expectedList.size() ) );
      assertThat( "Unexpected size of list in the response.", actualList,
            Matchers.containsInAnyOrder( expectedList.toArray() ) );
   }


   /**
    * Verify that {@link CrewsEndpoint#getCrews} returns an OK response with a list of crews to
    * which user belongs when the user belongs to all the crews.
    */
   @Test
   public void getCrewsReturnsAllCrewsWhenUserBelongsToAllCrews() {
      // Given a user.
      UserKey userId = Domain.createUser();
      HumanResourceKey user = Domain.createHumanResource( hr -> {
         hr.setUser( userId );
      } );

      // Given many crews to which the user belongs.
      DepartmentKey crew1 = Domain.createCrew( crew -> {
         crew.setUsers( user );
         crew.setCode( CREW_CODE_1 );
         crew.setName( CREW_NAME_1 );
      } );
      UUID crewId1 = OrgWorkDept.findByPrimaryKey( crew1 ).getAltId();
      DepartmentKey crew2 = Domain.createCrew( crew -> {
         crew.setUsers( user );
         crew.setCode( CREW_CODE_2 );
         crew.setName( CREW_NAME_2 );
      } );
      UUID crewId2 = OrgWorkDept.findByPrimaryKey( crew2 ).getAltId();
      DepartmentKey crew3 = Domain.createCrew( crew -> {
         crew.setUsers( user );
         crew.setCode( CREW_CODE_3 );
         crew.setName( CREW_NAME_3 );
      } );
      UUID crewId3 = OrgWorkDept.findByPrimaryKey( crew3 ).getAltId();

      List<CrewsResponse> expectedList = new ArrayList<>( 3 );
      expectedList.add( buildCrewResponse( crewId1, CREW_CODE_1, CREW_NAME_1 ) );
      expectedList.add( buildCrewResponse( crewId2, CREW_CODE_2, CREW_NAME_2 ) );
      expectedList.add( buildCrewResponse( crewId3, CREW_CODE_3, CREW_NAME_3 ) );

      // When getCrews is called with the user id.
      Response response =
            new CrewsEndpoint( mockAuthorizer ).getCrews( NA_SESSION_CONTEXT, userId.toString() );

      // Then a response is returned
      // and the status of the response of OK
      // and the entity of the response contains a list of all the crews
      assertThat( "Unexpected status in the response.", response.getStatus(),
            is( Status.OK.getStatusCode() ) );

      List<CrewsResponse> actualList = getListFromResponse( response );
      assertThat( "Unexpected size of list in the response.", actualList.size(),
            is( expectedList.size() ) );
      assertThat( "Unexpected size of list in the response.", actualList,
            Matchers.containsInAnyOrder( expectedList.toArray() ) );
   }


   /**
    * Verify that {@link CrewsEndpoint#getCrews} returns an OK response with a list of crews to
    * which user belongs when the user belongs to some but not all the crews.
    */
   @Test
   public void getCrewsWhenUserBelongsToSomeCrews() {
      // Given a user.
      UserKey userId = Domain.createUser();
      HumanResourceKey user = Domain.createHumanResource( hr -> {
         hr.setUser( userId );
      } );

      // Given many crews but the user only belongs to some of the crews.
      Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_1 );
         crew.setName( CREW_NAME_1 );
      } );
      DepartmentKey crew2 = Domain.createCrew( crew -> {
         crew.setUsers( user );
         crew.setCode( CREW_CODE_2 );
         crew.setName( CREW_NAME_2 );
      } );
      UUID crewId2 = OrgWorkDept.findByPrimaryKey( crew2 ).getAltId();
      DepartmentKey crew3 = Domain.createCrew( crew -> {
         crew.setUsers( user );
         crew.setCode( CREW_CODE_3 );
         crew.setName( CREW_NAME_3 );
      } );
      UUID crewId3 = OrgWorkDept.findByPrimaryKey( crew3 ).getAltId();
      Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_4 );
         crew.setName( CREW_NAME_4 );
      } );

      List<CrewsResponse> expectedList = new ArrayList<>( 2 );
      expectedList.add( buildCrewResponse( crewId2, CREW_CODE_2, CREW_NAME_2 ) );
      expectedList.add( buildCrewResponse( crewId3, CREW_CODE_3, CREW_NAME_3 ) );

      // When getCrews is called with the user id.
      Response response =
            new CrewsEndpoint( mockAuthorizer ).getCrews( NA_SESSION_CONTEXT, userId.toString() );

      // Then a response is returned
      // and the status of the response of OK
      // and the entity of the response contains a list of only the crews to which the user belongs.
      assertThat( "Unexpected status in the response.", response.getStatus(),
            is( Status.OK.getStatusCode() ) );

      List<CrewsResponse> actualList = getListFromResponse( response );
      assertThat( "Unexpected size of list in the response.", actualList.size(),
            is( expectedList.size() ) );
      assertThat( "Unexpected size of list in the response.", actualList,
            Matchers.containsInAnyOrder( expectedList.toArray() ) );
   }


   /**
    * Verify that {@link CrewsEndpoint#getCrews} returns an OK response with an empty list when the
    * user belongs to no crews.
    */
   @Test
   public void getCrewsWhenUserBelongsToNoCrews() {
      // Given a user.
      UserKey userId = Domain.createUser();

      // Given many crews but the user does not belong to any of them.
      Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_1 );
         crew.setName( CREW_NAME_1 );
      } );
      DepartmentKey crew2 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_2 );
         crew.setName( CREW_NAME_2 );
      } );
      OrgWorkDept.findByPrimaryKey( crew2 ).getAltId();
      DepartmentKey crew3 = Domain.createCrew( crew -> {
         crew.setCode( CREW_CODE_3 );
         crew.setName( CREW_NAME_3 );
      } );
      OrgWorkDept.findByPrimaryKey( crew3 ).getAltId();

      // When getCrews is called with the user id.
      Response response =
            new CrewsEndpoint( mockAuthorizer ).getCrews( NA_SESSION_CONTEXT, userId.toString() );

      // Then a response is returned
      // and the status of the response of OK
      // and the entity of the response contains an empty list.
      assertThat( "Unexpected status in the response.", response.getStatus(),
            is( Status.OK.getStatusCode() ) );

      List<CrewsResponse> actualList = getListFromResponse( response );
      assertThat( "Unexpected size of list in the response.", actualList.size(), is( 0 ) );
   }


   @SuppressWarnings( "unchecked" )
   private List<CrewsResponse> getListFromResponse( Response response ) {
      return ( List<CrewsResponse> ) response.getEntity();
   }


   private CrewsResponse buildCrewResponse( UUID id, String code, String name ) {
      // Note: the label format reflects that in the plsql function tolabel()
      return new CrewsResponse( id.toString(), code, name, code + " (" + name + ")" );
   }

}
