# Banking API — Pipeline DevSecOps

API REST bancária desenvolvida em **Java 17 + Spring Boot 3** com pipeline de CI/CD completo no **Jenkins**, seguindo práticas modernas de **DevSecOps**: build, testes automatizados, análise de segurança em containers e publicação contínua em registry.

Projeto pessoal de portfólio focado em automação, qualidade e segurança no ciclo de entrega de software.

---

## 🏗️ Arquitetura

```
┌──────────────┐      ┌──────────────┐      ┌──────────────┐
│   GitHub     │─────▶│   Jenkins    │─────▶│  Docker Hub  │
│  (código)    │      │  (pipeline)  │      │  (imagem)    │
└──────────────┘      └──────────────┘      └──────────────┘
                             │
                             ▼
                      ┌──────────────┐
                      │  Trivy Scan  │
                      │ (segurança)  │
                      └──────────────┘
```

---

## 🛠️ Stack

| Componente | Tecnologia |
| :--- | :--- |
| **Linguagem** | Java 17 |
| **Framework** | Spring Boot 3.2 |
| **Build** | Maven |
| **Testes** | JUnit 5 + Mockito |
| **Cobertura** | JaCoCo |
| **Container** | Docker (multi-stage, non-root) |
| **CI/CD** | Jenkins (Groovy Declarative Pipeline) |
| **Segurança** | Trivy (SCA + Container Scan) |
| **Registry** | Docker Hub |

---

## 📡 Endpoints da API

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/v1/accounts` | Criar conta |
| `GET` | `/api/v1/accounts/{id}` | Consultar conta |
| `POST` | `/api/v1/transfers` | Transferir entre contas |
| `GET` | `/actuator/health` | Health check |
| `GET` | `/actuator/prometheus` | Métricas Prometheus |

### Exemplo de uso

**Criar conta:**
```bash
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{"holderName":"João","initialBalance":"1000.00"}'
```

**Transferir entre contas:**
```bash
curl -X POST http://localhost:8080/api/v1/transfers \
  -H "Content-Type: application/json" \
  -d '{"sourceId":1,"targetId":2,"amount":"100.00"}'
```

---

## 🔄 Pipeline (Estágios)

O pipeline é executado automaticamente a cada `git push` na branch `main`:

| # | Estágio | Descrição |
| :--- | :--- | :--- |
| 1 | **Checkout** | Clona o código do GitHub |
| 2 | **Build & Test** | Compila com Maven + testes JUnit + cobertura JaCoCo |
| 3 | **Docker Build** | Constrói imagem Docker (multi-stage + non-root) |
| 4 | **Security Scan** | Trivy escaneia vulnerabilidades na imagem |
| 5 | **Push to Registry** | Publica a imagem aprovada no Docker Hub |
| 6 | **Notify** | Reporta sucesso/falha do build |

---

## 🚀 Como executar localmente

### Pré-requisitos
- Java 17
- Maven 3.8+
- Docker (opcional)

### Rodar os testes
```bash
mvn clean verify
```

### Rodar a aplicação
```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

### Rodar com Docker
```bash
docker build -t banking-api .
docker run -p 8080:8080 banking-api
```

---

## 🔐 Práticas de Segurança

- **Container non-root:** a imagem Docker executa com o usuário `appuser`.
- **Multi-stage build:** imagem final enxuta (JRE Alpine + JAR).
- **Scan de vulnerabilidades:** Trivy roda a cada build e reporta CVEs.
- **Secrets:** credenciais gerenciadas pelo Jenkins Credentials (nunca no código).

---

## 📝 Autor

**João Victor Alves Carvalho**
- GitHub: [@Joaovac08](https://github.com/Joaovac08)
- Docker Hub: [@joaovac06](https://hub.docker.com/u/joaovac06)
