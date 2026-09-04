# 🏨 Hotel Management System – Android App

An Android-based **Hotel Management System** developed using **Java and Android Studio**.

The application is designed to simplify basic hotel/restaurant operations such as menu management, billing, cart management, bill history, bill details and bill printing.

The project uses a **local SQLite database** for storing login, menu and billing-related data.

---

## 📱 Project Overview

The **Hotel Management System** is an Android application developed to reduce manual work in hotel and restaurant billing operations.

The application provides an admin-based interface where the user can:

- Login to the application
- Manage menu items
- Search dishes
- Create customer bills
- Add selected dishes to a cart
- Calculate item and total amounts
- Save generated bills
- View previous bills
- View complete bill details
- Delete bills with confirmation
- Print bills using the Android Print Framework

The application uses **SQLite** as the local database, so no external database server is required.

---

## ✨ Features

### 🔐 Login
- Admin login with SQLite authentication.

### 🏠 Dashboard
- Central dashboard with quick navigation.

### 🍽️ Menu Management
- Add, search, update and delete dishes.
- Store dish details in SQLite.

### 🧾 Billing
- Enter customer details.
- Select dishes, set quantity and calculate total.
- Save generated bills.

### 🛒 Cart
- Manage selected items and quantities.
- Calculate total amount.

### 📋 Bill History
- View saved bills and bill details.

### 🗑️ Delete Bill
- Delete bills with confirmation.

### 🖨️ Print Bill
- Print bills using Android Print Framework.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java | Application development |
| Android Studio | Development environment |
| XML | User interface design |
| SQLite | Local data storage |
| Android SDK | Android application development |
| RecyclerView | Displaying lists of items and bills |
| Android Print Framework | Printing bills |
| Gradle | Build and dependency management |

---

## 🗂️ Project Structure

```text
Android-Hotel-Management-System/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/example/hotelmanagementsystem/
│           │       │
│           │       ├── LoginActivity.java
│           │       ├── DashboardActivity.java
│           │       ├── MenuActivity.java
│           │       ├── BillingActivity.java
│           │       ├── BillHistoryActivity.java
│           │       ├── BillDetailsActivity.java
│           │       ├── BillPrintAdapter.java
│           │       ├── BillAdapter.java
│           │       ├── DatabaseHelper.java
│           │       └── SystemBarHelper.java
│           │
│           ├── res/
│           │   ├── drawable/
│           │   ├── layout/
│           │   ├── mipmap/
│           │   └── values/
│           │
│           └── AndroidManifest.xml
│
├── screenshots/
│   ├── login.png
│   ├── dashboard.png
│   ├── menu-management.png
│   ├── add-dish.png
│   ├── billing.png
│   ├── cart.png
│   ├── saved-bill.png
│   ├── bill-history.png
│   ├── bill-details.png
│   ├── delete-confirmation.png
│   └── print-bill.png
│
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
├── .gitignore
└── README.md
````

---

## 🔄 Application Workflow

```text
Admin Login
     ↓
Dashboard
     ↓
 ┌───────────────┬───────────────┐
 ↓               ↓
Menu          Billing
Management       ↓
 ↓           Select Dishes
Add / Edit /     ↓
Delete /      Add to Cart
Search            ↓
              Calculate Total
                   ↓
                Save Bill
                   ↓
              Bill History
                   ↓
              Bill Details
              ↙          ↘
          Print          Delete
```

---

## 💾 Database

The application uses **SQLite** for local data storage.

The `DatabaseHelper.java` class is responsible for managing the SQLite database and performing database operations.

SQLite is used to store application data such as:

* Admin login information
* Menu/dish information
* Bill information
* Bill items

### Advantages of using SQLite

* Local database
* No separate database server required
* Easy to integrate with Android
* Suitable for small applications
* Can work without continuous internet connectivity


---

# 📸 Screenshots

The following screenshots show the main features and working of the application.

## 🔐 Login Screen

<img src="screenshots/login.png" alt="Login Screen" width="300">

---

## 🏠 Dashboard

<img src="screenshots/dashboard.png" alt="Dashboard" width="300">

---

## 🍽️ Menu Management

<img src="screenshots/menu-management.png" alt="Menu Management" width="300">

---

## 🧾 Billing Screen

<img src="screenshots/billing.png" alt="Billing Screen" width="300">

---

## 🛒 Cart / Selected Items

<img src="screenshots/cart.png" alt="Cart" width="300">

---

## 📋 Bill History

<img src="screenshots/bill-history.png" alt="Bill History" width="300">

---

## 🗑️ Delete Bill Confirmation

<img src="screenshots/delete-confirmation.png" alt="Delete Bill Confirmation" width="300">

---

## 🖨️ Print Bill

<img src="screenshots/print-bill.png" alt="Print Bill" width="300">

---

## ⚙️ Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/SirsodeRaj/Android-Hotel-Management-System.git
```

### 2. Open in Android Studio

Open the cloned project in **Android Studio**.

Wait for Gradle synchronization to complete.

### 3. Connect Android Device or Emulator

You can run the application using:

* Android Emulator
* Physical Android smartphone

If using a physical device, enable USB debugging.

### 4. Build the Project

In Android Studio, select:

```text
Build → Make Project
```

### 5. Run the Application

Click:

```text
Run ▶
```

Select the connected emulator or Android device.

---

## 🔑 Default Login

The current application uses a local SQLite administrator account.

```text
Username: admin
Password: 123
```

> For a production application, default credentials should be changed and passwords should be stored securely.

---

## 📂 Main Modules

| Module          | Description                    |
| --------------- | ------------------------------ |
| Login           | Admin authentication           |
| Dashboard       | Main navigation and overview   |
| Menu Management | Manage dishes and prices       |
| Billing         | Create new bills               |
| Cart            | Manage selected dishes         |
| Bill History    | View previously saved bills    |
| Bill Details    | View complete bill information |
| Delete Bill     | Remove saved bills             |
| Print Bill      | Print generated bills          |
| Database        | Manage local SQLite data       |

---

## 👨‍💻 Developer

**Raj Sirsode**

B.Tech – Artificial Intelligence & Data Science

---

## ⭐ If You Like This Project

If you find this project useful, consider giving the repository a ⭐ on GitHub.

````
