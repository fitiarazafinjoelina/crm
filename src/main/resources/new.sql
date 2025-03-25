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

CREATE TABLE ticket_lead_import(
    id INT AUTO_INCREMENT PRIMARY KEY ,
    customer_email VARCHAR(100) NOT NULL ,
    subject_or_name	VARCHAR(250),
    type VARCHAR(10) check (
        type =
        'lead' or
        type =
        'ticket'
        ) NOT NULL ,
    status VARCHAR(100) CHECK (
        status =
        'meeting-to-schedule' or
        status = 'scheduled' or
        status = 'archived' or
        status = 'success' or
        status ='assign-to-sales' or
        status ='open' or
        status ='assigned' or
        status ='on-hold' or
        status ='in-progress' or
        status ='resolved' or
        status ='closed' or
        status ='reopened' or
        status ='pending-customer-response' or
        status = 'escalated' or
        status = 'archived'
        ) NOT NULL,
    expense decimal(16,2) check ( expense > 0 ) NOT NULL
);

CREATE TABLE customer_import(
    id INT AUTO_INCREMENT PRIMARY KEY ,
    customer_email VARCHAR(100) NOT NULL,
    customer_name VARCHAR(200) NOT NULL
);

CREATE TABLE budget_import(
    id int AUTO_INCREMENT PRIMARY KEY ,
    customer_email varchar(100) UNIQUE NOT NULL ,
    budget decimal(16,2) check ( budget > 0 ) NOT NULL
);

insert into budget_import  (customer_email,budget) values('raz@yahoo.com',-2);