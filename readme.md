# **Redis Java Integration Project**

*Demonstrating Redis features with Java (Maven-based project)*  

---

## **Table of Contents**
- [**Redis Java Integration Project**](#redis-java-integration-project)
	- [**Table of Contents**](#table-of-contents)
	- [**Project-Overview**](#project-overview)
	- [**Prerequisites**](#prerequisites)
	- [**Installation \& Setup**](#installation--setup)
		- [**1. Clone the Repository (Optional)**](#1-clone-the-repository-optional)
		- [**2. Start Redis Server**](#2-start-redis-server)
		- [**3. Build the Project**](#3-build-the-project)
	- [**Project-Components**](#project-components)
		- [**1. Sorted-Leaderboard**](#1-sorted-leaderboard)
		- [**2. PubSub-System**](#2-pubsub-system)
		- [**3. URL-Shortener**](#3-url-shortener)
	- [**Technical-Details**](#technical-details)
		- [Dependencies](#dependencies)
		- [**Project-Structure**](#project-structure)
		- [**Trouble-Shooting**](#trouble-shooting)
		- [**Connect**](#connect)

---

## **Project-Overview**

This project demonstrates key Redis use cases implemented in Java:
1. **Sorted Leaderboard** – Uses Redis Sorted Sets (ZSET) to maintain and display player rankings.
2. **Pub/Sub Messaging** – A real-time publish-subscribe system with message logging.
3. **URL Shortener** – A service to shorten long URLs with an HTTP redirection server.

Built with **Maven** for dependency management and tested with **Redis 7.x** and **Java 21**.

---

## **Prerequisites**

Before running the project, ensure you have:

| Requirement       | Installation Guide                          |
|-------------------|---------------------------------------------|
| **Redis Server**  | [Download & Install Redis](https://redis.io/download) |
| **Java JDK 21+**  | [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://openjdk.org/) |
| **Maven 3.8.6+**  | [Install Maven](https://maven.apache.org/install.html) |
| **Git (Optional)**| [Git SCM](https://git-scm.com/downloads)    |

---

## **Installation & Setup**
### **1. Clone the Repository (Optional)**

```bash
git clone https://github.com/nikhilkhilwani/Java-Redis.git
cd Java-Redis
```

### **2. Start Redis Server**

```bash
redis-server
```

*Keep this running in a seperate Terminal*

### **3. Build the Project**

```bash
mvn clean package
```

## **Project-Components**
***
### **1. Sorted-Leaderboard**

**Description**

Maintains a real-time gaming leaderboard using Redis Sorted Sets (`ZADD`, `ZREVRANGE`). Players are automatically ranked by score.

**Location**: /src/main/java/leaderboard/SortedLeaderBoard.java

**How to Run**

```bash
mvn exec:java -Dexec.mainClass="leaderboard.SortedLeaderBoard"
```

**Features**
- Add players with scores
    
- View ranked leaderboard
    
- Persistent storage in Redis

***

### **2. PubSub-System**

**Description**

A real-time messaging system where:
- **Publisher** sends messages to a channel.
    
- **Subscriber** listens and logs messages to `subscriber_log.txt`.

**Location**:
/src/main/java/pubsub/Publisher.java
/src/main/java/pubsub/Subscriber.java

**How to Run**

- **Terminal 1 (Subscriber)**:
    
    
    ```bash
	mvn exec:java -Dexec.mainClass="pubsub.Subscriber"
	```
    
- **Terminal 2 (Publisher)**:
    
    
    ```bash
	mvn exec:java -Dexec.mainClass="pubsub.Publisher"
	```

**Features**

- Dynamic channel subscription
    
- Message persistence in logs
    
- Multi-client support

***

### **3. URL-Shortener**

**Description**

Shortens long URLs (e.g., `http://example.com/very/very/very/long/url/` → `http://localhost:8080/abc`) using Redis key-value storage.

**Location**:
/src/main/java/shortener/URL_Shortener.java
/src/main/java/shortener/RedirectServer.java

**How to Run**

1. **Start the URL Shortener**:
    
```bash
    mvn exec:java -Dexec.mainClass="shortener.URL_Shortener"
``` 
2. **Start the Redirect Server** (in another terminal):
    
```bash
    
    mvn exec:java -Dexec.mainClass="shortener.RedirectServer"
```
*Leave ```RedirectServer.java``` running*. Perform all operations in ```URL_Shortener.java```. You can access all your URLs using ```http://localhost:8080/x``` 

**Features**

- Base62 encoding for short URLs
- HTTP 302 redirects  
- CLI interface for management

***

## **Technical-Details**

### Dependencies

| Library       | Purpose                      |
| ------------- | ---------------------------- |
| jedis         | Redis Java Client            |
| commons-pool2 | Connection Pooling for jedis |

*Defined in pom.xml*

### **Project-Structure**

- java-redis/
	- src/
		- main/java/
			- leaderboard/
			- pubsub/
			- shortener/
	- pom.xml                                  
	- readme.md

***

### **Trouble-Shooting**

| issue                    | solution                                     |
| ------------------------ | -------------------------------------------- |
| ```Connection Refused``` | Ensure Redis Server is Running(redis-server) |
| ```Class Not Found```    | Run ```mvn clean package``` to re-build      |
| ```Invalid Input```      | Follow CLI Properly                          |
| ```Port 8080 in use```   | Change port in ```RedirectServer.java```     |

***

### **Connect**

- 👨💻 **Author**: Nikhil Khilwani
    
- 🔗 **LinkedIn**: [Nikhil Khilwani](https://www.linkedin.com/in/nikhil-khilwani-596192249/)
    
- 🌟 **GitHub**: [nikhilkhilwani](https://github.com/nikhilkhilwani)

**Feel free to reach out for collaborations or questions!**