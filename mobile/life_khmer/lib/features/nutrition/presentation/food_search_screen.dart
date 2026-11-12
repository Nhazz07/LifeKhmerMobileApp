import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../main.dart';
import '../domain/food.dart';

class FoodSearchScreen extends ConsumerStatefulWidget {
  const FoodSearchScreen({super.key});

  @override
  ConsumerState<FoodSearchScreen> createState() => _FoodSearchScreenState();
}

class _FoodSearchScreenState extends ConsumerState<FoodSearchScreen> {
  Future<List<Food>>? _foods;

  Future<List<Food>> _search(String query) async {
    final page = await ref
        .read(api)
        .get('nutrition/foods', q: {'query': query, 'page': 0, 'size': 30});
    return (page['content'] as List)
        .map((item) => Food.fromJson(Map<String, dynamic>.from(item as Map)))
        .toList();
  }

  @override
  void initState() {
    super.initState();
    _foods = _search('');
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Food search')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: TextField(
              decoration: const InputDecoration(
                labelText: 'Search foods',
                prefixIcon: Icon(Icons.search),
              ),
              onChanged: (value) => setState(() => _foods = _search(value)),
            ),
          ),
          Expanded(
            child: FutureBuilder<List<Food>>(
              future: _foods,
              builder: (context, snapshot) {
                if (snapshot.hasError) {
                  return const Center(child: Text('Unable to load foods.'));
                }
                if (!snapshot.hasData) {
                  return const Center(child: CircularProgressIndicator());
                }
                if (snapshot.data!.isEmpty) {
                  return const Center(child: Text('No foods found.'));
                }
                return ListView.builder(
                  itemCount: snapshot.data!.length,
                  itemBuilder: (_, index) {
                    final food = snapshot.data![index];
                    return ListTile(
                      title: Text(food.name),
                      subtitle: Text('${food.caloriesPer100g} kcal per 100 g'),
                      onTap: () => Navigator.of(context).push(
                        MaterialPageRoute<void>(
                          builder: (_) => FoodDetailsScreen(food: food),
                        ),
                      ),
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

class FoodDetailsScreen extends StatelessWidget {
  const FoodDetailsScreen({required this.food, super.key});
  final Food food;

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(title: Text(food.name)),
    body: ListView(
      padding: const EdgeInsets.all(20),
      children: [
        Text(
          '${food.caloriesPer100g} kcal',
          style: Theme.of(context).textTheme.displaySmall,
        ),
        const Text('per 100 g'),
        const SizedBox(height: 24),
        _Nutrient('Protein', food.proteinPer100g),
        _Nutrient('Carbohydrates', food.carbohydratesPer100g),
        _Nutrient('Fat', food.fatPer100g),
        _Nutrient('Sugar', food.sugarPer100g),
        _Nutrient('Fiber', food.fiberPer100g),
      ],
    ),
  );
}

class _Nutrient extends StatelessWidget {
  const _Nutrient(this.label, this.value);
  final String label;
  final double value;
  @override
  Widget build(BuildContext context) => ListTile(
    title: Text(label),
    trailing: Text('${value.toStringAsFixed(1)} g'),
  );
}
