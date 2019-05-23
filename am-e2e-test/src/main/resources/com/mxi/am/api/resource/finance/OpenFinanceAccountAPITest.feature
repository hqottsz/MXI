@CSI @AMAPI
Feature: Open Finance Account API Test

	@SmokeTest
  Scenario: An Update Finance Account API request is sent to Maintenix to open a closed finance account
    Given a Create Finance Account API request is sent to Maintenix
      | name         | code  | accountTypeCd | externalId | closed |
      | OPEN_ACCOUNT | CODE1 | INVASSET      | EXT_REF    | true   |
    When an Update Finance Account API request is sent to Maintenix to open the finance account
      | name         | code  | closed |
      | OPEN_ACCOUNT | CODE1 | false  |
    Then the finance account is open
      | name         | code  | accountTypeCd | externalId | closed |
      | OPEN_ACCOUNT | CODE1 | INVASSET      | EXT_REF    | false  |
