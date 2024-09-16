package com.example.escaner.Firebase.auth

import com.example.escaner.Firebase.user.UserRepositoryImplement
import com.example.escaner.Repositorios.AuthRepository
import com.example.escaner.Repositorios.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindAuthRepository(authRepository: AuthRepositoryImplement): AuthRepository

    @Binds
    abstract fun userRepository(userRepository: UserRepositoryImplement): UserRepository
}