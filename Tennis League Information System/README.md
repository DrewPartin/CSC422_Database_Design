# Tennis League Information System (Console + JDBC)

A Java console application that connects to a MySQL database (TennisLeague) using JDBC and provides menu-driven access to view and manage Teams, Players, and Coaches.

## Requirements
- Java (JDK 17+ recommended)
- MySQL Server 8+
- MySQL Connector/J (included in `lib/`)
- VS Code (recommended) with Java extensions (optional)

## Folder Structure
```
src/
└── app/
    ├── App.java            # Console UI (menus)
    ├── dao/                # JDBC data access objects (SQL lives here)
    │   ├── TeamDAO.java
    │   ├── PlayerDAO.java
    │   └── CoachDAO.java
    ├── model/              # Model classes (Team / Player / Coach)
    │   ├── Team.java
    │   ├── Player.java
    │   └── Coach.java
    └── util/               # Shared utilities
    └── DatabaseUtil.java   # Reads db.properties and returns JDBC Connection

lib/
└── mysql-connector-j-*.jar

sql/
└── Tennis League Seed Data.sql

db.properties.example
.gitignore
README.md
```

## Setup
### 1. Create the database and seed data
Run the provided SQL script in MySQL to create the `TennisLeague` database and tables with seed data.

### 2. Configure database connection
> Note: This project uses a local config file 'db.properties' that is not committed to Git.
1. Copy:
   - `db.properties.example`
2. Rename the copy to:
   - `db.properties`
3. Update `db.user` and `db.password` with your MySQL credentials.

Example:
```properties
db.url=jdbc:mysql://localhost:3306/TennisLeague?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=YOUR_USERNAME
db.password=YOUR_PASSWORD
```