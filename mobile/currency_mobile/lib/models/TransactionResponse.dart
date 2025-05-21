// lib/models/transaction_response.dart

class TransactionResponse {
  final String id;
  final String currencyName;
  final String currencyCode;
  final double currencyRate;
  final String actionType; // Możemy pozostawić jako String, lub sparsować na enum jeśli chcemy
  final double quantity;
  final double amount;

  TransactionResponse({
    required this.id,
    required this.currencyName,
    required this.currencyCode,
    required this.currencyRate,
    required this.actionType,
    required this.quantity,
    required this.amount,
  });

  factory TransactionResponse.fromJson(Map<String, dynamic> json) {
    return TransactionResponse(
      id: json['id'],
      currencyName: json['currencyName'],
      currencyCode: json['currencyCode'],
      currencyRate: json['currencyRate'].toDouble(),
      actionType: json['actionType'],
      quantity: json['quantity'].toDouble(),
      amount: json['amount'].toDouble(),
    );
  }
}