@RunPPCTest
Feature: Login to PPC

    @RefactorNonSmoke
    Scenario: Logging in to PPC
     Given PPC client is launched
      When I connect to PPC as "mxi"
      Then I should be logged into PPC
