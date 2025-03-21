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
    username=$1
    password=$2
    token=$3
    password_set=$4

    sudo mysql -u "$MYSQL_USER" -p"$MYSQL_PASS" -D "$MYSQL_DB" -e "
        INSERT INTO customer_login_info (username, password, token, password_set)
        VALUES ('$username', '$password', '$token', $password_set);
    "
}

# Number of records to generate
NUM_RECORDS=2

# Loop to generate random records
for ((i=1; i<=NUM_RECORDS; i++))
do
    # Generate random username, password, and token
    username=$(generate_random_string 8)
    password=$(generate_random_string 12)
    token=$(generate_token)
    password_set=$((RANDOM % 2))  # Random 0 or 1 for password_set

    # Insert the data into the database
    insert_data "$username" "$password" "$token" "$password_set"

    # Optionally, print a message
    echo "Inserted record $i: Username=$username, Token=$token"
done

echo "Data insertion complete."
