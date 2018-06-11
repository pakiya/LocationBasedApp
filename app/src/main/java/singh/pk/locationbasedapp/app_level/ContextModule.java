package singh.pk.locationbasedapp.app_level;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import singh.pk.locationbasedapp.app_level.ApplicationContext;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @ApplicationContext
    public Context context() {
        return context;
    }
}
