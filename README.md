# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmJ43h+P40DsIyMQinAEbSHACgwAAMhAWSFFhzBOtQ-rNG0XS9AY6j5GgWaKnMay-P8HBXKBgpAf64Hlp8kIgupOxfGhunwr6zpdjACDCeKGJCSJBJEmApJvoYFTDvyDJMlOKlcjeNL7pSy4wGKEpujKcplu8SqmCqwYagAQiGMAuWoWAeT6oH1KlHAZTkUYxnGaAwOhUBJuUYlpnhBE5qoebzNBRYlvUejriihKZQ29EJaqGpukYEBqDAaAQMwVpouV3mLhJVBIjAAByvb9tlVW5ZK0yXtASAAF4oBwrXQBVVU1TA6YAIz1fyTUFmMx2lj4216rtB27HRTaJeqMAAJJoCA0AouAMCHhw5hICaAozbeDobdZroDFo8jbp5FSbcyL3xG9h2PRVmHIKmF1ONdoxjA1d0tcW0D1M9FE4x9jYMf1SUdrcj0wOV63o9U-qgcVKCxopXO85VFTnQArHVZMU-mVNtTAGLg6o47EKVMAQAAZjAlAlsSSyfQxs0ClVbOLVFW7rVSsP0oecgoM+8Tnpe17G-ewqPgGLuW9Z7YWfUTnihkqgAZgFkgaLLyEQZ8wkah3wUVR9Zx7R+Om+duH4WTAU0WRYyJ4hyekb1TaMV4vgBF4KDoDEcSJFXNdOb4WBiYKm0NNIEYCRG7QRt0PTyaoinDAXSEi5JlkPLCen55eSfIdCjzAfNi12fYzeOcJzeFW5qOUsbvlgPPzvwYXaBzsFd4VGFEVPt78iyvKo-oCzP2O-PY0TTAmu+E2bvw52eoK0+x7wATUV0WMGZ420unQm2Fiak2zLdOWhZqZPUgVAfah0S7M2+hqbSHNx4LXfOUTa-NoyC3VqdcWcCwD1CllnJBuYUEPTQfUJWahVZCwKBrbWutoD6xgIbRiQVeRXxXi6L2L4fadkFAfFEMwIA0EdifK82gL5iLhpUMKGRFE0CkZRM+oCPxL39E5Fop5g6h3DqbTa4wGwwOTLQnC0t7FXCZiI8uLEUTrn8NgcUGoBLTQAOJKg0K3eaUlgk937vYJUI855GJgZUf2MBxjPwXmHUxk8PL1GQDkUJOZHJoh3u5X2XkbajhgIyI+Z9VHzw0QuVQS5hThXFHfaRD8YoZNfhqd+Z9P7MB-s9I2lScoI2WqtYxpDI5bXppg960DRYExKHQhBN1mHNVQQrOmO0FnYOEZgPBbMMKEOmWQ0WAtuFlWodVZxMAGEbMaiwx67DlZcPVlrHWaDBGHNEU0iOgCDGvnKdbS+PlbJokKWoDEjT9zX2FDUmA0KPT-wkTZFF2hIU5FMNzFJ2SA5QrCVYhAgFsmAvAWk5YcScwFgaOMGlKBfrSALJdcIwRAggk2PEXUKA3Scj2N8ZIoA1T8sgosb4jKlpKglRcGAnQtLLNgaslxjDqVhLpQypUzLWXss5csblvKxWGTGEKhAIrjXNVNSCKVMrTVyoVYxPqTEK7+A4AAdjcE4FATgYgRmCHAbiAA2eAE5DDQvKhEqyE96jSQ6LE+JWN55ZltXMRVE8dIEqpWMVNVqDZZOnjkiZdt0TQoxHAMNpTQHlAPtUpkx8MlwpHC0+ot9gVYu6YkpCvSYD5XSigbqOK8WbXyjvK5VDQJnXuemRh5NkFbNYQrDqMAuquRwT2-pY9xpDN-qM8Fc0ZlAuAWtcpPMY1zL2Vgo6aC05OJVesmW877qvIVBgq966e0ELQZzc5szyElWFrcyWri52bOfWwxW7zMHXN4d8vW+bnVoujcQmyjsQWyN3PuqpJaUBlsZa7SpCLagVqPIYRlBpxSOHw9oYYpgkNm0kYy5l0yp5fnqCR+20KSVksLRSvS6q5g6vqGyjlQjb2VAztLbNTGWXCb1WJ51njmIBEsCgPsEBNi1yQAkMAqn1OaYAFIQHFMiis-hhUgDVEUWhbdZnNGZLJHojKEmnyQlmbA5rVNQDgBAOyUA1gyfcck1jcJ6gAEgRjrE85QHzfm9gAHUWC-V7j0ZKAkFBwAANKSu1bJmAInAhiZschxaAArYzaAy1GaDgO1yZSMNgs0fSOttSkL1LPk2rRQpW3tPbV0p+XaX5HIGgYj+27v67v+SFMBi1j2-vPZjeZV6lkZuVUTK6TzKbbJpq+pb71130dyX14A1aZCVMPnh3LnXFzaNaW2zF-Xqm5Z7X2ne83KWjtq5lcdgHJ00PvTOrMyxZYLpfcu1dPUFN-zGbYiZ1GUYbqrOaGAYZu14pOaWEU0AdBIDXJGCh1ygPTozED0DzzQcQZNGaGs4ZkJQ9wSNr9JYf3Dr-ZcgnE7RZTvvY80YwOn3yx2xwlW0HPl8J+QhpsVsztYdptgZGuGlQYnh8Aa7Jtbv1Gq2Vcj-RKNPbmOhlDfss1a64-+UlBavx8bAva8T8B7mZwIg2DxIiXUsS8MAeUsRtP1ygJ7+3wZYDAGwB5wgeQeHWdWbZ89Hcu49z7r0Ywad8WFvqCAbgeAV1fZxcVhjNl0+B9hajCpsuC94CL--DXAfM8mgQOVY5TPoAs9PYeyl-7KG-a5-9omvPItk624uoXUG1bCy+fwqAvyPHo9SYgQP3HLfL0iTbhxSq71E0d2TddQA
