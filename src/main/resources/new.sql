CREATE TABLE budget(
                       budget_id INT AUTO_INCREMENT,
                       amount DECIMAL(15,2)   NOT NULL,
                       customer_id INT unsigned NOT NULL,
                       created_at datetime,
                       user_id int,
                       PRIMARY KEY(budget_id),
                       FOREIGN KEY(customer_id) REFERENCES customer(customer_id),
                       FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE alert_rate(
                            id INT AUTO_INCREMENT,
                            percentage DECIMAL(5,2)   NOT NULL,
                            PRIMARY KEY(id)
);