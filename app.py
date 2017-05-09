from flask import Flask
from flask import render_template
import mysql.connector
from mysql.connector import Error
import csv

app = Flask(__name__)

def get_book_ratings_csv():
    csv_path = './static/book-rating-csv/part-00000'
    csv_file = open(csv_path, 'rb')
    csv_obj = csv.DictReader(csv_file)
    csv_list = list(csv_obj)
    return csv_list

def get_electronics_ratings_csv():
    csv_path = './static/electronics-rating-csv/part-00000'
    csv_file = open(csv_path, 'rb')
    csv_obj = csv.DictReader(csv_file)
    csv_list = list(csv_obj)
    return csv_list

def get_cell_ratings_csv():
    csv_path = './static/Cell_Phones_and_Accessories-rating-csv/part-00000'
    csv_file = open(csv_path, 'rb')
    csv_obj = csv.DictReader(csv_file)
    csv_list = list(csv_obj)
    return csv_list

def get_clothing_ratings_csv():
    csv_path = './static/Clothing_Shoes_and_Jewelry-rating-csv/part-00000'
    csv_file = open(csv_path, 'rb')
    csv_obj = csv.DictReader(csv_file)
    csv_list = list(csv_obj)
    return csv_list

def get_office_ratings_csv():
    csv_path = './static/Office_Products-rating-csv/part-00000'
    csv_file = open(csv_path, 'rb')
    csv_obj = csv.DictReader(csv_file)
    csv_list = list(csv_obj)
    return csv_list

@app.route("/books/")
def books():
    template = 'index.html'
    ratings_object_list = get_book_ratings_csv()
    return render_template(template, ratings_object_list=ratings_object_list)

@app.route("/electronics/")
def electronics():
    template = 'electronics.html'
    ratings_object_list = get_electronics_ratings_csv()
    return render_template(template, ratings_object_list=ratings_object_list)

@app.route("/cell/")
def cell():
    template = 'cell.html'
    ratings_object_list = get_cell_ratings_csv()
    return render_template(template, ratings_object_list=ratings_object_list)

@app.route("/clothing/")
def clothing():
    template = 'clothing.html'
    ratings_object_list = get_clothing_ratings_csv()
    return render_template(template, ratings_object_list=ratings_object_list)

@app.route("/office/")
def office():
    template = 'office.html'
    ratings_object_list = get_office_ratings_csv()
    return render_template(template, ratings_object_list=ratings_object_list)

if __name__ == '__main__':
    app.run(debug=True, use_reloader=True)
