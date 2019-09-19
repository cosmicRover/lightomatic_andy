import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:lightomatic_andy/platform_functions/PlatformFunctions.dart';
import 'package:lightomatic_andy/widgets/TextWidgets.dart';

class MainScreen extends StatefulWidget {
  @override
  _MainScreenState createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  //define a method channel
  static const platform = const MethodChannel("dev.findjoy/lightomatic_andy");
  String platformDetails = "awaiting";
  String fenceStatus = "awaiting";

  @override
  void initState() {
    setState(() {
      PlatformFunctions().getPlatform(platform).then((value) {
        platformDetails = value;
      });

      PlatformFunctions().executeAndroidTask(platform).then((value) {
        fenceStatus = value;
      });
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Lightomatic")),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          MainScreenWidgets().mainScreenTextBox("$platformDetails"),
          MainScreenWidgets().mainScreenTextBox("$fenceStatus")
        ],
      ),
    );
  }
}
