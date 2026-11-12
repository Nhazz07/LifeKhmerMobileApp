// ignore: unused_import
import 'package:intl/intl.dart' as intl;

import 'app_localizations.dart';

// ignore_for_file: type=lint

/// The translations for English (`en`).
class AppLocalizationsEn extends AppLocalizations {
  AppLocalizationsEn([String locale = 'en']) : super(locale);

  @override
  String get appName => 'LifeKhmer';

  @override
  String get appTagline => 'Personal Health & Smart Nutrition';

  @override
  String get navHome => 'Home';

  @override
  String get navHealth => 'Health';

  @override
  String get navNutrition => 'Nutrition';

  @override
  String get navScan => 'Scan';

  @override
  String get navProfile => 'Profile';

  @override
  String get navSettings => 'Settings';

  @override
  String get guestWelcomeTitle => 'Welcome to LifeKhmer';

  @override
  String get guestWelcomeSubtitle =>
      'Explore Cambodian nutrition, food catalog, and health tools.';

  @override
  String get guestExploreButton => 'Explore as Guest';

  @override
  String get guestLoginPrompt => 'Have an account? Sign in';

  @override
  String get guestModeBanner => 'You are exploring in Guest Mode.';

  @override
  String get guestSignInAction => 'Sign In to Save';

  @override
  String get guestAuthRequiredTitle => 'Account Required';

  @override
  String get guestAuthRequiredBody =>
      'Create an account or sign in to save your personal health records, log meals, and track medications.';

  @override
  String get guestContinueExploring => 'Continue Exploring';

  @override
  String get authSignIn => 'Sign In';

  @override
  String get authSignUp => 'Create Account';

  @override
  String get authFullName => 'Full Name';

  @override
  String get authEmail => 'Email Address';

  @override
  String get authPassword => 'Password';

  @override
  String get authDateOfBirth => 'Date of Birth';

  @override
  String get authAlreadyHaveAccount => 'Already have an account? Sign In';

  @override
  String get authDontHaveAccount => 'Don\'t have an account? Create one';

  @override
  String get authSignOut => 'Sign Out';

  @override
  String get authSignOutConfirm => 'Are you sure you want to sign out?';

  @override
  String get dashGreeting => 'Hello';

  @override
  String get dashDailyCalories => 'Daily Calories';

  @override
  String get dashTarget => 'Target';

  @override
  String get dashConsumed => 'Consumed';

  @override
  String get dashRemaining => 'Remaining';

  @override
  String get dashMacros => 'Macronutrients';

  @override
  String get dashProtein => 'Protein';

  @override
  String get dashCarbs => 'Carbohydrates';

  @override
  String get dashFat => 'Fat';

  @override
  String get dashLatestVitals => 'Latest Health Measurements';

  @override
  String get dashUpcomingAppointments => 'Upcoming Appointments';

  @override
  String get dashActiveMeds => 'Active Medications';

  @override
  String get dashQuickActions => 'Quick Actions';

  @override
  String get dashNoRecordsYet => 'No measurements recorded yet.';

  @override
  String get healthTitle => 'Health Tracking';

  @override
  String get healthAddMeasurement => 'Add Measurement';

  @override
  String get healthTypeWeight => 'Weight';

  @override
  String get healthTypeHeight => 'Height';

  @override
  String get healthTypeBloodPressure => 'Blood Pressure';

  @override
  String get healthTypeHeartRate => 'Heart Rate';

  @override
  String get healthTypeTemperature => 'Body Temperature';

  @override
  String get healthValue => 'Value';

  @override
  String get healthDiastolic => 'Diastolic (mmHg)';

  @override
  String get healthUnit => 'Unit';

  @override
  String get healthNotes => 'Notes (optional)';

  @override
  String get healthSave => 'Save Measurement';

  @override
  String get healthHistory => 'Measurement History';

  @override
  String get nutritionTitle => 'Nutrition Diary';

  @override
  String get nutritionCatalog => 'Food Catalog';

  @override
  String get nutritionSearchPlaceholder => 'Search Cambodian foods...';

  @override
  String get nutritionAddToDiary => 'Add to Diary';

  @override
  String get nutritionMealType => 'Meal Type';

  @override
  String get nutritionBreakfast => 'Breakfast';

  @override
  String get nutritionLunch => 'Lunch';

  @override
  String get nutritionDinner => 'Dinner';

  @override
  String get nutritionSnack => 'Snack';

  @override
  String get nutritionPortionGrams => 'Portion (grams)';

  @override
  String get nutritionCalories => 'Calories';

  @override
  String get nutritionSaveMeal => 'Record Meal';

  @override
  String get nutritionPer100g => 'per 100g';

  @override
  String get scanTitle => 'AI Food Scanner';

  @override
  String get scanSubtitle =>
      'Photograph any Cambodian dish for instant nutritional estimates.';

  @override
  String get scanCamera => 'Take Photo';

  @override
  String get scanGallery => 'Choose from Gallery';

  @override
  String get scanAnalyzing => 'Analyzing food with AI...';

  @override
  String get scanConfidence => 'Confidence';

  @override
  String get scanEstimatedNutrition => 'Estimated Nutrition';

  @override
  String get scanDisclaimer =>
      'AI estimates are for nutritional guidance and not medical diagnosis.';

  @override
  String get scanConfirmAndSave => 'Confirm & Add to Diary';

  @override
  String get medTitle => 'Medications';

  @override
  String get medAdd => 'Add Medication';

  @override
  String get medName => 'Medication Name';

  @override
  String get medDosage => 'Dosage (e.g. 500mg)';

  @override
  String get medFrequency => 'Frequency (e.g. Once daily)';

  @override
  String get medInstructions => 'Instructions (e.g. After meals)';

  @override
  String get medStartDate => 'Start Date';

  @override
  String get medEndDate => 'End Date (optional)';

  @override
  String get medLogTaken => 'Mark as Taken';

  @override
  String get medNoActive => 'No active medications.';

  @override
  String get apptTitle => 'Appointments';

  @override
  String get apptSchedule => 'Schedule Appointment';

  @override
  String get apptDoctor => 'Doctor / Specialist';

  @override
  String get apptFacility => 'Hospital / Clinic';

  @override
  String get apptTime => 'Date & Time';

  @override
  String get apptPurpose => 'Purpose';

  @override
  String get apptCancel => 'Cancel Appointment';

  @override
  String get apptNoUpcoming => 'No upcoming appointments.';

  @override
  String get symptomTitle => 'Symptom Tracking';

  @override
  String get symptomLog => 'Log Symptom';

  @override
  String get symptomName => 'Symptom Name';

  @override
  String get symptomSeverity => 'Severity (1 - 10)';

  @override
  String get symptomSeverityMild => 'Mild';

  @override
  String get symptomSeverityModerate => 'Moderate';

  @override
  String get symptomSeveritySevere => 'Severe';

  @override
  String get symptomNoRecords => 'No symptoms recorded.';

  @override
  String get settingsTitle => 'Settings';

  @override
  String get settingsLanguage => 'Language';

  @override
  String get settingsLanguageEn => 'English';

  @override
  String get settingsLanguageKm => 'Khmer (ភាសាខ្មែរ)';

  @override
  String get settingsTheme => 'Theme';

  @override
  String get settingsThemeLight => 'Light Mode';

  @override
  String get settingsThemeDark => 'Dark Mode';

  @override
  String get settingsThemeSystem => 'System Default';

  @override
  String get settingsAbout => 'About LifeKhmer';

  @override
  String get settingsPrivacy => 'Privacy & Data Security';

  @override
  String get commonCancel => 'Cancel';

  @override
  String get commonSave => 'Save';

  @override
  String get commonDelete => 'Delete';

  @override
  String get commonEdit => 'Edit';

  @override
  String get commonLoading => 'Loading...';

  @override
  String get commonError => 'Something went wrong. Please try again.';

  @override
  String get commonSuccess => 'Operation completed successfully.';
}
