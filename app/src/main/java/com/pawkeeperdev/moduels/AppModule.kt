package com.pawkeeperdev.moduels

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pawkeeperdev.dao.PetDao
import com.pawkeeperdev.dao.UserDao
import com.pawkeeperdev.dataBase.UserDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): UserDataBase {
        return Room.databaseBuilder(app, UserDataBase::class.java, "userData.db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UserDataBase): UserDao {
        return database.UserDao()
    }

    @Provides
    @Singleton
    fun providePetDao(database: UserDataBase): PetDao {
        return database.petDao()
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE INDEX IF NOT EXISTS index_pets_ownerId ON pets (ownerId)")
        }
    }
}