
CREATE TABLE order_user_details_mapping (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id VARCHAR(50) NOT NULL,
  request_id VARCHAR(50) NOT NULL,
  amount DOUBLE NOT NULL,
  address_id VARCHAR(50) NOT NULL,
  user_id VARCHAR(50) NOT NULL,
  KEY 'idx_request_id' ('request_id'),
  KEY 'idx_user_id' ('user_id'));


CREATE TABLE order_products_mapping (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id VARCHAR(100) NOT NULL,
  product_id VARCHAR(100) NOT NULL,
  product_status VARCHAR(50) NOT NULL,
   KEY 'idx_order_id' ('order_id'),
);
