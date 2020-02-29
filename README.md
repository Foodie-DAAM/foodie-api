# Foodie

## Foodie Scraper
Scrapes recipes from https://allrecipes.com.

### Prerequisites
* Have Python 3 installed
* `$ pip install beautifulsoup4 recipe-scrapers`

### Run
* `$ python get_recipes.py --pages 5000 --sleep 5 --status 1`
* `$ python get_pictures.py --filename "recipes_raw.json"`

## Foodie Loader
Loads scrapped recipes into a database. 

### Prerequisites
* Have a PostgreSQL DB running.