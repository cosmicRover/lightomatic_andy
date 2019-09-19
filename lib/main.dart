import 'package:flutter/material.dart';
import 'package:lightomatic_andy/screens/main_screen/MainScreen.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      theme: ThemeData(primaryColor: Colors.yellow, accentColor: Colors.black),
      home: MainScreen(),
    );
  }
}
