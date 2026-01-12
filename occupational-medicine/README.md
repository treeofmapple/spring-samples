# Occupational Medicine

This is a full-stack application featuring a Rust-based backend API, a data generation utility, and a JavaScript-based frontend. The system uses PostgreSQL for the database, with data population orchestrated by Docker Compose.

---

## Prerequisites

Before you begin, ensure you have the following tools installed on your system.

### 1. Rust and Cargo
You'll need the Rust programming language and its package manager, Cargo. The recommended way to install them is by using `rustup`.

* **On Linux or macOS:**
    ```sh
    curl --proto '=https' --tlsv1.2 -sSf [https://sh.rustup.rs](https://sh.rustup.rs) | sh
    ```

* **On Windows:**
    Download and run `rustup-init.exe` from the [official Rust website](https://www.rust-lang.org/tools/install).

This will install both `rust` and the package manager `cargo`. Follow the on-screen instructions to complete the installation. You may need to restart your terminal or shell for the changes to take effect.

### 2. Node.js and npm
The frontend is a Node.js application, which requires `npm` (Node Package Manager) to be installed.

* Download and install the latest LTS version of Node.js from the [official website](https://nodejs.org/). `npm` is included with the installation.

### 3. Docker and Docker Compose
We use Docker to run the PostgreSQL database and upload the generated data.

* The easiest way to get both is by installing [**Docker Desktop**](https://www.docker.com/products/docker-desktop/) for your operating system (Windows, macOS, or Linux).

### 4. C++ Build Tools
A C++ toolchain is required for compiling some native dependencies.

* **For Visual Studio Code Users:**
    1.  Install the [**C/C++ extension**](https://marketplace.visualstudio.com/items?itemName=ms-vscode.cpptools) from the Visual Studio Code Marketplace.
    2.  You also need a compiler. On **Windows**, you can install the "Desktop development with C++" workload via the Visual Studio Installer (see below). On **macOS**, you can install the Xcode command-line tools. On **Linux**, you can install `build-essential`.

* **For Visual Studio (IDE) on Windows:**
    1.  Download the [**Visual Studio Installer**](https://visualstudio.microsoft.com/downloads/).
    2.  When installing, make sure to select the **"Desktop development with C++"** workload. This will install all the necessary tools, including the MSVC compiler toolchain.

---

## Getting Started: Installation and Setup

Follow these steps to get the entire system configured and running.

### 1. Clone the Repository
First, clone this repository to your local machine.

```sh
git clone [https://github.com/your-username/your-repository.git](https://github.com/your-username/your-repository.git)
cd your-repository
```

### 2. Configure the Backend API (`backend-api`)
The backend needs a `.env` file with the database connection details.

1.  Navigate to the backend directory:
    ```sh
    cd backend-api
    ```
2.  Create a `.env` file by copying the sample file:
    ```sh
    cp .env.sample .env
    ```
3.  Open the newly created `.env` file and fill in your database credentials. These should match the credentials you will use in the `docker-compose.yml` file in the `datagenerator` directory.

### 3. Generate and Upload Data (`datagenerator`)
This step generates the necessary data and automatically uploads it to the PostgreSQL database using Docker.

1.  Navigate to the data generator directory from the project root:
    ```sh
    cd datagenerator
    ```
2.  Run the data generator script. This may take some time to complete.
    ```sh
    cargo run
    ```
3.  Once the script finishes generating the data files, use Docker Compose to build and run the database container. This command will also copy the generated data into the database.
    ```sh
    docker-compose up --build
    ```
    After this process completes, the PostgreSQL database will be running and accessible externally.

### 4. Install Frontend Dependencies (`frontend-system`)
Finally, install the necessary Node.js packages for the frontend application.

1.  Navigate to the frontend directory from the project root:
    ```sh
    cd frontend-system
    ```
2.  Install all dependencies using `npm`:
    ```sh
    npm install
    ```

---

## Running the Application

After the initial setup is complete, follow these steps to run the servers.

### 1. Run the Backend API
1.  Open a new terminal.
2.  Navigate to the `backend-api` directory.
3.  Start the backend server:
    ```sh
    cargo run
    ```
    The backend API will be running on **`http://localhost:8080`**.

### 2. Run the Frontend Application
1.  Open another new terminal.
2.  Navigate to the `frontend-system` directory.
3.  Start the frontend development server:
    ```sh
    npm run dev
    ```
    The frontend application will be running on **`http://localhost:3000`**.

You can now access the web application by opening your browser and navigating to `http://localhost:3000`.
