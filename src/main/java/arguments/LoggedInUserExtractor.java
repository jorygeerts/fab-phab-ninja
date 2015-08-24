package arguments;

import ninja.Context;
import ninja.params.ArgumentExtractor;

/**
 * Created by jgeerts on 10-8-15.
 */
public class LoggedInUserExtractor implements ArgumentExtractor<UserStruct> {

    @Override
    public UserStruct extract(Context context) {

        // if we got no session we break:
        if (context.getSession() != null) {

            String username = context.getSession().get("username");

            return new UserStruct(context.getSession().get("username"));

        }

        return null;
    }

    @Override
    public Class getExtractedType() {
        return UserStruct.class;
    }

    @Override
    public String getFieldName() {
        return null;
    }
}