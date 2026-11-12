import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';

import 'features/health/presentation/add_measurement_screen.dart';
import 'features/nutrition/presentation/food_search_screen.dart';

const baseUrl = String.fromEnvironment(
  'API_BASE_URL',
  defaultValue: 'http://10.0.2.2:8090/api/v1/',
);
const store = FlutterSecureStorage();
final api = Provider((_) => Api());
final session = AsyncNotifierProvider<Auth, String?>(Auth.new);

// ---------------------------------------------------------------------------
// Api
// ---------------------------------------------------------------------------

class Api {
  Api()
    : dio = Dio(
        BaseOptions(
          baseUrl: baseUrl,
          connectTimeout: const Duration(seconds: 12),
          receiveTimeout: const Duration(seconds: 20),
        ),
      ) {
    dio.interceptors.add(
      InterceptorsWrapper(
        onRequest: (o, h) async {
          final t = await store.read(key: 'access');
          if (t != null) o.headers['Authorization'] = 'Bearer $t';
          h.next(o);
        },
      ),
    );
  }
  final Dio dio;
  Future<Map<String, dynamic>> get(String p, {Map<String, dynamic>? q}) async =>
      (await dio.get(p, queryParameters: q)).data['data'];
  Future<Map<String, dynamic>> post(String p, Map<String, dynamic> d) async =>
      (await dio.post(p, data: d)).data['data'];
}

// ---------------------------------------------------------------------------
// Auth
// ---------------------------------------------------------------------------

class Auth extends AsyncNotifier<String?> {
  @override
  Future<String?> build() => store.read(key: 'access');
  Future<void> login(String email, String password) async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(() async {
      final d = await ref.read(api).post('auth/login', {
        'email': email.trim(),
        'password': password,
      });
      await store.write(key: 'access', value: d['accessToken']);
      await store.write(key: 'refresh', value: d['refreshToken']);
      await store.write(key: 'name', value: d['fullName']);
      return d['accessToken'] as String;
    });
  }

  Future<void> register(String name, String email, String password) async {
    await ref.read(api).post('auth/register', {
      'fullName': name.trim(),
      'email': email.trim(),
      'password': password,
    });
    await login(email, password);
  }

  Future<void> logout() async {
    final r = await store.read(key: 'refresh');
    try {
      if (r != null) {
        await ref.read(api).post('auth/logout', {'refreshToken': r});
      }
    } catch (_) {}
    await store.deleteAll();
    state = const AsyncData(null);
  }
}

// ---------------------------------------------------------------------------
// App
// ---------------------------------------------------------------------------

void main() => runApp(const ProviderScope(child: App()));

class App extends ConsumerWidget {
  const App({super.key});
  @override
  Widget build(BuildContext c, WidgetRef r) {
    final a = r.watch(session);
    final loggedIn = a.asData?.value != null;
    final router = GoRouter(
      refreshListenable: ValueNotifier(a),
      redirect: (context, s) => a.isLoading
          ? null
          : !loggedIn && s.matchedLocation != '/login'
          ? '/login'
          : loggedIn && s.matchedLocation == '/login'
          ? '/'
          : null,
      routes: [
        GoRoute(path: '/', builder: (context, state) => const Home()),
        GoRoute(path: '/login', builder: (context, state) => const Login()),
      ],
    );
    return MaterialApp.router(
      title: 'LifeKhmer',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.teal),
        useMaterial3: true,
      ),
      darkTheme: ThemeData.dark(useMaterial3: true),
      routerConfig: router,
    );
  }
}

// ---------------------------------------------------------------------------
// Login
// ---------------------------------------------------------------------------

class Login extends ConsumerStatefulWidget {
  const Login({super.key});
  @override
  ConsumerState<Login> createState() => _Login();
}

class _Login extends ConsumerState<Login> {
  final e = TextEditingController(),
      p = TextEditingController(),
      n = TextEditingController(),
      f = GlobalKey<FormState>();
  bool register = false;
  @override
  Widget build(BuildContext c) {
    final a = ref.watch(session);
    return Scaffold(
      body: Center(
        child: SingleChildScrollView(
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Form(
              key: f,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Text('LifeKhmer', style: Theme.of(c).textTheme.displaySmall),
                  if (register)
                    TextFormField(
                      controller: n,
                      decoration: const InputDecoration(labelText: 'Full name'),
                      validator: (v) =>
                          v == null || v.isEmpty ? 'Required' : null,
                    ),
                  TextFormField(
                    controller: e,
                    decoration: const InputDecoration(labelText: 'Email'),
                    validator: (v) => v == null || !v.contains('@')
                        ? 'Valid email required'
                        : null,
                  ),
                  TextFormField(
                    controller: p,
                    obscureText: true,
                    decoration: const InputDecoration(labelText: 'Password'),
                    validator: (v) => v == null || v.length < 8
                        ? 'At least 8 characters'
                        : null,
                  ),
                  const SizedBox(height: 16),
                  FilledButton(
                    onPressed: a.isLoading
                        ? null
                        : () async {
                            if (!f.currentState!.validate()) return;
                            if (register) {
                              await ref
                                  .read(session.notifier)
                                  .register(n.text, e.text, p.text);
                            } else {
                              await ref
                                  .read(session.notifier)
                                  .login(e.text, p.text);
                            }
                          },
                    child: Text(register ? 'Create account' : 'Sign in'),
                  ),
                  TextButton(
                    onPressed: () => setState(() => register = !register),
                    child: Text(
                      register ? 'Sign in instead' : 'Create account',
                    ),
                  ),
                  if (a.hasError)
                    Text(
                      errorText(a.error),
                      style: TextStyle(color: Theme.of(c).colorScheme.error),
                    ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

String errorText(Object? e) =>
    e is DioException &&
        e.response?.data is Map &&
        e.response!.data['message'] != null
    ? e.response!.data['message'].toString()
    : 'Unable to connect. Check your network and try again.';

// ---------------------------------------------------------------------------
// Home
// ---------------------------------------------------------------------------

class Home extends ConsumerStatefulWidget {
  const Home({super.key});
  @override
  ConsumerState<Home> createState() => _Home();
}

class _Home extends ConsumerState<Home> {
  var i = 0;
  @override
  Widget build(BuildContext c) {
    final p = [
      const Dashboard(),
      const Records(
        title: 'Health measurements',
        path: 'health/measurements',
        name: 'measurementType',
      ),
      const Records(
        title: 'Nutrition diary',
        path: 'nutrition/meals',
        name: 'foodName',
      ),
      const Unavailable(
        'Food analysis',
        'The backend AI/image endpoint is not available.',
      ),
      const Profile(),
    ];
    return Scaffold(
      body: SafeArea(child: p[i]),
      bottomNavigationBar: NavigationBar(
        selectedIndex: i,
        onDestinationSelected: (v) => setState(() => i = v),
        destinations: const [
          NavigationDestination(icon: Icon(Icons.home_outlined), label: 'Home'),
          NavigationDestination(
            icon: Icon(Icons.monitor_heart_outlined),
            label: 'Health',
          ),
          NavigationDestination(
            icon: Icon(Icons.restaurant_outlined),
            label: 'Nutrition',
          ),
          NavigationDestination(
            icon: Icon(Icons.camera_alt_outlined),
            label: 'Scan',
          ),
          NavigationDestination(
            icon: Icon(Icons.person_outline),
            label: 'Profile',
          ),
        ],
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// DashboardModel
// ---------------------------------------------------------------------------

class DashboardModel {
  final String greeting;
  final String userName;
  final Map<String, dynamic> profile;
  final Map<String, dynamic> nutritionToday;
  final Map<String, dynamic> healthSnapshot;
  final List<dynamic> recentMeals;
  final List<dynamic> recentMeasurements;
  final List<dynamic> upcomingAppointments;
  final List<dynamic> activeMedications;

  const DashboardModel({
    required this.greeting,
    required this.userName,
    required this.profile,
    required this.nutritionToday,
    required this.healthSnapshot,
    required this.recentMeals,
    required this.recentMeasurements,
    required this.upcomingAppointments,
    required this.activeMedications,
  });

  factory DashboardModel.fromJson(Map<String, dynamic> json) {
    return DashboardModel(
      greeting: json['greeting'] as String? ?? 'Welcome back!',
      userName: json['userName'] as String? ?? '',
      profile: json['profile'] as Map<String, dynamic>? ?? {},
      nutritionToday: json['nutritionToday'] as Map<String, dynamic>? ?? {},
      healthSnapshot: json['healthSnapshot'] as Map<String, dynamic>? ?? {},
      recentMeals: json['recentMeals'] as List<dynamic>? ?? [],
      recentMeasurements: json['recentMeasurements'] as List<dynamic>? ?? [],
      upcomingAppointments:
          json['upcomingAppointments'] as List<dynamic>? ?? [],
      activeMedications: json['activeMedications'] as List<dynamic>? ?? [],
    );
  }
}

// ---------------------------------------------------------------------------
// dashboardProvider
// ---------------------------------------------------------------------------

final dashboardProvider =
    AsyncNotifierProvider<DashboardNotifier, DashboardModel?>(
      DashboardNotifier.new,
    );

class DashboardNotifier extends AsyncNotifier<DashboardModel?> {
  @override
  Future<DashboardModel?> build() => _fetch();

  Future<DashboardModel?> _fetch() async {
    final token = ref.watch(session).asData?.value;
    if (token == null) return null;
    try {
      final data = await ref.read(api).get('dashboard/overview');
      return DashboardModel.fromJson(data);
    } catch (_) {
      return null;
    }
  }

  Future<void> refresh() async {
    state = const AsyncLoading();
    state = await AsyncValue.guard(_fetch);
  }
}

// ---------------------------------------------------------------------------
// Dashboard (router widget)
// ---------------------------------------------------------------------------

class Dashboard extends ConsumerWidget {
  const Dashboard({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final loggedIn = ref.watch(session).asData?.value != null;
    if (!loggedIn) {
      return const GuestDashboard();
    }
    return const AuthenticatedDashboard();
  }
}

// ---------------------------------------------------------------------------
// GuestDashboard
// ---------------------------------------------------------------------------

class GuestDashboard extends ConsumerStatefulWidget {
  const GuestDashboard({super.key});

  @override
  ConsumerState<GuestDashboard> createState() => _GuestDashboardState();
}

class _GuestDashboardState extends ConsumerState<GuestDashboard> {
  Map<String, dynamic>? _guestInfo;
  bool _loading = true;

  @override
  void initState() {
    super.initState();
    _loadGuestInfo();
  }

  Future<void> _loadGuestInfo() async {
    try {
      final data = await ref.read(api).get('dashboard/guest-info');
      if (mounted) setState(() => _guestInfo = data);
    } catch (_) {
      // ignore — show defaults
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    final appName = _guestInfo?['appName'] as String? ?? 'LifeKhmer';
    final tagline =
        _guestInfo?['tagline'] as String? ??
        'Your Personal Health & Nutrition Companion';
    final features =
        _guestInfo?['features'] as List<dynamic>? ??
        [
          'Health Tracking',
          'Nutrition Diary',
          'AI Food Scanner',
          'Medication Reminders',
          'Appointments',
          'Symptom Tracking',
        ];

    final featureIcons = [
      Icons.monitor_heart,
      Icons.restaurant,
      Icons.camera_alt,
      Icons.medication,
      Icons.calendar_month,
      Icons.healing,
    ];

    return Scaffold(
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // Welcome hero card
            Container(
              decoration: BoxDecoration(
                gradient: LinearGradient(
                  begin: Alignment.topLeft,
                  end: Alignment.bottomRight,
                  colors: [cs.primary, cs.primaryContainer],
                ),
              ),
              padding: const EdgeInsets.fromLTRB(24, 48, 24, 32),
              child: Column(
                children: [
                  Icon(Icons.health_and_safety, size: 72, color: cs.onPrimary),
                  const SizedBox(height: 12),
                  Text(
                    appName,
                    style: Theme.of(context).textTheme.displaySmall?.copyWith(
                      color: cs.onPrimary,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    tagline,
                    textAlign: TextAlign.center,
                    style: Theme.of(context).textTheme.bodyLarge
                        ?.copyWith(color: cs.onPrimary.withValues(alpha: 0.9)),
                  ),
                  const SizedBox(height: 16),
                  Container(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 16,
                      vertical: 8,
                    ),
                    decoration: BoxDecoration(
                      color: cs.onPrimary.withValues(alpha: 0.15),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Text(
                      'No personal data available — sign in to track your health',
                      textAlign: TextAlign.center,
                      style: Theme.of(context).textTheme.bodySmall
                          ?.copyWith(color: cs.onPrimary),
                    ),
                  ),
                ],
              ),
            ),

            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  // Features card
                  if (_loading)
                    const Center(
                      child: Padding(
                        padding: EdgeInsets.all(24),
                        child: CircularProgressIndicator(),
                      ),
                    )
                  else
                    Card(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(12),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(16),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              'What you can do with LifeKhmer',
                              style: Theme.of(context).textTheme.titleLarge,
                            ),
                            const SizedBox(height: 12),
                            ...List.generate(features.length, (i) {
                              final icon = i < featureIcons.length
                                  ? featureIcons[i]
                                  : Icons.check_circle_outline;
                              return Padding(
                                padding: const EdgeInsets.symmetric(
                                  vertical: 4,
                                ),
                                child: Row(
                                  children: [
                                    Icon(icon, color: cs.primary, size: 20),
                                    const SizedBox(width: 12),
                                    Expanded(
                                      child: Text(
                                        '${features[i]}',
                                        style: Theme.of(context)
                                            .textTheme
                                            .bodyMedium,
                                      ),
                                    ),
                                  ],
                                ),
                              );
                            }),
                          ],
                        ),
                      ),
                    ),

                  const SizedBox(height: 12),

                  // Discover Cambodian Foods card
                  Card(
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                    color: cs.secondaryContainer,
                    child: InkWell(
                      borderRadius: BorderRadius.circular(12),
                      onTap: () => Navigator.of(context).push(
                        MaterialPageRoute<void>(
                          builder: (_) => const FoodSearchScreen(),
                        ),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(16),
                        child: Row(
                          children: [
                            Icon(
                              Icons.restaurant_menu,
                              color: cs.onSecondaryContainer,
                              size: 32,
                            ),
                            const SizedBox(width: 16),
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    'Discover Cambodian Foods',
                                    style: Theme.of(context)
                                        .textTheme
                                        .titleMedium
                                        ?.copyWith(
                                          color: cs.onSecondaryContainer,
                                        ),
                                  ),
                                  Text(
                                    'Browse our nutrition database — no sign-in required',
                                    style: Theme.of(context).textTheme.bodySmall
                                        ?.copyWith(
                                          color: cs.onSecondaryContainer
                                              .withValues(alpha: 0.8),
                                        ),
                                  ),
                                ],
                              ),
                            ),
                            Icon(
                              Icons.chevron_right,
                              color: cs.onSecondaryContainer,
                            ),
                          ],
                        ),
                      ),
                    ),
                  ),

                  const SizedBox(height: 24),

                  // CTA buttons
                  FilledButton.icon(
                    icon: const Icon(Icons.login),
                    label: const Text('Sign in to LifeKhmer'),
                    onPressed: () => context.go('/login'),
                  ),
                  const SizedBox(height: 8),
                  OutlinedButton.icon(
                    icon: const Icon(Icons.person_add),
                    label: const Text('Create a free account'),
                    onPressed: () => context.go('/login'),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// AuthenticatedDashboard
// ---------------------------------------------------------------------------

class AuthenticatedDashboard extends ConsumerWidget {
  const AuthenticatedDashboard({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final dashAsync = ref.watch(dashboardProvider);

    return dashAsync.when(
      loading: () => const Center(child: CircularProgressIndicator()),
      error: (err, _) => _buildErrorState(context, ref),
      data: (dashboard) {
        if (dashboard == null) {
          return _buildEmptyState(context, ref);
        }
        return RefreshIndicator(
          onRefresh: () => ref.read(dashboardProvider.notifier).refresh(),
          child: SingleChildScrollView(
            physics: const AlwaysScrollableScrollPhysics(),
            padding: const EdgeInsets.symmetric(vertical: 12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                _HeaderCard(dashboard: dashboard),
                const SizedBox(height: 12),
                _NutritionCard(dashboard: dashboard),
                const SizedBox(height: 12),
                _HealthOverviewCard(dashboard: dashboard),
                const SizedBox(height: 12),
                _QuickActionsCard(),
                const SizedBox(height: 12),
                _RecentMealsCard(dashboard: dashboard),
                const SizedBox(height: 12),
                _AppointmentsCard(dashboard: dashboard),
                const SizedBox(height: 12),
                _MedicationsCard(dashboard: dashboard),
                const SizedBox(height: 24),
              ],
            ),
          ),
        );
      },
    );
  }

  Widget _buildErrorState(BuildContext context, WidgetRef ref) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Card(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Icon(
                  Icons.error_outline,
                  size: 48,
                  color: Theme.of(context).colorScheme.error,
                ),
                const SizedBox(height: 16),
                Text(
                  'Could not load dashboard',
                  style: Theme.of(context).textTheme.titleLarge,
                ),
                const SizedBox(height: 8),
                const Text('Check your connection and try again.'),
                const SizedBox(height: 16),
                FilledButton.icon(
                  icon: const Icon(Icons.refresh),
                  label: const Text('Retry'),
                  onPressed: () =>
                      ref.read(dashboardProvider.notifier).refresh(),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildEmptyState(BuildContext context, WidgetRef ref) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Card(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Icon(
                  Icons.dashboard_outlined,
                  size: 48,
                  color: Theme.of(context).colorScheme.primary,
                ),
                const SizedBox(height: 16),
                Text(
                  'Dashboard unavailable',
                  style: Theme.of(context).textTheme.titleLarge,
                ),
                const SizedBox(height: 8),
                const Text('No data available at this time.'),
                const SizedBox(height: 16),
                FilledButton.icon(
                  icon: const Icon(Icons.refresh),
                  label: const Text('Refresh'),
                  onPressed: () =>
                      ref.read(dashboardProvider.notifier).refresh(),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Header Card
// ---------------------------------------------------------------------------

class _HeaderCard extends StatelessWidget {
  const _HeaderCard({required this.dashboard});
  final DashboardModel dashboard;

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Card(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        clipBehavior: Clip.antiAlias,
        child: Container(
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors: [cs.primary, cs.primaryContainer],
            ),
          ),
          padding: const EdgeInsets.all(20),
          child: Row(
            children: [
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      dashboard.greeting.isNotEmpty
                          ? dashboard.greeting
                          : 'Welcome back!',
                      style: Theme.of(context).textTheme.headlineSmall
                          ?.copyWith(
                            color: cs.onPrimary,
                            fontWeight: FontWeight.bold,
                          ),
                    ),
                    if (dashboard.userName.isNotEmpty) ...[
                      const SizedBox(height: 4),
                      Text(
                        dashboard.userName,
                        style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                          color: cs.onPrimary.withValues(alpha: 0.9),
                        ),
                      ),
                    ],
                  ],
                ),
              ),
              Icon(
                Icons.health_and_safety,
                size: 40,
                color: cs.onPrimary.withValues(alpha: 0.7),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Nutrition Card
// ---------------------------------------------------------------------------

class _NutritionCard extends StatelessWidget {
  const _NutritionCard({required this.dashboard});
  final DashboardModel dashboard;

  @override
  Widget build(BuildContext context) {
    final nutrition = dashboard.nutritionToday;
    final calories = _toDouble(nutrition['calories']);
    final protein = _toDouble(nutrition['protein']);
    final carbs = _toDouble(nutrition['carbohydrates']);
    final fat = _toDouble(nutrition['fat']);
    final sugar = _toDouble(nutrition['sugar']);

    final hasData = calories > 0 || protein > 0 || carbs > 0 || fat > 0;
    final cs = Theme.of(context).colorScheme;

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Card(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Icon(Icons.restaurant, color: cs.primary),
                  const SizedBox(width: 8),
                  Text(
                    "Today's Nutrition",
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                ],
              ),
              const SizedBox(height: 12),
              if (!hasData) ...[
                const Text('No meals logged today'),
                const SizedBox(height: 8),
                OutlinedButton.icon(
                  icon: const Icon(Icons.add),
                  label: const Text('Log Meal'),
                  onPressed: () => Navigator.of(context).push(
                    MaterialPageRoute<void>(
                      builder: (_) => const FoodSearchScreen(),
                    ),
                  ),
                ),
              ] else ...[
                Center(
                  child: Text(
                    '${calories.round()} kcal',
                    style: Theme.of(context).textTheme.displaySmall?.copyWith(
                      color: cs.primary,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                const SizedBox(height: 4),
                const Center(child: Text('Total Calories')),
                const SizedBox(height: 12),
                Row(
                  children: [
                    _NutrientChip(label: 'Protein', value: protein, unit: 'g'),
                    const SizedBox(width: 8),
                    _NutrientChip(label: 'Carbs', value: carbs, unit: 'g'),
                    const SizedBox(width: 8),
                    _NutrientChip(label: 'Fat', value: fat, unit: 'g'),
                    const SizedBox(width: 8),
                    _NutrientChip(label: 'Sugar', value: sugar, unit: 'g'),
                  ],
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }
}

class _NutrientChip extends StatelessWidget {
  const _NutrientChip({
    required this.label,
    required this.value,
    required this.unit,
  });
  final String label, unit;
  final double value;

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Expanded(
      child: Container(
        padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 4),
        decoration: BoxDecoration(
          color: cs.surfaceContainerHighest,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Column(
          children: [
            Text(
              '${value.toStringAsFixed(1)}$unit',
              style: Theme.of(context).textTheme.bodyMedium
                  ?.copyWith(fontWeight: FontWeight.bold),
            ),
            Text(label, style: Theme.of(context).textTheme.labelSmall),
          ],
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Health Overview Card
// ---------------------------------------------------------------------------

class _HealthOverviewCard extends StatelessWidget {
  const _HealthOverviewCard({required this.dashboard});
  final DashboardModel dashboard;

  @override
  Widget build(BuildContext context) {
    final snap = dashboard.healthSnapshot;
    final weightKg = snap['latestWeightKg'];
    final heightCm = snap['latestHeightCm'];
    final systolic = snap['latestBloodPressureSystolic'];
    final diastolic = snap['latestBloodPressureDiastolic'];
    final heartRate = snap['latestHeartRate'];
    final measDate = snap['latestMeasurementDate'] as String?;

    final hasData =
        weightKg != null ||
        heightCm != null ||
        heartRate != null ||
        systolic != null;
    final cs = Theme.of(context).colorScheme;

    String? dateStr;
    if (measDate != null) {
      try {
        final dt = DateTime.parse(measDate);
        dateStr = DateFormat('MMM d').format(dt);
      } catch (_) {}
    }

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Card(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Icon(Icons.monitor_heart, color: cs.primary),
                  const SizedBox(width: 8),
                  Text(
                    'Health Overview',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                ],
              ),
              const SizedBox(height: 12),
              if (!hasData) ...[
                const Text('No measurements recorded yet'),
                const SizedBox(height: 8),
                OutlinedButton.icon(
                  icon: const Icon(Icons.add),
                  label: const Text('Add Measurement'),
                  onPressed: () => Navigator.of(context).push(
                    MaterialPageRoute<void>(
                      builder: (_) => const AddMeasurementScreen(),
                    ),
                  ),
                ),
              ] else ...[
                Row(
                  children: [
                    _StatTile(
                      label: 'Weight',
                      value: weightKg != null
                          ? '${_toDouble(weightKg).toStringAsFixed(1)} kg'
                          : '—',
                      icon: Icons.scale,
                    ),
                    _StatTile(
                      label: 'Height',
                      value: heightCm != null
                          ? '${_toDouble(heightCm).toStringAsFixed(0)} cm'
                          : '—',
                      icon: Icons.height,
                    ),
                    _StatTile(
                      label: 'BP',
                      value: systolic != null && diastolic != null
                          ? '${_toDouble(systolic).round()}/'
                                '${_toDouble(diastolic).round()}'
                          : '—',
                      icon: Icons.favorite,
                    ),
                    _StatTile(
                      label: 'Heart Rate',
                      value: heartRate != null
                          ? '${_toDouble(heartRate).round()} bpm'
                          : '—',
                      icon: Icons.monitor_heart,
                    ),
                  ],
                ),
                if (dateStr != null) ...[
                  const SizedBox(height: 8),
                  Text(
                    'Last updated: $dateStr',
                    style: Theme.of(context).textTheme.bodySmall
                        ?.copyWith(color: cs.onSurfaceVariant),
                  ),
                ],
              ],
            ],
          ),
        ),
      ),
    );
  }
}

class _StatTile extends StatelessWidget {
  const _StatTile({
    required this.label,
    required this.value,
    required this.icon,
  });
  final String label, value;
  final IconData icon;

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Expanded(
      child: Container(
        margin: const EdgeInsets.symmetric(horizontal: 2),
        padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 4),
        decoration: BoxDecoration(
          color: cs.surfaceContainerHighest,
          borderRadius: BorderRadius.circular(8),
        ),
        child: Column(
          children: [
            Icon(icon, size: 18, color: cs.primary),
            const SizedBox(height: 4),
            Text(
              value,
              style: Theme.of(context).textTheme.bodySmall
                  ?.copyWith(fontWeight: FontWeight.bold),
              textAlign: TextAlign.center,
            ),
            Text(
              label,
              style: Theme.of(context).textTheme.labelSmall,
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Quick Actions Card
// ---------------------------------------------------------------------------

class _QuickActionsCard extends StatelessWidget {
  const _QuickActionsCard();

  @override
  Widget build(BuildContext context) {
    final cs = Theme.of(context).colorScheme;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Card(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Quick Actions',
                style: Theme.of(context).textTheme.titleLarge,
              ),
              const SizedBox(height: 12),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  _QuickActionButton(
                    icon: Icons.monitor_heart,
                    label: 'Add\nMeasurement',
                    color: cs.primary,
                    onTap: () => Navigator.of(context).push(
                      MaterialPageRoute<void>(
                        builder: (_) => const AddMeasurementScreen(),
                      ),
                    ),
                  ),
                  _QuickActionButton(
                    icon: Icons.restaurant,
                    label: 'Log\nMeal',
                    color: Colors.orange,
                    onTap: () => Navigator.of(context).push(
                      MaterialPageRoute<void>(
                        builder: (_) => const FoodSearchScreen(),
                      ),
                    ),
                  ),
                  _QuickActionButton(
                    icon: Icons.camera_alt,
                    label: 'Scan\nFood',
                    color: Colors.purple,
                    onTap: () => ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Coming Soon')),
                    ),
                  ),
                  _QuickActionButton(
                    icon: Icons.medication,
                    label: 'Add\nMedication',
                    color: Colors.red,
                    onTap: () => ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Coming Soon')),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _QuickActionButton extends StatelessWidget {
  const _QuickActionButton({
    required this.icon,
    required this.label,
    required this.color,
    required this.onTap,
  });
  final IconData icon;
  final String label;
  final Color color;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Column(
        children: [
          Container(
            width: 56,
            height: 56,
            decoration: BoxDecoration(
              color: color.withValues(alpha: 0.1),
              borderRadius: BorderRadius.circular(16),
            ),
            child: Icon(icon, color: color, size: 28),
          ),
          const SizedBox(height: 6),
          Text(
            label,
            textAlign: TextAlign.center,
            style: Theme.of(context).textTheme.labelSmall,
          ),
        ],
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Recent Meals Card
// ---------------------------------------------------------------------------

class _RecentMealsCard extends StatelessWidget {
  const _RecentMealsCard({required this.dashboard});
  final DashboardModel dashboard;

  @override
  Widget build(BuildContext context) {
    final meals = dashboard.recentMeals.take(5).toList();
    final cs = Theme.of(context).colorScheme;

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Card(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Icon(Icons.restaurant_menu, color: cs.primary),
                  const SizedBox(width: 8),
                  Text(
                    'Recent Meals',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                ],
              ),
              const SizedBox(height: 8),
              if (meals.isEmpty)
                const Padding(
                  padding: EdgeInsets.symmetric(vertical: 8),
                  child: Text('No recent meals'),
                )
              else
                ...meals.map((m) {
                  final meal = m as Map<String, dynamic>;
                  final name = meal['foodName'] as String? ?? '';
                  final type = meal['mealType'] as String? ?? '';
                  final cal = _toDouble(meal['calories']);
                  final at = meal['consumedAt'] as String?;
                  String timeStr = '';
                  if (at != null) {
                    try {
                      timeStr = DateFormat('h:mm a').format(DateTime.parse(at));
                    } catch (_) {
                      timeStr = at;
                    }
                  }
                  return ListTile(
                    contentPadding: EdgeInsets.zero,
                    leading: Icon(
                      Icons.restaurant,
                      color: cs.primary,
                      size: 20,
                    ),
                    title: Text(
                      name,
                      style: Theme.of(context).textTheme.bodyMedium,
                    ),
                    subtitle: Text('$type • $timeStr'),
                    trailing: Text(
                      '${cal.round()} kcal',
                      style: Theme.of(context).textTheme.bodySmall
                          ?.copyWith(fontWeight: FontWeight.bold),
                    ),
                  );
                }),
              Align(
                alignment: Alignment.centerRight,
                child: TextButton(
                  onPressed: () {},
                  child: const Text('View All'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Upcoming Appointments Card
// ---------------------------------------------------------------------------

class _AppointmentsCard extends StatelessWidget {
  const _AppointmentsCard({required this.dashboard});
  final DashboardModel dashboard;

  @override
  Widget build(BuildContext context) {
    final appts = dashboard.upcomingAppointments.take(3).toList();
    final cs = Theme.of(context).colorScheme;

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Card(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Icon(Icons.calendar_month, color: cs.primary),
                  const SizedBox(width: 8),
                  Text(
                    'Upcoming Appointments',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                ],
              ),
              const SizedBox(height: 8),
              if (appts.isEmpty)
                const Padding(
                  padding: EdgeInsets.symmetric(vertical: 8),
                  child: Text('No upcoming appointments'),
                )
              else
                ...appts.map((a) {
                  final appt = a as Map<String, dynamic>;
                  final doctor = appt['doctorName'] as String? ?? '';
                  final facility = appt['healthcareFacility'] as String? ?? '';
                  final purpose = appt['purpose'] as String? ?? '';
                  final timeStr = appt['appointmentTime'] as String?;
                  String formattedTime = '';
                  if (timeStr != null) {
                    try {
                      formattedTime = DateFormat('MMM d, h:mm a')
                          .format(DateTime.parse(timeStr));
                    } catch (_) {
                      formattedTime = timeStr;
                    }
                  }
                  return ListTile(
                    contentPadding: EdgeInsets.zero,
                    leading: Icon(
                      Icons.local_hospital,
                      color: cs.primary,
                      size: 20,
                    ),
                    title: Text(
                      doctor,
                      style: Theme.of(context).textTheme.bodyMedium,
                    ),
                    subtitle: Text(
                      '$facility\n$formattedTime${purpose.isNotEmpty ? ' • $purpose' : ''}',
                    ),
                    isThreeLine: true,
                  );
                }),
            ],
          ),
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Active Medications Card
// ---------------------------------------------------------------------------

class _MedicationsCard extends StatelessWidget {
  const _MedicationsCard({required this.dashboard});
  final DashboardModel dashboard;

  @override
  Widget build(BuildContext context) {
    final meds = dashboard.activeMedications.take(5).toList();
    final cs = Theme.of(context).colorScheme;

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Card(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  Icon(Icons.medication, color: cs.primary),
                  const SizedBox(width: 8),
                  Text(
                    'Active Medications',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                ],
              ),
              const SizedBox(height: 8),
              if (meds.isEmpty)
                const Padding(
                  padding: EdgeInsets.symmetric(vertical: 8),
                  child: Text('No active medications'),
                )
              else
                ...meds.map((m) {
                  final med = m as Map<String, dynamic>;
                  final name = med['name'] as String? ?? '';
                  final dosage = med['dosage'] as String? ?? '';
                  final frequency = med['frequency'] as String? ?? '';
                  return ListTile(
                    contentPadding: EdgeInsets.zero,
                    leading: Icon(
                      Icons.medication,
                      color: cs.primary,
                      size: 20,
                    ),
                    title: Text(
                      name,
                      style: Theme.of(context).textTheme.bodyMedium,
                    ),
                    subtitle: Text('$dosage • $frequency'),
                  );
                }),
            ],
          ),
        ),
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Records
// ---------------------------------------------------------------------------

class Records extends ConsumerWidget {
  const Records({
    required this.title,
    required this.path,
    required this.name,
    super.key,
  });
  final String title, path, name;
  @override
  Widget build(BuildContext c, WidgetRef r) => Scaffold(
    appBar: AppBar(
      title: Text(title),
      actions: path == 'nutrition/meals'
          ? [
              IconButton(
                icon: const Icon(Icons.search),
                onPressed: () => Navigator.of(c).push(
                  MaterialPageRoute<void>(
                    builder: (_) => const FoodSearchScreen(),
                  ),
                ),
              ),
            ]
          : path == 'health/measurements'
          ? [
              IconButton(
                icon: const Icon(Icons.add),
                onPressed: () async {
                  final created = await Navigator.of(c).push<bool>(
                    MaterialPageRoute<bool>(
                      builder: (_) => const AddMeasurementScreen(),
                    ),
                  );
                  if (created == true && c.mounted) {
                    Navigator.of(c).pushReplacement(
                      MaterialPageRoute<void>(builder: (_) => const Home()),
                    );
                  }
                },
              ),
            ]
          : null,
    ),
    body: FutureBuilder<Map<String, dynamic>>(
      future: r.read(api).get(path, q: {'page': 0, 'size': 30}),
      builder: (c, s) {
        if (!s.hasData) {
          return Center(
            child: s.hasError
                ? Text(errorText(s.error))
                : const CircularProgressIndicator(),
          );
        }
        final l = (s.data!['content'] as List);
        return l.isEmpty
            ? const Center(child: Text('No records yet.'))
            : ListView.builder(
                itemCount: l.length,
                itemBuilder: (_, x) {
                  final m = l[x] as Map;
                  return ListTile(
                    title: Text('${m[name] ?? ''}'),
                    subtitle: Text(
                      '${m['recordedAt'] ?? m['consumedAt'] ?? ''}',
                    ),
                  );
                },
              );
      },
    ),
  );
}

// ---------------------------------------------------------------------------
// Profile
// ---------------------------------------------------------------------------

class Profile extends ConsumerWidget {
  const Profile({super.key});
  @override
  Widget build(BuildContext c, WidgetRef r) => Scaffold(
    appBar: AppBar(title: const Text('Profile')),
    body: FutureBuilder<Map<String, dynamic>>(
      future: r.read(api).get('accounts/me'),
      builder: (c, s) => !s.hasData
          ? Center(
              child: s.hasError
                  ? Text(errorText(s.error))
                  : const CircularProgressIndicator(),
            )
          : Padding(
              padding: const EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    '${s.data!['fullName']}',
                    style: Theme.of(c).textTheme.headlineSmall,
                  ),
                  Text('Language: ${s.data!['preferredLanguage']}'),
                  const Spacer(),
                  OutlinedButton(
                    onPressed: () => r.read(session.notifier).logout(),
                    child: const Text('Sign out'),
                  ),
                ],
              ),
            ),
    ),
  );
}

// ---------------------------------------------------------------------------
// Unavailable
// ---------------------------------------------------------------------------

class Unavailable extends StatelessWidget {
  const Unavailable(this.title, this.detail, {super.key});
  final String title, detail;
  @override
  Widget build(BuildContext c) => Card(
    child: Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(title, style: Theme.of(c).textTheme.titleLarge),
          Text(detail),
        ],
      ),
    ),
  );
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

double _toDouble(dynamic v) {
  if (v == null) return 0.0;
  if (v is double) return v;
  if (v is int) return v.toDouble();
  if (v is String) return double.tryParse(v) ?? 0.0;
  return 0.0;
}
