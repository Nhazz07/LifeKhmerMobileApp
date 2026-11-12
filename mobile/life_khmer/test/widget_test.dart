import 'package:flutter_test/flutter_test.dart';
import 'package:life_khmer/main.dart';

void main() {
  test(
    'errors are safe',
    () => expect(errorText(Exception('secret')), contains('Unable to connect')),
  );
}
