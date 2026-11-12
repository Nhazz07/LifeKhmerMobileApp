import 'package:flutter/material.dart';

/// Used only for modules without a backend contract. It never simulates data.
class UnavailableScreen extends StatelessWidget {
  const UnavailableScreen({
    required this.title,
    required this.reason,
    super.key,
  });

  final String title;
  final String reason;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(title)),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Icon(Icons.cloud_off_outlined, size: 48),
              const SizedBox(height: 16),
              Text(title, style: Theme.of(context).textTheme.headlineSmall),
              const SizedBox(height: 8),
              Text(reason, textAlign: TextAlign.center),
            ],
          ),
        ),
      ),
    );
  }
}
