package com.mxi.am.api.resource.maintenance.eng.config.configurationslot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJBContext;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Inject;
import com.mxi.am.api.annotation.CSIContractTest;
import com.mxi.am.api.annotation.CSIContractTest.Project;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.maintenance.eng.config.configurationslot.impl.ConfigurationSlotResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


/**
 * Database Tests for ConfigurationSlot API
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class ConfigurationSlotResourceBeanTest extends ResourceBeanTest {

   private static final String CONFIGSLOT_ID = "2FDDD6BDF12D11E8BE3FC9020000122A";
   private static final String ASSEMBLY_ID = "2FDB6593F12D11E8BE3FC9020000122A";
   private static final String POSITION_ID = "2FE020D7F12D11E8BE3FC9020000122A";
   private static final String CONFIG_SLOT_CD = "ACFT_CD1";
   private static final String ASSEMBLY_CD = "ACFT_CD1";
   private static final String POSITION_NAME = "1";
   private static final int POSITION_CD = 1;

   private static final String INVALID_ID = "5D732E2E0D41470E816E7ABF14C8AF38";
   private static final String INVALID_CONFIG_SLOT_CD = "ACFT_CD2";
   private static final String EMPTY_ID = "";

   private ConfigurationSlot configurationSlot;

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Inject
   private ConfigurationSlotResourceBean configurationSlotResourceBean;

   @Mock
   private Principal principal;

   @Mock
   private EJBContext ejbContext;


   @Before
   public void setUp() throws MxException {

      InjectorContainer.get().injectMembers( this );
      configurationSlotResourceBean.setEJBContext( ejbContext );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );

      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

      buildExpectedConfigSlotResult();
   }


   /**
    *
    * Test correct Config Slot returns for valid configslot_id
    *
    * @throws AmApiResourceNotFoundException
    */
   @Test
   @CSIContractTest( Project.UPS )
   public void testGetConfigSlotByIdSuccess() throws AmApiResourceNotFoundException {
      ConfigurationSlot actualConfigSlot =
            configurationSlotResourceBean.get( CONFIGSLOT_ID, false, false, false, false );
      assertEquals( configurationSlot, actualConfigSlot );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetConfigSlotByIdConfigSlotNotFound() throws AmApiResourceNotFoundException {
      configurationSlotResourceBean.get( INVALID_ID, false, false, false, false );
   }


   @Test( expected = AmApiResourceNotFoundException.class )
   public void testGetConfigSlotByIdEmptyId() throws AmApiResourceNotFoundException {
      configurationSlotResourceBean.get( EMPTY_ID, false, false, false, false );
   }


   @Test
   public void testSearchConfigSlotSuccess() throws AmApiResourceNotFoundException {
      ConfigurationSlotSearchParameters searchParams = new ConfigurationSlotSearchParameters();
      searchParams.setAssemblyCd( ASSEMBLY_CD );
      searchParams.setConfigSlotCd( CONFIG_SLOT_CD );
      searchParams.setIds( Arrays.asList( CONFIGSLOT_ID ) );

      List<ConfigurationSlot> configSlotList = configurationSlotResourceBean.search( searchParams );
      assertEquals( 1, configSlotList.size() );
      assertEquals( configurationSlot, configSlotList.get( 0 ) );
   }


   @Test
   public void testSearchConfigSlotConfigSlotNotFound() throws AmApiResourceNotFoundException {
      ConfigurationSlotSearchParameters searchParams = new ConfigurationSlotSearchParameters();
      searchParams.setConfigSlotCd( INVALID_CONFIG_SLOT_CD );

      List<ConfigurationSlot> configSlotList = configurationSlotResourceBean.search( searchParams );
      assertTrue( configSlotList.isEmpty() );
   }


   /**
    *
    * Builds the expected Configuration Slot
    *
    */
   public void buildExpectedConfigSlotResult() {
      Position position = new Position();
      position.setId( POSITION_ID );
      position.setPositionId( POSITION_CD );
      position.setPositionName( POSITION_NAME );

      configurationSlot = new ConfigurationSlot();
      configurationSlot.setId( CONFIGSLOT_ID );
      configurationSlot.setAssemblyId( ASSEMBLY_ID );
      configurationSlot.setCode( CONFIG_SLOT_CD );
      configurationSlot.setAssemblyCode( ASSEMBLY_CD );
      configurationSlot.setParameters( null );
      configurationSlot.setMandatory( true );
      configurationSlot.setParentId( null );
      configurationSlot.setParentCode( null );
      configurationSlot.setPartGroupIds( null );
      configurationSlot.setPositions( Arrays.asList( position ) );
      configurationSlot.setSubConfigSlots( null );
      configurationSlot.setRequirementDefinitionIds( null );
   }

}
