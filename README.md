# PaidyInterview project

## Overview
This project contains the source code and the test for a simple restaurant application. It consists in a defined set of
API's to call in order to manage orders.

## Definition
The APIs are defined as the following:

* createOrder(Int, String): creates an order and stores it
* deleteOrder(Int, String): deletes a specific order
* getOrders(Int): gets all the orders for a specified table
* getOrder(Int, String): gets the first matching order for a specific table

## Assumptions and information
* We can create more than one order containing the same item for a table
* Getting a specific order doesn't show all the orders with the same items for a table but ine only
* Deleting a specific order deletes only one occurrence of orders
* The tests framework is pretty self-explanatory, tests are not heavily commented
* tests mainly focus on the business processes since those are the core of the application
* Outputs are routed to the console. Servers outputs some important steps and clients outputs the results
as asked in the assigment. The main executable might make the outputs messy, a lighter executable can be 
found in the test classes to have a better visibility on the output
* Orders are stored in a Map. It's an easy enough structure to handle and it allows to organize the orders 
according to the table number. It would not suffice for a bigger set of data but in our case it is enough
and eases the display of outputs.

## Structure
In this project, the following can be found:
* 2 test classes testing independent functionality and a test class testing the full API on a slow pace.
* 3 Classes containing main functions. One is the server, the second is the client and the third is the
combination of both using multiple thread to run a server and a plurality of clients. The third class is
the one showing the best how the application performs
* 1 class of Actor handling relaying the requests to the business
* 2 classes representing the business, 1 for the orders and 1 for the service taking care of them

## How to
This project is build with Gradle, it should automatically download the dependencies and detect the classes
upon import of the project in an IDE (advice to have more clarity for the output), otherwise, command lines
at the root of the project can be executed (gradle build and gradle run should work as is since the project
is configured as so). if you don't have gradle on your machine, the gradle wrapper should do the work for
you.

The main application to test is in the "MainApp" class. It instantiate 1 server thread and 9 clients. Server
thread stops upon pressing "Enter" and the client threads are timed (should be running for 4 minutes). Those
values are of course possible to change but are not located in a configuration file. They should be changed
within the code. Same for the number of items in the items pool to use or the number of tables to handle. The
application is configured for a pretty decent example with 4 items of orders and 3 tables. This allows a lot
of interactions between the requests. Each clients requests every 10 + (random value between 0 and 5 seconds)
allowing a more chaotic behavior. This results in a request to the server around every seconds. Those numbers
can also be increased but the readability of the output becomes less clear as the number of requests are
increasing.

### Hope you'll enjoy!

Kevin