package com.habittracker;

import com.habittracker.util.ThemePreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<ThemePreferences> themePreferencesProvider;

  public MainViewModel_Factory(Provider<ThemePreferences> themePreferencesProvider) {
    this.themePreferencesProvider = themePreferencesProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(themePreferencesProvider.get());
  }

  public static MainViewModel_Factory create(Provider<ThemePreferences> themePreferencesProvider) {
    return new MainViewModel_Factory(themePreferencesProvider);
  }

  public static MainViewModel newInstance(ThemePreferences themePreferences) {
    return new MainViewModel(themePreferences);
  }
}
