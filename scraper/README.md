# Foodie Scraper

Scrapes recipes from https://allrecipes.com

## Usage

### Prerequisites

* Have Python 3 installed
* `$ pip install beautifulsoup4 recipe-scrapers`

### Scrape recipe contents
* `$ python get_recipes.py --pages 5000 --sleep 5 --status 1`

### Download pictures
* `$ python get_pictures.py --filename "recipes_raw.json"`
