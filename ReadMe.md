# Food Request Forum - Event-Driven Architecture

## Overview
A Kafka-based event-driven system for managing food requests between customers and chefs. The system uses tag-based matching to connect food requests with appropriate chefs and manages the complete workflow through events.

## Project Structure
```
src/main/java/com/homey/foodforum/
├── consumers/
│   ├── FoodRequestConsumer.java
│   ├── ResponseConsumer.java
│   ├── TagMatchingConsumer.java
│   └── NotificationConsumer.java
├── events/
│   ├── Event.java
│   ├── FoodRequestEvent.java
│   ├── ResponseEvent.java
│   └── ChefNotificationEvent.java
├── models/
│   ├── enums/
│   │   ├── RequestStatus.java
│   │   └── ResponseStatus.java
│   ├── Chef.java
│   ├── FoodRequest.java
│   ├── ChefResponse.java
│   └── Location.java
├── producers/
│   ├── FoodRequestProducer.java
│   ├── ResponseProducer.java
│   └── ChefNotificationProducer.java
└── service/
    ├── FoodRequestService.java
    ├── ChefResponseService.java
    └── NotificationService.java
```

## Requirements

### Software Requirements
- Java 21 or higher
- Apache Kafka 3.9.0
- Maven
- IDE (preferably VSCode)

## Setup Instructions

### 1. Kafka Setup
```bash
# Start Zookeeper
bin/zookeeper-server-start.sh -daemon config/zookeeper.properties

# Start Kafka
bin/kafka-server-start.sh -daemon config/server.properties

# Create required topics
bin/kafka-topics.sh --create --topic food-requests --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic chef-responses --bootstrap-server localhost:9092
bin/kafka-topics.sh --create --topic chef-notifications --bootstrap-server localhost:9092
```

### 2. Project Setup
```bash
# Clone repository
git clone [repository-url]

# Build project
mvn clean install
```

## Event-Driven Architecture

### Event Flow
1. Food Request Creation
   - User creates request → FoodRequestEvent
   - Published to "food-requests" topic

2. Tag Matching
   - TagMatchingConsumer processes FoodRequestEvent
   - Matches request tags with chef tags
   - Creates ChefNotificationEvent
   - Published to "chef-notifications" topic

3. Chef Response
   - Chef receives notification
   - Creates response → ResponseEvent
   - Published to "chef-responses" topic

### Event Types
- FoodRequestEvent: Initial food request creation
- ChefNotificationEvent: Notification for matching chefs
- ResponseEvent: Chef's response to food request

## Usage

### Running the Application
Run Main.java to start the CLI application.

### Available Operations
1. Create Food Request
   - Enter dish details
   - Add relevant tags
   - System matches with appropriate chefs

2. Add Chef Response
   - Enter request ID
   - Provide price and preparation details
   - System notifies customer

3. View Requests
   - See all active food requests
   - Filter by status

4. View Responses
   - Check responses for specific requests
   - View chef details

5. View Chef Notifications
   - See matched requests for each chef
   - View matching tags

### Logging
System prints notifications and event processing details to console.