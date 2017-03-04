# camel-jpa-transaction
Test Camel Jpa Transaction with Splitter

Postgres startup on docker:
docker run --name camel-jpa -p 5432:5432 -e POSTGRES_PASSWORD=123456 -e POSTGRES_USER=camel  -d postgres:9.5-alpine