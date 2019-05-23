package com.mxi.mx.core.services.user;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.am.ee.OperateAsUserRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.license.exception.LicenseException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.trigger.TriggerException;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.hr.domain.HumanResourceRevokedAllAuthorityEvent;
import com.mxi.mx.core.hr.domain.HumanResourceGrantedAllAuthorityEvent;
import com.mxi.mx.core.hr.domain.HumanResourceAssignedAuthoritiesEvent;
import com.mxi.mx.core.hr.domain.HumanResourceCreatedEvent;
import com.mxi.mx.core.hr.domain.HumanResourceDeletedEvent;
import com.mxi.mx.core.hr.domain.HumanResourceRevokedAuthoritiesEvent;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.table.org.OrgHrTable;


public class UserServiceTest {

   private static final int USERID_TESTUSER = 999;
   private static final String USERNAME_TESTUSER = "testuser";
   private static final String USERNAME_REVOKED = "username-revoke";
   private static final String USERNAME_GRANTED = "username-grant";

   private static final String FIRST_NAME = "first_name";
   private static final String LAST_NAME = "last_name";
   private static final String USERNAME = "username";
   private static final String MIDDLE_NAME = "middle_name";
   private static final String EMAIL_ID = "user@domain.com";
   private static final String HUMAN_RESOURCE_CODE = "hr_cd";
   private static final OrgKey ORGANIZATION_KEY = new OrgKey( 0, 1 );

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public OperateAsUserRule operateAsUserRule =
         new OperateAsUserRule( USERID_TESTUSER, USERNAME_TESTUSER );

   private RecordingEventBus eventBus;

   private AuthorityKey authorityKeyAirCanada;
   private AuthorityKey authorityKeyWestJet;


   @Test
   public void publishHrAllAuthorityRevokedEventWhenAllAuthorityIsRevoked() {

      // ARRANGE
      UserKey userKey = Domain.createUser( user -> {
         user.setUsername( USERNAME_REVOKED );
      } );

      HumanResourceKey humanResourceKey = Domain.createHumanResource( hr -> {
         hr.setUser( userKey );
      } );

      // ACT
      UserService.setAllAuthority( userKey, false );

      // ASSERT
      assertEquals( new HumanResourceRevokedAllAuthorityEvent( USERNAME_REVOKED, humanResourceKey,
            USERNAME_TESTUSER ), eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void publishHumanResourceAllAuthorityGrantedEventWhenAllAuthorityIsGranted() {

      // ARRANGE
      UserKey userKey = Domain.createUser( user -> {
         user.setUsername( USERNAME_GRANTED );
      } );

      HumanResourceKey humanResourceKey = Domain.createHumanResource( hr -> {
         hr.setUser( userKey );
      } );

      // ACT
      UserService.setAllAuthority( userKey, true );

      // ASSERT
      assertEquals( new HumanResourceGrantedAllAuthorityEvent( humanResourceKey, USERNAME_GRANTED,
            USERNAME_TESTUSER ), eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void createUserMustEmitHumanResourceCreatedEvent()
         throws LicenseException, MxException, TriggerException {

      // ARRANGE
      UserDetailsTO userDetails = new UserDetailsTO();
      userDetails.setFirstName( FIRST_NAME );
      userDetails.setLastName( LAST_NAME );
      userDetails.setUsername( USERNAME );
      userDetails.setMiddleName( MIDDLE_NAME );
      userDetails.setEmailAddress( EMAIL_ID );
      userDetails.setHrCode( HUMAN_RESOURCE_CODE );
      userDetails.setOrganizationKey( ORGANIZATION_KEY );

      // ACT
      UserService.create( userDetails );

      HumanResourceKey humanResourceKey = OrgHrTable.getHumanResourceKey( USERNAME );

      // ASSERT
      assertEquals(
            new HumanResourceCreatedEvent( humanResourceKey, HUMAN_RESOURCE_CODE, FIRST_NAME,
                  MIDDLE_NAME, LAST_NAME, USERNAME, EMAIL_ID ),
            eventBus.getEventMessages().get( 0 ).getPayload() );

   }


   @Test
   public void
         whenHumanResourceIsAssignedToOneAuthorityThenHumanResourceAuthoritiesAssignedEventIsPublished()
               throws MxException {
      // ARRANGE
      UserKey user = Domain.createUser();

      HumanResourceKey humanResource = Domain.createHumanResource( hr -> {
         hr.setUser( user );
      } );

      UserService userService = new UserService();
      AuthorityKey[] authorityKeys = { authorityKeyAirCanada };

      // ACT
      userService.addAuthority( user, authorityKeys );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals(
            new HumanResourceAssignedAuthoritiesEvent( humanResource,
                  Arrays.asList( authorityKeys ) ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void
         whenHumanResourceIsAssignedToThreeAuthorityThenHumanResourceAuthoritiesAssignedEventIsPublished()
               throws MxException {
      // ARRANGE
      UserKey user = Domain.createUser();

      HumanResourceKey humanResource = Domain.createHumanResource( hr -> {
         hr.setUser( user );
      } );

      UserService userService = new UserService();
      AuthorityKey[] authorityKeys = { authorityKeyAirCanada, authorityKeyWestJet };

      // ACT
      userService.addAuthority( user, authorityKeys );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals(
            new HumanResourceAssignedAuthoritiesEvent( humanResource,
                  Arrays.asList( authorityKeys ) ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void
         whenAuthorityIsRevokedFromHumanResourceThenHumanResourceRevokedAuthoritiesEventIsPublished()
               throws MxException {

      // ARRANGE
      UserKey user = Domain.createUser();

      HumanResourceKey humanResource = Domain.createHumanResource( hr -> {
         hr.setUser( user );
         hr.addAuthority( authorityKeyAirCanada );
      } );

      AuthorityKey[] authorityKeys = { authorityKeyAirCanada };

      // ACT
      UserService.removeAuthority( user, authorityKeys );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals(
            new HumanResourceRevokedAuthoritiesEvent( humanResource,
                  Arrays.asList( authorityKeys ) ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void whenHumanResourceIsDeletedThenHumanResourceDeletedEventIsPublished()
         throws MxException {
      // ARRANGE
      String userName = "spiderman";
      String firstName = "spider";
      String lastName = "man";
      String email = "spiderman@spider.zzz";

      UserKey userKey = Domain.createUser( user -> {
         user.setEmailId( email );
         user.setFirstName( firstName );
         user.setLastName( lastName );
         user.setUsername( userName );
      } );

      HumanResourceKey humanResource = Domain.createHumanResource( hr -> {
         hr.setUser( userKey );
      } );

      // ACT
      UserService.delete( new UserKey[] { userKey } );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals( new HumanResourceDeletedEvent( humanResource, userName, firstName, lastName,
            null, email, USERNAME_TESTUSER ), eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void whenMultipleHumanResourceAreDeletedThenMultipleHumanResourceDeletedEventArePublished()
         throws MxException {

      // ARRANGE
      List<UserKey> userKeys = new ArrayList<UserKey>( 3 );
      List<HumanResourceKey> humanResourceKeys = new ArrayList<HumanResourceKey>( 3 );

      int index = 0;
      while ( index < 3 ) {
         final int idx = index;
         UserKey userKey = Domain.createUser( user -> {
            user.setEmailId( "email" + idx );
            user.setFirstName( "firstName" + idx );
            user.setLastName( "lastName" + idx );
            user.setUsername( "userName" + idx );
         } );
         humanResourceKeys.add( Domain.createHumanResource( hr -> {
            hr.setUser( userKey );
         } ) );
         userKeys.add( userKey );
         index++;
      } ;

      // ACT
      UserService.delete( userKeys.toArray( new UserKey[3] ) );

      // ASSERT
      assertEquals( 3, eventBus.getEventMessages().size() );
      while ( index < 3 ) {
         assertEquals( new HumanResourceDeletedEvent( humanResourceKeys.get( index ),
               "userName" + index, "firstName" + index, "lastName" + index, null, "email" + index,
               USERNAME_TESTUSER ), eventBus.getEventMessages().get( index ).getPayload() );
         index++;
      }
   }


   @Before
   public void setUp() {
      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();
      Domain.createHumanResource( hr -> {
         hr.setUsername( USERNAME_TESTUSER );
         hr.setUser( new UserKey( USERID_TESTUSER ) );
      } );

      authorityKeyAirCanada = Domain.createAuthority( authority -> {
         authority.setAuthorityCode( "ACC" );
         authority.setAuthorityName( "AIR CANADA" );
      } );

      authorityKeyWestJet = Domain.createAuthority( authority -> {
         authority.setAuthorityCode( "WJ" );
         authority.setAuthorityName( "West Jet" );
      } );
   }
}
