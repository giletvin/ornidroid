#!/bin/sh
# script de creation de base de données sqlite a partir du fichier csv des oiseaux. Le separateur du csv est |
#pre requis sqlite3 php5-sqlite
php create_insert_data_sql_file.php
export DATABASE_NAME=ornidroid.jpg
rm $DATABASE_NAME
cat create_tables.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_country.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_bird.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_taxonomy.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_bird_description.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_scientific_order_and_family.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_category.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_habitat_trad.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_remarkable_sign_trad.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_beak_form.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_size.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_colours.sql | sqlite3 $DATABASE_NAME

php add_countries.php
php add_traductions.php
php add_oiseaux_net_links.php
cat generate_insert_data_traductions.sql | sqlite3 $DATABASE_NAME
cat generate_insert_data_bird_country.sql | sqlite3 $DATABASE_NAME
cat generate_update_oiseaux_net_links.sql | sqlite3 $DATABASE_NAME

sqlite3 $DATABASE_NAME 'VACUUM;'










