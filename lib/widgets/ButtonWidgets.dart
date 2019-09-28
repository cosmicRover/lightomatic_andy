import 'package:flutter/material.dart';

class ButtonWidget{
  Widget flatButton(bool value, Function onChangeFunc()){
    return Switch(
      value: false,
      activeColor: Colors.green,
      onChanged: onChangeFunc()
      //onChanged: onChangeFunc,
    );
  }

}