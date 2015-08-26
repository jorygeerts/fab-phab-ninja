package filters;

import com.google.inject.Inject;
import ninja.*;
import services.PhotoService;

/**
 * Created by jgeerts on 26-8-15.
 */
public class PhotoServiceReadyFilter implements Filter {

    @Inject
    protected PhotoService photoService;

    @Override
    public Result filter(FilterChain filterChain, Context context) {

        if (!photoService.isReady()) {
            return Results.html().template("views/system/in-boot-process.ftl.html");
        }

        return filterChain.next(context);
    }

}
