# Product manager #

Product manager is a simple application with possibility to manage products.
There are two implementations of databases(Hibernate, in-memory).
Also used REST to communicate with application.

## Tech/frameworks used ##

<img src="https://whirly.pl/wp-content/uploads/2017/05/spring.png" width="200"><img src="http://yaqzi.pl/wp-content/uploads/2016/12/apache_maven.png" width="200"><img src="https://upload.wikimedia.org/wikipedia/commons/2/2c/Mockito_Logo.png" width="200"><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNkximiwITI1smJcOkn_bx2Zk_RnNKnmDq23Ua26wTVd_YNJcWgw" width="200"><img src="https://shiftkeylabs.ca/wp-content/uploads/2017/02/JUnit_logo.png" width="200"><img src="https://jules-grospeiller.fr/media/logo_competences/lang/json.png" width="200"><img src="http://www.postgresqltutorial.com/wp-content/uploads/2012/08/What-is-PostgreSQL.png" width="200"><img src="https://cdn.bulldogjob.com/system/readables/covers/000/001/571/thumb/27-02-2019.png" width="200"><img src="https://i2.wp.com/bykowski.pl/wp-content/uploads/2018/07/hibernate-2.png?w=300" width="200"><width="200"><img src="http://mapstruct.org/images/mapstruct.png" width="200">

## Instalation ##

* JDK 1.8
* Apache Maven 3.x

## Build and Run ##
```
mvn clean package:
mvn exec:java
```
## API ##

Application is available on localhost:[PORT]. Use ```http://localhost:[PORT]/swagger-ui.html#```

## Setup Database ##

To change using database go to [application.properties](https://github.com/CodersTrustPL/project-8-basia-daniel-maksym/blob/master/src/main/resources/application.properties). You can choose in-file, in-memory, mongo or hibernate database
```
   application.database=in-memory
   application.database=hibernate
```
Application works correctly without hibernate database.

To use **hibernate** , firt  configure it on your computer, use PgAdmin ( or another tool) and [hibernate.properties](https://github.com/MaksymChernevskyy/ProductManager/blob/master/src/main/resources/hibernate.properties)
```
spring.datasource.url=yourDatabase
```

## For an end User ##

```http://localhost:[PORT]/```
Allows you to view product data in the database