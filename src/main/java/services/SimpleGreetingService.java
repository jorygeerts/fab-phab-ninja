package services;

import arguments.UserStruct;

/**
 * Created by jgeerts on 10-8-15.
 */
public class SimpleGreetingService implements GreetingService {
    @Override
    public String greetUser(UserStruct userStruct) {
        return "Hello there, " + userStruct.getUsername();
    }
}
