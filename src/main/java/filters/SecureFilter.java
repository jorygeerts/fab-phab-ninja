package filters;

import ninja.*;

public class SecureFilter  implements Filter
{

    /** If a username is saved we assume the session is valid */
    public final String USERNAME = "username";

    @Override
    public Result filter(FilterChain chain, Context context) {

        // if we got no cookies we break:
        if (context.getSession() == null
                || context.getSession().get(USERNAME) == null) {

            return Results.forbidden().html().template("/views/system/403forbidden.ftl.html");

        }

        return chain.next(context);
    }
}
