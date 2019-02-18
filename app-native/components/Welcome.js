import React, { Component } from "react";
import service from "../service/GshowService";
import { StyleSheet, Button, View } from 'react-native';

export default class extends Component {

  startGame() {
    service.startGame();
    service.registerPlayer("Mike");
  }

  render() {
    return <View>
        <Button title="Start Game!" onPress={() => this.startGame()} />
      </View>
  }
}
