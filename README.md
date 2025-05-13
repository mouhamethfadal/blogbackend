# Blog Backend

A Spring Boot-based RESTful API backend for my personal blog, featuring MongoDB integration, JWT authentication, and centralized configuration.

## Overview

Blog Backend is a comprehensive Spring Boot application designed to support my personal blog. It integrates  backend services for blog content management, user authentication, and API documentation.

## Features

- **Spring Boot 3.4.0**: Modern Java framework for backend development
- **Java 21**: Modern LTS release with performance and security improvements
- **MongoDB Integration**: NoSQL database for flexible data storage
- **JWT Authentication**: Secure API access with JSON Web Tokens
- **Spring Security**: Role-based access control and security configurations
- **Spring Cloud Config**: Centralized configuration management
- **Swagger/OpenAPI Documentation**: Auto-generated API documentation
- **Docker Support**: Containerization for consistent deployment
- **Environment Configuration**: .env file support for local development
- **Actuator Endpoints**: Application health monitoring and management

## Prerequisites

- Java 21 or higher
- Maven 3.9+
- MongoDB instance
- Spring Cloud Config Server (for centralized configuration)


## Configuration

### Environment Variables

Copy the `.env.template` file to `.env` and set the variables.

### MongoDB Configuration

The application uses MongoDB for data storage. The connection is configured through the MongoConfig class, which masks sensitive information in logs.

### Security Configuration

Security configuration adapts based on the active profile:
- **Development profile**: Swagger UI accessible
- **Production profile**:Swagger UI blocked

CSRF is disabled for both profile because we use jwt and stateless authentication.

## Getting Started

### Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/mouhamethfadal/blogbackend.git
   cd blogbackend
   ```

2. Set up the environment variables:
   ```bash
   cp .env.template .env
   # Edit .env with your configuration
   ```

3. Run the application on your IDE (IntelliJ preferably):

## API Endpoints

The API documentation is available at `/swagger-ui.html` when running in development mode.

## Testing

Run tests with:
```
mvn test
```


## License

[MIT LICENCE]

## Contact

Project maintained by [Fadal](https://github.com/mouhamethfadal)
