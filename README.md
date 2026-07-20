# Alumni Tracking and Career Management System (ATCMS)

An enterprise-level web application designed to manage alumni relations and facilitate career opportunities, including features like job browsing and alumni dashboards.

## 🏗️ Architecture

This project is built using a Java EE multi-module architecture:
*   **ATCMS (Enterprise/EAR):** The main assembly project that bundles the application for deployment.
*   **ATCMS-ejb:** Contains the backend business logic and database communication.
*   **ATCMS-war:** Contains the frontend user interface and web assets.

## 💻 Tech Stack

*   **Frontend:** Java Server Faces (JSF)
*   **Backend:** Java EE (EJB)
*   **Persistence:** Java Persistence API (JPA) for entity management
*   **IDE:** Apache NetBeans

## 🚀 Getting Started

### Prerequisites
*   Apache NetBeans IDE
*   [Insert Database Name, e.g., MySQL or PostgreSQL]
*   [Insert Application Server, e.g., GlassFish or Payara]

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/YourUsername/ATCMS.git](https://github.com/YourUsername/ATCMS.git)
    ```
2.  **Open in NetBeans:**
    *   Go to `File > Open Project`.
    *   Navigate to the cloned directory and open the main `ATCMS` enterprise project.
    *   **Important:** You must also manually open the `ATCMS-ejb` and `ATCMS-war` sub-projects the first time to properly link the modules in your IDE workspace.
3.  **Database Configuration:**
    *   [Add brief instructions here on how to connect the JPA entities to the local database, e.g., creating a connection pool or updating the persistence.xml]
4.  **Deploy:**
    *   Right-click the `ATCMS` (EAR) project and select **Clean and Build**.
    *   Right-click again and select **Run** to deploy to your local application server.
