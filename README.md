cas-addon-hazelcast-ticket-registry
===================================

# NOTE!!!
Please note that this library is not maintained anymore, as starting from version `4.1.0`, CAS server includes native implementation of the [Hazelcast ticket registry](https://jasig.github.io/cas/4.1.x/installation/Hazelcast-Ticket-Registry.html). And CAS version `4.2.x` introduced more flexible and powerful [auto configuration model](https://jasig.github.io/cas/4.2.x/installation/Hazelcast-Ticket-Registry.html) for this registry implementation.


## CAS ticket registry implementation based on Hazelcast distributed grid software

> This project was developed as part of Unicon's [Open Source Support program](https://unicon.net/opensource).
Professional Support / Integration Assistance for this module is available. For more information [visit](https://unicon.net/opensource/cas).

## Current version
`1.1.0-GA`

## Supported CAS version
The minimum supported CAS server version is `4.0.0`

## Usage

* Define a dependency in your CAS war overlay:

  > Maven:

  ```xml
  <dependency>
      <groupId>net.unicon.cas</groupId>
      <artifactId>cas-addon-hazelcast-ticket-registry</artifactId>
      <version>1.1.0-GA</version>
      <scope>runtime</scope>
  </dependency>
  ```

  > Gradle:

  ```Groovy
  dependencies {
        ...
        runtime 'net.unicon.cas:cas-addon-hazelcast-ticket-registry:1.1.0-GA'
        ...
  }
  ```

* Add `classpath*:/META-INF/spring/*.xml` entry to `contextConfigLocation` (for CAS version < 4.1.0)

  > in `WEB-INF/web.xml`

  ```xml
  <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            /WEB-INF/spring-configuration/*.xml
            /WEB-INF/deployerConfigContext.xml
            classpath*:/META-INF/spring/*.xml
        </param-value>
  </context-param>
  ```

* Add **ALL** the cluster member nodes (comma-separated hostnames or ip addresses) in `hz.cluster.members` property of the externalized `cas.properties` file (distributed on all the participated CAS nodes):

```java
hz.cluster.members=cas1.example.com,cas2.example.com
```
