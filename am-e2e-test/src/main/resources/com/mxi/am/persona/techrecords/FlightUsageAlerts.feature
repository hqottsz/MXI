@CSI @FlightAPI
Feature: Display Alert When Usage Message Values Break Business Logic

  # Note, the following Alert must be set to Active and be assigned to one of the User's Roles: Usage Record Creation Failed
  Background:
    Given I am a superuser
    Given that a user has navigated to the user alerts page
      | Page_Name   | Parent_Menu_Item | Child_Menu_Item |
      | User Alerts | Options          | Alerts          |
    And that a user has deleted all notifications

  @SmokeTest @Ignore
  Scenario: OEM Serial Number does not exist in Maintenix
    Given that the usage on an Engine changes
    When the external system sends "an invalid" inbound Usage message
      | name       | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn  | tso  | tsi  |
      | APU Test 1 | 2099-09-08 13:00:04 | false               | Sample Doc Ref     |                 999 | SNJSJSJ1          |               102 | HOURS      | 1104 | 1104 | 1104 |
    Then an appropriate synchronous "Usage Record Creation Failed" alert is raised
      | Message                                                                                                                                     |
      | The creation of usage record with external ID ''999'' failed. Cause: ''No inventory item found for the provided serial number 'SNJSJSJ1'.'' |

  @RefactorNonSmoke
  Scenario: OEM Serial Number does not exist in Maintenix
    Given that the usage on an Engine changes
    When the external system sends "an invalid" inbound Usage message
      | name       | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn  | tso  | tsi  |
      | APU Test 1 | 2099-09-08 13:00:04 | false               | Sample Doc Ref     |                 999 | SNJSJSJ1          |               102 | HOURS      | 1104 | 1104 | 1104 |
    Then an appropriate synchronous "Usage Record Creation Failed" alert is raised
      | Message                                                                                                                                     |
      | The creation of usage record with external ID ''999'' failed. Cause: ''No inventory item found for the provided serial number 'SNJSJSJ1'.'' |

  # Note: Scenario named Timestamp is older than timestamp of the most recent Usage record Is Located in FlightUsage Feature file
  @RefactorNonSmoke
  Scenario: OEM Serial Number, is not assigned to correct aircraft Registration Code
    Given that the usage on an Engine changes
    When the external system sends "an invalid" inbound Usage message
      | name       | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn  | tso  | tsi  |
      | APU Test 1 | 2099-09-08 13:00:04 | false               | Sample Doc Ref     |                 999 | TECHREC-004       |               101 | HOURS      | 1104 | 1104 | 1104 |
    Then an appropriate synchronous "Usage Record Creation Failed" alert is raised
      | Message                                                                                                                                                                                |
      | The creation of usage record with external ID ''999'' failed. Cause: ''Inventory item with serial number 'TECHREC-004' is not assigned to the aircraft with registration code '101'.'' |

  @RefactorNonSmoke
  Scenario: TSN and or TSO and or TSI Hours are less than the current respective APU Usage Hours
    Given that the usage on an Engine changes
    When the external system sends "an invalid" inbound Usage message
      | name       | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn | tso | tsi |
      | APU Test 1 | 2099-09-08 13:00:04 | false               | Sample Doc Ref     |                 999 | TECHREC-004       |               102 | HOURS      | 999 | 999 | 999 |
    Then an appropriate synchronous "Usage Record Creation Failed" alert is raised
      | Message                                                                                                                                                                |
      | The creation of usage record with external ID ''999'' failed. Cause: ''The provided usage values for usage parameter 'HOURS' are less than the current usage values.'' |

  @RefactorNonSmoke
  Scenario: TSN and or TSO and or TSI Cycles are less than the current respective APU Usage Cycles
    Given that the usage on an Engine changes
    When the external system sends "an invalid" inbound Usage message
      | name       | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn | tso | tsi |
      | APU Test 1 | 2099-09-08 13:00:04 | false               | Sample Doc Ref     |                 999 | TECHREC-004       |               102 | CYCLES     | 999 | 999 | 999 |
    Then an appropriate synchronous "Usage Record Creation Failed" alert is raised
      | Message                                                                                                                                                                 |
      | The creation of usage record with external ID ''999'' failed. Cause: ''The provided usage values for usage parameter 'CYCLES' are less than the current usage values.'' |

  @RefactorNonSmoke
  Scenario: No Manufacturer Code, OEM Serial Number has duplicates in database
    # Note that duplicate OEM Serial Number does not have to be of the same Part Number
    Given that the usage on an Engine changes
    And a duplicate serial number exists
    When the external system sends "an invalid" inbound Usage message
      | name       | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn  | tso  | tsi  |
      | APU Test 1 | 2099-09-08 13:00:04 | false               | Sample Doc Ref     |                 999 | TECHREC-004       |               102 | HOURS      | 1104 | 1104 | 1104 |
    Then an appropriate synchronous "Usage Record Creation Failed" alert is raised
      | Message                                                                                                                                                                               |
      | The creation of usage record with external ID ''999'' failed. Cause: ''Multiple inventory items were found for the provided values. Values should identify a unique inventory item.'' |
