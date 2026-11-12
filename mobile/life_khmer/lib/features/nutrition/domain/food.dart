class Food {
  const Food({
    required this.id,
    required this.name,
    required this.caloriesPer100g,
    required this.proteinPer100g,
    required this.carbohydratesPer100g,
    required this.fatPer100g,
    required this.sugarPer100g,
    required this.fiberPer100g,
  });

  factory Food.fromJson(Map<String, dynamic> json) => Food(
    id: json['id'] as String,
    name: json['name'] as String,
    caloriesPer100g: (json['caloriesPer100g'] as num).toDouble(),
    proteinPer100g: (json['proteinPer100g'] as num).toDouble(),
    carbohydratesPer100g: (json['carbohydratesPer100g'] as num).toDouble(),
    fatPer100g: (json['fatPer100g'] as num).toDouble(),
    sugarPer100g: (json['sugarPer100g'] as num).toDouble(),
    fiberPer100g: (json['fiberPer100g'] as num).toDouble(),
  );

  final String id;
  final String name;
  final double caloriesPer100g;
  final double proteinPer100g;
  final double carbohydratesPer100g;
  final double fatPer100g;
  final double sugarPer100g;
  final double fiberPer100g;
}
