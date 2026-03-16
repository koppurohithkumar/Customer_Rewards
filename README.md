✨ Features

Reward points calculation based on transaction amount
Monthly aggregation (YYYY-MM)
Total reward points calculation
Query by date range (fromDate, toDate)
Per‑customer and all‑customer summaries
Custom exceptions for invalid dates and missing customers
H2 in‑memory database with seed data (20 customers, 50 transactions)
Unit tests using JUnit 5, Mockito, and MockMvc


🧮 Reward Calculation Rules
Based on purchase amount:

<= 50 → 0 points
51–100 → 1 point per dollar above 50
> 100 →

2 points per dollar above 100
+50 points for the 51–100 tier



Examples:

120 → (120−100) * 2 + 50 = 90 points
75 → (75−50) * 1 = 25 points


🧱 Tech Stack

Java 21
Spring Boot 3.5.x
Spring Web / Spring Data JPA / Validation
H2 Database
Maven
JUnit 5, Mockito, MockMvc


📁 Project Structure
src/main/java/com/example/customer_rewards
 ├── controller
 ├── service
 ├── repository
 ├── model
 ├── dto
 ├── exception
 └── util


🚀 Running the Project
Run (Dev)
mvn spring-boot:run

Build & Run
mvn clean package
java -jar target/customer-rewards-0.0.1-SNAPSHOT.jar

H2 Console
http://localhost:8080/h2-console

JDBC URL:
jdbc:h2:mem:testdb


🌐 REST API Endpoints

Your controller currently uses root paths, not /api/....

All Customers
🔹 No date range
GET http://localhost:8080/

🔹 With date range
GET http://localhost:8080/?fromDate=2025-01-01&toDate=2025-03-31

🔹 Only from
GET http://localhost:8080/?fromDate=2025-01-15

🔹 Only to
GET http://localhost:8080/?toDate=2025-02-20

❌ Invalid date range (400)
GET http://localhost:8080/?fromDate=2025-03-31&toDate=2025-01-01


Single Customer
🔹 No date range
GET http://localhost:8080/1

🔹 With date range
GET http://localhost:8080/1?fromDate=2025-01-01&toDate=2025-02-28

❌ Customer not found (404)
GET http://localhost:8080/9999

❌ Invalid date range (400)
GET http://localhost:8080/1?fromDate=2025-03-31&toDate=2025-01-01


🧪 Testing
✔ Unit Tests for Service
Covers:

reward calculation
monthly grouping
date range validation
no transaction data
customer not found

✔ Controller Tests (Slice Testing)

@WebMvcTest(TransactionController.class)
@MockitoBean for service mocking
Tests all HTTP paths and responses

Run:
mvn test


🗄 Database Seed (H2)
Your data.sql loads:

20 customers
50 purchase transactions

This data is auto‑loaded at startup.

🛠 Utility
CalculateRewardPoints contains the logic for calculating reward points.

❗ Error Handling

400 BAD REQUEST → invalid date format or fromDate > toDate
404 NOT FOUND → customer not found
404 NOT FOUND → no transactions in database


🔮 Future Enhancements

Pagination for all-customers endpoint
JWT authentication
Export reports (CSV/PDF)
UI dashboard (React/Angular)
Caching for high traffic

CUSTOMER
INSERT INTO customer (customer_id, name) VALUES (1,  'Alice Johnson');
INSERT INTO customer (customer_id, name) VALUES (2,  'Bob Smith');
INSERT INTO customer (customer_id, name) VALUES (3,  'Carol Nguyen');
INSERT INTO customer (customer_id, name) VALUES (4,  'David Patel');
INSERT INTO customer (customer_id, name) VALUES (5,  'Eva Martinez');
INSERT INTO customer (customer_id, name) VALUES (6,  'Frank Zhou');
INSERT INTO customer (customer_id, name) VALUES (7,  'Grace Lee');
INSERT INTO customer (customer_id, name) VALUES (8,  'Hannah Kim');
INSERT INTO customer (customer_id, name) VALUES (9,  'Ian Brown');
INSERT INTO customer (customer_id, name) VALUES (10, 'Jasmine Clark');
INSERT INTO customer (customer_id, name) VALUES (11, 'Kevin Wilson');
INSERT INTO customer (customer_id, name) VALUES (12, 'Linda Garcia');
INSERT INTO customer (customer_id, name) VALUES (13, 'Mohit Sharma');
INSERT INTO customer (customer_id, name) VALUES (14, 'Nora Adams');
INSERT INTO customer (customer_id, name) VALUES (15, 'Omar Ali');
INSERT INTO customer (customer_id, name) VALUES (16, 'Priya Desai');
INSERT INTO customer (customer_id, name) VALUES (17, 'Quentin Miles');
INSERT INTO customer (customer_id, name) VALUES (18, 'Riya Kapoor');
INSERT INTO customer (customer_id, name) VALUES (19, 'Samir Khan');
INSERT INTO customer (customer_id, name) VALUES (20, 'Tanya Verma');

PURCHASE TRANSACTION
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (1,  1, 120, TIMESTAMP '2025-01-05 10:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (2,  1,  60, TIMESTAMP '2025-02-10 12:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (3,  2,  75, TIMESTAMP '2025-03-10 16:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (4,  3,  45, TIMESTAMP '2025-01-12 09:15:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (5,  3, 110, TIMESTAMP '2025-02-18 14:40:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (6,  4, 200, TIMESTAMP '2025-01-20 18:05:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (7,  4,  52, TIMESTAMP '2025-03-01 11:30:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (8,  5,  99, TIMESTAMP '2025-02-03 08:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (9,  5, 130, TIMESTAMP '2025-03-28 19:45:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (10, 6,  51, TIMESTAMP '2025-01-02 13:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (11, 6,  49, TIMESTAMP '2025-01-25 10:30:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (12, 6, 101, TIMESTAMP '2025-02-20 17:20:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (13, 7,  88, TIMESTAMP '2025-01-30 09:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (14, 7, 160, TIMESTAMP '2025-03-15 15:15:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (15, 8,  72, TIMESTAMP '2025-02-07 12:10:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (16, 8,  55, TIMESTAMP '2025-02-28 08:25:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (17, 9, 140, TIMESTAMP '2025-01-08 18:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (18, 10, 65, TIMESTAMP '2025-03-22 10:10:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (19, 10, 95, TIMESTAMP '2025-03-29 21:30:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (20, 11, 115, TIMESTAMP '2025-02-11 07:45:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (21, 11,  58, TIMESTAMP '2025-02-12 13:05:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (22, 12,  47, TIMESTAMP '2025-01-03 09:05:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (23, 12, 102, TIMESTAMP '2025-01-18 20:20:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (24, 13,  50, TIMESTAMP '2025-03-02 14:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (25, 13, 200, TIMESTAMP '2025-03-25 18:30:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (26, 14,  77, TIMESTAMP '2025-01-27 16:45:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (27, 14,  90, TIMESTAMP '2025-02-14 10:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (28, 15, 123, TIMESTAMP '2025-01-09 11:25:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (29, 15,  59, TIMESTAMP '2025-01-31 17:50:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (30, 16,  95, TIMESTAMP '2025-02-05 09:35:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (31, 16, 105, TIMESTAMP '2025-02-25 19:10:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (32, 17,  53, TIMESTAMP '2025-01-06 08:40:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (33, 17,  89, TIMESTAMP '2025-03-08 20:05:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (34, 17, 150, TIMESTAMP '2025-03-18 12:15:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (35, 18,  70, TIMESTAMP '2025-02-02 10:20:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (36, 18, 115, TIMESTAMP '2025-03-12 15:55:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (37, 19,  35, TIMESTAMP '2025-01-04 07:30:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (38, 19, 108, TIMESTAMP '2025-01-22 18:10:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (39, 19,  99, TIMESTAMP '2025-02-16 09:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (40, 20,  80, TIMESTAMP '2025-03-03 13:45:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (41, 20, 135, TIMESTAMP '2025-03-20 21:00:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (42,  2, 155, TIMESTAMP '2025-02-21 10:10:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (43,  5,  62, TIMESTAMP '2025-01-28 08:05:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (44,  7,  49, TIMESTAMP '2025-02-09 19:20:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (45, 10, 140, TIMESTAMP '2025-01-17 12:12:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (46, 12,  76, TIMESTAMP '2025-03-06 11:11:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (47, 13,  88, TIMESTAMP '2025-02-22 17:17:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (48, 16, 180, TIMESTAMP '2025-03-24 20:20:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (49, 18,  52, TIMESTAMP '2025-01-13 06:45:00');
INSERT INTO purchase_transaction (transaction_id, customer_id, amount, local_date_time) VALUES (50, 20, 100, TIMESTAMP '2025-02-27 09:55:00');

API Testing
##############################
# CURRENT CONTROLLER (NO BASE PATH)
# Paths: "/" and "/{customerId}"
##############################

# All customers — no dates
GET http://localhost:8080/

# All customers — valid date ranges
GET http://localhost:8080/?fromDate=2025-01-01&toDate=2025-03-31
GET http://localhost:8080/?fromDate=2025-02-10&toDate=2025-02-10   # same day
GET http://localhost:8080/?fromDate=2025-01-15                     # only from
GET http://localhost:8080/?toDate=2025-02-20                       # only to

# All customers — edge/invalid
GET http://localhost:8080/?fromDate=2025-03-31&toDate=2025-01-01   # invalid bounds -> 400
GET http://localhost:8080/?fromDate=2024-02-29&toDate=2024-02-29   # leap day
GET http://localhost:8080/?fromDate=02-10-2025&toDate=2025/02/10   # bad format -> 400

# Single customer — no dates
GET http://localhost:8080/1

# Single customer — valid date ranges
GET http://localhost:8080/1?fromDate=2025-01-01&toDate=2025-02-28
GET http://localhost:8080/1?fromDate=2025-02-10&toDate=2025-02-10  # same day
GET http://localhost:8080/1?fromDate=2025-01-15                    # only from
GET http://localhost:8080/1?toDate=2025-02-20                      # only to

# Single customer — edge/invalid
GET http://localhost:8080/1?fromDate=2025-03-31&toDate=2025-01-01  # invalid bounds -> 400
GET http://localhost:8080/9999                                      # not found -> 404
GET http://localhost:8080/1?fromDate=02-10-2025&toDate=2025/02/10   # bad format -> 400


##############################
# ALTERNATE CONTROLLER (WITH BASE PATH)
# If you switch to: @RequestMapping("/api/rewards")
# Paths: "/api/rewards/all" and "/api/rewards/customer/{customerId}"
##############################

# All customers — no dates
GET http://localhost:8080/api/rewards/all

# All customers — valid date ranges
GET http://localhost:8080/api/rewards/all?fromDate=2025-01-01&toDate=2025-03-31
GET http://localhost:8080/api/rewards/all?fromDate=2025-02-10&toDate=2025-02-10  # same day
GET http://localhost:8080/api/rewards/all?fromDate=2025-01-15                     # only from
GET http://localhost:8080/api/rewards/all?toDate=2025-02-20                       # only to

# All customers — edge/invalid
GET http://localhost:8080/api/rewards/all?fromDate=2025-03-31&toDate=2025-01-01   # invalid bounds -> 400
GET http://localhost:8080/api/rewards/all?fromDate=02-10-2025&toDate=2025/02/10   # bad format -> 400

# Single customer — no dates
GET http://localhost:8080/api/rewards/customer/1

# Single customer — valid date ranges
GET http://localhost:8080/api/rewards/customer/1?fromDate=2025-01-01&toDate=2025-02-28
GET http://localhost:8080/api/rewards/customer/1?fromDate=2025-02-10&toDate=2025-02-10  # same day
GET http://localhost:8080/api/rewards/customer/1?fromDate=2025-01-15                    # only from
GET http://localhost:8080/api/rewards/customer/1?toDate=2025-02-20                      # only to

# Single customer — edge/invalid
GET http://localhost:8080/api/rewards/customer/1?fromDate=2025-03-31&toDate=2025-01-01  # invalid bounds -> 400
GET http://localhost:8080/api/rewards/customer/9999                                      # not found -> 404
GET http://localhost:8080/api/rewards/customer/1?fromDate=02-10-2025&toDate=2025/02/10   # bad format -> 400
