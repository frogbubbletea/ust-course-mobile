package com.frogbubbletea.ustcoursemobile.di

import android.content.Context
import androidx.room.Room
import com.frogbubbletea.ustcoursemobile.data.StarredCoursesRepository
import com.frogbubbletea.ustcoursemobile.data.StarredDao
import com.frogbubbletea.ustcoursemobile.data.StarredDatabase
import com.frogbubbletea.ustcoursemobile.data.StarredRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindsStarredList(
        starredCoursesRepository: StarredCoursesRepository
    ): StarredRepository

    companion object {
        @Provides
        @Singleton
        fun provideStarredDatabase(@ApplicationContext context: Context): StarredDatabase =
            Room.databaseBuilder(
                context,
                StarredDatabase::class.java,
                "starred_database"
            ).build()

        @Provides
        fun provideStarredDao(database: StarredDatabase): StarredDao = database.starredDao()
    }
}