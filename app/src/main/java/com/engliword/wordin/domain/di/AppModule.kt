package com.engliword.wordin.domain.di

import android.content.Context
import com.engliword.wordin.data.googleDrive.GoogleDriveRepository
import com.engliword.wordin.domain.usecase.DownloadDatabaseUseCase
import com.engliword.wordin.domain.usecase.UploadDatabaseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGoogleDriveRepository(
        @ApplicationContext context: Context
    ): GoogleDriveRepository {
        return GoogleDriveRepository(context)
    }

    @Provides
    @Singleton
    fun provideUploadDatabaseUseCase(repository: GoogleDriveRepository): UploadDatabaseUseCase {
        return UploadDatabaseUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDownloadDatabaseUseCase(repository: GoogleDriveRepository): DownloadDatabaseUseCase {
        return DownloadDatabaseUseCase(repository)
    }
}