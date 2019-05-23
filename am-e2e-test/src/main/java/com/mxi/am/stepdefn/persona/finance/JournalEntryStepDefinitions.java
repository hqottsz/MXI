package com.mxi.am.stepdefn.persona.finance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;

import com.mxi.am.driver.integrationtesting.finance.JournalEntryMessageDriver;
import com.mxi.am.driver.web.NavigationDriver;
import com.mxi.am.driver.web.inventory.AdjustAvgUnitPricePageDriver;
import com.mxi.am.driver.web.inventory.AdjustQuantityPageDriver;
import com.mxi.am.driver.web.inventory.AdjustTotalSparesPageDriver;
import com.mxi.am.driver.web.inventory.ArchiveInventoryPageDriver;
import com.mxi.am.driver.web.inventory.ChangeOwnerPageDriver;
import com.mxi.am.driver.web.inventory.CreateInventoryPageDriver;
import com.mxi.am.driver.web.inventory.ScrapInventoryPageDriver;
import com.mxi.am.driver.web.inventory.UnarchiveInventoryPageDriver;
import com.mxi.am.driver.web.inventory.editinventorypage.EditInventoryPageDriver;
import com.mxi.am.driver.web.inventory.inventorydetailspage.InventoryDetailsPageDriver;
import com.mxi.am.driver.web.part.partdetailspage.PartDetailsPageDriver;
import com.mxi.am.driver.web.part.partsearchpage.PartSearchPageDriver;
import com.mxi.xml.xsd.core.finance.sendDetailedInventoryFinancialLog.x30.SendDetailedInventoryFinancialLogDocument;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


/**
 * Journal Entry Step Definition
 *
 * @author jalarie
 *
 */
public class JournalEntryStepDefinitions {

   @Inject
   private NavigationDriver iNavigation;

   @Inject
   private JournalEntryMessageDriver iJournalEntryMessageDriver;

   @Inject
   private PartSearchPageDriver iPartSearch;

   @Inject
   private CreateInventoryPageDriver iCreateInventory;

   @Inject
   private PartDetailsPageDriver iPartsDetails;

   @Inject
   private EditInventoryPageDriver iEditIventory;

   @Inject
   private InventoryDetailsPageDriver iInventoryDetails;

   @Inject
   private ArchiveInventoryPageDriver iArchiveInventory;

   @Inject
   private UnarchiveInventoryPageDriver iUnarchiveInventory;

   @Inject
   private ScrapInventoryPageDriver iScrapInventory;

   @Inject
   private ChangeOwnerPageDriver iChangeOwner;

   @Inject
   private AdjustAvgUnitPricePageDriver iAdjustUnitPrice;

   @Inject
   private AdjustQuantityPageDriver iAdjustQuantity;

   @Inject
   private AdjustTotalSparesPageDriver iAdjustTotalSpares;


   @Given( "^I navigate to the part search page$" )
   public void navigateToPartSearch( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      iNavigation.navigate( aTableRow.get( "Parent_Menu_Item" ),
            aTableRow.get( "Child_Menu_Item" ) );
   }


   @Given( "^I select a part$" )
   public void selectSerializedPart( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      iPartSearch.clickClearAll();
      iPartSearch.setOemPartNo( aTableRow.get( "OEM_Part_No" ) );
      iPartSearch.clickSearch();
      iPartSearch.clickTabPartsFound().getResults().get( 0 )
            .clickOemPartNo( aTableRow.get( "OEM_Part_No" ) );
   }


   @When( "^I create inventory$" )
   public void createInventory( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );

      iPartsDetails.clickCreateInventory();

      iCreateInventory.generateSerialNo();
      iCreateInventory.setLocation( aTableRow.get( "Location" ) );
      iCreateInventory.setManufacturedDate( aTableRow.get( "Manufactured_Date" ) );
      iCreateInventory.clickOk();

      iEditIventory.clickFinish();
   }


   @Then( "^CRTINV financial log message is generated including part details$" )
   public void crtinvFinancialLogMessageIsGeneratedIncludingPartDetails(
         List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      // Get type for later comparison to out bound message
      String lType = aTableRow.get( "Type" );

      // Create variable to hold value of action field from out bound message
      String lMessageType = "";

      while ( lType.equals( lMessageType ) ) {
         SendDetailedInventoryFinancialLogDocument lSendDetailedInvFncLogDoc =
               iJournalEntryMessageDriver.receive();
         // Fail if the queue is empty
         if ( lSendDetailedInvFncLogDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            lType = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getType();
            Assert.assertEquals( aTableRow.get( "Type" ), lType );

            String lDate = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Current Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            String lPartNumberOem = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getPartNumberOem();
            Assert.assertEquals( aTableRow.get( "Part_Number_OEM" ), lPartNumberOem );
            String lPartNumberDescription =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getPartNumberDescription();
            Assert.assertEquals( aTableRow.get( "Part_Number_Description" ),
                  lPartNumberDescription );
            String lManufacturerCode =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getManufacturerCode();
            Assert.assertEquals( aTableRow.get( "Manufacturer_Code" ), lManufacturerCode );

            String lCreditCode = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getTransactionAccountArray( 0 ).getAccount().getCode();
            Assert.assertEquals( aTableRow.get( "Code" ), lCreditCode );
         }
      }
   }


   @When( "^I archive inventory$" )
   public void archieveInventory( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      iInventoryDetails.clickArchiveInventory();
      iArchiveInventory.setChargeAccount( aTableRow.get( "Charge_To_Account" ) );
      iArchiveInventory.ok();
      iArchiveInventory.setPassword( aTableRow.get( "Password" ) );
      iArchiveInventory.authenticate();

   }


   @Then( "^ARCHIVE financial log message is generated including part details$" )
   public void archiveFinancialLogMessageIsGeneratedIncludingPartDetails(
         List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      // Get type for later comparison to out bound message
      String lType = aTableRow.get( "Type" );

      // Create variable to hold value of action field from out bound message
      String lMessageType = "";

      while ( lType.equals( lMessageType ) ) {
         SendDetailedInventoryFinancialLogDocument lSendDetailedInvFncLogDoc =
               iJournalEntryMessageDriver.receive();
         // Fail if the queue is empty
         if ( lSendDetailedInvFncLogDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {

            lType = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getType();
            Assert.assertEquals( aTableRow.get( "Type" ), lType );

            String lDate = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Current Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            String lPartNumberOem = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getPartNumberOem();
            Assert.assertEquals( aTableRow.get( "Part_Number_OEM" ), lPartNumberOem );
            String lPartNumberDescription =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getPartNumberDescription();
            Assert.assertEquals( aTableRow.get( "Part_Number_Description" ),
                  lPartNumberDescription );
            String lManufacturerCode =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getManufacturerCode();
            Assert.assertEquals( aTableRow.get( "Manufacturer_Code" ), lManufacturerCode );

            String lCreditCode = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getTransactionAccountArray( 0 ).getAccount().getCode();
            Assert.assertEquals( aTableRow.get( "Code" ), lCreditCode );
         }
      }
   }


   @When( "^I unarchive inventory$" )
   public void unarchieveInventory( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      iInventoryDetails.clickUnarchiveInventory();
      iUnarchiveInventory.setCreditAccount( aTableRow.get( "Credit_To_Account" ) );
      iUnarchiveInventory.setLocation( aTableRow.get( "Location" ) );
      iUnarchiveInventory.setCondition( aTableRow.get( "Condition" ) );
      iUnarchiveInventory.ok();

   }


   @Then( "^UNARCH financial log message is generated including part details$" )
   public void unarchFinancialLogMessageIsGeneratedIncludingPartDetails(
         List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      // Get type for later comparison to out bound message
      String lType = aTableRow.get( "Type" );

      // Create variable to hold value of action field from out bound message
      String lMessageType = "";

      while ( lType.equals( lMessageType ) ) {
         SendDetailedInventoryFinancialLogDocument lSendDetailedInvFncLogDoc =
               iJournalEntryMessageDriver.receive();
         // Fail if the queue is empty
         if ( lSendDetailedInvFncLogDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            lType = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getType();
            Assert.assertEquals( aTableRow.get( "Type" ), lType );

            String lDate = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Current Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            String lPartNumberOem = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getPartNumberOem();
            Assert.assertEquals( aTableRow.get( "Part_Number_OEM" ), lPartNumberOem );
            String lPartNumberDescription =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getPartNumberDescription();
            Assert.assertEquals( aTableRow.get( "Part_Number_Description" ),
                  lPartNumberDescription );
            String lManufacturerCode =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getManufacturerCode();
            Assert.assertEquals( aTableRow.get( "Manufacturer_Code" ), lManufacturerCode );

            String lCreditCode = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getTransactionAccountArray( 0 ).getAccount().getCode();
            Assert.assertEquals( aTableRow.get( "Code" ), lCreditCode );
         }
      }
   }


   @When( "^I change owner$" )
   public void changeOwner( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      iInventoryDetails.clickChangeOwner();
      iChangeOwner.setNewOwner( aTableRow.get( "New_Owner" ) );
      iChangeOwner.setChargeAccount( aTableRow.get( "Charge_To_Account" ) );
      iChangeOwner.ok();

   }


   @Then( "^CHGOWN financial log message is generated including part details$" )
   public void chgownFinancialLogMessageIsGeneratedIncludingPartDetails(
         List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      // Get type for later comparison to out bound message
      String lType = aTableRow.get( "Type" );

      // Create variable to hold value of action field from out bound message
      String lMessageType = "";

      while ( lType.equals( lMessageType ) ) {
         SendDetailedInventoryFinancialLogDocument lSendDetailedInvFncLogDoc =
               iJournalEntryMessageDriver.receive();
         // Fail if the queue is empty
         if ( lSendDetailedInvFncLogDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            lType = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getType();
            Assert.assertEquals( aTableRow.get( "Type" ), lType );

            String lDate = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Current Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            String lPartNumberOem = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getPartNumberOem();
            Assert.assertEquals( aTableRow.get( "Part_Number_OEM" ), lPartNumberOem );
            String lPartNumberDescription =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getPartNumberDescription();
            Assert.assertEquals( aTableRow.get( "Part_Number_Description" ),
                  lPartNumberDescription );
            String lManufacturerCode =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getManufacturerCode();
            Assert.assertEquals( aTableRow.get( "Manufacturer_Code" ), lManufacturerCode );

            String lCreditCode = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getTransactionAccountArray( 0 ).getAccount().getCode();
            Assert.assertEquals( aTableRow.get( "Code" ), lCreditCode );

         }
      }
   }


   @When( "I scrap the inventory$" )
   public void scrapInventory( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );

      iInventoryDetails.clickScrapInventory();
      iScrapInventory.okButton();
      iScrapInventory.setPassword( aTableRow.get( "Password" ) );
      iScrapInventory.authenticate();
   }


   @Then( "^SCRAP financial log message is generated including part details$" )
   public void scrapFinancialLogMessageIsGeneratedIncludingPartDetails(
         List<Map<String, String>> aDataTable ) throws Throwable {

      Map<String, String> aTableRow = aDataTable.get( 0 );

      // Get type for later comparison to out bound message
      String lType = aTableRow.get( "Type" );

      // Create variable to hold value of action field from out bound message
      String lMessageType = "";

      while ( lType.equals( lMessageType ) ) {

         SendDetailedInventoryFinancialLogDocument lSendDetailedInvFncLogDoc =
               iJournalEntryMessageDriver.receive();
         // Fail if the queue is empty
         if ( lSendDetailedInvFncLogDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            lMessageType = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getType();
            Assert.assertEquals( lType, lMessageType );

            String lDate = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Current Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            String lPartNumberOem = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getPartNumberOem();
            Assert.assertEquals( aTableRow.get( "Part_Number_OEM" ), lPartNumberOem );
            String lPartNumberDescription =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getPartNumberDescription();
            Assert.assertEquals( aTableRow.get( "Part_Number_Description" ),
                  lPartNumberDescription );
            String lManufacturerCode =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getManufacturerCode();
            Assert.assertEquals( aTableRow.get( "Manufacturer_Code" ), lManufacturerCode );

            String lCreditCode = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getTransactionAccountArray( 0 ).getAccount().getCode();
            Assert.assertEquals( aTableRow.get( "Code" ), lCreditCode );
         }
      }
   }


   @When( "I adjust unit price$" )
   public void adjustUnitPrice( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      iPartsDetails.clickAdjustUnitPrice();
      iAdjustUnitPrice.setNewPrice( aTableRow.get( "New_Price" ) );
      iAdjustUnitPrice.setChargeAccount( aTableRow.get( "Charge_To_Account" ) );
      iAdjustUnitPrice.ok();
   }


   @Then( "^ADJPRICE financial log message is generated including part details$" )
   public void adjpriceFinancialLogMessageIsGeneratedIncludingPartDetails(
         List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      // Get type for later comparison to out bound message
      String lType = aTableRow.get( "Type" );

      // Create variable to hold value of action field from out bound message
      String lMessageType = "";

      while ( lType.equals( lMessageType ) ) {
         SendDetailedInventoryFinancialLogDocument lSendDetailedInvFncLogDoc =
               iJournalEntryMessageDriver.receive();
         // Fail if the queue is empty
         if ( lSendDetailedInvFncLogDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            lMessageType = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getType();
            Assert.assertEquals( lType, lMessageType );

            String lDate = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Current Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            String lPartNumberOem = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getPartNumberOem();
            Assert.assertEquals( aTableRow.get( "Part_Number_OEM" ), lPartNumberOem );
            String lPartNumberDescription =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getPartNumberDescription();
            Assert.assertEquals( aTableRow.get( "Part_Number_Description" ),
                  lPartNumberDescription );
            String lManufacturerCode =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getManufacturerCode();
            Assert.assertEquals( aTableRow.get( "Manufacturer_Code" ), lManufacturerCode );

            String lCreditCode = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getTransactionAccountArray( 0 ).getAccount().getCode();
            Assert.assertEquals( aTableRow.get( "Code" ), lCreditCode );
         }
      }
   }


   @Given( "^I create inventory for a batch part$" )
   public void createInventoryForABatchPart( List<Map<String, String>> aDataTable )
         throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );

      iPartsDetails.clickCreateInventory();

      iCreateInventory.setLocation( aTableRow.get( "Location" ) );
      iCreateInventory.setManufacturedDate( aTableRow.get( "Manufactured_Date" ) );
      iCreateInventory.clickOk();

      iEditIventory.clickFinish();
      iJournalEntryMessageDriver.clear();
   }


   @When( "I adjust quantity$" )
   public void adjustQuantity( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );

      iInventoryDetails.clickAdjustQuantity();
      iAdjustQuantity.setActualQuantity( aTableRow.get( "Quantity" ) );
      iAdjustQuantity
            .setChargeAdjustmentToAccount( aTableRow.get( "Charge_Adjustment_to_Account" ) );
      iAdjustQuantity.ok();
   }


   @Then( "^QTYADJ financial log message is generated including part details$" )
   public void qtyadjFinancialLogMessageIsGeneratedIncludingPartDetails(
         List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      // Get type for later comparison to out bound message
      String lType = aTableRow.get( "Type" );

      // Create variable to hold value of action field from out bound message
      String lMessageType = "";

      while ( lType.equals( lMessageType ) ) {
         SendDetailedInventoryFinancialLogDocument lSendDetailedInvFncLogDoc =
               iJournalEntryMessageDriver.receive();
         // Fail if the queue is empty
         if ( lSendDetailedInvFncLogDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            lMessageType = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getType();
            Assert.assertEquals( lType, lMessageType );

            String lDate = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Current Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            String lPartNumberOem = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getPartNumberOem();
            Assert.assertEquals( aTableRow.get( "Part_Number_OEM" ), lPartNumberOem );
            String lPartNumberDescription =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getPartNumberDescription();
            Assert.assertEquals( aTableRow.get( "Part_Number_Description" ),
                  lPartNumberDescription );
            String lManufacturerCode =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getManufacturerCode();
            Assert.assertEquals( aTableRow.get( "Manufacturer_Code" ), lManufacturerCode );

            String lCreditCode = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getTransactionAccountArray( 0 ).getAccount().getCode();
            Assert.assertEquals( aTableRow.get( "Code" ), lCreditCode );
         }
      }
   }


   @When( "^I adjust total spares$" )
   public void iAdjustTotalSpares( List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );

      iPartsDetails.clickAdjustTotalSpares();
      iAdjustTotalSpares.setNewValue( aTableRow.get( "Total_Spares" ) );

      if ( aTableRow.containsKey( "Note" ) ) {
         iAdjustTotalSpares.setNote( aTableRow.get( "Note" ) );
      }
      iAdjustQuantity.ok();
   }


   @Then( "^SPARESQTYADJ financial log message is generated including part details$" )
   public void sparesqtyadjFinancialLogMessageIsGeneratedIncludingPartDetails(
         List<Map<String, String>> aDataTable ) throws Throwable {
      Map<String, String> aTableRow = aDataTable.get( 0 );
      // Get type for later comparison to out bound message
      String lType = aTableRow.get( "Type" );

      // Create variable to hold value of action field from out bound message
      String lMessageType = "";

      while ( lType.equals( lMessageType ) ) {
         SendDetailedInventoryFinancialLogDocument lSendDetailedInvFncLogDoc =
               iJournalEntryMessageDriver.receive();
         // Fail if the queue is empty
         if ( lSendDetailedInvFncLogDoc == null ) {
            Assert.fail( "The ASB Notification Queue is empty" );
         } else {
            lMessageType = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getType();
            Assert.assertEquals( lType, lMessageType );

            String lDate = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getDate();

            String lpattern = ( "yyyy-MM-dd HH:mm:ss" );

            SimpleDateFormat lDateFormat = new SimpleDateFormat( lpattern );
            lDateFormat.setLenient( false );

            try {
               lDateFormat.parse( lDate.trim() );
            } catch ( ParseException pe ) {
               Assert.fail(
                     "The Current Date field is not of the correct format yyyy-MM-dd HH:mm:ss" );
            }

            String lPartNumberOem = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getPartNumberOem();
            Assert.assertEquals( aTableRow.get( "Part_Number_OEM" ), lPartNumberOem );
            String lPartNumberDescription =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getPartNumberDescription();
            Assert.assertEquals( aTableRow.get( "Part_Number_Description" ),
                  lPartNumberDescription );
            String lManufacturerCode =
                  lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                        .getTransactionArray( 0 ).getManufacturerCode();
            Assert.assertEquals( aTableRow.get( "Manufacturer_Code" ), lManufacturerCode );

            String lCreditCode = lSendDetailedInvFncLogDoc.getSendDetailedInventoryFinancialLog()
                  .getTransactionArray( 0 ).getTransactionAccountArray( 0 ).getAccount().getCode();
            Assert.assertEquals( aTableRow.get( "Code" ), lCreditCode );
         }
      }
   }
}
