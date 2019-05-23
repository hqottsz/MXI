Feature: Revise Reference Document
   As a maintenance program engineer,
   I want to revise reference documents including all of the relevant revision and activation information and verify that proper traceability information is available.

   @JspSmokeTest
   Scenario: Revising and activating a reference document results in proper taceability information being available

       The maintenance program engineer revises a reference document and then activates the revised reference document
       As a result, the revision and activation information is properly updated

       Given I am an engineer
       And a reference document exists
       When I revise a reference document
       Then the reference document is updated
