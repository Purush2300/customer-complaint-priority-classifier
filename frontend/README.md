# AI Customer Complaint Priority Classifier

A full-stack application that uses AI to analyze customer complaints, classify their priority, and generate actionable recommendations.

## Features

- Create and manage customer complaints
- Analyze complaints using the Gemini API
- Generate complaint summaries, priority levels, reasons, and suggested actions
- Store and retrieve complaints using MySQL
- React dashboard connected to a Spring Boot REST API
- Delete complaints through the application

## Tech Stack

- **Frontend:** React, Vite, JavaScript, CSS
- **Backend:** Java 17, Spring Boot, Spring AI
- **Database:** MySQL
- **AI Provider:** Google Gemini API
- **API Testing:** Postman / cURL

## Project Structure

```text
complaint-assistant/
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── ...
├── frontend/
│   ├── src/
│   ├── package.json
│   └── ...
├── .gitignore
└── README.md
```

## Application Workflow

1. The user enters a customer complaint through the React frontend.
2. React sends an HTTP request to the Spring Boot REST API.
3. Spring Boot receives the complaint and processes the request through the backend service layer.
4. For AI analysis, the backend sends the complaint text to Google Gemini through Spring AI.
5. Gemini generates a summary, priority classification, reason, and suggested action.
6. The backend saves the complaint and its analysis results in MySQL.
7. The API returns the result to React, which displays the complaint and its analysis on the dashboard.
8. Users can retrieve existing complaints or delete complaints through the application.

## REST API Endpoints

Base URL: `http://localhost:9090/api/complaints`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/complaints` | Retrieve all complaints |
| POST | `/api/complaints` | Create a complaint |
| POST | `/api/complaints/analyze` | Analyze a complaint using AI and save the result |
| DELETE | `/api/complaints/{id}` | Delete a complaint by ID |

### Example: Create a Complaint

**Request**

```http
POST /api/complaints
Content-Type: application/json
```

**Example JSON body**

```json
{
  "description": "My order has not arrived after five days."
}
```

### Example: Analyze a Complaint

**Request**

```http
POST /api/complaints/analyze
Content-Type: application/json
```

**Example JSON body**

```json
{
  "description": "I paid for my order ten days ago, but it has not arrived and customer support is not responding."
}
```

**Example response**

```json
{
  "id": 1,
  "description": "I paid for my order ten days ago, but it has not arrived and customer support is not responding.",
  "summary": "Delayed order with unresponsive customer support.",
  "priority": "HIGH",
  "reason": "The customer has paid for an order that has not arrived, and support has not responded.",
  "suggestedAction": "Investigate the shipment and provide an update or refund.",
  "status": "OPEN"
}
```

*Note: The response is illustrative. Your actual response fields and AI-generated values may differ.*

## Running Locally

### Prerequisites

- Java 17
- Maven
- Node.js and npm
- MySQL
- Google Gemini API key

### 1. Configure the Backend

Configure your MySQL database and credentials in the backend configuration. Set the Gemini API key using an environment variable rather than committing it to Git.

Example environment variable:

```text
GEMINI_API_KEY=your_api_key
```

Ensure your Spring Boot configuration references the environment variable.

### 2. Start the Backend

Open a terminal inside `backend/` and run:

```bash
mvn spring-boot:run
```

The backend should start on:

```text
http://localhost:9090
```

### 3. Start the Frontend

Open another terminal inside `frontend/` and run:

```bash
npm install
npm run dev
```

Open the local URL printed by Vite, typically:

```text
http://localhost:5173
```

## Security

- Store API keys and database credentials in environment variables.
- Never commit secrets, `.env` files containing real credentials, or passwords to GitHub.
- Configure CORS to allow only the frontend origins required by your application.

## Future Improvements

- Filter complaints by priority and status
- Add dashboard statistics and complaint status updates
- Implement user authentication and authorization
- Deploy the application to a cloud platform