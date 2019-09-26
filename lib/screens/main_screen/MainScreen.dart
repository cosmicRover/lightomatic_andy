import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_background_location/flutter_background_location.dart';
import 'package:geolocator/geolocator.dart';
import 'package:lightomatic_andy/geolocator_functions/GeolocatorFunctions.dart';
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
  String latLon = "awaiting";
  // StreamSubscription<Position> positionStream;
  // var geolocator = Geolocator();
  // var options = LocationOptions(accuracy: LocationAccuracy.high, distanceFilter: 10);

  //gotta make the location request first

  @override
  void initState() {

    setState(() {
      PlatformFunctions().getPlatform(platform).then((value) {
        platformDetails = value;
      });

      PlatformFunctions().executeAndroidTask(platform).then((value) {
        fenceStatus = value;
      });

      // PlatformFunctions().getAndroidLocation(platform).then((value){
      //   latLon = value;
      // });

      // positionStream = geolocator.getPositionStream(options).listen((Position position){
      //   print(position == null ? 'Unknown' : position.latitude.toString() + ', ' + position.longitude.toString());
      //   //latLon = position.latitude.toString() + " " + position.longitude.toString();
      // });

    });

    loc();

    super.initState();
  }

  void loc() async{
    await FlutterBackgroundLocation.startLocationService();
    await FlutterBackgroundLocation.getLocationUpdates((location){
      print("LAT -->> ${location.latitude}");
      print("LON -->> ${location.longitude}");
    });
  }

  @override
  void dispose() {
    // positionStream.cancel();
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
          MainScreenWidgets().mainScreenTextBox("$latLon")
        ],
      ),
    );
  }
}
