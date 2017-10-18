/*
1. Start with Northwind.Customers. (I would provide the DDL and some sample data)
2. Create a new table Addresses that is of the form
Addresses(id, street_name, city, region, postal_code, country), where id will be a unique integer. region may be NULL. All other columns are not NULL. The combination of (street_name, city, region, postal_code, country) must be a UNIQUE KEY.
3. Load Addresses with the fields Address, City, Region, PostalCode and Country from Customers.
4. Create a table new_customers with the same schema as Customers.
5. Load new_customers with the data from Customers.
6. Create a new column address_id on new_customers that is an int.
7. For each row in new_customers, set the value of address_id to the matching Address tuple.
8. Drop the address columns, except address_id from new customers.
9. Add a foreign key constraint from address_id on new_customers to id on Addresses */

/* Create another table just for addresses with a primary key id */
DROP TABLE Addresses;
CREATE TABLE Addresses(
	id INT(10) NOT NULL AUTO_INCREMENT ,
    street_name VARCHAR(80) NOT NULL,
    city VARCHAR(20) NOT NULL,
    region VARCHAR(20) DEFAULT NULL,
    postal_code VARCHAR(20) DEFAULT NULL,
    country VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (street_name, city, region, postal_code, country)
);

/* Load the new table with data from customers */
INSERT INTO Addresses (street_name, city, region, postal_code, country) 
	SELECT DISTINCT Address, City, Region, PostalCode, Country
FROM northwind.customers;

/* Create a new table to modify address related columns to an id in Addresses table */
CREATE TABLE new_customers LIKE Customers;	/* Like with include constraints as well */

INSERT INTO new_customers SELECT * FROM Customers;

/* Add a new column called address_id */
ALTER TABLE new_customers
ADD COLUMN address_id int;

UPDATE new_customers SET address_id = 
	(SELECT id FROM Addresses
	WHERE Addresses.street_name = new_customers.address 
					AND ((Addresses.region = new_customers.region) OR ((Addresses.region IS NULL) AND (new_customers.region IS NULL)))
                    AND (Addresses.postal_code = new_customers.PostalCode)
                    AND (Addresses.country = new_customers.Country));
                    
ALTER TABLE new_customers 
DROP COLUMN Address,
DROP COLUMN City,
DROP COLUMN Region,
DROP COLUMN PostalCode,
DROP COLUMN Country,
ADD CONSTRAINT `fk_address_id` FOREIGN KEY(address_id) REFERENCES Addresses(id);
