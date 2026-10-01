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

Server Sequence Diagram: https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAGZM9qBACu2AMQALG4AHABMAJwgMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVx0UBQwAA1vBkOYYEg0NgfMxsjBjggAGbQOYcGDQGAgaBQPxYNicbiwUr3G7PGAAIWAHAAkujMQBRAAeKmwBAK12qlDu92K5nqgScTmG4zm6mA9nmix5UG8dRgemJsIAjqkclCzJxMDSnvTKjdRPVYVkcpQABQZB1gSgZQ1qMAASlFIhUd0qsnkShU6nqqrAAFUBk63h8UH6Q4plGpVEGjDpagAxNHE2OUFM6ywwBOzMQwuGIwuwTZIMDxMsDRMwYAIWHs0soPnZMAaFNh9MS63VW0wWsp-1UUQjqrUZkKDtwjiWdoI9D8wXCwpM+mS5EYGVONwKsZK1QqtXLDVa+rluZt5ddmDyeHoE3oDiYQdp9Rzm0VHqNAfAQBBp1ne5f3DVRahATt3VreMBhTZNtCHf97mMWoFA4AsUO0CDAyg9C-1g+C4XdBRMXiJ1gBo1Cf1ImDM2w3DiWoxsp0AwwGUqC06XqV0+w9NRQOpR46QAsVtVeFsKyWep9hBejG3aCB3zQRTlkuadxT4pESjAeowjlM8H3mGAlOBZZVPidTNO0-Yri-UwvF8AJoHYVUYhzOAeWkOAFBgAAZCAskKKUylHBdtWaNoul6Ax1HyNAFQstZfn+Dgrj3TMBOZEYMpgLKdmhTtESirFuSxDNcQJKAiRJWByU1KlzUkygDL3eo2U5GqtxQIVUr0-dKiq495VGc90yvPZb2gepdRgA0jTAT8zQKq15xnICYAQcK0SdMKIs9Na-R4oNoPTSMUBjOMLLQ0MyNY7MYDzTgJwI+QS2bKYK0wKsES+8V60bP73grIwIDUGA0AgZgez7JjnpY7qxz2kCwKI3jGRk+oADkEZzXxOEG4aRTygyJpgWVAjPC85qUhbtTeOGEZgAkfE4DbvyBmt5MfR4Sv0P4dmh9nmGOMAQHiFHUzRvGAxQeouVa2FwBx6TYvqaNpho6AkAALxQDhyZ3Ua5wm2UAEYGdm1V5s1Ray31xtDZN3YXLNa7MKV3aVZBqBuIx3jg2Ym6KLkFBONouzGN9jMsOzdiYFjkPlbnLb6hOtEMlUcSOtpfT-cKiZBcs6zvjshyPysnSTSpg8jJMszpospzq5o2utPrsZebc7w-H8LwUHQGI4kSUfx5O3wsCqzMesaaQeRCnl2h5boemS1RUuGGuNPMHwQEt+5s5gEYD80q4tsX0P6gO+w5+O8K57O70LtDzNE9u+7nSv9AT0FbDmTvUAA4ndIOxY8TeBmG2buh9Cj83gWpRBks2zqAgEQaOxIwZNmABDeWGEk7+3HFjcCl10Y6xgETMAJNuZmwFENC2TdxqHmMrbe2ypHbqmdqzN28QPamwHonbWAddbfWAFrEiqMbocBQNwKiNE6IMW0EA4hr1agZBmBAGgadVHyGkWfTq2pZ6YnzoXW+VCaj1DIMfGCp9m7ShgKZKaIxvbfk8EPAIsJiT+GwGiREIU+wwDARWDQC9S5xTAevLe9gKz7wQZpU+-ETEvAAWgG+Ji77Kwfn2MJSoVGoM0p-TOMjgERhgDUJAeJLBIQyeol6oCYAADVKA1NLBkwGFUUH2TQfDZgVB2xIG-KI6xZDQIUK-tY5ktDY5CMYduEarDDLOM4dNRmPCxgs3vAIhZIiI5+xiuIqBhFKHh1kZU5AOQClqGQkWNRRCmmVGwqEyB1zmAQDxG2cASA9HxKVEY1JxdtS3OCTkIuloxGFWWACtQCxGjjDhRyaQCKbZhDcIEEEmx4gNhQLWRMexvjJFAPCAlClFjfDhQTCslKLgwE6LlGSVt2GtzcbC8JCKGhIorCitFGKsXLBxXi8lnxKUghJSAMlFciUgmpbS-u9LGWmk8e5YeHAADsEQnAoCcDEHkbg4D+QAGzwAQoYW5MAijsJyTYxorQOhxISQInuCp5VzCZbFfKaSL4ZLWO6lA5VqyrNIGiDEtUcTtgak1Uk6t2pbRmdqPqXJw3m2Wcy6mrLaZt0VA7NU2y+FLXZCtFAXpjQeMhVJUhe0o7uluUUvpml-UVhpXMUpAdv6HNglU9ptT6lJMAU8tGLy3ptKgB03pPdunBoyegoZyBRldrERM7GlComEwRvM8dns02UwzU4o82a7YbLzU7O8rs7L7IrWM6tgdJxnOmRcip3ba0oHrQGxpw6sz1FTmgFAmwYBwuncDS1eC4ZQ3bJ2VcMAkY5AHEu8ZmNJlAp2ouZ8q51yaV3bufdbCW7ZtPCe7h+adk6mLatb0A813AstJGXl0hK0l2OTC5F0gYCetuJm-DriFQcrmHy+o6LMUVq8R5fwlgFEHQA7EJACQwASbAhAADAApCAaJQlQ38JK+E1qjK2uZM0aMiUehwsScU9ACpsAIGABJqAcAIAHSgM2-j0gOP6Ro4JX1NFIBNpKtZ2z9nHPOZQCioNwMF5hsxKiOqUbCSm2amSCk8aTGJt6uyFNvImEUxw16rjayc0zWI2el2y0KPlpVYx7aPF6gACs1NoHrX6vzNnKCBegMFlF7bIJPuIfUapfa4wNKHSAkd9Qx0Tq6cg2dAyqnDMXZckhxyV1TLKeumhm6DbbtNthxxeH8vHtzUV3h57uaXq217CrN6lt7XvYY85Mgu21fq-WqzLW7MOfa4B+jn6RvfrTnhCGr4IBfcfGB8kjngOIlY9F1EaB50jOG0cna45aH0LJlllh0y1uo9Jos5h6bcsHuMtm+mRHLxbNIzNrmPMK3INewFj7sBsSQZXKWN86BEeLeq2ndDa4NxoB29R1D2olxQb51hjHBPONE+PIRw75OSOFqfGL18-ODkLeXXtW5GcO0GXPqpvOYkEASRBdC7UdiyK7ZDcTnjowRNqoCF4GzE9ZNTygM7xAcJYDAGwFZwgeQChWsicxuKK814by3sYFJyOa3cDwE6LrxEetkXqCAOPUAE+c80UiL3T4EBGG0HoAwBfDFXZj4HC3DihdL0r+mK3NNbfuIq0AA