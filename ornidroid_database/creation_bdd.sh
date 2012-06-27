#!/bin/sh
# script de creation de base de données sqlite a partir du fichier csv des oiseaux. Le separateur du csv est |
php create_insert_data_sql_file.php

rm ornidroid.sqlite
cat create_tables.sql | sqlite3 ornidroid.sqlite
cat insert_data_table_bird.sql | sqlite3 ornidroid.sqlite
cat insert_data_table_taxonomy.sql | sqlite3 ornidroid.sqlite
cat insert_data_table_bird_description.sql | sqlite3 ornidroid.sqlite
cat insert_data_table_scientific_order_and_family.sql | sqlite3 ornidroid.sqlite
php add_traductions.php
touch ornidroid.sqlite.properties







