
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'models/CurrencyDetail.dart';
import 'models/TransactionRequest.dart';
import 'models/TransactionResponse.dart';

class CurrencyDetailScreen extends StatefulWidget {
  final String currencyCode;

  const CurrencyDetailScreen({super.key, required this.currencyCode});

  @override
  State<CurrencyDetailScreen> createState() => _CurrencyDetailScreenState();
}

class _CurrencyDetailScreenState extends State<CurrencyDetailScreen> {
  late Future<CurrencyDetail> _currencyDetailFuture;
  final TextEditingController _amountController = TextEditingController();
  TransactionResponse? _transactionResponse; // Zmienna do przechowywania odpowiedzi z transakcji

  @override
  void initState() {
    super.initState();
    _currencyDetailFuture = fetchCurrencyDetails(widget.currencyCode);
  }

  @override
  void dispose() {
    _amountController.dispose();
    super.dispose();
  }

  Future<CurrencyDetail> fetchCurrencyDetails(String code) async {
    final response = await http.get(Uri.parse('http://10.0.2.2:8080/api/v1/currency/$code'));

    if (response.statusCode == 200) {
      return CurrencyDetail.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
    } else {
      throw Exception('Failed to load details for ${widget.currencyCode}: ${response.statusCode}');
    }
  }

  Future<void> _postTransaction(TransactionRequest request) async {
    setState(() {
      _transactionResponse = null; // Czyść poprzednią odpowiedź przed nowym żądaniem
    });

    try {
      final response = await http.post(
        Uri.parse('http://10.0.2.2:8080/api/v1/currency'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: jsonEncode(request.toJson()),
      );

      if (response.statusCode == 200) {
        final Map<String, dynamic> responseBody = jsonDecode(utf8.decode(response.bodyBytes));
        setState(() {
          _transactionResponse = TransactionResponse.fromJson(responseBody);
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Transakcja ${request.actionType.name} udana! Amount: ${_transactionResponse!.amount.toStringAsFixed(2)}')),
        );
      } else {
        final errorBody = utf8.decode(response.bodyBytes);
        throw Exception('Failed to perform transaction: ${response.statusCode} - $errorBody');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Błąd transakcji: $e')),
      );
      setState(() {
        _transactionResponse = null;
      });
    }
  }

  Future<void> _completeTransaction(String uuid) async {
    try {
      final response = await http.patch(
        Uri.parse('http://10.0.2.2:8080/api/v1/currency/$uuid/complete'),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        },

      );

      if (response.statusCode == 200 || response.statusCode == 204) { // 200 OK lub 204 No Content
        _showCompletionDialog(); // Pokaż okienko sukcesu
      } else {
        final errorBody = utf8.decode(response.bodyBytes);
        throw Exception('Failed to complete transaction: ${response.statusCode} - $errorBody');
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Błąd finalizacji transakcji: $e')),
      );
    }
  }

  void _showCompletionDialog() {
    showDialog(
      context: context,
      barrierDismissible: false, // Użytkownik musi kliknąć OK
      builder: (BuildContext dialogContext) { // Użyj dialogContext
        return AlertDialog(
          title: const Text('Transakcja Zrealizowana!'),
          content: const Text('Twoja transakcja została pomyślnie zrealizowana.'),
          actions: <Widget>[
            TextButton(
              child: const Text('OK'),
              onPressed: () {
                Navigator.of(dialogContext).pop(); // Zamknij dialog
                Navigator.of(context).pop(); // Wróć do poprzedniego ekranu (CurrencyScreen)
              },
            ),
          ],
        );
      },
    );
  }


  void _onBuyPressed(CurrencyDetail details) {
    final amountText = _amountController.text;
    final quantity = double.tryParse(amountText);

    if (quantity != null && quantity > 0) {
      final request = TransactionRequest(
        currencyCode: details.currencyCode,
        currencyName: details.currencyName,
        quantity: quantity,
        currencyRate: details.ask,
        actionType: ActionType.BUY,
      );
      _postTransaction(request);
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Proszę wprowadzić poprawną, dodatnią ilość do kupienia.')),
      );
    }
  }

  void _onSellPressed(CurrencyDetail details) {
    final amountText = _amountController.text;
    final quantity = double.tryParse(amountText);

    if (quantity != null && quantity > 0) {
      final request = TransactionRequest(
        currencyCode: details.currencyCode,
        currencyName: details.currencyName,
        quantity: quantity,
        currencyRate: details.bid,
        actionType: ActionType.SELL,
      );
      _postTransaction(request);
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Proszę wprowadzić poprawną, dodatnią ilość do sprzedania.')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('${widget.currencyCode} Details'),
        centerTitle: true,
      ),
      body: FutureBuilder<CurrencyDetail>(
        future: _currencyDetailFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Błąd: ${snapshot.error}'));
          } else if (!snapshot.hasData) {
            return const Center(child: Text('Brak danych o walucie.'));
          } else {
            final currencyDetails = snapshot.data!;
            return Padding(
              padding: const EdgeInsets.all(16.0),
              child: Card(
                elevation: 4,
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        currencyDetails.currencyName,
                        style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 8),
                      Text(
                        'Kod waluty: ${currencyDetails.currencyCode}',
                        style: const TextStyle(fontSize: 18, color: Colors.grey),
                      ),
                      const Divider(height: 24, thickness: 1),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text(
                            'Cena kupna (Ask):',
                            style: TextStyle(fontSize: 18),
                          ),
                          Text(
                            '${currencyDetails.ask}',
                            style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.green),
                          ),
                        ],
                      ),
                      const SizedBox(height: 8),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text(
                            'Cena sprzedaży (Bid):',
                            style: TextStyle(fontSize: 18),
                          ),
                          Text(
                            '${currencyDetails.bid}',
                            style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold, color: Colors.red),
                          ),
                        ],
                      ),
                      const SizedBox(height: 24),
                      TextField(
                        controller: _amountController,
                        keyboardType: const TextInputType.numberWithOptions(decimal: true, signed: false),
                        decoration: const InputDecoration(
                          border: OutlineInputBorder(),
                          labelText: 'Ilość',
                          hintText: 'Wprowadź ilość waluty',
                        ),
                      ),
                      const SizedBox(height: 16),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          Expanded(
                            child: ElevatedButton.icon(
                              onPressed: () => _onBuyPressed(currencyDetails),
                              icon: const Icon(Icons.shopping_cart),
                              label: const Text('Kup'),
                              style: ElevatedButton.styleFrom(
                                  padding: const EdgeInsets.symmetric(vertical: 12)
                              ),
                            ),
                          ),
                          const SizedBox(width: 16),
                          Expanded(
                            child: ElevatedButton.icon(
                              onPressed: () => _onSellPressed(currencyDetails),
                              icon: const Icon(Icons.attach_money),
                              label: const Text('Sprzedaj'),
                              style: ElevatedButton.styleFrom(
                                  padding: const EdgeInsets.symmetric(vertical: 12)
                              ),
                            ),
                          ),
                        ],
                      ),
                      // Wynik transakcji i przycisk finalizacji
                      if (_transactionResponse != null)
                        Padding(
                          padding: const EdgeInsets.only(top: 24.0),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                'Wynik wstępnej transakcji:', // Zmieniony tekst
                                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                              ),
                              const SizedBox(height: 8),
                              Text(
                                'Typ: ${_transactionResponse!.actionType}',
                                style: const TextStyle(fontSize: 16),
                              ),
                              Text(
                                'Ilość: ${_transactionResponse!.quantity}',
                                style: const TextStyle(fontSize: 16),
                              ),
                              Text(
                                'Kurs: ${_transactionResponse!.currencyRate.toStringAsFixed(4)}',
                                style: const TextStyle(fontSize: 16),
                              ),
                              Text(
                                'Obliczona kwota: ${_transactionResponse!.amount.toStringAsFixed(2)}',
                                style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold, color: Colors.blueAccent),
                              ),
                              const SizedBox(height: 16),
                              Center( // Wyśrodkuj przycisk finalizacji
                                child: ElevatedButton.icon(
                                  onPressed: () {
                                    // Wyślij żądanie PATCH z UUID transakcji
                                    _completeTransaction(_transactionResponse!.id);
                                  },
                                  icon: const Icon(Icons.check_circle_outline),
                                  label: const Text('Finalizuj Transakcję'),
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: Colors.green, // Uczyń przycisk bardziej widocznym
                                    foregroundColor: Colors.white,
                                    padding: const EdgeInsets.symmetric(horizontal: 30, vertical: 15),
                                    textStyle: const TextStyle(fontSize: 18),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                    ],
                  ),
                ),
              ),
            );
          }
        },
      ),
    );
  }
}