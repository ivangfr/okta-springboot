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

- [`Java 11+`](https://www.oracle.com/java/technologies/downloads/#java11)
- [`Okta` account](https://developer.okta.com/signup/)

## Configure Okta

### Access Developer Edition Account

- If you do not have a Developer Edition Account, you can create one at https://developer.okta.com/signup/
- If you already have, access https://developer.okta.com/login/

### Access Okta Admin Dashboard

If you are in `Okta Developer Dashboard` home page, click `Admin` button on the top-right

![okta-developer-home](documentation/okta-developer-home.png)

The picture below is how `Okta Admin Dashboard` looks like

![okta-admin-dashboard](documentation/okta-admin-dashboard.png)

### Add Application

- In the `Okta Admin Dashboard` main menu on the left, click `Applications` menu and then `Applications` sub-menu
- In the next page, click `Create App Integration` button
- Select `OIDC - OpenID Connect` as _Sign on method_ and `Web Application` as _Application type_. Click `Next` button
- Enter the following values in the form
  - General Settings
    - App integration name: `Simple Service`
    - Sign-in redirect URIs: `http://localhost:8080/login/oauth2/code/okta`
    - Sign-out redirect URIs: `http://localhost:8080`
  - Assignments
    - Controlled access: `Skip group assignment for now`
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

### Fix Person username

> **Warning:** if we don't do the fix, we will see the following error
> ```
> Login with OAuth 2.0
> [invalid_token_response] An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: 400 Bad Request: [{"error":"server_error","error_description":"The 'sub' system claim could not be evaluated."}]
>```
- In the `Okta Admin Dashboard` main menu on the left, click `Applications` menu and then `Applications` sub-menu
- In Applications list whose status are `ACTIVE`, select `Simple Service` application
- Click `Assignments` tab
- Edit `Mario Bros` by clicking on the `pen` icon
- Set `mario.bros@test.com` in the `User Name` text-field
- Click `Save` button

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
  ./mvnw clean spring-boot:run --projects simple-service
  ```

- ### Running application as a Docker container

  - **Build Docker Image**
    
    - JVM
      ```
      ./docker-build.sh
      ```
    - Native
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
      -e OKTA_CLIENT_ID=${OKTA_CLIENT_ID} \
      -e OKTA_CLIENT_SECRET=${OKTA_CLIENT_SECRET} \
      -e OKTA_DOMAIN=${OKTA_DOMAIN} \
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

## Shutdown

Go to the terminal where the application is running and press `Ctrl+C`

## Running Test Cases

- In a terminal, make sure you are inside `okta-springboot` root folder

- Run the command below to start the **Unit Tests**
  ```
  ./mvnw clean test --projects simple-service
  ```

## Cleanup

### Docker image

To remove the Docker images created by this project, go to terminal and run the following command
```
docker rmi ivanfranchin/simple-service:1.0.0
```

### Okta Configuration

#### Delete Person

- In the `Okta Admin Dashboard` main menu on the left, click `Directory` menu and then `People` sub-menu
- Click `Mario Bros` in the People list
- In `Mario Bros` profile, click `More Actions` multi-button and then `Deactivate`
- Confirm deactivation by clicking `Deactivate` button
- Still in `Mario Bros` profile, click `Delete` button
- Confirm deletion by clicking `Delete` button

#### Delete Application

- In the `Okta Admin Dashboard` main menu on the left, click `Applications` menu and then `Applications` sub-menu
- In Application list whose status is `ACTIVE`, click `Simple Service`'s `gear` icon and then click `Deactivate`
- Confirm deactivation by clicking `Deactivate Application` button
- In Application list whose status is `INACTIVE`, click `Simple Service`'s `gear` icon and then click `Delete`
- Confirm deletion by clicking `Delete Application` button
