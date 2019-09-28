import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_background_location/flutter_background_location.dart';
import 'package:lightomatic_andy/platform_functions/PlatformFunctions.dart';
import 'package:lightomatic_andy/widgets/ButtonWidgets.dart';
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
  String latLon = "awaiting";

  @override
  void initState() {
      PlatformFunctions().getPlatform(platform).then((value) {
        platformDetails = value;
      });
    super.initState();
  }

  void loc() async{
    await FlutterBackgroundLocation.startLocationService();
    await FlutterBackgroundLocation.getLocationUpdates((location){
      print("LAT -->> ${location.latitude}");
      print("LON -->> ${location.longitude}");
      setState(() {
        latLon = "lat: ${location.latitude.toString()} \nlon ${location.longitude.toString()}";
      });
    });
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Lightomatic")),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          MainScreenWidgets().mainScreenTextBox("$platformDetails"),
          MainScreenWidgets().mainScreenTextBox("$fenceStatus"),
          MainScreenWidgets().mainScreenTextBox("$latLon"),
          ButtonWidget().flatButton()//idea is to check if location is enabled/disabled on start
          //then configure the switch based on the output
          //then there will a be a function to enable/disable location monitoring on request from switch
        ],
      ),
    );
  }
}
