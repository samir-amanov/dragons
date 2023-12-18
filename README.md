# Dragons of Mugloar game player API

This is a [Spring Boot](https://spring.io/) Rest Client application, and it uses RestTemplate to communicate with [Dragons of Mugloar API](https://www.dragonsofmugloar.com/).

Main class is DragonsOfMugloarApplication and general logic is saved in GameRunner, RunnerHelper and MessageProcessor classes.

# How to run
* Clone this repository to your local machine
* Go to root folder and run commands:

       mvn clean install

       mvn spring-boot:run
  
After starting Spring Boot application it will ask you to enter the number of tries. The number of tries means how many times you want the application to play the game. It is okay to enter 1 if you want to try the application.

     Enter number of tries to start the game...


# Sample result
![game](https://github.com/samir-amanov/dragons/assets/50835183/e9430d55-e3e6-43f6-876c-27e0c57158ce)

* This screenshot shows sample result after run. "1" is entered for the number of tries, so application will play the game only one time. 
* Final result will show Score, Gold, Lives, Turn and either VICTORY or DEFEAT after the game finished, it will do it for each try.
* Finally, application will calculate percentage of wins against played games.

You can also find detailed logs in **logs/application.log**.
