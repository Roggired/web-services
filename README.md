## Лабораторные работы 1-3 по Технологиям веб-сервисов

Работу выполнили студенты P4216: Егошин А.В., Кулинич Я.В.

## Configuration

Для бэка см. `resources/config.properties`

## Prerequisites

JDK 17
Docker

## How to run

1. build
```bash
./gradlew build
```
2. start db
```bash
docker compose up -d
```
3. start back
```bash
./gradlew build && java -jar back/build/libs/back-1.0.jar
```
4. start client
```bash
./gradlew build && java -jar client/build/libs/client-1.0.jar
```
