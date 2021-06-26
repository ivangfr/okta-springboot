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
  ./mvnw clean spring-boot:run --projects simple-service
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

## Shutdown Application

- Go to the terminal where it is running and press `Ctrl+C`
- To remove the Docker images created by this project, run
  ```
  ./remove-docker-images.sh
  ```

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

Unable to build to Docker native image
```
[INFO] --- spring-aot-maven-plugin:0.10.1-SNAPSHOT:test-generate (test-generate) @ simple-service ---
[INFO] Spring Native operating mode: native
[ERROR] java.lang.IllegalStateException: ERROR: in 'com.okta.spring.boot.oauth.OktaOAuth2AutoConfig'
  these methods are directly invoking methods marked @Bean: [oidcUserService] - due to the enforced proxyBeanMethods=false
  for components in a native-image, please consider refactoring to use instance injection. If you are confident this is
  not going to affect your application, you may turn this check off using -Dspring.native.verify=false.
[ERROR] [org.springframework.nativex.type.Type.verifyComponent(Type.java:2480),
  org.springframework.nativex.support.ResourcesHandler.processType(ResourcesHandler.java:1340),
  org.springframework.nativex.support.ResourcesHandler.processType(ResourcesHandler.java:1007),
  org.springframework.nativex.support.ResourcesHandler.checkAndRegisterConfigurationType(ResourcesHandler.java:997),
  org.springframework.nativex.support.ResourcesHandler.processFactoriesKey(ResourcesHandler.java:925),
  org.springframework.nativex.support.ResourcesHandler.processSpringFactory(ResourcesHandler.java:874),
  org.springframework.nativex.support.ResourcesHandler.processSpringFactories(ResourcesHandler.java:697),
  org.springframework.nativex.support.ResourcesHandler.register(ResourcesHandler.java:114),
  org.springframework.nativex.support.SpringAnalyzer.analyze(SpringAnalyzer.java:87),
  org.springframework.aot.nativex.ConfigurationContributor.contribute(ConfigurationContributor.java:70),
  org.springframework.aot.BootstrapCodeGenerator.generate(BootstrapCodeGenerator.java:75),
  org.springframework.aot.maven.TestGenerateMojo.execute(TestGenerateMojo.java:65),
  org.apache.maven.plugin.DefaultBuildPluginManager.executeMojo(DefaultBuildPluginManager.java:137),
  org.apache.maven.lifecycle.internal.MojoExecutor.execute(MojoExecutor.java:210),
  org.apache.maven.lifecycle.internal.MojoExecutor.execute(MojoExecutor.java:156),
  org.apache.maven.lifecycle.internal.MojoExecutor.execute(MojoExecutor.java:148),
  org.apache.maven.lifecycle.internal.MojoExecutor.executeForkedExecutions(MojoExecutor.java:355),
  org.apache.maven.lifecycle.internal.MojoExecutor.execute(MojoExecutor.java:200),
  org.apache.maven.lifecycle.internal.MojoExecutor.execute(MojoExecutor.java:156),
  org.apache.maven.lifecycle.internal.MojoExecutor.execute(MojoExecutor.java:148),
  org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject(LifecycleModuleBuilder.java:117),
  org.apache.maven.lifecycle.internal.LifecycleModuleBuilder.buildProject(LifecycleModuleBuilder.java:81),
  org.apache.maven.lifecycle.internal.builder.singlethreaded.SingleThreadedBuilder.build(SingleThreadedBuilder.java:56),
  org.apache.maven.lifecycle.internal.LifecycleStarter.execute(LifecycleStarter.java:128),
  org.apache.maven.DefaultMaven.doExecute(DefaultMaven.java:305),
  org.apache.maven.DefaultMaven.doExecute(DefaultMaven.java:192),
  org.apache.maven.DefaultMaven.execute(DefaultMaven.java:105),
  org.apache.maven.cli.MavenCli.execute(MavenCli.java:957),
  org.apache.maven.cli.MavenCli.doMain(MavenCli.java:289),
  org.apache.maven.cli.MavenCli.main(MavenCli.java:193),
  java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method),
  java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62),
  java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),
  java.base/java.lang.reflect.Method.invoke(Method.java:566),
  org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced(Launcher.java:282),
  org.codehaus.plexus.classworlds.launcher.Launcher.launch(Launcher.java:225),
  org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode(Launcher.java:406),
  org.codehaus.plexus.classworlds.launcher.Launcher.main(Launcher.java:347),
  java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method),
  java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62),
  java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43),
  java.base/java.lang.reflect.Method.invoke(Method.java:566),
  org.apache.maven.wrapper.BootstrapMainStarter.start(BootstrapMainStarter.java:39),
  org.apache.maven.wrapper.WrapperExecutor.execute(WrapperExecutor.java:122),
  org.apache.maven.wrapper.MavenWrapperMain.main(MavenWrapperMain.java:61)]
```