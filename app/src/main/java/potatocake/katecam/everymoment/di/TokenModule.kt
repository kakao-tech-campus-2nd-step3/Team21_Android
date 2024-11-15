package potatocake.katecam.everymoment.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import potatocake.katecam.everymoment.GlobalApplication
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenModule {

    @Provides
    @Singleton
    @Named("jwtToken")
    fun provideJwtToken(): String {
        val jwtToken = GlobalApplication.prefs.getString("token", "null")
        return "Bearer $jwtToken"
    }
}
