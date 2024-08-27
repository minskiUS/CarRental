# Demo application for interview purpose

### To run build docker image and run docker-compose

```bash
docker build -t carrent .
docker-compose up --build
```

### Access swagger
Swagger is available: http://localhost:8080/swagger-ui/index.html#


### Tools and frameworks
Gradle, Spring Boot, Spring Security, Spring Data JPA, Spring MVC, Lombok, Mockito, WireMock, Docker,
Swagger

### Functionality
- [x] oauth (jwt)
- [x] login/ signup
- [x] role separation (user/admin)
- [x] entities modification (add/update/delete)
- [x] basic validation
- [x] booking management
- [ ] microservices (banking service, insurance service, notification service)
- [ ] event driven approach
- [ ] k8s
- [ ] aws (sns, sqs, ec2)