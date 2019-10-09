import 'package:flutter/material.dart';

class ButtonWidget{
  Widget flatButton(Function onChangeFunc){
    return FlatButton(child: Text("Press to activate fence"),
    onPressed: onChangeFunc(),
    );
  }
}