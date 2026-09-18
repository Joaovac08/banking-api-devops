# ============================================================
# ESTÁGIO 1: BUILD (Maven + JDK)
# ============================================================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia só o pom.xml primeiro (aproveita cache do Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# ESTÁGIO 2: RUNTIME (JRE Alpine - imagem final pequena)
# ============================================================
FROM eclipse-temurin:17-jre-alpine

# Cria usuário não-root (segurança)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copia só o JAR final do estágio de build
COPY --from=build /app/target/banking-api.jar app.jar

# Muda para o usuário não-root
USER appuser

# Expõe a porta da API
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
