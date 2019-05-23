@CSI @FlightAPI
Feature: Receive Usage message and validate schema prior to sending response - aircraft identifier registration code provided and oem serial number provided instead of manufacturer code, incorrect schema format

  Background:
    Given I am a superuser

  # Note, the following Alert must be set to Active and be assigned to one of the User's Roles: Usage Record Creation Failed
  @SmokeTest
  Scenario: Happy path, Usage message received, registration code and OEM SN provided, send response
    Given that the usage on an Engine changes
    When the external system sends "a valid" inbound Usage message
      | name       | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn  | tso  | tsi  |
      | ENG Test 1 | 2000-09-08 13:00:03 | false               | Sample Doc Ref     |             1998899 | TECHREC-002       |               102 | HOURS      | 1104 | 1104 | 1104 |
    Then the specified Engine usage information is updated accordingly and a response message with a status of "COMPLETE" is sent
      | status   | Namespace                                               | external-identifier | barcode | tsn  | tso  | tsi  | oem-serial-number | oem-part-number |
      | COMPLETE | http://xml.mxi.com/xsd/core/flights/usages-response/1.0 |             1998899 |         | 1104 | 1104 | 1104 | TECHREC-002       | ENG_ASSY_PN1    |

  # This scenario is placed within this Feature and not FlightUsageAlerts since it is dependent on the Engine having at least one historical Usage Record
  # Add Ignore, because it failed intermittent
	@RefactorNonSmoke @Ignore
  Scenario: Timestamp is older than timestamp of the most recent Usage record
    Given that a user has navigated to the user alerts page
      | Page_Name   | Parent_Menu_Item | Child_Menu_Item |
      | User Alerts | Options          | Alerts          |
    And that a user has deleted all notifications
    Given that the usage on an Engine changes
    When the external system sends "an invalid" inbound Usage message
      | name       | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn  | tso  | tsi  |
      | ENG Test 1 | 1999-09-08 13:00:02 | false               | Sample Doc Ref     |                 999 | TECHREC-002       |               102 | HOURS      | 1104 | 1104 | 1104 |
    Then an appropriate synchronous "Usage Record Creation Failed" alert is raised
      | Message                                                                                                                                                              |
      | The creation of usage record with external ID ''999'' failed. Cause: ''The provided collection date is before the collection date of the most recent usage record.'' |

	@RefactorNonSmoke
  Scenario: Schema validation missing mandatory ELEMENT exception scenarios, no name Element
    Given that the usage on an Engine changes
    When the external system sends "an invalid missing mandatory elements" inbound Usage message
      | name | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn  | tso  | tsi  |
      |      | 2099-09-08 13:00:04 | false               | Sample Doc Ref     |                 9999 | TECHREC-002       |               102 | HOURS      | 1105 | 1105 | 1105 |
    Then a response message with a status of "ERROR" is sent
      | status | Namespace                                    | message                                                                                                                                                                                                                         |
      | ERROR  | http://xml.mxi.com/xsd/integration/error/1.1 | Expected element 'name@http://xml.mxi.com/xsd/core/flights/usage/1.2' instead of 'collection-date@http://xml.mxi.com/xsd/core/flights/usage/1.2' here in element usage-attributes@http://xml.mxi.com/xsd/core/flights/usage/1.2 |

	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke @Ignore
  Scenario: Schema validation missing mandatory ELEMENT VALUE exception scenarios, no name Element Value
    Given that the usage on an Engine changes
    When the external system sends "an invalid missing mandatory element values" inbound Usage message
      | name | collection-date     | process-as-historic | document-reference | external-identifier | oem-serial-number | registration-code | usage-parm | tsn  | tso  | tsi  |
      |      | 2099-09-08 13:00:04 | false               | Sample Doc Ref     |                 99999 | TECHREC-002       |               102 | HOURS      | 1106 | 1106 | 1106 |
    Then a response message with a status of "ERROR" is sent
      | status | Namespace                                    | message                               |
      | ERROR  | http://xml.mxi.com/xsd/integration/error/1.1 | Usage name must not be null or empty. |
