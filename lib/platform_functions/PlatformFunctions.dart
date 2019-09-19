import 'dart:async';
import 'package:flutter/services.dart'; //for platform specific codes

class PlatformFunctions{

  //get platform info
  Future<String> getPlatform(MethodChannel platform)async{
    try {
      dynamic result = await platform.invokeMethod('getPlatform');
      return result.toString();
    }on PlatformException catch(e){
      return e.toString();
    }
  }

  //for android
  Future<dynamic> executeAndroidTask(MethodChannel platform)async{
    try {
      dynamic result = await platform.invokeMethod('exeAndroidFence');
      return result.toString();
    }on PlatformException catch(e){
      return e.toString();
    }
  }
}