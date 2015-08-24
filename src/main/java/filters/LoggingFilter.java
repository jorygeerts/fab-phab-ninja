package filters;

import ninja.Context;
import ninja.Filter;
import ninja.FilterChain;
import ninja.Result;

/**
 * Created by jgeerts on 5-8-15.
 */
public class LoggingFilter implements Filter {

    @Override
    public Result filter(FilterChain filterChain, Context context) {
        System.out.println("This is a logging filter");


        int counter = 0;
        String counterStr;
        try {
            counterStr = context.getSession().get("counter");
            counter = Integer.parseInt(counterStr);
        } catch (NumberFormatException nfe) {}

        counter++;

        counterStr = Integer.toString(counter);

        context.getSession().put("counter", counterStr);

        return filterChain.next(context);
    }
}
