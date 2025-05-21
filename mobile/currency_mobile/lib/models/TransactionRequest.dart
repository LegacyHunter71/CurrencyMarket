// lib/models/transaction_request.dart

enum ActionType {
  SELL,
  BUY,
}

class TransactionRequest {
  final String currencyCode;
  final String currencyName;
  final double quantity;
  final double currencyRate; // To będzie bid dla BUY, ask dla SELL
  final ActionType actionType;

  TransactionRequest({
    required this.currencyCode,
    required this.currencyName,
    required this.quantity,
    required this.currencyRate,
    required this.actionType,
  });

  Map<String, dynamic> toJson() {
    return {
      'currencyCode': currencyCode,
      'currencyName': currencyName,
      'quantity': quantity,
      'currencyRate': currencyRate,
      'actionType': actionType.name, // Konwertuj enum na string (np. "SELL", "BUY")
    };
  }
}