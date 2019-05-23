@RunAMTechnicalRecords
Feature: Receiving of a Part Creation alert when a part is created

   @SmokeTest
   Scenario: When configured, technical records clerks receive Part Created alerts when parts are created by other users

      In the case when the Technical Records Clerk role is configured to receive Part Created alerts
      then users with that role will be notified with an alert when a part is created.
      Typically a user with the Material Controller role creates parts.
      By default, the alert will not be assigned and will indicate the part number of the part that was created.

      Given that technical records clerks are configured to receive Part Created alerts
      When another user creates a new part
      Then a technical records clerk is notified with an alert
      And the alert indicates the part number of the new part
