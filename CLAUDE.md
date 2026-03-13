# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Microservice part of the **Pricer** system. Responsible for user interface through telegram bot and automate notifications.

## Commands

```bash
# Build
./mvnw clean package

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ClassName

# Run a single test method
./mvnw test -Dtest=ClassName#methodName
```

## Architecture

**Tech stack:** Spring Boot 3.5.7, Java 17, Spring Data JPA, PostgreSQL, Lombok

**Runtime config** (`application.yaml`): Requires env vars `DB_HOST`, `DB_USER`, `DB_PASSWORD`.

### Package structure

```
ru.svsand.pricer.tgbot
├── Application.java          # Spring Boot entry point
├── Context.java              # Application context configuration
│
├── bot/                      # View layer (telegram bot)
│   ├── Bot.java              # Telegram bot entry point
│   ├── BotClient.java        # Telegram bot client
│   ├── BotMenu.java          # Menu
│   ├── BotObjectMapper.java  # Object mapper
│   └── commands/             # Command routing and base abstractions
│       ├── CommandService.java  # Command service
│       ├── Command.java      # Command interface
│       ├── CommandBase.java  # Base command implementation
│       └── impl/             # Command implementations
│           └── [Commands]    # Concrete command implementations
│
├── db/                       # Database layer
│   ├── [DAOs]                # JPA entities (ProductDao, SearchDao, etc.)
│   ├── [Managers]            # Business logic over repositories (ProductManager, etc.)
│   └── [Repositories]        # Spring Data JPA interfaces
│
└── logic/                    # Business layer
    └── [domain models]       # Product, Search, User, SearchStatistic, Store (enum)
```

### Domain, view, DAO separation

Domain objects located in the `logic` package are plain Java classes used throughout the logic layer. 
View layer objects located in the `bot` package are used for telegram bot communication. 
JPA entities located in the `db` package are used for database persistence. Manager classes handle conversion between the three layers.

## Coding Guidelines

- Prioritize clean code, readability, efficiency, and maintainability
- Follow the SOLID and KISS principles
- Follow best practise and design patterns appropriate for the language and framework
- Use early returns when possible
- Always add or modify documentation for public methods and classes when creating new functions and classes or modifying existing ones
- Use AAA pattern and write tests with Arrange, Act, Assert structure

## Coverage requirement

JaCoCo enforces **80% minimum line coverage** per package. CI will fail if this threshold is not met.
