# Course Platform Analytics

Event-driven analytics system using Kafka, Prometheus, and Grafana to track and visualize user behavior on a course platform.

## Project Overview

This project demonstrates a complete analytics pipeline where user events from a React frontend flow through Kafka to a Spring Boot consumer, get converted into Prometheus metrics, and are visualized in Grafana dashboards.

## Architecture Diagram

```
┌─────────────────┐
│  React Frontend │
│  (User Events)  │
└────────┬────────┘
         │ HTTP POST
         ▼
┌─────────────────────────────────────┐
│  Producer Service (Port 8080)       │
│  - Spring Boot REST API             │
│  - Receives user events             │
│  - Publishes to Kafka topic         │
└────────┬────────────────────────────┘
         │ Kafka Events
         ▼
┌─────────────────────────────────────┐
│  Kafka Topic: "testy"               │
│  - Event broker & storage           │
└────────┬────────────────────────────┘
         │ Consume & Process
         ▼
┌─────────────────────────────────────┐
│  Consumer Service (Port 8081)       │
│  - Spring Boot Kafka Consumer       │
│  - Micrometer Metrics               │
│  - /actuator/prometheus endpoint    │
└────────┬────────────────────────────┘
         │ Metrics Scraping
         ▼
┌─────────────────────────────────────┐
│  Prometheus                         │
│  - Time-series metrics database     │
│  - Pulls metrics periodically       │
└────────┬────────────────────────────┘
         │ PromQL Queries
         ▼
┌─────────────────────────────────────┐
│  Grafana                            │
│  - Visualize metrics & dashboards   │
└─────────────────────────────────────┘
```

## Tech Stack

| Component | Version/Technology |
|-----------|-------------------|
| Language | Java 21 |
| Framework | Spring Boot |
| Message Broker | Apache Kafka |
| Metrics | Micrometer |
| Time-Series DB | Prometheus |
| Visualization | Grafana |
| Build Tool | Gradle |
| OS | Ubuntu VM |
| Containerization | None (VM-based) |

## Tracked User Events

- `LANDING_PAGE_VIEW` - User lands on the home page
- `COURSE_PAGE_VIEW` - User views a course detail page
- `BUY_COURSE_CLICKED` - User initiates purchase

## Project Structure

```
course-platform-analytics/
├── producer-service/
│   ├── src/main/java/
│   │   └── com/example/producer/
│   │       ├── controller/
│   │       ├── service/
│   │       └── model/
│   ├── build.gradle
│   └── application.yml
├── consumer-service/
│   ├── src/main/java/
│   │   └── com/example/consumer/
│   │       ├── listener/
│   │       ├── service/
│   │       └── model/
│   ├── build.gradle
│   └── application.yml
├── frontend/
│   └── src/
│       └── components/
├── prometheus.yml
└── README.md
```

## Ports Used

| Service | Port | Endpoint |
|---------|------|----------|
| Producer Service | 8080 | `http://localhost:8080/api/events` |
| Consumer Service | 8081 | `http://localhost:8081/actuator/prometheus` |
| Prometheus | 9090 | `http://localhost:9090` |
| Grafana | 3000 | `http://localhost:3000` |
| Kafka Broker | 9092 | `localhost:9092` |

## How to Run

### Prerequisites
- Java 21 installed
- Kafka running on `localhost:9092`
- Prometheus configured
- Grafana running on port 3000

### Step 1: Start Kafka
```bash
# In Kafka directory
./bin/kafka-server-start.sh config/server.properties
```

### Step 2: Create Kafka Topic
```bash
./bin/kafka-topics.sh --create --topic testy \
  --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

### Step 3: Start Producer Service
```bash
cd producer-service
./gradlew bootRun
```

### Step 4: Start Consumer Service
```bash
cd consumer-service
./gradlew bootRun
```

### Step 5: Configure & Start Prometheus
```bash
./prometheus --config.file=prometheus.yml
```

### Step 6: Access Grafana
Open `http://localhost:3000` (default: admin/admin)

## Kafka → Prometheus → Grafana Flow

1. **Frontend sends event** → Producer REST API (`POST /api/events`)
2. **Producer publishes** → Kafka topic "testy"
3. **Consumer listens** → Kafka topic and processes events
4. **Micrometer records** → Custom metrics (counters, gauges)
5. **Prometheus scrapes** → Metrics endpoint `/actuator/prometheus`
6. **Grafana queries** → Prometheus for visualization

## Prometheus Configuration

`prometheus.yml` scrape config:
```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'consumer-service'
    static_configs:
      - targets: ['localhost:8081']
    metrics_path: '/actuator/prometheus'
```

## Sample Metrics

Once events are processed, Prometheus exposes metrics like:

```
# HELP events_total Total number of events processed
# TYPE events_total counter
events_total{event_type="LANDING_PAGE_VIEW"} 45
events_total{event_type="COURSE_PAGE_VIEW"} 32
events_total{event_type="BUY_COURSE_CLICKED"} 8

# HELP event_processing_duration_seconds Event processing time
# TYPE event_processing_duration_seconds histogram
event_processing_duration_seconds_bucket{le="0.1"} 78
event_processing_duration_seconds_bucket{le="0.5"} 82
event_processing_duration_seconds_bucket{le="+Inf"} 85
```

## Grafana PromQL Queries

### Total Events by Type
```promql
sum by (event_type) (events_total)
```

### Events Per Second (Rate)
```promql
rate(events_total[1m])
```

### 95th Percentile Processing Duration
```promql
histogram_quantile(0.95, event_processing_duration_seconds_bucket)
```

### Buy Course Conversion Rate
```promql
events_total{event_type="BUY_COURSE_CLICKED"} / events_total{event_type="COURSE_PAGE_VIEW"}
```

## Key Concepts Demonstrated

- **Event-Driven Architecture** - Asynchronous event processing via Kafka
- **Metrics Instrumentation** - Micrometer for application observability
- **Time-Series Data** - Prometheus for metrics storage and querying
- **Data Visualization** - Grafana dashboards for business insights
- **Microservices Communication** - Kafka as central event broker
- **Separation of Concerns** - Producer and Consumer independently scalable

## Future Improvements

- [ ] Docker Compose setup for easier deployment
- [ ] Elasticsearch + Kibana for log aggregation
- [ ] Alerting rules in Prometheus
- [ ] Authentication & authorization (OAuth2)
- [ ] Multi-partition Kafka topic with consumer group scaling
- [ ] Database persistence (PostgreSQL) for event history
- [ ] Advanced Grafana dashboards with drill-down capabilities
- [ ] Load testing with JMeter
- [ ] CI/CD pipeline (GitHub Actions)

## Author

Created as an educational project demonstrating event-driven analytics with modern Java stack.
