package com.habittracker.data.repository;

import com.habittracker.data.local.dao.HabitCompletionDao;
import com.habittracker.data.local.dao.HabitDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class HabitRepository_Factory implements Factory<HabitRepository> {
  private final Provider<HabitDao> habitDaoProvider;

  private final Provider<HabitCompletionDao> completionDaoProvider;

  public HabitRepository_Factory(Provider<HabitDao> habitDaoProvider,
      Provider<HabitCompletionDao> completionDaoProvider) {
    this.habitDaoProvider = habitDaoProvider;
    this.completionDaoProvider = completionDaoProvider;
  }

  @Override
  public HabitRepository get() {
    return newInstance(habitDaoProvider.get(), completionDaoProvider.get());
  }

  public static HabitRepository_Factory create(Provider<HabitDao> habitDaoProvider,
      Provider<HabitCompletionDao> completionDaoProvider) {
    return new HabitRepository_Factory(habitDaoProvider, completionDaoProvider);
  }

  public static HabitRepository newInstance(HabitDao habitDao, HabitCompletionDao completionDao) {
    return new HabitRepository(habitDao, completionDao);
  }
}
