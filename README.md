# Foodie

## Foodie API

### Links
- http://localhost:8080/v3/api-docs/
- http://localhost:8080/v3/api-docs.yaml
- http://localhost:8080/swagger-ui.html

| Verb | Endpoint       | Description                                         |
|------|----------------|-----------------------------------------------------|
| GET  | /recipes        | Get all recipes, paged.                            |
| GET  | /recipes/{id}   | Get recipe with ID.                                |
| GET  | /recipes/digest | Run batch job to populate database with test data. |

### Setting up the database
Install the [MongoDB Community Server](https://www.mongodb.com/download-center/community), and run the following commands in the mongo shell (`mongo --port 27017`) to configure the admin users. After that is done, you may use `mongo --port 27017 --authenticationDatabase foodie -u foodie -p` to connect to the database.

```mongojs
use admin
db.createUser({
	user: "admin",
	pwd: "admin",
	roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
});

use foodie
db.createUser({
	user: 'foodie',
	pwd: 'foodie',
	roles: [ 'readWrite' ]
});
db.adminCommand({ shutdown: 1 });
```

## Foodie Loader
Loads scrapped recipes into a database.

*How to use:* start the API service, then call the endpoint at `http://localhost:10000/recipes/refresh`.

## Foodie Scraper
Scrapes recipes from https://allrecipes.com.

### Prerequisites
* Have Python 3 installed
* `pip install beautifulsoup4 recipe-scrapers`

### Run
* `python get_recipes.py --pages 5000 --sleep 5 --status 1`
* `python get_pictures.py --filename "recipes_raw.json"`

## Docker

Setting up dev environment:
* `docker-compose -p foodie up -d db`
* `docker-compose -p foodie up -d api`

Building and pushing a new API image:
* `gradlew bootJar`
* `docker-compose build`
* `docker image tag foodie:latest sandrohc/foodie:latest`
* `docker push sandrohc/foodie:latest`

Building and testing a local image:
* `docker build -t foodie .`
* `docker run -it --rm foodie`
