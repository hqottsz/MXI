package com.mxi.mx.core.services.user.authority;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.configuration.axon.RecordingEventBus;
import com.mxi.mx.core.hr.domain.HumanResourceAssignedAuthoritiesEvent;
import com.mxi.mx.core.hr.domain.HumanResourceRevokedAuthoritiesEvent;
import com.mxi.mx.core.key.AuthorityKey;
import com.mxi.mx.core.key.HrAuthorityKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.services.authority.user.UserService;


public class UserServiceTest {

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   private RecordingEventBus eventBus;

   private AuthorityKey authorityKeyAirCanada;
   private HumanResourceKey humanResourceOne;
   private HumanResourceKey humanResourceTwo;
   private HumanResourceKey humanResourceThree;


   @Before
   public void setUp() {
      eventBus = injectionOverrideRule.select( RecordingEventBus.class ).get();

      authorityKeyAirCanada = Domain.createAuthority( authority -> {
         authority.setAuthorityCode( "ACC" );
         authority.setAuthorityName( "AIR CANADA" );
      } );

      humanResourceOne = Domain.createHumanResource( hr -> hr.setUser( Domain.createUser( user -> {
         user.setUsername( "APOLLO" );
         user.setUserId( 9999 );
      } ) ) );

      humanResourceTwo = Domain.createHumanResource( hr -> hr.setUser( Domain.createUser( user -> {
         user.setUsername( "ANTEHNA" );
         user.setUserId( 8888 );
      } ) ) );

      humanResourceThree =
            Domain.createHumanResource( hr -> hr.setUser( Domain.createUser( user -> {
               user.setUsername( "MINOS" );
               user.setUserId( 777 );
            } ) ) );
   }


   @Test
   public void
         whenAuthorityIsAssignedToHumanResourceThenHumanResourceAuthoritiesAssignedEventIsPublished()
               throws MxException {
      // ARRANGE
      UserService userService = new UserService();
      HumanResourceKey[] humanResourceKeys = { humanResourceOne };

      // ACT
      userService.add( authorityKeyAirCanada, humanResourceKeys );

      // ASSERT
      assertEquals( 1, eventBus.getEventMessages().size() );
      assertEquals(
            new HumanResourceAssignedAuthoritiesEvent( humanResourceOne,
                  Arrays.asList( authorityKeyAirCanada ) ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
   }


   @Test
   public void
         whenAuthorityIsAssignedToThreeHumanResourceThenThreeHumanResourceAuthoritiesAssignedEventsArePublished()
               throws MxException {
      // ARRANGE
      UserService userService = new UserService();
      HumanResourceKey[] humanResourceKeys =
            { humanResourceOne, humanResourceTwo, humanResourceThree };

      // ACT
      userService.add( authorityKeyAirCanada, humanResourceKeys );

      // ASSERT
      assertEquals( 3, eventBus.getEventMessages().size() );
      assertEquals(
            new HumanResourceAssignedAuthoritiesEvent( humanResourceOne,
                  Arrays.asList( authorityKeyAirCanada ) ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
      assertEquals(
            new HumanResourceAssignedAuthoritiesEvent( humanResourceTwo,
                  Arrays.asList( authorityKeyAirCanada ) ),
            eventBus.getEventMessages().get( 1 ).getPayload() );
      assertEquals(
            new HumanResourceAssignedAuthoritiesEvent( humanResourceThree,
                  Arrays.asList( authorityKeyAirCanada ) ),
            eventBus.getEventMessages().get( 2 ).getPayload() );
   }


   @Test
   public void
         whenAuthorityIsRevokedFromTwoHumanResourceThenTwoHumanResourceRevokedAuthoritiesEventArePublished()
               throws MxException {
      // ARRANGE
      UserService userService = new UserService();

      HrAuthorityKey[] hrAuthorityKeys = {
            new HrAuthorityKey( humanResourceOne.getDbId(), humanResourceOne.getId(),
                  authorityKeyAirCanada.getDbId(), authorityKeyAirCanada.getId() ),
            new HrAuthorityKey( humanResourceTwo.getDbId(), humanResourceTwo.getId(),
                  authorityKeyAirCanada.getDbId(), authorityKeyAirCanada.getId() ) };

      // ACT
      userService.remove( hrAuthorityKeys );

      // ASSERT
      assertEquals( 2, eventBus.getEventMessages().size() );
      assertEquals(
            new HumanResourceRevokedAuthoritiesEvent( humanResourceOne,
                  Arrays.asList( authorityKeyAirCanada ) ),
            eventBus.getEventMessages().get( 0 ).getPayload() );
      assertEquals(
            new HumanResourceRevokedAuthoritiesEvent( humanResourceTwo,
                  Arrays.asList( authorityKeyAirCanada ) ),
            eventBus.getEventMessages().get( 1 ).getPayload() );
   }
}
