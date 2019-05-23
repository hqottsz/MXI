package com.mxi.am.api.resource.materials.asset.usage;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJBContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
import com.mxi.am.api.exception.AmApiBusinessException;
import com.mxi.am.api.exception.AmApiResourceNotFoundException;
import com.mxi.am.api.resource.materials.asset.inventory.CurrentUsage;
import com.mxi.am.api.resource.materials.asset.inventory.Inventory;
import com.mxi.am.api.resource.materials.asset.inventory.InventoryModifyParameters;
import com.mxi.am.api.resource.materials.asset.inventory.impl.InventoryResourceBean;
import com.mxi.am.api.resource.materials.asset.usage.impl.UsageResourceBean;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.testing.ResourceBeanTest;


@RunWith( MockitoJUnitRunner.class )
public class UsageResourceBeanTest extends ResourceBeanTest {

   @Rule
   public InjectionOverrideRule injectionOverrideRule = new InjectionOverrideRule();

   @Mock
   EJBContext ejbContext;

   @Mock
   private Principal principal;

   @Inject
   UsageResourceBean usageResourceBean;

   InventoryResourceBean inventoryResourceBean;

   private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
   private static final String RECORD_DATE = "2019-10-17 15:01:00";

   private static final String INV_CLASS_CD = "TRK";
   private static final String INV_PART_NO = "15861-12";
   private static final String INV_LOC_ID = "5B5CD8E992DA429E8FAAB0DF1077E0F9";
   private static final String INV_COND_CD = "RFI";
   private static final String INV_OWNER_ID = "68FD8EDF9131483DBE3BA6ADAB1AE023";
   private static final String INV_FIN_STATUS_CD = "ROTABLE";

   private static final String USG_NAME = "Test Usage";
   private static final String USG_PARM_HR = "HOURS";
   private static final String USG_PARM_CYC = "CYCLES";


   @Before
   public void setUp() throws MxException, AmApiBusinessException {

      inventoryResourceBean = InjectorContainer.get().getInstance( InventoryResourceBean.class );
      inventoryResourceBean.setEJBContext( ejbContext );

      InjectorContainer.get().injectMembers( this );

      setAuthorizedUser( AUTHORIZED );
      setUnauthorizedUser( UNAUTHORIZED );
      initializeTest();

      Mockito.when( ejbContext.getCallerPrincipal() ).thenReturn( principal );
      Mockito.when( principal.getName() ).thenReturn( AUTHORIZED );

   }


   @Test
   @CSIContractTest( Project.UPS )
   public void testCreateUsage()
         throws ParseException, AmApiResourceNotFoundException, AmApiBusinessException {

      Inventory givenInventory = buildDefaultInventory();

      Usage givenUsage = new Usage();
      givenUsage.setInventoryId( givenInventory.getId() );
      givenUsage.setName( USG_NAME );
      givenUsage.setRecordDate( new SimpleDateFormat( DATE_FORMAT ).parse( RECORD_DATE ) );

      UsageData usageData1 = new UsageData();
      usageData1.setTsi( 5.0 );
      usageData1.setTsn( 11.0 );
      usageData1.setTso( 0.0 );
      usageData1.setUsageParameter( USG_PARM_HR );

      UsageData usageData2 = new UsageData();
      usageData2.setTsi( 1.0 );
      usageData2.setTsn( 2.2 );
      usageData2.setTso( 0.0 );
      usageData2.setUsageParameter( USG_PARM_CYC );

      givenUsage.setUsageData( Arrays.asList( usageData1, usageData2 ) );

      List<CurrentUsage> givenInventoryUsage = buildInventoryCurrUsage( givenUsage );

      Response createdUsageResponse =
            usageResourceBean.post( iAuthorizedSecurityContext, givenUsage, null );
      assertStatus( Status.CREATED, createdUsageResponse );

      Usage retrievedUsage = ( Usage ) createdUsageResponse.getEntity();
      givenUsage.setId( retrievedUsage.getId() );

      Inventory newInventory = inventoryResourceBean.get( retrievedUsage.getInventoryId() );

      assertUsageData( givenUsage, retrievedUsage );
      assertInventoryCurrentUsage( givenInventoryUsage, newInventory.getCurrentUsages() );

   }


   private Inventory buildDefaultInventory() throws AmApiBusinessException {

      Inventory inventory = new Inventory();
      inventory.setInventoryClass( INV_CLASS_CD );
      inventory.setPartNumber( INV_PART_NO );
      inventory.setLocationId( INV_LOC_ID );
      inventory.setLocked( false );
      inventory.setConditionCode( INV_COND_CD );
      inventory.setOwnerId( INV_OWNER_ID );
      inventory.setFinanceStatusCode( INV_FIN_STATUS_CD );

      return inventoryResourceBean.create( inventory, new InventoryModifyParameters() );
   }


   private List<CurrentUsage> buildInventoryCurrUsage( Usage usage )
         throws AmApiResourceNotFoundException {

      Inventory inventory = inventoryResourceBean.get( usage.getInventoryId() );

      for ( CurrentUsage inventoryUsage : inventory.getCurrentUsages() ) {
         for ( UsageData usageData : usage.getUsageData() ) {
            if ( inventoryUsage.getDataTypeCode().equals( usageData.getUsageParameter() ) ) {
               inventoryUsage.setTsi( inventoryUsage.getTsi() + usageData.getTsi() );
               inventoryUsage.setTsn( inventoryUsage.getTsn() + usageData.getTsn() );
               inventoryUsage.setTso( inventoryUsage.getTso() + usageData.getTso() );
            }
         }
      }

      return inventory.getCurrentUsages();
   }


   @Override
   protected void assertStatus( Response.Status status, Response response ) {
      String message = null;
      Object entity = response.getEntity();
      if ( entity != null ) {
         message = entity.toString();
      }
      assertEquals( message, status.getStatusCode(), response.getStatus() );
   }


   private void assertUsageData( Usage expectedUsage, Usage actualUsage ) {

      List<UsageData> expectedUsageDatas = expectedUsage.getUsageData();
      List<UsageData> actualUsageDatas = actualUsage.getUsageData();

      int foundUsageDataNum = 0;
      for ( UsageData actualUsageData : actualUsageDatas ) {
         for ( UsageData expectedUsageData : expectedUsageDatas ) {
            if ( actualUsageData.getUsageParameter()
                  .equals( expectedUsageData.getUsageParameter() ) ) {
               assertEquals(
                     "Incorrect TSI found for usage parameter: "
                           + actualUsageData.getUsageParameter() + ".",
                     actualUsageData.getTsi(), expectedUsageData.getTsi() );
               assertEquals(
                     "Incorrect TSN found for usage parameter: "
                           + actualUsageData.getUsageParameter() + ".",
                     actualUsageData.getTsn(), expectedUsageData.getTsn() );
               assertEquals(
                     "Incorrect TSO found for usage parameter: "
                           + actualUsageData.getUsageParameter() + ".",
                     actualUsageData.getTso(), expectedUsageData.getTso() );
               foundUsageDataNum++;
            }
         }
      }

      assertEquals( "Incorrect number of usages found in retrieved usage data.",
            expectedUsageDatas.size(), foundUsageDataNum );
      assertEquals( "Incorrect usage name found in retrieved usage data.", expectedUsage.getName(),
            actualUsage.getName() );
      assertEquals( "Incorrect record date found in retrieved usage data.",
            expectedUsage.getRecordDate(), actualUsage.getRecordDate() );
      assertEquals( "Incorrect inventory ID found in retrieved usage data.",
            expectedUsage.getInventoryId(), actualUsage.getInventoryId() );

   }


   private void assertInventoryCurrentUsage( List<CurrentUsage> expectedCurrUsage,
         List<CurrentUsage> actualCurrUsage ) {
      Comparator<CurrentUsage> currUsageComparator = new Comparator<CurrentUsage>() {

         @Override
         public int compare( CurrentUsage currUsage1, CurrentUsage currUsage2 ) {
            return ( currUsage1.getDataTypeCode() + currUsage1.getDataTypeDesc() )
                  .compareTo( currUsage2.getDataTypeCode() + currUsage2.getDataTypeDesc() );
         }
      };

      Collections.sort( expectedCurrUsage, currUsageComparator );
      Collections.sort( actualCurrUsage, currUsageComparator );

      assertEquals( "Inventory has not recorded new usage data.", expectedCurrUsage,
            actualCurrUsage );

   }
}
