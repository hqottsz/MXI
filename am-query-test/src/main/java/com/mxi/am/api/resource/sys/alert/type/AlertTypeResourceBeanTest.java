package com.mxi.am.api.resource.sys.alert.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.sys.alert.type.impl.AlertTypeResourceBean;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.db.jpa.JPADatabaseConnectionRuleAndProvider;
import com.mxi.mx.db.jpa.TestEntityManagerProvider;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Integration test for AlertTypeResourceBean
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class AlertTypeResourceBeanTest extends ResourceBeanTest {

   private static final Integer FAKE_ALERT_TYPE_ID = 9999;

   private static final String NAME1 = "core.alert.POSSIBLE_FAULT_AUTHORITY_name";
   private static final String MESSAGE1 = "core.alert.POSSIBLE_FAULT_AUTHORITY_message";
   private static final Integer PRIORITY1 = 0;
   private static final Integer ALERT_TYPE_ID1 = 3000;
   private static final String CATEGORY1 = "FAULT";
   private static final Boolean ACTIVE1 = true;
   private static final String DESCRIPTION1 = "core.alert.POSSIBLE_FAULT_AUTHORITY_description";
   private static final Boolean KEYBOOL1 = true;
   private static final String NOTIFYCLASS1 =
         "com.mxi.mx.core.plugin.alert.inventory.InventoryAuthorityFilterRule";
   private static final String NOTIFYCODE1 = "ROLE";
   private static final String PRIORITYCALCCLASS1 =
         "com.mxi.mx.core.plugin.alert.fault.FaultPriorityCalculator";

   AlertTypeResourceBean alertTypeResourceBean;

   @ClassRule
   public static final JPADatabaseConnectionRuleAndProvider jpaDatabaseConnectionRule =
         new JPADatabaseConnectionRuleAndProvider();

   @Rule
   public InjectionOverrideRule injectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( EntityManager.class ).toProvider( TestEntityManagerProvider.class );
               bind( TestEntityManagerProvider.class ).toInstance( jpaDatabaseConnectionRule );
            }
         } );


   @BeforeClass
   public static void loadData() throws SQLException {
      DataLoaders.load( jpaDatabaseConnectionRule.getConnection(),
            AlertTypeResourceBeanTest.class );
   }


   @Before
   public void setup() throws MxException, ParseException {
      alertTypeResourceBean = InjectorContainer.get().getInstance( AlertTypeResourceBean.class );
   }


   @Ignore( "This test is unique in that it uses a JPA connection for the DataLoader and the component under test. OPER-29477 broke support for this.  Raised OPER-29790 to revisit this as solution for this test." )
   @Test
   public void testGetAlertTypeSuccess() throws Exception {
      AlertType expectedAlertType = constructExpectedResults();
      AlertType actualAlertType = alertTypeResourceBean.get( ALERT_TYPE_ID1 );

      actualAlertType.setRoleCodes( actualAlertType.getRoleCodes().stream()
            .sorted( ( o1, o2 ) -> o1.compareTo( o2 ) ).collect( Collectors.toList() ) );

      assertEquals( expectedAlertType, actualAlertType );
   }


   @Test
   public void testGetAlertTypeNotFound() throws Exception {
      try {
         alertTypeResourceBean.get( FAKE_ALERT_TYPE_ID );
      } catch ( AmApiResourceNotFoundException exception ) {
         assertTrue(
               "Expected Invalid Alert Id:" + FAKE_ALERT_TYPE_ID
                     + " does not exist in the exception Id",
               String.valueOf( FAKE_ALERT_TYPE_ID ).equals( exception.getId() ) );
      }
   }


   private AlertType constructExpectedResults() {
      AlertType alertType = new AlertType();
      alertType.setName( NAME1 );
      alertType.setMessage( MESSAGE1 );
      alertType.setPriority( PRIORITY1 );
      alertType.setId( ALERT_TYPE_ID1 );
      alertType.setCategory( CATEGORY1 );
      alertType.setActive( ACTIVE1 );
      alertType.setDescription( DESCRIPTION1 );
      alertType.setKeyBool( KEYBOOL1 );
      alertType.setNotifyClass( NOTIFYCLASS1 );
      alertType.setNotifyCode( NOTIFYCODE1 );
      alertType.setPriorityCalcClass( PRIORITYCALCCLASS1 );

      return alertType;
   }

}
