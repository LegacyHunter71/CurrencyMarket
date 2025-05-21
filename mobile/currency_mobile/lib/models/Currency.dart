class Currency {
  final String currencyCode;
  final String currencyName;

  Currency({required this.currencyCode, required this.currencyName});

  factory Currency.fromJson(Map<String, dynamic> json) {
    return Currency(
      currencyCode: json['currencyCode'],
      currencyName: json['currencyName'],
    );
  }
}