package com.ke.hs.di

import com.ke.hs.module.parser.DeckCardObserver
import com.ke.hs.parser.DeckCardObserverImpl
import com.ke.hs.parser.FileTextProvider
import com.ke.hs.parser.FileTextProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class Module {

//    @Provides
//    @Singleton
//    fun provideHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideMoshi(): Moshi {
//        return Moshi.Builder()
//            .add(CardClassAdapter())
//            .add(CardTypeAdapter())
//            .add(RarityAdapter())
//            .add(SpellSchoolAdapter())
//            .add(MechanicsAdapter())
//            .add(RaceAdapter())
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideHearthStoneJsonApi(
//        okHttpClient: OkHttpClient,
//        moshi: Moshi
//    ): HearthStoneJsonApi {
//
//        return Retrofit.Builder()
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .client(okHttpClient)
//            .baseUrl(HearthStoneJsonApi.BASE_URL)
//            .build()
//            .create(HearthStoneJsonApi::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): Database {
//        return Room.databaseBuilder(
//            context,
//            Database::class.java,
//            "card.db"
//        ).build()
//    }
//
//    @Provides
//    fun provideCardDao(database: Database): CardDao {
//        return database.cardDao()
//    }
//
//    @Provides
//    fun provideGameDao(database: Database): GameDao = database.gameDao()
//
//
//    @Provides
//    fun provideZonePositionChangedEventDao(database: Database): ZonePositionChangedEventDao =
//        database.zonePositionChangedEventDao()
//
//
//    @Provides
//    fun provideBlockTagStack(impl: BlockTagStackImpl): BlockTagStack {
//        return impl
//    }
//
//    @Provides
//    fun providePowerParser(powerParserImpl: PowerParserImpl): PowerParser {
//        return powerParserImpl
//    }
//
//    @Provides
//    fun providePowerTagHandler(powerTagHandlerImpl: PowerTagHandlerImpl): PowerTagHandler =
//        powerTagHandlerImpl

    @Provides
    fun provideDeckCardObserver(deckCardObserverImpl: DeckCardObserverImpl): DeckCardObserver =
        deckCardObserverImpl


    @Provides
    fun provideFileTextProvider(fileTextProviderImpl: FileTextProviderImpl): FileTextProvider {
        return fileTextProviderImpl
    }
}