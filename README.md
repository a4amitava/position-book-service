# position-book-service
Position Book system that can process trade events, and from which a user can retrieve a particular position identified by a trading account and security.

Technology Stack: Java11, Springboot, Lombok, RestTemplate for Api, maven, and Junit 5 as testing framework

Service description: This service provides 3 rest endpoints(json formatted)-

1._ processing events🥇

URI-1: POST http://localhost:8080/api/positions/events
sample pay load is provided in the payload folder. 

URI-2: GET http://localhost:8080/api/positions
this will fetch all the postions aggregated using the trading account and the security identifier.
 
URI-3: GET http://localhost:8080/api/positions/ACC2/SEC2
this will retrun postion for the Account and Security


*** Deployment Strategy: a manifest file has been created to deploy the artifact on cloud foundary's contanier based PAsS platofrom. 
A cmd file also provided to use the manifest for the deployment. This can be further enchanded to deploy on any cloud/contrainer platform.


*** Junit Coverage: Junit 5 has used to test and verify the logic. For Api/controller testing RestTemplate framework has been used endpoint
![image](https://user-images.githubusercontent.com/27267623/179429534-aca8a5e2-24a8-451d-9199-353149f09066.png)
