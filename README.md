# Foodie

## Foodie API

- `http://localhost:10000/api-docs`
- `http://localhost:10000/api-docs.yaml`
- `http://localhost:10000/recipes`


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
