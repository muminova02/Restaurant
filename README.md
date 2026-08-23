# Restaurant Telegram Bot

A Java **Telegram bot** for a restaurant: customers browse the menu, build a
cart, and place orders directly in chat, while an admin can manage the menu.

## Overview

This is a chat-based ordering application built on the Telegram Bot API (not a
web/REST backend). A customer interacts entirely through Telegram:

- browse the menu by category (drinks, salads, main dishes, ice cream);
- add items to a personal cart;
- place an order and review order history;

and an **admin** role can manage menu items. Conversations are driven by
per-user **state machines** (customer / admin / order states), and application
data is held in an in-memory store.

For a recruiter, this repository shows Java application structure, event-driven
bot handling, keyboard/callback UX, and state management — without any web
framework.

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Java 17 |
| Build Tool | Maven |
| Bot Framework | [TelegramBots](https://github.com/rubenlagus/TelegramBots) (long polling) |
| Boilerplate | Lombok |
| Persistence | In-memory (singleton with Java collections) |

> Note: there is **no external database** — data is stored in memory and resets
> when the process stops. No web/REST layer and no Spring framework are used.

## Architecture

The bot receives updates via long polling and dispatches them through handlers
into service classes that mutate the in-memory store:

```text
Telegram Update
      ↓
BotService (TelegramLongPollingBot)
      ↓
CommandHandler / CallbackQueryHandler
      ↓
Service layer (BotLogicService, UserService, AdminService,
               InlineMarkupService, ReplyMarkupService)
      ↓
In-memory store (Db singleton)
```

State is tracked with enums (`UserState`, `AdminState`, `BuyurtmaState`,
`State`) so each chat advances through a defined conversation flow.

## Main Functionality

- Menu browsing by category (drinks / salads / main dishes / ice cream)
- Per-user cart ("savat") management
- Order placement and order history ("buyurtma")
- Admin flow for managing menu items
- Inline and reply keyboards with callback-query handling
- State-machine-driven conversation flow for customers and admin

## Project Structure

```text
src/main/java/org/example/
├── Main.java                 # entrypoint: registers the bot
├── service/                  # bot dispatch, command/callback handlers, UX, admin & user logic
├── entity/                   # User, Meal, MenuType, Buyurtma (order), Xabar (message)
├── enums/                    # UserState, AdminState, BuyurtmaState, State
├── payload/                  # inline string helpers
├── db/                       # Db.java — in-memory singleton store
└── util/                     # helper utilities
src/main/resources/           # menu/meal images
```

## Running Locally

**Requirements:** Java 17, Maven, and a Telegram bot token from
[@BotFather](https://t.me/BotFather).

The bot token is read from the `BOT_TOKEN` environment variable (see
[`.env.example`](.env.example)) — it is **not** hardcoded.

```bash
git clone https://github.com/muminova02/Restaurant.git
cd Restaurant

export BOT_TOKEN=your_telegram_bot_token   # from @BotFather

mvn package
mvn exec:java -Dexec.mainClass=org.example.Main
```

*(Maven has no wrapper committed; use a local Maven installation.)*

## What This Project Demonstrates

- Java 17 application development with Maven
- Integration with an external API (Telegram Bot API) via the TelegramBots library
- Event-driven handling of commands and callback queries
- State-machine-based conversation flows
- Layered separation between dispatch, service logic, and data
- Keyboard/menu UX and in-memory data modelling

## Developer

**Muqaddas Muminova**

GitHub: [https://github.com/muminova02](https://github.com/muminova02)
