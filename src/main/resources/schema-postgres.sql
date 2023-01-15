DROP TABLE IF EXISTS clients;
DROP TABLE IF EXISTS sellers;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS offers;
DROP TABLE IF EXISTS purchases;
DROP TABLE IF EXISTS purchase_offers;
CREATE TABLE clients(
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255)
);
CREATE TABLE sellers(
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);
CREATE TABLE products(
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);
CREATE TABLE offers(
  seller_id VARCHAR(255) PRIMARY KEY,
  product_id VARCHAR(255) PRIMARY KEY,
  price_amount FLOAT NOT NULL,
  CONSTRAINT fk_seller FOREIGN KEY(seller_id) REFERENCES sellers(id),
  CONSTRAINT fk_product FOREIGN KEY(product_id) REFERENCES products(id)
);
CREATE TABLE purchases(
  id VARCHAR(255) PRIMARY KEY,
  client_id VARCHAR(255) NOT NULL,
  dateTime DATE NOT NULL,
  CONSTRAINT fk_client FOREIGN KEY(client_id) REFERENCES clients(id)
);
CREATE TABLE purchase_offers(
  purchase_id VARCHAR(255) PRIMARY KEY,
  seller_id VARCHAR(255) NOT NULL,
  product_id VARCHAR(255) NOT NULL,
  price_amount FLOAT NOT NULL,
  quantity INTEGER NOT NULL,
  CONSTRAINT fk_purchase FOREIGN KEY(purchase_id) REFERENCES purchases(id),
  CONSTRAINT fk_seller FOREIGN KEY(seller_id) REFERENCES sellers(id),
  CONSTRAINT fk_product FOREIGN KEY(product_id) REFERENCES products(id)
);