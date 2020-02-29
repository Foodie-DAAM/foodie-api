# Original project:
#  - https://github.com/rtlee9/recipe-box
#
# Recipe sites:
#  - Food Network (http://www.foodnetwork.com)
#  - Epicurious (http://www.epicurious.com)
#  - All Recipes (https://allrecipes.com)

import json
import time
import sys
import re
import argparse
from os import path
from urllib import request
from urllib.error import HTTPError, URLError
from datetime import datetime
from bs4 import BeautifulSoup
from multiprocessing import Pool, cpu_count
from recipe_scrapers import scrape_me

import config

HEADERS = {
    'User-Agent': 'Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.0.7) Gecko/2009021910 Firefox/3.0.7'
}

def get_recipe(url):
    try:
        print('Scraping %s' % url)
        scrap = scrape_me(url)
    except:
        print('Could not scrape URL {}'.format(url))
        return {}

    try:
        title = scrap.title()
    except AttributeError:
        title = None

    try:
        ingredients = scrap.ingredients()
    except AttributeError:
        ingredients = None

    try:
        instructions = scrap.instructions().split('\n')
    except AttributeError:
        instructions = None

    try:
        total_time = scrap.total_time()
    except AttributeError:
        total_time = None

    try:
        yields = scrap.yields()
    except AttributeError:
        yields = None

    try:
        picture = scrap.picture()
    except AttributeError:
        picture = scrap.soup.select_one('img.rec-photo')
        if picture:
            picture = picture['src']
        
        if not picture:
            picture = scrap.soup.select_one('.image-container .lazy-image')
            if picture:
                picture = picture['data-src']
            else:
                picture = None

    # description
    description = scrap.soup.select_one('.recipe-summary > p')
    if not description:
        description = scrap.soup.select_one('.submitter__description')

    if description:
        description = description.get_text()
    else:
        description = None


    # nutrition facts
    nutrition_facts_soup = scrap.soup.select_one('.nutrition-summary-facts')
    if not nutrition_facts_soup:
        nutrition_facts_soup = scrap.soup.select_one('.recipe-nutrition-section')

    if nutrition_facts_soup:
        nutrition_facts = nutrition_facts_soup.get_text()
    else:
        nutrition_facts = None

#    if not nutrition_facts_soup:
#        nutrition_facts = None
#    else:
#        nutrition_facts = []
#        nutrition_facts_soup = nutrition_facts_soup.get_text().strip()
#        
#        # remove duplicate whitespaces
#        nutrition_facts_soup = re.sub(r' {2,}', ' ', nutrition_facts_soup)
#
#        # remove idiosyncrasy where '.' is used as a separator
#        nutrition_facts_soup = re.sub(r'\. ', '\n', nutrition_facts_soup)
#
#        # Split on: \n ;
#        nutrition_facts_soup = re.split("[\n;]+", nutrition_facts_soup)
#
#        if len(nutrition_facts_soup) == 1:
#            nutrition_facts.append(nutrition_facts_soup[0])
#        else:
#            for val in nutrition_facts_soup:
#                if not val or 'Per Serving' in val or 'Full nutrition' in val:
#                    continue
#
#                # remove last char, if not a letter
#                val = val if val[-1].isalpha() else val[:-1]
#
#                nutrition_facts.append(val)

    return {
        'url': url,
        'title': title,
        'description': description,
        'ingredients': ingredients,
        'instructions': instructions,
        'total_time': total_time,
        'yields': yields,
        'picture': picture,
        'nutrition_facts': nutrition_facts
    }


def get_all_recipes(page_num):
    base_url = 'https://allrecipes.com'
    search_url_str = 'recipes/?page'
    url = '{}/{}={}'.format(base_url, search_url_str, page_num)

    try:
        soup = BeautifulSoup(request.urlopen(request.Request(url, headers=HEADERS)).read(), "html.parser")
        recipe_link_items = soup.select('article.fixed-recipe-card > .fixed-recipe-card__info > a')
        recipe_links = list(set([r['href'] for r in recipe_link_items if r is not None and '/recipe/' in r['href']]))
        return [
            get_recipe(r) for r in recipe_links
        ]
    except (HTTPError, URLError):
        print('Could not parse page {}'.format(url))
        return []


def scrape_recipe_box(scraper, page_iter):
    timestamp = generate_timestamp()
    recipes = []

    start = time.time()
    if args.multi:
        pool = Pool(cpu_count() * 2)
        results = pool.map(scraper, page_iter)
        for r in results:
            recipes.extend(r)
    else:
        for i in page_iter:
            recipes.extend(scraper(i))

            print('Scraping page %d of %d' % (i, max(page_iter)))
            quick_save(recipes, timestamp)

            # change file every 100 pages
            if i % 100 == 0:
                recipes = []
                timestamp = generate_timestamp()
            
            time.sleep(args.sleep)

    print('Scraped %d recipes in %.0f minutes' % (len(recipes), (time.time() - start) / 60))
    quick_save(recipes, timestamp)


def quick_save(recipes, timestamp):
    filename = path.join(config.path_data, 'recipes_raw_%s.json' % timestamp)
    save_recipes(filename, recipes)

def save_recipes(filename, recipes):
    with open(filename, 'w') as f:
        json.dump(recipes, f, indent=2)

def generate_timestamp():
    # https://www.w3resource.com/python-exercises/python-basic-exercise-3.php
    return datetime.now().strftime("%Y-%m-%d_%H-%M")

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--multi',  action='store_true',    help='Multi threading')
    parser.add_argument('--start',  type=int, default=1,    help='Start page')
    parser.add_argument('--pages',  type=int, default=3000, help='Number of pages to scrape')
    parser.add_argument('--sleep',  type=int, default=0,    help='Seconds to wait before scraping next page')
    args = parser.parse_args()

    page_iter = range(args.start, args.pages + args.start)
    scrape_recipe_box(get_all_recipes, page_iter)
