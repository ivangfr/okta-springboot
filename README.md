# springboot-okta

The goal of this project is to create a simple [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) REST API, called `simple-service`. It uses [`Okta`](https://www.okta.com/) to handle authentication.

> **Note:** In the repository [`springboot-react-okta`](https://github.com/ivangfr/springboot-react-okta) you can find a more complex example that involves:
> - implementation of a [`ReactJS`](https://reactjs.org/) front-end application and a `Spring Boot` back-end application, both secured by `Okta`;
> - enabling and creating `Okta` groups (a.k.a `ROLES` of the applications);
> - adding new users. 

## Application

- **simple-service**

  `Spring Boot` Web Java application that exposes two endpoints:
  - `/api/public`: endpoint that can be access by anyone, it is not secured
  - `/api/private`: endpoint that can just be accessed by users registered in `Okta`

## Configure Okta

First of all, you must create a free account at https://developer.okta.com/signup/. Once you have it, log in and let's start the configuration.

### Add an OpenID Connect Client

- In `Okta Developer Dashboard`, click on `Applications` and then on `Add Application` button.
- Select `Web` and click on `Next` button.
- Enter the following values in the form

  | Setting              | Value                                        |
  | -------------------- | -------------------------------------------- |
  | Name                 | Simple Service                               |
  | Base URIs            | http://localhost:8080/                       |
  | Login redirect URIs  | http://localhost:8080/login/oauth2/code/okta |
  | Logout redirect URIs | http://localhost:8080                        |
  | Grant Types Allowed  | Authorization Code                           |

- After the application is created, there are some values that you will need during all project configuration and execution.

  | Setting       | Example (fake)              | Where to Find                                                      |
  | ------------- | --------------------------- | ------------------------------------------------------------------ |
  | Org URL       | https://dev-123456.okta.com | On the home screen of the developer dashboard, in the upper right  |
  | Okta Domain   | dev-123456.okta.com         | It is the Org URL without `https://`                               |
  | Client ID     | 0bcky2d71eXtSsscC123        | In the applications list or on the `General` tab of a specific app |
  | Client Secret | m6tgtn_70aXNdtIKbeAAxXvEaoi9aVxeFX68If-T | On the `General` tab of a specific app                |

## Start application

- Open a terminal and make sure you are in `springboot-okta` root folder

- Export the following environment variables. Those values were obtained while configuring `Okta`. See [`Configuring Okta > Add an OpenID Connect Client`](https://github.com/ivangfr/springboot-okta#add-an-openid-connect-client) section.
  ```
  export OKTA_DOMAIN=...
  export OKTA_CLIENT_ID=...
  export OKTA_CLIENT_SECRET=...
  ```

- Then, run the [`Maven`](https://maven.apache.org/) command below
  ```
  ./mvnw spring-boot:run --projects simple-service
  ```

## Testing simple-service endpoints

- Test `/public` endpoint
  - In a browser and access the URL `http://localhost:8080/public`
  - It should return `It is public.`

- Test `/private` endpoint
  - In a browser and access the URL `http://localhost:8080/private`
  - It should redirect you to `Okta` login page
  - Enter your `username` and `password` (the ones you used to create a free `Okta` account)
  - If you informed your credentials correctly, it should return `<YOUR FULLNAME>, it is private.`
