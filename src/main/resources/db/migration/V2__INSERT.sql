
INSERT INTO customers (id, name, email, created_at)
VALUES
    (1, 'Илон Маск', 'elon@spacex.com', CURRENT_TIMESTAMP),
    (2, 'Джефф Безос', 'jeff@amazon.com', CURRENT_TIMESTAMP);

INSERT INTO orders (id, customer_id, amount, order_status, created_at)
VALUES

(1, 1, 100.50, 'NEW', CURRENT_TIMESTAMP),

(2, 1, 20000.00, 'PAID', CURRENT_TIMESTAMP),

(3, 2, 5.99, 'CANCELLED', CURRENT_TIMESTAMP),

(4, 2, 99.90, 'NEW', CURRENT_TIMESTAMP);

SELECT setval('customers_id_seq', (SELECT MAX(id) FROM customers));
SELECT setval('orders_id_seq', (SELECT MAX(id) FROM orders));