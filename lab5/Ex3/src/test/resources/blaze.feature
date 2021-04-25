Feature: Book Trip

  Scenario: Book a Trip
    Given I am at "https://blazedemo.com"
    And Choose 'Paris' to 'London'
    And I press 'Find Flights'
    And I Select Option 4
    And I complete 'creditCardMonth' '12'
    And I check 'rememberMe'
    And I press 'Purchase Flight'
    Then The Page Title Should Be 'Confirmation'