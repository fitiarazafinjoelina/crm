#!/bin/bash

# MySQL credentials
MYSQL_USER="root"
MYSQL_PASS="mimimavo"
MYSQL_DB="crm"

# Function to generate a random string of specified length
generate_random_string() {
    length=$1
    tr -dc A-Za-z0-9 </dev/urandom | head -c "$length"
}

# Function to generate a random token (UUID format)
generate_token() {
    uuidgen
}

# Function to insert random data into the table
insert_data() {
    id=$1
    username=$2
    password=$3
    token=$4
    password_set=$5

    # Insert the data into the database with the explicit ID
    sudo mysql -u "$MYSQL_USER" -p"$MYSQL_PASS" -D "$MYSQL_DB" -e "
        INSERT INTO customer_login_info (id, username, password, token, password_set)
        VALUES ($id, '$username', '$password', '$token', $password_set);
    "

    # Optionally print a message with the inserted ID
    echo "Inserted record: ID=$id, Username=$username, Token=$token"
}

# Starting ID (the first ID to insert)
START_ID=1

# Number of records to generate
NUM_RECORDS=4

# Loop to generate random records and insert them
for ((i=0; i<NUM_RECORDS; i++))
do
    # Calculate the ID for the current record
    id=$((START_ID + i))

    # Generate random username, password, and token
    username=$(generate_random_string 8)
    password=$(generate_random_string 12)
    token=$(generate_token)
    password_set=$((RANDOM % 2))  # Random 0 or 1 for password_set

    # Insert the data with the generated ID
    insert_data "$id" "$username" "$password" "$token" "$password_set"

done

echo "Data insertion complete."
