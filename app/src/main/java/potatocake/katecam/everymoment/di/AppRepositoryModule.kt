package potatocake.katecam.everymoment.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.repository.DiaryRepository
import potatocake.katecam.everymoment.data.repository.FriendDiaryRepository
import potatocake.katecam.everymoment.data.repository.FriendRepository
import potatocake.katecam.everymoment.data.repository.MyInfoRepository
import potatocake.katecam.everymoment.data.repository.NotificationRepository
import potatocake.katecam.everymoment.data.repository.PostRepository
import potatocake.katecam.everymoment.data.repository.UserRepository
import potatocake.katecam.everymoment.data.repository.impl.DiaryRepositoryImpl
import potatocake.katecam.everymoment.data.repository.impl.FriendDiaryRepositoryImpl
import potatocake.katecam.everymoment.data.repository.impl.FriendRepositoryImpl
import potatocake.katecam.everymoment.data.repository.impl.MyInfoRepositoryImpl
import potatocake.katecam.everymoment.data.repository.impl.NotificationRepositoryImpl
import potatocake.katecam.everymoment.data.repository.impl.PostRepositoryImpl
import potatocake.katecam.everymoment.data.repository.impl.UserRepositoryImpl
import javax.inject.Named
import javax.inject.Qualifier

@Qualifier
annotation class DiaryRepositoryQualifier

@Qualifier
annotation class FriendRepositoryQualifier

@Qualifier
annotation class FriendDiaryRepositoryQualifier

@Qualifier
annotation class NotificationRepositoryQualifier

@Qualifier
annotation class MyInfoRepositoryQualifier

@Qualifier
annotation class PostRepositoryQualifier

@Qualifier
annotation class UserRepositoryQualifier


@InstallIn(SingletonComponent::class)
@Module
object AppRepositoryModule {

    @Provides
    @DiaryRepositoryQualifier
    fun provideDiaryRepository(
        apiService: PotatoCakeApiService,
        @Named("jwtToken") token: String
    ): DiaryRepository {
        return DiaryRepositoryImpl(apiService, token)
    }

    @Provides
    @FriendRepositoryQualifier
    fun provideFriendRepository(
        apiService: PotatoCakeApiService,
        @Named("jwtToken") token: String
    ): FriendRepository {
        return FriendRepositoryImpl(apiService, token)
    }

    @Provides
    @FriendDiaryRepositoryQualifier
    fun provideFriendDiaryRepository(
        apiService: PotatoCakeApiService,
        @Named("jwtToken") token: String
    ): FriendDiaryRepository {
        return FriendDiaryRepositoryImpl(apiService, token)
    }

    @Provides
    @NotificationRepositoryQualifier
    fun provideNotificationRepository(
        apiService: PotatoCakeApiService,
        @Named("jwtToken") token: String
    ): NotificationRepository {
        return NotificationRepositoryImpl(apiService, token)
    }

    @Provides
    @MyInfoRepositoryQualifier
    fun provideMyInfoRepository(
        apiService: PotatoCakeApiService,
        @Named("jwtToken") token: String
    ): MyInfoRepository {
        return MyInfoRepositoryImpl(apiService, token)
    }

    @Provides
    @PostRepositoryQualifier
    fun providePostRepository(
        apiService: PotatoCakeApiService,
        @Named("jwtToken") token: String
    ): PostRepository {
        return PostRepositoryImpl(apiService, token)
    }

    @Provides
    @UserRepositoryQualifier
    fun provideUserRepository(
        apiService: PotatoCakeApiService,
        @Named("jwtToken") token: String
    ): UserRepository {
        return UserRepositoryImpl(apiService, token)
    }
}
