class CurrencyDetail {
  final String currencyName;
  final String currencyCode;
  final double bid;
  final double ask;

  CurrencyDetail({
    required this.currencyName,
    required this.currencyCode,
    required this.bid,
    required this.ask,
  });

  factory CurrencyDetail.fromJson(Map<String, dynamic> json) {
    return CurrencyDetail(
      currencyName: json['currencyName'],
      currencyCode: json['currencyCode'],
      bid: json['bid'].toDouble(), // Upewnij się, że parsujesz jako double
      ask: json['ask'].toDouble(), // Upewnij się, że parsujesz jako double
    );
  }
}