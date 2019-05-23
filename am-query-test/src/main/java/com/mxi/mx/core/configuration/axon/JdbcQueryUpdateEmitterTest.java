package com.mxi.mx.core.configuration.axon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.axonframework.common.transaction.NoTransactionManager;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.GenericSubscriptionQueryMessage;
import org.axonframework.queryhandling.SubscriptionQueryBackpressure;
import org.axonframework.queryhandling.UpdateHandlerRegistration;
import org.axonframework.serialization.Serializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mxi.am.db.connection.DatabaseConnectionRule;

import reactor.util.concurrent.Queues;


public class JdbcQueryUpdateEmitterTest {

   private JdbcQueryUpdateEmitter emitter;

   @Rule
   public DatabaseConnectionRule databaseConnectionRule = new DatabaseConnectionRule();

   private Thread thread;

   private CountDownLatch terminatedLatch = new CountDownLatch( 1 );

   private boolean isActive = true;


   @Before
   public void setUp() {
      Serializer serializer = new AxonSerializer();
      TransactionManager transactionManager = NoTransactionManager.INSTANCE;
      QueryUpdateMessageProcessor processor =
            new QueryUpdateMessageProcessor( serializer, transactionManager,
                  Executors.newSingleThreadExecutor(), 100L, Duration.ofMinutes( 10 ) ) {

               @Override
               protected boolean isActive() {
                  return isActive;
               }
            };
      emitter = new JdbcQueryUpdateEmitter( serializer, processor );
      thread = new Thread( () -> {
         processor.run();
         terminatedLatch.countDown();
      } );
      thread.start();
   }


   @After
   public void tearDown() throws InterruptedException {
      isActive = false;
      assertTrue( terminatedLatch.await( 10, TimeUnit.SECONDS ) );
   }


   @Test
   public void registerQueryUpdateHandlers() {
      GenericSubscriptionQueryMessage<TestQuery, String, String> query =
            new GenericSubscriptionQueryMessage<>( new TestQuery( "Testing" ),
                  ResponseTypes.instanceOf( String.class ),
                  ResponseTypes.instanceOf( String.class ) );

      emitter.registerUpdateHandler( query, SubscriptionQueryBackpressure.defaultBackpressure(),
            Queues.SMALL_BUFFER_SIZE );

      assertTrue( emitter.queryUpdateHandlerRegistered( query ) );
   }


   @Test
   public void cancelQueryUpdateHandlers() {
      GenericSubscriptionQueryMessage<TestQuery, String, String> query =
            new GenericSubscriptionQueryMessage<>( new TestQuery( "Testing" ),
                  ResponseTypes.instanceOf( String.class ),
                  ResponseTypes.instanceOf( String.class ) );
      UpdateHandlerRegistration<String> registration = emitter.registerUpdateHandler( query,
            SubscriptionQueryBackpressure.defaultBackpressure(), Queues.SMALL_BUFFER_SIZE );

      registration.getRegistration().cancel();

      assertFalse( emitter.queryUpdateHandlerRegistered( query ) );
   }


   @Test
   public void receiveUpdate() throws InterruptedException {
      GenericSubscriptionQueryMessage<TestQuery, String, String> query =
            new GenericSubscriptionQueryMessage<>( new TestQuery( "Testing" ),
                  ResponseTypes.instanceOf( String.class ),
                  ResponseTypes.instanceOf( String.class ) );
      UpdateHandlerRegistration<String> registration = emitter.registerUpdateHandler( query,
            SubscriptionQueryBackpressure.defaultBackpressure(), Queues.SMALL_BUFFER_SIZE );

      CountDownLatch countDownLatch = new CountDownLatch( 1 );
      registration.getUpdates().subscribe( message -> {
         assertEquals( "Testing", message.getPayload() );
         countDownLatch.countDown();
      }, ( error ) -> {
         // ignore
      }, () -> {
         // ignore
      } );
      emitter.emit( TestQuery.class, ( q ) -> true, "Testing" );

      assertTrue( countDownLatch.await( 1, TimeUnit.SECONDS ) );
   }


   @Test
   public void receiveCompleteExceptionally() throws InterruptedException {
      GenericSubscriptionQueryMessage<TestQuery, String, String> query =
            new GenericSubscriptionQueryMessage<>( new TestQuery( "Testing" ),
                  ResponseTypes.instanceOf( String.class ),
                  ResponseTypes.instanceOf( String.class ) );
      UpdateHandlerRegistration<String> registration = emitter.registerUpdateHandler( query,
            SubscriptionQueryBackpressure.defaultBackpressure(), Queues.SMALL_BUFFER_SIZE );

      CountDownLatch countDownLatch = new CountDownLatch( 1 );
      registration.getUpdates().subscribe( message -> {
         // ignore
      }, ( error ) -> {
         countDownLatch.countDown();
      }, () -> {
         // ignore
      } );
      emitter.completeExceptionally( TestQuery.class, ( q ) -> true,
            new IllegalStateException( "exception" ) );

      assertTrue( countDownLatch.await( 1, TimeUnit.SECONDS ) );
      assertFalse( emitter.queryUpdateHandlerRegistered( query ) );
   }


   @Test
   public void receiveComplete() throws InterruptedException {
      GenericSubscriptionQueryMessage<TestQuery, String, String> query =
            new GenericSubscriptionQueryMessage<>( new TestQuery( "Testing" ),
                  ResponseTypes.instanceOf( String.class ),
                  ResponseTypes.instanceOf( String.class ) );
      UpdateHandlerRegistration<String> registration = emitter.registerUpdateHandler( query,
            SubscriptionQueryBackpressure.defaultBackpressure(), Queues.SMALL_BUFFER_SIZE );

      CountDownLatch countDownLatch = new CountDownLatch( 1 );
      registration.getUpdates().subscribe( message -> {
         // ignore
      }, ( error ) -> {
         // ignore
      }, () -> {
         countDownLatch.countDown();
      } );
      emitter.complete( TestQuery.class, ( q ) -> true );

      assertTrue( countDownLatch.await( 1, TimeUnit.SECONDS ) );
      assertFalse( emitter.queryUpdateHandlerRegistered( query ) );
   }


   @Test
   public void doNotReceiveComplete_IfFilterDoesNotMatch() throws InterruptedException {
      GenericSubscriptionQueryMessage<TestQuery, String, String> query =
            new GenericSubscriptionQueryMessage<>( new TestQuery( "Testing" ),
                  ResponseTypes.instanceOf( String.class ),
                  ResponseTypes.instanceOf( String.class ) );
      UpdateHandlerRegistration<String> registration = emitter.registerUpdateHandler( query,
            SubscriptionQueryBackpressure.defaultBackpressure(), Queues.SMALL_BUFFER_SIZE );

      CountDownLatch countDownLatch = new CountDownLatch( 1 );
      registration.getUpdates().subscribe( message -> {
         // ignore
      }, ( error ) -> {
         // ignore
      }, () -> {
         countDownLatch.countDown();
      } );
      emitter.complete( TestQuery.class, ( q ) -> false );

      assertFalse( countDownLatch.await( 1, TimeUnit.SECONDS ) );
      assertTrue( emitter.queryUpdateHandlerRegistered( query ) );
   }


   @Test
   public void doNotReceiveCompleteExceptionally_IfFilterDoesNotMatch()
         throws InterruptedException {
      GenericSubscriptionQueryMessage<TestQuery, String, String> query =
            new GenericSubscriptionQueryMessage<>( new TestQuery( "Testing" ),
                  ResponseTypes.instanceOf( String.class ),
                  ResponseTypes.instanceOf( String.class ) );
      UpdateHandlerRegistration<String> registration = emitter.registerUpdateHandler( query,
            SubscriptionQueryBackpressure.defaultBackpressure(), Queues.SMALL_BUFFER_SIZE );

      CountDownLatch countDownLatch = new CountDownLatch( 1 );
      registration.getUpdates().subscribe( message -> {
         // ignore
      }, ( error ) -> {
         countDownLatch.countDown();
      }, () -> {
         // ignore
      } );
      emitter.completeExceptionally( TestQuery.class, ( q ) -> false,
            new IllegalStateException( "exception" ) );

      assertFalse( countDownLatch.await( 1, TimeUnit.SECONDS ) );
      assertTrue( emitter.queryUpdateHandlerRegistered( query ) );
   }


   @Test
   public void doNotReceiveUpdates_IfFilterDoesNotMatch() throws InterruptedException {
      GenericSubscriptionQueryMessage<TestQuery, String, String> query =
            new GenericSubscriptionQueryMessage<>( new TestQuery( "Testing" ),
                  ResponseTypes.instanceOf( String.class ),
                  ResponseTypes.instanceOf( String.class ) );
      UpdateHandlerRegistration<String> registration = emitter.registerUpdateHandler( query,
            SubscriptionQueryBackpressure.defaultBackpressure(), Queues.SMALL_BUFFER_SIZE );

      CountDownLatch countDownLatch = new CountDownLatch( 1 );
      registration.getUpdates().subscribe( message -> {
         countDownLatch.countDown();
      }, ( error ) -> {
         // ignore
      }, () -> {
         // ignore
      } );
      emitter.emit( TestQuery.class, ( q ) -> false, "Testing" );

      assertFalse( countDownLatch.await( 1, TimeUnit.SECONDS ) );
      assertTrue( emitter.queryUpdateHandlerRegistered( query ) );
   }


   @Test
   public void cancelSubscription() throws InterruptedException {
      GenericSubscriptionQueryMessage<TestQuery, String, String> query =
            new GenericSubscriptionQueryMessage<>( new TestQuery( "Testing" ),
                  ResponseTypes.instanceOf( String.class ),
                  ResponseTypes.instanceOf( String.class ) );
      UpdateHandlerRegistration<String> registration = emitter.registerUpdateHandler( query,
            SubscriptionQueryBackpressure.defaultBackpressure(), Queues.SMALL_BUFFER_SIZE );

      registration.getUpdates().subscribe( message -> {
         // ignore
      }, ( error ) -> {
         // ignore
      }, () -> {
         // ignore
      } );
      registration.getRegistration().cancel();
      Thread.sleep( 1000L );
      assertFalse( emitter.queryUpdateHandlerRegistered( query ) );
   }


   public static class TestQuery {

      private final String arg;


      public TestQuery(@JsonProperty( "arg" ) String arg) {
         this.arg = arg;
      }


      public String getArg() {
         return arg;
      }


      @Override
      public String toString() {
         return "TestQuery [arg=" + arg + "]";
      }
   }
}
