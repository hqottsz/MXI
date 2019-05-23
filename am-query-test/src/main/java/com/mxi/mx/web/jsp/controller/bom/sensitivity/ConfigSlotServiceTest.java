package com.mxi.mx.web.jsp.controller.bom.sensitivity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.services.bom.sensitivity.model.ConfigSlotSensitivityDetails;
import com.mxi.mx.domain.sensitivity.Sensitivity;
import com.mxi.mx.web.jsp.controller.bom.service.ConfigSlotDetails;
import com.mxi.mx.web.jsp.controller.bom.service.ConfigSlotService;


@RunWith( MockitoJUnitRunner.class )
public class ConfigSlotServiceTest {

   private static final Sensitivity SENSITIVITY =
         Sensitivity.builder().name( "" ).order( 1 ).warning( "" ).build();

   private static final List<ConfigSlotSensitivityDetails> EMPTY_SENSITIVITY_LIST =
         new ArrayList<ConfigSlotSensitivityDetails>();
   private static final List<Sensitivity> NO_SENSITIVITIES = Collections.emptyList();
   private static final List<Sensitivity> SENSITIVITIES = Arrays.asList( SENSITIVITY );
   private final ConfigSlotDetails SYSTEM_CONFIG_SLOT_DETAILS =
         ConfigSlotDetails.builder().withBomClassKey( RefBOMClassKey.SYS ).build();
   private final ConfigSlotDetails ROOT_CONFIG_SLOT_DETAILS =
         ConfigSlotDetails.builder().withBomClassKey( RefBOMClassKey.ROOT ).build();

   private ConfigSlotService iService;
   private ConfigSlotDetails iMockConfigSlotDetails;
   private List<ConfigSlotSensitivityDetails> iMockSensitivities;

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      iService = new ConfigSlotService();
      iMockConfigSlotDetails = mock( ConfigSlotDetails.class );

      iMockSensitivities = new ArrayList<ConfigSlotSensitivityDetails>();
      iMockSensitivities.add( mock( ConfigSlotSensitivityDetails.class ) );

      when( iMockConfigSlotDetails.getBomClassKey() ).thenReturn( RefBOMClassKey.SYS );
      when( iMockConfigSlotDetails.getSensitivities() ).thenReturn( iMockSensitivities );
   }


   @Test
   public void hasSystemSensitivities_trackedSlot() {
      when( iMockConfigSlotDetails.getBomClassKey() ).thenReturn( RefBOMClassKey.TRK );
      assertFalse( iService.hasSystemSensitivities( iMockConfigSlotDetails ) );
   }


   @Test
   public void hasSystemSensitivities_null() {
      assertFalse( iService.hasSystemSensitivities( null ) );
   }


   @Test
   public void hasSystemSensitivities_noSensitivities() {
      when( iMockConfigSlotDetails.getSensitivities() ).thenReturn( EMPTY_SENSITIVITY_LIST );
      assertFalse( iService.hasSystemSensitivities( iMockConfigSlotDetails ) );
   }


   @Test
   public void hasSystemSensitivities_nonTracked_withSensitivities() {
      assertTrue( iService.hasSystemSensitivities( iMockConfigSlotDetails ) );
   }


   @Test
   public void showPropagateSensitivities_createMode() {
      assertFalse( iService.showPropagateSensitivities( true, iMockConfigSlotDetails ) );
   }


   @Test
   public void showPropagateSensitivities_noSensitivities() {
      when( iMockConfigSlotDetails.getSensitivities() ).thenReturn( EMPTY_SENSITIVITY_LIST );
      assertFalse( iService.showPropagateSensitivities( false, iMockConfigSlotDetails ) );
   }


   @Test
   public void showPropagateSensitivities_null() {
      assertFalse( iService.showPropagateSensitivities( false, null ) );
   }


   @Test
   public void showPropagateSensitivities_trackedSlot() {
      when( iMockConfigSlotDetails.getBomClassKey() ).thenReturn( RefBOMClassKey.TRK );
      assertFalse( iService.showPropagateSensitivities( false, iMockConfigSlotDetails ) );
   }


   @Test
   public void showPropagateSensitivities_nonTracked_withSensitivities() {
      assertTrue( iService.showPropagateSensitivities( false, iMockConfigSlotDetails ) );
   }


   @Test
   public void drawSystemSensitivitiesPane_editSystemWithSensitivities() {
      assertTrue(
            iService.drawSystemSensitivitiesPane( SYSTEM_CONFIG_SLOT_DETAILS, SENSITIVITIES ) );
   }


   @Test
   public void drawSystemSensitivitiesPane_editSystemWithNoSensitivities() {
      assertFalse(
            iService.drawSystemSensitivitiesPane( SYSTEM_CONFIG_SLOT_DETAILS, NO_SENSITIVITIES ) );
   }


   /**
    * As currently implemented, this does NOT reflect a real situation. Sensitivities can only exist
    * on configuration slots that correspond to systems.
    */
   @Test
   public void drawSystemSensitivitiesPane_editNonSystemWithSensitivities() {
      assertFalse(
            iService.drawSystemSensitivitiesPane( ROOT_CONFIG_SLOT_DETAILS, SENSITIVITIES ) );
   }


   @Test
   public void drawSystemSensitivitiesPane_editNonSystemWithNoSensitivities() {
      assertFalse(
            iService.drawSystemSensitivitiesPane( ROOT_CONFIG_SLOT_DETAILS, NO_SENSITIVITIES ) );
   }


   @Test
   public void drawSystemSensitivitiesPane_createSystemWithSensitivities() {
      assertTrue( iService.drawSystemSensitivitiesPane( null, SENSITIVITIES ) );
   }


   @Test
   public void drawSystemSensitivitiesPane_createSystemWithNoSensitivities() {
      assertFalse( iService.drawSystemSensitivitiesPane( null, NO_SENSITIVITIES ) );
   }


   @Test
   public void drawSystemSensitivitiesPane_createNonSystemWithSensitivities() {
      assertTrue( iService.drawSystemSensitivitiesPane( null, SENSITIVITIES ) );
   }


   @Test
   public void drawSystemSensitivitiesPane_createNonSystemWithNoSensitivities() {
      assertFalse( iService.drawSystemSensitivitiesPane( null, NO_SENSITIVITIES ) );
   }

}
