#!/bin/sh
# script de creation de base de données sqlite a partir du fichier csv des oiseaux. Le separateur du csv est |
php create_insert_data_sql_file.php
export DATABASE_NAME=ornidroid.jpg
rm $DATABASE_NAME
cat create_tables.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_bird.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_taxonomy.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_bird_description.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_scientific_order_and_family.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_category.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_habitat.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_beak_form.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_size.sql | sqlite3 $DATABASE_NAME
cat insert_data_table_colours.sql | sqlite3 $DATABASE_NAME
php add_traductions.php
#generation du fichier .size pour le controle de mise a jour dans l appli
cat $DATABASE_NAME | wc -c > $DATABASE_NAME.size









