import 'package:flutter/material.dart';

import 'CurrencyScreen.dart';



void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'ChangeMoneyGoodPrince',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const CurrencyScreen(),
    );
  }
}
