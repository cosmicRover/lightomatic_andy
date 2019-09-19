import 'package:flutter/material.dart';

class MainScreenWidgets{
  Widget mainScreenTextBox(String text){
    return Align(
      alignment: Alignment.center,
      child: Text(text, style: TextStyle(fontSize: 30.0),),
    );
  }
}