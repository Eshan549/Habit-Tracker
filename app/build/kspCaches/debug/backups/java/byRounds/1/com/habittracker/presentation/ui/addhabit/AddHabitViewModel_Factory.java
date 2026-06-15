package com.habittracker.presentation.ui.addhabit;

import android.content.Context;
import com.habittracker.data.repository.HabitRepository;
import com.habittracker.util.NotificationScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AddHabitViewModel_Factory implements Factory<AddHabitViewModel> {
  private final Provider<HabitRepository> repositoryProvider;

  private final Provider<NotificationScheduler> notificationSchedulerProvider;

  private final Provider<Context> contextProvider;

  public AddHabitViewModel_Factory(Provider<HabitRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider,
      Provider<Context> contextProvider) {
    this.repositoryProvider = repositoryProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public AddHabitViewModel get() {
    return newInstance(repositoryProvider.get(), notificationSchedulerProvider.get(), contextProvider.get());
  }

  public static AddHabitViewModel_Factory create(Provider<HabitRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider,
      Provider<Context> contextProvider) {
    return new AddHabitViewModel_Factory(repositoryProvider, notificationSchedulerProvider, contextProvider);
  }

  public static AddHabitViewModel newInstance(HabitRepository repository,
      NotificationScheduler notificationScheduler, Context context) {
    return new AddHabitViewModel(repository, notificationScheduler, context);
  }
}
