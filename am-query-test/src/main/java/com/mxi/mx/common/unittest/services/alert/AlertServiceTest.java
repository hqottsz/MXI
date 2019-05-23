package com.mxi.mx.common.unittest.services.alert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.alert.Alert;
import com.mxi.mx.common.alert.AlertEngineCacheStub;
import com.mxi.mx.common.alert.AlertNotificationType;
import com.mxi.mx.common.alert.AlertType;
import com.mxi.mx.common.alert.MxAlert;
import com.mxi.mx.common.alert.MxAlertEngineCache;
import com.mxi.mx.common.alert.login.LoginAlert;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.key.AlertKey;
import com.mxi.mx.common.services.alert.AlertService;
import com.mxi.mx.common.services.alert.LoginNotificationRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.table.alert.UtlAlertTable;


/**
 * Tests the {@link AlertService} class.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class AlertServiceTest {

   private static final String ALERT_MESSAGE = "ALERT_MESSAGE";

   private static final int ALERT_PRIORITY = 0;

   private static final int ALERT_PRIORITY_1 = 1;

   private static final String ALERT_TITLE = "ALERT_TITLE";

   private static final Integer USER_1 = 1;

   private static final Integer USER_2 = 2;

   private static final Integer USER_3 = 3;

   private static final Integer USER_4 = 4;

   private static final Set<Integer> MULTIPLE_USERS =
         new HashSet<Integer>( Arrays.asList( USER_1, USER_2 ) );

   private static final Set<Integer> ADDITIONAL_USERS =
         new HashSet<Integer>( Arrays.asList( USER_3, USER_4 ) );

   private AlertService iAlertService;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   /**
    * Verify that {@link AlertService#sendAlerts} does not update acknowldeged LOGIN alerts.
    *
    * @throws Exception
    */
   @Test
   public void testSendAlertsDoesNotUpdateAcknowldegedLoginAlerts() throws Exception {

      setupAlertEngineCacheForLoginAlert();

      Set<Integer> lUsers = new HashSet<Integer>( Arrays.asList( USER_1 ) );

      // Setup a LOGIN alert to act as the existing alert.
      final Alert lOriginalAlert =
            new LoginAlert( ALERT_TITLE, ALERT_MESSAGE, ALERT_PRIORITY, lUsers );

      // Create the alert.
      Set<Alert> lAlerts = new HashSet<Alert>( Arrays.asList( lOriginalAlert ) );

      iAlertService.sendAlerts( lAlerts );

      // Verify the priority of the alert.
      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( "utl_alert", new DataSetArgument() );
      lQs.first();
      assertEquals( 1, lQs.getRowCount() );
      assertEquals( ALERT_PRIORITY, lQs.getInt( "priority" ) );

      // Acknowledge the alert.
      AlertKey lAlertKey = new AlertKey( lQs.getInt( "alert_id" ) );
      acknowledgeAlert( lAlertKey );

      // Create an updated alert (with new priority).
      Alert lUpdatedAlert = new MxAlert( null, LoginAlert.PRIMARY_KEY, ALERT_PRIORITY_1, new Date(),
            lOriginalAlert.getParameters() );
      lUpdatedAlert = new LoginAlert( ALERT_TITLE, ALERT_MESSAGE, ALERT_PRIORITY_1, lUsers );
      lAlerts = new HashSet<Alert>( Arrays.asList( lUpdatedAlert ) );

      // Execute the test.
      iAlertService.sendAlerts( lAlerts );

      // Verify the priority of the alert has not changed.
      lQs = QuerySetFactory.getInstance().executeQueryTable( "utl_alert", new DataSetArgument() );
      lQs.first();
      assertEquals( 1, lQs.getRowCount() );
      assertEquals( ALERT_PRIORITY, lQs.getInt( "priority" ) );
   }


   /**
    * Verify that {@link AlertService#sendAlerts} creates an alert for each additional intended user
    * when updating an existing LOGIN alert.
    *
    * @throws Exception
    */
   @Test
   public void testSendAlertsForLoginAlertWithAdditionalUsers() throws Exception {
      setupAlertEngineCacheForLoginAlert();

      // Setup a LOGIN alert to act as the existing alert.
      Alert lAlert = new LoginAlert( ALERT_TITLE, ALERT_MESSAGE, ALERT_PRIORITY, MULTIPLE_USERS );

      // Create the alerts.
      Set<Alert> lAlerts = new HashSet<Alert>( Arrays.asList( lAlert ) );
      iAlertService.sendAlerts( lAlerts );

      // Create a set of new and previous users.
      Set<Integer> lUsers = new HashSet<Integer>( MULTIPLE_USERS );
      lUsers.addAll( ADDITIONAL_USERS );

      lAlert = new LoginAlert( ALERT_TITLE, ALERT_MESSAGE, ALERT_PRIORITY, lUsers );
      lAlerts = new HashSet<Alert>( Arrays.asList( lAlert ) );

      // Execute the test.
      iAlertService.sendAlerts( lAlerts );

      // Verify there is a utl_alert record for each user.
      Set<AlertKey> lAlertIds = verifyUtlAlert( lUsers.size() );

      // Verify there is a utl_user_alert record for each user.
      verifyUtlUserAlert( lAlertIds );

      // Verify there is a utl_alert_status_log record for each user's alert.
      verifyUtlAlertStatusLog( lAlertIds );

      // Verify there is a utl_alert_log record for each user's alert.
      verifyUtlAlertLog( lAlertIds );
   }


   /**
    * Verify that {@link AlertService#sendAlerts} updates un-acknowldeged LOGIN alerts.
    *
    * @throws Exception
    */
   @Test
   public void testSendAlertsUpdatesUnAcknowldegedLoginAlerts() throws Exception {
      setupAlertEngineCacheForLoginAlert();

      Set<Integer> lUsers = new HashSet<Integer>( Arrays.asList( USER_1 ) );

      // Setup a LOGIN alert to act as the existing alert.
      Alert lAlert = new LoginAlert( ALERT_TITLE, ALERT_MESSAGE, ALERT_PRIORITY, lUsers );

      // Create the alerts.
      Set<Alert> lAlerts = new HashSet<Alert>( Arrays.asList( lAlert ) );

      iAlertService.sendAlerts( lAlerts );

      // Verify the priority of the alert.
      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( "utl_alert", new DataSetArgument() );
      lQs.first();
      assertEquals( 1, lQs.getRowCount() );
      assertEquals( ALERT_PRIORITY, lQs.getInt( "priority" ) );

      // Create an updated alert (new priority).
      lAlert = new LoginAlert( ALERT_TITLE, ALERT_MESSAGE, ALERT_PRIORITY_1, lUsers );
      lAlerts = new HashSet<Alert>( Arrays.asList( lAlert ) );

      // Execute the test.
      iAlertService.sendAlerts( lAlerts );

      // Verify the priority of the alert.
      lQs = QuerySetFactory.getInstance().executeQueryTable( "utl_alert", new DataSetArgument() );
      lQs.first();
      assertEquals( 1, lQs.getRowCount() );
      assertEquals( ALERT_PRIORITY_1, lQs.getInt( "priority" ) );
   }


   /**
    * Verify that {@link AlertService#sendAlerts} creates an alert for each intended user when
    * sending a LOGIN alert.
    *
    * @throws Exception
    */
   @Test
   public void testSendAlertsWithLoginAlertForManyUsers() throws Exception {
      setupAlertEngineCacheForLoginAlert();

      // Setup a LOGIN alert to test with.
      Alert lAlert = new LoginAlert( ALERT_TITLE, ALERT_MESSAGE, ALERT_PRIORITY, MULTIPLE_USERS );

      Set<Alert> lAlerts = new HashSet<Alert>( Arrays.asList( lAlert ) );

      // Execute the test.
      iAlertService.sendAlerts( lAlerts );

      // Verify there is a utl_alert record for each user.
      Set<AlertKey> lAlertIds = verifyUtlAlert( MULTIPLE_USERS.size() );

      // Verify there is a utl_user_alert record for each user.
      verifyUtlUserAlert( lAlertIds );

      // Verify there is a utl_alert_status_log record for each user's alert.
      verifyUtlAlertStatusLog( lAlertIds );

      // Verify there is a utl_alert_log record for each user's alert.
      verifyUtlAlertLog( lAlertIds );
   }


   /**
    * {@inheritDoc}
    */
   @Before
   public void setUp() throws Exception {
      iAlertService = new AlertService();
   }


   /**
    * Emulate the acknowledgement of the alert by updating the utl_alert record.
    *
    * @param aAlertKey
    *           alert to acknowledge
    */
   private void acknowledgeAlert( AlertKey aAlertKey ) {
      UtlAlertTable lUtlAlertTable = new UtlAlertTable( aAlertKey );
      lUtlAlertTable.setAlertStatus( "ACK" );
      lUtlAlertTable.update();
   }


   /**
    * Sets up the alert engine cache to handle requests for login alerts.
    */
   private void setupAlertEngineCacheForLoginAlert() {
      AlertType lLoginAlertType =
            new AlertType( LoginAlert.PRIMARY_KEY, "Name", AlertType.CATEGORY_LOGIN, "Description",
                  "Message", false, AlertNotificationType.PRIVATE, null, 0, null, null, true );

      AlertEngineCacheStub lAlertEngineCache = new AlertEngineCacheStub();
      lAlertEngineCache.setAlertType( lLoginAlertType );
      lAlertEngineCache.addAlertNotificationRule(
            "com.mxi.mx.common.services.alert.PrivateNotificationRule",
            new LoginNotificationRule() );

      MxAlertEngineCache.setInstance( lAlertEngineCache );
   }


   /**
    * Verifies the correct utl_alert records were created.
    *
    * @param aNumberOfExpectedAlerts
    *
    * @return set of alert keys
    */
   private Set<AlertKey> verifyUtlAlert( int aNumberOfExpectedAlerts ) {
      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( "utl_alert", new DataSetArgument() );

      assertNotNull( lQs );
      assertEquals( aNumberOfExpectedAlerts, lQs.getRowCount() );

      Set<AlertKey> lAlertIds = new HashSet<AlertKey>( aNumberOfExpectedAlerts );
      while ( lQs.next() ) {
         lAlertIds.add( lQs.getKey( AlertKey.class, "alert_id" ) );
      }

      return lAlertIds;
   }


   /**
    * Verifies the correct utl_alert_log records were created.
    *
    * @param aAlertIds
    *           expected alert ids
    */
   private void verifyUtlAlertLog( Set<AlertKey> aAlertIds ) {

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "utl_alert_log",
            new DataSetArgument() );

      assertNotNull( lQs );
      assertEquals( aAlertIds.size() * 2, lQs.getRowCount() );

      while ( lQs.next() ) {

         assertTrue( aAlertIds.contains( lQs.getKey( AlertKey.class, "alert_id" ) ) );

         String lSystemNote = lQs.getString( "system_note" );
         assertTrue( lSystemNote.contains( i18n.get( "mxcommonejb.msg.ALERT_WAS_CREATED" ) )
               || lSystemNote.contains(
                     i18n.get( "mxcommonejb.msg.ALERT_WAS_ASSIGN", new Object[] { null } ) )
               || lSystemNote
                     .contains( i18n.get( "mxcommonejb.msg.ALERT_WAS_ASSIGN", "N/A N/A" ) ) );
      }
   }


   /**
    * Verifies the correct utl_alert_status_log records were created.
    *
    * @param aAlertIds
    *           expected alert ids
    */
   private void verifyUtlAlertStatusLog( Set<AlertKey> aAlertIds ) {
      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "utl_alert_status_log",
            new DataSetArgument() );

      assertNotNull( lQs );
      assertEquals( aAlertIds.size(), lQs.getRowCount() );

      while ( lQs.next() ) {
         assertTrue( aAlertIds.contains( lQs.getKey( AlertKey.class, "alert_id" ) ) );
         assertTrue( "NEW".equals( lQs.getString( "alert_status_cd" ) ) );
      }
   }


   /**
    * Verifies the correct utl_user_alert records were created.
    *
    * @param aExpectedAlertIds
    *           expected alert ids in utl_user_alert
    */
   private void verifyUtlUserAlert( Set<AlertKey> aExpectedAlertIds ) {

      QuerySet lQs = QuerySetFactory.getInstance().executeQueryTable( "utl_user_alert",
            new DataSetArgument() );

      assertNotNull( lQs );
      assertEquals( aExpectedAlertIds.size(), lQs.getRowCount() );

      Set<Integer> lUserIds = new HashSet<Integer>( aExpectedAlertIds.size() );

      while ( lQs.next() ) {
         AlertKey lAlertId = lQs.getKey( AlertKey.class, "alert_id" );

         assertTrue( aExpectedAlertIds.contains( lAlertId ) );
         lUserIds.add( lQs.getInt( "user_id" ) );
      }

      assertTrue( lUserIds.contains( USER_1 ) );
      assertTrue( lUserIds.contains( USER_2 ) );
   }

}
