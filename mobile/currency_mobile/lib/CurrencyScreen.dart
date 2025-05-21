
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'models/currency.dart';
import 'CurrencyDetailScreen.dart';

class CurrencyScreen extends StatefulWidget {
  const CurrencyScreen({super.key});

  @override
  State<CurrencyScreen> createState() => _CurrencyScreenState();
}

class _CurrencyScreenState extends State<CurrencyScreen> {
  late Future<List<Currency>> _currenciesFuture;

  @override
  void initState() {
    super.initState();
    _currenciesFuture = fetchCurrencies();
  }

  Future<List<Currency>> fetchCurrencies() async {
    final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/v1/currency')); // Pamiętaj o adresie IP emulatora XD

    if (response.statusCode == 200) {
      List<dynamic> jsonList = jsonDecode(utf8.decode(response.bodyBytes));
      return jsonList.map((json) => Currency.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load currencies');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          "ChangeMoneyGoodPrince",
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        centerTitle: true,
        actions: [
          GestureDetector(
            onTap: () {
              setState(() {
                _currenciesFuture = fetchCurrencies();
              });
            },
            child: const Icon(Icons.refresh),
          ),
          const SizedBox(width: 16),
        ],
      ),
      body: FutureBuilder<List<Currency>>(
        future: _currenciesFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Błąd: ${snapshot.error}'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(child: Text('Brak dostępnych walut.'));
          } else {
            return ListView.builder(
              itemCount: snapshot.data!.length,
              itemBuilder: (context, index) {
                final currency = snapshot.data![index];
                return Card(
                  margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
                  child: ListTile(
                    title: Text(currency.currencyName),
                    subtitle: Text(currency.currencyCode),
                    onTap: () {
                      // Tutaj nawigujemy do nowego ekranu!
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => CurrencyDetailScreen(
                            currencyCode: currency.currencyCode, // Przekazujemy kod waluty
                          ),
                        ),
                      );
                    },
                  ),
                );
              },
            );
          }
        },
      ),
    );
  }
}