# 🧾 Sales Management System

## 📌 Project Overview
This project is a **Java-based Sales Management System** developed for academic purposes.  
It simulates a small retail environment where users can:
- Log in using stored credentials
- Manage products and inventory
- Record sales transactions
- Generate weekly and monthly sales reports
- Perform point-of-sale operations through a graphical interface

This repository also includes a structured testing effort (static + dynamic testing) as part of the course requirements.

---

## ▶️ How to Open and Run the Project

### Requirements
- **Java JDK 8+**
- **JavaFX** (needed for the GUI)
- **Eclipse IDE** (recommended)

### Steps
1. Clone the repository:
   git clone https://github.com/albimoku-svg/ariprotest.git

2. Open the project in Eclipse:
   - File → Open Projects from File System
   - Select the cloned folder

3. If you use **Java 11+**, configure JavaFX (add JavaFX SDK and VM options).

4. Run the application from the main entry class (example):
   - `LoginApplication`

---

## 🧪 Testing (Short Overview)

### Static Testing
- Static analysis was performed using tools such as **SpotBugs** and **SonarLint**.
- The analysis reported warnings/informational messages (e.g., unused imports, dead stores, type-safety notes), which were reviewed and documented.

### Dynamic Testing
JUnit 5 tests were implemented at different levels:
- **Unit Tests**: verify individual methods in isolation (no UI / no file system).
- **Integration Tests**: verify interactions between components (e.g., SalesReportManager with file loading and report aggregation).
- **System Tests**: validate end-to-end scenarios through the application workflow.

> Note: Some reporting features work correctly at logic level (confirmed by tests), but due to JavaFX UI limitations the reports may not be fully visible in the interface; this is documented as a system-level issue.

---
