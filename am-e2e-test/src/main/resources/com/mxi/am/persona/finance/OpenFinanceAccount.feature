@CSI @AMAPI
Feature: Open Finance account

  @SmokeTest
  Scenario: An open account message is sent to Maintenix to open a closed finance account using the account code
    Given I am a superuser
    And I navigate to the following page
      | Page_Name     | Parent_Menu_Item   | Child_Menu_Item |
      | Vendor Search | Purchasing Manager | Account Search  |
    And that I have a finance account in the closed state
      | name              | code              | accountTypeCd | externalId |
      | OPEN_ACCOUNT_TEST | TEST_ACCOUNT_FINANCE_OPEN | INVASSET      | EXT_REF    |
    When an Update Finance Account API request is sent to Maintenix to open the finance account
      | name              | code              | closed |
      | OPEN_ACCOUNT_TEST | TEST_ACCOUNT_FINANCE_OPEN | false  |
    Then the finance account is opened
