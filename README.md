# 🏁 Get started here
This project was developed to put my current knowledge into practice, using as a model a project proposed in the form of a challenge for a job vacancy. The project is a simplified version of a payment service with transfer between users, covering the rules proposed by the challenge.
- RESTful API
- SpringBoot
- Clean Architecture
- Model-View-Controller (MVC) software design pattern
- Exception handling
- Integration Tests
- Unitary tests
# ℹ️ How to use this project?
The project was structured to use an in-memory database (H2), so it can be tested more easily, the database settings are already in the properties file. To test, simply start the application and use the API collection (Exported as JSON in resources folder) to create users and create new transactions.
## Step 1: Import the Postman Collection
Import the JSON file in the resources folder into your Postman. It will contain everything you need to essentially use the service.
## Step 2: Create the Users
In the imported Collection, you will find the User folder, there are two POST methods prepared to create a user of each type. You can also edit one of these methods, or create a new one, to test some exceptions when creating a user, such as a repeated document or email.
## Step 3: Accounts Creation
In the challenge proposal, it was not specified where the user's balance should be, but I took the liberty of creating a new entity, Account, to keep track of each user's balance, and separate the responsibilities of each object. 
A user's Account is automatically created when creating a new user. As the proposal is a simplified version, I haven't created a method to add credit (yet...), so the balance for each Account automatically starts as 10.00.
You can query an account by user id via the GET method in the Accounts folder, being able to validate the current balance of the account.
## Step 4: Create a Transaction
A template of the POST method for creating a transaction is also available in the collection. Just send the transaction amount, the user id sending the amount, and the user id receiving it.
Some validations are carried out when creating a transaction, such as whether the user has the balance available for that transaction, and whether the user sending the amount is not a retailer type user (rule proposed in the challenge).
# 🔭Improvement proposal (To be done)
- Microservices
- Create a method to add balance to the user's account
