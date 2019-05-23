@LineTechnician @RunAMLine 
Feature: Display Sensitivity Alerts

    As a Line Technician
    I would like to be able to see Sensitivity Alerts
    So that I am aware that my actions may downgrade certain capabilities of the aircraft or
    require re-certifications on the aircraft when raising a fault against a failed system
  
    @E2ESmokeBuild1 @SensitivityAlertsData 
    Scenario: Line Technician views Sensitivity Alerts when raising a fault on a failed system with enabled system sensitivities
        Given I am a line technician
        When I raise a fault against a failed system with enabled system sensitivities
        Then I see sensitivity alert warnings which are enabled on the failed system

    @E2ESmokeBuild1 @SensitivityChipsData
    Scenario: Line Technician views Sensitivity Chips for a fault with enabled system sensitivities
        Given I am a line technician
        When I view the fault details for a failed system with enabled system sensitivities
        Then I see sensitivity chips indicating the sensitivities for the fault's failed system

    @E2ESmokeBuild1 @PartGroupSensitivitityChipsData
    Scenario: Line Technician views Sensitivity Chips for a fault with enabled part group sensitivities
        Given I am a line technician
        When I view the fault details which has a part requirement from a part group with enabled sensitivities
        Then I see sensitivity chips indicating the sensitivities for the part group
