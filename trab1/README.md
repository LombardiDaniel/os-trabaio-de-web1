# AA1

To run the app:

```sh
docker compose up --build
```

then access [http://localhost:8000/](http://localhost:8000/), set:

```
System: PostgreSQL
Server: db
Username: user
Password: password
Database: db
```

and execute [scripts/init.sql](./scripts/init.sql) and [scripts/load.sql](./scripts/load.sql)
to initialize the db.

Then simply access [http://localhost:8080/](http://localhost:8080/).

There are 2 users already created `admin@patos.dev` and `lombardi@patos.dev`, both their passwords are `admin`.
