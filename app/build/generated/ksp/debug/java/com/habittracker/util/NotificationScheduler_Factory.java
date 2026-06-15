package com.habittracker.util;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class NotificationScheduler_Factory implements Factory<NotificationScheduler> {
  @Override
  public NotificationScheduler get() {
    return newInstance();
  }

  public static NotificationScheduler_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NotificationScheduler newInstance() {
    return new NotificationScheduler();
  }

  private static final class InstanceHolder {
    private static final NotificationScheduler_Factory INSTANCE = new NotificationScheduler_Factory();
  }
}
