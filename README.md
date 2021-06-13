# okta-springboot

The goal of this project is to create a simple [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) REST API application, called `simple-service`, that uses [`Okta`](https://www.okta.com/) to handle authentication.

> **Note:** In the repository [`okta-springboot-react`](https://github.com/ivangfr/okta-springboot-react) you can find a more complex example that involves:
> - Implementation of a [`ReactJS`](https://reactjs.org/) front-end application and a `Spring Boot` back-end application, both secured by `Okta`
> - Enabling and creating `Okta` groups (a.k.a `ROLES` of the applications)

## Application

- ### simple-service

  `Spring Boot` Web Java application that exposes two endpoints:
  - `/api/public`: endpoint that can be access by anyone, it is not secured
  - `/api/private`: endpoint that can just be accessed by users registered in `Okta`

## Prerequisites

- [`Java 11+`](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [`Okta` account](https://developer.okta.com/signup/)

## Configure Okta

### Access Developer Edition Account

- If you do not have a Developer Edition Account, you can create one at https://developer.okta.com/signup/
- If you already have, access https://developer.okta.com/login/

### Access Okta Admin Dashboard

If you are in `Okta Developer Dashboard` home page, click `Admin` button on the top-right

![okta-developer-home](images/okta-developer-home.png)

The picture below is how `Okta Admin Dashboard` looks like

![okta-admin-dashboard](images/okta-admin-dashboard.png)

### Add Application

- In the `Okta Admin Dashboard` main menu on the left, click `Applications` menu and then `Applications` sub-menu
- In the next page, click `Create App Integration` button
- Select `OIDC - OpenID Connect` as _Sign on method_, `Web Application` as _Application type_, and click `Next` button
- Enter the following values in the form
  - App integration name : `Simple Service`
  - Sign-in redirect URIs: `http://localhost:8080/login/oauth2/code/okta`
  - Sign-out redirect URIs: `http://localhost:8080`
- Click `Save` button
- On the next screen, it's shown the 3 important values you will need to configure and run the `Simple Service`: `Client ID`, `Client Secret` and `Okta Domain`
  
### Add Person

- In the `Okta Admin Dashboard` main menu on the left, click `Directory` menu and then `People` sub-menu
- In the next page, click `Add person` button
- Enter the following information
  - First name: `Mario`
  - Last name: `Bros`
  - Username: `mario.bros@test.com`
  - Primary email: `mario.bros@test.com`
  - Password: `Set by admin`
  - Set a strong password in the text-field that will appear
  - `Uncheck` the check-box that says _"User must change password on first login"_
- Click `Save` button

### Assign Person to Application

- In the `Okta Admin Dashboard` main menu on the left, click `Applications` menu and then `Applications` sub-menu
- In the next page, click `Assign Users to App` button
- Select the `Simple Service` check-box in the _Applications_ column and `Mario Bros` check-box in the _People_ column. Click `Next` button to continue assignment process
- Click `Confirm Assignments` button

## Start application

- Open a terminal and make sure you are in `okta-springboot` root folder

- Export the following environment variables. Those values were obtained while [adding Application](#add-application).
  ```
  export OKTA_CLIENT_ID=...
  export OKTA_CLIENT_SECRET=...
  export OKTA_DOMAIN=...
  ```

- ### Running application using Maven

  ```
  ./mvnw clean package spring-boot:run --projects simple-service -DskipTests
  ```

- ### Running application as a Docker container

  - **Build Docker Image**
    
    - JVM
      ```
      ./docker-build.sh
      ```
    - Native (it's not working yet, see [Issues](#issues))
      ```
      ./docker-build.sh native
      ```

  - **Environment Variables**
    
    | Environment Variable | Description                                 |
    | -------------------- | ------------------------------------------- |
    | `OKTA_CLIENT_ID`     | Specify the `Client ID` defined by Okta     |
    | `OKTA_CLIENT_SECRET` | Specify the `Client Secret` defined by Okta |
    | `OKTA_DOMAIN`        | Specify the `Domain` defined by Okta        |

  - **Start Docker Container**
    
    ```
    docker run --rm --name simple-service -p 8080:8080 \
      --env OKTA_CLIENT_ID=${OKTA_CLIENT_ID} \
      --env OKTA_CLIENT_SECRET=${OKTA_CLIENT_SECRET} \
      --env OKTA_DOMAIN=${OKTA_DOMAIN} \
      ivanfranchin/simple-service:1.0.0
    ```

## Testing endpoints

- Test `/public` endpoint
  - In a browser, access http://localhost:8080/public
  - It should return `It is public.`

- Test `/private` endpoint
  - In a browser, access http://localhost:8080/private
  - It should redirect you to `Okta` login page
  - Enter `Mario Bros` username (`mario.bros@test.com`) and password
  - It should return `Mario Bros, it is private.`

## Shutdown Application

Go to the terminal where it is running and press `Ctrl+C`

## Okta Clean Up

### Delete Person

- In the `Okta Admin Dashboard` main menu on the left, click `Directory` menu and then `People` sub-menu
- Click `Mario Bros` in the People list
- In `Mario Bros` profile, click `More Actions` multi-button and then `Deactivate`
- Confirm deactivation by clicking `Deactivate` button
- Still in `Mario Bros` profile, click `Delete` button
- Confirm deletion by clicking `Delete` button

### Delete Application

- In the `Okta Admin Dashboard` main menu on the left, click `Applications` menu and then `Applications` sub-menu
- In Application list whose status is `ACTIVE`, click `Simple Service`'s `gear` icon and then click `Deactivate`
- Confirm deactivation by clicking `Deactivate Application` button
- In Application list whose status is `INACTIVE`, click `Simple Service`'s `gear` icon and then click `Delete`
- Confirm deletion by clicking `Delete Application` button

## Issues

The native Docker image is built successfully, but the following exception is thrown at startup
```
ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed

java.lang.IllegalStateException: Error processing condition on com.okta.spring.boot.oauth.OktaOAuth2ResourceServerAutoConfig.opaqueTokenIntrospector
	at org.springframework.boot.autoconfigure.condition.SpringBootCondition.matches(SpringBootCondition.java:60) ~[com.mycompany.simpleservice.SimpleServiceApplication:2.4.7]
	at org.springframework.context.annotation.ConditionEvaluator.shouldSkip(ConditionEvaluator.java:108) ~[na:na]
	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.loadBeanDefinitionsForBeanMethod(ConfigurationClassBeanDefinitionReader.java:193) ~[na:na]
	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.loadBeanDefinitionsForConfigurationClass(ConfigurationClassBeanDefinitionReader.java:153) ~[na:na]
	at org.springframework.context.annotation.ConfigurationClassBeanDefinitionReader.loadBeanDefinitions(ConfigurationClassBeanDefinitionReader.java:129) ~[na:na]
	at org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions(ConfigurationClassPostProcessor.java:343) ~[com.mycompany.simpleservice.SimpleServiceApplication:5.3.8]
	at org.springframework.context.annotation.ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry(ConfigurationClassPostProcessor.java:247) ~[com.mycompany.simpleservice.SimpleServiceApplication:5.3.8]
	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanDefinitionRegistryPostProcessors(PostProcessorRegistrationDelegate.java:311) ~[na:na]
	at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:112) ~[na:na]
	at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:746) ~[na:na]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:564) ~[na:na]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:144) ~[na:na]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:771) ~[com.mycompany.simpleservice.SimpleServiceApplication:2.4.7]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:763) ~[com.mycompany.simpleservice.SimpleServiceApplication:2.4.7]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:438) ~[com.mycompany.simpleservice.SimpleServiceApplication:2.4.7]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:339) ~[com.mycompany.simpleservice.SimpleServiceApplication:2.4.7]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1329) ~[com.mycompany.simpleservice.SimpleServiceApplication:2.4.7]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1318) ~[com.mycompany.simpleservice.SimpleServiceApplication:2.4.7]
	at com.mycompany.simpleservice.SimpleServiceApplication.main(SimpleServiceApplication.java:12) ~[com.mycompany.simpleservice.SimpleServiceApplication:na]
Caused by: java.lang.IllegalStateException: java.io.FileNotFoundException: class path resource [com/okta/spring/boot/oauth/OktaOpaqueTokenIntrospectConditional$IssuerCondition.class] cannot be opened because it does not exist
	at org.springframework.boot.autoconfigure.condition.AbstractNestedCondition$MemberConditions.getMetadata(AbstractNestedCondition.java:149) ~[na:na]
	at org.springframework.boot.autoconfigure.condition.AbstractNestedCondition$MemberConditions.getMemberConditions(AbstractNestedCondition.java:121) ~[na:na]
	at org.springframework.boot.autoconfigure.condition.AbstractNestedCondition$MemberConditions.<init>(AbstractNestedCondition.java:114) ~[na:na]
	at org.springframework.boot.autoconfigure.condition.AbstractNestedCondition.getMatchOutcome(AbstractNestedCondition.java:62) ~[com.mycompany.simpleservice.SimpleServiceApplication:2.4.7]
	at org.springframework.boot.autoconfigure.condition.SpringBootCondition.matches(SpringBootCondition.java:47) ~[com.mycompany.simpleservice.SimpleServiceApplication:2.4.7]
	... 18 common frames omitted
Caused by: java.io.FileNotFoundException: class path resource [com/okta/spring/boot/oauth/OktaOpaqueTokenIntrospectConditional$IssuerCondition.class] cannot be opened because it does not exist
	at org.springframework.core.io.ClassPathResource.getInputStream(ClassPathResource.java:187) ~[na:na]
	at org.springframework.core.type.classreading.SimpleMetadataReader.getClassReader(SimpleMetadataReader.java:55) ~[na:na]
	at org.springframework.core.type.classreading.SimpleMetadataReader.<init>(SimpleMetadataReader.java:49) ~[na:na]
	at org.springframework.core.type.classreading.SimpleMetadataReaderFactory.getMetadataReader(SimpleMetadataReaderFactory.java:103) ~[na:na]
	at org.springframework.core.type.classreading.SimpleMetadataReaderFactory.getMetadataReader(SimpleMetadataReaderFactory.java:81) ~[na:na]
	at org.springframework.boot.autoconfigure.condition.AbstractNestedCondition$MemberConditions.getMetadata(AbstractNestedCondition.java:146) ~[na:na]
	... 22 common frames omitted
```