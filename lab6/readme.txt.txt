Vulnerability: 1
public ResponseEntity<Car> createCar(@RequestBody Car car){
->Replace this persistent entity with a simple POJO or DTO object

Code Smells: 4
2 Major

assertEquals(c,null);
Swap these 2 arguments so they are in the correct order: expected value, actual value.

assertEquals(available.size(),2);
Swap these 2 arguments so they are in the correct order: expected value, actual value.

2 Blocker

void getAllCars()
-Add at least one assertion to this test case.

void contextLoads()
Add at least one assertion to this test case


Ex2 
Technical Debt - 24 min
Effort to fix all Code Smells