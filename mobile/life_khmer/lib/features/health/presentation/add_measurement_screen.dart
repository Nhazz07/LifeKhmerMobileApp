import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

import '../../../main.dart';

class AddMeasurementScreen extends ConsumerStatefulWidget {
  const AddMeasurementScreen({super.key});

  @override
  ConsumerState<AddMeasurementScreen> createState() =>
      _AddMeasurementScreenState();
}

class _AddMeasurementScreenState extends ConsumerState<AddMeasurementScreen> {
  final _formKey = GlobalKey<FormState>();
  final _valueController = TextEditingController();
  final _secondaryValueController = TextEditingController();
  final _notesController = TextEditingController();
  String _type = 'WEIGHT';
  bool _submitting = false;
  String? _error;

  @override
  void dispose() {
    _valueController.dispose();
    _secondaryValueController.dispose();
    _notesController.dispose();
    super.dispose();
  }

  String get _unit => switch (_type) {
    'WEIGHT' => 'kg',
    'HEIGHT' => 'cm',
    'TEMPERATURE' => '°C',
    'BLOOD_PRESSURE' => 'mmHg',
    'HEART_RATE' => 'bpm',
    _ => 'unit',
  };

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() {
      _submitting = true;
      _error = null;
    });
    try {
      await ref.read(api).post('health/measurements', {
        'measurementType': _type,
        'value': double.parse(_valueController.text),
        'secondaryValue': _type == 'BLOOD_PRESSURE'
            ? double.parse(_secondaryValueController.text)
            : null,
        'unit': _unit,
        'recordedAt': DateTime.now().toIso8601String(),
        'notes': _notesController.text.trim().isEmpty
            ? null
            : _notesController.text.trim(),
      });
      if (mounted) Navigator.of(context).pop(true);
    } catch (error) {
      setState(() => _error = errorText(error));
    } finally {
      if (mounted) setState(() => _submitting = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Add measurement')),
      body: SafeArea(
        child: Form(
          key: _formKey,
          child: ListView(
            padding: const EdgeInsets.all(20),
            children: [
              DropdownButtonFormField<String>(
                initialValue: _type,
                decoration: const InputDecoration(
                  labelText: 'Measurement type',
                ),
                items:
                    const [
                          'WEIGHT',
                          'HEIGHT',
                          'TEMPERATURE',
                          'BLOOD_PRESSURE',
                          'HEART_RATE',
                        ]
                        .map(
                          (type) =>
                              DropdownMenuItem(value: type, child: Text(type)),
                        )
                        .toList(),
                onChanged: _submitting
                    ? null
                    : (value) => setState(() => _type = value!),
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _valueController,
                decoration: InputDecoration(labelText: 'Value ($_unit)'),
                keyboardType: const TextInputType.numberWithOptions(
                  decimal: true,
                ),
                validator: (value) => double.tryParse(value ?? '') == null
                    ? 'Enter a valid value.'
                    : null,
              ),
              if (_type == 'BLOOD_PRESSURE') ...[
                const SizedBox(height: 16),
                TextFormField(
                  controller: _secondaryValueController,
                  decoration: const InputDecoration(
                    labelText: 'Diastolic (mmHg)',
                  ),
                  keyboardType: const TextInputType.numberWithOptions(
                    decimal: true,
                  ),
                  validator: (value) => double.tryParse(value ?? '') == null
                      ? 'Enter a valid value.'
                      : null,
                ),
              ],
              const SizedBox(height: 16),
              TextFormField(
                controller: _notesController,
                maxLines: 3,
                decoration: const InputDecoration(
                  labelText: 'Notes (optional)',
                ),
              ),
              if (_error != null)
                Padding(
                  padding: const EdgeInsets.only(top: 12),
                  child: Text(
                    _error!,
                    style: TextStyle(
                      color: Theme.of(context).colorScheme.error,
                    ),
                  ),
                ),
              const SizedBox(height: 24),
              FilledButton(
                onPressed: _submitting ? null : _submit,
                child: Text(_submitting ? 'Saving…' : 'Save measurement'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
