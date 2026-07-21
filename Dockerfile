# --- Étape de build ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copie du pom.xml seul d'abord : permet à Docker de mettre les dépendances
# en cache et de ne pas les retélécharger à chaque build si le pom ne change pas
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# --- Étape d'exécution ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# -Xmx contraint la mémoire du JVM pour rester dans la limite de 512 Mo
# du plan gratuit de Render (RAM système + threads + metaspace en plus du heap)
ENTRYPOINT ["java", "-Xmx400m", "-jar", "app.jar"]