package com.habittracker.di;

import com.habittracker.data.local.HabitDatabase;
import com.habittracker.data.local.dao.HabitCompletionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class AppModule_ProvideCompletionDaoFactory implements Factory<HabitCompletionDao> {
  private final Provider<HabitDatabase> dbProvider;

  public AppModule_ProvideCompletionDaoFactory(Provider<HabitDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public HabitCompletionDao get() {
    return provideCompletionDao(dbProvider.get());
  }

  public static AppModule_ProvideCompletionDaoFactory create(Provider<HabitDatabase> dbProvider) {
    return new AppModule_ProvideCompletionDaoFactory(dbProvider);
  }

  public static HabitCompletionDao provideCompletionDao(HabitDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCompletionDao(db));
  }
}
