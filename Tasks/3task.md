# Data Management


## SQLite

[SQLite](https://www.sqlite.org) is a software library that implements a self-contained, serverless, SQL database engine.
It is used extensively, and it ships with the Android operating system.
Furthermore, the source code for SQLite is in the public domain.
Unlike most SQL databases, SQLite does not require a separate server process; it reads and writes directly to disk files.
There is a C/C++ interface to SQLite, namely \texttt{sqlite3}.

### Action Items

* __Read__: [SQLite Quick Start Guide](https://www.sqlite.org/quickstart.html).


## Java Database Connectivity

The [Java Database Connectivity](https://www.oracle.com/technetwork/java/javase/jdbc/index.html) (JDBC) API is the industry standard for database-independent connectivity between Java applications and a wide range of databases.
The JDBC API provides a call-level API for SQL-based database access.

### Action Items

* __Read__:
Tutorial on JDBC
* __Program__:
Java application that adds and edits entries in a SQLite database.
* __Showcase__:
Database content using a GUI.
* Option: Repeat these steps on [Android](https://developer.android.com/) phone using [SQLiteOpenHelper](https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html) and [SQLiteDatabase](https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html).
