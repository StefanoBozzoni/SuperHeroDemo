import androidx.room.Room
import com.example.superheroesdemo.data.local.LocalDataSource
import com.example.superheroesdemo.data.local.RemoteDataSource
import com.example.superheroesdemo.data.local.database.AppDatabase
import com.example.superheroesdemo.data.local.database.DatabaseConstants
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val localModule = module {

    single(named(DatabaseConstants.DATABASE_NAME)) {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DatabaseConstants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    single(named("LOCAL_DATA_SOURCE")) { LocalDataSource(get(named(DatabaseConstants.DATABASE_NAME))) }

    single(named("REMOTE_DATA_SOURCE")) { RemoteDataSource(get(named("APP_SERVICE"))) }


}