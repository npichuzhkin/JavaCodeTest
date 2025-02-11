# JavaCodeTest
___
## Описание
Тестовое задание. API, позволяющий производить внесение и списание средств из кошелька, а также получить информацию о балансе. 

## Для запуска
1. Необходимо запустить Docker
2. Поднимается с помощью команды:

>     docker-compose up --build

При первом запуске в таблице создаётся один кошелёк с балансом 10 000.
Для получения ID кошелька нужно выполнить команды:
> 1.     docker exec -it wallets_db bin/bash
> 2.     psql -U postgres -d wallets_db
> 3.     SELECT * FROM WALLET

Пример ответа:

                       id                  | amount
     --------------------------------------+--------
      159240be-2c6e-42e4-84b1-0df1cc4def43 |  10000
      (1 row)

## Что представляет из себя
Состоит из 2 связанных контейнеров (один контейнер - сервер, второй - БД).

Сервер - Tomcat 9, включающий jdk8 и сам war-файл.

БД (wallets_db) - PostgreSQL 15, содержит одну таблицу (wallet), в которой 2 поля. Поле id (primary key + автогенерация uuid) и поле amount (целочисленное).

Нагрузочное тестирование производилось с помощью Apache Jmeter - [plan файл](https://github.com/npichuzhkin/JavaCodeTest/tree/master/plan).

## Эндпоинты
### GET /api/v1/wallets/{walletId}
Показывает идентификатор (walletId) и баланс (balance) в формате JSON.

Пример тела ответа:
```json
{
    "walletId": "75361b95-8085-4d71-9e17-be1a3fb5b0b0",
    "balance": 10000
}
```
### POST /api/v1/wallet 
Вносит (DEPOSIT) или списывает (WITHDRAW) средства.
Принимает идентификатор (walletId) операцию (operationType) и сумму (amount) в формате JSON.

Пример тела запроса:
```json
{
    "walletId": "75361b95-8085-4d71-9e17-be1a3fb5b0b0",
    "operationType": "DEPOSIT",
    "amount": 10000
}
```
