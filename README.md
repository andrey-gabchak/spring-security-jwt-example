# Example of Spring Security configuration using JWT

- Spring Boot
- Spring Security
- JWT
- Maven
- MySQL
- Flyway
- Lombok

-----------

At first launch, Flyway will create tables 
for users and roles if they don't exist.

Also, Flyway will initial 3 default roles:
- ADMIN
- PAID_USER
- FREE_USER

and 3 default users:

|email|password|roles|
|-----|--------|-----|
|admin@gmail.com|admin|ADMIN, PAID_USER, FREE_USER|
|paid@gmail.com|paid|PAID_USER, FREE_USER|
|free@gmail.com|free|FREE_USER|

So, you don't have to worry about creating of test users. 
You can sing in and check how it works.