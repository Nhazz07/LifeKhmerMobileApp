import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:intl/intl.dart' as intl;

import 'app_localizations_en.dart';
import 'app_localizations_km.dart';

// ignore_for_file: type=lint

/// Callers can lookup localized strings with an instance of AppLocalizations
/// returned by `AppLocalizations.of(context)`.
///
/// Applications need to include `AppLocalizations.delegate()` in their app's
/// `localizationDelegates` list, and the locales they support in the app's
/// `supportedLocales` list. For example:
///
/// ```dart
/// import 'generated/app_localizations.dart';
///
/// return MaterialApp(
///   localizationsDelegates: AppLocalizations.localizationsDelegates,
///   supportedLocales: AppLocalizations.supportedLocales,
///   home: MyApplicationHome(),
/// );
/// ```
///
/// ## Update pubspec.yaml
///
/// Please make sure to update your pubspec.yaml to include the following
/// packages:
///
/// ```yaml
/// dependencies:
///   # Internationalization support.
///   flutter_localizations:
///     sdk: flutter
///   intl: any # Use the pinned version from flutter_localizations
///
///   # Rest of dependencies
/// ```
///
/// ## iOS Applications
///
/// iOS applications define key application metadata, including supported
/// locales, in an Info.plist file that is built into the application bundle.
/// To configure the locales supported by your app, you’ll need to edit this
/// file.
///
/// First, open your project’s ios/Runner.xcworkspace Xcode workspace file.
/// Then, in the Project Navigator, open the Info.plist file under the Runner
/// project’s Runner folder.
///
/// Next, select the Information Property List item, select Add Item from the
/// Editor menu, then select Localizations from the pop-up menu.
///
/// Select and expand the newly-created Localizations item then, for each
/// locale your application supports, add a new item and select the locale
/// you wish to add from the pop-up menu in the Value field. This list should
/// be consistent with the languages listed in the AppLocalizations.supportedLocales
/// property.
abstract class AppLocalizations {
  AppLocalizations(String locale)
    : localeName = intl.Intl.canonicalizedLocale(locale.toString());

  final String localeName;

  static AppLocalizations? of(BuildContext context) {
    return Localizations.of<AppLocalizations>(context, AppLocalizations);
  }

  static const LocalizationsDelegate<AppLocalizations> delegate =
      _AppLocalizationsDelegate();

  /// A list of this localizations delegate along with the default localizations
  /// delegates.
  ///
  /// Returns a list of localizations delegates containing this delegate along with
  /// GlobalMaterialLocalizations.delegate, GlobalCupertinoLocalizations.delegate,
  /// and GlobalWidgetsLocalizations.delegate.
  ///
  /// Additional delegates can be added by appending to this list in
  /// MaterialApp. This list does not have to be used at all if a custom list
  /// of delegates is preferred or required.
  static const List<LocalizationsDelegate<dynamic>> localizationsDelegates =
      <LocalizationsDelegate<dynamic>>[
        delegate,
        GlobalMaterialLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
      ];

  /// A list of this localizations delegate's supported locales.
  static const List<Locale> supportedLocales = <Locale>[
    Locale('en'),
    Locale('km'),
  ];

  /// No description provided for @appName.
  ///
  /// In en, this message translates to:
  /// **'LifeKhmer'**
  String get appName;

  /// No description provided for @appTagline.
  ///
  /// In en, this message translates to:
  /// **'Personal Health & Smart Nutrition'**
  String get appTagline;

  /// No description provided for @navHome.
  ///
  /// In en, this message translates to:
  /// **'Home'**
  String get navHome;

  /// No description provided for @navHealth.
  ///
  /// In en, this message translates to:
  /// **'Health'**
  String get navHealth;

  /// No description provided for @navNutrition.
  ///
  /// In en, this message translates to:
  /// **'Nutrition'**
  String get navNutrition;

  /// No description provided for @navScan.
  ///
  /// In en, this message translates to:
  /// **'Scan'**
  String get navScan;

  /// No description provided for @navProfile.
  ///
  /// In en, this message translates to:
  /// **'Profile'**
  String get navProfile;

  /// No description provided for @navSettings.
  ///
  /// In en, this message translates to:
  /// **'Settings'**
  String get navSettings;

  /// No description provided for @guestWelcomeTitle.
  ///
  /// In en, this message translates to:
  /// **'Welcome to LifeKhmer'**
  String get guestWelcomeTitle;

  /// No description provided for @guestWelcomeSubtitle.
  ///
  /// In en, this message translates to:
  /// **'Explore Cambodian nutrition, food catalog, and health tools.'**
  String get guestWelcomeSubtitle;

  /// No description provided for @guestExploreButton.
  ///
  /// In en, this message translates to:
  /// **'Explore as Guest'**
  String get guestExploreButton;

  /// No description provided for @guestLoginPrompt.
  ///
  /// In en, this message translates to:
  /// **'Have an account? Sign in'**
  String get guestLoginPrompt;

  /// No description provided for @guestModeBanner.
  ///
  /// In en, this message translates to:
  /// **'You are exploring in Guest Mode.'**
  String get guestModeBanner;

  /// No description provided for @guestSignInAction.
  ///
  /// In en, this message translates to:
  /// **'Sign In to Save'**
  String get guestSignInAction;

  /// No description provided for @guestAuthRequiredTitle.
  ///
  /// In en, this message translates to:
  /// **'Account Required'**
  String get guestAuthRequiredTitle;

  /// No description provided for @guestAuthRequiredBody.
  ///
  /// In en, this message translates to:
  /// **'Create an account or sign in to save your personal health records, log meals, and track medications.'**
  String get guestAuthRequiredBody;

  /// No description provided for @guestContinueExploring.
  ///
  /// In en, this message translates to:
  /// **'Continue Exploring'**
  String get guestContinueExploring;

  /// No description provided for @authSignIn.
  ///
  /// In en, this message translates to:
  /// **'Sign In'**
  String get authSignIn;

  /// No description provided for @authSignUp.
  ///
  /// In en, this message translates to:
  /// **'Create Account'**
  String get authSignUp;

  /// No description provided for @authFullName.
  ///
  /// In en, this message translates to:
  /// **'Full Name'**
  String get authFullName;

  /// No description provided for @authEmail.
  ///
  /// In en, this message translates to:
  /// **'Email Address'**
  String get authEmail;

  /// No description provided for @authPassword.
  ///
  /// In en, this message translates to:
  /// **'Password'**
  String get authPassword;

  /// No description provided for @authDateOfBirth.
  ///
  /// In en, this message translates to:
  /// **'Date of Birth'**
  String get authDateOfBirth;

  /// No description provided for @authAlreadyHaveAccount.
  ///
  /// In en, this message translates to:
  /// **'Already have an account? Sign In'**
  String get authAlreadyHaveAccount;

  /// No description provided for @authDontHaveAccount.
  ///
  /// In en, this message translates to:
  /// **'Don\'t have an account? Create one'**
  String get authDontHaveAccount;

  /// No description provided for @authSignOut.
  ///
  /// In en, this message translates to:
  /// **'Sign Out'**
  String get authSignOut;

  /// No description provided for @authSignOutConfirm.
  ///
  /// In en, this message translates to:
  /// **'Are you sure you want to sign out?'**
  String get authSignOutConfirm;

  /// No description provided for @dashGreeting.
  ///
  /// In en, this message translates to:
  /// **'Hello'**
  String get dashGreeting;

  /// No description provided for @dashDailyCalories.
  ///
  /// In en, this message translates to:
  /// **'Daily Calories'**
  String get dashDailyCalories;

  /// No description provided for @dashTarget.
  ///
  /// In en, this message translates to:
  /// **'Target'**
  String get dashTarget;

  /// No description provided for @dashConsumed.
  ///
  /// In en, this message translates to:
  /// **'Consumed'**
  String get dashConsumed;

  /// No description provided for @dashRemaining.
  ///
  /// In en, this message translates to:
  /// **'Remaining'**
  String get dashRemaining;

  /// No description provided for @dashMacros.
  ///
  /// In en, this message translates to:
  /// **'Macronutrients'**
  String get dashMacros;

  /// No description provided for @dashProtein.
  ///
  /// In en, this message translates to:
  /// **'Protein'**
  String get dashProtein;

  /// No description provided for @dashCarbs.
  ///
  /// In en, this message translates to:
  /// **'Carbohydrates'**
  String get dashCarbs;

  /// No description provided for @dashFat.
  ///
  /// In en, this message translates to:
  /// **'Fat'**
  String get dashFat;

  /// No description provided for @dashLatestVitals.
  ///
  /// In en, this message translates to:
  /// **'Latest Health Measurements'**
  String get dashLatestVitals;

  /// No description provided for @dashUpcomingAppointments.
  ///
  /// In en, this message translates to:
  /// **'Upcoming Appointments'**
  String get dashUpcomingAppointments;

  /// No description provided for @dashActiveMeds.
  ///
  /// In en, this message translates to:
  /// **'Active Medications'**
  String get dashActiveMeds;

  /// No description provided for @dashQuickActions.
  ///
  /// In en, this message translates to:
  /// **'Quick Actions'**
  String get dashQuickActions;

  /// No description provided for @dashNoRecordsYet.
  ///
  /// In en, this message translates to:
  /// **'No measurements recorded yet.'**
  String get dashNoRecordsYet;

  /// No description provided for @healthTitle.
  ///
  /// In en, this message translates to:
  /// **'Health Tracking'**
  String get healthTitle;

  /// No description provided for @healthAddMeasurement.
  ///
  /// In en, this message translates to:
  /// **'Add Measurement'**
  String get healthAddMeasurement;

  /// No description provided for @healthTypeWeight.
  ///
  /// In en, this message translates to:
  /// **'Weight'**
  String get healthTypeWeight;

  /// No description provided for @healthTypeHeight.
  ///
  /// In en, this message translates to:
  /// **'Height'**
  String get healthTypeHeight;

  /// No description provided for @healthTypeBloodPressure.
  ///
  /// In en, this message translates to:
  /// **'Blood Pressure'**
  String get healthTypeBloodPressure;

  /// No description provided for @healthTypeHeartRate.
  ///
  /// In en, this message translates to:
  /// **'Heart Rate'**
  String get healthTypeHeartRate;

  /// No description provided for @healthTypeTemperature.
  ///
  /// In en, this message translates to:
  /// **'Body Temperature'**
  String get healthTypeTemperature;

  /// No description provided for @healthValue.
  ///
  /// In en, this message translates to:
  /// **'Value'**
  String get healthValue;

  /// No description provided for @healthDiastolic.
  ///
  /// In en, this message translates to:
  /// **'Diastolic (mmHg)'**
  String get healthDiastolic;

  /// No description provided for @healthUnit.
  ///
  /// In en, this message translates to:
  /// **'Unit'**
  String get healthUnit;

  /// No description provided for @healthNotes.
  ///
  /// In en, this message translates to:
  /// **'Notes (optional)'**
  String get healthNotes;

  /// No description provided for @healthSave.
  ///
  /// In en, this message translates to:
  /// **'Save Measurement'**
  String get healthSave;

  /// No description provided for @healthHistory.
  ///
  /// In en, this message translates to:
  /// **'Measurement History'**
  String get healthHistory;

  /// No description provided for @nutritionTitle.
  ///
  /// In en, this message translates to:
  /// **'Nutrition Diary'**
  String get nutritionTitle;

  /// No description provided for @nutritionCatalog.
  ///
  /// In en, this message translates to:
  /// **'Food Catalog'**
  String get nutritionCatalog;

  /// No description provided for @nutritionSearchPlaceholder.
  ///
  /// In en, this message translates to:
  /// **'Search Cambodian foods...'**
  String get nutritionSearchPlaceholder;

  /// No description provided for @nutritionAddToDiary.
  ///
  /// In en, this message translates to:
  /// **'Add to Diary'**
  String get nutritionAddToDiary;

  /// No description provided for @nutritionMealType.
  ///
  /// In en, this message translates to:
  /// **'Meal Type'**
  String get nutritionMealType;

  /// No description provided for @nutritionBreakfast.
  ///
  /// In en, this message translates to:
  /// **'Breakfast'**
  String get nutritionBreakfast;

  /// No description provided for @nutritionLunch.
  ///
  /// In en, this message translates to:
  /// **'Lunch'**
  String get nutritionLunch;

  /// No description provided for @nutritionDinner.
  ///
  /// In en, this message translates to:
  /// **'Dinner'**
  String get nutritionDinner;

  /// No description provided for @nutritionSnack.
  ///
  /// In en, this message translates to:
  /// **'Snack'**
  String get nutritionSnack;

  /// No description provided for @nutritionPortionGrams.
  ///
  /// In en, this message translates to:
  /// **'Portion (grams)'**
  String get nutritionPortionGrams;

  /// No description provided for @nutritionCalories.
  ///
  /// In en, this message translates to:
  /// **'Calories'**
  String get nutritionCalories;

  /// No description provided for @nutritionSaveMeal.
  ///
  /// In en, this message translates to:
  /// **'Record Meal'**
  String get nutritionSaveMeal;

  /// No description provided for @nutritionPer100g.
  ///
  /// In en, this message translates to:
  /// **'per 100g'**
  String get nutritionPer100g;

  /// No description provided for @scanTitle.
  ///
  /// In en, this message translates to:
  /// **'AI Food Scanner'**
  String get scanTitle;

  /// No description provided for @scanSubtitle.
  ///
  /// In en, this message translates to:
  /// **'Photograph any Cambodian dish for instant nutritional estimates.'**
  String get scanSubtitle;

  /// No description provided for @scanCamera.
  ///
  /// In en, this message translates to:
  /// **'Take Photo'**
  String get scanCamera;

  /// No description provided for @scanGallery.
  ///
  /// In en, this message translates to:
  /// **'Choose from Gallery'**
  String get scanGallery;

  /// No description provided for @scanAnalyzing.
  ///
  /// In en, this message translates to:
  /// **'Analyzing food with AI...'**
  String get scanAnalyzing;

  /// No description provided for @scanConfidence.
  ///
  /// In en, this message translates to:
  /// **'Confidence'**
  String get scanConfidence;

  /// No description provided for @scanEstimatedNutrition.
  ///
  /// In en, this message translates to:
  /// **'Estimated Nutrition'**
  String get scanEstimatedNutrition;

  /// No description provided for @scanDisclaimer.
  ///
  /// In en, this message translates to:
  /// **'AI estimates are for nutritional guidance and not medical diagnosis.'**
  String get scanDisclaimer;

  /// No description provided for @scanConfirmAndSave.
  ///
  /// In en, this message translates to:
  /// **'Confirm & Add to Diary'**
  String get scanConfirmAndSave;

  /// No description provided for @medTitle.
  ///
  /// In en, this message translates to:
  /// **'Medications'**
  String get medTitle;

  /// No description provided for @medAdd.
  ///
  /// In en, this message translates to:
  /// **'Add Medication'**
  String get medAdd;

  /// No description provided for @medName.
  ///
  /// In en, this message translates to:
  /// **'Medication Name'**
  String get medName;

  /// No description provided for @medDosage.
  ///
  /// In en, this message translates to:
  /// **'Dosage (e.g. 500mg)'**
  String get medDosage;

  /// No description provided for @medFrequency.
  ///
  /// In en, this message translates to:
  /// **'Frequency (e.g. Once daily)'**
  String get medFrequency;

  /// No description provided for @medInstructions.
  ///
  /// In en, this message translates to:
  /// **'Instructions (e.g. After meals)'**
  String get medInstructions;

  /// No description provided for @medStartDate.
  ///
  /// In en, this message translates to:
  /// **'Start Date'**
  String get medStartDate;

  /// No description provided for @medEndDate.
  ///
  /// In en, this message translates to:
  /// **'End Date (optional)'**
  String get medEndDate;

  /// No description provided for @medLogTaken.
  ///
  /// In en, this message translates to:
  /// **'Mark as Taken'**
  String get medLogTaken;

  /// No description provided for @medNoActive.
  ///
  /// In en, this message translates to:
  /// **'No active medications.'**
  String get medNoActive;

  /// No description provided for @apptTitle.
  ///
  /// In en, this message translates to:
  /// **'Appointments'**
  String get apptTitle;

  /// No description provided for @apptSchedule.
  ///
  /// In en, this message translates to:
  /// **'Schedule Appointment'**
  String get apptSchedule;

  /// No description provided for @apptDoctor.
  ///
  /// In en, this message translates to:
  /// **'Doctor / Specialist'**
  String get apptDoctor;

  /// No description provided for @apptFacility.
  ///
  /// In en, this message translates to:
  /// **'Hospital / Clinic'**
  String get apptFacility;

  /// No description provided for @apptTime.
  ///
  /// In en, this message translates to:
  /// **'Date & Time'**
  String get apptTime;

  /// No description provided for @apptPurpose.
  ///
  /// In en, this message translates to:
  /// **'Purpose'**
  String get apptPurpose;

  /// No description provided for @apptCancel.
  ///
  /// In en, this message translates to:
  /// **'Cancel Appointment'**
  String get apptCancel;

  /// No description provided for @apptNoUpcoming.
  ///
  /// In en, this message translates to:
  /// **'No upcoming appointments.'**
  String get apptNoUpcoming;

  /// No description provided for @symptomTitle.
  ///
  /// In en, this message translates to:
  /// **'Symptom Tracking'**
  String get symptomTitle;

  /// No description provided for @symptomLog.
  ///
  /// In en, this message translates to:
  /// **'Log Symptom'**
  String get symptomLog;

  /// No description provided for @symptomName.
  ///
  /// In en, this message translates to:
  /// **'Symptom Name'**
  String get symptomName;

  /// No description provided for @symptomSeverity.
  ///
  /// In en, this message translates to:
  /// **'Severity (1 - 10)'**
  String get symptomSeverity;

  /// No description provided for @symptomSeverityMild.
  ///
  /// In en, this message translates to:
  /// **'Mild'**
  String get symptomSeverityMild;

  /// No description provided for @symptomSeverityModerate.
  ///
  /// In en, this message translates to:
  /// **'Moderate'**
  String get symptomSeverityModerate;

  /// No description provided for @symptomSeveritySevere.
  ///
  /// In en, this message translates to:
  /// **'Severe'**
  String get symptomSeveritySevere;

  /// No description provided for @symptomNoRecords.
  ///
  /// In en, this message translates to:
  /// **'No symptoms recorded.'**
  String get symptomNoRecords;

  /// No description provided for @settingsTitle.
  ///
  /// In en, this message translates to:
  /// **'Settings'**
  String get settingsTitle;

  /// No description provided for @settingsLanguage.
  ///
  /// In en, this message translates to:
  /// **'Language'**
  String get settingsLanguage;

  /// No description provided for @settingsLanguageEn.
  ///
  /// In en, this message translates to:
  /// **'English'**
  String get settingsLanguageEn;

  /// No description provided for @settingsLanguageKm.
  ///
  /// In en, this message translates to:
  /// **'Khmer (ភាសាខ្មែរ)'**
  String get settingsLanguageKm;

  /// No description provided for @settingsTheme.
  ///
  /// In en, this message translates to:
  /// **'Theme'**
  String get settingsTheme;

  /// No description provided for @settingsThemeLight.
  ///
  /// In en, this message translates to:
  /// **'Light Mode'**
  String get settingsThemeLight;

  /// No description provided for @settingsThemeDark.
  ///
  /// In en, this message translates to:
  /// **'Dark Mode'**
  String get settingsThemeDark;

  /// No description provided for @settingsThemeSystem.
  ///
  /// In en, this message translates to:
  /// **'System Default'**
  String get settingsThemeSystem;

  /// No description provided for @settingsAbout.
  ///
  /// In en, this message translates to:
  /// **'About LifeKhmer'**
  String get settingsAbout;

  /// No description provided for @settingsPrivacy.
  ///
  /// In en, this message translates to:
  /// **'Privacy & Data Security'**
  String get settingsPrivacy;

  /// No description provided for @commonCancel.
  ///
  /// In en, this message translates to:
  /// **'Cancel'**
  String get commonCancel;

  /// No description provided for @commonSave.
  ///
  /// In en, this message translates to:
  /// **'Save'**
  String get commonSave;

  /// No description provided for @commonDelete.
  ///
  /// In en, this message translates to:
  /// **'Delete'**
  String get commonDelete;

  /// No description provided for @commonEdit.
  ///
  /// In en, this message translates to:
  /// **'Edit'**
  String get commonEdit;

  /// No description provided for @commonLoading.
  ///
  /// In en, this message translates to:
  /// **'Loading...'**
  String get commonLoading;

  /// No description provided for @commonError.
  ///
  /// In en, this message translates to:
  /// **'Something went wrong. Please try again.'**
  String get commonError;

  /// No description provided for @commonSuccess.
  ///
  /// In en, this message translates to:
  /// **'Operation completed successfully.'**
  String get commonSuccess;
}

class _AppLocalizationsDelegate
    extends LocalizationsDelegate<AppLocalizations> {
  const _AppLocalizationsDelegate();

  @override
  Future<AppLocalizations> load(Locale locale) {
    return SynchronousFuture<AppLocalizations>(lookupAppLocalizations(locale));
  }

  @override
  bool isSupported(Locale locale) =>
      <String>['en', 'km'].contains(locale.languageCode);

  @override
  bool shouldReload(_AppLocalizationsDelegate old) => false;
}

AppLocalizations lookupAppLocalizations(Locale locale) {
  // Lookup logic when only language code is specified.
  switch (locale.languageCode) {
    case 'en':
      return AppLocalizationsEn();
    case 'km':
      return AppLocalizationsKm();
  }

  throw FlutterError(
    'AppLocalizations.delegate failed to load unsupported locale "$locale". This is likely '
    'an issue with the localizations generation tool. Please file an issue '
    'on GitHub with a reproducible sample app and the gen-l10n configuration '
    'that was used.',
  );
}
