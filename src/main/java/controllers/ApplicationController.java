/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import arguments.LoggedInUser;
import arguments.UserStruct;
import com.google.inject.Inject;
import filters.LoggingFilter;
import filters.SecureFilter;
import ninja.BasicAuthFilter;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;

import com.google.inject.Singleton;
import ninja.metrics.Counted;
import ninja.metrics.Metered;
import ninja.metrics.Timed;
import ninja.params.PathParam;
import services.GreetingService;


@Singleton
public class ApplicationController {

    @Inject
    private GreetingService greetingService;


    public Result index() {

        return Results.html();

    }
    
    public Result helloWorldJson() {
        
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.content = "Hello World! Hello Json!";
        simplePojo.test = "Woazers?!";

        return Results.json().render(simplePojo);

    }

    public Result helloName(@PathParam("name") String name) {
        SimplePojo sp = new SimplePojo();
        sp.content = "Hello, " + name;

        return Results.json().render(sp);
    }

    @FilterWith({BasicAuthFilter.class, LoggingFilter.class})
    @Metered
    public Result helloSecure(@LoggedInUser UserStruct user)
    {
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.content = "Secure is hell!";

        if (user != null) {
            simplePojo.test = greetingService.greetUser(user);
        }

        return Results.json().render(simplePojo);
    }
    
    public static class SimplePojo {

        public String content;
        public String test;
        
    }
}
